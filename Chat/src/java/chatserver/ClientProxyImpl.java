/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author student
 */
public class ClientProxyImpl extends UnicastRemoteObject implements ClientProxy{
    private ClientIF client;
    
    public ClientProxyImpl(ClientIF client) throws RemoteException {
        this.client = client;
    }  
    
    @Override
    public void receiveMessage(String username, String message) throws RemoteException {
       client.receiveMessage(username, message);
    }
    
}
