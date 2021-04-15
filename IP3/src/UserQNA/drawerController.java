package UserQNA;

import Chat.Chat;
import EditAcc.Edit;
import FileShare.FileShare;
import Home.Home;

import HomeTutor.drawerTutorController;
import LoginRegister.LoginRegister;
import com.jfoenix.controls.JFXButton;
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
    private JFXButton fileBut;
    @FXML
    private JFXButton sgnOutBut;
    @FXML
    private Label userUsername;
    @FXML 
    private ImageView profilePic;
    


    @FXML
    private void home(ActionEvent event){
        
         SwitchWindow.switchWindow((Stage) homeBut.getScene().getWindow(), new Home(currentUser)); 
    
    }
    
    @FXML
    private void chat(ActionEvent event){
          SwitchWindow.switchWindow((Stage) chatBut.getScene().getWindow(), new Chat(currentUser));
    }
    @FXML
    private void qa(ActionEvent event){
     
            SwitchWindow.switchWindow((Stage) qaBut.getScene().getWindow(), new UserQNA(currentUser)); 
    
         
    } 

    @FXML
    private void acc (ActionEvent event){
    
            SwitchWindow.switchWindow((Stage) accBut.getScene().getWindow(), new Edit(currentUser)); 
    
    }
    
    @FXML
    private void fileShare (ActionEvent event){
    
            SwitchWindow.switchWindow((Stage) fileBut.getScene().getWindow(), new FileShare(currentUser)); 
    
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         Platform.runLater(new Runnable() {
            @Override
            public void run() {
        userUsername.setText(currentUser.getUsername());
        InputStream fs = null;
                try {
                    fs = currentUser.getImage();
                } catch (SQLException ex) {
                    Logger.getLogger(drawerTutorController.class.getName()).log(Level.SEVERE, null, ex);
                }
        Image image = new Image(fs);
        profilePic.setImage(image);
    }
        });
    }
    
    @FXML
    private void signOut(ActionEvent event) throws SQLException{
        
        currentUser.updateLogin(false);
        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister()); 
    }    

    public void setData(User user) {
        this.currentUser = user;

    }

   
}
