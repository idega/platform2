/*
 * Created on 15.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.provider.event;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.provider.business.ProviderSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;

/**
 * @author laddi
 */
public class ProviderEventListener implements IWPageEventListener {

	/* (non-Javadoc)
	 * @see com.idega.event.IWPageEventListener#actionPerformed(com.idega.presentation.IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) {
		ProviderSession session = getProviderSession(iwc);
		try {
		    if(iwc.isParameterSet(session.getParameterProviderID()))
		        session.setProviderID(Integer.parseInt(iwc.getParameter(session.getParameterProviderID())));
			if (iwc.isParameterSet(session.getParameterSeasonID()))
				session.setSeasonID(Integer.parseInt(iwc.getParameter(session.getParameterSeasonID())));
			if (iwc.isParameterSet(session.getParameterYearID()))
				session.setYearID(Integer.parseInt(iwc.getParameter(session.getParameterYearID())));
			if (iwc.isParameterSet(session.getParameterStudyPathID()))
				session.setStudyPathID(Integer.parseInt(iwc.getParameter(session.getParameterStudyPathID())));
		}
		catch (RemoteException e) {
			return false;
		}
			
		return true;
	}

	protected ProviderSession getProviderSession(IWUserContext iwuc) {
		try {
			return (ProviderSession) IBOLookup.getSessionInstance(iwuc, ProviderSession.class);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re.getMessage());
		}
	}
}