package is.idega.idegaweb.travel.presentation;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.data.IDOLookup;
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
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is>Grímur Jónsson</a>
 * @version 1.0
 */

public class AdministratorReports extends Reports {

  private Supplier _supplier;
  private List _usedSuppliers;
  private List _allSuppliers;
  protected AdministratorReport _report;
  private static final String PARAMETER_ONLINE_REPORT = OnlineBookingReport.class.toString();//"adRep_or";
  private static final String PARAMETER_REFUND_REPORT = RefundReport.class.toString();
  private String PARAMETER_SUPPLIER_ID = "adRep_spID";
  public static final String PARAMETER_SUPPLIER_ID_STATIC = "adRep_stSpId";

  public AdministratorReports() {
  }

  public void main(IWContext iwc) throws Exception {
//    super.main(iwc);
    init(iwc);

    if (super.isLoggedOn(iwc)) {

      add(Text.BREAK);
      String _action = iwc.getParameter(ACTION);
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

      Table table = new Table();
//      table.setWidth("90%");
      table.add(getBackLink());
      add(table);
    } else {
      add(super.getLoggedOffTable(iwc));
    }

  }

  protected void init(IWContext iwc) throws Exception {
    super.init(iwc);

    String action = iwc.getParameter(ACTION);
    if (action == null) action = "";
    
    if (action.equals(PARAMETER_ONLINE_REPORT) && _report == null) {
      _report = new OnlineBookingReport(iwc);
    } else if (action.equals(PARAMETER_REFUND_REPORT) && _report == null) {
    		_report = new RefundReport(iwc);
    }

    String suppId = iwc.getParameter(PARAMETER_SUPPLIER_ID);
    if (suppId != null && !suppId.equals("-1")) {
      SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
      _supplier = suppHome.findByPrimaryKey(new Integer(suppId));
      _usedSuppliers = new Vector();
      _usedSuppliers.add(_supplier);
    }else if (_allSuppliers == null && super.isSupplierManager()) {
    	SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
    	_usedSuppliers = (List) suppHome.findAll(getSupplierManager()); 
    }else { //if (suppId != null && suppId.equals("-1") && _allSuppliers != null) {
    	_usedSuppliers = _allSuppliers;
    } /*else {
			_usedSuppliers = new Vector();
		  Supplier[] supps = new Supplier[]{};
		  try {
				supps = com.idega.block.trade.stockroom.data.SupplierBMPBean.getValidSuppliers();
				for (int i = 0; i < supps.length; i++) {
					_usedSuppliers.add(supps[i]);
				}
		  }catch (SQLException sql) {
			sql.printStackTrace(System.out);
	  }
    	
    }*/

  }

  public Table topTable(IWContext iwc) {
      Table topTable = new Table(5,3);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text tframeText = (Text) theText.clone();
          tframeText.setText(_iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");


      Supplier[] supps = getSuppliers();
      DropdownMenu trip = new DropdownMenu(supps, PARAMETER_SUPPLIER_ID);

//        trip = ProductBusiness.getDropdownMenuWithProducts(iwc, _supplier.getID(), PARAMETER_PRODUCT_ID);
        if (_supplier != null) {
            trip.setSelectedElement(Integer.toString(_supplier.getID()));
        } else if (iwc.isParameterSet(PARAMETER_SUPPLIER_ID_STATIC)){
          trip.setSelectedElement(iwc.getParameter(PARAMETER_SUPPLIER_ID_STATIC));
        }
        trip.addMenuElementFirst("-1", _iwrb.getLocalizedString("travel.all_suppliers","All suppliers"));

			IWTimestamp now = IWTimestamp.RightNow();

      DateInput active_from = new DateInput(PARAMATER_DATE_FROM);
          active_from.setDate(_stamp.getSQLDate());
					active_from.setYearRange(2001, now.getYear()+4);

      DateInput active_to = new DateInput(PARAMATER_DATE_TO);
          active_to.setDate(_toStamp.getSQLDate());
					active_to.setYearRange(2001, now.getYear()+4);


      Text nameText = (Text) theText.clone();
          nameText.setText(_iwrb.getLocalizedString("travel.supplier","Supplier"));
          nameText.addToText(":");
      Text timeframeFromText = (Text) theText.clone();
          timeframeFromText.setText(_iwrb.getLocalizedString("travel.from","From"));
          timeframeFromText.addToText(":");
      Text timeframeToText = (Text) theText.clone();
          timeframeToText.setText(_iwrb.getLocalizedString("travel.to","To"));
          timeframeToText.addToText(":");



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


protected Supplier[] getSuppliers() {
	if (this._allSuppliers != null) {
		return (Supplier[]) _allSuppliers.toArray(new Supplier[]{});
	} else {
		Supplier[] supps = new Supplier[]{};
	  try {
			SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			Collection coll = suppHome.findAll(getSupplierManager());
			if (coll != null) {
				supps = (Supplier[]) coll.toArray(new Supplier[]{});
			}
//	    supps = com.idega.block.trade.stockroom.data.SupplierBMPBean.getValidSuppliers();
	  }catch (Exception sql) {
	    sql.printStackTrace(System.out);
	  }
	  return supps;
	}
}

protected void reportList(IWContext iwc) throws Exception {
    Table table = super.getTable();

    OnlineBookingReport obr = new OnlineBookingReport(iwc);
    RefundReport rr = new RefundReport(iwc);

    int row = 1;
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.report","Report")), 1, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.description","Description")), 2, row);
    table.setRowColor(row, super.backgroundColor);


    ++row;
    Link obrLink = new Link(obr.getReportName());
      obrLink.addParameter(ACTION, PARAMETER_ONLINE_REPORT);
    table.add(obrLink, 1, row);
    table.add(getText(obr.getReportDescription()), 2, row);
    table.setRowColor(row, super.GRAY);

    ++row;
    Link rrLink = new Link(rr.getReportName());
      rrLink.addParameter(ACTION, PARAMETER_REFUND_REPORT);
    table.add(rrLink, 1, row);
    table.add(getText(rr.getReportDescription()), 2, row);
    table.setRowColor(row, super.GRAY);

    add(table);
  }


  public Table report(IWContext iwc) throws Exception{
    Table table = new Table();
      table.setWidth("90%");
      table.setAlignment("center");
      table.setCellpaddingAndCellspacing(0);


    if (_report.useTwoDates()) {
      table.add(_report.getAdministratorReport(_usedSuppliers, iwc, _stamp, _toStamp));
    }else {
      table.add(_report.getAdministratorReport(_usedSuppliers, iwc, _stamp));
    }

    return table;
  }
  
  public void setSuppliers(Collection suppliers) {
  	this._allSuppliers = (Vector) suppliers;
  }
  
  public void setReport(AdministratorReport report) {
  	this._report = report;
  }
}