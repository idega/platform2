package com.idega.block.trade.stockroom.data;

import com.idega.core.data.GenericGroup;
import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResellerStaffGroup extends GenericGroup {
  public static final String GROUP_TYPE_VALUE = "sr_reseller_staff";


  public ResellerStaffGroup() {
    super();
  }

  public ResellerStaffGroup(int id) throws SQLException {
    super(id);
  }


  public String getGroupTypeValue() {
    return GROUP_TYPE_VALUE;
  }


  public static String getClassName(){
    return ResellerStaffGroup.class.getName();
  }

  protected boolean identicalGroupExistsInDatabase() throws Exception {
    return false;
  }

  }