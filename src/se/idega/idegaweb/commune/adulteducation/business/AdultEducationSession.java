/*
 * $Id: AdultEducationSession.java,v 1.5 2005/06/02 06:24:37 laddi Exp $
 * Created on Jun 1, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.sql.Date;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOSession;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/06/02 06:24:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public interface AdultEducationSession extends IBOSession {

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getSchoolSeason
	 */
	public SchoolSeason getSchoolSeason() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setSeason
	 */
	public void setSeason(Object seasonPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getSchoolType
	 */
	public SchoolType getSchoolType() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setSchoolType
	 */
	public void setSchoolType(Object schoolTypePK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getStudyPathGroup
	 */
	public SchoolStudyPathGroup getStudyPathGroup() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setStudyPathGroup
	 */
	public void setStudyPathGroup(Object studyPathGroupPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getCourse
	 */
	public AdultEducationCourse getCourse() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setCourse
	 */
	public void setCourse(Object coursePK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getSchoolClass
	 */
	public SchoolClass getSchoolClass() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setSchoolClass
	 */
	public void setSchoolClass(Object schoolClassPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getDate
	 */
	public Date getDate() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setDate
	 */
	public void setDate(Date date) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getSort
	 */
	public int getSort() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setSort
	 */
	public void setSort(int sort) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getChoice
	 */
	public AdultEducationChoice getChoice() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setChoice
	 */
	public void setChoice(Object choicePK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getStudent
	 */
	public User getStudent() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setStudent
	 */
	public void setStudent(String userPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#setStudentUniqueID
	 */
	public void setStudentUniqueID(String userUniqueID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getSchool
	 */
	public School getSchool() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationSessionBean#getSchoolPK
	 */
	public Object getSchoolPK() throws java.rmi.RemoteException;
}
