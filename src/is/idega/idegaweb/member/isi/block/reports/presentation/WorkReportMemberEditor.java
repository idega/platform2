package is.idega.idegaweb.member.isi.block.reports.presentation;



import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
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
import javax.ejb.RemoveException;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.data.EntityValueHolder;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converters.CheckBoxConverter;
import com.idega.block.entity.presentation.converters.ConverterConstants;
import com.idega.block.entity.presentation.converters.DropDownMenuConverter;
import com.idega.block.entity.presentation.converters.DropDownPostalCodeConverter;
import com.idega.block.entity.presentation.converters.EditOkayButtonConverter;
import com.idega.block.entity.presentation.converters.OptionProvider;
import com.idega.block.entity.presentation.converters.TextEditorConverter;
import com.idega.core.data.PostalCode;
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on August 26, 2003
 */
public class WorkReportMemberEditor extends WorkReportSelector {
  private static final String STEP_NAME_LOCALIZATION_KEY = "workreportmembereditor.step_name";
  
  private static final String SUBMIT_CREATE_NEW_ENTRY_KEY = "submit_cr_new_entry_key";
  private static final String SUBMIT_DELETE_ENTRIES_KEY = "submit_del_new_entry_key";
  private static final String SUBMIT_CANCEL_KEY = "submit_cancel_key";

  private static final Integer NEW_ENTRY_ID_VALUE = new Integer(-1);
  
  private static final String ACTION_SHOW_NEW_ENTRY = "action_show_new_entry";
  
  private static final String CHECK_BOX = "checkBox";

  private static final String NAME = WorkReportMember.class.getName()+".NAME";
  private static final String PERSONAL_ID = WorkReportMember.class.getName()+".PERSONAL_ID";
  private static final String STREET_NAME = WorkReportMember.class.getName()+".STREET_NAME";
  private static final String POSTAL_CODE_ID = PostalCode.class.getName()+".POSTAL_CODE_ID|POSTAL_CODE";
  
  private static final String SSN = "ssn";
  
  private List fieldList;
  private List leagueList;
  
  protected Map memberLeaguesMap = null;
    
  public WorkReportMemberEditor() {
    super();
    setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
  }  
  
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    initializeFieldList(iwc);
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
  
