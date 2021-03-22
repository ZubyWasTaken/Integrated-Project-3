/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EditAcc;

import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import ip3.Hash;
import ip3.Shaker;
import ip3.SwitchWindow;
import ip3.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author stani
 */
public class PasswordEditController implements Initializable {
    
    @FXML
    private JFXButton cancelBut;
    @FXML
    private JFXButton saveBut;
    @FXML
    private JFXPasswordField oldPass;
    @FXML
    private JFXPasswordField newPass1;
      @FXML
    private JFXPasswordField newPass2;
    User currentUser;
    
    
     public void setData(User user) {
    currentUser=user;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    private void updatePassword(ActionEvent event) throws SQLException{
         ArrayList<String> allUsers = new ArrayList<>();
        Hash h = new Hash();
        SQLHandler sql = new SQLHandler();
        String pass = oldPass.getText();
        String passNew = newPass1.getText();
        String passNew2 = newPass2.getText();
        if (!h.verifyHash(pass, currentUser.getPassword())) {
           
            String tilte = "Password";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Old password is invalid. Please re-enter");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        }
         
         if (pass.isEmpty()||passNew.isEmpty()) {

            changePasswordFailed();
            }
     
        if (passNew.length() < 8 || passNew.length() > 32) {
            
            String tilte = "Register";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Password must be between 8-32 characters");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        }
        

        if (!passNew.equals(passNew2)){
            
            String tilte = "Register";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Passwords do not match. Please try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        }
        
         else {
            passNew = h.hash(passNew);
            currentUser.setPassword(passNew);
            currentUser.editPassword(currentUser);

               SwitchWindow.switchWindow((Stage) saveBut.getScene().getWindow(), new Edit(currentUser));

        }
}

    
   
    @FXML 
 private void back(ActionEvent event){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will not be saved. Do you wish to proceed? ", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
            SwitchWindow.switchWindow((Stage) cancelBut.getScene().getWindow(), new Edit(currentUser));   
              
            }
 }
 
 private void changePasswordFailed() {
        Shaker shake = new Shaker(saveBut);
        shake.shake();
        oldPass.requestFocus();
}
    
}
