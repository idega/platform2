/*
 * Created on 15.7.2003
 */
package is.idega.idegaweb.golf.course.business;

import is.idega.idegaweb.golf.course.data.Course;
import is.idega.idegaweb.golf.course.data.CourseHome;
import is.idega.idegaweb.golf.course.data.Hole;
import is.idega.idegaweb.golf.course.data.HoleHome;
import is.idega.idegaweb.golf.course.data.Tee;
import is.idega.idegaweb.golf.course.data.TeeColor;
import is.idega.idegaweb.golf.course.data.TeeColorHome;
import is.idega.idegaweb.golf.course.data.TeeHome;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;

/**
 * @author laddi
 */
public class CourseBusinessBean extends IBOServiceBean implements CourseBusiness {

	protected CourseHome getScorecardHome() throws RemoteException {
		return (CourseHome) IBOLookup.getServiceInstance(getIWApplicationContext(), Course.class);
	}
	
	protected HoleHome getHoleHome() throws RemoteException {
		return (HoleHome) IBOLookup.getServiceInstance(getIWApplicationContext(), Hole.class);
	}
	
	protected TeeHome getTeeHome() throws RemoteException {
		return (TeeHome) IBOLookup.getServiceInstance(getIWApplicationContext(), Tee.class);
	}
	
	protected TeeColorHome getTeeColorHome() throws RemoteException {
		return (TeeColorHome) IBOLookup.getServiceInstance(getIWApplicationContext(), TeeColor.class);
	}	
}