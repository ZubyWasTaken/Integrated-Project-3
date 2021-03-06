/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Chat.Chat;
import FileShare.FileShare;
import LoginRegister.LoginRegister;
import SQL.SQLHandler;
import UserQNA.UserQNA;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.Drawer;
import ip3.SwitchWindow;
import ip3.User;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.awt.TextArea;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * @author Zuby
 */
public class homeController implements Initializable {

    @FXML
    private Label labelWelcome;

    User currentUser;

    @FXML
    private JFXButton UserQNA;

    @FXML
    private JFXButton sgnOutBut;

    @FXML
    private JFXButton btnUK;

    @FXML
    private JFXButton btnTech;

    @FXML
    private JFXButton btnPolitics;

    @FXML
    private JFXButton btnWorld;

    @FXML
    private JFXButton btnBusiness;

    @FXML
    private JFXButton btnScience;

    @FXML
    private Label username;

    @FXML
    private Label feedTitle;

    @FXML
    private Label lblArticle1;

    @FXML
    private Hyperlink articleHyperlink1;

    @FXML
    private Label lblArticle2;

    @FXML
    private Hyperlink articleHyperlink2;

    @FXML
    private Label lblArticle3;

    @FXML
    private Hyperlink articleHyperlink3;

    @FXML
    private Label lblArticle4;

    @FXML
    private Hyperlink articleHyperlink4;

    @FXML
    private Label lblArticle5;

    @FXML
    private Hyperlink articleHyperlink5;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private TableView<User> usersOnline;

    @FXML
    private TableColumn<User, String> user;

    @FXML
    private Label repliesCount;

    @FXML
    private JFXButton qa;

    @FXML
    private JFXButton shareFiles;

    @FXML
    private JFXButton chat;

    @FXML
    private JFXButton teacherQNA;

    @FXML
    private AnchorPane userDetails;

    @FXML
    private ImageView profilePic;

    @FXML
    private Label userUsername;

    @FXML
    private JFXButton infoChat;

    @FXML
    private JFXButton infoFile;

    @FXML
    private JFXButton infoQNA;

    @FXML
    private TextFlow info;

    private SQLHandler sql = new SQLHandler();
    int count = 0;
    Timestamp now = new Timestamp(System.currentTimeMillis() - 3600000);
    ObservableList<User> data = FXCollections.observableArrayList();

    public void setData(User user) throws SQLException {
        currentUser = user;
        sql.updateLogin(currentUser.getUserID(), true);
        Timestamp timestamp = sql.getLastSeenQ(user.getUserID());
        count = sql.countUnseenReplies(currentUser.getUserID(), now, timestamp);
    }

