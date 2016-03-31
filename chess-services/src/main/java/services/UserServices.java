package services;

import dataStore.User;

import javax.ejb.EJBTransactionRolledbackException;
import java.util.List;


public abstract interface UserServices {

    public User getUserById(Long id);

    public User getUserByLogin(String login);

    public List<User> getUsersList();

    public User addNewUser(User user) throws EJBTransactionRolledbackException;

    public User updateUser(User user) throws EJBTransactionRolledbackException;
}
