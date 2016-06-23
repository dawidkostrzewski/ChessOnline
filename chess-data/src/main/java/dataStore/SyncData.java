package dataStore;


import java.io.Serializable;

public class SyncData implements Serializable {

    private String entityClass;
    private String entityId;

    public SyncData(String entityClass, String entityId) {
        this.entityClass = entityClass;
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }
}
