package is.idega.idegaweb.campus.block.allocation.presentation;


import com.idega.presentation.ui.*;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.presentation.text.*;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationHolder;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.*;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.presentation.ApplicationFilerWindow;
import is.idega.idegaweb.campus.block.allocation.presentation.*;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.data.ApartmentContracts;
import is.idega.idegaweb.campus.block.allocation.data.*;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.presentation.CampusApprover;
import is.idega.idegaweb.campus.data.*;
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

public class CampusAllocator extends Block{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus.block.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private String redColor = "#942829";
  private String blueColor = "#27324B",lightBlue ="#ECEEF0";
  private int iSubjectId = -1;
  private int dayBuffer = 1;
  private int monthOverlap = 1;
  private String sGlobalStatus = "S";
  private ListIterator iterator = null;
  private LinkedList linkedlist = null;
  private int iTypeId,iComplexId;
  private Parameter pTypeId = null,pComplexId = null;
  private SystemProperties SysProps = null;
  private String bottomThickness = "8";
  protected boolean isAdmin = false;
  protected String MiddleColor,LightColor,DarkColor,WhiteColor,TextFontColor,HeaderFontColor,IndexFontColor;
  protected int fontSize = 2;
  protected boolean fontBold = false;
  protected String styleAttribute = "font-size: 8pt";

  public String getLocalizedNameKey(){
    return "allocator";
  }

  public String getLocalizedNameValue(){
    return "Allocator";
  }

  public CampusAllocator() {
    LightColor = "#D7DADF";
    MiddleColor = "#9fA9B3";
    DarkColor = "#27334B";
    WhiteColor = "#FFFFFF";
    TextFontColor = "#000000";
    HeaderFontColor = DarkColor;
    IndexFontColor = "#000000";
  }

  public void setDayBuffer(int buffer){
    dayBuffer = buffer;
  }

  public void setOverlap(int overlap  ){
    monthOverlap = overlap;
  }

