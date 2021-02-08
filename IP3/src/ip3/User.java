package ip3;

import SQL.SQLHandler;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
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
    private static final SQLHandler sql = new SQLHandler();

    public User(String user) throws SQLException {
        userid = 1;
        firstname = "zubair";
        surname = "khalid";
        username = "zkhalid";
        password = "password";

    }

}
