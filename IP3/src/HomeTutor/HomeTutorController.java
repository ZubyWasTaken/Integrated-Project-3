/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeTutor;

import LoginRegister.LoginRegister;
import QA_Tutor.QA_Tutor;
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
    @FXML
    private JFXButton qa;
    
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
    
    @FXML
    private void qaSwitch(ActionEvent event){
         SwitchWindow.switchWindow((Stage) qa.getScene().getWindow(), new QA_Tutor(currentUser)); 
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
