package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.block.presentation.ImageWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.util.text.TextSoap;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductItemThumbnail extends ProductItem {

  private Image defaultImage = this._defaultImage;
  private int _width = 0;
  private int _height = 0;
  private boolean _clickableThumbnail = true;
  private boolean _addBorder = false;

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
    ICFile file = null;
    Image image = this.defaultImage;
    if ( this._product != null ) {
      file = this._product.getFile();
      int fileId = this._product.getFileId();
      if ( fileId != -1 ) {
        image = getImage( fileId );
      }
    }

    if ( image != null ) {
			String att = file.getMetaData(ProductEditorWindow.imageAttributeKey);
	
			if (att != null) {
				image.addMarkupAttributes(getAttributeMap(att));
				if (!getAttributeMap(att).containsKey("align")) {
					image.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
				}
			}
			else {
				image.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
			}

      if ( this._width > 0 ) {
        image.setWidth( this._width );
      }
      if ( this._height > 0 ) {
        image.setWidth( this._height );
      }
      if (this._addBorder) {
		image.setBorder(1);
	}
      
		  if (this._clickableThumbnail) {
				List images = null;
				try {
					if (this._product != null) {
						Collection coll = this._product.getICFile();
						if (coll != null) {
							images = new Vector(coll);
						}
						else {
							images = new Vector();
						//	images = new Vector(EntityFinder.getInstance().findRelated(_product, ICFile.class));
						}
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
					imageLink.addParameter(ImageWindow.prmInfo, TextSoap.convertSpecialCharacters(this._product.getProductName(this._localeId)));
					add(imageLink);
				}
				else {
					add(image);
				}
			}
		else {
			add( image );
		}
    }
  }

  /**
   *  Sets the width attribute of the ProductItemThumbnail object
   *
   *@param  width  The new width value
   */
  public void setWidth( int width ) {
    this._width = width;
  }

  /**
   *  Sets the height attribute of the ProductItemThumbnail object
   *
   *@param  height  The new height value
   */
  public void setHeight( int height ) {
    this._height = height;
  }

	public void setClickableThumbnail(boolean clickableThumbnail) {
		this._clickableThumbnail = clickableThumbnail;
	}

	/**
	 * @param border
	 */
	public void setAddBorder(boolean border) {
		this._addBorder = border;
	}
}