/*
 *  $Id: IShopBundleStarter.java,v 1.1 2002/03/11 22:59:27 palli Exp $
 *
 *  Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.business;

import com.idega.builder.app.IBApplication;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.intershop.presentation.IShopToolbarButton;
import java.util.List;
import java.util.Vector;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */
public class IShopBundleStarter implements IWBundleStartable {
  /**
   * Constructor for the IShopBundleStarter object
   */
  public IShopBundleStarter() { }

  /**
   * Description of the Method
   *
   * @param starterBundle  Description of the Parameter
   */
  public void start(IWBundle starterBundle) {
    System.out.println("Starting Intershop initialization");
    IShopToolbarButton separator = new IShopToolbarButton(starterBundle, true);
    IShopToolbarButton button = new IShopToolbarButton(starterBundle, false);

    List l = (List)starterBundle.getApplication().getAttribute(IBApplication.TOOLBAR_ITEMS);
    if (l == null) {
      l = new Vector();
      starterBundle.getApplication().setAttribute(IBApplication.TOOLBAR_ITEMS, l);
    }

    l.add(separator);
    l.add(button);
  }
}