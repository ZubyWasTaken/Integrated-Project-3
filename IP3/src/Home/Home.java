/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Interests.InterestController;
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
 * @author Zuby
 */
public class Home extends Application {

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

    public Home(User user) {
        currentUser = user;
    }


}
