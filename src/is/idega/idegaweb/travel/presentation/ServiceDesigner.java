package is.idega.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.block.trade.stockroom.business.*;
import is.idega.travel.business.TravelStockroomBusiness;
import com.idega.util.idegaCalendar;
import com.idega.core.accesscontrol.business.AccessControl;
import java.sql.SQLException;

import is.idega.travel.data.Service;

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


  private void priceCategoryCreation(IWContext iwc) {
      this.priceCategoryCreation = new Boolean(true);
      if (this.getService(iwc) != null) {
        ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), false);
          String sHowMany = iwc.getParameter("how_many");
          int iHowMany = 2;
          if (sHowMany == null) {
            if (prices.length > 0) {
              sHowMany = Integer.toString(prices.length);
            }else {
              sHowMany = "2";
            }
          }else {
            //Integer.parseInt(sHowMany);
          }
          iHowMany = Integer.parseInt(sHowMany);

          Form howManyForm = new Form();
  //            sb.add(howManyForm);
              Parameter par = new Parameter(this.ServiceAction, this.PriceCategoryRefresh);
                par.keepStatusOnAction();
              howManyForm.add(par);

          Table tableHowMany = new Table();
            howManyForm.add(tableHowMany);

          Text howManyText = (Text) theText.clone();
            howManyText.setText("T - Hve marga verðliði");

          TextInput howMany = new TextInput("how_many",sHowMany);
            howMany.setAsIntegers("Temp - bara tölur takk");
            howMany.setAsNotEmpty("Temp - selja e-ð ");

          SubmitButton howManySubmit = new SubmitButton("T - áfram");

          tableHowMany.add(howManyText,1,1);
          tableHowMany.add(howMany,2,1);
          tableHowMany.add(howManySubmit,3,1);


          Form form = new Form();
            form.setName(this.NAME_OF_PRICE_CATEGORY_FORM);

          Table table = new Table();
            form.add(table);
            table.setAlignment("center");
            table.setWidth("95%");
            int row = 1;

          PriceCategory[] cats = tsb.getPriceCategories(this.supplier.getID());
          DropdownMenu toClone = new DropdownMenu(cats,"price_category_id");
  //          toClone.keepStatusOnAction();
          DropdownMenu categories;
          TextInput priceDiscount;



          Text counter;

          Text catName = (Text) theText.clone();
            catName.setText("T - nafn");
          Text priceDiscountText = (Text) theText.clone();
            priceDiscountText.setText("T - Price / Discount");

          table.setColor(1,row,super.backgroundColor);
          table.mergeCells(1,row,3,row);

          Link link = new Link();
            link.setText("t PriceCategoryDesigner");
            link.setWindowToOpen(PriceCategoryDesigner.class);

            table.add(link,1,row);
            table.setAlignment(1,row,"right");


          for (int i = 1; i <= iHowMany; i++) {
              priceDiscount = new TextInput("price_discount");
                priceDiscount.setAsNotEmpty("T - verður að skrá verð eða afslátt á allt verðliði");
                priceDiscount.keepStatusOnAction();
              categories = (DropdownMenu) toClone.clone();
//              categories = new DropdownMenu(cats,"price_category_id");
//                categories.keepStatusOnAction();

              try {
                priceDiscount.setContent(Integer.toString((int)prices[i-1].getPrice()));
                categories.setSelectedElement(Integer.toString(prices[i-1].getPriceCategoryID()));
                table.add(new HiddenInput(this.parameterProductPriceId,Integer.toString(prices[i-1].getID())),1,row);//PriceCategoryID())),1,row);
              }catch (ArrayIndexOutOfBoundsException a) {
                table.add(new HiddenInput(this.parameterProductPriceId,"-1"),1,row);
              }

              ++row;
              table.add(catName,2,row);
              table.add(categories,3,row);

              ++row;
              table.add(priceDiscountText,2,row);
              table.add(priceDiscount,3,row);

              ++row;
              table.mergeCells(1,row,3,row);
              table.setHeight(row,"15");
              table.setColor(1,row, super.backgroundColor);

          }

          table.setAlignment(1,row,"right");
          table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.ServiceAction, this.PriceCategorySave),1,row);


          if (iHowMany > 0) {
            //SubmitButton savePrice = new SubmitButton(this.ServiceAction, this.PriceCategorySave);

          }


          add(howManyForm);
          add(Text.getBreak());
          add(form);
      }else {
        add("TEMP SERVICE ER NULL");
        this.priceCategoryCreation = null;
      }

  }

  private void priceCategorySave(IWContext iwc) {
      String[] priceDiscount = (String[]) iwc.getParameterValues("price_discount");
      String[] priceCategoryIds = (String[]) iwc.getParameterValues("price_category_id");

      String[] productPriceIds = (String[]) iwc.getParameterValues(this.parameterProductPriceId);

      Service service = this.getService(iwc);

      try {
        if (priceDiscount != null) {
          int priceCategoryId = 0;
          int productPriceId = -1;

          ProductPrice.clearPrices(service.getID());

          PriceCategory pCategory;
          for (int i = 0; i < priceDiscount.length; i++) {
              productPriceId = Integer.parseInt(productPriceIds[i]);
              priceCategoryId = Integer.parseInt(priceCategoryIds[i]);
              pCategory = new PriceCategory(priceCategoryId);

              if (pCategory.getType().equals(PriceCategory.PRICETYPE_DISCOUNT)) {
                tsb.setPrice(productPriceId,service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), ProductPrice.PRICETYPE_DISCOUNT);
              }else if (pCategory.getType().equals(PriceCategory.PRICETYPE_PRICE)) {
                tsb.setPrice(productPriceId,service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), ProductPrice.PRICETYPE_PRICE);
              }
          }
        }
        this.removeService(iwc);
        this.priceCategoryCreation = null;

      }catch (Exception e) {
        e.printStackTrace(System.err);
      }


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

}