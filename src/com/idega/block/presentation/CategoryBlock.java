package com.idega.block.presentation;

import com.idega.block.IWBlock;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.core.data.ICCategory;
import com.idega.block.category.business.CategoryFinder;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 2.0
 */

public abstract class CategoryBlock extends Block{

  private ICCategory icCategory;
  private int icCategoryId = -1;
  public final static String prmCategoryId = "catbl_catid";
  private boolean autocreate = true;

  public int getCategoryId(){
    return icCategoryId;
  }

  public void setAutoCreate(boolean autocreate){
    autocreate = autocreate;
  }

  protected void initCategory(IWContext iwc){
    if(icCategoryId <= 0){
      String sCategoryId = iwc.getParameter(prmCategoryId );
      if(sCategoryId != null){
        icCategoryId = Integer.parseInt(sCategoryId);
        icCategory = CategoryFinder.getCategory(icCategoryId);
      }
      else if(getICObjectInstanceID() > 0){
        icCategoryId = CategoryFinder.getObjectInstanceCategoryId(getICObjectInstanceID(),autocreate,getCategoryType());
      }
    }
  }

  public void initializeInMain(IWContext iwc){
    initCategory(iwc);
  }

  public synchronized Object clone() {
    CategoryBlock obj = null;
    try {
      obj = (CategoryBlock)super.clone();
      obj.icCategory = icCategory;
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

  public Link getCategoryLink(String type){
    Link L = new Link();
    L.addParameter(CategoryWindow.prmCategoryId,getCategoryId());
    L.addParameter(CategoryWindow.prmObjInstId,getICObjectInstanceID());
    L.addParameter(CategoryWindow.prmCategoryType,type);
    L.setWindowToOpen(CategoryWindow.class);
    return L;
  }

  public String getCategoryType(){
    return "no_type";
  }


}
