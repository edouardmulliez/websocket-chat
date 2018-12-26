package websocket.chat.connection;

import websocket.chat.model.Message;

import org.apache.commons.dbutils.DbUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesDao extends Dao {

    public static void addMessage(Message message){
        // TODO: throw Exception when User is already in DB
        Connection con = null;
        PreparedStatement preparedStmt = null;
        try {
            con = getConnection();
            String query = "INSERT INTO `messages` (`user_name`,`content`, `dt`) VALUES (?, ?, ?); ";
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, message.getUserName());
            preparedStmt.setString (2, message.getContent());
            preparedStmt.setTimestamp (3, message.getDt());
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(preparedStmt);
            DbUtils.closeQuietly(con);
        }
    }

    public static List<Message> getMessages(){
        return getMessages(-1);
    }

    public static List<Message> getMessages(int maxNb){
        boolean hasLimit = maxNb >= 0;
        List<Message> messages = new ArrayList<>();
        Connection con = null;
        PreparedStatement preparedStmt = null;
        ResultSet rset = null;
        try {
            con = getConnection();
            String query = "SELECT * FROM `messages` ORDER BY `dt` DESC";
            query += hasLimit ? " LIMIT ?;": ";";
            preparedStmt = con.prepareStatement(query);
            if (hasLimit)
                preparedStmt.setInt (1, maxNb);
            rset = preparedStmt.executeQuery();

            while (rset.next()){
                String content = rset.getString("content");
                String userName = rset.getString("user_name");
                Timestamp dt = rset.getTimestamp("dt");
                messages.add(new Message(content, userName, dt));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(preparedStmt);
            DbUtils.closeQuietly(con);
        }
        // Put the oldest messages at beginning.
        Collections.reverse(messages);
        return messages;
    }

}
