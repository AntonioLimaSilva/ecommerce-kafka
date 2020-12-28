package br.com.luciano.ecommerce;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class OrderServlet extends HttpServlet {

    private static final String TOPIC_ORDER = "ECOMMERCE_NEW_ORDER";
    private static final String TOPIC_EMAIL = "ECOMMERCE_SEND_EMAIL";

    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<Email> emailKafkaDispatcher = new KafkaDispatcher<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var subject = new Email("Thank you for your order! We are proposing your order.");
        var email = req.getParameter("email");
        var amount = new BigDecimal(req.getParameter("amount"));

        var order = new Order(UUID.randomUUID().toString(), amount, email);

        try {
            orderKafkaDispatcher.send(TOPIC_ORDER, email, order);
            emailKafkaDispatcher.send(TOPIC_EMAIL, email, subject);
        } catch (ExecutionException | InterruptedException e) {
            throw new ServletException("Error processing message ", e);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("Create new order");
    }

    @Override
    public void destroy() {
        super.destroy();
        this.orderKafkaDispatcher.close();
        this.emailKafkaDispatcher.close();
    }
}
