/*
 * $Id: CampusFinanceIndex.java,v 1.1 2001/10/04 13:38:22 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.finance.presentation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.IFrame;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idegaweb.campus.tariffs.CampusFinanceMenu;
import com.idega.block.text.presentation.TextReader;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusFinanceIndex extends JModuleObject {

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  public final static String FRAME_NAME = "rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusFinanceIndex() {
  }

  public void main(ModuleInfo modinfo){
    iwb = getBundle(modinfo);
    Table myTable = new Table(1,2);
    myTable.add(iwb.getImage("money.gif"),1,1);
    TextReader TR  = new TextReader("campus_finance");
    myTable.add(TR,1,2);
    add(myTable);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER ;
  }

}
