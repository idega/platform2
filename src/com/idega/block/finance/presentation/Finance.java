package com.idega.block.finance.presentation;

import com.idega.block.IWBlock;
import com.idega.block.finance.business.*;
import com.idega.block.finance.data.*;
import com.idega.presentation.Block;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.presentation.ui.*;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.text.*;
import com.idega.util.text.Edit;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import java.util.List;
import java.text.DateFormat;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class Finance extends Block implements IWBlock{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
  public final static String CATEGORY_PROPERTY="finance_category";
  protected boolean isAdmin = false;
  private int iCategoryId = -1;
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private IWBundle core ;
  boolean newobjinst = false;
  boolean administrative = true;
  private List FinanceObjects = null;
  public final static String FRAME_NAME = "fin_frame";
  public static String prmFinanceClass = "fin_clss";

  private static final String prmCategoryId = "fin_cat";

  public Finance() {

  }

  public Finance(int iCategoryId){
   this.iCategoryId = iCategoryId;
  }

  protected void control(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    boolean info = false;

    Table T = new Table();
    T.setWidth("100%");
   // T.setHeight("100%");
    T.setCellpadding(0);
    T.setCellspacing(0);

    if(iCategoryId <= 0){
      String sCategoryId = iwc.getParameter(prmCategoryId );
      if(sCategoryId != null)
        iCategoryId = Integer.parseInt(sCategoryId);
      else if(getICObjectInstanceID() > 0){
        iCategoryId = FinanceFinder.getInstance().getInstance().getObjectInstanceCategoryId(getICObjectInstanceID(),true);
        if(iCategoryId <= 0 ){
          newobjinst = true;
        }
      }
    }
    if(isAdmin && administrative && getICObjectInstanceID() > 0){
      T.add(getAdminPart(iCategoryId,false,newobjinst,info,iwc),1,1);
    }
    /*
    String className = null;
    if(iwc.isParameterSet(prmFinanceClass)){
      className = iwc.getParameter(prmFinanceClass);
      iwc.setSessionAttribute(prmFinanceClass,className);
    }
    else if(iwc.getSessionAttribute(prmFinanceClass)!=null){
      className = (String) iwc.getSessionAttribute(prmFinanceClass);
    }
    if(className !=null){
      try{
       T.add(getLinkTable(1),1,1);

      Object obj =  Class.forName(className).newInstance();
      if(obj instanceof PresentationObject)
        T.add((PresentationObject)obj,1,2);

      }
      catch(Exception e){}
    }

    else{

     T.add(getBoxedLinks(),1,1);
    */

    FinanceIndex index = new FinanceIndex(iCategoryId);
    if(FinanceObjects !=null)
      index.addFinanceObjectAll(FinanceObjects);

    T.add(index,1,2);



    add(T);

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private PresentationObject getAdminPart(int iCategoryId,boolean enableDelete,boolean newObjInst,boolean info,IWContext iwc){
    Table T = new Table(3,1);
    T.setCellpadding(2);
    T.setCellspacing(2);

    IWBundle core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    if(iCategoryId > 0){
      /*
      Link ne = new Link(core.getImage("/shared/create.gif","create"));
      ne.setWindowToOpen(FinanceEditorWindow.class);
      ne.addParameter(FinanceEditorWindow.prmCategory,iCategoryId);
      T.add(ne,1,1);
      T.add(T.getTransparentCell(iwc),1,1);
      */

      Link change = new Link(core.getImage("/shared/edit.gif","edit"));
      change.setWindowToOpen(FinanceEditorWindow.class);
      change.addParameter(FinanceEditorWindow.prmCategory,iCategoryId);
      change.addParameter(FinanceEditorWindow.prmObjInstId,getICObjectInstanceID());
      T.add(change,1,1);

      if ( enableDelete ) {
        T.add(T.getTransparentCell(iwc),1,1);
        Link delete = new Link(core.getImage("/shared/delete.gif"));
        delete.setWindowToOpen(FinanceEditorWindow.class);
        delete.addParameter(FinanceEditorWindow.prmDelete,iCategoryId);
        T.add(delete,3,1);
      }
    }
    if(newObjInst){
      Link newLink = new Link(core.getImage("/shared/create.gif"));
      newLink.setWindowToOpen(FinanceEditorWindow.class);
      if(newObjInst)
        newLink.addParameter(FinanceEditorWindow.prmObjInstId,getICObjectInstanceID());

      T.add(newLink,2,1);
    }
    T.setWidth("100%");
    return T;
  }

  public PresentationObject getBoxedLinks(){
    Table frame = new Table(3,3);
    frame.setWidth("100%");
    frame.setHeight("100%");
    Table box = new Table();
      int row = 1;
      box.add(getLink(PaymentTypeEditor.class,iwrb.getLocalizedString("payment_types","Payment types")),1,row++);
      box.add(getLink(TariffKeyEditor.class,iwrb.getLocalizedString("tariff_keys","Tariff keys")),1,row++);
      box.add(getLink(AccountKeyEditor.class,iwrb.getLocalizedString("account_keys","Account keys")),1,row++);
      box.add(getLink(TariffIndexEditor.class,iwrb.getLocalizedString("indexes","Indexes")),1,row++);
      box.add(getLink(TariffEditor.class,iwrb.getLocalizedString("tariff","Tariffs")),1,row++);
      box.add(getLink(TariffAssessments.class,iwrb.getLocalizedString("assessment","Assessment")),1,row++);
      box.add(getLink(EntryGroups.class,iwrb.getLocalizedString("bunks","Bunks")),1,row++);
      box.add(getLink(Accounts.class,iwrb.getLocalizedString("accounts","Accounts")),1,row++);
      if(FinanceObjects != null){
        java.util.Iterator I = FinanceObjects.iterator();
        FinanceObject obj;
        while(I.hasNext()){
          obj = (FinanceObject) I.next();
          box.add(getLink(obj.getClass(),iwrb.getLocalizedString(obj.getKey(),obj.getValue())),1,row++);
        }
      }
      box.setColor(Edit.colorLight);
    frame.add(box,2,2);
    return frame;
  }

  public PresentationObject getLinkTable(int menuNr){
     Table frame = new Table();
      frame.setWidth("100%");
      frame.setCellpadding(2);
      frame.setCellspacing(2);

        int row = 1;
        int col = 1;
        frame.add(getLink(PaymentTypeEditor.class,iwrb.getLocalizedString("payment_types","Payment types")),col,row);
        frame.add(getLink(TariffKeyEditor.class,iwrb.getLocalizedString("tariff_keys","Tariff keys")),col,row);
        frame.add(getLink(AccountKeyEditor.class,iwrb.getLocalizedString("account_keys","Account keys")),col,row);
        frame.add(getLink(TariffIndexEditor.class,iwrb.getLocalizedString("indexes","Indexes")),col,row);
        frame.add(getLink(TariffEditor.class,iwrb.getLocalizedString("tariff","Tariffs")),col,row);
        frame.add(getLink(TariffAssessments.class,iwrb.getLocalizedString("assessment","Assessment")),col,row);
        frame.add(getLink(EntryGroups.class,iwrb.getLocalizedString("bunks","Bunks")),col,row);
        frame.add(getLink(Accounts.class,iwrb.getLocalizedString("accounts","Accounts")),col,row);
         if(FinanceObjects != null){
          java.util.Iterator I = FinanceObjects.iterator();
          FinanceObject obj;
          while(I.hasNext()){
            obj = (FinanceObject) I.next();
            frame.add(getLink(obj.getClass(),iwrb.getLocalizedString(obj.getKey(),obj.getValue())),1,row++);
          }
        }


      return frame;
  }

  public boolean deleteBlock(int iObjectInstanceId){
    return FinanceBusiness.deleteBlock(iObjectInstanceId);
  }

  public static Parameter getCategoryParameter(int iCategoryId){
    return new Parameter("fin_cat_id",String.valueOf(iCategoryId));
  }

  public static int parseCategoryId(IWContext iwc){
    if(iwc.isParameterSet("fin_cat_id"))
      return Integer.parseInt(iwc.getParameter("fin_cat_id"));
    else if(iwc.getApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(CATEGORY_PROPERTY)!=null)
      return Integer.parseInt(iwc.getApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(CATEGORY_PROPERTY));
    else
      return -1;
  }

  public Link getLink(Class cl,String name){
    Link L = new Link(name);
    L.addParameter(Finance.getCategoryParameter(iCategoryId));
    L.addParameter(getFinanceObjectParameter(cl));
    L.setFontSize(1);
    L.setFontColor(Edit.colorDark);
    return L;
  }

  public Parameter getFinanceObjectParameter(Class financeClass){
    return new Parameter(prmFinanceClass,financeClass.getName());
  }

  public void addFinanceObject(Block obj){
    if(FinanceObjects == null)
      FinanceObjects = new java.util.Vector();
    FinanceObjects.add(obj);
  }

  public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    control(iwc);
  }

  public void setAdministrative(boolean administrative){
    this.administrative = administrative;
  }

   public synchronized Object clone() {
    Finance obj = null;
    try {
      obj = (Finance)super.clone();
      obj.FinanceObjects  = FinanceObjects;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}
