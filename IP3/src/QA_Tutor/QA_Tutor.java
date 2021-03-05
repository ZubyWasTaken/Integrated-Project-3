/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import HomeTutor.HomeTutorController;
import ip3.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author stani
 */
public class QA_Tutor extends Application{

    User currentUser;

    public void start(Stage stage) throws Exception {
      
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QA_Tutor.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        QA_TutorController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Q & A");
        stage.show();        
        stage.centerOnScreen();
    }
  
 public QA_Tutor(User user) {
        currentUser=user;
    }

   
}