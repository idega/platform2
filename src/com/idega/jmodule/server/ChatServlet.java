package com.idega.jmodule.server;

import com.idega.jmodule.server.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChatServlet extends RemoteDaemonHttpServlet
                         implements ChatServer {

  // source acts as the distributor of new messages
  MessageSource source = new MessageSource();

  // socketClients holds references to all the socket-connected clients
  Vector socketClients = new Vector();

  // rmiClients holds references to all the RMI clients
  Vector rmiClients = new Vector();

  // doGet() returns the next message.  It blocks until there is one.
  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    // Return the next message (blocking)
    out.println(getNextMessage());
  }

  // doPost() accepts a new message and broadcasts it to all
  // the currently listening HTTP and socket clients.
  public void doPost(HttpServletRequest req, HttpServletResponse res)
                                throws ServletException, IOException {
    // Accept the new message as the "message" parameter
    String message = req.getParameter("message");

    // Broadcast it to all listening clients
    if (message != null) broadcastMessage(message);

    // Set the status code to indicate there will be no response
    res.setStatus(res.SC_NO_CONTENT);
  }

  // getNextMessage() returns the next new message.
  // It blocks until there is one.
  public String getNextMessage() {
    // Create a message sink to wait for a new message from the
    // message source.
    return new MessageSink().getNextMessage(source);
  }

  // broadcastMessage() informs all currently listening clients that there
  // is a new message.  Causes all calls to getNextMessage() to unblock.
  public void broadcastMessage(String message) {
    // Send the message to all the HTTP-connected clients by giving the
    // message to the message source
    source.sendMessage(message);

    // Directly send the message to all the socket-connected clients
    Enumeration enum = socketClients.elements();
    while (enum.hasMoreElements()) {
      Socket client = null;
      try {
        client = (Socket)enum.nextElement();
        PrintStream out = new PrintStream(client.getOutputStream());
        out.println(message);
      }
      catch (IOException e) {
        // Problem with a client, close and remote it
        try {
          if (client != null) client.close();
        }
        catch (IOException ignored) { }
        socketClients.removeElement(client);
      }
    }

    // Directly send the message to all RMI clients
    enum = rmiClients.elements();
    while (enum.hasMoreElements()) {
      ChatClient chatClient = null;
      try {
        chatClient = (ChatClient)enum.nextElement();
        chatClient.setNextMessage(message);
      }
      catch (RemoteException e) {
        // Problem communicating with a client, remove it
        deleteClient(chatClient);
      }
    }
  }

  protected int getSocketPort() {
    // We listen on port 2428 (look at a phone to see why)
    return 2428;
  }

  public void handleClient(Socket client) {
    // We have a new socket client.  Add it to our list.
    socketClients.addElement(client);
  }

  public void addClient(ChatClient client) {
    // We have a new RMI client.  Add it to our list.
    rmiClients.addElement(client);
  }

  public void deleteClient(ChatClient client) {
    // Remote the specified client from our list.
    rmiClients.removeElement(client);
  }
}

// MessageSource acts as the source for new messages.
// Clients interested in receiving new messages can
// observe this object.
class MessageSource extends Observable {
  public void sendMessage(String message) {
    setChanged();
    notifyObservers(message);
  }
}

// MessageSink acts as the receiver of new messages.
// It listens to the source.
class MessageSink implements Observer {

  String message = null;  // set by update() and read by getNextMessage()

  // Called by the message source when it gets a new message
  synchronized public void update(Observable o, Object arg) {
    // Get the new message
    message = (String)arg;

    // Wake up our waiting thread
    notify();
  }

  // Gets the next message sent out from the message source
  synchronized public String getNextMessage(MessageSource source) {
    // Tell source we want to be told about new messages
    source.addObserver(this);

    // Wait until our update() method receives a message
    while (message == null) {
      try { wait(); } catch (Exception ignored) { }
    }

    // Tell source to stop telling us about new messages
    source.deleteObserver(this);

    // Now return the message we received
    // But first set the message instance variable to null
    // so update() and getNextMessage() can be called again.
    String messageCopy = message;
    message = null;
    return messageCopy;
  }
}
