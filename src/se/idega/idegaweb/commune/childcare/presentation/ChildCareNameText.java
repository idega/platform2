package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.childcare.business.ChildCareSession;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

/**
 * @author Laddi
 */
public class ChildCareNameText extends Text {

	/**
	 * Creates a new <code>SchoolNameText</code>.
	 */
	public ChildCareNameText() {
		super();
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		int schoolID = getChildCareSession(iwc).getChildCareID();
		if (schoolID != -1) {
			String schoolName = getSchoolBusiness(iwc).getSchool(new Integer(schoolID)).getSchoolName();	
			setText(schoolName);
		}
		else {
			setText("");
		}
	}
	
	private ChildCareSession getChildCareSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);
	}

	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}
}