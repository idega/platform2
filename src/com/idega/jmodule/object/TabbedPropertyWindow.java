package com.idega.jmodule.object;

import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.jmodule.object.FrameSet;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.TabbedPropertyPanel;
import com.idega.jmodule.object.textObject.Text;


/**
 * Title:        IW
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public abstract class TabbedPropertyWindow extends Window {

  protected TabbedPropertyPanel panel = null;

  public TabbedPropertyWindow(){
    this(410,482);
  }

  public TabbedPropertyWindow(int width, int height){
    super(width,height);
    super.setScrollbar(false);
    super.setAllMargins(0);
    super.setTopMargin(3);
  }

  public void _main(ModuleInfo modinfo) throws Exception {
    this.empty();
    panel = TabbedPropertyPanel.getInstance(getSessionAddressString(), modinfo );
    if(panel.justConstructed()){
      panel.setAlignment("center");
      panel.setVerticalAlignment("middle");
      initializePanel(modinfo, panel);
    }

    if(panel.clickedCancel() || panel.clickedOk()){
      if(panel.clickedOk()){
        this.setParentToReload();
      }
      this.close();
      panel.dispose(modinfo);
    }else{
      this.add(panel);
    }
    super._main(modinfo);

  }

  public ModuleObject[] getAddedTabs(){
    if(panel != null){
      return panel.getAddedTabs();
    } else {
      throw new RuntimeException("TabbedPropertyPanel not set. TabbedPropertyPanel is set in main(ModuleInfo modinfo)");
    }
  }

  public abstract String getSessionAddressString();

  public abstract void initializePanel( ModuleInfo modinfo, TabbedPropertyPanel panel);



}