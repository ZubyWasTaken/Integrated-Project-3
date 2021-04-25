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
 * @author stani
 */
public class Categories {

    private final int catId;
    private final String catName;
    private static final SQLHandler sql = new SQLHandler();

    public Categories(int catId, String catName) {
        this.catName = catName;
        this.catId = catId;

    }

    public int getId() {
        return this.catId;
    }

    public String getName() {
        return this.catName;
    }

    public static int fetchCatId(String tempcat) throws SQLException {
        List catInfo = sql.searchCategoriesTable(tempcat);

        int tempCatId = (int) catInfo.get(0);

        return tempCatId;

    }
}


