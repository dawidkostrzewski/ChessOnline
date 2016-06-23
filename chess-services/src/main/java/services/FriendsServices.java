package services;

import dataStore.Friends;
import dataStore.User;

import javax.ejb.EJBTransactionRolledbackException;
import java.util.List;

public interface FriendsServices {

    public Friends createFriend(Friends entity) throws EJBTransactionRolledbackException;

    public List<User> getFriendsForUser(Long userId) throws EJBTransactionRolledbackException;

    public Boolean checkIfCanAdd(Long userId, Long friendId);

    public void removeFriend(Long id, Long friendId) throws EJBTransactionRolledbackException;

    public void deleteFriend(int id) throws EJBTransactionRolledbackException;
}
