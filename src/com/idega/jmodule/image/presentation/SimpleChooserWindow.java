package com.idega.jmodule.image.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.jmodule.image.business.SimpleImage;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.image.data.ImageEntity;
import com.idega.util.idegaTimestamp;
import com.idega.idegaweb.IWBundle;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

 public class SimpleChooserWindow extends IWAdminWindow {

    private String IW_BUNDLE_IDENTIFIER="com.idega.block.image";

    public SimpleChooserWindow(){
      super();
      setResizable(true);
      setWidth(726);
      setHeight(460);
    }

    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }

    public void  main(IWContext iwc) throws Exception{
      IWBundle iwb = getBundle(iwc);
      SimpleChooser SC = new SimpleChooser();
      SC.setToIncludeLinks(false);
      add(SC);
      addHeaderObject(SC.getLinkTable(iwb));
      setTitle("Image Chooser jmodule image");
      addTitle("Image Chooser" );

      //setParentToReload();
    }
}