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
public interface ClientProxy extends Remote {
    public void receiveMessage (String username, String message) throws RemoteException;
    
}
