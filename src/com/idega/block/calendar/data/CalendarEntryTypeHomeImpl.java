package com.idega.block.calendar.data;


public class CalendarEntryTypeHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryTypeHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarEntryType.class;
 }

 public CalendarEntryType create() throws javax.ejb.CreateException{
  return (CalendarEntryType) super.idoCreate();
 }

 public CalendarEntryType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CalendarEntryType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CalendarEntryType) super.idoFindByPrimaryKey(id);
 }

 public CalendarEntryType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarEntryType) super.idoFindByPrimaryKey(pk);
 }

 public CalendarEntryType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}