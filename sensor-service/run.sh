#! /bin/bash

java -cp `/opt/mapr/bin/mapr classpath`:/usr/share/mapr-apps/sensor-service.jar com.mapr.demos.SensorApplication $1

