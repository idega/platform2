package is.idega.idegaweb.campus.block.finance.presentation;

import is.idega.idegaweb.campus.presentation.Campus;
import is.idega.idegaweb.campus.block.finance.business.CampusAccountFinder;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.campus.data.*;
import java.util.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.util.text.TextFormat;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Building;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.presentation.Finance;
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

  private IWBundle iwb;
  private IWResourceBundle iwrb;
  private TextFormat tf;

  private String prmDateFrom = "ctr_date_from";
  private String prmDateTo = "ctr_date_to";
  private String prmBuilding = "ctr_building";
  private String prmAccountKey = "ctr_account_key";
  private java.sql.Timestamp from = null,to = null;
  private java.text.DecimalFormat df;
  int building = -1,key=-1;

  private List reports = null;

  public CampusTariffReports() {
  }

  public String getBundleIdentifier(){
    return this.CAMPUS_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc)throws java.rmi.RemoteException{
    //debugParameters(iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tf = TextFormat.getInstance();
    df = (java.text.DecimalFormat)java.text.DecimalFormat.getInstance();
    if(iwc.isParameterSet("search"))
      parse(iwc);
    add(getSearchForm());
    if(reports!=null)
      add(getResult(iwc));
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
    building = iwc.isParameterSet(prmBuilding)?Integer.parseInt(iwc.getParameter(prmBuilding)):-1;
    key = iwc.isParameterSet(prmAccountKey)?Integer.parseInt(iwc.getParameter(prmAccountKey)):-1;
    //reports = CampusAccountFinder.getAccountEntryReport(building,key,from,to);
    //reports = CampusAccountFinder.findAccountEntryReports(building,key,from,to);
    try{
    reports = EntryReportBMPBean.findAllBySearch(building,key,from,to);
    }
    catch(java.sql.SQLException ex){
      ex.printStackTrace();
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
    DateInput inpFrom = new DateInput(prmDateFrom);
    if(from!=null)
      inpFrom.setDate(new java.sql.Date(from.getTime()));
    else
      inpFrom.setDate(new IWTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());
    inpFrom.setYearRange(today.getYear()-5,today.getYear()+1);
    DateInput inpTo = new DateInput(prmDateTo);
    if(to!=null)
      inpTo.setDate( new java.sql.Date(to.getTime()));
    else
      inpTo.setDate(today.getSQLDate());
    inpTo.setYearRange(today.getYear()-5,today.getYear()+1);

    T.add(tf.format(iwrb.getLocalizedString("building","Building")),1,1);
    T.add(tf.format(iwrb.getLocalizedString("account_key","Account_key")),2,1);
    T.add(tf.format(iwrb.getLocalizedString("date_from","Due date from")),3,1);
    T.add(tf.format(iwrb.getLocalizedString("date_to","Due date to")),4,1);
    T.add(buildings,1,2);
    T.add(keys,2,2);
    T.add(inpFrom,3,2);
    T.add(inpTo,4,2);

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
      T.add(tf.format(iwrb.getLocalizedString("building","Building"),tf.HEADER),1,row);
      T.add(tf.format(iwrb.getLocalizedString("account_key","Account key"),tf.HEADER),2,row);
      T.add(tf.format(iwrb.getLocalizedString("account_key_info","Account key info"),tf.HEADER),3,row);
      T.add(tf.format(iwrb.getLocalizedString("sum","Sum"),tf.HEADER),4,row);
      T.add(tf.format(iwrb.getLocalizedString("number","Number"),tf.HEADER),5,row);
      row++;
      while(iter.hasNext()){
        col = 1;
        entry = (EntryReport) iter.next();

        if(iBuildingId != entry.getBuildingId() ){

          if (iBuildingId >0){
            T.add(tf.format(String.valueOf(total),tf.HEADER),4,row++);
            Alltotal += total;
          }
          T.add(tf.format(entry.getBuildingName(),tf.HEADER),1,row++);
          total = 0;
        }
        col++;
        T.add(tf.format(entry.getKeyName()),2,row);
        T.add(tf.format(entry.getKeyInfo()),3,row);
        T.add(tf.format(String.valueOf(entry.getTotal())),4,row);
        T.add(tf.format(" ( "+entry.getNumber()+" )"),5,row);

        iBuildingId = entry.getBuildingId();
        total += entry.getTotal();

        row++;

      }
      if (iBuildingId >0){
        T.add(tf.format(String.valueOf(total),tf.HEADER),4,row++);
        Alltotal += total;
      }
      if(!(Alltotal==total))
        T.add(tf.format(String.valueOf(Alltotal),tf.HEADER),4,row);
      T.getContentTable().setColumnAlignment(4,"right");
      T.getContentTable().setColumnAlignment(5,"right");

    }
    else
      T.add(tf.format(iwrb.getLocalizedString("no_entries_found","No entries where found"),tf.HEADER));
    return T;
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
    List keys = FinanceFinder.getInstance().listOfAccountKeys();
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
