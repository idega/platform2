package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Reports extends TravelManager {

  protected Report _report;
  protected IWResourceBundle _iwrb;
  private Supplier _supplier;
  private Reseller _reseller;
  private Product _product;
  private List _products;
  protected IWTimestamp _stamp;
  protected IWTimestamp _toStamp;
  protected static Link _link;
  protected static Form _form;

  private String _action;
  private String PARAMETER_DAILY_REPORT = DailyReport.class.toString();//"reps_day_rep";
  private String PARAMETER_PICKUP_REPORT = HotelPickupReporter.class.toString();//"reps_pic_rep";
  private String PARAMETER_USER_REPORT = UserBookingReporter.class.toString();//"reps_usr_rep";
  private String PARAMETER_ONLINE_REPORT = OnlineBookingReport.class.toString();//"reps_onl_rep";
  public static String PARAMETER_PRODUCT_ID = "reps_prd_id";

  protected static final String ACTION = "reps_act";
  protected static final String PARAMATER_DATE_FROM = "active_from";
  protected static final String PARAMATER_DATE_TO = "active_to";

  protected static List parametersToMaintain = new Vector();
  
  public Reports() {
  }

  public void main(IWContext iwc) throws Exception {
    init(iwc);

    add(Text.BREAK);

    if (super.isLoggedOn(iwc)) {
      if (_action == null && _report == null) {
        reportList(iwc);
      }else {
        Form form = new Form();
          form.maintainParameter(this.ACTION);
					if (parametersToMaintain != null && !parametersToMaintain.isEmpty()) {
			      Iterator iter = parametersToMaintain.iterator();
			      Parameter p;
			      while (iter.hasNext()) {
			      	p = (Parameter) iter.next();
			      	form.addParameter(p.getName(), p.getValueAsString());
			      }
					}
          
        form.add(topTable(iwc));
        form.add(report(iwc));
        form.add(Text.BREAK);
        add(form);
      }
    }else {
      add(super.getLoggedOffTable(iwc));
    }
  }

  protected void init(IWContext iwc) throws Exception {
    super.main(iwc);
    _iwrb = super.getResourceBundle();
    _supplier = super.getSupplier();
    _reseller = super.getReseller();

    try {
      String productId = iwc.getParameter(PARAMETER_PRODUCT_ID);
      ProductHome phome = (ProductHome) IDOLookup.getHomeLegacy(Product.class);
      if (productId != null && !productId.equals("-1")) {
        _product = getProductBusiness(iwc).getProduct(Integer.parseInt(productId)); //phome.findByPrimaryKey(Integer.parseInt(productId));
        _products = new Vector();
          _products.add(_product);
      }else if (productId == null || productId.equals("-1")) {
        _products = new Vector();
        if (_supplier != null) {
          _products = getProductBusiness(iwc).getProducts(iwc, _supplier.getID());
        }else if (_reseller != null) {
          /** @todo laga kannski til */
          Product[] repps = getContractBusiness(iwc).getProductsForReseller(iwc, _reseller.getID());
          for (int i = 0; i < repps.length; i++) {
            _products.add(repps[i]);
          }
        }
      }
      if (_products == null) {
        _products = new Vector();
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }


    _action = iwc.getParameter(this.ACTION);

    /** @todo hmmm er haegt ad fordast thetta check her ??? */
    if (_action != null && _report == null) {
      if (_action.equals(PARAMETER_USER_REPORT)) {
        _report = new UserBookingReporter(iwc);
      }else if (_action.equals(PARAMETER_PICKUP_REPORT)) {
        _report = new HotelPickupReporter(iwc);
      }else if (_action.equals(PARAMETER_DAILY_REPORT)) {
        _report = new DailyReport(iwc);
      }else if (_action.equals(PARAMETER_ONLINE_REPORT)) {
        _report = new OnlineBookingReport(iwc);
      }
      //_report = (Report) Class.forName(_action).newInstance();

    }


    String from_time = iwc.getParameter(PARAMATER_DATE_FROM);
    if (from_time != null) {
      _stamp = new IWTimestamp(from_time);
    }
    else {
      _stamp = IWTimestamp.RightNow();
    }

    String to_time = iwc.getParameter(PARAMATER_DATE_TO);
    if (to_time != null) {
      _toStamp = new IWTimestamp(to_time);
    }else {
      _toStamp = new IWTimestamp(_stamp);
      _toStamp.addDays(14);
    }
  }

  protected void reportList(IWContext iwc) throws Exception {
    Table table = super.getTable();

    UserBookingReporter ubr = new UserBookingReporter(iwc);
    HotelPickupReporter hpr = new HotelPickupReporter(iwc);
    DailyReport dr = new DailyReport(iwc);
    OnlineBookingReport obr = new OnlineBookingReport(iwc);

    int row = 1;
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.report","Report")), 1, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.description","Description")), 2, row);
    table.setRowColor(row, super.backgroundColor);

//    if (this._supplier != null) {
      ++row;
      Link ubrLink = new Link(getText(ubr.getReportName()));
        ubrLink.addParameter(ACTION, PARAMETER_USER_REPORT);
      table.add(ubrLink, 1, row);
      table.add(getText(ubr.getReportDescription()), 2, row);
      table.setRowColor(row, super.GRAY);

      ++row;
      Link hprLink = new Link(getText(hpr.getReportName()));
        hprLink.addParameter(ACTION, PARAMETER_PICKUP_REPORT);
      table.add(hprLink, 1, row);
      table.add(getText(hpr.getReportDescription()), 2, row);
      table.setRowColor(row, super.GRAY);

      ++row;
      Link drLink = new Link(getText(dr.getReportName()));
        drLink.addParameter(ACTION, PARAMETER_DAILY_REPORT);
      table.add(drLink, 1, row);
      table.add(getText(dr.getReportDescription()), 2, row);
      table.setRowColor(row, super.GRAY);
