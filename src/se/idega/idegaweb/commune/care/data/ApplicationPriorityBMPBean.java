/*
 * $Id: ApplicationPriorityBMPBean.java,v 1.1 2005/01/31 13:52:14 anders Exp $
 *
 * Copyright (C) 2005 Idega. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf & Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.care.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Entity bean for storing application priority messages.
 * <p>
 * Last modified: $Date: 2005/01/31 13:52:14 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 * @see ChildCareApplicationBMPBean
 */
public class ApplicationPriorityBMPBean extends GenericEntity implements ApplicationPriority {

	private static final String ENTITY_NAME = "comm_application_priority";

	private static final String COLUMN_ID = "id";
	private static final String COLUMN_APPLICATION_ID = "application_id";
	private static final String COLUMN_PRIORITY_DATE = "priority_date";
	private static final String COLUMN_MESSAGE = "message";

	private static final int MAX_MESSAGE_LENGTH = 2000;

	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		setAsPrimaryKey(getIDColumnName(), true);
		addManyToOneRelationship(COLUMN_APPLICATION_ID, ChildCareApplication.class);
		addAttribute(COLUMN_PRIORITY_DATE, "Date for when application priority set", Date.class);
		addAttribute(COLUMN_MESSAGE, "Message for the cause of priority", java.lang.String.class, MAX_MESSAGE_LENGTH);
	}

	/**
	 * Returns the id for the application.
	 */
	public int getApplicationId() {
		return getIntColumnValue(COLUMN_APPLICATION_ID);	
	}

	/**
	 * Returns the application.
	 */
	public ChildCareApplication getApplication() {
		return (ChildCareApplication) getColumnValue(COLUMN_APPLICATION_ID);	
	}

	/**
	 * Returns the timestamp for when the application priority was set.
	 */
	public Date getPriorityDate() {
		return getDateColumnValue(COLUMN_PRIORITY_DATE);	
	}
	
	/**
	 * Returns the message for the priority cause.
	 */
	public String getMessage() {
		return getStringColumnValue(COLUMN_MESSAGE);	
	}
	
	/**
	 * Sets the application id.
	 * @param applicationId the application id to set
	 */
	public void setApplicationId(int applicationId) { 
		setColumn(COLUMN_APPLICATION_ID, applicationId); 
	}
	
	/**
	 * Sets the application.
	 * @param application the application to set
	 */
	public void setApplication(ChildCareApplication application) {
		if (application != null) {
			setColumn(COLUMN_APPLICATION_ID, ((Integer) application.getPrimaryKey()).intValue());
		}
	}

	/**
	 * Sets the date for when the application priority was set.
	 * @param priorityDate the date to set
	 */
	public void setPriorityDate(Date priorityDate) { 
		setColumn(COLUMN_PRIORITY_DATE, priorityDate); 
	}
	
	/**
	 * Sets the message for the priority cause.
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		if (message != null && message.length() > MAX_MESSAGE_LENGTH) {
			message = message.substring(0, MAX_MESSAGE_LENGTH);
		}
		setColumn(COLUMN_MESSAGE, message); 
	}

	/**
	 * Finds all application priorities for the specified time period and provider.
	 * @param from the from date
	 * @param to the to date
	 * @param providerId the id for a provider
	 * @return collection of entries found
	 * @throws FinderException
	 */
	public Collection ejbFindByPeriodAndProvider(Date from, Date to, int providerId) throws FinderException {
		Table table = new Table(this, "p");
		Table applicationTable = new Table(ChildCareApplication.class, "a");
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addJoin(table, COLUMN_APPLICATION_ID, applicationTable, (new ChildCareApplicationBMPBean()).getIDColumnName());
		if (from != null) {
			query.addCriteria(new MatchCriteria(table, COLUMN_PRIORITY_DATE, MatchCriteria.GREATEREQUAL, from));
		}
		if (to != null) {
			query.addCriteria(new MatchCriteria(table, COLUMN_PRIORITY_DATE, MatchCriteria.LESSEQUAL, to));
		}
		if (providerId > 0) {
			query.addCriteria(new MatchCriteria(applicationTable, ChildCareApplicationBMPBean.PROVIDER_ID, MatchCriteria.EQUALS, providerId));
		}
		query.addOrder(new Order(new Column(table, COLUMN_PRIORITY_DATE), true));
		query.addOrder(new Order(new Column(applicationTable, ChildCareApplicationBMPBean.PROVIDER_ID), true));
		return idoFindPKsByQuery(query);
	}
}
