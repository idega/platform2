/*
 * $Id: AdultEducationSession.java,v 1.1 2005/05/25 13:06:37 laddi Exp $
 * Created on May 24, 2005
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


/**
 * Last modified: $Date: 2005/05/25 13:06:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
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
}