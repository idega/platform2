/*
 * $Id: ProductViewerLayoutHandler.java,v 1.8 2004/06/28 14:07:44 thomas Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.trade.stockroom.business;

import java.util.List;

import com.idega.block.trade.stockroom.presentation.ProductViewerLayoutIdega;
import com.idega.block.trade.stockroom.presentation.ProductViewerLayoutStandard;
import com.idega.block.trade.stockroom.presentation.ProductViewerLayoutTeaser;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ProductViewerLayoutHandler implements ICPropertyHandler {
  /**
   *
   */
  public ProductViewerLayoutHandler() {
  }

  public List getDefaultHandlerTypes() {
    return(null);
  }

  /**
   *
   */
  public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
    DropdownMenu menu = new DropdownMenu(name);
    menu.addMenuElement("","Select:");
    menu.addMenuElement(ProductViewerLayoutIdega.class.getName(), "Idega Portal");
		menu.addMenuElement(ProductViewerLayoutStandard.class.getName(), "Standard");
		menu.addMenuElement(ProductViewerLayoutTeaser.class.getName(), "Teaser");
    menu.setSelectedElement(value);
    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
