/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import HomeTutor.HomeTutor;
import SQL.SQLHandler;
import UserQNA.UserQNAController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.Drawer;
import ip3.Question;
import ip3.Reply;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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

    @FXML
    private TextArea replyArea;

    @FXML
    private AnchorPane repliesPane;

    @FXML
    private TextFlow repliesQ;
    
    @FXML
    private ListView repliesView;
   
    ObservableList<Question> data = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
    User currentUser;
    public int questionid;
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
                
                
                drawer.setDisable(true);
                Drawer newdrawer = new Drawer();
                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                
                try {
                    data.clear();
                    feed.getItems().clear();
                    data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
                    data.forEach((_item) -> {
                        try {
                            displayQs(_item);
                        } catch (SQLException ex) {
                            Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
    private void close(ActionEvent event) {

        repliesPane.setVisible(false);
       
    }
    
     @FXML 
 private void goHome(ActionEvent event){

             SwitchWindow.switchWindow((Stage) btnHome.getScene().getWindow(), new HomeTutor(currentUser));
         }
  private void displayQs(Question question) throws SQLException {
  int replyCount = sql.countAllReplies(question.getId());
        //  feed.setMouseTransparent(true);
        feed.setFocusTraversable(false);
        
        TextFlow questText = new TextFlow();
        Text text = new Text(question.getText());

        text.setStyle("-fx-font: 16 arial;");
        questText.getChildren().add(text);

        int questId = question.getId();
        String usersId =  question.getSender();
        
        
        
        String questionId = String.valueOf(questId);

        HBox quest = new HBox();

        Button btn = new Button();

        // quest.setStyle("-fx-background-color: #b7d4cb;");
        HBox answers = new HBox();

        btn.setId(questionId);
        Label resolved = new Label();
        if (question.getResolved() == true ){
            resolved.setText("(Resolved)");
        }
        else{
            resolved.setText("(Not resolved)");
        }
        Label author = new Label();
        author.setText("By: " + usersId);
        Label datePosted = new Label();
        
        datePosted.setText("Date posted: " + question.getDate());

        // btn.setPrefWidth(100);
        btn.setText("Replies (" + replyCount + ")");
        
        resolved.setAlignment(Pos.CENTER_RIGHT);
        
        answers.setMaxWidth(feed.getWidth() - 20);

        answers.setAlignment(Pos.BOTTOM_RIGHT);
        btn.setAlignment(Pos.CENTER_RIGHT);
        btn.setStyle("-fx-cursor: hand;");
        author.setStyle("-fx-padding: 0 20 5 0;");
        datePosted.setStyle("-fx-padding: 0 20 5 0;");
        
        answers.getChildren().addAll(author, datePosted, btn);

        quest.setMaxWidth(feed.getWidth() - 20);

        quest.setAlignment(Pos.TOP_LEFT);

        author.setAlignment(Pos.BOTTOM_LEFT);
        quest.getChildren().addAll(questText, resolved);
        feed.getItems().add( quest);
        feed.getItems().add( answers);
        
        //  msgArea.requestFocus();

        loadReplies(btn);
    }

    @FXML
    private void sendReply(ActionEvent event) throws SQLException {

        String newReply = replyArea.getText().trim();
        if (newReply.equals("")) {
            System.out.println("nothing");
        } else {
            replyArea.clear();

            Reply.createReply(newReply, questionid, currentUser.getUserID());
            // SwitchWindow.switchWindow((Stage) replyBtn.getScene().getWindow(), new UserQNA(currentUser));

           load(questionid);
        }
    }

    private void load(int questId){
        repliesQ.getChildren().clear();
                repliesView.getItems().clear();
                questionid = questId;
                try {
                    Question currentQuestion = Question.search(questionid);
                    Text text = new Text(currentQuestion.getText());
                    repliesQ.getChildren().add(text);
                    System.out.println(currentQuestion.getText());
                } catch (SQLException ex) {
                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                }

                ObservableList<Reply> replies = FXCollections.observableArrayList();

                try {
                    replies = sql.showReplies(questionid);
                } catch (SQLException ex) {
                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                }
                replies.forEach((_item) -> {
                    try {
                        displayReplies(_item);
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                repliesPane.setVisible(true);
    }
    
    @FXML
    private void loadReplies(Button btn) {

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                load(parseInt(btn.getId()));
                
            }
        });

    }

    @FXML
    private void displayReplies(Reply reply) throws SQLException, IOException {

        //  feed.setMouseTransparent(true);
       repliesView.setFocusTraversable(false);
        replyArea.clear();
        
        
        
        TextFlow replyText = new TextFlow();
        Text text = new Text(reply.getText());

        text.setStyle("-fx-font: 16 arial;");
        replyText.getChildren().add(text);
         
      
        HBox replies = new HBox();
        replies.getChildren().addAll(replyText); 
        HBox details = new HBox();
        // quest.setStyle("-fx-background-color: #b7d4cb;");
        Label author = new Label();
        Label datePosted = new Label();
        author.setText("By: " + reply.getSender() );
        datePosted.setText("Date Posted: " + reply.getDate());
       
        details.setMaxWidth(feed.getWidth()-20);
        details.setAlignment(Pos.BOTTOM_RIGHT);
        details.getChildren().addAll(author, datePosted);
        User sender = new User(reply.getSender());
               if (sender.getUserID()==currentUser.getUserID()){
                JFXButton btn = new JFXButton();
                btn.setId(String.valueOf(reply.getId()));
                btn.setText("Remove");
                btn.setStyle("-fx-cursor: hand;");
                btn.setAlignment(Pos.TOP_RIGHT);
                btn.setUnderline(true);
                replies.getChildren().add(btn);
                remove(btn);
               }
               
        replies.setMaxWidth(feed.getWidth());

        replies.setAlignment(Pos.TOP_LEFT);
        replyText.setTextAlignment(TextAlignment.RIGHT);
        author.setStyle("-fx-padding: 0 20 5 0;");
        datePosted.setStyle("-fx-padding: 0 20 5 0;");
      
        author.setAlignment(Pos.BOTTOM_RIGHT);
        repliesView.getItems().add(replies);
        repliesView.getItems().add(details);
    }

    @FXML
    private void remove(JFXButton btn) throws SQLException, IOException {
          Reply currentReply = Reply.search(parseInt(btn.getId()));
          btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    sql.deleteReply(currentReply.getId());
                    load(questionid);
                } catch (SQLException ex) {
                    Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
           
 });
                  }
}

