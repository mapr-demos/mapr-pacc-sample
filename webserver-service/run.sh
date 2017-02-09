#! /bin/bash

# Create /apps/logs directory to save webserver logs on MFS using fuse mount
# If no mount path, save logs to local /tmp/logs

DIR=$MAPR_MOUNT_PATH/$MAPR_CLUSTER

if [ -d "$DIR" ]; then
    LOG_DIR=$DIR/apps/logs
else
    LOG_DIR=/tmp/logs
fi

# Create Log dir and set permission for Read/Write
sudo mkdir -p $LOG_DIR
sudo chmod -R 777 $LOG_DIR

java -cp $MAPR_CLASSPATH:/usr/share/mapr-apps/webserver-service.jar com.mapr.demos.WebServer $1 $LOG_DIR