package com.idega.block.staff.presentation;

import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.TabbedPropertyPanel;
import com.idega.jmodule.object.ModuleObject;
import com.idega.core.user.presentation.UserPropertyWindow;
import com.idega.core.user.presentation.GeneralUserInfoTab;
import com.idega.core.user.presentation.UserGroupList;
import com.idega.block.staff.presentation.StaffInfoTab;
import com.idega.core.user.presentation.UserPhoneTab;

/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class StaffPropertyWindow extends UserPropertyWindow{

  public static final String PARAMETERSTRING_USER_ID = "ic_user_id";

  public StaffPropertyWindow(){
    super();
    this.setBackgroundColor("#d4d0c8");
  }

  public String getSessionAddressString(){
    return "st_staff_property_window";
  }

  public void initializePanel( ModuleInfo modinfo, TabbedPropertyPanel panel){
    panel.addTab(new GeneralUserInfoTab(), 0, modinfo);
    panel.addTab(new StaffInfoTab(), 1, modinfo);
    panel.addTab(new StaffImageTab(), 2, modinfo);
    panel.addTab(new UserPhoneTab(), 3, modinfo);
    panel.addTab(new UserGroupList(), 4, modinfo);
  }

}