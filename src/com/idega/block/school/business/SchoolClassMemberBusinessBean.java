package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.data.IDOLookup;

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

	public SchoolClassMember findClassMemberInClass(int studentID, int schoolClassID) throws FinderException, RemoteException {
		return getSchoolClassMemberHome().findByUserAndSchoolClass(studentID, schoolClassID);
	}

	public Collection findClassMember(int studentID) throws FinderException, RemoteException {
		return getSchoolClassMemberHome().findByStudent(studentID);
	}

	public void removeSchoolClassMemberFromClass(int studentID, int schoolClassID) throws FinderException, RemoveException, RemoteException {
		SchoolClassMember member = findClassMemberInClass(studentID, schoolClassID);
		member.remove();
	}
	
	public void removeSchoolClassMember(int studentID) throws FinderException, RemoteException, RemoveException {
		Collection members = findClassMember(studentID);
		if ( members != null ) {
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				SchoolClassMember member = (SchoolClassMember) iter.next();
				member.remove();
			}	
		}	
	}
	
	public void storeSchoolClassMember(int studentID,int schoolClassID,Timestamp registerDate,int registrator) throws CreateException, FinderException, RemoteException {
		SchoolClassMember member;
		if ( schoolClassID != -1 )
			member = this.findClassMemberInClass(studentID,schoolClassID);
		else
			member = this.getSchoolClassMemberHome().create();
			
		member.setClassMemberId(studentID);
		member.setSchoolClassId(schoolClassID);
		if ( registerDate != null )
			member.setRegisterDate(registerDate);
		if ( registrator != -1 )
			member.setRegistratorId(registrator);
		
		member.store();
	}

}
