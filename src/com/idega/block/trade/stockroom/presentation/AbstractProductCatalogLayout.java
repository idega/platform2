package com.idega.block.trade.stockroom.presentation;

import java.util.*;
import com.idega.presentation.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public abstract class AbstractProductCatalogLayout extends Block {

  public AbstractProductCatalogLayout() {
  }

  public abstract PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories);

}