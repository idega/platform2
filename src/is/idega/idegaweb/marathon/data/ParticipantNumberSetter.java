/*
 * Created on Aug 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package is.idega.idegaweb.marathon.data;

import is.idega.idegaweb.marathon.business.RunBusiness;
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

		RunHome runHome = null;
		Collection run = null;
		try {
			runHome = (RunHome) getIDOHome(Run.class);
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
				Run r = (Run) iter.next();
				r.setParticipantNumber(-1);
				getRunBiz(iwc).setParticipantNumber(r);
				r.store();
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
