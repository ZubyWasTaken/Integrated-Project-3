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
public class HomeTutor extends Application{

    User currentUser;

    public void start(Stage stage) throws Exception {
      
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeTutor.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        HomeTutorController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("UserQNA");
        stage.show();        
        stage.centerOnScreen();
    }
  
 public HomeTutor(User user) {
        currentUser=user;
    }

   
}