  protected void control(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    this.fontSize = 1;
    if(iwc.getParameter("list")!=null){
      if(iwc.getSessionAttribute("sess_cplx_id")!=null)
        iwc.removeSessionAttribute("sess_cplx_id");
      if(iwc.getSessionAttribute("sess_type_id")!=null)
        iwc.removeSessionAttribute("sess_type_id");
    }
    if(iwc.getParameter("type_id")!=null){
      pTypeId = new Parameter("type_id",iwc.getParameter("type_id"));
      this.iTypeId = Integer.parseInt(iwc.getParameter("type_id"));
      iwc.setSessionAttribute("sess_type_id",new Integer(iTypeId));
    }
    else if(iwc.getSessionAttribute("sess_type_id")!=null){
      this.iTypeId = ((Integer)iwc.getSessionAttribute("sess_type_id")).intValue();
    }
    if(iwc.getParameter("cplx_id")!=null){
      pComplexId = new Parameter("cplx_id",iwc.getParameter("cplx_id"));
      this.iComplexId = Integer.parseInt(iwc.getParameter("cplx_id"));
      iwc.setSessionAttribute("sess_cplx_id",new Integer(iComplexId));
    }
    else if(iwc.getSessionAttribute("sess_cplx_id")!=null){
      this.iComplexId = ((Integer)iwc.getSessionAttribute("sess_cplx_id")).intValue();
    }

    if(iwc.getApplicationAttribute(SysProps.getEntityTableName())!=null){
      SysProps = (SystemProperties)iwc.getApplicationAttribute(SysProps.getEntityTableName());
    }

    Table Frame = new Table();
    Frame.add(getHomeLink(),1,1);
    int row = 2;
    if(isAdmin){
      if(iTypeId > 0 && iComplexId > 0){

        if(iwc.getParameter("allocate")!=null){
          int applicantId = Integer.parseInt(iwc.getParameter("allocate"));
          Frame.add( getApartmentsForm(iTypeId,iComplexId,applicantId,-1),3,row );
          Frame.add( getApplicantInfo(applicantId,iwc),1,row);
        }
        else if(iwc.getParameter("change")!=null){
          int iContractId = Integer.parseInt(iwc.getParameter("change"));
          int applicantId = Integer.parseInt(iwc.getParameter("applicant"));
          Frame.add( getApartmentsForm(iTypeId,iComplexId,applicantId,iContractId),3,row );
          //Frame.add( getContractTable(iContractId),3,1 );
          Frame.add( getWaitingList(iTypeId,iComplexId,iContractId),1,row );
        }
        else if(iwc.getParameter("view_aprtmnt")!=null){
          int iApartmentId = Integer.parseInt(iwc.getParameter("view_aprtmnt"));
          int iContractId = Integer.parseInt(iwc.getParameter("contract"));
          int iApplicantId = Integer.parseInt(iwc.getParameter("applicant"));
          idegaTimestamp from = new idegaTimestamp(iwc.getParameter("from"));
          Frame.add( getContractsForm(iApartmentId,iApplicantId,iContractId,from),3,row );
          //Frame.add( getContractTable(iContractId),3,1 );
          Frame.add( getApplicantInfo(iApplicantId,iwc),1,row );
        }
        else if(iwc.getParameter("save_allocation")!=null){
          if(saveAllocation(iwc))
            System.err.println("vistadist");
          else
             System.err.println("vistadist ekki");
          Frame.add( getWaitingList(iTypeId,iComplexId,-1),1,row );
        }
        else if(iwc.getParameter("delete_allocation")!=null){
          deleteAllocation(iwc);
          Frame.add( getWaitingList(iTypeId,iComplexId,-1),1,row );
        }
        else
          Frame.add( getWaitingList(iTypeId,iComplexId,-1),1,row );

      }
      else
        Frame.add(getCategoryLists(),1,row);

      Frame.setRowVerticalAlignment(2,"top");
      add(Frame);
  }
  else
    add(formatText(iwrb.getLocalizedString("access_denied","Access denied")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private PresentationObject getCategoryLists(){
    Table T = new Table();
    T.setWidth("100%");
    List Categories = BuildingFinder.listOfApartmentCategory();
    if(Categories != null){
      int row = 2;
      int cLen = Categories.size();
      int listCount = 0,contractCount = 0,appliedCount =0,appCnt1=0,appCnt2=0,appCnt3=0;

      int totalCount = 0,totalFree = 0,totalApplied = 0,totApp1 = 0,totApp2= 0,totApp3 = 0;
      int freeCount = 0;
      int type,cmpx;
      Image printImage = iwb.getImage("print.gif");
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
            contractCount = ContractFinder.countApartmentsInTypeAndComplex(type,cmpx,Contract.statusSigned);

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
  private PresentationObject getWaitingLists(ApartmentCategory AC){
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

  private PresentationObject getWaitingList(int aprtTypeId,int cmplxId,int ContractId){
    Table Frame = new Table();
    int row = 1;
    int col = 1;
    boolean ifLong = ContractId < 0? true:false;

    Frame.add(headerText(iwrb.getLocalizedString("nr","Nr")),col++,row);
    Frame.add(headerText(iwrb.getLocalizedString("a","A")),col++,row);
    Frame.add(headerText(iwrb.getLocalizedString("name","Name")),col++,row);
    Frame.add(headerText(iwrb.getLocalizedString("ssn","Socialnumber")),col++,row);
    Frame.add(headerText(iwrb.getLocalizedString("residence","Residence")),col++,row);
    if(ifLong)
      Frame.add(headerText(iwrb.getLocalizedString("legal_residence","Legal residence")),col++,row);
    Frame.add(headerText(iwrb.getLocalizedString("mobile_phone","Mobile phone")),col++,row);
    Frame.add(headerText(iwrb.getLocalizedString("phone","Phone")),col++,row);
    if(ifLong)
      Frame.add(headerText(iwrb.getLocalizedString("application","Application")),col++,row);

    List L = CampusApplicationFinder.listOfWaitinglist(aprtTypeId,cmplxId);
    Hashtable HT = ContractFinder.hashOfApplicantsContracts();
    boolean bcontracts = false;
    if(HT !=null)
      bcontracts = true;
    if(L!=null){
      int len = L.size();
      row = 2;
      String TempColor = "#000000";
      boolean redColorSet = false;
      for (int i = 0; i < len; i++) {
        col = 1;
        WaitingList WL = (WaitingList)L.get(i);
        try{

          Applicant A = new Applicant(WL.getApplicantId().intValue());

          Frame.add(formatText(WL.getOrder().intValue()),col++,row);

          if(bcontracts && HT.containsKey(new Integer(A.getID()))){
            Contract C = (Contract) HT.get(new Integer(A.getID()));
            if(C.getID() == ContractId){
              TempColor = TextFontColor;
              TextFontColor = "FF0000";
              redColorSet = true;
            }
            //Frame.add(formatText(getApartmentString(C)),4,i+1);
            Frame.add(getChangeLink(C.getID(),C.getApplicantId().intValue()),col++,row);
          }
          else{
            Frame.add(getAllocateLink(A.getID()),col++,row);
          }
          Frame.add(formatText(A.getFullName()),col++,row);
          Frame.add(formatText(A.getSSN()),col++,row);
          Frame.add(formatText(A.getResidence()),col++,row);
          if(ifLong)
          Frame.add(formatText(A.getLegalResidence()),col++,row);
          Frame.add(formatText(A.getMobilePhone()),col++,row);
          Frame.add(formatText(A.getResidencePhone()),col++,row);
          if(ifLong)
          Frame.add(getPDFLink(iwb.getImage("print.gif"),A.getID()),col,row);

          if(redColorSet)
            TextFontColor = TempColor;
        }
        catch(SQLException sql){}
         row++;
      }

    }
    else
      Frame.add(formatText(iwrb.getLocalizedString("not_to_allocate","Nothing to Allocate!")));
    Frame.setCellpadding(2);
    Frame.setCellspacing(1);
    Frame.setHorizontalZebraColored(lightBlue,WhiteColor);
    Frame.setRowColor(1,blueColor);
    Frame.setRowColor(row,redColor);

    Frame.mergeCells(1,row,col,row);
    Frame.setWidth(1,"15");
    Frame.add(formatText(" "),1,row);
    Frame.setHeight(row,bottomThickness);
    Frame.setWidth("100%");
    Frame.setColumnAlignment(2,"center");
    if(ifLong)
    Frame.setColumnAlignment(9,"center");
    return Frame;
  }

  private Link getAllocateLink(int id){
    Link L = new Link(iwb.getImage("red.gif"));
    L.addParameter("allocate",String.valueOf(id));
    if(pTypeId!=null && pComplexId!=null){
      L.addParameter(pTypeId);
      L.addParameter(pComplexId);
    }
    return L;
  }

  private Link getApartmentContractsLink(Text display,int applicant_id,int contract_id,int apartment_id,idegaTimestamp from){
    Link L = new Link(display);
    L.addParameter("view_aprtmnt",String.valueOf(apartment_id));
    L.addParameter("contract",String.valueOf(contract_id));
    L.addParameter("applicant",String.valueOf(applicant_id));
    L.addParameter("from",from.toString());
    if(pTypeId!=null && pComplexId!=null){
      L.addParameter(pTypeId);
      L.addParameter(pComplexId);
    }
    return L;
  }

  private Link getChangeLink(int Contractid,int iApplicantId){
    Link L = new Link(iwb.getImage("green.gif"));
    L.addParameter("change",String.valueOf(Contractid));
    L.addParameter("applicant",String.valueOf(iApplicantId));
    if(pTypeId!=null && pComplexId!=null){
      L.addParameter(pTypeId);
      L.addParameter(pComplexId);
    }
    return L;
  }

  private Link getHomeLink(){
    Link L =  new Link(iwb.getImage("list.gif"));
    L.addParameter("list","");
    return L;
  }

  public PresentationObject getApplicantInfo(int applicantId,IWContext iwc){
    CampusApprover CA = new CampusApprover();
    PresentationObject MO = new Table();

      CampusApplicationHolder AH = CampusApplicationFinder.getApplicantInfo(applicantId);
      if(AH !=null){
        Table Frame = new Table(2,2);
        Frame.mergeCells(1,2,2,2);
        Frame.setVerticalAlignment(1,2,"top");
        Frame.add(CA.getViewApplicant(AH.getApplicant(),AH.getCampusApplication(),iwrb),1,1);
        String sSpouse = AH.getCampusApplication().getSpouseName();
        String sChildren = AH.getCampusApplication().getChildren();
        boolean bSpouse = false,bChildren = false;
        if(sSpouse !=null && sSpouse.length() > 0){
          Frame.add(CA.getViewSpouse(AH.getCampusApplication(),iwrb),2,1);
          bSpouse = true;
        }
        if(sChildren !=null && sChildren.length() > 0){
          Frame.add(CA.getViewChildren(AH.getCampusApplication(),iwrb),2,1);
          bChildren = true;
        }
        if(bChildren && bSpouse){
          Frame.mergeCells(1,1,2,1);
        }

        Frame.add(CA.getViewApartment(AH.getCampusApplication(),AH.getApplied(),iwc,iwrb),1,2);

        MO =  Frame;
      }
      else
        add("er null");

    return MO;
  }

  private PresentationObject getApartmentsForm(int aprtTypeId,int cmplxId,int applicant_id,int contract_id){
    Form myForm = new Form();
    ApartmentType AT = BuildingCacher.getApartmentType(aprtTypeId);
    Complex CX = BuildingCacher.getComplex(cmplxId);
    Contract C = ContractFinder.getContract(contract_id);
    ApartmentTypePeriods ATP = getPeriod(AT.getID());

    Table Frame = new Table();
    Table Header = new Table();
    Header.add(headerText(AT.getName()+" "),1,1);
    Header.add(headerText(CX.getName()),1,1);
    Header.mergeCells(1,1,4,1);
    Header.setRowColor(1,blueColor);
    Header.setWidth("100%");
    Frame.add(Header,1,1);
    Frame.add(getContractMakingTable(C,ATP,applicant_id,null ));
    Frame.add(getFreeApartments(AT,CX,applicant_id ,C),1,3);
    myForm.add(Frame);
    return myForm;
  }

  private PresentationObject getContractsForm(int apartmentId,int applicant_id,int contract_id,idegaTimestamp from){
    Apartment A = BuildingCacher.getApartment(apartmentId );
    Contract C = ContractFinder.getContract(contract_id);
    ApartmentTypePeriods ATP = getPeriod(A.getApartmentTypeId());

    Form myForm = new Form();
    Table Frame = new Table();
    Table Header = new Table();
    StringBuffer sHeader = new StringBuffer(A.getName());
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    sHeader.append(" ");
    sHeader.append(F.getName());
    sHeader.append(" ");
    sHeader.append(BuildingCacher.getBuilding(F.getBuildingId()).getName());
    Header.add(headerText(sHeader.toString()),1,1);
    Header.setRowColor(1,blueColor);
    Header.setWidth("100%");
    Frame.add(Header,1,1);
    Frame.add(getContractMakingTable(C,ATP,applicant_id ,from));
    Frame.add(getApartmentContracts(A.getID()),1,3);
    myForm.add(Frame);
    return myForm;
  }

  private PresentationObject getApartmentContracts(int iApartmentId){
    Table T = new Table();
    List L = ContractFinder.listOfApartmentContracts(iApartmentId);
    java.util.Map M = ContractFinder.mapOfApartmentUsersBy(ContractFinder.listOfApartmentUsers(iApartmentId));
    if(L!= null){
      java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);
      java.util.Iterator I = L.iterator();
      Contract C ;
      User user;
      Integer UID;
      idegaTimestamp temp = null;
      int row = 1;
      while(I.hasNext()){
        C = (Contract) I.next();
        UID = C.getUserId();
        if(M!=null && M.containsKey(UID)){
          user = (User) M.get(UID);
          T.add(formatText(user.getName() ),1,row);
        }
        T.add(formatText(df.format((java.util.Date)C.getValidFrom())),2,row);
        T.add(formatText(df.format((java.util.Date)C.getValidTo())),3,row);
        T.add(formatText(getStatus(C.getStatus())),4,row);
        T.add(new HiddenInput("apartmentid",String.valueOf(iApartmentId )));
        row++;
      }
    }
    return T;
  }


  private PresentationObject getFreeApartments(ApartmentType AT,Complex CX,int applicant_id,Contract C){

    java.util.Map M =  ContractFinder.mapOfAvailableApartmentContracts(AT.getID() ,CX.getID() );
    //List L = ContractFinder.listOfAvailable(ContractFinder.APARTMENT,AT.getID() ,CX.getID() );
		//List l = ContractFinder.listOfNonContractApartments(AT.getID() ,CX.getID());
    List L = BuildingFinder.listOfApartmentsInTypeAndComplex(AT.getID() ,CX.getID());

    boolean hasContract = C!= null?true:false;
    int iContractId = hasContract ? C.getID():-1;
    boolean bcontracts = false;
    if(M!=null)
      bcontracts = true;
    Table T = new Table();
    if(L!=null){
      int len = L.size();
      Apartment A ;
      Floor F;
      Building B;

      int row = 1;
      RadioButton RB1,RB2;
      Integer apId;
      boolean Available = false;
      idegaTimestamp nextAvailable;
      for (int i = 0; i < len; i++) {
        A = (Apartment) L.get(i);
        apId = new Integer(A.getID());

        RB1 = new RadioButton("apartmentid",apId.toString());
        //RB2 = new RadioButton("next_apartmentid",apId.toString());
        boolean isThis = false;

        // Mark current apartment
        if(hasContract && C.getApartmentId().intValue() == apId.intValue()  ){
          //if(C.getStatus().equals(C.statusCreated ) ||C.getStatus().equals(C.statusPrinted  ) )
            RB1.setSelected();

          isThis = true;
          TextFontColor = "#FF0000";
        }

        // List all apartments and show next availability ( date )
        nextAvailable = null;
        if( M != null && M.containsKey(apId) ){
          ApartmentContracts AC = (ApartmentContracts) M.get(apId);
          //System.err.println(" contractcount "+AC.getContracts().size());
          nextAvailable = AC.getNextDate();
          Available = false;
        }
        else{
          Available = true;
        }

        // If apartment is not in contract table:
        if(Available || isThis){
          if(A.getUnavailableUntil()!=null){
            idegaTimestamp it = new idegaTimestamp(A.getUnavailableUntil());
            if(! it.isLaterThan(idegaTimestamp.RightNow())){
              T.add(RB1,1,row);
            }
          }
          else{
            T.add(RB1,1,row);
          }
        }


        T.add(formatText(A.getName()),2,row);
        F = BuildingCacher.getFloor(A.getFloorId());
        T.add(formatText(F.getName()),3,row);
        T.add(formatText((BuildingCacher.getBuilding(F.getBuildingId())).getName()),4,row);
        if(nextAvailable != null){
          Text text = formatText(nextAvailable.getISLDate());
          T.add(getApartmentContractsLink(text,applicant_id ,iContractId,A.getID(),nextAvailable),5,row);
        }

        if(isThis)
          TextFontColor = "#000000";
        row++;
      }

      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      int lastrow = row;
      T.setRowColor(row,redColor);
      T.mergeCells(1,row,4,row);
      T.add(formatText(" "),1,row);
      T.setHeight(row,bottomThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
    }
    return T;
  }

  private idegaTimestamp stampFromPeriod(ApartmentTypePeriods ATP,boolean From){
     idegaTimestamp contractDateFrom = idegaTimestamp.RightNow();
     idegaTimestamp contractDateTo = idegaTimestamp.RightNow();
     if(ATP!=null){
        // Period checking
        //System.err.println("ATP exists");
        boolean first = ATP.hasFirstPeriod();
        boolean second = ATP.hasSecondPeriod();
         idegaTimestamp today = new idegaTimestamp();

        // Two Periods
        if(first && second){

          if(today.getMonth() > ATP.getFirstDateMonth()+monthOverlap && today.getMonth() <= ATP.getSecondDateMonth()+monthOverlap ){
            contractDateFrom = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
            contractDateTo = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
          }
          else if(today.getMonth() <= 12){
            contractDateFrom = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
            contractDateTo = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear()+1);
          }
          else{
            contractDateFrom = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
            contractDateTo = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          }

        }
        // One Periods
        else if(first && !second){
          //System.err.println("two sectors");
          contractDateFrom = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
          contractDateTo = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
        }
        else if(!first && second){
          //System.err.println("two sectors");
          contractDateFrom = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          contractDateTo = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear()+1);
        }
     }
      if(From)
        return contractDateFrom;
      else
        return contractDateTo;
  }

  private PresentationObject getContractMakingTable(Contract C,ApartmentTypePeriods ATP,int applicant_id,idegaTimestamp from){
    Table T = new Table();
      int row = 1;
      SubmitButton save = new SubmitButton("save_allocation","Save");

      setStyle(save);
      DateInput dateFrom = new DateInput("contract_date_from",true);
      DateInput dateTo = new DateInput("contract_date_to",true);

      idegaTimestamp contractDateFrom = new idegaTimestamp();
      idegaTimestamp contractDateTo = new idegaTimestamp();

      if(C!=null){
        contractDateTo = new idegaTimestamp(C.getValidTo());
        contractDateFrom = new idegaTimestamp(C.getValidFrom());
      }
      else if(ATP!=null){
        // Period checking
        //System.err.println("ATP exists");
        contractDateTo = stampFromPeriod(ATP,false);
        contractDateFrom = stampFromPeriod(ATP,true);

        if(dayBuffer > 0){
        contractDateFrom.addDays(dayBuffer);
        }
        // end of Period checks
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
      if(from != null)
        contractDateFrom = from;

      dateFrom.setDate(contractDateFrom.getSQLDate());
      dateTo.setDate(contractDateTo.getSQLDate());
      dateFrom.setStyleAttribute("style",styleAttribute);
      dateTo.setStyleAttribute("style",styleAttribute);
      if(applicant_id != -1){
        HiddenInput Hid = new HiddenInput("applicantid",String.valueOf(applicant_id));
        T.add(Hid);
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
      if(C!=null){
        SubmitButton delete = new SubmitButton("delete_allocation","Delete");
        setStyle(delete);
        T.add(delete,1,row);
        T.add(new HiddenInput("contract_id",String.valueOf(C.getID())));
      }
      T.add(save,3,row);
      T.mergeCells(1,row,2,row);
      T.mergeCells(3,row,4,row);
    return T;
  }


  private PresentationObject getContractTable(int iContractId){
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

  private boolean saveAllocation(IWContext iwc){
    boolean returner = false;
    String sContractId = iwc.getParameter("contract_id");
    String sApartmentId = iwc.getParameter("apartmentid");
    String sApplicantId = iwc.getParameter("applicantid");
    String sDateFrom = iwc.getParameter("contract_date_from");
    String sDateTo = iwc.getParameter("contract_date_to");
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

  private boolean deleteAllocation(IWContext iwc){
    String sContractId = iwc.getParameter("contract_id");
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

  private User makeNewUser(Applicant A){
    UserBusiness ub = new UserBusiness();
		try{
		return ub.insertUser(A.getFirstName(),A.getMiddleName(),A.getLastName(),A.getFirstName(),"",null,null,null);
		}
		catch(SQLException ex){
		  ex.printStackTrace();
		}
		return null;
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
    T.setFontColor(TextFontColor);
    T.setFontSize(this.fontSize);
    return T;
  }
  private Text boldText(int i){
    return boldText(String.valueOf(i));
  }

   public Link getPDFLink(PresentationObject MO,int cam_app_id){
    Link L = new Link(MO);
		L.setWindowToOpen(ApplicationFilerWindow.class);
    L.addParameter("cam_app_id",cam_app_id);
    return L;
  }
  public Link getPDFLink(PresentationObject MO,int aprt_type_id,int cmplx_id){
    Link L = new Link(MO);
		L.setWindowToOpen(ApplicationFilerWindow.class);
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

public Text formatText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      if(this.fontBold)
      T.setBold();
      T.setFontColor(this.TextFontColor);
      T.setFontSize(this.fontSize);
    }
    return T;
  }
  public Text formatText(int i){
    return formatText(String.valueOf(i));
  }
  protected void setStyle(InterfaceObject O){
    O.setAttribute("style",this.styleAttribute);
  }
  public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

}
