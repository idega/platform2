package com.idega.block.trade.stockroom.business;
import javax.ejb.FinderException;
import com.idega.core.data.ICFileHome;
import com.idega.data.*;
import com.idega.business.IBOServiceBean;
import java.rmi.RemoteException;
import com.idega.business.IBOLookup;
import com.idega.presentation.Image;
import com.idega.block.trade.business.CurrencyHolder;
import java.util.*;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.core.business.Category;
import com.idega.block.category.business.CategoryFinder;
import com.idega.presentation.ui.SelectionBox;
import com.idega.util.IWTimestamp;
import com.idega.core.data.ICFile;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductEditorBusiness extends IBOServiceBean{
  private static ProductEditorBusiness peb;

  private ProductEditorBusiness() {}

  public static ProductEditorBusiness getInstance() {
    if (peb == null) {
      return new ProductEditorBusiness();
    }else {
      return peb;
    }
  }

  public void setCategories(Product product, String[] categoryIds) throws RemoteException, IDOException, FinderException{
    if (categoryIds != null) {
      int[] iCategoryIds = new int[categoryIds.length];
      for (int i = 0; i < categoryIds.length; i++) {
        iCategoryIds[i] = Integer.parseInt(categoryIds[i]) ;
      }

      product.setProductCategories(iCategoryIds);
    }
  }

  public void dropImage(Product product, boolean update)  throws RemoteException {
    product.setFileId(null);
    if (update) {
      product.store();
    }
  }


  public void setThumbnail(Product product, int thumbnailId) throws RemoteException, FinderException, IDOException {
    try {
      boolean perform = true;
      if (thumbnailId != -1) {
	int newThumbId = thumbnailId;
	int oldThumbId = product.getFileId();

	if (newThumbId == product.getFileId()) {
	  perform = false;
	}


	if (perform) {
	  if (newThumbId == -1) {
	    dropImage(product, true);
	    //product.setFileId(null);
	    //product.update();
	  }else {
	    product.setFileId(newThumbId);
	    product.store();
            ICFileHome fileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
            ICFile file = fileHome.findByPrimaryKey(newThumbId);
            product.removeICFile(file);
	    //product.removeFrom(ICFile.class, newThumbId);
	  }
	}

	if (perform) {
	  if (oldThumbId != -1) {
            ICFileHome fileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
            ICFile file = fileHome.findByPrimaryKey(oldThumbId);
            product.addICFile(file);
//	    product.addTo(ICFile.class, oldThumbId);
	  }
	}
      }
    }catch (IDOException ido) {
      ido.printStackTrace(System.err);
    }
  }

  public boolean setPrice(Product product, String price, String currencyId) throws RemoteException {
    if (price == null) {
      return false;
    }else {
      try {
	ProductPrice pPri = getStockroomBusiness().getPrice(product);
	int oldP = 0;
	int pCurrId = -1;
	if (pPri != null) {
	  oldP = (int) pPri.getPrice();
	  pCurrId = pPri.getID();
	}
	int newP = Integer.parseInt(price);
	if (oldP != newP || Integer.parseInt(currencyId) != pCurrId) {
	  ProductPrice pPrice = ((com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).createLegacy();
	    pPrice.setIsValid(true);
	    pPrice.setPrice(Float.parseFloat(price));
	    pPrice.setPriceType(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE);
	    pPrice.setProductId(product.getID());
	    pPrice.setPriceDate(IWTimestamp.getTimestampRightNow());
	    pPrice.setCurrencyId(Integer.parseInt(currencyId));
	  pPrice.insert();
	  return true;
	}else {
	  return true;
	}
      }catch (Exception e) {
	return false;
      }
    }
  }

  public SelectionBox getCategorySelectionBox(Product product, String name, int productCatalogObjectInstanceId) throws RemoteException{
    SelectionBox catSel = new SelectionBox(name);
    List cats = CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(productCatalogObjectInstanceId);
    if (product != null) {
      try {
        cats = getProductBusiness().getProductCategories(product);
      }catch (IDORelationshipException ido) {
        ido.printStackTrace(System.err);
      }
    }

    Category icCat;
    if (cats != null) {
      catSel.addMenuElements(cats);
//      if ( product != null ) { Gimmi 17.08.02
        catSel.setAllSelected(true);
//      }
    }

    return catSel;
  }

  public SelectionBox getSelectionBox(Product product, String name, int productCatalogObjectInstanceId) throws RemoteException{
	  SelectionBox catSel = new SelectionBox(name);
	  List cats = CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(productCatalogObjectInstanceId);
	  if (product != null) {
		try {
	 	  cats = getProductBusiness().getProductCategories(product);
		}catch (IDORelationshipException ido) {
		  ido.printStackTrace(System.err);
		}
	  }

	  Category icCat;
	  if (cats != null ) {
	  	catSel.addMenuElements(cats);
	  	if ( product != null )
		  	catSel.setAllSelected(true);
	  }

	  return catSel;
  }

  public List getFiles(Product product) throws RemoteException {
    List files = new Vector();
    if (product == null) {
      return null;
    }else {
      try {
        Collection coll = product.getICFile();
//	List list = EntityFinder.getInstance().findRelated(product, ICFile.class);
	files = new Vector(coll);
      }catch (IDORelationshipException ido) {
	ido.printStackTrace(System.err);
      }
    }
    if (files != null) {
      int imageId = product.getFileId();
      if (imageId != -1) {
	try {
	  if (!files.contains(((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).findByPrimaryKeyLegacy(imageId))) {
	    files.add(0, ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).findByPrimaryKeyLegacy(imageId));
	  }
	}catch (SQLException sql){
	  sql.printStackTrace(System.err);
	}
      }
    }
    return files;
  }


  public void addImage(Product product, int imageId) throws RemoteException, FinderException {
    if (imageId != -1 && product != null) {
      try {
        ICFileHome fHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
        product.addICFile(fHome.findByPrimaryKey(imageId));
//	product.addTo(ICFile.class, imageId);
      }catch (IDOAddRelationshipException re){
	//sql.printStackTrace(System.err);
      }
    }
  }

  public void removeImage(Product product, int imageId) throws RemoteException, FinderException {
    if (imageId != -1) {
      try {
	if (product.getFileId() == imageId) {
	  product.setFileId(null);
	  product.store();
	}else {
          ICFileHome fHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
          product.removeICFile(fHome.findByPrimaryKey(imageId));
//	  product.removeFrom(ICFile.class, imageId);
	}
      }catch (IDOException ido){
	ido.printStackTrace(System.err);
      }
    }
  }

  public boolean deleteProduct(Product product) throws RemoteException {
    try {
      getProductBusiness().deleteProduct(product);
      return true;
    }catch (IDOException ido) {
      ido.printStackTrace(System.err);
      return false;
    }
  }

  public Image getImage(Object object) throws SQLException{
    if (object instanceof ICFile) {
      ICFile f = (ICFile) object;
      return new Image(f.getID());
    }else {
      return null;
    }
  }

  public DropdownMenu getCurrencyDropdown(String dropdownName, String defaultCurrency) {
    DropdownMenu _currencies = new DropdownMenu(dropdownName);
    List currencyList = CurrencyBusiness.getCurrencyList();
    Iterator iter = currencyList.iterator();
    while (iter.hasNext()) {
      CurrencyHolder holder = (CurrencyHolder) iter.next();
      _currencies.addMenuElement(holder.getCurrencyID(), holder.getCurrencyName());
    }
    if (defaultCurrency != null) {
      try {
	_currencies.setSelectedElement(Integer.toString(CurrencyBusiness.getCurrencyHolder(defaultCurrency).getCurrencyID()));
      }
      catch (Exception e) {
      }
    }
    return _currencies;
  }

  private StockroomBusiness getStockroomBusiness() throws RemoteException{
    return (StockroomBusiness) IBOLookup.getServiceInstance(super.getIWApplicationContext(), StockroomBusiness.class);
  }

  private ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(super.getIWApplicationContext(), ProductBusiness.class);
  }
}
