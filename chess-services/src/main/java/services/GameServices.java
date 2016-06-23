package services;


import dataStore.Game;

import javax.ejb.EJBTransactionRolledbackException;
import javax.jms.JMSException;
import java.util.List;

public interface GameServices {

    public Game createNewGame(Game game) throws EJBTransactionRolledbackException,JMSException;

    public Game updateGame(Game game, Long id) throws EJBTransactionRolledbackException,JMSException;

    public List<Game> getGamesForUserByUserId(Long userId);

    public void deleteGame(Long id) throws EJBTransactionRolledbackException;
}
