package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolClassBusinessBean extends IBOServiceBean implements SchoolClassBusiness {

	public SchoolClassHome getSchoolClassHome() throws RemoteException {
		return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
	}
	
	public SchoolClass findSchoolClass(Object primaryKey) throws FinderException,RemoteException {
    return getSchoolClassHome().findByPrimaryKey(primaryKey);
	}
	
	public Collection findSchoolClassesBySchool(int schoolID) throws FinderException,RemoteException {
		return getSchoolClassHome().findBySchool(schoolID);
	}
	
	public Collection findSchoolClassesBySchoolAndSeason(int schoolID,int schoolSeasonID) throws FinderException,RemoteException {
		return getSchoolClassHome().findBySchoolAndSeason(schoolID,schoolSeasonID);
	}
	
	public Collection findSchoolClassesBySchoolAndYear(int schoolID,int schoolYearID) throws FinderException,RemoteException {
		return getSchoolClassHome().findBySchoolAndYear(schoolID,schoolYearID);
	}
	
	public Collection findSchoolClassesBySchoolAndSeasonAndYear(int schoolID,int schoolSeasonID,int schoolYearID) throws FinderException,RemoteException {
		return getSchoolClassHome().findBySchoolAndSeasonAndYear(schoolID,schoolSeasonID,schoolYearID);
	}
	
	public Collection findSchoolClassesByTeacher(int teacherID) throws FinderException,RemoteException {
		return getSchoolClassHome().findByTeacher(teacherID);
	}
	
	public Collection findSchoolClassesBySchoolAndTeacher(int schoolID,int teacherID) throws FinderException,RemoteException {
		return getSchoolClassHome().findBySchoolAndTeacher(schoolID,teacherID);
	}
	
	public Collection findSchoolClassesBySchoolAndSeasonAndTeacher(int schoolID,int schoolSeasonID,int teacherID) throws FinderException,RemoteException {
		return getSchoolClassHome().findBySchoolAndSeasonAndTeacher(schoolID,schoolSeasonID,teacherID);
	}
	
	public void removeSchoolClass(int schoolClassID) throws FinderException,RemoveException,RemoteException {
		SchoolClass schoolClass = findSchoolClass(new Integer(schoolClassID));
		schoolClass.remove();
	}
	
	public void storeSchoolClass(int schoolClassID,String className,int schoolID,int schoolSeasonID,int schoolYearID,int teacherID) throws CreateException,FinderException,RemoteException {
		SchoolClass schoolClass;
		if ( schoolClassID != -1 )
			schoolClass = findSchoolClass(new Integer(schoolClassID));
		else
			schoolClass = getSchoolClassHome().create();
		
		schoolClass.setSchoolClassName(className);
		schoolClass.setSchoolId(schoolID);
		schoolClass.setSchoolSeasonId(schoolSeasonID);
		schoolClass.setSchoolYearId(schoolYearID);
		if ( teacherID != -1 )
			schoolClass.setTeacherId(teacherID);
		
		schoolClass.store();
	}
}