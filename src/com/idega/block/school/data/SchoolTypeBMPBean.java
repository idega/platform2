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
  public static final String SCHOOLTYPE = "sch_school_type";

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute("type_name","Schooltype",true,true,String.class);
    this.addAttribute("type_info","Info",true,true,String.class);
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

  public Collection ejbFindAllSchoolTypes() throws javax.ejb.FinderException{
    return super.idoFindAllIDsBySQL();
  }

}