/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import SQL.SQLHandler;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author stani
 */
public class Interests {
    private final int intId;
    private final String intName;
    private final int catId;
    private static final SQLHandler sql = new SQLHandler();
        public Interests(int intId, String intName, int catId) {
        this.intId = intId;
        this.intName = intName;
        this.catId = catId;
    }
       public int getId(){
           return this.intId;
       }
       public String getName(){
           return this.intName;
       }
       public int getCarId(){
           return this.catId;
       }
       public static int fetchIntId(String tempcat) throws SQLException {
        List intInfo = sql.searchInterestsTable(tempcat);

        int tempIntId = (int) intInfo.get(0);

        return tempIntId;

    }
}
