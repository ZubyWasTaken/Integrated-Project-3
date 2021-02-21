/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interests;

import SQL.SQLHandler;
import ip3.Shaker;
import ip3.Uni;
import ip3.User;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    User currentUser;
    @FXML
    TextField getfname;
    @FXML
    TextField getsurname;
    @FXML
    TextField getemail;
    @FXML
    DatePicker getdob;
    @FXML
    Button registerBut;
    @FXML
    ChoiceBox uniSelect;
    LocalDate date = LocalDate.now();
    ObservableList<Uni> data = FXCollections.observableArrayList();
    ObservableList<String> names = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
    public void setData(User user) {
        currentUser = user;

    }
    @FXML
    private void register(MouseEvent event) throws SQLException, ParseException {
        String firstname, surname, username, password, email, dob;
        int uniId;
        
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
         else
         {
             String tempcat = (String) uniSelect.getSelectionModel().getSelectedItem();
        uniId = Uni.fetchUniId(tempcat);   
        User.createUser(username, password, firstname, surname, dob, email, uniId);
        System.out.println("Registered Successfully");
         }
}
    private void registerFailed() {
        Shaker shake = new Shaker(registerBut);
        shake.shake();
        getfname.requestFocus();
        //regusername.getStyleClass().add("wrong");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    DatePicker maxDate = new DatePicker(); // DatePicker, used to define max date available, you can also create another for minimum date
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
            
        try {
            data = sql.showUniversities();
        } catch (SQLException ex) {
            Logger.getLogger(InterestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Uni d:data)
        {
            
           
            names.add(d.getName());
        }
        uniSelect.setItems(names);
        }
    }
   

