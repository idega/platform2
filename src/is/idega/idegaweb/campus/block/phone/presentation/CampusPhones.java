package is.idega.idegaweb.campus.block.phone.presentation;

import is.idega.idegaweb.campus.block.phone.business.PhoneFinder;
import is.idega.idegaweb.campus.block.phone.data.CampusPhone;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentCategoryHome;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingEntity;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.FloorHome;
import com.idega.data.IDOLookup;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusPhones extends CampusBlock implements IWPageEventListener{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
 
  private String  sCLBU="-1",sCLFL="-1",sCLCX="-1",sCLTP="-1",sCLCT="-1",sORDER = "-1";
  private final String prmCx = "cl_clx",prmBu = "cl_bu",prmFl = "cl_fl",prmCt="cl_ct",prmTp="cl_tp",prmOrder="ct_or";
  private final String sessCx = "s_clx",sessBu = "s_bu",sessFl = "s_fl",sessCt="s_ct",sessTp="s_tp",sessOrder="s_or";
  private String[] prmArray = { prmCx ,prmBu ,prmFl,prmCt,prmTp,prmOrder};
  private String[] sessArray = {sessCx ,sessBu ,sessFl,sessCt,sessTp,sessOrder};
  private Integer[] iValues = {Integer.valueOf(sCLBU),Integer.valueOf(sCLFL),Integer.valueOf(sCLCX),Integer.valueOf(sCLCT),Integer.valueOf(sCLTP)};
  protected boolean isAdmin = false;
  private boolean fetch = false;


  public CampusPhones() {
    super();
  }

  public String getLocalizedNameKey(){
    return "campus_phones";
  }

  public String getLocalizedNameValue(){
    return "Phones";
  }

  protected void control(IWContext iwc){

    fetch = false;
    for (int i = 0; i < prmArray.length; i++) {
      if(iwc.getParameter(prmArray[i])!=null){
        iValues[i] = Integer.valueOf((iwc.getParameter(prmArray[i])));
        iwc.setSessionAttribute(sessArray[i],iValues[i]);
        fetch = true;
      }
      else if(iwc.getSessionAttribute(sessArray[i])!=null){
        iValues[i] = ((Integer)iwc.getSessionAttribute(sessArray[i]));
        fetch = true;
      }
    }

    if(isAdmin){
        try {
			add(statusForm());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
        if(fetch)
			try {
				add(getPhoneTable(iwc));
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
    }
    else
      add(getNoAccessObject(iwc));
    //add(String.valueOf(iSubjectId));
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private Form statusForm()throws RemoteException,FinderException{
    Form myForm = new Form();

    DropdownMenu complex = drpLodgings(((ComplexHome)IDOLookup.getHome(Complex.class)).findAll(),prmArray[0],"--",iValues[0].toString());
    DropdownMenu building = drpLodgings(((BuildingHome)IDOLookup.getHome(Building.class)).findAll(),prmArray[1],"--",iValues[1].toString());
    DropdownMenu floor = drpFloors(prmArray[2],"--",iValues[2].toString(),true);
    DropdownMenu cat = drpLodgings(((ApartmentCategoryHome)IDOLookup.getHome(ApartmentCategory.class)).findAll(),prmArray[3],"--",iValues[3].toString());
    DropdownMenu type = drpLodgings(((ApartmentTypeHome)IDOLookup.getHome(ApartmentType.class)).findAll(),prmArray[4],"--",iValues[4].toString());
    DropdownMenu order = orderDrop(prmArray[5],"--",iValues[5].toString());

    //Edit.setStyle(status);
    
    Table T = new Table();
      T.add(getHeader(localize("complex","Complex")),1,1);
      T.add(getHeader(localize("building","Building")),2,1);
      T.add(getHeader(localize("floor","Floor")),3,1);
      T.add(getHeader(localize("category","Category")),4,1);
      T.add(getHeader(localize("type","Type")),5,1);
     // T.add(getHeader(localize("order","Order")),2,1);
     // T.add(status,1,2);
      T.add(complex,1,2);
      T.add(building,2,2);
      T.add(floor,3,2);
      T.add(cat,4,2);
      T.add(type,5,2);
      //T.add(order,2,2);
      SubmitButton get = (SubmitButton) getSubmitButton("conget",null,"Get","get");
   
      T.add(get,1,3);
    T.setCellpadding(1);
    T.setCellspacing(0);

    myForm.add(T);
    return myForm;
  }


  private DropdownMenu drpLodgings(Collection buildingEntities,String name,String display,String selected) {
   

    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addDisabledMenuElement("-1",display);
    if(buildingEntities != null){
    	for (Iterator iter = buildingEntities.iterator(); iter.hasNext();) {
    		BuildingEntity element = (BuildingEntity) iter.next();
       
          drp.addMenuElement(element.getPrimaryKey().toString(),element.getName());
        }
        if(!"".equalsIgnoreCase(selected)){
          drp.setSelectedElement(selected);
        }
      }
    
    return drp;
  }

  private DropdownMenu drpFloors(String name,String display,String selected,boolean withBuildingName)throws RemoteException,FinderException {
   Collection floors = ((FloorHome)IDOLookup.getHome(Floor.class)).findAll();
   BuildingHome bh =(BuildingHome) IDOLookup.getHome(Building.class);
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addDisabledMenuElement("-1",display);
    for (Iterator iter = floors.iterator(); iter.hasNext();) {
		Floor floor = (Floor) iter.next();

        if(withBuildingName){
          try{
          drp.addMenuElement(floor.getPrimaryKey().toString() ,floor.getName()+" "+(bh.findByPrimaryKey(new Integer(floor.getBuildingId())).getName()));
          }
          catch(Exception e){}}
        else
          drp.addMenuElement(floor.getPrimaryKey().toString() ,floor.getName());
      }
    
    if(!"".equalsIgnoreCase(selected)){
      drp.setSelectedElement(selected);
    }
    return drp;
  }

  private DropdownMenu orderDrop(String name,String display ,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display)){
      drp.addDisabledMenuElement("-1",display);
    }

    if(!"".equals(selected))
      drp.setSelectedElement(selected);
    return drp;
  }


  private PresentationObject getPhoneTable(IWContext iwc)throws RemoteException,FinderException{
    Form form = new Form();
    //int order = Integer.parseInt(iValues[5]);
    Collection apartments = ((ApartmentHome)IDOLookup.getHome(Apartment.class)).findBySearch(iValues[0],iValues[1],iValues[2],iValues[3],iValues[4],true);
    //List L = BuildingFinder.listOfApartments(sValues[0],sValues[1],sValues[2],sValues[3],sValues[4],order);
    //Map M = PhoneFinder.mapOfPhonesByApartmentId(PhoneFinder.listOfPhones(sValues[0],sValues[1],sValues[2],sValues[3],sValues[4],order));
    Map M = PhoneFinder.mapOfPhonesByApartmentId(PhoneFinder.listOfPhones());
    CampusPhone P = null;
    Apartment A = null;
    Building B = null;
    Floor F = null;
    Complex CX = null;
    Table T = new Table();
    T.setCellspacing(1);
    T.setCellpadding(2);
    if(apartments!=null){

      int len = apartments.size();
      int row = 2;
      int col = 1;
      int i =0;
      ApartmentViewHome avh =(ApartmentViewHome)IDOLookup.getHome(ApartmentView.class);
    for (Iterator iter = apartments.iterator(); iter.hasNext();) {
		 A = (Apartment) iter.next();
		 try {
		 //ApartmentView aview =avh.findByPrimaryKey(A.getPrimaryKey());
        col = 1;
       

        
          Integer ID = (Integer)(A.getPrimaryKey());
          TextInput ti = new TextInput("ti_"+i);
          T.add(new HiddenInput("apid"+i,ID.toString()));

          T.add(getText(String.valueOf(i+1)),1,row);
          T.add((getApartmentTable(A)),2,row);

          ti.setLength(10);
          
          T.add(ti,3,row);
          if(M != null && M.containsKey(ID)){
            P = (CampusPhone)M.get(ID);
            ti.setContent(P.getPhoneNumber());
            T.add(getText(new IWTimestamp(P.getDateInstalled()).getLocaleDate(iwc.getCurrentLocale())),4,row);
            T.add(new HiddenInput("phoneid"+i,P.getPrimaryKey().toString()),1,row);
          }
          row++;
          i++;
          col = 1;
        }
        catch (Exception ex) {  ex.printStackTrace(); }
        }
        form.add(new HiddenInput("ap_count",String.valueOf(len)));
        T.add(getHeader(localize("apartment","Apartment")),2,1);
        T.add(getHeader(localize("number","Number")),3,1);
        T.add(getHeader(localize("installed","Installed")),4,1);


        T.setHorizontalZebraColored(getZebraColor1(),getZebraColor2());
        T.setRowColor(1,getHeaderColor());
       
        T.mergeCells(1,row,8,row);
        T.setWidth(1,"15");
        T.add(getText(" "),1,row);
        T.setColumnAlignment(1,"left");
        
        //T.setWidth("100%");
    }
    else
      add(getText(localize("no_apartments","No Apartments")));
    form.add(T);
    form.add(new SubmitButton("save",localize("save","Save")));
    form.setEventListener(this.getClassName());

    return form;


  }

  private void update(IWContext iwc){
    String sCount = iwc.getParameter("ap_count");
    Map M = PhoneFinder.mapOfPhones(PhoneFinder.listOfPhones());
    if(sCount != null){
      int iCount = Integer.parseInt(sCount);
      if(iCount > 0){
        String sAPId,sPHId,sNumber;
        Integer iPHId,iAPId;

        for (int i = 0; i < iCount; i++) {
          try{
            sAPId = iwc.getParameter("apid"+i);
            sPHId = iwc.getParameter("phoneid"+i);
            sNumber = iwc.getParameter("ti_"+i);

            iPHId = new Integer(sPHId!=null?sPHId:"-1");

            if(sAPId != null && sNumber != null && sNumber.length() > 0){
              iAPId = new Integer(sAPId);
              // if updating entity
              if(M!=null && M.containsKey(iPHId)){
                CampusPhone P = (CampusPhone)M.get(iPHId);
                if(!P.getPhoneNumber().equals(sNumber)){
                  P.setPhoneNumber(sNumber);
                  P.setDateInstalled(IWTimestamp.RightNow().getDate());
                  P.store();
                }
              }
              // if new entity
              else{
                CampusPhone P = ((is.idega.idegaweb.campus.block.phone.data.CampusPhoneHome)com.idega.data.IDOLookup.getHomeLegacy(CampusPhone.class)).createLegacy();
                P.setPhoneNumber(sNumber);
                P.setApartmentId(iAPId.intValue());
                P.setDateInstalled(IWTimestamp.RightNow().getDate());
                P.store();
              }
            }
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
      }
    }
  }

  private PresentationObject getApartmentTable(Apartment A){
    Table T = new Table();

   
   
    Floor F = A.getFloor();
    Building B = F.getBuilding();
    Complex C = B.getComplex();
    T.add(getText(A.getName()),1,1);
    T.add(getText(F.getName()),2,1);
    T.add(getText(B.getName()),3,1);
    T.add(getText(C.getName()),4,1);

    return T;

  }

  public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
    //System.err.println("test is main");
  }


  public boolean actionPerformed(IWContext iwc)throws IWException{
    //System.err.println("test is actionPerformed");
    update(iwc);
    return true;
  }
}
