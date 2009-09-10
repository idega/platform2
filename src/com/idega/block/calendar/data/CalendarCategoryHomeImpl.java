package com.idega.block.calendar.data;


public class CalendarCategoryHomeImpl extends com.idega.data.IDOFactory implements CalendarCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarCategory.class;
 }

 public CalendarCategory create() throws javax.ejb.CreateException{
  return (CalendarCategory) super.idoCreate();
 }

 public CalendarCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CalendarCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CalendarCategory) super.idoFindByPrimaryKey(id);
 }

 public CalendarCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarCategory) super.idoFindByPrimaryKey(pk);
 }

 public CalendarCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}