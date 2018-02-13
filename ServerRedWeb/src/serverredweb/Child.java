/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.lang.String;
import java.io.Serializable;

/**
 *
 * @author maxterminatorx
 */
public class Child implements Serializable{
    
    
    
    transient private int id;
    transient private int parentId;
    private String name;
    private String hash;
    
    
    public Child(int id,int parentId,String name,String hash){
        this.id=id;this.parentId=parentId;this.name=name;this.hash=hash;
    }
    
    
    public String getName(){
        return name;
    }
    
    public String getHash(){
        return hash;
    }
    
    
    
    
    private static final long serialVersionUID = 0x1DD3_8F99_331A_ABCDL;
}
