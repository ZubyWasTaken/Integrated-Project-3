/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserQNA;

import Home.Home;
import LoginRegister.LoginRegister;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.SwitchWindow;
import ip3.User;
import ip3.Drawer;
import SQL.SQLHandler;
import ip3.Question;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Zuby
 */
public class UserQNAController implements Initializable {

    User currentUser;
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;



    @FXML
    private Button btnHome;

    @FXML
    private Label username;

    @FXML
    private ListView feed;

    @FXML
    private Button msgBtn;

    @FXML
    private TextArea msgArea;
    
    Timestamp now = new Timestamp(System.currentTimeMillis());
    ObservableList<Question> data = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();

    @FXML
    private void goHome(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) btnHome.getScene().getWindow(), new Home(currentUser));
    }

    @FXML
    private void sendMsg(ActionEvent event) throws SQLException {

     String typeQuest = msgArea.getText().trim();
        if (typeQuest.equals("")) {
            System.out.println("nothing");
    }
            else {
             msgArea.clear();
            Question.createQuestion(typeQuest, currentUser.getUserID(), now);
             SwitchWindow.switchWindow((Stage) msgBtn.getScene().getWindow(), new UserQNA(currentUser));

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

        final int MAX_CHARS = 280;

        msgArea.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> change.getControlNewText().length() <= MAX_CHARS ? change : null));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                username.setText(currentUser.getUsername());
                drawer.setDisable(true);
                Drawer newdrawer = new Drawer();

                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                try {
                    data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
                    data.forEach((_item) -> {
                        displayQs(_item);
                    });

                } catch (SQLException ex) {
                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                } 

            }
        });
    }

    public void setData(User user) throws SQLException {
        currentUser = user;
        sql.updateLastSeenQ(currentUser.getUserID(), now);

    }
     private void displayQs(Question question){
       
            TextFlow questText = new TextFlow();
            Text text = new Text(question.getText());

            text.setStyle("-fx-font: 16 arial;");
            questText.getChildren().add(text);

            HBox quest = new HBox();

            // quest.setStyle("-fx-background-color: #b7d4cb;");
            HBox answers = new HBox();
            Button btn = new Button();
            // btn.setPrefWidth(100);
            btn.setText("View all");

            answers.setMaxWidth(feed.getWidth() - 20);

            answers.setAlignment(Pos.BOTTOM_RIGHT);
            btn.setAlignment(Pos.CENTER_RIGHT);

            answers.getChildren().addAll(btn);

            quest.setMaxWidth(feed.getWidth() - 20);

            quest.setAlignment(Pos.TOP_LEFT);

            quest.getChildren().addAll(questText);

            feed.getItems().add(quest);

            feed.getItems().add(answers);
    }
}

 

