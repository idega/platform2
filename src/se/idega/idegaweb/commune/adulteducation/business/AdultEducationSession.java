/*
 * $Id: AdultEducationSession.java,v 1.2 2005/05/26 07:16:21 laddi Exp $
 * Created on May 26, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOSession;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/26 07:16:21 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
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
	public void setStudent(String userUniqueID) throws java.rmi.RemoteException;
}
