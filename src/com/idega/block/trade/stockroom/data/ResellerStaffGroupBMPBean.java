package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.user.data.GroupBMPBean;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResellerStaffGroupBMPBean extends GroupBMPBean implements com.idega.block.trade.stockroom.data.ResellerStaffGroup {
  public static final String GROUP_TYPE_VALUE = "sr_reseller_staff";


  public ResellerStaffGroupBMPBean() {
    super();
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
  
  public Collection ejbFindGroupsByName(String name) throws FinderException {
  	return super.ejbFindGroupsByName(name);
  }
  
  public Collection ejbFindGroupsByNameAndDescription(String name, String description) throws FinderException {
  	return super.ejbFindGroupsByNameAndDescription(name, description);
  }
  }
