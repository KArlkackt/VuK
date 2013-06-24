/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author student
 */
public class ClientProxyImpl extends UnicastRemoteObject implements ClientProxy{
    private ChatClient client;
    
    public ClientProxyImpl(ChatClient client) throws RemoteException {
        this.client = client;
    }  
    
    @Override
    public void receiveMessage(String username, String message) throws RemoteException {
       client.receiveMessage(username, message);
    }
    
}
