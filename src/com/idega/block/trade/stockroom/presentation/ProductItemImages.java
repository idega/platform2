package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.builder.handler.HorizontalVerticalViewHandler;
import com.idega.core.data.ICFile;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductItemImages extends ProductItem {
  private boolean _verticalView = false;
  private boolean _horizontalView = false;
  private int _width = 0;
  private int _height = 0;
  private int _cellspacing = 3;
  private String _alignment;
  private boolean _zoom = false;
  private String _tableWidth;
  private int _numberOfImages = 0;

  /**
   *  Constructor for the ProductItemThumbnail object
   */
  public ProductItemImages() { }
  public ProductItemImages(int productId) throws RemoteException, FinderException{
    super(productId);
  }
  public ProductItemImages(Product product) throws RemoteException{
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
    List images = null;
    try {
      if (_product != null) {
        Collection coll = _product.getICFile();
        images = new Vector(coll);
//	images = new Vector(EntityFinder.getInstance().findRelated(_product, ICFile.class));
      }else {
	images = new Vector();
      }
    }catch (IDORelationshipException ido) {
      ido.printStackTrace(System.err);
      images = new Vector();
    }

    if (!_horizontalView && !_verticalView ) {
      setVerticalView(true);
    }

    Table table = new Table();
      table.setCellpadding(0);
      table.setCellspacing(_cellspacing);
      if ( _tableWidth != null )
	table.setWidth(_tableWidth);
    int row = 1;
    int column = 1;
    Image image;
    ICFile file;

    try {
      for (int i = 0; i < images.size(); i++) {
	file = (ICFile) images.get(i);
	image = new Image(file.getID());
	String att = file.getMetaData(ProductEditorWindow.imageAttributeKey);

	if(att != null) {
	  image.setAttributes(getAttributeMap(att));
	  if (!getAttributeMap(att).containsKey("align"))
	  	image.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
	}
	else
		image.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
	  

	if ( _width > 0 ) {
	  image.setWidth( _width );
	}
	if ( _height > 0 ) {
	  image.setHeight( _height );
	}
	if ( _alignment != null ) {
	  table.setAlignment( column , row , _alignment);
	}

	if ( _zoom ) {
	  Link link = new Link(image);
	    link.setTarget("_blank");
	    link.setURL(image.getMediaURL());
	  table.add(link,column,row);
	}
	else {
	  table.add(image, column, row);
	}


	if (this._horizontalView) {
	  if ( column == _numberOfImages ) {
	    column = 1;
	    row++;
	  }
	  else {
	    ++column;
	  }
	}else {
	  if ( row == _numberOfImages ) {
	    row = 1;
	    column++;
	  }
	  else {
	    ++row;
	  }
	}
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    if (images != null && images.size() > 0) {
      add(table);
    }

  }

  /**
   *  Sets width for all images
   *
   *@param  width  The new width value
   */
  public void setWidth( int width ) {
    _width = width;
  }

  public void setTableWidth(String width) {
    _tableWidth = width;
  }

  /**
   *  Sets height for all images
   *
   *@param  height  The new height value
   */
  public void setHeight( int height ) {
    _height = height;
  }

  /**
   * Sets the view
   * 1 = HORIZONTAL
   * 2 = VERTICAL
   */
  public void setView(int view) {
    if (view == HorizontalVerticalViewHandler.HORIZONTAL) {
      setHorizontalView(true);
    }else {
      setVerticalView(true);
    }
  }

  public void setVerticalView(boolean verticalView) {
    _verticalView = verticalView;
    _horizontalView = !verticalView;

  }

  public void setHorizontalView(boolean horizontalView) {
    _horizontalView = horizontalView;
    _verticalView = !horizontalView;
  }

  public void setImageAlignment(String alignment) {
    _alignment = alignment;
  }

  public void setSpaceBetweenImages(int pixels) {
    _cellspacing = pixels;
  }

  public void setImagesToZoom(boolean zoom) {
    _zoom = zoom;
  }

  public void setNumberOfImagesInLine(int numberOfImages) {
    _numberOfImages = numberOfImages;
  }

}
