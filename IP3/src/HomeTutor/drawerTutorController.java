package HomeTutor;

import EditAcc.Edit;
import LoginRegister.LoginRegister;

import QA_Tutor.QA_Tutor;
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

public class drawerTutorController implements Initializable {
    User currentUser;

    @FXML
    private JFXButton homeBut;
    @FXML
    private JFXButton qaBut;
    @FXML
    private JFXButton accBut;
    @FXML
    private JFXButton sgnOutBut;
    @FXML
    private Label userUsername;
    @FXML
    private ImageView profilePic;
    @FXML
    private void home(ActionEvent event){
     
            SwitchWindow.switchWindow((Stage) homeBut.getScene().getWindow(), new HomeTutor(currentUser));
       
    }
    
   
    @FXML
    private void qa(ActionEvent event){
           SwitchWindow.switchWindow((Stage) qaBut.getScene().getWindow(), new QA_Tutor(currentUser));
    } 

    @FXML
    private void acc (ActionEvent event){
           SwitchWindow.switchWindow((Stage) accBut.getScene().getWindow(), new Edit(currentUser)); 
    
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
