/*
 * $Id: IShopExportPage.java,v 1.4 2002/04/06 19:11:21 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.presentation;

import com.idega.builder.business.BuilderLogic;
import com.idega.builder.data.IBPage;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.util.FileUtil;
import is.idega.idegaweb.intershop.data.IShopTemplate;
import is.idega.idegaweb.intershop.business.IShopTemplateHome;
import is.idega.idegaweb.intershop.business.IShopExportBusiness;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
*/
public class IShopExportPage extends IWAdminWindow {
  private static final String IW_BUNDLE_IDENTIFIER  = "is.idega.idegaweb.intershop";

  private boolean exportToSybase(IWContext iwc) {
    BuilderLogic instance = BuilderLogic.getInstance();

    IWBundle bundle = getBundle(iwc);
    StringBuffer path = new StringBuffer(bundle.getPropertiesRealPath());
    if (!path.toString().endsWith(FileUtil.getFileSeparator()))
      path.append(FileUtil.getFileSeparator());

    path.append(bundle.getProperty("sybaseproperties","sybasedb.properties"));

    String ib_page_id = instance.getCurrentIBPage(iwc);
    IBPage ibpage = null;
    int page_id = -1;
    try {
      page_id = Integer.parseInt(ib_page_id);
      ibpage = ((com.idega.builder.data.IBPageHome)com.idega.data.IDOLookup.getHomeLegacy(IBPage.class)).findByPrimaryKeyLegacy(page_id);
    }
    catch(Exception e) {
      return(false);
    }

    String subType = ibpage.getSubType();
    if (subType == null)
      return(false);

    if (!subType.equals(IShopTemplate.SUBTYPE_NAME))
      return(false);

    IShopTemplate is_page = IShopTemplateHome.getInstance().findByPageId(page_id);
    if (is_page == null)
      return(false);

    String html = instance.getCurrentPageHtml(iwc);
    if (html == null)
      return(false);

    Properties props = new Properties();
    try {
      props.load(new FileInputStream(path.toString()));
    }
    catch(FileNotFoundException e) {
      e.printStackTrace();
      return(false);
    }
    catch(IOException e) {
      e.printStackTrace();
      return(false);
    }

    IShopExportBusiness.getInstance().exportPage(is_page,props,html,iwc.getApplicationContext());

    return(true);
  }

  public void main(IWContext iwc) throws Exception {
    exportToSybase(iwc);

    close();
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  }
}
