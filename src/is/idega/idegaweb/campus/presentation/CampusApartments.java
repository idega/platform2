/*
 * $Id: CampusApartments.java,v 1.2 2002/02/15 12:29:24 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;


import com.idega.presentation.BlockMenu;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.IFrame;
import com.idega.presentation.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.block.building.presentation.BuildingEditor;
import com.idega.block.building.presentation.BuildingEditorWindow;
import com.idega.block.building.presentation.BuildingStatistics;
import is.idega.idegaweb.campus.block.phone.presentation.CampusPhones;
import is.idega.idegaweb.campus.block.building.presentation.*;

import java.util.List;
import java.util.Vector;
import java.util.Collection;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class CampusApartments extends Block{

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  public final static String FRAME_NAME = "cap_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  public static String prmClass = "apartment_clss";
  private List objects = null;

   public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    SysPropsSetter.isSysPropsInMemoryElseLoad(iwc);

    BlockMenu menu = new BlockMenu();
    menu.setClassParameterName(prmClass);
    addStandardObjects();
    menu.addAll(objects);
    add(menu);
  }

  public void addStandardObjects(){
    if(objects == null)
      objects = new Vector();
    objects.add(0,new ApartmentFreezer());
    objects.add(0,new ApartmentSerie());
    objects.add(0,new AprtTypePeriodMaker());
    objects.add(0,new BuildingEditor());
    objects.add(0,new CampusPhones());
  }


   public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

   public void addBlockObject(Block obj){
    if(objects == null)
      objects = new Vector();
    objects.add(obj);
  }

  public void addBlockObjectAll(java.util.Collection coll){
    if(objects == null)
      objects = new Vector();
    objects.addAll(coll);
  }

  public synchronized Object clone() {
    CampusApartments obj = null;
    try {
      obj = (CampusApartments)super.clone();
      obj.objects  = objects;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

}
