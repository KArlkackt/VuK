/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author student
 */
public interface ChatProxy extends Remote {
    public void sendMessage (String message) throws RemoteException;
    public String getUsername() throws RemoteException;
    public ChatServer getServer() throws RemoteException;
    public ClientProxy getHandle() throws RemoteException;
}
