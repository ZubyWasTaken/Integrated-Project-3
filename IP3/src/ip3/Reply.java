/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import SQL.SQLHandler;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
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
    private final String sender;
    
    private static SQLHandler sql = new SQLHandler();
    
     public static Reply search(int replyId) throws SQLException, IOException {
        List questInfo = sql.searchReplies(replyId);
        int id = (int) questInfo.get(0);
        int quest_id = (int) questInfo.get(1);
        String text = (String) questInfo.get(2);
        String sender = (String) questInfo.get(3);
        Reply reply = new Reply (id,quest_id,text,sender);
        return reply;
                
    }
    
     public Reply(int id, int quest_id, String text, String sender){
         this.id=id;
         this.quest_id=quest_id;
         this.text=text;
         this.sender=sender;  
     }
     
     public int getId(){
         return this.id;
     }
     
     public int getQuestId(){
         return this.quest_id;
     }
     
     public String getText(){
         return this.text;
     }
     
     public String getSender(){
         return this.sender;
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


