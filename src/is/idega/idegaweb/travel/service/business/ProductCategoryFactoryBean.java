package is.idega.idegaweb.travel.service.business;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.*;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.presentation.ui.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductCategoryFactoryBean extends IBOServiceBean implements ProductCategoryFactory {

  public static final String CATEGORY_TYPE_PRODUCT = "sr_prod_cat_iwT_product";
  public static final String CATEGORY_TYPE_TOUR = "sr_prod_cat_tour";
  public static final String CATEGORY_TYPE_HOTEL = "sr_prod_cat_hotel";
  public static final String CATEGORY_TYPE_FISHING = "sr_prod_cat_fishing";
  public static final String CATEGORY_TYPE_CAR_RENTAL = "sr_prod_cat_car_rental";
  public static final String CATEGORY_TYPE_DEFAULT = CATEGORY_TYPE_TOUR;

  public ProductCategoryFactoryBean() {
  }

  public Collection getProductCategory(Product product) throws RemoteException, FinderException{
    Collection ids = null;
    ProductCategoryHome pHome;
    ProductCategory pCat;
    try {
      ids = product.getProductCategories();
      if (ids != null) {
        pHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
        if (ids.size() == 0) {
          pCat = pHome.getProductCategory(this.CATEGORY_TYPE_DEFAULT);
          product.addCategory(pCat);
        }

      }
    }catch (IDORelationshipException idoR){
      idoR.printStackTrace(System.err);
    }

    return ids;
  }

  public String getProductCategoryType(ProductCategory pCat) throws RemoteException {
    String returner = pCat.getCategoryType();

    if (returner == null) {
      returner = this.CATEGORY_TYPE_DEFAULT;
      pCat.setCategoryType(returner);
      pCat.store();
    }

    return returner;
  }

  public Collection getAllProductCategories() throws RemoteException, FinderException{
    ProductCategoryHome pHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
    List list = new Vector();
      list.add(pHome.getProductCategory(this.CATEGORY_TYPE_TOUR).getPrimaryKey());
      list.add(pHome.getProductCategory(this.CATEGORY_TYPE_HOTEL).getPrimaryKey());
      list.add(pHome.getProductCategory(this.CATEGORY_TYPE_FISHING).getPrimaryKey());
//      list.add(pHome.getProductCategory(this.CATEGORY_TYPE_PRODUCT).getPrimaryKey());
      list.add(pHome.getProductCategory(this.CATEGORY_TYPE_CAR_RENTAL).getPrimaryKey());
    return list;
  }

  public ProductCategoryHome getProductCategoryHome() {
    return (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
  }

  public String getProductCategoryTypeDefaultName(String type) {
    if (type != null) {
      if (type.equals(this.CATEGORY_TYPE_TOUR)) {
        return "Tour";
      }else if (type.equals(this.CATEGORY_TYPE_HOTEL)) {
        return "Accomodation";
      }else if (type.equals(this.CATEGORY_TYPE_FISHING)) {
        return "Fishing";
      }else if (type.equals(this.CATEGORY_TYPE_PRODUCT)) {
        return "Product";
      }else if (type.equals(this.CATEGORY_TYPE_CAR_RENTAL)) {
        return "Car rental";
      }
    }
    return "Temp Category !";
  }

  public DropdownMenu getProductCategoryDropdown(IWResourceBundle iwrb, Supplier supplier, String name) throws IDORelationshipException, RemoteException {
    DropdownMenu menu = new DropdownMenu(name);
    Collection coll = supplier.getProductCategories();
    Iterator iter = coll.iterator();
    ProductCategory pCat;
    String type;
    while (iter.hasNext()) {
      pCat = (ProductCategory) iter.next();
      type = getProductCategoryType(pCat);
      menu.addMenuElement(type, iwrb.getLocalizedString(type));
    }
    return menu;
  }

  public ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }
}
