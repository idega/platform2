package com.idega.block.trade.stockroom.data;

import java.sql.SQLException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.core.location.data.Address;
import com.idega.data.IDOQuery;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class PriceCategoryBMPBean extends com.idega.data.GenericEntity implements PriceCategory{

  public static final String PRICETYPE_PRICE = "sr_pricetype_price";
  public static final String PRICETYPE_DISCOUNT = "sr_pricetype_discount";

  public static final int PRICE_VISIBILITY_PRIVATE = 1;
	public static final int PRICE_VISIBILITY_PUBLIC  = 2;
	public static final int PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC = 3;

  public PriceCategoryBMPBean(){
    super();
  }

  public PriceCategoryBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "Name", true, true, String.class, 255);
    addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class, 255);
    addAttribute(getColumnNameType(),"Type",true,true,String.class,255);
    addAttribute(getColumnNameExtraInfo(), "Aðrar upplysingar", true, true, String.class, 255);
    addAttribute(getColumnNameNetbookingCategory(), "Verðflokkur fyrir netbókun", true, true, Boolean.class, 255);
    addAttribute(getColumnNameSupplierId(),"supplier_id (owner)", true, true, Integer.class, "many_to_one", Supplier.class);
    addAttribute(getColumnNameParentId(),"parent_id", true, true, Integer.class, "many_to_one", PriceCategory.class);
    addAttribute(getColumnNameIsValid(), "is valid", true, true, Boolean.class);
    addAttribute(getColumnNameCountAsPerson(), "count as person", true, true, Boolean.class);
    /** added 20.11.2002 by gimmi */
    addAttribute(getColumnNameVisibility(), "visibility", true, true, Integer.class);
    addAttribute(getColumnNameKey(), "key", true, true, String.class);

    this.addManyToManyRelationShip(Address.class);
    this.addTreeRelationShip();
    
    addIndex("IDX_PRICECAT_1", new String[]{getIDColumnName(), getColumnNameType()});
    addIndex("IDX_PRICECAT_2", new String[]{getIDColumnName(), getColumnNameType(), getColumnNameIsValid()});
    addIndex("IDX_PRICECAT_3", new String[]{getIDColumnName(), getColumnNameType(), getColumnNameIsValid(), getColumnNameParentId()});
    addIndex(getColumnNameType());
    addIndex("IDX_PRICECAT_5", new String[]{getColumnNameType(), getColumnNameIsValid()});
  }

  public void delete() {
    try {
      setColumn(getColumnNameIsValid(), false);
      this.update();
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
  }

  public void setDefaultValues() {
    setColumn(getColumnNameIsValid(), true);
    setColumn(getColumnNameCountAsPerson(), "true");
  }

  public String getEntityName(){
    return getPriceCategoryTableName();
  }
  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }

  public void setName(String name){
    setColumn(getColumnNameName(),name);
  }

  public String getDescription() {
    return getStringColumnValue(getColumnNameDescription());
  }

  public void setDescription(String description) {
    setColumn(getColumnNameDescription(),description);
  }

  public String getExtraInfo(){
    return getStringColumnValue(getColumnNameExtraInfo());
  }

  public void setExtraInfo(String extraInfo){
    setColumn(getColumnNameExtraInfo(),extraInfo);
  }


  public String getType(){
    return getStringColumnValue(getColumnNameType());
  }

  public void setType(String type){
    setColumn(getColumnNameType(),type);
  }

  public void isNetbookingCategory(boolean value){
    setColumn(getColumnNameNetbookingCategory(), value);
  }

  public boolean isNetbookingCategory(){
    return getBooleanColumnValue(getColumnNameNetbookingCategory());
  }

  public void setSupplierId(int id){
    setColumn(getColumnNameSupplierId(), id);
  }

  public int getSupplierId(){
    return getIntColumnValue(getColumnNameSupplierId());
  }

  public void setParentId(int id){
    setColumn(getColumnNameParentId(), id);
  }

  public int getParentId(){
    return getIntColumnValue(getColumnNameParentId());
  }
  
  public void setKey(String key) {
  	setColumn(getColumnNameKey(), key);
  }
  
  public String getKey() {
  	return getStringColumnValue(getColumnNameKey());
  }

  public void setCountAsPerson(boolean countAsPerson) {
    setColumn(getColumnNameCountAsPerson(), countAsPerson);
  }

  public boolean getCountAsPerson() {
    return getBooleanColumnValue(getColumnNameCountAsPerson(), true);
  }

	public int getVisibility() {
		int vis = this.getIntColumnValue(getColumnNameVisibility());	
		if (vis < 1) {
			try {
				isNetbookingCategory(false);
				if (isNetbookingCategory()) {
					setVisibility(PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC);
					update();
					System.out.println("[PriceCategoryBMPBean] backward compatability for visibility");
					return PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC;	
				}else {
					setVisibility(PRICE_VISIBILITY_PRIVATE);
					update();
					System.out.println("[PriceCategoryBMPBean] backward compatability for visibility");
					return PRICE_VISIBILITY_PRIVATE;
				}
			} catch (SQLException e) {
				System.out.println("[PriceCategoryBMPBean] backward compatability for visibility FAILED ("+e.getMessage()+")");
//				e.printStackTrace(System.err);
			}
		}
		return vis;
	}
	
	public Object ejbFindByKey(String key) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsWithSingleQuotes(getColumnNameIsValid(), "Y")
		.appendAndEqualsQuoted(getColumnNameKey(), key);
		return this.idoFindOnePKByQuery(query);
	}
	
	public void setVisibility(int visibility) {
		this.setColumn(getColumnNameVisibility(), visibility);	
	}
	
	public Collection ejbFindBySupplierAndCountAsPerson(int supplierID, boolean countAsPerson) throws FinderException {
		Table table = new Table(this);
		Column supplier = new Column(table, getColumnNameSupplierId());
		Column valid = new Column(table, getColumnNameIsValid());
		Column count = new Column(table, getColumnNameCountAsPerson());
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(supplier, MatchCriteria.EQUALS, supplierID));
		query.addCriteria(new MatchCriteria(count, MatchCriteria.EQUALS, countAsPerson));
		query.addCriteria(new MatchCriteria(valid, MatchCriteria.EQUALS, true));
		
		return this.idoFindPKsBySQL(query.toString());
	}
	

  public static String getColumnNameName() {return "CATEGORY_NAME";}
  public static String getColumnNameDescription() {return "DESCRIPTION";}
  public static String getColumnNameType(){return "CATEGORY_TYPE";}
  public static String getColumnNameExtraInfo() {return "EXTRA_INFO";}
  public static String getColumnNameSupplierId() {return "SUPPLIER_ID";}
  public static String getColumnNameParentId() {return "PARENT_ID";}
  public static String getColumnNameNetbookingCategory() {return "NETBOOKING_CATEGORY";}
  public static String getColumnNameIsValid() {return "IS_VALID";}
  public static String getColumnNameCountAsPerson() {return "COUNT_AS_PERSON";}
  public static String getPriceCategoryTableName() {return "SR_PRICE_CATEGORY";}
	public static String getColumnNameVisibility() {return "PRICE_VISIBILITY";}
	public static String getColumnNameKey() { return "CATEGORY_KEY";}
}

