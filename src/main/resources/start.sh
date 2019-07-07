#!/usr/bin/env bash

#调用stop.sh
./stop.sh

jar_name=

for jar_name in `ls .|egrep '*.jar|*.war'`
do
   if [ -n "$jar_name" ];then
      echo "find  $jar_name"
   fi
   break

done

if [ "$jar_name" = "" ];then
   echo "not find *.jar or *.war"
   exit 11
fi

echo "start $jar_name ..."


#Xms：初始值
#Xmx：最大值
#Xmn：最小值

nohup  ${JAVA_HOME}/bin/java -Xms100m -Xmx500m -jar ${jar_name}>/dev/null --logging.config=config/logback-spring.xml  2>&1 &

sleep 8
tailf log/info.log


