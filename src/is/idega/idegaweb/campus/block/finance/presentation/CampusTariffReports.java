package is.idega.idegaweb.campus.block.finance.presentation;

import is.idega.idegaweb.campus.data.AccountEntryReport;
import is.idega.idegaweb.campus.data.AccountEntryReportBMPBean;
import is.idega.idegaweb.campus.data.EntryReport;
import is.idega.idegaweb.campus.data.EntryReportBMPBean;
import is.idega.idegaweb.campus.presentation.Campus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Building;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.block.finance.presentation.Finance;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
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

public class CampusTariffReports extends Finance implements Campus{

  private IWResourceBundle iwrb;
  

  private String prmDateFrom = "ctr_date_from";
  private String prmDateTo = "ctr_date_to";
  private String prmBuilding = "ctr_building";
  private String prmAccountKey = "ctr_account_key";
  private java.sql.Timestamp from = null,to = null;
  
  Integer building = null,key=null;

  private List reports = null;
  private Collection accountReports = null;
  private Collection distinctKeys = null;
  private Map keyMap = null;

  public CampusTariffReports() {
  	setWidth("100%");
  }

  public String getBundleIdentifier(){
    return this.CAMPUS_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc)throws java.rmi.RemoteException{
  	
    //debugParameters(iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
 
    if(iwc.isParameterSet("search"))
      parse(iwc);
    setLocalizedTitle("entry_reports","Entry reports");
    setSearchPanel(getSearchForm());
    if(reports!=null)
      setMainPanel(getResult(iwc));
    else if (accountReports!=null)
    	setMainPanel(getAccountResult(iwc));
  }
  

  private void parse(IWContext iwc){

    if(iwc.isParameterSet(prmDateFrom)){
      try {
        from = new IWTimestamp(iwc.getParameter(prmDateFrom)).getTimestamp();
      }
      catch (Exception ex) {  }
    }
     if(iwc.isParameterSet(prmDateTo)){
      try {
        to = new IWTimestamp(iwc.getParameter(prmDateTo)).getTimestamp();
      }
      catch (Exception ex) {  }
    }
    building = iwc.isParameterSet(prmBuilding)?Integer.valueOf(iwc.getParameter(prmBuilding)):null;
    key = iwc.isParameterSet(prmAccountKey)?Integer.valueOf(iwc.getParameter(prmAccountKey)):null;
    //reports = CampusAccountFinder.getAccountEntryReport(building,key,from,to);
    //reports = CampusAccountFinder.findAccountEntryReports(building,key,from,to);
    if(!iwc.isParameterSet("bycontract")){
    	try{
    		reports = EntryReportBMPBean.findAllBySearch(building,key,from,to);
    	}
    	catch(java.sql.SQLException ex){
    		ex.printStackTrace();
    	}	
    }
    else{
    	try{
    		accountReports = AccountEntryReportBMPBean.findAllBySearch(building,key,from,to,iwc.isParameterSet("bytariffkey"));
    	}
    	catch(java.sql.SQLException ex){
    		ex.printStackTrace();
    	}	
    }
  }

  public PresentationObject getSearchForm()throws java.rmi.RemoteException{
    DataTable T = new DataTable();
    T.setUseBottom(false);
    T.setUseTop(false);
    T.setTitlesHorizontal(true);
    DropdownMenu buildings = getBuildingsDrop(prmBuilding);
    buildings.setSelectedElement(String.valueOf(building));
    DropdownMenu keys = getAccountKeysDrop(prmAccountKey);
    keys.setSelectedElement(String.valueOf(key));
    IWTimestamp today = IWTimestamp.RightNow();
    DateInput inpFrom = new DateInput(prmDateFrom,true);
    if(from!=null)
      inpFrom.setDate(new java.sql.Date(from.getTime()));
    else
      inpFrom.setDate(new IWTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());
    inpFrom.setYearRange(today.getYear()-5,today.getYear()+1);
    DateInput inpTo = new DateInput(prmDateTo,true);
    if(to!=null)
      inpTo.setDate( new java.sql.Date(to.getTime()));
    else
      inpTo.setDate(today.getSQLDate());
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
    chkByContract.keepStatusOnAction();
    chkByTariffKey.keepStatusOnAction();
    T.add(chkByContract,1,3);
    T.add(getSmallHeader(localize("show_by_account","Show by contract")),1,3);
    T.add(chkByTariffKey,1,3);
    T.add(getSmallHeader(localize("show_by_tariffkey","Show by tariff key")),1,3);
    T.getContentTable().mergeCells(1,3,4,3);
    

    SubmitButton search = new SubmitButton(iwrb.getLocalizedImageButton("search","Search"),"search","true");
    T.addButton(search);
    Form F = new Form();
    F.add(T);
    return F;
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
    	int offset = 3;
    	int col = offset;
    	int keySize = distinctKeys.size();
    	double[] total = new double[keySize+1];
    	for (int i = 0; i < total.length; i++) {
			total[i]  = 0;
		}
    	T.add(getHeader(localize("name","Name")),1,1);
    	T.add(getHeader(localize("personal_id","Personal ID")),2,1);
    	
    	
    	for (Iterator iterator = distinctKeys.iterator(); iterator.hasNext();) {
			Integer keyId = (Integer) iterator.next();
			T.add(getHeader((String)keyMap.get(keyId)),col++,1);
		}
    	T.add(getHeader(localize("total","Total")),col++,1);
    	
    	java.text.DateFormat df = getShortDateFormat(iwc.getCurrentLocale());
    	for (Iterator iter = coll.iterator(); iter.hasNext();) {
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
						T.add(getText(report.getName()),1,row);
						T.add(getText(report.getPersonalID()),2,row);
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
    	 for (int i = offset; i <= keySize+offset ; i++) {
    			T.setColumnAlignment(i,Table.HORIZONTAL_ALIGN_RIGHT);
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
  

  public DropdownMenu getBuildingsDrop(String name)throws java.rmi.RemoteException{
    DropdownMenu drp = new DropdownMenu(name);
    List buildings = BuildingCacher.getBuildings();
    if(buildings!=null&&!buildings.isEmpty()){
      Iterator iter = buildings.iterator();
      Building building;
      drp.addMenuElement(-100,iwrb.getLocalizedString("all_buildings","All buildings"));
      while(iter.hasNext()){
        building = (Building) iter.next();
        drp.addMenuElement(building.getPrimaryKey().toString(),building.getName());
      }
    }
    return drp;
  }

  public DropdownMenu getAccountKeysDrop(String name)throws java.rmi.RemoteException{
    DropdownMenu drp = new DropdownMenu(name);
    Collection keys=null;
	try {
		keys = ((AccountKeyHome)IDOLookup.getHome(AccountKey.class)).findAll();
	} catch (FinderException e) {
		e.printStackTrace();
	}
	if(keys!=null&&!keys.isEmpty()){
      Iterator iter = keys.iterator();
      AccountKey key;
      drp.addMenuElement(-100,iwrb.getLocalizedString("all_account_keys","All account keys"));
      while(iter.hasNext()){
        key = (AccountKey) iter.next();
        drp.addMenuElement(key.getPrimaryKey().toString(),key.getInfo());
      }
    }
    return drp;
  }
}
