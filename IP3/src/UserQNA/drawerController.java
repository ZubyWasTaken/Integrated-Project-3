package UserQNA;

import ip3.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class drawerController implements Initializable {
    User currentUser;




    public void setData(User user) {
        this.currentUser = user;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
