package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * @author gimmi
 */
public class SupplyPoolBMPBean extends GenericEntity  implements SupplyPool{

	private static final String TABLENAME = "SR_SUPPLY_POOL";
	static final String COLUMN_NAME = "POOL_NAME";
	static final String COLUMN_DESCRIPTION = "POOL_DESCRIPTION";
	static final String COLUMN_SUPPLIER_ID = "SR_SUPPLIER_ID";
	static final String COLUMN_IS_DELETED = "IS_DELETED";

	public String getEntityName() {
		return TABLENAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "nafn", true, true, String.class);
		addAttribute(COLUMN_DESCRIPTION, "làsing", true, true, String.class);
		addAttribute(COLUMN_IS_DELETED, "valid", true, true, Boolean.class);
		
		this.addManyToManyRelationShip(Product.class);
		this.addManyToOneRelationship(COLUMN_SUPPLIER_ID, Supplier.class);
	}
	
	public void setDefaultValues() {
		setColumn(COLUMN_IS_DELETED, false);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}
	
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}
	
	public void setSupplier(Supplier supplier) {
		setColumn(COLUMN_SUPPLIER_ID, supplier);
	}
	
	public void setSupplierID(int supplierID) {
		setColumn(COLUMN_SUPPLIER_ID, supplierID);
	}
	
	public Supplier getSupplier() {
		return (Supplier) getColumnValue(COLUMN_SUPPLIER_ID);
	}
	
	public int getSupplierID() {
		return getIntColumnValue(COLUMN_SUPPLIER_ID);
	}
	
	public void addProduct(Product product) throws IDOAddRelationshipException {
		this.idoAddTo(product);
	}
	
	public void removeProduct(Product product) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(product);
	}
		
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		Column column = new Column(table, COLUMN_IS_DELETED);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(column, MatchCriteria.NOTEQUALS, true));
		return this.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindBySupplier(Supplier supplier) throws FinderException {
		Table table = new Table(this);
		Column column = new Column(table, COLUMN_IS_DELETED);
		Column suppID = new Column(table, COLUMN_SUPPLIER_ID);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(column, MatchCriteria.NOTEQUALS, true));
		query.addCriteria(new MatchCriteria(suppID, MatchCriteria.EQUALS, supplier));
		
		return this.idoFindPKsByQuery(query);
	}
	
	public Object ejbFindByProduct(Product product) throws IDORelationshipException, FinderException {
			return ejbFindByProduct(product.getPrimaryKey());
	}
	
	public Object ejbFindByProduct(Object productPK) throws IDORelationshipException, FinderException {
		Table table = new Table(this);
		Table productTable = new Table(Product.class);
		Column column = new Column(table, COLUMN_IS_DELETED);
		Column productID = new Column(productTable, ProductBMPBean.getIdColumnName());
		
		SelectQuery query = new SelectQuery(table);
		query.addManyToManyJoin(table, productTable);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(column, MatchCriteria.NOTEQUALS, true));
		query.addCriteria(new MatchCriteria(productID, MatchCriteria.EQUALS, productPK));
		
		return this.idoFindOnePKByQuery(query);
	}
	
	public void remove() {
		setColumn(COLUMN_IS_DELETED, true);
		store();
	}
	
}
