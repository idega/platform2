/*
 * Created on 6.5.2004
 */
package se.idega.idegaweb.commune.school.music.event;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.school.music.business.MusicSchoolSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;


/**
 * @author laddi
 */
public class MusicSchoolEventListener implements IWPageEventListener {

	/* (non-Javadoc)
	 * @see com.idega.event.IWPageEventListener#actionPerformed(com.idega.presentation.IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			MusicSchoolSession session = getSession(iwc);

			if (iwc.isParameterSet(session.getParameterNameChildID())) {
				session.setChild(iwc.getParameter(session.getParameterNameChildID()));
			}
			
			if (iwc.isParameterSet(session.getParameterNameProviderID())) {
				session.setProvider(iwc.getParameter(session.getParameterNameProviderID()));
			}
			
			if (iwc.isParameterSet(session.getParameterNameInstrumentID())) {
				session.setInstrument(iwc.getParameter(session.getParameterNameInstrumentID()));
			}
			else if (iwc.isParameterSetAsEmpty(session.getParameterNameInstrumentID())) {
				session.setInstrument(null);
			}
			
			if (iwc.isParameterSet(session.getParameterNameDepartmentID())) {
				session.setDepartment(iwc.getParameter(session.getParameterNameDepartmentID()));
			}
			else if (iwc.isParameterSetAsEmpty(session.getParameterNameDepartmentID())) {
				session.setDepartment(null);
			}
			
			if (iwc.isParameterSet(session.getParameterNameSeasonID())) {
				session.setSeason(iwc.getParameter(session.getParameterNameSeasonID()));
			}
			else if (iwc.isParameterSetAsEmpty(session.getParameterNameSeasonID())) {
				session.setSeason(null);
			}
			
			if (iwc.isParameterSet(session.getParameterNameGroupID())) {
				session.setGroup(iwc.getParameter(session.getParameterNameGroupID()));
			}
			else if (iwc.isParameterSetAsEmpty(session.getParameterNameGroupID())) {
				session.setGroup(null);
			}
			
			if (iwc.isParameterSet(session.getParameterNameStudentID())) {
				session.setStudent(iwc.getParameter(session.getParameterNameStudentID()));
			}
			else if (iwc.isParameterSetAsEmpty(session.getParameterNameStudentID())) {
				session.setStudent(null);
			}
			
			if (iwc.isParameterSet(session.getParameterNameApplicationID())) {
				session.setApplication(iwc.getParameter(session.getParameterNameApplicationID()));
			}
			else if (iwc.isParameterSetAsEmpty(session.getParameterNameApplicationID())) {
				session.setApplication(null);
			}
			
			return true;
		}
		catch (RemoteException re){
			re.printStackTrace();
			return false;
		}
	}
	
	private MusicSchoolSession getSession(IWUserContext iwuc) {
		try {
			return (MusicSchoolSession) IBOLookup.getSessionInstance(iwuc, MusicSchoolSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}