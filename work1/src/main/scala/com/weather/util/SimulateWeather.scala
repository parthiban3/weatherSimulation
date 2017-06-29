package com.weather.util

import com.weather.dataSource.CityDetails
import com.weather.dataSource.CityGeoDetails
import com.weather.util.dateGen
import com.weather.simulate.TemperatureGen

object SimulateWeather {
  
  //Starting application method
  def main(args: Array[String]): Unit = {
    printFormattedData();    
  }
  
  //Prints data in console in the designated format
  def printFormattedData(){
    var cityLocalDates = dateGen.getCityLocalDates()
    var cityTemperature = TemperatureGen.getCityTemperature()
    for (i <- 0 until CityDetails.cityName.length) {
        print(CityDetails.cityName(i)+"|")
        for(j <- 0 until CityGeoDetails.geoPosition(0).length){
           print(CityGeoDetails.geoPosition(i)(j))
            if(j!=2){print(",")}else{print("|")}
        }
        print(cityLocalDates(i)+"|")
        print(cityTemperature(i)+"|")
        println("")
    }    
  }
  
}