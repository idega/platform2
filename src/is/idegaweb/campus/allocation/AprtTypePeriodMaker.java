package is.idegaweb.campus.allocation;

import com.idega.jmodule.object.ModuleObjectContainer;
import java.util.List;
import java.sql.SQLException;
import com.idega.jmodule.object.ModuleInfo;
import is.idegaweb.campus.presentation.Edit;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.InterfaceObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.Image;
import is.idegaweb.campus.entity.ApartmentTypePeriods;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.ApartmentType;
import com.idega.data.EntityFinder;
import java.util.Hashtable;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class AprtTypePeriodMaker extends ModuleObjectContainer{

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  private boolean isAdmin = false;
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  public AprtTypePeriodMaker() {

  }

  protected void control(ModuleInfo modinfo){

      if(isAdmin){
        if(modinfo.getParameter("save") != null){
          doUpdate(modinfo);
        }

        this.add(makeInputTable());

      }
      else
        this.add(new Text("Ekki Réttindi"));

  }

  public ModuleObject makeInputTable(){
    Form F = new Form();
    Table T = new Table();
    List Types = BuildingCacher.getTypes();
    Hashtable ht = hashOfStuff();
    if(Types != null){
      int len = Types.size();
      ApartmentType AT;

      T.add(Edit.titleText(iwrb.getLocalizedString("apartment_type","Apartment type")),1,1);
      T.add(Edit.titleText(iwrb.getLocalizedString("first_date","First date (D/M)")),2,1);
      T.add(Edit.titleText(iwrb.getLocalizedString("second_date","Second date (D/M)")),3,1);
      int row = 2;
      for (int i = 0; i < len; i++) {
        AT = (ApartmentType) Types.get(i);
        T.add(AT.getName(),1,row);
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
      SubmitButton save = new SubmitButton("save",iwrb.getLocalizedString("save","Save"));
      Edit.setStyle(save);
      T.add(save,1,row);
       T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
       T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = row;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,4,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);

    }
    else
      T.add("No data");
    F.add(getHomeLink());
    F.add(T);
    return F;
  }

  public void doUpdate(ModuleInfo modinfo){
    if(modinfo.getParameter("count")!=null){
      int count = Integer.parseInt(modinfo.getParameter("count"));
      ApartmentTypePeriods ATP ;
      boolean update = false;
      try{
        for (int i = 0; i < count; i++) {
          String sId = modinfo.getParameter("id"+i);
          if(sId != null){
            int id = Integer.parseInt(sId);
            int dayOne = Integer.parseInt(modinfo.getParameter("dayone"+i));
            int monthOne = Integer.parseInt(modinfo.getParameter("monthone"+i));
            int dayTwo = Integer.parseInt(modinfo.getParameter("daytwo"+i));
            int monthTwo = Integer.parseInt(modinfo.getParameter("monthtwo"+i));
            int typeid = Integer.parseInt(modinfo.getParameter("typeid"+i));
            if(id == -1){
              ATP = new ApartmentTypePeriods();
              update = false;
            }
            else{
              ATP = new ApartmentTypePeriods(id);
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
    ApartmentTypePeriods ATP = new ApartmentTypePeriods();
    try {
     L = EntityFinder.findAll(ATP);
    }
    catch (SQLException ex) {
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


  public void main(ModuleInfo modinfo){
    try{
      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){
      isAdmin = false;
    }
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    control(modinfo);
  }

  private Link getHomeLink(){
    return new Link(new Image("/pics/list.gif"),"/allocation/index.jsp");
  }

}