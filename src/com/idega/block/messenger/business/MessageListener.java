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
  private long threadSleep = 500;//0.5 sec
  private boolean runThread = true;
  private Vector dialogs = null;
  private int length = 0;

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
    length++;
  }

  public void run(){
    while(runThread){
      try {
        if( dialogs != null ){
          for (int i = 0; i < length; i++) {
            client.getMessagesFromDialog((MessageDialog)dialogs.elementAt(i));
          }
        }
        t.sleep(threadSleep);
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
  }

  public void start(){
    if( t == null ){
      t = new Thread();
      t.start();
      run();
    }
  }

  public void stop(){
    if ( t != null ){
      t=null;
      runThread = false;
    }
  }

  public void setIntervalForMsgChecking(long interval){
    this.threadSleep = interval;
  }
}