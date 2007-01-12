package com.idega.block.timesheet.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.projectmanager.data.Project;
import com.idega.block.projectmanager.data.ProjectBMPBean;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TimesheetProjectBMPBean extends ProjectBMPBean implements TimesheetProject,Project {

  public TimesheetProjectBMPBean() {
    super();
  }

  public TimesheetProjectBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes(){
    super.initializeAttributes();
    this.addManyToManyRelationShip(User.class);
  }

  public void insertStartData() {
  }
  
  public Collection getUsers() {
  	try {
  		return super.idoGetRelatedEntities(User.class);
  	}
  	catch (Exception e) {
  		e.printStackTrace();
  		throw new RuntimeException("Error in getUsers() : " + e.getMessage());
  	}
  }
  public Collection ejbFindUserRelated(User user) throws FinderException{
  		try {
			return super.idoGetReverseRelatedEntities(user);
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
  }
  
  public Collection ejbFindEntryRelatedByUserWithinPeriod(Integer userID,java.sql.Date fromDate,java.sql.Date toDate) throws FinderException{
  	IDOQuery query = super.idoQuery().appendSelect().appendDistinct().append(" P.* ");
  	String[] tables ={TimesheetEntryBMPBean.TIMESHEET_ENTRY,ProjectBMPBean.PROJECT};
  	String[] prm ={"E","P"};
  	query.appendFrom(tables,prm);
  	query.appendWhereEquals(TimesheetEntryBMPBean.USER_ID,userID.intValue());
  	query.appendAnd().append(" E.").append(TimesheetEntryBMPBean.PROJECT_ID).append("=").append(" P.").append(ProjectBMPBean.PROJECT_ID);
  	query.appendAnd().appendWithinDates(TimesheetEntryBMPBean.ENTRY_DATE,fromDate,toDate);
  	System.out.println(query.toString());
  	return super.idoFindPKsByQuery(query);
  
  }
  
  public void addUser(User user) throws IDOAddRelationshipException {
  	this.idoAddTo(user);
  }
  
  public void removeUser(User user) throws IDORemoveRelationshipException {
  	this.idoRemoveFrom(user);
  }
  

	/* (non-Javadoc)
	 * @see com.idega.block.projectmanager.data.ProjectBMPBean#ejbFindAllOrderByNumber()
	 */
	public Collection ejbFindAllOrderByNumber() throws FinderException {
		// TODO Auto-generated method stub
		return super.ejbFindAllOrderByNumber();
	}

}
