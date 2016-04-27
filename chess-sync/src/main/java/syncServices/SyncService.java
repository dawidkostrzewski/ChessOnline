package syncServices;

import dataStore.SyncData;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.ServerChannel;

import java.util.Date;
import java.util.StringTokenizer;

public class SyncService {


    private static final String CHANNEL_NAME = "/ChessRests/sync";

    public SyncService() {
    }

    public void push(SyncData data) {
        BayeuxServer instance = BayeuxInitializer.getInitializer().getServer();
        final String entityId = data.getEntityId();
        final String entityClass = data.getEntityClass().toLowerCase() + "s";
        instance.createChannelIfAbsent(CHANNEL_NAME + "/" + entityClass + "/" + entityId, new ConfigurableServerChannel.Initializer() {
            public void configureChannel(ConfigurableServerChannel configurableServerChannel) {
                System.out.println("CREATE " + CHANNEL_NAME + "/" + entityClass + "/" + entityId + " CHANNEL");
            }
        });
        ServerChannel channel = instance.getChannel(CHANNEL_NAME + "/" + entityClass + "/" + entityId);
        if (channel != null) {
            channel.publish(null, data);
            System.out.println("Sync channel: " + "/" + entityClass + "/" + entityId);
        } else {
            System.out.println("NULL CHANNEL");
        }
    }
}
