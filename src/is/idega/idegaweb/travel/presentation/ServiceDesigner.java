package is.idega.idegaweb.travel.presentation;

import javax.ejb.FinderException;
import java.rmi.RemoteException;
import com.idega.data.*;
import java.util.*;
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
import is.idega.idegaweb.travel.service.business.*;
import is.idega.idegaweb.travel.service.presentation.*;
import com.idega.core.data.*;

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

  private TravelStockroomBusiness tsb;
  private Supplier supplier;
  private Service service;
  private ProductCategory productCategory;

  public static String ServiceAction = "service_action";
  private String PriceCategoryRefresh = "refresh_categories";
  private String PriceCategorySave = "save_categories";
  private String ServiceSessionAttribute = "service_designer_service_id";
  public static String parameterUpdateAction = "seviceDesignerUpdate";
  public static String parameterUpdateServiceId = "serviceDesignerUpdateServiceId";
  public static String parameterProductPriceId = "serviceDesignerProductPriceId";
  public static String parameterProductCategoryId = "parameterProductCategoryId";
  public static String parameterTimeframeId = "parameterTimeframeId";
  public static String parameterAddressId = "parameterAddressId";
  public static String parameterCreate = "create";

  private String PARAMETER_PRODUCT_CATEGORY_TYPE = "pCat_type";

  public static String NAME_OF_FORM = "service_designer_form";
  public static String NAME_OF_PRICE_CATEGORY_FORM = "service_price_category_form";

