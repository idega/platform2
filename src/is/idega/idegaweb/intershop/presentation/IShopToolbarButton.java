/*
 *  $Id: IShopToolbarButton.java,v 1.3 2002/04/16 14:42:38 palli Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.presentation;

import com.idega.builder.app.IBToolbarButton;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import is.idega.idegaweb.intershop.presentation.IShopExportPage;
import is.idega.idegaweb.intershop.presentation.IShopCreatePageWindow;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */
public class IShopToolbarButton implements IBToolbarButton {
  public static final int BUTTON_SEPERATOR = 0;
  public static final int BUTTON_EXPORT    = 1;
  public static final int BUTTON_NEW_PAGE  = 2;
  private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.intershop";
  private Link _link = null;
  private boolean _isSeparator = false;

  /**
   * Constructor for the IShopToolbarButton object
   *
   * @param iwb          Description of the Parameter
   * @param isSeparator  Description of the Parameter
   */
  public IShopToolbarButton(IWBundle iwb, int type) {
    switch (type) {
      case BUTTON_SEPERATOR :
        _isSeparator = true;
        break;
      case BUTTON_EXPORT :
        Image image = iwb.getImage("export_intershop.gif", "export_intershop1.gif", "Export to Intershop", 20, 20);
        image.setHorizontalSpacing(2);
        _link = new Link(image);
        _link.setWindowToOpen(IShopExportPage.class);
        break;
      case BUTTON_NEW_PAGE :
        Image image2 = iwb.getImage("newpage_intershop.gif", "newpage_intershop1.gif", "New Intershop Page", 20, 20);
        image2.setHorizontalSpacing(2);
        _link = new Link(image2);
        _link.setWindowToOpen(IShopCreatePageWindow.class);
        break;
    }
  }

  /**
   * Gets the link attribute of the IShopToolbarButton object
   *
   * @return   The link value
   */
  public Link getLink() {
    return (_link);
  }

  /**
   * Gets the isSeparator attribute of the IShopToolbarButton object
   *
   * @return   The isSeparator value
   */
  public boolean getIsSeparator() {
    return (_isSeparator);
  }

  /**
   * @return   The bundleIdentifier value
   */
  public String getBundleIdentifier() {
    return (IW_BUNDLE_IDENTIFIER);
  }
}