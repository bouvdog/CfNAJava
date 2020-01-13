# CfNAJava

The goal is to make a player aid application for the SPI game Campaign for North Africa.

As of 1-12-2020 very little is implemented. 

## Acknowledgements
Algorithms and ideas about hexgrids were leveraged and taken from: https://www.redblobgames.com/grids/hexagons/

## Build Dependencies
Java 11.0.5
Maven 3.6.3
Docker for Windows 19.03.5

The docker command I have used to launch the container is:
docker container run --name cfna -d -p 8080:8080 <image id>

## Land Game Rules of Play

6.0 The Capability Point System
General Rule
CNA uses a Capability Point system to control all aspects of a given land unit's abilities. A unit's Capability Point Allowance (CPA) is a numerical representation of a unit's ability to understand any and all functions: movement (including retreats and advances), combat, loading, transporting, construction, etc. There is not a function in the game that will not cose a unit some of it CPA. CPA thus regulates each and every aspect of play. 
.....

6.1 How the CPA System Works
6.11 Each unit has a CPA. The CPA for each unit is given on the OA Sheets. Certain Gun units have a CPA of '0' these units are considered to have a CPA of 10 for all purposes other than movement. 


25.0 Fortifications
General Rule
All major cities in the game are considered fortifications. In addition, players may construct 
fortifications (see Case 24.4). Fortifications are availabe in levels, reflective of the depth and
strength of the fortifications. Most fortiifications and cities have two possible levels, but Cairo
and Alexandria have three possible levels. Fortifications give beneifts to units in that hex.
Cases
25.11 Each level of Foritifcation gives an increasing defensive benefit to units in that hex 
(see T.E.C., Case 8.37)
25.12 Each major city on the game-map is a Level 2 fortification. Cairo and Alexandria are Level 3 
fortifications. Villages are not fortifications.
25.13 Constructed fortifications are either Level 1 or Level 2. Fortifications can never be constructed
higher than Level 2, except in Cairo or Alexandria. 



