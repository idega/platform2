package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.data.EntityFinder;
import java.util.LinkedList;
import java.util.List;
import com.idega.data.GenericEntity;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import java.lang.Integer;
import com.idega.projects.golf.entity.GolferPageData;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferJModuleObject extends JModuleObject {

  protected final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  protected int member_id ;
  protected GolferPageData golferPageData;

  public GolferJModuleObject() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(ModuleInfo modinfo) throws SQLException{
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    this.member_id = 3152;
    golferPageData = new GolferPageData();
    List list = EntityFinder.findAllByColumn( (GenericEntity) golferPageData, GolferPageData.MEMBER_ID, 3152);
    golferPageData = (GolferPageData) list.get(0);
  }
}