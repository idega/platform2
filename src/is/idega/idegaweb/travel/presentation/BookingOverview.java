
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.ContractHome;
import is.idega.idegaweb.travel.service.tour.business.TourBusiness;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductCategoryHome;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.BusyBar;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class BookingOverview extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private Supplier supplier;
  private Reseller reseller;
  private Contract contract;

  private Product product;
  private int _productId = -1;

  private IWTimestamp fromStamp;
  private IWTimestamp toStamp;

  private String closerLookDateParameter = "viewServiceDate";

  private String parameterDeleteBooking = "bookingOverviewDeleteBooking";
  private String bookingOverviewAction = "bookingOverviewAction";
  private String parameterBookingId = "bookingOverviewBookingId";
  private String parameterViewAll = "-109";

  public BookingOverview() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      initialize(iwc);
      supplier = super.getSupplier();

      if (super.isLoggedOn(iwc)) {
        if (reseller != null && contract == null) {
          product = null;
        }
        displayForm(iwc);
        super.addBreak();
      }else {
        add(super.getLoggedOffTable(iwc));
      }
  }

  public void initialize(IWContext iwc) throws RemoteException{
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();
      reseller = super.getReseller();

      String productId = iwc.getParameter(com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName());
      try {
        if (productId == null) {
          productId = (String) iwc.getSessionAttribute("TB_BOOKING_PRODUCT_ID");
        }else {
          _productId = Integer.parseInt(productId);
          iwc.setSessionAttribute("TB_BOOKING_PRODUCT_ID",productId);
        }
        if (productId != null && _productId != -1 && !productId.equals(parameterViewAll)) {
          product = getProductBusiness(iwc).getProduct(_productId);
        }
      }catch (FinderException sql) {
        sql.printStackTrace(System.err);
      }


      if ((reseller != null) && (product != null)){
        try {
        			ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
        			contract = cHome.findByProductAndReseller(product.getID(), reseller.getID());
            /*Contract[] contracts = (Contract[]) (is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class)).findAllByColumn(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId(), Integer.toString(reseller.getID()), is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId(), Integer.toString(product.getID()) );
            if (contracts.length > 0) {
              contract = contracts[0];
            }*/
        }catch (FinderException sql) {
            sql.printStackTrace(System.err);
        }

      }

      fromStamp = getFromIdegaTimestamp(iwc);
      toStamp = getToIdegaTimestamp(iwc);

  }

  public void displayForm(IWContext iwc) throws Exception{

      Form form = new Form();
      Table topTable = getTopTable(iwc);
        form.add(topTable);
        form.add(Text.BREAK);
      Table table = new Table();

      String action = iwc.getParameter(this.bookingOverviewAction);
      if (action == null) action = "";

      String view = iwc.getParameter(closerLookDateParameter);
      if (action.equals(""))
      if (view != null) action = "view";

      if (action.equals("")) {
         table = getContentTable(iwc);
      }else if (action.equals("view")) {
         table = getViewService(iwc);
      }else if (action.equals(this.parameterDeleteBooking)) {
         deleteBooking(iwc);
         table = getViewService(iwc);
      }


      form.add(table);

      Table par = new Table(1,1);
        par.setAlignment(1,1,"right");
        par.setAlignment("center");
        par.setWidth("90%");
        par.add(new PrintButton(iwrb.getImage("buttons/print.gif")),1,1);
        form.add(Text.BREAK);
        form.add(par);


      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  // BUSINESS
  public IWTimestamp getFromIdegaTimestamp(IWContext iwc) {
    IWTimestamp stamp = null;
    String from_time = iwc.getParameter("active_from");
    if (from_time!= null) {
      stamp = new IWTimestamp(from_time);
    } else {
      stamp = IWTimestamp.RightNow();
    }
    return stamp;
  }

  // BUSINESS
  public IWTimestamp getToIdegaTimestamp(IWContext iwc) {
    IWTimestamp stamp = null;
    String from_time = iwc.getParameter("active_to");
    if (from_time!= null) {
      stamp = new IWTimestamp(from_time);
    } else {
      stamp = IWTimestamp.RightNow();
      stamp.addDays(15);
    }
    return stamp;
  }


  public Table getTopTable(IWContext iwc) throws RemoteException{
      Table topTable = new Table(5,3);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = new DropdownMenu();
        if (supplier != null) {
          trip = getProductBusiness(iwc).getDropdownMenuWithProducts(iwc, supplier.getID());
        }else if (reseller != null){
          trip = getContractBusiness(iwc).getDropdownMenuWithProducts(iwc, reseller.getID());
        }

        trip.addMenuElementFirst(parameterViewAll,iwrb.getLocalizedString("travel.all_products","All products"));


          if (product != null) {
              trip.setSelectedElement(Integer.toString(product.getID()));
          }

			IWTimestamp now = IWTimestamp.RightNow();

      DateInput active_from = new DateInput("active_from");
          active_from.setDate(fromStamp.getDate());
          active_from.setYearRange(2001, now.getYear()+4);
      DateInput active_to = new DateInput("active_to");
          active_to.setDate(toStamp.getDate());
					active_to.setYearRange(2001, now.getYear()+4);

      Text tfFromText = (Text) theText.clone();
          tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theText.clone();
          tfToText.setText(iwrb.getLocalizedString("travel.to","to"));


      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.product_name_lg","Name of product"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");
      Text generalOverviewText = (Text) theText.clone();
          generalOverviewText.setText(iwrb.getLocalizedString("travel.general_overview","General overview"));
          generalOverviewText.addToText(":");
      Text inqOnlyText = (Text) theText.clone();
          inqOnlyText.setText(iwrb.getLocalizedString("travel.inqueries_only","Inqueries only"));
          inqOnlyText.addToText(":");

      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(timeframeText,1,2);
      topTable.add(tfFromText,1,2);
      topTable.add(active_from,2,2);
      topTable.add(tfToText,2,2);
      topTable.add(active_to,2,2);
      topTable.mergeCells(2,1,4,1);
      topTable.mergeCells(2,2,4,2);


      topTable.setAlignment(5,2,"right");
      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/get.gif"));
      topTable.add(submit,5,2);

      BusyBar bb = new BusyBar("waiting");
      bb.setInterfaceObject(submit);
//      bb.setWidth("200");
//      bb.setHeight("50");
      topTable.mergeCells(1, 3, 5, 3);
      topTable.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_CENTER);
      topTable.add(bb, 1, 3);



      return topTable;
  }

  public Table getContentHeader(IWContext iwc) {
      Table table = new Table(2,1);
      table.setWidth("95%");

      String mode = iwc.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          if (mode.equals("inqueries_only")) {
              headerText.setText(iwrb.getLocalizedString("travel.inqueries","Inqueries"));
          }else {
              headerText.setText(iwrb.getLocalizedString("travel.overview","Overview"));
          }

      IWCalendar calFrom = new IWCalendar(fromStamp);
      IWCalendar calTo = new IWCalendar(toStamp);

      Text timeText = (Text) theBoldText.clone();
          timeText.setText(calFrom.getLocaleDate()+" - "+calTo.getLocaleDate());

      table.setAlignment(1,1,"left");
      table.add(headerText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeText,2,1);

      return table;
  }

  public Table getContentTable(IWContext iwc) throws Exception {
      Table table = new Table();
      table.setWidth("95%");
      table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
      table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_CENTER);

      if (_productId != -1) {

          int productId = _productId;

          Collection products = null;
          ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
          try {
						if (productId == Integer.parseInt(this.parameterViewAll)) {
		          	if (supplier != null) {
			              products = pHome.findProductsOrderedByProductCategory(supplier.getID(), this.fromStamp, this.toStamp );
		          	}else if (reseller != null) {
			          		Product[] prodArr = getContractBusiness(iwc).getProductsForReseller(iwc, reseller.getID());
			          		products = new Vector();
			          		for ( int i = 0; i < prodArr.length; i++) {
			          			products.add(prodArr[i]);	
			          		}
		          	}
						}else {
							products = new Vector();
							products.add(pHome.findByPrimaryKey(new Integer(productId)));
						}

          }catch (FinderException sql) {
            sql.printStackTrace(System.err);
            products  = new Vector();
          }

          is.idega.idegaweb.travel.service.presentation.BookingOverview bo = null;
          if (product == null) {
            int productsSize = products.size();
            Collection cats;
            Product prod;
            ProductCategoryHome pCatHome = (ProductCategoryHome) IDOLookup.getHome(ProductCategory.class);
            Collection tempProducts = new Vector();
            int newCat = -1;
            int oldCat = -1;
            String catName = "";
            String oldCatName = "";
            boolean getTable = false;

            Iterator iter = products.iterator();
            while (iter.hasNext()) {
              getTable = false;
              prod = (Product) iter.next();
              cats = super.getProductCategoryFactory(iwc).getProductCategory(prod);
              Iterator iter2 = cats.iterator();
              if (iter2.hasNext()) {
                ProductCategory pCat = (ProductCategory) iter2.next();
                catName = pCat.getName();
                newCat = pCat.getID();
                if ( newCat != oldCat ) {
                  if (oldCat != -1) {
                    table.add(getHeaderText(getProductCategoryFactory(iwc).getProductCategoryTypeDefaultName(oldCatName)));
                    table.addBreak();
                    table.add(bo.getBookingOverviewTable(iwc, tempProducts));
                    table.addBreak();
                    table.addBreak();
                    tempProducts = new Vector();
//                    getTable = true;
                  }
                  bo = super.getServiceHandler(iwc).getBookingOverview(iwc, prod);
                }
                oldCat = newCat;
                oldCatName = catName;

                if (!iter.hasNext()){
                  getTable = true;
                }
                tempProducts.add(prod);
              }
              if (getTable) {
                table.add(getHeaderText(getProductCategoryFactory(iwc).getProductCategoryTypeDefaultName(catName)));
                table.add(bo.getBookingOverviewTable(iwc, tempProducts));
                tempProducts = new Vector();
              }
            }
          }else {
            bo = super.getServiceHandler(iwc).getBookingOverview(iwc, product);
            table = bo.getBookingOverviewTable(iwc, products);
          }


      }
      else {
        table.add(super.getHeaderText(iwrb.getLocalizedString("travel.please_select_a_product","Please select a product")));
      }
      return table;

  }



  public Table getViewService(IWContext iwc) throws Exception {
    String view_date = iwc.getParameter(this.closerLookDateParameter);

    IWTimestamp stamp = new IWTimestamp(view_date);
    is.idega.idegaweb.travel.service.presentation.BookingOverview bo = super.getServiceHandler(iwc).getBookingOverview(iwc, product);
    return bo.getDetailedInfo(iwc, product, stamp);

  }

  public void deleteBooking(IWContext iwc) throws RemoteException, FinderException{
    String lBookingId = iwc.getParameter(this.parameterBookingId);
    if (lBookingId != null) {
      getBooker(iwc).deleteBooking(Integer.parseInt(lBookingId));
    }
  }

  private TourBusiness getTourBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TourBusiness) IBOLookup.getServiceInstance(iwac, TourBusiness.class);
  }
}
