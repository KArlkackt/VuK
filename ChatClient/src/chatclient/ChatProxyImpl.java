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
class ChatProxyImpl extends UnicastRemoteObject implements ChatProxy {

    private ChatServerImpl server;
    private String username;
    private ClientProxy handle;

    public ChatProxyImpl() throws RemoteException {
    }

    public ChatProxyImpl(ChatServerImpl server, String username, ClientProxy handle) throws RemoteException {
        this.server = server;
        this.username = username;
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        server.sendMessage(message, this);
    }

    @Override
    public ClientProxy getHandle() throws RemoteException {
        return handle;
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override
    public ChatServer getServer() throws RemoteException {
        return server;
    }
}
