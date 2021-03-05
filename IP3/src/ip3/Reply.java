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
    private final int sender;
    
    SQLHandler sql = new SQLHandler();
    
     public Reply(int question) throws SQLException {
        ArrayList<String> questInfo = sql.searchReplies(question);
        id = parseInt(questInfo.get(0));
        quest_id = parseInt(questInfo.get(1));
        text = questInfo.get(2);
        sender = parseInt(questInfo.get(3));
    }
    
     public Reply(int id, int quest_id, String text, int sender){
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
     
     public int getSender(){
         return this.sender;
     }
     /*
     public void createQuestion(int cat_id, String text, int sender) throws SQLException{
         sql.createQuestion(cat_id, text, sender);
     }
     */
}


