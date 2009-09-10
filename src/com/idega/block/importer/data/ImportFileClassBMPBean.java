package com.idega.block.importer.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;


/**
 * Title:        com.idega.block.importer.data.ImportFileClassBMPBean
 * Description: A table of available Import handlers
 * Copyright:    Idega Software (c) 2002
 * Company:      Idega Software http://www.idega.com
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class ImportFileClassBMPBean extends com.idega.data.GenericEntity implements ImportFileClass {


  public ImportFileClassBMPBean() {
    super();
  }

  public ImportFileClassBMPBean(int id) throws SQLException {
    super(id);
  }


  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    this.addAttribute(getClassColumnName(),"Class name",true,true,"java.lang.String",500);
    this.addAttribute(getDescriptionColumnName(),"Description",true,true,"java.lang.String",1000);
  }
  public String getEntityName() {
    return "im_file_class";
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

  public String getName(){
    return this.getStringColumnValue(getNameColumnName());
  }

  public void setDescription(String description){
    this.setColumn(getDescriptionColumnName(),description);
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

*/


  public void insertStartData() throws SQLException {
    try{
		ImportFileClass generic = ((ImportFileClassHome)IDOLookup.getHome(ImportFileClass.class)).create();
		generic.setName("Generic import file");
		generic.setDescription("A generic file reader. Reads both column based and row based record files. Adjustible through some properties. The default is reading a column based file where each record is separated with a new line character (\n) and each value is separated by a semi colon (;). ");
		generic.setClassName(GenericImportFile.class.getName());
      	generic.store();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    
    try{
		ImportFileClass column = ((ImportFileClassHome)IDOLookup.getHome(ImportFileClass.class)).create();
		column.setName("Column separated file");
		column.setDescription("A column separated file reade. By default each record is separated with a new line character (\n) and each value is separated by a semi colon (;) but it can be adjusted by properties.");
		column.setClassName(GenericImportFile.class.getName());
      	column.store();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    
    //temporary remove
  /*  try{
		ImportFileClass nacka = ((ImportFileClassHome)IDOLookup.getHome(ImportFileClass.class)).create();
		nacka.setName("Nacka citizen file");
		nacka.setDescription("A rows based file reader. Each record starts with a ");
		nacka.setClassName("se.idega.idegaweb.commune.block.importer.data.NackaImportFile");
      	nacka.store();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    
    */
    
  }

  public Collection ejbFindAllImportFileClasses()throws FinderException{
    return super.idoFindAllIDsBySQL();
  }

  public Integer ejbFindByClassName(String className) throws FinderException {
	IDOQuery query = idoQuery();
	query.appendSelectAllFrom(this).appendWhereEqualsQuoted(getClassColumnName(), className);
	return (Integer)idoFindOnePKByQuery(query);
  }

}
