package com.idega.jmodule.image.presentation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.image.presentation.*;

public class ImageBrowser extends JModuleObject{
  public void main(ModuleInfo modinfo)throws Exception{
    ImageBrowser browser = new ImageBrowser();
    browser.setShowAll(true);
    add(browser);
  }
}
