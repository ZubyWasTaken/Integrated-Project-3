package ip3;

import SQL.SQLHandler;

import static java.lang.Integer.parseInt;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.StringProperty;

/**
 * @author Zuby
 */

public class User {


    private final int userid;
    private String firstname;
    private String surname;
    private String username;
    private String password;
    private final String dateOfBirth;
    private String email;
    private int uniId;
    private int catId;
    private final int titleId;
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
        uniId = parseInt(userInfo.get(7));
        catId = parseInt(userInfo.get(8));
        titleId = parseInt(userInfo.get(9));

    }


    public User(int userid, String firstname, String surname, String username, String password, String dateOfBirth, String email, int uniId, int catId, int titleId) {
        this.userid = userid;
        this.firstname = firstname;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.uniId = uniId;
        this.catId = catId;
        this.titleId = titleId;
    }

    public User(String username, String password) {
        userid = 0;
        this.username = username;
        this.password = password;
        dateOfBirth = null;
        email = null;
        firstname = null;
        surname = null;
        uniId = 0;
        catId = 0;
        titleId = 0;
    }


    public static void createUser(String username, String password, String firstname, String surname, String dateOfBirth, String email, int uniId, int catId, int titleId) throws SQLException, ParseException {

        //unsure of the format it'll parse it to.

        sql.createUser(username, password, firstname, surname, dateOfBirth, email, uniId, catId, titleId);

    }

    public static boolean match(String val) {
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher match = pattern.matcher(val);
        boolean valUser = match.find();
        return valUser;

    }

    public static boolean matchName(String val) {
        Pattern pattern = Pattern.compile("[^A-Za-z]");
        Matcher match = pattern.matcher(val);
        boolean valUser = match.find();
        return valUser;

    }

    public static int fetchUniId(String email) {
        int uniID;
        String emailGcu = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@caledonian.ac.uk";
  
        Pattern pat = Pattern.compile(emailGcu);
       // Pattern pat2 = Pattern.compile(emailStrath);
        if (pat.matcher(email).matches()) {
            uniID = 1;
       
        } else {
            uniID = 0;
        }
        return uniID;
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
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

    public String getEmail() {
        return this.email;
    }

    public int getTitleId() {
        return this.titleId;
    }

    public int getCatId() {
        return this.catId;
    }

    public int getUniId() {
        return this.uniId;
    }


    //getters for dob and email
    //setters for everything

    public void setPassword(String password) {
        this.password = password;
    }

    public void editPassword(User currentUser) throws SQLException {
        sql.editPassword(currentUser.getUserID(), currentUser.getPassword());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUniId(int uniId) {
        this.uniId = uniId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public boolean equals(Object obj) {
        return (this == obj);
    }

    public void updateLogin(boolean login) throws SQLException {
        sql.updateLogin(userid, login);
    }
}