//    }

    ++row;
    Link obrLink = new Link(getText(obr.getReportName()));
      obrLink.addParameter(ACTION, PARAMETER_ONLINE_REPORT);
    table.add(obrLink, 1, row);
    table.add(getText(obr.getReportDescription()), 2, row);
    table.setRowColor(row, super.GRAY);

    add(table);
  }


  public Table topTable(IWContext iwc) throws RemoteException{
      Table topTable = new Table(5,3);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text tframeText = (Text) theText.clone();
          tframeText.setText(_iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = null;
      if (_supplier == null) {
      	trip = getProductBusiness(iwc).getDropdownMenuWithProducts(iwc, _products, PARAMETER_PRODUCT_ID);
      } else {
        trip = getProductBusiness(iwc).getDropdownMenuWithProducts(iwc, _supplier.getID(), PARAMETER_PRODUCT_ID);
      }
        if (_product != null) {
            trip.setSelectedElement(Integer.toString(_product.getID()));
        }
        trip.addMenuElementFirst("-1", _iwrb.getLocalizedString("travel.all_services","All services"));


			IWTimestamp now = IWTimestamp.RightNow();

      DateInput active_from = new DateInput(PARAMATER_DATE_FROM);
          active_from.setDate(_stamp.getSQLDate());
					active_from.setYearRange(2001, now.getYear()+4);

      DateInput active_to = new DateInput(PARAMATER_DATE_TO);
          active_to.setDate(_toStamp.getSQLDate());
					active_to.setYearRange(2001, now.getYear()+4);


      Text nameText = (Text) theText.clone();
          nameText.setText(_iwrb.getLocalizedString("travel.product_name_lg","Name of product"));
          nameText.addToText(":");
      Text timeframeFromText = (Text) theText.clone();
          timeframeFromText.setText(_iwrb.getLocalizedString("travel.from","From"));
          timeframeFromText.addToText(":");
      Text timeframeToText = (Text) theText.clone();
          timeframeToText.setText(_iwrb.getLocalizedString("travel.to","To"));
          timeframeToText.addToText(":");



      Text bookingReportText = (Text) theText.clone();
          bookingReportText.setText(_iwrb.getLocalizedString("travel.booking_report","Booking Report"));
          bookingReportText.addToText(":");

      Text hotelPickupPlaceReportText = (Text) theText.clone();
          hotelPickupPlaceReportText.setText(_iwrb.getLocalizedString("travel.hotel_pickup_list","Hotel pick-up list"));
          hotelPickupPlaceReportText.addToText(": ");

      Text userReportText = (Text) theText.clone();
          userReportText.setText(_iwrb.getLocalizedString("travel.user_report","User report"));
          userReportText.addToText(": ");

      topTable.setAlignment(1,1, "right");
      topTable.setAlignment(2,1, "left");
      topTable.setAlignment(3,1, "right");
      topTable.setAlignment(4,1, "left");
      topTable.setAlignment(3,2, "right");
      topTable.setAlignment(4,2, "left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(timeframeFromText,3,1);
      topTable.add(active_from,4,1);

      if (_report != null && _report.useTwoDates()) {
        topTable.add(timeframeToText,3,2);
        topTable.add(active_to,4,2);
      }

      topTable.mergeCells(1,2,2,2);

      topTable.setAlignment(1,3,"left");
      topTable.setAlignment(5,3,"right");
      topTable.add(new SubmitButton(_iwrb.getImage("/buttons/get.gif")),5,3);

      return topTable;
  }

  public Table report(IWContext iwc) throws Exception{
    Table table = new Table();
      table.setWidth("90%");
      table.setAlignment("center");
      table.setCellpaddingAndCellspacing(0);


    if (_report.useTwoDates()) {
      table.add(_report.getReport(iwc, _products, _stamp, _toStamp));
    }else {
      table.add(_report.getReport(iwc, _products, _stamp));
    }

    return table;
  }

  public static Form getReportForm() {
    if (_form == null) {
      Form form = new Form();
      form.maintainParameter(ACTION);
      form.maintainParameter(PARAMATER_DATE_FROM);
      form.maintainParameter(PARAMATER_DATE_TO);
		  if (parametersToMaintain != null && !parametersToMaintain.isEmpty()) {
				Iterator iter = parametersToMaintain.iterator();
				Parameter p;
				while (iter.hasNext()) {
				  p = (Parameter) iter.next();
				  form.maintainParameter(p.getName());
				}
		  }
    }
    return (Form) _form.clone();
  }

  public static Link getReportLink(Text text) {
    Link link = getReportLink();
      link.setText(text);
    return link;
  }

  public static Link getReportLink() {
    if (_link == null) {
      _link = new Link();
      _link.setToMaintainParameter(ACTION, true);
      _link.setToMaintainParameter(PARAMATER_DATE_FROM, true);
      _link.setToMaintainParameter(PARAMATER_DATE_TO, true);
		  if (parametersToMaintain != null && !parametersToMaintain.isEmpty()) {
				Iterator iter = parametersToMaintain.iterator();
				Parameter p;
				while (iter.hasNext()) {
				  p = (Parameter) iter.next();
				  _link.setToMaintainParameter(p.getName(), true);
				}
		  }
    }
    return (Link) _link.clone();
  }
  
  public void setReport(Report report) {
  	this._report = report;
  }
  
  public void setProducts(List products) {
  	this._products = products;
  }
  
  public void maintainParameter(String name, String value) {
  	Parameter p = new Parameter(name, value);
  	parametersToMaintain.add(p);
  }
  
}