//idega 2001 - Laddi



package com.idega.block.cal.data;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.text.data.LocalizedText;
import com.idega.data.IDOQuery;



public class CalendarEntryTypeBMPBean extends com.idega.data.GenericEntity implements com.idega.block.cal.data.CalendarEntryType {

	public CalendarEntryTypeBMPBean(){
		super();
	}

	public CalendarEntryTypeBMPBean(int id)throws SQLException{
		super(id);
	}
	
//  public void insertStartData()throws Exception{
//
////    String[] entries = { "Afmæli","Fundir","Viðburður","Frídagur","Skiladagur","Tilkynning","Birthday","Meeting","Event","Holiday","Deadline","Announcement" };
//  	
//  	String[] entries = {"general","practice"};
//
//    for ( int a = 0; a < 2; a++ ) {
//      EntityBulkUpdater bulk = new EntityBulkUpdater();
//      CalendarEntryType type = ((com.idega.block.cal.data.CalendarEntryTypeHome)com.idega.data.IDOLookup.getHome(CalendarEntryType.class)).create();
//
//      LocalizedText text = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
//      text.setLocaleId(TextFinder.getLocaleId(new Locale("is","IS")));
//      text.setHeadline(entries[a]);
//
//      LocalizedText text2 = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
//      text2.setLocaleId(TextFinder.getLocaleId(Locale.ENGLISH));
//      text2.setHeadline(entries[a+6]);
//      
//      bulk.add(type,EntityBulkUpdater.insert);
//      bulk.add(text,EntityBulkUpdater.insert);
//      bulk.add(text2,EntityBulkUpdater.insert);
//      bulk.execute();
//
//      text.idoAddTo(type);
//      text2.idoAddTo(type);
//    }
//
//  }

	public void initializeAttributes(){
//		addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "CalTypeName", true, true, String.class);
    addAttribute(getColumnNameCalendarTypeID(), "CalTypeID",true,true,Integer.class);
    addManyToManyRelationShip(LocalizedText.class,"CAL_TYPE_LOCALIZED_TEXT");
	}

	public static String getEntityTableName() { return "CAL_TYPE"; }
	public static String getColumnNameCalendarTypeID() { return "CAL_TYPE_ID"; }
	public static String getColumnNameName() { return "CAL_TYPE_NAME"; }

  public String getIDColumnName(){
		return getColumnNameCalendarTypeID();
	}

	public String getEntityName(){
		return getEntityTableName();
	}

  public String getName() {
  	return getStringColumnValue(getColumnNameName());
  }
  
  public void setName(String name) {
  	setColumn(getColumnNameName(),name);
  }
  
  //ejbFind... 
  public Collection ejbFindTypes() throws FinderException {
  	List result = new ArrayList(super.idoFindAllIDsOrderedBySQL("CAL_TYPE_NAME"));
  	return result;
  }
  
  public Collection ejbFindTypeByName(String name) throws FinderException {
  	IDOQuery query = idoQueryGetSelect();
  	query.appendWhereEqualsQuoted("CAL_TYPE_NAME", name);
  	return super.idoFindPKsByQuery(query);
  }
  
  public Collection ejbFindTypeById(int id) throws FinderException{
  	Collection result = new ArrayList(1);
  	result.add(idoFindOnePKByColumnBySQL(getIDColumnName(), Integer.toString(id)));
  	return result;
  }

  //DELETE
	public void delete() throws SQLException{
    removeFrom(com.idega.block.text.data.LocalizedTextBMPBean.getStaticInstance(LocalizedText.class));
		super.delete();
	}

}
