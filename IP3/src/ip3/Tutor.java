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

public class Tutor {

   
    private final int userid;
    private final String firstname;
    private final String surname;
    private final String username;
    private final String password;
    private final int catId;
    private final String title;
    private final int uniId;
    private static final SQLHandler sql = new SQLHandler();

    public Tutor(String tutor) throws SQLException {
        ArrayList<String> userInfo = sql.searchTutorsTable(tutor);
        userid = parseInt(userInfo.get(0));
        firstname = userInfo.get(1);
        surname = userInfo.get(2);
        username = userInfo.get(3);
        password = userInfo.get(4);
        catId = parseInt(userInfo.get(5));
        title = userInfo.get(6);
        uniId=parseInt(userInfo.get(7));

    }

    
     public Tutor(String username, String password) {
        userid=0;
         this.username=username;
        this.password=password;
        firstname=null;
        surname=null;
        uniId=0;
        catId=0;
        title=null;
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

    
    public String getTitle(){
    return this.title;
    }
    
    public int getUniId(){
        return this.uniId;
    }
    
    public int gtCatId(){
        return this.catId;
    }
     
    
    //getters for dob and email
    //setters for everything
}
