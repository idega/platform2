package com.idega.block.timesheet.business;


public interface TimesheetSessionHome extends com.idega.business.IBOHome
{
 public TimesheetSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}