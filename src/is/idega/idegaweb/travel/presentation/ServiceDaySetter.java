package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import com.idega.presentation.text.Text;
import com.idega.util.IWCalendar;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.data.IDOLookup;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceDaySetter extends TravelWindow {
  public static final String PARAMETER_SERVICE_ID = "sds_serv_id";
  private String ACTION = "sbs_action";
  private String PARAMETER_UPDATE = "sbs_update";
  private String PARAMETER_AVAILABLE = "sbs_avail_";
  private String PARAMETER_MAX = "sbs_max_";
  private String PARAMETER_MIN = "sbs_min_";
  private String PARAMETER_ESTIMATED = "sbs_estimated_";


  private IWResourceBundle _iwrb;
  private int _localeId;
  private Product _product;
  private ServiceDayHome _serviceDayHome;
  private IWCalendar _cal;
  private ServiceDay[] _serviceDays;
  private int _textInputSize = 5;


  public ServiceDaySetter() {
    super.setWidth(350);
    super.setHeight(330);
    super.setTitle("idegaWeb Travel");
    super.setStatus(true);
    super.setResizable(true);
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);

    if (_product != null) {
      String action = iwc.getParameter(ACTION);
      if (action != null && action.equals(PARAMETER_UPDATE)) {
        executeUpdate(iwc);
      }
      drawForm(iwc);
    }else {
      noProduct();
    }
  }

  private void init(IWContext iwc) {
    _iwrb = super.getResourceBundle(iwc);
    _localeId = iwc.getCurrentLocaleId();
    _cal = new IWCalendar();
    String serviceId = iwc.getParameter(PARAMETER_SERVICE_ID);

    try {
      if (serviceId != null) {
        ProductHome productHome = (ProductHome)IDOLookup.getHome(Product.class);
        _serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);

        _product = productHome.findByPrimaryKey(new Integer(serviceId));

        ServiceDay sDay = _serviceDayHome.create();
        _serviceDays = sDay.getServiceDaysOfWeek(Integer.parseInt(serviceId));
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void executeUpdate(IWContext iwc) {
    String avail;
    String max;
    String min;
    String est;
    int iMax;
    int iMin;
    int iEst;

    try {
      ServiceDay sDay = _serviceDayHome.create();
        _serviceDayHome.setServiceWithNoDays(_product.getID());
        sDay.store();

      for (int i = ServiceDayBMPBean.SUNDAY; i <= ServiceDayBMPBean.SATURDAY; i++) {
        avail = iwc.getParameter(PARAMETER_AVAILABLE+i);
        max = iwc.getParameter(PARAMETER_MAX+i);
        min = iwc.getParameter(PARAMETER_MIN+i);
        est = iwc.getParameter(PARAMETER_ESTIMATED+i);
        try {
          iMax = Integer.parseInt(max);
        }catch (NumberFormatException n) {
          iMax = -1;
        }
        try {
          iMin = Integer.parseInt(min);
        }catch (NumberFormatException n) {
          iMin = -1;
        }
        try {
          iEst = Integer.parseInt(est);
        }catch (NumberFormatException n) {
          iEst = -1;
        }

        if (avail != null) {
          sDay = _serviceDayHome.create();
          sDay.setDayOfWeek(_product.getID(), i, iMax, iMin, iEst);
          sDay.store();
        }
      }

      _serviceDays = sDay.getServiceDaysOfWeek(_product.getID());

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void noProduct() {
    add(getText(_iwrb.getLocalizedString("travel.no_product_selected","No product selected")));
  }

  private void drawForm(IWContext iwc) throws RemoteException{
    Form form = new Form();
    Table table = TravelManager.getTable();
    form.add(table);
    form.maintainParameter(PARAMETER_SERVICE_ID);

    int row = 1;
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.day","Day")), 1, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.available","Available")), 2, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.max","Max")), 3, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.min","Min")), 4, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.estimated","Estimated")), 5, row);
    table.setRowColor(row, TravelManager.backgroundColor);


    int arrayIndex = 0;
    if (_serviceDays.length == 0) {
      arrayIndex = -1;
    }

    CheckBox avail;
    TextInput max;
    TextInput min;
    TextInput estimated;
    int iMax = -1;
    int iMin = -1;
    int iEst = -1;

    for (int i = ServiceDayBMPBean.SUNDAY; i <= ServiceDayBMPBean.SATURDAY; i++) {
      ++row;

      avail = new CheckBox(PARAMETER_AVAILABLE+i);
      max = new TextInput(PARAMETER_MAX+i);
      min = new TextInput(PARAMETER_MIN+i);
      estimated = new TextInput(PARAMETER_ESTIMATED+i);
        max.setSize(_textInputSize);
        min.setSize(_textInputSize);
        estimated.setSize(_textInputSize);

      if ( arrayIndex != -1 && _serviceDays[arrayIndex].getDayOfWeek() == i) {
          avail.setChecked(true);
          iMax = _serviceDays[arrayIndex].getMax();
          iMin = _serviceDays[arrayIndex].getMin();
          iEst = _serviceDays[arrayIndex].getEstimated();
          if (iMax != -1) {
            max.setContent(Integer.toString(iMax));
          }
          if (iMin != -1) {
            min.setContent(Integer.toString(iMin));
          }
          if (iEst != -1) {
            estimated.setContent(Integer.toString(iEst));
          }

          if ( ++arrayIndex == _serviceDays.length) {
            arrayIndex = -1;
          }
      }



      table.add(getText(_cal.getNameOfDay(i, iwc.getCurrentLocale())), 1, row);
      table.add(avail, 2, row);
      table.add(max, 3, row);
      table.add(min, 4, row);
      table.add(estimated, 5, row);

      table.setRowColor(row, TravelManager.GRAY);
    }

    ++row;
    table.mergeCells(1, row, 5, row);
    table.setRowColor(row, TravelManager.GRAY);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.add(new SubmitButton(_iwrb.getLocalizedImageButton("travel.update","Update"), ACTION, PARAMETER_UPDATE), 1, row);



    table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
    table.setAlignment("center");
    add(Text.NON_BREAKING_SPACE);
    add(form);
  }

}