package com.idega.block.messenger.business;

import com.idega.block.messenger.presentation.MessengerApplet;

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
  private long threadSleep = 5000;//5 sec
  private boolean runThread = true;

  public MessageListener(MessengerApplet applet) {
    this.client = applet;
  }

  public MessageListener(MessengerApplet applet, long interval) {
    this(applet);
    setIntervalForMsgChecking(interval);
  }

  public void run(){
    while(runThread){
      try {
        client.getMessagesFromDialog();
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