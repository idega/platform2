package is.idegaweb.campus.allocation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;
import com.idega.core.user.data.User;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicationSubject;
import is.idegaweb.campus.entity.*;
import com.idega.block.application.business.*;
import com.idega.block.building.data.*;
import com.idega.block.building.business.BuildingCacher;
import com.idega.util.idegaTimestamp;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.jmodule.object.Image;
import is.idegaweb.campus.application.*;
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

public class CampusContracts extends KeyEditor{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private String redColor = "#942829";
  private String blueColor = "#27324B",lightBlue ="#ECEEF0";
  private int iSubjectId = -1;
  private String sGlobalStatus = "C";
  private ListIterator iterator = null;
  private LinkedList linkedlist = null;
  private String bottomThickness = "8";
  private String conPrm = "contract_status";
  private String sessConPrm = "sess_con_status";

  /*
  Blár litur í topp # 27324B
  Hvítur litur fyrir neðan það # FFFFFF
  Ljósblár litur í töflu # ECEEF0
  Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public CampusContracts(String sHeader) {
    super(sHeader);
  }

  protected void control(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    this.fontSize = 1;

    if(modinfo.getParameter(conPrm)!=null){
      this.sGlobalStatus= (modinfo.getParameter(conPrm));
      modinfo.setSessionAttribute(sessConPrm,sGlobalStatus);
    }
    else if(modinfo.getSessionAttribute(sessConPrm)!=null){
      this.sGlobalStatus = ((String)modinfo.getSessionAttribute(sessConPrm));
    }

    if(isAdmin){
        add(statusForm());
        if(modinfo.getParameter("sign")!=null){
          doSignContract(modinfo);
          add(getContractTable(modinfo));
        }
        else if(modinfo.getParameter("signed_id")!=null){
          add(getSignatureTable(modinfo));
        }
        else
          add(getContractTable(modinfo));
      }

    else
      add(formatText(iwrb.getLocalizedString("access_denied","Access denied")));
    //add(String.valueOf(iSubjectId));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }
   private Form subjectForm(){
    Form myForm = new Form();
    DropdownMenu drp = contractDrop("");
    DropdownMenu status = statusDrop(conPrm,sGlobalStatus);
    drp.setToSubmit();
    status.setToSubmit();
    setStyle(status);
    myForm.add(drp);
    myForm.add(status);
    return myForm;
  }

  private Form statusForm(){
    Form myForm = new Form();
    DropdownMenu status = statusDrop(conPrm,sGlobalStatus);
    status.setToSubmit();
    setStyle(status);
    myForm.add(status);
    return myForm;
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
      setStyle(drp);
      drp.setSelectedElement(selected);
    }
    return drp;
  }

  private ModuleObject getContractTable(ModuleInfo modinfo){

    List L = ContractFinder.listOfStatusContracts(this.sGlobalStatus);
    Contract C = null;
    User U = null;
    Applicant Ap = null;
    Apartment A = null;
    Building B = null;
    Floor F = null;
    Complex CX = null;
    Table T = new Table();
    if(L!=null){
      Image printImage = new Image("/pics/print.gif");
      Image registerImage = new Image("/pics/register.gif");
      int len = L.size();
      int row = 2;
      for (int i = 0; i < len; i++) {

        try {
          C = (Contract) L.get(i);
          U = new User(C.getUserId().intValue());
          Ap = new Applicant(C.getApplicantId().intValue());
          A = new Apartment(C.getApartmentId().intValue());
          //if(C.getStatus().equalsIgnoreCase(Contract.statusCreated))
            T.add(getPDFLink(printImage,C.getID(),Ap.getSSN()),1,row);
          if(C.getStatus().equalsIgnoreCase(Contract.statusPrinted))
            T.add(getSignedLink(registerImage,C.getID()),2,row);
          T.add(formatText(Ap.getFullName()),3,row);
          T.add(formatText(Ap.getSSN()),4,row);
          T.add((getApartmentTable(A)),5,row);
          T.add(formatText(C.getValidFrom().toString()),6,row);
          T.add(formatText(C.getValidTo().toString()),7,row);
          row++;
        }
        catch (SQLException ex) {  ex.printStackTrace(); }
        }
        T.add(headerText(" "),1,1);
        T.add(headerText(iwrb.getLocalizedString("name","Name")),3,1);
        T.add(headerText(iwrb.getLocalizedString("ssn","Socialnumber")),4,1);
        T.add(headerText(iwrb.getLocalizedString("apartment","Apartment")),5,1);
        T.add(headerText(iwrb.getLocalizedString("validfrom","Valid from")),6,1);
        T.add(headerText(iwrb.getLocalizedString("validto","Valid To")),7,1);
        T.setHorizontalZebraColored(lightBlue,WhiteColor);
        T.setRowColor(1,blueColor);
        T.setRowColor(row,redColor);
        //T.mergeCells(1,1,2,1);
        T.mergeCells(1,row,8,row);
        T.setWidth(1,"15");
        T.add(formatText(" "),1,row);
        T.setColumnAlignment(1,"left");
        T.setHeight(row,bottomThickness);
        T.setWidth("100%");
    }
    else
      add(formatText(iwrb.getLocalizedString("no_contracts","No contracts")));
    return T;
  }

  private ModuleObject getSignatureTable(ModuleInfo modinfo){
    int iContractId = Integer.parseInt( modinfo.getParameter("signed_id"));
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

  private void doSignContract(ModuleInfo modinfo){
    int id = Integer.parseInt(modinfo.getParameter("signed_id"));
    try {
      Contract eContract = new Contract(id);
      eContract.setStatusSigned();
      eContract.update();
    }
    catch (SQLException ex) {

    }
  }

  private ModuleObject getApartmentTable(Apartment A){
    Table T = new Table();
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    T.add(formatText(A.getName()),1,1);
    T.add(formatText(F.getName()),2,1);
    T.add(formatText(B.getName()),3,1);
    T.add(formatText(C.getName()),4,1);
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

   public Text headerText(String text){
    Text T = new Text(text);
    T.setBold();
    T.setFontColor(this.WhiteColor);
    T.setFontSize(1);
    return T;
  }

  public Link getSignedLink(ModuleObject MO,int contractId){
    Window W = new Window("Signature","/allocation/contractsign.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("signed_id",contractId);
    return L;
  }

  public Link getPDFLink(ModuleObject MO,int contractId){
    Window W = new Window("PDF","/allocation/contractfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("contract_id",contractId);
    return L;
  }

  public Link getPDFLink(ModuleObject MO,int contractId,String filename){
    Window W = new Window("PDF","/allocation/contractfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("contract_id",contractId);
    L.addParameter("fname",filename);
    return L;
  }
   public Link getPDFLink(ModuleObject MO,String status){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("app_status",status);
    return L;
  }
}