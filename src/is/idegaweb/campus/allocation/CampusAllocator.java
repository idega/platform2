package is.idegaweb.campus.allocation;

import com.idega.presentation.ui.*;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.presentation.text.*;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
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
import com.idega.presentation.PresentationObjectContainer;
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

public class CampusAllocator extends PresentationObjectContainer{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
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

  public CampusAllocator(String sHeader) {
    this();
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
    Frame.add(getHomeLink());
    if(isAdmin){
      if(iTypeId > 0 && iComplexId > 0){

        if(iwc.getParameter("allocate")!=null){
          int applicantId = Integer.parseInt(iwc.getParameter("allocate"));
          Frame.add( getFreeApartments(iTypeId,iComplexId,applicantId,-1),3,1 );
          Frame.add( getApplicantInfo(applicantId,iwc),1,1 );
        }
        else if(iwc.getParameter("change")!=null){
          int iContractId = Integer.parseInt(iwc.getParameter("change"));
          Frame.add( getFreeApartments(iTypeId,iComplexId,-1,iContractId),3,1 );
          //Frame.add( getContractTable(iContractId),3,1 );
          Frame.add( getWaitingList(iTypeId,iComplexId,iContractId),1,1 );
        }
        else if(iwc.getParameter("save_allocation")!=null){
          if(saveAllocation(iwc))
            System.err.println("vistadist");
          else
             System.err.println("vistadist ekki");
          Frame.add( getWaitingList(iTypeId,iComplexId,-1),1,1 );
        }
        else if(iwc.getParameter("delete_allocation")!=null){
          deleteAllocation(iwc);
          Frame.add( getWaitingList(iTypeId,iComplexId,-1),1,1 );
        }
        else
          Frame.add( getWaitingList(iTypeId,iComplexId,-1),1,1 );

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

  private PresentationObject getCategoryLists(){
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
            Frame.add(getChangeLink(C.getID()),col++,row);
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
          Frame.add(getPDFLink(new Image("/pics/print.gif"),A.getID()),col,row);

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

  private PresentationObject getFreeApartments(int aprtTypeId,int cmplxId,int applicant_id,int contract_id){
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
        // Period checking
        System.err.println("ATP exists");
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
          System.err.println("two sectors");
          contractDateFrom = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
          contractDateTo = new idegaTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
        }
        else if(!first && second){
          System.err.println("two sectors");
          contractDateFrom = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          contractDateTo = new idegaTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear()+1);
        }
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

  private User makeNewUser(Applicant eApplicant){
    User U = new User();
    //U.setDisplayName(eApplicant.getFullName());
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
    T.setFontColor(TextFontColor);
    T.setFontSize(this.fontSize);
    return T;
  }
  private Text boldText(int i){
    return boldText(String.valueOf(i));
  }

   public Link getPDFLink(PresentationObject MO,int cam_app_id){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("cam_app_id",cam_app_id);
    return L;
  }
  public Link getPDFLink(PresentationObject MO,int aprt_type_id,int cmplx_id){
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
    try{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.getAccessControler().isAdmin(iwc);
    }
    catch(SQLException sql){ isAdmin = false;}
    control(iwc);
  }

}