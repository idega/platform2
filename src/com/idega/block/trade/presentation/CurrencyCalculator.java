package com.idega.block.trade.presentation;

import java.util.Iterator;
import java.util.List;

import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.text.TextSoap;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CurrencyCalculator extends PresentationObjectContainer {

  private String parameterFrom = "from";
  private String parameterTo = "to";
  private String parameterPrice = "price";
  private String parameterAll = "ALL";
  private DropdownMenu from = new DropdownMenu(parameterFrom);
  private DropdownMenu to   = new DropdownMenu(parameterTo);

  private IWResourceBundle iwrb;
  private IWBundle bundle;

  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";

  public CurrencyCalculator() {
  }

  public void main(IWContext iwc) {
    init(iwc);
    displayForm(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);

    List currencyList = CurrencyBusiness.getCurrencyList();
    Iterator iter = currencyList.iterator();
    while (iter.hasNext()) {
      CurrencyHolder holder = (CurrencyHolder) iter.next();
      from.addMenuElement(holder.getCurrencyName(), holder.getCurrencyName());
      to.addMenuElement(holder.getCurrencyName(), holder.getCurrencyName());
    }
    from.setSelectedElement(CurrencyBusiness.defaultCurrency);
    to.addMenuElement(this.parameterAll,"ALL");
  }

  private void displayForm(IWContext iwc) {
    Form form = new Form();
    Table table = new Table();
      form.add(table);
      table.setAlignment("center");


    String sFrom = iwc.getParameter(this.parameterFrom);
    String sTo = iwc.getParameter(this.parameterTo);
    String sPrice = iwc.getParameter(this.parameterPrice);
    if (sPrice != null)
    sPrice = TextSoap.findAndReplace(sPrice, ',', '.');

    if (sTo == null || !sTo.equals(this.parameterAll)) {

      TextInput price = new TextInput(parameterPrice);
        price.setSize(15);
      if (sPrice != null) price.setContent(sPrice);
      if (sTo != null) to.setSelectedElement(sTo);
      if (sFrom != null) from.setSelectedElement(sFrom);

      table.add(getText(iwrb.getLocalizedString("from", "From")), 1,1);
      table.add(getText(iwrb.getLocalizedString("to","To")), 2,1);
      table.add(getText(iwrb.getLocalizedString("price","Price")), 3,1);
      table.add(getText(iwrb.getLocalizedString("new_price","New price")), 4,1);
      table.add(from, 1,2);
      table.add(to, 2,2);
      table.add(price, 3,2);

      table.add(getText(getNewPrice(iwc)), 4, 2);
      table.setAlignment(4,2,"right");

      table.add(new SubmitButton(iwrb.getLocalizedImageButton("get", "Get")), 4, 3);

      add(form);
    }else {
      List list = CurrencyBusiness.getCurrencyList();
      if (list != null) {
        int row = 1;
        table.add(getText(iwrb.getLocalizedString("from", "From")), 1,1);
        table.add(getText(iwrb.getLocalizedString("to","To")), 3,1);
        CurrencyHolder holder;
        for (int i = 0; i < list.size(); i++) {
          ++row;
          holder = (CurrencyHolder) list.get(i);
          table.add(sPrice+Text.NON_BREAKING_SPACE+sFrom, 1,row);
          table.add(getText(" = "), 2,row);
          try {
            table.add(getText(TextSoap.decimalFormat(Float.toString(CurrencyBusiness.convertCurrency(sFrom, holder.getCurrencyName(), Float.parseFloat(sPrice))), 2)+Text.NON_BREAKING_SPACE+holder.getCurrencyName()), 3,row);
          }catch (NumberFormatException n) {
            table.add(getText("- "+holder.getCurrencyName()),3, row);
          }
        }
        ++row;
        table.mergeCells(1, row, 3 ,row);
        table.setAlignment(1, row, "center");
        table.add(new BackButton(iwrb.getLocalizedImageButton("back", "Back")), 1, row);
      }
      add(table);
    }
    add(Text.BREAK);
    add(getDisclamer(iwc));
  }

  private Table getDisclamer(IWContext iwc) {
    Table table = new Table();
      table.setAlignment(1,1,"center");
      table.setAlignment("center");
      /*table.add("Gögnin eru fengin frá heimasiðu Seðlabankans");
      try {
        CurrencyHolder holder = CurrencyBusiness.getCurrencyHolder(CurrencyBusiness.defaultCurrency);
        table.add(holder.getTimestamp().toSQLString());
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
      table.add(Text.BREAK);*/
      table.add(iwrb.getLocalizedString("displayed_without_guarantee","Displayed without guarantee"));

    return table;
  }

  private String getNewPrice(IWContext iwc) {
    String price = "";

    try {
      String sFrom = iwc.getParameter(this.parameterFrom);
      String sTo = iwc.getParameter(this.parameterTo);
      String sPrice = iwc.getParameter(this.parameterPrice);

      if (sFrom != null && sTo != null && sPrice != null && !sPrice.equals("")) {
        sPrice = TextSoap.findAndReplace(sPrice, ',', '.');
        price = TextSoap.decimalFormat(Float.toString(CurrencyBusiness.convertCurrency(sFrom, sTo, Float.parseFloat(sPrice))), 2)+Text.NON_BREAKING_SPACE+sTo;
      }

    }catch (NumberFormatException n) {
      price = "-";
    }
    return price;
  }

  private Text getText(String content) {
    Text text = new Text(content);
      text.setBold(true);
    return text;
  }

}
