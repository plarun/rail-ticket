package rail.api.server;

import com.sun.net.httpserver.HttpServer;
import rail.api.controller.v1.handler.user.LoginHandler;
import rail.api.controller.v1.handler.user.LogoutHandler;
import rail.api.controller.v1.handler.user.RegisterHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RailServer {
    private static HttpServer server;

    private RailServer() {}

    public synchronized static HttpServer getServer() {
        final int port = 8060;
        if (server == null) {
            try {
                server = HttpServer.create(new InetSocketAddress(port), 5);
                initContext();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return server;
    }

    private static void initContext() {
        server.createContext("/api/v1/register", new RegisterHandler());
        server.createContext("/api/v1/login", new LoginHandler());
        server.createContext("/api/v1/logout", new LogoutHandler());
    }
}
