package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 24, 2003
 */
public class WorkReportDivisionBoardEditor extends WorkReportSelector {
  private static final String STEP_NAME_LOCALIZATION_KEY = "workreportboarddivisioneditor.step_name";
  
//  private static final String SUBMIT_CREATE_NEW_ENTRY_KEY = "submit_cr_new_entry_key";
//  private static final String SUBMIT_SAVE_NEW_ENTRY_KEY = "submit_sv_new_entry_key";
//  private static final String SUBMIT_DELETE_ENTRIES_KEY = "submit_del_new_entry_key";
  private static final String SUBMIT_CANCEL_KEY = "submit_cancel_key";
  private static final String SUBMIT_FINISH_KEY = "submit_finish_key";
  private static final String SUBMIT_REOPEN_KEY = "submit_reopen_key";

//  private static final Integer NEW_ENTRY_ID_VALUE = new Integer(-1);
//  private static final String NO_LEAGUE_VALUE = "no_league_value";
  
//  private static final String ACTION_SHOW_NEW_ENTRY = "action_show_new_entry";
  
//  private static final String CHECK_BOX = "checkBox";

  private static final String LEAGUE = "league";

  private static final String HOME_PAGE = WorkReportDivisionBoard.class.getName()+".HOME_PAGE";
  private static final String PERSONAL_ID = WorkReportDivisionBoard.class.getName()+".PERSONAL_ID";
  private static final String STREET_NAME = WorkReportDivisionBoard.class.getName()+".STREET_NAME";
  private static final String POSTAL_CODE_ID = PostalCode.class.getName()+".POSTAL_CODE_ID|POSTAL_CODE";
  private static final String FIRST_PHONE = WorkReportDivisionBoard.class.getName()+".FIRST_PHONE";
  private static final String SECOND_PHONE = WorkReportDivisionBoard.class.getName()+".SECOND_PHONE";
  private static final String FAX = WorkReportDivisionBoard.class.getName()+".FAX";
  private static final String EMAIL = WorkReportDivisionBoard.class.getName()+".EMAIL";
  private static final String HAS_NATIONAL_LEAGUE = "HAS_NATIONAL_LEAGUE";
  
  private static List FIELD_LIST;
  
  static { 
    FIELD_LIST = new ArrayList();
    
    FIELD_LIST.add(LEAGUE);
    FIELD_LIST.add(HOME_PAGE);
    FIELD_LIST.add(PERSONAL_ID);
    FIELD_LIST.add(STREET_NAME);
    FIELD_LIST.add(POSTAL_CODE_ID);
    FIELD_LIST.add(FIRST_PHONE);
    FIELD_LIST.add(SECOND_PHONE);
    FIELD_LIST.add(FAX);
    FIELD_LIST.add(EMAIL);
  }
  
  private boolean editable = true;
  private boolean isReadOnly = false;
  
