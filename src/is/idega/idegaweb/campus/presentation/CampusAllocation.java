/*
 * $Id: CampusAllocation.java,v 1.4 2002/01/10 01:06:18 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;


import com.idega.presentation.BlockMenu;
import is.idega.idegaweb.campus.presentation.EmailSetter;
import is.idega.idegaweb.campus.block.building.presentation.AprtTypePeriodMaker;
import is.idega.idegaweb.campus.block.application.presentation.*;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.SysPropsSetter;
import is.idega.idegaweb.campus.block.allocation.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.finance.presentation.*;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationFinder;

import java.util.List;
import java.util.Vector;
import java.util.Collection;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;




/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAllocation extends Block{

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus.block.allocation";
  public final static String FRAME_NAME = "cal_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  public static String prmClass = "alloc_clss";
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
    objects.add(0,new SysPropsSetter());
    objects.add(0,new AprtTypePeriodMaker());
    objects.add(0,new SubjectMaker());
    objects.add(0,new EmailSetter());
    objects.add(0,new ContractTextSetter());
    objects.add(0,new CampusAllocator());
    objects.add(0,new RoughOrderForm());
    objects.add(0,new CampusContracts());
    objects.add(0,new CampusApprover());
  }


   public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

   public void addFinanceObject(Block obj){
    if(objects == null)
      objects = new Vector();
    objects.add(obj);
  }

  public void addFinanceObjectAll(java.util.Collection coll){
    if(objects == null)
      objects = new Vector();
    objects.addAll(coll);
  }

  public synchronized Object clone() {
    CampusAllocation obj = null;
    try {
      obj = (CampusAllocation)super.clone();
      obj.objects  = objects;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

}
