#! /bin/bash

LOG_DIR=$MAPR_MOUNT_PATH/$MAPR_CLUSTER/$2

# Create /apps/logs directory to save logs on MFS using fuse mount
mkdir -p $LOG_DIR

java -cp $MAPR_CLASSPATH:/usr/share/mapr-apps/webserver-service.jar com.mapr.demos.WebServer $1 $LOG_DIR

