package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;

import java.rmi.RemoteException;

import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.PriceCategoryHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
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
        onlineTxt.setText(iwrb.getLocalizedString("travel.visible","Visible"));

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
//      BooleanInput online;
//	DropdownMenu online = getVisibilityDropdown();
//	DropdownMenu ddType = getTypeDropdown();
//	DropdownMenu ddDisc = getDiscountDropdown(categories);

      CheckBox delete;

      for (int i = 0; i < categories.length; i++) {
        ++counter;
        ++row;
        numberTxt = (Text) super.smallText.clone();
          numberTxt.setFontColor(super.BLACK);
          numberTxt.setText(Integer.toString(counter));

        nameInp = new TextInput("priceCategoryName");
          nameInp.setContent(categories[i].getName());


        DropdownMenu online = getVisibilityDropdown();
          online.setSelectedElement(categories[i].getVisibility());
		DropdownMenu ddVis = (DropdownMenu) online.clone();
			ddVis.setSelectedElement(categories[i].getVisibility());

        DropdownMenu ddOne = getTypeDropdown();
          ddOne.setSelectedElement(categories[i].getType());

        DropdownMenu ddTwo = getDiscountDropdown(categories);
          ddTwo.setSelectedElement(Integer.toString(categories[i].getParentId()));

        delete = new CheckBox("priceCategoryToDelete_"+categories[i].getID());

        table.add(new HiddenInput(this.parameterPriceCategoryId,Integer.toString(categories[i].getID())));
        table.add(numberTxt,1,row);
        table.add(nameInp,2,row);
        table.add(ddVis,3,row);
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
//        online = new BooleanInput("priceCategoryOnline");
//ddVis = new DropdownMenu("priceCategoryVisible");
//      	ddVis.addMenuElement(PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE, iwrb.getLocalizedString("travel.web_only","Web only"));
//      	ddVis.addMenuElement(PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, iwrb.getLocalizedString("travel.online_only","Online only"));
//      	ddVis.addMenuElement(PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC, iwrb.getLocalizedString("travel.everywhere","Everywhere"));
				DropdownMenu ddVis = getVisibilityDropdown();
          ddVis.setSelectedElement(Integer.toString(PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE));
        DropdownMenu ddOne = getTypeDropdown();
        	ddOne.setSelectedElement(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE);
        DropdownMenu ddTwo = getDiscountDropdown(categories);
        	ddTwo.setSelectedElement(-1);

        table.add(new HiddenInput(this.parameterPriceCategoryId,"-1"));
        table.add(numberTxt,1,row);
        table.add(nameInp,2,row);
/*        table.add(PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE+" / "+ddVis.getSelectedElementValue(),3,row);
        table.add(ddVis,3,row);
        if ( this.miscellaneousServices ) {
          table.add(new HiddenInput(ddOne.getName(), com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE), 3, row);
          table.add(new HiddenInput(ddTwo.getName(), "-1"), 3, row);
          table.mergeCells(3, row, 5 ,row);
        }else {
          table.add(PriceCategoryBMPBean.PRICETYPE_PRICE+" / "+ddOne.getSelectedElementValue(),4,row);
          table.add(ddOne,4,row);
          table.add("-1 / "+ddTwo.getSelectedElementValue(),5,row);
          table.add(ddTwo,5,row);
        }*/

        table.add(ddVis,3,row);
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

	DropdownMenu a = new DropdownMenu("repps");
	    a.addMenuElement("1", "Númer 1");
	    a.addMenuElement("2", "Númer 2");

/*
	for (int c = 1; c < 2; c++) {
	    DropdownMenu r = (DropdownMenu) a.clone();
		r.setSelectedElement("1");
	    form.add(r);
	}

	for (int b = 1; b < 2; b++) {
	    DropdownMenu r = (DropdownMenu) a.clone();
		r.setSelectedElement("2");
	    form.add(r);
	}

				DropdownMenu ddVis1 = (DropdownMenu) online.clone();
          ddVis1.setSelectedElement(Integer.toString(PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE));
        form.add(PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE+" / "+ddVis1.getSelectedElementValue());
        form.add(ddVis1);
*/
      return form;
  }

private DropdownMenu getTypeDropdown() {
	  DropdownMenu ddType = new DropdownMenu("priceCategoryType");
	    ddType.addMenuElement(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE, iwrb.getLocalizedString("travel.price","Price"));
	    ddType.addMenuElement(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT, iwrb.getLocalizedString("travel.discount","Discount"));
	   return ddType;
}

private DropdownMenu getDiscountDropdown(PriceCategory[] categories) {
	  DropdownMenu ddDisc = new DropdownMenu(categories,"priceCategoryParent");
	    ddDisc.addMenuElementFirst("-1",Text.NON_BREAKING_SPACE);
	   return ddDisc;
}

public DropdownMenu getVisibilityDropdown() {
	  DropdownMenu online = new DropdownMenu("priceCategoryVisible");
	  	online.addMenuElement(PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE, iwrb.getLocalizedString("travel.web_only","Web only"));
	  	online.addMenuElement(PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, iwrb.getLocalizedString("travel.online_only","Online only"));
	  	online.addMenuElement(PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC, iwrb.getLocalizedString("travel.everywhere","Everywhere"));
	return online;
}



  public void savePriceCategories(IWContext iwc) {
    String[] catIds = iwc.getParameterValues(this.parameterPriceCategoryId);
    String[] names  = iwc.getParameterValues("priceCategoryName");
    String[] visible= iwc.getParameterValues("priceCategoryVisible");
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
            int visibility = -1;
              visibility = Integer.parseInt(visible[i]);
            boolean bOnline;

            if (type[i].equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)) {
              parentId = Integer.parseInt(parent[i]);
              priceCategoryId = tsb.createPriceCategory(supplier.getID(), names[i], "",type[i], "", visibility, parentId);
            }else if (type[i].equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)) {
              priceCategoryId = tsb.createPriceCategory(supplier.getID(), names[i], "",type[i], "", visibility);
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
            int visibility = -1;
              visibility = Integer.parseInt(visible[i]);

              pCat.setName(names[i]);
              pCat.setDescription("");
              pCat.setType(type[i]);
              if (type[i].equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)) {
                pCat.setParentId(Integer.parseInt(parent[i]));
              }
              pCat.setSupplierId(supplier.getID());
              pCat.setExtraInfo("");
              pCat.setVisibility(visibility);
              pCat.isNetbookingCategory(false);
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
