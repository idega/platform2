/*
 * $Id: IShopExportPage.java,v 1.1 2002/03/11 22:59:27 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.presentation;

import com.idega.builder.business.BuilderLogic;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.presentation.IWAdminWindow;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
*/
public class IShopExportPage extends IWAdminWindow {
  public void main(IWContext iwc) throws Exception {
    String html = BuilderLogic.getInstance().getCurrentPageHtml(iwc);
    System.out.println("source length = " + html.length());

    System.out.println("html = " + html);
    this.close();
  }
}