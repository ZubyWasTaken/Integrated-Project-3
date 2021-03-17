/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserQNA;

import Home.Home;
import LoginRegister.LoginRegister;
import QA_Tutor.ReplyTutor;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.SwitchWindow;
import ip3.User;
import ip3.Drawer;
import SQL.SQLHandler;
import ip3.Question;
import ip3.Reply;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException;
import static java.lang.Integer.parseInt;

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;
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

    @FXML
    private void goHome(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) btnHome.getScene().getWindow(), new Home(currentUser));
    }

    @FXML
    private void sendMsg(ActionEvent event) throws SQLException {

        String typeQuest = msgArea.getText().trim();
        if (typeQuest.equals("")) {
            System.out.println("nothing");
        } else {
            msgArea.clear();
            Question.createQuestion(typeQuest, currentUser.getUserID());
            SwitchWindow.switchWindow((Stage) sendBtn.getScene().getWindow(), new UserQNA(currentUser));

        }
    }


    @FXML
    private void close(ActionEvent event) {

        repliesPane.setVisible(false);
       
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                repliesPane.setVisible(false);
                username.setText(currentUser.getUsername());
                drawer.setDisable(true);
                Drawer newdrawer = new Drawer();

                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                try {
                    data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
                    data.forEach((_item) -> {
                        try {
                            displayQs(_item);
                        } catch (SQLException ex) {
                            Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                        }
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

    private void displayQs(Question question) throws SQLException {
  int replyCount = sql.countAllReplies(question.getId());
        //  feed.setMouseTransparent(true);
        feed.setFocusTraversable(false);
        msgArea.clear();
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

        Label author = new Label();
        author.setText("By: " + usersId);
        Label datePosted = new Label();
        
        datePosted.setText("Date posted: " + question.getDate());

        // btn.setPrefWidth(100);
        btn.setText("Replies (" + replyCount + ")");

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
        quest.getChildren().addAll(questText);
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

            TextFlow replyText = new TextFlow();
            Text text = new Text(newReply);
System.out.println(newReply);
            text.setStyle("-fx-font: 16 arial;");
            replyText.getChildren().add(text);
            
            HBox replies = new HBox();
            
            replies.setMaxWidth(feed.getWidth() - 20);

            replies.setAlignment(Pos.TOP_RIGHT);
             replies.getChildren().addAll(replyText);
            repliesView.getItems().add(replies);

        }
    }

    @FXML
    private void loadReplies(Button btn) {

        questionid = parseInt(btn.getId());
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                repliesQ.getChildren().clear();
                repliesView.getItems().clear();
                questionid = parseInt(btn.getId());
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
                    displayReplies(_item);
                });

                repliesPane.setVisible(true);
            }
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

}
