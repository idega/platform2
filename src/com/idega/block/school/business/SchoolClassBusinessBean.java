package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

/**
 * @author Laddi
 */
public class SchoolClassBusinessBean extends IBOServiceBean implements SchoolClassBusiness {

	public SchoolClassHome getSchoolClassHome() throws RemoteException {
		return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
	}
	
	public SchoolClass findSchoolClass(Object primaryKey) throws RemoteException {
    try {
	    return getSchoolClassHome().findByPrimaryKey(primaryKey);
    }
    catch (FinderException fe) {
    	return null;
    }
	}
	
	public Collection findSchoolClassesBySchool(int schoolID) throws RemoteException {
    try {
			return getSchoolClassHome().findBySchool(schoolID);
    }
    catch (FinderException fe) {
    	return new Vector();
    }
	}
	
	public Collection findSchoolClassesBySchoolAndSeason(int schoolID,int schoolSeasonID) throws RemoteException {
    try {
			return getSchoolClassHome().findBySchoolAndSeason(schoolID,schoolSeasonID);
    }
    catch (FinderException fe) {
    	return new Vector();
    }
	}
	
	public Collection findSchoolClassesBySchoolAndYear(int schoolID,int schoolYearID) throws RemoteException {
    try {
			return getSchoolClassHome().findBySchoolAndYear(schoolID,schoolYearID);
    }
    catch (FinderException fe) {
    	return new Vector();
    }
	}
	
	public Collection findSchoolClassesBySchoolAndSeasonAndYear(int schoolID,int schoolSeasonID,int schoolYearID) throws RemoteException {
    try {
			return getSchoolClassHome().findBySchoolAndSeasonAndYear(schoolID,schoolSeasonID,schoolYearID);
    }
    catch (FinderException fe) {
    	return new Vector();
    }
	}
	
	public Collection findSchoolClassesByTeacher(int teacherID) throws RemoteException {
    try {
			return getSchoolClassHome().findByTeacher(teacherID);
    }
    catch (FinderException fe) {
    	return new Vector();
    }
	}
	
	public Collection findSchoolClassesBySchoolAndTeacher(int schoolID,int teacherID) throws RemoteException {
    try {
			return getSchoolClassHome().findBySchoolAndTeacher(schoolID,teacherID);
    }
    catch (FinderException fe) {
    	return new Vector();
    }
	}
	
	public Collection findSchoolClassesBySchoolAndSeasonAndTeacher(int schoolID,int schoolSeasonID,int teacherID) throws RemoteException {
    try {
			return getSchoolClassHome().findBySchoolAndSeasonAndTeacher(schoolID,schoolSeasonID,teacherID);
    }
    catch (FinderException fe) {
    	return new Vector();
    }
	}
	
	public int getNumberOfStudentsInClass(int schoolClassID) throws RemoteException {
    try {
			return getSchoolClassHome().getNumberOfStudentsInClass(schoolClassID);
    }
    catch (FinderException fe) {
    	return 0;
    }
    catch (IDOException ie) {
    	return 0;
    }
	}
	
	public void removeSchoolClass(int schoolClassID) throws RemoteException {
    try {
			SchoolClass schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			schoolClass.remove();
    }
    catch (FinderException fe) {
    	fe.printStackTrace(System.err);
    }
    catch (RemoveException re) {
    	re.printStackTrace(System.err);
    }
	}
	
	public void invalidateSchoolClass(int schoolClassID) throws RemoteException {
    try {
			SchoolClass schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			schoolClass.setValid(false);
			schoolClass.store();
    }
    catch (FinderException fe) {
    	fe.printStackTrace(System.err);
    }
	}
	
	public void storeSchoolClass(int schoolClassID,String className,int schoolID,int schoolSeasonID,int schoolYearID,int teacherID) throws RemoteException {
		try {
			SchoolClass schoolClass;
			if ( schoolClassID != -1 )
				schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			else
				schoolClass = getSchoolClassHome().create();
			
			schoolClass.setSchoolClassName(className);
			schoolClass.setSchoolId(schoolID);
			schoolClass.setSchoolSeasonId(schoolSeasonID);
			schoolClass.setSchoolYearId(schoolYearID);
			if ( teacherID != -1 )
				schoolClass.setTeacherId(teacherID);
			schoolClass.setValid(true);
			
			schoolClass.store();
		}
    catch (FinderException fe) {
    	fe.printStackTrace(System.err);
    }
    catch (CreateException ce) {
    	ce.printStackTrace(System.err);
    }
	}
}