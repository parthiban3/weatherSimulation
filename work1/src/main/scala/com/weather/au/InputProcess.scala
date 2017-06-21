package com.weather.au
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import java.util.TimeZone
import java.text.SimpleDateFormat
import java.util.Date
import org.apache.spark.sql._
import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException
import java.text.ParseException



//Input weather data processing
object InputProcess {
  
  def main(args: Array[String]): Unit = {
    
    //Application Configuration
    try {
      val conf = new SparkConf().setAppName(Constants.appName).setMaster(Constants.executionMode)
  		val sc = new SparkContext(conf)
      val dirPath:String=System.getProperty(Constants.workspacePath);
  	  val inputRDD = sc.textFile(dirPath.concat(Constants.inputFilePath))
      val sqlContext = new org.apache.spark.sql.SQLContext(sc)
      import sqlContext.implicits._
  
  	  //Convert the input RDD to Data frame
      val inputDF = inputRDD.map(_.split(",")).map(e=> WeatherInputDean(e(0).trim,e(1).trim,e(2).trim,e(3).trim,e(4).trim,e(5).trim,e(6).trim,e(7).trim,e(8).trim,e(9).trim,e(10).trim)).toDF()
  	  inputDF.registerTempTable(Constants.weatherDataTable)
  	  inputDF.cache()
  	  
  	  //Form the query and get the calculated data
  	  var currDate:String = getCurrentDate(Constants.ZoneGMTPlusTen)
  	  var currDay:String = currDate.substring(Constants.dayStartInx, Constants.dayEndInx)
  	  var currMon:String = currDate.substring(Constants.monStartInx, Constants.monEndInx)
  	  var qry:String=Constants.calcQuery.replaceAll(Constants.repString2, currDay)
  	  qry=qry.replaceAll(Constants.repString1, currMon)
  	  val calculatedRecords = sqlContext.sql(qry)
  	  
  	  //Format the output and print in the console
  	  val formattedRdd = calculatedRecords.map(X=> X(0)+"|"+X(1)+"|"+X(2)+"|"+X(2)+"|"+X(4)+"|"+X(3)+"|"+X(5)+"|"+X(6))
  	  formattedRdd.collect().foreach(println) 
      
    } catch {
         case ex: FileNotFoundException => {
            println("InputProcess.main: Missing file exception")
         }
         case ex: IOException => {
            println("InputProcess.main: IO Exception - File unable to read/write")
         }
      }
  }
  
  //gets the current date of the zone
   def getCurrentDate( timeZone:String ) : String = {
      var formatter = new SimpleDateFormat(Constants.dateFormatZone)
      formatter.setTimeZone(TimeZone.getTimeZone(timeZone))
      var todayDate= new Date()
      /*var startDate = "03/11/2017 12:30:00"; //--- To change different dates
      var s:Date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(startDate)
      println(s)
      todayDate=s*/
      var dateString = formatter.format(todayDate)
      var dateFormatted:String=dateString.substring(Constants.timeStartInx,Constants.timeEndInx)
      return dateFormatted
   }
}