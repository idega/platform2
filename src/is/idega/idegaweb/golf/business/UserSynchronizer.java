package is.idega.idegaweb.golf.business;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.EventTimer;

/**
 * @author gimmi
 */
public class UserSynchronizer implements IWBundleStartable, ActionListener {

	private static String EVENT_TIMER_COMMAND = "golf_user_sync";
	int delayBeforeStart = 30;
	private EventTimer timer;
	private UserSynchronizationBusiness usBusiness;
	public UserSynchronizer() {
		super();
	}
	
	public void start(IWBundle starterBundle) {
		try {
			usBusiness = (UserSynchronizationBusiness) IBOLookup.getServiceInstance(starterBundle.getApplication().getIWApplicationContext(), UserSynchronizationBusiness.class);
			timer = new EventTimer(EventTimer.THREAD_SLEEP_5_MINUTES, EVENT_TIMER_COMMAND);
			timer.addActionListener(this);
			timer.start(delayBeforeStart*1000);
			System.out.println("Starting User Synchronization Deamon, next sync in "+delayBeforeStart+" seconds");
		}
		catch (IBOLookupException e) {
			stop(starterBundle);
			System.out.println("Starting User Synchronization Deamon, "+e.getMessage());
		}
	}

	public void stop(IWBundle starterBundle) {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals(EVENT_TIMER_COMMAND)) {
			try {
				System.out.println("User Synchronization Deamon : Synchronization starting");
				if (usBusiness == null) {
					System.out.println("User Synchronization executed WITH errors. (usBusiness = null)");
				} else if (usBusiness.sync()) {
					System.out.println("User Synchronization executed without errors.");
				} else {
					System.out.println("User Synchronization executed WITH errors.");
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
