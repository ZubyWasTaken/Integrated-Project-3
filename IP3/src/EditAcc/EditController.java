/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EditAcc;

import HomeTutor.*;
import Interests.InterestController;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import ip3.Categories;
import ip3.Drawer;
import ip3.SwitchWindow;
import ip3.User;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
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
public class EditController implements Initializable {
User currentUser;
@FXML
private JFXTextField showLoc;
@FXML
private AnchorPane pane;
@FXML
private ImageView imageShow;
@FXML
private JFXButton cancelBut;
@FXML
private JFXButton saveBut;
@FXML
private JFXTextField username;
@FXML
private JFXTextField fname;
@FXML
private JFXTextField surname;
@FXML
private JFXComboBox catSelect;
@FXML
private JFXTextField email;
@FXML
private JFXButton backBut;
@FXML 
private Tab accTab;
@FXML
private Tab personalTab;
@FXML
private JFXTabPane tp;
@FXML
private JFXHamburger hamburger; 
@FXML
private JFXDrawer drawer;


SQLHandler sql=new SQLHandler();       
byte[] photo=null;
String filename=null;
String location;
int catId;
ArrayList<String> allUsers;
ObservableList<Categories> data2 = FXCollections.observableArrayList();
ObservableList<String> namesCat = FXCollections.observableArrayList();


 @FXML
 private void upload(MouseEvent event){
    FileChooser fc = new FileChooser();
    fc.setTitle("Open File Dialog");
    Stage stage = (Stage)pane.getScene().getWindow();
    File file = fc.showOpenDialog(stage);
    if (file !=null)
    {
        location=file.getAbsolutePath();
        Image image = new Image (file.toURI().toString());
        imageShow.setImage(image);
             
    }
  
    }
 @FXML
 private void save (ActionEvent event) throws FileNotFoundException, IOException, SQLException{
    usernameSave();
    picSave();
    emailSave();
    fnameSave();
    surnameSave();
    catSave();
    String tilte = "Success";
    TrayNotification tray = new TrayNotification();
    AnimationType type = AnimationType.POPUP;

    tray.setAnimationType(type);
    tray.setTitle(tilte);
    tray.setMessage("All changes are successfully saved.");
    tray.setNotificationType(NotificationType.SUCCESS);
    tray.showAndDismiss(Duration.millis(3000));
           
    SwitchWindow.switchWindow((Stage) saveBut.getScene().getWindow(), new Edit(currentUser));   
 }
 
 @FXML 
 private void passEdit(ActionEvent event){
    SwitchWindow.switchWindow((Stage) saveBut.getScene().getWindow(), new passwordEdit(currentUser));   
 }
 @FXML 
 private void cancel(ActionEvent event){
    
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will not be saved. Do you wish to proceed? ", ButtonType.YES, ButtonType.CANCEL);
    alert.showAndWait();

    if (alert.getResult() == ButtonType.YES) {
        SwitchWindow.switchWindow((Stage) cancelBut.getScene().getWindow(), new Edit(currentUser));   
              
        }
     
 }
 
 @FXML
 private void returnToAcc(Event event) throws SQLException{

 }
  @FXML
 private void returnToPersonal(Event event){
    tp.getSelectionModel().select(personalTab);
 }
 
  @FXML 
 private void back(ActionEvent event){
    
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will not be saved. Do you wish to proceed? ", ButtonType.YES, ButtonType.CANCEL);
    alert.showAndWait();

    if (alert.getResult() == ButtonType.YES) {
        SwitchWindow.switchWindow((Stage) backBut.getScene().getWindow(), new HomeTutor(currentUser));   
              
            }
     
 }
    /**
     * Initializes the controller class.
     */
 
 @Override
public void initialize(URL url, ResourceBundle rb) {
    Platform.runLater(new Runnable() {
    @Override
        public void run() {
            try {
                
                Drawer newdrawer = new Drawer();
                drawer.setDisable(true);
                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                
                username.setText(currentUser.getUsername());
                tp.getSelectionModel().select(accTab);
                InputStream fs= sql.getImage(currentUser.getUserID());
                Image image = new Image(fs);
        
                imageShow.setImage(image);
                username.setText(currentUser.getUsername());
                fname.setText(currentUser.getFirstname());
                surname.setText(currentUser.getSurname());
                email.setText(currentUser.getEmail());
                catPopulate();
                catSelect.getSelectionModel().select(currentUser.getCatId()-1);
           
        
    }   catch (SQLException ex) {
        Logger.getLogger(EditController.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        
        catSelect.setOnAction(new EventHandler() {
        @Override
        public void handle(Event event) {
            String tempcat = (String) catSelect.getSelectionModel().getSelectedItem();
            try {
                 catId = Categories.fetchCatId(tempcat);
            } catch (SQLException ex) {
                Logger.getLogger(InterestController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }           
});   
}
    });
            }
  
private void catPopulate(){
          try {
            data2 = sql.showCategories();
        } catch (SQLException ex) {
            Logger.getLogger(InterestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Categories c:data2)
        {
            namesCat.add(c.getName());
        }
        catSelect.setItems(namesCat);
}
  
 public void setData(User user) throws SQLException {
    currentUser=user;
    this.allUsers = allUsers=sql.getAllUsers();
    }
 
private void usernameSave() throws SQLException{
    if(!username.getText().equals(currentUser.getUsername())){
        
    if (allUsers.contains(username.getText())) {

            String tilte = "Username";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("This username is taken. Please select a new one");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        }
        currentUser.setUsername(username.getText());
        sql.updateUsername(currentUser.getUserID(),currentUser.getUsername());
    }
        
    
}

private void fnameSave() throws SQLException{
     if(!fname.getText().equals(currentUser.getFirstname())){
     if (User.matchName(fname.getText()) == true)
     {
         String tilte = "Name";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Name invalid.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
}
     else {
         currentUser.setFirstname(fname.getText());
         sql.updateFirstname(currentUser.getUserID(),currentUser.getFirstname());
     }
}
}


private void surnameSave() throws SQLException{
    if(!surname.getText().equals(currentUser.getSurname())){
     if (User.matchName(fname.getText()) == true)
     {
         String tilte = "Name";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Name invalid.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
}
     else {
         currentUser.setSurname(surname.getText());
         sql.updateSurname(currentUser.getUserID(),currentUser.getSurname());
     }
}
    
}

private void picSave() throws FileNotFoundException, SQLException, IOException{
         if (location != null){
         File image = new File (location);
         FileInputStream fis = new FileInputStream(image);
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         byte[] buf = new byte[1024];
         for (int readNum; (readNum=fis.read(buf))!=-1;){
             bos.write(buf,0,readNum);
         }
         photo=bos.toByteArray();
         sql.updateImage(photo,currentUser.getUserID());
}
}

private void emailSave() throws SQLException{
    if (!email.getText().equals(currentUser.getEmail())){
        if (User.isValid(email.getText()) == false) {
            String tilte = "Email";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Email Invalid");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        }
        else {
            
            currentUser.setEmail(email.getText());
            currentUser.setUniId(User.fetchUniId(currentUser.getEmail()));
            sql.updateEmail(currentUser.getUserID(),currentUser.getEmail());
            sql.updateUni(currentUser.getUserID(),currentUser.getUniId());
        }
    }
    
}

     
    private void catSave() throws SQLException{
    if (catId!=currentUser.getCatId()){
    currentUser.setCatId(catId);
    sql.updateCategory(currentUser.getCatId());
        }
}
    }
  
            

                
