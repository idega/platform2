package com.idega.block.importer.data;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Iterator;
import java.util.Collection;
import com.idega.data.*;
import java.sql.SQLException;


/**
 * Title:        com.idega.block.importer.data.ImportHandlerBMPBean
 * Description: A table of available Import handlers
 * Copyright:    Idega Software (c) 2002
 * Company:      Idega Software http://www.idega.com
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class ImportHandlerBMPBean extends com.idega.data.GenericEntity implements ImportHandler {


  public ImportHandlerBMPBean() {
    super();
  }

  public ImportHandlerBMPBean(int id) throws SQLException {
    super(id);
  }


  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    this.addAttribute(getClassColumnName(),"Class name",true,true,"java.lang.String",500);
    this.addAttribute(getDescriptionColumnName(),"Description",true,true,"java.lang.String",500);
  }
  public String getEntityName() {
    return "im_handler";
  }

  public static String getNameColumnName(){
    return "name";
  }
  public static String getClassColumnName(){
      return "class_name";
  }

  public static String getDescriptionColumnName(){
    return "description";
  }

  public void setName(String name){
    this.setColumn(getNameColumnName(),name);
  }

  public void setDescription(String description){
    this.setColumn(getDescriptionColumnName(),description);
  }

  public String getName(){
    return this.getStringColumnValue(getNameColumnName());
  }

  public String getDescription(){
    return this.getStringColumnValue(getDescriptionColumnName());
  }
  
    public void setClassName(String className){
    this.setColumn(getClassColumnName(),className);
  }
  
  public String getClassName(){
    return this.getStringColumnValue(getClassColumnName());
  }
  


  /*public Gender ejbHomeGetMaleGender() throws FinderException,RemoteException{
   return ((GenderHome)this.getEJBHome()).findByGenderName(NAME_MALE);
  }

  
  public Integer ejbFindByGenderName(String name) throws FinderException,RemoteException{
   Collection genders = super.idoFindAllIDsByColumnBySQL(getNameColumnName(),name);
   Iterator iter = genders.iterator();
   Integer gender = null;
    if( iter.hasNext() ) {
       gender = (Integer) iter.next();
    }
    else{
     throw new FinderException("Gender named : "+name+" not found");
    }

    return gender;
  }

*/


  public void insertStartData() throws SQLException {
/*
    try{
      Gender male = ((GenderHome)IDOLookup.getHome(Gender.class)).create();
      male.setName(NAME_MALE);
      male.store();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
*/
  }

  public Collection ejbFindAllImportHandlers()throws FinderException{
    return super.idoFindAllIDsBySQL();
  }

}
