/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import SQL.SQLHandler;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stani
 */
public class Question {
    private int id;
    private String text;
    private String sender;
    private boolean resolved;
    private int replies;
    
    private static SQLHandler sql = new SQLHandler();
    
     /*public Question(int category) throws SQLException {
        ArrayList<String> questInfo = sql.searchQuestions(category);
        id = parseInt(questInfo.get(0));
        cat_id = parseInt(questInfo.get(1));
        text = questInfo.get(2);
        sender = parseInt(questInfo.get(3));
    }
*/
    public static Question search(int userquest) throws SQLException, IOException {
        List questionInfo = sql.searchQuestions(userquest);
        int id = (int) questionInfo.get(0);
        String text = (String) questionInfo.get(1);
        String sender = (String) questionInfo.get(2);
        boolean resolved = (boolean) questionInfo.get(3);
        Question currentQuestion = new Question(id, text, sender, resolved);

        return currentQuestion;
    }
    public Question(int id, String text, String sender, boolean resolved){
         this.id=id;
         this.text=text;
         this.sender=sender;  
         this.resolved=resolved;
         //getReplies();
     }
     
     public int getId(){
         return this.id;
     }
     
   
     
     public String getText(){
         return this.text;
     }
     
     public String getSender(){
         return this.sender;
     }
     
     public boolean getResolved(){
         return this.resolved;
     }
     public void createQuestion( String text, int sender) throws SQLException{
         sql.createQuestion(text, sender);
     }

    /*private void getReplies() {
        
    }
     */
}