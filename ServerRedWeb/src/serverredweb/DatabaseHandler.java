/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.io.File;
import java.io.FileInputStream;
import java.lang.String;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author maxim.p
 */
public abstract class DatabaseHandler implements DatabaseController{

    
    private Connection con;
    private Statement state;
    private boolean connectionError;
    
    private boolean isConnected;
    private boolean isClosed;
    
    
    private String user,pass,host;
    private int port;
    
    
    public DatabaseHandler(){
        
        try{
        Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException exc){
            ServerGUI.error("SQL Error Log: not found driver");
            connectionError = true;
        }
    }
    
    
    @Override
    public void connect(String user, String password,String fullDatabaseURL)throws SQLException{
        try{
            con = DriverManager.getConnection(fullDatabaseURL, user, password);
            
            state = con.createStatement();
            isClosed = false;
            
            
            
            
            
        }catch(SQLException sqlex){
                connectionError = true;
                throw sqlex;
        }
        
        
    }
    
    
    @Override
    public void connect(String user, String password)throws SQLException{
        connect(user,password,DatabaseController.DEFAULT_MYSQL_PROTOCOL+
                              DatabaseController.DEFAULT_MYSQL_LOCALHOST+':'+
                              DatabaseController.DEFAULT_MYSQL_PORT+'/'+
                              DatabaseController.DEFAULT_MYSQL_DATABASE);
        
    }
    
    public void connect(String user, String password,String host,int port,String database,boolean setConnected)throws SQLException{
        
        this.user = user;
        this.pass = password;
        this.host = host;
        this.port = port;
        
        connect(user,password,DatabaseController.DEFAULT_MYSQL_PROTOCOL+host+':'+port+'/'+database);
        isConnected = setConnected;
    }
    
    public boolean checkRequiredTables(){
        
        Statement sys = null;
        
        boolean parents,children,categories,cells;
        
        parents=children=categories=cells = false;
        
        try{
            sys = DriverManager.getConnection(DatabaseController.DEFAULT_MYSQL_PROTOCOL+host+':'+port
                    +"/information_schema"
                    , user, pass).createStatement();
            
        }catch(SQLException sqlex){
            
            //System.out.println();
            
            ServerRedWeb.printSQLError(sqlex);
        }
        
        try(ResultSet res = sys.executeQuery("SELECT `NAME` FROM `INNODB_SYS_TABLES`")){
            
            while(res.next()){
                switch(res.getString("NAME")){
                    case "app_database/parents":
                        parents = true;
                        break;
                    case "app_database/children":
                        children = true;
                        break;
                    case "app_database/categories":
                        categories = true;
                        break;
                    case "app_database/cells":
                        cells = true;
                        break;
                }
            }
                
            
            
        }catch(SQLException sqlex){
            ServerRedWeb.printSQLError(sqlex);
        }
        
        
        return parents&&children&&categories&&cells;
    }
    
    public boolean fixTables(){
        boolean parents,children,categories,cells;
        parents=children=categories=cells = false;
        Statement sys = null;
        
        try{
            sys = DriverManager.getConnection(DatabaseController.DEFAULT_MYSQL_PROTOCOL+host+':'+port
                    +"/information_schema"
                    , user, pass).createStatement();
        }catch(SQLException sqlex){
            ServerRedWeb.printSQLError(sqlex);
        }
        
        
        try(ResultSet res = sys.executeQuery("SELECT `NAME` FROM `INNODB_SYS_TABLES`")){
            while(res.next()){
                switch(res.getString("NAME")){
                    case "app_database/parents":
                        parents = true;
                        break;
                    case "app_database/children":
                        children = true;
                        break;
                    case "app_database/categories":
                        categories = true;
                        break;
                    case "app_database/cells":
                        cells = true;
                        break;
                }
            }
        }catch(SQLException sqlex){
            ServerRedWeb.printSQLError(sqlex);
        }
        
        
        boolean result = false;
        
        if(!parents){
            try{
                state.execute(DatabaseController.QUIRY_CREATE_PARENTS_TABLE);
                result = true;
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }
        }
        if(!children){
            try{
                state.execute(DatabaseController.QUIRY_CREATE_CHILDREN_TABLE);
                result = true;
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }
        }
        if(!categories){
            try{
                state.execute(DatabaseController.QUIRY_CREATE_CATEGORIES_TABLE);
                PreparedStatement pref = con.prepareStatement("INSERT INTO `categories` VALUES(DEFAULT,0,'ארוחת בוקר',?)");
                File file = new File("res\\child_mode_option_eat.png");
                InputStream is = new FileInputStream(file);
                pref.setBinaryStream(1, is);
                
                pref.execute();
                
                result = true;
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }catch(IOException ioex){
                System.out.println(ioex);
            }
        }
        if(!cells){
            try{
                state.execute(DatabaseController.QUIRY_CREATE_CELLS_TABLE);
                result = true;
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }
        }
        
        return result;
        
    }
    
