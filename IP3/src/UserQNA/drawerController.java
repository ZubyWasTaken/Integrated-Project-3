package UserQNA;

import EditAcc.Edit;
import Home.Home;

import HomeTutor.HomeTutor;
import QA_Tutor.QA_Tutor;
import ip3.SwitchWindow;
import ip3.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class drawerController implements Initializable {
    User currentUser;

    @FXML
    private HBox homeBut;
    @FXML
    private HBox qaBut;
    @FXML
    private HBox chatBut;
    @FXML
    private HBox accBut;
    


    @FXML
    private void home(MouseEvent event){
        if (currentUser.getTitleId()==1){
            SwitchWindow.switchWindow((Stage) homeBut.getScene().getWindow(), new Home(currentUser)); 
    }
        
         else{
            SwitchWindow.switchWindow((Stage) homeBut.getScene().getWindow(), new HomeTutor(currentUser));
        }  
    }
    
    @FXML
    private void qa(MouseEvent event){
       if (currentUser.getTitleId()==1){
            SwitchWindow.switchWindow((Stage) qaBut.getScene().getWindow(), new UserQNA(currentUser)); 
    }
       else{
           SwitchWindow.switchWindow((Stage) qaBut.getScene().getWindow(), new QA_Tutor(currentUser));
       }  
    } 

    @FXML
    private void acc (MouseEvent event){
    
            SwitchWindow.switchWindow((Stage) accBut.getScene().getWindow(), new Edit(currentUser)); 
    
    }
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
        

    public void setData(User user) {
        this.currentUser = user;

    }

   
}
