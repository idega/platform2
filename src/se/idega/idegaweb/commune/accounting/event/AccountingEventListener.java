/*
 * Created on 7.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.accounting.event;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.care.business.AccountingSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;

/**
 * @author laddi
 */
public class AccountingEventListener implements IWPageEventListener {

	/* (non-Javadoc)
	 * @see com.idega.event.IWPageEventListener#actionPerformed(com.idega.presentation.IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) {
		try {
			AccountingSession session = getAccountingSession(iwc);
			if (iwc.isParameterSet(session.getParameterOperationalField())) {
				session.setOperationalField(iwc.getParameter(session.getParameterOperationalField()));
				return true;
			}
		}
		catch (RemoteException e) {
			return false;
		}
		
		return false;
	}

	protected AccountingSession getAccountingSession(IWUserContext iwuc) {
		try {
			return (AccountingSession) IBOLookup.getSessionInstance(iwuc, AccountingSession.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}