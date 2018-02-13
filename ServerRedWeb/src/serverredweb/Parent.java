/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.lang.String;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author maxim.p
 */
public class Parent {
    
    private int id;
    private String simCard;
    private Date createdDate;
    
    private Parent(int id,String simCard,Date createdDate){
        this.id=id;
        this.simCard=simCard;
        this.createdDate=createdDate;
    }
    
    public int getId(){
        return id;
    }
    public String getSimCard(){
        return simCard;
    }
    public String getStringFormatDate(){
        return String.valueOf(createdDate.getYear()+1900)+'-'+(createdDate.getMonth()+1)+'-'+createdDate.getDate();
    }
    public Date getRegistredDate(){
        return createdDate;
    }
    
    public boolean isImported(){
        return id != 0;
    }
    
    public Parent(String simCard,Date createdDate){
        this.simCard=simCard;
        this.createdDate=createdDate;
    }
    
    public static Parent importFromDatabase(RedWebDB database,int id)throws SQLException{
        if(database==null||!database.isConnected())
            throw new SQLException("database is disconnected");
        
        Parent p=null;
        synchronized(database){
            try(ResultSet res = database.getParent(id);){
            
                String sim = res.getString("sim_card");
                Date registred = res.getDate("registration_date");
            
                p = new Parent(id,sim,registred);
                
            }catch(SQLException sqlex){
                System.out.println("SQL Error Log Exception: "+sqlex);
                System.out.println("SQL Error Log Massage: "+sqlex.getMessage());
            }
        }
        return p;
    }
    
    
    public static Parent importFromDatabase(RedWebDB database,String simCard)throws SQLException{
        if(database==null||!database.isConnected())
            throw new SQLException("database is disconnected");
        
        Parent p=null;
        synchronized(database){
            try(ResultSet res = database.getParent(simCard);){
                res.next();
                int id = res.getInt("id");
                String sim = res.getString("sim_card");
                Date registred = res.getDate("registration_date");
            
                p = new Parent(id,sim,registred);
            
            }catch(SQLException sqlex){
                sqlex.printStackTrace();
                System.out.println("SQL Error Log Exception: "+sqlex);
                System.out.println("SQL Error Log Massage: "+sqlex.getMessage());
            }
        }
        
        return p;
    }
    
    
}
