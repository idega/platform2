package is.idega.travel.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import is.idega.travel.business.TravelStockroomBusiness;
import is.idega.travel.presentation.TravelWindow;
import com.idega.block.trade.stockroom.data.*;

import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class PriceCategoryDesigner extends TravelWindow {
  public static String SERVICE_ID_PARAMETER_NAME = "serviceId";

  private int serviceId = -1;
  private int supplierId = -1;

  private String action = "priceCategoryDesignerAction";
  private String parameterNew = "newPriceCategory";
  private String parameterDelete = "deletePriceCategory";
  private String parameterCloserLook = "viewPriceCategory";
  private String parameterSave = "savePriceCategory";
  private String parameterUpdate = "updatePriceCategory";
  private String parameterClose = "closeWindow";

  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
  private int iCatId;
  private String sCatId;

  boolean fillForm = false;
  boolean newCat = false;

  Text tName;
  Text tDescription;
  Text tExtraInfo;
  Text tOnline;
  Text tType;
  Text tPrice;
  Text tDiscountOf;

  private PriceCategory priceCategory;


  public PriceCategoryDesigner() {
    super.setHeight(400);
    super.setWidth(650);
  }

  public void main(ModuleInfo modinfo) {
    super.main(modinfo);

    if (checkForEverything(modinfo)) {
      String sAction = modinfo.getParameter(action);
      if (sAction == null) sAction = "";

      System.err.println("Action = "+sAction);
      if ( (sAction.equals("")) || (sAction.equals(this.parameterCloserLook)) ){
        if (sAction.equals(this.parameterCloserLook)) fillForm = true;
        mainMenu(modinfo);
      }else if (sAction.equals(parameterNew)) {
        newCat = true;
        mainMenu(modinfo);
      }else if (sAction.equals(parameterDelete)) {
        delete();
        mainMenu(modinfo);
      }else if (sAction.equals(parameterSave)) {
        saveUpdate(modinfo,false);
        mainMenu(modinfo);
      }else if (sAction.equals(parameterUpdate)) {
        saveUpdate(modinfo,true);
        mainMenu(modinfo);
      }else if (sAction.equals(parameterClose)) {
        super.close(true);
      }


    }
  }


  public void mainMenu(ModuleInfo modinfo) {
      Form form = new Form();
        form.add(getHeaderTable(modinfo));

      HorizontalRule hr = new HorizontalRule();
        hr.setWidth("90%");
      add(form);
      add(hr);

      if (newCat || fillForm)
      add(getBodyForm(modinfo));

      Paragraph p = new Paragraph("right");
      SubmitButton close = new SubmitButton(iwrb.getImage("/buttons/close.gif"),this.action,this.parameterClose );
      p.add(close);
      form.add(p);
  }

  public Table getHeaderTable(ModuleInfo modinfo) {
      Table table = new Table();
        table.setAlignment("center");
        table.setWidth("90%");

      PriceCategory[] categories = TravelStockroomBusiness.getPriceCategories(this.supplierId);
      DropdownMenu cats = new DropdownMenu(categories,"price_category_id");
        cats.setSelectedElement(sCatId);

      Text tPriceCategory = (Text) text.clone();
        tPriceCategory.setFontSize(Text.FONT_SIZE_12_HTML_3);
        tPriceCategory.setBold();
        tPriceCategory.setText("T - PriceCategory");

      SubmitButton sNew = new SubmitButton("T-nýr",action, parameterNew);
      SubmitButton sDel = new SubmitButton(iwrb.getImage("/buttons/delete.gif"),action, parameterDelete);
      SubmitButton sClo= new SubmitButton(iwrb.getImage("/buttons/closer.gif"),action, parameterCloserLook);

      table.add(tPriceCategory,1,1);
      table.mergeCells(1,1,3,1);

      table.add(cats,1,2);
      table.mergeCells(1,2,2,2);
      table.add(sClo,3,2);

      table.add(sNew,1,3);
      table.add(sDel,3,3);

      table.setAlignment(3,3,"right");

    return table;
  }

  private Form getBodyForm(ModuleInfo modinfo) {
    Form form = new Form();
    Table table = new Table();
      form.add(table);
      table.setWidth("90%");
      table.setAlignment("center");


      TextInput tiName = new TextInput("name");
      BooleanInput tiOnline = new BooleanInput("online");
        tiOnline.setSelected(true);

      TextInput tiDescription = new TextInput("description");

      DropdownMenu tiType = new DropdownMenu("price_type");
            tiType.addMenuElement(PriceCategory.PRICETYPE_PRICE,"Verð");
            tiType.addMenuElement(PriceCategory.PRICETYPE_DISCOUNT,"Afsláttur");

      TextArea tiExtraInfo = new TextArea("extra_info");
        tiExtraInfo.setHeight(4);
        tiExtraInfo.setWidth(60);

//      TextInput tiPrice = new TextInput("price");
      DropdownMenu tiDiscountOf = new DropdownMenu(TravelStockroomBusiness.getPriceCategories(this.supplierId),"discount_of");
          tiDiscountOf.addMenuElementFirst("-1","T-Ekkert");

      SubmitButton saveUpdate = new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.action, this.parameterSave);

    if (this.priceCategory != null) {
      if (fillForm) {
        saveUpdate = new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.action, this.parameterUpdate);
        tiName.setContent(this.priceCategory.getName());
        tiOnline.setSelected(this.priceCategory.isNetbookingCategory());
        tiDescription.setContent(this.priceCategory.getDescription());
        tiType.setSelectedElement(this.priceCategory.getType());
        tiExtraInfo.setContent(this.priceCategory.getExtraInfo());
        //tiPrice.setContent("fix");
        tiDiscountOf.setSelectedElement(Integer.toString(this.priceCategory.getParentId()));
        table.add(new HiddenInput("price_category_id_to_update",Integer.toString(this.priceCategory.getID())));
      }
    }


      table.add(this.tName,1,1);
      table.add(tiName,2,1);
      table.add(this.tOnline,3,1);
      table.add(tiOnline,4,1);
      table.add(this.tDescription,1,2);
      table.add(tiDescription,2,2);
      table.add(this.tType,3,2);
      table.add(tiType,4,2);
      table.add(this.tExtraInfo,1,3);
      table.add(Text.getBreak(),1,3);
      table.add(tiExtraInfo,1,3);
      table.mergeCells(1,3,4,3);
