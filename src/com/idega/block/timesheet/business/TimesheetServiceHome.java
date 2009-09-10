package com.idega.block.timesheet.business;


public interface TimesheetServiceHome extends com.idega.business.IBOHome
{
 public TimesheetService create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}