/*
 * $Id: AfterSchoolChoice.java,v 1.2.2.1 2005/11/16 23:23:27 sigtryggur Exp $
 * Created on Aug 9, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/11/16 23:23:27 $ by $Author: sigtryggur $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2.2.1 $
 */
public interface AfterSchoolChoice extends IDOEntity, ChildCareApplication {

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getSchoolSeasonId
	 */
	public int getSchoolSeasonId();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getPayerName
	 */
	public String getPayerName();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getPayerPersonalID
	 */
	public String getPayerPersonalID();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardType
	 */
	public String getCardType();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardNumber
	 */
	public String getCardNumber();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardValidMonth
	 */
	public int getCardValidMonth();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardValidYear
	 */
	public int getCardValidYear();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getFClass
	 */
	public boolean getFClass();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setSchoolSeasonId
	 */
	public void setSchoolSeasonId(int schoolSeasonID);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setPayerName
	 */
	public void setPayerName(String name);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setPayerPersonalID
	 */
	public void setPayerPersonalID(String personalID);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardType
	 */
	public void setCardType(String type);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardNumber
	 */
	public void setCardNumber(String number);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardValidMonth
	 */
	public void setCardValidMonth(int month);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardValidYear
	 */
	public void setCardValidYear(int year);
	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setFClass
	 */
	public void setFClass(boolean fClass);
}