//      table.add(this.tPrice,1,4);
//      table.add(tiPrice,2,4);
      table.add(this.tDiscountOf,3, 4);
      table.add(tiDiscountOf,4,4);

      table.add(saveUpdate,4,6);
      table.setAlignment(4,6,"right");

    return form;
  }

  private boolean checkForEverything(ModuleInfo modinfo) {
    boolean returner = true;
    try {
      String service_id = modinfo.getParameter(SERVICE_ID_PARAMETER_NAME);
      this.supplierId = TravelStockroomBusiness.getUserSupplierId(modinfo);

      iCatId = -1;
      sCatId = modinfo.getParameter("price_category_id");
      if (sCatId != null) {
        iCatId = Integer.parseInt(sCatId);
        priceCategory = new PriceCategory(iCatId);
      }else {
        sCatId = "";
        priceCategory = null;
      }

      tName = (Text) text.clone();
      tDescription = (Text) text.clone();
      tExtraInfo = (Text) text.clone();
      tOnline = (Text) text.clone();
      tType = (Text) text.clone();
      tPrice = (Text) text.clone();
      tDiscountOf = (Text) text.clone();
        tName.setBold();
        tDescription.setBold();
        tExtraInfo.setBold();
        tOnline.setBold();
        tType.setBold();
        tPrice.setBold();
        tDiscountOf.setBold();

        tName.setText("T - tName");
        tDescription.setText("T - tDescription");
        tExtraInfo.setText("T - tExtraInfo");
        tOnline.setText("T - tOnline");
        tType.setText("T - tType");
        tPrice.setText("T - tPrice");
        tDiscountOf.setText("T - tDiscountOf");


    }catch (Exception e) {
      e.printStackTrace(System.err);
      returner = false;
    }
    return returner;
  }

  private void delete() {
    this.priceCategory.delete();
    this.priceCategory = null;
    this.sCatId = null;
    this.iCatId = -1;
  }

  private void saveUpdate(ModuleInfo modinfo, boolean isUpdate) {
    System.err.println("er hér - -1");

      String name =   modinfo.getParameter("name");
      String desc =   modinfo.getParameter("description");
      String online = modinfo.getParameter("online");
      String type =   modinfo.getParameter("price_type");
      String info =   modinfo.getParameter("extra_info");
      String discountOf = modinfo.getParameter("discount_of");

      if (isUpdate) {
          String updatePriceCategoryId = modinfo.getParameter("price_category_id_to_update");
          try {
            boolean bOnline;
                if (online.equals("Y")) {
                    bOnline = true;
                }else {
                  bOnline = false;
                }

            PriceCategory pCat = new PriceCategory(Integer.parseInt(updatePriceCategoryId));
              pCat.setName(name);
              pCat.setDescription(desc);
              pCat.setType(type);
              if (type.equals(PriceCategory.PRICETYPE_DISCOUNT)) {
                pCat.setParentId(Integer.parseInt(discountOf));
              }
              pCat.setSupplierId(supplierId);
              pCat.setExtraInfo(info);
              pCat.isNetbookingCategory(bOnline);
            pCat.update();

          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
          }

      }
      else {

          try {
            if ((name != null) && (!name.equals(""))){
              int priceCategoryId = 0;
              int parentId;
              boolean bOnline;
                  if (online.equals("Y")) {
                      bOnline = true;
                  }else {
                    bOnline = false;
                  }

                  if (type.equals(PriceCategory.PRICETYPE_DISCOUNT)) {
                    parentId = Integer.parseInt(discountOf);
                    priceCategoryId = tsb.createPriceCategory(supplierId, name, desc,type, info, bOnline, parentId);
                  }else if (type.equals(PriceCategory.PRICETYPE_PRICE)) {
                    priceCategoryId = tsb.createPriceCategory(supplierId, name, desc,type, info, bOnline);
                  }
            }

          }catch (Exception e) {
            e.printStackTrace(System.err);
          }
      }

  }


}