/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import SQL.SQLHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author stani
 */
public class Post {
    private int id;
    private String text;
    private String sender;
    private static SQLHandler sql = new SQLHandler();
    //private int replies;
     public static Post search(int userpost) throws SQLException, IOException {
        List questionInfo = sql.searchPosts(userpost);
        int id = (int) questionInfo.get(0);
        String text = (String) questionInfo.get(1);
        String sender = (String) questionInfo.get(2);
       
        Post currentPost = new Post(id, text, sender);

        return currentPost;
    }
      public Post(int id, String text, String sender){
         this.id=id;
         this.text=text;
         this.sender=sender;  
      
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
     
     
     public void createPost( String text, int sender, Timestamp timestamp) throws SQLException{
         
         sql.createPost(text, sender,timestamp);
     }
}
