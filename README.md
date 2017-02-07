# Sample Application for MapR Container Client


## Prepare

In the root folder of the project, run the following command to build the Java applications

```
$ mvn clean package

```


Create a new Stream and Topic in your MapR Cluster:

```
$ maprcli stream create -path /apps/sensors -produceperm p -consumeperm p -topicperm p

$ maprcli stream topic create -path /apps/sensors -topic computer
```

Create a directory to store application logs

```
$ mkdir -p /mapr/my.cluster.com/apps/logs
```


## Build and Run the Containers

Sensor Application

```
$ cd sensor-service

$ docker build -t mapr-sensor-producer .

$ docker run -it -e MAPR_CLDB_HOSTS=192.168.99.18 -e MAPR_CLUSTER=my.cluster.com -e MAPR_CONTAINER_USER=mapr --name producer -i -t mapr-sensor-producer

```

Web Application

```
$ cd webserver-service

$ docker build -t mapr-web-consumer .

$ docker run -it --privileged --cap-add SYS_ADMIN --cap-add SYS_RESOURCE --device /dev/fuse -p 8080:8080  -e MAPR_CLDB_HOSTS=192.168.99.18 -e MAPR_CLUSTER=my.cluster.com -e MAPR_CONTAINER_USER=mapr -e MAPR_MOUNT_PATH=/mapr --name web -i -t mapr-web-consumer


```

Access the Web application in the host using http://localhost:8080


