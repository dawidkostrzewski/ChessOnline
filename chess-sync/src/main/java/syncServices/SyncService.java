package syncServices;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.ServerChannel;

import java.util.Date;
import java.util.StringTokenizer;

public class SyncService {


    private static final String CHANNEL_NAME = "/ChessRests/sync";

    public SyncService() {
    }

    public void push(final String subChannel, String data) {
        BayeuxServer instance = BayeuxInitializer.getInitializer().getServer();
        final String entityId = data;
        instance.createChannelIfAbsent(CHANNEL_NAME + "/invites/" + entityId, new ConfigurableServerChannel.Initializer() {
            public void configureChannel(ConfigurableServerChannel configurableServerChannel) {
                System.out.println("CREATE " + CHANNEL_NAME + "/invites/" + entityId + " CHANNEL");
            }
        });
        ServerChannel channel = instance.getChannel(CHANNEL_NAME + "/invites/" + entityId);
        if (channel != null) {
            channel.publish(null, data);
            System.out.println("Sync channel: " + "/invites/" + entityId);
        } else {
            System.out.println("NULL CHANNEL");
        }
    }
}
