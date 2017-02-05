#! /bin/bash

java -cp `/opt/mapr/bin/mapr classpath`:/usr/share/mapr-apps/webserver-service.jar com.mapr.demos.WebServer $1 $2

