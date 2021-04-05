/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Interests.InterestController;
import ip3.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Zuby
 */
public class Home extends Application{

    User currentUser;

    public void start(Stage stage) throws Exception {
      
            stage.getIcons().add(new Image("/Resources/icon.png"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeScreen.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        homeController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();        
        stage.centerOnScreen();
    }
  
 public Home(User user) {
        currentUser=user;
    }

   
}
