/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QA_Tutor;

import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import ip3.Question;
import ip3.SwitchWindow;
import ip3.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Question> table;
     @FXML
    private TableColumn<Question, String> col_quest;
    @FXML
    private TableColumn<Question, Integer> col_author;
   
    @FXML
    private JFXButton replyBut;
    ObservableList<Question> data = FXCollections.observableArrayList();
    SQLHandler sql = new SQLHandler();
    User currentUser;
    
    public void setData(User user) {
    currentUser = user;
    }
  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Platform.runLater(new Runnable() {
    @Override
            public void run() {
         try {
            data = sql.showQuestionsTable(currentUser.getCatId());
     
        
        col_quest.setCellValueFactory(new PropertyValueFactory<>("text"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("sender"));
        table.setItems(data);
        FilteredList<Question> filtQuest = new FilteredList<>(data, e -> true);
        /*search.setOnKeyReleased(e -> {
            search.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filtQuest.setPredicate((Predicate<? super Question>) question -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (question.getText().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });*/
           /* SortedList<Question> sortedData = new SortedList<>(filtQuest);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);*/
            } catch (SQLException ex) {
            Logger.getLogger(QA_TutorController.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
    });
         
    }    
    
    @FXML
    private void addReply(ActionEvent event) throws SQLException, IOException{
         Question currentQuestion = Question.search(getTablePos());
            SwitchWindow.switchWindow((Stage) replyBut.getScene().getWindow(), new Reply(currentQuestion,currentUser));
    }
    
     private String getTablePos() {
        TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
        int index = pos.getRow();
        Question item = table.getItems().get(index);
        String questionText = (String) col_quest.getCellObservableValue(item).getValue();

        return questionText;
    }
}
