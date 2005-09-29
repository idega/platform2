/*
 * $Id: AfterSchoolCareDaysBMPBean.java,v 1.2 2005/09/29 07:16:04 laddi Exp $
 * Created on Aug 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/09/29 07:16:04 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class AfterSchoolCareDaysBMPBean extends GenericEntity  implements AfterSchoolCareDays{

	private static final String ENTITY_NAME = "comm_after_school_care_days";
	
	private static final String COLUMN_APPLICATION = "application_id";
	private static final String COLUMN_DAY_OF_WEEK = "day_of_week";
	private static final String COLUMN_TIME_OF_DEPARTURE = "time_of_departure";
	private static final String COLUMN_PICKED_UP = "picked_up";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addManyToOneRelationship(COLUMN_APPLICATION, ChildCareApplication.class);
		
		addAttribute(COLUMN_DAY_OF_WEEK, "Day of week", Integer.class);
		addAttribute(COLUMN_TIME_OF_DEPARTURE, "Time of departure", Timestamp.class);
		addAttribute(COLUMN_PICKED_UP, "Picked up", Boolean.class);
	}
	
	//Getters
	public ChildCareApplication getApplication() {
		return (ChildCareApplication) getColumnValue(COLUMN_APPLICATION);
	}
	
	public Object getApplicationPK() {
		return getIntegerColumnValue(COLUMN_APPLICATION);
	}
	
	public int getDayOfWeek() {
		return getIntColumnValue(COLUMN_DAY_OF_WEEK);
	}
	
	public Time getTimeOfDeparture() {
		Timestamp timestamp = getTimestampColumnValue(COLUMN_TIME_OF_DEPARTURE);
		if (timestamp != null) {
			IWTimestamp stamp = new IWTimestamp(timestamp);
			return stamp.getTime();
		}
		return null;
	}
	
	public boolean isPickedUp() {
		return getBooleanColumnValue(COLUMN_PICKED_UP, true);
	}
	
	//Setters
	public void setApplication(ChildCareApplication application) {
		setColumn(COLUMN_APPLICATION, application);
	}
	
	public void setApplication(Object applicationPK) {
		setColumn(COLUMN_APPLICATION, applicationPK);
	}
	
	public void setDayOfWeek(int dayOfWeek) {
		setColumn(COLUMN_DAY_OF_WEEK, dayOfWeek);
	}
	
	public void setTimeOfDeparture(Time time) {
		IWTimestamp stamp = new IWTimestamp(time);
		setColumn(COLUMN_TIME_OF_DEPARTURE, stamp.getTimestamp());
	}
	
	public void setPickedUp(boolean pickedUp) {
		setColumn(COLUMN_PICKED_UP, pickedUp);
	}
	
	//Finders
	public Collection ejbFindAllByApplication(ChildCareApplication application) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_APPLICATION, MatchCriteria.EQUALS, application));
		query.addOrder(table, COLUMN_DAY_OF_WEEK, true);
		
		return idoFindPKsByQuery(query);
	}
}