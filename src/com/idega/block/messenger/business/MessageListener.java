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
  private long threadSleep = 5000;//5 seconds
  private boolean runThread = false;

  public MessageListener(MessengerApplet applet) {
    this.client = applet;
  }

  public MessageListener(MessengerApplet applet, long interval) {
    this(applet);
    setInterval(interval);
  }

  public void run(){
    while(runThread){
      try {
        client.cycle();
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

    runThread = true;

  }

  public void stop(){
    if ( t != null ){
      runThread = false;
    }
  }

   /**Destroy the thread*/
  public void destroy() {
   t = null;
  }

  public void setInterval(long interval){
    this.threadSleep = interval;
  }
}