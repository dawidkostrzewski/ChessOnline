package servicesImpl;

import dataStore.Game;
import dataStore.SyncData;
import services.GameServices;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.persistence.*;
import java.util.List;

@Stateless
@LocalBean
public class GameServicesImpl implements GameServices {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private InviteServicesImpl inviteServices;

    @EJB
    private SyncServiceImpl syncService;

    public Game createNewGame(Game game) throws EJBTransactionRolledbackException,JMSException {
        try{
            em.persist(game);
            em.flush();
            System.out.println("CREATE GAME");
            SyncData syncData = new SyncData(game.getClass().getSimpleName(),game.getUserId().toString());
            syncService.sync(syncData);
            syncData = new SyncData(game.getClass().getSimpleName(),game.getOpponentId().toString());
            syncService.sync(syncData);
            return game;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        } catch (JMSException e){
        throw new JMSException(e.getMessage());
        }
    }

    public Game updateGame(Game game, Long id) throws EJBTransactionRolledbackException,JMSException {
        try{
            em.merge(game);
            em.flush();
            SyncData syncData = new SyncData(game.getClass().getSimpleName(),id.toString());
            syncService.sync(syncData);
            return game;
        } catch (EJBTransactionRolledbackException e) {
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    public List<Game> getGamesForUserByUserId(Long userId) {
        try{
            TypedQuery<Game> query = em.createNamedQuery("getGamesForUserByUserId",Game.class);
            query.setParameter("userId",userId);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    public void deleteGame(Long id) throws EJBTransactionRolledbackException{
        try{
            Game game = em.find(Game.class,id);
            em.remove(game);
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }
}
