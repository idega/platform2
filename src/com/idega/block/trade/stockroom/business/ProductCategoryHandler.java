/*
 *  $Id: ProductCategoryHandler.java,v 1.2 2002/04/06 19:07:37 tryggvil Exp $
 *
 *  Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.block.trade.stockroom.business;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.core.business.CategoryFinder;
import com.idega.block.trade.stockroom.presentation.*;
import java.util.List;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.builder.handler.PropertyHandler;

/**
 *@author     <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 *@created    10. mars 2002
 *@version    1.0
 */
public class ProductCategoryHandler implements PropertyHandler {
 /**
  */
 public ProductCategoryHandler() { }

 /**
  *@return    The defaultHandlerTypes value
  */
 public List getDefaultHandlerTypes() {
  return ( null );
 }

 /**
  *@param  name   Description of the Parameter
  *@param  value  Description of the Parameter
  *@param  iwc    Description of the Parameter
  *@return        The handlerObject value
  */
 public PresentationObject getHandlerObject( String name, String value, IWContext iwc ) {
  List categories = CategoryFinder.getInstance().listOfCategories(com.idega.block.trade.stockroom.data.ProductCategoryBMPBean.CATEGORY_TYPE_PRODUCT);
  DropdownMenu menu = new DropdownMenu( name );
  menu.addMenuElement( "", "Select:" );
  menu.addMenuElements(categories);
  menu.setSelectedElement( value );
  return ( menu );
 }

 /**
  *@param  values  Description of the Parameter
  *@param  iwc     Description of the Parameter
  */
 public void onUpdate( String values[], IWContext iwc ) { }
}
