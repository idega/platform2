package com.idega.projects.campus.service;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleObject;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class CampusObject extends JModuleObject {

  public String iObjectName = "Campus";
  public abstract ModuleObject getTabs();

  public String getObjectName(){
      return iObjectName;
  }

}