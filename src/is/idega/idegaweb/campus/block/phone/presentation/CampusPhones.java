package is.idega.idegaweb.campus.block.phone.presentation;


import is.idega.idegaweb.campus.block.phone.business.PhoneFinder;
import is.idega.idegaweb.campus.block.phone.data.CampusPhone;
import is.idega.idegaweb.campus.presentation.Edit;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLegacyEntity;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
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

public class CampusPhones extends Block implements IWPageEventListener{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private int iSubjectId = -1;
  private String sGlobalStatus = "C", sCLBU="-1",sCLFL="-1",sCLCX="-1",sCLTP="-1",sCLCT="-1",sORDER = "-1";


  private final String prmCx = "cl_clx",prmBu = "cl_bu",prmFl = "cl_fl",prmCt="cl_ct",prmTp="cl_tp",prmOrder="ct_or";
  private final String sessCx = "s_clx",sessBu = "s_bu",sessFl = "s_fl",sessCt="s_ct",sessTp="s_tp",sessOrder="s_or";
  private String[] prmArray = { prmCx ,prmBu ,prmFl,prmCt,prmTp,prmOrder};
  private String[] sessArray = {sessCx ,sessBu ,sessFl,sessCt,sessTp,sessOrder};
  private String[] sValues = {sCLBU,sCLFL,sCLCX,sCLCT,sCLTP,sORDER};
  protected boolean isAdmin = false;
  private boolean fetch = false;

