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
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class CourseBusinessBean extends IBOServiceBean implements CourseBusiness {

	protected CourseHome getCourseHome() {
		try {
			return (CourseHome) IBOLookup.getServiceInstance(getIWApplicationContext(), Course.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	protected HoleHome getHoleHome() {
		try {
			return (HoleHome) IBOLookup.getServiceInstance(getIWApplicationContext(), Hole.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	protected TeeHome getTeeHome() {
		try {
			return (TeeHome) IBOLookup.getServiceInstance(getIWApplicationContext(), Tee.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	protected TeeColorHome getTeeColorHome() {
		try {
			return (TeeColorHome) IBOLookup.getServiceInstance(getIWApplicationContext(), TeeColor.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public boolean storeCourse(Object courseID, Object clubID, String courseName, String courseType, int numberOfHoles) {
		Course course = null;

		try {
			course = getCourseHome().findByPrimaryKey(courseID);
		}
		catch (FinderException fe) {
			try {
				course = getCourseHome().create();
			}
			catch (CreateException ce) {
				ce.printStackTrace(System.err);
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

	public boolean storeTee(Object teeID, Object courseID, Object teeColorID, Object genderID, float courseRating, int slope, int par, Date validFrom, String teeName) {
		Tee tee = null;
		try {
			tee = getTeeHome().findByPrimaryKey(teeID);
		}
		catch (FinderException e) {
			try {
				tee = getTeeHome().create();
			}
			catch (CreateException ce) {
				return false;
			}
		}
		
		if (tee.getValidTo() != null) {
			try {
				Tee oldTee = getTeeHome().findTeeByCourse(courseID, teeColorID, genderID);
				IWTimestamp validTo = new IWTimestamp(validFrom);
				validTo.addDays(-1);
				oldTee.setValidTo(validTo.getDate());
				oldTee.store();
			}
			catch (FinderException fe) {
			}
		}
		
		tee.setCourseID(courseID);
		tee.setGenderID(genderID);
		tee.setTeeColorID(teeColorID);
		tee.setValidFrom(validFrom);
		tee.setCourseRating(courseRating);
		tee.setSlope(slope);
		tee.setPar(par);
		tee.setTeeName(teeName);
		tee.store();
		
		return true;
	}
	
	public boolean storeHoles(Collection holes, Object courseID, Object teeColorID, int par, int handicap, Date validFrom, String holeName) {
		Hole hole = null;
		int holeNumber = 1;
		
		Iterator iter = holes.iterator();
		while (iter.hasNext()) {
			Integer holeID = (Integer) iter.next();
			try {
				hole = getHoleHome().findByPrimaryKey(holeID);
			}
			catch (FinderException fe) {
				try {
					hole = getHoleHome().create();
				}
				catch (CreateException ce) {
					return false;
				}
			}

			if (hole.getValidTo() != null) {
				try {
					Hole oldHole = getHoleHome().findHoleByCourseAndTeeColorAndNumber(courseID, teeColorID, holeNumber);
					IWTimestamp validTo = new IWTimestamp(validFrom);
					validTo.addDays(-1);
					oldHole.setValidTo(validTo.getDate());
					oldHole.store();
				}
				catch (FinderException fe) {
				}
			}
			
			hole.setCourseID(courseID);
			hole.setTeeColorID(teeColorID);
			hole.setHoleNumber(holeNumber);
			hole.setHandicap(handicap);
			hole.setPar(par);
			hole.setValidFrom(validFrom);
			hole.setHoleName(holeName);
			
			holeNumber++;
		}
		
		return true;
	}

	public void setCourseValidation(int courseID, boolean isValid) {
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