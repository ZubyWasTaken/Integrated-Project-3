/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EditAcc;

/**
 *
 * @author stani
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import HomeTutor.*;
import ip3.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Zuby
 */
public class passwordEdit extends Application{

    User currentUser;

    public void start(Stage stage) throws Exception {
      
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("passwordEdit.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        PasswordEditController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();        
        stage.centerOnScreen();
    }
  
 public passwordEdit(User user) {
        currentUser=user;
    }

   
}