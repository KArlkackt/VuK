/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Jannik
 */
public class SwingGUI implements UserInterface {

    private String nickname;
    private JFrame mainFrame;
    private JTextArea output;
    private JTextField input;
    private JButton close;
    private ClientProxy handle;
    private ChatProxy session;
    private ChatServer server;
    private ChatClient client;

    @Override
    public void showMessage(String username, String message) throws RemoteException {        
        output.append(username + ": " + message + "\n");
        output.setCaretPosition(output.getText().length() - 1);
    }

    @Override
    public String getUsername() throws RemoteException {
        {
            try {
                String name = JOptionPane.showInputDialog(null, "Please, enter your nickname!");
                if (name != null && name.trim().length() > 0) {
                    nickname = name;
                    return name;
                } else if (name == null) {
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Please choose a nickname with more than one letter!");
                    getUsername();
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.exit(0);
            }

            return null;
        }
    }

    @Override
    public void run(ChatProxy chat, ChatClient client) throws RemoteException {
        session = chat;
        server = chat.getServer();
        handle = chat.getHandle();
        this.client = client;
        
        mainFrame = new JFrame();
        mainFrame.setTitle(nickname);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        output = new JTextArea();
        output.setEditable(false);
        JScrollPane scroller = new JScrollPane();
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getViewport().setView(output);
        mainFrame.getContentPane().add(scroller, BorderLayout.CENTER);
        input = new JTextField();
        mainFrame.getContentPane().add(input, BorderLayout.NORTH);


        close = new JButton("close");
        setListeners();
        mainFrame.getContentPane().add(close, BorderLayout.SOUTH);

        mainFrame.setSize(400, 300);
        mainFrame.setVisible(true);
    }

    private void setListeners() {
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = input.getText();
                    if(!text.equals("")){
                        client.sendMessage(text);
                        input.setText("");
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    close();
                } catch (RemoteException ex) {
                    Logger.getLogger(SwingGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public void close() throws RemoteException {
        server.unsubscribeUser(session);
        System.exit(0);
    }
}
