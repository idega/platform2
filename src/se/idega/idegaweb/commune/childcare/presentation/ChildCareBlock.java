package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * @author laddi
 */
public abstract class ChildCareBlock extends CommuneBlock {

	private ChildCareBusiness business;
	protected ChildCareSession session;
	private int _childCareID = -1;
	
	public void main(IWContext iwc) throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		business = getSchoolCommuneBusiness(iwc);
		session = getSchoolCommuneSession(iwc);
		initialize(iwc);

		init(iwc);
	}
	
	private void initialize(IWContext iwc) throws RemoteException {
		_childCareID = session.getChildCareID();	
	}
	
	public abstract void init(IWContext iwc) throws Exception;
	
	private ChildCareBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}
	
	private ChildCareSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
	}
	
	/**
	 * @return ChildCareBusiness
	 */
	public ChildCareBusiness getBusiness() {
		return business;
	}

	/**
	 * @return ChildCareSession
	 */
	public ChildCareSession getSession() {
		return session;
	}

	/**
	 * @return int
	 */
	public int getChildcareID() {
		return _childCareID;
	}
}