package is.idega.idegaweb.campus.block.building.presentation;


import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Floor;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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

public class ApartmentFreezer extends CampusBlock {


  private final String strAction = "fin_action";

  protected boolean isAdmin = false;
  

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
          try {
			T.add(makeEditTable(Integer.valueOf(iwc.getParameter("apartment_id")),iwc),1,3);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
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

    TextInput SearchInput = getTextInput("ap_search");
    
    SubmitButton SearchButton = new SubmitButton("search","Search");
    

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

    try {
		Collection apartmentViews =((ApartmentViewHome)IDOLookup.getHome(ApartmentViewHome.class)).findByApartmentName(searchName);
		
		if(apartmentViews != null &&!apartmentViews.isEmpty()){

		 
		  Table T = new Table();
		  int row = 1;
		  ApartmentHome aph = (ApartmentHome)IDOLookup.getHome(Apartment.class);
		 for (Iterator iter = apartmentViews.iterator(); iter.hasNext();) {
			ApartmentView apView = (ApartmentView) iter.next();
			Apartment ap =aph.findByPrimaryKey(apView.getApartmentID());
		   
		    Link l = new Link(apView.getApartmentName());
		    l.addParameter("apartment_id",apView.getApartmentID().toString());
		    T.add(l,1,row);
		    T.add(getText(apView.getFloorName()),2,row);
		    T.add(getText(apView.getBuildingName()),3,row);
		    if(ap.getUnavailableUntil()!=null)
		      T.add(getText((new IWTimestamp(ap.getUnavailableUntil())).getLocaleDate(iwc.getCurrentLocale())),4,row);
		    else
		      T.add(getText("Unfrozen"),4,row);
		    row++;
		  }
		  Right.add(T);
		}
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}

    try {
		Collection L = getBuildingService(iwc).getApartmentHome().findByName(searchName);
		if(L != null){

		  Table T = new Table();
		  int row = 1;
		  for (Iterator iter = L.iterator(); iter.hasNext();) {
			Apartment A = (Apartment) iter.next();
		    Floor F = A.getFloor();
		    Building B = F.getBuilding();
		    Link l = new Link(A.getName());
		    l.addParameter("apartment_id",A.getPrimaryKey().toString());
		    T.add(l,1,row);
		    T.add(getText(F.getName()),2,row);
		    T.add(getText(B.getName()),3,row);
		    if(A.getUnavailableUntil()!=null)
		      T.add(getText((new IWTimestamp(A.getUnavailableUntil())).getLocaleDate(iwc.getCurrentLocale())),4,row);
		    else
		      T.add(getText("Unfrozen"),4,row);
		    row++;
		  }
		  Right.add(T);
		}
	} catch (RemoteException e1) {
		e1.printStackTrace();
	} catch (EJBException e1) {
		e1.printStackTrace();
	} catch (FinderException e1) {
		e1.printStackTrace();
	}

    return Frame;
  }

  private PresentationObject makeEditTable(Integer id,IWContext iwc)throws RemoteException,FinderException{
    Table Frame = new Table(3,2);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
    ApartmentView aView = ((ApartmentViewHome)IDOLookup.getHome(ApartmentView.class)).findByPrimaryKey(id);
    Apartment A =((ApartmentHome)IDOLookup.getHome(Apartment.class)).findByPrimaryKey(id);
    
    DateInput DI = new DateInput("frozen_date",true);
    //DI.setIWContext(iwc);
    if(A.getUnavailableUntil()!=null)
      DI.setDate(A.getUnavailableUntil());
    //else
    //  DI.setToCurrentDate();
    
    HiddenInput hid = new HiddenInput("app_id",String.valueOf(id));
    SubmitButton sb = new SubmitButton("freeze","Freeze");

   

    Form myForm = new Form();
    Table T = new Table();

    T.add(getText(aView.getApartmentName()),1,1);
    T.add(getText(aView.getFloorName()),2,1);
    T.add(getText(aView.getBuildingName()),3,1);

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
      Integer id = Integer.valueOf(appId);
      Apartment A = ((ApartmentHome)IDOLookup.getHome(Apartment.class)).findByPrimaryKey(id);
      IWTimestamp iT = new IWTimestamp(frozenDate);
      A.setUnavailableUntil(iT.getDate());
      A.store();
      }
    }
    catch(Exception e){}

    return T;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}
