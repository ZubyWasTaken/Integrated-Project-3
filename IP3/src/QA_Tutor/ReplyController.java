/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import ip3.Question;
import ip3.SwitchWindow;
import ip3.User;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
        try {
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
    
    @FXML
    private void send (ActionEvent event) throws SQLException{
        String reply = replyText.getText();
        sql.addReply(reply, currentQuestion.getId(),currentUser.getUserID());
        SwitchWindow.switchWindow((Stage) sendBut.getScene().getWindow(), new Reply(currentQuestion,currentUser));
    }
    
}
