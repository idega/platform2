package is.idega.idegaweb.campus.block.finance.presentation;


import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.finance.business.*;
import is.idega.idegaweb.campus.exception.*;
import com.idega.block.finance.data.*;
import com.idega.block.building.data.*;
import com.idega.block.finance.business.*;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.data.GenericEntity;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.text.*;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaTimestamp;

import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusEntryGroups extends PresentationObjectContainer {

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  public  String strAction = "tt_action";
  protected boolean isAdmin = false;
  public final char cComplex = 'x';
  public final char cAll = 'a';
  public final char cBuilding = 'b';
  public final char cFloor = 'f';
  public final char cCategory = 'c';
  public final char cType = 't';
  public final char cApartment = 'p';

  private int iCashierId = -1;


  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus.block.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusEntryGroups() {

  }

   protected void control(IWContext iwc){

   if(isAdmin){
      try{
        PresentationObject MO = new Text();

        if(iwc.getParameter(strAction) == null){
          MO = getTableOfGroups(iwc);
        }
        if(iwc.getParameter(strAction) != null){
          String sAct = iwc.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO = getTableOfGroups(iwc); break;
            case ACT2 : MO = doMainTable(iwc);      break;
            case ACT3 : MO = doSomeThing( iwc);      break;
            case ACT4 : MO = getTableOfAssessmentAccounts( iwc);      break;
            case ACT5 : MO = doRollback(iwc); break;
            default: MO = getTableOfGroups(iwc);           break;
          }
        }
         Table T = new Table();
         T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(0);
        T.add(Edit.headerText(iwrb.getLocalizedString("tariffer","Tariffer"),3),1,1);
        T.add(makeLinkTable(  1),1,2);
        T.add(MO,1,3);
        add(T);

      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else
      add(iwrb.getLocalizedString("access_denied","Access denies"));
  }

  private PresentationObject doRollback(IWContext iwc){
    Table T = new Table();
    String sRoundId = iwc.getParameter("rollback");
    if(sRoundId != null){
      int iRoundId = Integer.parseInt(sRoundId);
      try{
        CampusAssessmentBusiness.rollBackAssessment(iRoundId);
        T.add(iwrb.getLocalizedString("rollbac_success","Rollback was successfull"));
      }
      catch(RollBackException ex){
        T.add(iwrb.getLocalizedString("rollbac_illegal","Rollback was illegal"));
      }
    }
    return T;
  }

  private PresentationObject doSomeThing(IWContext iwc){
    PresentationObject MO = new Text("failed");
    if(iwc.getParameter("ent_to")!=null){

      String dateFrom = iwc.getParameter("ent_from");
      String dateTo = iwc.getParameter("ent_to");
      if(!"".equals(dateTo)){
        idegaTimestamp to = new idegaTimestamp(dateTo);
        idegaTimestamp from = null;
        if(!"".equals(dateFrom))
          from =  new idegaTimestamp(dateFrom );

        doGroup(from,to);
        return new Text("Succeeded");
      }
      return new Text("Invalid to date");
    }
    return MO;
  }

  private PresentationObject doGroup(idegaTimestamp from , idegaTimestamp to){
    try {
      //System.err.println(" doGroup start :"+idegaTimestamp.RightNow().toString());
      //CampusAssessmentBusiness.groupEntries(from,to);
      CampusAssessmentBusiness.groupEntriesWithSQL(from,to);
      //System.err.println(" doGroup end   :"+idegaTimestamp.RightNow().toString());
      return Edit.formatText(iwrb.getLocalizedString("group_created","Group was created"));
    }
    catch (CampusFinanceException ex) {
      ex.printStackTrace();
      return Edit.formatText(iwrb.getLocalizedString("group_not_created","Group was not created"));
    }
  }

  protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(Edit.colorDark);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link(iwrb.getLocalizedString("view","View"));
    Link1.setFontColor(Edit.colorLight);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link(iwrb.getLocalizedString("new","New"));
    Link2.setFontColor(Edit.colorLight);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link Link3 = new Link(iwrb.getLocalizedString("test","test"));
    Link3.setFontColor(Edit.colorLight);
    Link3.setWindowToOpen(MessageWindow.class);
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
      LinkTable.add(Link3,3,1);
    }
    return LinkTable;
  }

  private PresentationObject getTableOfGroups(IWContext iwc){
    Table T = new Table();
    int row = 2;
    List L = Finder.listOfEntryGroups();
    if(L!= null){
      int len = L.size();

      String sRollBack = iwrb.getLocalizedString("rollback","Rollback");
      T.add(Edit.formatText(iwrb.getLocalizedString("group_id","Group id")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("group_date","Group date")),2,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("entry_from","Entries from")),3,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("entry_to","Entries to")),4,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("file_name","File name")),5,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("real_count","Real count")),6,1);
      //T.add(Edit.formatText(sRollBack),4,1);


      int col = 1;
      row = 2;
      EntryGroup EG;
      java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
      for (int i = 0; i < len; i++) {
        col = 1;
        EG = (EntryGroup) L.get(i);
        T.add(getGroupLink(EG.getName(),EG.getID()),col++,row);
        T.add(Edit.formatText(new idegaTimestamp(EG.getGroupDate()).getLocaleDate(iwc)),col++,row);
        T.add(Edit.formatText(EG.getEntryIdFrom()),col++,row);
        T.add(Edit.formatText(EG.getEntryIdTo()),col++,row);
        T.add(Edit.formatText(EG.getFileName()),col++,row);
        T.add(Edit.formatText(CampusAssessmentBusiness.getGroupEntryCount(EG)),col++,row);

        /*
        Link R = new Link(iwb.getImage("rollback.gif"));
        R.addParameter("rollback",AR.getID());
        R.addParameter(strAction ,ACT5);

        T.add(R,col++,row);
*/
        row++;
      }
      T.setWidth("100%");
      T.setCellpadding(2);
      T.setCellspacing(1);
      T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
      T.setRowColor(1,Edit.colorMiddle);
      row++;
    }
    else
      T.add(iwrb.getLocalizedString("no_groups","No groups"),1,row);
    return T;
  }

  private PresentationObject getTableOfAssessmentAccounts(IWContext iwc){
    Table T = new Table();
    String id = iwc.getParameter("entry_group_id");
    if(id != null){
      List L = Finder.listOfEntriesInGroup(Integer.parseInt(id));
      if(L!= null){
        int len = L.size();
        T.add(Edit.titleText(iwrb.getLocalizedString("entry_name","Entry name")),1,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("last_updated","Last updated")),2,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("amount","Amount")),3,1);

        int col = 1;
        int row = 2;
        AccountEntry A;
        java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
        for (int i = 0; i < len; i++) {
          col = 1;
          A = (AccountEntry) L.get(i);
          T.add(Edit.formatText(A.getName()),col++,row);
          T.add(Edit.formatText(new idegaTimestamp(A.getLastUpdated()).getLocaleDate(iwc)),col++,row);
          T.add(Edit.formatText(nf.format(A.getTotal())),col++,row);
          row++;
        }
        T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(1);
        T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
        T.setRowColor(1,Edit.colorMiddle);
        row++;
      }
      else
        add("is empty");
    }
    return T;
  }

  private PresentationObject doMainTable(IWContext iwc){
    Form F = new Form();
    Table T = new Table();

    int row = 2;

    T.add(Edit.formatText(iwrb.getLocalizedString("entries_from","Entries from")),1,row);

    idegaTimestamp today = idegaTimestamp.RightNow();
    DateInput di1 = new DateInput("ent_from",true);
    //di.setDate(new idegaTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());
    T.add(di1,2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("entries_to","Entries to")),1,row);
    DateInput di2 = new DateInput("ent_to",true);
    di2.setDate(today.getSQLDate());
    T.add(di2,2,row);

    SubmitButton sb = new SubmitButton("commit",iwrb.getLocalizedString( "commit","Commit"));
    Edit.setStyle(sb);

    DropdownMenu drpAccountTypes = new DropdownMenu("account_type");
    drpAccountTypes.addMenuElement(Account.typeFinancial,iwrb.getLocalizedString("financial","Financial"));
    drpAccountTypes.addMenuElement(Account.typePhone,iwrb.getLocalizedString("phone","phone"));
    Edit.setStyle(drpAccountTypes );


    row++;
    T.add(sb,2,row);
    row++;


    T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    T.setRowColor(1,Edit.colorMiddle);
    T.mergeCells(1,1,2,1);
    T.setWidth("100%");
    F.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    F.add(T);
    return F;
  }

  private DropdownMenu drpAccountKeys(List AK,String name){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    if(AK != null){
      drp.addMenuElements(AK);
    }
    return drp;
  }

  private PresentationObject makeEntryGroups(){
    return new Text();
  }

  private List listOfConAccAprt(char cAttribute, int iAttributeId){
    List L = null;
    switch (cAttribute) {
    case 'a' : L = CampusAccountFinder.listOfRentingUserAccounts();  break;
    case 't' : L = CampusAccountFinder.listOfConAccAprtByType(iAttributeId); break;
    case 'p' : L = CampusAccountFinder.listOfConAccAprtByApartment(iAttributeId); break;
    }
  return L;
  }

  private Link getGroupLink(String name,int id){
    Link L = new Link(name);
    L.addParameter(strAction,ACT4);
    L.addParameter("entry_group_id",id);
    L.setFontSize(Edit.textFontSize);
    return L;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}
