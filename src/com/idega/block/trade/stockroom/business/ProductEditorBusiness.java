package com.idega.block.trade.stockroom.business;
import com.idega.presentation.Image;
import com.idega.block.trade.business.CurrencyHolder;
import java.util.*;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.data.EntityFinder;
import com.idega.core.business.Category;
import com.idega.data.IDOFinderException;
import com.idega.block.category.business.CategoryFinder;
import com.idega.presentation.ui.SelectionBox;
import com.idega.util.idegaTimestamp;
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

public class ProductEditorBusiness {
  private static ProductEditorBusiness peb;

  private ProductEditorBusiness() {}

  public static ProductEditorBusiness getInstance() {
    if (peb == null) {
      return new ProductEditorBusiness();
    }else {
      return peb;
    }
  }

  public void setCategories(Product product, String[] categoryIds) {
    try {
      product.removeFrom(ProductCategory.class);
      if (categoryIds != null) {
        ProductCategory pCat;
        for (int i = 0; i < categoryIds.length; i++) {
          pCat = ((com.idega.block.trade.stockroom.data.ProductCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ProductCategory.class)).findByPrimaryKeyLegacy(Integer.parseInt(categoryIds[i]));
          addCategory(product, pCat);
        }
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
  }

  public boolean addCategory(Product product, ProductCategory productCategory) {
    try {
      productCategory.addTo(product);
      return true;
    }catch (Exception e) {
      return false;
    }
  }

  public void dropImage(Product product, boolean update) throws SQLException {
    product.setFileId(null);
    if (update) {
      product.update();
    }
  }


  public void setThumbnail(Product product, int thumbnailId) {
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
            product.update();
            product.removeFrom(ICFile.class, newThumbId);
          }
        }

        if (perform) {
          if (oldThumbId != -1) {
            product.addTo(ICFile.class, oldThumbId);
          }
        }
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
  }

  public boolean setPrice(Product product, String price, String currencyId) {
    if (price == null) {
      return false;
    }else {
      try {
        ProductPrice pPri = StockroomBusiness.getPrice(product);
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
            pPrice.setPriceDate(idegaTimestamp.getTimestampRightNow());
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

  public SelectionBox getCategorySelectionBox(Product product, String name, int productCatalogObjectInstanceId) {
      SelectionBox catSel = new SelectionBox(name);
      List cats = CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(productCatalogObjectInstanceId);
      if (cats != null) {
        catSel = new SelectionBox(cats);
        catSel.setName(name);
      }
      if (product != null) {
        try {
          List rCats = ProductBusiness.getProductCategories(product);
          Category icCat;
          if (rCats != null && rCats.size() > 0) {
            for (int i = 0; i < rCats.size(); i++) {
              icCat = (Category) rCats.get(i);
              catSel.setSelectedElement(Integer.toString(icCat.getID()));
            }
          }
        }catch (IDOFinderException ido) {
          ido.printStackTrace(System.err);
        }
      }
    return catSel;
  }

  public List getFiles(Product product) {
    List files = new Vector();
    if (product == null) {
      return null;
    }else {
      try {
        List list = EntityFinder.getInstance().findRelated(product, ICFile.class);
        files = new Vector(list);
      }catch (IDOFinderException ido) {
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


  public void addImage(Product product, int imageId) {
    if (imageId != -1) {
      try {
        product.addTo(ICFile.class, imageId);
      }catch (SQLException sql){
        //sql.printStackTrace(System.err);
      }
    }
  }

  public void removeImage(Product product, int imageId) {
    if (imageId != -1) {
      try {
        if (product.getFileId() == imageId) {
          product.setFileId(null);
          product.update();
        }else {
          product.removeFrom(ICFile.class, imageId);
        }
      }catch (SQLException sql){
        sql.printStackTrace(System.err);
      }
    }
  }

  public boolean deleteProduct(Product product) {
    try {
      ProductBusiness.deleteProduct(product);
      return true;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
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


}
