package com.idega.block.school.data;

import com.idega.data.*;
import java.sql.SQLException;
import java.util.Collection;
import javax.ejb.FinderException;

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
  public static final String TYPECATEGORY = "category_id";
  public static final String SCHOOLCATEGORY = "school_category";
  public static final String SCHOOLTYPE = "sch_school_type";

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(SCHOOLCATEGORY,"category",true,true,String.class);
    this.addAttribute(NAME,"Schooltype",true,true,String.class);
    this.addAttribute(INFO,"Info",true,true,String.class);
    this.addAttribute(LOC_KEY,"Localization key",String.class);
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

  public String getSchoolCategory(){
    return getStringColumnValue(SCHOOLCATEGORY);
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

  public Collection ejbFindAllSchoolTypes() throws javax.ejb.FinderException{
    return super.idoFindAllIDsBySQL();
  }

  public Collection ejbFindAllByCategory(String category) throws javax.ejb.FinderException {
    return super.idoFindAllIDsByColumnBySQL(SCHOOLCATEGORY,category);
  }

}