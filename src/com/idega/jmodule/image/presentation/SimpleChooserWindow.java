package com.idega.jmodule.image.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.jmodule.image.business.SimpleImage;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.image.data.ImageEntity;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

 public class SimpleChooserWindow extends IWAdminWindow {

    public String sessImageParameter = "image_id";

    public SimpleChooserWindow(){
      super("Image Chooser");
    }

    public void  main(ModuleInfo modinfo){
      super.initAll(modinfo);
      SimpleChooser SC = new SimpleChooser();
      SC.setToIncludeLinks(false);
      add(SC);
      addToLink(SC.getLinkTable());
      setParentToReload();
    }
}