package br.com.luciano.ecommerce;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpService {

    public static void main(String[] args) throws Exception {
        var server = new Server(8080);

        var servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder(new OrderServlet()), "/orders");

        server.setHandler(servletContextHandler);

        server.start();
        server.join();
    }

}
