
package com.idega.block.finance.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.BlockMenu;
import com.idega.presentation.IWContext;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class FinanceIndex extends Finance {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private int iCategoryId = -1;
  private List FinanceObjects = null;

  public FinanceIndex() {

  }
  public FinanceIndex(int iCategoryId){
    this.iCategoryId =  iCategoryId;
  }
  public void setCategoryId(int iCategoryId){
     this.iCategoryId =  iCategoryId;
  }
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    if(iCategoryId <= 0){
      iCategoryId = Finance.parseCategoryId(iwc);
    }

    BlockMenu menu = new BlockMenu();
    menu.setClassParameterName("fin_clss");
    addStandardObjects();
    menu.addAll(FinanceObjects);
    menu.addParameterToMaintain(Finance.getCategoryParameter(iCategoryId));
    add(menu);
  }

  public void addStandardObjects(){
    if(FinanceObjects == null)
      FinanceObjects = new Vector();
    FinanceObjects.add(0,new Accounts());
    FinanceObjects.add(0,new EntryGroups());
    FinanceObjects.add(0,new TariffAssessments());
    FinanceObjects.add(0,new TariffEditor());
    FinanceObjects.add(0,new TariffIndexEditor());
    FinanceObjects.add(0,new TariffKeyEditor());
    FinanceObjects.add(0,new AccountKeyEditor());
    FinanceObjects.add(0,new PaymentTypeEditor());
  }

  public void addFinanceObject(Block obj){
    if(FinanceObjects == null)
      FinanceObjects = new Vector();
    FinanceObjects.add(obj);
  }

  public void addFinanceObjectAll(java.util.Collection coll){
    if(FinanceObjects == null)
      FinanceObjects = new Vector();
    FinanceObjects.addAll(coll);
  }

  public synchronized Object clone() {
    FinanceIndex obj = null;
    try {
      obj = (FinanceIndex)super.clone();
      obj.FinanceObjects  = FinanceObjects;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

}
