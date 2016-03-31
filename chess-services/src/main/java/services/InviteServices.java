package services;

import dataStore.Invite;

import javax.ejb.EJBTransactionRolledbackException;
import java.util.List;

public abstract interface InviteServices {

    public Invite getInvite(Long id);

    public Invite updateInvite(Invite invite) throws EJBTransactionRolledbackException;

    public List<Invite> getInvitesByReciverId(Long id);

    public Invite createNewInvite(Invite invite) throws EJBTransactionRolledbackException;
}
