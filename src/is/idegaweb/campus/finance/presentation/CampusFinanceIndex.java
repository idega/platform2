/*
 * $Id: CampusFinanceIndex.java,v 1.3 2001/11/08 15:40:40 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.finance.presentation;


import com.idega.presentation.text.*;
import com.idega.presentation.ui.IFrame;
import com.idega.presentation.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
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
public class CampusFinanceIndex extends Block {

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  public final static String FRAME_NAME = "rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusFinanceIndex() {
  }

  public void main(IWContext iwc){
    iwb = getBundle(iwc);
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
