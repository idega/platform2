/*
 * $Id: InvoiceReceiverBMPBean.java,v 1.1 2003/11/05 15:55:19 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.data;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * Entity bean for setting users as invoice receivers.
 * <p>
 * Last modified: $Date: 2003/11/05 15:55:19 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class InvoiceReceiverBMPBean extends GenericEntity implements InvoiceReceiver {

	private static final String ENTITY_NAME = "cacc_invoice_receiver";

	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_IS_RECEIVER = "is_receiver";
	
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
		return COLUMN_USER_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addOneToOneRelationship(getIDColumnName(), User.class);
		setAsPrimaryKey(getIDColumnName(), true);
		addAttribute(COLUMN_IS_RECEIVER, "Is receiver of invoices (y/n)", true, true, Boolean.class);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER_ID);
	}
	
	public int getUserId() {
		return getIntColumnValue(COLUMN_USER_ID);
	}

	public boolean getIsReceiver() {
		boolean isReceiver = false;
		Boolean b = (Boolean) this.getColumnValue(COLUMN_IS_RECEIVER);
		if (b != null) {
			isReceiver = b.booleanValue();
		}
		return isReceiver;
	}
	
	public void setUser(Integer userId) {
		setColumn(COLUMN_USER_ID, userId);
	}
	
	public void setUser(int userId) {
		setColumn(COLUMN_USER_ID, userId);
	}
	
	public void setUser(User user) {
		setColumn(COLUMN_USER_ID, user);
	}

	public void setIsReceiver(boolean b) {
		this.setColumn(COLUMN_IS_RECEIVER, b);
	}

	/**
	 * Finds the invoice receiver for the specified user id.
	 * @throws FinderException
	 */
	public Integer ejbFindByUser(int userId) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_USER_ID, userId);
		return (Integer) idoFindOnePKByQuery(query);
	}

	/**
	 * Finds the invoice receiver for the specified user.
	 * @throws FinderException
	 */
	public Integer ejbFindByUser(User user) throws FinderException {
		int userId = -1;
		try {
			userId = ((Integer) user.getPrimaryKey()).intValue();
		} catch (Exception e) {}
		return ejbFindByUser(userId);
	}
}
