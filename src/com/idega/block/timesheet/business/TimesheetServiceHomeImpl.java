package com.idega.block.timesheet.business;


public class TimesheetServiceHomeImpl extends com.idega.business.IBOHomeImpl implements TimesheetServiceHome
{
 protected Class getBeanInterfaceClass(){
  return TimesheetService.class;
 }


 public TimesheetService create() throws javax.ejb.CreateException{
  return (TimesheetService) super.createIBO();
 }



}