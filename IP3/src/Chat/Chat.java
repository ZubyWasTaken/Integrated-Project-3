/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import ip3.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

/**
 *
 * @author erino
 */
public class Chat extends Application {
        User currentUser;
       
        public void start(Stage stage) throws Exception {
      
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        ChatController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Chat");
        stage.show();        
        stage.centerOnScreen();
        
         
    }
        
     public Chat(User user) {
        currentUser = user;
    }
  
}
   




