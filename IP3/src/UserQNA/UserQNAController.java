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
import com.jfoenix.controls.JFXTextArea;
import ip3.Question;
import ip3.Reply;

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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
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
    private ListView feed;

    @FXML
    private ListView repliesView;

    @FXML
    private JFXTextArea msgArea;

    @FXML
    private JFXTextArea replyArea;

    @FXML
    private AnchorPane repliesPane;

    @FXML
    private TextFlow repliesQ;
    @FXML
    private JFXButton refreshBtn;

    public int questionid;
    Timestamp now = new Timestamp(System.currentTimeMillis() - 3600000);
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
    private void refresh(ActionEvent event) {

        //TODOREFRESH
        try {
            loadQs();
            sql.updateLastSeenQ(currentUser.getUserID(), now);
        } catch (SQLException ex) {
            Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final int MAX_CHARS = 280;
        msgArea.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> change.getControlNewText().length() <= MAX_CHARS ? change : null));
        final int MAX_REPLY = 200;
        replyArea.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> change.getControlNewText().length() <= MAX_REPLY ? change : null));

        //  Timer timer = new Timer();
        //  timer.scheduleAtFixedRate(new TimerTask() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                drawer.setDisable(true);
                Drawer newdrawer = new Drawer();

                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                try {
                    loadQs();
                    sql.updateLastSeenQ(currentUser.getUserID(), now);
                } catch (SQLException ex) {
                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    @FXML
    private void loadReplies(Button btn) {

        btn.setOnAction((ActionEvent event) -> {
            try {
                loadR(parseInt(btn.getId()));
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @FXML
    private void displayReplies(Reply reply) throws SQLException {

        repliesView.setFocusTraversable(false);
        replyArea.clear();

        //Setting the current question
        User sender = reply.getSender();
        TextFlow replyText = new TextFlow();
        String userReply = reply.getText();
        Text text = new Text(userReply);
        text.setStyle("-fx-font: 16 arial;");
        replyText.getChildren().add(text);
        replyText.setTextAlignment(TextAlignment.RIGHT);

        //Getting profile pics
        ImageView profilePic = new ImageView();
        InputStream fs = sql.getImage(sender.getUserID());
        Image image = new Image(fs);
        profilePic.setImage(image);
        profilePic.setFitHeight(20);
        profilePic.setFitWidth(20);
        profilePic.setStyle("-fx-padding: 0 20 5 0;");

        //Getting author
        Label author = new Label();
        author.setText("By: " + sender.getUsername());
        author.setAlignment(Pos.BOTTOM_RIGHT);
        author.setStyle("-fx-padding: 0 20 5 0;");

        //Getting date
        Label datePosted = new Label();
        datePosted.setText("Date Posted: " + reply.getDate());
        datePosted.setStyle("-fx-padding: 0 20 5 0;");

        VBox container = new VBox();
        container.setSpacing(5);
        container.setMaxWidth(repliesView.getWidth() - 35);
        container.setAlignment(Pos.TOP_LEFT);
        container.setId(String.valueOf(reply.getId()));

        //Setting the boxes
        VBox quest = new VBox();
        quest.setAlignment(Pos.TOP_LEFT);
        HBox questhbox = new HBox();
        quest.getChildren().add(questhbox);
        quest.setMinHeight(95);
        questhbox.setAlignment(Pos.TOP_LEFT);
        questhbox.getChildren().addAll(replyText);
        questhbox.setSpacing(5);

        VBox answers = new VBox();
        answers.setMinHeight(20);
        answers.setAlignment(Pos.CENTER_RIGHT);
        HBox answerhbox = new HBox();
        answers.getChildren().add(answerhbox);
        answerhbox.setAlignment(Pos.CENTER_RIGHT);
        answerhbox.getChildren().addAll(author, profilePic, datePosted);
        answerhbox.setSpacing(5);

        container.getChildren().add(quest);
        container.getChildren().add(answers);

        //Adding them to the pane
        repliesView.getItems().addAll(container);
    }

    @FXML
    private void clickItem(MouseEvent event) {
        repliesView.setOnMouseClicked((MouseEvent event1) -> {
            if (event1.getButton() == MouseButton.SECONDARY) {
                try {
                    VBox vbox = (VBox) repliesView.getSelectionModel().selectedItemProperty().getValue();
                    int id = parseInt(vbox.getId());
                    Reply currentReply = Reply.search(id);
                    User sender = currentReply.getSender();
                    if (sender.getUserID() == currentUser.getUserID()) {

                        ContextMenu context = new ContextMenu();

                        MenuItem remove = new MenuItem("Remove");
                        context.getItems().addAll(remove);
                        repliesView.setContextMenu(context);
                        context.show(repliesView, Side.BOTTOM, repliesView.getLayoutX(), repliesView.getLayoutY());

                        remove.setOnAction((ActionEvent event2) -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this ", ButtonType.YES, ButtonType.CANCEL);
                            alert.showAndWait();
                            if (alert.getResult() == ButtonType.YES) {
                                try {
                                    sql.deleteReply(currentReply.getId());
                                    loadR(questionid);
                                } catch (SQLException ex) {
                                    Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });

                    }
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    @FXML
    private void settings(javafx.scene.input.MouseEvent event) {
        feed.setOnMouseClicked((javafx.scene.input.MouseEvent event1) -> {
            if (event1.getButton() == MouseButton.SECONDARY) {
                try {
                    VBox VBox = (VBox) feed.getSelectionModel().selectedItemProperty().getValue();
                    int id = parseInt(VBox.getId());
                    Question currentQuestion = Question.search(id);
                    User sender = currentQuestion.getSender();
                    if (sender.getUserID() == currentUser.getUserID()) {

                        ContextMenu context = new ContextMenu();
                        MenuItem remove = new MenuItem("Remove");
                        context.getItems().add(remove);
                        if (currentQuestion.getResolved() == false) {
                            MenuItem edit = new MenuItem("Mark as resolved");
                            context.getItems().add(edit);
                            edit.setOnAction((ActionEvent event2) -> {
                                try {
                                    sql.setResolved(currentQuestion.getId());
                                    loadQs();
                                } catch (SQLException ex) {
                                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
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
                                } catch (SQLException ex) {
                                    Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });

                    }
                } catch (SQLException | IOException ex) {
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
    private void loadQs() throws SQLException {
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

        feed.setFocusTraversable(false);

        //Getting the Question
        int replyCount = sql.countAllReplies(question.getId());

        TextFlow questText = new TextFlow();
        Text text = new Text(question.getText());
        text.setStyle("-fx-font: 16 arial;");
        questText.getChildren().add(text);
        User sender = question.getSender();

        //Getting the image
        ImageView profilePic = new ImageView();
        InputStream fs = sql.getImage(sender.getUserID());
        Image image = new Image(fs, 16, 16, false, false);
        profilePic.setImage(image);

        // profilePic.setFitHeight(20);
        //  profilePic.setFitWidth(20);
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
        if (question.getResolved() == true) {
            resolved.setText("(Resolved)");
        } else {
            resolved.setText("(Not resolved)");
        }
        resolved.setAlignment(Pos.CENTER_RIGHT);

        VBox container = new VBox();
        container.setMaxWidth(feed.getWidth() - 32);
        container.setAlignment(Pos.TOP_LEFT);
        container.setId(String.valueOf(question.getId()));

        //Setting the boxes
        VBox quest = new VBox();
        quest.setAlignment(Pos.TOP_LEFT);
        HBox questhbox = new HBox();
        quest.getChildren().add(questhbox);
        quest.setMinHeight(100);
        questhbox.setAlignment(Pos.TOP_LEFT);
        questhbox.getChildren().addAll(questText);
        questhbox.setSpacing(5);

        VBox answers = new VBox();
        answers.setMaxHeight(100);
        answers.setAlignment(Pos.BOTTOM_RIGHT);
        HBox answerhbox = new HBox();
        answers.getChildren().add(answerhbox);
        answerhbox.setAlignment(Pos.CENTER_RIGHT);
        answerhbox.getChildren().addAll(resolved, author, profilePic, datePosted, btn);
        answerhbox.setSpacing(5);

        container.getChildren().add(quest);
        container.getChildren().add(answers);

        //Adding them to the feed
        feed.getItems().addAll(container);

        msgArea.requestFocus();

        loadReplies(btn);
    }

    //retrieving replies from database//
    private void loadR(int questId) throws SQLException {
        sql.updateLastSeenQ(currentUser.getUserID(), now);
        repliesQ.getChildren().clear();
        repliesView.getItems().clear();
        questionid = questId;
        try {
            Question currentQuestion = Question.search(questionid);
            Text text = new Text(currentQuestion.getText());

            text.setStyle("-fx-font: 16 arial;");

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
            } catch (SQLException ex) {
                Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        repliesPane.setVisible(true);
    }

    @FXML
    private void showResolvedQs(ActionEvent event) throws SQLException {

        data.clear();
        feed.getItems().clear();
        data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
        data.forEach((_item) -> {
            try {
                if (_item.getResolved() == true) {
                    displayQs(_item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    private void showUnresolvedQs(ActionEvent event) throws SQLException {

        data.clear();
        feed.getItems().clear();
        data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
        data.forEach((_item) -> {
            try {
                if (_item.getResolved() == false) {
                    displayQs(_item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    private void showAllQs(ActionEvent event) throws SQLException {
        loadQs();
    }

    @FXML
    private void showMyQs(ActionEvent event) throws SQLException {

        data.clear();
        feed.getItems().clear();
        data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
        FXCollections.reverse(data);
        data.forEach((_item) -> {
            User tempUser = _item.getSender();


            try {

                if (tempUser.getUserID() == currentUser.getUserID()) {
                    displayQs(_item);

                }
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    private void sortAsc(ActionEvent event) throws SQLException {
        data.clear();
        feed.getItems().clear();
        data = sql.showQuestionsTable(currentUser.getCatId(), currentUser.getUniId());
        FXCollections.reverse(data);
        data.forEach((_item) -> {
            try {
                if (_item.getResolved() == false) {
                    displayQs(_item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
