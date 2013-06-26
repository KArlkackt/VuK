/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.rmi.RemoteException;

/**
 *
 * @author Jannik
 */
interface UserInterface {

    public void showMessage(String username, String message) throws RemoteException;

    public String getUsername() throws RemoteException;

    public void run(ChatProxy chat, ChatClient client) throws RemoteException;

    public void close() throws RemoteException;
}
