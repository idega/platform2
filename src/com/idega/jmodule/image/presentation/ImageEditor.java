package com.idega.jmodule.image.presentation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.image.presentation.*;

public class ImageEditor extends JModuleObject{
private boolean refresh = false;
private boolean showAll = true;

  public void main(ModuleInfo modinfo)throws Exception{
    String sRefresh = modinfo.getRequest().getParameter("refresh");
    ImageBrowser browser = new ImageBrowser();
    browser.setShowAll(showAll);

    if( (sRefresh!=null) || refresh ) browser.refresh();
    add(browser);
  }

  public void refresh(){
    this.refresh=true;
  }

  public void setShowAll(boolean showAll){
    this.showAll = showAll;
  }
}
