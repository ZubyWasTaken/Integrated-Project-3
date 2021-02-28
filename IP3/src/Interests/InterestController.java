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
import ip3.Uni;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
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
    
    //Variables//
    LocalDate date = LocalDate.now();
    ObservableList<Uni> data = FXCollections.observableArrayList();
    ObservableList<Categories> data2 = FXCollections.observableArrayList();
    ObservableList<String> names = FXCollections.observableArrayList();
    ObservableList<String> namesCat = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
     String firstname, surname, username, password, email, dob;
    int uniId;
    int catId;
    int title_id = 1;
    User currentUser;
    
    public void setData(User user) {
    currentUser = user;
    }
    
    @FXML
    private void register(ActionEvent event) throws SQLException, ParseException, IOException {
 
        
        firstname = getfname.getText();
        firstname = firstname.substring(0, 1).toUpperCase() + firstname.substring(1).toLowerCase();
        surname = getsurname.getText();
        surname = surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase();
        username = currentUser.getUsername();
        password = currentUser.getPassword();
        email = getemail.getText();
        email = email.substring(0, 1).toUpperCase() + email.substring(1).toLowerCase();
        dob = getdob.getValue().toString();
        
           

         if (User.isValid(email) == false) {
            String tilte = "Register";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Email Invalid");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        }

          if (User.matchName(firstname) == true || User.matchName(surname) == true  ) {
            String tilte = "Register";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Name invalid.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        }
        if(dob.isEmpty() || firstname.isEmpty() || surname.isEmpty() || email.isEmpty() ){
            String tilte = "Register";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("Please enter all details");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;
        }
       
         else
         {
            uniId=User.getUniId(email);
            User.createUser(username, password, firstname, surname, dob, email, uniId, catId, title_id);
            String message = username;
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle("Register");
            tray.setMessage("Welcome to StudyBudz, " + message + "!");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(3000));
            User user = new User(username);
            setImage(username);
             SwitchWindow.switchWindow((Stage) registerBut.getScene().getWindow(), new Home(user)); 
         }
}
    private void registerFailed() {
        Shaker shake = new Shaker(registerBut);
        shake.shake();
        getfname.requestFocus();
    }
    
    private void setImage(String username) throws FileNotFoundException, SQLException, IOException{
        User user = new User(username);
        File image = new File ("C:\\Users\\stani\\Desktop\\Integrated-Project-3\\IP3\\src\\SQL\\files\\noPic.png");
         FileInputStream fis = new FileInputStream(image);
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         byte[] buf = new byte[1024];
         for (int readNum; (readNum=fis.read(buf))!=-1;){
             bos.write(buf,0,readNum);
         }
         byte[] photo=bos.toByteArray();
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
            setStyle("-fx-background-color: #ffc0cb;"); //To set background on different color
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

    
    }
    
}

   

