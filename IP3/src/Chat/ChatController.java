/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import Home.Home;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import ip3.Drawer;
import ip3.Post;
import ip3.SwitchWindow;
import ip3.User;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author erino
 */
public class ChatController implements Initializable {

    // IO streams
    DataOutputStream output = null;

    @FXML
    public JFXTextArea viewMsg;

    @FXML
    private JFXTextArea messageArea;

    @FXML
    private JFXButton sendMsg;

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;

    @FXML
    Label usersOnline;

    @FXML
    private JFXButton sgnOutBut;

    @FXML
    public static JFXListView<String> onlineUsers;

    String username;
    int userID;
    User currentUser;
    int count = 0;
    SQLHandler sql = new SQLHandler();

    Timestamp now = new Timestamp(System.currentTimeMillis());

    public void setData(User user) throws SQLException {
        currentUser = user;
        count = sql.countUsersOnline(currentUser.getUserID(), currentUser.getCatId(), currentUser.getUniId());

    }

    @FXML
    private void signOut(ActionEvent event) throws SQLException {

        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new Home(currentUser));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            displayMsgs();
        } catch (SQLException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                drawer.setDisable(true);
                Drawer newdrawer = new Drawer();
                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                System.out.println("Working?");
                username = currentUser.getUsername();
             

              
                usersOnline.setText(String.valueOf(count));
                userID = currentUser.getUserID();

            }
        });

        try {
           
            // Create a socket to connect to the server
            Socket socket = new Socket(ConnectionUtil.host, ConnectionUtil.port);

            //Connection successful
            viewMsg.appendText("Connected. \n");

            // Create an output stream to send data to the server
            output = new DataOutputStream(socket.getOutputStream());

            //create a thread in order to read message from server continuously
            TaskReadThread task = new TaskReadThread(socket, this);
            Thread thread = new Thread(task);
            thread.start();

        } catch (IOException ex) {

              viewMsg.appendText(ex.toString() + '\n');
        }
    }

    public void displayMsgs() throws SQLException {
        ObservableList<Post> data = FXCollections.observableArrayList();
        ArrayList<String> userdata = new ArrayList<>();
        data = sql.showMsgs();
        String name = "";
        for (Post post : data) {
            String msg = post.getText();
            int id = post.getSender();
            userdata = sql.searchByID(id);
            name = userdata.get(1);
            viewMsg.appendText("[" + name + "]: " + msg + "\n");
        }

    }

    /**
     * Handle button action
     */
    public void sendMsg(ActionEvent event) throws SQLException {
        try {
            //get message

            String message = messageArea.getText().trim();

            //if username is empty set it to 'Unknown' 
            if (username.length() == 0) {
                username = "Unknown";
            }
            //if message is empty, just return : don't send the message
            if (message.length() == 0) {
                return;
            }

            //send message to server
            output.writeUTF("[" + username + "]: " + message + "");

            output.flush();
            Post.createPost(userID, message);
            //clear the textfield
            messageArea.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

}
