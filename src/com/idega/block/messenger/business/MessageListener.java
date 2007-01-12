package com.idega.block.messenger.business;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.AWTEventMulticaster;

/**
 * Title:        com.idega.block.messenger.business
 * Description:  idega classes
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class MessageListener implements Runnable{
  private ActionListener listener;
  private Thread t;
  private long threadSleep = 5000;//5 seconds
  private boolean runThread = false;

  public MessageListener() {
  }

  public MessageListener(long interval) {
    setInterval(interval);
  }

  public void run(){
    while(this.runThread){
      try {
        if( this.listener!=null ) {
			this.listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"iw-cycle"));
		}
        Thread.sleep(this.threadSleep);
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
  }

  public void start(){
    this.runThread = true;
    if( this.t == null ){
      this.t = new Thread(this,"MessageListener thread");
      this.t.setPriority(Thread.MIN_PRIORITY);
      this.t.start();
    }
  }

  public void stop(){
    if ( this.t != null ){
      this.runThread = false;
    }
  }

   /**Destroy the thread*/
  public void destroy() {
    stop();
    this.t = null;
  }

  public void setInterval(long interval){
    this.threadSleep = interval;
  }

  public void addActionListener(ActionListener l) {
    this.listener = AWTEventMulticaster.add(this.listener, l);
  }

  public void removeActionListener(ActionListener l) {
    this.listener = AWTEventMulticaster.remove(this.listener, l);
  }

  private ActionListener getActionListener(){
    return this.listener;
  }

}
