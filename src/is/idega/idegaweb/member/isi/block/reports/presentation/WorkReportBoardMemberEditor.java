package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.data.EntityValueHolder;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converters.DropDownMenuConverter;
import com.idega.block.entity.presentation.converters.OptionProvider;
import com.idega.block.entity.presentation.converters.TextEditorConverter;
import com.idega.business.IBOLookup;
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 3, 2003
 */
public class WorkReportBoardMemberEditor extends WorkReportSelector {
	
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportboardmembereditor.step_name";
  
  private static final String SUBMIT_CREATE_NEW_ENTRY_KEY = "submit_cr_new_entry_key";
  private static final String SUBMIT_SAVE_NEW_ENTRY_KEY = "submit_sv_new_entry_key";
  private static final Integer NEW_ENTRY_ID_VALUE = new Integer(-1);
  
  private static final String ACTION_SHOW_NEW_ENTRY = "action_show_new_entry";
  
  protected static final String LEAGUE = "league";

  private static final String STATUS = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.STATUS";
  private static final String NAME = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.NAME";
  private static final String PERSONAL_ID = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.PERSONAL_ID";
  private static final String STREET_NAME = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.STREET_NAME";
  private static final String POSTAL_CODE_ID = "com.idega.core.data.PostalCode.POSTAL_CODE_ID|POSTAL_CODE";
  private static final String HOME_PHONE = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.HOME_PHONE";
  private static final String WORK_PHONE = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.WORK_PHONE";
  private static final String FAX = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.FAX";
  private static final String EMAIL = "is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember.EMAIL";
  
  private List fieldList;
  
  { 
    fieldList = new ArrayList();
    fieldList.add(LEAGUE);
    fieldList.add(STATUS);
    fieldList.add(NAME);
    fieldList.add(PERSONAL_ID);
    fieldList.add(STREET_NAME);
    fieldList.add(POSTAL_CODE_ID);
    fieldList.add(HOME_PHONE);
    fieldList.add(WORK_PHONE);
    fieldList.add(FAX);
    fieldList.add(EMAIL);
  }
    
  
//  private List orderedHelperList;

  public WorkReportBoardMemberEditor() {
    super();
    setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
  }  
  
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    IWResourceBundle resourceBundle = getResourceBundle(iwc);
    