  private void initializeFieldList(IWContext iwc) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    Collection leagues;
    try {
      leagues = workReportBusiness.getAllWorkReportGroupsForYearAndType( getYear() ,IWMemberConstants.GROUP_TYPE_LEAGUE);
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportMemberEditor]: Can't retrieve WorkReportGroups. Message is: "
          + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportMemberEditor]: Can't retrieve WorkReportGroups.");
    }
    fieldList = new ArrayList();
    leagueList = new ArrayList();
    fieldList.add(NAME);
    fieldList.add(PERSONAL_ID);
    fieldList.add(STREET_NAME);
    fieldList.add(POSTAL_CODE_ID);
    Iterator iterator = leagues.iterator();
    while (iterator.hasNext())  {
      WorkReportGroup group = (WorkReportGroup) iterator.next();
      String groupName = group.getName();
      leagueList.add(groupName);
    }
  }
  
  private String parseAction(IWContext iwc) {
    String action = "";
    // does the user want to cancel something?
    if (iwc.isParameterSet(SUBMIT_CANCEL_KEY)) {
      return action;
      // !! do nothing else !!
      // do not modify entry
      // do not create an entry
      // do not delete an entry
    }
    // does the user want to delete an existings entries?
    if (iwc.isParameterSet(SUBMIT_DELETE_ENTRIES_KEY)) {
      List entriesToDelete = CheckBoxConverter.getResultByParsingUsingDefaultKey(iwc);
      if (! entriesToDelete.isEmpty())  {
        deleteWorkReportMember(entriesToDelete, iwc);
        // !! do nothing else !!
        // do not modify entry
        // do not create an entry
        return action;
      }
    }

    // does the user want to save a new entry?
    if (iwc.isParameterSet(SUBMIT_CREATE_NEW_ENTRY_KEY))  {
      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
      if (iwc.isParameterSet(SSN)) {
        String personalId = iwc.getParameter(SSN);
        createWorkReportMember(personalId, iwc);
      }
    }  
    // does the user want to modify an existing entity? 
    if (iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY)) {
      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
      String id = iwc.getParameter(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY);
      Integer primaryKey = null;
      try {
        primaryKey = new Integer(id);
      }
      catch (NumberFormatException ex)  {
        System.err.println("[WorkReportBoardMemberEditor] Wrong primary key. Message is: " +
          ex.getMessage());
        ex.printStackTrace(System.err);
      }
      WorkReportMember member = findWorkReportMember(primaryKey, iwc);
      Iterator iterator = fieldList.iterator();
      while (iterator.hasNext())  {
        String field = (String) iterator.next();
        EntityPathValueContainer entityPathValueContainerFromTextEditor = 
          TextEditorConverter.getResultByEntityIdAndEntityPathShortKey(primaryKey, field, iwc);
        EntityPathValueContainer entityPathValueContainerFromDropDownMenu = 
          DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(primaryKey, field, iwc);
        if (entityPathValueContainerFromTextEditor.isValid()) {
          setValuesOfWorkReportMember(entityPathValueContainerFromTextEditor, member, workReportBusiness);
        }
        if (entityPathValueContainerFromDropDownMenu.isValid()) {
          setValuesOfWorkReportMember(entityPathValueContainerFromDropDownMenu, member, workReportBusiness);
        }
      }
      // update the connections to leagues
      // get the current leagues of the member
      Collection membersLeague = null;
      try {
        membersLeague = member.getLeaguesForMember();
      } 
      catch (IDOException ex) {
        System.err.println("[WorkReportMemberEditor]: Couldn't get leagues. Message is: " +
          ex.getMessage());
        ex.printStackTrace(System.err);
        return action;
      }  
      Iterator membersLeagueIterator = membersLeague.iterator();
      Collection membersLeagueNames = new ArrayList();
      while (membersLeagueIterator.hasNext())  {
        WorkReportGroup league= (WorkReportGroup) membersLeagueIterator.next();
        String leagueName = league.getName();
        membersLeagueNames.add(leagueName);
      }
      Iterator leagueIterator = leagueList.iterator();
      while (leagueIterator.hasNext())  {
        String key = (String) leagueIterator.next();
        boolean isChecked = CheckBoxConverter.isEntityChecked(iwc, key, primaryKey);
        boolean wasChecked = membersLeagueNames.contains(key);
        if ( isChecked ^ wasChecked) {
          if (isChecked)  {
            addLeague(workReportBusiness, key, member);
          }
          else {
            removeLeague(workReportBusiness, key, member);
          }
        }
      }
      member.store();
      return action;
    }
    return action;
  }
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle, Form form, String action) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    try {
      // create data from the database
      workReportBusiness.createWorkReportData(getWorkReportId());
    } catch (RemoteException ex) {
      System.err.println(
        "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
          + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportMemberEditor]: Can't retrieve WorkReportBusiness.");
    } 
    // get members
    Collection members;
    try {
      members =
        workReportBusiness.getAllWorkReportMembersForWorkReportId(getWorkReportId());
    } catch (RemoteException e) {
      System.err.println("[WorkReportMemberEditor] Can't get members. Message was: "+ e.getMessage());
      e.printStackTrace(System.err);
      members = new ArrayList();
    }
    // create map: member as key, leagues as value 
    memberLeaguesMap = new HashMap();
    Iterator membersIterator = members.iterator();
    while (membersIterator.hasNext())  {
      WorkReportMember member = (WorkReportMember) membersIterator.next();
      try {
        Iterator leagues = member.getLeaguesForMember().iterator();
        List leaguesList = new ArrayList();
        while (leagues.hasNext()) {
          WorkReportGroup league = (WorkReportGroup) leagues.next();
          String leagueName = league.getName();
          leaguesList.add(leagueName);
        }
        Integer memberId = (Integer) member.getPrimaryKey();
        memberLeaguesMap.put(memberId, leaguesList);
      }
      catch (IDOException ex) {
        System.err.println("[WorkReportMemberEditor] Can't get leagues. Message is: " + 
          ex.getMessage());
        ex.printStackTrace(System.err);
      }
    }
    EntityBrowser browser = getEntityBrowser(members, resourceBundle, form);
    // put browser into a table
    Table mainTable = new Table(1,2);
    mainTable.add(browser, 1,1);
    PresentationObject inputField = getPersonalIdInputField(resourceBundle);
    PresentationObject newEntryButton = getCreateNewEntityButton(resourceBundle);
    PresentationObject deleteEntriesButton = getDeleteEntriesButton(resourceBundle);
    PresentationObject cancelButton = getCancelButton(resourceBundle);
    Table buttonTable = new Table(4,1);
    buttonTable.add(inputField,1,1);
    buttonTable.add(newEntryButton,2,1);
    buttonTable.add(deleteEntriesButton,3,1);
    buttonTable.add(cancelButton, 4,1);
    mainTable.add(buttonTable,1,2);
    return mainTable;    
  }
  
  private PresentationObject getPersonalIdInputField(IWResourceBundle resourceBundle) {
    Text text = new Text(resourceBundle.getLocalizedString("WorkReportMemberEditor.add_member","Enter personal id"));
    text.setBold();
    TextInput ssn = new TextInput("ssn");
    String error = resourceBundle.getLocalizedString("WorkReportMemberEditor.ssn_not_valid","The personal id you entered is invalid");
    ssn.setAsIcelandicSSNumber(error);
    Table table = new Table(2,1);
    table.add(text,1,1);
    table.add(ssn,2,1);
    return table;
  }
  
  private PresentationObject getCreateNewEntityButton(IWResourceBundle resourceBundle) {
    String createNewEntityText = resourceBundle.getLocalizedString("wr_div_board_member_editor_create_new_entry", "New entry");
    SubmitButton button = new SubmitButton(createNewEntityText, SUBMIT_CREATE_NEW_ENTRY_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }
  
  private PresentationObject getDeleteEntriesButton(IWResourceBundle resourceBundle)  {
    String deleteEntityText = resourceBundle.getLocalizedString("wr_board_member_editor_remove_entries", "Remove");
    SubmitButton button = new SubmitButton(deleteEntityText, SUBMIT_DELETE_ENTRIES_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }  

  private PresentationObject getCancelButton(IWResourceBundle resourceBundle)  {
    String cancelText = resourceBundle.getLocalizedString("wr_board_member_editor_cancel", "Cancel");
    SubmitButton button = new SubmitButton(cancelText, SUBMIT_CANCEL_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }    
 
  private EntityBrowser getEntityBrowser(Collection entities, IWResourceBundle resourceBundle, Form form)  {
    // define converter
    CheckBoxConverter checkBoxConverter = new CheckBoxConverter();
    TextEditorConverter textEditorConverter = new TextEditorConverter(form);
    textEditorConverter.maintainParameters(this.getParametersToMaintain());
    EntityToPresentationObjectConverter dropDownPostalCodeConverter = getConverterForPostalCode(form);
    
    // define path short keys and map corresponding converters
    Object[] columns = {
      "okay", new EditOkayButtonConverter(),
      CHECK_BOX, checkBoxConverter,
      NAME, textEditorConverter,
      PERSONAL_ID, textEditorConverter,
      STREET_NAME, textEditorConverter,
      POSTAL_CODE_ID, dropDownPostalCodeConverter};
    EntityBrowser browser = new EntityBrowser();
    browser.setLeadingEntity(WorkReportDivisionBoard.class);
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
    // add more columns
    Iterator iterator = leagueList.iterator();
    int i = 100;
    while (iterator.hasNext())  {
      String leagueName = (String) iterator.next();
      EntityToPresentationObjectConverter converter = new WorkReportCheckBoxConverter(leagueName);
      browser.setMandatoryColumn(i++, leagueName);
      browser.setEntityToPresentationConverter(leagueName, converter);
    }
    browser.setEntities("dummy_string", entities);
    return browser;
  }
  
  
  
  /**
   * converter for postal code column
   */
  private EntityToPresentationObjectConverter getConverterForPostalCode(Form form) {
    DropDownPostalCodeConverter dropDownPostalCodeConverter = new DropDownPostalCodeConverter(form);
    dropDownPostalCodeConverter.setCountry("Iceland");
    dropDownPostalCodeConverter.maintainParameters(getParametersToMaintain());
    return dropDownPostalCodeConverter;
  }     

  /** business method: delete WorkReportBoardMembers.
   * @param ids - List of primaryKeys (Integer)
   */
  private void deleteWorkReportMember(List ids, IWContext iwc) {
    Iterator iterator = ids.iterator();
    while (iterator.hasNext())  {
      Integer id = (Integer) iterator.next();
      WorkReportMember member = findWorkReportMember(id, iwc);
      if (member != null) {
        try {
          member.remove();
        }
        catch (RemoveException ex) {
          System.err.println(
            "[WorkReportDivisionBoardEditor]: Can't remove WorkReportDivisionBoard. Message is: "
              + ex.getMessage());
          ex.printStackTrace(System.err);
          // do nothing
        }
      }
    }
    
  }

  /** business method: find
   * @param primaryKey
   * @return desired WorkReportDivisionBoard or null if not found
   */
  private WorkReportMember findWorkReportMember(Integer primaryKey, IWContext iwc) {
    WorkReportMember member = null;
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    try {
      member = workReportBusiness.getWorkReportMemberHome().findByPrimaryKey(primaryKey);
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportDivisionBoardEditor]: Can't retrieve WorkReportDivisionBoard. Message is: "
          + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportDivisionBoardEditor]: Can't retrieve WorkReportDivisionBoard.");
    }
    catch (FinderException ex)  {
      System.err.println(
      "[WorkReportDivisionBoardEditor]: Can't find WorkReportDivisionBoard. Message is: "
        + ex.getMessage());
      ex.printStackTrace(System.err);
    }  
    return member;
  }
  
  // business method: create
  private void createWorkReportMember(String personalId, IWApplicationContext iwac)  {
    try {
      getWorkReportBusiness(iwac).createWorkReportMember(getWorkReportId(), personalId);
    } catch (CreateException e) {
      System.err.println("[WorkReportDivisionBoardEditor] Could not create new WorkReportDivisionBoard. Message is: "+ e.getMessage());
      e.printStackTrace(System.err);
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
          + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportMemberEditor]: Can't retrieve WorkReportBusiness.");
    }
  }

  private void setValuesOfWorkReportMember(EntityPathValueContainer valueContainer, WorkReportMember member, WorkReportBusiness workReportBusiness)  {
    String pathShortKey = valueContainer.getEntityPathShortKey();
    Object value = valueContainer.getValue();
    
    if (pathShortKey.equals(NAME)) {
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
  }
  
  // business method: remove league
  private void removeLeague(WorkReportBusiness workReportBusiness, String groupToBeRemoved, WorkReportMember member) {
    int year = getYear();
    try {
      workReportBusiness.removeWorkReportGroupFromEntity(groupToBeRemoved, year, member);
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
        + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
    }
  }
  
  // business method: add league
  private void addLeague(WorkReportBusiness workReportBusiness, String groupToBeAdded, WorkReportMember member) {
    int year = getYear();
    try {
      workReportBusiness.addWorkReportGroupToEntity(groupToBeAdded, year, member);
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
        + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
    }
  }  

  /** 
   * CheckBoxConverterHelper:
   * Inner class.
   */
  
  class WorkReportCheckBoxConverter extends CheckBoxConverter {
    
    public WorkReportCheckBoxConverter(String key) {
      super(key);
    }
    
    public PresentationObject getPresentationObject(
      Object entity,
      EntityPath path,
      EntityBrowser browser,
      IWContext iwc) {
      
      EntityRepresentation idoEntity = (EntityRepresentation) entity;
      Integer id = (Integer) idoEntity.getPrimaryKey();
      CheckBox checkBox = new CheckBox(getKeyForCheckBox(), id.toString());
      Collection leagues = (Collection) memberLeaguesMap.get(id);
      boolean shouldBeChecked = (leagues != null && leagues.contains(getKeyForCheckBox()));
      checkBox.setChecked(shouldBeChecked);
      boolean disableCheckBox = true;
      if (iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_KEY)) {
        String idEditEntity = iwc.getParameter(ConverterConstants.EDIT_ENTITY_KEY);
        try {
          Integer primaryKey = new Integer(idEditEntity);
          if (id.equals(primaryKey))  {
            disableCheckBox = false;
          }
        }
        catch (NumberFormatException ex)  {
        }
      }
      checkBox.setDisabled(disableCheckBox);
      return checkBox;
    }
    
    public PresentationObject getHeaderPresentationObject(
      EntityPath entityPath,
      EntityBrowser browser,
      IWContext iwc) {
      Text text = new Text(getKeyForCheckBox());
      text.setBold();
      return text;
    }
  }
    
}