    @FXML
    private void signOut(ActionEvent event) throws SQLException {
        sql.updateLogin(currentUser.getUserID(), false);
        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister());
    }

    @FXML
    private void qaSwitch(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) qa.getScene().getWindow(), new UserQNA(currentUser));
    }

    @FXML
    private void fileShare(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) shareFiles.getScene().getWindow(), new FileShare(currentUser));
    }

    @FXML
    private void chat(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) chat.getScene().getWindow(), new Chat(currentUser));
    }

    @FXML
    private void questionAnswer(ActionEvent event) throws SQLException {

        SwitchWindow.switchWindow((Stage) teacherQNA.getScene().getWindow(), new UserQNA(currentUser));
    }

    @FXML
    private void getOnlineUser(MouseEvent event) throws SQLException {

        TablePosition pos = (TablePosition) usersOnline.getSelectionModel().getSelectedCells().get(0);
        int index = pos.getRow();
        User item = usersOnline.getItems().get(index);

        String username = (String) user.getCellObservableValue(item).getValue();
        User user = new User(username);
        userDetails.setVisible(true);
        userUsername.setText(user.getUsername());
        InputStream fs = sql.getImage(user.getUserID());
        javafx.scene.image.Image image = new javafx.scene.image.Image(fs);
        profilePic.setImage(image);
    }

    @FXML
    private void close(ActionEvent event) {
        userDetails.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //**SETTING DISPLAY AS UK (FIRST BUTTON)**//
        handleMouseEvents();
        btnUK.setDisable(true);
        btnWorld.setDisable(false);
        btnBusiness.setDisable(false);
        btnTech.setDisable(false);
        btnPolitics.setDisable(false);
        btnScience.setDisable(false);

        info.setVisible(false);

        //This reads the current articles on BBC (https://www.bbc.co.uk/news/uk)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/uk/rss.xml");
        Feed feed = parser.readFeed();
        List<FeedMessage> articles = feed.getMessages();

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Drawer newdrawer = new Drawer();

                drawer.setDisable(true);
                newdrawer.drawerPullout(drawer, currentUser, hamburger);
                username.setText(currentUser.getFirstname());

                try {
                    data = sql.showUsersOnline(currentUser.getUniId(), currentUser.getCatId(), currentUser.getUserID());
                } catch (SQLException ex) {
                    Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
                }

                repliesCount.setText(Integer.toString(count));
                user.setCellValueFactory(new PropertyValueFactory<>("username"));
                usersOnline.setItems(data);
                //Hides the labels and hyperlinks from the user
//                for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
//                    label.setVisible(false);
//                }
//
//                for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
//                    hyperlink.setVisible(false);
//                }

            }
        });
    }

    //FEEDS//
    @FXML
    private void UKFeed(ActionEvent Event) {
        //Unhides all other buttons and hides this button
        btnUK.setDisable(true);
        btnWorld.setDisable(false);
        btnBusiness.setDisable(false);
        btnTech.setDisable(false);
        btnPolitics.setDisable(false);
        btnScience.setDisable(false);

        //This reads the current articles on BBC (https://www.bbc.co.uk/news/uk)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/uk/rss.xml");
        Feed feed = parser.readFeed();
        List<FeedMessage> articles = feed.getMessages();

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }

    }

    @FXML
    private void worldFeed(ActionEvent event) {
        //Unhides all other buttons and hides this button
        btnUK.setDisable(false);
        btnWorld.setDisable(true);
        btnBusiness.setDisable(false);
        btnTech.setDisable(false);
        btnPolitics.setDisable(false);
        btnScience.setDisable(false);

        //This reads the current articles on BBC (https://www.bbc.co.uk/news/world)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/world/rss.xml");
        Feed feed = parser.readFeed();
        List<FeedMessage> articles = feed.getMessages();

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }

    }

    @FXML
    private void businessFeed(ActionEvent event) {
        //Unhides all other buttons and hides this button
        btnUK.setDisable(false);
        btnWorld.setDisable(false);
        btnBusiness.setDisable(true);
        btnTech.setDisable(false);
        btnPolitics.setDisable(false);
        btnScience.setDisable(false);

        //This reads the current articles on BBC (https://www.bbc.co.uk/news/business)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/business/rss.xml");
        Feed feed = parser.readFeed();
        List<FeedMessage> articles = feed.getMessages();

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }
    }

    @FXML
    private void techFeed(ActionEvent event) {
        //Unhides all other buttons and hides this button
        btnUK.setDisable(false);
        btnWorld.setDisable(false);
        btnBusiness.setDisable(false);
        btnTech.setDisable(true);
        btnPolitics.setDisable(false);
        btnScience.setDisable(false);

        //This reads the current articles on BBC (https://www.bbc.co.uk/news/technology)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/technology/rss.xml");
        Feed feed = parser.readFeed();
        List<FeedMessage> articles = feed.getMessages();

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }
    }

    @FXML
    private void politicsFeed(ActionEvent event) {
        //Unhides all other buttons and hides this button
        btnUK.setDisable(false);
        btnWorld.setDisable(false);
        btnBusiness.setDisable(false);
        btnTech.setDisable(false);
        btnPolitics.setDisable(true);
        btnScience.setDisable(false);

        //This reads the current articles on BBC (https://www.bbc.co.uk/news/politics)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/politics/rss.xml");
        Feed feed = parser.readFeed();
        List<FeedMessage> articles = feed.getMessages();

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }
    }

    @FXML
    private void scienceFeed(ActionEvent event) {
        //Unhides all other buttons and hides this button
        btnUK.setDisable(false);
        btnWorld.setDisable(false);
        btnBusiness.setDisable(false);
        btnTech.setDisable(false);
        btnPolitics.setDisable(false);
        btnScience.setDisable(true);

        //This reads the current articles on BBC (https://www.bbc.co.uk/news/science_and_environment)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/science_and_environment/rss.xml");
        Feed feed = parser.readFeed();
        List<FeedMessage> articles = feed.getMessages();

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }
    }

    private void handleMouseEvents() {
        infoQNA.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        info.setVisible(true);

                        Text text = new Text("Ask Tutors in your course questions related to your subject.\nTeachers will"
                                + " respond within 3 days.\nStudents can also help out with questions and further discussions.\n"
                                + "Mark a question as resolved when you feel your question has been answered!");
                        text.setStyle("-fx-font: 16 arial;");

                        info.getChildren().add(text);
                    }
                });

        infoQNA.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        info.setVisible(false);

                        info.getChildren().clear();
                    }
                });
        infoChat.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        info.setVisible(true);

                        Text text = new Text("Chat informally as a group with other students in your course.\n"
                                + "Ask each other questions, discuss lectures or even discuss todays top stories from the BBC News Feed!");
                        text.setStyle("-fx-font: 16 arial;");
                        info.getChildren().add(text);
                    }
                });

        infoChat.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        info.setVisible(false);

                        info.getChildren().clear();
                    }
                });
        infoFile.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        info.setVisible(true);

                        Text text = new Text("Upload and download files with other students.\n"
                                + "Students can share images, text documents and presentations.\n"
                                + "Maybe you want to share useful lecture notes with your peers or get feedback on a presentation you created!");
                        text.setStyle("-fx-font: 16 arial;");
                        info.getChildren().add(text);
                    }
                });

        infoFile.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        info.setVisible(false);

                        info.getChildren().clear();
                    }
                });
    }
}
