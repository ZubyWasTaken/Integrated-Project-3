/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Chat.Chat;

import Interests.Interests;
import LoginRegister.LoginRegister;
import com.jfoenix.controls.JFXButton;
import ip3.SwitchWindow;
import ip3.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Zuby
 */
public class homeController implements Initializable {
   
    @FXML
    private Label labelWelcome;
    User currentUser;
    @FXML
    private JFXButton sgnOutBut;
    
     @FXML
    private JFXButton chat;
    @FXML
    private Label username;
    
    public void setData(User user) {
    currentUser = user;
  
    
    }
    
    @FXML
    private void signOut(ActionEvent event){
        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister()); 
    }
    
    @FXML
    private void viewChats(ActionEvent event){
       
        SwitchWindow.switchWindow((Stage) chat.getScene().getWindow(), new Chat(currentUser)); 
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
  
        
         Platform.runLater(new Runnable() {
    @Override
            public void run() {
            username.setText(currentUser.getUsername());
}
         });
    }
}





