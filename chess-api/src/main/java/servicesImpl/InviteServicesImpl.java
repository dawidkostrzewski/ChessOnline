package servicesImpl;

import dataStore.Invite;
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

    public Invite createNewInvite(Invite invite) throws JMSException {
        try{
            em.persist(invite);
            em.flush();
            System.out.println("CREATE INVITE");
            syncService.sync(invite.getReciverId());
            return invite;
        } catch (Exception e){
            throw new JMSException(e.getMessage());
        }
    }
}
