package is.idega.idegaweb.campus.business;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.EventTimer;
import com.idega.util.IWTimestamp;

/**
 * Title:GroupRelationDaemonBundleStarter
 * Description: GroupRelationDaemonBundleStarter implements the IWBundleStartable interface. The start method of this
 * object is called during the Bundle loading when starting up a idegaWeb applications. It checks for pending grouprelations and processes them.
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0
 */
public class CampusDaemonBundleStarter implements IWBundleStartable, ActionListener {
	private IWBundle bundle;
	private EventTimer timer;
	public static final String TIMER_THREAD_NAME = "CampusContractDaemon";
	private CampusService campusService;
	
	public CampusDaemonBundleStarter() {
	}
	
	public void start(IWBundle bundle) {
		this.bundle = bundle;
		timer = new EventTimer(EventTimer.THREAD_SLEEP_24_HOURS, TIMER_THREAD_NAME);
		timer.addActionListener(this);
		
		// wait until 01:00 am to start daemon
		IWTimestamp now = IWTimestamp.RightNow();
		long startwait = IWTimestamp.getMilliSecondsBetween(now,new IWTimestamp(now.getYear(),now.getMonth(),now.getDay(),23,59,59));
		startwait += (60*60*1000);
		timer.start(startwait);
		System.out.println("Group Relation Daemon Bundle Starter: starting");
	}
	
	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getActionCommand().equalsIgnoreCase(TIMER_THREAD_NAME)) {
				System.out.println("[Campus 24 hour Daemon - "+IWTimestamp.RightNow().toString()+" ] - Ending expired contracts");
				getCampusService(bundle.getApplication().getIWApplicationContext()).getContractService().endExpiredContracts();
				IWTimestamp oneYearBack = IWTimestamp.RightNow();
				oneYearBack.addYears(-1);
				campusService.getContractService().finalizeGarbageContracts(oneYearBack.getDate());
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
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}
	
	public CampusService getCampusService(IWApplicationContext iwc) {
		if (campusService == null) {
			try {
				campusService = (CampusService) com.idega.business.IBOLookup.getServiceInstance(iwc, CampusService.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return campusService;
	}
}