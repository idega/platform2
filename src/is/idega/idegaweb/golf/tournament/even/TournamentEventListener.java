/*
 * Created on 26.4.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.even;

import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;


/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentEventListener implements IWPageEventListener {

	/* (non-Javadoc)
	 * @see com.idega.event.IWPageEventListener#actionPerformed(com.idega.presentation.IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			TournamentSession session = getTournamentSession(iwc);
			
			if (iwc.isParameterSet(session.getParameterNameTournamentID())) {
				session.setTournamentID(Integer.parseInt(iwc.getParameter(session.getParameterNameTournamentID())));
			}
			if (iwc.isParameterSet(session.getParameterNameStartDate())) {
				session.setStartDate(new IWTimestamp(iwc.getParameter(session.getParameterNameStartDate())).getDate());
			}
			if (iwc.isParameterSet(session.getParameterNameEndDate())) {
				session.setEndDate(new IWTimestamp(iwc.getParameter(session.getParameterNameEndDate())).getDate());
			}
			
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}