  public WorkReportDivisionBoardEditor() {
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
  
  protected void addBreakLine() {};
  
  private String parseAction(IWContext iwc) {
    String action = "";
    if (iwc.isParameterSet(SUBMIT_FINISH_KEY))  {
      setWorkReportAsFinished(true, iwc);
      return action;
    }
    
    if (iwc.isParameterSet(SUBMIT_REOPEN_KEY))  {
      setWorkReportAsFinished(false, iwc);
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
//    if (iwc.isParameterSet(SUBMIT_DELETE_ENTRIES_KEY)) {
//      List entriesToDelete = CheckBoxConverter.getResultByParsingUsingDefaultKey(iwc);
//      if (! entriesToDelete.isEmpty())  {
//        deleteWorkReportDivisionBoard(entriesToDelete, iwc);
//        // !! do nothing else !!
//        // do not modify entry
//        // do not create an entry
//        return action;
//      }
//    }
//    // does the user want to edit a new entry?
//
//    if (iwc.isParameterSet(SUBMIT_CREATE_NEW_ENTRY_KEY))  {
//      action = ACTION_SHOW_NEW_ENTRY;
//    }  
//    // does the user want to save a new entry?
//    if (iwc.isParameterSet(SUBMIT_SAVE_NEW_ENTRY_KEY))  {
//      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
//      WorkReportDivisionBoard board = createWorkReportDivisionBoard();
//      Iterator iterator = FIELD_LIST.iterator();
//      while (iterator.hasNext())  {
//        String field = (String) iterator.next();
//        EntityPathValueContainer entityPathValueContainerFromTextEditor = 
//          TextEditorConverter.getResultByEntityIdAndEntityPathShortKey(NEW_ENTRY_ID_VALUE, field, iwc);
//        EntityPathValueContainer entityPathValueContainerFromDropDownMenu = 
//          DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(NEW_ENTRY_ID_VALUE, field, iwc);
//        if (entityPathValueContainerFromTextEditor.isValid()) {
//          setValuesOfWorkReportDivisionBoard(entityPathValueContainerFromTextEditor, board, workReportBusiness);
//        }
//        if (entityPathValueContainerFromDropDownMenu.isValid()) {
//          setValuesOfWorkReportDivisionBoard(entityPathValueContainerFromDropDownMenu, board, workReportBusiness);
//        }
//      }
//      board.store();
//    }  
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
      WorkReportDivisionBoard board = findWorkReportDivisionBoard(primaryKey, iwc);
            Iterator iterator = FIELD_LIST.iterator();
      while (iterator.hasNext())  {
        String field = (String) iterator.next();
        EntityPathValueContainer entityPathValueContainerFromTextEditor = 
          TextEditorConverter.getResultByEntityIdAndEntityPathShortKey(primaryKey, field, iwc);
        EntityPathValueContainer entityPathValueContainerFromDropDownMenu = 
          DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(primaryKey, field, iwc);
        if (entityPathValueContainerFromTextEditor.isValid()) {
          setValuesOfWorkReportDivisionBoard(entityPathValueContainerFromTextEditor, board, workReportBusiness);
        }
        if (entityPathValueContainerFromDropDownMenu.isValid()) {
          setValuesOfWorkReportDivisionBoard(entityPathValueContainerFromDropDownMenu, board, workReportBusiness);
        }
      }
      boolean wasChecked = board.hasNationalLeague();
      boolean isChecked = CheckBoxConverter.isEntityCheckedUsingDefaultKey(iwc, primaryKey);
      if (wasChecked ^ isChecked) {
        board.setHasNationalLeague(isChecked);
      }
      board.store();
      return action;
    }  
//    // does the user want to modify an existing entity?
//    EntityPathValueContainer entityPathValueContainerFromTextEditor = TextEditorConverter.getResultByParsing(iwc);
//    EntityPathValueContainer entityPathValueContainerFromDropDownMenu = DropDownMenuConverter.getResultByParsing(iwc);
//    if (entityPathValueContainerFromTextEditor.isValid()) {
//      updateWorkReportDivisionBoard(entityPathValueContainerFromTextEditor, iwc);
//    }
//    if (entityPathValueContainerFromDropDownMenu.isValid()) {
//      updateWorkReportDivisionBoard(entityPathValueContainerFromDropDownMenu, iwc);
//    }
    return action;
  }
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle, Form form, String action) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    WorkReport workReport = null;
    try {
      int workReportId = getWorkReportId();
      workReport = workReportBusiness.getWorkReportById(workReportId); 
      // should the data be editable?
      isReadOnly = workReportBusiness.isWorkReportReadOnly(workReportId);
      editable = !( isReadOnly || workReport.isBoardPartDone());
    }
    catch (RemoteException ex) {
      String message =
        "[WorkReportDivisionBoardEditor]: Can't retrieve WorkReportBusiness.";
      System.err.println(message + " Message is: " + ex.getMessage());
      ex.printStackTrace(System.err);
      throw new RuntimeException(message);
    }
    Collection coll;
    Collection leagues;
//    Set referencedLeagues = new HashSet();
    try {
      int workReportId = getWorkReportId();
      coll = workReportBusiness.getAllWorkReportDivisionBoardForWorkReportId(workReportId);
      leagues = workReportBusiness.getLeaguesOfWorkReportById(workReportId);
    } 
    catch (Exception e) {
      System.err.println("[WorkReportDivisionBoardEditor] Can't get members. Message was: "+ e.getMessage());
      e.printStackTrace(System.err);
      coll = new ArrayList();
      leagues = new ArrayList();
    }
    // create new entities
    List list = new ArrayList();
    Iterator iterator = coll.iterator();
    while (iterator.hasNext())  {
      WorkReportDivisionBoard board = (WorkReportDivisionBoard) iterator.next();
      WorkReportGroup league = null;
      try {
        league = board.getLeague();
      }
      catch (IDOException ex) {
        System.err.println(
          "[WorkReportDivisionBoardEditor]: Can't retrieve league. Message is: "
            + ex.getMessage());
        ex.printStackTrace(System.err);
        throw new RuntimeException("[WorkReportDivsionBoardEditor]: Can't retrieve league.");
      }
      String leagueName;
      if (league == null) {
        leagueName = "error: no league defined";
      }
      else {
        leagueName = league.getShortName();
      }
//      referencedLeagues.add(league.getPrimaryKey());
      WorkReportDivisionBoardHelper helper = new WorkReportDivisionBoardHelper(leagueName, board);
      list.add(helper);
    }
    // create missing entities
//    Iterator leagueIterator = leagues.iterator();
//    while (leagueIterator.hasNext())  {
//      WorkReportGroup workReportGroup = (WorkReportGroup) leagueIterator.next();
//      Integer pk = (Integer) workReportGroup.getPrimaryKey();
//      if (! referencedLeagues.contains(pk)) {
//        WorkReportDivisionBoard board = createWorkReportDivisionBoard(pk.intValue());
//        String leagueName = workReportGroup.getName();
//        WorkReportDivisionBoardHelper helper = new WorkReportDivisionBoardHelper(leagueName, board);
//        list.add(helper);
//      }
//   }         
//    // add a value holder for a new entry if desired
//    if (ACTION_SHOW_NEW_ENTRY.equals(action)) {
//      EntityValueHolder valueHolder = new EntityValueHolder(); 
//      // trick , because postal code is a "foreign" column 
//      valueHolder.setColumnValue("POSTAL_CODE_ID", valueHolder); 
//      WorkReportDivisionBoardHelper valueHolderHelper = new WorkReportDivisionBoardHelper("", valueHolder);
//      list.add(valueHolderHelper);
//    }
    // sort list
    Comparator comparator = new Comparator()  {
      public int compare(Object first, Object second) {
        // check if the element is a new entity, a new entity should be shown first
        // the element is a new element if the primaryKey is less than zero.
        Integer primaryKey = (Integer) ((WorkReportDivisionBoardHelper) first).getPrimaryKey();
        if (primaryKey.intValue() < 0) {
          return -1;
        }
        primaryKey = (Integer) ((WorkReportDivisionBoardHelper) second).getPrimaryKey();
        if (primaryKey.intValue() < 0)  {
          return 1;
        }
        // sort according to the name of the league 
        String firstLeague = (String) ((WorkReportDivisionBoardHelper) first).getColumnValue(LEAGUE);
        String secondLeague = (String) ((WorkReportDivisionBoardHelper) second).getColumnValue(LEAGUE);
        int result = firstLeague.compareTo(secondLeague);
        // if the leagues are equal sort according to the names
        if (result == 0) {
          String firstName = (String) ((WorkReportDivisionBoardHelper) first).getColumnValue("NAME");
          String secondName = (String) ((WorkReportDivisionBoardHelper) second).getColumnValue("NAME");
          firstName = (firstName == null) ? "" : firstName;
          secondName = (secondName == null) ? "" : secondName;
          return firstName.compareTo(secondName);
        }
        return result;
      }
    };
    Collections.sort(list, comparator);
    EntityBrowser browser = getEntityBrowser(list, resourceBundle, form);
    // put browser into a table
    // do not show the buttons if not editable
    if (! isReadOnly) {
      Table mainTable = new Table(1,2);
      mainTable.add(browser, 1,1);
      mainTable.setCellspacing(0);
      mainTable.setCellpadding(0);
      if (editable) {
        mainTable.add(getFinishButton(resourceBundle), 1, 2);
      }
      else {
//        Text text = new Text(resourceBundle.getLocalizedString("wr_division_board_editor_board_part_finished", "Board part has been finished."));
//        text.setBold();
        mainTable.add(getReopenButton(resourceBundle), 1,2);
      }
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
			Text readOnlyText = new Text(resourceBundle.getLocalizedString("WorkReportDivisionBoardEditor.report_is_read_only", "The report is read only"));
			readOnlyTable.add(readOnlyText,1,1);
			mainTable.add(readOnlyTable,1,2);
			return mainTable;
		}
    return browser;    
  }
  
  private PresentationObject getFinishButton(IWResourceBundle resourceBundle) {
    String finishText = resourceBundle.getLocalizedString("wr_division_board_editor_finish", "Finish");
    SubmitButton button = new SubmitButton(finishText, SUBMIT_FINISH_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }
  
  private PresentationObject getReopenButton(IWResourceBundle resourceBundle) {
    String reopenText = resourceBundle.getLocalizedString("wr_division_board_editor_reopen", "Reopen");
    SubmitButton button = new SubmitButton(reopenText, SUBMIT_REOPEN_KEY, "dummy_value");
    button.setAsImageButton(true);
    return button;
  }
 
  private EntityBrowser getEntityBrowser(Collection entities, IWResourceBundle resourceBundle, Form form)  {
    // define converter
//    CheckBoxConverter checkBoxConverter = new CheckBoxConverter();
    TextEditorConverter textEditorConverter = new TextEditorConverter(form);
    textEditorConverter.maintainParameters(this.getParametersToMaintain());
    EditOkayButtonConverter okayConverter = new EditOkayButtonConverter();
    okayConverter.maintainParameters(this.getParametersToMaintain());
    EntityToPresentationObjectConverter textConverter = new WorkReportTextConverter();
    DropDownMenuConverter dropDownPostalCodeConverter = getConverterForPostalCode(form);
    WorkReportHasNationalLeagueConverter nationalLeagueConverter = new WorkReportHasNationalLeagueConverter();
    nationalLeagueConverter.maintainParameters(this.getParametersToMaintain());
    
    
    // define if the converters should be editable or not
//    checkBoxConverter.setEditable(editable);
    textEditorConverter.setEditable(editable);
    dropDownPostalCodeConverter.setEditable(editable);
    nationalLeagueConverter.setEditable(editable);
    // WorkReportTextConverter is not an editor 
    // define path short keys and map corresponding converters
    Object[] columns = {
      "okay", okayConverter,
      LEAGUE, textConverter,
      HAS_NATIONAL_LEAGUE, nationalLeagueConverter,
      HOME_PAGE, textEditorConverter,
      PERSONAL_ID, textEditorConverter,
      STREET_NAME, textEditorConverter,
      POSTAL_CODE_ID, dropDownPostalCodeConverter,
      FIRST_PHONE, textEditorConverter,
      SECOND_PHONE, textEditorConverter,
      FAX, textEditorConverter,
      EMAIL, textEditorConverter};
    EntityBrowser browser = EntityBrowser.getInstanceUsingEventSystemAndExternalForm();
    browser.setLeadingEntity(WorkReportDivisionBoard.class);
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    if( entities!=null && !entities.isEmpty()) { 
    	browser.setDefaultNumberOfRows(entities.size());
    }
    for (int i = 0; i < columns.length; i+=2) {
      String column = (String) columns[i];
      EntityToPresentationObjectConverter converter = (EntityToPresentationObjectConverter) columns[i+1];
      browser.setMandatoryColumn(i, column);
      browser.setEntityToPresentationConverter(column, converter);
    }
    browser.setEntities("dummy_string", entities);
    browser.setCellpadding(2);
    browser.setCellspacing(0);
    browser.setBorder(0);
    browser.setColorForEvenRows("#EFEFEF");
    browser.setColorForOddRows("#FFFFFF");
    browser.setColorForHeader("#DFDFDF");
    return browser;
  }
  
  
//  /**
//   * Converter for league column 
//   */
//  private DropDownMenuConverter getConverterForLeague(final IWResourceBundle resourceBundle, Form form) {
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
//          coll = business.getLeaguesOfWorkReportById(getWorkReportId());
//        }
//        catch (Exception ex) {
//          System.err.println(
//            "[WorkReportDivisionBoardEditor]: Can't retrieve WorkReportBusiness. Message is: "
//              + ex.getMessage());
//          ex.printStackTrace(System.err);
//          throw new RuntimeException("[WorkReportDivisionBoardEditor]: Can't retrieve WorkReportBusiness.");
//        }
//        SortedSet names = new TreeSet();
//        Iterator collIterator = coll.iterator();
//        while (collIterator.hasNext())  {
//          WorkReportGroup league = (WorkReportGroup) collIterator.next();
//          String name = league.getName(); 
//          // do not offer to create a board with the same league twice
//          if (! referencedLeagues.contains(name)) {
//            names.add(name);
//          }
//        }        
//        Iterator nameIterator = names.iterator();
//        while (nameIterator.hasNext())  {
//          String name = (String) nameIterator.next();
//          String display = resourceBundle.getLocalizedString(name, name);
//          optionMap.put(name, display);
//          }
//        }
//        return optionMap;
//      }
//    };     
//    converter.setOptionProvider(optionProvider); 
//    converter.maintainParameters(getParametersToMaintain());
//    return converter;
//  }  
  
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
  private void deleteWorkReportDivisionBoard(List ids, IWContext iwc) {
    Iterator iterator = ids.iterator();
    while (iterator.hasNext())  {
      Integer id = (Integer) iterator.next();
      WorkReportDivisionBoard board = findWorkReportDivisionBoard(id, iwc);
      if (board != null) {
        try {
          board.remove();
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
  private WorkReportDivisionBoard findWorkReportDivisionBoard(Integer primaryKey, IWContext iwc) {
    WorkReportDivisionBoard board = null;
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    try {
      board = workReportBusiness.getWorkReportDivisionBoardHome().findByPrimaryKey(primaryKey);
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
    return board;
  }
  
  // business method: create
  private WorkReportDivisionBoard createWorkReportDivisionBoard(int workReportGroupId)  {
    WorkReportDivisionBoard workReportDivisionBoard = null;
    try {
      workReportDivisionBoard =
        (WorkReportDivisionBoard) IDOLookup.create(WorkReportDivisionBoard.class);
    } catch (IDOLookupException e) {
      System.err.println("[WorkReportDivisonBoardEditor] Could not lookup home of WorkReportDivisionBoard. Message is: "+ e.getMessage());
      e.printStackTrace(System.err);
    } catch (CreateException e) {
      System.err.println("[WorkReportDivisionBoardEditor] Could not create new WorkReportDivisionBoard. Message is: "+ e.getMessage());
      e.printStackTrace(System.err);
    }
    workReportDivisionBoard.setReportId(getWorkReportId());
    workReportDivisionBoard.setLeague(workReportGroupId);
    workReportDivisionBoard.store();
    return workReportDivisionBoard;
  }

  private void setValuesOfWorkReportDivisionBoard(EntityPathValueContainer valueContainer, WorkReportDivisionBoard board, WorkReportBusiness workReportBusiness)  {
    String pathShortKey = valueContainer.getEntityPathShortKey();
    Object value = valueContainer.getValue();
    
    if (pathShortKey.equals(HOME_PAGE)) {
      board.setHomePage(value.toString());
    }
    else if (pathShortKey.equals(PERSONAL_ID))  {
      board.setPersonalId(value.toString());
    }
    else if (pathShortKey.equals(STREET_NAME))  {
      board.setStreetName(value.toString());
    }
    else if(pathShortKey.equals(POSTAL_CODE_ID))  {
      try {
        int postalCode = Integer.parseInt(value.toString());
        if (ConverterConstants.NULL_ENTITY_ID.intValue() == postalCode) {
          board.setPostalCode(null);
        }
        else {
          board.setPostalCodeID(postalCode);
        }
      }
      catch (NumberFormatException ex)  {
      }
    }
    else if(pathShortKey.equals(FIRST_PHONE))  {
      board.setFirstPhone(value.toString());
    }
    else if (pathShortKey.equals(SECOND_PHONE)) {
      board.setSecondPhone(value.toString());
    }
    else if (pathShortKey.equals(FAX))  {
      board.setFax(value.toString());
    }
    else if (pathShortKey.equals(EMAIL))  {
      board.setEmail(value.toString());
    }
//    else if (pathShortKey.equals(LEAGUE)) {
//      // special case, sometimes there is not a previous value
////      Object previousValue = valueContainer.getPreviousValue();
////      String oldWorkGroupName = (previousValue == null) ? null : previousValue.toString();
//      String newWorkReportGroupName = value.toString();
//      int year = getYear();
//      try {
//        
//        //workReportBusiness.changeWorkReportGroupOfEntity(getWorkReportId(), oldWorkGroupName, year, newWorkGroupName, year, board);
//        WorkReportGroup workReportGroup = workReportBusiness.findWorkReportGroupByNameAndYear(newWorkReportGroupName, year);
//        if (workReportGroup != null)  {
//          Integer pk = (Integer) workReportGroup.getPrimaryKey();
//          board.setWorKReportGroupID(pk.intValue());
//        }
//      }
//      catch (RemoteException ex) {
//        System.err.println(
//          "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
//            + ex.getMessage());
//        ex.printStackTrace(System.err);
//        throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
//      }
//    }
  }

  private void setWorkReportAsFinished(boolean setAsFinished, IWContext iwc)  {
    int workReportId = getWorkReportId();
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    try {
      WorkReport workReport = workReportBusiness.getWorkReportById(workReportId);
      workReport.setBoardPartDone(setAsFinished);
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

  class WorkReportTextConverter implements  EntityToPresentationObjectConverter  {    
    
    public PresentationObject getHeaderPresentationObject (
        EntityPath entityPath,
        EntityBrowser browser,
        IWContext iwc) {
      return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);   
    }
    
    public PresentationObject getPresentationObject(
        Object entity,
        EntityPath path,
        EntityBrowser browser,
        IWContext iwc) {
      String name = path.getShortKey();
      String value = ((EntityRepresentation) entity).getColumnValue(name).toString();
      return new Text(value);
    }
    
  }
  
  class WorkReportHasNationalLeagueConverter extends CheckBoxAsLinkConverter {
    
    protected boolean shouldEntityBeChecked(Object entity, Integer primaryKey) {
      WorkReportDivisionBoardHelper board = (WorkReportDivisionBoardHelper) entity;
      return  board.hasNationalLeague();
    }

  }

  /** 
   * WorkReportDivisionBoardHelper:
   * Inner class, represents a WorkReportDivisionBoard but with the league as additional value.
   *
   */ 
    
  class WorkReportDivisionBoardHelper implements EntityRepresentation {
    
    String league = null;
    Object board = null;
    
    public WorkReportDivisionBoardHelper()  {
    }
    
    public WorkReportDivisionBoardHelper(String league, Object board) {
      this.league = league;
      this.board = board;
    }
    
    public void setLeague(String league)  {
      this.league = league;
    }
    
    public void setMember(WorkReportDivisionBoard board) {
      this.board = board;
    }
    
    public Object getColumnValue(String columnName) {
      if (LEAGUE.equals(columnName))  {
        return league;
      }
      return ((EntityRepresentation) board).getColumnValue(columnName);
    }  
    
    public Object getPrimaryKey() {
      return ((EntityRepresentation) board).getPrimaryKey();
    }
    
    public boolean hasNationalLeague()  {
      return ((WorkReportDivisionBoard) board).hasNationalLeague();
    }
  }
}
