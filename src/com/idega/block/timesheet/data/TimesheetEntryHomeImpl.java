package com.idega.block.timesheet.data;


public class TimesheetEntryHomeImpl extends com.idega.data.IDOFactory implements TimesheetEntryHome
{
 protected Class getEntityInterfaceClass(){
  return TimesheetEntry.class;
 }

 public TimesheetEntry create() throws javax.ejb.CreateException{
  return (TimesheetEntry) super.idoCreate();
 }

 public TimesheetEntry createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TimesheetEntry findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TimesheetEntry) super.idoFindByPrimaryKey(id);
 }

 public TimesheetEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TimesheetEntry) super.idoFindByPrimaryKey(pk);
 }

 public TimesheetEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}