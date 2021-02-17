package ip3;

import SQL.SQLHandler;
import static java.lang.Integer.parseInt;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Zuby
 */
public class User {

    private final int userid;
    private final String firstname;
    private final String surname;
    private final String username;
    private final String password;
    private final String dob;
    private final String email;
    private static final SQLHandler sql = new SQLHandler();

    private String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public User(String user) throws SQLException {
        ArrayList<String> userInfo = sql.searchUsersTable(user);
        userid = parseInt(userInfo.get(0));
        username = userInfo.get(1);
        password = userInfo.get(2);
        firstname = userInfo.get(3);
        surname = userInfo.get(4);
        dob = convertDateToString(userInfo.get(5));
        email = userInfo.get(6);
    }

    public int getUserID() {
        return this.userid;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
