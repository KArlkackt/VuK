/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author student
 */
public interface ChatServer extends Remote {
    public ChatProxy subscribeUser (String username, ClientProxy handle) throws RemoteException;
    public boolean unsubscribeUser (ChatProxy username) throws RemoteException;    
}
