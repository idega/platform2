/*
 * $Id: IShopExportPage.java,v 1.2 2002/03/19 09:41:10 palli Exp $
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
import com.idega.idegaweb.presentation.IWAdminWindow;
import is.idega.idegaweb.intershop.data.IShopTemplate;
import is.idega.idegaweb.intershop.business.IShopTemplateHome;
import is.idega.idegaweb.intershop.business.IShopExportBusiness;
import java.util.Properties;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
*/
public class IShopExportPage extends IWAdminWindow {
  private boolean exportToSybase(IWContext iwc) {
    BuilderLogic instance = BuilderLogic.getInstance();

    String ib_page_id = instance.getCurrentIBPage(iwc);
    IBPage ibpage = null;
    int page_id = -1;
    try {
      page_id = Integer.parseInt(ib_page_id);
      ibpage = new IBPage(page_id);
    }
    catch(Exception e) {
      return(false);
    }

    if (!ibpage.getSubType().equals(IShopTemplate.SUBTYPE_NAME))
      return(false);

    IShopTemplate is_page = IShopTemplateHome.getInstance().getElement(page_id);
    if (is_page == null)
      return(false);

    String html = instance.getCurrentPageHtml(iwc);
    if (html == null)
      return(false);

    Properties props = new Properties();
//    props.load();

    return(true);
  }

  public void main(IWContext iwc) throws Exception {
//    exportToSybase(iwc);
    IShopExportBusiness.getInstance().exportPage(null,null,null,iwc.getApplicationContext());

    close();
  }
}