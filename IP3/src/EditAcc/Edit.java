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
public class Edit extends Application{

    User currentUser;

    public void start(Stage stage) throws Exception {
      
            stage.getIcons().add(new Image("/Resources/icon.png"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        EditController controller = loader.getController();
        controller.setData(currentUser);
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();        
        stage.centerOnScreen();
    }
  
 public Edit(User user) {
        currentUser=user;
    }

   
}
