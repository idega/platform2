package is.idegaweb.campus.allocation;

import is.idegaweb.campus.presentation.Edit;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.IWContext;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
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
import java.util.Vector;


public class CampusApprover extends PresentationObjectContainer{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private int iSubjectId = -1;
  private String sGlobalStatus = "S",sGlobalOrder = Applicant.getFullnameOrderValue();
  private ListIterator iterator = null;
  private LinkedList linkedlist = null;
  private final String sView = "view",sEdit = "edit";
  protected boolean isAdmin = false;

  /*
  Blár litur í topp # 27324B
  Hvítur litur fyrir neðan það # FFFFFF
  Ljósblár litur í töflu # ECEEF0
  Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public CampusApprover() {

  }

  protected void control(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);

    if(iwc.getSessionAttribute("iterator")!=null){
      iterator = (ListIterator)iwc.getSessionAttribute("iterator");
    }
    if(iwc.getParameter("app_subject_id")!=null){
      this.iSubjectId = Integer.parseInt(iwc.getParameter("app_subject_id"));
      iwc.setSessionAttribute("subject_id",new Integer(iSubjectId));
    }
    else if(iwc.getSessionAttribute("subject_id")!=null){
      this.iSubjectId = ((Integer)iwc.getSessionAttribute("subject_id")).intValue();
    }
    if(iwc.getParameter("global_status")!=null){
      this.sGlobalStatus= (iwc.getParameter("global_status"));
      iwc.setSessionAttribute("gl_status",sGlobalStatus);
    }
    else if(iwc.getSessionAttribute("gl_status")!=null){
      this.sGlobalStatus = ((String)iwc.getSessionAttribute("gl_status"));
    }
    if(iwc.getParameter("global_order")!=null){
      this.sGlobalOrder= (iwc.getParameter("global_order"));
      iwc.setSessionAttribute("gl_order",sGlobalOrder);
    }
    else if(iwc.getSessionAttribute("gl_order")!=null){
      this.sGlobalOrder = ((String)iwc.getSessionAttribute("gl_order"));
    }

    if(isAdmin){
      if(iwc.getParameter("view")!=null){
        int id = Integer.parseInt(iwc.getParameter("view"));
        add(makeApplicationTable(id,false,iwc,iwrb));
      }
      else if(iwc.getParameter("application_id")!=null){
        int id = Integer.parseInt(iwc.getParameter("application_id"));
        boolean bEdit = false;
        if(iwc.getParameter("editor")!=null){
          bEdit = true;
        }
        else if(iwc.getParameter("viewer")!=null){
          bEdit = false;
        }

        if(iwc.getParameter("save")!= null){
          updateWholeApplication(iwc,id);
        }
        else{
          updateApplication(iwc,id);
        }

        if(bEdit){
          add(makeApplicationForm(id,bEdit,iwc,iwrb));
        }
        else{
          add(makeApplicationTable(id,bEdit,iwc,iwrb));
        }
      }
      else{
        add(subjectForm());
        add(makeApplicantTable(iwc,iwrb));
      }
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

  private void updateApplication(IWContext iwc,int id){
    //int id = Integer.parseInt(iwc.getParameter("application_id"));
    String status = iwc.getParameter("status_drop");
    try{
      Application A = new Application(id);
      A.setStatus(status);
      A.update();
    }
    catch(Exception e){
      e.printStackTrace();

    }
  }

  private void updateWholeApplication(IWContext iwc,int id){
    try {
      Application eApplication = new Application(id);
      Applicant eApplicant = new Applicant(eApplication.getApplicantId());
      if( eApplication !=null && eApplicant != null){
        CampusApplication A = new CampusApplication();
        CampusApplication eCampusApplication = ((CampusApplication[])(A.findAllByColumn(A.getApplicationIdColumnName(),id)))[0];
        List L = CampusApplicationFinder.listOfAppliedInApplication(eCampusApplication.getID());
        updateApplicant(iwc,eApplicant,eCampusApplication);
        updateApartment(iwc,eCampusApplication,L);
        updateSpouse(iwc,eCampusApplication);
        updateChildren(iwc,eCampusApplication);
        try {
          eApplicant.update();
          eCampusApplication.update();
          for (int i = 0; i < L.size(); i++) {
            Applied applied = (Applied) L.get(i);
            int aid = applied.getID();
            if(aid == -1)
              applied.insert();
            else if(aid < -1)
              applied.delete();
            else if(aid > 0)
              applied.update();
          }

        }
        catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    }
    catch (SQLException ex) {

    }


  }

  public PresentationObject makeApplicantTable(IWContext iwc,IWResourceBundle iwrb){

    Table T = new Table();
      T.setCellpadding(2);
      T.setCellspacing(1);
    List L = ApplicationFinder.listOfApplicationHoldersInSubject(this.iSubjectId,this.sGlobalStatus,this.sGlobalOrder);

    if(L != null){
      ListIterator iterator = L.listIterator();
      iwc.setSessionAttribute("iterator",iterator);
      int len = L.size();
      int row = 1;
      int col = 1;
       System.out.println("lengd:"+len);
      Image printImage = new Image("/pics/print.gif");
      Image viewImage = new Image("/pics/view.gif");
      T.add(headerText(iwrb.getLocalizedString("nr","Nr")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("name","Name")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("ssn","Socialnumber")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("legal_residence","Legal Residence")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("residence","Residence")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("po","PO")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("phone","Residence phone")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("mobile_phone","Mobile phone")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("v","V")),col++,row);
      T.add(headerText(iwrb.getLocalizedString("p","P")),col++,row);
      int lastcol = 1;
      for (int i = 0; i < len; i++) {
        row = i+2;
        col = 1;
        ApplicationHolder AH = (ApplicationHolder) L.get(i);
        Applicant A = AH.getApplicant();
        Application a = AH.getApplication();
        T.add(Edit.formatText(String.valueOf(i+1)),col++,row);
        String Name = A.getFirstName()+" "+A.getMiddleName()+" "+A.getLastName();
        T.add(Edit.formatText(Name),col++,row);
        T.add(Edit.formatText(A.getSSN()!=null?A.getSSN():""),col++,row);
        T.add(Edit.formatText(A.getLegalResidence()!=null?A.getLegalResidence():""),col++,row);
        T.add(Edit.formatText(A.getResidence()!=null?A.getResidence():""),col++,row);
        T.add(Edit.formatText(A.getPO()!=null?A.getPO():""),col++,row);
        T.add(Edit.formatText(A.getResidencePhone()!=null?A.getResidencePhone():""),col++,row);
        T.add(Edit.formatText(A.getMobilePhone()!=null?A.getMobilePhone():""),col++,row);
        T.add((getPDFLink(printImage,A.getID())),col++,row);
        T.add( getApplicationLink(viewImage,a.getID()),col,row);
        if(lastcol < col)
          lastcol = col;
      }

      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = len+2;
      T.mergeCells(1,lastrow,lastcol,lastrow);
      T.setRowColor(lastrow,Edit.colorRed);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.add(getPDFLink(printImage,sGlobalStatus,iSubjectId),1,++row);

    }
    else{
      T.add(Edit.formatText(iwrb.getLocalizedString("no_applications","No applications in database")));
    }
    return T;
  }

  public PresentationObject makeApplicationTable(int id,boolean bEdit,IWContext iwc,IWResourceBundle iwrb){
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
          InnerFrame.add(getViewApplicant(eApplicant,eCampusApplication,iwrb),1,1);
          InnerFrame.add(getViewSpouse(eCampusApplication,iwrb),2,1);
          InnerFrame.add(getViewChildren(eCampusApplication,iwrb),2,2);

        Table Frame = new Table(1,6);
          Frame.setCellpadding(2);
          Frame.setCellspacing(1);
          Frame.setWidth("100%");
          Frame.setBorder(border);
          Frame.add(getViewApplication(eApplication),1,1);
          Frame.add(InnerFrame,1,3);
          Frame.add(getViewApartment(eCampusApplication,L,iwc,iwrb),1,5);

        Table OtherFrame = new Table(1,2);
          OtherFrame.setCellpadding(2);
          OtherFrame.setCellspacing(1);
          OtherFrame.setBorder(border);
          OtherFrame.setWidth("200");
          OtherFrame.setRowVerticalAlignment(1,"top");
          OtherFrame.add(new HiddenInput("application_id",String.valueOf(id)));
          OtherFrame.add(getRemoteControl(eApplication.getStatus(),bEdit,iwrb),1,1);
          OtherFrame.add(getKnobs(iwrb),1,2);

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


  public PresentationObject makeApplicationForm(int id,boolean bEdit,IWContext iwc,IWResourceBundle iwrb){
     Form theForm = new Form();

     Table OuterFrame = new Table(2,1);
        OuterFrame.setCellpadding(0);
        OuterFrame.setCellspacing(0);
    theForm.add(OuterFrame);

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
          InnerFrame.add(this.getFieldsApplicant(eApplicant,eCampusApplication,iwrb),1,1);
          InnerFrame.add(this.getFieldsSpouse(eCampusApplication,iwrb),2,1);
          InnerFrame.add(this.getFieldsChildren(eCampusApplication,iwrb),2,2);

        Table Frame = new Table(1,6);
          Frame.setCellpadding(2);
          Frame.setCellspacing(1);
          Frame.setWidth("100%");
          Frame.setBorder(border);
          Frame.add( getViewApplication(eApplication) ,1,1);
          Frame.add(InnerFrame,1,3);
          Frame.add( getFieldsApartment(eCampusApplication,L,iwc,iwrb) ,1,5);

        Table OtherFrame = new Table(1,2);
          OtherFrame.setCellpadding(2);
          OtherFrame.setCellspacing(1);
          OtherFrame.setBorder(border);
          OtherFrame.setWidth("200");
          OtherFrame.setRowVerticalAlignment(1,"top");
          OtherFrame.add(new HiddenInput("application_id",String.valueOf(id)));
          OtherFrame.add(this.getRemoteControl(eApplication.getStatus(),bEdit,iwrb),1,1);
          OtherFrame.add(this.getKnobs(iwrb),1,2);


        OuterFrame.add(Frame,1,1);
        OuterFrame.add(OtherFrame,2,1);


      }
    }
    catch(SQLException sql){sql.printStackTrace();}
    catch(Exception e){e.printStackTrace();}
    return theForm;
  }

  public PresentationObject getViewApplicant(Applicant eApplicant,CampusApplication eCampusApplication,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("applicant","Applicant")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("ssn","Socialnumber")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("legal_residence","Legal Residence")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("residence","Residence")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("po","PO")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("phone","Residence phone")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("mobile_phone","Mobile phone")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("email","Email")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("faculty","Faculty")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("studytrack","Study Track")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("study_begins","Study begins")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("study_ends","Study ends")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("income","Income")),col,row++);

      col = 2;
      row = 2;
      T.add(Edit.formatText(eApplicant.getFullName()),col,row++);
      T.add(Edit.formatText(eApplicant.getSSN()),col,row++);
      T.add(Edit.formatText(eApplicant.getLegalResidence()),col,row++);
      T.add(Edit.formatText(eApplicant.getResidence()),col,row++);
      T.add(Edit.formatText(eApplicant.getPO()),col,row++);
      T.add(Edit.formatText(eApplicant.getResidencePhone()),col,row++);
      T.add(Edit.formatText(eApplicant.getMobilePhone()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getEmail()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getFaculty()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getStudyTrack()),col,row++);
      String beginMonth = (eCampusApplication.getStudyBeginMonth().toString());
      String endMonth = (eCampusApplication.getStudyEndMonth().toString());
      T.add(Edit.formatText(beginMonth+" "+eCampusApplication.getStudyBeginYear().intValue()),col,row++);
      T.add(Edit.formatText(endMonth+" "+eCampusApplication.getStudyEndYear().intValue()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getIncome().intValue()),col,row);

      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,2,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = row+1;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,2,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
      return T;
  }

  public PresentationObject getFieldsApplicant(Applicant eApplicant,CampusApplication eCampusApplication,IWResourceBundle iwrb){
    int year = idegaTimestamp.RightNow().getYear();
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("applicant","Applicant")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("ssn","Socialnumber")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("legal_residence","Legal Residence")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("residence","Residence")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("po","PO")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("phone","Residence phone")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("mobile_phone","Mobile phone")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("email","Email")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("faculty","Faculty")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("studytrack","Study Track")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("study_begins","Study begins")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("study_ends","Study ends")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("income","Income")),col,row++);

      col = 2;
      row = 2;

      TextInput tiFullName = new TextInput("ti_full",eApplicant.getFullName()!=null?eApplicant.getFullName():"");
      Edit.setStyle(tiFullName);
      TextInput tiSsn = new TextInput("ti_ssn",eApplicant.getSSN()!=null?eApplicant.getSSN():"");
      Edit.setStyle(tiSsn);
      TextInput tiLegRes = new TextInput("ti_legres",eApplicant.getLegalResidence()!=null?eApplicant.getLegalResidence():"");
      Edit.setStyle(tiLegRes);
      TextInput tiRes = new TextInput("ti_res",eApplicant.getResidence()!=null?eApplicant.getResidence():"");
      Edit.setStyle(tiRes);
      TextInput tiPo = new TextInput("ti_po",eApplicant.getPO()!=null?eApplicant.getPO():"");
      Edit.setStyle(tiPo);
      TextInput tiResPho = new TextInput("ti_respho",eApplicant.getResidencePhone()!=null?eApplicant.getResidencePhone():"");
      Edit.setStyle(tiResPho);
      TextInput tiMobPho = new TextInput("ti_mobpho",eApplicant.getMobilePhone()!=null?eApplicant.getMobilePhone():"");
      Edit.setStyle(tiMobPho);
      TextInput tiEmail = new TextInput("ti_email",eCampusApplication.getEmail()!=null?eCampusApplication.getEmail():"");
      Edit.setStyle(tiEmail);
      TextInput tiFac = new TextInput("ti_facult",eCampusApplication.getFaculty()!=null?eCampusApplication.getFaculty():"");
      Edit.setStyle(tiFac);
      TextInput tiTrack= new TextInput("ti_track",eCampusApplication.getStudyTrack()!=null?eCampusApplication.getStudyTrack():"");
      Edit.setStyle(tiTrack);
      TextInput tiIncome= new TextInput("ti_income",eCampusApplication.getIncome().toString());
      Edit.setStyle(tiIncome);
      tiIncome.setAsIntegers();

      T.add(tiFullName,col,row++);
      T.add(tiSsn,col,row++);
      T.add(tiLegRes,col,row++);
      T.add(tiRes,col,row++);
      T.add(tiPo,col,row++);
      T.add(tiResPho,col,row++);
      T.add(tiMobPho,col,row++);
      T.add(tiEmail,col,row++);
      T.add(tiFac,col,row++);
      T.add(tiTrack,col,row++);
      String beginMonth = (eCampusApplication.getStudyBeginMonth().toString());
      String endMonth = (eCampusApplication.getStudyEndMonth().toString());
      String beginYear = eCampusApplication.getStudyBeginYear().toString();
      String endYear = eCampusApplication.getStudyEndYear().toString();
      DropdownMenu drBM = intDrop("dr_bm",beginMonth,1,12);
      DropdownMenu drEM = intDrop("dr_em",endMonth,1,12);
      DropdownMenu drBY = intDrop("dr_by",beginYear,year-10,year+10);
      DropdownMenu drEY = intDrop("dr_ey",endYear,year-10,year+10);
      Edit.setStyle(drBM);
      Edit.setStyle(drEM);
      Edit.setStyle(drBY);
      Edit.setStyle(drEY);
      T.add(drBM,col,row);
      T.add(drBY,col,row++);
      T.add(drEM,col,row);
      T.add(drEY,col,row++);
      T.add(tiIncome,col,row);

      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,2,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = row+1;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,2,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
      return T;
  }

  public void updateApplicant(IWContext iwc,Applicant eApplicant,CampusApplication eCampusApplication){
    String sFullName =iwc.getParameter("ti_full");
    String sSsn = iwc.getParameter("ti_ssn");
    String sLegRes = iwc.getParameter("ti_legres");
    String sRes = iwc.getParameter("ti_res");
    String sPo = iwc.getParameter("ti_po");
    String sResPho = iwc.getParameter("ti_respho");
    String sMobPho = iwc.getParameter("ti_mobpho");
    System.err.print(sMobPho);
    String sEmail = iwc.getParameter("ti_email");
    String sFac = iwc.getParameter("ti_facult");
    String sTrack= iwc.getParameter("ti_track");
    String sIncome= iwc.getParameter("ti_income");
    String sBM = iwc.getParameter("dr_bm");
    String sEM = iwc.getParameter("dr_em");
    String sBY = iwc.getParameter("dr_by");
    String sEY = iwc.getParameter("dr_ey");

    try{
      int iIncome = 0;
      if(sIncome != null)
       iIncome = Integer.parseInt(sIncome);
      int iBM = sBM!=null ?Integer.parseInt(sBM):0;
      int iEM = sEM!=null ?Integer.parseInt(sEM):0;
      int iBY = sBY!=null?Integer.parseInt(sBY):0;
      int iEY = sEY!=null? Integer.parseInt(sEY):0;
      eCampusApplication.setIncome(iIncome);
      eCampusApplication.setStudyBeginMonth(iBM);
      eCampusApplication.setStudyBeginYear(iBY);
      eCampusApplication.setStudyEndMonth(iEM);
      eCampusApplication.setStudyEndYear(iEY);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    eCampusApplication.setEmail(sEmail);
    eCampusApplication.setFaculty(sFac);
    eCampusApplication.setStudyTrack(sTrack);
    eApplicant.setLegalResidence(sLegRes);
    eApplicant.setSSN(sSsn);
    eApplicant.setPO(sPo);
    eApplicant.setResidencePhone(sResPho);
    eApplicant.setMobilePhone(sMobPho);
    eApplicant.setResidence(sRes);
    if(sFullName!= null){
      StringTokenizer st = new StringTokenizer(sFullName);
      if(st.hasMoreTokens()){
        eApplicant.setFirstName(st.nextToken());
      }
      String mid = "";
      if(st.hasMoreTokens()){
        mid = (st.nextToken());
      }

      if(st.hasMoreTokens()){
        eApplicant.setLastName(st.nextToken());
        eApplicant.setMiddleName(mid);
      }
      else{
        eApplicant.setLastName(mid);
      }
    }
  }

  public PresentationObject getViewSpouse(CampusApplication eCampusApplication,IWResourceBundle iwrb){
    int year = idegaTimestamp.RightNow().getYear();
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("spouse","Spouse")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("ssn","Socialnumber")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("school","School")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("studytrack","Study Track")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("study_begins","Study begins")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("study_ends","Study ends")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("income","Income")),col,row++);
      col = 2;
      row = 2;

      T.add(Edit.formatText(eCampusApplication.getSpouseName()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getSpouseSSN()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getSpouseSchool()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getSpouseStudyTrack()),col,row++);
      String beginMonth = (eCampusApplication.getSpouseStudyBeginMonth().toString());
      String endMonth = (eCampusApplication.getSpouseStudyEndMonth().toString());
      T.add(Edit.formatText(beginMonth+" "+eCampusApplication.getStudyBeginYear().intValue()),col,row++);
      T.add(Edit.formatText(endMonth+" "+eCampusApplication.getStudyEndYear().intValue()),col,row++);
      T.add(Edit.formatText(eCampusApplication.getSpouseIncome().intValue()),col,row);

      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,2,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = row+1;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,2,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
      return T;
  }

  public PresentationObject getFieldsSpouse(CampusApplication eCampusApplication,IWResourceBundle iwrb){
    int year = idegaTimestamp.RightNow().getYear();
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("spouse","Spouse")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("name","Name")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("ssn","Socialnumber")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("school","School")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("studytrack","Study Track")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("study_begins","Study begins")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("study_ends","Study ends")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("income","Income")),col,row++);
      col = 2;
      row = 2;

      TextInput tiSpName = new TextInput("ti_sp_name",eCampusApplication.getSpouseName());
      TextInput tiSpSsn = new TextInput("ti_sp_ssn",eCampusApplication.getSpouseSSN());
      TextInput tiSpSchl = new TextInput("ti_sp_schl",eCampusApplication.getSpouseSchool());
      TextInput tiSpStTr = new TextInput("ti_sp_sttr",eCampusApplication.getSpouseStudyTrack());
      TextInput tiSPIncome = new TextInput("ti_sp_income",eCampusApplication.getSpouseIncome().toString());
      Edit.setStyle(tiSpName);
      Edit.setStyle(tiSpSsn);
      Edit.setStyle(tiSpSchl);
      Edit.setStyle(tiSpStTr);
      Edit.setStyle(tiSPIncome);

      T.add(tiSpName,col,row++);
      T.add(tiSpSsn,col,row++);
      T.add(tiSpSchl,col,row++);
      T.add(tiSpStTr,col,row++);

      String beginMonth = eCampusApplication.getSpouseStudyBeginMonth().toString();
      String endMonth = eCampusApplication.getSpouseStudyEndMonth().toString();
      String beginYear = eCampusApplication.getSpouseStudyBeginYear().toString();
      String endYear = eCampusApplication.getSpouseStudyEndYear().toString();
      DropdownMenu drBM = intDrop("dr_sp_bm",beginMonth,1,12);
      DropdownMenu drEM = intDrop("dr_sp_em",endMonth,1,12);
      DropdownMenu drBY = intDrop("dr_sp_by",beginYear,year-10,year+10);
      DropdownMenu drEY = intDrop("dr_sp_ey",endYear,year-10,year+10);
      Edit.setStyle(drBM);
      Edit.setStyle(drEM);
      Edit.setStyle(drBY);
      Edit.setStyle(drEY);
      T.add(drBM,col,row);
      T.add(drBY,col,row++);
      T.add(drEM,col,row);
      T.add(drEY,col,row++);
      T.add(tiSPIncome,col,row);

      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,2,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = row+1;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,2,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth(1,"50");
      T.setWidth("100%");
      return T;
  }

  public void updateSpouse(IWContext iwc,CampusApplication eCampusApplication){
    String sSpName = iwc.getParameter("ti_sp_name");
    String sSpSsn = iwc.getParameter("ti_sp_ssn");
    String sSpSchl = iwc.getParameter("ti_sp_schl");
    String sSpStTr = iwc.getParameter("ti_sp_sttr");
    String sSPIncome = iwc.getParameter("ti_sp_income");
    String sBM = iwc.getParameter("dr_sp_bm");
    String sEM = iwc.getParameter("dr_sp_em");
    String sBY = iwc.getParameter("dr_sp_by");
    String sEY = iwc.getParameter("dr_sp_ey");

    try{
      int iIncome = Integer.parseInt(sSPIncome);
      int iBM = Integer.parseInt(sBM);
      int iEM = Integer.parseInt(sEM);
      int iBY = Integer.parseInt(sBY);
      int iEY = Integer.parseInt(sEY);
      eCampusApplication.setSpouseIncome(iIncome);
      eCampusApplication.setSpouseStudyBeginMonth(iBM);
      eCampusApplication.setSpouseStudyBeginYear(iBY);
      eCampusApplication.setSpouseStudyEndMonth(iEM);
      eCampusApplication.setSpouseStudyEndYear(iEY);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    /*
    eCampusApplication.setChildren();
    eCampusApplication.setContactPhone();
    eCampusApplication.setCurrentResidenceId();
    eCampusApplication.setEmail();
    eCampusApplication.setFaculty();
    eCampusApplication.setHousingFrom();
    eCampusApplication.setIncome();
    eCampusApplication.setOnWaitinglist();
    eCampusApplication.setOtherInfo();
    */

