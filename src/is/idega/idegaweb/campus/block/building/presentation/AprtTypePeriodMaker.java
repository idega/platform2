package is.idega.idegaweb.campus.block.building.presentation;


import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.presentation.Edit;

import java.util.Hashtable;
import java.util.List;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.ApartmentType;
import com.idega.data.EntityFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
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

public class AprtTypePeriodMaker extends Block{

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  private boolean isAdmin = false;
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public String getLocalizedNameKey(){
    return "periods";
  }

  public String getLocalizedNameValue(){
    return "Periods";
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  public AprtTypePeriodMaker() {

  }

  protected void control(IWContext iwc){
      //debugParameters(iwc);
      if(isAdmin){
        if(iwc.isParameterSet("save.x") || iwc.isParameterSet("save")){
          doUpdate(iwc);
        }

        this.add(makeInputTable());

      }
      else
        this.add(new Text("Ekki Réttindi"));

  }

  public PresentationObject makeInputTable(){
    Form F = new Form();
    DataTable T = new DataTable();
    T.addTitle(iwrb.getLocalizedString("apartment_periods","Apartment periods"));
    T.setTitlesVertical(false);
    List Types = BuildingCacher.getTypes();
    Hashtable ht = hashOfStuff();
    if(Types != null){
      int len = Types.size();
      ApartmentType AT;

      T.add(Edit.formatText(iwrb.getLocalizedString("apartment_type","Apartment type")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("first_date","First date (D/M)")),2,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("second_date","Second date (D/M)")),3,1);
      int row = 2;
      for (int i = 0; i < len; i++) {
        AT = (ApartmentType) Types.get(i);
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
          id = ATP.getID();
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
      }
      T.add(new HiddenInput("count",String.valueOf(len)));
      SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"save");
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
              ATP = ((is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriodsHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentTypePeriods.class)).createLegacy();
              update = false;
            }
            else{
              ATP = ((is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriodsHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentTypePeriods.class)).findByPrimaryKeyLegacy(id);
              update = true;
            }
            ATP.setApartmentTypeId(typeid);
            ATP.setFirstDate(dayOne,monthOne);
            ATP.setSecondDate(dayTwo,monthTwo);
            if(update)
              ATP.update();
            else
              ATP.insert();
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

  public List listOfStuff(){
    List L = null;
    try {
     L = EntityFinder.getInstance().findAll(ApartmentTypePeriods.class);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return L;
  }

  public Hashtable hashOfStuff(){
    Hashtable ht = null;
    List L = listOfStuff();
    if(L!=null){
      int len = L.size();
      ht = new Hashtable(len);
      ApartmentTypePeriods ATP ;
      for (int i = 0; i < len; i++) {
        ATP = (ApartmentTypePeriods)L.get(i);
        ht.put(new Integer(ATP.getApartmentTypeId()),ATP);
      }

    }
    return ht;
  }


  public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    control(iwc);
  }


}
