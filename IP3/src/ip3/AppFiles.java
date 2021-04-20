/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author stani
 */
public class AppFiles {
    private int id;
    private String fileName;
    private Blob fileByte;
    private String size;
    private User author;
    private Timestamp timestamp;
    
    public AppFiles(int id, String name, Blob blob, String size, String username, Timestamp timestamp) throws SQLException{
        this.id=id;
        this.fileName = name;
        this.fileByte=blob;
        this.size=size;
        this.timestamp=timestamp;
        this.author = new User(username);
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getFileName(){
        return this.fileName;
    }
    
    public Blob getFileByte(){
        return this.fileByte;
    }
    
    public String getSize(){
        return this.size;
    }

    public Timestamp getTimestamp() {return this.timestamp;}
    
    public User getAuthor(){
        return this.author;
    }
    public String getExtension(){
        int index = fileName.lastIndexOf('.');
        String extension = null;
        if(index > 0) {
        extension = fileName.substring(index + 1);  
}
        return extension;
}
    
}
