package UserQNA;

import Home.Home;
import QA_Tutor.QA_TutorController;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.SwitchWindow;
import ip3.User;
import ip3.Drawer;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import ip3.Question;
import ip3.Reply;
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
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

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
    private ListView repliesView;

    @FXML
    private Button sendBtn;

    @FXML
    private Button replyBtn;

    @FXML
    private TextArea msgArea;

    @FXML
    private TextArea replyArea;

    @FXML
    private AnchorPane repliesPane;

    @FXML
    private TextFlow repliesQ;
    

    public int questionid;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    ObservableList<Question> data = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();

    
    //FXML controls//
    
    @FXML
    private void goHome(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) btnHome.getScene().getWindow(), new Home(currentUser));
    }

    @FXML
    private void sendMsg(ActionEvent event) throws SQLException {

        String typeQuest = msgArea.getText().trim();
        if (typeQuest.equals("")) {
            
            String tilte = "Message";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("You have not entered a message");
            tray.setNotificationType(NotificationType.INFORMATION);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        } else {
            msgArea.clear();
            Question.createQuestion(typeQuest, currentUser.getUserID());
            loadQs();

        }
    }


    @FXML
    private void close(ActionEvent event) {

        repliesPane.setVisible(false);
       
    }
    @FXML
    private void sendReply(ActionEvent event) throws SQLException {

        String newReply = replyArea.getText().trim();
        if (newReply.equals("")) {
            String tilte = "Reply";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage("You have not entered a reply");
            tray.setNotificationType(NotificationType.INFORMATION);
            tray.showAndDismiss(Duration.millis(3000));
            return;
        } else {
            replyArea.clear();

            Reply.createReply(newReply, questionid, currentUser.getUserID());
            loadR(questionid);
        }
    }
   

