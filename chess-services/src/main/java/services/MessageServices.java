package services;


import dataStore.Message;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Remote;
import javax.jms.JMSException;
import java.util.List;


public interface MessageServices {

    public Message createNewMessage(Message message) throws EJBTransactionRolledbackException, JMSException;

    public List<Message> getMessageListByReciverId(Long id);

    public void deleteMessage(Long id) throws EJBTransactionRolledbackException;
}
