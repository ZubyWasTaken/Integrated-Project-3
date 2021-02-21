/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import ip3.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Zuby
 */
public class Home {

    User user;

  

    public Home(User currentUser) {
           user = currentUser;
    }

   

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
        stage.centerOnScreen();
    }

   
}
