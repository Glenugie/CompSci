#!/bin/bash

reset
cd Dropbox/CompSci/CS3517/mud/src
javac com/cs3517/mud/*.java
rmic com.cs3517.mud.MUDServiceImpl
gnome-terminal --tab-with-profile=Default -x rmiregistry 50010
gnome-terminal --tab-with-profile=Default -x java com.cs3517.mud.MUDServiceMainline 50010 50011
sleep 0.5
java com.cs3517.mud.MUDClient localhost 50010
