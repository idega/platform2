/*
 * Created on Aug 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package is.idega.idegaweb.marathon.data;

import is.idega.idegaweb.marathon.business.RunBusiness;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;


/**
 * @author birna
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParticipantNumberSetter extends IBOServiceBean{
	
	public void main(IWContext iwc)throws Exception{}

	public void run() {
		IWContext iwc = IWContext.getInstance();

		ParticipantHome runHome = null;
		Collection run = null;
		Collection run2 = null;
		try {
			runHome = (ParticipantHome) getIDOHome(Participant.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(runHome != null) {
			
			try {
				run = runHome.findAll();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		if(run != null) {
			Iterator iter = run.iterator();
			
			while(iter.hasNext()) {
				Participant r = (Participant) iter.next();
				r.setParticipantNumber(-1);
				r.store();
			}
		}
		if(runHome != null) {
			try {
				run2 = runHome.findAll();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		if(run2 != null) {
			Iterator iter2 = run.iterator();
			while(iter2.hasNext()) {
				Participant r = (Participant) iter2.next();
				if(r.getParticipantNumber() == -1) {
					try {
						getRunBiz(iwc).setParticipantNumber(r);
					}
					catch (RemoteException re) {
						log(re);
					}
				}
			}
			
		}
		
	
		
	}
	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}
	
}
