package servicesImpl;

import dataStore.*;
import services.UserServices;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;


@Stateless
@LocalBean
public class UserServicesImpl implements UserServices {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private MessageServicesImpl messageServices;

    @EJB
    private InviteServicesImpl inviteServices;

    @EJB
    private FriendsServicesImpl friendsServices;

    @EJB
    private GameServicesImpl gameServices;
    
    public User getUserById(Long id) throws EJBTransactionRolledbackException {
        try{
            return em.find(User.class,id);
        } catch (NullPointerException e){
            return null;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    
    public User getUserByLogin(String login) throws EJBTransactionRolledbackException {
        try{
            TypedQuery<User> query = em.createNamedQuery("getUserByLogin",User.class);
            query.setParameter("login",login);
            return query.getSingleResult();
        } catch (NoResultException e){
            return null;
        } catch (EJBTransactionRolledbackException e) {
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    
    public List<User> getUsersList() throws EJBTransactionRolledbackException {
        try{
            TypedQuery<User> query = em.createNamedQuery("getUserListByPointsDescOrder",User.class);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        } catch (EJBTransactionRolledbackException e) {
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    
    public User addNewUser(User user) throws EJBTransactionRolledbackException{
        try{
            em.persist(user);
            em.flush();
            return user;
        } catch (EJBTransactionRolledbackException e){
           throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    
    public User updateUser(User user) throws EJBTransactionRolledbackException{
        try{
            em.merge(user);
            return user;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    public void deleteUser(Long id) throws EJBTransactionRolledbackException{
        try{
            User user = em.find(User.class,id);

            List<Message> messageRecivedList = messageServices.getMessageListByReciverId(id);
            for(Message message : messageRecivedList){
                messageServices.deleteMessage(message.getId());
            }

            List<Message> messageSentList = messageServices.getMessageListBySenderId(id);
            for(Message message : messageSentList){
                messageServices.deleteMessage(message.getId());
            }

            List<Invite> inviteRecivedList = inviteServices.getInvitesByReciverId(id);
            for(Invite invite : inviteRecivedList){
                inviteServices.deleteInvite(invite.getId());
            }

            List<Invite> inviteSentList = inviteServices.getInvitesBySenderId(id);
            for(Invite invite : inviteSentList){
                inviteServices.deleteInvite(invite.getId());
            }

            List<Game> gameList = gameServices.getGamesForUserByUserId(id);
            for(Game game : gameList){
                gameServices.deleteGame(game.getId());
            }

            TypedQuery<Friends> query = em.createNamedQuery("getFriendForUserById",Friends.class);
            query.setParameter("userId", id);
            List<Friends> friendsList = query.getResultList();
            for(Friends friend : friendsList){
                friendsServices.deleteFriend(friend.getId());
            }
            em.remove(user);
        } catch (EJBTransactionRolledbackException e) {
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }
}
