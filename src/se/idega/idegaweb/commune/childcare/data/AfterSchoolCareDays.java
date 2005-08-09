/*
 * $Id: AfterSchoolCareDays.java,v 1.1 2005/08/09 16:35:19 laddi Exp $
 * Created on Aug 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Time;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/08/09 16:35:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface AfterSchoolCareDays extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#getApplication
	 */
	public ChildCareApplication getApplication();

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#getApplicationPK
	 */
	public Object getApplicationPK();

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#getDayOfWeek
	 */
	public int getDayOfWeek();

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#getTimeOfDeparture
	 */
	public Time getTimeOfDeparture();

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#isPickedUp
	 */
	public boolean isPickedUp();

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#setApplication
	 */
	public void setApplication(ChildCareApplication application);

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#setApplication
	 */
	public void setApplication(Object applicationPK);

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#setDayOfWeek
	 */
	public void setDayOfWeek(int dayOfWeek);

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#setTimeOfDeparture
	 */
	public void setTimeOfDeparture(Time time);

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#setPickedUp
	 */
	public void setPickedUp(boolean pickedUp);
}
