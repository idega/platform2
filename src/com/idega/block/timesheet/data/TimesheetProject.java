package com.idega.block.timesheet.data;

import com.idega.jmodule.projectmanager.data.Project;
import com.idega.core.user.data.User;
import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TimesheetProject extends Project {

  public TimesheetProject() {
    super();
  }

  public TimesheetProject(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes(){
    super.initializeAttributes();
    this.addManyToManyRelationShip(User.class);
  }

  public void insertStartData() {
  }

}