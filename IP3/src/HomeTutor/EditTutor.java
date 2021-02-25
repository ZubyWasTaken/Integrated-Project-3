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


import Home.homeController;
import Interests.InterestController;
import ip3.Tutor;
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
public class EditTutor extends Application{

    Tutor currentTutor;

    public void start(Stage stage) throws Exception {
      
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editTutor.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        EditTutorController controller = loader.getController();
        controller.setData(currentTutor);
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();        
        stage.centerOnScreen();
    }
  
 public EditTutor(Tutor tutor) {
        currentTutor=tutor;
    }

   
}
