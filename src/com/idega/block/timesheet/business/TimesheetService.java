package com.idega.block.timesheet.business;


public interface TimesheetService extends com.idega.business.IBOService
{
 public com.idega.block.timesheet.data.ResourceHome getResourceHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.timesheet.data.TimesheetEntryHome getTimesheetEntryHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.timesheet.data.TimesheetProjectHome getTimesheetProjectHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
}
