package com.mapr.demos;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class SensorApplication {

  public static String topicName = "/apps/sensors:computer";



  public static void main(String[] args) throws IOException {

    // get the topic from command line
    if (args.length != 0) {
      topicName = args[0];
    }


    // set up the producer
    KafkaProducer<String, String> producer;
    Properties properties = new Properties();

    properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
    properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
    properties.setProperty("streams.buffer.max.time.ms","300");

    producer = new KafkaProducer<>(properties);

    System.out.println("**** SensorApplication Started ****");
    System.out.println(" Posting to "+ topicName);

    Timer timer = new Timer();
    timer.schedule(new MetricsSensor(producer), 0, 500);

  }

}



