/*
 *  $Id: ProductCatalogLayoutHandler.java,v 1.9 2004/08/27 10:35:37 gimmi Exp $
 *
 *  Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.block.trade.stockroom.business;
import com.idega.block.trade.stockroom.presentation.*;
import java.util.List;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.core.builder.presentation.ICPropertyHandler;

/**
 *@author     <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 *@created    10. mars 2002
 *@version    1.0
 */
public class ProductCatalogLayoutHandler implements ICPropertyHandler {
 /**
  */
 public ProductCatalogLayoutHandler() { }

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
  menu.addMenuElement( ProductCatalogLayoutSingleFile.class.getName(), "Single file" );
  menu.addMenuElement( ProductCatalogLayoutProductList.class.getName(), "Product list" );
  menu.addMenuElement( ProductCatalogLayoutExpandedList.class.getName(), "Expanded list" );
  menu.addMenuElement( ProductCatalogLayoutMultibleColumns.class.getName(), "Column list" );
  menu.addMenuElement( ProductCatalogLayoutCategories.class.getName(), "Category list" );
  menu.addMenuElement( ProductCatalogLayoutSimple.class.getName(), "Simple list" );
  menu.addMenuElement( ProductCatalogLayoutWineList.class.getName(), "Category tree" );
  menu.addMenuElement( ProductCatalogLayoutMetadataList.class.getName(), "Metadata list" );
  menu.setSelectedElement( value );
  return ( menu );
 }

 /**
  *@param  values  Description of the Parameter
  *@param  iwc     Description of the Parameter
  */
 public void onUpdate( String values[], IWContext iwc ) { }
}
