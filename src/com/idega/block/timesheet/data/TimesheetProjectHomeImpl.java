package com.idega.block.timesheet.data;


public class TimesheetProjectHomeImpl extends com.idega.data.IDOFactory implements TimesheetProjectHome
{
 protected Class getEntityInterfaceClass(){
  return TimesheetProject.class;
 }

 public TimesheetProject create() throws javax.ejb.CreateException{
  return (TimesheetProject) super.idoCreate();
 }

 public TimesheetProject createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TimesheetProject findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TimesheetProject) super.idoFindByPrimaryKey(id);
 }

 public TimesheetProject findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TimesheetProject) super.idoFindByPrimaryKey(pk);
 }

 public TimesheetProject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}