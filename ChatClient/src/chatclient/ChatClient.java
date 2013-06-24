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
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.*;

public class ChatClient {

    static UserInterface gui;
    private ClientProxy handle;
    private ChatProxy session;
    private ChatServer server;
    private static String nickname;

    /**
     *
     * @param nickname
     */
    public ChatClient(String nickname) throws RemoteException, NotBoundException, MalformedURLException {
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
    }

    public void receiveMessage(String nickname, String message) throws RemoteException {
        gui.showMessage(nickname, message);
    }

    public void close() throws RemoteException {
        gui.close();
    }

    public void sendMessage(String message) throws RemoteException {
        session.sendMessage(message);
    }

    public ChatProxy getSession() {
        return session;
    }

    public static void main(String[] args) {
        try {
            gui = new SwingGUI();
            nickname = gui.getUsername();
            //System.out.println(nickname);
            ChatClient client = new ChatClient(nickname);
            gui.run(client.getSession(), client);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
