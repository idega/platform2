package is.idega.idegaweb.member.isi.block.reports.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKey;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKeyHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecord;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecordHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converters.CheckBoxConverter;
import com.idega.block.entity.presentation.converters.ConverterConstants;
import com.idega.block.entity.presentation.converters.DropDownMenuConverter;
import com.idega.block.entity.presentation.converters.EditOkayButtonConverter;
import com.idega.block.entity.presentation.converters.TextEditorConverter;
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.datastructures.HashMatrix;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Aug 30, 2003
 */
public class WorkReportAccountEditor extends WorkReportSelector {

  private static final String STEP_NAME_LOCALIZATION_KEY = "workreportboardmembereditor.step_name";
  
  private static final String SUBMIT_CREATE_NEW_ENTRY_KEY = "submit_cr_new_entry_key";
  private static final String SUBMIT_DELETE_ENTRIES_KEY = "submit_del_new_entry_key";
  private static final String SUBMIT_CANCEL_KEY = "submit_cancel_key";
  
  // do not use -1 (-1 means null or new entity)
  // dummy keys for primary entries
  private static final String INCOME_SUM_KEY = "FIN_income_sum";
  private static final String EXPONSES_SUM_KEY = "FIN_exponses_sum";
  private static final String INCOME_EXPONSES_SUM_KEY = "FIN_income_exponses_sum";
  private static final String ASSET_SUM_KEY = "FIN_asset_sum";
  private static final String DEBT_SUM_KEY = "FIN_debt_sum";
  
  private static final String ACTION_SHOW_NEW_ENTRY = "action_show_new_entry";
  
  private static final String CHECK_BOX = "checkBox";

  private List fieldList = new ArrayList();
      
  private HashMatrix leagueKeyMatrix = new HashMatrix();
  
  private Map accountKeyNamePrimaryKey = new HashMap();

  public WorkReportAccountEditor() {
    super();
    setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
  }  
  
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    IWResourceBundle resourceBundle = getResourceBundle(iwc);
    
    if (this.getWorkReportId() != -1) {
      //sets this step as bold, if another class calls it this will be overwritten 
      setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
      initialize(iwc);
      String action = parseAction(iwc);
      Form form = new Form();
      PresentationObject pres = getContent(iwc, resourceBundle, form, action);
      form.maintainParameters(this.getParametersToMaintain());
      form.add(pres);
      add(form);
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
        deleteWorkReportAccountGroupHelper(entriesToDelete, iwc);
        // !! do nothing else !!
        // do not modify entry
        // do not create an entry
        return action;
      }
    }

//    // does the user want to create a new entry?
//    if (iwc.isParameterSet(SUBMIT_CREATE_NEW_ENTRY_KEY))  {
//      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
//      if (iwc.isParameterSet(SSN)) {
//        String personalId = iwc.getParameter(SSN);
//        createWorkReportMember(personalId, iwc);
//      }
//    }  
    // does the user want to modify an existing entity? 
    if (iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY)) {
      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
      String id = iwc.getParameter(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY);
      Integer groupId = null;
      try {
        groupId = new Integer(id);
      }
      catch (NumberFormatException ex)  {
        System.err.println("[WorkReportAccountEditor] Wrong primary key. Message is: " +
          ex.getMessage());
        ex.printStackTrace(System.err);
      }
      Iterator iterator = fieldList.iterator();
      while (iterator.hasNext())  {
        String field = (String) iterator.next();
        EntityPathValueContainer entityPathValueContainerFromTextEditor = 
          TextEditorConverter.getResultByEntityIdAndEntityPathShortKey(groupId, field, iwc);
        EntityPathValueContainer entityPathValueContainerFromDropDownMenu = 
          DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(groupId, field, iwc);
        if (entityPathValueContainerFromTextEditor.isValid()) {
          setValuesOfWorkReportClubAccountRecord(entityPathValueContainerFromTextEditor, groupId, workReportBusiness);
        }
        if (entityPathValueContainerFromDropDownMenu.isValid()) {
          setValuesOfWorkReportClubAccountRecord(entityPathValueContainerFromDropDownMenu, groupId, workReportBusiness);
        }
      }
      return action;
    }  
      
      
