/*
 * $Id: CampusBuildingMenu.java,v 1.1 2001/08/30 06:48:48 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.building;

import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.FrameList;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.block.building.presentation.BuildingEditor;
import com.idega.jmodule.object.Page;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusBuildingMenu extends FrameList{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6 = 6,ACT7 = 7;
  private final String strAction = "fin_action";
  private String DarkColor = "#27334B";
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public CampusBuildingMenu() {
  }

  public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    setLinkStyle("font-family: Verdana, Arial, sans-serif; font-weight: bold; font-size: 7pt; text-decoration: none;");
    makeLinkTable();
    setZebraColors("#FFFFFF","#ECECEC");
  }

  public void makeLinkTable(){

      Window buildWindow = new Window("b_editor",BuildingEditor.class,Page.class);
      buildWindow.setWidth(550);
      buildWindow.setHeight(500);
      buildWindow.setResizable(true);
      Link Build = new Link(new Text(iwrb.getLocalizedString("building_editor","Building Editor")),buildWindow);

      addToList(Build);

      addToList(ApartmentFreezer.class,iwrb.getLocalizedString("apartment_freeze","Freeze apartments"),CampusBuilding.FRAME_NAME);
      addToList(ApartmentSerie.class,iwrb.getLocalizedString("apartment_serie","Apartment serie"),CampusBuilding.FRAME_NAME);



  }
}