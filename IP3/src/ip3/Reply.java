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
 * @author stani
 */
public class Reply {

    
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
/**
 *
 * @author stani
 */

    private int id;
    private int quest_id;
    private final String text;
    private final User sender;
    private final String date;

    private static SQLHandler sql = new SQLHandler();

    public static Reply search(int replyId) throws SQLException, IOException {
        List replyInfo = sql.searchReplies(replyId);
        int id = (int) replyInfo.get(0);
        int quest_id = (int) replyInfo.get(1);
        String text = (String) replyInfo.get(2);
        String sender = (String) replyInfo.get(3);
        String date = (String) replyInfo.get(4);
        Reply reply = new Reply(id, quest_id, text, sender, date);
        return reply;

    }

    public Reply(int id, int quest_id, String text, String sender, String date) throws SQLException {
        this.id = id;
        this.quest_id = quest_id;
        this.text = text;
        this.sender = new User(sender);
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public int getQuestId() {
        return this.quest_id;
    }

    public String getText() {
        return this.text;
    }

    public User getSender() {
        return this.sender;
    }

    public String getDate() {
        return this.date;
    }

    public static void createReply(String replyText, int questionid, int userID) throws SQLException {
        sql.addReply(replyText, questionid, userID);

    }
     /*
     
     public void createReply(int cat_id, String text, int sender) throws SQLException{
         sql.createQuestion(cat_id, text, sender);
     }
     */
}


