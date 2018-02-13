/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverredweb;

import java.lang.String;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import javax.swing.JOptionPane;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.SQLException;


import java.util.ArrayDeque;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import static serverredweb.ServerRedWeb.GUI;
import static serverredweb.ServerRedWeb.startServer;

/**
 *
 * @author maxim.p
 */
public class ServerGUI extends javax.swing.JFrame implements java.awt.event.WindowListener{
    
    public boolean allTablesExist(){
        return((DatabaseConnectionFrame)(this.sqlConnection)).allTablesExist();
    }
    
    public void enableRestart() {
        jButton3.setEnabled(true);
    }
    
    public void disableRestart() {
        jButton3.setEnabled(false);
    }
    
    
    
    
    private void startServer(ActionEvent e){
        
        
        
        disableRestart();
        String textHost = txtHost.getText();
        //txtHost.addInputMethodListener(l);
                
        String textPost = txtPort.getText();
        
        synchronized(ServerGUI.class){
            if(ServerRedWeb.server != null && !ServerRedWeb.server.isClosed()&&currentServerThread!=null){
                try{
                    ServerRedWeb.server.close();
                    currentServerThread.stop();
                }catch(IOException ioex){
                    JOptionPane.showMessageDialog(this, "there is an problem to close the server!");
                }
            }
        }
        
        
        if(textHost.isEmpty()||textPost.isEmpty()){
            JOptionPane.showMessageDialog(this, "host or port are missing!");
            synchronized(ServerRedWeb.class){
                ServerRedWeb.GUI.enableRestart();
            }
            return;
        }
        
        
        InetSocketAddress host = null;
        
        try{
            
            host =  new InetSocketAddress(InetAddress.getByName(textHost),Integer.valueOf(textPost));
            
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "port format must be between 0 and 65535!");
            return;
        }
        catch(UnknownHostException ex){
            JOptionPane.showMessageDialog(this, "illegal host!");
            return;
        }
        catch(IllegalArgumentException ex){
            JOptionPane.showMessageDialog(this, "there is an exception!");
            return;
        }finally{
            
        }
        
        
        
        
        
            startServer(host);
            this.btnSQL.setEnabled(true);
        
        //
        
