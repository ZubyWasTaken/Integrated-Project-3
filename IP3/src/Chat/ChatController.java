/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import ip3.Drawer;
import ip3.Message;
import ip3.User;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private Label countUsers;
    
     @FXML
    public JFXListView<String> onlineUsers;

     @FXML
    private TableView<User> usersOnline;
    
    @FXML
    private TableColumn<User,String> user;
    
    @FXML
    private JFXDrawer drawer;    
    
    @FXML
    private JFXHamburger hamburger;    
    
    String username;
    ObservableList<User> data = FXCollections.observableArrayList();
    User currentUser;
    int count = 0;
    SQLHandler sql = new SQLHandler();

    public void setData(User user) throws SQLException {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                
                Drawer newdrawer = new Drawer();
                drawer.setDisable(true);
                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                username = currentUser.getUsername();
                try {
                    data = sql.showChatUsers(currentUser.getUniId(), currentUser.getCatId(),currentUser.getUserID());
                } catch (SQLException ex) {
                    Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
                user.setCellValueFactory(new PropertyValueFactory<>("username"));
                usersOnline.setItems(data);
                int rows = usersOnline.getItems().size();
                countUsers.setText(String.valueOf(rows));

               
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

          //  viewMsg.appendText(ex.toString() + '\n');
        }
    }

    
//      public void broadcastOnline(String username) throws IOException {
//              output.writeUTF(username);
//          
//      
//      }
    /**
     * Handle button action
     */
    public void sendMsg(ActionEvent event) {
        try {
            //get username and message

            String message = messageArea.getText().trim();

            Message newmsg = new Message(message, username);

            //  System.out.println(newmsg);
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
            output.writeUTF(username);
            output.flush();

            //clear the textfield
            messageArea.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

}
