/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pages.Home;

import Pages.Login.Login;
import ip3.SwitchWindow;
import ip3.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Zuby
 */
public class homeController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button loginButton;

    @FXML
    private void loginButton(ActionEvent event) throws SQLException {
        String username = "zkhali";

       login(username);
    }
    
    public void login(String user) throws SQLException {
        User currentUser = new User(user);
        
        
            SwitchWindow.switchWindow((Stage) loginButton.getScene().getWindow(), new Login(currentUser));
      
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
