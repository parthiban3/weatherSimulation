package com.weather.au

//Input weather data structure - Bean class
case class WeatherInputDean (
    reg:String,
    station:String, 
    years:String,
    months:String,
    days:String,
    wdsp:String,
    mxspd:String,
    maxt:String,
    mint:String,
    prcp:String,
    frshtt:String
 )