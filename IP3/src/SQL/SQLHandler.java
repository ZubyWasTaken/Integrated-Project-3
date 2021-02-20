/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
    public void createUser(int id, String username, String password, String firstname, String surname, String userscore) throws SQLException {

        String sql = "INSERT INTO Users ( FirstName, Surname, Username, Password, isAdmin, UserScore) VALUES(?,?,?,?,?,?)";
        query = conn.prepareStatement(sql);

        ;
        query.setString(1, firstname);
        query.setString(2, surname);
        query.setString(3, username);
        query.setString(4, password);
        query.setString(6, userscore);

        query.executeUpdate();
        query.close();
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
            output.add((rs.getString("UserID")));
            output.add((rs.getString("Firstname")));
            output.add((rs.getString("Surname")));
            output.add((rs.getString("Username")));
            output.add((rs.getString("Password")));
            output.add((rs.getString("isAdmin")));
            output.add((rs.getString("UserScore")));
        }
        return output;
    }
    
    public void addFile(String location) throws SQLException, FileNotFoundException {
        File file = new File (location);
        fs=new FileInputStream(file);
        query=conn.prepareStatement("INSERT INTO FILES(ID,LOCATION,FILETOBYTE) VALUES (?,?,?)");
        query.setString(1, "1");
        query.setString(2,"files/uni.png" );
        query.setBinaryStream(3,fs,(int)file.length());
        query.executeUpdate();
        System.out.println("Image Stored Successfully");
        query.close();
    }
}