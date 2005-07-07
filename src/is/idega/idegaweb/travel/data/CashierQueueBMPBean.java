/*
 * $Id: CashierQueueBMPBean.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


public class CashierQueueBMPBean extends GenericEntity  implements CashierQueue{

	private static final String TABLE_NAME = "TB_CASHIER_QUEUE";
	private static final String COLUMN_SUPPLIER_MANAGER_ID = "SUPPLIER_MANAGER_ID";
	private static final String COLUMN_CASHIER_ID = "CASHIER_ID";
	private static final String COLUMN_OWNER_ID = "OWNER_ID";
	private static final String COLUMN_AUTHENTICATION_NUMBER = "AUTH_NR";
	private static final String COLUMN_IS_VALID = "IS_VALID";
	private static final String COLUMN_CLIENT_NAME = "CLIENT_NAME";
	private static final String COLUMN_CREATION_DATE = "CREATION_DATE";
	private static final String COLUMN_MODIFICATION_DATE = "MODIFICATION_DATE";
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_SUPPLIER_MANAGER_ID, Group.class);
		addManyToOneRelationship(COLUMN_CASHIER_ID, User.class);
		addManyToOneRelationship(COLUMN_OWNER_ID, User.class);
		addAttribute(COLUMN_CLIENT_NAME, "client_name", true, true, String.class, 70);
		addAttribute(COLUMN_IS_VALID, "is_valid", true, true, Boolean.class);
		addAttribute(COLUMN_AUTHENTICATION_NUMBER, "auth_number", true, true, String.class);
		addAttribute(COLUMN_CREATION_DATE, "creation_date", true, true, Timestamp.class);
		addAttribute(COLUMN_MODIFICATION_DATE, "modification_date", true, true, Timestamp.class);

		addManyToManyRelationShip(GeneralBooking.class);
		
		setNullable(COLUMN_CASHIER_ID, true);
		setNullable(COLUMN_OWNER_ID, false);
		setNullable(COLUMN_SUPPLIER_MANAGER_ID, false);
		
		addIndex(COLUMN_SUPPLIER_MANAGER_ID);
	}
	
	public void setDefaultValues() {
		setColumn(COLUMN_IS_VALID, true);
		setColumn(COLUMN_CREATION_DATE, IWTimestamp.getTimestampRightNow());
	}
	
	public void removeAndDisableBookings() throws RemoveException {
		try {
			Collection coll = idoGetRelatedEntities(GeneralBooking.class);
			if (coll != null && !coll.isEmpty()) {
				Iterator iter = coll.iterator();
				GeneralBooking b;
				while (iter.hasNext()) {
					b = (GeneralBooking) iter.next();
					b.setIsValid(false);
					b.store();
				}
			}
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		remove();
	}
	public void remove() throws RemoveException {
		setColumn(COLUMN_IS_VALID, false);
		store();
	}
	
	public void store() {
		setColumn(COLUMN_MODIFICATION_DATE, IWTimestamp.getTimestampRightNow());
		super.store();
	}
	
	public void addBooking(GeneralBooking booking) throws IDOAddRelationshipException {
		this.idoAddTo(booking);
	}
	
	public void setSupplierManager(Group supplierManager) {
		setColumn(COLUMN_SUPPLIER_MANAGER_ID, supplierManager.getPrimaryKey());
	}
	
	public Group getSupplierManager() {
		return (Group) getColumnValue(COLUMN_SUPPLIER_MANAGER_ID);
	}
	
	public void setCashier(User cashier) {
		setColumn(COLUMN_CASHIER_ID, cashier.getPrimaryKey());
	}
	
	public User getCashier() {
		return (User) getColumnValue( COLUMN_CASHIER_ID );
	}
	
	public void setOwner(User owner) {
		setColumn(COLUMN_OWNER_ID, owner.getPrimaryKey());
	}
	
	public User getOwner() {
		return (User) getColumnValue( COLUMN_OWNER_ID );
	}
	
	public void setAuthenticationNumber(String authNumber) {
		setColumn(COLUMN_AUTHENTICATION_NUMBER, authNumber);
	}
	
	public void setClientName(String name) {
		setColumn(COLUMN_CLIENT_NAME, name);
	}
	
	public String getClientName() {
		return getStringColumnValue(COLUMN_CLIENT_NAME);
	}
	
	public String getAuthenticationNumber() {
		return getStringColumnValue(COLUMN_AUTHENTICATION_NUMBER);
	}
	public Collection ejbFindAllBySupplierManager(Group supplierManager) throws FinderException {
		return ejbFindAll(supplierManager, null, null);
	}
	public Collection ejbFindAllByCashier(User cashier) throws FinderException {
		return ejbFindAll(null, cashier, null);
	}
	public Collection ejbFindAllByOwner(User owner) throws FinderException {
		return ejbFindAll(null, null, owner);
	}
	
	public Collection ejbFindAll(Group supplierManager, User cashier, User owner) throws FinderException {
		Table table = new Table(this);
				
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_IS_VALID), MatchCriteria.EQUALS, true));
		if (supplierManager != null) {
			query.addCriteria(new MatchCriteria(new Column(table, COLUMN_SUPPLIER_MANAGER_ID), MatchCriteria.EQUALS, supplierManager));
		}
		if (cashier != null) {
			query.addCriteria(new MatchCriteria(new Column(table, COLUMN_CASHIER_ID), MatchCriteria.EQUALS, cashier));
		}
		if (owner != null) {
			query.addCriteria(new MatchCriteria(new Column(table, COLUMN_OWNER_ID), MatchCriteria.EQUALS, owner));
		}
		query.addOrder(table, getIDColumnName(), true);
		
		return this.idoFindPKsByQuery(query);
	}
	
	public Collection getBookingIDs() throws IDORelationshipException, FinderException {
		return this.idoGetRelatedEntityPKs(this.getStaticInstanceIDO(GeneralBooking.class));
	}
	
	
}
