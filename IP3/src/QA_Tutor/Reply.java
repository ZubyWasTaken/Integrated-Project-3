/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import ip3.Question;
import ip3.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author stani
 */
public class Reply extends Application {

    Question currentQuestion;
    User currentUser;
    Reply(Question currentQuestion, User currentUser) {
        this.currentQuestion=currentQuestion;
        this.currentUser=currentUser;
    }

    @Override
    public void start(Stage stage) throws Exception {
   
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reply.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        ReplyController controller = loader.getController();
        controller.setData(currentQuestion, currentUser);
        stage.setScene(scene);
        stage.setTitle("Q & A");
        stage.show();        
        stage.centerOnScreen();
    }
    }

   
    

