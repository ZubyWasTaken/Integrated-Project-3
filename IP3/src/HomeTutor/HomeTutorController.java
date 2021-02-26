/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeTutor;

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
 * FXML Controller class
 *
 * @author stani
 */
public class HomeTutorController implements Initializable {
    @FXML
    private Label labelWelcome;
    User currentUser;
    @FXML
    private JFXButton sgnOutBut;
    @FXML 
    private JFXButton editBut;
    
    public void setData(User user) {
    currentUser = user;
    

    }
 @FXML
    private void signOut(ActionEvent event){
        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister()); 
    }
     @FXML
    private void edit(ActionEvent event){
        SwitchWindow.switchWindow((Stage) editBut.getScene().getWindow(), new EditTutor(currentUser)); 
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         Platform.runLater(new Runnable() {
    @Override
            public void run() {
    
}
         });
    }
}
