package is.idega.idegaweb.golf.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;

import java.util.LinkedList;
import java.util.List;
import com.idega.data.GenericEntity;
import com.idega.presentation.IWContext;
import java.sql.SQLException;
import java.lang.Integer;
import is.idega.idegaweb.golf.entity.GolferPageData;
import is.idega.idegaweb.golf.entity.GolferPageDataBMPBean;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferJModuleObject extends Block {

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

  public void main(IWContext modinfo) throws SQLException{
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    this.member_id = 3152;
    golferPageData = (GolferPageData) IDOLookup.instanciateEntity(GolferPageData.class);
    List list = EntityFinder.findAllByColumn(golferPageData, GolferPageDataBMPBean.MEMBER_ID, 3152);
    golferPageData = (GolferPageData) list.get(0);
  }
}