package ip3;

import SQL.SQLHandler;
import static java.lang.Integer.parseInt;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final String dateOfBirth;
    private final String email;
    private static final SQLHandler sql = new SQLHandler();

    public User(String user) throws SQLException {
        ArrayList<String> userInfo = sql.searchUsersTable(user);
        userid = parseInt(userInfo.get(0));
        username = userInfo.get(1);
        password = userInfo.get(2);
        firstname = userInfo.get(3);
        surname = userInfo.get(4);
        dateOfBirth = userInfo.get(5);
        email = userInfo.get(6);
    }

    
     public static void initialUser(String username, String password) throws SQLException {
        sql.initialUser(username, password);
    }

    
    public static void createUser(String userid, String username, String password, String firstname, String surname, String dateOfBirth, String email) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        Date date = (Date) formatter.parse(dateOfBirth);
        //unsure of the format it'll parse it to.

        sql.createUser(username, password, firstname, surname, date, email);

    }
    
    public static boolean match(String val){
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher match = pattern.matcher(val);
        boolean valUser = match.find();
        return valUser;

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

     public String getDOB() {
        return this.dateOfBirth;
    }
     
     public String getEmail(){
         return this.email;
     }
     
     
    
    //getters for dob and email
    //setters for everything
}
