/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import SQL.SQLHandler;
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
    
    private final int id;
    private final int quest_id;
    private final String text;
    private final String sender;
    
    SQLHandler sql = new SQLHandler();
    
     public Reply(int question) throws SQLException {
        List questInfo = sql.searchReplies(question);
        id = (int) questInfo.get(0);
        quest_id = (int)questInfo.get(1);
        text = (String)questInfo.get(2);
        sender = (String) questInfo.get(3);
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
     /*
     public void createQuestion(int cat_id, String text, int sender) throws SQLException{
         sql.createQuestion(cat_id, text, sender);
     }
     */
}


