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
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.Application;
import com.idega.block.application.business.ApplicationHolder;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idegaweb.campus.application.*;
import is.idegaweb.campus.allocation.*;
import is.idegaweb.campus.entity.*;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.sql.SQLException;


public class CampusApprover extends KeyEditor{

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
  private String bottomThickness = "8";

  /*
  Blár litur í topp # 27324B
  Hvítur litur fyrir neðan það # FFFFFF
  Ljósblár litur í töflu # ECEEF0
  Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public CampusApprover(String sHeader) {
    super(sHeader);
    this.fontSize = 1;
  }

  protected void control(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    if(modinfo.getSessionAttribute("iterator")!=null){
      iterator = (ListIterator)modinfo.getSessionAttribute("iterator");
    }
    if(modinfo.getParameter("app_subject_id")!=null){
      this.iSubjectId = Integer.parseInt(modinfo.getParameter("app_subject_id"));
      modinfo.setSessionAttribute("subject_id",new Integer(iSubjectId));
    }
    else if(modinfo.getSessionAttribute("subject_id")!=null){
      this.iSubjectId = ((Integer)modinfo.getSessionAttribute("subject_id")).intValue();
    }
    if(modinfo.getParameter("global_status")!=null){
      this.sGlobalStatus= (modinfo.getParameter("global_status"));
      modinfo.setSessionAttribute("gl_status",sGlobalStatus);
    }
    else if(modinfo.getSessionAttribute("gl_status")!=null){
      this.sGlobalStatus = ((String)modinfo.getSessionAttribute("gl_status"));
    }

    if(isAdmin){
      if(modinfo.getParameter("view")!=null){
        int id = Integer.parseInt(modinfo.getParameter("view"));
        add(makeApplicationTable(id,modinfo,iwrb));
      }
      else if(modinfo.getParameter("application_id")!=null){
        this.updateApplication(modinfo,iwrb);
      }
      else{
        add(subjectForm());
        add(makeApplicantTable(modinfo,iwrb));
      }
    }
    else
      add(formatText("Ekki Réttindi"));
    //add(String.valueOf(iSubjectId));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private void updateApplication(ModuleInfo modinfo,IWResourceBundle iwrb){
    int id = Integer.parseInt(modinfo.getParameter("application_id"));
    String status = modinfo.getParameter("status_drop");
    try{
      Application A = new Application(id);
      A.setStatus(status);
      A.update();
      add(makeApplicationTable(id,modinfo,iwrb));
    }
    catch(Exception e){
      e.printStackTrace();

    }
  }

  public ModuleObject makeApplicantTable(ModuleInfo modinfo,IWResourceBundle iwrb){
    Table T = new Table();
      T.setCellpadding(2);
      T.setCellspacing(1);
    List L = ApplicationFinder.listOfNewApplicationHoldersInSubject(this.iSubjectId,this.sGlobalStatus);

    if(L != null){
      ListIterator iterator = L.listIterator();
      modinfo.setSessionAttribute("iterator",iterator);
      int len = L.size();
      int row = 1;
      int col = 1;

      T.add(headerText(iwrb.getLocalizedString("nr","Nr")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("name","Name")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("ssn","Socialnumber")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("legal_residence","Legal Residence")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("residence","Residence")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("po","PO")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("phone","Residence phone")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("v","V")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("p","P")),col++,row);
      int lastcol = 1;
      for (int i = 0; i < len; i++) {
        row = i+2;
        col = 1;
        ApplicationHolder AH = (ApplicationHolder) L.get(i);
        Applicant A = AH.getApplicant();
        Application a = AH.getApplication();
        T.add(formatText(String.valueOf(i+1)),col++,row);
        String Name = A.getFirstName()+" "+A.getMiddleName()+" "+A.getLastName();
        T.add(formatText(Name),col++,row);
        T.add(formatText(A.getSSN()),col++,row);
        T.add(formatText(A.getLegalResidence()),col++,row);
        T.add(formatText(A.getResidence()),col++,row);
        T.add(formatText(A.getPO()),col++,row);
        T.add(formatText(A.getResidencePhone()),col++,row);
        T.add((getPDFLink(new Text("P"),A.getID())),col++,row);
        T.add( getApplicationLink(a.getID()),col,row);
        if(lastcol < col)
          lastcol = col;
      }

      T.setHorizontalZebraColored(this.lightBlue,this.WhiteColor);
      T.setRowColor(1,this.blueColor);
      int lastrow = len+2;
      T.mergeCells(1,lastrow,lastcol,lastrow);
      T.setRowColor(lastrow,this.redColor);
      T.add(formatText(" "),1,lastrow);
      T.setHeight(lastrow,bottomThickness);
      T.add(getPDFLink(new Text("Print"),sGlobalStatus,iSubjectId),1,++row);
    }
    else{
      T.add(formatText("Engar umsóknir í gagnagrunni"));
    }
    return T;
  }

  public ModuleObject makeApplicationTable(int id,ModuleInfo modinfo,IWResourceBundle iwrb){
     Table OuterFrame = new Table(2,1);
        OuterFrame.setCellpadding(0);
        OuterFrame.setCellspacing(0);

    try{
      Application  eApplication = null;
      Applicant eApplicant = null;
      if(id < -1 && iterator != null){
        ApplicationHolder AS = null;
        if( id == -2 && iterator.hasPrevious()){
          AS = (ApplicationHolder)iterator.previous();
        }
        else if(id == -4 && iterator.hasNext()){
          AS = (ApplicationHolder)iterator.next();
        }
        if(AS !=null){
          eApplication = AS.getApplication();
          eApplicant = AS.getApplicant();
          id = eApplication.getID();
        }
      }
      else{
        eApplication = new Application(id);
        eApplicant = new Applicant(eApplication.getApplicantId());
      }

      if( eApplication !=null && eApplicant != null){
        CampusApplication A = new CampusApplication();
        CampusApplication eCampusApplication = ((CampusApplication[])(A.findAllByColumn(A.getApplicationIdColumnName(),id)))[0];
        List L = CampusApplicationFinder.listOfAppliedInApplication(eCampusApplication.getID());

        int border = 0;
        OuterFrame.setBorder(border);
        OuterFrame.setRowVerticalAlignment(1,"top");
        OuterFrame.setWidth(1,"550");

        Table InnerFrame = new Table(2,2);
          InnerFrame.mergeCells(1,1,1,2);
          InnerFrame.setCellpadding(0);
          InnerFrame.setCellspacing(0);
          InnerFrame.setBorder(border);
          InnerFrame.setWidth("100%");
          InnerFrame.setRowVerticalAlignment(1,"top");
          InnerFrame.setVerticalAlignment(2,2,"top");
          InnerFrame.setWidth(1,"50%");
          InnerFrame.setWidth(2,"50%");
          InnerFrame.add(this.getApplicantTable(eApplicant,eCampusApplication,iwrb),1,1);
          InnerFrame.add(this.getSpouseTable(eCampusApplication,iwrb),2,1);
          InnerFrame.add(this.getChildrenTable(eCampusApplication,iwrb),2,2);

        Table Frame = new Table(1,6);
          Frame.setCellpadding(2);
          Frame.setCellspacing(1);
          Frame.setWidth("100%");
          Frame.setBorder(border);
          Frame.add(getApplicationTable(eApplication),1,1);
          Frame.add(InnerFrame,1,3);
          Frame.add(this.getApartmentTable(eCampusApplication,L,modinfo,iwrb),1,5);

        Table OtherFrame = new Table(1,2);
          OtherFrame.setCellpadding(2);
          OtherFrame.setCellspacing(1);
          OtherFrame.setBorder(border);
          OtherFrame.setWidth("200");
          OtherFrame.setRowVerticalAlignment(1,"top");
          OtherFrame.add(new HiddenInput("application_id",String.valueOf(id)));
          OtherFrame.add(this.getRemoteControl(eApplication.getStatus(),iwrb),1,1);
          OtherFrame.add(this.getKnobs(iwrb),1,2);

        Form theForm = new Form();
        theForm.add(OtherFrame);
        OuterFrame.add(Frame,1,1);
        OuterFrame.add(theForm,2,1);


      }
    }
    catch(SQLException sql){sql.printStackTrace();}
    catch(Exception e){e.printStackTrace();}
    return OuterFrame;
  }

  public ModuleObject getApplicantTable(Applicant eApplicant,CampusApplication eCampusApplication,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("applicant","Applicant")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("name","Name")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("ssn","Socialnumber")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("legal_residence","Legal Residence")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("residence","Residence")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("po","PO")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("phone","Residence phone")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("email","Email")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("faculty","Faculty")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("studytrack","Study Track")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("study_begins","Study begins")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("study_ends","Study ends")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("income","Income")),col,row++);

      col = 2;
      row = 2;
      T.add(formatText(eApplicant.getFullName()),col,row++);
      T.add(formatText(eApplicant.getSSN()),col,row++);
      T.add(formatText(eApplicant.getLegalResidence()),col,row++);
      T.add(formatText(eApplicant.getResidence()),col,row++);
      T.add(formatText(eApplicant.getPO()),col,row++);
      T.add(formatText(eApplicant.getResidencePhone()),col,row++);
      idegaCalendar iC = new idegaCalendar();
      T.add(formatText(eCampusApplication.getEmail()),col,row++);
      T.add(formatText(eCampusApplication.getFaculty()),col,row++);
      T.add(formatText(eCampusApplication.getStudyTrack()),col,row++);
      String beginMonth = (eCampusApplication.getStudyBeginMonth().toString());
      String endMonth = (eCampusApplication.getStudyEndMonth().toString());
      T.add(formatText(beginMonth+" "+eCampusApplication.getStudyBeginYear().intValue()),col,row++);
      T.add(formatText(endMonth+" "+eCampusApplication.getStudyEndYear().intValue()),col,row++);
      T.add(formatText(eCampusApplication.getIncome().intValue()),col,row);

      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,2,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      int lastrow = row+1;
      T.setRowColor(lastrow,redColor);
      T.mergeCells(1,lastrow,2,lastrow);
      T.add(formatText(" "),1,lastrow);
      T.setHeight(lastrow,bottomThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
      return T;
  }

  public ModuleObject getSpouseTable(CampusApplication eCampusApplication,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("spouse","Spouse")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("name","Name")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("ssn","Socialnumber")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("school","School")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("studytrack","Study Track")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("study_begins","Study begins")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("study_ends","Study ends")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("income","Income")),col,row++);
      col = 2;
      row = 2;

      T.add(formatText(eCampusApplication.getSpouseName()),col,row++);
      T.add(formatText(eCampusApplication.getSpouseSSN()),col,row++);
      T.add(formatText(eCampusApplication.getSpouseSchool()),col,row++);
      T.add(formatText(eCampusApplication.getSpouseStudyTrack()),col,row++);
      String beginMonth = (eCampusApplication.getSpouseStudyBeginMonth().toString());
      String endMonth = (eCampusApplication.getSpouseStudyEndMonth().toString());
      T.add(formatText(beginMonth+" "+eCampusApplication.getStudyBeginYear().intValue()),col,row++);
      T.add(formatText(endMonth+" "+eCampusApplication.getStudyEndYear().intValue()),col,row++);
      T.add(formatText(eCampusApplication.getSpouseIncome().intValue()),col,row);

      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,2,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      int lastrow = row+1;
      T.setRowColor(lastrow,redColor);
      T.mergeCells(1,lastrow,2,lastrow);
      T.add(formatText(" "),1,lastrow);
      T.setHeight(lastrow,bottomThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
      return T;
  }

  public ModuleObject getChildrenTable(CampusApplication eCampusApplication,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("children","Children")),col,row++);
      StringTokenizer st = new StringTokenizer(eCampusApplication.getChildren(),"\n");
      while(st.hasMoreTokens()){
         T.add(formatText(st.nextToken()),col,row++);
      }
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      int lastrow = row;
      T.setRowColor(lastrow,redColor);
      T.mergeCells(1,lastrow,2,lastrow);
      T.setVerticalAlignment("top");
      T.add(formatText(" "),1,lastrow);
      T.setHeight(lastrow,bottomThickness);
      T.setWidth("100%");
      return T;
  }
  public ModuleObject getApartmentTable(CampusApplication eCampusApplication,List lApplied,ModuleInfo modinfo,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("applied","Applied")),col,row++);
      if(lApplied != null){
        int len = lApplied.size();
        for (int i = 0; i < len; i++) {
          Applied A = (Applied) lApplied.get(i);
          T.add(boldText(i+1),1,row);
          T.add(formatText((BuildingCacher.getApartmentType(A.getApartmentTypeId().intValue()).getName())),2,row++);
        }
      }
      col = 3;
      row = 1;
       T.add(headerText(iwrb.getLocalizedString("requests","Requests")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("housingfrom","Housing from")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("wantfurniture","Wants furniture")),col,row++);
      T.add(boldText(iwrb.getLocalizedString("onwaitinglist","On waitinglist")),col,row++);
      col = 4;
      row = 2;
      idegaTimestamp iT = new idegaTimestamp(eCampusApplication.getHousingFrom());
      T.add(formatText(iT.getLocaleDate(modinfo)),col,row++);
      if(eCampusApplication.getWantFurniture())
        T.add(formatText("X"),col,row++);
      if(eCampusApplication.getOnWaitinglist())
        T.add(formatText("X"),col,row++);
      T.mergeCells(1,1,2,1);
      T.mergeCells(3,1,4,1);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      int lastrow = 5;
      T.setRowColor(lastrow,redColor);
      T.mergeCells(1,lastrow,4,lastrow);
      T.add(formatText(" "),1,lastrow);
      T.setHeight(lastrow,bottomThickness);
      T.setWidth("100%");
      return T;
  }

  public ModuleObject getApplicationTable(Application eApplication){
    Table T = new Table();
      T.add(headerText(iwrb.getLocalizedString("application","Application")),1,1);
      T.add(boldText(iwrb.getLocalizedString("submitted","Submitted")),1,2);
      T.add(formatText(eApplication.getSubmitted().toString()),2,2);
      T.add(boldText(iwrb.getLocalizedString("changed","Status change")),1,3);
      T.add(formatText(eApplication.getStatusChanged().toString()),2,3);
      T.add(boldText(iwrb.getLocalizedString("status","Status")),3,2);
      T.add(formatText(getStatus(eApplication.getStatus())),3,3);


      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,3,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      int lastrow = 4;
      T.setRowColor(lastrow,redColor);
      T.mergeCells(1,lastrow,3,lastrow);
      T.add(formatText(" "),1,lastrow);
      T.setHeight(lastrow,bottomThickness);
      T.setWidth("100%");
    return T;
  }

  private ModuleObject getRemoteControl(String sStatus,IWResourceBundle iwrb){
      Table T = new Table();
      T.add(headerText(iwrb.getLocalizedString("control","Control")),1,1);
      T.add(boldText(iwrb.getLocalizedString("tax_return","Tax return")),1,2);
      T.add(boldText(iwrb.getLocalizedString("study_progress","Study progress")),1,3);
      T.add(boldText(iwrb.getLocalizedString("choice1","Choice 1")),1,4);
      T.add(boldText(iwrb.getLocalizedString("choice2","Choice 2")),1,5);
      TextInput units = new TextInput("unit");
       units.setLength(1);
       setStyle(units);
      CheckBox choice1 = new CheckBox("choice1");
       setStyle(choice1);
      CheckBox choice2 = new CheckBox("choice2");
       setStyle(choice2);
      CheckBox choice3 = new CheckBox("choice3");
       setStyle(choice3);
      T.add(choice1,2,2);
      T.add(units,2,3);
      T.add(choice2,2,4);
      T.add(choice3,2,5);
      DropdownMenu status = statusDrop("status_drop",sStatus);
      status.setToSubmit();
      setStyle(status);
      T.add(status,2,6);
      T.mergeCells(1,1,2,1);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
       T.setHorizontalZebraColored(lightBlue,WhiteColor);
      T.setRowColor(1,blueColor);
      int lastrow = 7;
      T.setRowColor(lastrow,redColor);
      T.mergeCells(1,lastrow,4,lastrow);
      T.add(formatText(" "),1,lastrow);
      T.setHeight(lastrow,bottomThickness);
      T.setHeight("100%");
      T.setWidth("100%");
    return T;
  }

  private ModuleObject getKnobs(IWResourceBundle iwrb){
    Table T = new Table(5,1);
    T.setAlignment("center");

    if(iterator != null){
      if(iterator.hasPrevious()){
        Link lLast = new Link(new Image("/pics/last.gif"));
        lLast.addParameter("view","-2");
        T.add(lLast,1,1);
      }
      if(iterator.hasNext()){
        Link lNext = new Link(new Image("/pics/next.gif"));
        lNext.addParameter("view","-4");
        T.add(lNext,5,1);
      }
    }
    Link lList = new Link(new Image("/pics/list.gif"));
    T.add(lList,3,1);
    T.setCellpadding(1);
    T.setCellspacing(1);
    T.setBorder(0);
    return T;
  }

  public void doUpdate(ModuleInfo modinfo){
    String sDesc= modinfo.getParameter("app_subj_desc").trim();
    String sDate = modinfo.getParameter("app_subj_xdate");
    if(sDesc.length() > 0){
      ApplicationSubject AS = new ApplicationSubject();
      AS.setDescription(sDesc);
      AS.setExpires(new idegaTimestamp(sDate).getSQLDate());
      try {
        AS.insert();
      }
      catch (SQLException ex) {

      }
    }
  }

  private Form subjectForm(){
    Form myForm = new Form();
    DropdownMenu drp = subjectDrop(String.valueOf(this.iSubjectId));
    DropdownMenu status = statusDrop("global_status",sGlobalStatus);
    drp.setToSubmit();
    status.setToSubmit();
    setStyle(status);
    myForm.add(drp);
    myForm.add(status);
    return myForm;
  }

  private DropdownMenu subjectDrop(String selected){
    List L = ApplicationFinder.listOfSubject();
    DropdownMenu drp = new DropdownMenu("app_subject_id");
    drp.addMenuElement(-1,iwrb.getLocalizedString("subject","Subject"));
    if(L!=null){
      ApplicationSubject AS;
      int len = L.size();
      for (int i = 0; i < len; i++) {
        AS = (ApplicationSubject) L.get(i);
        drp.addMenuElement(AS.getID(),AS.getName());
      }
      setStyle(drp);
      drp.setSelectedElement(selected);
    }
    return drp;
  }

  private String getStatus(String status){
    String r = "";
    char c = status.charAt(0);
    switch (c) {
      case 'S': r = iwrb.getLocalizedString("submitted","Submitted"); break;
      case 'A': r = iwrb.getLocalizedString("approved","Approved");   break;
      case 'R': r = iwrb.getLocalizedString("rejected","Rejected");  break;
    }
    return r;
  }

  private DropdownMenu statusDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("S",getStatus("S"));
    drp.addMenuElement("A",getStatus("A"));
    drp.addMenuElement("R",getStatus("R"));
    drp.setSelectedElement(selected);
    return drp;
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

  public Link getApplicationLink(int id){
    Link L = new Link("X");
    L.setFontSize(1);
    L.addParameter("view",id);
    return L;
  }

  public Text headerText(String text){
    Text T = new Text(text);
    T.setBold();
    T.setFontColor(this.WhiteColor);
    T.setFontSize(1);
    return T;
  }

  public Link getPDFLink(ModuleObject MO,int cam_app_id){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("cam_app_id",cam_app_id);
    return L;
  }
  public Link getPDFLink(ModuleObject MO,String status,int subject_id){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("app_status",status);
    L.addParameter("app_sub_id",subject_id);
    return L;
  }

}

