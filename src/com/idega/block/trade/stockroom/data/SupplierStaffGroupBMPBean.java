package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.user.data.GroupBMPBean;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class SupplierStaffGroupBMPBean extends GroupBMPBean implements com.idega.block.trade.stockroom.data.SupplierStaffGroup {
//public class SupplierStaffGroupBMPBean extends com.idega.user.data.GroupBMPBean implements com.idega.block.trade.stockroom.data.SupplierStaffGroup {
  public static final String GROUP_TYPE_VALUE = "sr_supplier_staff";

  /*
  public SupplierStaffGroupBMPBean() {
    super();
  }

  public SupplierStaffGroupBMPBean(int id) throws SQLException {
    super(id);
  }
  */

  public String getGroupTypeValue() {
    return GROUP_TYPE_VALUE;
  }


  public static String getClassName(){
    return SupplierStaffGroup.class.getName();
  }

  protected boolean identicalGroupExistsInDatabase() throws Exception {
    return false;
  }
  
  public Collection ejbFindGroupsByName(String name) throws FinderException {
  	return super.ejbFindGroupsByName(name);
  }
  
  public Collection ejbFindGroupsByNameAndDescription(String name, String description) throws FinderException {
  	return super.ejbFindGroupsByNameAndDescription(name, description);
  }

  } // Class SupplierStaffGroup
