/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.atvr.supplier.application.data;

import com.idega.data.GenericEntity;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ProductCategoryBMPBean extends GenericEntity implements ProductCategory {
	private final static String ENTITY_NAME = "wine_prodcat";

	protected final static String CATEGORY = "category";
	protected final static String DESCRIPTION = "description";
	protected final static String BELONGS_TO = "belongs_to";

	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(CATEGORY,"",true,true,java.lang.String.class,20);
		addAttribute(DESCRIPTION,"",true,true,java.lang.String.class,1000);
		
		addManyToOneRelationship(BELONGS_TO,ProductCategory.class);
	}
	
	public void setCategory(String category) {
		setColumn(CATEGORY,category);	
	}
	
	public void setDescription(String description) {
		setColumn(DESCRIPTION,description);	
	}
	
	public void setBelongsToCategory(int id) {
		setColumn(BELONGS_TO,id);			
	}
	
	public String getCategory() {
		return getStringColumnValue(CATEGORY);
	}
	
	public String getDescription() {
		return getStringColumnValue(DESCRIPTION);	
	}
	
	public int getBelongsToCategoryId() {
		return getIntColumnValue(BELONGS_TO);	
	}

	public ProductCategory getBelongsToCategory() {
		return (ProductCategory)getColumnValue(BELONGS_TO);	
	}
	
	public Collection ejbFindAllMainCategories() throws FinderException, RemoteException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(BELONGS_TO);
		sql.append(" is null");
				
		return (Collection)super.idoFindPKsBySQL(sql.toString());		
	}
	
	public Collection ejbFindAllCategoriesBelongingTo(String category) throws FinderException, RemoteException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(BELONGS_TO);
		sql.append(" = ");
		sql.append(category);
				
		return (Collection)super.idoFindPKsBySQL(sql.toString());				
	}
	
	public Collection ejbFindAllByCategory(String category) throws FinderException, RemoteException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(CATEGORY);
		sql.append(" = ");
		sql.append(category);
				
		return (Collection)super.idoFindPKsBySQL(sql.toString());				
	}
}