//  private Boolean priceCategoryCreation;
  private String sessionNameServiceId = "tourDesignerSessionTourId";
  private String PRODUCT_CATEGORY_TYPE = ProductCategoryFactoryBean.CATEGORY_TYPE_DEFAULT;

  private int serviceId = -1;



  public ServiceDesigner() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }

  private void init(IWContext iwc) throws RemoteException {
    bundle = super.getBundle();
    iwrb = super.getResourceBundle();
    supplier = super.getSupplier();
    tsb = getTravelStockroomBusiness(iwc);

    String pCatType = iwc.getParameter(PARAMETER_PRODUCT_CATEGORY_TYPE);

    if (pCatType != null) {
      PRODUCT_CATEGORY_TYPE = pCatType;
    }else {
      PRODUCT_CATEGORY_TYPE = ProductCategoryFactoryBean.CATEGORY_TYPE_DEFAULT;
    }

    String id = iwc.getParameter(this.parameterUpdateServiceId);
    if (id == null) {
      String sServiceId = getSessionServiceId(iwc);
      if (sServiceId != null) {
        serviceId = Integer.parseInt(sServiceId);
      }
    }else {
      serviceId = Integer.parseInt(id);
      try {
        Product prod = ProductBusiness.getProduct(serviceId);
        Collection coll = getProductCategoryFactory(iwc).getProductCategory(prod);
        Iterator iter = coll.iterator();
        if (iter.hasNext()) {
          PRODUCT_CATEGORY_TYPE = ((ProductCategory) iter.next()).getCategoryType();
        }
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }catch (FinderException fe) {
        fe.printStackTrace(System.err);
      }
    }



    try {
      productCategory = ((ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class)).getProductCategory(PRODUCT_CATEGORY_TYPE);
    }catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }
  }

  public void main(IWContext iwc) throws Exception{
      super.main(iwc);
      init(iwc);

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

  private void setCategoryCreation(IWContext iwc, boolean isCreation) {
    if (isCreation) {
      iwc.setSessionAttribute("sd_isCategoryCreation", new Boolean(isCreation));
    }else {
      iwc.removeSessionAttribute("sd_isCategoryCreation");
    }
  }

  public boolean isCategoryCreation(IWContext iwc) {
    Object obj = iwc.getSessionAttribute("sd_isCategoryCreation");
    if (obj == null) {
      return false;
    }
    else {
      return true;
    }
  }

  private Form getProductCategoryForm(IWContext iwc) throws RemoteException{
    Form form = new Form();
    Table table = new Table();
    form.add(table);
    table.add("ProductCategory : ");
    try {
      Collection coll = this.supplier.getProductCategories();
      DropdownMenu pCats = getProductCategoryFactory(iwc).getProductCategoryDropdown(iwrb, supplier, PARAMETER_PRODUCT_CATEGORY_TYPE);
        pCats.setSelectedElement(PRODUCT_CATEGORY_TYPE);
        pCats.setToSubmit();
      table.add(pCats);
    }catch (IDORelationshipException idor) {
      idor.printStackTrace(System.err);
    }
    return form;
  }

  private void displayForm(IWContext iwc) throws Exception{
    add(Text.BREAK);
    add(getProductCategoryForm(iwc));

    DesignerForm df = getServiceHandler(iwc).getDesignerForm(iwc, productCategory);


    if (serviceId != -1) {
      Form form = df.getDesignerForm(iwc, serviceId);
        form.addParameter(PARAMETER_PRODUCT_CATEGORY_TYPE, PRODUCT_CATEGORY_TYPE);
      add(form);
      setCategoryCreation(iwc, false);
      setSessionServiceId(iwc, serviceId);
    }else {
      Form form = df.getDesignerForm(iwc);
        form.addParameter(PARAMETER_PRODUCT_CATEGORY_TYPE, PRODUCT_CATEGORY_TYPE);
      add(form);
    }
  }


  private void createService(IWContext iwc) throws Exception{

      if ( !isCategoryCreation(iwc) ) {
          DesignerForm df = getServiceHandler(iwc).getDesignerForm(iwc, productCategory);
//        TourDesigner td = new TourDesigner(iwc);
          int tourId = df.handleInsert(iwc);
          setService(iwc,tourId);
          removeSessionServiceId(iwc);
      }

      tsb.removeServiceDayHashtable(iwc);
      priceCategoryCreation(iwc);

  }

  private void setService(IWContext iwc,int serviceId) throws RemoteException, FinderException{
    service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(serviceId));
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


  private void priceCategoryCreation(IWContext iwc) throws SQLException, RemoteException{
      setCategoryCreation(iwc, true);
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

          Product product = ProductBusiness.getProduct((Integer)this.service.getPrimaryKey());
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

          Link servDaySetter = new Link(iwrb.getLocalizedImageButton("travel.active_dats","Active days"));
            servDaySetter.addParameter(ServiceDaySetter.PARAMETER_SERVICE_ID,  product.getID());
            servDaySetter.setWindowToOpen(ServiceDaySetter.class);

          Timeframe[] tFrames = product.getTimeframes();
          List addresses = com.idega.util.ListUtil.getEmptyList();
          try {
            addresses = ProductBusiness.getDepartureAddresses(product, true);
          }catch (IDOFinderException ido) {
            ido.printStackTrace(System.err);
          }
          int addressesSize = addresses.size();
          TravelAddress address;

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
          table.mergeCells(1,row,3,row);
          ++row;
          table.add(servDaySetter,1,row);
          table.setRowColor(row, super.GRAY);
          table.mergeCells(1,row,3,row);
          ++row;


          Text addrText;
          TextInput priceDiscount;
          Text categoryName;
          Text infoText;
          Text counter;
          idegaTimestamp timestamp;

//          TextInput maxUsage;


          for (int l = 0; l < addressesSize; l++) {
            address = (TravelAddress) addresses.get(l);
            table.mergeCells(1,row,3,row);
            table.setRowColor(row, super.backgroundColor);
            addrText = (Text) super.theBoldText.clone();
              addrText.setText(address.getName());
            table.add(addrText, 1, row);
            ++row;

            PriceCategory[] cats = tsb.getPriceCategories(this.supplier.getID());
            PriceCategory[] misc = tsb.getMiscellaneousServices(this.supplier.getID());

            for (int k = 0; k < tFrames.length; k++) {

              Text catName = (Text) theText.clone();
                catName.setText(iwrb.getLocalizedString("travel.price","Price"));
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

              ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(((Integer)service.getPrimaryKey()).intValue(), tFrames[k].getID(), address.getID(), false);
              for (int i = 0; i < cats.length; i++) {
                  table.add(new HiddenInput(parameterTimeframeId, Integer.toString(tFrames[k].getID())),1,row);
                  table.add(new HiddenInput(parameterAddressId, Integer.toString(address.getID())),1,row);
                  categoryName = (Text) theText.clone();
                    categoryName.setFontColor(super.BLACK);
                    categoryName.setText(cats[i].getName());
                  infoText = (Text) theText.clone();
                    infoText.setFontColor(super.BLACK);
                    infoText.setText(cats[i].getName());


                  priceDiscount = new TextInput("price_discount");

                  if (cats[i].getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)) {
                    infoText.setText("");
                  }else if (cats[i].getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)){
                    try {
                      priceDiscount.setSize(6);
                      infoText.setText("%");
                      infoText.addToText(Text.NON_BREAKING_SPACE);
                      infoText.addToText(iwrb.getLocalizedString("travel.discount_of","discount of"));
                      infoText.addToText(Text.NON_BREAKING_SPACE);
                      infoText.addToText(((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(cats[i].getParentId()).getName());
                    }catch (SQLException sql) {
                      sql.printStackTrace(System.err);
                    }
                  }

                  HiddenInput hi = new HiddenInput(this.parameterProductPriceId, "-1");

                  int iMaxUsage = 0;

                  for (int j = 0; j < prices.length; j++) {
                    iMaxUsage = prices[j].getMaxUsage();
                    if (cats[i].getID() == prices[j].getPriceCategoryID()) {
                      if (prices[j].getPriceType() == com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE) {
                        priceDiscount.setContent(Integer.toString((int)prices[j].getPrice()));
                      }else {
                        priceDiscount.setContent(Float.toString(prices[j].getPrice()));
                      }
                      hi.setValue(prices[j].getID());
                      break;
                    }

                  }
                  table.add(hi, 1, row);


                  ++row;
                  table.add(new HiddenInput(this.parameterProductCategoryId,Integer.toString(cats[i].getID())),1,row);
                  table.add(categoryName,1,row);
                  table.add(priceDiscount,2,row);
                  table.setAlignment(2,row,"right");
                  table.setWidth(2,"150");
                  table.add(infoText,3,row);

                   table.setAlignment(3, row, "left");
                  table.setRowColor(row,super.GRAY);
              }

              if (misc.length > 0) {
                ++row;
                catName = (Text) theText.clone();
                catName.setText(iwrb.getLocalizedString("travel.miscellaneous_services","Miscellaneous services"));
                catName.setFontColor(super.WHITE);
                table.add(catName, 1, row);
                table.setRowColor(row, super.backgroundColor);
              }

              ProductPrice[] miscPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(((Integer)service.getPrimaryKey()).intValue(), tFrames[k].getID(), address.getID(), false);

              for (int i = 0; i < misc.length; i++) {
                  table.add(new HiddenInput(parameterTimeframeId, Integer.toString(tFrames[k].getID())),1,row);
                  table.add(new HiddenInput(parameterAddressId, Integer.toString(address.getID())),1,row);
                  categoryName = (Text) theText.clone();
                    categoryName.setFontColor(super.BLACK);
                    categoryName.setText(misc[i].getName());
                  infoText = (Text) theText.clone();
                    infoText.setFontColor(super.BLACK);
                    infoText.setText(misc[i].getName());


                  priceDiscount = new TextInput("price_discount");

                  if (misc[i].getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)) {
                    infoText.setText("");
                  }

                  HiddenInput hi = new HiddenInput(this.parameterProductPriceId, "-1");
                  int iMaxUsage = 0;


                  for (int j = 0; j < miscPrices.length; j++) {
                    iMaxUsage = miscPrices[j].getMaxUsage();
                    if (misc[i].getID() == miscPrices[j].getPriceCategoryID()) {
                      if (miscPrices[j].getPriceType() == com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE) {
                        priceDiscount.setContent(Integer.toString((int)miscPrices[j].getPrice()));
                      }else {
                        priceDiscount.setContent(Float.toString(miscPrices[j].getPrice()));
                      }
                      hi.setValue(miscPrices[j].getID());
                      break;
                    }
                  }
                  table.add(hi, 1, row);


                  ++row;
                  table.add(new HiddenInput(this.parameterProductCategoryId,Integer.toString(misc[i].getID())),1,row);
                  table.add(categoryName,1,row);
                  table.add(priceDiscount,2,row);
                  table.setAlignment(2,row,"right");
                  table.setWidth(2,"150");
                  table.add(infoText,3,row);

                   table.setAlignment(3, row, "left");
                  table.setRowColor(row,super.GRAY);
              }

              ++row;
            }
          }

          table.setRowColor(row,super.GRAY);
          table.setAlignment(3,row,"right");
          table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.ServiceAction, this.PriceCategorySave),3,row);

          add(Text.BREAK);
          add(form);
      }else {
        add("SERVICE ER NULL");
        setCategoryCreation(iwc, false);
      }

  }

  private void priceCategorySave(IWContext iwc) {
      String[] timeframeIds = (String[]) iwc.getParameterValues(parameterTimeframeId);
      String[] addressIds = (String[]) iwc.getParameterValues(parameterAddressId);
      String[] priceDiscount = (String[]) iwc.getParameterValues("price_discount");
      String[] maxUsage = (String[]) iwc.getParameterValues("max_usage");
      String[] priceCategoryIds = (String[]) iwc.getParameterValues(this.parameterProductCategoryId);

      String[] productPriceIds = (String[]) iwc.getParameterValues(this.parameterProductPriceId);
      String text_id = iwc.getParameter("le_text_id");


      Service service = this.getService(iwc);

      try {
        if (priceDiscount != null) {
          int priceCategoryId = 0;
          int productPriceId = -1;

          Product product = ProductBusiness.getProduct((Integer)service.getPrimaryKey());
          if (text_id != null && !text_id.equals("")) {
            TxText pText = product.getText();
            if (pText == null) {
              TxText text = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(Integer.parseInt(text_id));
              text.addTo(product);
            }
          }

          com.idega.block.trade.stockroom.data.ProductPriceBMPBean.clearPrices(((Integer)service.getPrimaryKey()).intValue());

          float price;
          int iMaxUsage;
          PriceCategory pCategory;
          ProductPrice pPrice;
          for (int i = 0; i < priceDiscount.length; i++) {
            pPrice = null;
            if (!priceDiscount[i].equals("")) {
              productPriceId = Integer.parseInt(productPriceIds[i]);
              priceCategoryId = Integer.parseInt(priceCategoryIds[i]);

              pCategory = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(priceCategoryId);

              if (pCategory.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)) {
                priceDiscount[i] = TextSoap.findAndReplace(priceDiscount[i],',','.');
                pPrice = tsb.setPrice(productPriceId,((Integer)service.getPrimaryKey()).intValue() , priceCategoryId, tsb.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_DISCOUNT, Integer.parseInt(timeframeIds[i]), Integer.parseInt(addressIds[i]));
              }else if (pCategory.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)) {
                priceDiscount[i] = TextSoap.findAndCut(priceDiscount[i],".");
                if (priceDiscount[i].indexOf(",") > 0) {
                  priceDiscount[i] = TextSoap.findAndCut(priceDiscount[i],",");
                  price = (float) Float.parseFloat(priceDiscount[i]);
                  price = price / 100;
                }else {
                  price = (float) Float.parseFloat(priceDiscount[i]);
                }
                pPrice = tsb.setPrice(productPriceId,((Integer)service.getPrimaryKey()).intValue() , priceCategoryId, tsb.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), price, com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE, Integer.parseInt(timeframeIds[i]), Integer.parseInt(addressIds[i]));
              }

              if (pPrice != null) {
                try {
                  iMaxUsage = Integer.parseInt(maxUsage[i]);
                  pPrice.setMaxUsage(iMaxUsage);
                  pPrice.update();
                }catch (Exception e) {
                  debug(e.getMessage());
                }
              }
            }
          }
        }
        this.removeService(iwc);
        this.setCategoryCreation(iwc, false);

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
