package com.idega.block.trade.stockroom.data;

import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.data.*;
import java.sql.SQLException;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class VariantBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.Variant {

  public VariantBMPBean() {
    super();
  }

  public VariantBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "Name", true, true, String.class);
    addAttribute(getColumnNameProductId(), "product_id", true, true, Integer.class, super.MANY_TO_ONE, Product.class);
  }
  public String getEntityName() {
    return getTableNameVariant();
  }

  public static String getTableNameVariant() {return "SR_VARIANT";}
  public static String getColumnNameName() {return "SR_VARIANT_NAME";}
  public static String getColumnNameProductId() {return "SR_PRODUCT_ID";}


  public void setName(String name) {
    setColumn(getColumnNameName(), name);
  }

  public void setProductId(int productId) {
    setColumn(getColumnNameProductId(), productId);
  }

  public void setProduct(Product product) {
    setProductId(product.getID());
  }


  public String getName() {
    return getStringColumnValue(getColumnNameName());
  }

  public int getProductId() {
    return getIntColumnValue(getColumnNameProductId());
  }

  public Product getProduct() throws SQLException {
    return ProductBusiness.getProduct(getProductId());
  }



}
