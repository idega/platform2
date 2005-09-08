package com.idega.block.trade.business;


/**
 * Title:TradeBundleStarter
 * Description: TradeBundleStarter implements the IWBundleStartable interface. The start method of this
 * object is called during the Bundle loading when starting up a idegaWeb applications.
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.core.component.data.ICObject;
import com.idega.core.data.ICApplicationBinding;
import com.idega.core.data.ICApplicationBindingHome;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.EventTimer;

public class TradeBundleStarter implements IWBundleStartable,ActionListener{
	
	private IWBundle bundle_;
	private EventTimer timer;
	public static final String IW_CURRENCY_TIMER = "iw_currency_timer";
	public static final String DATASOURCE = "travel.datasource";
	
	public TradeBundleStarter() {
	}
	
	public void start(IWBundle bundle){
		bundle_ = bundle;
		checkDataSource(bundle);
		timer = new EventTimer(EventTimer.THREAD_SLEEP_24_HOURS/2,IW_CURRENCY_TIMER);
		timer.addActionListener(this);
		//Starts the thread while waiting for 3 mins. before the idegaWebApp starts up.
		// -- Fix for working properly on Interebase with entity-auto-create-on.
		timer.start(3*60*1000);
		System.out.println("Trade bundle starter: starting");
	}
	
	/**
	 * @param bundle
	 */
	private void checkDataSource(IWBundle bundle) {
		// Switching the datasource
		String dataSource = null;
		try {
			ICApplicationBindingHome abHome = (ICApplicationBindingHome) IDOLookup.getHome(ICApplicationBinding.class);
			ICApplicationBinding ab = abHome.findByPrimaryKey(DATASOURCE);
			dataSource = ab.getValue();
		}
		catch (IDOLookupException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		if (dataSource == null) {
			dataSource = bundle.getProperty("datasource");
			if (dataSource != null) {
				try {
					ICApplicationBindingHome abHome = (ICApplicationBindingHome) IDOLookup.getHome(ICApplicationBinding.class);
					ICApplicationBinding ab = abHome.create();
					ab.setKey(DATASOURCE);
					ab.setValue(dataSource);
					ab.setBindingType("travel.binding");
					ab.store();
				}
				catch (IDOLookupException e1) {
					e1.printStackTrace();
				}
				catch (CreateException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (dataSource != null) {
			try {
				Collection entities = bundle.getDataObjects();
				if (entities != null){
					Iterator iter = entities.iterator();
					while (iter.hasNext())
					{
						ICObject ico = (ICObject) iter.next();
						try
						{
							Class c = ico.getObjectClass();
							IDOFactory home = (IDOFactory) IDOLookup.getHome(c);
							home.setDatasource(dataSource, false);
						}
						catch (ClassNotFoundException e)
						{
							System.out.println("Cant set the dataSource : Class " + e.getMessage() + " not found");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void actionPerformed(ActionEvent event) {
		try{
			if (event.getActionCommand().equalsIgnoreCase(IW_CURRENCY_TIMER)) {
				CurrencyBusiness.getCurrencyMap(bundle_);
			}
		}
		catch (com.idega.data.IDONoDatastoreError error) {
			System.err.println("TradeBundleStarter.actionPerformed() Error: "+error.getMessage());
		}
		catch (RemoteException re) {
			System.err.println("TradeBundleStarter.actionPerformed() Error: "+re.getMessage());
		}
		catch (Exception re) {
			System.err.println("TradeBundleStarter.actionPerformed() Error: "+re.getMessage());
			re.printStackTrace();
		}
	}
	
	/**
	 * @see com.idega.idegaweb.IWBundleStartable#stop(IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}
}