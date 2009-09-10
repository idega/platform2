package com.idega.block.timesheet.business;


public interface TimesheetSession extends com.idega.business.IBOSession
{
 public java.util.Collection getResources() throws java.rmi.RemoteException;
 public java.util.Collection getUserProjects(com.idega.user.data.User p0) throws java.rmi.RemoteException;
}
