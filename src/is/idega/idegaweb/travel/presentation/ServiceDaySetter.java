package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.text.Text;
import javax.ejb.FinderException;
import com.idega.util.idegaCalendar;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.travel.data.*;
import java.util.Collection;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.data.IDOLookup;
import com.idega.block.trade.stockroom.business.ProductBusiness;
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
  private String PARAMETER_AVAILABLE = "sbs_avail_";
  private String PARAMETER_MAX = "sbs_max_";
  private String PARAMETER_MIN = "sbs_min_";
  private String PARAMETER_ESTIMATED = "sbs_estimated_";

  private IWResourceBundle _iwrb;
  private int _localeId;
  private Product _product;
  private ServiceDayHome _serviceDayHome;
  private idegaCalendar _cal;
//  private Service _service;
  private Integer[] _serviceDayIds;


  public ServiceDaySetter() {
    super.setWidth(600);
    super.setHeight(500);
    super.setTitle("idegaWeb Travel");
    super.setStatus(true);
  }

  public void main(IWContext iwc) {
    super.main(iwc);
    init(iwc);

    if (_product != null) {
      drawForm(iwc);
    }else {
      noProduct();
    }
  }

  private void init(IWContext iwc) {
    _iwrb = super.getResourceBundle(iwc);
    _localeId = iwc.getCurrentLocaleId();
    _cal = new idegaCalendar();
    String serviceId = iwc.getParameter(PARAMETER_SERVICE_ID);

    try {
      if (serviceId != null) {
        ProductHome productHome = (ProductHome)IDOLookup.getHome(Product.class);
//        ServiceHome serviceHome = (ServiceHome)IDOLookup.getHome(Service.class);
        _serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);

        _product = productHome.findByPrimaryKey(Integer.parseInt(serviceId));
//        _service = serviceHome.findByPrimaryKey(Integer.parseInt(serviceId));

        ServiceDay sDay = _serviceDayHome.create();
        Collection sDays = sDay.getServiceDays(Integer.parseInt(serviceId));
        _serviceDayIds = (Integer[]) sDays.toArray(new Integer[]{});
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void noProduct() {
    add(getText(_iwrb.getLocalizedString("travel.no_product_selected","No product selected")));
  }

  private void drawForm(IWContext iwc) {
    Form form = new Form();
    Table table = TravelManager.getTable();
    form.add(table);

//    table.add(getText(ProductBusiness.getProductNameWithNumber(_product, true, _localeId)));
    int row = 1;
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.day","Day")), 1, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.available","Available")), 2, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.max","Max")), 3, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.min","Min")), 4, row);
    table.add(getTextHeader(_iwrb.getLocalizedString("travel.estimated","Estimated")), 5, row);
    table.setRowColor(row, TravelManager.backgroundColor);


    int arrayIndex = 0;
    if (_serviceDayIds.length == 0) {
      arrayIndex = -1;
    }
    ServiceDay serviceDay;
    CheckBox avail;
    TextInput max;
    TextInput min;
    TextInput estimated;

    for (int i = ServiceDayBMPBean.SUNDAY; i <= ServiceDayBMPBean.SATURDAY; i++) {
      ++row;

      avail = new CheckBox(PARAMETER_AVAILABLE+i);
      max = new TextInput(PARAMETER_MAX+i);
      min = new TextInput(PARAMETER_MIN+i);
      estimated = new TextInput(PARAMETER_ESTIMATED+i);

      debug("arrayIndex = "+arrayIndex);

      try {
        serviceDay = _serviceDayHome.findByPrimaryKey(_serviceDayIds[arrayIndex]);
        if ( arrayIndex != -1 && serviceDay.getDayOfWeek() == i) {
          debug("weeee : "+i);
            avail.setChecked(true);
            max.setContent(Integer.toString(serviceDay.getMax()));
            min.setContent(Integer.toString(serviceDay.getMin()));
            estimated.setContent(Integer.toString(serviceDay.getEstimated()));

            if ( ++arrayIndex == _serviceDayIds.length) {
              debug(arrayIndex +" == "+_serviceDayIds.length);
              arrayIndex = -1;
            }
        } else {
          debug("doooh : "+i);
          debug("...id : "+serviceDay.getDayOfWeek());
        }
      }catch (FinderException fe) {
        fe.printStackTrace(System.err);
      }



      table.add(getText(_cal.getNameOfDay(i, iwc)), 1, row);
      table.add(avail, 2, row);
      table.add(max, 3, row);
      table.add(min, 4, row);
      table.add(estimated, 5, row);

      table.setRowColor(row, TravelManager.GRAY);
    }



    table.setAlignment("center");
    add(Text.NON_BREAKING_SPACE);
    add(form);
  }

}