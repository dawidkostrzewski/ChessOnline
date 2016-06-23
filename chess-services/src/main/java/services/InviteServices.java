package services;

import dataStore.Invite;

import javax.ejb.EJBTransactionRolledbackException;
import java.util.List;

public interface InviteServices {

    public Invite getInvite(Long id);

    public Invite updateInvite(Invite invite) throws EJBTransactionRolledbackException;

    public List<Invite> getInvitesByReciverId(Long id);

    public List<Invite> getInvitesBySenderId(Long id);

    public Invite createNewInvite(Invite invite) throws Exception;

    public Invite refuseInvite(Long id) throws EJBTransactionRolledbackException;

    public void deleteInvite(Long id) throws EJBTransactionRolledbackException;
}
