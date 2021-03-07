/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Chat.Chat;

import Interests.Interests;
import LoginRegister.LoginRegister;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.SwitchWindow;
import ip3.User;
import ip3.Drawer;
import Home.drawerController;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXListView;
import ip3.Question;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Zuby
 */
public class homeController implements Initializable {
    User currentUser;
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label labelWelcome;

    @FXML
    private Button sgnOutBut;
    
     
    @FXML
    private Label username;
    
      @FXML
    private ListView feed;

    @FXML
    private Button msgBtn;

 @FXML
    private TextArea msgArea;
 
   ObservableList<Question> data = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();

    @FXML
    private void signOut(ActionEvent event){
        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister()); 
    }
    
    @FXML
    private void  sendMsg(ActionEvent event){
        
    String typeQuest = msgArea.getText().trim();
       if (typeQuest.equals("")){
      System.out.println("nothing");
             
       }
       else{
           
           
         
             
        msgArea.clear();
    TextFlow questText = new TextFlow();
              Text text = new Text(typeQuest);
              
 text.setStyle("-fx-font: 16 arial;");
     questText.getChildren().add(text);
               
              
              HBox quest = new HBox();
             
         // quest.setStyle("-fx-background-color: #b7d4cb;");

              HBox answers = new HBox();
           Button btn = new Button();
          // btn.setPrefWidth(100);
           btn.setText("Replies");
           
           
                answers.setMaxWidth(200);
               
                answers.setAlignment(Pos.CENTER_RIGHT);
                btn.setAlignment(Pos.CENTER_RIGHT);
                
                answers.getChildren().addAll(btn);
                
                quest.setMaxWidth(feed.getWidth() - 20);
               
                quest.setAlignment(Pos.TOP_LEFT);
               
              quest.getChildren().addAll(questText);
              
              
               feed.getItems().add(quest);
           
             feed.getItems().add(answers);
             
       }
               
    }
    
    
//    public synchronized void  addFeed(){
//         Task<HBox> yourMessages = new Task<HBox>() {
//            @Override
//            public HBox call() throws Exception {
//             
//              Label quest = new Label();
//               
//                    quest.setText("This my question g");
//                  
//               
//                HBox hbox = new HBox();
//                hbox.setMaxWidth(feed.getWidth() - 20);
//                hbox.setAlignment(Pos.TOP_RIGHT);
//               
//                hbox.getChildren().addAll(quest);
//                return hbox;
//             }
//      
//           
//      
//       
//
//     };
//                yourMessages.setOnSucceeded(event -> feed.getItems().add(yourMessages.getValue()));
//        
//    }
    
    
    
    
             
//    @FXML
//    private void viewChats(ActionEvent event){
//       
//        SwitchWindow.switchWindow((Stage) chat.getScene().getWindow(), new Chat(currentUser)); 
//    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
   final int MAX_CHARS = 280 ;

      

        msgArea.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= MAX_CHARS ? change : null));
        
         Platform.runLater(new Runnable() {
    @Override
            public void run() {

        username.setText(currentUser.getUsername());

        Drawer newdrawer = new Drawer();

        newdrawer.drawerPullout(drawer, currentUser, hamburger);

    }
         });
    }

    public void setData(User user) {
        currentUser = user;


    }
}





