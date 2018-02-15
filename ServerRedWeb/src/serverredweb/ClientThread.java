/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.lang.String;
import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author maxterminatorx
 */
public class ClientThread extends Thread{
    
    public static final byte CMD_CHECK_IS_PARENT_EXIST = 0X0A;
    public static final byte CMD_ADD_NEW_PARENT = 0X0B;
   
    public static final byte CMD_CHECK_IS_CHILD_EXIST = 0X1A;
    public static final byte CMD_ADD_NEW_CHILD = 0X1B;
    
    public static final byte CMD_GET_CHILD_LIST = 0X2A;
    
    public static final byte CMD_UPDATE_PARENT_ONLINE = 0X3A;
    public static final byte CMD_UPDATE_CHILD_ONLINE = 0X3B;
    
    public static final byte CMD_ADD_CATEGORY = 0X4A;
    public static final byte CMD_GET_CATEGORIES = 0X4B;
    
    
    //public static final String URL_INFO = "http://192.168.2.186/check123.php";
    
    public static final String URL_INFO = "http://212.199.145.226/API/RlAppLChecker.php";
    
    
    public static final String KEYPASS = "z56+}Ry&@@vbn4T8GG3HJk*/\\0\n\n\\\\8Tu6&3##\u8566";

    
    
    
    public static class HashGenerator{
        
        private static final String hashKeys = "qwe0rty2u3io4p5asd6fg7hjk8lz9xcvbnm";
        private static final Random hashGenerator = new Random();
        
        public static String generateKey(int hashLength){
            char[] hash = new char[hashLength];
            
            for(int i=0;i<hash.length;i++)
                hash[i] = hashKeys.charAt(hashGenerator.nextInt(hashKeys.length()));
            
            return new String(hash);
        }
        
        public static String generateKey(){
            return generateKey(10);
            //System.getenv(hashKeys)
        }
        
        
    }
    
    
    
    private Socket client;
    
    private Date connectionDate;
    
    
    public ClientThread(Socket client){
        this.client=client;
        connectionDate=new Date();
    }
    
