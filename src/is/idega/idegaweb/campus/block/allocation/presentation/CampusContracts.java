package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.allocation.business.*;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.finance.presentation.*;
import com.idega.core.user.data.User;
import com.idega.data.GenericEntity;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicationSubject;

import com.idega.block.application.business.*;
import com.idega.block.building.data.*;
import com.idega.block.building.business.BuildingCacher;
import com.idega.util.idegaTimestamp;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.presentation.Image;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.List;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class CampusContracts extends Block{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus.block.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private int iSubjectId = -1;
  private String sGlobalStatus = "C", sCLBU="-1",sCLFL="-1",sCLCX="-1",sCLTP="-1",sCLCT="-1",sORDER = "-1";
  private ListIterator iterator = null;
  private LinkedList linkedlist = null;


  private final String prmCx = "cl_clx",prmBu = "cl_bu",prmFl = "cl_fl",prmCt="cl_ct",prmTp="cl_tp",prmOrder="ct_or";
  private final String sessCx = "s_clx",sessBu = "s_bu",sessFl = "s_fl",sessCt="s_ct",sessTp="s_tp",sessOrder="s_or";
  private String[] prmArray = { prmCx ,prmBu ,prmFl,prmCt,prmTp,prmOrder};
  private String[] sessArray = {sessCx ,sessBu ,sessFl,sessCt,sessTp,sessOrder};
  private String[] sValues = {sCLBU,sCLFL,sCLCX,sCLCT,sCLTP,sORDER};
  protected boolean isAdmin = false;
  private String conPrm = "contract_status";
  private String sessConPrm = "sess_con_status";

  public String getLocalizedNameKey(){
    return "contracts";
  }

  public String getLocalizedNameValue(){
    return "Contracts";
  }

  protected void control(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    for (int i = 0; i < prmArray.length; i++) {
      if(iwc.getParameter(prmArray[i])!=null){
      sValues[i] = (iwc.getParameter(prmArray[i]));
      iwc.setSessionAttribute(sessArray[i],sValues[i]);
      }
      else if(iwc.getSessionAttribute(sessArray[i])!=null){
        sValues[i] = ((String)iwc.getSessionAttribute(sessArray[i]));
      }
    }

    if(iwc.getParameter(conPrm)!=null){
      this.sGlobalStatus= (iwc.getParameter(conPrm));
      iwc.setSessionAttribute(sessConPrm,sGlobalStatus);
    }
    else if(iwc.getSessionAttribute(sessConPrm)!=null){
      this.sGlobalStatus = ((String)iwc.getSessionAttribute(sessConPrm));
    }

    if(isAdmin){
        add(statusForm());
        if(iwc.getParameter("sign")!=null){
          doSignContract(iwc);
          add(getContractTable(iwc));
        }
        else if(iwc.getParameter("signed_id")!=null){
          add(getSignatureTable(iwc));
        }
        else
          add(getContractTable(iwc));
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
   private Form subjectForm(){
    Form myForm = new Form();
    DropdownMenu drp = contractDrop("");
    DropdownMenu status = statusDrop(conPrm,sGlobalStatus);
    drp.setToSubmit();
    status.setToSubmit();
    Edit.setStyle(status);
    myForm.add(drp);
    myForm.add(status);
    return myForm;
  }

  private Form statusForm(){
    Form myForm = new Form();
    DropdownMenu status = statusDrop(conPrm,sGlobalStatus);
    DropdownMenu complex = drpLodgings(new Complex(),prmArray[0],"--",sValues[0]);
    DropdownMenu building = drpLodgings(new Building(),prmArray[1],"--",sValues[1]);
    DropdownMenu floor = drpFloors(prmArray[2],"--",sValues[2],true);
    DropdownMenu cat = drpLodgings(new ApartmentCategory(),prmArray[3],"--",sValues[3]);
    DropdownMenu type = drpLodgings(new ApartmentType(),prmArray[4],"--",sValues[4]);
    DropdownMenu order = orderDrop(prmArray[5],"--",sValues[5]);
    Edit.setStyle(status);
    Edit.setStyle(complex);
    Edit.setStyle(building);
    Edit.setStyle(floor);
    Edit.setStyle(cat);
    Edit.setStyle(type);
    Edit.setStyle(order);
    Table T = new Table();
      T.add(Edit.formatText(iwrb.getLocalizedString("status","Status")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("complex","Complex")),1,3);
      T.add(Edit.formatText(iwrb.getLocalizedString("building","Building")),2,3);
      T.add(Edit.formatText(iwrb.getLocalizedString("floor","Floor")),3,3);
      T.add(Edit.formatText(iwrb.getLocalizedString("category","Category")),4,3);
      T.add(Edit.formatText(iwrb.getLocalizedString("type","Type")),5,3);
      T.add(Edit.formatText(iwrb.getLocalizedString("order","Order")),2,1);
      T.add(status,1,2);
      T.add(complex,1,4);
      T.add(building,2,4);
      T.add(floor,3,4);
      T.add(cat,4,4);
      T.add(type,5,4);
      T.add(order,2,2);
      SubmitButton get = new SubmitButton("conget",iwrb.getLocalizedString("get","Get"));
      Edit.setStyle(get);
      T.add(get,3,2);
    T.setCellpadding(1);
    T.setCellspacing(0);

    myForm.add(T);
    return myForm;
  }

  private DropdownMenu drpLodgings(GenericEntity lodgings,String name,String display,String selected) {
    GenericEntity[] lods = new GenericEntity[0];
    try{
      lods =  (lodgings).findAll();
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addDisabledMenuElement("-1",display);
    int len = lods.length;
    if(len > 0){
      for(int i = 0; i < len ; i++){
        drp.addMenuElement(lods[i].getID(),lods[i].getName());
      }
      if(!"".equalsIgnoreCase(selected)){
        drp.setSelectedElement(selected);
      }
    }
    return drp;
  }

  private DropdownMenu drpFloors(String name,String display,String selected,boolean withBuildingName) {
    Floor[] lods = new Floor[1];
    try{
      lods =  (Floor[])(new Floor()).findAll();
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addDisabledMenuElement("-1",display);
    for(int i = 0; i < lods.length ; i++){
      if(withBuildingName){
        try{
        drp.addMenuElement(lods[i].getID() ,lods[i].getName()+" "+(BuildingCacher.getBuilding(lods[i].getBuildingId()).getName()));
        }
        catch(Exception e){}}
      else
        drp.addMenuElement(lods[i].getID() ,lods[i].getName());
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
    drp.addMenuElement(ContractFinder.NAME,iwrb.getLocalizedString("name","Name"));
    drp.addMenuElement(ContractFinder.SSN,iwrb.getLocalizedString("ssn","Socialnumber"));
    drp.addMenuElement(ContractFinder.APARTMENT,iwrb.getLocalizedString("ssn","Socialnumber"));
    drp.addMenuElement(ContractFinder.FLOOR,iwrb.getLocalizedString("floor","Floor"));
    drp.addMenuElement(ContractFinder.BUILDING,iwrb.getLocalizedString("building","Building"));
    drp.addMenuElement(ContractFinder.COMPLEX,iwrb.getLocalizedString("complex","Complex"));
    drp.addMenuElement(ContractFinder.CATEGORY,iwrb.getLocalizedString("category","Category"));
    drp.addMenuElement(ContractFinder.TYPE,iwrb.getLocalizedString("type","Type"));
    if(!"".equals(selected))
      drp.setSelectedElement(selected);
    return drp;
  }

  private DropdownMenu contractDrop(String selected){
    List L = ContractFinder.listOfStatusContracts(this.sGlobalStatus);
    DropdownMenu drp = new DropdownMenu("app_contract_id");
    drp.addMenuElement(-1,iwrb.getLocalizedString("contract","Contract"));
    if(L!=null){
      Contract C = null;
      int len = L.size();
      for (int i = 0; i < len; i++) {
        try{
        C = (Contract) L.get(i);
        Applicant A = new Applicant(C.getApplicantId().intValue());
        drp.addMenuElement(C.getID(),A.getName());
        }
        catch(SQLException ex){
        }
      }
      Edit.setStyle(drp);
      drp.setSelectedElement(selected);
    }
    return drp;
  }

  private PresentationObject getContractTable(IWContext iwc){
    int order = Integer.parseInt(sValues[5]);
    List L = ContractFinder.listOfContracts(sValues[0],sValues[1],sValues[2],sValues[3],sValues[4],sGlobalStatus,order);
    //List L = ContractFinder.listOfStatusContracts(this.sGlobalStatus);
    Contract C = null;
    User U = null;
    Applicant Ap = null;
    Apartment A = null;
    Building B = null;
    Floor F = null;
    Complex CX = null;
    Table T = new Table();
    T.setCellspacing(0);
    T.setCellpadding(2);
    if(L!=null){
      Image printImage =  iwb.getImage("/print.gif");
      Image registerImage = iwb.getImage("/pen.gif");
      Image resignImage = iwb.getImage("/scissors.gif");
      int len = L.size();
      int row = 2;
      int col = 1;
      StringBuffer sbIDs = new StringBuffer();
      for (int i = 0; i < len; i++) {
        col = 1;
        try {

          C = (Contract) L.get(i);
          sbIDs.append(C.getID());
          sbIDs.append(ContractFiler.prmSeperator);
          U = new User(C.getUserId().intValue());
          Ap = new Applicant(C.getApplicantId().intValue());
          A = new Apartment(C.getApartmentId().intValue());
          T.add(Edit.formatText(i+1),col++,row);
          //if(C.getStatus().equalsIgnoreCase(Contract.statusCreated) || C.getStatus().equalsIgnoreCase(Contract.statusPrinted) )
          T.add(getPDFLink(printImage,C.getID(),Ap.getSSN()),col,row);
          if(C.getStatus().equalsIgnoreCase(Contract.statusSigned))
            T.add(getReSignLink(resignImage,C.getID()),col,row);
          col++;
          if(C.getStatus().equalsIgnoreCase(Contract.statusPrinted) || C.getStatus().equalsIgnoreCase(Contract.statusSigned)  )
            T.add(getSignedLink(registerImage,C.getID()),col,row);
          col++;
          T.add(Edit.formatText(Ap.getFullName()),col++,row);
          T.add(Edit.formatText(Ap.getSSN()),col++,row);
          T.add((getApartmentTable(A)),col++,row);
          T.add(Edit.formatText(C.getValidFrom().toString()),col++,row);
          T.add(Edit.formatText(C.getValidTo().toString()),col++,row);
          row++;
          col = 1;
        }
        catch (SQLException ex) {  ex.printStackTrace(); }
        }
        T.add(getPDFLink(printImage,sbIDs.toString()),1,row);
        T.add(Edit.titleText(" "),col++,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("print","Print")),col++,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("sign","Sign")),col++,1);
        col = 4;
        T.add(Edit.titleText(iwrb.getLocalizedString("name","Name")),col++,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("ssn","Socialnumber")),col++,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("apartment","Apartment")),col++,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("validfrom","Valid from")),col++,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("validto","Valid To")),col++,1);
        T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
        T.setRowColor(1,Edit.colorBlue);
        T.setRowColor(row,Edit.colorRed);
        T.mergeCells(1,row,8,row);
        T.setWidth(1,"15");
        T.add(Edit.formatText(" "),1,row);
        T.setColumnAlignment(1,"left");
        T.setHeight(row,Edit.bottomBarThickness);
        T.setWidth("100%");
    }
    else
      add(Edit.formatText(iwrb.getLocalizedString("no_contracts","No contracts")));
    return T;
  }

  private PresentationObject getSignatureTable(IWContext iwc){
    int iContractId = Integer.parseInt( iwc.getParameter("signed_id"));
    try {
      Contract eContract = new Contract(iContractId);
      Applicant eApplicant = new Applicant(eContract.getApplicantId().intValue());
      Table T = new Table();
      T.add(eApplicant.getFullName(),1,1);
      SubmitButton save = new SubmitButton("sign","Save");
      HiddenInput HI = new HiddenInput("signed_id",String.valueOf(eContract.getID()));
      T.add(save,1,2);
      T.add(HI,1,2);
      Form F = new Form();
      F.add(T);
      return F;
    }
    catch (SQLException ex) {
      return new Text("");
    }
  }

  private void doSignContract(IWContext iwc){
    int id = Integer.parseInt(iwc.getParameter("signed_id"));
    try {
      Contract eContract = new Contract(id);
      eContract.setStatusSigned();
      eContract.update();
    }
    catch (SQLException ex) {

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

  private String getStatus(String status){
    String r = "";
    char c = status.charAt(0);
    switch (c) {
      case 'C': r = iwrb.getLocalizedString("created","Created"); break;
      case 'P': r = iwrb.getLocalizedString("printed","Printed"); break;
      case 'S': r = iwrb.getLocalizedString("signed","Signed");   break;
      case 'R': r = iwrb.getLocalizedString("rejected","Rejected");  break;
      case 'T': r = iwrb.getLocalizedString("terminated","Terminated");   break;
      case 'E': r = iwrb.getLocalizedString("ended","Ended");  break;
    }
    return r;
  }

  private DropdownMenu statusDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("C",getStatus("C"));
    drp.addMenuElement("P",getStatus("P"));
    drp.addMenuElement("S",getStatus("S"));
    drp.addMenuElement("R",getStatus("R"));
    drp.addMenuElement("T",getStatus("T"));
    drp.addMenuElement("E",getStatus("E"));
    drp.setSelectedElement(selected);
    return drp;
  }



  public Link getSignedLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractSignWindow.class);
    L.addParameter("signed_id",contractId);
    return L;
  }

   public Link getReSignLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractReSignWindow.class);
    L.addParameter("contract_id",contractId);
    return L;
  }

  public Link getPDFLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
    L.addParameter(ContractFiler.prmOneId,contractId);
    return L;
  }

  public Link getPDFLink(PresentationObject MO,int contractId,String filename){
    Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
    L.addParameter(ContractFiler.prmOneId,contractId);
    L.addParameter(ContractFiler.prmFileName,filename);
    return L;
  }
   public Link getPDFLink(PresentationObject MO,String ids){
    Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
    L.addParameter(ContractFiler.prmManyIds,ids);
    return L;
  }


  public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}
