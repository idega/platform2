package com.idega.block.finance.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.TariffKey;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class AccountKeyEditor extends Finance {

  public String strAction = "ake_action";
  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;

  public String getLocalizedNameKey(){
    return "accountkey";
  }

  public String getLocalizedNameValue(){
    return "Accountkey";
  }

   protected void control(IWContext iwc){

   if(isAdmin){

      try{
        PresentationObject MO = new Text();

        if(iwc.getParameter(strAction) == null){
          MO = getMain(iwc);
        }
        if(iwc.getParameter(strAction) != null){
          String sAct = iwc.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO = getMain(iwc);        break;
            case ACT2 : MO = getChange(iwc);      break;
            case ACT3 : MO = doUpdate(iwc);      break;
            default: MO = getMain(iwc);           break;
          }
        }
        Table T = new Table(1,3);
        add(Edit.headerText(iwrb.getLocalizedString("account_key_editor","Account key editor"),3));
        T.add(makeLinkTable(1,iCategoryId));
        T.add(MO);
        T.setWidth("100%");
        add(T);

      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else
      add(iwrb.getLocalizedString("access_denied","Access denies"));
  }

  protected PresentationObject makeLinkTable(int menuNr,int iCategoryId){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(Edit.colorDark);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link(iwrb.getLocalizedString("view","View"));
    Link1.setFontColor(Edit.colorLight);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
    Link Link2 = new Link(iwrb.getLocalizedString("change","Change"));
    Link2.setFontColor(Edit.colorLight);
    Link2.addParameter(strAction,String.valueOf(this.ACT2));
    Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  private PresentationObject getMain(IWContext iwc){
    Table keyTable = new Table();
    Collection keys = null;
	java.util.Map hk = null;
	try {
		keys = getFinanceService().getAccountKeyHome().findByCategory(getFinanceCategoryId());;
		hk = getFinanceService().getAccountBusiness().mapOfTariffKeys();
	} catch (RemoteException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}
    //List keys = FinanceFinder.getInstance().listOfAccountKeys(iCategoryId);
    if(keys !=null && hk!=null){
      int count = keys.size();
      keyTable = new Table(4,count+1);
      keyTable.setWidth("100%");
      keyTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
      keyTable.setRowColor(1,Edit.colorMiddle);
      keyTable.setCellpadding(2);
      keyTable.setCellspacing(1) ;
      keyTable.add(Edit.formatText("Nr"),1,1);
      keyTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);
      keyTable.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),3,1);
      keyTable.add(Edit.formatText(iwrb.getLocalizedString("tariff_key","Tariff key")),4,1);

      //java.util.Map hk = FinanceFinder.getInstance().mapOfTariffKeys(iCategoryId);
     
      if(isAdmin){
        if(count > 0){
          AccountKey key;
          int row = 2;
          int rowcount = 1;
          for (Iterator iter = keys.iterator(); iter.hasNext();) {
          	key = (AccountKey) iter.next();
            keyTable.add(Edit.formatText(String.valueOf(rowcount++)),1,row);
            keyTable.add(Edit.formatText(key.getName()),2,row);
            keyTable.add(Edit.formatText(key.getInfo()),3,row);
            Integer tkid = new Integer(key.getTariffKeyId());
            if(hk.containsKey(tkid))
              keyTable.add( Edit.formatText( ((TariffKey)hk.get( tkid)).getName() ),4,row);
            row++;
          }
        }
      }
    }
    return(keyTable);
  }

  private PresentationObject getChange(IWContext iwc) throws SQLException{
    Form myForm = new Form();
    myForm.maintainAllParameters();
    Collection keys = null;
	Collection Tkeys = null;
	try {
		keys = getFinanceService().getAccountKeyHome().findByCategory(getFinanceCategoryId());
		Tkeys = getFinanceService().getTariffKeyHome().findByCategory(getFinanceCategoryId());
	} catch (RemoteException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}
    int count = 0;
    if(keys !=null && Tkeys!=null)
       count = keys.size();
    int inputcount = count+5;
    Table inputTable =  new Table(5,inputcount+1);
    inputTable.setWidth("100%");
    //inputTable.setWidth(1,"15");
    inputTable.setCellpadding(2);
    inputTable.setCellspacing(1);
    //inputTable.setColumnAlignment(1,"right");
    inputTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    inputTable.setRowColor(1,Edit.colorMiddle);
    inputTable.add(Edit.formatText("Nr"),1,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("info","Inro")),3,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("tariff_key","Tariff key")),4,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("delete","Delete")),5,1);
    AccountKey key;
    Iterator iter = keys.iterator();
    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      TextInput nameInput, infoInput;
      HiddenInput idInput;
      CheckBox delCheck;
      DropdownMenu iDrp =  keyDrp(Tkeys);
      iDrp.setName("ake_keydrp"+i);
      Edit.setStyle(iDrp);
      int pos;
      if(i <= count && iter.hasNext()){
        pos = i-1;
        key  = (AccountKey) iter.next();
        nameInput  = new TextInput("ake_nameinput"+i,(key.getName()));
        infoInput = new TextInput("ake_infoinput"+i,(key.getInfo()));
        String sId = key.getPrimaryKey().toString();
        idInput = new HiddenInput("ake_idinput"+i,sId);
        delCheck = new CheckBox("ake_delcheck"+i,"true");
        iDrp.setSelectedElement(String.valueOf(key.getTariffKeyId()));
        Edit.setStyle(delCheck);
        inputTable.add(delCheck,5,i+1);
      }
      else{
        nameInput  = new TextInput("ake_nameinput"+i);
        infoInput = new TextInput("ake_infoinput"+i);
        idInput = new HiddenInput("ake_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      infoInput.setSize(40);

      Edit.setStyle(nameInput);
      Edit.setStyle(infoInput);

      inputTable.add(Edit.formatText(rownum),1,i+1);
      inputTable.add(nameInput,2,i+1);
      inputTable.add(infoInput,3,i+1);
      inputTable.add(iDrp,4,i+1);
      inputTable.add(idInput);
    }
    myForm.add(new HiddenInput("ake_count", String.valueOf(inputcount) ));
    myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    myForm.add(Finance.getCategoryParameter(iCategoryId));
    myForm.add(inputTable);
    SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save","Save"));
    Edit.setStyle(save);
    myForm.add(save);

    return (myForm);
  }

  private PresentationObject doUpdate(IWContext iwc){
    int count = Integer.parseInt(iwc.getParameter("ake_count"));
    String sName,sInfo,sDel,sTKid;
    Integer ID,TKid;

    for (int i = 1; i < count+1 ;i++){
      sName = iwc.getParameter("ake_nameinput"+i ).trim();
      sInfo = iwc.getParameter("ake_infoinput"+i).trim();
      sDel = iwc.getParameter("ake_delcheck"+i);
      sTKid = iwc.getParameter("ake_keydrp"+i);
      TKid = Integer.valueOf(sTKid);
      ID = Integer.valueOf(iwc.getParameter("ake_idinput"+i));
      try {
		if(sDel != null && sDel.equalsIgnoreCase("true")){
		    getFinanceService().removeAccountKey(ID);
		  }
		  else if(!sName.equalsIgnoreCase("")){
		  	getFinanceService().createOrUpdateAccountKey(ID,sName,sInfo,TKid,getFinanceCategoryId());
		  }
	} catch (RemoteException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	} catch (RemoveException e) {
		e.printStackTrace();
	} catch (CreateException e) {
		e.printStackTrace();
	}
    }// for loop

   return getMain(iwc);
  }

  private DropdownMenu keyDrp(Collection TK){
    DropdownMenu drp = new DropdownMenu();
    drp.addMenuElement(0,"--");
    if(TK != null)
      drp.addMenuElements(TK);
    return drp;
  }

  public void main(IWContext iwc){
    control(iwc);
  }

}// class AccountKeyEditor
