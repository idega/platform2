package com.idega.block.calendar.data;


public class CalendarEntryHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarEntry.class;
 }

 public CalendarEntry create() throws javax.ejb.CreateException{
  return (CalendarEntry) super.idoCreate();
 }

 public CalendarEntry createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CalendarEntry findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CalendarEntry) super.idoFindByPrimaryKey(id);
 }

 public CalendarEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarEntry) super.idoFindByPrimaryKey(pk);
 }

 public CalendarEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}