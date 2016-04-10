package services;

import javax.ejb.Remote;
import javax.jms.JMSException;


public interface SyncService {
    void sync(Long entityId) throws JMSException;
}
