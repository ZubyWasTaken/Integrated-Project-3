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
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
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
import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;

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
    private Label labelWelcome;

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

    @FXML
    private AnchorPane repliesPane;

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
            Question.createQuestion(typeQuest, currentUser.getUserID(), now);
            SwitchWindow.switchWindow((Stage) msgBtn.getScene().getWindow(), new UserQNA(currentUser));

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

        msgArea.setTextFormatter(new TextFormatter<String>(change
                -> change.getControlNewText().length() <= MAX_CHARS ? change : null));

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
                        displayQs(_item);
                        ;
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

    private void displayQs(Question question) {

        //  feed.setMouseTransparent(true);
        feed.setFocusTraversable(false);
        msgArea.clear();
        TextFlow questText = new TextFlow();
        Text text = new Text(question.getText());

        text.setStyle("-fx-font: 16 arial;");
        questText.getChildren().add(text);

        HBox quest = new HBox();

        // quest.setStyle("-fx-background-color: #b7d4cb;");
        HBox answers = new HBox();
        Button btn = new Button();
        Label author = new Label();
        Label datePosted = new Label();
        author.setText("Author");
        datePosted.setText("Date Posted");

        // btn.setPrefWidth(100);
        btn.setText("Replies (" + 2 + ")");

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

        feed.getItems().add(0, answers);
        feed.getItems().add(0, quest);
        //  msgArea.requestFocus();

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                repliesPane.setVisible(true);

            }
        });

    }

}
