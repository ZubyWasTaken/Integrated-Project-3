/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author sqlitetutorial.net
 */
public class SQLHandler {

    Connection conn = SQLHandler.getConn();
    PreparedStatement query;

    public SQLHandler() {

    }

    //----------------------//
    // CONNECT TO SQLITE DB //
    //----------------------//
    public static Connection getConn() {

        String url = "jdbc:sqlite:src/SQL/ip3.db";
        Connection conn;
        try {
            conn = DriverManager.getConnection(url);
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
    public void createUser( String username, String password, String firstname, String surname, Date dob , String email) throws SQLException {

        String sql = "INSERT INTO Users ( username, password, firstname, surname, dob, email) VALUES(?,?,?,?,?,?)";
        query = conn.prepareStatement(sql);

        
        query.setString(1, username);
        query.setString(2, password);
        query.setString(3, firstname);
        query.setString(4, surname);
        query.setDate(5, dob);
        //unsure which format this date is
        query.setString(6, email);

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
        }
        return output;
    }

    public void initialUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO Users ( username, password) VALUES(?,?)";
        query = conn.prepareStatement(sql);

        
        query.setString(1, username);
        query.setString(2, password);
       

        query.executeUpdate();
        query.close();
    
    }
}

