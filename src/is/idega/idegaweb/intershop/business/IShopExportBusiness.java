/*
 *  $Id: IShopExportBusiness.java,v 1.1 2002/03/11 22:59:27 palli Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.business;

import is.idega.idegaweb.intershop.data.IShopTemplate;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class IShopExportBusiness {
  private static IShopExportBusiness _instance = null;

  private IShopExportBusiness() {
  }

  public static IShopExportBusiness getInstance() {
    if (_instance != null)
      _instance = new IShopExportBusiness();

    return(_instance);
  }

  public boolean exportPage(IShopTemplate page) {

    return(true);
  }
}