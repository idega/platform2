/*
 * $Id: AdultEducationSession.java,v 1.4 2005/05/30 10:01:42 laddi Exp $
 * Created on May 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.sql.Date;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOSession;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/30 10:01:42 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
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
}
