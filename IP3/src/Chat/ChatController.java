/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import ip3.User;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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
    private Label nameDisplay;
    
    @FXML
    private Label usersOnline;

    String username;

    User currentUser;
    int count = 0;
    SQLHandler sql = new SQLHandler();

    public void setData(User user) throws SQLException {
        currentUser = user;
        count = sql.countUsersOnline(currentUser.getUserID(),currentUser.getCatId(),currentUser.getUniId());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
            System.out.println("Working?");
                 username = currentUser.getUsername();
                 String name = currentUser.getFirstname();
                 nameDisplay.setText(name);
                 usersOnline.setText(String.valueOf(count));
                 
            }
        });
// onlineList.setItems(userList);
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

    /**
     * Handle button action
     */
    public void sendMsg(ActionEvent event) {
        try {
            //get username and message
           
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

            //clear the textfield
            messageArea.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

}