    if (this.getWorkReportId() != -1) {
			//sets this step as bold, if another class calls it this will be overwritten 
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
      String action = parseAction(iwc);
      Form form = new Form();
      PresentationObject pres = getContent(iwc, resourceBundle, form, action);
      form.maintainParameters(this.getParametersToMaintain());
      form.add(pres);
      add(form);
    }
  }
  
  private String parseAction(IWContext iwc) {
    // does the user want to edit a new entry?
    String action = "";
    if (iwc.isParameterSet(SUBMIT_CREATE_NEW_ENTRY_KEY))  {
      action = ACTION_SHOW_NEW_ENTRY;
    }  
    // does the user want to save a new entry?
    if (iwc.isParameterSet(SUBMIT_SAVE_NEW_ENTRY_KEY))  {
      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
      WorkReportBoardMember member = createWorkReportBoardMember();
      Iterator iterator = fieldList.iterator();
      while (iterator.hasNext())  {
        String field = (String) iterator.next();
        EntityPathValueContainer entityPathValueContainerFromTextEditor = 
          TextEditorConverter.getResultByEntityIdAndEntityPathShortKey(NEW_ENTRY_ID_VALUE, field, iwc);
        EntityPathValueContainer entityPathValueContainerFromDropDownMenu = 
          DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(NEW_ENTRY_ID_VALUE, field, iwc);
        if (entityPathValueContainerFromTextEditor.isValid()) {
          setValuesOfWorkReportBoardMember(entityPathValueContainerFromTextEditor, member, workReportBusiness);
        }
        if (entityPathValueContainerFromDropDownMenu.isValid()) {
          setValuesOfWorkReportBoardMember(entityPathValueContainerFromDropDownMenu, member, workReportBusiness);
        }
      }
      member.store();
    }  
    // does the user want to modify an existing entity?
    EntityPathValueContainer entityPathValueContainerFromTextEditor = TextEditorConverter.getResultByParsing(iwc);
    EntityPathValueContainer entityPathValueContainerFromDropDownMenu = DropDownMenuConverter.getResultByParsing(iwc);
    if (entityPathValueContainerFromTextEditor.isValid()) {
      updateWorkReportBoardMember(entityPathValueContainerFromTextEditor, iwc);
    }
    if (entityPathValueContainerFromDropDownMenu.isValid()) {
      updateWorkReportBoardMember(entityPathValueContainerFromDropDownMenu, iwc);
    }
    return action;
  }
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle, Form form, String action) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    Collection coll;
    try {
      coll =
        workReportBusiness.getAllWorkReportBoardMembersForWorkReportId(
          getWorkReportId());
    } catch (RemoteException e) {
      System.err.println("[WorkReportMemberDataEditor] Can't get members. Message was: "+ e.getMessage());
      e.printStackTrace(System.err);
      coll = new ArrayList();
    }
    // create new entities
    List list = new ArrayList();
    Iterator iterator = coll.iterator();
    while (iterator.hasNext())  {
      WorkReportBoardMember member = (WorkReportBoardMember) iterator.next();
      Collection leagues = null;
      try {
        leagues = member.getLeaguesForMember();
      }
      catch (IDOException ex) {
        System.err.println(
          "[WorkReportBoardMemberEditor]: Can't retrieve leagues. Message is: "
            + ex.getMessage());
        ex.printStackTrace(System.err);
        throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve leagues.");
      }
      Iterator leagueIterator = leagues.iterator();
      while (leagueIterator.hasNext())  {
        WorkReportGroup league = (WorkReportGroup) leagueIterator.next();
        String leagueName = league.getName();
        WorkReportBoardMemberHelper helper = new WorkReportBoardMemberHelper(leagueName, member);
        list.add(helper);
      }
    }
    // add a value holder for a new entry if desired
    if (ACTION_SHOW_NEW_ENTRY.equals(action)) {
      EntityValueHolder valueHolder = new EntityValueHolder(); 
      // trick , because postal code is a "foreign" column 
      valueHolder.setColumnValue("POSTAL_CODE_ID", valueHolder); 
      WorkReportBoardMemberHelper valueHolderHelper = new WorkReportBoardMemberHelper("", valueHolder);
      list.add(valueHolderHelper);
    }
    // sort list
    Comparator comparator = new Comparator()  {
      public int compare(Object first, Object second) {
        String firstLeague = (String) ((WorkReportBoardMemberHelper) first).getColumnValue(LEAGUE);
        String secondLeague = (String) ((WorkReportBoardMemberHelper) second).getColumnValue(LEAGUE);
        return firstLeague.compareTo(secondLeague);
      }
    };
    Collections.sort(list, comparator);
