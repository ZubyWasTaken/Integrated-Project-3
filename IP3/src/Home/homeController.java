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
import com.jfoenix.controls.JFXTextArea;
import ip3.SwitchWindow;
import ip3.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * @author Zuby
 */
@SuppressWarnings("DuplicatedCode")
public class homeController implements Initializable {

    @FXML
    private Label labelWelcome;

    User currentUser;


    @FXML
    private JFXButton sgnOutBut;

    @FXML
    private JFXButton btnUK;

    @FXML
    private JFXButton btnTech;

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


    public void setData(User user) {
        currentUser = user;
    }

    @FXML
    private void signOut(ActionEvent event) {
        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister());
    }

    @FXML
    private void UKFeed(ActionEvent event){
        //Unhides all other buttons and hides this button
        btnUK.setVisible(false);
        btnTech.setVisible(true);

        //This reads the current articles on BBC UK (https://www.bbc.co.uk/news/uk)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/uk/rss.xml");
        Feed feed = parser.readFeed();
        System.out.println(feed);
        List<FeedMessage> articles = feed.getMessages();

        feedTitle.setText(feed.getTitle());

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
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
        btnTech.setVisible(false);
        btnUK.setVisible(true);

        //This reads the current articles on BBC UK (https://www.bbc.co.uk/news/technology)
        //The articles will change as the news updates on BBC
        RSSFeedParser parser = new RSSFeedParser(
                "http://feeds.bbci.co.uk/news/technology/rss.xml");
        Feed feed = parser.readFeed();
        System.out.println(feed);
        List<FeedMessage> articles = feed.getMessages();

        feedTitle.setText(feed.getTitle());

        //------------------//
        //First Article     //
        //------------------//
        lblArticle1.setText(articles.get(0).getTitle());
        articleHyperlink1.setText(articles.get(0).getLink());

        articleHyperlink1.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(0).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Second Article    //
        //------------------//
        lblArticle2.setText(articles.get(1).getTitle());
        articleHyperlink2.setText(articles.get(1).getLink());

        articleHyperlink2.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(1).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Third Article     //
        //------------------//
        lblArticle3.setText(articles.get(2).getTitle());
        articleHyperlink3.setText(articles.get(2).getLink());

        articleHyperlink3.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(2).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Fourth Article    //
        //------------------//
        lblArticle4.setText(articles.get(3).getTitle());
        articleHyperlink4.setText(articles.get(3).getLink());

        articleHyperlink4.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(3).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        //------------------//
        //Fifth Article    //
        //------------------//
        lblArticle5.setText(articles.get(4).getTitle());
        articleHyperlink5.setText(articles.get(4).getLink());

        articleHyperlink5.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI(articles.get(4).getLink()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
            label.setVisible(true);
        }

        for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
            hyperlink.setVisible(true);
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                username.setText(currentUser.getUsername());

                //Sets feed title to inform user to select a feed
                feedTitle.setText("Please Select a Feed.");


                //Hides the labels and hyperlinks from the user
                for (Label label : Arrays.asList(lblArticle1, lblArticle2, lblArticle3, lblArticle4, lblArticle5)) {
                    label.setVisible(false);
                }

                for (Hyperlink hyperlink : Arrays.asList(articleHyperlink1, articleHyperlink2, articleHyperlink3, articleHyperlink4, articleHyperlink5)) {
                    hyperlink.setVisible(false);
                }


            }
        });
    }
}





