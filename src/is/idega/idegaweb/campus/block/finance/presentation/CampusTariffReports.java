package is.idega.idegaweb.campus.block.finance.presentation;


import is.idega.idegaweb.campus.business.CampusSettings;
import is.idega.idegaweb.campus.data.AccountEntriesReportBMPBean;
import is.idega.idegaweb.campus.data.AccountEntryReport;
import is.idega.idegaweb.campus.data.AccountEntryReportBMPBean;
import is.idega.idegaweb.campus.data.EntryReport;
import is.idega.idegaweb.campus.data.EntryReportBMPBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.block.finance.presentation.Finance;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.GenericSelect;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SelectPanel;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusTariffReports extends Finance {

  private IWResourceBundle iwrb;
  

  private String prmDateFrom = "ctr_date_from";
  private String prmDateTo = "ctr_date_to";
  private String prmBuilding = "ctr_building";
  private String prmAccountKey = "ctr_account_key";
  private IWTimestamp from = null,to = null;
  
  Integer building = null,key=null;
  String[] buildings= null,acckeys= null;

  private List reports = null;
  private Collection accountReports = null;
  private Collection accountEntriesReports = null;
  private Collection distinctKeys = null;
  private Collection keys = null;
  private Map keyMap = null;
  private boolean useBoxSelection = true;

  public CampusTariffReports() {
  	setWidth("100%");
  }

  public String getBundleIdentifier(){
    return CampusSettings.IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc)throws java.rmi.RemoteException{
  	
    //debugParameters(iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    if(iwc.isLoggedOn()){
	    if(iwc.isParameterSet("search"))
	      parse(iwc);
	    setLocalizedTitle("entry_reports","Entry reports");
	    setSearchPanel(getSearchForm());
	    if(reports!=null)
	      setMainPanel(getResult(iwc));
	    else if (accountReports!=null)
	    	setMainPanel(getAccountResult(iwc));
	    else if(accountEntriesReports!=null)
	    	setMainPanel(getAccountEntriesResult(iwc));
    }
  }
  

  private void parse(IWContext iwc){

    if(iwc.isParameterSet(prmDateFrom)){
      try {
        from = new IWTimestamp(iwc.getParameter(prmDateFrom));
      }
      catch (Exception ex) {  }
    }
     if(iwc.isParameterSet(prmDateTo)){
      try {
        to = new IWTimestamp(iwc.getParameter(prmDateTo));
      }
      catch (Exception ex) {  }
    }
    buildings = iwc.isParameterSet(prmBuilding)?iwc.getParameterValues(prmBuilding):null;//Integer.valueOf(iwc.getParameter(prmBuilding)):null;
    acckeys = iwc.isParameterSet(prmAccountKey)?iwc.getParameterValues(prmAccountKey):null;//Integer.valueOf(iwc.getParameter(prmAccountKey)):null;
    //reports = CampusAccountFinder.getAccountEntryReport(building,key,from,to);
    //reports = CampusAccountFinder.findAccountEntryReports(building,key,from,to);
    if(!iwc.isParameterSet("bycontract")){
    	
        	try{
        		reports = EntryReportBMPBean.findAllBySearch(buildings,acckeys,from.getTimestamp(),to.getTimestamp());
        	}
        	catch(java.sql.SQLException ex){
        		ex.printStackTrace();
        	}	
        
    }
    
    else {
    	if(iwc.isParameterSet("byjoins")) {
        	try {
        		keys = AccountEntriesReportBMPBean.getAccountKeys(building,from.getDate(),to.getDate());
    			accountEntriesReports = AccountEntriesReportBMPBean.findAllBySearch(buildings,acckeys,from.getDate(),to.getDate(),iwc.isParameterSet("bytariffkey"));
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}}
    		else{
    	try{
    		accountReports = AccountEntryReportBMPBean.findAllBySearch(buildings,acckeys,from.getTimestamp(),to.getTimestamp(),iwc.isParameterSet("bytariffkey"));
    	}
    	catch(java.sql.SQLException ex){
    		ex.printStackTrace();
    	}	}
    }
  }

  public PresentationObject getSearchForm()throws java.rmi.RemoteException{
  	DataTable T =getDataTable();
    T.setUseBottom(false);
    T.setUseTop(false);
    T.setTitlesHorizontal(true);
    GenericSelect buildings = getBuildingsSelection(prmBuilding);
    buildings.setSelectedOption(String.valueOf(building));
    buildings.keepStatusOnAction(true);
    GenericSelect keys = getAccountKeysSelection(prmAccountKey);
    keys.setSelectedOption(String.valueOf(key));
    keys.keepStatusOnAction(true);
    IWTimestamp today = IWTimestamp.RightNow();
    DateInput inpFrom = new DateInput(prmDateFrom,true);
    if(from!=null)
      inpFrom.setDate(from.getDate());
    else
      inpFrom.setDate(new IWTimestamp(1,today.getMonth(),today.getYear()).getDate());
    inpFrom.setYearRange(today.getYear()-5,today.getYear()+1);
    DateInput inpTo = new DateInput(prmDateTo,true);
    if(to!=null)
      inpTo.setDate( to.getDate());
    else
      inpTo.setDate(today.getDate());
    inpTo.setYearRange(today.getYear()-5,today.getYear()+1);

    T.add(getHeader(iwrb.getLocalizedString("building","Building")),1,1);
    T.add(getHeader(iwrb.getLocalizedString("account_key","Account_key")),2,1);
    T.add(getHeader(iwrb.getLocalizedString("date_from","Due date from")),3,1);
    T.add(getHeader(iwrb.getLocalizedString("date_to","Due date to")),4,1);
    T.add(buildings,1,2);
    T.add(keys,2,2);
    T.add(inpFrom,3,2);
    T.add(inpTo,4,2);
    
    CheckBox chkByContract = new CheckBox("bycontract","true");
    CheckBox chkByTariffKey = new CheckBox("bytariffkey","true");
    CheckBox chkByJoinSQL = new CheckBox("byjoins","true");
    chkByContract.keepStatusOnAction();
    chkByTariffKey.keepStatusOnAction();
    chkByJoinSQL.keepStatusOnAction();
    T.add(chkByContract,1,3);
    T.add(getSmallHeader(localize("show_by_accounts","Show by accounts")),1,3);
    T.add(chkByTariffKey,1,3);
    T.add(getSmallHeader(localize("show_by_tariffkey","Show by tariff key")),1,3);
    //T.add(chkByJoinSQL,1,3);
    T.add(getSmallHeader(localize("show_by_join_sql","Show by join sql")),1,3);
    T.getContentTable().mergeCells(1,3,4,3);
    T.getContentTable().setRowVerticalAlignment(2,Table.VERTICAL_ALIGN_TOP);
    

    SubmitButton search = new SubmitButton(iwrb.getLocalizedImageButton("search","Search"),"search","true");
    T.addButton(search);
   
    return T;
  }

  public PresentationObject getResult(IWContext iwc)throws java.rmi.RemoteException{
    DataTable T = new DataTable();
    List L = reports;
    T.setUseBottom(false);
    T.setUseTop(false);
    T.setTitlesHorizontal(true);
    if(L!=null && !L.isEmpty()){
      java.util.Iterator iter = L.iterator();
      EntryReport entry;
      int iBuildingId = -1,row = 1,col=1;
      float total = 0,Alltotal = 0;
      boolean last = false;
      T.add(getHeader(iwrb.getLocalizedString("building","Building")),1,row);
      T.add(getHeader(iwrb.getLocalizedString("account_key","Account key")),2,row);
      T.add(getHeader(iwrb.getLocalizedString("account_key_info","Account key info")),3,row);
      T.add(getHeader(iwrb.getLocalizedString("sum","Sum")),4,row);
      T.add(getHeader(iwrb.getLocalizedString("number","Number")),5,row);
      row++;
      while(iter.hasNext()){
        col = 1;
        entry = (EntryReport) iter.next();

        if(iBuildingId != entry.getBuildingId() ){

          if (iBuildingId >0){
            T.add(getAmountText((total)),4,row++);
            Alltotal += total;
          }
          T.add(getHeader(entry.getBuildingName()),1,row++);
          total = 0;
        }
        col++;
        T.add(getText(entry.getKeyName()),2,row);
        T.add(getText(entry.getKeyInfo()),3,row);
        T.add(getAmountText((entry.getTotal())),4,row);
        T.add(getText(" ( "+entry.getNumber()+" )"),5,row);

        iBuildingId = entry.getBuildingId();
        total += entry.getTotal();

        row++;

      }
      if (iBuildingId >0){
        T.add(getAmountText((total)),4,row++);
        Alltotal += total;
      }
      if(!(Alltotal==total))
        T.add(getAmountText((Alltotal)),4,row);
      T.getContentTable().setColumnAlignment(4,"right");
      T.getContentTable().setColumnAlignment(5,"right");

    }
    else
      T.add(getHeader(iwrb.getLocalizedString("no_entries_found","No entries where found")));
    return T;
  }
  
  public PresentationObject getAccountResult(IWContext iwc){
  	Table T = new Table();
  	T.setNoWrap();
  
    if(accountReports!=null && !accountReports.isEmpty()){
    	
    	Collection coll = groupReportsByContracts();
    	Map contractMap;
    	int row = 2;
    	int offset = 4;
    	int col = offset;
    	int keySize = distinctKeys.size();
    	double[] total = new double[keySize+1];
    	for (int i = 0; i < total.length; i++) {
			total[i]  = 0;
		}
    	T.add(getHeader(localize("name","Name")),2,1);
    	T.add(getHeader(localize("personal_id","Personal ID")),3,1);
    	
    	
    	for (Iterator iterator = distinctKeys.iterator(); iterator.hasNext();) {
			Integer keyId = (Integer) iterator.next();
			T.add(getHeader((String)keyMap.get(keyId)),col++,1);
		}
    	T.add(getHeader(localize("total","Total")),col++,1);
    	
    	java.text.DateFormat df = getShortDateFormat(iwc.getCurrentLocale());
    	int ind= 1;
    	for (Iterator iter = coll.iterator(); iter.hasNext();) {
    		T.add(getText(String.valueOf(ind++)),1,row);
    		contractMap  = (Map) iter.next();
    		boolean common =false;
    		double linetotal = 0;
    		
    		col = offset;
    		// for every distinct account key
    		int index = 0;
    		for (Iterator iterator = distinctKeys.iterator(); iterator.hasNext();) {
				Integer keyId = (Integer) iterator.next();
				AccountEntryReport report = (AccountEntryReport)contractMap.get(keyId);
				// print common info
				if(report!=null){
					if(!common){
						T.add(getText(report.getName()),2,row);
						T.add(getText(report.getPersonalID()),3,row);
						common = true;
					}
					double lineKeyTotal =report.getTotal().doubleValue();
					linetotal += lineKeyTotal;
					total[index++]+=lineKeyTotal;
					T.add(getAmountText(lineKeyTotal),col,row);
				}
				col++;
				
			}
			T.add(getAmountText(linetotal),col,row);
			total[keySize]+=linetotal;
			row++;
		}
    	for (int i = 0; i < total.length; i++) {
    		T.add(getAmountText(total[i]),offset+i,row);
		}
    	
    
    	 T.setVerticalZebraColored(getZebraColor1(),getZebraColor2());
    	 T.setRowColor(1,getHeaderColor());
    	 T.setColumnAlignment(3,Table.HORIZONTAL_ALIGN_RIGHT);
    	 T.setAlignment(3,1,Table.HORIZONTAL_ALIGN_LEFT);
    	 for (int i = offset; i <= keySize+offset ; i++) {
    			T.setColumnAlignment(i,Table.HORIZONTAL_ALIGN_RIGHT);
    			T.setAlignment(i,1,Table.HORIZONTAL_ALIGN_LEFT);
    	 }
    	 T.setRowColor(row,getHeaderColor());
    }
   
    
   
    return T;
  }
  
  private Collection groupReportsByContracts(){
  		List collOfMaps = new ArrayList();
  		Map mapOfIndexByContract = new HashMap();
  		distinctKeys = new ArrayList();
  		keyMap = new HashMap();
  		for (Iterator iter = accountReports.iterator(); iter.hasNext();) {
			AccountEntryReport element = (AccountEntryReport) iter.next();
			Integer accountID = element.getAccountID();
			
			if(mapOfIndexByContract.containsKey(accountID)){
				int index = ((Integer) mapOfIndexByContract.get(accountID)).intValue();
				Map m = ((Map)  collOfMaps.get(index));
				m.put(element.getKeyID(),element);
				collOfMaps.set(index,m);
			}
			else{
				Map map = new HashMap();
				map.put(element.getKeyID(),element);
				mapOfIndexByContract.put(accountID,new Integer(collOfMaps.size()));
				collOfMaps.add(map);
			}
			if(!distinctKeys.contains(element.getKeyID())){
				distinctKeys.add(element.getKeyID());
				keyMap.put(element.getKeyID(),element.getKeyInfo());
			}
			
		}
  	
  		return collOfMaps;
  }
  
  public PresentationObject getAccountEntriesResult(IWContext iwc) throws java.rmi.RemoteException{
  	Table T = new Table();
  	T.setNoWrap();
  	if(keys!=null && accountEntriesReports!=null && !accountEntriesReports.isEmpty()){
  		T.add(getHeader(localize("name","Name")),2,1);
    	T.add(getHeader(localize("personal_id","Personal ID")),3,1);
    	int col = 4;
    	String[] keyIds = new String[keys.size()];
    	int i = 0;
    	for (Iterator iter = keys.iterator(); iter.hasNext();) {
			AccountKey key = (AccountKey) iter.next();
			T.add(getHeader(key.getName()+"("+key.getInfo()+")"),col++,1);
			keyIds[i++]  = key.getPrimaryKey().toString();
		}
    	T.add(getHeader(localize("line_total","Line total")),col,1);
  		int row = 2;
  		double[] columntotal = new double[keyIds.length+1];
  		double linetotal = 0;
		double total = 0;
		double amount=0;
  		int ind = 1;
  		for (Iterator iter = accountEntriesReports.iterator(); iter.hasNext();) {
			AccountEntriesReportBMPBean report = (AccountEntriesReportBMPBean) iter.next();
			linetotal = 0.0;
			col = 1;
			T.add(getText(String.valueOf(ind++)),col++,row);
			T.add(getText(report.getName()),col++,row);
			T.add(getText(report.getPersonalID()),col++,row);
			for (int j = 0; j < keyIds.length; j++) {
				if(report.getEntries().containsKey(keyIds[j])){
					amount = ((Float)report.getEntries().get(keyIds[j])).doubleValue();
					linetotal +=amount;
					T.add(getAmountText(amount),col,row);
					columntotal[j] +=amount;
				}
				col++;
			}
			columntotal[columntotal.length-1]+=linetotal;
			T.add(getAmountText(linetotal),col,row);
			
			row++;
		}
  		col = 4;
  		for (int j = 0; j < columntotal.length; j++) {
  			
			T.add(getAmountText(columntotal[j]),col,row);
			T.setColumnAlignment(col,Table.HORIZONTAL_ALIGN_RIGHT);
			col++;
		}
  	
  	}
  	
  	T.setVerticalZebraColored(getZebraColor1(),getZebraColor2());
	T.setRowColor(1,getHeaderColor());
  	
  	return T;
  }
  

  public GenericSelect getBuildingsSelection(String name)throws java.rmi.RemoteException{
  	GenericSelect drp = useBoxSelection ? (GenericSelect)new SelectPanel(name):(GenericSelect)new SelectDropdown(name);
    try {
		Collection buildings = ((BuildingHome)IDOLookup.getHome(Building.class)).findAll();
		if(buildings!=null&&!buildings.isEmpty()){
		  Iterator iter = buildings.iterator();
		  Building building;
		  if(!useBoxSelection)
		  drp.addOption(new SelectOption(iwrb.getLocalizedString("all_buildings","All buildings"),""));
		  while(iter.hasNext()){
		    building = (Building) iter.next();
		    drp.addOption(new SelectOption(building.getName(),building.getPrimaryKey().toString()));
		  }
		}
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	catch (EJBException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}
    return drp;
  }
  

  public GenericSelect getAccountKeysSelection(String name)throws java.rmi.RemoteException{
  	GenericSelect drp = useBoxSelection?(GenericSelect)new SelectPanel(name):(GenericSelect)new SelectDropdown(name);
    Collection keys=null;
	try {
		keys = ((AccountKeyHome)IDOLookup.getHome(AccountKey.class)).findAll();
	} catch (FinderException e) {
		e.printStackTrace();
	}
	if(keys!=null&&!keys.isEmpty()){
      Iterator iter = keys.iterator();
      AccountKey key;
      if(!useBoxSelection)
      	drp.addOption(new SelectOption(iwrb.getLocalizedString("all_account_keys","All account keys"),""));
      while(iter.hasNext()){
        key = (AccountKey) iter.next();
        drp.addOption(new SelectOption(key.getInfo(),key.getPrimaryKey().toString()));
      }
    }
    return drp;
  }
}