//    // store this list for converters
//    orderedHelperList = list;
//    Collection entities = new ArrayList();
//    Iterator iteratorHelper = list.iterator();
//    while (iteratorHelper.hasNext())  {
//      WorkReportBoardMemberHelper helper = (WorkReportBoardMemberHelper) iteratorHelper.next();
//      entities.add(helper.getMember());
//    }      

    EntityBrowser browser = getEntityBrowser(list, resourceBundle, form);
    // put browser into a table
    Table mainTable = new Table(1,2);
    mainTable.add(browser, 1,1);
    PresentationObject newEntryButton = (ACTION_SHOW_NEW_ENTRY.equals(action)) ? 
      getSaveNewEntityButton(resourceBundle) : getCreateNewEntityButton(resourceBundle);
    mainTable.add(newEntryButton,1,2);
    return mainTable;
    
  }
  
  private PresentationObject getCreateNewEntityButton(IWResourceBundle resourceBundle) {
    String createNewEntityText = resourceBundle.getLocalizedString("wr_board_member_editor_create_new_entry", "New entry");
    SubmitButton button = new SubmitButton(createNewEntityText, SUBMIT_CREATE_NEW_ENTRY_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }
  
  private PresentationObject getSaveNewEntityButton(IWResourceBundle resourceBundle)  {
    String saveNewEntityText = resourceBundle.getLocalizedString("wr_board_member_editor_save_new_entry", "Save");
    SubmitButton button = new SubmitButton(saveNewEntityText, SUBMIT_SAVE_NEW_ENTRY_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }    
 
  private EntityBrowser getEntityBrowser(Collection entities, IWResourceBundle resourceBundle, Form form)  {
    // define converter
    TextEditorConverter textEditorConverter = new TextEditorConverter(form);
    textEditorConverter.maintainParameters(this.getParametersToMaintain());
    EntityToPresentationObjectConverter statusDropDownMenuConverter = getConverterForStatus(resourceBundle, form);
    EntityToPresentationObjectConverter leagueDropDownMenuConverter = getConverterForLeague(resourceBundle, form);
    
    // define path short keys and map corresponding converters
    Object[] columns = {
      LEAGUE, leagueDropDownMenuConverter,
      STATUS, statusDropDownMenuConverter,
      NAME, textEditorConverter,
      PERSONAL_ID, textEditorConverter,
      STREET_NAME, textEditorConverter,
      POSTAL_CODE_ID, textEditorConverter,
      HOME_PHONE, textEditorConverter,
      WORK_PHONE, textEditorConverter,
      FAX, textEditorConverter,
      EMAIL, textEditorConverter};
    EntityBrowser browser = new EntityBrowser();
    browser.setLeadingEntity("is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember");
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    if( entities!=null && !entities.isEmpty()) browser.setDefaultNumberOfRows(entities.size());
    // switch off the internal form of the browser
    browser.setUseExternalForm(true);
    for (int i = 0; i < columns.length; i+=2) {
      String column = (String) columns[i];
      EntityToPresentationObjectConverter converter = (EntityToPresentationObjectConverter) columns[i+1];
      browser.setMandatoryColumn(i, column);
      browser.setEntityToPresentationConverter(column, converter);
    }
    browser.setEntities("dummy_string", entities);
    return browser;
  }
  
  private EntityToPresentationObjectConverter getConverterForStatus(final IWResourceBundle resourceBundle, Form form) {
    DropDownMenuConverter converter = new DropDownMenuConverter(form);
    OptionProvider optionProvider = new OptionProvider() {
      
      Map optionMap = null;
      
      public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
        if (optionMap == null)  {
          optionMap = new TreeMap();
          String[] options = { 
            IWMemberConstants.MEMBER_BOARD_MEMBER, 
            IWMemberConstants.MEMBER_BOARD_STATUS_CHAIR_MAN,
            IWMemberConstants.MEMBER_CASHIER,
            IWMemberConstants.MEMBER_SECRETARY};
          for (int i = 0; i < options.length; i++) {  
            String display = resourceBundle.getLocalizedString(options[i], options[i]);
            optionMap.put(options[i],display);
          }
        }
        return optionMap;
      }
    };     
    converter.setOptionProvider(optionProvider); 
    converter.maintainParameters(getParametersToMaintain());
    return converter;
  }
  
  /**
   * Converter for league column 
   */
  private EntityToPresentationObjectConverter getConverterForLeague(final IWResourceBundle resourceBundle, Form form) {
    DropDownMenuConverter converter = new DropDownMenuConverter(form);
//      protected Object getValue(
//        Object entity,
//        EntityPath path,
//        EntityBrowser browser,
//        IWContext iwc)  {
//          int i = browser.getCurrentIndexOfEntities();
//          WorkReportBoardMemberHelper helper = (WorkReportBoardMemberHelper) orderedHelperList.get(i);
//          return helper.getLeague();
        

        
    OptionProvider optionProvider = new OptionProvider() {
      
    Map optionMap = null;
      
    public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
      if (optionMap == null)  {
        optionMap = new TreeMap();
        WorkReportBusiness business = getWorkReportBusiness(iwc);
        Collection coll = null;
        try {
          coll = business.getAllLeagueWorkReportGroupsForYear(getYear());
        }
        catch (RemoteException ex) {
          System.err.println(
            "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
              + ex.getMessage());
          ex.printStackTrace(System.err);
          throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
        }
        Iterator collIterator = coll.iterator();
        while (collIterator.hasNext())  {
          WorkReportGroup league = (WorkReportGroup) collIterator.next();
          String name = league.getName(); 
          String display = resourceBundle.getLocalizedString(name, name);
          optionMap.put(name, display);
          }
        }
        return optionMap;
      }
    };     
    converter.setOptionProvider(optionProvider); 
    converter.maintainParameters(getParametersToMaintain());
    return converter;
  }    
  
  private WorkReportBoardMember createWorkReportBoardMember()  {
    WorkReportBoardMember workReportBoardMember = null;
    try {
      workReportBoardMember =
        (WorkReportBoardMember) IDOLookup.create(WorkReportBoardMember.class);
    } catch (IDOLookupException e) {
      System.err.println("[WorkReportBoardMember] Could not lookup home of WorkReportBoardMember. Message is: "+ e.getMessage());
      e.printStackTrace(System.err);
    } catch (CreateException e) {
      System.err.println("[WorkReportBoardMember] Could not create new WorkReportBoardMember. Message is: "+ e.getMessage());
      e.printStackTrace(System.err);
    }
    workReportBoardMember.setReportId(getWorkReportId());
    workReportBoardMember.store();
    return workReportBoardMember;
  }
  
  private void updateWorkReportBoardMember(EntityPathValueContainer valueContainer, IWContext iwc)  {
    // precondition: value container is valid, that is its method isValid() returns true.
    // get the corresponding entity
    Integer id = valueContainer.getEntityId();
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    WorkReportBoardMember member = null;
    try {
      member = workReportBusiness.getWorkReportBoardMemberHome().findByPrimaryKey(id);
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBoardMember. Message is: "
          + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBoardMember.");
    }
    catch (FinderException ex)  {
      System.err.println(
      "[WorkReportBoardMemberEditor]: Can't find WorkReportBoardMember, Message is: "
        + ex.getMessage());
      ex.printStackTrace(System.err);
    }
    setValuesOfWorkReportBoardMember(valueContainer, member, workReportBusiness);
    member.store();
  }

  private void setValuesOfWorkReportBoardMember(EntityPathValueContainer valueContainer, WorkReportBoardMember member, WorkReportBusiness workReportBusiness)  {
    String pathShortKey = valueContainer.getEntityPathShortKey();
    Object value = valueContainer.getValue();
    
    if (pathShortKey.equals(STATUS))  {
      member.setStatus(value.toString());
    }
    else if (pathShortKey.equals(NAME)) {
      member.setName(value.toString());
    }
    else if (pathShortKey.equals(PERSONAL_ID))  {
      member.setPersonalId(value.toString());
    }
    else if (pathShortKey.equals(STREET_NAME))  {
      member.setStreetName(value.toString());
    }
    else if(pathShortKey.equals(POSTAL_CODE_ID))  {
      try {
        int postalCode = Integer.parseInt(value.toString());
        member.setPostalCodeID(postalCode);
      }
      catch (NumberFormatException ex)  {
      }
    }
    else if(pathShortKey.equals(HOME_PHONE))  {
      member.setHomePhone(value.toString());
    }
    else if (pathShortKey.equals(WORK_PHONE)) {
      member.setWorkPhone(value.toString());
    }
    else if (pathShortKey.equals(FAX))  {
      member.setFax(value.toString());
    }
    else if (pathShortKey.equals(EMAIL))  {
      member.setEmail(value.toString());
    }
    else if (pathShortKey.equals(LEAGUE)) {
      // special case, sometimes there is not a previous value
      Object previousValue = valueContainer.getPreviousValue();
      String oldWorkGroupName = (previousValue == null) ? null : previousValue.toString();
      String newWorkGroupName = value.toString();
      int year = getYear();
      try {
        workReportBusiness.changeWorkReportGroupOfMember(oldWorkGroupName, year, newWorkGroupName, year, member);
      }
      catch (RemoteException ex) {
        System.err.println(
          "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
            + ex.getMessage());
        ex.printStackTrace(System.err);
        throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
      }
    }
  }
}

  /** 
   * WorkReportBoardMemberHelper:
   * Inner class, represents a WorkReportBoardMember but with the league as additional value.
   *
   */ 
    
  class WorkReportBoardMemberHelper implements EntityRepresentation {
    
    String league = null;
    Object member = null;
    
    public WorkReportBoardMemberHelper()  {
    }
    
    public WorkReportBoardMemberHelper(String league, Object member) {
      this.league = league;
      this.member = member;
    }
    
    public void setLeague(String league)  {
      this.league = league;
    }
    
    public void setMember(WorkReportBoardMember member) {
      this.member = member;
    }
    
    public Object getColumnValue(String columnName) {
      if (WorkReportBoardMemberEditor.LEAGUE.equals(columnName))  {
        return league;
      }
      return ((EntityRepresentation) member).getColumnValue(columnName);
    }  
    
    public Object getPrimaryKey() {
      return ((EntityRepresentation) member).getPrimaryKey();
    }
  }

  


  