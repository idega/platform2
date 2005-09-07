/*
 * $Id: ManagementType.java,v 1.1 2005/09/07 11:39:46 palli Exp $
 * Created on Sep 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.regulations.data;



import com.idega.data.IDOEntity;


/**
 * 
 *  Last modified: $Date: 2005/09/07 11:39:46 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public interface ManagementType extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ManagementTypeBMPBean#setManagementType
	 */
	public void setManagementType(String type);

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ManagementTypeBMPBean#getManagementType
	 */
	public String getManagementType();

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ManagementTypeBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String type);

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ManagementTypeBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

}
