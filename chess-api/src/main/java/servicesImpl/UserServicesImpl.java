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

    @PersistenceContext(type= PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    public User getUserById(Long id){
        return em.find(User.class,id);
    }

    @Override
    public User getUserByLogin(String login){
        try{
            TypedQuery<User> query = em.createNamedQuery("getUserByLogin",User.class);
            query.setParameter("login",login);
            return query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<User> getUsersList(){
        try{
            TypedQuery<User> query = em.createNamedQuery("getUsersListByPointsDescOrder",User.class);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
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
