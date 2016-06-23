package services;

import dataStore.User;

import javax.ejb.EJBTransactionRolledbackException;
import java.util.List;


public abstract interface UserServices {

    public User getUserById(Long id) throws EJBTransactionRolledbackException;

    public User getUserByLogin(String login) throws EJBTransactionRolledbackException;

    public List<User> getUsersList() throws EJBTransactionRolledbackException;

    public User addNewUser(User user) throws EJBTransactionRolledbackException;

    public User updateUser(User user) throws EJBTransactionRolledbackException;

    public void deleteUser(Long id) throws EJBTransactionRolledbackException;
}