  private String sessConPrm = "sess_con_status";

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
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);

    fetch = false;
    for (int i = 0; i < prmArray.length; i++) {
      if(iwc.getParameter(prmArray[i])!=null){
        sValues[i] = (iwc.getParameter(prmArray[i]));
        iwc.setSessionAttribute(sessArray[i],sValues[i]);
        fetch = true;
      }
      else if(iwc.getSessionAttribute(sessArray[i])!=null){
        sValues[i] = ((String)iwc.getSessionAttribute(sessArray[i]));
        fetch = true;
      }
    }

    if(isAdmin){
        add(statusForm());
        if(fetch)
         add(getPhoneTable(iwc));
    }
    else
      add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));
    //add(String.valueOf(iSubjectId));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private Form statusForm(){
    Form myForm = new Form();
    DropdownMenu complex = drpLodgings(((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).createLegacy(),prmArray[0],"--",sValues[0]);
    DropdownMenu building = drpLodgings(((com.idega.block.building.data.BuildingHome)com.idega.data.IDOLookup.getHomeLegacy(Building.class)).createLegacy(),prmArray[1],"--",sValues[1]);
    DropdownMenu floor = drpFloors(prmArray[2],"--",sValues[2],true);
    DropdownMenu cat = drpLodgings(((com.idega.block.building.data.ApartmentCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).createLegacy(),prmArray[3],"--",sValues[3]);
    DropdownMenu type = drpLodgings(((com.idega.block.building.data.ApartmentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).createLegacy(),prmArray[4],"--",sValues[4]);
    DropdownMenu order = orderDrop(prmArray[5],"--",sValues[5]);
    //Edit.setStyle(status);
    Edit.setStyle(complex);
    Edit.setStyle(building);
    Edit.setStyle(floor);
    Edit.setStyle(cat);
    Edit.setStyle(type);
    Edit.setStyle(order);
    Table T = new Table();
      T.add(Edit.formatText(iwrb.getLocalizedString("complex","Complex")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("building","Building")),2,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("floor","Floor")),3,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("category","Category")),4,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("type","Type")),5,1);
     // T.add(Edit.formatText(iwrb.getLocalizedString("order","Order")),2,1);
     // T.add(status,1,2);
      T.add(complex,1,2);
      T.add(building,2,2);
      T.add(floor,3,2);
      T.add(cat,4,2);
      T.add(type,5,2);
      //T.add(order,2,2);
      SubmitButton get = new SubmitButton("conget",iwrb.getLocalizedString("get","Get"));
      Edit.setStyle(get);
      T.add(get,1,3);
    T.setCellpadding(1);
    T.setCellspacing(0);

    myForm.add(T);
    return myForm;
  }

  private DropdownMenu drpLodgings(IDOLegacyEntity lodgings,String name,String display,String selected) {
    List L = null;
    try{
      L = EntityFinder.findAll(lodgings);
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addDisabledMenuElement("-1",display);
    if(L != null){
      int len = L.size();
      if(len > 0){
        for(int i = 0; i < len ; i++){
          IDOLegacyEntity ge = (IDOLegacyEntity) L.get(i);
          drp.addMenuElement(ge.getID(),ge.getName());
        }
        if(!"".equalsIgnoreCase(selected)){
          drp.setSelectedElement(selected);
        }
      }
    }
    return drp;
  }

  private DropdownMenu drpFloors(String name,String display,String selected,boolean withBuildingName) {
    List L = null;
    try{
      L = EntityFinder.getInstance().findAll(Floor.class);
    }
    catch(Exception e){}
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addDisabledMenuElement("-1",display);
    if(L!= null){
      int len = L.size();
      for(int i = 0; i < len ; i++){
        Floor ge = (Floor) L.get(i);
        if(withBuildingName){
          try{
          drp.addMenuElement(ge.getID() ,ge.getName()+" "+(BuildingCacher.getBuilding(ge.getBuildingId()).getName()));
          }
          catch(Exception e){}}
        else
          drp.addMenuElement(ge.getID() ,ge.getName());
      }
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


  private PresentationObject getPhoneTable(IWContext iwc){
    Form form = new Form();
    int order = Integer.parseInt(sValues[5]);
    List L = BuildingFinder.listOfApartments(sValues[0],sValues[1],sValues[2],sValues[3],sValues[4],order);
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
    if(L!=null){

      int len = L.size();
      int row = 2;
      int col = 1;
      for (int i = 0; i < len; i++) {
        col = 1;
        try {

          A = (Apartment) L.get(i);
          Integer ID = new Integer(A.getID());
          TextInput ti = new TextInput("ti_"+i);
          T.add(new HiddenInput("apid"+i,String.valueOf(A.getID())));
          T.add(Edit.formatText(i+1),1,row);
          T.add((getApartmentTable(A)),2,row);
          ti.setLength(10);
          Edit.setStyle(ti);
          T.add(ti,3,row);
          if(M != null && M.containsKey(ID)){
            P = (CampusPhone)M.get(ID);
            ti.setContent(P.getPhoneNumber());
            T.add(Edit.formatText(new IWTimestamp(P.getDateInstalled()).getLocaleDate(iwc)),4,row);
            T.add(new HiddenInput("phoneid"+i,String.valueOf(P.getID())),1,row);
          }
          row++;
          col = 1;
        }
        catch (Exception ex) {  ex.printStackTrace(); }
        }
        form.add(new HiddenInput("ap_count",String.valueOf(len)));
        T.add(Edit.titleText(iwrb.getLocalizedString("apartment","Apartment")),2,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("number","Number")),3,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("installed","Installed")),4,1);


        T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
        T.setRowColor(1,Edit.colorBlue);
        T.setRowColor(row,Edit.colorRed);
        T.mergeCells(1,row,8,row);
        T.setWidth(1,"15");
        T.add(Edit.formatText(" "),1,row);
        T.setColumnAlignment(1,"left");
        T.setHeight(row,Edit.bottomBarThickness);
        //T.setWidth("100%");
    }
    else
      add(Edit.formatText(iwrb.getLocalizedString("no_apartments","No Apartments")));
    form.add(T);
    form.add(new SubmitButton("save",iwrb.getLocalizedString("save","Save")));
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
                  P.setDateInstalled(IWTimestamp.RightNow().getSQLDate());
                  P.update();
                }
              }
              // if new entity
              else{
                CampusPhone P = ((is.idega.idegaweb.campus.block.phone.data.CampusPhoneHome)com.idega.data.IDOLookup.getHomeLegacy(CampusPhone.class)).createLegacy();
                P.setPhoneNumber(sNumber);
                P.setApartmentId(iAPId.intValue());
                P.setDateInstalled(IWTimestamp.RightNow().getSQLDate());
                P.insert();
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
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    T.add(Edit.formatText(A.getName()),1,1);
    T.add(Edit.formatText(F.getName()),2,1);
    T.add(Edit.formatText(B.getName()),3,1);
    T.add(Edit.formatText(C.getName()),4,1);
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
