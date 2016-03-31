package servicesImpl;

import dataStore.Invite;
import services.InviteServices;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
@LocalBean
public class InviteServicesImpl implements InviteServices {

    @PersistenceContext(type= PersistenceContextType.EXTENDED)
    private EntityManager em;



    @Override
    public Invite getInvite(Long id){
        Invite invite = em.find(Invite.class,id);

        return invite;
    }

    @Override
    public Invite updateInvite(Invite invite) throws EJBTransactionRolledbackException{
        try{
            em.merge(invite);
            em.flush();
            return invite;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }

    @Override
    public List<Invite> getInvitesByReciverId(Long id){
        try{
            TypedQuery<Invite> query = em.createNamedQuery("getInvitesByReciverIdOrderByTimeDesc",Invite.class);
            query.setParameter("userId",id);
            return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Invite createNewInvite(Invite invite) throws EJBTransactionRolledbackException {
        try{
            em.persist(invite);
            em.flush();
            return invite;
        } catch (EJBTransactionRolledbackException e){
            throw new EJBTransactionRolledbackException(e.getMessage());
        }
    }
}
