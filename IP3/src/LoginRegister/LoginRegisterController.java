/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import ip3.Hash;
import ip3.Shaker;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author erino
 */
public class LoginRegisterController implements Initializable {

    @FXML
    private AnchorPane layer2;
    @FXML
    private JFXButton signin;
    @FXML
    private Label lab1;
    @FXML
    private Label lab2;
    @FXML
    private Label l3;
    @FXML
    private Label s1;
    @FXML
    private Label s2;
    @FXML
    private Label s3;
    @FXML
    private JFXButton signup;
    @FXML
    private Label a2;
    @FXML
    private Label b2;

    @FXML
    private JFXButton btnsignup;
    @FXML
    private JFXButton btnsignin;
    @FXML
    private TextField regusername;


    @FXML
    private TextField loginUsername;
    
     @FXML
    private JFXTextField regUsername;

    @FXML
    private PasswordField loginPassword;
    
    @FXML
    private JFXPasswordField  regpassword;

    @FXML
    private AnchorPane layer1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s1.setVisible(false);
        s2.setVisible(false);
        s3.setVisible(false);
        signup.setVisible(false);

        b2.setVisible(false);
        btnsignin.setVisible(false);
        loginUsername.setVisible(false);
        loginPassword.setVisible(false);

        regusername.setVisible(true);
        regpassword.setVisible(true);

    }

    public void loginFailed() {
        Shaker shaker = new Shaker(signin);
        shaker.shake();
        loginPassword.setText("");
        loginUsername.requestFocus();
    }
    

     @FXML
    private void register(MouseEvent event) {
        String username = regusername.getText().trim();
       
        
       /*  if (password.length() < 8 || password.length() > 32) {
           
           String tilte = "Register";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Password must be between 8-32 characters.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
             
        }
        */
    }


    @FXML
    private void login(MouseEvent event) throws SQLException {

        String username = loginUsername.getText().trim();
        String password = loginPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
         //   loginUsername.getStyleClass().add("wrong");
           String tilte = "Log In";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Please enter both a username and a password");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        }

        Hash h = new Hash();
      // SQLHandler sql = new SQLHandler();
       // ArrayList<String> user = sql.searchUsersTable(username);
        String user = "erin";
        if (user.length() < 6) {
            loginFailed();
           String tilte = "Log In";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Username not long enough");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

     //   } else if (!h.verifyHash(password, user.get(4))) {
     //       loginFailed();
     //   } else {
           // login(username);
        }

//        if("erin".equals(n1.getText())&&"password".equals(n2.getText())){
//            String tilte = "Sign In";
//            String message = n1.getText();
//            TrayNotification tray = new TrayNotification();
//            AnimationType type = AnimationType.POPUP;
//        
//            tray.setAnimationType(type);
//            tray.setTitle(tilte);
//            tray.setMessage(message);
//            tray.setNotificationType(NotificationType.SUCCESS);
//            tray.showAndDismiss(Duration.millis(3000));
//        }
//        if(!"erin".equals(n1.getText())){
//            String tilte = "Sign In";
//            String message = "Error Username "+"'"+n1.getText()+"'"+" Wrong";
//            TrayNotification tray = new TrayNotification();
//            AnimationType type = AnimationType.POPUP;
//        
//            tray.setAnimationType(type);
//            tray.setTitle(tilte);
//            tray.setMessage(message);
//            tray.setNotificationType(NotificationType.ERROR);
//            tray.showAndDismiss(Duration.millis(3000));
//        }
//        if (!"password".equals(n2.getText())){
//            String tilte = "Sign In";
//            String message = "Error Password " + "Wrong";
//            TrayNotification tray = new TrayNotification();
//            AnimationType type = AnimationType.POPUP;
//        
//            tray.setAnimationType(type);
//            tray.setTitle(tilte);
//            tray.setMessage(message);
//            tray.setNotificationType(NotificationType.ERROR);
//            tray.showAndDismiss(Duration.millis(3000));
//        }
//        if (!"erin".equals(n1.getText())&&!"password".equals(n2.getText())){
//            String tilte = "Sign In";
//            String message = "Error Username "+"'"+n1.getText()+"'"+", and Password " +"Wrong";
//            TrayNotification tray = new TrayNotification();
//            AnimationType type = AnimationType.POPUP;
//        
//            tray.setAnimationType(type);
//            tray.setTitle(tilte);
//            tray.setMessage(message);
//            tray.setNotificationType(NotificationType.ERROR);
//            tray.showAndDismiss(Duration.millis(3000));
//        }
    }

    @FXML
    private void loginAnimation(MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(layer2);

        slide.setToX(491);
        slide.play();

        layer1.setTranslateX(-309);
        btnsignin.setVisible(true);

        b2.setVisible(true);

        s1.setVisible(true);
        s2.setVisible(true);
        s3.setVisible(true);
        signup.setVisible(true);
        lab1.setVisible(false);
        lab2.setVisible(false);
        l3.setVisible(false);
        signin.setVisible(false);

        a2.setVisible(false);
        btnsignup.setVisible(false);
        loginUsername.setVisible(true);
        loginPassword.setVisible(true);
        regusername.setVisible(false);
        regpassword.setVisible(false);

        slide.setOnFinished((e -> {

        }));
    }

    @FXML
    private void signupAnimation(MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(layer2);

        slide.setToX(0);
        slide.play();

        layer1.setTranslateX(0);
        btnsignin.setVisible(false);

        b2.setVisible(false);

        s1.setVisible(false);
        s2.setVisible(false);
        s3.setVisible(false);
        signup.setVisible(false);
        lab1.setVisible(true);
        lab2.setVisible(true);
        l3.setVisible(true);
        signin.setVisible(true);

        a2.setVisible(true);
        btnsignup.setVisible(true);
        loginUsername.setVisible(false);
        loginPassword.setVisible(false);

        regusername.setVisible(true);
        regpassword.setVisible(true);

        slide.setOnFinished((e -> {

        }));
    }

   
}
