package se.idega.idegaweb.commune.childcare.event;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.childcare.business.ChildCareSession;

import com.idega.business.IBOLookup;
import com.idega.business.IWEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;

/**
 * @author laddi
 */
public class ChildCareEventListener implements IWEventListener {

	private int _childCareID = -1;
	
	/**
	 * @see com.idega.business.IWEventListener#actionPerformed(com.idega.presentation.IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			ChildCareSession session = getChildCareSession(iwc);
			_childCareID = session.getChildCareID();	
	
			if (iwc.isParameterSet(session.getParameterChildCareID()))
			_childCareID = Integer.parseInt(iwc.getParameter(session.getParameterChildCareID()));

			session.setChildCareID(_childCareID);
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}

	private ChildCareSession getChildCareSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
	}
}
