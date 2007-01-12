
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
    this.iwrb = getResourceBundle(iwc);
    this.iwb = getBundle(iwc);
    if(this.iCategoryId <= 0){
      this.iCategoryId = Finance.parseCategoryId(iwc);
    }

    BlockMenu menu = new BlockMenu();
    menu.setClassParameterName("fin_clss");
    addStandardObjects();
    menu.addAll(this.FinanceObjects);
    menu.addParameterToMaintain(Finance.getCategoryParameter(this.iCategoryId));
    add(menu);
  }

  public void addStandardObjects(){
    if(this.FinanceObjects == null) {
		this.FinanceObjects = new Vector();
	}
    this.FinanceObjects.add(0,new Accounts());
    this.FinanceObjects.add(0,new EntryGroups());
    this.FinanceObjects.add(0,new TariffAssessments());
    this.FinanceObjects.add(0,new TariffEditor());
    this.FinanceObjects.add(0,new TariffIndexEditor());
    this.FinanceObjects.add(0,new TariffKeyEditor());
    this.FinanceObjects.add(0,new AccountKeyEditor());
    this.FinanceObjects.add(0,new PaymentTypeEditor());
  }

  public void addFinanceObject(Block obj){
    if(this.FinanceObjects == null) {
		this.FinanceObjects = new Vector();
	}
    this.FinanceObjects.add(obj);
  }

  public void addFinanceObjectAll(java.util.Collection coll){
    if(this.FinanceObjects == null) {
		this.FinanceObjects = new Vector();
	}
    this.FinanceObjects.addAll(coll);
  }

  public synchronized Object clone() {
    FinanceIndex obj = null;
    try {
      obj = (FinanceIndex)super.clone();
      obj.FinanceObjects  = this.FinanceObjects;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

}
