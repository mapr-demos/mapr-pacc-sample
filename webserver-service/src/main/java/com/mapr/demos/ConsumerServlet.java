package com.mapr.demos;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConsumerServlet extends HttpServlet implements Runnable {

  public List<String> kafkaMessages, kafkaMessagesBuffer = new ArrayList<String>();
  private Properties props;
  private KafkaConsumer<String, String> consumer;
  Thread Trans;

  /**
   *   setting up the properties and other consumer using the constructor of ConsumerServlet
   */
  public ConsumerServlet(String topicName) {
    props = new Properties();
    props.put("auto.offset.reset", "earliest");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("goup.id","mapr-demo-web-consumer");
    consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList(topicName));

  }

  /**
   *   setup the thread and run it using the init-method
   */
  @Override
  public void init (ServletConfig config) throws ServletException {
    super.init(config);
    Trans = new Thread(this);
    Trans.setPriority(Thread.MIN_PRIORITY);
    Trans.start();
  }

  /**
   *   implement the doPost-Method if we want to use it
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    //empty -- nothing to post here
  }

  /**
   *   doGet will get print out our returned message from kafka
   */
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    if (! kafkaMessages.isEmpty()) {
      kafkaMessagesBuffer.clear();
      kafkaMessagesBuffer.addAll(kafkaMessages);
    }

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String title = "Last messages -  refreshed every 3 seconds";

    out.println("<HTML>" +
                "<HEAD>\n" +
                    "<meta http-equiv=\"Refresh\" content=\"3\">\n" +
                    "<STYLE>\n" +
                    "body { font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif; }\n" +
                    "</STYLE>\n" +
                    "</HEAD>\n" +
            "<BODY>\n" +
            "<H2 ALIGN=CENTER>" + title + "</H2>\n" +

            "<UL>\n");

      for (String message : kafkaMessagesBuffer) {
        out.println("<LI style='font-size:small'>"+ message +"</LI>");
      }


      out.println("</UL>\n" +
            "</BODY></HTML>");

    kafkaMessages.clear();

  }

  /**
   *   thread, which grabs the messages from kafka and stores the latest one in "kafkaMessage"
   */
  @Override
  public void run() {
    kafkaMessages = new ArrayList<String>();
    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(100);
        for (ConsumerRecord<String, String> record : records) {
          kafkaMessages.add(record.value());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      consumer.close();
    }
  }
}