package syncServices;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerChannel;

import java.util.Date;
import java.util.StringTokenizer;

public class SyncService {


    private static final String CHANNEL_NAME = "/chess/sync";

    public SyncService() {
    }

    public void push(String subChannel, String data) {
        BayeuxServer instance = BayeuxInitializer.getInitializer().getServer();
        ServerChannel channel = instance.getChannel(CHANNEL_NAME + subChannel);
        if (channel != null) {
            channel.publish(null, data);
        } else {
            System.out.println("syncing channels: " + subChannel + " " + new Date());
        }
    }
}
