package servicesImpl;


import dataStore.Message;
import dataStore.SyncData;
import services.MessageServices;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.persistence.*;
import java.util.List;

@Stateless
@LocalBean
public class MessageServicesImpl implements MessageServices {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SyncServiceImpl syncService;

    public Message createNewMessage(Message message) throws EJBTransactionRolledbackException,JMSException {
        try{
            em.persist(message);
            em.flush();
            SyncData syncData = new SyncData(message.getClass().getSimpleName(),message.getReciverId().toString());
            syncService.sync(syncData);
            return message;
        } catch (JMSException e){
            throw new JMSException(e.getMessage());
        } catch (EJBTransactionRolledbackException e){
            throw  new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    public List<Message> getMessageListByReciverId(Long id) {
        try{
            TypedQuery<Message> query = em.createNamedQuery("getMessagesByReciverId",Message.class);
            query.setParameter("userId",id);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    public List<Message> getMessageListBySenderId(Long id) {
        try{
            TypedQuery<Message> query = em.createNamedQuery("getMessagesBySenderId",Message.class);
            query.setParameter("userId",id);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    public void deleteMessage(Long id) {
       try {
           Message message = em.find(Message.class,id);
           em.remove(message);
       } catch (EJBTransactionRolledbackException e){
           throw new EJBTransactionRolledbackException(e.getMessage());
       }
    }
}
