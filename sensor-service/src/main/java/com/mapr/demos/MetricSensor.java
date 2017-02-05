package com.mapr.demos;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.TimerTask;

class MetricsSensor extends TimerTask {

  private KafkaProducer<String, String> kafkaProducer;

  public MetricsSensor(KafkaProducer<String, String> producer) {
    kafkaProducer = producer;
  }

  public void run() {

    StringBuilder sb = new StringBuilder("{");
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
      method.setAccessible(true);
      if (method.getName().startsWith("get")
              && Modifier.isPublic(method.getModifiers())) {
        Object value;
        try {
          value = method.invoke(operatingSystemMXBean);
        } catch (Exception e) {
          value = e;
        } // try
        String name = method.getName();
        // remove the get in the name
        name = name.substring(3, name.length());
        String entry = String.format(" \"%s\" : \"%s\"  ,", name, value);
        sb.append(entry);
      }
    }

    // add timestemp
    sb.append(" \"timestamp\" : ").append(System.currentTimeMillis());
    sb.append("}");
    kafkaProducer.send( new ProducerRecord<String, String>(SensorApplication.topicName, sb.toString() ));

  }
}