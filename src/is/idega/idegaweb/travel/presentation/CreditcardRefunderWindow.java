package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.util.*;
import com.idega.util.text.*;
/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class CreditcardRefunderWindow extends TravelWindow {

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

  public CreditcardRefunderWindow() {
    this.setTitle("idegaWeb Travel");
    this.setStatus(true);
  }

  public void main(IWContext iwc){
    super.main(iwc);

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
      text.setStyle(TravelManager.theBoldTextStyle);
      text.setBold();
    return text;
  }

  private void keys(IWContext iwc) {
    try {
      com.idega.block.tpos.business.TPosClient t = new com.idega.block.tpos.business.TPosClient(iwc);
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
      com.idega.block.tpos.business.TPosClient t = new com.idega.block.tpos.business.TPosClient(iwc);
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
      com.idega.block.tpos.business.TPosClient t = new com.idega.block.tpos.business.TPosClient(iwc);
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
    Text ccNumber = getText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
    Text ccYear   = getText(iwrb.getLocalizedString("travel.year","Year"));
    Text ccMonth  = getText(iwrb.getLocalizedString("travel.month","Month"));
    Text amount   = getText(iwrb.getLocalizedString("travel.amount","Amount"));

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
    table.add(new CloseButton(iwrb.getImage("buttons/close.gif")),1 ,row);
    table.add(new SubmitButton(iwrb.getImage("buttons/save.gif"), this.sAction, this.parameterVerify),2 ,row);

    add(Text.BREAK);
    add(form);

    if (super.isSuperAdmin) {
      Link keysLink = new Link(getText("Get Keys"));
        keysLink.addParameter(sAction, parameterKeys);

      Link CACLink = new Link(getText("Get CACertificate"));
        CACLink.addParameter(sAction, parameterCACertificate);

      Link newBatchLink = new Link(getText("Create New Batch"));
        newBatchLink.addParameter(sAction, parameterNewBatch);


      add(CACLink);
      add(Text.BREAK);
      add(keysLink);
      add(Text.BREAK);
      add(newBatchLink);
    }

  }

  private void verify(IWContext iwc) {
    String number = iwc.getParameter(this.parameterNumber);
    String year   = iwc.getParameter(this.parameterYear);
    String month  = iwc.getParameter(this.parameterMonth);
    String amount = iwc.getParameter(this.parameterAmount);
      amount = TextSoap.findAndReplace(amount,',','.');

    Text ccNumber = getText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
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
    table.add(new BackButton(iwrb.getImage("buttons/no.gif")),1 ,row);
    Link link = new Link(iwrb.getImage("buttons/yes.gif"));
      link.addParameter(this.sAction, this.parameterComplete);
      link.addParameter(this.parameterNumber, number);
      link.addParameter(this.parameterYear, year);
      link.addParameter(this.parameterMonth, month);
      link.addParameter(this.parameterAmount, amount);
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


    Table table = new Table();
      table.setAlignment("center");
      table.setCellpadding(2);

      try{
        System.out.println("Starting TPOS test : "+idegaTimestamp.RightNow().toString());
        com.idega.block.tpos.business.TPosClient t = new com.idega.block.tpos.business.TPosClient(iwc);
        String heimild = t.doRefund(number,month,year,Float.parseFloat(amount),"ISK");
        System.out.println("Ending TPOS test : "+idegaTimestamp.RightNow().toString());

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
        table.add(new Link(iwrb.getImage("buttons/back.gif")),1,row);
        table.add(new CloseButton(iwrb.getImage("buttons/close.gif")),2,row);
      }
      catch(com.idega.block.tpos.business.TPosException e) {
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
        table.add(new BackButton(iwrb.getImage("buttons/back.gif")),1,row);
        table.add(new CloseButton(iwrb.getImage("buttons/close.gif")),2,row);
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }

      add(Text.BREAK);
      add(table);
  }

}
