/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import HomeTutor.HomeTutor;
import SQL.SQLHandler;
import UserQNA.UserQNAController;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import ip3.Drawer;
import ip3.Question;
import ip3.Reply;
import ip3.SwitchWindow;
import ip3.User;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

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
    private ListView feed;

    @FXML
    private JFXTextArea replyArea;

    @FXML
    private AnchorPane repliesPane;

    @FXML
    private TextFlow repliesQ;
    
    @FXML
    private ListView repliesView;
   
    Tooltip tooltip = new Tooltip("Right-click for more details");
    ObservableList<Question> data = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
    User currentUser;
    public int questionid;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    
    
    public void setData(User user) throws SQLException {
    currentUser = user;
    }
  
@FXML
    private void refresh(ActionEvent event) {

        //TODOREFRESH
        try {
            loadAllQs();

        } catch (SQLException ex) {
            Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final int MAX_REPLY = 200;
        replyArea.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> change.getControlNewText().length() <= MAX_REPLY ? change : null));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    loadAllQs();
                } catch (SQLException ex) {
                    Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }
    
    private void loadAllQs() throws SQLException{
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

    @FXML
    private void close(ActionEvent event) {

        repliesPane.setVisible(false);
       
    }
    
     @FXML 
 private void goHome(ActionEvent event){

             SwitchWindow.switchWindow((Stage) btnHome.getScene().getWindow(), new HomeTutor(currentUser));
         }
 
  private void displayQs(Question question) throws SQLException {
  
       feed.setFocusTraversable(false);
      
      //Getting the Question
        int replyCount = sql.countAllReplies(question.getId());
        TextFlow questText = new TextFlow();
        Text text = new Text(question.getText());
        text.setStyle("-fx-font: 16 Sans-Serif;");
        questText.getChildren().add(text);
        User sender = question.getSender();
   
        //Getting the image
        ImageView profilePic = new ImageView();
        InputStream fs= sender.getImage();
        Image image = new Image(fs);
        profilePic.setImage(image);
        profilePic.setFitHeight(20);
        profilePic.setFitWidth(20);
        
        //Getting the author
        Label author = new Label();
        author.setText("By: " + sender.getUsername());
        author.setStyle("-fx-padding: 0 20 5 0;");
        author.setAlignment(Pos.BOTTOM_LEFT);
        
        //Getting the date
        Label datePosted = new Label();
        datePosted.setText("Date posted: " + question.getDate());
        datePosted.setStyle("-fx-padding: 0 20 5 0;");
        
        //Adding reply button
        Button btn = new Button();
        btn.setText("Replies (" + replyCount + ")");
        btn.setId(String.valueOf(question.getId()));
        btn.setAlignment(Pos.CENTER_RIGHT);
        btn.getStyleClass().add("categories-button");
        
        //Adding resolved status
        Label resolved = new Label();
        if (question.getResolved() == true ){
            resolved.setText("(Resolved)");
        }
        else{
            resolved.setText("(Not resolved)");
        }
        resolved.setAlignment(Pos.CENTER_RIGHT);
       
        //Setting the bozes
        HBox quest = new HBox();
        quest.setMaxWidth(feed.getWidth() - 20);
        quest.setAlignment(Pos.TOP_LEFT);
        quest.getChildren().addAll(questText, resolved);
        
        HBox answers = new HBox();
        answers.setMaxWidth(feed.getWidth() - 20);
        answers.setAlignment(Pos.BOTTOM_RIGHT);
        answers.getChildren().addAll(author, profilePic,datePosted,btn);

        //Adding them to the feed
        feed.getItems().addAll(quest, answers);

        //  msgArea.requestFocus();
        if(question.getSender().getUserID()== currentUser.getUserID()){
           Tooltip.install(feed, tooltip);
        }
        loadReplies(btn);
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
            load(questionid);
        }
    }

    private void load(int questId) throws SQLException{
        sql.updateLastSeenQ(currentUser.getUserID(), now);
        repliesQ.getChildren().clear();
                repliesView.getItems().clear();
                questionid = questId;
                try {
                    Question currentQuestion = Question.search(questionid);
                    Text text = new Text(currentQuestion.getText());
                    repliesQ.getChildren().add(text);
                    
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

        btn.setOnAction((ActionEvent event) -> {
            try {
                load(parseInt(btn.getId()));
            } catch (SQLException ex) {
                Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @FXML
    private void displayReplies(Reply reply) throws SQLException, IOException {
        
        repliesView.setFocusTraversable(false);
        replyArea.clear();
        
        
        //Setting the current question
        User sender = reply.getSender();
        TextFlow replyText = new TextFlow();
        Text text = new Text(reply.getText());
        text.setStyle("-fx-font: 16 arial;");
        replyText.getChildren().add(text);
        replyText.setTextAlignment(TextAlignment.RIGHT);
        
        //Getting profile pics
        ImageView profilePic = new ImageView();
        InputStream fs= sender.getImage();
        Image image = new Image(fs);
        profilePic.setImage(image);
        profilePic.setFitHeight(20);
        profilePic.setFitWidth(20);
        profilePic.setStyle("-fx-padding: 0 20 5 0;");
        
        //Getting author
        Label author = new Label();
        author.setText("By: " + sender.getUsername() );
        author.setAlignment(Pos.BOTTOM_RIGHT);
        author.setStyle("-fx-padding: 0 20 5 0;");
      
        //Getting date
        Label datePosted = new Label();
        datePosted.setText("Date Posted: " + reply.getDate());
        datePosted.setStyle("-fx-padding: 0 20 5 0;");
        
        //Setting the boxes
        HBox replies = new HBox();
        replies.getChildren().addAll(replyText); 
        replies.setId(String.valueOf(reply.getId()));
        replies.setAlignment(Pos.TOP_LEFT);
        replies.setMaxWidth(feed.getWidth());
        
        HBox details = new HBox();
        details.setMaxWidth(feed.getWidth()-20);
        details.setAlignment(Pos.BOTTOM_RIGHT);
        details.getChildren().addAll(author, profilePic,datePosted);
       
        //Adding them to the pane
        repliesView.getItems().addAll(replies,details);
        if(reply.getSender().getUserID()== currentUser.getUserID()){
           Tooltip.install(replies, tooltip);
        }
    }

   
   
    @FXML
         private void clickItem (MouseEvent event){
             repliesView.setOnMouseClicked((MouseEvent event1) -> {
                 if(event1.getButton()==MouseButton.SECONDARY){
                     try{
                         HBox hbox =  (HBox) repliesView.getSelectionModel().selectedItemProperty().getValue();
                         int id = parseInt(hbox.getId());
                         Reply currentReply = Reply.search(id);
                         User sender = currentReply.getSender();
                         if (sender.getUserID()==currentUser.getUserID()){
                             
                             ContextMenu context = new ContextMenu();
                             
                             MenuItem remove = new MenuItem("Remove");
                             context.getItems().addAll( remove);
                             repliesView.setContextMenu(context);
                             context.show(repliesView, Side.BOTTOM, repliesView.getLayoutX(), repliesView.getLayoutY());
                             
                             remove.setOnAction((ActionEvent event2) -> {
                                 Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this ", ButtonType.YES, ButtonType.CANCEL);
                                 alert.showAndWait();
                                 if (alert.getResult() == ButtonType.YES) {
                                     try {
                                         sql.deleteReply(currentReply.getId());
                                         load(questionid);
                                     } catch (SQLException ex) {
                                         Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                                     }
                                 }          });
                                    
                         }
                     }   catch (SQLException | IOException ex) {
                         Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
             });
         
}
@FXML
private void showResolvedQs (ActionEvent event) throws SQLException{
              
    data.clear();
    feed.getItems().clear(); 
    data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
    data.forEach((_item) -> {
        try {
            if (_item.getResolved()==true){
                displayQs(_item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
    });
}

@FXML
private void showUnresolvedQs (ActionEvent event) throws SQLException{
              
    data.clear();
    feed.getItems().clear(); 
    data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
    data.forEach((_item) -> {
        try {
            if (_item.getResolved()==false){
                displayQs(_item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
    });
}

@FXML
private void showAllQs (ActionEvent event) throws SQLException{
    loadAllQs();
}

@FXML
private void sortAsc(ActionEvent event) throws SQLException{
    data.clear();
    feed.getItems().clear(); 
    data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
    FXCollections.reverse(data);
    data.forEach((_item) -> {
        try {
            if (_item.getResolved()==false){
                displayQs(_item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
    });
}
}


