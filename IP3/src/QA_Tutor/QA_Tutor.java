/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import UserQNA.UserQNA;
import ip3.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stani
 */
public class QA_Tutor extends Application{

    User currentUser;

    
  
    
    public void start(Stage stage) throws Exception {
          stage.getIcons().add(new Image("/Resources/icon.png"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QA_Tutor.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        QA_TutorController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Q & A");
        stage.show();        
        stage.centerOnScreen();
        root.requestFocus();
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
  
 public QA_Tutor(User user) {
        currentUser=user;
    }

   
}
