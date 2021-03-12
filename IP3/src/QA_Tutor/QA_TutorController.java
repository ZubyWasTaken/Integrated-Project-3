/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import HomeTutor.HomeTutor;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.Drawer;
import ip3.Question;
import ip3.SwitchWindow;
import ip3.User;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author stani
 */
public class QA_TutorController implements Initializable {

    /**
     * Initializes the controller class.
     */
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


   
    ObservableList<Question> data = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
    User currentUser;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    public void setData(User user) throws SQLException {
    currentUser = user;
    
    sql.updateLastSeenQ(currentUser.getUserID(), now);

    }
  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
     @Override
            public void run() {
           
         Platform.runLater(new Runnable() {
    @Override
            public void run() {
            username.setText(currentUser.getUsername());
                drawer.setDisable(true);
                Drawer newdrawer = new Drawer();

                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                try {
                    data.clear();
                    feed.getItems().clear();
                    data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
                    data.forEach((_item) -> {
                        displayQs(_item);
                    });
               
            }
                catch (SQLException ex) {
                    Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
         });
    }


         
    },0,10000); 
                }
    
     /*private int getTablePos() {
        TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
        int index = pos.getRow();
        Question item = table.getItems().get(index);
        return item.getId();
    }
     */
    /* @FXML
     private void clickItem(MouseEvent event) {
    table.setOnMouseClicked((MouseEvent event1) -> {
        if(event1.getClickCount()==2){
            try {
               Question currentQuestion = Question.search(getTablePos());
               SwitchWindow.switchWindow((Stage) table.getScene().getWindow(), new ReplyTutor(currentQuestion,currentUser));
            } catch (SQLException | IOException ex) {
                Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
    });
    
}*/
     @FXML 
 private void goHome(ActionEvent event){

             SwitchWindow.switchWindow((Stage) btnHome.getScene().getWindow(), new HomeTutor(currentUser));
         }
 private void displayQs(Question question){
       
            TextFlow questText = new TextFlow();
            Text text = new Text(question.getText());

            text.setStyle("-fx-font: 16 arial;");
            questText.getChildren().add(text);

            HBox quest = new HBox();
           quest.setStyle("-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
            quest.setId(String.valueOf(question.getId()));
            // quest.setStyle("-fx-background-color: #b7d4cb;");
           HBox answers = new HBox();
           Button btn = new Button();
           btn.setPrefWidth(100);
           btn.setText("View all");

            answers.setMaxWidth(feed.getWidth() - 20);

            answers.setAlignment(Pos.BOTTOM_RIGHT);
            btn.setAlignment(Pos.CENTER_RIGHT);

           answers.getChildren().addAll(btn);

            quest.setMaxWidth(feed.getWidth() - 20);
            quest.getChildren().addAll(questText);

            feed.getItems().add(quest);

            feed.getItems().add(answers);
    }

@FXML
         private void clickItem (MouseEvent event){
             feed.setOnMouseClicked((MouseEvent event1) -> {
             if(event1.getClickCount()==2){
                 try{
                     HBox hbox =  (HBox) feed.getSelectionModel().selectedItemProperty().getValue();
                     int id = parseInt(hbox.getId());
                     Question currentQuestion = Question.search(id);  
                     SwitchWindow.switchWindow((Stage) feed.getScene().getWindow(), new ReplyTutor(currentQuestion,currentUser));
                 } catch (SQLException | IOException ex) {
                     Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         });
         }
 }

