/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;


import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;
import java.lang.String;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.function.DoubleToIntFunction;
import java.util.function.IntFunction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author maxterminatorx
 */
public class ServerRedWeb {

    
    static ServerSocket server;
    static RedWebDB database;
    public static ServerGUI GUI;
    static boolean showAnim = false;
    
    
    private static final String APP_ID= "soviet_server_reactor";
    
    public static void main( String[] args) {
        //Maxim123.printMax();
        //database.setRequiredTables();
        boolean alreadyRunning;
        try {
            JUnique.acquireLock(APP_ID, new MessageHandler() {
                public String handle(String message) {
                    // A brand new argument received! Handle it!
                    return null;
                }
            });
            alreadyRunning = false;
        } catch (AlreadyLockedException e) {
            alreadyRunning = true;
        }
        if (alreadyRunning) {
            for (int i = 0; i < args.length; i++) {
                String sendMessage = JUnique.sendMessage(APP_ID, args[0]);
            }
            JOptionPane.showMessageDialog(null, "There are already running instance of of the Soviet Server Reactor",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        
        
        
        
        
        
        
        if(args.length == 3)
            showAnim=true;
        
        GUI = new ServerGUI();
        /*
        String useIP = "192.168.1.251";
        int usePort = 3000;
        
        InetSocketAddress useAddress;
        
        
        if(args.length == 2 || args.length == 3){
            
            try{
                
                usePort = Integer.valueOf(args[1]);
                if(usePort > 0X0000ffff || usePort < 0){
                    ServerGUI.error("Port Error Format: Port must be between 0 and "+0X0000ffff);
                    usePort = 3000;
                    ServerGUI.log("Using default Port: "+usePort);
                }
                
                
                
                useAddress = new InetSocketAddress(InetAddress.getByName(args[0]),usePort);
                
            }catch(NumberFormatException ex){
                ServerGUI.error("Port Error Format: "+ex.getMessage());
                ServerGUI.log("Using default Port: "+usePort);
                useAddress = new InetSocketAddress(useIP,usePort);
            }
            catch(UnknownHostException uhex){
                ServerGUI.error("IP Host Error: "+uhex.getMessage());
                ServerGUI.log("Using default eHost: "+useIP);
                
                useAddress = new InetSocketAddress(useIP,usePort);
            }
            
             
            
            
        }
        else if(args.length == 1){
            ServerGUI.log("No Port parameter detected using default Port: "+usePort);
            
            try{
                 useAddress = new InetSocketAddress(InetAddress.getByName(args[0]),usePort);
            }catch(UnknownHostException uhex){
                ServerGUI.error("IP Host Error: "+uhex.getMessage());
                ServerGUI.log("Using default Host: "+useIP);
                
                useAddress = new InetSocketAddress(useIP,usePort);
            }
        }else{
            ServerGUI.log("No IP address parameter detected using default Host: "+useIP);
            ServerGUI.log("No port parameter detected using default Port: "+usePort);
            
            useAddress = new InetSocketAddress(useIP,usePort);
            
        }*/
        
        
        new Thread(){
            
            
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(15000);
                        GUI.log("update connections.");
                        String sss = null;
                        
                    }catch(InterruptedException iex){
                    
                    }
                    if(database!=null&&database.isConnected()&&GUI.allTablesExist()){
                        try{
                            database.updateConnections();
                            GUI.log("connections updated.");
                        }catch(SQLException sqlex){
                            sqlex.printStackTrace();
                            printSQLError(sqlex);
                        }
                    }
                }
            }
        }.start();
        
        
        
        Scanner scan = new Scanner(System.in);
        
        String command = "";
        
        while(!command.equals("exit")){
            command = scan.next();
            switch(command){
                case "gui":
                    if(GUI == null){
                        GUI = new ServerGUI();
                        ServerGUI.log("Soviet Server Reactor GUI activated.");
                    }else if(!GUI.isVisible()){
                        GUI.setVisible(true);
                        ServerGUI.log("Soviet Server Reactor GUI activated.");
                    }
                    else
                        ServerGUI.log("Soviet Server Reactor GUI already running.");
                    break;
                case "+anim":
                    showAnim = true;
                    if(GUI!=null)
                        GUI.repaint();
                    ServerGUI.log("anim ON.");
                    
                    break;
                case "-anim":
                    showAnim = false;
                    if(GUI!=null)
                        GUI.repaint();
                    ServerGUI.log("anim OFF.");
                    break;
                case "sql":
                    if(database==null||!database.isConnected()){
                        System.out.println("database not avalable.");
                        break;
                    }
                    System.out.println("exec - executable query. (like DELECT,INSERT,UPDATE,...)");
                    System.out.println("res - resultable query. (like SELECT)");
                    switch(scan.next()){
                        case "exec":
                            scan.nextLine();
                            System.out.print("query: ");
                            int rowsAffected = database.flowQueryExec(scan.nextLine());
                            if(rowsAffected>=0){
                                System.out.println("query executed successfully.");
                                System.out.println("there are was "+rowsAffected+" rows affected.");
                            }else{
                                System.out.println("failed to execute query.");
                            }
                            break;
                        case "res":
                            scan.nextLine();
                            System.out.print("query: ");
                            System.out.println(database.flowQueryResult(scan.nextLine()));
                    }
                    
            }
        }
        
        
        
        System.out.println("Soviet Server Reactor was stopped.");
        System.exit(0);
        
    }
    
    
    
    public static void startServer(InetSocketAddress host){
        try(ServerSocket server = new ServerSocket();
                DatabaseController database = new RedWebDB();){
            server.bind(host);
            ServerGUI.log("Soviet Server Reacstor v0.8 BETA is started run on address: "+server.getInetAddress().getHostAddress());
            ServerGUI.log("Soviet Server Reactor v0.8 BETA is started run on port: "+server.getLocalPort());
            
            
            
            ServerRedWeb.server = server;
            if(GUI != null){
                GUI.setServerData(server);
            }
            
            
            ServerRedWeb.database=(RedWebDB)database;
            
            synchronized(ServerRedWeb.class){
                if(GUI!=null)
                    synchronized(GUI){
                        GUI.enableRestart();
                    }
            }
            
            
            while(true){
                listenToClients().start();
            }
            
            
        }catch(IOException ioex){
            
            ServerGUI.error("Server Error:");
            ServerGUI.error("IOException: "+ioex);
            ServerGUI.error("Massage: "+ioex.getMessage());
            
            synchronized(ServerRedWeb.class){
                GUI.enableRestart();
            }
            
            
        }catch(SQLException sqlex){
            
            printSQLError(sqlex);
            
            ServerGUI.log("SQL Log: the connection to MySQL was failed\n"
                +"see in Server Error Log the reason");
            
            synchronized(ServerRedWeb.class){
                GUI.enableRestart();
            }
        }
    }
    
    
    
    public static ClientThread listenToClients()throws IOException{
        Socket client = server.accept();
        return new ClientThread(client);
    }
    
    public static void printSQLError(SQLException sqlex){
        ServerGUI.error("SQL Error Log:");
        ServerGUI.error("SQLException: "+sqlex);
        ServerGUI.error("Massage: "+sqlex.getMessage());
        ServerGUI.error("SQL State: "+sqlex.getSQLState());
        ServerGUI.error("SQL Error Code: "+sqlex.getErrorCode());
            
        for(Throwable t:sqlex)
            ServerGUI.error("SQL Error Log: "+t.getMessage());
        
        sqlex.printStackTrace();
        
        
    }
}