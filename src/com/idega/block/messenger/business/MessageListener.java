package com.idega.block.messenger.business;

import com.idega.block.messenger.presentation.MessengerApplet;
import com.idega.block.messenger.presentation.MessageDialog;
import java.util.Vector;

/**
 * Title:        com.idega.block.messenger.business
 * Description:  idega classes
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class MessageListener implements Runnable{
  private MessengerApplet client;
  private Thread t;
  private long threadSleep = 1000;//1 sec
  private boolean runThread = false;
  private Vector dialogs = null;

  int i = 1;

  public MessageListener(MessengerApplet applet) {
    this.client = applet;
  }

  public MessageListener(MessengerApplet applet, long interval) {
    this(applet);
    setIntervalForMsgChecking(interval);
  }

  public void addMessageDialog(MessageDialog msg){
    if( dialogs == null ) dialogs = new Vector();
    dialogs.addElement(msg);
  }

  public void run(){
    while(runThread){
      try {
        System.out.println("IN THREAD count :"+i);
        if( dialogs != null ){
          int length = dialogs.size();
          System.out.println("IN THREAD before loop. size = "+length);
          for (int i = 0; i < length; i++) {

            System.out.println("IN THREAD INSIDE loop");

            client.getMessagesFromDialog((MessageDialog)dialogs.elementAt(i));
          }
        }
        t.sleep(threadSleep);
        i++;
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
  }

  public void start(){
    if( t == null ){
      t = new Thread();
      runThread = true;
      t.start();
      run();
    }
  }

  public void stop(){
    if ( t != null ){
      runThread = false;
    }
  }

  public void setIntervalForMsgChecking(long interval){
    this.threadSleep = interval;
  }
}