        //ServerRedWeb.startServer(host);
    }
    
    
    private void useDefaultHost(java.awt.event.ItemEvent e){
        if(e.getStateChange()==e.SELECTED){
            txtHost.setText(System.getenv("USERDOMAIN"));
            txtHost.setEnabled(false);
            return;
        }
        
        txtHost.setText("");
        txtHost.setEnabled(true);
        
        
        
    }
    
    
    private static Thread currentServerThread;
    
    
    
    
    
    
    
    static void startServer(InetSocketAddress host){
        
        class LocalThread extends Thread{
            InetSocketAddress host;
            LocalThread(InetSocketAddress host){
                this.host=host;
            }
        }
        
        (currentServerThread = new LocalThread(host){
            
            @Override
            public void run(){
                ServerRedWeb.startServer(host);
            }
            
        }).start();
    }
    
    
    static final short MAX_LOGS = 100;
    
    static final ArrayDeque<String> LOGS = new ArrayDeque();
    
    static final ArrayDeque<String> ERR_LOGS = new ArrayDeque();
    
    static String edit(Object obj,ArrayDeque deq){
        if(deq.size() >= MAX_LOGS)
            deq.removeFirst();
        deq.add(obj);
        
        StringBuffer sb = new StringBuffer();
        for(Object o:deq)
            sb.append(o).append('\n');
        
        return sb.toString();
        
    }
    
    private static ServerGUI registred;
    
    public static void log(String msg){
        if(registred!=null&&registred.isVisible()){
            SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                
                
                registred.jTextArea1.setText(edit(msg,LOGS));
                
                registred.jTextArea1.setCaretPosition(registred.jTextArea1.getDocument().getLength());
                
                
                
            }
        });
            return;
        }
        System.out.println(msg);
        
        
        
    }
    
    

    
    public static void error(String msg){
        if(registred!=null&&registred.isVisible()){
            SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                
                
                registred.jTextArea2.setText((edit(msg,ERR_LOGS)));
                registred.jTextArea2.setCaretPosition(registred.jTextArea2.getDocument().getLength());
            }
        });
            return;
        }
        System.out.println(msg);
    }
    
    @Override
    public void setVisible(boolean flag){
        super.setVisible(flag);
        requestFocus();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
       //System.out.println("Soviet Server Reactor GUI deactivated.");
       int okCxl = JOptionPane.showConfirmDialog(this, passfield, "Enter Password for Exit.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
       
       
       
       if(okCxl == JOptionPane.OK_OPTION){
            String pass = new String(passfield.getPassword());
            if(pass!=null&&pass.equals("1234"))
            System.exit(0);
       }
       passfield.setText("");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
    
    static ClassLoader cl = ClassLoader.getSystemClassLoader();

    /**
     * Creates new form ServerGUI
     */
    private Color textareaColor;
    
    private javax.swing.JPasswordField passfield;
    
    private JFrame sqlConnection;
    
    public ServerGUI() {
        
        String os = System.getenv("OS");
        initComponents();
        
        passfield = new javax.swing.JPasswordField();
        passfield.setSize(200, 80);
        
        
        if(registred == null)
            registred = this;
        
        bgimg = new javax.swing.ImageIcon(getClass().getResource("/serverredweb/bg.png")).getImage();
        ico = new javax.swing.ImageIcon(getClass().getResource("/serverredweb/iconssr.png"));
        iconimg = ico.getImage();
        
        
        
        jTextArea1.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                
              
            }
            
        });
        
        
        jTextArea2.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                
            }
            
        });
        
        
        
        
        this.setTitle("Soviet Server Reactor v0.8 BETA");
        
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.setIconImage(iconimg);
        this.addWindowListener(this);
        
        sqlConnection = new DatabaseConnectionFrame();
        
        
        btnSQL.setEnabled(false);
        
        btnSQL.addActionListener((event)->{
            
            sqlConnection.setVisible(true);
            
            
        });
        
        
        this.setVisible(true);
        ServerGUI.log("OS: "+os);
        this.setLocationRelativeTo(null);
        requestFocus();
    }
    
    private Image bgimg,iconimg;
    private ImageIcon ico;
    
    
    public void setServerData(ServerSocket server){
        if(server == null)
            return;
        
        this.jLabel1.setText("Asocciated Host : "+server.getInetAddress().getHostName());
        this.jLabel2.setText("IP Address            : "+server.getInetAddress().getHostAddress());
        this.jLabel5.setText("Port                         : "+server.getLocalPort());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel(){
            @Override
            public void paint(java.awt.Graphics g){
                jTextArea1.setSize(jTextArea1.getWidth(), ServerGUI.this.getHeight()/3);
                g.setColor(java.awt.Color.WHITE);
                g.fillRect(0, 0, ServerGUI.this.getWidth(), ServerGUI.this.getHeight());
                if(ServerRedWeb.showAnim)
                g.drawImage(ServerGUI.this.bgimg, 0, 0,ServerGUI.this.getWidth(),ServerGUI.this.getHeight(), null);
                super.paintComponents(g);
            }

        };
        btnSQL = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnCloseGUI = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        txtHost = new javax.swing.JTextField();
        txtPort = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        btnSQL.setText("MySQL Connection.");
        btnSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSQLActionPerformed(evt);
            }
        });

        if(ServerRedWeb.server == null)
        jLabel1.setText("");
        else
        jLabel1.setText("Asocciated Host : "+ServerRedWeb.server.getInetAddress().getHostName());
        jLabel1.setBackground(java.awt.Color.BLACK);
        jLabel1.setOpaque(true);
        jLabel1.setForeground(java.awt.Color.WHITE);

        btnCloseGUI.setText("close GUI.");

        if(ServerRedWeb.server == null)
        jLabel2.setText("");
        else
        jLabel2.setText("IP Address            : "+ServerRedWeb.server.getInetAddress().getHostAddress());
        jLabel2.setBackground(java.awt.Color.BLACK);
        jLabel2.setOpaque(true);
        jLabel2.setForeground(java.awt.Color.WHITE);

        jButton3.setText("Restart Server");
        jButton3.addActionListener(this::startServer);

        txtHost.setText("");

        txtPort.setText("");
        txtPort.addKeyListener(new KeyAdapter(){

            public void keyReleased(KeyEvent e){
                String text = txtPort.getText();
                StringBuffer sb = new StringBuffer();

                for(int i=0;i<text.length();i++){
                    char currentChar = text.charAt(i);
                    if(currentChar>='0'&&currentChar<='9')
                    sb.append(currentChar);
                }

                text = sb.toString();

                if(text.isEmpty()){
                    txtPort.setText("");
                    return;
                }

                if(text.length()>5){
                    txtPort.setText(String.valueOf(0xffff));
                    return;
                }

                if(Integer.valueOf(text)>0Xffff){
                    txtPort.setText(String.valueOf(0xffff));
                    return;
                }

                txtPort.setText(text);

            }

        });

        jLabel3.setText("Server Log:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        jTextArea1.setBackground(Color.BLACK);
        jTextArea1.setForeground(Color.GREEN);
        jTextArea1.setEditable(false);

        DefaultCaret caret1 = (DefaultCaret)jTextArea1.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        jLabel4.setText("Server Error Log:");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);
        jTextArea2.setBackground(Color.BLACK);
        jTextArea2.setForeground(Color.RED);
        jTextArea2.setEditable(false);

        DefaultCaret caret2 = (DefaultCaret)jTextArea2.getCaret();
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        if(ServerRedWeb.server == null)
        jLabel5.setText("");
        else
        jLabel5.setText("Port                         : "+ServerRedWeb.server.getLocalPort());

        jCheckBox1.setText(":use environment host");
        jCheckBox1.addItemListener(this::useDefaultHost);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPort, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                            .addComponent(txtHost))
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 293, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCloseGUI, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSQL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSQL, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnCloseGUI, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jLabel5.setOpaque(true);

        jLabel5.setBackground(Color.BLACK);
        jLabel5.setForeground(Color.WHITE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSQLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSQLActionPerformed

    /**
     * @param args the command line arguments
     */
    /*
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerGUI().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloseGUI;
    private javax.swing.JButton btnSQL;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables

    
}
