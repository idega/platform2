package com.idega.block.employment.data;

import com.idega.core.data.GenericGroup;
import java.sql.SQLException;

/**
 * Title:        IW Travel Booking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class EmployeeGroup extends GenericGroup {

  public EmployeeGroup() {
    super();
  }

  public EmployeeGroup(int id) throws SQLException {
    super(id);
  }


  public String getGroupTypeValue() {
    return "ep_employee";
  }


  public static String getClassName(){
    return EmployeeGroup.class.getName();
  }


} // EmployeeGroup