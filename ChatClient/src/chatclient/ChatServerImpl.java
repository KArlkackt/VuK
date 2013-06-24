/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author student
 */
public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

    ArrayList<ChatProxy> users;

    public ChatServerImpl() throws RemoteException {
        users = new ArrayList();

        System.out.println("Chatserver created...");
    }

    @Override
    public ChatProxy subscribeUser(String username, ClientProxy handle) throws RemoteException {       
        ChatProxy cp = new ChatProxyImpl(this, username, handle);
        boolean found = false;
        for(ChatProxy cpu : users){
            if(cpu.getUsername().equals(username)){
                found = true;
                System.out.println(username + " is already subscribed!");
                return null;
            }
        }
        if(!found){
            users.add(cp);
            System.out.println("User " + username + " subscribed!");
            return cp;
        }
             
        return null;
    }

    @Override
    public boolean unsubscribeUser(ChatProxy user) throws RemoteException {

        for(ChatProxy temp : users){
            if(temp.getUsername().equals(user.getUsername())){
                users.remove(temp);
                System.out.println(temp.getUsername() + " unsubscribed");
                return true;
            }
        }
         return false;
        
//        if (users.contains(user) == true) {
//            users.remove(user);
//            System.out.println(user.getUsername() + " unsubscribed");
//            return true;
//        } else {
//            System.out.println(user.getUsername() + " is not in the list");
//            return false;
//        }
    }

    public void sendMessage(String message, ChatProxyImpl cp) {
        ChatProxyImpl temp;
        for (int i = 0; i < users.size(); i++) {
            temp = (ChatProxyImpl) users.get(i);
            try {
                temp.getHandle().receiveMessage(cp.getUsername(), message);
            } catch (RemoteException ex) {
                System.out.println("Unable to contact client " + cp.getUsername());
            }
        }
    }

    public static void main(String[] args) {

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            //registry = LocateRegistry.getRegistry();
        } catch (RemoteException rex) {
            System.err.println("RemoteException occured");
        }
        if (registry == null) {
            System.err.println("Cannot find registry");
            System.exit(0);
        }

        try {
            registry.rebind("ChatServer", new ChatServerImpl());
            // Es gibt auch eine statische Methode, die verwendet werden kann.
            // Naming.rebind ("TimeServer", new TimeServerImpl());
            System.out.println("ChatServerImpl registered as 'ChatServer' ...");
        } catch (java.rmi.ConnectException cex) {
            System.err.println("ConnectException while accessing registry (port = " + Registry.REGISTRY_PORT + ")");
            System.err.println("Run 'rmiregistry " + Registry.REGISTRY_PORT + "'");
        } catch (Exception ex) {
            System.err.println("Exception during server registration (port = " + Registry.REGISTRY_PORT + ")");
            ex.printStackTrace();
        }
    }
}
