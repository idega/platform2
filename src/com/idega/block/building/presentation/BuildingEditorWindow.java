package com.idega.block.building.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;


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
		setResizable(true);
  }

  public void main(IWContext iwc) throws Exception{
    BuildingEditor BE = new BuildingEditor();
    add(BE);
    BE.setToIncludeLinks(false);
    setTitle("Building Editor");
    addTitle("Building Editor");
    addHeaderObject(BE.getLinkTable(iwc));


  }
}