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
    AnchorPane pane;
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
    JFXTextField locImg;

    byte[] photo = null;
    File image;

    //Variables//
    LocalDate date = LocalDate.now();
    ObservableList<Categories> data2 = FXCollections.observableArrayList();
    ObservableList<String> namesCat = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
    String firstname, surname, username, password, email, userDob;
    LocalDate dob;
    int uniId;
    int catId;
    int title_id = 1;
    User currentUser;
    TrayNotification tray = new TrayNotification();
    AnimationType type = AnimationType.POPUP;

    public void setData(User user) {
        currentUser = user;
        tray.setAnimationType(type);
    }

    @FXML
    private void register(ActionEvent event) throws SQLException, ParseException, IOException {

        firstname = getfname.getText().trim();

        surname = getsurname.getText().trim();

        username = currentUser.getUsername().trim();
        password = currentUser.getPassword().trim();
        email = getemail.getText().trim();

        dob = getdob.getValue();
        if (dob != null) {
            userDob = dob.toString();
        } else {
            userDob = "";
        }
//        } else {
//            tray.setAnimationType(type);
//            tray.setTitle("Register");
//            tray.setMessage("Enter a date");
//            tray.setNotificationType(NotificationType.ERROR);
//            tray.showAndDismiss(Duration.millis(3000));
//
//            registerFailed();
//            return;
//        }
//        

        if (userDob.isEmpty() & dob == null & firstname.isEmpty() & surname.isEmpty() & email.isEmpty()) {

            tray.setAnimationType(type);
            tray.setTitle("Register");
            tray.setMessage("Please enter all details");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        } else if (firstname.isEmpty()) {

            tray.setAnimationType(type);
            tray.setTitle("Register");
            tray.setMessage("Please enter first name.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        } else if (surname.isEmpty()) {

            tray.setAnimationType(type);
            tray.setTitle("Register");
            tray.setMessage("Please enter surname.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        } else if (User.isValid(email) == false) {

            tray.setAnimationType(type);
            tray.setTitle("Register");
            tray.setMessage("Email Invalid");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        } else if (userDob.isEmpty()) {
            tray.setAnimationType(type);
            tray.setTitle("Register");
            tray.setMessage("Date of birth empty.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        } else if (catId == 0) {

            tray.setAnimationType(type);
            tray.setTitle("Register");
            tray.setMessage("Please select an interest");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;

        } else if (User.matchName(firstname) == true || User.matchName(surname) == true) {

            tray.setTitle("Register");
            tray.setMessage("Name invalid.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            registerFailed();
            return;
        } else {
            uniId = User.fetchUniId(email);
            if (uniId == 0) {

                tray.setTitle("Register");
                tray.setMessage("Please use a valid university email");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
                registerFailed();

            } else {
                String newSurn = surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase();
                String newfirst = firstname.substring(0, 1).toUpperCase() + firstname.substring(1).toLowerCase();
                String newEmail = email.toLowerCase();
                User.createUser(username, password, newfirst, newSurn, userDob, newEmail, uniId, catId, title_id);

                tray.setTitle("Register");
                tray.setMessage("Welcome to StudyBudz, " + username + "!");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(3000));
                User user = new User(username);
                setImage(username);
                SwitchWindow.switchWindow((Stage) registerBut.getScene().getWindow(), new Home(user));
            }
        }
    }

    private void registerFailed() {
        Shaker shake = new Shaker(registerBut);
        shake.shake();
        getfname.requestFocus();
    }

//    private void setImage(String username) throws FileNotFoundException, SQLException, IOException {
//        User user = new User(username);
//        File image = new File("src/SQL/files/noPic.png");
//        Path path = Paths.get("src/SQL/files/noPic.png");
//        byte[] photo = Files.readAllBytes(path);
//        sql.addImage(photo, user.getUserID());
//    }
    @FXML
    private void cancel(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) cancelBut.getScene().getWindow(), new LoginRegister());
    }

    //Adding categories for selection
    private void catPopulate() {
        try {
            data2 = sql.showCategories();
        } catch (SQLException ex) {
            Logger.getLogger(InterestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Categories c : data2) {
            namesCat.add(c.getName());
        }
        catSelect.setItems(namesCat);
    }

    private void setImage(String username) throws FileNotFoundException, SQLException, IOException {
        User user = new User(username);

        if (locImg.getText().isEmpty()) {
            image = new File("src/Resources/noPic.png");

        } else {
            image = new File(locImg.getText());

        }
        FileInputStream fis = new FileInputStream(image);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }
        photo = bos.toByteArray();
        sql.addImage(photo, user.getUserID());
    }

    @FXML
    private void upload(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open File Dialog");
        Stage stage = (Stage) pane.getScene().getWindow();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            String location = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString());
            locImg.setText(location);

        }
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

    }

}
