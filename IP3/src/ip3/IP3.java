/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Zuby
 */
public class IP3 extends Application {
    
    @Override
    public void start(Stage primaryStage) throws SQLException, IOException {
        primaryStage.getIcons().add(new Image("/Resources/icon.png"));
        Parent root = FXMLLoader.load(getClass().getResource("/LoginRegister/LoginRegister.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}


