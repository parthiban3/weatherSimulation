package com.weather.simulate
import java.util.TimeZone
import java.text.SimpleDateFormat
import java.util.Date
import java.text.ParseException
import com.weather.au.Constants
import com.weather.util.dateGen
import com.weather.dataSource.CityDetails
import com.weather.dataSource.TemperatureDetails
import com.weather.dataSource.TopographyDetails

object TemperatureGen{

  //Method will generate temperature for the cities
   def getCityTemperature():Array[String] ={
      var currDateTen:String = dateGen.getCurrentDate(Constants.ZoneGMTPlusTen)
      var currDateNine:String =dateGen. getCurrentDate(Constants.ZoneGMTPlusNine)
      var matrix = new Array[String](5)
      var currDay:String = currDateTen.substring(Constants.dayStartInx, Constants.dayEndInx)
  	  var currMon:String = currDateTen.substring(Constants.monStartInx, Constants.monEndInx)
  	  var currTime:String = currDateTen.substring(Constants.tStartInx, Constants.tEndInx)
  	  var currDay_Town:String = currDateNine.substring(Constants.dayStartInx, Constants.dayEndInx)
  	  var currMon_Town:String = currDateNine.substring(Constants.monStartInx, Constants.monEndInx)
  	  var currTime_Town:String = currDateNine.substring(Constants.tStartInx, Constants.tEndInx)
  	  matrix(0) = getCityTemperatureSydney(currDay,currMon,currTime)
  	  matrix(1) = getCityTemperatureTown(currDay,currMon,currTime)
  	  matrix(2) = getCityTemperatureAdelaide(currDay,currMon,currTime)
  	  matrix(3) = getCityTemperatureBrisbane(currDay,currMon,currTime)
  	  matrix(4) = getCityTemperatureMelbourne(currDay,currMon,currTime)
     return matrix
   }
   def getCityTemperatureSydney(currDay:String,currMon:String,currTime:String):String ={
      var currTemp:Double = 0.0
  	  var windDiffTemp:Double=0.0
  	  var windPromTemp:Double=0.0
  	  var PercepPromTemp:Double=0.0
  	  var rainPromTemp:Double=0.0
  	  var monSoonPromTemp:Double=0.0
  	  var calDiffTemp:Double=0.0
  	  var tempCondition:String=""
  	  var tempPressure:Double=0.0
  	  var tempDewpoint:Double=0.0
  	  var tempHum:Double=0.0
  	  
  	  //Get monsoon data - Max and Min temperature of the month
  	  val (minTemp, maxTemp) = TemperatureDetails.tempData(TemperatureDetails.Sydney)((currMon.toInt)-1)
  	  val (minWin, maxWin) = TopographyDetails.windSpeed(TopographyDetails.Sydney)((currMon.toInt)-1)
  	  val rainfall=TopographyDetails.rainfall(TopographyDetails.Sydney)((currMon.toInt)-1)
  	  val perception=TopographyDetails.perception(TopographyDetails.Sydney)((currMon.toInt)-1)

  	  //weather condition and temperature formula
  	  if(currTime.toInt == 12 ){
  	    calDiffTemp=maxTemp.toDouble
  	  }else if(currTime.toInt < 12){
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(12.0 - currTime.toDouble)) 
  	  }else{
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(currTime.toDouble-12.0))
  	  }

  	  windDiffTemp = (minWin.toDouble + maxWin.toDouble)/2
  	  
  	  if(windDiffTemp <= 10){
  	    windPromTemp = 1.0  
  	  }else if(windDiffTemp > 10 && windDiffTemp <= 20){
  	    windPromTemp = -0.5
      }else if(windDiffTemp > 20 && windDiffTemp <= 25){
        windPromTemp = -1.0
      }else{
        windPromTemp = 0
      }
  	  
  	  if(perception.toDouble <= 10){
  	    PercepPromTemp = 1.0
  	  }else if(perception.toDouble > 10 && perception.toDouble <= 20){
  	    PercepPromTemp = -0.5
  	  }else{
  	    PercepPromTemp = -1.0
  	  }
  	  
  	  if(rainfall.toDouble <=20){
  	    rainPromTemp = 1.0
  	  }else if(rainfall.toDouble >20 && rainfall.toDouble <=40){
  	    rainPromTemp = -0.5
  	  }else{
  	    rainPromTemp = -1.0
  	  }
  	  
  	  if(currMon.toInt >=12 || currMon.toInt < 3){
  	    monSoonPromTemp = 2.0
  	  }else if(currMon.toInt >=3 || currMon.toInt < 6){
  	    monSoonPromTemp = 0.5
  	  }else if(currMon.toInt >=6 || currMon.toInt < 8){
  	    monSoonPromTemp = -0.5
  	  }else{
  	    monSoonPromTemp = 0
  	  }
  	  
  	  currTemp = calDiffTemp+windPromTemp+PercepPromTemp+rainPromTemp+monSoonPromTemp
      
  	  if(currTemp<=0.0){
  	    tempCondition="Snow"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-3
  	  }else if(currTemp>30.0){
  	    tempCondition="Sunny"
  	    tempPressure=1.0
  	    tempDewpoint=currTemp-1
  	  }else if(monSoonPromTemp < 0.0 && PercepPromTemp < 0.0 && windPromTemp < 0.0){
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }else if(currTemp>20.0 && currTemp<30.0 && monSoonPromTemp> 0.0){
  	    tempCondition="Sunny"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-1
  	  }else{
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }
  	  
  	  //Air pressure formula
  	  tempPressure=tempPressure*14.7 // 1 Atmosphere = 14.7 psi
  	  tempPressure=tempPressure*6894.76 //psi to pa
  	  tempPressure=tempPressure/100 // pa to hpa
  	  tempPressure=tempPressure+currTemp-1000.0
  	   	  
  	  //Humidity formula
  	  var eS:Double=6.11*10.0*(7.5*currTemp/(237.7+currTemp)) //Es=6.11*10.0**(7.5*Tc/(237.7+Tc)) -  saturation vapor pressure
  	  var e:Double=6.11*10.0*(7.5*tempDewpoint/(237.7+tempDewpoint)) //E=6.11*10.0**(7.5*Tdc/(237.7+Tdc)) - actual vapor pressure
  	  tempHum=(e/eS)*100 //Relative Humidity(RH) in percent =(E/Es)*100
  	  
  	  //Formatting the output
  	  var tempPressureFormatted=f"$tempPressure%.1f"
  	  var tempFormatted=f"$currTemp%.1f"
  	  if (tempHum<10.0){tempHum=47}
  	  var tempHumFormatted=f"$tempHum%.0f"
  	  if (currTemp>0.0){tempFormatted="+"+tempFormatted}
  	  
     return tempCondition+"|"+tempFormatted+"|"+tempPressureFormatted+"|"+tempHumFormatted
   }
   
   def getCityTemperatureTown(currDay:String,currMon:String,currTime:String):String ={
      var currTemp:Double = 0.0
  	  var windDiffTemp:Double=0.0
  	  var windPromTemp:Double=0.0
  	  var PercepPromTemp:Double=0.0
  	  var rainPromTemp:Double=0.0
  	  var monSoonPromTemp:Double=0.0
  	  var calDiffTemp:Double=0.0
  	  var tempCondition:String=""
  	  var tempPressure:Double=0.0
  	  var tempDewpoint:Double=0.0
  	  var tempHum:Double=0.0
  	  
  	  //Get monsoon data - Max and Min temperature of the month
  	  val (minTemp, maxTemp) = TemperatureDetails.tempData(TemperatureDetails.Townsville)((currMon.toInt)-1)
  	  val (minWin, maxWin) = TopographyDetails.windSpeed(TopographyDetails.Townsville)((currMon.toInt)-1)
  	  val rainfall=TopographyDetails.rainfall(TopographyDetails.Townsville)((currMon.toInt)-1)
  	  val perception=TopographyDetails.perception(TopographyDetails.Townsville)((currMon.toInt)-1)

  	  //weather condition and temperature formula
  	  if(currTime.toInt == 12 ){
  	    calDiffTemp=maxTemp.toDouble
  	  }else if(currTime.toInt < 12){
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(12.0 - currTime.toDouble)) 
  	  }else{
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(currTime.toDouble-12.0))
  	  }

  	  windDiffTemp = (minWin.toDouble + maxWin.toDouble)/2
  	  
  	  if(windDiffTemp <= 10){
  	    windPromTemp = 1.0  
  	  }else if(windDiffTemp > 10 && windDiffTemp <= 20){
  	    windPromTemp = -0.5
      }else if(windDiffTemp > 20 && windDiffTemp <= 25){
        windPromTemp = -1.0
      }else{
        windPromTemp = 0
      }
  	  
  	  if(perception.toDouble <= 10){
  	    PercepPromTemp = 1.0
  	  }else if(perception.toDouble > 10 && perception.toDouble <= 20){
  	    PercepPromTemp = -0.5
  	  }else{
  	    PercepPromTemp = -1.0
  	  }
  	  
  	  if(rainfall.toDouble <=20){
  	    rainPromTemp = 1.0
  	  }else if(rainfall.toDouble >20 && rainfall.toDouble <=40){
  	    rainPromTemp = -0.5
  	  }else{
  	    rainPromTemp = -1.0
  	  }
  	  
  	  if(currMon.toInt >=12 || currMon.toInt < 3){
  	    monSoonPromTemp = 2.0
  	  }else if(currMon.toInt >=3 || currMon.toInt < 6){
  	    monSoonPromTemp = 0.5
  	  }else if(currMon.toInt >=6 || currMon.toInt < 8){
  	    monSoonPromTemp = -0.5
  	  }else{
  	    monSoonPromTemp = 0
  	  }
  	  
  	  currTemp = calDiffTemp+windPromTemp+PercepPromTemp+rainPromTemp+monSoonPromTemp
      
  	  if(currTemp<=0.0){
  	    tempCondition="Snow"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-3
  	  }else if(currTemp>30.0){
  	    tempCondition="Sunny"
  	    tempPressure=1.0
  	    tempDewpoint=currTemp-1
  	  }else if(monSoonPromTemp < 0.0 && PercepPromTemp < 0.0 && windPromTemp < 0.0){
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }else if(currTemp>20.0 && currTemp<30.0 && monSoonPromTemp> 0.0){
  	    tempCondition="Sunny"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-1
  	  }else{
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }
  	  
  	  //Air pressure formula
  	  tempPressure=tempPressure*14.7 // 1 Atmosphere = 14.7 psi
  	  tempPressure=tempPressure*6894.76 //psi to pa
  	  tempPressure=tempPressure/100 // pa to hpa
  	  tempPressure=tempPressure+currTemp-1000.0
  	   	  
  	  //Humidity formula
  	  var eS:Double=6.11*10.0*(7.5*currTemp/(237.7+currTemp)) //Es=6.11*10.0**(7.5*Tc/(237.7+Tc)) -  saturation vapor pressure
  	  var e:Double=6.11*10.0*(7.5*tempDewpoint/(237.7+tempDewpoint)) //E=6.11*10.0**(7.5*Tdc/(237.7+Tdc)) - actual vapor pressure
  	  tempHum=(e/eS)*100 //Relative Humidity(RH) in percent =(E/Es)*100
  	  
  	  //Formatting the output
  	  //var tempHumFormatted=f"$tempHum%.0f"
  	  var tempPressureFormatted=f"$tempPressure%.1f"
  	  var tempFormatted=f"$currTemp%.1f"
  	  if (tempHum<10.0){tempHum=47}
  	  var tempHumFormatted=f"$tempHum%.0f"
  	  if (currTemp>0.0){tempFormatted="+"+tempFormatted}
  	  
     return tempCondition+"|"+tempFormatted+"|"+tempPressureFormatted+"|"+tempHumFormatted
   }
   
   def getCityTemperatureAdelaide(currDay:String,currMon:String,currTime:String):String ={
      var currTemp:Double = 0.0
  	  var windDiffTemp:Double=0.0
  	  var windPromTemp:Double=0.0
  	  var PercepPromTemp:Double=0.0
  	  var rainPromTemp:Double=0.0
  	  var monSoonPromTemp:Double=0.0
  	  var calDiffTemp:Double=0.0
  	  var tempCondition:String=""
  	  var tempPressure:Double=0.0
  	  var tempDewpoint:Double=0.0
  	  var tempHum:Double=0.0
  	  
  	  //Get monsoon data - Max and Min temperature of the month
  	  val (minTemp, maxTemp) = TemperatureDetails.tempData(TemperatureDetails.Adelaide)((currMon.toInt)-1)
  	  val (minWin, maxWin) = TopographyDetails.windSpeed(TopographyDetails.Adelaide)((currMon.toInt)-1)
  	  val rainfall=TopographyDetails.rainfall(TopographyDetails.Adelaide)((currMon.toInt)-1)
  	  val perception=TopographyDetails.perception(TopographyDetails.Adelaide)((currMon.toInt)-1)

  	  //weather condition and temperature formula
  	  if(currTime.toInt == 12 ){
  	    calDiffTemp=maxTemp.toDouble
  	  }else if(currTime.toInt < 12){
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(12.0 - currTime.toDouble)) 
  	  }else{
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(currTime.toDouble-12.0))
  	  }

  	  windDiffTemp = (minWin.toDouble + maxWin.toDouble)/2
  	  
  	  if(windDiffTemp <= 10){
  	    windPromTemp = 1.0  
  	  }else if(windDiffTemp > 10 && windDiffTemp <= 20){
  	    windPromTemp = -0.5
      }else if(windDiffTemp > 20 && windDiffTemp <= 25){
        windPromTemp = -1.0
      }else{
        windPromTemp = 0
      }
  	  
  	  if(perception.toDouble <= 10){
  	    PercepPromTemp = 1.0
  	  }else if(perception.toDouble > 10 && perception.toDouble <= 20){
  	    PercepPromTemp = -0.5
  	  }else{
  	    PercepPromTemp = -1.0
  	  }
  	  
  	  if(rainfall.toDouble <=20){
  	    rainPromTemp = 1.0
  	  }else if(rainfall.toDouble >20 && rainfall.toDouble <=40){
  	    rainPromTemp = -0.5
  	  }else{
  	    rainPromTemp = -1.0
  	  }
  	  
  	  if(currMon.toInt >=12 || currMon.toInt < 3){
  	    monSoonPromTemp = 2.0
  	  }else if(currMon.toInt >=3 || currMon.toInt < 6){
  	    monSoonPromTemp = 0.5
  	  }else if(currMon.toInt >=6 || currMon.toInt < 8){
  	    monSoonPromTemp = -0.5
  	  }else{
  	    monSoonPromTemp = 0
  	  }
  	  
  	  currTemp = calDiffTemp+windPromTemp+PercepPromTemp+rainPromTemp+monSoonPromTemp
      
  	  if(currTemp<=0.0){
  	    tempCondition="Snow"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-3
  	  }else if(currTemp>30.0){
  	    tempCondition="Sunny"
  	    tempPressure=1.0
  	    tempDewpoint=currTemp-1
  	  }else if(monSoonPromTemp < 0.0 && PercepPromTemp < 0.0 && windPromTemp < 0.0){
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }else if(currTemp>20.0 && currTemp<30.0 && monSoonPromTemp> 0.0){
  	    tempCondition="Sunny"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-1
  	  }else{
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }
  	  
  	  //Air pressure formula
  	  tempPressure=tempPressure*14.7 // 1 Atmosphere = 14.7 psi
  	  tempPressure=tempPressure*6894.76 //psi to pa
  	  tempPressure=tempPressure/100 // pa to hpa
  	  tempPressure=tempPressure+currTemp-1000.0
  	   	  
  	  //Humidity formula
  	  var eS:Double=6.11*10.0*(7.5*currTemp/(237.7+currTemp)) //Es=6.11*10.0**(7.5*Tc/(237.7+Tc)) -  saturation vapor pressure
  	  var e:Double=6.11*10.0*(7.5*tempDewpoint/(237.7+tempDewpoint)) //E=6.11*10.0**(7.5*Tdc/(237.7+Tdc)) - actual vapor pressure
  	  tempHum=(e/eS)*100 //Relative Humidity(RH) in percent =(E/Es)*100
  	  
  	  //Formatting the output
  	  //var tempHumFormatted=f"$tempHum%.0f"
  	  var tempPressureFormatted=f"$tempPressure%.1f"
  	  var tempFormatted=f"$currTemp%.1f"
  	  if (tempHum<10.0){tempHum=47}
  	  var tempHumFormatted=f"$tempHum%.0f"
  	  if (currTemp>0.0){tempFormatted="+"+tempFormatted}
  	  
     return tempCondition+"|"+tempFormatted+"|"+tempPressureFormatted+"|"+tempHumFormatted
   }
   
   def getCityTemperatureBrisbane(currDay:String,currMon:String,currTime:String):String ={
      var currTemp:Double = 0.0
  	  var windDiffTemp:Double=0.0
  	  var windPromTemp:Double=0.0
  	  var PercepPromTemp:Double=0.0
  	  var rainPromTemp:Double=0.0
  	  var monSoonPromTemp:Double=0.0
  	  var calDiffTemp:Double=0.0
  	  var tempCondition:String=""
  	  var tempPressure:Double=0.0
  	  var tempDewpoint:Double=0.0
  	  var tempHum:Double=0.0
  	  
  	  //Get monsoon data - Max and Min temperature of the month
  	  val (minTemp, maxTemp) = TemperatureDetails.tempData(TemperatureDetails.Brisbane)((currMon.toInt)-1)
  	  val (minWin, maxWin) = TopographyDetails.windSpeed(TopographyDetails.Brisbane)((currMon.toInt)-1)
  	  val rainfall=TopographyDetails.rainfall(TopographyDetails.Brisbane)((currMon.toInt)-1)
  	  val perception=TopographyDetails.perception(TopographyDetails.Brisbane)((currMon.toInt)-1)

  	  //weather condition and temperature formula
  	  if(currTime.toInt == 12 ){
  	    calDiffTemp=maxTemp.toDouble
  	  }else if(currTime.toInt < 12){
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(12.0 - currTime.toDouble)) 
  	  }else{
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(currTime.toDouble-12.0))
  	  }

  	  windDiffTemp = (minWin.toDouble + maxWin.toDouble)/2
  	  
  	  if(windDiffTemp <= 10){
  	    windPromTemp = 1.0  
  	  }else if(windDiffTemp > 10 && windDiffTemp <= 20){
  	    windPromTemp = -0.5
      }else if(windDiffTemp > 20 && windDiffTemp <= 25){
        windPromTemp = -1.0
      }else{
        windPromTemp = 0
      }
  	  
  	  if(perception.toDouble <= 10){
  	    PercepPromTemp = 1.0
  	  }else if(perception.toDouble > 10 && perception.toDouble <= 20){
  	    PercepPromTemp = -0.5
  	  }else{
  	    PercepPromTemp = -1.0
  	  }
  	  
  	  if(rainfall.toDouble <=20){
  	    rainPromTemp = 1.0
  	  }else if(rainfall.toDouble >20 && rainfall.toDouble <=40){
  	    rainPromTemp = -0.5
  	  }else{
  	    rainPromTemp = -1.0
  	  }
  	  
  	  if(currMon.toInt >=12 || currMon.toInt < 3){
  	    monSoonPromTemp = 2.0
  	  }else if(currMon.toInt >=3 || currMon.toInt < 6){
  	    monSoonPromTemp = 0.5
  	  }else if(currMon.toInt >=6 || currMon.toInt < 8){
  	    monSoonPromTemp = -0.5
  	  }else{
  	    monSoonPromTemp = 0
  	  }
  	  
  	  currTemp = calDiffTemp+windPromTemp+PercepPromTemp+rainPromTemp+monSoonPromTemp
      
  	  if(currTemp<=0.0){
  	    tempCondition="Snow"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-3
  	  }else if(currTemp>30.0){
  	    tempCondition="Sunny"
  	    tempPressure=1.0
  	    tempDewpoint=currTemp-1
  	  }else if(monSoonPromTemp < 0.0 && PercepPromTemp < 0.0 && windPromTemp < 0.0){
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }else if(currTemp>20.0 && currTemp<30.0 && monSoonPromTemp> 0.0){
  	    tempCondition="Sunny"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-1
  	  }else{
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }
  	  
  	  //Air pressure formula
  	  tempPressure=tempPressure*14.7 // 1 Atmosphere = 14.7 psi
  	  tempPressure=tempPressure*6894.76 //psi to pa
  	  tempPressure=tempPressure/100 // pa to hpa
  	  tempPressure=tempPressure+currTemp-1000.0
  	   	  
  	  //Humidity formula
  	  var eS:Double=6.11*10.0*(7.5*currTemp/(237.7+currTemp)) //Es=6.11*10.0**(7.5*Tc/(237.7+Tc)) -  saturation vapor pressure
  	  var e:Double=6.11*10.0*(7.5*tempDewpoint/(237.7+tempDewpoint)) //E=6.11*10.0**(7.5*Tdc/(237.7+Tdc)) - actual vapor pressure
  	  tempHum=(e/eS)*100 //Relative Humidity(RH) in percent =(E/Es)*100
  	  
  	  //Formatting the output
  	  //var tempHumFormatted=f"$tempHum%.0f"
  	  var tempPressureFormatted=f"$tempPressure%.1f"
  	  var tempFormatted=f"$currTemp%.1f"
  	  if (tempHum<10.0){tempHum=47}
  	  var tempHumFormatted=f"$tempHum%.0f"
  	  if (currTemp>0.0){tempFormatted="+"+tempFormatted}
  	  
     return tempCondition+"|"+tempFormatted+"|"+tempPressureFormatted+"|"+tempHumFormatted
   }
   def getCityTemperatureMelbourne(currDay:String,currMon:String,currTime:String):String ={
      var currTemp:Double = 0.0
  	  var windDiffTemp:Double=0.0
  	  var windPromTemp:Double=0.0
  	  var PercepPromTemp:Double=0.0
  	  var rainPromTemp:Double=0.0
  	  var monSoonPromTemp:Double=0.0
  	  var calDiffTemp:Double=0.0
  	  var tempCondition:String=""
  	  var tempPressure:Double=0.0
  	  var tempDewpoint:Double=0.0
  	  var tempHum:Double=0.0
  	  
  	  //Get monsoon data - Max and Min temperature of the month
  	  val (minTemp, maxTemp) = TemperatureDetails.tempData(TemperatureDetails.Melbourne)((currMon.toInt)-1)
  	  val (minWin, maxWin) = TopographyDetails.windSpeed(TopographyDetails.Melbourne)((currMon.toInt)-1)
  	  val rainfall=TopographyDetails.rainfall(TopographyDetails.Melbourne)((currMon.toInt)-1)
  	  val perception=TopographyDetails.perception(TopographyDetails.Melbourne)((currMon.toInt)-1)

  	  //weather condition and temperature formula
  	  if(currTime.toInt == 12 ){
  	    calDiffTemp=maxTemp.toDouble
  	  }else if(currTime.toInt < 12){
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(12.0 - currTime.toDouble)) 
  	  }else{
  	    calDiffTemp=maxTemp.toDouble - (((maxTemp.toDouble - minTemp.toDouble)/12)*(currTime.toDouble-12.0))
  	  }

  	  windDiffTemp = (minWin.toDouble + maxWin.toDouble)/2
  	  
  	  if(windDiffTemp <= 10){
  	    windPromTemp = 1.0  
  	  }else if(windDiffTemp > 10 && windDiffTemp <= 20){
  	    windPromTemp = -0.5
      }else if(windDiffTemp > 20 && windDiffTemp <= 25){
        windPromTemp = -1.0
      }else{
        windPromTemp = 0
      }
  	  
  	  if(perception.toDouble <= 10){
  	    PercepPromTemp = 1.0
  	  }else if(perception.toDouble > 10 && perception.toDouble <= 20){
  	    PercepPromTemp = -0.5
  	  }else{
  	    PercepPromTemp = -1.0
  	  }
  	  
  	  if(rainfall.toDouble <=20){
  	    rainPromTemp = 1.0
  	  }else if(rainfall.toDouble >20 && rainfall.toDouble <=40){
  	    rainPromTemp = -0.5
  	  }else{
  	    rainPromTemp = -1.0
  	  }
  	  
  	  if(currMon.toInt >=12 || currMon.toInt < 3){
  	    monSoonPromTemp = 2.0
  	  }else if(currMon.toInt >=3 || currMon.toInt < 6){
  	    monSoonPromTemp = 0.5
  	  }else if(currMon.toInt >=6 || currMon.toInt < 8){
  	    monSoonPromTemp = -0.5
  	  }else{
  	    monSoonPromTemp = 0
  	  }
  	  
  	  currTemp = calDiffTemp+windPromTemp+PercepPromTemp+rainPromTemp+monSoonPromTemp
      
  	  if(currTemp<=0.0){
  	    tempCondition="Snow"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-3
  	  }else if(currTemp>30.0){
  	    tempCondition="Sunny"
  	    tempPressure=1.0
  	    tempDewpoint=currTemp-1
  	  }else if(monSoonPromTemp < 0.0 && PercepPromTemp < 0.0 && windPromTemp < 0.0){
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }else if(currTemp>20.0 && currTemp<30.0 && monSoonPromTemp> 0.0){
  	    tempCondition="Sunny"
  	    tempPressure=3.0
  	    tempDewpoint=currTemp-1
  	  }else{
  	    tempCondition="Rainy"
  	    tempPressure=2.0
  	    tempDewpoint=currTemp-2
  	  }
  	  
  	  //Air pressure formula
  	  tempPressure=tempPressure*14.7 // 1 Atmosphere = 14.7 psi
  	  tempPressure=tempPressure*6894.76 //psi to pa
  	  tempPressure=tempPressure/100 // pa to hpa
  	  tempPressure=tempPressure+currTemp-1000.0
  	   	  
  	  //Humidity formula
  	  var eS:Double=6.11*10.0*(7.5*currTemp/(237.7+currTemp)) //Es=6.11*10.0**(7.5*Tc/(237.7+Tc)) -  saturation vapor pressure
  	  var e:Double=6.11*10.0*(7.5*tempDewpoint/(237.7+tempDewpoint)) //E=6.11*10.0**(7.5*Tdc/(237.7+Tdc)) - actual vapor pressure
  	  tempHum=(e/eS)*100 //Relative Humidity(RH) in percent =(E/Es)*100
  	  
  	  //Formatting the output
  	  //var tempHumFormatted=f"$tempHum%.0f"
  	  var tempPressureFormatted=f"$tempPressure%.1f"
  	  var tempFormatted=f"$currTemp%.1f"
  	  if (tempHum<10.0){tempHum=47}
  	  var tempHumFormatted=f"$tempHum%.0f"
  	  if (currTemp>0.0){tempFormatted="+"+tempFormatted}
  	  
     return tempCondition+"|"+tempFormatted+"|"+tempPressureFormatted+"|"+tempHumFormatted
   }
}