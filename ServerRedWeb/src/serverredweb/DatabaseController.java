/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.lang.String;
import java.sql.SQLException;
import java.util.Map;
import java.io.Closeable;

/**
 *
 * @author maxim.p
 */
public interface DatabaseController extends AutoCloseable{
    
    
    /**
     * default data connection
     */
    
    String DEFAULT_MYSQL_PROTOCOL = "jdbc:mysql://";
    
    String DEFAULT_MYSQL_LOCALHOST = "localhost";
    
    String DEFAULT_MYSQL_DATABASE = "app_database";
    
    int DEFAULT_MYSQL_PORT = 3306;
    
    
    /**
     * quireis for create tables
     */
    
    
    String QUIRY_CREATE_PARENTS_TABLE = "CREATE TABLE `parents` ( `id` INT(10) UNSIGNED NOT NULL "+
                                        "AUTO_INCREMENT , `sim_card` VARCHAR(19) CHARACTER SET utf8 COLLATE "+
                                        "utf8_general_ci NOT NULL , `registration_date` DATE NOT NULL , `using_app` "+
                                        "TINYINT(1) NOT NULL DEFAULT '0' , `last_connected` DATETIME NOT NULL DEFAULT "+
                                        "CURRENT_TIMESTAMP , PRIMARY KEY (`id`), UNIQUE (`sim_card`)) ENGINE = InnoDB;";
    
    String QUIRY_CREATE_CHILDREN_TABLE = "CREATE TABLE `children` ( `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,"+
                                         "`parent_id` INT(10) UNSIGNED NOT NULL , `name` VARCHAR(32) CHARACTER SET utf8 "+
                                         "COLLATE utf8_general_ci NOT NULL , `hash` VARCHAR(10) CHARACTER SET utf8 "+
                                         "COLLATE utf8_general_ci NOT NULL , `using_app` TINYINT(1) NOT NULL DEFAULT '0'"+
                                         ", `last_connected` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY "+
                                         "(`id`), UNIQUE (`hash`)) ENGINE = InnoDB;";
    
    String QUIRY_CREATE_CATEGORIES_TABLE = "CREATE TABLE `categories` ( `id` INT(12) UNSIGNED NOT NULL AUTO_INCREMENT ,"+
                                           "`parent_id` INT(10) UNSIGNED NOT NULL , `title` VARCHAR(14) CHARACTER SET utf8 "+
                                           "COLLATE utf8_general_ci NOT NULL , `image` MEDIUMBLOB NULL DEFAULT NULL ,"+
                                           "PRIMARY KEY (`id`)) ENGINE = InnoDB;";
    
    String QUIRY_CREATE_CELLS_TABLE = "CREATE TABLE `cells` ( `id` INT(12) NOT NULL AUTO_INCREMENT ,"+
                                      "`category_id` INT(12) NOT NULL , `image` MEDIUMBLOB NULL DEFAULT NULL ,"+
                                      "`sound` MEDIUMBLOB NULL DEFAULT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
    
    
    
    
    void connect(String user,String password,String host)throws SQLException;
    
    void connect(String user,String password)throws SQLException;
    
    void connect()throws SQLException;
    
    
    
    
    boolean isConnected();
    
    
    
    @Override
    void close() throws SQLException;
}
