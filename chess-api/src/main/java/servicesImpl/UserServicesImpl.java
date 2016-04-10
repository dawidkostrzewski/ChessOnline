package servicesImpl;

import dataStore.User;
import services.UserServices;

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

    @Override
    public User getUserById(Long id) throws EJBTransactionRolledbackException {
        try{
            return em.find(User.class,id);
        } catch (NullPointerException e){
            return null;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    @Override
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

    @Override
    public List<User> getUsersList() throws EJBTransactionRolledbackException {
        try{
            TypedQuery<User> query = em.createNamedQuery("getUsersListByPointsDescOrder",User.class);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        } catch (EJBTransactionRolledbackException e) {
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    @Override
    public User addNewUser(User user) throws EJBTransactionRolledbackException{
        try{
            em.persist(user);
            em.flush();
            return user;
        } catch (EJBTransactionRolledbackException e){
           throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    @Override
    public User updateUser(User user) throws EJBTransactionRolledbackException{
        try{
            em.merge(user);
            return user;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }
}
