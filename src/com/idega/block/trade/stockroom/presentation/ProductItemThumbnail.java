package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.core.data.ICFile;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.block.presentation.ImageWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;

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
  private boolean _clickableThumbnail;

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
      
		  if (_clickableThumbnail) {
				List images = null;
				try {
					if (_product != null) {
						Collection coll = _product.getICFile();
						if (coll != null)
							images = new Vector(coll);
						else
							images = new Vector();
						//	images = new Vector(EntityFinder.getInstance().findRelated(_product, ICFile.class));
					}
					else {
						images = new Vector();
					}
				}
				catch (IDORelationshipException ido) {
					ido.printStackTrace(System.err);
					images = new Vector();
				}

				if (!images.isEmpty()) {
					Link imageLink = new Link(image);
					imageLink.setWindowToOpen(ImageWindow.class);
					imageLink.addParameter(ImageWindow.prmImageId, ((ICFile) images.get(0)).getPrimaryKey().toString());
					add(imageLink);
				}
				else
					add(image);
			}
		  else
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

	public void setClickableThumbnail(boolean clickableThumbnail) {
		_clickableThumbnail = clickableThumbnail;
	}
}
