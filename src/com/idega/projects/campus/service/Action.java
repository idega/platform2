package com.idega.projects.campus.service;

import com.idega.jmodule.object.ModuleObject;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public interface Action {

  public final static String sManagerAction = "manager_action";
  public final static String sAdminAction = "admin_action";
  public final static String sStaffAction = "staff_action";
  public final static String sTenantAction = "tenant_action";
  public final static String sApplicantAction = "applicant_action";
  ModuleObject getTabs();


}