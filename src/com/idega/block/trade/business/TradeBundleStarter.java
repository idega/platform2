package com.idega.block.trade.business;


/**
 * Title:MediaBundleStarter
 * Description: MediaBundleStarter implements the IWBundleStartable interface. The start method of this
 * object is called during the Bundle loading when starting up a idegaWeb applications.
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0
 */

import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWBundle;
import com.idega.util.EventTimer;
import com.idega.block.trade.business.CurrencyBusiness;

public class TradeBundleStarter implements IWBundleStartable,ActionListener{

public static IWBundle bundle_;
public static final String IW_CURRENCY_TIMER = "iw_currency_timer";

  public TradeBundleStarter() {
  }

  public void start(IWBundle bundle){
    bundle_ = bundle;
    EventTimer timer = new EventTimer(EventTimer.THREAD_SLEEP_24_HOURS/2,IW_CURRENCY_TIMER);
    timer.addActionListener(this);
    timer.start();
    System.out.println("Trade bundle starter: starting");
  }

  public void actionPerformed(ActionEvent event) {
    try{
      if(event.getActionCommand().equalsIgnoreCase(IW_CURRENCY_TIMER)){
        CurrencyBusiness.getCurrencyMap(bundle_);
      }
    }
    catch(com.idega.data.IDONoDatastoreError error){
      System.err.println("TradeBundleStarter.actionPerformed() Error: "+error.getMessage());
    }catch (RemoteException re) {
      System.err.println("TradeBundleStarter.actionPerformed() Error: "+re.getMessage());
    }catch (Exception re) {
      System.err.println("TradeBundleStarter.actionPerformed() Error: "+re.getMessage());
    }
  }

}
