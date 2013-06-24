/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 *
 * @author student
 */
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import javax.swing.*;

public class ChatClient extends JFrame {

    JTextArea output;
    JTextField input;
    ClientProxy handle;
    ChatProxy session;
    String nickname;
    ChatServer server;

    public ChatClient(String nickname) throws Exception {
        server = (ChatServer) Naming.lookup("ChatServer");
        handle = new ClientProxyImpl(this);
        session = server.subscribeUser(nickname, handle);
//        boolean b = false;
//        while (!b) {
//            if (server.subscribeUser(nickname, handle) != null) {
//                b = true;
//                session = server.subscribeUser(nickname, handle);
//            } else {
//                JOptionPane.showMessageDialog(null, "Please choose another name! ");                
//            }
//
//        }

        setTitle(nickname);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        output = new JTextArea();
        output.setEditable(false);
        JScrollPane scroller = new JScrollPane();
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getViewport().setView(output);
        getContentPane().add(scroller, BorderLayout.CENTER);
        input = new JTextField();
        getContentPane().add(input, BorderLayout.NORTH);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage(input.getText());
                    input.setText("");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton close = new JButton("close");
        getContentPane().add(close, BorderLayout.SOUTH);
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        setSize(400, 300);
    }

    public void receiveMessage(String nickname, String message) {
        output.append(nickname + ": " + message + "\n");
        output.setCaretPosition(output.getText().length() - 1);
    }

    public void close() {
        try {
            server.unsubscribeUser(session);
            System.exit(0);
        } catch (RemoteException e) {
            System.out.println("Uebelster FAIL!");
        }
    }

    public void sendMessage(String message) throws RemoteException {
        session.sendMessage(message);
    }

    public static void main(String[] args) {
        try {
            String name = JOptionPane.showInputDialog(null, "Please, enter your nickname!");
            if (name != null && name.trim().length() > 0) {
                ChatClient client = new ChatClient(name);
                client.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Please choose a nickname with more than one letter!");
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
