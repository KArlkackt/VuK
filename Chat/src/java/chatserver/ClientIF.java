/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author Jannik
 */
public interface ClientIF {

    public void receiveMessage(String nickname, String message) throws RemoteException;

    public void close() throws RemoteException;

    public void sendMessage(String message) throws RemoteException;

    public ChatProxy getSession();
}
