/*
 * $Id: AfterSchoolChoiceHome.java,v 1.2 2005/08/09 16:34:50 laddi Exp $
 * Created on Aug 9, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/08/09 16:34:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface AfterSchoolChoiceHome extends IDOHome {

	public AfterSchoolChoice create() throws javax.ejb.CreateException;

	public AfterSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndSeason
	 */
	public Collection findByChildAndSeason(Integer childID, Integer seasonID) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndChoiceNumberAndSeason
	 */
	public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID, Integer choiceNumber, Integer seasonID)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndChoiceNumberAndSeason
	 */
	public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID, Integer choiceNumber, Integer seasonID,
			String[] caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndProviderAndSeason
	 */
	public AfterSchoolChoice findByChildAndProviderAndSeason(int childID, int providerID, int seasonID,
			String[] caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(int providerId, CaseStatus caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(School provider, String caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(School provider, CaseStatus caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(int providerId, String caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndNotInStatus
	 */
	public Collection findAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndNotInStatus
	 */
	public Collection findAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus, String sorting)
			throws FinderException;
}