    protected boolean setRequiredTables(){
        return true;
    }
    
    @Override
    public void connect() throws SQLException {
        connect("root","");
    }

    @Override
    public boolean isConnected(){
        return isConnected;
    }

    @Override
    public void close() throws SQLException {
        state.close();
        con.close();
        isClosed = true;
        isConnected = false;
    }
    
    public boolean isClosed(){
        return isClosed;
    }
    
    protected boolean isConnectionError(){
        return connectionError;
    }
    
    public byte checkSimCard(String simCard){
        
        ResultSet res = null;
        
        try{
            
            synchronized(state){
                res = state.executeQuery("SELECT `id` FROM `parents` WHERE `sim_card` = '"+simCard+"'");
            
            
            int counter = 0;
            while(res.next())
                counter++;
            
            if(counter>0)
                return 1;
            return 0;
            }
        }catch(SQLException sqlex){
            ServerRedWeb.printSQLError(sqlex);
            return -1;
        }
        finally{
            if(res != null)
                try{
                    res.close();
                }catch(SQLException sqlex){
                    ServerRedWeb.printSQLError(sqlex);
                }
        }
    }
    /**
     * 
     * 
     * @param parentId id of the parent in database
     * @return Parent object
     */
    
    public ResultSet getParent(int parentId) throws SQLException{
        synchronized(state){
            return state.executeQuery("SELECT * FROM `parents` WHERE `id` = '"+parentId+"'");
        }
    }
    
    public ResultSet getParent(String simCard) throws SQLException{
            return con.createStatement().executeQuery("SELECT * FROM `parents` WHERE `sim_card` = '"+simCard+"'");
                
    }
    
    public synchronized void addParent(Parent p) throws SQLException{
        synchronized(state){
            state.executeUpdate("INSERT INTO `parents` (`sim_card`,`registration_date`)VALUES('"+p.getSimCard()+"',CURRENT_DATE)");
        }
    }
    
    public synchronized int addchild(int parentId,String name,String hash) throws SQLException{
        synchronized(state){
            return state.executeUpdate("INSERT INTO `children` (`parent_id`,`name`,`hash`)VALUES('"
                    +parentId+"'int,'"+name+"','"+hash+"')");
        }
    }
    
    public ResultSet getChildByHash(String childHash) throws SQLException{
        synchronized(state){
            return state.executeQuery("SELECT `id` FROM `children` WHERE `hash` = '"+childHash+"'");
        }
    }
    
    public ResultSet getChildById(int id) throws SQLException{
        synchronized(state){
            return state.executeQuery("SELECT `id` FROM `children` WHERE `id` = "+id+"");
        }
    }
    
    public void updateParentOnline(String simCard,Date d) throws SQLException{
        
        String query = "UPDATE `parents` SET `using_app` = 1 , `last_connected` = '"+getFormatDateTime(d)+"' WHERE `sim_card` = '"+simCard+"'";
        ServerRedWeb.GUI.log(query);
        synchronized(state){
            state.executeUpdate(query);
        }
    }
    
    public void updateChildOnline(String hash,Date d) throws SQLException{
        String query = "UPDATE `children` SET `using_app` = 1 , `last_connected` = '"+getFormatDateTime(d)+"' WHERE `hash` = '"+hash+"'";
        ServerRedWeb.GUI.log(query);
        synchronized(state){
            state.executeUpdate("UPDATE `children` SET `using_app` = 1 , `last_connected` = '"+getFormatDateTime(d)+"' WHERE `hash` = '"+hash+"'");
        }
    }

    public ArrayList<Child>getChildList(String sim_card)throws SQLException{
        ArrayList<Child> data = new ArrayList();
        
        synchronized(state){
            
            ResultSet parent = getParent(sim_card);
            
            int parentId = -1;
            
            while(parent.next()){
                parentId = parent.getInt("id");
            }
            parent.close();
            
            if(parentId == -1){
                return data;
            }
            
            ResultSet results = state.executeQuery("SELECT * FROM `children` RIGHT JOIN `parents` ON "
                    +"`children`.`parent_id`=`parents`.`id` WHERE `parents`.`id` = "+parentId);
            
            while(results.next()){
                data.add(new Child(results.getInt("id"),results.getInt("parent_id"),results.getString("name"),results.getString("hash")));
            }
            
            results.close();
            
        }
        
        return data;
    }
    
