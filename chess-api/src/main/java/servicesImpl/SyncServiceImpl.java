package servicesImpl;

import dataStore.SyncData;
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

    @Resource(mappedName = "java:/queue/SyncQueue")
    private Queue syncQueue;

    public void sync(SyncData syncData) throws JMSException {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            prepareSendMessage(session, syncQueue, syncData);
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

    private void prepareSendMessage(Session session, Queue queue, SyncData syncData) throws JMSException{
        Message message = session.createObjectMessage(syncData);
        sendMessage(session,queue,message);
    }

    private void sendMessage(Session session, Queue queue, Message message) throws JMSException{
        MessageProducer producer = session.createProducer(queue);
        producer.send(message);
    }
}
