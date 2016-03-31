package services;


import dataStore.Game;

import javax.ejb.EJBTransactionRolledbackException;
import java.util.List;

public interface GameServices {

    public Game createNewGame(Game game) throws EJBTransactionRolledbackException;

    public Game updateGame(Game game) throws EJBTransactionRolledbackException;

    public List<Game> getGamesForUserByUserId(Long userId);
}
