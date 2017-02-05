package com.mapr.demos;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebServer {

  public static void main( String[] args ) throws Exception
  {

    if (args.length != 2) {
      throw new IllegalArgumentException("Must have 2 parameters [topic] [log]");
    }
    String topicName = args[0];
    String logPath = args[1];

    System.out.println("Starting Web Server / Consuming from "+ topicName);


    NCSARequestLog requestLog = new NCSARequestLog(logPath+"/yyyy_mm_dd_mapr_web_request.log");
    requestLog.setAppend(true);
    requestLog.setExtended(false);
    requestLog.setLogTimeZone("GMT");
    requestLog.setLogLatency(true);


    Server server = new Server(8080);
    server.setRequestLog( requestLog );


    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new ConsumerServlet(topicName)),"/*");

    server.start();
    Log.getRootLogger().info("***  Web Server started on port 8080", new Object[]{});
    server.join();



  }


}
