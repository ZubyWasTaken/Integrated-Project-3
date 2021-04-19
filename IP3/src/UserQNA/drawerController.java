package UserQNA;

import Chat.Chat;
import EditAcc.Edit;
import FileShare.FileShare;
import Home.Home;

import HomeTutor.HomeTutor;
import LoginRegister.LoginRegister;
import QA_Tutor.QA_Tutor;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import ip3.Drawer;
import ip3.SwitchWindow;
import ip3.User;
import java.io.InputStream;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class drawerController implements Initializable {
    User currentUser;

    @FXML
    private JFXButton homeBut;
    @FXML
    private JFXButton qaBut;
    @FXML
    private JFXButton chatBut;
    @FXML
    private JFXButton accBut;
     @FXML
    private JFXButton sgnOutBut;
    
    @FXML
    private JFXButton fileShare;
    
        @FXML
    private ImageView drawerPfp;

    @FXML
    private Label drawerName;

SQLHandler sql=new SQLHandler();       

    @FXML
    private void home(Event event){
        if (currentUser.getTitleId()==1){
            SwitchWindow.switchWindow((Stage) homeBut.getScene().getWindow(), new Home(currentUser)); 
    }
        
         else{
            SwitchWindow.switchWindow((Stage) homeBut.getScene().getWindow(), new HomeTutor(currentUser));
        }  
    }
    
    
    
    @FXML
    private void qa(Event event){
       if (currentUser.getTitleId()==1){
            SwitchWindow.switchWindow((Stage) qaBut.getScene().getWindow(), new UserQNA(currentUser)); 
    }
       else{
           SwitchWindow.switchWindow((Stage) qaBut.getScene().getWindow(), new QA_Tutor(currentUser));
       }  
    } 

    @FXML
    private void acc (Event event){
    
            SwitchWindow.switchWindow((Stage) accBut.getScene().getWindow(), new Edit(currentUser)); 
    
    }
    @FXML
    private void share (Event event){
    
            SwitchWindow.switchWindow((Stage) fileShare.getScene().getWindow(), new FileShare(currentUser)); 
    
    }
    @FXML
    private void chatDrawer (Event event){
    
            SwitchWindow.switchWindow((Stage) chatBut.getScene().getWindow(), new Chat(currentUser)); 
    
    }
    
      @FXML
    private void signOut (Event event){
    
            SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister()); 
    
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         Platform.runLater(new Runnable() {
            @Override
            public void run() {
               String username = currentUser.getUsername();
               drawerName.setText(username);
                InputStream fs;
                try {
                    fs = sql.getImage(currentUser.getUserID());
                    Image image = new Image(fs);
                      drawerPfp.setImage(image);
                } catch (SQLException ex) {
                    Logger.getLogger(drawerController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        
              

            }
        });
    }
    
        

    public void setData(User user) {
        this.currentUser = user;

    }

   
}
