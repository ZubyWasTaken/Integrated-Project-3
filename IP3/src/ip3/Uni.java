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
public class Uni {
    private final int uniId;
    private final String uniName;
    private final String uniLoc;
    private static final SQLHandler sql = new SQLHandler();
    
        public Uni(int uniId, String uniName, String uniLoc) {
        this.uniId = uniId;
        this.uniName = uniName;
        this.uniLoc = uniLoc;
    }
       public int getId(){
           return this.uniId;
       }
       public String getName(){
           return this.uniName;
       }
       public String getLoc(){
           return this.uniLoc;
       }
       public static int fetchUniId(String tempcat) throws SQLException {
        List uniInfo = sql.searchUniTable(tempcat);

        int tempUniId = (int) uniInfo.get(0);

        return tempUniId;

    }
}
