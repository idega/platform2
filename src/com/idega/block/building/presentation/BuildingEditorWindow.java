package com.idega.block.building.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.jmodule.object.ModuleInfo;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class BuildingEditorWindow extends IWAdminWindow {

  public BuildingEditorWindow() {
    super();
  }

  public void main(ModuleInfo modinfo) throws Exception{
    BuildingEditor BE = new BuildingEditor();
    add(BE);
    BE.setToIncludeLinks(false);
    setTitle("Building Editor");
    addTitle("Building Editor");
    addHeaderObject(BE.getLinkTable(modinfo));
  }
}