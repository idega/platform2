/*
 * Created on 26.3.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.trade.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.Group;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CreditCardInformationBMPBean extends GenericEntity implements CreditCardInformation {

	private static final String ENTITY_NAME = "CC_INFORMATION";
	private static final String COLUMN_TYPE = "CC_TYPE";
	private static final String COLUMN_MERCHANT_PK = "CC_MERCHANT_PK";
	private static final String COLUMN_SUPPLIER_MANAGER_ID = "SUPPLIER_MANAGER_ID";
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_TYPE, "type", true, true, String.class);
		this.addAttribute(COLUMN_MERCHANT_PK, "merchantPK", true, true, String.class);
		this.addManyToOneRelationship( COLUMN_SUPPLIER_MANAGER_ID, Group.class);
	}

	public String getMerchantPKString() {
		return getStringColumnValue(COLUMN_MERCHANT_PK);
	}
	
	public String getType() {
		return getStringColumnValue(COLUMN_TYPE);
	}

	public Group getSupplierManager() {
		return (Group) getColumnValue(COLUMN_SUPPLIER_MANAGER_ID);
	}
	
	public void setMerchantPK(Object pk) {
		setColumn(COLUMN_MERCHANT_PK, pk);
	}

	public void setType(String type) {
		setColumn(COLUMN_TYPE, type);
	}
	
	public void setSupplierManager(Group supplierManager) {
		setColumn(COLUMN_SUPPLIER_MANAGER_ID, supplierManager);
	}
	
	public Collection ejbFindBySupplierManager(Group supplierManager) throws FinderException {
		Table table = new Table(this);
		Column col = new Column(table, COLUMN_SUPPLIER_MANAGER_ID);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(col, MatchCriteria.EQUALS, supplierManager));
		
		return idoFindPKsByQuery(query);
	}
	
}
