/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author erino
 */
public class InterestController implements Initializable {

    @FXML
    private JFXDatePicker birthday;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField userEmail;

    @FXML
    private JFXTextField secondName;

    @FXML
    private JFXComboBox<?> uniChoice;

    @FXML
    private JFXButton finishReg;

    @FXML
    private void done(ActionEvent event) throws SQLException {
                System.out.println("hi");
        String first = firstName.getText().trim();
        String second = secondName.getText().trim();
        
        System.out.println(first);
        System.out.println(second);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}
