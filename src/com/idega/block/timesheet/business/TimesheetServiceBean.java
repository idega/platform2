/*
 * Created on Jan 5, 2004
 *
 */
package com.idega.block.timesheet.business;

import java.rmi.RemoteException;

import com.idega.block.timesheet.data.Resource;
import com.idega.block.timesheet.data.ResourceHome;
import com.idega.block.timesheet.data.TimesheetEntryHome;
import com.idega.block.timesheet.data.TimesheetProjectHome;
import com.idega.business.IBOServiceBean;

/**
 * TimsheetServiceBean
 * @author aron 
 * @version 1.0
 */
public class TimesheetServiceBean extends IBOServiceBean implements TimesheetService{
	
	public ResourceHome getResourceHome()throws RemoteException{
		return (ResourceHome)getIDOHome(Resource.class);
	}
	
	public TimesheetEntryHome getTimesheetEntryHome()throws RemoteException{
		return (TimesheetEntryHome)getIDOHome(Resource.class);
	}
	
	public TimesheetProjectHome getTimesheetProjectHome()throws RemoteException{
		return (TimesheetProjectHome)getIDOHome(Resource.class);
	}
}
