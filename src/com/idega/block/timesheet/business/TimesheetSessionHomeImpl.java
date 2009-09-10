package com.idega.block.timesheet.business;


public class TimesheetSessionHomeImpl extends com.idega.business.IBOHomeImpl implements TimesheetSessionHome
{
 protected Class getBeanInterfaceClass(){
  return TimesheetSession.class;
 }


 public TimesheetSession create() throws javax.ejb.CreateException{
  return (TimesheetSession) super.createIBO();
 }



}