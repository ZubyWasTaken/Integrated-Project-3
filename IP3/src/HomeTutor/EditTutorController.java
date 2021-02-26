/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeTutor;

import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import ip3.SwitchWindow;
import ip3.User;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author stani
 */
public class EditTutorController implements Initializable {
User currentUser;
@FXML
private JFXTextField showLoc;
@FXML
private AnchorPane pane;
@FXML
private ImageView imageShow;
@FXML
private JFXButton backBut;
@FXML
private JFXButton saveBut;

SQLHandler sql=new SQLHandler();       
byte[] photo=null;
String filename=null;

 public void setData(User user) {
    currentUser=user;
    }
 @FXML
 private void upload(ActionEvent event){
  FileChooser fc = new FileChooser();
     fc.setTitle("Open File Dialog");
     Stage stage = (Stage)pane.getScene().getWindow();
     File file = fc.showOpenDialog(stage);
     if (file !=null)
     {
         showLoc.setText(file.getAbsolutePath());
         Image image = new Image (file.toURI().toString());
         imageShow.setImage(image);
             
     }
  
    }
 @FXML
 private void save (ActionEvent event) throws FileNotFoundException, IOException, SQLException{
     String location = showLoc.getText();
         File image = new File (location);
         FileInputStream fis = new FileInputStream(image);
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         byte[] buf = new byte[1024];
         for (int readNum; (readNum=fis.read(buf))!=-1;){
             bos.write(buf,0,readNum);
         }
         photo=bos.toByteArray();
         sql.updateImage(photo,currentUser.getUserID());
         System.out.println("Success");
         SwitchWindow.switchWindow((Stage) saveBut.getScene().getWindow(), new EditTutor(currentUser));   
 }
 @FXML 
 private void back(ActionEvent event){
     SwitchWindow.switchWindow((Stage) backBut.getScene().getWindow(), new HomeTutor(currentUser));   
 }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
    @Override
            public void run() {
    try {
       
        InputStream fs= sql.getImage(currentUser.getUserID());
        Image image = new Image(fs);
        imageShow.setImage(image);
    } catch (SQLException ex) {
        Logger.getLogger(EditTutorController.class.getName()).log(Level.SEVERE, null, ex);
    }
            }
    });
    
}
}
