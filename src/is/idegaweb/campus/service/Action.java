/*
 * $Id: Action.java,v 1.3 2001/11/08 15:40:40 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;


import com.idega.presentation.PresentationObject;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public interface Action {

  public final static String sManagerAction = "manager_action";
  public final static String sAdminAction = "admin_action";
  public final static String sStaffAction = "staff_action";
  public final static String sTenantAction = "tenant_action";
  public final static String sApplicantAction = "applicant_action";
  PresentationObject getTabs();


}
