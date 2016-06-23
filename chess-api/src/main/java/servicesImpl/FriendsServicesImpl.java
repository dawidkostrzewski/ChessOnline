package servicesImpl;


import dataStore.Friends;
import dataStore.Message;
import dataStore.User;
import services.FriendsServices;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
public class FriendsServicesImpl implements FriendsServices {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SyncServiceImpl syncService;

    public Friends createFriend(Friends entity) throws EJBTransactionRolledbackException{
        try{
            em.persist(entity);
            em.flush();
            return entity;
        } catch (EJBTransactionRolledbackException e){
            System.out.println(e.getMessage());
            throw new EJBTransactionRolledbackException(e.getMessage());
        }

    }

    public List<User> getFriendsForUser(Long userId) {
        try{
            List<User> userList = new ArrayList<User>();
            TypedQuery<Friends> query = em.createNamedQuery("getFriendForUserById",Friends.class);
            query.setParameter("userId",userId);
            List<Friends> friendsList= query.getResultList();
            for (Friends friend: friendsList) {
                if(friend.getUserId().longValue() == userId){
                    userList.add(em.find(User.class,friend.getFriendId()));
                }
                else{
                    userList.add(em.find(User.class,friend.getUserId()));
                }
            }
            return userList;
        } catch (NoResultException e){
            return null;
        }
    }

    public Boolean checkIfCanAdd(Long userId,Long friendId) {

        TypedQuery<Friends> query = em.createNamedQuery("checkIfCanAdd",Friends.class);
        query.setParameter("userId", userId);
        query.setParameter("friendId", friendId);

        TypedQuery<Message> messageQuery = em.createNamedQuery("getFriendInvites",Message.class);
        messageQuery.setParameter("userId", userId);
        messageQuery.setParameter("friendId", friendId);

        if(query.getResultList().size() == 0 && messageQuery.getResultList().size() == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) throws EJBTransactionRolledbackException {
        try{
            TypedQuery<Friends> query = em.createNamedQuery("getFriendByUserIdByFriendId",Friends.class);
            query.setParameter("userId", userId);
            query.setParameter("friendId", friendId);
            List<Friends> friendsList = query.getResultList();
            Friends friend = friendsList.get(0);
            em.remove(friend);
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    public void deleteFriend(int id) throws EJBTransactionRolledbackException{
        try{
            Friends friend = em.find(Friends.class,id);
            em.remove(friend);
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }
}
