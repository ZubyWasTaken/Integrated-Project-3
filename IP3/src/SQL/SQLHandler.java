/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;


import ip3.Categories;
import ip3.Uni;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        String url = "jdbc:mysql://139.59.171.16/ip3dev";
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
    public ArrayList getAllTutors() throws SQLException {

        ArrayList<String> output = new ArrayList<>();
        String sql = "SELECT username FROM tutors";
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


    
    public  void updateImage(byte[] photo, int tutorid) throws SQLException {
      String sql = "UPDATE  profile_pics SET filetobyte=? WHERE  user_id=\""+tutorid+"\"";
        query = conn.prepareStatement(sql);
            query.setBytes(1,photo);
            query.executeUpdate();
        query.close();
    }
    
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
        /*
        public List searchInterestsTable(String searchQuery) throws SQLException {

        List output = new ArrayList<>();
        String sql = "SELECT id, name, catId FROM interests WHERE name = \"" + searchQuery + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            output.add((rs.getInt("id")));
            output.add((rs.getString("name")));
            output.add((rs.getInt("catId")));

        }
        return output;
}
        public ObservableList InterestsTable(int searchQuery) throws SQLException {

        ObservableList<Interests> output = FXCollections.observableArrayList();
        output.clear();
        String sql = "SELECT id, name, catId FROM interests WHERE catId = \"" + searchQuery + "\"";
        query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int catId = rs.getInt("catId");
            output.add(new Interests(id, name,catId));
        }
        return output;
}*/

    public void addImage(byte[] photo, int userID) throws SQLException {
        String sql = "INSERT INTO  profile_pics (filetobyte,user_id) VALUES (?,?)";
        query = conn.prepareStatement(sql);
            query.setBytes(1,photo);
            query.setInt(2,userID);
            query.executeUpdate();
        query.close();
    }

   
    public void updateUsername(int userID, String username) throws SQLException {
        String sql = "UPDATE Users SET username=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,username);
            query.executeUpdate();
        query.close();
    }

    public void editPassword(int userID, String password) throws SQLException {
        String sql = "UPDATE Users SET password=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,password);
            query.executeUpdate();
        query.close();
    }

    public void updateFirstname(int userID, String firstname) throws SQLException {
         String sql = "UPDATE Users SET firstname=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,firstname);
            query.executeUpdate();
        query.close();
    }
    public void updateSurname(int userID, String surname) throws SQLException {
         String sql = "UPDATE Users SET sruname=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,surname);
            query.executeUpdate();
        query.close();
    }
    public void updateEmail(int userID, String email) throws SQLException {
         String sql = "UPDATE Users SET email=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
            query.setString(1,email);
        
            query.executeUpdate();
        query.close();
    }
     

    public void updateUni(int userID, int uniId) throws SQLException {
        String sql = "UPDATE Users SET uniid=? WHERE  id=\""+userID+"\"";
        query = conn.prepareStatement(sql);
        query.setInt(1,uniId);
        query.executeUpdate();
        query.close();
    }
}