    eCampusApplication.setSpouseName(sSpName);
    eCampusApplication.setSpouseStudyTrack(sSpStTr);
    eCampusApplication.setSpouseSchool(sSpSchl);
    eCampusApplication.setSpouseSSN(sSpSsn);
  }

  public PresentationObject getViewChildren(CampusApplication eCampusApplication,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("children","Children")),col,row++);
      StringTokenizer st = new StringTokenizer(eCampusApplication.getChildren(),"\n");
      while(st.hasMoreTokens()){
         T.add(Edit.formatText(st.nextToken()),col,row++);
      }
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = row;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,2,lastrow);
      T.setVerticalAlignment("top");
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth("100%");
      return T;
  }

  public PresentationObject getFieldsChildren(CampusApplication eCampusApplication,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("children","Children")),col,row++);
      TextArea taChilds = new TextArea("ti_sp_childs",eCampusApplication.getChildren());
      taChilds.setWidth(30);
      taChilds.setHeight(4);
      Edit.setStyle(taChilds);
      T.add(taChilds,col,row++);

      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = row;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,2,lastrow);
      T.setVerticalAlignment("top");
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth("100%");
      return T;
  }

  public void updateChildren(IWContext iwc,CampusApplication eCampusApplication){
    String sChilds = iwc.getParameter("ti_sp_childs");
    if(sChilds != null){
      eCampusApplication.setChildren(sChilds);
    }
  }

  public PresentationObject getViewApartment(CampusApplication eCampusApplication,List lApplied,IWContext iwc,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;
      T.add(headerText(iwrb.getLocalizedString("applied","Applied")),col,row++);
      if(lApplied != null){
        int len = lApplied.size();
        for (int i = 0; i < len; i++) {
          Applied A = (Applied) lApplied.get(i);
          T.add(Edit.formatText(i+1),1,row);
          T.add(Edit.formatText((BuildingCacher.getApartmentType(A.getApartmentTypeId().intValue()).getName())),2,row++);
        }
      }
      col = 3;
      row = 1;
       T.add(headerText(iwrb.getLocalizedString("requests","Requests")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("housingfrom","Housing from")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("wantfurniture","Wants furniture")),col,row++);
      T.add(Edit.formatText(iwrb.getLocalizedString("onwaitinglist","On waitinglist")),col,row++);
      col = 4;
      row = 2;
      idegaTimestamp iT = new idegaTimestamp(eCampusApplication.getHousingFrom());
      T.add(Edit.formatText(iT.getLocaleDate(iwc)),col,row++);
      if(eCampusApplication.getWantFurniture())
        T.add(Edit.formatText("X"),col,row++);
      if(eCampusApplication.getOnWaitinglist())
        T.add(Edit.formatText("X"),col,row++);
      T.mergeCells(1,1,2,1);
      T.mergeCells(3,1,4,1);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = 5;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,4,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth("100%");
      return T;
  }

  public DropdownMenu drpTypes(Vector v,String name,String selected,boolean firstEmpty){
    DropdownMenu drpTypes = new DropdownMenu(name);
    Edit.setStyle(drpTypes);
    if(firstEmpty)
      drpTypes.addMenuElementFirst("-1","-");
    for (int i = 0; i < v.size(); i++) {
      ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper)v.elementAt(i);
      drpTypes.addMenuElement(eAprtType.getKey(),eAprtType.getName());
    }
    drpTypes.setSelectedElement(selected);
    return drpTypes;
  }

  public PresentationObject getFieldsApartment(CampusApplication eCampusApplication,List lApplied,IWContext iwc,IWResourceBundle iwrb){
    Table T = new Table();
      int col = 1;
      int row = 1;

      T.add(headerText(iwrb.getLocalizedString("applied","Applied")),col,row++);

      String sOne = "-1",sTwo = "-1",sThree = "-3";
      if(lApplied != null){
        int len = lApplied.size();
        Applied A;
        ApartmentTypeComplexHelper ATCH;
        if(len >= 1){
          A = (Applied) lApplied.get(0);
          ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(),A.getComplexId().intValue());
          sOne = ATCH.getKey();
        }
        if(len >= 2){
          A = (Applied) lApplied.get(1);
          ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(),A.getComplexId().intValue());
          sTwo = ATCH.getKey();
        }
        if(len >= 3){
          A = (Applied) lApplied.get(2);
          ATCH = new ApartmentTypeComplexHelper(A.getApartmentTypeId().intValue(),A.getComplexId().intValue());
          sThree = ATCH.getKey();
        }
      }

      java.util.Vector vAprtType = BuildingFinder.getAllApartmentTypesComplex();
      DropdownMenu drpOne = drpTypes(vAprtType,"drp_one",sOne,false);
      DropdownMenu drpTwo = drpTypes(vAprtType,"drp_two",sTwo,true);
      DropdownMenu drpThree = drpTypes(vAprtType,"drp_three",sThree,true);
      Edit.setStyle(drpOne);
      Edit.setStyle(drpTwo);
      Edit.setStyle(drpThree);

      T.add(Edit.titleText(1),1,row);
      T.add(drpOne,2,row++);
      T.add(Edit.titleText(2),1,row);
      T.add(drpTwo,2,row++);
      T.add(Edit.titleText(3),1,row);
      T.add(drpThree,2,row++);

      col = 3;
      row = 1;
       T.add(headerText(iwrb.getLocalizedString("requests","Requests")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("housingfrom","Housing from")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("wantfurniture","Wants furniture")),col,row++);
      T.add(Edit.titleText(iwrb.getLocalizedString("onwaitinglist","On waitinglist")),col,row++);
      col = 4;
      row = 2;
      idegaTimestamp iT = new idegaTimestamp(eCampusApplication.getHousingFrom());

      DateInput diRentFrom = new DateInput("ap_rentfrom",true);
      diRentFrom.setDate(iT.getSQLDate());
      diRentFrom.setStyleAttribute("style",Edit.styleAttribute);
      T.add(diRentFrom,col,row++);
      CheckBox chkFurni = new CheckBox("ap_furni","true");
      Edit.setStyle(chkFurni);
      CheckBox chkWait = new CheckBox("ap_wait","true");
      Edit.setStyle(chkWait);
      if(eCampusApplication.getWantFurniture())
        chkFurni.setChecked(true);
      T.add(chkFurni,col,row++);
      if(eCampusApplication.getOnWaitinglist())
        chkWait.setChecked(true);
      T.add(chkWait,col,row++);
      T.mergeCells(1,1,2,1);
      T.mergeCells(3,1,4,1);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = 5;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,4,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth("100%");
      return T;
  }

  public void updateApartment(IWContext iwc,CampusApplication eCampusApplication,List lApplied){
    String sRentFrom = iwc.getParameter("ap_rentfrom");
    String sFurni = iwc.getParameter("ap_furni");
    String sWait = iwc.getParameter("ap_wait");
    System.err.println("RentFrom "+sRentFrom);
    if(sRentFrom!= null)
      eCampusApplication.setHousingFrom(new idegaTimestamp(sRentFrom).getSQLDate());
    if("true".equals(sFurni)){
      eCampusApplication.setWantFurniture(true);
    }
    else
      eCampusApplication.setWantFurniture(false);
    if("true".equals(sWait)){
      eCampusApplication.setOnWaitinglist(true);
    }
    else
      eCampusApplication.setOnWaitinglist(false);

    String key1 = (String)iwc.getParameter("drp_one");
    String key2 = (String)iwc.getParameter("drp_two");
    String key3 = (String)iwc.getParameter("drp_three");
    if(key1!=null && key2!=null && key3!=null){
      Applied applied1 = null;
      Applied applied2 = null;
      Applied applied3 = null;
      if(lApplied!=null){
        System.err.println("lapplied er nul");
        applied1 = (Applied) lApplied.get(0);
      }
      else{
        applied1 = new Applied();
      }
      int type = ApartmentTypeComplexHelper.getPartKey(key1,1);
      int complex = ApartmentTypeComplexHelper.getPartKey(key1,2);
      applied1.setApartmentTypeId(type);
      applied1.setComplexId(complex);
      applied1.setOrder(1);

      if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
        if(lApplied.size() >= 2){
          applied2 = (Applied) lApplied.get(1);
        }
        else{
          applied2 = new Applied();
        }
        type = ApartmentTypeComplexHelper.getPartKey(key2,1);
        complex = ApartmentTypeComplexHelper.getPartKey(key2,2);
        applied2.setApartmentTypeId(type);
        applied2.setComplexId(complex);
        applied2.setOrder(2);
      }

      if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
        if(lApplied.size() >= 3){
          applied3 = (Applied) lApplied.get(2);
        }
        else{
          applied3 = new Applied();
        }
        type = ApartmentTypeComplexHelper.getPartKey(key3,1);
        complex = ApartmentTypeComplexHelper.getPartKey(key3,2);
        applied3.setApartmentTypeId(type);
        applied3.setComplexId(complex);
        applied3.setOrder(3);

      }

      if(applied3 == null && lApplied.size() >= 3){
        ((Applied)lApplied.get(2)).setID(-3);
      }
      if(applied2 == null && lApplied.size() >= 2){
        ((Applied)lApplied.get(1)).setID(-3);
      }
    }
    else{
      System.err.println("no key parameters for apartment");
    }
  }


  public PresentationObject getViewApplication(Application eApplication){
    Table T = new Table();
      T.add(headerText(iwrb.getLocalizedString("application","Application")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("submitted","Submitted")),1,2);
      T.add(Edit.formatText(eApplication.getSubmitted().toString()),2,2);
      T.add(Edit.formatText(iwrb.getLocalizedString("changed","Status change")),1,3);
      T.add(Edit.formatText(eApplication.getStatusChanged().toString()),2,3);
      T.add(Edit.formatText(iwrb.getLocalizedString("status","Status")),3,2);
      T.add(Edit.formatText(getStatus(eApplication.getStatus())),3,3);


      T.setCellpadding(1);
      T.setCellspacing(1);
      T.mergeCells(1,1,3,1);
      T.setBorder(0);
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = 4;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,3,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setWidth("100%");
    return T;
  }

  private PresentationObject getRemoteControl(String sStatus,boolean bEdit,IWResourceBundle iwrb){
      Table T = new Table();
      T.add(headerText(iwrb.getLocalizedString("control","Control")),1,1);
      T.add(Edit.titleText(iwrb.getLocalizedString("tax_return","Tax return")),1,2);
      T.add(Edit.titleText(iwrb.getLocalizedString("study_progress","Study progress")),1,3);
      T.add(Edit.titleText(iwrb.getLocalizedString("choice1","Choice 1")),1,4);
      T.add(Edit.titleText(iwrb.getLocalizedString("choice2","Choice 2")),1,5);
      TextInput units = new TextInput("unit");
       units.setLength(1);
       Edit.setStyle(units);
      CheckBox choice1 = new CheckBox("choice1");
       Edit.setStyle(choice1);
      CheckBox choice2 = new CheckBox("choice2");
       Edit.setStyle(choice2);
      CheckBox choice3 = new CheckBox("choice3");
       Edit.setStyle(choice3);
      T.add(choice1,2,2);
      T.add(units,2,3);
      T.add(choice2,2,4);
      T.add(choice3,2,5);
      DropdownMenu status = statusDrop("status_drop",sStatus);
      status.setToSubmit();
      Edit.setStyle(status);
      T.add(status,2,6);
      if(bEdit){
        SubmitButton view = new SubmitButton("viewer","View");
        T.add(view,2,7);
      }
      else{
        SubmitButton edit = new SubmitButton("editor","Edit");
        T.add(edit,2,7);
      }
      SubmitButton save = new SubmitButton("save","Save");
      T.add(save,2,7);

      T.mergeCells(1,1,2,1);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setBorder(0);
       T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorBlue);
      int lastrow = 8;
      T.setRowColor(lastrow,Edit.colorRed);
      T.mergeCells(1,lastrow,4,lastrow);
      T.add(Edit.formatText(" "),1,lastrow);
      T.setHeight(lastrow,Edit.bottomBarThickness);
      T.setHeight("100%");
      T.setWidth("100%");
    return T;
  }

  private PresentationObject getKnobs(IWResourceBundle iwrb){
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

  public void doUpdate(IWContext iwc){
    String sDesc= iwc.getParameter("app_subj_desc").trim();
    String sDate = iwc.getParameter("app_subj_xdate");
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
    DropdownMenu order = orderDrop("global_order",sGlobalOrder);
    drp.setToSubmit();
    status.setToSubmit();
    order.setToSubmit();
    Edit.setStyle(status);
    Edit.setStyle(order);
    myForm.add(drp);
    myForm.add(status);
    myForm.add(order);
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
      Edit.setStyle(drp);
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

  private DropdownMenu intDrop(String name,String selected,int low,int high){
    DropdownMenu drp = new DropdownMenu(name);
    for (int i = low; i <= high; i++) {
      drp.addMenuElement(String.valueOf(i));
    }
    drp.setSelectedElement(selected);
    return drp;
  }

  private DropdownMenu statusDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("S",getStatus("S"));
    drp.addMenuElement("A",getStatus("A"));
    drp.addMenuElement("R",getStatus("R"));
    drp.setSelectedElement(selected);
    return drp;
  }

   private DropdownMenu orderDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    Applicant A = new Applicant();

    drp.addMenuElement(A.getFullnameOrderValue(),iwrb.getLocalizedString("name","Name"));
    drp.addMenuElement(A.getSSNColumnName(),iwrb.getLocalizedString("ssn","Socialnumber"));
    drp.addMenuElement(A.getLegalResidenceColumnName(),iwrb.getLocalizedString("legal_residence","Legal Residence"));
    drp.addMenuElement(A.getResidenceColumnName(),iwrb.getLocalizedString("residence","Residence"));
    drp.addMenuElement(A.getResidenceColumnName(),iwrb.getLocalizedString("phone","Residence phone"));
    drp.setSelectedElement(selected);
    return drp;
  }


  public Link getApplicationLink(PresentationObject MO,int id){
    Link L = new Link(MO);
    L.setFontSize(1);
    L.addParameter("view",id);
    return L;
  }

  public Text headerText(String text){
    Text T = new Text(text);
    T.setBold();
    T.setFontColor(Edit.colorWhite);
    T.setFontSize(1);
    return T;
  }

  public Link getPDFLink(PresentationObject MO,int cam_app_id){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("cam_app_id",cam_app_id);
    return L;
  }
  public Link getPDFLink(PresentationObject MO,String status,int subject_id){
    Window W = new Window("PDF","/allocation/applicationfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("app_status",status);
    L.addParameter("app_sub_id",subject_id);
    return L;
  }

  public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

}

