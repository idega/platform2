package is.idega.idegaweb.member.isi.block.reports.presentation;



import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converter.CheckBoxConverter;
import com.idega.block.entity.presentation.converter.ConverterConstants;
import com.idega.block.entity.presentation.converter.editable.CheckBoxAsLinkConverter;
import com.idega.block.entity.presentation.converter.editable.DropDownMenuConverter;
import com.idega.block.entity.presentation.converter.editable.DropDownPostalCodeConverter;
import com.idega.block.entity.presentation.converter.editable.EditOkayButtonConverter;
import com.idega.block.entity.presentation.converter.editable.TextEditorConverter;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;

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
  private static final String SUBMIT_FINISH_KEY = "submit_finish_key";
  private static final String SUBMIT_REOPEN_KEY = "submit_reopen_key";

  private static final Integer NEW_ENTRY_ID_VALUE = new Integer(-1);
  
  private static final String ACTION_SHOW_NEW_ENTRY = "action_show_new_entry";
  
  private static final String CHECK_BOX = "checkBox";

  private static final String NAME = WorkReportMember.class.getName()+".NAME";
  private static final String PERSONAL_ID = WorkReportMember.class.getName()+".PERSONAL_ID";
  private static final String STREET_NAME = WorkReportMember.class.getName()+".STREET_NAME";
  private static final String POSTAL_CODE_ID = PostalCode.class.getName()+".POSTAL_CODE_ID|POSTAL_CODE";
  
  private static final String SSN = "ssn";
  
  private String errorMessageStyle = "errorMessage";
  
  private List fieldList;
  
  private boolean personalIdnotCorrect = false;
  private boolean memberAlreadyExist = false;
  private boolean editable = true;
  private boolean isReadOnly = false;
  
  private boolean updateWorkReportData = false;
  
  // key: member id, value: collection of league ids, to that the member belongs
  private Map memberLeaguesIdMap = null;
  // key: league id, int number of members that belong to that league 
  private Map leagueCountMap = null;
  private SortedMap leagueNameId = null;
  private Integer  mainBoardId = null;
  
  private int playersCount; 
  private int membersTotalSum;
  
  private String newMemberMessage = null;
    
  public WorkReportMemberEditor() {
    super();
    setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
  }  
  
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    if (this.getWorkReportId() != -1) {
      //sets this step as bold, if another class calls it this will be overwritten 
      setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
      initializeFieldList(iwc);
      IWResourceBundle resourceBundle = getResourceBundle(iwc);
      String action = parseAction(iwc, resourceBundle);
      Form form = new Form();
      PresentationObject pres = getContent(iwc, resourceBundle, form, action);
      form.maintainParameters(this.getParametersToMaintain());
      form.add(pres);
      add(form);
    }
  }
  
  protected void addBreakLine() {};
  
  private void initializeFieldList(IWContext iwc) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    Collection leagues;
    try {
      leagues = workReportBusiness.getLeaguesOfWorkReportById(getWorkReportId());
      WorkReportGroup workReportGroup = workReportBusiness.getMainBoardWorkReportGroup(getYear());
      mainBoardId = (Integer) workReportGroup.getPrimaryKey();
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportMemberEditor]: Can't retrieve WorkReportGroups. Message is: "
          + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportMemberEditor]: Can't retrieve WorkReportGroups.");
    }
    catch (IDOException idEx) {
      System.err.println(
        "[WorkReportMemberEditor]: Can't retrieve WorkReportGroups. Message is: "
          + idEx.getMessage());
      idEx.printStackTrace(System.err);
      leagues = new ArrayList();
    }
    fieldList = new ArrayList();
    fieldList.add(NAME);
    fieldList.add(PERSONAL_ID);
    fieldList.add(STREET_NAME);
    fieldList.add(POSTAL_CODE_ID);
    Iterator iterator = leagues.iterator();
    leagueCountMap = new HashMap();
    leagueNameId = new TreeMap();
    while (iterator.hasNext())  {
      WorkReportGroup group = (WorkReportGroup) iterator.next();
      // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      // special case: REMOVE the league that represents the main board
      // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      Integer primaryKey = (Integer) group.getPrimaryKey();
      if (! mainBoardId.equals(primaryKey))  {
        leagueCountMap.put( primaryKey, new Integer(0));
        String shortName = group.getShortName();
        leagueNameId.put(shortName, primaryKey);
      }
    }
  }
  
  private String parseAction(IWContext iwc, IWResourceBundle resourceBundle) {
    String action = "";
    if (iwc.isParameterSet(SUBMIT_FINISH_KEY))  {
      setWorkReportAsFinished(true, iwc);
      return action;
    }
    // does the user want to reopen the report?
    if (iwc.isParameterSet(SUBMIT_REOPEN_KEY))  {
      setWorkReportAsFinished(false, iwc);
      return action;
    }
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
        updateWorkReportData = true;
        return action;
      }
    }

    // does the user want to save a new entry?
    if (iwc.isParameterSet(SUBMIT_CREATE_NEW_ENTRY_KEY))  {
      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
      if (iwc.isParameterSet(SSN)) {
        String personalId = iwc.getParameter(SSN);
        WorkReportMember member = createWorkReportMember(personalId, iwc);
        if (member != null) {
          String name = member.getName();
          String ssn = member.getPersonalId();
          String ssnMessage = resourceBundle.getLocalizedString("wr_member_editor_ssn", "ssn");
          StringBuffer buffer = 
            new StringBuffer(resourceBundle.getLocalizedString("wr_member_editor_new_entry_was_created", "A new entry was created"));
          buffer.append(": ");
          buffer.append(name);
          buffer.append(" ");
          buffer.append(ssnMessage);
          buffer.append(": ");
          buffer.append(ssn);
          newMemberMessage = buffer.toString();
          updateWorkReportData = true;
        }
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
          setValuesOfWorkReportMember(entityPathValueContainerFromTextEditor, member, workReportBusiness, iwc);
        }
        if (entityPathValueContainerFromDropDownMenu.isValid()) {
          setValuesOfWorkReportMember(entityPathValueContainerFromDropDownMenu, member, workReportBusiness, iwc);
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
      Collection membersLeagueIds = new ArrayList();
      while (membersLeagueIterator.hasNext())  {
        WorkReportGroup league= (WorkReportGroup) membersLeagueIterator.next();
        Integer leagueId = (Integer) league.getPrimaryKey();
        membersLeagueIds.add(leagueId);
      }
      Iterator leagueIterator = leagueCountMap.keySet().iterator();
      while (leagueIterator.hasNext())  {
        Integer key = (Integer) leagueIterator.next();
        boolean isChecked = CheckBoxConverter.isEntityChecked(iwc, key.toString(), primaryKey);
        boolean wasChecked = membersLeagueIds.contains(key);
        if ( isChecked ^ wasChecked) {
          if (isChecked)  {
            addLeague(workReportBusiness, key , member);
          }
          else {
            removeLeague(workReportBusiness, key, member);
          }
        }
      }
      member.store();
      updateWorkReportData = true;
      return action;
    }
    return action;
  }
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle, Form form, String action) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    WorkReport workReport = null;
    try {
      // create data from the database
      int workReportId = getWorkReportId();
      workReport = workReportBusiness.getWorkReportById(getWorkReportId()); 
      isReadOnly = workReportBusiness.isWorkReportReadOnly(workReportId);   
      editable = ! (isReadOnly || workReport.isMembersPartDone());
    } 
    catch (RemoteException ex) {
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
    memberLeaguesIdMap = new HashMap();
    playersCount = 0;
    membersTotalSum = members.size();
    Iterator membersIterator = members.iterator();
    while (membersIterator.hasNext())  {
      WorkReportMember member = (WorkReportMember) membersIterator.next();
      try {
        Iterator leagues = member.getLeaguesForMember().iterator();
        List leaguesList = new ArrayList();
        // if there is at least one league the member is a player
        if (leagues.hasNext())  {
          playersCount++;
        }
        while (leagues.hasNext()) {
          WorkReportGroup league = (WorkReportGroup) leagues.next();
          Integer leagueId = (Integer) league.getPrimaryKey();
          if ( mainBoardId != null && (! mainBoardId.equals(leagueId) ) ) {
            leaguesList.add(leagueId);
            Integer count = (Integer) leagueCountMap.get(leagueId);
            // if count is equal to null something is wrong: connection between work report and work report group is
            // missing. So usually count is equal to null should never occur.
            // Also: REMOVE the league that represents the main board
            count = (count == null) ? new Integer(1) : new Integer( (count.intValue()) + 1 );
            leagueCountMap.put(leagueId, count);
          }
        }
        Integer memberId = (Integer) member.getPrimaryKey();
        memberLeaguesIdMap.put(memberId, leaguesList);
      }
      catch (IDOException ex) {
        System.err.println("[WorkReportMemberEditor] Can't get leagues. Message is: " + 
          ex.getMessage());
        ex.printStackTrace(System.err);
      }
    }
    if (updateWorkReportData) {
      try {
        updateWorkReportDataAndBoardData(iwc);
      }
      catch (Exception ex) {
        String message =
          "[WorkReportBoardMemberEditor]: Can't update work report.";
        System.err.println(message + " Message is: " + ex.getMessage());
        ex.printStackTrace(System.err);
      }
    }
    
      
    EntityBrowser browser = getEntityBrowser(members, resourceBundle, form, iwc);
		Table errorMessageTable = getErrorMessageTable();
    // get new entry message 
    if (newMemberMessage != null) {
      Text text = new Text(newMemberMessage);
      text.setBold();
      errorMessageTable.add(text);
      add(errorMessageTable);
    }
    // get error message
		
    if (personalIdnotCorrect) {
      String message = resourceBundle.getLocalizedString("wr_editor_ssn_not_valid", "The input of the social security number is not valid");
      Text text = new Text(message);
//      text.setBold();
//      text.setFontColor("#FF0000");
			errorMessageTable.add(text);
			add(errorMessageTable);
    }
    if (memberAlreadyExist) {
      String message = resourceBundle.getLocalizedString("wr_account_member_member_with_ssn_already_exist", "The member with the specified social security number does already exist");
      Text text = new Text(message);
//      text.setBold();
//      text.setFontColor("#FF0000");
			errorMessageTable.add(text);
			add(errorMessageTable);
    }
    
    // put browser into a table

    if (! isReadOnly) {
      Table mainTable = new Table(1,2);
      mainTable.add(browser, 1,1);
      mainTable.setCellspacing(0);
      mainTable.setCellpadding(0);
      Table buttonTable = new Table(4,1);
      if (editable) {
        PresentationObject inputField = getPersonalIdInputField(resourceBundle);
        PresentationObject newEntryButton = getCreateNewEntityButton(resourceBundle);
        PresentationObject deleteEntriesButton = getDeleteEntriesButton(resourceBundle);

        buttonTable.add(inputField,1,1);
        buttonTable.add(newEntryButton,2,1);
        buttonTable.add(deleteEntriesButton,3,1);
        buttonTable.add(getFinishButton(resourceBundle), 4, 1);
      }
      else {
//        Text text = new Text(resourceBundle.getLocalizedString("wr_member_editor_member_part_finished", "Member part has been finished."));
//        text.setBold();
        buttonTable.add(getReopenButton(resourceBundle), 4 , 1);
      }
      mainTable.add(buttonTable,1,2);
      return mainTable;
    }
    //if the report is read only, then it is printed out:
    else if(isReadOnly){
			Table mainTable = new Table(1,2);
			mainTable.add(browser, 1,1);
			mainTable.setCellspacing(0);
			mainTable.setCellpadding(0);
    	Table readOnlyTable = new Table(1,1);
    	readOnlyTable.setCellspacing(0);
    	readOnlyTable.setCellpadding(0);
    	Text readOnlyText = new Text(resourceBundle.getLocalizedString("WorkReportMemberEditor.report_is_read_only", "The report is read only"));
    	readOnlyTable.add(readOnlyText,1,1);
    	mainTable.add(readOnlyTable,1,2);
    	return mainTable;
    	
    }
    return browser;    
  }
  public Table getErrorMessageTable() {
  	Table errorMessageTable = new Table(1,1);
  	errorMessageTable.setCellpaddingAndCellspacing(0);
  	errorMessageTable.setWidth(Table.HUNDRED_PERCENT);
  	errorMessageTable.setAlignment("center");
  	errorMessageTable.setAlignment(1,1,"center");
  	errorMessageTable.setStyleClass(1,1,errorMessageStyle);
  	
  	return errorMessageTable;
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

  private PresentationObject getFinishButton(IWResourceBundle resourceBundle) {
    String finishText = resourceBundle.getLocalizedString("wr_member_editor_finish", "Finish");
    SubmitButton button = new SubmitButton(finishText, SUBMIT_FINISH_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }

  private PresentationObject getReopenButton(IWResourceBundle resourceBundle) {
    String reopenText = resourceBundle.getLocalizedString("wr_member_editor_reopen", "Reopen");
    SubmitButton button = new SubmitButton(reopenText, SUBMIT_REOPEN_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }
 
  private EntityBrowser getEntityBrowser(Collection entities, IWResourceBundle resourceBundle, Form form, IWContext iwc)  {
    // define converter
    CheckBoxConverter checkBoxConverter = new CheckBoxConverter();
    TextEditorConverter textEditorConverter = new TextEditorConverter(form);
    String message = resourceBundle.getLocalizedString("wr_member_editor_not_a_valid_ssn", "The input is not a valid social security number");
    EditOkayButtonConverter okayConverter = new EditOkayButtonConverter();
    okayConverter.maintainParameters(this.getParametersToMaintain());
    textEditorConverter.maintainParameters(this.getParametersToMaintain());
    DropDownMenuConverter dropDownPostalCodeConverter = getConverterForPostalCode(form);
    // define if the converters should be editable
    checkBoxConverter.setEditable(editable);
    textEditorConverter.setEditable(editable);
    dropDownPostalCodeConverter.setEditable(editable);    
    // define path short keys and map corresponding converters
    // if a converter is "null" the default converter of the entity browser is used
    Object[] columns = {
      "okay", okayConverter,
      CHECK_BOX, checkBoxConverter,
      NAME, null,
      PERSONAL_ID, null,
      STREET_NAME, textEditorConverter,
      POSTAL_CODE_ID, dropDownPostalCodeConverter};
    EntityBrowser browser = EntityBrowser.getInstanceUsingExternalForm();
    browser.setLeadingEntity(WorkReportMember.class);
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    // maintain parameters
    List parameters = getParametersToMaintain();
    Iterator para = parameters.iterator();
    while (para.hasNext())  {
      String parameterKey = (String) para.next();
      String parameterValue  = iwc.getParameter(parameterKey);
      String parameterString = (parameterValue == null) ? "" : parameterValue;
      Parameter parameter = new Parameter(parameterKey, parameterString);
      browser.addMandatoryParameter(parameter);
    }
    if( entities!=null && !entities.isEmpty()) {
    	browser.setDefaultNumberOfRows(entities.size());
    }
    for (int i = 0; i < columns.length; i+=2) {
      String column = (String) columns[i];
      EntityToPresentationObjectConverter converter = (EntityToPresentationObjectConverter) columns[i+1];
      browser.setMandatoryColumn(i, column);
      browser.setEntityToPresentationConverter(column, converter);
    }
    // add more columns
    Iterator iterator = leagueNameId.entrySet().iterator();
    int i = 100;
    while (iterator.hasNext())  {
     Map.Entry entry = (Map.Entry) iterator.next();
     String leagueName = (String) entry.getKey();
     Integer primaryKey = (Integer) entry.getValue();
     WorkReportCheckBoxConverter converter = new WorkReportCheckBoxConverter(primaryKey, leagueName);
     converter.setEditable(editable);
     converter.maintainParameters(getParametersToMaintain());
     browser.setMandatoryColumn(i++, leagueName);
     browser.setEntityToPresentationConverter(leagueName, converter);
    }
    browser.setDefaultNumberOfRows(Math.min(entities.size(), 20));
    browser.setEntities("dummy_string", entities);
    // some look settings
    browser.setCellpadding(2);
    browser.setCellspacing(0);
    browser.setBorder(0);
    browser.setColorForEvenRows("#EFEFEF");
    browser.setColorForOddRows("#FFFFFF");
    browser.setColorForHeader("#DFDFDF");
    browser.setArtificialCompoundId("workreportmembereditor", null);
    return browser;
  }
  
  
  
  /**
   * converter for postal code column
   */
  private DropDownMenuConverter getConverterForPostalCode(Form form) {
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
  private WorkReportMember findWorkReportMember(Integer primaryKey, IWApplicationContext iwac) {
    WorkReportMember member = null;
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwac);
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
  private WorkReportMember createWorkReportMember(String personalId, IWApplicationContext iwac)  {
    WorkReportMember member = findWorkReportMember(personalId, iwac);
    if (member != null) {
      memberAlreadyExist = true;
      return null;
    }
    try {
      member = getWorkReportBusiness(iwac).createWorkReportMember(getWorkReportId(), personalId);
      if (member == null) {
        personalIdnotCorrect = true;
      }
      return member;
    } 
    catch (CreateException e) {
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
    return null;
  }
  
  private void updateWorkReportDataAndBoardData(IWApplicationContext iwac) 
      throws RemoteException, FinderException, IDOException{
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwac);
    try {
      workReportBusiness.updateWorkReportData(getWorkReportId());
    }
    catch (Exception ex) {
      String message =
        "[WorkReportBoardMemberEditor]: Can't update work report data.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
    }
//    WorkReport workReport = workReportBusiness.getWorkReportById(getWorkReportId());
//    
//    workReport.setNumberOfMembers(membersTotalSum);
//    workReport.setNumberOfPlayers(playersCount);
//    workReport.store();
//    
//    WorkReportDivisionBoardHome home = workReportBusiness.getWorkReportDivisionBoardHome();
//    Collection boards = home.findAllWorkReportDivisionBoardByWorkReportId(getWorkReportId());
//    
//    Iterator iterator = boards.iterator();
//    while (iterator.hasNext())  {
//      WorkReportDivisionBoard board = (WorkReportDivisionBoard) iterator.next();
//      WorkReportGroup workReportGroup = board.getLeague();
//      String leagueName = workReportGroup.getName();
//      Integer number = (Integer) leagueCountMap.get(leagueName);
//      board.setNumberOfPlayers(number.intValue());
//      board.store();
    }

  private void setValuesOfWorkReportMember(EntityPathValueContainer valueContainer, WorkReportMember member, WorkReportBusiness workReportBusiness, IWApplicationContext iwac)  {
    String pathShortKey = valueContainer.getEntityPathShortKey();
    Object value = valueContainer.getValue();
    
//    if (pathShortKey.equals(PERSONAL_ID))  {
//      String socialSecurityNumber = value.toString();
//      User user = getUserBySocialSecurityNumber(socialSecurityNumber, workReportBusiness);
//      // there are some users in the system without any social security number
//      if (socialSecurityNumber.length() == 0 || user == null) {
//        personalIdnotCorrect = true;
//        return;
//      }
//      WorkReportMember wrMember = findWorkReportMember(socialSecurityNumber, iwac);
//      // if you have found the member that you are currently editing do not complain please
//      if (wrMember != null &&
//          ! (member.getPrimaryKey().equals(wrMember.getPrimaryKey())) ) {
//        memberAlreadyExist = true;
//        return;
//      }
//
//      member.setName(user.getName());
//      member.setUserId(((Integer) user.getPrimaryKey()).intValue());
//      member.setPersonalId(value.toString());
//      
//    }
    if (pathShortKey.equals(STREET_NAME))  {
      member.setStreetName(value.toString());
    }
    else if(pathShortKey.equals(POSTAL_CODE_ID))  {
      try {
        int postalCode = Integer.parseInt(value.toString());
        if (ConverterConstants.NULL_ENTITY_ID.intValue() == postalCode) {
          member.setPostalCode(null);
        }
        else {
          member.setPostalCodeID(postalCode);
        }
      }
      catch (NumberFormatException ex)  {
      }
    }
  }

  /** business method: find
   * @param primaryKey
   * @return desired WorkReportDivisionBoard or null if not found
   */  
  private WorkReportMember findWorkReportMember(String ssn, IWApplicationContext iwac) {
    WorkReportMember member = null;
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwac);
    try {
      member = workReportBusiness.getWorkReportMemberHome().
      findWorkReportMemberBySocialSecurityNumberAndWorkReportId(ssn, getWorkReportId());
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
      member = null;
    }  
    return member;
  }



  
  private User getUserBySocialSecurityNumber(String socialSecurirtyNumber, WorkReportBusiness workReportBusiness)  {
    User user = null;
    try {
      user = workReportBusiness.getUser(socialSecurirtyNumber);
    } 
    catch (FinderException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't retrieve user.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
    }
	catch (RemoteException ex) {
	  String message =
        "[WorkReportAccountEditor]: Can't retrieve user.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
	}
	
    return user;
  }    
 
  // business method: remove league
  private void removeLeague(WorkReportBusiness workReportBusiness, Integer groupToBeRemoved, WorkReportMember member) {
    try {
      WorkReportGroup workReportGroup = workReportBusiness.getWorkReportGroupHome().findByPrimaryKey(groupToBeRemoved);
      workReportBusiness.removeWorkReportGroupFromEntity(getWorkReportId(), workReportGroup, member);
    }
    catch (FinderException findEx) {
      String message =
        "[WorkReportMemberEditor]: Can't retrieve work report group.";
      System.err.println(message + " Message is: " + findEx.getMessage());
      findEx.printStackTrace(System.err);
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
  private void addLeague(WorkReportBusiness workReportBusiness, Integer groupToBeAdded, WorkReportMember member) {
    try {
      WorkReportGroup workReportGroup = workReportBusiness.getWorkReportGroupHome().findByPrimaryKey(groupToBeAdded);
      workReportBusiness.addWorkReportGroupToEntity(getWorkReportId(), workReportGroup, member);
    }
    catch (FinderException findEx) {
      String message =
        "[WorkReportMemberEditor]: Can't retrieve work report group.";
      System.err.println(message + " Message is: " + findEx.getMessage());
      findEx.printStackTrace(System.err);
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
        + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
    }
  }  

  private void setWorkReportAsFinished(boolean setAsFinished, IWContext iwc)  {
    int workReportId = getWorkReportId();
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    try {
      WorkReport workReport = workReportBusiness.getWorkReportById(workReportId);
      workReport.setMembersPartDone(setAsFinished);
      workReport.store();
    }
    catch (RemoteException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException(message);
    }
  }

  /** 
   * CheckBoxConverterHelper:
   * Inner class.
   */
  
  class WorkReportCheckBoxConverter extends CheckBoxAsLinkConverter {
    
    private String leagueName;
    private Integer leagueId;
    
    public WorkReportCheckBoxConverter(Integer key, String leagueName) {
      super(key.toString());
      this.leagueName = leagueName;
      // save convertions from string to integer
      this.leagueId = key;
    }
    
    protected boolean shouldEntityBeChecked(Object entity, Integer primaryKey) { 

      Collection leagues = (Collection) memberLeaguesIdMap.get(primaryKey);
      return (leagues != null && leagues.contains(leagueId));
    }

    
    public PresentationObject getHeaderPresentationObject(
      EntityPath entityPath,
      EntityBrowser browser,
      IWContext iwc) {
       
      StringBuffer buffer = new StringBuffer(leagueName);
      buffer.append(": ");
      buffer.append(leagueCountMap.get(leagueId));
      Text text = new Text(buffer.toString());
      text.setBold();
      return text;
    }
  }

}