package com.idega.block.trade.stockroom.presentation;

import com.idega.block.trade.stockroom.data.Product;
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

public abstract class AbstractProductViewerLayout extends Block {

  public AbstractProductViewerLayout() {
  }

  public abstract PresentationObject getDemo(ProductViewer productViewer, IWContext iwc);
  public abstract PresentationObject getViewer(ProductViewer productViewer, Product product, IWContext iwc);
}