    public synchronized void updateConnections()throws SQLException{
        synchronized(state){
        long currentTimeStamp = new Date().getTime();
        
        ResultSet parents = state.executeQuery("SELECT `id`,`last_connected` FROM `parents` WHERE `using_app` = 1");
        
        
        try{
            while(parents.next()){
                if(currentTimeStamp-parents.getTimestamp("last_connected").getTime() > 15000){
                    state.executeUpdate("UPDATE `parents` SET `using_app` = "+0
                    +" WHERE `id` = "
                    +parents.getString("id"));
                }
                //parents.next();
            }
        }catch(SQLException sqlex){
        }
        
       
        
        ResultSet children = state.executeQuery("SELECT `id`,`last_connected` FROM `children` WHERE `using_app` = 1");
        
        while(children.next()){
            if(currentTimeStamp-children.getTimestamp("last_connected").getTime() > 15000){
                state.executeUpdate("UPDATE `children` SET `using_app` = "+0
                        +" WHERE `id` = "
                        +children.getString("id"));
            }
            //children.next();
        
        }
        }
    }
    
    public void addCategory(String sim,String name,byte[] data)throws SQLException{
        
        int id = 0;
        
        synchronized(state){
        
            ResultSet res = getParent(sim);
            res.first();
            id = res.getInt("id");
        }
        
        if(id == 0){
            throw new SQLException("failed to invoke parents id");
        }
        
        PreparedStatement pst = con.prepareStatement("INSERT INTO `categories` (`id`,`parent_id`,`title`,`image`) VALUES(DEFAULT,"+
            id+",'"+name+"',?)");
        pst.setBytes(1, data);
        //pst.setBinaryStream(2, data);
        
        pst.executeUpdate();
    }
    
    public static String getFormatDateTime(Date d){
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(d.getYear()+1900);
        sb.append('-');
        int month = d.getMonth()+1;
        sb.append(month<10?"0"+month:month);
        sb.append('-');
        int day = d.getDate();
        sb.append(day<10?"0"+day:day);
        sb.append(' ');
        
        int h=d.getHours(),m=d.getMinutes(),s=d.getSeconds();
        
        sb.append(h<10?"0"+h:h);
        sb.append(':');
        sb.append(m<10?"0"+m:m);
        sb.append(':');
        sb.append(s<10?"0"+s:s);
        
        return sb.toString();
        
    }
    
    public String flowQueryResult(String query){
        
        StringBuffer str = new StringBuffer();
        
        try(ResultSet res = state.executeQuery(query)){
            final ResultSetMetaData meta = res.getMetaData();
            final int colCount = meta.getColumnCount();
            for(int i=1;i<=colCount;i++){
                str.append(meta.getColumnName(i)+'\t');
            }
            str.append('\n');
            for(int i=1;i<=colCount;i++){
                for(int j=0;j<meta.getColumnName(i).length();j++)
                    str.append('-');
                str.append('\t');
            }
            str.append('\n');
            while(res.next()){
                for(int i=1;i<=colCount;i++){
                    str.append(res.getString(i)+'\t');
                }
                str.append('\n');
            }
        }catch(SQLException sqlex){
            return sqlex.toString();
        }
        
        
        return str.toString();
    }
    
    public int flowQueryExec(String query){
        
        try{
            return state.executeUpdate(query);
        }catch(SQLException sqlex){
            System.out.println(sqlex);
            return -1;
        }
    }
    
    public Map<String, byte[]> getCategories(Parent p) {
        
        Map <String, byte[]> categories= new HashMap<>();
        
        synchronized(state){
            try(ResultSet res = state.executeQuery("SELECT `title`,`image` FROM `categories` WHERE `parent_id` = 0 OR `parent_id` = "+p.getId())){
                while(res.next()){
                    InputStream datastream = res.getBlob("image").getBinaryStream();
                    byte[] data = new byte[datastream.available()];
                    datastream.read(data);
                    categories.put(res.getString("title"), data);
                }
            }catch(SQLException sqlex){
                
            }
            catch(IOException ioex){
                
            }
        }
        return categories;
    }
    
}
