/*
 * $Id: VacationType.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 25.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import com.idega.data.IDOEntity;
import com.idega.data.MetaDataCapable;


/**
 * Last modified: 25.11.2004 14:19:42 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public interface VacationType extends IDOEntity, MetaDataCapable {

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#getAllowsForwarding
	 */
	public boolean getAllowsForwarding();

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#setAllowsForwarding
	 */
	public void setAllowsForwarding(boolean allowes);

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#getTypeName
	 */
	public String getTypeName();

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#setTypeName
	 */
	public void setTypeName(String typeName);

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#getLocalizedKey
	 */
	public String getLocalizedKey();

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#setLocalizedKey
	 */
	public void setLocalizedKey(String localizedKey);

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#setExtraTypeInformation
	 */
	public void setExtraTypeInformation(String key, String value, String type);

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#getExtraTypeInformation
	 */
	public String getExtraTypeInformation(String key);

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#getExtraTypeInformationType
	 */
	public String getExtraTypeInformationType(String key);

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#removeExtraTypeInformation
	 */
	public void removeExtraTypeInformation(String key);
}
