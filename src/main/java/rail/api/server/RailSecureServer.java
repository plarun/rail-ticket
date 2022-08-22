package rail.api.server;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import rail.api.controller.v1.handler.user.LoginHandler;
import rail.api.controller.v1.handler.user.LogoutHandler;
import rail.api.controller.v1.handler.user.RegisterHandler;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;

public class RailSecureServer {
    private static HttpsServer server;

    private RailSecureServer() {}

    public synchronized static HttpsServer getServer() {
        final int port = 8060;
        if (server == null) {
            try {
                InetSocketAddress inet = new InetSocketAddress(port);

                server = HttpsServer.create(inet, 0);
                SSLContext ssl = SSLContext.getInstance("TLS");

                char[] password = "password".toCharArray();
                KeyStore keyStore = KeyStore.getInstance("JKS");

                InputStream in = new FileInputStream("C:/Users/arunp/IdeaProjects/RailTicket/src/main/resources/keystore.jks");
                keyStore.load(in, password);

                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                keyManagerFactory.init(keyStore, password);

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                trustManagerFactory.init(keyStore);

                ssl.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                server.setHttpsConfigurator(new HttpsConfigurator(ssl) {
                    public void configure(HttpsParameters param) {
                        try {
                            SSLContext context = getSSLContext();
                            SSLEngine engine = context.createSSLEngine();
                            SSLParameters parameters = context.getSupportedSSLParameters();

                            param.setNeedClientAuth(false);
                            param.setCipherSuites(engine.getEnabledCipherSuites());
                            param.setProtocols(engine.getEnabledProtocols());
                            param.setSSLParameters(parameters);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                initContext();
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException |
                    CertificateException | UnrecoverableKeyException | KeyManagementException e) {
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
