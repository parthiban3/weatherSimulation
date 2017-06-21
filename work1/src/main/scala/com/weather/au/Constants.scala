package com.weather.au

object Constants {
  val appName="WeatherSimulation"
  val executionMode="local"
  val inputFilePath="/src/main/resources/inputData/All.txt"
  val outputFilePath="/src/main/resources/output/out1"
  val weatherDataTable="weatherData"
  val dateFormatZone="yyyy-MM-dd HH:mm:ss z"
  val ZoneGMTPlusTen="GMT+10"
  val ZoneGMTPlusNine="GMT+9:30"
  val dayStartInx:Int=8
  val dayEndInx:Int=10
  val monStartInx:Int=5
  val monEndInx:Int=7
  val timeStartInx:Int=0
  val timeEndInx:Int=19
  val locSyd:String="Sydney"
  val locTow:String="Townsville"
  val locAde:String="Adelaide"
  val locBris:String="Brisbane"
  val locMel:String="Melbourne"
  val posSyd:String="-33.86,151.21,39"
  val posTow:String="-19.25,146.81,16"
  val posAde:String="-34.92,138.62,48"
  val posBris:String="-27.46,153.02,28"
  val posMel:String="-37.83,144.98,7"
  var currDateTen:String = InputProcess.getCurrentDate(Constants.ZoneGMTPlusTen)
  var currDateNine:String = InputProcess.getCurrentDate(Constants.ZoneGMTPlusNine)
  //query will get the average of climate data for the particular day based on historical data
  val calcQuery:String="select X.station, case when X.station='"+locSyd+"' then '"+posSyd+"' when X.station='"+locTow+"' then '"+posTow+"' when X.station='"+locAde+"' then '"+posAde+"' when X.station='"+locBris+"' then '"+posBris+"' when X.station='"+locMel+"' then '"+posMel+"' else '0' END,case when X.station='"+locTow+"' then '"+currDateNine+"' else '"+currDateTen+"' END, substring((((X.c1+X.c2)/2)),0,4), case when (((X.c1+X.c2)/2)) >= 30 then 'Sunny' when (((X.c1+X.c2)/2)) <= 0 then 'Snowy' when (((X.c1+X.c2)/2)) >=20 then 'Cloudy' else 'Rainy' end,(X.c3+950),case when (X.c4)<50 then (X.c4+50) else (X.c4+0) end from (select station,((AVG(maxt)-32)/1.8) c1,((AVG(mint)-32)/1.8) c2,max(mxspd) c3,max(prcp) c4 from "+weatherDataTable+" where months='@' and days='@@' group by station) X"
  val repString1:String="@"
  val repString2:String="@@"
  val workspacePath:String="user.dir"
}