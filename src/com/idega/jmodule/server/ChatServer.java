package com.idega.jmodule.server;
import com.idega.jmodule.server.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
  public String getNextMessage() throws RemoteException;
  public void broadcastMessage(String message) throws RemoteException;

  public void addClient(ChatClient client) throws RemoteException;
  public void deleteClient(ChatClient client) throws RemoteException;
}
