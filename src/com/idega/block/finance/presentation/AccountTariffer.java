package com.idega.block.finance.presentation;

import com.idega.block.finance.business.AccountBusiness;
import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.FinanceAccount;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffGroup;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class AccountTariffer extends Finance {

  private static String prmGroup = "at_grp";
  private FinanceAccount account;
  private int iAccountId = -1;
  private int searchPage = -1,viewPage = -1;
  private AccountBusiness accBuiz;
  private String prmNewTariff = "fin_ati_nwta";

  private String prmQuantity = "fin_trf_qty";
  private String prmTariffIds = "fin_trf_ids";
  private String prmTariffCheck = "fin_trf_chk";
  private String prmTariffName = "fin_trf_nme";
  private String prmTariffInfo = "fin_trf_ifo";
  private String prmAccountKey = "fin_acc_kid";
  private String prmTariffGroupId = "fin_tgr_id";
  private String prmPayDate = "fin_pay_dte";
  private String prmAmount = "fin_trf_amt";
  private String prmDiscount = "fin_trf_dsc";
  private String prmConfirm = "fin_confirm";
  private String prmSaveTariff = "fin_sve_trf";

  int iGroupId = -1;

  public AccountTariffer() {
  }

   protected void control(IWContext iwc) throws java.rmi.RemoteException{

    if(isAdmin){
      accBuiz = (AccountBusiness) IBOLookup.getServiceInstance(iwc,AccountBusiness.class);
//      iCategoryId = Finance.parseCategoryId(iwc);


      List groups = FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
      TariffGroup group = null;
      if(iwc.isParameterSet(prmGroup))
        iGroupId = Integer.parseInt(iwc.getParameter(prmGroup));
      if(iGroupId > 0 ){
        group = FinanceFinder.getInstance().getTariffGroup(iGroupId);
      }
      else if(groups !=null){
        //group = (TariffGroup) groups.get(0);
        //iGroupId = group.getID();
      }

      if(iwc.isParameterSet(prmAccountId)){
        iAccountId = Integer.parseInt(iwc.getParameter(prmAccountId));
        if(iAccountId>0){
          parse(iwc);
          account = FinanceFinder.getInstance().getFinanceAccount(iAccountId);
        }
      }

      Table T = new Table(1,6);
      T.setCellpadding(0);
      T.setCellspacing(0);
      T.add(textFormat.format(iwrb.getLocalizedString("account_tariffer","Account tariffer"),textFormat.HEADER),1,1);
      T.setHeight(2,"15");
      T.add(getAccountInfo(iwc),1,3);
      T.add(getGroupLinks(iCategoryId,iGroupId,groups),1,4);
      if(group!=null)
        T.add(getTariffTable(group),1,5);
      else
        T.add(getNewTariffTable(iwc),1,5);
      T.add(getTariffPropertiesTable(),1,6);
      //T.setWidth("450");
      Form myForm = new Form();
      myForm.add(new HiddenInput(prmCategoryId,String.valueOf(iCategoryId)));
      myForm.add(new HiddenInput(prmGroup,String.valueOf(iGroupId)));
      myForm.add(T);
      add(myForm);
    }
  }

  private void parse(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet(prmConfirm)&& iwc.getParameter(prmConfirm).equals("true")){
      //System.err.println("confirmation");
      String paydate = iwc.getParameter(prmPayDate);
      IWTimestamp Pd = new IWTimestamp(paydate);
      String SDiscount = iwc.getParameter(prmDiscount);
      int discount = SDiscount!=null && !SDiscount.equals("")?Integer.parseInt(SDiscount):-1;

      AssessmentBusiness assBuiz = (AssessmentBusiness) IBOLookup.getServiceInstance(iwc,AssessmentBusiness.class);
      String[] qtys = iwc.getParameterValues(prmQuantity);
      String[] ids = iwc.getParameterValues(prmTariffIds);
      if(qtys!=null && qtys.length>0 && ids!=null && qtys.length==ids.length){
		Vector t_ids = new Vector();
      	for (int i = 0; i < qtys.length; i++) {
			Integer qty = Integer.valueOf(qtys[i]);
			for (int j = 0; j <qty.intValue(); j++) {
				t_ids.add(qty.toString());
			}
		}
		if(t_ids.size()>0){
			String[] tariffIds = (String[]) t_ids.toArray(new String[0]);
			assBuiz.assessTariffsToAccount(tariffIds,iAccountId,Pd.getSQLDate(),discount,iGroupId,iCategoryId);
		}
		
	  }
      else if(iwc.isParameterSet(prmTariffCheck)){
        //System.err.println("using tariffs");
        String[] tariff_ids = iwc.getParameterValues(prmTariffCheck);
        assBuiz.assessTariffsToAccount(tariff_ids,iAccountId,Pd.getSQLDate(),discount,iGroupId,iCategoryId);
      }
      else{
        int keyId = iwc.isParameterSet(prmAccountKey)?Integer.parseInt(iwc.getParameter(prmAccountKey)):-1;;
        int price = iwc.isParameterSet(prmAmount)?Integer.parseInt(iwc.getParameter(prmAmount)):0;
        if(keyId>0 && price !=0){
          int TariffGroupId = iwc.isParameterSet(prmTariffGroupId)?Integer.parseInt(iwc.getParameter(prmTariffGroupId)):-1;
          //System.err.println("using new tariff");
          String name = iwc.getParameter(prmTariffName);
          String info = iwc.getParameter(prmTariffInfo);
          boolean saveTariff = iwc.isParameterSet(prmSaveTariff);

          assBuiz.assessTariffsToAccount(price,name,info,
          iAccountId ,
          keyId,Pd.getSQLDate(),TariffGroupId,iCategoryId,saveTariff);
        }
      }
    }
  }

  private PresentationObject getAccountInfo(IWContext iwc) throws java.rmi.RemoteException{
    DataTable T = new DataTable();
    T.setUseBottom(false);
    T.setWidth("100%");
    T.setTitlesVertical(true);
    if(account!=null){
      T.add(textFormat.format(iwrb.getLocalizedString("account_number","Account number"),textFormat.HEADER),1,1);
      T.add(textFormat.format(account.getAccountName()),2,1);
      UserBusiness uBuiz = (UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);
      User user = uBuiz.getUser(account.getUserId());
      T.add(textFormat.format(iwrb.getLocalizedString("account_owner","Account owner"),textFormat.HEADER),1,2);
      T.add(textFormat.format(user.getName()),2,2);
      T.add(textFormat.format(iwrb.getLocalizedString("account_balance","Account balance"),textFormat.HEADER),1,3);
      DecimalFormat Dformat = (DecimalFormat) DecimalFormat.getCurrencyInstance(iwc.getCurrentLocale());
      T.add(textFormat.format(Dformat.format(account.getBalance())),2,3);
      T.add(textFormat.format(iwrb.getLocalizedString("last_updated","Last updated"),textFormat.HEADER),1,4);
      T.add(textFormat.format(account.getLastUpdated().toString()),2,4);
    }
    return T;
  }

  private PresentationObject getGroupLinks(int iCategoryId , int iGroupId,List groups)throws java.rmi.RemoteException{
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    int col = 1;
    if(groups!=null){
      java.util.Iterator I = groups.iterator();
      TariffGroup group;
      Link tab;
      while (I.hasNext()) {
        group = (TariffGroup) I.next();
        tab = new Link(iwb.getImageTab(group.getName(),false));
        tab.addParameter(Finance.getCategoryParameter(iCategoryId));
        tab.addParameter(prmGroup,group.getID());
        if(account!=null)
          tab.addParameter(prmAccountId,account.getAccountId());

        T.add(tab,col++,1);
      }
    }
    Link newTariff = new Link(iwrb.getLocalizedImageTab("new","New",false));
    newTariff.addParameter(getCategoryParameter(iCategoryId));
    if(account!=null)
      newTariff.addParameter(prmAccountId,account.getAccountId());
    newTariff.addParameter(prmNewTariff,"true");
    //Link edit = new Link(iwrb.getLocalizedImageTab("edit","textFormat",false));
    //edit.setWindowToOpen(TariffGroupWindow.class);
    //edit.addParameter(Finance.getCategoryParameter(iCategoryId));
    T.add(newTariff,col,1);
    return T;
  }

  private PresentationObject getTariffTable(TariffGroup group)throws java.rmi.RemoteException{
    Collection listOfTariffs = null;
    Map map = null;
    boolean hasMap = false;
    if(group!=null){
        if(group.getHandlerId() > 0){
          FinanceHandler handler = FinanceFinder.getInstance().getFinanceHandler(group.getHandlerId());
          map = handler.getAttributeMap();
          if(map !=null)
            hasMap = true;
        }
      }
    listOfTariffs = FinanceFinder.getInstance().listOfTariffs(group.getID());
    //Table T = new Table();
    DataTable T = new DataTable();
    T.setUseBottom(false);
    T.setWidth("100%");
    T.addTitle(iwrb.getLocalizedString("tariffs","Tariffs")+"  "+group.getName());
    T.setTitlesVertical(false);
    int col = 1;
    int row = 1;
    T.add(textFormat.format(iwrb.getLocalizedString("use","Use")),col++,row);
    if(hasMap)
      T.add(textFormat.format(iwrb.getLocalizedString("attribute","Attribute")),col++,row);
    T.add(textFormat.format(iwrb.getLocalizedString("name","Name")),col++,row);
    T.add(textFormat.format(iwrb.getLocalizedString("price","Price")),col++,row);
	T.add(textFormat.format(iwrb.getLocalizedString("quantity","Qty.")),col++,row);
    row++;
    if(listOfTariffs!=null){
      java.util.Iterator I = listOfTariffs.iterator();
      Tariff tariff;
      while(I.hasNext()){
        col = 1;
        tariff = (Tariff) I.next();
        CheckBox chk = new CheckBox(prmTariffCheck,tariff.getPrimaryKey().toString());
        T.add(chk,col++,row);
        if(hasMap)
          T.add(textFormat.format((String) map.get(tariff.getTariffAttribute())),col++,row);
        T.add(textFormat.format(tariff.getName()),col++,row);
        T.add(textFormat.format(Float.toString(tariff.getPrice())),col,row);
        IntegerInput QtyInput= new IntegerInput(prmQuantity);
        T.add(QtyInput,col,row);
        T.add(new HiddenInput(prmTariffIds,tariff.getPrimaryKey().toString()));
        row++;
      }
      T.getContentTable().setColumnAlignment(col,"right");
      T.getContentTable().setAlignment(1,col,"left");
    }
    return T;
  }

  private PresentationObject getTariffPropertiesTable()throws java.rmi.RemoteException{
    DataTable T = new DataTable();
    T.setUseBottom(false);
    T.setWidth("100%");
    T.setTitlesHorizontal(true);
    //T.addTitle(iwrb.getLocalizedString("properties","Properties"));
    int row = 1;
    int col = 1;

    T.add(textFormat.format(iwrb.getLocalizedString("paydate","Paydate")),col,row++);
    DateInput payDate = new DateInput(prmPayDate,true);
    payDate.setDate(IWTimestamp.RightNow().getSQLDate());
    T.add(payDate,col,row);
    col++;
    row = 1;
    T.add(textFormat.format(iwrb.getLocalizedString("discount","Discount")+" (%)"),col,row++);
    //DropdownMenu dr = getIntDrop("discount",0,100,"");
    TextInput discount = new TextInput(prmDiscount);
    discount.setContent("0");
    T.add(discount,col,row);
    col++;
    row = 2;
    SubmitButton confirm = new SubmitButton(iwrb.getLocalizedImageButton("fin_confirm","Confirm"),prmConfirm,"true");

    T.add(confirm,col,row);
    if(account!=null)
      T.add(new HiddenInput(prmAccountId,String.valueOf(account.getAccountId())));


    return T;
  }

  private PresentationObject getNewTariffTable(IWContext iwc)throws java.rmi.RemoteException{
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.setUseBottom(false);
    T.setTitlesVertical(true);
    TextInput tariffName = new TextInput(prmTariffName);
    DropdownMenu accountKeys = getAccountKeysDrop(prmAccountKey);
    DropdownMenu tariffGroups = getTariffGroupsDrop(prmTariffGroupId);
    CheckBox saveTariff = new CheckBox(prmSaveTariff);
    TextInput amount = new TextInput(prmAmount);
    T.add(textFormat.format(iwrb.getLocalizedString("tariff.name","Tariff name")),1,1);
    T.add(textFormat.format(iwrb.getLocalizedString("tariff.account_key","Account key")),1,2);
    T.add(textFormat.format(iwrb.getLocalizedString("tariff.save_under","Save under")),1,3);
    T.add(textFormat.format(iwrb.getLocalizedString("tariff.amount","Amount")),1,4);
    T.add(textFormat.format(iwrb.getLocalizedString("tariff.save_tariff","Save tariff")),1,5);
    T.add(tariffName,2,1);
    T.add(accountKeys,2,2);
    T.add(tariffGroups,2,3);
    T.add(amount,2,4);
    T.add(saveTariff,2,5);
    return T;
  }

  private DropdownMenu getAccountKeysDrop(String name)throws java.rmi.RemoteException{
    Collection keys = FinanceFinder.getInstance().listOfAccountKeys(iCategoryId);
    DropdownMenu drp = new DropdownMenu(name);
    if(keys!=null){
      Iterator iter = keys.iterator();
      while(iter.hasNext()){
        AccountKey key = (AccountKey)iter.next();
        drp.addMenuElement(key.getPrimaryKey().toString(),key.getInfo());
      }

    }
    return drp;
  }

   private DropdownMenu getTariffGroupsDrop(String name)throws java.rmi.RemoteException{
    //Collection groups = FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
    Collection groups = FinanceFinder.getInstance().listOfTariffGroupsWithOutHandlers(iCategoryId);
    DropdownMenu drp = new DropdownMenu(name);
    if(groups!=null){
      Iterator iter = groups.iterator();
      while(iter.hasNext()){
        TariffGroup grp = (TariffGroup) iter.next();
        drp.addMenuElement(grp.getPrimaryKey().toString(),grp.getName());
      }
    }
    return drp;
  }

  private DropdownMenu getIntDrop(String name,int start, int end, String selected){
    DropdownMenu drp = new DropdownMenu(name);
    for (int i = start; i <= end; i++) {
      drp.addMenuElement(String.valueOf(i));
    }
    drp.setSelectedElement(selected);
    return drp;
  }

  public void setAccountViewPage(int pageId){
    this.viewPage = pageId;
  }

  public void setAccountSearchPage(int pageId){
    this.searchPage = pageId;
  }

  public void main(IWContext iwc)throws java.rmi.RemoteException{
    control(iwc);
  }
}
