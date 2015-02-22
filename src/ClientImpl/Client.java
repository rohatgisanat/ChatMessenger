package ClientImpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class Client extends javax.swing.JFrame {  //Client class

    PrintWriter printToServer;
    BufferedReader readFromServer,readUserName;
    String str;
    Socket sock;
  
    Client() {
    }

    public Client(Socket soc) throws IOException {          //Constructor
        initComponents();
        sock = soc;
        this.printToServer = new PrintWriter(soc.getOutputStream(), true);
        this.readFromServer = new BufferedReader(new InputStreamReader(soc.getInputStream()));       
         printToServer.println(GetName());
        this.setVisible(true);
        mainChat();

    }
    
    
    String GetName() throws FileNotFoundException, IOException{ // Reads Username from file
       readUserName=new BufferedReader(new FileReader("C:\\\\Users\\\\user\\\\Documents\\\\NetBeansProjects\\\\JAVA\\\\ChatMessenger\\\\src\\\\UserName.txt"));
       return readUserName.readLine();
    }

    void GetUserName() throws IOException{   //Change user name from menu
       final JDialog jf=new JDialog();
       JPanel text =new JPanel();
       JLabel label=new JLabel("Enter your username here. Changes will be reflected next time the program starts.");
       final JTextField TextField=new JTextField(GetName());
       text.add(label);
       text.add(TextField);
       jf.setTitle("UserName");
       JPanel addButton= new JPanel();
       JButton submit =new JButton("Submit");
       addButton.add(submit);
       jf.add(text,BorderLayout.NORTH);
       jf.add(addButton,BorderLayout.SOUTH);
       jf.setMinimumSize(new Dimension(600,120));
       jf.setVisible(true);
       
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                    try (PrintWriter fileprint = new PrintWriter(new File("C:\\\\Users\\\\user\\\\Documents\\\\NetBeansProjects\\\\JAVA\\\\ChatMessenger\\\\src\\\\UserName.txt"))) {
                        fileprint.print(TextField.getText());
                    } catch (FileNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                     jf.dispose();
                
            
            }
           
        });
       jf.setVisible(true);
    }
    
    void exit() throws IOException {    // Exit message when the client gets disconnected
       
        JDialog dialog = new JDialog();
        JTextField errorMessage = new JTextField();
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(HIDE_ON_CLOSE);
        printToServer.println("EXIT");
        dialog.setTitle("Exit Message");
        dialog.setResizable(false);
        dialog.setMinimumSize(new Dimension(400, 200));
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        errorMessage.setEnabled(false);
        errorMessage.setDisabledTextColor(Color.black);
        errorMessage.setEditable(false);
        errorMessage.setSize(new Dimension(800, 800));
        errorMessage.setHorizontalAlignment(JTextField.CENTER);
        errorMessage.setText("You Have Been Disconnected!");
        dialog.add(errorMessage);
        printToServer.close();
        dialog.setVisible(true);
        sock.close();
        super.dispose();
        System.exit(0);

    }

    public void mainChat() throws IOException {    
           
        while (readFromServer.ready()) {       // Wait till data is received from the server
            try {
                
                if ((str = readFromServer.readLine()) != null) {
                    
                    if(str.startsWith("Connection Asked:")){  // If other User tries to coneect to this user
                             printToServer.println(str.substring("Connection Asked:".length()));
                    }
                    if (str.startsWith("Connected to:")) {
                        jLabel2.setText(str.substring(13, str.length()));
                    } else if (str.startsWith("Broadcast:")) {  // Data about members online
                        if (str.equals("Broadcast:1")) {
                            jTextArea3.setText("");
                        } else {
                            jTextArea3.append(str.substring(10, str.length()) + "\n");
                        }
                    } else if (str.equals("DISCONNECTED")) {   //On being disconnected
                        exit();  //Exit function
                        return;
                    } else {

                        jTextArea1.append("\n"+"<USER:>" + str);// Show chat data on the screen
                    }
                }
            }
            catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     void msgWindow(String msg, String title) {  // Message Window for help and error
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JEditorPane errorMessage = new JEditorPane();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle(title);
        dialog.setResizable(true);
        errorMessage.setContentType("text/html");
        dialog.setMinimumSize(new Dimension(400, 200));
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        errorMessage.setEnabled(false);
        errorMessage.setEditable(false);
        errorMessage.setSize(new Dimension(800, 800));
        errorMessage.setText(msg);
        errorMessage.setDisabledTextColor(Color.black);
        dialog.add(errorMessage);
        dialog.setVisible(true);
    }   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CHAT CLIENT");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setOpaque(false);
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea2KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea2);

        jLabel1.setText("CONNECTED TO:");

        jLabel2.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Kalinga", 1, 10)); // NOI18N
        jLabel3.setText("ONLINE RIGHT NOW:");

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jTextArea3.setOpaque(false);
        jScrollPane4.setViewportView(jTextArea3);

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jMenu1.setText("File");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Change User Name");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Clear Chat");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem3.setText("Help");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addComponent(jSeparator1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                        .addGap(133, 133, 133))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>                        

    private void jTextArea2KeyPressed(java.awt.event.KeyEvent evt) {          //The typing window. The data is send when the user presses enter key                              
        try {
            mainChat();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            str = jTextArea2.getText();
            jTextArea1.append("\n" +"<YOU:>" + str);
            printToServer.println(str);
            jTextArea2.setText("");
            jTextArea2.moveCaretPosition(0);
            

       }
    }                                     

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {                                           

        try {
            exit();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }       //Exit button in menu                                   

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        jTextArea1.setText(""); 
    }       // Clear chat option                                   

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        msgWindow("<html><h3>HELP</h3><ul>"
                + "<li> Exit by using the Exit in Menu.</li>"
                + "<li> You can Type \"EXIT\" while chat to exit.</li>"
                + "<li> Once  the chat is disconnected you have to start the messenger again.</li>"
                + "<li> Change your username using the change usernae option in Menu.</li>"
                + "<li> Once you enter the messenger then type the User name you want to connect to in the window.</li>"
                + "<li>ENJOY!</li></ul>"
                + "<b>Made By Sanat Rohatgi</b></html>", "Help"); 
    }       //Help window                                   

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        try {
            GetUserName();    
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }         //Change Username option                                 

    public static void main(String args[]) throws UnknownHostException {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {    //Thread starts
                try {
                    Client obj1;
                    InetAddress add = InetAddress.getByName("rohatgisanat"); //check the address of server host
                                                                             //(might change) user to user.
                    obj1 = new Client(new Socket(add, 444));                // connecting to server  on port 444
                } catch (Exception ex) {
                    new Client().msgWindow("Sorry!! Could Not Connect To The Server."  // Error if not connected
                            + "Try Again Later", "Error!");
                    System.exit(0);
                }

            }

        });

    }           //Main function

   
    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    // End of variables declaration                   
}
