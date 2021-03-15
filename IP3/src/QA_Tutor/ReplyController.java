/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import ip3.Drawer;
import ip3.Question;
import ip3.SwitchWindow;
import ip3.User;
import ip3.Reply;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
public class ReplyController implements Initializable {

    @FXML
    private JFXTextArea replyText;
    @FXML
    private JFXButton sendBut;
     @FXML
    private TableView<Reply> table;
     @FXML
    private TableColumn<Reply, String> col_reply;
    @FXML
    private TableColumn<Reply, String> col_author;
    @FXML
    private JFXButton removeBut;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    
    ObservableList<Reply> data = FXCollections.observableArrayList();
    Question currentQuestion;
    User currentUser;
    SQLHandler sql = new SQLHandler();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Platform.runLater(new Runnable() {
    @Override
            public void run() {
            removeBut.setVisible(false);
            try {
                Drawer newdrawer = new Drawer();
                drawer.setDisable(true);
                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                
                data = sql.showReplies(currentQuestion.getId());
            } catch (SQLException ex) {
            Logger.getLogger(ReplyController.class.getName()).log(Level.SEVERE, null, ex);
            }
     
        
             col_reply.setCellValueFactory(new PropertyValueFactory<>("text"));
             col_author.setCellValueFactory(new PropertyValueFactory<>("sender"));
             table.setItems(data);
                
         
    }   
         });
    }
    
    void setData(Question currentQuestion, User currentUser) {
        this.currentQuestion=currentQuestion;
        this.currentUser=currentUser;
    }
    
    private int getTablePos() {
        TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
        int index = pos.getRow();
        Reply item = table.getItems().get(index);
        return item.getId();
    }
     
    
    @FXML
    private void send (ActionEvent event) throws SQLException{
        String reply = replyText.getText();
        if (reply.isEmpty()){
            String tilte = "Reply";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("You have not entered a reply");
            tray.setNotificationType(NotificationType.INFORMATION);
            tray.showAndDismiss(Duration.millis(3000));
        }
        else {
        
        sql.addReply(reply, currentQuestion.getId(),currentUser.getUserID());
        SwitchWindow.switchWindow((Stage) sendBut.getScene().getWindow(), new ReplyTutor(currentQuestion,currentUser));
        }
    }
    
    @FXML
    private void remove (ActionEvent event) throws SQLException, IOException {
         Reply currentReply = Reply.search(getTablePos());
         sql.deleteReply(currentReply.getId());
         SwitchWindow.switchWindow((Stage) table.getScene().getWindow(), new ReplyTutor(currentQuestion,currentUser));
    }
    
    @FXML
    private void clickItem(MouseEvent event) {
    table.setOnMouseClicked((MouseEvent event1) -> {
            try {
               Reply currentReply = Reply.search(getTablePos());
               User sender = new User(currentReply.getSender());
               if (sender.getUserID()==currentUser.getUserID()){
                  removeBut.setVisible(true); 
               }
               else {
                   removeBut.setVisible(false);
               }
              /* SwitchWindow.switchWindow((Stage) table.getScene().getWindow(), new ReplyTutor(currentQuestion,currentUser));*/
            } catch (SQLException | IOException ex) {
                Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
            }
       
       
    });
    
     }
}
