package UserQNA;

import Chat.Chat;
import EditAcc.Edit;
import FileShare.FileShare;
import Home.Home;

import HomeTutor.HomeTutor;
import QA_Tutor.QA_Tutor;
import com.jfoenix.controls.JFXButton;
import ip3.SwitchWindow;
import ip3.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
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
    private JFXButton fileShare;


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
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
        

    public void setData(User user) {
        this.currentUser = user;

    }

   
}
