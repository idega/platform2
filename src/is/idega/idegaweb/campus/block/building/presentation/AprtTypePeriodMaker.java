package is.idega.idegaweb.campus.block.building.presentation;


import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriodsHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;
import is.idega.idegaweb.campus.presentation.Edit;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class AprtTypePeriodMaker extends CampusBlock{


  private boolean isAdmin = false;


  public String getLocalizedNameKey(){
    return "periods";
  }

  public String getLocalizedNameValue(){
    return "Periods";
  }

  public AprtTypePeriodMaker() {

  }

  protected void control(IWContext iwc){
      //debugParameters(iwc);
      if(isAdmin){
        if(iwc.isParameterSet("save.x") || iwc.isParameterSet("save")){
          doUpdate(iwc);
        }

        try {
			this.add(makeInputTable());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

      }
      else
        this.add(getNoAccessObject(iwc));

  }

  public PresentationObject makeInputTable()throws RemoteException,FinderException{
    Form F = new Form();
    DataTable T = getDataTable();
    T.addTitle(localize("apartment_periods","Apartment periods"));
    T.setTitlesVertical(false);
   Collection types = ((ApartmentTypeHome)IDOLookup.getHome(ApartmentType.class)).findAll();
    Hashtable ht = hashOfStuff();
    if(types != null&& !types.isEmpty()){
      int len =types.size();
      ApartmentType AT;


      T.add(getHeader(localize("apartment_type","Apartment type")),1,1);
      T.add(getHeader(localize("first_date","First date (D/M)")),2,1);
      T.add(getHeader(localize("second_date","Second date (D/M)")),3,1);

      int row = 2;
      int i =0;
      for (Iterator iter = types.iterator(); iter.hasNext();) {
		 AT = (ApartmentType) iter.next();
		
	
        T.add(Edit.formatText(AT.getName()),1,row);
        Integer typeId = new Integer(AT.getID());
        DropdownMenu drpDayOne = dayDrop("dayone"+i);
        DropdownMenu drpMonthOne = monthDrop("monthone"+i);
        DropdownMenu drpDayTwo = dayDrop("daytwo"+i);
        DropdownMenu drpMonthTwo = monthDrop("monthtwo"+i);
        int id = -1;
        if(ht !=null && ht.containsKey(new Integer(AT.getID())) ){
          ApartmentTypePeriods ATP = (ApartmentTypePeriods) ht.get(typeId);

          drpDayOne.setSelectedElement(String.valueOf(ATP.getFirstDateDay()));
          drpMonthOne.setSelectedElement(String.valueOf(ATP.getFirstDateMonth()));
          drpDayTwo.setSelectedElement(String.valueOf(ATP.getSecondDateDay()));
          drpMonthTwo.setSelectedElement(String.valueOf(ATP.getSecondDateMonth()));
          id = new Integer(ATP.getPrimaryKey().toString()).intValue();
        }
        Edit.setStyle( drpDayOne);
        Edit.setStyle(drpMonthOne);
        Edit.setStyle(drpDayTwo);
        Edit.setStyle(drpMonthTwo);
        T.add(new HiddenInput("typeid"+i,String.valueOf(AT.getID())),1,row);
        T.add(new HiddenInput("id"+i,String.valueOf(id)),1,row);
        T.add(drpDayOne,2,row);
        T.add(drpMonthOne,2,row);
        T.add(drpDayTwo,3,row);
        T.add(drpMonthTwo,3,row);
        row++;
        i++;
      }
      T.add(new HiddenInput("count",String.valueOf(len)));

      SubmitButton save = new SubmitButton(getResourceBundle().getLocalizedImageButton("save","Save"),"save");

      Edit.setStyle(save);
      T.addButton(save);

    }
    else
      T.add("No data");

    F.add(T);
    return F;
  }

  public void doUpdate(IWContext iwc){
    if(iwc.getParameter("count")!=null){
      int count = Integer.parseInt(iwc.getParameter("count"));
      ApartmentTypePeriods ATP ;
      boolean update = false;
      try{
        for (int i = 0; i < count; i++) {
          String sId = iwc.getParameter("id"+i);
          if(sId != null){
            int id = Integer.parseInt(sId);
            int dayOne = Integer.parseInt(iwc.getParameter("dayone"+i));
            int monthOne = Integer.parseInt(iwc.getParameter("monthone"+i));
            int dayTwo = Integer.parseInt(iwc.getParameter("daytwo"+i));
            int monthTwo = Integer.parseInt(iwc.getParameter("monthtwo"+i));
            int typeid = Integer.parseInt(iwc.getParameter("typeid"+i));
            if(id == -1){
              ATP = ((ApartmentTypePeriodsHome)IDOLookup.getHome(ApartmentTypePeriods.class)).create();
              update = false;
            }
            else{
              ATP = ((ApartmentTypePeriodsHome)IDOLookup.getHome(ApartmentTypePeriods.class)).findByPrimaryKey(new Integer(id));
              update = true;
            }
            ATP.setApartmentTypeId(typeid);
            ATP.setFirstDate(dayOne,monthOne);
            ATP.setSecondDate(dayTwo,monthTwo);
            ATP.store();
          }
        }
      }
      catch(Exception ex){}
    }
  }

  private DropdownMenu dayDrop(String name){
    DropdownMenu drp = new DropdownMenu(name);
    for (int i = 0; i <= 31; i++) {
      drp.addMenuElement(String.valueOf(i));
    }

    return drp;
  }

  private DropdownMenu monthDrop(String name){
    DropdownMenu drp = new DropdownMenu(name);
    for (int i = 0; i <= 12; i++) {
      drp.addMenuElement(String.valueOf(i));
    }

    return drp;
  }


  public Hashtable hashOfStuff()throws FinderException,RemoteException{
    Hashtable ht = null;
   Collection L = ((ApartmentTypePeriodsHome)IDOLookup.getHome(ApartmentTypePeriods.class)).findAll();
    if(L!=null){
      int len = L.size();
      ht = new Hashtable(len);
      ApartmentTypePeriods ATP ;
     for (Iterator iter = L.iterator(); iter.hasNext();) {
     	ATP = (ApartmentTypePeriods) iter.next();

        ht.put(new Integer(ATP.getApartmentTypeId()),ATP);
      }

    }
    return ht;
  }


  public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }


}
