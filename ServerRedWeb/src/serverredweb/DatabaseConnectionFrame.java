/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author maxterminatorx
 */
public class DatabaseConnectionFrame extends javax.swing.JFrame {

    /**
     * Creates new form DatabaseConnectionFrame
     */
    
    private boolean allTablesExist;
    
    public boolean allTablesExist(){
        return allTablesExist;
    }
    
    public DatabaseConnectionFrame() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
        ImageIcon img = new ImageIcon(getClass().getResource("db_connection128.png"));
        
        imgLabel.setIcon(img);
        
        this.setIconImage(img.getImage());
        this.setLocationRelativeTo(null);
        
        
        
        this.databaseTxt.setText(DatabaseController.DEFAULT_MYSQL_DATABASE);
        this.usernameTxt.setText(DBKeys.APP_DATA_USER);
        this.passwordTxt.setText(DBKeys.APP_DATA_PASS);
        
        this.sqlHostTxt.setText(DatabaseController.DEFAULT_MYSQL_LOCALHOST);
        this.sqlPortTxt.setText(String.valueOf(DatabaseController.DEFAULT_MYSQL_PORT));
        
        
        missingTablesLabel.setVisible(false);
        fixItBtn.setVisible(false);
        
        btnConnect.addActionListener(this::connectToDatabase);
        this.fixItBtn.addActionListener(this::fixTables);
        
    }
    
    private void connectToDatabase(ActionEvent e){
        btnConnect.setEnabled(false);
        
        
        if(ServerRedWeb.database!=null&&ServerRedWeb.database.isConnected()){
            try{
                ServerRedWeb.database.close();
                btnConnect.setText("CONNECT");
                this.databaseTxt.setEnabled(true);
                this.usernameTxt.setEnabled(true);
                this.passwordTxt.setEnabled(true);
                this.sqlPortTxt.setEnabled(true);
                this.sqlHostTxt.setEnabled(true);
                btnConnect.setEnabled(true);
                allTablesExist = false;
                return;
            }catch(SQLException sqlex){
                JOptionPane.showMessageDialog(this, "there is an problem to disconnect from the database.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
                ServerRedWeb.printSQLError(sqlex);
            }
        }
        
        ServerGUI.log("SQL Log: waiting for connection to MySQL Database...");
        
        String database = databaseTxt.getText()
                ,user = usernameTxt.getText()
                ,password = String.valueOf(passwordTxt.getPassword())
                ,port = sqlPortTxt.getText(),
                host = sqlHostTxt.getText();
        
        if(database.isEmpty()||
                user.isEmpty()||
                password.isEmpty()||
                port.isEmpty()||
                host.isEmpty()){
            
            JOptionPane.showMessageDialog(this, "there are empty fields!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            btnConnect.setEnabled(true);
            return;
        }
        
        
        
        
        
        try{
            //ServerRedWeb.database.connect();
            ServerRedWeb.database.connect(user, password, host, Integer.valueOf(port), database, true);
            ServerGUI.log("SQL Log: Connection ssucceded to database.");
            btnConnect.setText("DISCONNECT");
            this.databaseTxt.setEnabled(false);
            this.usernameTxt.setEnabled(false);
            this.passwordTxt.setEnabled(false);
            this.sqlPortTxt.setEnabled(false);
            this.sqlHostTxt.setEnabled(false);
            
        }catch(NullPointerException npe){
            ServerGUI.error("SQL Log: The server is offline.");
            btnConnect.setEnabled(true);
        }catch(SQLException sqlex){
            ServerRedWeb.printSQLError(sqlex);
            btnConnect.setEnabled(true);
        }
        
        
        if(!ServerRedWeb.database.checkRequiredTables()){
            missingTablesLabel.setVisible(true);
            fixItBtn.setVisible(true);
            fixItBtn.setEnabled(true);
            allTablesExist = false;
        }else{
            allTablesExist = true;
        }
        
        btnConnect.setEnabled(true);
    }
    
    
    private void fixTables(ActionEvent e){
        fixItBtn.setEnabled(false);
        if(!ServerRedWeb.database.fixTables()){
            fixItBtn.setEnabled(true);
            JOptionPane.showMessageDialog(this,"Error in initializing the Reqired Tables.");
            allTablesExist = false;
        }else{
            ServerGUI.log("Required Tables was created Successfully.");
            fixItBtn.setVisible(false);
            this.missingTablesLabel.setVisible(false);
            allTablesExist = true;
        }
        
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnConnect = new javax.swing.JButton();
        imgLabel = new javax.swing.JLabel();
        missingTablesLabel = new javax.swing.JLabel();
        fixItBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        passwordLabel = new javax.swing.JLabel();
        sqlHostLabel = new javax.swing.JLabel();
        sqlPortLabel = new javax.swing.JLabel();
        databaseTxt = new javax.swing.JTextField();
        usernameTxt = new javax.swing.JTextField();
        passwordTxt = new javax.swing.JPasswordField();
        sqlHostTxt = new javax.swing.JTextField();
        sqlPortTxt = new javax.swing.JTextField();
        userNameLabel = new javax.swing.JLabel();
        dataBaseLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnConnect.setText("CONNECT");

        imgLabel.setText("");

        missingTablesLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        missingTablesLabel.setForeground(new java.awt.Color(255, 0, 0));
        missingTablesLabel.setText("There are some Tables missing");

        fixItBtn.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        fixItBtn.setLabel("Fix It!");

        passwordLabel.setText("Password:");

        sqlHostLabel.setText("MySQL host:");

        sqlPortLabel.setText("MySQL Port:");

        databaseTxt.setText("");
        databaseTxt.setSize(100, databaseTxt.getSize().height);

        usernameTxt.setText("");
        usernameTxt.setSize(100, usernameTxt.getSize().height);

        passwordTxt.setText("");
        passwordTxt.setSize(100, passwordTxt.getSize().height);

        sqlHostTxt.setText("");

        sqlPortTxt.setText("");

        userNameLabel.setText("User Name:");

        dataBaseLabel.setText("Database Name:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                    .addComponent(sqlHostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sqlPortLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dataBaseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sqlPortTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sqlHostTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(passwordTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(usernameTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(databaseTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dataBaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(databaseTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sqlHostLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sqlHostTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sqlPortLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sqlPortTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(missingTablesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fixItBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(missingTablesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fixItBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        missingTablesLabel.getAccessibleContext().setAccessibleName("There are some Tables missing!");

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JLabel dataBaseLabel;
    private javax.swing.JTextField databaseTxt;
    private javax.swing.JButton fixItBtn;
    private javax.swing.JLabel imgLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel missingTablesLabel;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordTxt;
    private javax.swing.JLabel sqlHostLabel;
    private javax.swing.JTextField sqlHostTxt;
    private javax.swing.JLabel sqlPortLabel;
    private javax.swing.JTextField sqlPortTxt;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JTextField usernameTxt;
    // End of variables declaration//GEN-END:variables
}
