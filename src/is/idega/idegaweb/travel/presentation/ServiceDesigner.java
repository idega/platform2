package is.idega.idegaweb.travel.presentation;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.text.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.business.*;
import is.idega.idegaweb.travel.service.presentation.*;

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
  public static String parameterCreate = "create";

  private String PARAMETER_PRODUCT_CATEGORY_TYPE = "pCat_type";

  public static String NAME_OF_FORM = "service_designer_form";

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
			Collection coll;
			try {
				coll = supplier.getProductCategories();
				Iterator iter = coll.iterator();
				ProductCategory pCat;
				String type;
				if (iter.hasNext()) {
				  pCat = (ProductCategory) iter.next();
				  PRODUCT_CATEGORY_TYPE = pCat.getCategoryType();
				}
			} catch (IDORelationshipException e) {
				PRODUCT_CATEGORY_TYPE = ProductCategoryFactoryBean.CATEGORY_TYPE_DEFAULT;
			}
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
        Product prod = getProductBusiness(iwc).getProduct(serviceId);
        Collection coll = getProductCategoryFactory(iwc).getProductCategory(prod);
        Iterator iter = coll.iterator();
        if (iter.hasNext()) {
          PRODUCT_CATEGORY_TYPE = ((ProductCategory) iter.next()).getCategoryType();
        }
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

      if (super.isLoggedOn(iwc) && supplier != null) {
        if (iwc.getParameter(super.sAction) != null) {
          if (iwc.getParameter(super.sAction).equals(super.parameterServiceDesigner)) {
            removeSessionServiceId(iwc);
          }
        }

        String action = iwc.getParameter(ServiceAction);
//        System.out.println("action = "+action);
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
						finalize(iwc);
        }


      }else {
        add(super.getLoggedOffTable(iwc));
      }

      super.addBreak();
  }

private void finalize(IWContext iwc) throws Exception {
	Service service = getService(iwc);
	if (service != null) {
		Product product = getProductBusiness(iwc).getProduct((Integer)service.getPrimaryKey());
    DesignerForm df = getServiceHandler(iwc).getDesignerForm(iwc, productCategory);
    df.finalizeCreation( iwc, product );
	}
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


  private void priceCategoryCreation(IWContext iwc) throws SQLException, RemoteException, FinderException{
      setCategoryCreation(iwc, true);
      if (this.getService(iwc) != null) {

					Form form = new Form();

          Table table = new Table();
            table.setAlignment("center");
            table.setWidth("90%");
            table.setColor(super.WHITE);
            table.setCellspacing(1);
            int row = 1;

          Product product = getProductBusiness(iwc).getProduct((Integer)this.service.getPrimaryKey());

          Link tfAdder = new Link(iwrb.getLocalizedImageButton("travel.timeframes","Timeframes"));
          tfAdder.addParameter(TimeframeAdder._parameterProductId, product.getID());
          tfAdder.setWindowToOpen(TimeframeAdder.class);

          Link addAdder = new Link(iwrb.getLocalizedImageButton("travel.departure_place","Departure places"));
          addAdder.addParameter(AddressAdder._parameterProductId, product.getID());
          addAdder.setWindowToOpen(AddressAdder.class);

          Link servDaySetter = new Link(iwrb.getLocalizedImageButton("travel.active_dats","Active days"));
          servDaySetter.addParameter(ServiceDaySetter.PARAMETER_SERVICE_ID,  product.getID());
          servDaySetter.setWindowToOpen(ServiceDaySetter.class);
          
          Link productPriceDesigner = ProductPriceDesigner.getLink(product.getID());
          productPriceDesigner.setImage(iwrb.getLocalizedImageButton("travel.product_price_designer","Price designer"));
          
          Link creditCardHandler = new Link(iwrb.getLocalizedImageButton("travel.authorization_check", "Authorization Check Setter"));
          creditCardHandler.addParameter(CreditCardPropertiesSetter.PARAMETER_PRODUCT_ID, product.getID());
          creditCardHandler.setWindowToOpen(CreditCardPropertiesSetter.class);

          Timeframe[] tFrames = product.getTimeframes();
          List addresses = com.idega.util.ListUtil.getEmptyList();
          try {
            addresses = product.getDepartureAddresses(true);
          }catch (IDOFinderException ido) {
            ido.printStackTrace(System.err);
          }
          int addressesSize = addresses.size();
          TravelAddress address;

          Text serviceNameText = (Text) super.theBoldText.clone();
            serviceNameText.setText(getProductBusiness(iwc).getProductNameWithNumber(product));

          table.add(serviceNameText,1,row);
          table.mergeCells(1,row,3,row);
          table.setRowColor(row, super.backgroundColor);
          
          ++row;
					com.idega.block.text.presentation.TextChooser tc = new com.idega.block.text.presentation.TextChooser("le_text_id");
					if (product.getText() != null) {
					  tc.setValue(product.getText());
					}
								
					tc.setChooseImage(iwrb.getLocalizedImageButton("travel.extra_info","Extra info"));
					tc.addForm(false);
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
          table.add(productPriceDesigner,1,row);
          table.setRowColor(row, super.GRAY);
          table.mergeCells(1,row,3,row);
          ++row;
//          NOT FULLY TESTED
          if (isTestMode()) {
	          table.add(creditCardHandler,1,row);
	          table.setRowColor(row, super.GRAY);
	          table.mergeCells(1,row,3,row);
	          ++row;
          }

        ProductPriceDesigner ppd = new ProductPriceDesigner(iwc);
        form.add(table);
        form = ppd.getPriceCategoryForm(iwc, product, ServiceAction, PriceCategorySave, form);
				add(form);

      }else {
        add(iwrb.getLocalizedString("travel.service_not_found","Service not found."));
        setCategoryCreation(iwc, false);
      }

  }

  private void priceCategorySave(IWContext iwc) {
      String text_id = iwc.getParameter("le_text_id");
      Service service = this.getService(iwc);

      try {
        Product product = getProductBusiness(iwc).getProduct((Integer)service.getPrimaryKey());
        if (text_id != null && !text_id.equals("")) {
          TxText pText = product.getText();
          if (pText == null) {
            TxText text = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(Integer.parseInt(text_id));
            product.addText(text);
          }
        }

        ProductPriceDesigner ppd = new ProductPriceDesigner(iwc);
        boolean success = ppd.handleInsert(iwc, product);


        this.removeService(iwc);
        this.setCategoryCreation(iwc, false);

        if (success ) {
          saveSuccessful();
        }else {
          saveUnsuccessful();
        }

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

  private void saveUnsuccessful() {
    Text text = (Text) theText.clone();
      text.setFontColor(WHITE);
      text.setText(iwrb.getLocalizedString("travel.service_was_created_but_price_failed","Service was created, but there was an error while trying to save the Prices"));

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
    IWTimestamp from = new IWTimestamp(timeframe.getFrom());
    IWTimestamp to = new IWTimestamp(timeframe.getTo());
    Text text = new Text();
      text.setText(from.getLocaleDate(iwc)+ " - " + to.getLocaleDate(iwc) );
    return text;
  }

}
