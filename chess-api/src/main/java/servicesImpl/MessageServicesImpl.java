package servicesImpl;


import dataStore.Invite;
import dataStore.Message;
import services.MessageServices;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
@LocalBean
public class MessageServicesImpl implements MessageServices {

    @PersistenceContext(type= PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    public Message createNewMessage(Message message) throws EJBTransactionRolledbackException {
        try{
            em.persist(message);
            em.flush();
            return message;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    @Override
    public List<Message> getMessageListByReciverId(Long id) {
        try{
            TypedQuery<Message> query = em.createNamedQuery("getMessagesByReciverId",Message.class);
            query.setParameter("userId",id);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void deleteMessage(Long id) {
       try {
           Message message = em.find(Message.class,id);
           em.remove(message);
       } catch (EJBTransactionRolledbackException e){
           throw new EJBTransactionRolledbackException(e.getMessage());
       }
    }
}
