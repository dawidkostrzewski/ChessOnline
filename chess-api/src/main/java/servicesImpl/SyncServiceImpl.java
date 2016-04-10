package servicesImpl;

import services.SyncService;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.jms.Session;

@Stateless
@LocalBean
public class SyncServiceImpl implements SyncService {

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/SyncQueue")
    private Queue syncQueue;

    public void sync(Long entityId) throws JMSException {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            sendTextMessage(session, syncQueue, entityId.toString());
            session.close();
        } catch (JMSException e){
            System.out.println(e.getMessage());
            throw new JMSException(e.getMessage());
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    System.out.println(e.getMessage());
                    throw new JMSException(e.getMessage());
                }
            }
        }
    }

    private void sendTextMessage(Session session, Queue queue, String text) throws JMSException{
        TextMessage message = session.createTextMessage(text);
        sendMessage(session,queue,message);
    }

    private void sendMessage(Session session, Queue queue, TextMessage message) throws JMSException{
        MessageProducer producer = session.createProducer(queue);
        producer.send(message);
    }
}
