package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import java.rmi.RemoteException;

import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.PriceCategoryHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class PriceCategoryDesigner extends TravelManager {
  private IWResourceBundle iwrb;
  private Supplier supplier;
  private TravelStockroomBusiness tsb;

  private static String parameterSavePriceCategories = "p_sv_pcs";
  private static String parameterPriceCategoryId = "p_pr_cId";
  private String parameterMiscellaneousServices = "p_mc_sr";

  private static String sAction = "a_pcd";
  private boolean miscellaneousServices = false;

  public PriceCategoryDesigner(IWContext iwc) throws Exception {
    super.main(iwc);
    iwrb = super.getResourceBundle();
    supplier = super.getSupplier();
    tsb = getTravelStockroomBusiness(iwc);
  }

  public void handleInsert(IWContext iwc) {
    String action = iwc.getParameter(sAction);
    if (action != null) {
      if (action.equals(parameterSavePriceCategories)) {
        savePriceCategories(iwc);
      }
    }
  }

  public Form getPriceCategoriesForm(int supplierId) throws RemoteException{
    int extraRows = 3;

    Form form = new Form();

    Table table = new Table();
      form.add(table);
      table.setColor(super.WHITE);
      table.setCellspacing(1);

    PriceCategory[] categories = {};
    if (this.miscellaneousServices) {
      categories = tsb.getMiscellaneousServices(supplierId);
    }else {
      categories = tsb.getPriceCategories(supplierId);
    }
      Text nameTxt = (Text) theText.clone();
        nameTxt.setFontColor(super.WHITE);
        nameTxt.setBold();
        nameTxt.setText(iwrb.getLocalizedString("travel.name","Name"));

      Text onlineTxt = (Text)  theText.clone();
        onlineTxt.setFontColor(super.WHITE);
        onlineTxt.setBold();
        onlineTxt.setText(iwrb.getLocalizedString("travel.online","Online"));

      Text typeTxt = (Text)  theText.clone();
        typeTxt.setFontColor(super.WHITE);
        typeTxt.setBold();
        typeTxt.setText(iwrb.getLocalizedString("travel.type","Type"));

      Text discOfTxt = (Text)  theText.clone();
        discOfTxt.setFontColor(super.WHITE);
        discOfTxt.setBold();
        discOfTxt.setText(iwrb.getLocalizedString("travel.discount_of","Discount of"));

      Text deleteTxt = (Text)  theText.clone();
        deleteTxt.setFontColor(super.WHITE);
        deleteTxt.setBold();
        deleteTxt.setText(iwrb.getLocalizedString("travel.delete","Delete"));

      int row = 1;
      int counter = 0;

      table.add(nameTxt,2,row);
      table.add(onlineTxt,3,row);
      if (this.miscellaneousServices) {
        table.mergeCells(3, row, 5, row);
      }else {
        table.add(typeTxt,4,row);
        table.add(discOfTxt,5,row);
      }
      table.add(deleteTxt,6,row);
      table.setRowColor(row,super.backgroundColor);

      Text numberTxt;
      TextInput nameInp;
      BooleanInput online;
      DropdownMenu ddType = new DropdownMenu("priceCategoryType");
        ddType.addMenuElement(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE, iwrb.getLocalizedString("travel.price","Price"));
        ddType.addMenuElement(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT, iwrb.getLocalizedString("travel.discount","Discount"));
      DropdownMenu ddDisc = new DropdownMenu(categories,"priceCategoryParent");
        ddDisc.addMenuElementFirst("-1",Text.NON_BREAKING_SPACE);

      DropdownMenu ddOne;
      DropdownMenu ddTwo;

      CheckBox delete;

      for (int i = 0; i < categories.length; i++) {
        ++counter;
        ++row;
        numberTxt = (Text) super.smallText.clone();
          numberTxt.setFontColor(super.BLACK);
          numberTxt.setText(Integer.toString(counter));

        nameInp = new TextInput("priceCategoryName");
          nameInp.setContent(categories[i].getName());

        online = new BooleanInput("priceCategoryOnline");
          online.setSelected(categories[i].isNetbookingCategory());

        ddOne = (DropdownMenu) ddType.clone();
          ddOne.setSelectedElement(categories[i].getType());

        ddTwo = (DropdownMenu) ddDisc.clone();
          ddTwo.setSelectedElement(Integer.toString(categories[i].getParentId()));

        delete = new CheckBox("priceCategoryToDelete_"+categories[i].getID());

        table.add(new HiddenInput(this.parameterPriceCategoryId,Integer.toString(categories[i].getID())));
        table.add(numberTxt,1,row);
        table.add(nameInp,2,row);
        table.add(online,3,row);
        if ( this.miscellaneousServices ) {
          table.add(new HiddenInput(ddOne.getName(), com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE), 3, row);
          table.add(new HiddenInput(ddTwo.getName(), "-1"), 3, row);
          table.mergeCells(3, row, 5 ,row);
        }else {
          table.add(ddOne,4,row);
          table.add(ddTwo,5,row);
        }
        table.add(delete,6,row);
        table.setRowColor(row,super.GRAY);
      }
      for (int i = 0; i < extraRows; i++) {
        ++counter;
        ++row;
        numberTxt = (Text) super.smallText.clone();
          numberTxt.setFontColor(super.BLACK);
          numberTxt.setText(Integer.toString(counter));
        nameInp = new TextInput("priceCategoryName");
        online = new BooleanInput("priceCategoryOnline");
          online.setSelected(true);
        ddOne = (DropdownMenu) ddType.clone();
        ddTwo = (DropdownMenu) ddDisc.clone();

        table.add(new HiddenInput(this.parameterPriceCategoryId,"-1"));
        table.add(numberTxt,1,row);
        table.add(nameInp,2,row);
        table.add(online,3,row);
        if (this.miscellaneousServices) {
          table.add(new HiddenInput(ddOne.getName(), com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE), 3, row);
          table.add(new HiddenInput(ddTwo.getName(), "-1"), 3, row);
          table.mergeCells(3, row, 5, row);
        }else {
          table.add(ddOne,4,row);
          table.add(ddTwo,5,row);
        }
        table.setRowColor(row,super.GRAY);
      }
      ++row;
      table.setRowColor(row,super.GRAY);
      SubmitButton lSave = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.sAction, this.parameterSavePriceCategories);
      table.mergeCells(1,row,6,row);
      table.setColumnAlignment(1,"center");
      table.setColumnAlignment(6,"center");
      table.setWidth(1,row-1,"15");
      table.setAlignment(1,row,"right");
      if (super.isInPermissionGroup) {
        table.add(lSave,1,row);
      }


      return form;
  }



  public void savePriceCategories(IWContext iwc) {
    String[] catIds = iwc.getParameterValues(this.parameterPriceCategoryId);
    String[] names  = iwc.getParameterValues("priceCategoryName");
    String[] online = iwc.getParameterValues("priceCategoryOnline");
    String[] type   = iwc.getParameterValues("priceCategoryType");
    String[] parent = iwc.getParameterValues("priceCategoryParent");


    PriceCategoryHome pCatHome = (PriceCategoryHome) IDOLookup.getHomeLegacy(PriceCategory.class);
    PriceCategory pCat;
    try {
      for (int i = 0; i < catIds.length; i++) {
        if (catIds[i].equals("-1")) {   //NEW
          if ((names[i] != null) && (!names[i].equals(""))){
            int priceCategoryId = 0;
            int parentId;
            boolean bOnline;
            if (online[i].equals("Y")) {
                bOnline = true;
            }else {
              bOnline = false;
            }

            if (type[i].equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)) {
              parentId = Integer.parseInt(parent[i]);
              priceCategoryId = tsb.createPriceCategory(supplier.getID(), names[i], "",type[i], "", bOnline, parentId);
            }else if (type[i].equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)) {
              priceCategoryId = tsb.createPriceCategory(supplier.getID(), names[i], "",type[i], "", bOnline);
            }

            if (this.miscellaneousServices) {
              pCat = pCatHome.findByPrimaryKeyLegacy(priceCategoryId);
              pCat.setCountAsPerson(false);
              pCat.update();
            }
          }
        }else {   //UPDATE
          String del = iwc.getParameter("priceCategoryToDelete_"+catIds[i]);
          pCat = pCatHome.findByPrimaryKeyLegacy(Integer.parseInt(catIds[i]));
          if (del != null) {
            pCat.delete();
          }else {
            boolean bOnline;
            if (online[i].equals("Y")) {
                bOnline = true;
            }else {
              bOnline = false;
            }

              pCat.setName(names[i]);
              pCat.setDescription("");
              pCat.setType(type[i]);
              if (type[i].equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)) {
                pCat.setParentId(Integer.parseInt(parent[i]));
              }
              pCat.setSupplierId(supplier.getID());
              pCat.setExtraInfo("");
              pCat.isNetbookingCategory(bOnline);
              if (this.miscellaneousServices) {
                pCat.setCountAsPerson(false);
              }else {
                pCat.setCountAsPerson(true);
              }
            pCat.update();
          }
        }
      }

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void setMiscellaneousServices(boolean misc) {
    this.miscellaneousServices = misc;
  }

}
