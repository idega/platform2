package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

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

  public abstract PresentationObject getDemo(ProductViewer productViewer, IWContext iwc) throws RemoteException;
  public abstract PresentationObject getViewer(ProductViewer productViewer, Product product, IWContext iwc)throws RemoteException ;
}
