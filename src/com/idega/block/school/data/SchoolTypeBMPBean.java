package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2002 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolTypeBMPBean extends GenericEntity implements SchoolType{

  public static final String NAME = "type_name";
  public static final String INFO = "type_info";
  public static final String LOC_KEY = "loc_key";
  public static final String SCHOOLCATEGORY = "school_category";
  public static final String SCHOOLTYPE = "sch_school_type";
  public static final String MAXSCHOOLAGE = "max_school_age";
	public static final String IS_FREETIME_TYPE = "is_freetime_type";
	public static final String IS_FAMILY_FREETIME_TYPE = "is_family_freetime_type";
	public static final String IS_SELECTABLE = "is_selectable";
	public static final String ORDER = "type_order";

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(NAME,"Schooltype",true,true,String.class);
    this.addAttribute(INFO,"Info",true,true,String.class);
    this.addAttribute(MAXSCHOOLAGE,"Max school age",true,true,Integer.class);
    this.addAttribute(LOC_KEY,"Localization key",String.class);
		this.addAttribute(IS_FREETIME_TYPE,"Is freetime type",Boolean.class);
		this.addAttribute(IS_FAMILY_FREETIME_TYPE,"Is freetime type",Boolean.class);
		this.addAttribute(IS_SELECTABLE,"Is selectable",Boolean.class);
		this.addAttribute(ORDER,"Order",true,true,Integer.class);
    
    addManyToOneRelationship(SCHOOLCATEGORY, SchoolCategory.class);
  }

  public String getEntityName() {
    return SCHOOLTYPE;
  }

  public String getName(){
    return getSchoolTypeName();
  }

  public void setSchoolTypeName(String name){
    setColumn(NAME,name);
  }

  public String getSchoolTypeName(){
    return getStringColumnValue(NAME);
  }

   public void setSchoolTypeInfo(String info){
    setColumn(INFO,info);
  }

  public String getSchoolTypeInfo(){
    return getStringColumnValue(INFO);
  }

	public SchoolCategory getCategory(){
		return (SchoolCategory) getColumnValue(SCHOOLCATEGORY);
	}

  public String getSchoolCategory(){
    return getStringColumnValue(SCHOOLCATEGORY);
  }

	public void setCategory(SchoolCategory category){
		setColumn(SCHOOLCATEGORY,category);
	}

  public void setSchoolCategory(String category){
    setColumn(SCHOOLCATEGORY,category);
  }

  public String getLocalizationKey(){
    return getStringColumnValue(LOC_KEY);
  }

  public void setLocalizationKey(String key){
    setColumn(LOC_KEY,key);
  }

  public int getMaxSchoolAge(){
    return getIntColumnValue(MAXSCHOOLAGE);
  }

  public void setMaxSchoolAge(int maxAge){
    setColumn(MAXSCHOOLAGE,maxAge);
  }
  
  public boolean getIsFreetimeType() {
  	return getBooleanColumnValue(IS_FREETIME_TYPE, false);
  }
  
  public void setIsFreetimeType(boolean isFreetimeType) {
  	setColumn(IS_FREETIME_TYPE, isFreetimeType);
  }

  public boolean isSelectable() {
  		return getBooleanColumnValue(IS_SELECTABLE, false);
  }
  
  public void setSelectable(boolean isSelectable) {
  		setColumn(IS_SELECTABLE, isSelectable);
  }

  public boolean getIsFamilyFreetimeType() {
  	return getBooleanColumnValue(IS_FAMILY_FREETIME_TYPE, false);
  }
  
  public void setIsFamilyFreetimeType(boolean isFamilyFreetimeType) {
  	setColumn(IS_FAMILY_FREETIME_TYPE, isFamilyFreetimeType);
  }

  public int getOrder(){
		return getIntColumnValue(ORDER);
	}

	public void setOrder(int order){
		setColumn(ORDER,order);
	}
  
  public Collection ejbFindAllSchoolTypes() throws javax.ejb.FinderException{
      Table table = new Table(this);
      SelectQuery query = new SelectQuery(table);
      query.addColumn(new WildCardColumn());
      query.addOrder(table,NAME,true); 
      return idoFindPKsByQuery(query);
  }

  public Collection ejbFindAllByCategory(String category) throws javax.ejb.FinderException {
      Table table = new Table(this);
	  SelectQuery query = new SelectQuery(table);
	  query.addColumn(new WildCardColumn());
	  query.addCriteria(new MatchCriteria(table,SCHOOLCATEGORY,MatchCriteria.EQUALS,category,true));
	  return idoFindPKsByQuery(query);
  }

	public Collection ejbFindAllByCategory(String category, boolean showFreetimeTypes) throws javax.ejb.FinderException {
	    Table table = new Table(this);
	    SelectQuery query = new SelectQuery(table);
	    query.addColumn(new WildCardColumn());
	    query.addCriteria(new MatchCriteria(table,SCHOOLCATEGORY,MatchCriteria.EQUALS,category,true));
	    if(!showFreetimeTypes){
	        query.addCriteria(new OR(new MatchCriteria(table,IS_FREETIME_TYPE,MatchCriteria.EQUALS,false),new MatchCriteria(table,IS_FREETIME_TYPE,MatchCriteria.IS,MatchCriteria.NULL)));
	    }
	    return super.idoFindPKsByQuery(query);
	    /*
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEqualsQuoted(SCHOOLCATEGORY,category);
		if (!showFreetimeTypes) {
			query.appendAnd().appendLeftParenthesis().appendEquals(IS_FREETIME_TYPE, false).appendOr().append(IS_FREETIME_TYPE).appendIsNull().appendRightParenthesis();
		}
		return super.idoFindPKsByQuery(query);*/
	}

	/**
	 *	Finds one SchoolType from a typeKey.
	 *	@throws javax.ejb.FinderException if no SchoolType is found.	
	 */
  public Integer ejbFindByTypeKey(String typeKey) throws javax.ejb.FinderException{
      Table table = new Table(this);
	  SelectQuery query = new SelectQuery(table);
	  query.addColumn(new WildCardColumn());
	  query.addCriteria(new MatchCriteria(table,LOC_KEY,MatchCriteria.EQUALS,typeKey,true));
  	  return (Integer)super.idoFindOnePKByQuery(query);
  }
  
  public Collection ejbFindAllFreetimeTypes() throws FinderException {
      Table table = new Table(this);
	  SelectQuery query = new SelectQuery(table);
	  query.addColumn(new WildCardColumn());
	  query.addCriteria(new MatchCriteria(table,IS_FREETIME_TYPE,MatchCriteria.EQUALS,true));
  	  return idoFindPKsByQuery(query);
  }
}