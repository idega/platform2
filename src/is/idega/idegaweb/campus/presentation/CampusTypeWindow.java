package is.idega.idegaweb.campus.presentation;

import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.data.Tariff;
import com.idega.block.building.presentation.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.ui.Window;
import com.idega.util.Property;
import com.idega.util.PropertyList;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.idegaweb.IWResourceBundle;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusTypeWindow extends Window {

  public CampusTypeWindow() {
    setHeight(515);
    setWidth(420);
    //setResizable(true);
  }

  public String getBundleIdentifier(){
    return Campus.CAMPUS_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception{
    IWResourceBundle iwrb = getResourceBundle(iwc);
    int id = Integer.parseInt(iwc.getParameter(ApartmentTypeViewer.PARAMETER_STRING));
    ApartmentTypeViewer BE = new ApartmentTypeViewer(id);
    String attributeName = iwrb.getLocalizedString("tariffs","Tariffs");
    String today = com.idega.util.IWTimestamp.RightNow().getLocaleDate(iwc.getCurrentLocale());
    java.util.Collection typeTariffs = FinanceFinder.getInstance().getKeySortedTariffsByAttribute("t_"+id);
    if(typeTariffs !=null){
      NumberFormat format = DecimalFormat.getCurrencyInstance(iwc.getApplication().getSettings().getDefaultLocale());
      //  String rentString = format.format((long)room.getRent());
    	ArrayList list = new ArrayList();
   
      java.util.Iterator iter= typeTariffs.iterator();
      float total = 0;
      while(iter.hasNext()){
        Tariff tariff = (Tariff) iter.next();
        list.add(new Property(tariff.getName(),format.format(tariff.getPrice())));
        total += tariff.getPrice();
      }
      if(total > 0){
      	list.add(new Property("-----------","------------"));
        String sTotalName = iwrb.getLocalizedString("total","Total");
        String sTotalValue = format.format(total);
        list.add(new Property(sTotalName,sTotalValue));
        BE.setSpecialAttributes(attributeName+"  "+today,list);
      }
    }

    add(BE);
    setTitle("Rugl Viewer");
    //addTitle("Building Editor");
  }
}
