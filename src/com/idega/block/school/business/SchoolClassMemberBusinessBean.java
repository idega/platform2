package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOLookup;
import com.idega.user.data.User;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolClassMemberBusinessBean extends CaseBusinessBean implements SchoolClassMemberBusiness, CaseBusiness {

	public SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}

	public SchoolClassMember findClassMemberInClass(int studentID, int schoolClassID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByUserAndSchoolClass(studentID, schoolClassID);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public SchoolClassMember findByStudentAndSeason(User user, SchoolSeason season) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByUserAndSeason(user, season);
		}
		catch (FinderException fe) {
			return null;
		}	
	}

	public Collection findClassMember(int studentID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByStudent(studentID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}
	
	public Collection findStudentsInClass(int studentClassID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findBySchoolClass(studentClassID);	
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findStudentsBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findBySchoolAndSeasonAndYear(schoolID, seasonID, yearID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findStudentsBySchoolAndSeason(int schoolID, int seasonID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findBySchoolAndSeason(schoolID, seasonID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public void removeSchoolClassMemberFromClass(int studentID, int schoolClassID) throws RemoteException {
		try {
			SchoolClassMember member = getSchoolClassMemberHome().findByUserAndSchoolClass(studentID, schoolClassID);
			member.remove();
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
		catch (RemoveException re) {
			re.printStackTrace(System.err);
		}
	}
	
	public void removeSchoolClassMember(int studentID) throws RemoteException {
		try {
			Collection members = findClassMember(studentID);
			if ( !members.isEmpty() ) {
				Iterator iter = members.iterator();
				while (iter.hasNext()) {
					SchoolClassMember member = (SchoolClassMember) iter.next();
					member.remove();
				}	
			}	
		}
		catch (RemoveException re) {
			re.printStackTrace(System.err);
		}
	}
	
	public void storeSchoolClassMember(int studentID,int schoolClassID,Timestamp registerDate,int registrator) throws RemoteException {
		storeSchoolClassMember(studentID,schoolClassID,registerDate,registrator,null);	
	}

	public void storeSchoolClassMember(int studentID,int schoolClassID,Timestamp registerDate,int registrator,String notes) throws RemoteException {
		try {
			SchoolClassMember member = findClassMemberInClass(studentID,schoolClassID);
			if (member == null)
				member = this.getSchoolClassMemberHome().create();
				
			if (member != null ) {
				member.setClassMemberId(studentID);
				member.setSchoolClassId(schoolClassID);
				if ( registerDate != null )
					member.setRegisterDate(registerDate);
				if ( registrator != -1 )
					member.setRegistratorId(registrator);
				if (notes != null)
					member.setNotes(notes);
				
				member.store();
			}
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);	
		}	
	}

}
