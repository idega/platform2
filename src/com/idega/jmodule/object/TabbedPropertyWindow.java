package com.idega.jmodule.object;

import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.TabbedPropertyPanel;


/**
 * Title:        IW
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public abstract class TabbedPropertyWindow extends Window{

  public TabbedPropertyWindow(){
    super();
  }

  public TabbedPropertyWindow(int width, int height){
    super(width,height);
  }

  public void main(ModuleInfo modinfo) throws Exception {
    this.empty();
    TabbedPropertyPanel panel = TabbedPropertyPanel.getInstance(getSessionAddressString(), modinfo );
    if(panel.justConstructed()){
      initializePanel(modinfo, panel);
    }

    if(panel.clickedCancel() || panel.clickedOk()){
      this.setParentToReload();
      this.close();
      panel.dispose(modinfo);
    }else{
      this.add(panel);
    }

  }

  public abstract String getSessionAddressString();

  public abstract void initializePanel( ModuleInfo modinfo, TabbedPropertyPanel panel);



}