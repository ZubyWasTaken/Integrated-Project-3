/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileShare;
import LoginRegister.LoginRegister;
import SQL.SQLHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import ip3.AppFiles;
import ip3.Drawer;
import ip3.SwitchWindow;
import ip3.User;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


/**
 * FXML Controller class
 *
 * @author stani
 */
public class FileShareController implements Initializable {

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;
    
    @FXML
    private TextField search;
    
    @FXML
    private TableView files;
    
    @FXML
    private TableColumn<AppFiles,String> fileName;
    
    @FXML
    private TableColumn<AppFiles, String> fileAuthor;
    
    @FXML
    private TableColumn <AppFiles, String> fileSize;
    
    @FXML
    private AnchorPane pane;
    
    @FXML
    private TextField fileLoc;
    
    @FXML
    private JFXButton uploadBut;
    
   @FXML
    private JFXButton sgnOutBut;
   
    byte[] file=null;
    User currentUser;
    private SQLHandler sql = new SQLHandler();
    ObservableList<AppFiles> data = FXCollections.observableArrayList();
    TrayNotification tray = new TrayNotification();
    AnimationType type = AnimationType.POPUP;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      Platform.runLater(new Runnable() {
    @Override
            public void run() {
                drawer.setDisable(true);
                Drawer newdrawer = new Drawer();
                newdrawer.drawerPullout(drawer, currentUser, hamburger);
               
                displayFiles(data);
                //Search for a specific file
                FilteredList<AppFiles> filtFile = new FilteredList<>(data, e -> true);
                search.setOnKeyReleased(e -> {
                search.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filtFile.setPredicate((Predicate<? super AppFiles>) file -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (file.getFileName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            SortedList<AppFiles> sortedData = new SortedList<>(filtFile);
            sortedData.comparatorProperty().bind(files.comparatorProperty());
            files.setItems(sortedData);
            
         });
            }

          
    });
              }

 private void displayFiles(ObservableList<AppFiles> filesData) {
            
                fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
                fileSize.setCellValueFactory(new PropertyValueFactory<>("size"));
                fileAuthor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AppFiles, String>, ObservableValue<String>>() {
                @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<AppFiles, String> p) {
                    return new SimpleStringProperty(p.getValue().getAuthor().getUsername());
                    }
                    });
                files.setItems(filesData);
          }  
    
@FXML
private void upload(ActionEvent event) throws IOException, FileNotFoundException, SQLException{
    FileChooser fc = new FileChooser();
    fc.setTitle("Open File Dialog");
    Stage stage = (Stage)pane.getScene().getWindow();
    List<File> chosenFile = fc.showOpenMultipleDialog(stage);
    if (chosenFile !=null)
    {
        chosenFile.forEach((_item) -> {
           
        try {
            uploadFiles(_item);

            tray.setTitle("Upload");
            tray.setMessage("All files successfully uploaded");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(3000));
}       
       catch (IOException | SQLException ex) {
            tray.setTitle("Upload");
            tray.setMessage("There was an error when uploading the files. Please try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
            }
        
    });
    }
    SwitchWindow.switchWindow((Stage) uploadBut.getScene().getWindow(), new FileShare(currentUser));
    }
 
@FXML
         private void clickItem (MouseEvent event){
             files.setOnMouseClicked((MouseEvent event1) -> {
                 if(event1.getButton()==MouseButton.SECONDARY){
                     TablePosition pos = (TablePosition) files.getSelectionModel().getSelectedCells().get(0);
                     int i = pos.getRow();
                     AppFiles item = (AppFiles) files.getItems().get(i);
                     User author = item.getAuthor();
                     if (author.getUserID()==currentUser.getUserID()){
                         
                         ContextMenu context = new ContextMenu();
                         MenuItem remove = new MenuItem("Remove");
                         context.getItems().addAll( remove);
                         files.setContextMenu(context);
                         context.show(files, Side.TOP, files.getLayoutX(), files.getLayoutY());
                            
                         remove.setOnAction((ActionEvent event2) -> {
                             Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this? ", ButtonType.YES, ButtonType.CANCEL);
                             alert.showAndWait();
                             if (alert.getResult() == ButtonType.YES) {
                                 try {
                                     sql.deleteFile(item.getId());
                                     data = sql.showFiles(currentUser.getUniId(), currentUser.getCatId());
                                     displayFiles(data);
                                 } catch (SQLException ex) {
                                     Logger.getLogger(FileShareController.class.getName()).log(Level.SEVERE, null, ex);
                                 }
                             }          });
                         
                     }
                 }
             });
         
}

