/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EditAcc;

import Home.Home;
import HomeTutor.*;
import Interests.InterestController;
import LoginRegister.LoginRegister;
import QA_Tutor.QA_Tutor;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import ip3.Categories;
import ip3.Drawer;
import ip3.Hash;
import ip3.Shaker;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private JFXButton accTab;
    @FXML
    private JFXButton personalTab;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXPasswordField oldPass;
    @FXML
    private JFXPasswordField newPass1;
    @FXML
    private JFXPasswordField newPass2;
    @FXML
    private GridPane personalDet;
    @FXML
    private GridPane accDet;
    @FXML
    private JFXButton deleteBut;

    @FXML
    private JFXButton homeBut;

    SQLHandler sql = new SQLHandler();
    byte[] photo = null;
    String filename = null;
    String location;
    int catId;
    ArrayList<String> allUsers;
    ObservableList<Categories> data2 = FXCollections.observableArrayList();
    ObservableList<String> namesCat = FXCollections.observableArrayList();
    TrayNotification tray = new TrayNotification();
    AnimationType type = AnimationType.POPUP;
    Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    Hash h = new Hash();
    String newPassword;

    @FXML
    private void upload(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open File Dialog");
        Stage stage = (Stage) pane.getScene().getWindow();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            location = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString());
            imageShow.setImage(image);

        }

    }

    @FXML
    private void save(ActionEvent event) throws FileNotFoundException, IOException, SQLException {


        saveUsername();
        savePic();
        saveFirstName();
        saveSurname();
        saveCategory();
        savePassword();

        SwitchWindow.switchWindow((Stage) saveBut.getScene().getWindow(), new Edit(currentUser));
    }

    @FXML
    private void cancel(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will not be saved. Do you wish to proceed? ", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            SwitchWindow.switchWindow((Stage) cancelBut.getScene().getWindow(), new Edit(currentUser));

        }

    }

    @FXML
    private void goHome(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) homeBut.getScene().getWindow(), new QA_Tutor(currentUser));
    }

    @FXML
    private void returnToAcc(ActionEvent event) throws SQLException {
        personalDet.setVisible(false);
        accDet.setVisible(true);
        personalTab.setDisable(false);
        accTab.setDisable(true);
    }

    @FXML
    private void returnToPersonal(ActionEvent event) {
        personalDet.setVisible(true);
        accDet.setVisible(false);
        personalTab.setDisable(true);
        accTab.setDisable(false);

    }

    @FXML
    private void back(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will not be saved. Do you wish to proceed? ", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (currentUser.getTitleId() == 1) {
                SwitchWindow.switchWindow((Stage) backBut.getScene().getWindow(), new Home(currentUser));
            } else {
                SwitchWindow.switchWindow((Stage) backBut.getScene().getWindow(), new HomeTutor(currentUser));
            }
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
                    if (currentUser.getTitleId() != 1) {
                        drawer.setDisable(true);
                        hamburger.setVisible(false);
                        homeBut.setVisible(true);

                    } else {
                        hamburger.setVisible(true);
                        Drawer newdrawer = new Drawer();
                        drawer.setDisable(true);
                        newdrawer.drawerPullout(drawer, currentUser, hamburger);
                    }
                    username.setText(currentUser.getUsername());
                    accTab.setDisable(true);
                    InputStream fs = currentUser.getImage();
                    Image image = new Image(fs);

                    imageShow.setImage(image);
                    username.setText(currentUser.getUsername());
                    fname.setText(currentUser.getFirstname());
                    surname.setText(currentUser.getSurname());
                    email.setText(currentUser.getEmail());
                    catPopulate();
                    catSelect.getSelectionModel().select(currentUser.getCatId() - 1);
                    catId = currentUser.getCatId();

                } catch (SQLException ex) {
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

    @FXML
    private void deleteAccount(ActionEvent event) throws SQLException {
        delete();

    }

    private void delete() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirmation");
        dialog.setHeaderText("Are you sure? Your account and all related data will be deleted. Enter password to confirm.");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pwd = new PasswordField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Password:"), pwd);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Hash h = new Hash();
                if (h.verifyHash(pwd.getText(), currentUser.getPassword())) {
                    try {
                        sql.deleteAccount(currentUser.getUserID());
                        SwitchWindow.switchWindow((Stage) deleteBut.getScene().getWindow(), new LoginRegister());

                    } catch (SQLException ex) {
                        Logger.getLogger(EditController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    tray.setTitle("Password");
                    tray.setMessage("Password is incorrect. Please try again.");
                    tray.setNotificationType(NotificationType.ERROR);
                    tray.showAndDismiss(Duration.millis(3000));
                    delete();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

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

    public void setData(User user) throws SQLException {
        currentUser = user;
        this.allUsers = allUsers = sql.getAllUsers();
        tray.setAnimationType(type);
    }

    private void saveUsername() throws SQLException {

        if (username.getText().isEmpty()) {
            tray.setTitle("Username");
            tray.setMessage("Username is empty. Try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        } else {
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(username.getText());
            boolean b = m.find();

            if (b) {
                tray.setTitle("Username");
                tray.setMessage("Username has special characters. Try again.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));

            } else {
                //if input username is the same as the users current username
                // set it to itself, return false
                if (username.getText().equals(currentUser.getUsername())) {
                    currentUser.setUsername(username.getText());
                    sql.updateUsername(currentUser.getUserID(), currentUser.getUsername());
                    tray.setTitle("Success");
                    tray.setMessage("All changes are successfully saved.");
                    tray.setNotificationType(NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(3000));
                } else {
                    // if user table contains input username
                    // return true

                    //otherwise set username and return false
                    if (allUsers.contains(username.getText())) {
                        tray.setTitle("Username");
                        tray.setMessage("Username already exists. Try again.");
                        tray.setNotificationType(NotificationType.ERROR);
                        tray.showAndDismiss(Duration.millis(3000));
                    } else {
                        currentUser.setUsername(username.getText());
                        sql.updateUsername(currentUser.getUserID(), currentUser.getUsername());
                    }
                }
            }
        }
    }

    private void savePic() throws FileNotFoundException, SQLException, IOException {
        if (location != null) {
            File image = new File(location);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
            }
            photo = bos.toByteArray();
            sql.updateImage(photo, currentUser.getUserID());
            tray.setTitle("Success");
            tray.setMessage("All changes are successfully saved.");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(3000));
        }
    }


    private void saveFirstName() throws SQLException {
        if (fname.getText().isEmpty()) {
            tray.setTitle("First Name");
            tray.setMessage("First name is empty. Try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        } else {

            Matcher m = p.matcher(fname.getText());
            boolean b = m.find();

            if (b) {
                tray.setTitle("First Name");
                tray.setMessage("First name has special characters. Try again.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
            } else {
                currentUser.setFirstname(fname.getText());
                sql.updateFirstname(currentUser.getUserID(), currentUser.getFirstname());
                tray.setTitle("Success");
                tray.setMessage("All changes are successfully saved.");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(3000));
            }
        }
    }

    private void saveSurname() throws SQLException {
        if (surname.getText().isEmpty()) {
            tray.setTitle("First Name");
            tray.setMessage("Surname is empty. Try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        } else {
            Matcher m = p.matcher(surname.getText());
            boolean b = m.find();

            if (b) {
                tray.setTitle("First Name");
                tray.setMessage("Surname has special characters. Try again.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
            } else {
                currentUser.setSurname(surname.getText());
                sql.updateSurname(currentUser.getUserID(), currentUser.getSurname());
                tray.setTitle("Success");
                tray.setMessage("All changes are successfully saved.");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(3000));

            }
        }
    }

    private void saveCategory() throws SQLException {
        if (catId != currentUser.getCatId()) {
            currentUser.setCatId(catId);
            sql.updateCategory(currentUser.getCatId());
            tray.setTitle("Success");
            tray.setMessage("All changes are successfully saved.");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(3000));
        }
    }

    private void savePassword() throws SQLException {

        if (oldPass.getText().isEmpty()) {
            changePasswordFailed();
            tray.setTitle("Password");
            tray.setMessage("Password field is empty. Try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        }

        if (currentUser.getPassword().equals(newPass1.getText())) {
            tray.setTitle("Password");
            tray.setMessage("New password can't be same as old. Try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        }
        if(currentUser.getPassword().equals(newPass2.getText())){
            tray.setTitle("Password");
            tray.setMessage("New password can't be same as old. Try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        }

        if (h.verifyHash(oldPass.getText(), currentUser.getPassword())) {


            if (newPass1.getText().equals(newPass2.getText())) {
                if (newPass2.getText().length() < 8 || newPass2.getText().length() > 32) {
                    tray.setTitle("Password");
                    tray.setMessage("Password must be between 8-32 characters");
                    tray.setNotificationType(NotificationType.ERROR);
                    tray.showAndDismiss(Duration.millis(3000));
                } else {
                    newPassword = h.hash(newPass1.getText());
                    currentUser.setPassword(newPassword);
                    currentUser.editPassword(currentUser);
                    tray.setTitle("Success");
                    tray.setMessage("All changes are successfully saved.");
                    tray.setNotificationType(NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(3000));
                }
            } else {
                tray.setTitle("Password");
                tray.setMessage("New passwords don't match. Try again.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
            }
        } else {
            tray.setTitle("Password");
            tray.setMessage("Old password entered incorrect. Try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        }

    }

    private void changePasswordFailed() {
        Shaker shake = new Shaker(saveBut);
        shake.shake();
        oldPass.requestFocus();
    }
}