//    // does the user want to modify an existing entity?
//    EntityPathValueContainer entityPathValueContainerFromTextEditor = TextEditorConverter.getResultByParsing(iwc);
//    EntityPathValueContainer entityPathValueContainerFromDropDownMenu = DropDownMenuConverter.getResultByParsing(iwc);
//    if (entityPathValueContainerFromTextEditor.isValid()) {
//      updateWorkReportBoardMember(entityPathValueContainerFromTextEditor, iwc);
//    }
//    if (entityPathValueContainerFromDropDownMenu.isValid()) {
//      updateWorkReportBoardMember(entityPathValueContainerFromDropDownMenu, iwc);
//    }
    return action;
  }
  
  private void initialize(IWContext iwc) {
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
    // collect all work report account records
    WorkReportClubAccountRecordHome workReportClubAccountRecordHome = null;
    Collection workReportClubAccountRecords = null;
    try {
      workReportClubAccountRecordHome = workReportBusiness.getWorkReportClubAccountRecordHome();
    }
    catch (RemoteException ex) {
      System.err.println(
        "[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness. Message is: "
          + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException("[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness.");
    }
    try {
      workReportClubAccountRecords = workReportClubAccountRecordHome.findAllRecordsByWorkReportId(workReportId);
    }
    catch (FinderException ex) {
      String message =
        "[WorkReportBoardAccountEditor]: Can't find WorkReportClubAccountRecordHome";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      workReportClubAccountRecords = new ArrayList();
    }
      
    Iterator workReportClubAccountRecordsIterator = workReportClubAccountRecords.iterator();
    while (workReportClubAccountRecordsIterator.hasNext())  {
      WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) workReportClubAccountRecordsIterator.next();
      // note: workReportGroupId is -1 if the record belongs to the main board
      Integer groupId = new Integer(record.getWorkReportGroupId());
      Integer accountKey = new Integer(record.getAccountKeyId());
      leagueKeyMatrix.put(groupId, accountKey, record);
    }
  }    
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle, Form form, String action) {
    int workReportId = getWorkReportId();
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    WorkReport workReport = null;
    // get work report 
    try {
      workReport = workReportBusiness.getWorkReportById(workReportId);
    }
    catch (RemoteException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException(message);
    }
    // get leagues of the workreport
    // needs to be converted to a list because of sorting 
    List workReportLeagues;
    try {
      workReportLeagues = new ArrayList(workReport.getLeagues());
    }
    catch (IDOException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't retrieve leagues.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      workReportLeagues = new ArrayList();
    }
    // income, exponses, asset and debt keys 
    WorkReportAccountKeyHome accountKeyHome;
    try {
      accountKeyHome = workReportBusiness.getWorkReportAccountKeyHome();
    }
    catch (RemoteException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't retrieve WorkReportAccountKeyHome.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException(message);
    }
    List incomeKeys = null;
    List exponsesKeys = null;
    List assetKeys = null;
    List debtKeys = null; 
    try {
      incomeKeys = new ArrayList(accountKeyHome.findIncomeAccountKeys());
      exponsesKeys = new ArrayList(accountKeyHome.findExponsesAccountKeys());
      assetKeys = new ArrayList(accountKeyHome.findAssetAccountKeys());
      debtKeys = new ArrayList(accountKeyHome.findDeptAccountKeys());
    }
    catch (FinderException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't find account keys.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      incomeKeys = new ArrayList();
      exponsesKeys = new ArrayList();
      assetKeys = new ArrayList();
      debtKeys = new ArrayList();
    }
    // define the columns
    Comparator keyComparator = new Comparator() {
      public int compare(Object first, Object second) {
        String firstParentKey = ((WorkReportAccountKey) first).getParentKeyNumber();
        String secondParentKey = ((WorkReportAccountKey) second).getParentKeyNumber();
        Integer firstKey = new Integer(((WorkReportAccountKey) first).getKeyNumber());
        Integer secondKey = new Integer(((WorkReportAccountKey) second).getKeyNumber());
        // Object: (key, parent key)
        if (firstParentKey == null && secondParentKey == null)  {
          // first Object: (34, null), second Object: (12, null): 34 < 12
          return firstKey.compareTo(secondKey);
        }
        else if (firstParentKey != null && secondParentKey != null) {
          if (firstParentKey.equals(secondParentKey)) {
            // firstObject: (76, 12), second Object: (65, 12): 76 < 65
            return firstKey.compareTo(secondKey);
          }
          else {
            // firstObject: (13, 4), second Object: (76, 3): 4 < 3
            Integer firstParent = new Integer(firstParentKey);
            Integer secondParent = new Integer(secondParentKey);
            return firstParent.compareTo(secondParent);
          }
        }
        else if (firstParentKey != null) { // and second parent key is now null
          // first Object: (12, 3), second Object: (44, null): 3 <  44
          Integer firstParent = new Integer(firstParentKey);
          return firstParent.compareTo(secondKey);
        }
        else {
          // first Object: (12, null), second Object: (44, 5): 12 < 5
          Integer secondParent = new Integer(secondParentKey);
          return secondParent.compareTo(firstKey);
        }
      }
    };
    // sort keys
    Collections.sort(incomeKeys, keyComparator);
    Collections.sort(exponsesKeys, keyComparator);
    Collections.sort(assetKeys, keyComparator);
    Collections.sort(debtKeys, keyComparator);
    // add sorted keys to the fields
    addKeys(incomeKeys);
    fieldList.add(INCOME_SUM_KEY);
    addKeys(exponsesKeys);
    fieldList.add(EXPONSES_SUM_KEY);
    fieldList.add(INCOME_EXPONSES_SUM_KEY);
    addKeys(assetKeys);
    fieldList.add(ASSET_SUM_KEY);
    addKeys(debtKeys);
    fieldList.add(DEBT_SUM_KEY);

    // sort league collection
    Comparator leagueComparator = new Comparator()  {
      public int compare(Object first, Object second) {
        String firstName = ((WorkReportGroup) first).getName();
        String secondName = ((WorkReportGroup) second).getName();
        return firstName.compareTo(secondName);
      }
    };
    // sort leagues
    Collections.sort(workReportLeagues, leagueComparator);
    // add the main board (represented by null)
    workReportLeagues.add(null);
    // create helper 
    // iterate over leagues
    Collection workReportAccountGroupHelpers = new ArrayList();
    Iterator leagueIterator = workReportLeagues.iterator();
    while (leagueIterator.hasNext())  {
      WorkReportGroup group = (WorkReportGroup) leagueIterator.next();
      Integer groupId = (group == null) ? null : group.getGroupId();
      WorkReportAccountGroupHelper helper = new WorkReportAccountGroupHelper(groupId);
      workReportAccountGroupHelpers.add(helper);
    }
    // define entity browser
    EntityBrowser browser = getEntityBrowser(workReportAccountGroupHelpers, resourceBundle, form);
    // put browser into a table
    Table mainTable = new Table(1,2);
    mainTable.add(browser, 1,1);
    // add buttons
    PresentationObject newEntryButton = getCreateNewEntityButton(resourceBundle);
    PresentationObject deleteEntriesButton = getDeleteEntriesButton(resourceBundle);
    PresentationObject cancelButton = getCancelButton(resourceBundle);
    // add buttons
    Table buttonTable = new Table(3,1);
    buttonTable.add(newEntryButton,1,1);
    buttonTable.add(deleteEntriesButton,2,1);
    buttonTable.add(cancelButton, 3,1);
    mainTable.add(buttonTable,1,2);
    return mainTable;
  }
  
  private void addKeys(List accountKeys)  {
    Iterator iterator = accountKeys.iterator();
    while (iterator.hasNext())  {
      WorkReportAccountKey key = (WorkReportAccountKey) iterator.next();
      Integer primaryKey = (Integer) key.getPrimaryKey();
      String name = key.getKeyName();
      fieldList.add(name);
      accountKeyNamePrimaryKey.put(name, primaryKey);
    }
  }
  
  private PresentationObject getCreateNewEntityButton(IWResourceBundle resourceBundle) {
    String createNewEntityText = resourceBundle.getLocalizedString("wr_board_member_editor_create_new_entry", "New entry");
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
    EntityBrowser browser = new EntityBrowser();
    browser.setLeadingEntity(WorkReportClubAccountRecord.class);
    browser.setShowMirroredView(true);
    // no settings button 
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    browser.setEntities("dummy_string", entities);
    if( entities!=null && !entities.isEmpty()) {
      browser.setDefaultNumberOfRows(entities.size());
    }
    // switch off the internal form of the browser
    browser.setUseExternalForm(true);
    // define converter
    CheckBoxConverter checkBoxConverter = new CheckBoxConverter();
    TextEditorConverter textEditorConverter = new TextEditorConverter(form);
    textEditorConverter.maintainParameters(this.getParametersToMaintain());
    // define path short keys and map corresponding converters
    int i = 1;
    Iterator fieldListIterator = fieldList.iterator();
    while (fieldListIterator.hasNext()) {
      String fieldName = fieldListIterator.next().toString();
      browser.setMandatoryColumn(i++, fieldName);
      browser.setEntityToPresentationConverter(fieldName, textEditorConverter);
    }
    browser.setMandatoryColumn(i++, "okay");
    browser.setEntityToPresentationConverter("okay", new EditOkayButtonConverter());
    return browser;
  }
  