@FXML 
 private void download(ActionEvent e) throws  IOException, SQLException{
    try {
        TablePosition pos = (TablePosition) files.getSelectionModel().getSelectedCells().get(0);
        
        int i = pos.getRow();
        AppFiles item = (AppFiles) files.getItems().get(i);
  
        String extension = item.getExtension();
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(item.getFileName());
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(extension.toUpperCase(), "*."+extension), new FileChooser.ExtensionFilter("All files", "*.*"));
        Stage stage = (Stage)pane.getScene().getWindow();
        File dest = fc.showSaveDialog(stage);
        String location=dest.getAbsolutePath();
        try ( FileOutputStream fos = new FileOutputStream(location)){
            Blob fileData = item.getFileByte();
            int len = (int) fileData.length();
            byte[] buf = fileData.getBytes(1, len);
            fos.write(buf,0,len);
        }
        catch (IOException ex){
       ;
            tray.setTitle("Download");
            tray.setMessage("Error when downloading the file. Please try again.");
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        }
}
        catch (Exception ex){
           
            tray.setTitle("Download");
            tray.setMessage("No file is selected");
            tray.setNotificationType(NotificationType.INFORMATION);
            tray.showAndDismiss(Duration.millis(3000));
                   
            }
   }
 @FXML
private void showImageFiles(ActionEvent event) throws SQLException{
    
    ObservableList<AppFiles> filtFile =FXCollections.observableArrayList();
    data.forEach((_item) -> {
        String[] strs = {"png", "jpg", "jpeg", "gif", "eps", "raw","bmp","tiff","tif"};
        List<String> elements = new ArrayList<>();
        elements.addAll(Arrays.asList(strs));
        if (elements.contains(_item.getExtension())){
            filtFile.add(_item);     
        }    
    });
          displayFiles(filtFile);
          files.refresh();
}
@FXML
private void showPowerpoint(ActionEvent event) throws SQLException{
    
    ObservableList<AppFiles> filtFile =FXCollections.observableArrayList();
    data.forEach((_item) -> {
        String[] strs = {"pptx", "pptm", "ppt", "ppsx", "ppsm", "pps","potx","potm","pot","otp","odp","key"};
        List<String> elements = new ArrayList<>();
        elements.addAll(Arrays.asList(strs));
        if (elements.contains(_item.getExtension())){
            filtFile.add(_item);     
        }    
    });
          displayFiles(filtFile);
          files.refresh();
}
@FXML
private void showTextFiles(ActionEvent event) throws SQLException{
    
    ObservableList<AppFiles> filtFile =FXCollections.observableArrayList();
    data.forEach((_item) -> {
        String[] strs = {"doc", "docx", "odt", "pdf", "rtf", "tex","txt","wpd"};
        List<String> elements = new ArrayList<>();
        elements.addAll(Arrays.asList(strs));
        if (elements.contains(_item.getExtension())){
            filtFile.add(_item);     
        }    
    });
          displayFiles(filtFile);
          files.refresh();
}
@FXML
private void showAllFiles(ActionEvent event) {
    
    displayFiles(data);
    files.refresh();
}



void setData(User currentUser) throws SQLException {
        this.currentUser=currentUser;
        data = sql.showFiles(currentUser.getUniId(), currentUser.getCatId());
        tray.setAnimationType(type);
    }
  
private void uploadFiles(File item) throws FileNotFoundException, IOException, SQLException{
    String location=item.getAbsolutePath();
    Path path = Paths.get(location);
    Path fileName = path.getFileName();
    fileLoc.setText(location);
    if (location != null){
    File newFile  = new File (location);
    FileInputStream fis = new FileInputStream(newFile);
    String size = (double) newFile.length() / 1024 + "  kb";
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    for (int readNum; (readNum=fis.read(buf))!=-1;){
        bos.write(buf,0,readNum);
        }
    file=bos.toByteArray();
    sql.uploadFile(file,currentUser.getUserID(),fileName.toString(), size);
}
}
@FXML
    private void signOut(ActionEvent event) throws SQLException {
        sql.updateLogin(currentUser.getUserID(), false);
        SwitchWindow.switchWindow((Stage) sgnOutBut.getScene().getWindow(), new LoginRegister());
    }


}
