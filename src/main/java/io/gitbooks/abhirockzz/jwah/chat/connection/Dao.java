package io.gitbooks.abhirockzz.jwah.chat.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dao {

    protected static Config cfg = null;

    protected static Connection getConnection() throws SQLException {
        String host = Config.getProperty("HOST");
        String port = Config.getProperty("DB_PORT");
        String db = Config.getProperty("DB");
        String login = Config.getProperty("LOGIN");
        String password = Config.getProperty("PASSWORD");

        //TODO: put "timezone='UTC'" in a conf file for the mysql db
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?serverTimezone=UTC";
        return DriverManager.getConnection(url, login, password);
    }
}
