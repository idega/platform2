/*
 * $Id: AdultEducationChoiceReason.java,v 1.1 2005/05/11 07:16:22 laddi Exp $
 * Created on May 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/05/11 07:16:22 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface AdultEducationChoiceReason extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#getName
	 */
	public String getName();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#getLocalizedKey
	 */
	public String getLocalizedKey();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#isActive
	 */
	public boolean isActive();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#setLocalizedKey
	 */
	public void setLocalizedKey(String localizedKey);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#setIsActive
	 */
	public void setIsActive(boolean isActive);
}
