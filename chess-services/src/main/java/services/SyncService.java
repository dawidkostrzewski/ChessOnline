package services;

import dataStore.SyncData;

import javax.ejb.Remote;
import javax.jms.JMSException;


public interface SyncService {
    void sync(SyncData syncData) throws JMSException;
}
