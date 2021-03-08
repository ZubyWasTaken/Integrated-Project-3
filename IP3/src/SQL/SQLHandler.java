/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;


import com.jfoenix.controls.JFXTextArea;
import ip3.Categories;
import ip3.Question;
import ip3.Reply;
import ip3.Uni;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author sqlitetutorial.net
 */
public class SQLHandler {

    Connection conn = SQLHandler.getConn();
    PreparedStatement query;
    FileInputStream fs = null;

    public SQLHandler() {

    }

    //----------------------//
    // CONNECT TO SQLITE DB //
    //----------------------//
    public static Connection getConn() {

        String url = "jdbc:mysql://139.59.171.16/ip3_stella ";
        String username = "ip3";
        String password = "password";
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Connection Error");
            alert.setContentText(e.toString());
            alert.showAndWait();
            return null;
        }
        return conn;
    }

    
    
    
    //-----------------------------//
    // ADD NEW DATA TO USERS TABLE //
    //-----------------------------//
    public void createUser( String username, String password, String firstname, String surname, String dob , String email, int id, int catId, int titleId) throws SQLException {

        String sql = "INSERT INTO Users ( username, password, firstname, surname, dob, email, uniid, catid, title_id) VALUES(?,?,?,?,?,?,?,?,?)";
        query = conn.prepareStatement(sql);
            query.setString(1, username);
        query.setString(2, password);
        query.setString(3, firstname);
        query.setString(4, surname);
        query.setString(5, dob);
        //unsure which format this date is
        query.setString(6, email);
        query.setInt(7, id);
        query.setInt(8, catId);
        query.setInt(9, titleId);
        query.executeUpdate();
        query.close();
    }
    
       //------------------------------------//
    // GET ALL USERNAMES FROM USERS TABLE //
    //------------------------------------//
    public ArrayList getAllUsers() throws SQLException {

        ArrayList<String> output = new ArrayList<>();
        String sql = "SELECT username FROM Users";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();

        while (rs.next()) {
            output.add(rs.getString("Username"));
        }

        query.close();
        return output;
    }

    //------------------------------------//
    // GET A SPECIFIC USER FROM USERS TABLE //
    //------------------------------------//
    public ArrayList searchUsersTable(String searchQuery) throws SQLException {

        ArrayList<String> output = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE Username = \"" + searchQuery + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            output.add((rs.getString("id")));
            output.add((rs.getString("username")));
            output.add((rs.getString("password")));
            output.add((rs.getString("firstname")));
            output.add((rs.getString("surname")));
            output.add((rs.getString("dob")));
            //unsure which format this reads as
            output.add((rs.getString("email")));
            output.add((rs.getString("uniid")));
            output.add((rs.getString("catid")));
            output.add((rs.getString("title_id")));
        }
        return output;
    }

    //------------------//
   //UPDATE PROFILE PIC//
  //------------------//
   
    public  void updateImage(byte[] photo, int tutorid) throws SQLException {
      String sql = "UPDATE  profile_pics SET filetobyte=? WHERE  user_id=\""+tutorid+"\"";
        query = conn.prepareStatement(sql);
            query.setBytes(1,photo);
            query.executeUpdate();
        query.close();
    }
    //----------///
    //update login//
    //---------////
    
    public void updateLogin(int id, Timestamp timestamp, boolean online) throws SQLException{
        String sql = "UPDATE loginData SET lastLogin = ?, online = ? WHERE user_id =\"" + id + "\"";
         query = conn.prepareStatement(sql);
            query.setTimestamp(1,timestamp);
            query.setBoolean(2, online);
            query.executeUpdate();
        query.close();
    }
    
    //------------------//
   //LOAD PROFILE PIC  //
  //------------------//
   
    
    public InputStream getImage(int tutorid) throws SQLException{
       InputStream blob = null;
       String sql = "SELECT filetobyte FROM profile_pics WHERE user_id = \"" + tutorid + "\"";
       query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            blob = rs.getBinaryStream("filetobyte");
           
    }
       
        System.out.println("Success");
        return blob;
    }
    
    public void initialUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO Users ( username, password) VALUES(?,?)";
        query = conn.prepareStatement(sql);

        
        query.setString(1, username);
        query.setString(2, password);
       

        query.executeUpdate();
        query.close();

    
    }
    //------------------//
   //LOAD UNIS         //
  //------------------//
   
    public ObservableList showUniversities() throws SQLException{
        ObservableList<Uni> output = FXCollections.observableArrayList();
        output.clear();

        String sql = "SELECT * FROM uni";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();

        while (rs.next()) {
            int uniId = rs.getInt("id");
            String uniName = rs.getString("name");
            String uniLoc = rs.getString("location");
        
            output.add(new Uni(uniId, uniName, uniLoc));

        }
        query.close();
        return output;
    }
    
    //------------------//
   //GET A SPECIFIC UNI//
  //------------------//
   
        public List searchUniTable(String searchQuery) throws SQLException {

        List output = new ArrayList<>();
        String sql = "SELECT id, name FROM uni WHERE name = \"" + searchQuery + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            output.add((rs.getInt("id")));
            output.add((rs.getString("name")));

        }
        return output;
}
        
    //------------------//
   //GET A SPECIFIC CATEGORY//
  //------------------//
   
        public List searchCategoriesTable(String searchQuery) throws SQLException {

        List output = new ArrayList<>();
        String sql = "SELECT id, name FROM categories WHERE name = \"" + searchQuery + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            output.add((rs.getInt("id")));
            output.add((rs.getString("name")));

        }
        return output;
}
        
     //------------------//
   //LOAD ALL CATEGORIES//
  //------------------//
   
        public ObservableList showCategories() throws SQLException{
        ObservableList<Categories> output = FXCollections.observableArrayList();
        output.clear();

        String sql = "SELECT * FROM categories";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();

        while (rs.next()) {
            int catId = rs.getInt("id");
            String catName = rs.getString("name");
       output.add(new Categories(catId, catName));

        }
        query.close();
        return output;
    }
       
    //------------------//
   //ADD A PROFILE PIC//
  //------------------//
   
    public void addImage(byte[] photo, int userID) throws SQLException {
        String sql = "INSERT INTO  profile_pics (filetobyte,user_id) VALUES (?,?)";
        query = conn.prepareStatement(sql);
            query.setBytes(1,photo);
            query.setInt(2,userID);
            query.executeUpdate();
        query.close();
    }

       //------------------//
   //UPDATE USERNAME//
  //------------------//
   
    public void updateUsername(int userID, String username) throws SQLException {
        String sql = "UPDATE Users SET username=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,username);
            query.executeUpdate();
        query.close();
    }
    
    //------------------//
   //UPDATE PASSWORD//
  //------------------//
   
    public void editPassword(int userID, String password) throws SQLException {
        String sql = "UPDATE Users SET password=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,password);
            query.executeUpdate();
        query.close();
    }
    
        //------------------//
   //UPDATE FIRST NAME//
  //------------------//
   

    public void updateFirstname(int userID, String firstname) throws SQLException {
         String sql = "UPDATE Users SET firstname=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,firstname);
            query.executeUpdate();
        query.close();
    }
    
        //------------------//
   //UPDATE SURNAME//
  //------------------//
   
    
    public void updateSurname(int userID, String surname) throws SQLException {
         String sql = "UPDATE Users SET sruname=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,surname);
            query.executeUpdate();
        query.close();
    }
    
        //------------------//
   //UPDATE EMAIL//
  //------------------//
   
    public void updateEmail(int userID, String email) throws SQLException {
         String sql = "UPDATE Users SET email=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,email);
        
            query.executeUpdate();
        query.close();
    }
    
         //------------------//
   //UPDATE UNI//
  //------------------//
   

    public void updateUni(int userID, int uniId) throws SQLException {
        String sql = "UPDATE Users SET uniid=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
        query.setInt(1,uniId);
        query.executeUpdate();
        query.close();
    }
    
    //------------------------//
    //UPDATE CATEGORY--------//
    //-----------------------//
     public void updateCategory(int catId) throws SQLException {
        String sql = "UPDATE Users SET catid=? WHERE  id=\""+catId+"\"";
        query = conn.prepareStatement(sql);
        query.setInt(1,catId);
        query.executeUpdate();
        query.close();
    }

    //-----------------------------------====//
   //GET A SPECIFIC QUESTION                //
  //---------------------------------------//
   
    public List searchQuestions(int quest) throws SQLException {
        List output = new ArrayList<>();
       String sql = "SELECT Questions.id, Questions.text, Questions.user_id, Questions.resolved, Users.id, Users.username FROM Questions INNER JOIN Users ON\n" +
        "Questions.user_id= Users.id WHERE Questions.id = \"" + quest + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            output.add((rs.getInt("id")));
            output.add((rs.getString("text")));
            output.add((rs.getString("username")));
            output.add(rs.getBoolean("resolved"));
        }
        return output;
        
    }

        //------------------//
   //ADD A NEW QUESTION//
  //------------------//
   
    
    public void createQuestion(String text, int sender) throws SQLException {
        String sql = "INSERT INTO Questions (text, user_id) VALUES(?,?)";
        query = conn.prepareStatement(sql);
        query.setString(1, text);
        query.setInt(2, sender);
        query.executeUpdate();
        query.close();
    }

    public ObservableList showReplies(int quest_id) throws SQLException {
        ObservableList<Reply> output = FXCollections.observableArrayList();
        String sql = "SELECT Replies.id,Replies.quest_id, Replies.text, Users.username FROM Replies INNER JOIN Users ON Replies.user_id=Users.id WHERE Replies.quest_id = \"" + quest_id + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int question_id = rs.getInt("quest_id");
            String text = rs.getString("text");
            String replier = rs.getString("username");
            //Timestamp timestamp=rs.getTimestamp("timestamp");
            output.add(new Reply(id,question_id, text, replier));
        } 
        return output;   
    }
    
     public List searchReplies(int reply) throws SQLException {
        List output = new ArrayList<>();
       String sql = "SELECT Replies.id,Replies.quest_id, Replies.text, Replies.user_id, Users.id, Users.username FROM Replies INNER JOIN Users ON\n" +
        "Replies.user_id= Users.id WHERE Replies.id = \"" + reply + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            output.add((rs.getInt("id")));
            output.add((rs.getInt("quest_id")));
            output.add((rs.getString("text")));
            output.add((rs.getString("username")));

        }
        return output;
        
    }
    
        //------------------//
   //ADD A NEW REPLY//
  //------------------//
   
    
    
    
    public ObservableList showQuestionsTable(int cat_id, int uni_id) throws SQLException {

        ObservableList<Question> output = FXCollections.observableArrayList();
        output.clear();

        String sql = "SELECT Questions.id, Questions.text, Questions.user_id, Questions.resolved, Users.id, Users.username FROM Questions INNER JOIN Users ON\n" +
        "Questions.user_id= Users.id WHERE Questions.user_id =Users.id  AND Users.catid=\"" + cat_id + "\" AND Users.uniid=\"" + uni_id + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            int question_id = rs.getInt("id");
            String question = rs.getString("text");
            String sender = rs.getString("username");
            boolean resolved = rs.getBoolean("resolved");
            output.add(new Question(question_id, question, sender, resolved));
        }
        query.close();
        return output;
}

    public void addReply(String replyText, int id, int userID, Timestamp timestamp) throws SQLException {
       String sql = "INSERT INTO Replies ( quest_id, text, user_id, timestamp) VALUES(?,?,?,?)";
        query = conn.prepareStatement(sql);
        query.setInt(1, id);
        query.setString(2, replyText);
        query.setInt(3, userID);
        query.setTimestamp(4, timestamp);
        query.executeUpdate();
        query.close();
    }

    public void deleteReply(int id) throws SQLException {
      String sql = "DELETE FROM Replies WHERE id =\"" +id + "\"";
        query = conn.prepareStatement(sql);
        query.executeUpdate();
        query.close();   
    }

    public Timestamp getLastLogin(int id ) throws SQLException{
        Timestamp timestamp = null;
        String sql = "SELECT lastLogin FROM loginData WHERE user_id=\"" + id + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
         
        while (rs.next()) {
         timestamp = rs.getTimestamp("lastLogin");
      
    }
        return timestamp;
    }
    
    public int countQuestions(int cat_id, int uni_id, Timestamp now, Timestamp timestamp) throws SQLException{
        int count = 0;
        String sql = "SELECT Questions.id, Users.id FROM Questions INNER JOIN Users ON\n" +
        "Questions.user_id= Users.id  WHERE Users.catid=\"" + cat_id + "\" AND Users.uniid=\"" + uni_id + "\" AND Questions.timestamp < \"" + now +"\" AND Questions.timestamp >\"" + timestamp +"\"";
         query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
             count ++;
        }
        query.close();
        return count;
    }

    public List searchPosts(int userquest) throws SQLException {
        List output = new ArrayList<>();
       String sql = "SELECT Posts.id, Posts.text, Questions.user_id, Users.id, Users.username FROM Posts INNER JOIN Users ON\n" +
        "Posts.user_id= Users.id WHERE Posts.id = \"" + userquest + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            output.add((rs.getInt("id")));
            output.add((rs.getString("text")));
            output.add((rs.getString("username")));
        }
        return output;
    }

    public void createPost(String text, int sender, Timestamp timestamp) throws SQLException {
      
        String sql = "INSERT INTO Posts (text, timestamp, user_id,) VALUES(?,?,?)";
        query = conn.prepareStatement(sql);
        query.setString(1, text);
        query.setTimestamp(2, timestamp);
        query.setInt(3, sender);
        query.executeUpdate();
        query.close();
    }
    }
    


