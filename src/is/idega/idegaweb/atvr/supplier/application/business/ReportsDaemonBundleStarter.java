/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.atvr.supplier.application.business;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.GroupRelation;
import com.idega.util.EventTimer;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReportsDaemonBundleStarter implements IWBundleStartable, ActionListener {
	public static final String TIMER_THREAD_NAME = "atvr_reports";

	private GroupBusiness _groupBiz;
	private IWBundle _bundle;
	private EventTimer _timer;

	public ReportsDaemonBundleStarter() {
	}

	public void start(IWBundle bundle) {
		_bundle = bundle;
		_timer = new EventTimer(EventTimer.THREAD_SLEEP_1_HOUR, TIMER_THREAD_NAME);
//		_timer = new EventTimer(EventTimer.THREAD_SLEEP_10_SECONDS, TIMER_THREAD_NAME);
		_timer.addActionListener(this);
		//Starts the thread while waiting for 3 mins. before the idegaWebApp starts up.
		// -- Fix for working properly on Interebase with entity-auto-create-on.
		_timer.start(3 * 60 * 1000);
		System.out.println("ATVR Reports Daemon Bundle Starter: starting");
	}

	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getActionCommand().equalsIgnoreCase(TIMER_THREAD_NAME)) {
				System.out.println("[ATVR Reports Daemon - " + IWTimestamp.RightNow().toString() + " ] - Checking for reports from Navision");
			}
		}
		catch (Exception x) {
			x.printStackTrace();
		}
	}

	/**
	 * @see com.idega.idegaweb.IWBundleStartable#stop(IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		if (_timer != null) {
			_timer.stop();
			_timer = null;
		}
	}

	//	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
	//		if (groupBiz == null) {
	//			try {
	//				groupBiz = (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
	//			}
	//			catch (java.rmi.RemoteException rme) {
	//				throw new RuntimeException(rme.getMessage());
	//			}
	//		}
	//		return groupBiz;
	//	}
}