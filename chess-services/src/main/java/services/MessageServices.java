package services;


import dataStore.Message;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Remote;
import java.util.List;

@Remote
public interface MessageServices {

    public Message createNewMessage(Message message) throws EJBTransactionRolledbackException;

    public List<Message> getMessageListByReciverId(Long id);

    public void deleteMessage(Long id) throws EJBTransactionRolledbackException;
}
