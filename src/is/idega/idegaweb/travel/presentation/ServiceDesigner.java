package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.text.data.TxText;
import com.idega.util.idegaTimestamp;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import com.idega.util.idegaCalendar;
import com.idega.util.text.TextSoap;
import com.idega.core.accesscontrol.business.AccessControl;
import java.sql.SQLException;
import java.text.DecimalFormat;

import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.service.tour.presentation.TourDesigner;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceDesigner extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
  private Supplier supplier;
  private Service service;

  public static String ServiceAction = "service_action";
  private String PriceCategoryRefresh = "refresh_categories";
  private String PriceCategorySave = "save_categories";
  private String ServiceSessionAttribute = "service_designer_service_id";
  public static String parameterUpdateAction = "seviceDesignerUpdate";
  public static String parameterUpdateServiceId = "serviceDesignerUpdateServiceId";
  public static String parameterProductPriceId = "serviceDesignerProductPriceId";
  public static String parameterProductCategoryId = "parameterProductCategoryId";
  public static String parameterTimeframeId = "parameterTimeframeId";
  public static String parameterCreate = "create";

  public static String NAME_OF_FORM = "service_designer_form";
  public static String NAME_OF_PRICE_CATEGORY_FORM = "service_price_category_form";

  private static Boolean priceCategoryCreation;
  private String sessionNameServiceId = "tourDesignerSessionTourId";



  public ServiceDesigner() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws Exception{
      super.main(iwc);
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      supplier = super.getSupplier();

      if (super.isLoggedOn(iwc)) {
        if (iwc.getParameter(super.sAction) != null) {
          if (iwc.getParameter(super.sAction).equals(super.parameterServiceDesigner)) {
            removeSessionServiceId(iwc);
          }
        }

        String action = iwc.getParameter(ServiceAction);
        if (action == null) {action = "";}

        if (action.equals("")) {
            displayForm(iwc);
        }else if (action.equals(this.parameterCreate)) {
            createService(iwc);
        }else if (action.equals(parameterUpdateAction)) {
            add("unimplemented");
        }else if (action.equals(this.PriceCategoryRefresh) ) {
            priceCategoryCreation(iwc);
        }else if (action.equals(this.PriceCategorySave)) {
            priceCategorySave(iwc);
        }


      }else {
        add(super.getLoggedOffTable(iwc));
      }

      super.addBreak();
  }


  private void displayForm(IWContext iwc) throws Exception{
    /**
     * @todo implement for other types
     */
     add(Text.BREAK);
    TourDesigner td = new TourDesigner(iwc);

    String id = iwc.getParameter(this.parameterUpdateServiceId);
    if (id == null) {
      String sServiceId = getSessionServiceId(iwc);
      if (sServiceId != null) {
        id  = sServiceId;
      }
    }

    if (id != null) {
      add(td.getTourDesignerForm(iwc, Integer.parseInt(id)));
      setSessionServiceId(iwc, Integer.parseInt(id));
    }else {
      add(td.getTourDesignerForm(iwc));
    }
  }


  private void createService(IWContext iwc) throws Exception{

      if ( this.priceCategoryCreation == null ) {
        TourDesigner td = new TourDesigner(iwc);
          int tourId = td.createTour(iwc);
          setService(iwc,tourId);
          removeSessionServiceId(iwc);
      }

      tsb.removeServiceDayHashtable(iwc);
      priceCategoryCreation(iwc);

  }

  private void setService(IWContext iwc,int serviceId) throws SQLException{
      service = new Service(serviceId);
      iwc.setSessionAttribute(this.ServiceSessionAttribute, service);
  }

  private Service getService(IWContext iwc) {
    if (service == null) {
      service = (Service) iwc.getSessionAttribute(this.ServiceSessionAttribute);
    }
    return service;

  }

  private void removeService(IWContext iwc) {
      service = null;
      iwc.removeSessionAttribute(this.ServiceSessionAttribute);
  }


  private void priceCategoryCreation(IWContext iwc) throws SQLException {
      this.priceCategoryCreation = new Boolean(true);
      if (this.getService(iwc) != null) {




          Form form = new Form();
            form.setName(this.NAME_OF_PRICE_CATEGORY_FORM);

          Table table = new Table();
            form.add(table);
            table.setAlignment("center");
            table.setWidth("90%");
            table.setColor(super.WHITE);
            table.setCellspacing(1);
            int row = 1;

          Product product = new Product(this.service.getID());
          com.idega.block.text.presentation.TextChooser tc = new com.idega.block.text.presentation.TextChooser("le_text_id");
          if (product.getText() != null) {
            tc.setValue(product.getText());
          }

          tc.setChooseImage(iwrb.getLocalizedImageButton("travel.extra_info","Extra info"));

          Link tfAdder = new Link(iwrb.getLocalizedImageButton("travel.timeframes","Timeframes"));
            tfAdder.addParameter(TimeframeAdder._parameterProductId, product.getID());
            tfAdder.setWindowToOpen(TimeframeAdder.class);

          Link addAdder = new Link(iwrb.getLocalizedImageButton("travel.departure_place","Departure places"));
            addAdder.addParameter(AddressAdder._parameterProductId, product.getID());
            addAdder.setWindowToOpen(AddressAdder.class);


          Timeframe[] tFrames = product.getTimeframes();

          Text serviceNameText = (Text) super.theBoldText.clone();
            serviceNameText.setText(ProductBusiness.getProductNameWithNumber(product));
          table.add(serviceNameText,1,row);
          table.mergeCells(1,row,3,row);
          table.setRowColor(row, super.backgroundColor);
          ++row;
          table.add(tc,1,row);
          table.setRowColor(row, super.GRAY);
          table.mergeCells(1,row,3,row);
          ++row;
          table.add(tfAdder,1,row);
          table.setRowColor(row, super.GRAY);
          table.mergeCells(1,row,3,row);
          ++row;
          table.add(addAdder,1,row);
          table.setRowColor(row, super.GRAY);
          table.mergeCells(1,row,3,row);          ++row;

          for (int k = 0; k < tFrames.length; k++) {


          ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), tFrames[k].getID(), false);
            PriceCategory[] cats = tsb.getPriceCategories(this.supplier.getID());
            TextInput priceDiscount;

            Text categoryName;
            Text infoText;



            Text counter;

            Text catName = (Text) theText.clone();
              catName.setText(iwrb.getLocalizedString("travel.name","Name"));
              catName.setFontColor(super.WHITE);
            Text priceDiscountText = (Text) theText.clone();
              priceDiscountText.setText(iwrb.getLocalizedString("travel.price_discount","Price / Discount"));
              priceDiscountText.setFontColor(super.WHITE);
            Text timeframeText = getTimeframeText(tFrames[k], iwc);
              timeframeText.setFontColor(super.WHITE);



            table.add(catName,1,row);
            table.add(priceDiscountText,2,row);
            table.add(timeframeText,3,row);
            table.setAlignment(3, row, "right");
            table.setRowColor(row,super.backgroundColor);

            DecimalFormat df = new DecimalFormat("0.00");

            for (int i = 0; i < cats.length; i++) {
                table.add(new HiddenInput(parameterTimeframeId, Integer.toString(tFrames[k].getID())),1,row);
                categoryName = (Text) theText.clone();
                  categoryName.setFontColor(super.BLACK);
                  categoryName.setText(cats[i].getName());
                infoText = (Text) theText.clone();
                  infoText.setFontColor(super.BLACK);
                  infoText.setText(cats[i].getName());


                priceDiscount = new TextInput("price_discount");

                if (cats[i].getType().equals(PriceCategory.PRICETYPE_PRICE)) {
                  infoText.setText("");
                }else if (cats[i].getType().equals(PriceCategory.PRICETYPE_DISCOUNT)){
                  try {
                    priceDiscount.setSize(6);
                    infoText.setText("%");
                    infoText.addToText(Text.NON_BREAKING_SPACE);
                    infoText.addToText(iwrb.getLocalizedString("travel.of","of"));
                    infoText.addToText(Text.NON_BREAKING_SPACE);
                    infoText.addToText(new PriceCategory(cats[i].getParentId()).getName());
                  }catch (SQLException sql) {
                    sql.printStackTrace(System.err);
                  }
                }

                if (prices.length == 0) {
                  table.add(new HiddenInput(this.parameterProductPriceId,"-1"),1,row);
                }
                for (int j = 0; j < prices.length; j++) {
                  if (cats[i].getID() == prices[j].getPriceCategoryID()) {
                    try {
                      if (prices[j].getPriceType() == ProductPrice.PRICETYPE_PRICE) {
                        //priceDiscount.setContent(df.format(prices[j].getPrice()));
                        priceDiscount.setContent(Integer.toString((int)prices[j].getPrice()));
                      }else {
                        priceDiscount.setContent(Float.toString(prices[j].getPrice()));
                      }
                      table.add(new HiddenInput(this.parameterProductPriceId,Integer.toString(prices[j].getID())),1,row);//PriceCategoryID())),1,row);
                    }catch (ArrayIndexOutOfBoundsException a) {
                      table.add(new HiddenInput(this.parameterProductPriceId,"-1"),1,row);
                    }
                  }else {
                    table.add(new HiddenInput(this.parameterProductPriceId,"-1"),1,row);
                  }
                }


                ++row;
                table.add(new HiddenInput(this.parameterProductCategoryId,Integer.toString(cats[i].getID())),1,row);
                table.add(categoryName,1,row);
                table.add(priceDiscount,2,row);
                table.setAlignment(2,row,"right");
                table.setWidth(2,"150");
                table.add(infoText,3,row);
                table.setRowColor(row,super.GRAY);
            }

            ++row;
          }

          table.setRowColor(row,super.GRAY);
          table.setAlignment(3,row,"right");
          table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.ServiceAction, this.PriceCategorySave),3,row);

          add(Text.BREAK);
          add(form);
      }else {
        add("TEMP SERVICE ER NULL");
        this.priceCategoryCreation = null;
      }

  }

  private void priceCategorySave(IWContext iwc) {
      String[] timeframeIds = (String[]) iwc.getParameterValues(parameterTimeframeId);
      String[] priceDiscount = (String[]) iwc.getParameterValues("price_discount");
      String[] priceCategoryIds = (String[]) iwc.getParameterValues(this.parameterProductCategoryId);

      String[] productPriceIds = (String[]) iwc.getParameterValues(this.parameterProductPriceId);
      String text_id = iwc.getParameter("le_text_id");


      Service service = this.getService(iwc);

      try {
        if (priceDiscount != null) {
          int priceCategoryId = 0;
          int productPriceId = -1;

          Product product = new Product(service.getID());
          if (text_id != null && !text_id.equals("")) {
            TxText pText = product.getText();
            if (pText == null) {
              TxText text = new TxText(Integer.parseInt(text_id));
              text.addTo(product);
            }
          }

          ProductPrice.clearPrices(service.getID());

          float price;
          PriceCategory pCategory;
          for (int i = 0; i < priceDiscount.length; i++) {
            if (!priceDiscount[i].equals("")) {
              productPriceId = Integer.parseInt(productPriceIds[i]);
              priceCategoryId = Integer.parseInt(priceCategoryIds[i]);
              pCategory = new PriceCategory(priceCategoryId);

              if (pCategory.getType().equals(PriceCategory.PRICETYPE_DISCOUNT)) {
                priceDiscount[i] = TextSoap.findAndReplace(priceDiscount[i],',','.');
                tsb.setPrice(productPriceId,service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), ProductPrice.PRICETYPE_DISCOUNT, Integer.parseInt(timeframeIds[i]));
              }else if (pCategory.getType().equals(PriceCategory.PRICETYPE_PRICE)) {
                priceDiscount[i] = TextSoap.findAndCut(priceDiscount[i],".");
                if (priceDiscount[i].indexOf(",") > 0) {
                  priceDiscount[i] = TextSoap.findAndCut(priceDiscount[i],",");
                  price = (float) Float.parseFloat(priceDiscount[i]);
                  price = price / 100;
                }else {
                  price = (float) Float.parseFloat(priceDiscount[i]);
                }

                tsb.setPrice(productPriceId,service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), price, ProductPrice.PRICETYPE_PRICE, Integer.parseInt(timeframeIds[i]));
              }
            }
          }
        }
        this.removeService(iwc);
        this.priceCategoryCreation = null;

        saveSuccessful();

      }catch (Exception e) {
        e.printStackTrace(System.err);
      }


  }

  private void saveSuccessful() {
    Text text = (Text) theText.clone();
      text.setFontColor(WHITE);
      text.setText(iwrb.getLocalizedString("travel.service_was_created","Service was created"));

    add(Text.BREAK);
    add(text);
  }

  private String getSessionServiceId(IWContext iwc) {
    return (String) iwc.getSessionAttribute(sessionNameServiceId);
  }

  private void setSessionServiceId(IWContext iwc, int serviceId) {
    iwc.setSessionAttribute(sessionNameServiceId,Integer.toString(serviceId));
  }

  private void removeSessionServiceId(IWContext iwc) {
    iwc.removeSessionAttribute(sessionNameServiceId);
  }

  private Text getTimeframeText(Timeframe timeframe, IWContext iwc) {
    idegaTimestamp from = new idegaTimestamp(timeframe.getFrom());
    idegaTimestamp to = new idegaTimestamp(timeframe.getTo());
    Text text = new Text();
      text.setText(from.getLocaleDate(iwc)+ " - " + to.getLocaleDate(iwc) );
    return text;
  }

}
