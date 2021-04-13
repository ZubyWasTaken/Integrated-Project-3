/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginRegister;

import Home.Home;
import HomeTutor.HomeTutor;
import Interests.Interests;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import ip3.Hash;
import ip3.Shaker;
import ip3.SwitchWindow;
import ip3.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.synedra.validatorfx.Validator;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author erino
 */
public class LoginRegisterController implements Initializable {

    //FXML declaration
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
    private JFXTextField loginUsername;
    @FXML
    private JFXTextField regusername;
    @FXML
    private JFXPasswordField loginPassword;
    @FXML
    private JFXPasswordField regpassword;
    @FXML
    private AnchorPane layer1;
    
    //Other variables declaration
    ArrayList<String> allUsers = new ArrayList<>();
    SQLHandler sql = new SQLHandler();
    TrayNotification tray = new TrayNotification();
    AnimationType type = AnimationType.POPUP;
    private Validator validator = new Validator();
    String username ;
    String password;
    Hash h = new Hash();
    
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
            tray.setAnimationType(type);
             
        try {
            allUsers=sql.getAllUsers();
        } catch (SQLException ex) {
            Logger.getLogger(LoginRegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
            btnsignup.disableProperty().bind(validator.containsErrorsProperty());
            btnsignup.disableProperty().bind(validator.containsWarningsProperty());
            validator.createCheck()
             .dependsOn("username", regusername.textProperty())
             .withMethod(c -> {
             username = c.get("username");
            if (username.isEmpty()){
               c.warn("Please enter a username");
            }
                     else{
                      if (allUsers.contains(username)) {
                      c.error("This username is taken. Please select a new one.");
            }
                      else if (User.match(username) == true) {
                c.error("Username cannot contain special characters.");
              }
                      else{
                          username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase(); 
                      }
            }
            
          })
          .decorates(regusername)
          .immediate();;
          
           validator.createCheck()
                   .dependsOn("password",regpassword.textProperty())
                   .withMethod(c->{
                       password = c.get("password");
                       if (password.isEmpty()){
                          c.warn("Please enter a password");
                       }
                       else {                      
                        if ((password.length() < 8 || password.length() > 32)) {
                            c.error("Password must be between 8-32 characters.");
                        }
                       }
                        
                   })
                   .decorates(regpassword)
                   .immediate();
            btnsignup.setOnAction(e -> register(username, password));
           
        
    }

    
    private void register(String username, String password) {
            password = h.hash(password);
            User intialUser= new User(username, password);
            SwitchWindow.switchWindow((Stage) btnsignup.getScene().getWindow(), new Interests(intialUser));   
 
    }

    //failed login
    public void loginFailed() {
        Shaker shaker = new Shaker(signin);
        shaker.shake();
        loginPassword.setText("");
        loginUsername.requestFocus();
    }

    //failed register
    private void registerFailed() {
        Shaker shake = new Shaker(btnsignup);
        shake.shake();
        regusername.requestFocus();
        //regusername.getStyleClass().add("wrong");
    }

    @FXML
    private void login(MouseEvent event) throws SQLException {

        username = loginUsername.getText().trim();
        password = loginPassword.getText().trim();
        
        checkEmpty(username, password);
        allUsers = sql.searchUsersTable(username);
        
           
            if (allUsers.size() < 6){
                
                tray.setTitle("Login");
                tray.setMessage("Username is incorrect.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));

                loginFailed();

                return;
            } else if (!h.verifyHash(password, allUsers.get(2))) {
                
                tray.setTitle("Login");
                tray.setMessage("Password incorrect.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));

                loginFailed();

                return;

            } else {
             
                login(username);
   
                tray.setTitle("Login");
                tray.setMessage("Welcome Back, " + username);
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(3000));
                }
    }
 

    private void login(String user) throws SQLException {
        
        User currentUser = new User(user);
        if (currentUser.getTitleId()==1) {
            SwitchWindow.switchWindow((Stage) btnsignin.getScene().getWindow(), new Home(currentUser));
        } else {
            SwitchWindow.switchWindow((Stage) btnsignin.getScene().getWindow(), new HomeTutor(currentUser));
        }
       
    }

    @FXML
    private void checkEmpty(String username, String password) {
        if (username.isEmpty()) {
          
            tray.setTitle("Register");
            tray.setMessage("Username and password are empty.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();

            return;

        }

        if (username.isEmpty()) {
            
            tray.setTitle("Register");
            tray.setMessage("Username is empty.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        }

        if (password.isEmpty()) {
            
            tray.setTitle("Register");
            tray.setMessage("Password is empty.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();

            return;

    }
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
