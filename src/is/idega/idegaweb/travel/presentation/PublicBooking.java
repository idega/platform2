package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;

import com.idega.block.trade.stockroom.data.*;
import is.idega.idegaweb.travel.data.Service;

import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class PublicBooking extends Block  {

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";
  IWResourceBundle iwrb;
  IWBundle bundle;
  Product product;
  Service service;
  Supplier supplier;
  int productId = -1;


  public PublicBooking() {
  }

  public void main(IWContext iwc)throws Exception {
    init(iwc);

    if (productId != -1 ) {
      displayForm(iwc);
    }else {
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc.getCurrentLocale());

    String sProductId = iwc.getParameter(LinkGenerator.parameterProductId);
    if (sProductId != null) {
      try {

        productId = Integer.parseInt(sProductId);
        product = new Product(productId);
        if (!product.getIsValid()) {
          throw new SQLException("Product not valid");
        }
        service = new Service(productId);
        supplier = new Supplier(product.getSupplierId());

      }catch (SQLException s) {
        s.printStackTrace(System.err);
      }
    }
  }

  private void displayForm(IWContext iwc) {
      Table table = new Table(2,3);
        table.setWidth("90%");
        table.setHeight("90%");
        table.setAlignment("center");
        table.setCellspacing(1);
        table.setColor(TravelManager.WHITE);
        table.setBorder(0);


      Image background = bundle.getImage("images/sb_background.gif");
      table.setBackgroundImage(1, 2, background);
      table.setRowColor(3, TravelManager.backgroundColor);
      table.setColor(2, 1, TravelManager.backgroundColor);
      table.mergeCells(2, 1, 2, 2);
      table.setHeight(1, "20");
      table.setVerticalAlignment(1,1,"top");
      table.setVerticalAlignment(1,2,"top");
      table.setHeight(3, "20");
      table.setWidth(2, "20");


      table.add(leftTop(iwc),1,1);
      table.add(right(iwc),2,1);

      add(table);
  }


  private Table right(IWContext iwc)  {
    Table table = new Table(1,2);
      table.setVerticalAlignment(1,1,"top");
      table.setVerticalAlignment(1,2,"top");

    try {
      CalendarHandler ch = new CalendarHandler(iwc);
        ch.setProduct(product);
      table.add(ch.getCalendarTable(iwc));
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return table;
  }

  private Table leftTop(IWContext iwc) {
    Table table = new Table();
    try {
      ServiceOverview so = new ServiceOverview(iwc);
      table = so.getProductInfoTable(iwc, iwrb, product);
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return table;
  }

}