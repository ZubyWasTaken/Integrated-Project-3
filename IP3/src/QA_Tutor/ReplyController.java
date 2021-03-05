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
import ip3.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    
    Question currentQuestion;
    User currentUser;
    SQLHandler sql = new SQLHandler();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setData(Question currentQuestion, User currentUser) {
        this.currentQuestion=currentQuestion;
        this.currentUser=currentUser;
    }
    
    @FXML
    private void send (ActionEvent event) throws SQLException{
        String reply = replyText.getText();
        sql.addReply(reply, currentQuestion.getId(),currentUser.getUserID());
    }
    
}
