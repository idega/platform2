/*
 * $Id: ReferenceNumberInfo.java,v 1.4 2001/08/17 09:31:14 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.presentation;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.business.ReferenceNumberHandler;
import is.idegaweb.campus.application.CampusApplicationFinder;
import is.idegaweb.campus.application.CampusApplicationHolder;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.textObject.Text;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumberInfo extends ModuleObjectContainer {
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";

  public ReferenceNumberInfo() {
  }

  protected void control(ModuleInfo modinfo) {
    String ref = ReferenceNumberHandler.getReferenceNumber(modinfo);

    int aid = 0;
    try {
      aid = Integer.parseInt(ref);
    }
    catch(java.lang.NumberFormatException e) {
      aid = 0;
    }

    CampusApplicationHolder holder = CampusApplicationFinder.getApplicationInfo(aid);

    if (holder == null) {
      add(new Text("Það er engin umsókn skráð á þetta tilvísunarnúmer"));
      add(Text.getBreak());
    }
    else {
      Applicant applicant = holder.getApplicant();
      Application app = holder.getApplication();

      add(new Text("Halló " + applicant.getFirstName()));
      add(Text.getBreak());
      add(new Text("Umsókn þín er : " + app.getStatus()));
    }
  }

  private void approved(ModuleInfo modinfo) {

  }

  private void rejected(ModuleInfo modinfo) {

  }

  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }

  public void main(ModuleInfo modinfo) {
//    iwrb = getResourceBundle(modinfo);
//    iwb = getBundle(modinfo);
    control(modinfo);
  }
}