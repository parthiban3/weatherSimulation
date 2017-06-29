# weatherSimulation

Workspace Import:

Project Name: work1(maven project)

In eclipse, Select File -> Import -> Projects from Git -> Clone URI

Clone URI: https://github.com/parthiban3/weatherSimulation/

Provide your user name and password for github

Select "master" and remote name "origin"

Select "Import existing Eclipse projects"

Select the "work1" project and finish

Program:

Open the file SimulateWeather.scala (Path: work1\src\main\scala\com\weather\util)
Run as scala application
Output(Weather details for the day) will be printed in the console

1.Australia Monsoon season collected:(Same for all 5 cities - (Adelaide,Townsville,Melbourne,Brisbane & Sydney))

Summer: December to February.

Autumn: March to May.

Winter: June to August.

Spring: September to November


2.Their min and max weather also collected for each month

3.Their topological details also collected(latitude,longitude,elevation,windspeed,rainfall,perception)

4. Temperature calculated using own formula based on mansoon,windspeed,perception,railfall and elevation

5. Climate condition(Rainy,Snow,Sunny) calculated based on temperature and perception

6. Air pressure calculated based on below formula

1 Atmosphere = 14.7 psi

psi to pa(psi*6894.76)

pa to hpa

7. Humidity calculated based on below formula

Es=6.11*10.0**(7.5*Tc/(237.7+Tc)) -  saturation vapor pressure

E=6.11*10.0**(7.5*Tdc/(237.7+Tdc)) - actual vapor pressure

Relative Humidity(RH) in percent =(E/Es)*100

8. Functional testing done and attached spreadsheet

Software:

Apache Spark 1.6

JAVA 1.8
Scala IDE

