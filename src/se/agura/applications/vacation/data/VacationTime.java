/*
 * $Id: VacationTime.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 16.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: 16.11.2004 11:27:15 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public interface VacationTime extends IDOEntity {

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getYear
	 */
	public int getYear();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getWeekNumber
	 */
	public int getWeekNumber();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getMonday
	 */
	public int getMonday();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getTuesday
	 */
	public int getTuesday();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getWednesday
	 */
	public int getWednesday();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getThursday
	 */
	public int getThursday();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getFriday
	 */
	public int getFriday();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getSaturday
	 */
	public int getSaturday();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getSunday
	 */
	public int getSunday();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#getVacationRequest
	 */
	public VacationRequest getVacationRequest();

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setYear
	 */
	public void setYear(int year);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setWeekNumber
	 */
	public void setWeekNumber(int weekNo);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setMonday
	 */
	public void setMonday(int monday);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setTuesday
	 */
	public void setTuesday(int tuesday);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setWednesday
	 */
	public void setWednesday(int wednesday);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setThursday
	 */
	public void setThursday(int thursday);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setFriday
	 */
	public void setFriday(int friday);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setSaturday
	 */
	public void setSaturday(int saturday);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setSunday
	 */
	public void setSunday(int sunday);

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#setVacationRequest
	 */
	public void setVacationRequest(VacationRequest vacationRequest);
}
