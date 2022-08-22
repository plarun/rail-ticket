package rail;

import com.sun.net.httpserver.HttpsServer;
import rail.api.security.Authorizer;
import rail.api.server.DbServer;
import rail.api.server.RailSecureServer;

public class Main {
    public static void main(String[] args) {
        // Database server
        DbServer.connect();

        // JWT authorization
        Authorizer.init();

        // Rail server
//        HttpServer server = RailServer.getServer();
        HttpsServer server = RailSecureServer.getServer();
        server.setExecutor(null);
        server.start();
    }
}
