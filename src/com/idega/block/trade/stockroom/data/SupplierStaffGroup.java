package com.idega.block.trade.stockroom.data;

import com.idega.core.data.GenericGroup;
import java.sql.SQLException;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class SupplierStaffGroup extends GenericGroup {

  public SupplierStaffGroup() {
    super();
  }

  public SupplierStaffGroup(int id) throws SQLException {
    super(id);
  }


  public String getGroupTypeValue() {
    return "sr_supplier_staff";
  }


  public static String getClassName(){
    return SupplierStaffGroup.class.getName();
  }
} // Class SupplierStaffGroup