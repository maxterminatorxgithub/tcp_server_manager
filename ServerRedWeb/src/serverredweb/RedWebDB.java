/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author maxim.p
 */
public class RedWebDB extends DatabaseHandler{
    
    
    
    
    @Override
    public void connect()throws SQLException{
        super.connect(DBKeys.APP_DATA_USER, DBKeys.APP_DATA_PASS);
    }
    
    
    @Override
    public void close() throws SQLException {
        super.close();
        if(isConnected())
            System.out.println("Red Web Database was succussfully disconnected.");
        else if(isConnectionError())
            ServerGUI.error("There was an Error when trying to connect to the database");
        else
            System.out.println("Red Web Database failed to disconnect safety.");
    }

    
    
    
    
    
    
    
    
}
