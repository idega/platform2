package is.idegaweb.campus.allocation;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationHolder;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.*;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idegaweb.campus.application.*;
import is.idegaweb.campus.allocation.*;
import is.idegaweb.campus.entity.*;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.sql.SQLException;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class CampusAllocator extends KeyEditor{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private String redColor = "#942829";
  private String blueColor = "#27324B",lightBlue ="#ECEEF0";
  private int iSubjectId = -1;
  private String sGlobalStatus = "S";
  private ListIterator iterator = null;
  private LinkedList linkedlist = null;
  private int iTypeId,iComplexId;
  private Parameter pTypeId = null,pComplexId = null;
  private SystemProperties SysProps = null;
   private String bottomThickness = "8";

  public CampusAllocator(String sHeader) {
    super(sHeader);
  }

  protected void control(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    this.fontSize = 1;
    if(modinfo.getParameter("list")!=null){
      if(modinfo.getSessionAttribute("sess_cplx_id")!=null)
        modinfo.removeSessionAttribute("sess_cplx_id");
      if(modinfo.getSessionAttribute("sess_type_id")!=null)
        modinfo.removeSessionAttribute("sess_type_id");
    }
    if(modinfo.getParameter("type_id")!=null){
      pTypeId = new Parameter("type_id",modinfo.getParameter("type_id"));
      this.iTypeId = Integer.parseInt(modinfo.getParameter("type_id"));
      modinfo.setSessionAttribute("sess_type_id",new Integer(iTypeId));
    }
    else if(modinfo.getSessionAttribute("sess_type_id")!=null){
      this.iTypeId = ((Integer)modinfo.getSessionAttribute("sess_type_id")).intValue();
    }
    if(modinfo.getParameter("cplx_id")!=null){
      pComplexId = new Parameter("cplx_id",modinfo.getParameter("cplx_id"));
      this.iComplexId = Integer.parseInt(modinfo.getParameter("cplx_id"));
      modinfo.setSessionAttribute("sess_cplx_id",new Integer(iComplexId));
    }
    else if(modinfo.getSessionAttribute("sess_cplx_id")!=null){
      this.iComplexId = ((Integer)modinfo.getSessionAttribute("sess_cplx_id")).intValue();
    }

    if(modinfo.getApplicationAttribute(SysProps.getSystemPropertiesEnitityName())!=null){
      SysProps = (SystemProperties)modinfo.getApplicationAttribute(SysProps.getSystemPropertiesEnitityName());
    }

    Table Frame = new Table();
    Frame.add(getHomeLink());
    if(isAdmin){
      if(iTypeId > 0 && iComplexId > 0){

        if(modinfo.getParameter("allocate")!=null){
          int applicantId = Integer.parseInt(modinfo.getParameter("allocate"));
          Frame.add( getFreeApartments(iTypeId,iComplexId,applicantId,-1),3,1 );
          Frame.add( getApplicantInfo(applicantId,modinfo),1,1 );
        }
        else if(modinfo.getParameter("change")!=null){
          int iContractId = Integer.parseInt(modinfo.getParameter("change"));
          Frame.add( getFreeApartments(iTypeId,iComplexId,-1,iContractId),3,1 );
          //Frame.add( getContractTable(iContractId),3,1 );
          Frame.add( getWaitingList(iTypeId,iComplexId),1,1 );
        }
        else if(modinfo.getParameter("save_allocation")!=null){
          if(saveAllocation(modinfo))
            System.err.println("vistadist");
          else
             System.err.println("vistadist ekki");
          Frame.add( getWaitingList(iTypeId,iComplexId),1,1 );
        }
        else if(modinfo.getParameter("delete_allocation")!=null){
          deleteAllocation(modinfo);
          Frame.add( getWaitingList(iTypeId,iComplexId),1,1 );
        }
        else
          Frame.add( getWaitingList(iTypeId,iComplexId),1,1 );

      }
      else
        Frame.add(getCategoryLists(),1,1);

      Frame.setRowVerticalAlignment(1,"top");
      add(Frame);
  }
  else
    add(formatText(iwrb.getLocalizedString("access_denied","Access denied")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private ModuleObject getCategoryLists(){
    Table T = new Table();
    T.setWidth(500);
    List Categories = BuildingFinder.listOfApartmentCategory();
    if(Categories != null){
      int row = 2;
      int cLen = Categories.size();
      int listCount = 0,contractCount = 0,appliedCount =0,appCnt1=0,appCnt2=0,appCnt3=0;

      int totalCount = 0,totalFree = 0,totalApplied = 0,totApp1 = 0,totApp2= 0,totApp3 = 0;
      int freeCount = 0;
      int type,cmpx;
      Image printImage = new Image("/pics/print.gif");
      for (int i = 0; i < cLen; i++) {
        ApartmentCategory AC = (ApartmentCategory) Categories.get(i);
        List L = BuildingFinder.getApartmentTypesComplexForCategory(AC.getID());
        if(L!=null){
          int lLen = L.size();
          int catlist = 0,catfree=0,catcont=0,catapp=0,catcnt1=0,catcnt2=0,catcnt3=0;
          T.add(boldText(AC.getName()),1,row);
          T.mergeCells(1,row,2,row);
          row++;
          for (int j = 0; j < lLen; j++) {
            ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper)L.get(j);
            type = eAprtType.getKeyOne();
            cmpx = eAprtType.getKeyTwo();
            listCount = BuildingFinder.countApartmentsInTypeAndComplex(type,cmpx);
            contractCount = ContractFinder.countApartmentsInTypeAndComplex(type,cmpx,"");

            appliedCount = CampusApplicationFinder.countWaitingWithTypeAndComplex(type,cmpx,0);
            appCnt1 = CampusApplicationFinder.countWaitingWithTypeAndComplex(type,cmpx,1);
            appCnt2 = CampusApplicationFinder.countWaitingWithTypeAndComplex(type,cmpx,2);
            appCnt3 = CampusApplicationFinder.countWaitingWithTypeAndComplex(type,cmpx,3);

            totalCount+=listCount;
            catlist+=listCount;
            freeCount = listCount-contractCount;
            totalFree += freeCount;
            catfree  += freeCount;
            totalApplied += appliedCount;
            catapp += appliedCount;
            totApp1 += appCnt1;totApp2 += appCnt2;totApp3 += appCnt3;
            catcnt1+=appCnt1;catcnt2+= appCnt2;catcnt3+= appCnt3;

            T.add(getPDFLink(printImage,type,cmpx),1,row);
            T.add(getListLink(eAprtType),2,row);
            T.add(formatText(listCount),3,row);
            T.add(formatText(freeCount),4,row);
            T.add(formatText(appliedCount),5,row);
            T.add(formatText(appCnt1),6,row);
            T.add(formatText(appCnt2),7,row);
            T.add(formatText(appCnt3),8,row);
            row++;
          }
          T.add(boldText(catlist),3,row);
          T.add(boldText(catfree),4,row);
          T.add(boldText(catapp),5,row);
          T.add(boldText(catcnt1),6,row);
          T.add(boldText(catcnt2),7,row);
          T.add(boldText(catcnt3),8,row);
          row++;
        }
      }

      T.add(headerText(iwrb.getLocalizedString("apartment_category","Apartment category")),1,1);
      T.add(headerText(iwrb.getLocalizedString("apartments","Apartments")),3,1);
      T.add(headerText(iwrb.getLocalizedString("available","Available")),4,1);
      T.add(headerText(iwrb.getLocalizedString("applied","Applied")),5,1);
      T.add(headerText(iwrb.getLocalizedString("choice1","1.Choice")),6,1);
      T.add(headerText(iwrb.getLocalizedString("choice2","2.Choice")),7,1);
      T.add(headerText(iwrb.getLocalizedString("choice3","3.Choice")),8,1);
      T.add(getPDFLink(printImage,-1,-1),1,row);
      T.add(boldText(totalCount),3,row);
      T.add(boldText(totalFree),4,row);
      T.add(boldText(totalApplied),5,row);
      T.add(boldText(totApp1),6,row);
      T.add(boldText(totApp2),7,row);
      T.add(boldText(totApp3),8,row);
      row++;
      for (int i = 3; i <= 8; i++) {
         T.setColumnAlignment(i,"right");
      }
      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      T.setRowColor(row,redColor);
      T.mergeCells(1,1,2,1);
      T.mergeCells(1,row,8,row);
      T.setWidth(1,"15");
      T.add(formatText(" "),1,row);
      T.setColumnAlignment(1,"left");
      T.setHeight(row,bottomThickness);
      T.setWidth("100%");

    }
    return T;
  }
  private ModuleObject getWaitingLists(ApartmentCategory AC){
    Table T = new Table();

    List L = BuildingFinder.getApartmentTypesComplexForCategory(AC.getID());
    int row = 2;
    if(L!=null){
      int lLen = L.size();
      int listCount = 0;
      for (int i = 0; i < lLen; i++) {
        ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper)L.get(i);
        listCount = BuildingFinder.countApartmentsInTypeAndComplex(eAprtType.getKeyOne(),eAprtType.getKeyTwo());
        T.add(getListLink(eAprtType),2,row);
        T.add(formatText(listCount),3,row);
        row++;
      }
      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      T.setRowColor(row,redColor);
      T.mergeCells(1,1,2,1);
      T.mergeCells(1,row,2,row);
      T.setWidth(1,"15");
      T.add(headerText(AC.getName()),1,1);
      T.add(formatText(" "),1,row);
      T.setColumnAlignment(1,"left");
      T.setHeight(row,bottomThickness);
      T.setWidth("100%");
    }

    return T;
  }

  private Link getListLink(ApartmentTypeComplexHelper eAprtType){
    Link L = new Link(eAprtType.getName());
    L.setFontSize(this.fontSize);
    L.addParameter("type_id",eAprtType.getKeyOne());
    L.addParameter("cplx_id",eAprtType.getKeyTwo());
    return L;
  }

  private ModuleObject getWaitingList(int aprtTypeId,int cmplxId){
    Table Frame = new Table();
    List L = CampusApplicationFinder.listOfWaitinglist(aprtTypeId,cmplxId);
    Hashtable HT = ContractFinder.hashOfApplicantsContracts();
    boolean bcontracts = false;
    if(HT !=null)
      bcontracts = true;
    if(L!=null){
      int len = L.size();
      int row = 1;
      for (int i = 0; i < len; i++) {
        WaitingList WL = (WaitingList)L.get(i);
        try{
          Applicant A = new Applicant(WL.getApplicantId().intValue());
          Frame.add(formatText(WL.getOrder().intValue()),1,row);
           Frame.add(getPDFLink(new Image("/pics/print.gif"),A.getID()),2,row);
          Frame.add(formatText(A.getFullName()),3,row);


          if(bcontracts && HT.containsKey(new Integer(A.getID()))){
            Contract C = (Contract) HT.get(new Integer(A.getID()));
            //Frame.add(formatText(getApartmentString(C)),4,i+1);
            Frame.add(getChangeLink(C.getID()),2,row);
          }
          else{
            Frame.add(getAllocateLink(A.getID()),2,row);
          }

        }
        catch(SQLException sql){}
      }
      row++;
    }
    else
      Frame.add(formatText(iwrb.getLocalizedString("not_to_allocate","Nothing to Allocate!")));
    return Frame;
  }

  private Link getAllocateLink(int id){
    Link L = new Link(new Image("/pics/red.gif"));
    L.addParameter("allocate",String.valueOf(id));
    if(pTypeId!=null && pComplexId!=null){
      L.addParameter(pTypeId);
      L.addParameter(pComplexId);
    }
    return L;
  }

  private Link getChangeLink(int id){
    Link L = new Link(new Image("/pics/green.gif"));
    L.addParameter("change",String.valueOf(id));
    if(pTypeId!=null && pComplexId!=null){
      L.addParameter(pTypeId);
      L.addParameter(pComplexId);
    }
    return L;
  }

  private Link getHomeLink(){
    Link L =  new Link(new Image("/pics/list.gif"));
    L.addParameter("list","");
    return L;
  }

  public ModuleObject getApplicantInfo(int applicantId,ModuleInfo modinfo){
    CampusApprover CA = new CampusApprover("");
    ModuleObject MO = new Table();

      CampusApplicationHolder AH = CampusApplicationFinder.getApplicantInfo(applicantId);
      if(AH !=null){
        Table Frame = new Table(2,2);
        Frame.mergeCells(1,2,2,2);
        Frame.setVerticalAlignment(1,2,"top");
        Frame.add(CA.getApplicantTable(AH.getApplicant(),AH.getCampusApplication(),iwrb),1,1);
        String sSpouse = AH.getCampusApplication().getSpouseName();
        String sChildren = AH.getCampusApplication().getChildren();
        boolean bSpouse = false,bChildren = false;
        if(sSpouse !=null && sSpouse.length() > 0){
          Frame.add(CA.getSpouseTable(AH.getCampusApplication(),iwrb),2,1);
          bSpouse = true;
        }
        if(sChildren !=null && sChildren.length() > 0){
          Frame.add(CA.getChildrenTable(AH.getCampusApplication(),iwrb),2,1);
          bChildren = true;
        }
        if(bChildren && bSpouse){
          Frame.mergeCells(1,1,2,1);
        }

        Frame.add(CA.getApartmentTable(AH.getCampusApplication(),AH.getApplied(),modinfo,iwrb),1,2);

        MO =  Frame;
      }
      else
        add("er null");

    return MO;
  }

  private ModuleObject getFreeApartments(int aprtTypeId,int cmplxId,int applicant_id,int contract_id){
    List L = BuildingFinder.listOfApartmentsInTypeAndComplex(aprtTypeId,cmplxId);
    ApartmentType AT = BuildingCacher.getApartmentType(aprtTypeId);
    Complex CX = BuildingCacher.getComplex(cmplxId);
    Hashtable HT = ContractFinder.hashOfApartmentsContracts();
    ApartmentTypePeriods ATP = getPeriod(aprtTypeId);
    Contract C = null;
    boolean hasContract = false;
    if(contract_id != -1){
      try {
        C = new Contract(contract_id);
        hasContract = true;
      }
      catch (SQLException ex) {
        hasContract = false;
      }
    }

    boolean bcontracts = false;
    if(HT !=null)
      bcontracts = true;
    Table Frame = new Table();
    Table Header = new Table();
    Header.add(headerText(AT.getName()+" "),1,1);
    Header.add(headerText(CX.getName()),1,1);
    Header.mergeCells(1,1,4,1);
    Header.setRowColor(1,blueColor);
    Header.setWidth("100%");
    Frame.add(Header,1,1);

    if(L!=null){
      Form myForm = new Form();
      Table T = new Table();
      int len = L.size();
      Apartment A ;
      Floor F;
      Building B;
      int row = 1;
      SubmitButton save = new SubmitButton("save_allocation","Save");

      setStyle(save);
      DateInput dateFrom = new DateInput("contract_date_from",true);
      DateInput dateTo = new DateInput("contract_date_to",true);

      idegaTimestamp contractDateFrom = new idegaTimestamp();
      idegaTimestamp contractDateTo = new idegaTimestamp();
      if(hasContract){
        contractDateTo = new idegaTimestamp(C.getValidTo());
          contractDateFrom = new idegaTimestamp(C.getValidFrom());
      }
      else if(ATP!=null){
        boolean first = ATP.hasFirstPeriod();
        boolean second = ATP.hasSecondPeriod();
        // One Period
        if(first && second){
          idegaTimestamp today = new idegaTimestamp();
          if(today.getMonth() <= ATP.getFirstDateMonth()){
            contractDateFrom = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
            contractDateTo = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          }
          else if(today.getMonth() <= ATP.getSecondDateMonth() ){
            contractDateFrom = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
            contractDateTo = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
          }
          else{
            contractDateTo = new idegaTimestamp();
            contractDateFrom = new idegaTimestamp();
          }
        }
        // Two Periods
        else if(first && !second){
          contractDateTo = new idegaTimestamp();
          contractDateFrom = new idegaTimestamp();
        }
      }
      // are the System Properties set
      else if(SysProps !=null){
        contractDateTo = new idegaTimestamp(SysProps.getValidToDate());
        contractDateFrom = new idegaTimestamp();
      }
      else{
        contractDateTo = new idegaTimestamp();
        contractDateFrom = new idegaTimestamp();
      }
      dateFrom.setDate(contractDateFrom.getSQLDate());
      dateTo.setDate(contractDateTo.getSQLDate());
      dateFrom.setStyleAttribute("style",styleAttribute);
      dateTo.setStyleAttribute("style",styleAttribute);
      if(applicant_id != -1){
        HiddenInput Hid = new HiddenInput("applicantid",String.valueOf(applicant_id));
        myForm.add(Hid);
      }
      T.add(formatText(iwrb.getLocalizedString("validfrom","Valid from")),1,row);
      T.add(dateFrom,3,row);
      T.mergeCells(1,row,2,row);
      T.mergeCells(3,row,4,row);
      row++;
      T.add(formatText(iwrb.getLocalizedString("validto","Valid to")),1,row);
      T.add(dateTo,3,row);
      T.mergeCells(1,row,2,row);
      T.mergeCells(3,row,4,row);
      row++;
      if(hasContract){
        SubmitButton delete = new SubmitButton("delete_allocation","Delete");
        setStyle(delete);
        T.add(delete,1,row);
        T.add(new HiddenInput("contract_id",String.valueOf(C.getID())));
      }
      T.add(save,3,row);
      T.mergeCells(1,row,2,row);
      T.mergeCells(3,row,4,row);
      row++;
      for (int i = 0; i < len; i++) {
        A = (Apartment) L.get(i);
        int id = A.getID();
        RadioButton RB = new RadioButton("apartmentid",String.valueOf(id));
        boolean isThis = false;
        if(hasContract && C.getApartmentId().intValue() == id){
          RB.setSelected();
          isThis = true;
          TextFontColor = "#FF0000";
        }
        // If apartment is not in contract table:
        if(!(bcontracts && HT.containsKey(new Integer(A.getID()))) || isThis){
          if(A.getUnavailableUntil()!=null){
            idegaTimestamp it = new idegaTimestamp(A.getUnavailableUntil());
            if(! it.isLaterThan(idegaTimestamp.RightNow())){
              T.add(RB,1,row);
            }
          }
          else{
            T.add(RB,1,row);
          }

        T.add(formatText(A.getName()),2,row);
        F = BuildingCacher.getFloor(A.getFloorId());
        T.add(formatText(F.getName()),3,row);
        T.add(formatText((BuildingCacher.getBuilding(F.getBuildingId())).getName()),4,row);
        if(isThis)
          TextFontColor = "#000000";
        row++;
        }

      }
      T.setHorizontalZebraColored(lightBlue,WhiteColor);

      int lastrow = row;
      T.setRowColor(row,redColor);
      T.mergeCells(1,row,4,row);
      T.add(formatText(" "),1,row);
      T.setHeight(row,bottomThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
      myForm.add(T);
      Frame.add(myForm,1,2);
    }
    return Frame;
  }

  private ModuleObject getContractTable(int iContractId){
    Table T = new Table();

    Form myForm = new Form();
    Contract eContract = null;
    try {
      eContract = new Contract(iContractId);
      Apartment A = BuildingCacher.getApartment(eContract.getApartmentId().intValue());
      Floor F = BuildingCacher.getFloor(A.getFloorId());
      Building B = BuildingCacher.getBuilding(F.getBuildingId());
      Complex C = BuildingCacher.getComplex(B.getComplexId());

      T.add( "Apartment",1,2 );
      T.add(A.getName(),1,3);
      T.add(F.getName(),2,3);
      T.add(B.getName(),3,3);
      T.add(C.getName(),4,3);
      T.add(new SubmitButton("delete_allocation","Delete"),5,3);
      T.add(new HiddenInput("contract_id",String.valueOf(iContractId)));
    }
    catch (SQLException ex) {

    }
    T.setRowColor(1,blueColor);
    T.setRowColor(4,redColor);
    T.mergeCells(1,1,5,1);
    T.mergeCells(1,2,5,2);
    T.mergeCells(1,4,5,4);
    T.add(formatText(" "),1,1);
    T.setHeight(1,bottomThickness);
    T.add(formatText(" "),1,4);
    T.setHeight(4,bottomThickness);
    T.setCellpadding(1);
    T.setCellspacing(3);
    T.setBorder(0);
    myForm.add(T);
    return myForm;
  }

  private boolean saveAllocation(ModuleInfo modinfo){
    boolean returner = false;
    String sContractId = modinfo.getParameter("contract_id");
    String sApartmentId = modinfo.getParameter("apartmentid");
    String sApplicantId = modinfo.getParameter("applicantid");
    String sDateFrom = modinfo.getParameter("contract_date_from");
    String sDateTo = modinfo.getParameter("contract_date_to");
    if( sDateFrom!=null && sDateTo!=null){
      if(sApplicantId !=null && sApartmentId!=null ){
        int iApartmentId = Integer.parseInt(sApartmentId);
        int iApplicantId = Integer.parseInt(sApplicantId);
        idegaTimestamp from = new idegaTimestamp(sDateFrom);
        idegaTimestamp to = new idegaTimestamp(sDateTo);
        Applicant eApplicant = null;
        try{
          eApplicant = new Applicant(iApplicantId);
        }
        catch(SQLException ex){}

        List L = ContractFinder.listOfApplicantContracts(iApplicantId);
        if(L == null && eApplicant != null ){
          User eUser = makeNewUser(eApplicant);
          if(eUser!=null){
            returner = makeNewContract(eUser,eApplicant,iApartmentId,from,to);
          }
        }
      }
      else if(sContractId !=null){

        int iContractId = Integer.parseInt(sContractId);
        idegaTimestamp from = new idegaTimestamp(sDateFrom);
        idegaTimestamp to = new idegaTimestamp(sDateTo);
        Contract eContract = null;
        try{
          eContract = new Contract(iContractId);
          eContract.setValidFrom(from.getSQLDate());
          eContract.setValidTo(to.getSQLDate());
          if(sApartmentId!=null){
            int iApartmentId = Integer.parseInt(sApartmentId);
            eContract.setApartmentId(iApartmentId);
          }
          eContract.update();
          returner = true;
        }
        catch(SQLException ex){
          ex.printStackTrace();
        }
      }
    }

    return returner;
  }

  private boolean deleteAllocation(ModuleInfo modinfo){
    String sContractId = modinfo.getParameter("contract_id");
    int iContractId = Integer.parseInt(sContractId);
    try {
      Contract eContract = new Contract(iContractId);
      User eUser = new User(eContract.getUserId().intValue());
      eContract.delete();
      eUser.delete();

      return true;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false;
    }
  }

  private User makeNewUser(Applicant eApplicant){
    User U = new User();
    U.setDisplayName(eApplicant.getFullName());
    U.setFirstName(eApplicant.getFirstName());
    U.setMiddleName(eApplicant.getMiddleName());
    U.setLastName(eApplicant.getLastName());
    try{
      U.insert();
    }
    catch(SQLException ex){
      U = null;
    }
    return U;
  }

  private boolean makeNewContract(User eUser,Applicant eApplicant,int iApartmentId,idegaTimestamp from,idegaTimestamp to){

      Contract eContract = new Contract();
      eContract.setApartmentId(iApartmentId);
      eContract.setApplicantId(eApplicant.getID());
      eContract.setUserId(eUser.getID());
      eContract.setStatusCreated();
      eContract.setValidFrom(from.getSQLDate());
      eContract.setValidTo(to.getSQLDate());
      try{
        eContract.insert();
        return true;
      }
      catch(SQLException ex){
        return false;
      }
  }

  private java.sql.Date getValidToDate(SystemProperties SysProps){
    int years = SysProps.getContractYears();
    if(SysProps.getContractYears() > 0){
      idegaTimestamp now = idegaTimestamp.RightNow();
      idegaTimestamp iT = new idegaTimestamp(1,now.getMonth(),now.getYear()+years);
      return iT.getSQLDate();
    }
    else
     return SysProps.getContractDate();
  }

  public Text headerText(String text){
    Text T = new Text(text);
    T.setBold();
    T.setFontColor(this.WhiteColor);
    T.setFontSize(1);
    return T;
  }

  private Text boldText(String text){
    Text T = new Text(text);
    T.setBold();
    T.setFontColor(DarkColor);
    T.setFontSize(this.fontSize);
    return T;
  }
  private Text boldText(int i){
    return boldText(String.valueOf(i));
  }

   public Link getPDFLink(ModuleObject MO,int cam_app_id){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("cam_app_id",cam_app_id);
    return L;
  }
  public Link getPDFLink(ModuleObject MO,int aprt_type_id,int cmplx_id){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("aprt_type_id",aprt_type_id);
    L.addParameter("cmplx_id",cmplx_id);
    return L;
  }

  private ApartmentTypePeriods getPeriod(int aprt_type_id){
    try {
      ApartmentTypePeriods A = new ApartmentTypePeriods();
      List L = EntityFinder.findAllByColumn(A,A.getApartmentTypeIdColumnName(),aprt_type_id);
      if(L!=null)
        return (ApartmentTypePeriods) L.get(0);
      else
        return null;
    }
    catch (SQLException ex) {
      return null;
    }
  }

  private String getApartmentString(Contract eContract){
    Apartment A = BuildingCacher.getApartment(eContract.getApartmentId().intValue());
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    StringBuffer sb = new StringBuffer(A.getName());
    sb.append(" ");
    sb.append(F.getName());sb.append(" ");
    sb.append(B.getName());sb.append(" ");
    sb.append(C.getName());
    return sb.toString();
  }



}