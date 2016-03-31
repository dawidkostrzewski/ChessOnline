package servicesImpl;

import dataStore.Game;
import dataStore.Invite;
import services.GameServices;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
@LocalBean
public class GameServicesImpl implements GameServices {

    @PersistenceContext(type= PersistenceContextType.EXTENDED)
    private EntityManager em;

    @EJB
    private InviteServicesImpl inviteServices;

    @Override
    public Game createNewGame(Game game) throws EJBTransactionRolledbackException {
        try{
            em.persist(game);
            em.flush();
            return game;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    @Override
    public Game updateGame(Game game) throws EJBTransactionRolledbackException {
        try{
            em.merge(game);
            em.flush();
            return game;
        } catch (EJBTransactionRolledbackException e) {
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    @Override
    public List<Game> getGamesForUserByUserId(Long userId) {
        try{
            TypedQuery<Game> query = em.createNamedQuery("getGamesForUserByUserId",Game.class);
            query.setParameter("userId",userId);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }
}
