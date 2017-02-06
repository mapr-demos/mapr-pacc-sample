#! /bin/bash


# Create /apps/logs directory to save logs on MFS using fuse mount
mkdir -p $2

java -cp $MAPR_CLASSPATH:/usr/share/mapr-apps/webserver-service.jar com.mapr.demos.WebServer $1 $2

