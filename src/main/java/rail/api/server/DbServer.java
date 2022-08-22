package rail.api.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbServer {

    private static Connection conn;
    private static Properties queries;

    private DbServer() {}

    public synchronized static Connection getConnection() {
        if (conn == null)
            connect();
        return conn;
    }

    public synchronized static void connect() {
        try {
            InputStream in = new FileInputStream("C:/Users/arunp/IdeaProjects/RailTicket/src/main/resources/config.properties");

            Properties prop = new Properties();
            prop.load(in);

            final String connURL = prop.getProperty("db.url");
            final String username = prop.getProperty("db.username");
            final String password = prop.getProperty("db.password");
            final String schema = prop.getProperty("db.schema");
            prop.clear();

            queries = new Properties();
            in = new FileInputStream("C:/Users/arunp/IdeaProjects/RailTicket/src/main/resources/query.properties");
            queries.load(in);

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connURL + schema, username, password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getQuery(String queryName) {
        return queries.getProperty(queryName, null);
    }
}
