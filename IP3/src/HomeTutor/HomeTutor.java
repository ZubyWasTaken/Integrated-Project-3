/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeTutor;

/**
 *
 * @author stani
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import UserQNA.UserQNA;
import ip3.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zuby
 */
public class HomeTutor extends Application{

    User currentUser;

    public void start(Stage stage) throws Exception {
      
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeTutor.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        HomeTutorController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();        
        stage.centerOnScreen();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                try {
                    currentUser.updateLogin(false);
                } catch (SQLException ex) {
                    Logger.getLogger(UserQNA.class.getName()).log(Level.SEVERE, null, ex);
                }
                Platform.exit();
                System.exit(0);
            }
        });
    }
  
 public HomeTutor(User user) {
        currentUser=user;
    }

   
}
