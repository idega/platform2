package com.idega.block.timesheet.data;

import java.sql.SQLException;

import com.idega.core.user.data.User;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TimesheetProjectBMPBean extends com.idega.block.projectmanager.data.ProjectBMPBean implements com.idega.block.timesheet.data.TimesheetProject {

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

}
