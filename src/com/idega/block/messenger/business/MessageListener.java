package com.idega.block.messenger.business;

import com.idega.block.messenger.presentation.IdegaClient;

/**
 * Title:        com.idega.block.messenger.business
 * Description:  idega classes
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class MessageListener implements Runnable{
  private IdegaClient client;
  private Thread t;
  private long threadSleep = 500;//0.5 sec
  private boolean runThread = true;

  public MessageListener(IdegaClient client) {
    this.client = client;
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
}