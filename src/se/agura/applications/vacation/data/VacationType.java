/*
 * $Id: VacationType.java,v 1.2 2004/12/13 14:44:20 anna Exp $
 * Created on 8.12.2004
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
 * Last modified: 8.12.2004 14:05:29 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.2 $
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
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#getMaxDays
	 */
	public int getMaxDays();

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#setMaxDays
	 */
	public void setMaxDays(int maxDays);

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
