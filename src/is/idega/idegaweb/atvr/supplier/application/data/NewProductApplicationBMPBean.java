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

import com.idega.core.user.data.User;
import com.idega.data.GenericEntity;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;


/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class NewProductApplicationBMPBean extends GenericEntity implements NewProductApplication {
	private final static String ENTITY_NAME = "wine_newprod";

	protected final static String APPLICATION_TYPE = "appl_type";
	protected final static String DESCRIPTION = "description";
	protected final static String DESCRIPTION2 = "description_2";
	protected final static String QUANTITY = "quantity";
	protected final static String STRENGTH = "strength";
	protected final static String PRODUCER = "producer";
	protected final static String COUNTRY_OF_ORIGIN = "country";
	protected final static String BAR_CODE = "bar_code";
	protected final static String PRODUCT_CATEGORY = "prod_category";
	protected final static String SUPPLIER = "supplier";
	protected final static String APPLICATION_SENT = "sent";
	protected final static String PRICE = "price";
	protected final static String SUPPLIERS_PROD_ID = "suppl_prod_id";
	protected final static String AMOUNT = "amount";
	protected final static String WEIGHT = "weight";
	protected final static String STATUS = "status";
	protected final static String CARBON_MONOXIDE = "monoxide";

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

		addAttribute(APPLICATION_TYPE,"",true,true,java.lang.String.class,1);
		addAttribute(DESCRIPTION,"",true,true,java.lang.String.class,1000);
		addAttribute(DESCRIPTION2,"",true,true,java.lang.String.class,1000);
		addAttribute(QUANTITY,"",true,true,java.lang.String.class,100);
		addAttribute(STRENGTH,"",true,true,java.lang.String.class,100);
		addAttribute(PRODUCER,"",true,true,java.lang.String.class,1000);
		addAttribute(COUNTRY_OF_ORIGIN,"",true,true,java.lang.String.class,1000);
		addAttribute(BAR_CODE,"",true,true,java.lang.String.class,50);
		addAttribute(APPLICATION_SENT,"",true,true,java.sql.Timestamp.class);
		addAttribute(PRICE,"",true,true,java.lang.Float.class);
		addAttribute(SUPPLIERS_PROD_ID,"",true,true,java.lang.String.class,100);
		addAttribute(AMOUNT,"",true,true,java.lang.String.class,100);
		addAttribute(WEIGHT,"",true,true,java.lang.String.class,100);
		addAttribute(STATUS,"",true,true,java.lang.String.class,1);
		addAttribute(CARBON_MONOXIDE,"",true,true,java.lang.Float.class);
				
		addManyToOneRelationship(PRODUCT_CATEGORY,ProductCategory.class);
		addManyToOneRelationship(SUPPLIER,User.class);		
	}
	
	public void setApplicationType(String type) {
		setColumn(APPLICATION_TYPE,type);
	}
	
	public void setDescription(String description) {
		setColumn(DESCRIPTION,description);
	}	
	
	public void setDescription2(String description) {		
		setColumn(DESCRIPTION2,description);
	}
	
	public void setQuantity(String qty) {
		setColumn(QUANTITY,qty);
	}
	
	public void setStrength(String strength) {
		setColumn(STRENGTH,strength);
	}
	
	public void setProducer(String producer) {
		setColumn(PRODUCER,producer);
	}
	
	public void setCountryOfOrigin(String country) {
		setColumn(COUNTRY_OF_ORIGIN,country);
	}
	
	public void setBarCode(String code) {
		setColumn(BAR_CODE,code);
	}
	
	public void setProductCategoryId(int id) {
		setColumn(PRODUCT_CATEGORY,id);
	}
	
	public void setProductCategory(ProductCategory cat) {
		setColumn(PRODUCT_CATEGORY,cat);			
	}
	
	public void setSupplierId(int id) {
		setColumn(SUPPLIER,id);
	}
	
	public void setSupplier(User supplier) {
		setColumn(SUPPLIER,supplier);		
	}

	public void setApplicationSent(Timestamp sent) {
		setColumn(APPLICATION_SENT,sent);
	}
	
	public void setPrice(float price) {
		setColumn(PRICE,price);
	}
	
	public void setSuppliersProductId(String id) {
		setColumn(SUPPLIERS_PROD_ID,id);
	}
	
	public void setAmount(String amount) {		
		setColumn(AMOUNT,amount);
	}
	
	public void setWeigth(String weight) {
		setColumn(WEIGHT,weight);	
	}
	
	public void setStatus(String status) {
		setColumn(STATUS,status);
	}

	public void setCarbonMonoxide(float monoxide) {
		setColumn(CARBON_MONOXIDE,monoxide);	
	}



	public String getApplicationType() {
		return getStringColumnValue(APPLICATION_TYPE);
	}
	
	public String getDescription() {
		return getStringColumnValue(DESCRIPTION);
	}	
	
	public String getDescription2() {
		return getStringColumnValue(DESCRIPTION2);
	}
	
	public String getQuantity() {
		return getStringColumnValue(QUANTITY);
	}
	
	public String getStrength() {
		return getStringColumnValue(STRENGTH);
	}
	
	public String getProducer() {
		return getStringColumnValue(PRODUCER);
	}
	
	public String getCountryOfOrigin() {
		return getStringColumnValue(COUNTRY_OF_ORIGIN);
	}
	
	public String getBarCode() {
		return getStringColumnValue(BAR_CODE);
	}
	
	public int getProductCategoryId() {
		return getIntColumnValue(PRODUCT_CATEGORY);
	}
	
	public ProductCategory getProductCategory() {
		return (ProductCategory)getColumn(PRODUCT_CATEGORY);			
	}
	
	public int getSupplierId() {
		return getIntColumnValue(SUPPLIER);
	}
	
	public User getSupplier() {
		return (User)getColumnValue(SUPPLIER);
	}

	public Timestamp getApplicationSent() {
		return (Timestamp)getColumnValue(APPLICATION_SENT);
	}
	
	public float getPrice(float price) {
		return getFloatColumnValue(PRICE);
	}
	
	public String getSuppliersProductId() {
		return getStringColumnValue(SUPPLIERS_PROD_ID);
	}
	
	public String getAmount() {
		return getStringColumnValue(AMOUNT);
	}
	
	public String getWeigth() {
		return getStringColumnValue(WEIGHT);
	}
	
	public String getStatus() {
		return getStringColumnValue(STATUS);
	}
	
	public float getCarbonMonoxide() {
		return getFloatColumnValue(CARBON_MONOXIDE);	
	}
	
	public Collection ejbFindAll() throws FinderException, RemoteException {
		return super.idoFindAllIDsBySQL();
	}
}