/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import SQL.SQLHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author erino
 */
public class Message {

    private String date;
    private String username;
    private static SQLHandler sql = new SQLHandler();
    private int id;
    private String message;

    public Message(int id, String message, String username, String date) throws SQLException {
        this.id = id;
        this.message = message;
        this.username = username;
        this.date = date;
        //getReplies();
    }

    public Message(String message, String username) {
        id = 0;
        this.username = username;
        this.message = message;
        this.username = username;
        date = null;
    }

    public int getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public String getUsername() {
        return this.username;
    }

    public String getDate() {
        return this.date;
    }

    public static void createMessage(String message, String username) throws SQLException {
        // sql.addMessage(message, username);
    }
}
