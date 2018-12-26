package websocket.chat.connection;

import java.sql.*;
import org.apache.commons.dbutils.DbUtils;

public class UsersDao extends Dao {

    public static void addUser(String name, String password){
        // TODO: throw Exception when User is already in DB
        Connection con = null;
        PreparedStatement preparedStmt = null;
        try {
            con = getConnection();
            String query = "INSERT INTO `users` (`name`,`password`) VALUES (?, ?); ";
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, name);
            preparedStmt.setString (2, password);
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(preparedStmt);
            DbUtils.closeQuietly(con);
        }
    }

    public static boolean isUserInDb(String name){
        Connection con = null;
        PreparedStatement preparedStmt = null;
        ResultSet rset = null;
        try {
            con = getConnection();
            String query = "SELECT `name` from `users` WHERE  name = ?; ";
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, name);
            rset = preparedStmt.executeQuery();
            return rset.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rset);
            DbUtils.closeQuietly(preparedStmt);
            DbUtils.closeQuietly(con);
        }
        return false;
    }

    public static boolean isPasswordValid(String name, String password){
        Connection con = null;
        PreparedStatement preparedStmt = null;
        ResultSet rset = null;
        try {
            con = getConnection();
            String query = "SELECT `name` from `users` WHERE  name = ? AND password = ?; ";
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, name);
            preparedStmt.setString (2, password);
            rset = preparedStmt.executeQuery();
            return rset.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(preparedStmt);
            DbUtils.closeQuietly(con);
        }
        return false;
    }
}