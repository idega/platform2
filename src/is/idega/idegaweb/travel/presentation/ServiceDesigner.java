package is.idega.travel.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
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


  public static String NAME_OF_FORM = "service_designer_form";

  private static Boolean priceCategoryCreation;


  public ServiceDesigner() {
  }

  public void add(ModuleObject mo) {
    super.add(mo);
  }


  public void main(ModuleInfo modinfo) throws SQLException{
      super.main(modinfo);
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      supplier = super.getSupplier();

      if (supplier != null) {

        String action = modinfo.getParameter(ServiceAction);
        if (action == null) {action = "";}

        if (action.equals("")) {
            displayForm(modinfo);
        }else if (action.equals("create")) {
            createService(modinfo);
        }else if (action.equals(parameterUpdateAction)) {
            add("unimplemented");
        }else if (action.equals(this.PriceCategoryRefresh) ) {
            priceCategoryCreation(modinfo);
        }else if (action.equals(this.PriceCategorySave)) {
            priceCategorySave(modinfo);
        }


      }else {
        add("TEMP - Enginn supplier");
      }

      super.addBreak();
  }


  private void displayForm(ModuleInfo modinfo) throws SQLException{
    /**
     * @todo implement for other types
     */
    TourDesigner td = new TourDesigner(modinfo);
      add(td.getTourDesignerForm());
  }


  private void createService(ModuleInfo modinfo) throws SQLException{
      if ( this.priceCategoryCreation == null ) {
        TourDesigner td = new TourDesigner(modinfo);
          int tourId = td.createTour(modinfo);
          setService(modinfo,tourId);
      }
      priceCategoryCreation(modinfo);

  }

  private void setService(ModuleInfo modinfo,int serviceId) throws SQLException{
      service = new Service(serviceId);
      modinfo.setSessionAttribute(this.ServiceSessionAttribute, service);
  }

  private Service getService(ModuleInfo modinfo) {
    if (service == null) {
      service = (Service) modinfo.getSessionAttribute(this.ServiceSessionAttribute);
    }
    return service;

  }

  private void removeService(ModuleInfo modinfo) {
      service = null;
      modinfo.removeSessionAttribute(this.ServiceSessionAttribute);
  }


  private void priceCategoryCreation(ModuleInfo modinfo) {
      this.priceCategoryCreation = new Boolean(true);
      if (this.getService(modinfo) != null) {

          ShadowBox sb = new ShadowBox();
            sb.setWidth("90%");

          String sHowMany = modinfo.getParameter("how_many");
          if (sHowMany == null) {
            sHowMany = "2";
          }
          int iHowMany = Integer.parseInt(sHowMany);

          Form howManyForm = new Form();
              sb.add(howManyForm);
              howManyForm.addParameter(this.ServiceAction ,this.PriceCategoryRefresh);

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
            sb.add(form);

          Table table = new Table();
            form.add(table);
            table.setAlignment("center");
            table.setWidth("95%");
            int row = 1;

          DropdownMenu toClone = new DropdownMenu(tsb.getPriceCategories(this.supplier.getID()),"price_category_id");
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

              categories = (DropdownMenu) toClone.clone();

              ++row;
              table.add(catName,2,row);
              table.add(categories,3,row);

              ++row;
              table.add(priceDiscountText,2,row);
              table.add(priceDiscount,3,row);

              ++row;
              table.mergeCells(1,row,3,row);
              table.setColor(1,row, super.backgroundColor);

          }

          table.setAlignment(1,row,"right");
          table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.ServiceAction, this.PriceCategorySave),1,row);


          if (iHowMany > 0) {
            SubmitButton savePrice = new SubmitButton(this.ServiceAction, this.PriceCategorySave);

          }

          add(Text.getBreak());
          add(sb);
      }else {
        add("TEMP SERVICE ER NULL");
      }

  }

  private void priceCategorySave(ModuleInfo modinfo) {
      String[] priceDiscount = (String[]) modinfo.getParameterValues("price_discount");
      String[] priceCategoryIds = (String[]) modinfo.getParameterValues("price_category_id");

      Service service = this.getService(modinfo);

      try {
        if (priceDiscount != null) {
          int priceCategoryId = 0;

                    PriceCategory pCategory;
          for (int i = 0; i < priceDiscount.length; i++) {
                priceCategoryId = Integer.parseInt(priceCategoryIds[i]);
              pCategory = new PriceCategory(priceCategoryId);

              if (pCategory.getType().equals(PriceCategory.PRICETYPE_DISCOUNT)) {
                tsb.setPrice(service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), ProductPrice.PRICETYPE_DISCOUNT);
              }else if (pCategory.getType().equals(PriceCategory.PRICETYPE_PRICE)) {
                tsb.setPrice(service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), ProductPrice.PRICETYPE_PRICE);
              }
          }
        }
        this.removeService(modinfo);
        this.priceCategoryCreation = null;

      }catch (Exception e) {
        e.printStackTrace(System.err);
      }


  }


}