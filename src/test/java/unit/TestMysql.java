package unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rail.api.server.DbServer;

import java.sql.*;

public class TestMysql {
    @Test
    @DisplayName("Test Mysql DB connectivity")
    void testDbConnection() throws SQLException {
        DbServer.connect();

        Connection conn = DbServer.getConnection();
        String query = "Select user_name, password, email, age, gender From ussr Where user_name = ?";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, "test user 1");
        ResultSet result = prepStmt.executeQuery();

        assert result != null;

        while (result.next()) {
            String username = result.getString(1);
            String password = result.getString(2);
            String email = result.getString(3);
            int age = result.getInt(4);
            String gender = result.getString(5);

            System.out.println(username + " " + password + " " + email + " " + age + " " + gender);
        }
    }
}
