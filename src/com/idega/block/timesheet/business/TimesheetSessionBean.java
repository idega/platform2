/*
 * Created on Jan 5, 2004
 *
 */
package com.idega.block.timesheet.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;

/**
 * TimesheetSessionBean
 * @author aron 
 * @version 1.0
 */
public class TimesheetSessionBean extends IBOSessionBean implements TimesheetSession{
	
	Collection userProjects =null;
	Collection resources =null;
	
	public Collection getUserProjects(User user)throws RemoteException{
		if(this.userProjects==null){
			try{
			this.userProjects = getTimesheetService().getTimesheetProjectHome().findUserRelated(user);
			}
			catch(FinderException ex){
				throw new RemoteException(ex.getMessage());
			}
		}
		return this.userProjects;
	}
	
	public Collection getResources()throws RemoteException{
		if(this.resources==null){
			try{
				this.resources = getTimesheetService().getResourceHome().findAllOpen();
			}
			catch(FinderException ex){
				throw new RemoteException(ex.getMessage());
			}
		}
		return this.resources;
	}
	
	public TimesheetService getTimesheetService()throws RemoteException{
		return (TimesheetService)getServiceInstance(TimesheetService.class);
	}
}
