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

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;

/**
 * @author laddi
 */
public class CourseBusinessBean extends IBOServiceBean implements CourseBusiness {

	protected CourseHome getCourseHome() throws RemoteException {
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
	
	public boolean storeCourse(int courseID, int clubID, String courseName, String courseType, int numberOfHoles) throws RemoteException {
		Course course = null;
		
		if (courseID != -1) {
			try {
				course = getCourseHome().findByPrimaryKey(new Integer(courseID));
			}
			catch (FinderException e) {
				e.printStackTrace(System.err);
				return false;
			}
		}
		else {
			try {
				course = getCourseHome().create();
			}
			catch (CreateException e) {
				e.printStackTrace(System.err);
				return false;
			}
		}
		
		course.setClubID(clubID);
		course.setCourseName(courseName);
		course.setCourseType(courseType);
		course.setNumberOfHoles(numberOfHoles);
		course.setIsValid(true);
		course.store();
		
		return true;
	}
	
	public void setCourseValidation(int courseID, boolean isValid) throws RemoteException {
		try {
			Course course = getCourseHome().findByPrimaryKey(new Integer(courseID));
			course.setIsValid(isValid);
			course.store();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
}