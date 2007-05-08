#! /bin/sh

cd "`dirname $0`"
rm -R doc
mkdir doc
javadoc -sourcepath src -package -version -author -d doc be.ac.kuleuven.cs.ips2.project2002.logicCircuits be.ac.kuleuven.cs.ips2.project2002.awtgui src/LogicCircuitsDemo.java

