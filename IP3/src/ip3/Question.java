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
public class Question {
    private int id;
    private int cat_id;
    private String text;
    private int sender;
    
    private static SQLHandler sql = new SQLHandler();
    
     /*public Question(int category) throws SQLException {
        ArrayList<String> questInfo = sql.searchQuestions(category);
        id = parseInt(questInfo.get(0));
        cat_id = parseInt(questInfo.get(1));
        text = questInfo.get(2);
        sender = parseInt(questInfo.get(3));
    }
*/
    public static Question search(String userquest) throws SQLException, IOException {
       List questionInfo = sql.searchQuestions(userquest);
        int id = (int) questionInfo.get(0);
        int cat_id = (int) questionInfo.get(1);
        String text = (String) questionInfo.get(2);
        int sender = (int) questionInfo.get(3);
        
        Question currentQuestion = new Question(id, cat_id, text, sender);

        return currentQuestion;
    }
     public Question(int id, int cat_id, String text, int sender){
         this.id=id;
         this.cat_id=cat_id;
         this.text=text;
         this.sender=sender;  
     }
     
     public int getId(){
         return this.id;
     }
     
     public int getCatId(){
         return this.cat_id;
     }
     
     public String getText(){
         return this.text;
     }
     
     public int getSender(){
         return this.sender;
     }
     
     public void createQuestion(int cat_id, String text, int sender) throws SQLException{
         sql.createQuestion(cat_id, text, sender);
     }
     
}
