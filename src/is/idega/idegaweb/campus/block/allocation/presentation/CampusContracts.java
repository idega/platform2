package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.presentation.Edit;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.core.user.data.User;
import com.idega.data.IDOLegacyEntity;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
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

public class CampusContracts extends Block{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private int iSubjectId = -1, iGlobalSize =50;
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
  private String sizePrm = "global_size";
  private String sessSizePrm = "sess_global_size";

  public String getLocalizedNameKey(){
    return "contracts";
  }

  public String getLocalizedNameValue(){
    return "Contracts";
  }

  protected void control(IWContext iwc){
    is.idega.idegaweb.campus.presentation.SysPropsSetter.isSysPropsInMemoryElseLoad(iwc);
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
    
	if(iwc.getParameter(sizePrm)!=null){
		 this.iGlobalSize= Integer.parseInt(iwc.getParameter(sizePrm));
		 iwc.setSessionAttribute(sessSizePrm,new Integer(iGlobalSize));
	   }
	   else if(iwc.getSessionAttribute(sessSizePrm)!=null){
		 this.iGlobalSize = ((Integer)iwc.getSessionAttribute(sessSizePrm)).intValue();
	   }

    if(isAdmin){
        add(statusForm());
        if(iwc.getParameter("garbage")!=null){
          doGarbageContract(iwc);
        }
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
    DropdownMenu complex = drpLodgings(((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).createLegacy(),prmArray[0],"--",sValues[0]);
    DropdownMenu building = drpLodgings(((com.idega.block.building.data.BuildingHome)com.idega.data.IDOLookup.getHomeLegacy(Building.class)).createLegacy(),prmArray[1],"--",sValues[1]);
    DropdownMenu floor = drpFloors(prmArray[2],"--",sValues[2],true);
    //DropdownMenu cat = drpLodgings(((com.idega.block.building.data.ApartmentCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).createLegacy(),prmArray[3],"--",sValues[3]);
    //DropdownMenu type = drpLodgings(((com.idega.block.building.data.ApartmentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).createLegacy(),prmArray[4],"--",sValues[4]);
    DropdownMenu order = orderDrop(prmArray[5],"--",sValues[5]);
    DropdownMenu sizeMenu = sizeDrop(sizePrm,iGlobalSize);
    Edit.setStyle(status);
    Edit.setStyle(complex);
    Edit.setStyle(building);
    Edit.setStyle(floor);
	Edit.setStyle(sizeMenu);
    //Edit.setStyle(cat);
    //Edit.setStyle(type);
    Edit.setStyle(order);
    DataTable T = new DataTable();
    T.addTitle(iwrb.getLocalizedString("filter","Filter"));
    T.setTitlesHorizontal(true);
    T.setBottomHeight(3);
    //Table T = new Table();
      T.add(Edit.formatText(iwrb.getLocalizedString("status","Status")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("complex","Complex")),2,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("building","Building")),3,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("floor","Floor")),4,1);
     // T.add(Edit.formatText(iwrb.getLocalizedString("category","Category")),4,1);
     // T.add(Edit.formatText(iwrb.getLocalizedString("type","Type")),5,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("order","Order")),5,1);
	T.add(Edit.formatText(iwrb.getLocalizedString("viewsize","View size")),6,1);
      T.add(status,1,2);
      T.add(complex,2,2);
      T.add(building,3,2);
      T.add(floor,4,2);
	T.add(order,5,2);
		T.add(sizeMenu,6,2);
      //T.add(cat,4,2);
      //T.add(type,5,2);
    
      SubmitButton get = new SubmitButton("conget",iwrb.getLocalizedString("get","Get"));
      Edit.setStyle(get);
      T.add(get,7,2);

    myForm.add(T);
    return myForm;
  }
  
  private DropdownMenu sizeDrop(String name, int  selected) {
			  DropdownMenu drp = new DropdownMenu(name);
			  drp.addMenuElement("10");
			  drp.addMenuElement("20");
			  drp.addMenuElement("50");
			  drp.addMenuElement("100");
			  drp.addMenuElement("500");
			  drp.addMenuElement("-1","All");
			  drp.setSelectedElement(selected);
			  return drp;
		  }

