package is.idega.idegaweb.campus.block.building.presentation;


import is.idega.idegaweb.campus.presentation.Edit;

import java.util.List;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Floor;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ApartmentFreezer extends Block {



  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final String strAction = "fin_action";
  protected boolean isAdmin = false;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public String getLocalizedNameKey(){
    return "apartment_freezer";
  }

  public String getLocalizedNameValue(){
    return "Freezer";
  }
  protected void control(IWContext iwc){


      if(isAdmin){
        Table T = new Table();
        T.add((makeSearchTable()),1,1);
        if(iwc.getParameter("search")!= null){
          String searchId = iwc.getParameter("ap_search").trim();
          T.add(makeResultTable(searchId,iwc),1,2);
        }
        else if( iwc.getParameter("apartment_id")!= null){
          T.add(makeEditTable(Integer.parseInt(iwc.getParameter("apartment_id")),iwc),1,3);
        }
        else if(iwc.getParameter("freeze")!=null){
          T.add(this.freezeApartment(iwc),1,3);
          add("freeze");
        }
        add(T);
      }
      else
        add(new Text("Ekki Réttindi"));

  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  public PresentationObject makeSearchTable(){

    Table Frame = new Table(3,2);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
    Table Left = new Table();
      Left.setCellpadding(0);
      Left.setCellspacing(0);
    Table Right = new Table();
      Right.setCellpadding(0);
      Right.setCellspacing(0);
    Frame.add(Left,1,1);
    Frame.add(Right,3,1);

    Table T = new Table(2,1);
    TextInput SearchInput = new TextInput("ap_search");
    Edit.setStyle(SearchInput);
    SubmitButton SearchButton = new SubmitButton("search","Search");
    Edit.setStyle(SearchButton);
    T.add(SearchInput,1,1);
    T.add(SearchButton,2,1);
    Form F = new Form();
    F.add(T);
    Right.add(F);

    return Frame;

  }
  public PresentationObject makeResultTable(String searchName,IWContext iwc){
     Table Frame = new Table(3,2);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
    Table Left = new Table();
      Left.setCellpadding(0);
      Left.setCellspacing(0);
    Table Right = new Table();
      Right.setCellpadding(0);
      Right.setCellspacing(0);
    Frame.add(Left,1,1);
    Frame.add(Right,3,1);
    List L = BuildingFinder.searchApartment(searchName);
    if(L != null){

      int len = L.size();

      Table T = new Table();
      for (int i = 0; i < len; i++) {
        Apartment A = (Apartment) L.get(i);
        Floor F = BuildingCacher.getFloor( A.getFloorId());
        Building B = BuildingCacher.getBuilding( F.getBuildingId());
        Link l = new Link(A.getName());
        l.addParameter("apartment_id",A.getID());
        T.add(l,1,i+1);
        T.add(Edit.formatText(F.getName()),2,i+1);
        T.add(Edit.formatText(B.getName()),3,i+1);
        if(A.getUnavailableUntil()!=null)
          T.add(Edit.formatText((new IWTimestamp(A.getUnavailableUntil())).getLocaleDate(iwc)),4,i+1);
        else
          T.add(Edit.formatText("Unfrozen"),4,i+1);
      }
      Right.add(T);
    }
    return Frame;
  }

  private PresentationObject makeEditTable(int id,IWContext iwc){
    Table Frame = new Table(3,2);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
    Apartment A = BuildingCacher.getApartment(id);
    Floor F = BuildingCacher.getFloor( A.getFloorId());
    Building B = BuildingCacher.getBuilding( F.getBuildingId());

    DateInput DI = new DateInput("frozen_date",true);
    DI.setIWContext(iwc);
    if(A.getUnavailableUntil()!=null)
      DI.setDate(A.getUnavailableUntil());
    //else
    //  DI.setToCurrentDate();
    DI.setStyle(Edit.styleAttribute);
    HiddenInput hid = new HiddenInput("app_id",String.valueOf(id));
    SubmitButton sb = new SubmitButton("freeze","Freeze");
      Edit.setStyle(sb);
    Form myForm = new Form();
    Table T = new Table();
    T.add(Edit.formatText(A.getName()),1,1);
    T.add(Edit.formatText(F.getName()),2,1);
    T.add(Edit.formatText(B.getName()),3,1);
    T.add(DI,4,1);
    T.add(sb,5,1);
    T.add(hid,5,1);
    myForm.add(T);
    Frame.add(myForm);
    return Frame;
  }

  private PresentationObject freezeApartment(IWContext iwc){
    Table T = new Table();
    String appId = iwc.getParameter("app_id");
    String frozenDate = iwc.getParameter("frozen_date");

    try{
      if(frozenDate != null && frozenDate.length()==10){
      int id = Integer.parseInt(appId);
      Apartment A = BuildingCacher.getApartment(id);
      IWTimestamp iT = new IWTimestamp(frozenDate);
      A.setUnavailableUntil(iT.getSQLDate());
      A.update();
      }
    }
    catch(Exception e){}

    return T;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}
