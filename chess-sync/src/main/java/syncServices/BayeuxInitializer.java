package syncServices;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.BayeuxServerImpl;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


public class BayeuxInitializer extends GenericServlet {
    private static final long serialVersionUID = -7246516381725616278L;
    private static BayeuxInitializer instance;
    private static BayeuxServerImpl server;
    private static JMSClient jmsClient;

    public static BayeuxInitializer getInitializer() {
        return instance;
    }

    public BayeuxServer getServer() {
        return server;
    }

    public void init() throws ServletException {
        instance = this;
        server = (BayeuxServerImpl) getServletContext().getAttribute(
                BayeuxServer.ATTRIBUTE);
        jmsClient = new JMSClient();
        jmsClient.init();
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        throw new ServletException();
    }
}
