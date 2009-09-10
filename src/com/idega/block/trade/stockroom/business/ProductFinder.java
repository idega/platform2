package com.idega.block.trade.stockroom.business;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.core.component.business.ICObjectBusiness;
import com.idega.core.component.data.ICObjectInstance;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductFinder {

  public ProductFinder() {
  }

  public static Product getObjectInstanceFromID(int ICObjectInstanceID){
    try {
      ICObjectBusiness icob = ICObjectBusiness.getInstance();
      ICObjectInstance ICObjInst = icob.getICObjectInstance(ICObjectInstanceID);
      return (Product)icob.getRelatedEntity(ICObjInst,Product.class);
    }
    catch (com.idega.data.IDOFinderException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
