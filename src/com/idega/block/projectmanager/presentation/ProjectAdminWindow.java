package com.idega.block.projectmanager.presentation;

import com.idega.block.projectmanager.presentation.ProjectAdmin;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.jmodule.object.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProjectAdminWindow extends IWAdminWindow {

  public ProjectAdminWindow() {
      super();
      setResizable(true);
      setWidth(430);
      setHeight(460);
  }

  public void main(ModuleInfo modinfo) throws Exception{
    super.main(modinfo);
    ProjectAdmin pa = new ProjectAdmin();
    add(pa);
  }
}