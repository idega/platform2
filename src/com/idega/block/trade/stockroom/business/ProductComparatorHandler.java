/*
 *  $Id: ProductComparatorHandler.java,v 1.2 2004/06/28 11:18:35 thomas Exp $
 *
 *  Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.block.trade.stockroom.business;
import java.util.List;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.core.builder.data.ICPropertyHandler;

/**
 *@author     <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 *@created    10. mars 2002
 *@version    1.0
 */
public class ProductComparatorHandler implements ICPropertyHandler {
 /**
  */
 public ProductComparatorHandler() { }

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
  DropdownMenu menu = new DropdownMenu( name );
  menu.addMenuElement( "", "Select:" );
  menu.addMenuElement( ProductComparator.NAME, "Name" );
  menu.addMenuElement( ProductComparator.NUMBER, "Number" );
  menu.addMenuElement( ProductComparator.PRICE, "Price" );
  menu.setSelectedElement( value );
  return ( menu );
 }

 /**
  *@param  values  Description of the Parameter
  *@param  iwc     Description of the Parameter
  */
 public void onUpdate( String values[], IWContext iwc ) { }
}
