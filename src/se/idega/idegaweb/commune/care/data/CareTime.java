/*
 * $Id: CareTime.java,v 1.2 2004/12/05 09:05:33 laddi Exp $
 * Created on 1.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2004/12/05 09:05:33 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface CareTime extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.care.data.CareTimeBMPBean#getCode
	 */
	public String getCode();

	/**
	 * @see se.idega.idegaweb.commune.care.data.CareTimeBMPBean#getLocalizedKey
	 */
	public String getLocalizedKey();

	/**
	 * @see se.idega.idegaweb.commune.care.data.CareTimeBMPBean#getHours
	 */
	public int getHours();

	/**
	 * @see se.idega.idegaweb.commune.care.data.CareTimeBMPBean#setCode
	 */
	public void setCode(String code);

	/**
	 * @see se.idega.idegaweb.commune.care.data.CareTimeBMPBean#setLocalizedKey
	 */
	public void setLocalizedKey(String localizedKey);

	/**
	 * @see se.idega.idegaweb.commune.care.data.CareTimeBMPBean#setHours
	 */
	public void setHours(int hours);

}