    @Override
    public void run(){
        
        ServerGUI.log("Log: connection detected at: "+connectionDate);
        String clientIP = client.getInetAddress().getHostAddress();
        ServerGUI.log("Log: from ip address: "+clientIP);
        
        
        try(
                InputStream is = client.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                OutputStream os = client.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os)){
            
            
            String simCard;
            
            byte signal = (byte)is.read();
            System.out.println(Integer.toHexString(signal));
            
            
            String key;
            
            switch(signal){
                
                    
                case CMD_CHECK_IS_PARENT_EXIST:
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    ServerGUI.log("connected for check if parent exist.");
                    simCard = (String)ois.readObject();
                    
                    
                    
                    
                    
                    
                    byte result1 = checkSimCard(simCard);
                    System.out.println(result1);
                        os.write(result1);
                        System.out.println("asfasa");
                    break;
                    
                case CMD_ADD_NEW_PARENT:
                    
                    
                    
                    
                    
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    ServerGUI.log("connected for add new parent.");
                    simCard = (String)ois.readObject();
                    
                    
                    JSONObject json;
                    
                    try{
                        
                        json = returnJson(URL_INFO,"Act=CheckSim&SimNumber="+simCard);
                        
                        if(json.getString("Status").equals("Ok")){
                            oos.writeByte(addParent(simCard,connectionDate));
                        }
                        
                    }catch(JSONException jsonex){
                        os.write(-1);
                        break;
                    }
                    
                    
                    
                    
                    break;
                    
                case CMD_CHECK_IS_CHILD_EXIST:
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    ServerGUI.log("connected for check if child exist.");
                    String childHash = (String)ois.readObject();
                    
                    byte result = checkChildExist(childHash);
                    ServerRedWeb.GUI.log(String.valueOf(result));
                    os.write(result);
                    break;
                    
                case CMD_ADD_NEW_CHILD:
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    ServerGUI.log("connected for add new child.");
                    simCard = (String)ois.readObject();
                    String childName = (String)ois.readObject();
                    oos.writeObject(addNewChild(simCard,childName));
                    break;
                    
                case CMD_GET_CHILD_LIST:
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    
                    ArrayList<Child> obj = getChildList((String)ois.readObject());
                    oos.writeObject(obj);
                    System.out.println(obj);
                    break;
                    
                case CMD_UPDATE_PARENT_ONLINE:
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    
                    updateParentOnline((String)ois.readObject(),connectionDate);
                    break;
                    
                case CMD_UPDATE_CHILD_ONLINE:
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    
                    updateChildOnline((String)ois.readObject(),connectionDate);
                    break;
                    
                case CMD_ADD_CATEGORY:
                    
                    key = (String)ois.readObject();
                    
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    
                    
                    
                    simCard = (String)ois.readObject();
                    
                    String name = (String)ois.readObject();
                    
                    
                    byte[] data = (byte[])ois.readObject();
                    
                    addCategory(simCard,name,data);
                    
                    
                    
                    break;
                    
                case CMD_GET_CATEGORIES:
                    
                    key = (String)ois.readObject();
                    if(!key.contains(KEYPASS)){
                        printWrongKey(clientIP);
                        break;
                    }
                    simCard = (String)ois.readObject();
                    Parent p = Parent.importFromDatabase(ServerRedWeb.database, simCard);
                    
                    
                        Map<String,byte[]>map = ServerRedWeb.database.getCategories(p);
                        oos.writeObject(map);
                    
                    
                    break;
                    
                default:
                    ServerGUI.error("unknown operation from "+clientIP);
                    ServerGUI.error("Security Warning: some machine trying to hack the Server");
            }
            
            client.shutdownInput();
            client.shutdownOutput();
        }catch(Exception ex){
        
        }
        
    }
    
    
    
    
    public static void printWrongKey(String clientIP){
        ServerGUI.error("!!WARNING WRONG KEYPASS!! from: "+clientIP);
        ServerGUI.error("Security Warning: some machine trying to hack the Server.");
    } 
    
    
    /**
     * checkSimCard method checks if has SIM number
     * througth http://rlcellfinance system
     * 
     * @param simCard the SIM number to check if exist  
     * @return 1  - if the SIM card exist
     *         0  - if the SIM card not exist
     *         -1 - if has some error in request
     */
    
    public static byte checkSimCard(String simCard){
        
        if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected())
                return ServerRedWeb.database.checkSimCard(simCard);
       
        ServerGUI.error("SQL Error log: database is disconnected");
        return -1;
    }
    
    public static byte addParent(String simCard,Date registration){
        if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected()){
            try{
                ServerRedWeb.database.addParent(new Parent(simCard,registration));
                return 1;
            }catch(SQLException sqlex){
                
                ServerGUI.error("SQL Error Log:");
                ServerGUI.error("SQLException: "+sqlex);
                ServerGUI.error("Massage: "+sqlex.getMessage());
            
                for(Throwable t:sqlex){
                    ServerGUI.error("SQL Error Log: "+t.getMessage());
                }
                
                return -1;
            }
            
        }
        
        return -1;
    }
    
    
    public static byte checkChildExist(String hash){
        
        if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected()){
            try(ResultSet res= ServerRedWeb.database.getChildByHash(hash)){
                
                int counter = 0;
                while(res.next())
                    counter++;
                
                return (byte)(counter == 0 ? 6 : 7);
                
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }
        }
        
        return -1;
    }
    
    public static Child addNewChild(String simCard,String name){
        
        Child c = null;
        
        
        do{
            
            String hash = HashGenerator.generateKey();
            
            byte result = checkChildExist(hash);
            
            if(result == -1)
                return c;
            
            if(result == 6)
                continue;
            
            if(ServerRedWeb.database != null && ServerRedWeb.database.isConnected()){
                
                
                try{
                
                    Parent p = Parent.importFromDatabase(ServerRedWeb.database, simCard);
                
                    int id = ServerRedWeb.database.addchild(p.getId(),name,hash);
                    
                    ResultSet res = ServerRedWeb.database.getChildById(id);
                    
                    if(res.next()){
                        c = new Child(res.getInt("id"),
                        res.getInt("parent_id"),
                        res.getString("name"),
                        res.getString("hash"));
                    }
                    res.close();
                    
                    return c;
                }catch(SQLException sqlex){
                    ServerRedWeb.printSQLError(sqlex);
                    return c;
                }finally{
                }
            }
        }while(true);
    }
    
    public static void updateParentOnline(String parentSim,Date d){
        if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected()){
            try{
            ServerRedWeb.database.updateParentOnline(parentSim,d);
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }
        }else
            ServerRedWeb.GUI.log("database is not connected.");
    }
    
    public static void updateChildOnline(String childHash,Date d){
        if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected()){
            try{
            ServerRedWeb.database.updateChildOnline(childHash,d);
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }
        }else
            ServerRedWeb.GUI.log("database is not connected.");
    }
    
    public static ArrayList<Child> getChildList(String sim_card){
        try{
            if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected()){
                return ServerRedWeb.database.getChildList(sim_card);
            }
        }catch(SQLException ex){
            ServerRedWeb.printSQLError(ex);
            
        }
        return new ArrayList();
    }
    
    public static void addCategory(String simCard,String name,byte[] data){
        if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected()){
            
            try{
                ServerRedWeb.database.addCategory(simCard, name, data);
            }catch(SQLException sqlex){
                ServerRedWeb.printSQLError(sqlex);
            }
            
        }
    }
    
    private static JSONObject returnJson(String stringUrl, String params) throws JSONException,IOException{
        
            URL url = new URL(stringUrl);
            
            HttpURLConnection  con =(HttpURLConnection ) url.openConnection();
            
            con.setDoOutput(true);
            
            con.setDoInput(true);
            
            con.setRequestMethod("POST");
            con.getOutputStream().write(params.getBytes("UTF-8"));
            
            
            Scanner s = new Scanner(con.getInputStream());
            
            StringBuffer sb = new StringBuffer();
            
            while(s.hasNext()){
                sb.append(s.next());
            }
            
            System.out.println(sb);
            
            return new JSONObject(sb.toString());
    }
    
}

    
    
    
    