//  /**
//   * Converter for league column 
//   */
//  private EntityToPresentationObjectConverter getConverterForLeague(final IWResourceBundle resourceBundle, Form form) {
//    DropDownMenuConverter converter = new DropDownMenuConverter(form) {
//      protected Object getValue(
//        Object entity,
//        EntityPath path,
//        EntityBrowser browser,
//        IWContext iwc)  {
//          return ((EntityRepresentation) entity).getColumnValue(LEAGUE);
//        }
//      };        
//
//        
//    OptionProvider optionProvider = new OptionProvider() {
//      
//    Map optionMap = null;
//      
//    public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
//      if (optionMap == null)  {
//        optionMap = new TreeMap();
//        WorkReportBusiness business = getWorkReportBusiness(iwc);
//        Collection coll = null;
//        try {
//          coll = business.getAllLeagueWorkReportGroupsForYear(getYear());
//        }
//        catch (RemoteException ex) {
//          System.err.println(
//            "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
//              + ex.getMessage());
//          ex.printStackTrace(System.err);
//          throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
//        }
//        Iterator collIterator = coll.iterator();
//        while (collIterator.hasNext())  {
//          WorkReportGroup league = (WorkReportGroup) collIterator.next();
//          String name = league.getName(); 
//          String display = resourceBundle.getLocalizedString(name, name);
//          optionMap.put(name, display);
//          }
//        }
//        // add default value: no league (because main board members are not assigned to a league)
//        optionMap.put(NO_LEAGUE_VALUE, resourceBundle.getLocalizedString("wr_board_member_editor_no_league","no league"));
//        return optionMap;
//      }
//    };     
//    converter.setOptionProvider(optionProvider); 
//    converter.maintainParameters(getParametersToMaintain());
//    return converter;
//  }   
   
  /** business method: delete WorkReportAccountGroupHelper.
   * @param ids - List of primaryKeys (Integer)
   */
  private void deleteWorkReportAccountGroupHelper(List ids, IWContext iwc) {
    Iterator idIterator = ids.iterator();
    while (idIterator.hasNext())  {
      Integer groupId = (Integer) idIterator.next();
      Map recordsMap = leagueKeyMatrix.get(groupId);
      Collection records = recordsMap.values();
      Iterator iteratorRecords = records.iterator();
      while (iteratorRecords.hasNext())  {
        WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) iteratorRecords.next();
        try {
          record.remove();
        }
        catch (EJBException ex) {
          String message =
            "[WorkReportAccountEditor]: Can't remove WorkReportClubAccountRecord.";
          System.err.println(message + " Message is: " + ex.getMessage());
          ex.printStackTrace(System.err);
        }
        catch (RemoveException ex)  {
          String message =
            "[WorkReportAccountEditor]: Can't remove WorkReportClubAccountRecord.";
          System.err.println(message + " Message is: " + ex.getMessage());
          ex.printStackTrace(System.err);
        }
      }
    }
  }
    
  
