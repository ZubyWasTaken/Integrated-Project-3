/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interests;

import ip3.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author erino
 */
public class Interests extends Application {
        User currentUser;
        public void start(Stage stage) throws Exception {
      
        Parent root = FXMLLoader.load(getClass().getResource("Interests.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Interests");
        stage.show();        
        stage.centerOnScreen();
    }
     public Interests(User user) {
        currentUser = user;
    }
  
}