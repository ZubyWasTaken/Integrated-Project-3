/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interests;

import Home.Home;
import LoginRegister.LoginRegister;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import ip3.Categories;
import ip3.Shaker;
import ip3.User;
import ip3.SwitchWindow;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import net.synedra.validatorfx.Validator;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author erino
 */
public class InterestController implements Initializable {
    //FXML//
    @FXML
    JFXTextField getfname;
    @FXML
    JFXTextField getsurname;
    @FXML
    JFXTextField getemail;
    @FXML
    JFXDatePicker getdob;
    @FXML
    JFXButton registerBut;
    @FXML
    JFXComboBox catSelect;
    @FXML
    JFXButton cancelBut;
    @FXML
    AnchorPane pane;
    @FXML
    JFXTextField locImg;
    
    
    //Variables//
    LocalDate date = LocalDate.now();
    ObservableList<Categories> data2 = FXCollections.observableArrayList();
    ObservableList<String> namesCat = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
    String firstname, surname, username, password, email, dob;
    int uniId;
    int catId;
    int title_id = 1;
    User currentUser;
    TrayNotification tray = new TrayNotification();
    AnimationType type = AnimationType.POPUP;
    byte[] photo=null;
    private Validator validator = new Validator();
    
    public void setData(User user) {
    currentUser = user;
    tray.setAnimationType(type);
    }
    
    @FXML
    private void upload(ActionEvent event){
    FileChooser fc = new FileChooser();
    fc.setTitle("Open File Dialog");
    Stage stage = (Stage)pane.getScene().getWindow();
    File file = fc.showOpenDialog(stage);
    if (file !=null)
    {
        String location=file.getAbsolutePath();
        Image image = new Image (file.toURI().toString());
        locImg.setText(location);
             
    }
    }
    
    private void register() throws SQLException, ParseException, IOException {
            uniId=User.fetchUniId(email);
            User.createUser(currentUser.getUsername(), currentUser.getPassword(), firstname, surname, dob, email, uniId, catId, title_id);
        
            tray.setTitle("Register");
            tray.setMessage("Welcome to StudyBudz, " + username + "!");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(3000));  
            User user = new User(currentUser.getUsername()); 
            setImage(currentUser.getUsername());
            SwitchWindow.switchWindow((Stage) registerBut.getScene().getWindow(), new Home(user)); 
            }
         
private void setImage(String username) throws FileNotFoundException, SQLException, IOException{
        User user = new User(username); 
        
         File image = new File (locImg.getText());
         FileInputStream fis = new FileInputStream(image);
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         byte[] buf = new byte[1024];
         for (int readNum; (readNum=fis.read(buf))!=-1;){
             bos.write(buf,0,readNum);
         }
         photo=bos.toByteArray();
         sql.addImage(photo,user.getUserID());


}
    
    @FXML
    private void cancel(ActionEvent event){
        SwitchWindow.switchWindow((Stage) cancelBut.getScene().getWindow(), new LoginRegister()); 
    }
  
    
    //Adding categories for selection
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
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    DatePicker maxDate = new DatePicker(); // DatePicker, used to define max date available
    maxDate.setValue(date); // Max date available will be now
    final Callback<DatePicker, DateCell> dayCellFactory;

    dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (item.isAfter(maxDate.getValue())) { //Disable all dates after required date
            setDisable(true);
            getStyleClass().add("disabled-calendar"); //To set background on different color
        }
    }
    
};
        // update DatePicker cell factory
       getdob.setDayCellFactory(dayCellFactory);
       catPopulate();
       
       //Getting the id for the selected cateogry
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
        registerBut.disableProperty().bind(validator.containsErrorsProperty());
        registerBut.disableProperty().bind(validator.containsWarningsProperty());
        validator.createCheck()
                   .dependsOn("fname",getfname.textProperty())
                   .withMethod(c->{
                       firstname = c.get("fname");
                       if (firstname.isEmpty()){
                          c.warn("Please enter a first name.");
                       }
                       else {                      
                        if ((User.matchName(firstname) == true)) {
                            c.error("Please, enter a valid name.");
                        }
                        else{
                           firstname = firstname.substring(0, 1).toUpperCase() + firstname.substring(1).toLowerCase(); 
                        }
                       }
                        
                   })
                   .decorates(getfname)
                   .immediate();
        validator.createCheck()
                   .dependsOn("surname",getsurname.textProperty())
                   .withMethod(c->{
                       surname = c.get("surname");
                       if (surname.isEmpty()){
                          c.warn("Please enter a surname.");
                       }
                       else {                      
                        if ((User.matchName(surname) == true)) {
                            c.error("Please, enter a valid name.");
                        }
                        else{
                           surname = surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase(); 
                        }
                       }
                        
                   })
                   .decorates(getsurname)
                   .immediate();
        validator.createCheck()
                   .dependsOn("email",getemail.textProperty())
                   .withMethod(c->{
                       email = c.get("email");
                       if (email.isEmpty()){
                          c.warn("Please enter an email.");
                       }
                       else {                      
                        if ((User.isValid(email) == false)) {
                            c.error("Please, enter a valid email.");
                        }
                        else{
                           email = email.substring(0, 1).toUpperCase() + email.substring(1).toLowerCase();; 
                        }
                       }
                        
                   })
                   .decorates(getemail)
                   .immediate();
         validator.createCheck()
                   .dependsOn("dob",getdob.dayCellFactoryProperty())
                   .withMethod(c->{
                       dob = c.get("dob").toString();
                       if (dob.isEmpty()){
                          c.warn("Please select a date of birth.");
                       }
                       
                   })
                   .decorates(getdob)
                   .immediate();
    
    validator.createCheck()
                   .dependsOn("pic",locImg.textProperty())
                   .withMethod(c->{
                       String loc = c.get("pic");
                       if (loc.isEmpty()){
                          c.warn("Please select a picture.");
                       }
                   })
                   .decorates(locImg)
                   .immediate();
    
    validator.createCheck()
                   .dependsOn("cat",catSelect.valueProperty())
                   .withMethod(c->{
                       if (catId==0){
                          c.warn("Please select a category.");
                       }
                   })
                   .decorates(catSelect)
                   .immediate();
        registerBut.setOnAction(e -> {
        try {
            register();
        } catch (SQLException | ParseException | IOException ex) {
            Logger.getLogger(InterestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });
    }


}

   

