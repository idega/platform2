/*
 * Created on 5.10.2003
 */
package is.idega.idegaweb.golf.business;

import java.rmi.RemoteException;

import is.idega.idegaweb.golf.course.business.CourseBusiness;
import is.idega.idegaweb.golf.handicap.business.HandicapBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;

/**
 * @author laddi
 */
public class GolfBusinessBean extends IBOServiceBean implements GolfBusiness {

	public CourseBusiness getCourseBusiness() {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CourseBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public HandicapBusiness getHandicapBusiness() {
		try {
			return (HandicapBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), HandicapBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}