//  // business method: create
//  private WorkReportBoardMember createWorkReportBoardMember()  {
//    WorkReportBoardMember workReportBoardMember = null;
//    try {
//      workReportBoardMember =
//        (WorkReportBoardMember) IDOLookup.create(WorkReportBoardMember.class);
//    } catch (IDOLookupException e) {
//      System.err.println("[WorkReportBoardMemberEditor] Could not lookup home of WorkReportBoardMember. Message is: "+ e.getMessage());
//      e.printStackTrace(System.err);
//    } catch (CreateException e) {
//      System.err.println("[WorkReportBoardMemberEditor] Could not create new WorkReportBoardMember. Message is: "+ e.getMessage());
//      e.printStackTrace(System.err);
//    }
//    workReportBoardMember.setReportId(getWorkReportId());
//    workReportBoardMember.store();
//    return workReportBoardMember;
//  }
  
  // business method: set values (invoked by 'update' or 'create')
  private void setValuesOfWorkReportClubAccountRecord(EntityPathValueContainer valueContainer, Integer groupId, WorkReportBusiness workReportBusiness)  {
    String pathShortKey = valueContainer.getEntityPathShortKey();
    Object value = valueContainer.getValue();
    Float amount;
    try {
      amount = new Float(value.toString());
    }
    catch (NumberFormatException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't convert value to float value.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      // give up
      return;
    }
    Integer accountKey;
    try {
      accountKey = new Integer(pathShortKey.toString());
    }
    catch (NumberFormatException ex) {
      String message =
        "[WorkReportAccountEditor]: Can't convert pathShortKey to an integer value.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      // give up
      return;
    }
    Object previousValue = valueContainer.getPreviousValue();
    if (value.equals(previousValue))  {
      // nothing to do
      return;
    }
    // try to get an existing record
    WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) leagueKeyMatrix.get(groupId, accountKey);
    if (record == null)   {
      // okay, first create a record
      WorkReportClubAccountRecordHome home;
      try {
        home = workReportBusiness.getWorkReportClubAccountRecordHome();
      }
      catch (RemoteException ex) {
        String message =
          "[WorkAccountEditor]: Can't retrieve WorkReportClubAccountRecordHome.";
        System.err.println(message + " Message is: " + ex.getMessage());
        ex.printStackTrace(System.err);
        throw new RuntimeException(message);
      } 
      try {
        record = home.create();
        record.setReportId(getWorkReportId());
        record.setAccountKeyId(accountKey.intValue());
        // add this new record to the matrix
        leagueKeyMatrix.put(groupId, accountKey, amount);
      }
      catch (CreateException ex) {
        String message =
          "[WorkReportAccountEditor]: Can't create WorkreportClubAccountRecord.";
        System.err.println(message + " Message is: " + ex.getMessage());
        ex.printStackTrace(System.err);
        // give up
        return;
      }
    }
    record.setAmount(amount.floatValue());
    // do not forget to store
    record.store();
  }
    
    
  /** 
   * WorkReportBoardMemberHelper:
   *
   */     
  class WorkReportAccountGroupHelper implements EntityRepresentation {
    
    Integer groupId;
    
    public WorkReportAccountGroupHelper()  {
    }
    
    public WorkReportAccountGroupHelper(Integer groupId) {
      this.groupId = groupId;
    }
    
    public Float getEntry(String accountKeyName) {
      Integer primaryKey = (Integer) accountKeyNamePrimaryKey.get(accountKeyName);
      if (primaryKey == null) {
        return new Float(999999);
      }
      WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) leagueKeyMatrix.get(groupId, primaryKey);
      // sometimes the record does not exist yet
      if (record == null) {
        return new Float(-1);
      }
      float amount = record.getAmount();
      return new Float(amount);
    }
    
    public int getGroupId() {
      if (groupId == null)  {
        return -1;
      }
      return groupId.intValue();
    }
     
    public Object getColumnValue(String columnName) {
      return getEntry(columnName);
    }  
    
    public Object getPrimaryKey() {
      return new Integer(getGroupId());
    }
  }
  
  /**
   * 
   */
  
  class WorkReportAccountInputConverter extends TextEditorConverter {
    
    public WorkReportAccountInputConverter(Form form) {
      super(form);
    }
    
    protected Object getValue(
        Object entity,
        EntityPath path,
        EntityBrowser browser,
        IWContext iwc)  {
      String name = path.getShortKey();
      return ((EntityRepresentation) entity).getColumnValue(name);
    } 
    
  }       
    

} 


  
