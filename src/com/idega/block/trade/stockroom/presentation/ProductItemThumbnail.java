package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductItemThumbnail extends ProductItem {

  private Image defaultImage = _defaultImage;
  private int _width = 0;
  private int _height = 0;

  /**
   *  Constructor for the ProductItemThumbnail object
   */
  public ProductItemThumbnail() { }

  public ProductItemThumbnail(int productId) throws RemoteException, FinderException{
    super(productId);
  }
  public ProductItemThumbnail(Product product) throws RemoteException {
    super(product);
  }
  /**
   *  Description of the Method
   *
   *@param  iwc  Description of the Parameter
   */
  public void main( IWContext iwc ) throws Exception {
    super.main( iwc );
    drawObject();
  }

  /**
   *  Description of the Method
   */
  private void drawObject() throws RemoteException {
    Image image = defaultImage;
    if ( _product != null ) {
      int fileId = _product.getFileId();
      if ( fileId != -1 ) {
        image = getImage( fileId );
      }
    }

    if ( image != null ) {
      if ( _width > 0 ) {
        image.setWidth( _width );
      }
      if ( _height > 0 ) {
        image.setWidth( _height );
      }
      add( image );
    }
  }

  /**
   *  Sets the width attribute of the ProductItemThumbnail object
   *
   *@param  width  The new width value
   */
  public void setWidth( int width ) {
    _width = width;
  }

  /**
   *  Sets the height attribute of the ProductItemThumbnail object
   *
   *@param  height  The new height value
   */
  public void setHeight( int height ) {
    _height = height;
  }

}
