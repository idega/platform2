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
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

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
       
        setLocalizedTitle("account_key_editor","Account key editor");
        setSearchPanel(makeLinkTable(1,iCategoryId));
        setMainPanel(MO);
        
      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else
      add(getErrorText(localize("access_denied","Access denies")));
  }

  protected PresentationObject makeLinkTable(int menuNr,int iCategoryId){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
   
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link(getHeader(localize("view","View")));
   
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
   // Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
    Link Link2 = new Link(getHeader(localize("change","Change")));
    
    Link2.addParameter(strAction,String.valueOf(this.ACT2));
    //Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
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
		hk = getFinanceService().getAccountBusiness().getTariffKeyMap();
	} catch (RemoteException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}
    //List keys = FinanceFinder.getInstance().listOfAccountKeys(iCategoryId);
    if(keys !=null && hk!=null){
      int count = keys.size();
      keyTable = new Table(5,count+1);
      keyTable.setWidth("100%");
      keyTable.setHorizontalZebraColored(getZebraColor1(),getZebraColor2());
      keyTable.setRowColor(1,getHeaderColor());
      keyTable.setCellpadding(2);
      keyTable.setCellspacing(1) ;
      keyTable.add(getHeader("Nr"),1,1);
      keyTable.add(getHeader(localize("name","Name")),2,1);
      keyTable.add(getHeader(localize("info","Info")),3,1);
      keyTable.add(getHeader(localize("tariff_key","Tariff key")),4,1);
      keyTable.add(getHeader(localize("ordinal","Ordinal")),5,1);

      //java.util.Map hk = FinanceFinder.getInstance().mapOfTariffKeys(iCategoryId);
     
      if(isAdmin){
        if(count > 0){
          AccountKey key;
          int row = 2;
          int rowcount = 1;
          for (Iterator iter = keys.iterator(); iter.hasNext();) {
          	key = (AccountKey) iter.next();
            keyTable.add(getText(String.valueOf(rowcount++)),1,row);
            keyTable.add(getText(key.getName()),2,row);
            keyTable.add(getText(key.getInfo()),3,row);
            Integer tkid = new Integer(key.getTariffKeyId());
            if(hk.containsKey(tkid))
              keyTable.add( getText( ((TariffKey)hk.get( tkid)).getName() ),4,row);
            if(key.getOrdinal()!=null)
                keyTable.add(getText(key.getOrdinal().toString()),5,row);
            row++;
          }
        }
      }
    }
    return(keyTable);
  }

  private PresentationObject getChange(IWContext iwc) throws SQLException{
    
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
    DataTable inputTable =  getDataTable();
    inputTable.setUseBottom(false);
    inputTable.setUseTop(false);
    inputTable.setTitlesHorizontal(true);
    inputTable.setWidth(Table.HUNDRED_PERCENT);
    
    inputTable.add(getHeader("Nr"),1,1);
    inputTable.add(getHeader(localize("name","Name")),2,1);
    inputTable.add(getHeader(localize("info","Inro")),3,1);
    inputTable.add(getHeader(localize("tariff_key","Tariff key")),4,1);
    inputTable.add(getHeader(localize("ordinal","Ordinal")),5,1);
    inputTable.add(getHeader(localize("delete","Delete")),6,1);
    
    AccountKey key;
    Iterator iter = keys.iterator();
    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      TextInput nameInput, infoInput,ordinalInput;
      HiddenInput idInput;
      CheckBox delCheck;
      DropdownMenu iDrp =  keyDrp(Tkeys);
      iDrp.setName("ake_keydrp"+i);
      iDrp = (DropdownMenu) setStyle(iDrp,STYLENAME_INTERFACE);
      //int pos;
      if(i <= count && iter.hasNext()){
        //pos = i-1;
        key  = (AccountKey) iter.next();
        nameInput  = getTextInput("ake_nameinput"+i,(key.getName()));
        infoInput = getTextInput("ake_infoinput"+i,(key.getInfo()));
        ordinalInput = getTextInput("ake_ordinput"+i,key.getOrdinal()!=null?(key.getOrdinal().toString()):"");
        String sId = key.getPrimaryKey().toString();
        idInput = new HiddenInput("ake_idinput"+i,sId);
        delCheck = getCheckBox("ake_delcheck"+i,"true");
        iDrp.setSelectedElement(String.valueOf(key.getTariffKeyId()));
        
        inputTable.add(delCheck,6,i+1);
      }
      else{
        nameInput  = getTextInput("ake_nameinput"+i);
        infoInput = getTextInput("ake_infoinput"+i);
        idInput = new HiddenInput("ake_idinput"+i,"-1");
        ordinalInput = getTextInput("ake_ordinput"+i);
      }
      nameInput.setSize(20);
      infoInput.setSize(40);
      ordinalInput.setSize(3);

      inputTable.add(getText(rownum),1,i+1);
      inputTable.add(nameInput,2,i+1);
      inputTable.add(infoInput,3,i+1);
      inputTable.add(iDrp,4,i+1);
      inputTable.add(ordinalInput,5,i+1);
      inputTable.add(idInput);
    }
    inputTable.add(new HiddenInput("ake_count", String.valueOf(inputcount) ));
    inputTable.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    inputTable.add(Finance.getCategoryParameter(iCategoryId));
   
    SubmitButton save = new SubmitButton(localize("save","Save"));
    save = (SubmitButton) setStyle(save,STYLENAME_INTERFACE_BUTTON);
    inputTable.addButton(save);

    return (inputTable);
  }

  private PresentationObject doUpdate(IWContext iwc){
    int count = Integer.parseInt(iwc.getParameter("ake_count"));
    String sName,sInfo,sDel,sTKid,sOrd;
    Integer ID,TKid,ordinal=null;

    for (int i = 1; i < count+1 ;i++){
        ordinal = null;
      sName = iwc.getParameter("ake_nameinput"+i ).trim();
      sInfo = iwc.getParameter("ake_infoinput"+i).trim();
      sDel = iwc.getParameter("ake_delcheck"+i);
      sTKid = iwc.getParameter("ake_keydrp"+i);
      sOrd = iwc.getParameter("ake_ordinput"+i);
      TKid = Integer.valueOf(sTKid);
      ID = Integer.valueOf(iwc.getParameter("ake_idinput"+i));
      try {
        ordinal = Integer.valueOf(sOrd);
	    } catch (NumberFormatException e1) {
	       
	    }
      try {
		if(sDel != null && sDel.equalsIgnoreCase("true")){
		    getFinanceService().removeAccountKey(ID);
		  }
		  else if(!sName.equalsIgnoreCase("")){
		  	getFinanceService().createOrUpdateAccountKey(ID,sName,sInfo,TKid,ordinal,getFinanceCategoryId());
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
