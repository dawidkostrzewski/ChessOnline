package syncServices;

import dataStore.SyncData;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

public class JMSClient implements MessageListener {

    private final static String CF_LOOKUP_NAME = "java:/ConnectionFactory";
    private final static String QUEUE_LOOKUP_NAME = "java:/queue/SyncQueue";
    private final static String CHANEL_SUFFIX = "s";
    private final static String CHANEL_PREFIX= "/";

    private ConnectionFactory connectionFactory;
    private Queue queue;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;

    public void init() {
        try {
            lookupJMS();
            connection = connectionFactory.createConnection();
            session = connection.createSession();
            messageConsumer = session.createConsumer(queue);
            messageConsumer.setMessageListener(this);
            connection.start();
            System.out.println("SyncService started");
        } catch (IOException e) {
            System.out.println("Start syncService error: " + e.getMessage());
        } catch (NamingException e) {
            System.out.println("Start syncService error: " + e.getMessage());
        } catch (JMSException e) {
            System.out.println("Start syncService error: " + e.getMessage());
        }
    }

    public void onMessage(Message message) {
        try{

            SyncService service = new SyncService();
            service.push(message.getBody(SyncData.class));
        } catch(JMSException e){
            System.out.println(e.getMessage());
        }
    }

    private void lookupJMS() throws NamingException, IOException {
        Context ctx = new InitialContext();
        connectionFactory = (ConnectionFactory) ctx.lookup(CF_LOOKUP_NAME);
        queue = (Queue) ctx.lookup(QUEUE_LOOKUP_NAME);
    }
}
