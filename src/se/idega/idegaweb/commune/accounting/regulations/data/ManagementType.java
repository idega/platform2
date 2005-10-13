/*
 * $Id: ManagementType.java,v 1.2 2005/10/13 08:09:37 palli Exp $
 * Created on Oct 11, 2005
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
 *  Last modified: $Date: 2005/10/13 08:09:37 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.2 $
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

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ManagementTypeBMPBean#isCommuneManagementType
	 */
	public boolean isCommuneManagementType();

}