  private DropdownMenu drpLodgings(IDOLegacyEntity lodgings,String name,String display,String selected) {
    IDOLegacyEntity[] lods = new IDOLegacyEntity[0];
    try{
      lods =  (lodgings).findAll();
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addMenuElement("-1",display);
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
      lods =  (Floor[])(((com.idega.block.building.data.FloorHome)com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).createLegacy()).findAll();
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    if(!"".equals(display))
      drp.addMenuElement("-1",display);
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
    drp.addMenuElement(ContractFinder.APARTMENT,iwrb.getLocalizedString("apartment","Apartment"));
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
        Applicant A = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(C.getApplicantId().intValue());
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
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
    Contract C = null;
    User U = null;
    Applicant Ap = null;
    Apartment A = null;
    Building B = null;
    Floor F = null;
    Complex CX = null;
    Image printImage =  iwb.getImage("/print.gif");
    Image registerImage = iwb.getImage("/pen.gif");
    Image resignImage = iwb.getImage("/scissors.gif");
    Image keyImage = iwb.getImage("/key.gif");
    Image nokeyImage = iwb.getImage("/nokey.gif");
    Image garbageImage = iwb.getImage("/trashcan.gif");
    Image renewImage= iwb.getImage("/renew.gif");
    boolean garbage = false;
    int row = 1;
    int col = 1;
    DataTable T = new DataTable();
   
    T.setTitlesHorizontal(true);
    T.setWidth("100%");
    T.add(Edit.formatText("#"),col++,1);
    if(sGlobalStatus.equals(ContractBMPBean.statusEnded) || sGlobalStatus.equals(ContractBMPBean.statusResigned) || sGlobalStatus.equals(ContractBMPBean.statusRejected)){
      T.add((garbageImage),col++,1);
      garbage = true;
    }
    else
      T.add((printImage),col++,1);//Edit.formatText(iwrb.getLocalizedString("print","Print")
    T.add((resignImage),col++,1);//Edit.formatText(iwrb.getLocalizedString("sign","Sign"))
    T.add((registerImage),col++,1);//Edit.formatText(iwrb.getLocalizedString("sign","Sign"))
    T.add((renewImage),col++,1);
    //col = 4;
    T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),col++,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("ssn","Socialnumber")),col++,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("apartment","Apartment")),col++,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("validfrom","Valid from")),col++,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("validto","Valid To")),col++,1);
    T.add(keyImage,col++,1);//Edit.titleText(iwrb.getLocalizedString("key","Key")),col++,1);
    /*
    Table T = new Table();
    T.setCellspacing(0);
    T.setCellpadding(2);
    */
    row++;
    if(L!=null){

      int len = L.size();
      if(iGlobalSize>0&& iGlobalSize<=len){
      	len = iGlobalSize;
      }
	  T.addTitle(iwrb.getLocalizedString("contracts","Contracts")+" "+iwrb.getLocalizedString("showing","showing")
				  +" "+len+" "+iwrb.getLocalizedString("of","of")+" "+L.size());

      StringBuffer sbIDs = new StringBuffer();
      for (int i = 0; i < len; i++) {
        col = 1;
        try {

          C = (Contract) L.get(i);
          String status = C.getStatus();
          sbIDs.append(C.getID());
          sbIDs.append(ContractFiler.prmSeperator);
          U = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(C.getUserId().intValue());
          Ap = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(C.getApplicantId().intValue());
          A = ((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKeyLegacy(C.getApartmentId().intValue());
          T.add(getEditLink(Edit.formatText(i+1),C.getID()),col++,row);
          //if(C.getStatus().equalsIgnoreCase(ContractBMPBean.statusCreated) || C.getStatus().equalsIgnoreCase(ContractBMPBean.statusPrinted) )
          if(garbage)
            T.add(getGarbageLink(garbageImage,C.getID()),col++,row);
          else
            T.add(getPDFLink(printImage,C.getID(),Ap.getSSN()),col++,row);
          if(status.equalsIgnoreCase(ContractBMPBean.statusSigned))
            T.add(getReSignLink(resignImage,C.getID()),col,row);
          col++;
          if(status.equalsIgnoreCase(ContractBMPBean.statusPrinted) || status.equalsIgnoreCase(ContractBMPBean.statusSigned)  )
            T.add(getSignedLink(registerImage,C.getID(),isAdmin),col,row);
          col++;
          if(status.equalsIgnoreCase(ContractBMPBean.statusEnded) || status.equalsIgnoreCase(ContractBMPBean.statusResigned) || status.equalsIgnoreCase(ContractBMPBean.statusSigned))
             T.add(getRenewLink(renewImage,C.getID()),col,row);
          col++;
          T.add(Edit.formatText(Ap.getFullName()),col++,row);
          T.add(Edit.formatText(Ap.getSSN()),col++,row);
          T.add((getApartmentTable(A)),col++,row);
          T.add(Edit.formatText(df.format(C.getValidFrom())),col++,row);
          T.add(Edit.formatText(df.format(C.getValidTo())),col++,row);
          if(C.getIsRented())
            T.add(getKeyLink(keyImage,C.getID()),col++,row);
          else
            T.add(getKeyLink(nokeyImage,C.getID()),col++,row);
          row++;
          col = 1;
        }
        catch (SQLException ex) {  ex.printStackTrace(); }
        }
        /*
        T.add(getPDFLink(printImage,sbIDs.toString()),1,row);

        /*
        T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
        T.setRowColor(1,Edit.colorBlue);
        T.setRowColor(row,Edit.colorRed);
        T.mergeCells(1,row,8,row);
        T.setWidth(1,"15");
        T.add(Edit.formatText(" "),1,row);
        T.setColumnAlignment(1,"left");
        T.setHeight(row,Edit.bottomBarThickness);
        */

    }
    //else
     // add(Edit.formatText(iwrb.getLocalizedString("no_contracts","No contracts")));
    return T;
  }

  private PresentationObject getSignatureTable(IWContext iwc){
    int iContractId = Integer.parseInt( iwc.getParameter("signed_id"));
    try {
      Contract eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(iContractId);
      Applicant eApplicant = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eContract.getApplicantId().intValue());
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

  private void doGarbageContract(IWContext iwc){
    int id = Integer.parseInt(iwc.getParameter("garbage"));
    ContractBusiness.doGarbageContract(id);
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
      case 'U': r = iwrb.getLocalizedString("resigned","Resigned");   break;
      case 'E': r = iwrb.getLocalizedString("ended","Ended");  break;
      case 'G': r = iwrb.getLocalizedString("garbaged","Canned");  break;
    }
    return r;
  }

  private DropdownMenu statusDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("C",getStatus("C"));
    drp.addMenuElement("P",getStatus("P"));
    drp.addMenuElement("S",getStatus("S"));
    drp.addMenuElement("R",getStatus("R"));
    drp.addMenuElement("U",getStatus("U"));
    drp.addMenuElement("E",getStatus("E"));
    drp.setSelectedElement(selected);
    return drp;
  }



  public static Link getSignedLink(PresentationObject MO,int contractId,boolean isAdmin){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractSignWindow.class);
    L.addParameter("signed_id",contractId);
    if(isAdmin){
      L.addParameter(ContractSignWindow.prmAdmin,"true");
    }
    return L;
  }

  public static Link getEditLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractEditWindow.class);
    L.addParameter("contract_id",contractId);
    return L;
  }

  public static Link getRenewLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractRenewWindow.class);
    L.addParameter(ContractRenewWindow.prmContractId,contractId);
    return L;
  }

   public Link getReSignLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractResignWindow.class);
    if(isAdmin)
      L.addParameter(ContractResignWindow.prmAdmin,"true");

    L.addParameter("contract_id",contractId);
    return L;
  }

   public static Link getKeyLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractKeyWindow.class);
    L.addParameter(ContractKeyWindow.prmContractId,contractId);
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

   public Link getGarbageLink(PresentationObject MO,int contractId){
    Link L = new Link(MO);
    L.setWindowToOpen(ContractGarbageWindow.class);
    L.addParameter(ContractGarbageWindow.prmContractId,contractId);
    //L.addParameter("garbage",contractId);
    return L;
  }


  public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}
