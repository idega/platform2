package com.idega.block.creditcard.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;
/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class CreditcardRefunder extends Block {

  private int dateInputSize = 3;
  private int ccInputSize = 19;
  private int amountInputSize = 10;

  private String sAction = "ccrAction";

  private String parameterVerify   = "ccrVerify";
  private String parameterComplete = "ccrSave";
  private String parameterCACertificate = "ccrCACert";
  private String parameterKeys = "ccrKeys";
  private String parameterNewBatch = "ccrNewBatch";

  private String parameterNumber = "ccrNumber";
  private String parameterYear   = "ccrYear";
  private String parameterMonth  = "ccrMonth";
  private String parameterAmount = "ccrAmount";
  private String parameterSupplier = "ccrSupplier";
  
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";

  private IWResourceBundle iwrb;

  public CreditcardRefunder() {
    //this.setTitle("idegaWeb Travel");
    //this.setStatus(true);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception {
    //super.main(iwc);
    
    iwrb = this.getResourceBundle(iwc);

    String action = iwc.getParameter(this.sAction);
    if (action == null) {
      getRefundForm();
    }else if (action.equals(this.parameterVerify)){
      verify(iwc);
    }else if (action.equals(this.parameterComplete)){
      complete(iwc);
    }else if (action.equals(this.parameterKeys)){
      keys(iwc);
      getRefundForm();
    }else if (action.equals(this.parameterCACertificate)){
      cacCertificate(iwc);
      getRefundForm();
    }else if (action.equals(this.parameterNewBatch)){
      newBatch(iwc);
      getRefundForm();
    }
  }

  protected Text getText(String content) {
    Text text = new Text(content);
      //text.setStyle(TravelManager.theBoldTextStyle);
      text.setBold();
    return text;
  }

  private void keys(IWContext iwc) {
    try {
      com.idega.block.creditcard.business.TPosClient t = new com.idega.block.creditcard.business.TPosClient(iwc);
      if (t.getKeys()) {
        add(getText("Key creation successful"));
      }else {
        add(getText("Key creation failed"));
      }
    }catch (Exception e) {
      add(getText("Key creation failed"));
      e.printStackTrace(System.err);
    }
  }

  private void cacCertificate(IWContext iwc) {
    try {
      com.idega.block.creditcard.business.TPosClient t = new com.idega.block.creditcard.business.TPosClient(iwc);
      if (t.getCACertificate()) {
        add(getText("CA certificate creation successful"));
      }else {
        add(getText("CA certificate creation failed"));
      }
    }catch (Exception e) {
      add(getText("CA certificate creation failed"));
      e.printStackTrace(System.err);
    }
  }

  private void newBatch(IWContext iwc) {
    try {
      com.idega.block.creditcard.business.TPosClient t = new com.idega.block.creditcard.business.TPosClient(iwc);
      String bNum = t.createNewBatch();
      if (bNum != null) {
        add(getText("New batch creation successful, batch number = "+bNum));
      }else {
        add(getText("New batch creation failed"));
      }
    }catch (Exception e) {
      add(getText("New batch creation failed"));
      e.printStackTrace(System.err);
    }
  }

  private void getRefundForm() {
    Form form = new Form();
    Table table = new Table();
    form.add(table);
    table.setAlignment("center");
    table.setCellpadding(2);

    Text refund = getText(iwrb.getLocalizedString("travel.refunds","Refunds"));
    Text merchant = getText(iwrb.getLocalizedString("travel.merchant", "Merchant"));
    Text ccNumber = getText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
    Text ccYear   = getText(iwrb.getLocalizedString("travel.year","Year"));
    Text ccMonth  = getText(iwrb.getLocalizedString("travel.month","Month"));
    Text amount   = getText(iwrb.getLocalizedString("travel.amount","Amount"));

    DropdownMenu merchantInp = getSupplierDropdown();
    TextInput ccNumberInp = new TextInput(this.parameterNumber);
      ccNumberInp.setSize(this.ccInputSize);
      ccNumberInp.setMaxlength(this.ccInputSize);
    TextInput ccYearInp = new TextInput(this.parameterYear);
      ccYearInp.setSize(this.dateInputSize);
      ccYearInp.setMaxlength(2);
    TextInput ccMonthInp = new TextInput(this.parameterMonth);
      ccMonthInp.setSize(this.dateInputSize);
      ccMonthInp.setMaxlength(2);
    TextInput amountInp = new TextInput(this.parameterAmount);
      amountInp.setSize(this.amountInputSize);


    int row = 1;
    table.mergeCells(1, row, 3, row);
    table.add(refund, 1, row);

    ++row;
    ++row;
    table.add(merchant, 1, row);
    table.add(merchantInp, 2, row);
    table.mergeCells(2, row, 3, row);
    ++row;
    table.add(ccNumber, 1, row);
    table.add(ccMonth, 2, row);
    table.add(ccYear, 3, row);

    ++row;
    table.add(ccNumberInp, 1, row);
    table.add(ccMonthInp, 2, row);
    table.add(ccYearInp, 3, row);

    ++row;
    ++row;
    table.mergeCells(1, row, 3, row);
    table.add(amount, 1, row);

    ++row;
    table.mergeCells(1, row, 3, row);
    table.add(amountInp, 1, row);

    ++row;
    ++row;
    table.mergeCells(2, row, 3, row);
    table.setAlignment(2, row, "right");
    table.add(new CloseButton(iwrb.getLocalizedImageButton("creditcard.close", "Close")),1 ,row);
    table.add(new SubmitButton(iwrb.getLocalizedImageButton("creditcard.save", "Save"), this.sAction, this.parameterVerify),2 ,row);

    add(Text.BREAK);
    add(form);

    if (this.hasEditPermission()) {
      Link keysLink = new Link(getText("Get Keys"));
        keysLink.addParameter(sAction, parameterKeys);

      Link CACLink = new Link(getText("Get CACertificate"));
        CACLink.addParameter(sAction, parameterCACertificate);

      Link newBatchLink = new Link(getText("Create New Batch"));
        newBatchLink.addParameter(sAction, parameterNewBatch);

      add(Text.BREAK);
      add("Temporary : should only be used with TPOS");
      add(Text.BREAK);
      add(CACLink);
      add(Text.BREAK);
      add(keysLink);
      add(Text.BREAK);
      add(newBatchLink);
    }

  }

  private void verify(IWContext iwc) {
    String number = iwc.getParameter(this.parameterNumber);
    String supplier = iwc.getParameter(this.parameterSupplier);
    String year   = iwc.getParameter(this.parameterYear);
    String month  = iwc.getParameter(this.parameterMonth);
    String amount = iwc.getParameter(this.parameterAmount);
	  amount = TextSoap.findAndReplace(amount,',','.');

    Text ccNumber = getText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
    Text merchant   = getText(iwrb.getLocalizedString("travel.merchant","Merchant"));
    Text ccYear   = getText(iwrb.getLocalizedString("travel.year","Year"));
    Text ccMonth  = getText(iwrb.getLocalizedString("travel.month","Month"));
    Text ccAmount   = getText(iwrb.getLocalizedString("travel.amount","Amount"));
    Text notANumber = getText("X");
	  notANumber.setFontColor("RED");

    Table table = new Table();
	  table.setAlignment("center");
	  table.setCellpadding(2);


    int row = 1;
    boolean error = false;
    table.mergeCells(1,row,3,row);
    table.add(getText(iwrb.getLocalizedString("travel.is_information_correct","Is the following information correct ?")), 1, row);

    ++row;
    ++row;
    table.add(merchant, 2, row);
    table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
    if (getSupplier(supplier) != null) {
    		table.add(getText(getSupplier(supplier).getName()), 3, row);
    } else {
    		table.add(getText(iwrb.getLocalizedString("travel.default", "Default")), 3, row);
    }
    ++row;
    table.add(ccNumber,2,row);
    table.add(number,3,row);
    table.setAlignment(3, row, "right");
    try {
      Long.parseLong(number);
    }catch (NumberFormatException n) {
      //table.add(getText(Text.NON_BREAKING_SPACE),3,row);
      table.add(notANumber,4,row);
      error = true;
    }

    ++row;
    table.add(ccMonth,2,row);
    table.add(month,3,row);
    table.setAlignment(3, row, "right");
    try {
      if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
        throw new NumberFormatException();
      }
    }catch (NumberFormatException n) {
      table.add(notANumber,4,row);
      error = true;
    }

    ++row;
    table.add(ccYear,2,row);
    table.add(year,3,row);
    table.setAlignment(3, row, "right");
    try {
      Integer.parseInt(year);
    }catch (NumberFormatException n) {
      table.add(notANumber,4,row);
      error = true;
    }


    ++row;
    ++row;
    table.add(ccAmount,2,row);
    table.add(amount,3,row);
    table.setAlignment(3, row, "right");
    try {
      Float.parseFloat(amount);
    }catch (NumberFormatException n) {
      //table.add(getText(Text.NON_BREAKING_SPACE),3,row);
      table.add(notANumber,4,row);
      error = true;
    }

    ++row;
    ++row;
    table.mergeCells(1, row, 2, row);
    table.setAlignment(3, row, "right");
    table.add(new BackButton(iwrb.getLocalizedImageButton("creditcard.no", "No")),1 ,row);
    Link link = new Link(iwrb.getLocalizedImageButton("creditcard.yes", "Yes"));
	  link.addParameter(this.sAction, this.parameterComplete);
	  link.addParameter(this.parameterNumber, number);
	  link.addParameter(this.parameterYear, year);
	  link.addParameter(this.parameterMonth, month);
	  link.addParameter(this.parameterAmount, amount);
	  link.addParameter(this.parameterSupplier, supplier);
    if (!error)
    		table.add(link, 3, row);

    add(Text.BREAK);
    add(table);

  }

  private void complete(IWContext iwc) {
    String number = iwc.getParameter(this.parameterNumber);
    String year   = iwc.getParameter(this.parameterYear);
    String month  = iwc.getParameter(this.parameterMonth);
    
    String amount = iwc.getParameter(this.parameterAmount);
    String supplier = iwc.getParameter(this.parameterSupplier);


    Table table = new Table();
      table.setAlignment("center");
      table.setCellpadding(2);

      try{
        System.out.println("Starting TPOS test : "+IWTimestamp.RightNow().toString());
        CreditCardClient t = getCreditCardBusiness(iwc).getCreditCardClient(getSupplier(supplier), IWTimestamp.RightNow());
        /*
        if (getSupplier(supplier) == null) {
        	t = new TPosClient(iwc);
        } else {
    		t = new TPosClient(iwc, getSupplier(supplier).getTPosMerchant());
        }*/

        // TODO b¾ta viÝ CVC d—ti
        
        String heimild = t.doRefund(number,month,year, null, Float.parseFloat(amount),"ISK", null, null);
        System.out.println("Ending TPOS test : "+IWTimestamp.RightNow().toString());

        int row = 1;
        table.add(getText(iwrb.getLocalizedString("travel.success","Success")),1,row);
        table.mergeCells(1,row,2,row);
        ++row;
        ++row;
        table.add(getText(iwrb.getLocalizedString("travel.credidcard_authorization_number","Creditcard authorization number")),1,row);
        table.add(getText(heimild),2, row);
        table.setAlignment(2, row, "right");

        ++row;
        ++row;
        table.setAlignment(2, row, "right");
        table.add(new Link(iwrb.getLocalizedImageButton("creditcard.back", "Back")),1,row);
        table.add(new CloseButton(iwrb.getLocalizedImageButton("creditcard.close", "Close")),2,row);
      }
      catch(com.idega.block.creditcard.business.TPosException e) {
        System.out.println("TPOS errormessage = " + e.getErrorMessage());
        String errMsge = e.getErrorMessage();
        String errNumb = e.getErrorNumber();
        String display = e.getDisplayError();

        int row = 1;
        table.add(getText(iwrb.getLocalizedString("travel.error","Error")),1,row);
        table.mergeCells(1,row,2,row);
        ++row;
        ++row;
        table.add(getText(iwrb.getLocalizedString("travel.error_message","Error message")),1,row);
        table.add(getText(errMsge),2, row);

        ++row;
        table.add(getText(iwrb.getLocalizedString("travel.error_number","Error number")),1,row);
        table.add(getText(errNumb),2, row);

        ++row;
        table.add(getText(iwrb.getLocalizedString("travel.display_error","Display error")),1,row);
        table.add(getText(display),2, row);

        ++row;
        ++row;
        table.setAlignment(2, row, "right");
        table.add(new BackButton(iwrb.getLocalizedImageButton("creditcard.back", "Back")),1,row);
        table.add(new CloseButton(iwrb.getLocalizedImageButton("creditcard.close", "Close")),2,row);
        //table.add(new BackButton(iwrb.getImage("buttons/back.gif")),1,row);
        //table.add(new CloseButton(iwrb.getImage("buttons/close.gif")),2,row);
      }
      catch (Exception e) {
      	int row = 1;
				++row;
				table.add(getText(iwrb.getLocalizedString("travel.unknown_error","Unknown error")),1,row);
				table.mergeCells(1, row, 2, row);
				++row;
				++row;
				table.setAlignment(2, row, "right");
        table.add(new BackButton(iwrb.getLocalizedImageButton("creditcard.back", "Back")),1,row);
        table.add(new CloseButton(iwrb.getLocalizedImageButton("creditcard.close", "Close")),2,row);
				//table.add(new BackButton(iwrb.getImage("buttons/back.gif")),1,row);
				//table.add(new CloseButton(iwrb.getImage("buttons/close.gif")),2,row);
        e.printStackTrace(System.err);
      }

      add(Text.BREAK);
      add(table);
  }

  private DropdownMenu getSupplierDropdown() {
	  	DropdownMenu menu = new DropdownMenu(parameterSupplier);
	  	menu.addMenuElement(-1, iwrb.getLocalizedString("travel.default", "Default"));
	  	try {
	  		SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
	  		Collection coll = sHome.findWithTPosMerchant();
	  		Supplier supp;
	  		if (coll != null && !coll.isEmpty()) {
	  			Iterator iter = coll.iterator();
	  			while (iter.hasNext()) {
	  				supp = (Supplier) iter.next();
	  				menu.addMenuElement(supp.getID(), supp.getName());
	  			}
	  		}
	  	}catch (Exception e) {
	  		e.printStackTrace();
	  	}
	  	
	  	return menu;
  }
  
  private Supplier getSupplier(String supplierID) {
	  	try {
	  		int suppID = Integer.parseInt(supplierID);
	  		if (suppID > 0) {
	  			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
	  			return sHome.findByPrimaryKey(new Integer(supplierID));
	  		}
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  	}
	  	return null;
  }
  
  protected CreditCardBusiness getCreditCardBusiness(IWContext iwc) {
	  	try {
	  		return (CreditCardBusiness) IBOLookup.getServiceInstance(iwc, CreditCardBusiness.class);
	  	} catch (IBOLookupException rt) {
	  		throw new IBORuntimeException();
	  	}
  }
}
