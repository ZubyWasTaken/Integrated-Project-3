/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileShare;


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
public class FileShare extends Application{
     User currentUser;

    public void start(Stage stage) throws Exception {
      
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fileShare.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        FileShareController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();        
        stage.centerOnScreen();
    }
  
 public FileShare(User user) {
        currentUser=user;
    }
}
