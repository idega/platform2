package com.idega.block.trade.stockroom.data;

import com.idega.core.data.ICFile;
import com.idega.core.data.ICCategory;
import java.sql.SQLException;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author       <a href="mailto:gimmi@idega.is">Grimur Jónsson</a>
 * @version 1.0
 */

public class ProductCategoryBMPBean extends com.idega.core.data.ICCategoryBMPBean implements com.idega.block.trade.stockroom.data.ProductCategory {

  public static final String CATEGORY_TYPE_TOUR = "sr_prod_cat_tour";
  public static final String CATEGORY_TYPE_HOTEL = "sr_prod_cat_hotel";
  public static final String CATEGORY_TYPE_FISHING = "sr_prod_cat_fishing";
  public static final String CATEGORY_TYPE_PRODUCT = "sr_prod_cat_product";

  public ProductCategoryBMPBean(){
    super();
  }

  public ProductCategoryBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    super.initializeAttributes();
  }

  public void setDefaultValues() {
    super.setDefaultValues();
    this.setType(CATEGORY_TYPE_PRODUCT);
  }


} // Class ProductCategory
