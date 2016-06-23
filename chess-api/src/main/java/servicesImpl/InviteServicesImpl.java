package servicesImpl;

import dataStore.Invite;
import dataStore.SyncData;
import services.InviteServices;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.persistence.*;
import java.util.List;

@Stateless
@LocalBean
public class InviteServicesImpl implements InviteServices {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SyncServiceImpl syncService;

    public Invite getInvite(Long id){
        return em.find(Invite.class,id);
    }

    public Invite updateInvite(Invite invite) throws EJBTransactionRolledbackException{
        try{
            em.merge(invite);
            System.out.println("UPDATE INVITE");
            em.flush();
            return invite;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    public List<Invite> getInvitesByReciverId(Long id){
        try{
            TypedQuery<Invite> query = em.createNamedQuery("getInvitesByReciverIdOrderByTimeDesc",Invite.class);
            query.setParameter("userId",id);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    public List<Invite> getInvitesBySenderId(Long id){
        try{
            TypedQuery<Invite> query = em.createNamedQuery("getInvitesBySenderIdOrderByTimeDesc",Invite.class);
            query.setParameter("userId",id);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    public Invite createNewInvite(Invite invite) throws JMSException {
        try{
            em.persist(invite);
            em.flush();
            System.out.println("CREATE INVITE");
            SyncData syncData = new SyncData(invite.getClass().getSimpleName(),invite.getReciverId().toString());
            syncService.sync(syncData);
            return invite;
        } catch (Exception e){
            throw new JMSException(e.getMessage());
        }
    }

    public Invite refuseInvite(Long id) throws EJBTransactionRolledbackException {
        try{
            Invite invite = em.find(Invite.class,id);
            em.remove(invite);
            return invite;
        } catch (EJBTransactionRolledbackException e) {
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    public void deleteInvite(Long id) throws EJBTransactionRolledbackException{
        try{
            Invite invite = em.find(Invite.class,id);
            em.remove(invite);
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }
}
