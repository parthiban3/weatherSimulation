package com.weather.util
import java.util.TimeZone
import java.text.SimpleDateFormat
import java.util.Date
import java.text.ParseException
import com.weather.au.Constants

object dateGen {

  //gets the current date of the given zone
   def getCurrentDate( timeZone:String ) : String = {
      var formatter = new SimpleDateFormat(Constants.dateFormatZone)
      formatter.setTimeZone(TimeZone.getTimeZone(timeZone))
      var todayDate= new Date()
      /*var startDate = "02/01/2017 11:30:00"; //--- To change different dates
      var s:Date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(startDate)
      println(s)
      todayDate=s*/
      var dateString = formatter.format(todayDate)
      var dateFormatted:String=dateString.substring(Constants.timeStartInx,Constants.timeEndInx)
      return dateFormatted
   }
   
   //Method will get the array of local time for the cities
   def getCityLocalDates():Array[String] ={
     var currDateTen:String = getCurrentDate(Constants.ZoneGMTPlusTen)
     var currDateNine:String =getCurrentDate(Constants.ZoneGMTPlusNine)
     var cityLocalDates = Array(currDateTen,currDateNine,currDateTen,currDateTen,currDateTen)
     return cityLocalDates
   }
}