//    @FXML
//    private void viewChats(ActionEvent event){
//       
//        SwitchWindow.switchWindow((Stage) chat.getScene().getWindow(), new Chat(currentUser)); 
//    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final int MAX_CHARS = 280;
        msgArea.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> change.getControlNewText().length() <= MAX_CHARS ? change : null));
         Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
     @Override
            public void run() {
        Platform.runLater(() -> {
            
        username.setText(currentUser.getUsername());
        drawer.setDisable(true);
        Drawer newdrawer = new Drawer();
            
        newdrawer.drawerPullout(drawer, currentUser, hamburger);
            try {
                loadQs();
                sql.updateLastSeenQ(currentUser.getUserID(), now);
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
            },0,10000); 
                }
@FXML
    private void loadReplies(Button btn) {

        btn.setOnAction((ActionEvent event) -> {
            loadR(parseInt(btn.getId()));
        });

    }

    @FXML
    private void displayReplies(Reply reply) {

        //  feed.setMouseTransparent(true);
        repliesView.setFocusTraversable(false);
        replyArea.clear();
        TextFlow replyText = new TextFlow();
        Text text = new Text(reply.getText());

        text.setStyle("-fx-font: 16 arial;");
        replyText.getChildren().add(text);

        HBox replies = new HBox();
        replies.setId(String.valueOf(reply.getId()));
        HBox details = new HBox();
        // quest.setStyle("-fx-background-color: #b7d4cb;");
        Label author = new Label();
        Label datePosted = new Label();
        author.setText("By: " + reply.getSender() );
        datePosted.setText("Date Posted: " + reply.getDate());

        details.setMaxWidth(feed.getWidth()-20);
        details.setAlignment(Pos.BOTTOM_RIGHT);
        details.getChildren().addAll(author, datePosted);
        replies.setMaxWidth(feed.getWidth() - 20);

        replies.setAlignment(Pos.TOP_RIGHT);

        author.setStyle("-fx-padding: 0 20 5 0;");
        datePosted.setStyle("-fx-padding: 0 20 5 0;");
      
        author.setAlignment(Pos.BOTTOM_RIGHT);
        replies.getChildren().addAll(replyText);

        repliesView.getItems().add(replies);
        repliesView.getItems().add(details);
        
        //  feed.getItems().add(0, quest);
    }
    
   
    @FXML
         private void clickItem (javafx.scene.input.MouseEvent event){
             repliesView.setOnMouseClicked((javafx.scene.input.MouseEvent event1) -> {
             if(event1.getButton()==MouseButton.SECONDARY){
                 try{
                     HBox hbox =  (HBox) repliesView.getSelectionModel().selectedItemProperty().getValue();
                     int id = parseInt(hbox.getId());
                     Reply currentReply = Reply.search(id); 
                     User sender = new User(currentReply.getSender());
                    if (sender.getUserID()==currentUser.getUserID()){
                     
                       ContextMenu context = new ContextMenu();
                      
                       MenuItem remove = new MenuItem("Remove");
                       context.getItems().addAll( remove);
                       repliesView.setContextMenu(context);
                       context.show(repliesView, Side.BOTTOM, repliesView.getLayoutX(), repliesView.getLayoutY());
                        
                       remove.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                        try {
                        sql.deleteReply(currentReply.getId());
                        loadR(questionid);
                        } catch (SQLException ex) {
                        Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                        }
            }
        });
                   
        } 
             }   catch (SQLException | IOException ex) {
                     Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                 }
         }       
             });
         
}
        @FXML
            private void settings (javafx.scene.input.MouseEvent event){
             feed.setOnMouseClicked((javafx.scene.input.MouseEvent event1) -> {
             if(event1.getButton()==MouseButton.SECONDARY){
                 try{
                     HBox hbox =  (HBox) feed.getSelectionModel().selectedItemProperty().getValue();
                     int id = parseInt(hbox.getId());
                     Question currentQuestion = Question.search(id); 
                     User sender = new User(currentQuestion.getSender());
                    if (sender.getUserID()==currentUser.getUserID()){
                     
                       ContextMenu context = new ContextMenu();
                      
                       MenuItem remove = new MenuItem("Remove");
                       context.getItems().add( remove);
                       if (currentQuestion.getResolved()==false){
                           MenuItem edit = new MenuItem ("Mark as resolved");
                           context.getItems().add(edit);
                           edit.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                    sql.setResolved(currentQuestion.getId());
                                    loadQs();
                                    } catch (SQLException ex) {
                                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
                       }
                       feed.setContextMenu(context);
                       context.show(repliesView, Side.BOTTOM, repliesView.getLayoutX(), repliesView.getLayoutY());
                        
                       remove.setOnAction((ActionEvent event2) -> {
                           Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this ", ButtonType.YES, ButtonType.CANCEL);
                           alert.showAndWait();
                           if (alert.getResult() == ButtonType.YES) {
                               
                               try {
                                   sql.removeQuestion(currentQuestion.getId());
                                   loadQs();
                                   repliesPane.setVisible(false);
                               }
                               catch (SQLException ex) {
                                   Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           } 
                       });
               
                    }       
                }   catch (SQLException | IOException ex) {
                     Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                    }  
         
            }
    });
   }
            
    //private methods//
            
    public void setData(User user) throws SQLException {
        currentUser = user;
        

    }

    //retrieving the questions from the database//
    
    private void loadQs() throws SQLException{
        data.clear();
        feed.getItems().clear(); 
        data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
                    data.forEach((_item) -> {
                        try {
                            displayQs(_item);
                        } catch (SQLException ex) {
                            Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                
    }
   
    //creating a box for each question//
    
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
        quest.setId(String.valueOf(question.getId()));

        Button btn = new Button();

        // quest.setStyle("-fx-background-color: #b7d4cb;");
        HBox answers = new HBox();

       
        Label author = new Label();
        author.setText("By: " + usersId);
        Label datePosted = new Label();
        
        datePosted.setText("Date posted: " + question.getDate());

        // btn.setPrefWidth(100);
        btn.setText("Replies (" + replyCount + ")");
        
         btn.setId(questionId);
        Label resolved = new Label();
        if (question.getResolved() == true ){
            resolved.setText("(Resolved)");
        }
        else{
            resolved.setText("(Not resolved)");
        }
                
        
        
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
        
        msgArea.requestFocus();

        loadReplies(btn);
    }

//retrieving replies from database//
   
    private void loadR(int questId){
        repliesQ.getChildren().clear();
                repliesView.getItems().clear();
                questionid = questId;
                try {
                    Question currentQuestion = Question.search(questionid);
                    Text text = new Text(currentQuestion.getText());
                    repliesQ.getChildren().add(text);
                    System.out.println(currentQuestion.getText());
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                }

                ObservableList<Reply> replies = FXCollections.observableArrayList();

                try {
                    replies = sql.showReplies(questionid);
                } catch (SQLException ex) {
                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                }
                replies.forEach((_item) -> {
                    displayReplies(_item);
                });

                repliesPane.setVisible(true);
    }
    
    
}

