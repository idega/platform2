package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKey;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKeyHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecord;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecordHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converter.ConverterConstants;
import com.idega.block.entity.presentation.converter.editable.DropDownMenuConverter;
import com.idega.block.entity.presentation.converter.editable.EditOkayButtonConverter;
import com.idega.block.entity.presentation.converter.editable.TextEditorConverter;
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.datastructures.HashMatrix;

/**
 * <p>
 * Title: idegaWeb
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: idega Software
 * </p>
 * 
 * @author <a href="thomas@idega.is">Thomas Hilbig </a>
 * @version 1.0 Created on Aug 30, 2003
 */
public class WorkReportAccountEditor extends WorkReportSelector {

    private static final String STEP_NAME_LOCALIZATION_KEY = "workreportaccounteditor.step_name";

    //  private static final String SUBMIT_CREATE_NEW_ENTRY_KEY =
    // "submit_cr_new_entry_key";
    //  private static final String SUBMIT_DELETE_ENTRIES_KEY =
    // "submit_del_entry_key";
    private static final String SUBMIT_CANCEL_KEY = "submit_cancel_key";

    //  private static final String SUBMIT_SAVE_NEW_ENTRY_KEY =
    // "submit_sv_new_entry_key";
    private static final String SUBMIT_FINISH_KEY = "submit_finish_key";

    private static final String SUBMIT_REOPEN_KEY = "submit_reopen_key";

    private static final String LEAGUE_NAME = "FIN_league_name";

    //  private static final String ACTION_SHOW_NEW_ENTRY =
    // "action_show_new_entry";

    private static final String OKAY_BUTTON = "okayButton";

    private static final String INCOME = "income";

    private static final String EXPENSES = "expenses";

    private static final String ASSET = "asset";

    private static final String DEBT = "debt";

    private static final String EMPTY_COLUMN = "empty_column";

    private List specialFieldList;

    private Map specialFieldColorMap;

    private boolean editable = true;

    private boolean isReadOnly = false;

    private List leaguesWhereTheAccountIsOutOfBalance = null;

    private String errorMessageStyle = "errorMessage";

    // this number format is shared by the converters
    NumberFormat currencyNumberFormat = null;

    {
        specialFieldList = new ArrayList();

        specialFieldList.add(LEAGUE_NAME);
        specialFieldList.add(WorkReportConstants.INCOME_SUM_KEY);
        specialFieldList.add(WorkReportConstants.EXPENSES_SUM_KEY);
        specialFieldList.add(WorkReportConstants.INCOME_EXPENSES_SUB_SUM_KEY);
        specialFieldList.add(WorkReportConstants.INCOME_EXPENSES_SUM_KEY);
        specialFieldList.add(WorkReportConstants.ASSET_SUM_KEY);
        specialFieldList.add(WorkReportConstants.DEBT_SUM_KEY);
        for (int i = 0; i < WorkReportConstants.NOT_EDITABLE_FIN_NAMES.length; i++) {
            specialFieldList.add(WorkReportConstants.NOT_EDITABLE_FIN_NAMES[i]);
        }
    }

    {
        specialFieldColorMap = new HashMap();
        // green
        specialFieldColorMap.put(WorkReportConstants.INCOME_SUM_KEY, "#8AE588");
        specialFieldColorMap.put(WorkReportConstants.ASSET_SUM_KEY, "#8AE588");
        // red
        specialFieldColorMap.put(WorkReportConstants.EXPENSES_SUM_KEY,
                "#F89A8D");
        specialFieldColorMap.put(WorkReportConstants.DEBT_SUM_KEY, "#F89A8D");
        // blue
        for (int i = 0; i < WorkReportConstants.NOT_EDITABLE_FIN_NAMES.length; i++) {
            specialFieldColorMap.put(
                    WorkReportConstants.NOT_EDITABLE_FIN_NAMES[i], "#A1C2FA");
        }
        // yellow
        specialFieldColorMap.put(WorkReportConstants.INCOME_EXPENSES_SUM_KEY,
                "#FAFA46");
        specialFieldColorMap.put(WorkReportConstants.INCOME_EXPENSES_SUB_SUM_KEY,
        "#FAFA46");
        // black
        specialFieldColorMap.put(EMPTY_COLUMN, "#0000");
        specialFieldColorMap.put(OKAY_BUTTON, "#0000");
    }

    private List fieldList = new ArrayList();

    protected HashMatrix leagueKeyMatrix = new HashMatrix();

    protected Map accountKeyNameAccountKeyMap = new HashMap();

    private Map accountKeyNumberAccountKeyMap = new HashMap();

    private Map accountKeyPrimaryKeyAccountKeyMap = new HashMap();

    protected Map specialFieldAccountKeyIdsPlus = new HashMap();

    public WorkReportAccountEditor() {
        super();
        setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
    }

    public void main(IWContext iwc) throws Exception {
        super.main(iwc);
        IWResourceBundle resourceBundle = getResourceBundle(iwc);

        if (this.getWorkReportId() != -1) {
            //sets this step as bold, if another class calls it this will be
            // overwritten
            setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
            WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
            initializeAccountKeyData(workReportBusiness);
            initializeLeagueData(workReportBusiness);
            parseAction(iwc);
            Form form = new Form();
            PresentationObject pres = getContent(iwc, resourceBundle, form);
            form.maintainParameters(this.getParametersToMaintain());
            form.add(pres);
            add(form);
        }
    }

    protected void addBreakLine() {
    	// do nothing in this class
    }

    private String parseAction(IWContext iwc) {
        String action = "";
        // does the user want to close the report?
        if (iwc.isParameterSet(SUBMIT_FINISH_KEY)) {
            leaguesWhereTheAccountIsOutOfBalance = getLeaguesWhereTheAccountIsOutOfBalance();
            if (leaguesWhereTheAccountIsOutOfBalance.isEmpty()) {
                setWorkReportAsFinished(true, iwc);
            }
            return action;
        }
        // does the user want to reopen the report?
        if (iwc.isParameterSet(SUBMIT_REOPEN_KEY)) {
            setWorkReportAsFinished(false, iwc);
            return action;
        }
        // does the user want to cancel something?
        if (iwc.isParameterSet(SUBMIT_CANCEL_KEY)) { return action;
        // !! do nothing else !!
        // do not modify entry
        // do not create an entry
        // do not delete an entry
        }
        // does the user want to modify an existing entity?
        if (iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY)) {
            WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
            String id = iwc
                    .getParameter(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY);
            Integer groupId = null;
            try {
                groupId = new Integer(id);
            } catch (NumberFormatException ex) {
                System.err
                        .println("[WorkReportAccountEditor] Wrong primary key. Message is: "
                                + ex.getMessage());
                ex.printStackTrace(System.err);
            }
            Iterator iterator = fieldList.iterator();
            while (iterator.hasNext()) {
                String field = (String) iterator.next();
                EntityPathValueContainer entityPathValueContainerFromTextEditor = TextEditorConverter
                        .getResultByEntityIdAndEntityPathShortKey(groupId,
                                field, iwc);
                if (entityPathValueContainerFromTextEditor.isValid()) {
                    setValuesOfWorkReportClubAccountRecord(
                            entityPathValueContainerFromTextEditor, groupId,
                            workReportBusiness);
                }
            }
            // important: first save the fields, then change the league
            EntityPathValueContainer entityPathValueContainerFromDropDownMenu = DropDownMenuConverter
                    .getResultByEntityIdAndEntityPathShortKey(groupId,
                            LEAGUE_NAME, iwc);
            if (entityPathValueContainerFromDropDownMenu.isValid()) {
                setValuesOfWorkReportClubAccountRecord(
                        entityPathValueContainerFromDropDownMenu, groupId,
                        workReportBusiness);
            }
            return action;
        }

        return action;
    }

    private void initializeLeagueData(WorkReportBusiness workReportBusiness) {
        // collect all work report account records
        WorkReportClubAccountRecordHome workReportClubAccountRecordHome = null;
        Collection workReportClubAccountRecords = null;
        try {
            workReportClubAccountRecordHome = workReportBusiness
                    .getWorkReportClubAccountRecordHome();
        } catch (RemoteException ex) {
            System.err
                    .println("[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness. Message is: "
                            + ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException(
                    "[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness.");
        }
        try {
            workReportClubAccountRecords = workReportClubAccountRecordHome
                    .findAllRecordsByWorkReportId(workReportId);
        } catch (FinderException ex) {
            String message = "[WorkReportBoardAccountEditor]: Can't find WorkReportClubAccountRecordHome";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            workReportClubAccountRecords = new ArrayList();
        }

        Iterator workReportClubAccountRecordsIterator = workReportClubAccountRecords
                .iterator();
        while (workReportClubAccountRecordsIterator.hasNext()) {
            WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) workReportClubAccountRecordsIterator
                    .next();
            Integer groupId = new Integer(record.getWorkReportGroupId());
            Integer accountKey = new Integer(record.getAccountKeyId());
            leagueKeyMatrix.put(groupId, accountKey, record);
        }
        // validate parent child relations among the records
        Iterator leagueIterator = leagueKeyMatrix.firstKeySet().iterator();
        while (leagueIterator.hasNext()) {
            Map parentKeySum = new HashMap();
            Integer groupId = (Integer) leagueIterator.next();
            Map accountKeyRecordMap = leagueKeyMatrix.get(groupId);
            Iterator entryIterator = accountKeyRecordMap.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) entryIterator.next();
                Integer accountKeyId = (Integer) entry.getKey();
                WorkReportAccountKey accountKey = (WorkReportAccountKey) accountKeyPrimaryKeyAccountKeyMap
                        .get(accountKeyId);
                String parentKeyNumber = accountKey.getParentKeyNumber();
                // look up the parent account key
                if (parentKeyNumber != null) {
                    BigDecimal parentValue = (BigDecimal) parentKeySum
                            .get(parentKeyNumber);
                    if (parentValue == null) {
                        parentValue = new BigDecimal("0");
                    }
                    WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) entry
                            .getValue();
                    BigDecimal amount = new BigDecimal(Double.toString(record
                            .getAmount()));
                    BigDecimal result = parentValue.add(amount);
                    parentKeySum.put(parentKeyNumber, result);
                }
            }
            // store the new value of the parent records
            Iterator parentSumIterator = parentKeySum.entrySet().iterator();
            while (parentSumIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) parentSumIterator.next();
                String parentKeyNumber = (String) entry.getKey();
                BigDecimal result = (BigDecimal) entry.getValue();
                WorkReportAccountKey parent = (WorkReportAccountKey) accountKeyNumberAccountKeyMap
                        .get(parentKeyNumber);
                Integer primaryKeyOfParent = (Integer) parent.getPrimaryKey();
                createOrUpdateRecord(workReportBusiness, groupId,
                        primaryKeyOfParent, result);
            }
        }
    }

    private void initializeAccountKeyData(WorkReportBusiness workReportBusiness) {

        // income, exponses, asset and debt keys
        WorkReportAccountKeyHome accountKeyHome;
        try {
            accountKeyHome = workReportBusiness.getWorkReportAccountKeyHome();
        } catch (RemoteException ex) {
            String message = "[WorkReportAccountEditor]: Can't retrieve WorkReportAccountKeyHome.";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException(message);
        }
        List incomeKeys = null;
        List expensesKeys = null;
        List assetKeys = null;
        List debtKeys = null;
        try {
            incomeKeys = new ArrayList(accountKeyHome.findIncomeAccountKeys());
            expensesKeys = new ArrayList(accountKeyHome
                    .findExpensesAccountKeys());
            assetKeys = new ArrayList(accountKeyHome.findAssetAccountKeys());
            debtKeys = new ArrayList(accountKeyHome.findDeptAccountKeys());
        } catch (FinderException ex) {
            String message = "[WorkReportAccountEditor]: Can't find account keys.";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            incomeKeys = new ArrayList();
            expensesKeys = new ArrayList();
            assetKeys = new ArrayList();
            debtKeys = new ArrayList();
        }
        // define the columns
        Comparator keyComparator = new Comparator() {

            public int compare(Object first, Object second) {
                String firstParentKey = ((WorkReportAccountKey) first)
                        .getParentKeyNumber();
                String secondParentKey = ((WorkReportAccountKey) second)
                        .getParentKeyNumber();
                Integer firstKey = new Integer(((WorkReportAccountKey) first)
                        .getKeyNumber());
                Integer secondKey = new Integer(((WorkReportAccountKey) second)
                        .getKeyNumber());
                // Object: (key, parent key)
                if (firstParentKey == null && secondParentKey == null) {
                 // first Object: (34, null), second Object: (12, null): 34 < 12
                    return firstKey.compareTo(secondKey);
                } 
                else if (firstParentKey != null && secondParentKey != null) {
                    if (firstParentKey.equals(secondParentKey)) {
                    // firstObject: (76, 12), second Object: (65, 12): 76 < 65
                        return firstKey.compareTo(secondKey);
                    }
                     // firstObject: (13, 4), second Object: (76, 3): 4 < 3
                    Integer firstParent = new Integer(firstParentKey);
                    Integer secondParent = new Integer(secondParentKey);
                    return firstParent.compareTo(secondParent);
                } 
                else if (firstParentKey != null) { 
                	// and second parent key is now null
                    // first Object: (12, 3), second Object: (44, null): 3 < 44
                    Integer firstParent = new Integer(firstParentKey);
                    if (firstParent.equals(secondKey)) {
                    // if the parent is equal to the key, the object with the parent is less
                    	return -1;
                    }
                    return firstParent.compareTo(secondKey);
                } 
                else {
                    // first Object: (12, null), second Object: (44, 5): 12 < 5
                    Integer secondParent = new Integer(secondParentKey);
                    if (firstKey.equals(secondParent)) {
                    	// if the key is equal to the parent, the object with the parent is less
                    	return 1;
                    }
                    return firstKey.compareTo(secondParent);
                }
            }
        };
        // sort keys
        Collections.sort(incomeKeys, keyComparator);
        Collections.sort(expensesKeys, keyComparator);
        Collections.sort(assetKeys, keyComparator);
        Collections.sort(debtKeys, keyComparator);
        // add sorted keys to the fields
        fieldList.add(LEAGUE_NAME);
        addKeys(incomeKeys, INCOME);
        fieldList.add(WorkReportConstants.INCOME_SUM_KEY);
        fieldList.add(EMPTY_COLUMN);
        addKeys(expensesKeys, EXPENSES);
        fieldList.add(WorkReportConstants.EXPENSES_SUM_KEY);
        fieldList.add(EMPTY_COLUMN);
        fieldList.add(WorkReportConstants.INCOME_EXPENSES_SUM_KEY);
        fieldList.add(EMPTY_COLUMN);
        addKeys(assetKeys, ASSET);
        fieldList.add(WorkReportConstants.ASSET_SUM_KEY);
        fieldList.add(EMPTY_COLUMN);
        addKeys(debtKeys, DEBT);
        fieldList.add(WorkReportConstants.DEBT_SUM_KEY);

    }

    private PresentationObject getContent(IWContext iwc,
            IWResourceBundle resourceBundle, Form form) {
        int workReportIdTemp = getWorkReportId();
        WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
        WorkReport workReport = null;
        // get work report
        try {
            workReport = workReportBusiness.getWorkReportById(workReportIdTemp);
            isReadOnly = workReportBusiness.isWorkReportReadOnly(workReportIdTemp);
            editable = !(isReadOnly || workReport.isAccountPartDone());
        } catch (RemoteException ex) {
            String message = "[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness.";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException(message);
        }
        // get leagues of the workreport
        // needs to be converted to a list because of sorting
        List workReportLeagues;
        try {
            workReportLeagues = new ArrayList(workReportBusiness
                    .getLeaguesOfWorkReportById(workReportIdTemp));
        } catch (IDOException ex) {
            String message = "[WorkReportAccountEditor]: Can't retrieve leagues.";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            workReportLeagues = new ArrayList();
        } catch (RemoteException ex) {
            String message = "[WorkReportAccountEditor]: Can't retrieve leagues.";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException(message);
        }

        // sort league collection
        Comparator leagueComparator = new Comparator() {

            public int compare(Object first, Object second) {
                String firstName = ((WorkReportGroup) first).getShortName();
                String secondName = ((WorkReportGroup) second).getShortName();
                return firstName.compareTo(secondName);
            }
        };
        // sort leagues
        Collections.sort(workReportLeagues, leagueComparator);
        // create helper
        // iterate over leagues
        Collection workReportAccountGroupHelpers = new ArrayList();
        Iterator leagueIterator = workReportLeagues.iterator();
        SortedSet leagueNamesWhereTheAccountIsOutOfBalance = new TreeSet();
        while (leagueIterator.hasNext()) {
            WorkReportGroup group = (WorkReportGroup) leagueIterator.next();
            String groupName = group.getShortName();
            Integer groupId = (Integer) group.getPrimaryKey();
            if (leaguesWhereTheAccountIsOutOfBalance != null
                    && leaguesWhereTheAccountIsOutOfBalance.contains(groupId)) {
                leagueNamesWhereTheAccountIsOutOfBalance.add(groupName);
            }
            WorkReportAccountGroupHelper helper = new WorkReportAccountGroupHelper(
                    groupId, groupName);
            workReportAccountGroupHelpers.add(helper);
        }

        // add error message
        Table errorMessageTable = getErrorMessageTable();
        if (!leagueNamesWhereTheAccountIsOutOfBalance.isEmpty()) {
            String message = resourceBundle.getLocalizedString(
                    "wr_account_editor_account_out_of_balance",
                    "Account is out of balance");
            StringBuffer buffer = new StringBuffer(message);
            buffer.append(":");
            String comma = " ";
            Iterator nameIterator = leagueNamesWhereTheAccountIsOutOfBalance
                    .iterator();
            while (nameIterator.hasNext()) {
                buffer.append(comma);
                buffer.append((String) nameIterator.next());
                comma = ", ";
            }
            Text text = new Text(buffer.toString());
            //      text.setBold();
            //      text.setFontColor("#FF0000");
            errorMessageTable.add(text);
            add(errorMessageTable);
        }

        // define entity browser
        EntityBrowser browser = getEntityBrowser(workReportAccountGroupHelpers,
                resourceBundle, form);
        // put browser into a table
        if (!isReadOnly) {
            Table mainTable = new Table(1, 2);
            mainTable.setCellspacing(0);
            mainTable.setCellpadding(0);
            mainTable.add(browser, 1, 1);
            if (editable) {
                mainTable.add(getFinishButton(resourceBundle), 1, 2);
            } else {
                //        Text text = new
                // Text(resourceBundle.getLocalizedString("wr_account_editor_account_part_finished",
                // "Account part has been finished."));
                //        text.setBold();
                mainTable.add(getReopenButton(resourceBundle), 1, 2);
            }
            return mainTable;
        }
        //if the report is read only, then it is printed out:
        else if (isReadOnly) {
            Table mainTable = new Table(1, 2);
            mainTable.add(browser, 1, 1);
            mainTable.setCellspacing(0);
            mainTable.setCellpadding(0);
            Table readOnlyTable = new Table(1, 1);
            readOnlyTable.setCellspacing(0);
            readOnlyTable.setCellpadding(0);
            Text readOnlyText = new Text(resourceBundle.getLocalizedString(
                    "WorkReportAccountEditor.report_is_read_only",
                    "The report is read only"));
            readOnlyTable.add(readOnlyText, 1, 1);
            mainTable.add(readOnlyTable, 1, 2);
            return mainTable;
        }
        return browser;
    }

    public Table getErrorMessageTable() {
        Table errorMessageTable = new Table(1, 1);
        errorMessageTable.setCellpaddingAndCellspacing(0);
        errorMessageTable.setWidth(Table.HUNDRED_PERCENT);
        // errorMessageTable.setAlignment("center");
        errorMessageTable.setAlignment(1, 1, "center");
        errorMessageTable.setStyleClass(1, 1, errorMessageStyle);

        return errorMessageTable;
    }

    private void addKeys(List accountKeys, String accountArea) {
        List plus = new ArrayList();
        Iterator iterator = accountKeys.iterator();
        while (iterator.hasNext()) {
            WorkReportAccountKey key = (WorkReportAccountKey) iterator.next();
            String name = key.getKeyName();
            String keyNumber = key.getKeyNumber();
            Integer primaryKey = (Integer) key.getPrimaryKey();
            fieldList.add(name);
            accountKeyNameAccountKeyMap.put(name, key);
            accountKeyNumberAccountKeyMap.put(keyNumber, key);
            accountKeyPrimaryKeyAccountKeyMap.put(primaryKey, key);
            // do not add the values of the children
            if (key.getParentKeyNumber() == null) {
                plus.add(primaryKey);
            }
        }
        specialFieldAccountKeyIdsPlus.put(accountArea, plus);
    }

    private PresentationObject getFinishButton(IWResourceBundle resourceBundle) {
        String finishText = resourceBundle.getLocalizedString(
                "wr_account_editor_finish", "Finish");
        SubmitButton button = new SubmitButton(finishText, SUBMIT_FINISH_KEY,
                "dummy_value");
        button.setAsImageButton(true);
        return button;
    }

    private PresentationObject getReopenButton(IWResourceBundle resourceBundle) {
        String reopenText = resourceBundle.getLocalizedString(
                "wr_account_editor_reopen", "Reopen");
        SubmitButton button = new SubmitButton(reopenText, SUBMIT_REOPEN_KEY,
                "dummy_value");
        button.setAsImageButton(true);
        return button;
    }

    private EntityBrowser getEntityBrowser(Collection entities,
            IWResourceBundle resourceBundle, Form form) {
        EntityBrowser browser = EntityBrowser.getInstanceUsingEventSystemAndExternalForm();
        browser.setLeadingEntity(WorkReportClubAccountRecord.class);
        browser.setShowMirroredView(true);
        // no settings button
        browser.setAcceptUserSettingsShowUserSettingsButton(false, false);
        browser.setEntities("dummy_string", entities);
        if (entities != null && !entities.isEmpty()) {
            browser.setDefaultNumberOfRows(entities.size());
        }
        // define converter
        WorkReportAccountInputConverter textEditorConverter = new WorkReportAccountInputConverter(
                form, resourceBundle);
        EntityToPresentationObjectConverter textConverter = new WorkReportAccountTextConverter();
        EditOkayButtonConverter okayConverter = new EditOkayButtonConverter();
        EntityToPresentationObjectConverter emptyConverter = new EmptyConverter();
        textEditorConverter.maintainParameters(this.getParametersToMaintain());
        okayConverter.maintainParameters(this.getParametersToMaintain());

        // define if converters should be editable
        textEditorConverter.setEditable(editable);
        // textConverter does not offer any input fields
        // define path short keys and map corresponding converters
        int i = 1;
        Iterator fieldListIterator = fieldList.iterator();
        while (fieldListIterator.hasNext()) {
            String fieldName = fieldListIterator.next().toString();
            browser.setMandatoryColumn(i++, fieldName);
            browser.setAlignmentForColumn(fieldName,
                    Table.HORIZONTAL_ALIGN_RIGHT);
            EntityToPresentationObjectConverter converter;
            if (EMPTY_COLUMN.equals(fieldName)) {
                converter = emptyConverter;
            } else if (specialFieldList.contains(fieldName)) {
                converter = textConverter;
            } else {
                converter = textEditorConverter;
            }
            browser.setEntityToPresentationConverter(fieldName, converter);
        }
        browser.setMandatoryColumn(i++, OKAY_BUTTON);
        browser.setEntityToPresentationConverter(OKAY_BUTTON, okayConverter);
        browser.setCellpadding(1);
        browser.setCellspacing(0);
        browser.setBorder(1);
        browser.setColorForEvenRows("#EFEFEF");
        browser.setColorForOddRows("#FFFFFF");
        browser.setColorForHeader("#DFDFDF");
        browser.setColorForColumns(specialFieldColorMap);
        return browser;
    }

    // business method: set values (invoked by 'update' or 'create')
    private void setValuesOfWorkReportClubAccountRecord(
            EntityPathValueContainer valueContainer, Integer groupId,
            WorkReportBusiness workReportBusiness) {
        String pathShortKey = valueContainer.getEntityPathShortKey();
        Object value = valueContainer.getValue();
        BigDecimal amount;
        try {
            amount = new BigDecimal(value.toString());
        } catch (NumberFormatException ex) {
            String message = "[WorkReportAccountEditor]: Can't convert value to a number.";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            // give up
            return;
        }
        WorkReportAccountKey accountKey = (WorkReportAccountKey) accountKeyNameAccountKeyMap
                .get(pathShortKey);
        if (accountKey == null) {
            String message = "[WorkReportAccountEditor]: Can't find corresponding account key to pathShortKey.";
            System.err.println(message);
            // give up
            return;
        }
        Object previousValue = valueContainer.getPreviousValue();
        if (value.equals(previousValue)) {
        // nothing to do
        return; }
        BigDecimal oldValue;
        try {
            oldValue = new BigDecimal(previousValue.toString());
        } catch (NumberFormatException ex) {
            String message = "[WorkReportAccountEditor]: Can't restore previous value of account record.";
            System.err.println(message);
            // give up
            return;
        }
        Integer accountKeyId = (Integer) accountKey.getPrimaryKey();
        // check if the account key is a child of a parent account key
        String parentKeyNumber = accountKey.getParentKeyNumber();
        // look up the parent account key
        if (parentKeyNumber != null) {
            WorkReportAccountKey parentAccountKey = (WorkReportAccountKey) accountKeyNumberAccountKeyMap
                    .get(parentKeyNumber);
            if (parentAccountKey == null) {
                String message = "[WorkReportAccountEditor]: Can't find corresponding account key to pathShortKey.";
                System.err.println(message);
                // give up
                return;
            }
            // get amount of the parent
            Integer parentAccountKeyId = (Integer) parentAccountKey
                    .getPrimaryKey();
            WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) leagueKeyMatrix
                    .get(groupId, parentAccountKeyId);
            // calculate new parent value
            BigDecimal parentAmount = (record == null) ? new BigDecimal("0")
                    : new BigDecimal(Double.toString(record.getAmount()));
            parentAmount = parentAmount.add(amount.subtract(oldValue));
            // store parent and child in one transaction to avoid inconsistency
            TransactionManager tm = IdegaTransactionManager.getInstance();
            try {
                tm.begin();
                // child
                createOrUpdateRecord(workReportBusiness, groupId, accountKeyId,
                        amount);
                // parent
                createOrUpdateRecord(workReportBusiness, groupId,
                        parentAccountKeyId, parentAmount);
                tm.commit();
            } catch (Exception ex) {
                String message = "[WorkReportAccountEditor]: Can't store records.";
                System.err.println(message + " Message is: " + ex.getMessage());
                ex.printStackTrace(System.err);
                try {
                    tm.rollback();
                } catch (SystemException sysEx) {
                    String sysMessage = "[WorkReportAccountEditor]: Can't rollback.";
                    System.err.println(sysMessage + " Message is: "
                            + sysEx.getMessage());
                    sysEx.printStackTrace(System.err);
                }
            }
        } else {
            createOrUpdateRecord(workReportBusiness, groupId, accountKeyId,
                    amount);
        }
    }

    private void createOrUpdateRecord(WorkReportBusiness workReportBusiness,
            Integer groupId, Integer accountKeyId, BigDecimal amount) {
        WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) leagueKeyMatrix
                .get(groupId, accountKeyId);
        if (record == null) {
            // okay, first create a record
            WorkReportClubAccountRecordHome home;
            try {
                home = workReportBusiness.getWorkReportClubAccountRecordHome();
            } catch (RemoteException ex) {
                String message = "[WorkAccountEditor]: Can't retrieve WorkReportClubAccountRecordHome.";
                System.err.println(message + " Message is: " + ex.getMessage());
                ex.printStackTrace(System.err);
                throw new RuntimeException(message);
            }
            try {
                record = home.create();
                record.setReportId(getWorkReportId());
                record.setAccountKeyId(accountKeyId.intValue());
                record.setWorkReportGroupId(groupId.intValue());
                // add this new record to the matrix
                leagueKeyMatrix.put(groupId, accountKeyId, record);
            } catch (CreateException ex) {
                String message = "[WorkReportAccountEditor]: Can't create WorkreportClubAccountRecord.";
                System.err.println(message + " Message is: " + ex.getMessage());
                ex.printStackTrace(System.err);
                // give up
                return;
            }
        }
        record.setAmount(amount.doubleValue());
        // do not forget to store
        record.store();
    }

    private void setWorkReportAsFinished(boolean setAsFinished, IWContext iwc) {
        int workReportIdTemp = getWorkReportId();
        WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
        try {
            WorkReport workReport = workReportBusiness
                    .getWorkReportById(workReportIdTemp);
            workReport.setAccountPartDone(setAsFinished);
            workReport.store();
        } catch (RemoteException ex) {
            String message = "[WorkReportAccountEditor]: Can't retrieve WorkReportBusiness.";
            System.err.println(message + " Message is: " + ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException(message);
        }
    }

    private List getLeaguesWhereTheAccountIsOutOfBalance() {
        // assertion: league key matrix must be initialized
        List assetIds = (List) specialFieldAccountKeyIdsPlus.get(ASSET);
        List debtIds = (List) specialFieldAccountKeyIdsPlus.get(DEBT);
        List workReportGroupIdsOutOfBalance = new ArrayList();
        Iterator iterator = leagueKeyMatrix.firstKeySet().iterator();
        while (iterator.hasNext()) {
            Integer workReportGroupId = (Integer) iterator.next();
            Map keyRecordMap = leagueKeyMatrix.get(workReportGroupId);
            Iterator keyRecordIterator = keyRecordMap.entrySet().iterator();
            BigDecimal result = new BigDecimal("0");
            while (keyRecordIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) keyRecordIterator.next();
                Integer accountKey = (Integer) entry.getKey();
                WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) entry
                        .getValue();
                BigDecimal amount = new BigDecimal(Double.toString(record
                        .getAmount()));
                if (assetIds.contains(accountKey)) {
                    result = result.add(amount);
                } else if (debtIds.contains(accountKey)) {
                    result = result.subtract(amount);
                }
            }
            if (result.signum() != 0) {
                workReportGroupIdsOutOfBalance.add(workReportGroupId);
            }
        }
        return workReportGroupIdsOutOfBalance;
    }

    protected NumberFormat getCurrencyNumberFormat(IWContext iwc) {
        if (currencyNumberFormat == null) {
            Locale locale = iwc.getCurrentLocale();
            // special case: If we are in Iceland do not show the currency
            currencyNumberFormat = (locale.getCountry().equals("IS")) ? NumberFormat
                    .getNumberInstance(locale)
                    : NumberFormat.getCurrencyInstance(locale);
            // !!!!!!
            // do not show any digits after the decimal point
            // !!!!!!
            currencyNumberFormat.setMaximumFractionDigits(0);
        }
        return currencyNumberFormat;
    }

    /**
     * WorkReportAccountGroupHelper:
     *  
     */
    class WorkReportAccountGroupHelper implements EntityRepresentation {

        Integer groupId;

        String groupName;

        public WorkReportAccountGroupHelper() {
        	// default constructor
        }

        public WorkReportAccountGroupHelper(Integer groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
        }

        // returns either instance of String or instance of BigDecimal
        public Object getEntry(String accountKeyName) {
            WorkReportAccountKey accountKey = (WorkReportAccountKey) accountKeyNameAccountKeyMap
                    .get(accountKeyName);
            if (accountKey == null) { return getSpecialValues(accountKeyName); }
            Integer id = (Integer) accountKey.getPrimaryKey();
            WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) leagueKeyMatrix
                    .get(groupId, id);
            // sometimes the record does not exist yet
            if (record == null) { return new BigDecimal("0"); }
            double amount = record.getAmount();
            return new BigDecimal(Double.toString(amount));
        }

        public int getGroupId() {
            return groupId.intValue();
        }

        public Object getColumnValue(String columnName) {
            return getEntry(columnName);
        }

        public Object getPrimaryKey() {
            return new Integer(getGroupId());
        }

        private Object getSpecialValues(String accountKeyName) {
            if (accountKeyName.equals(LEAGUE_NAME)) {
                return groupName;
            } else if (accountKeyName
                    .equals(WorkReportConstants.INCOME_SUM_KEY)) {
                BigDecimal result = calculateAccountArea(INCOME);
                return result;
            } else if (accountKeyName
                    .equals(WorkReportConstants.EXPENSES_SUM_KEY)) {
                BigDecimal result = calculateAccountArea(EXPENSES);
                return result;

            } else if (accountKeyName
                    .equals(WorkReportConstants.INCOME_EXPENSES_SUM_KEY)) {
                BigDecimal income = calculateAccountArea(INCOME);
                BigDecimal expenses = calculateAccountArea(EXPENSES);
                BigDecimal result = income.subtract(expenses);
                return result;
            } else if (accountKeyName.equals(WorkReportConstants.ASSET_SUM_KEY)) {
                BigDecimal result = calculateAccountArea(ASSET);
                return result;
            } else if (accountKeyName.equals(WorkReportConstants.DEBT_SUM_KEY)) {
                BigDecimal result = calculateAccountArea(DEBT);
                return result;
            } else {
                return "unknown";
            }
        }

        private BigDecimal calculateAccountArea(String accountArea) {
            List plusIds = (List) specialFieldAccountKeyIdsPlus
                    .get(accountArea);
            BigDecimal plus = addRecords(plusIds);
            return plus;
        }

        private BigDecimal addRecords(List accountKeyIds) {
            BigDecimal sum = new BigDecimal("0");
            Iterator iterator = accountKeyIds.iterator();
            while (iterator.hasNext()) {
                Integer primaryKey = (Integer) iterator.next();
                WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) leagueKeyMatrix
                        .get(groupId, primaryKey);
                if (record != null) {
                    BigDecimal amount = new BigDecimal(Double.toString(record
                            .getAmount()));
                    sum = sum.add(amount);
                }
            }
            return sum;
        }

    }

    /**
     *  
     */
    class WorkReportAccountInputConverter extends TextEditorConverter {
        public WorkReportAccountInputConverter(Form form,
                IWResourceBundle resourceBundle) {
            super(form);
            String message = resourceBundle.getLocalizedString(
                    "wr_account_editor_message_entry_is_not_a_number",
                    "The input is not a valid number.");
            setAsDouble(message);
        }

        protected Object getValueForInput(Object entity, EntityPath path,
                EntityBrowser browser, IWContext iwc) {
            String name = path.getShortKey();
            BigDecimal value = (BigDecimal) ((EntityRepresentation) entity)
                    .getColumnValue(name);
            return value.toString();
        }

        protected Object getValueForLink(Object entity, EntityPath path,
                EntityBrowser browser, IWContext iwc) {
            String name = path.getShortKey();
            BigDecimal value = (BigDecimal) ((EntityRepresentation) entity)
                    .getColumnValue(name);
            NumberFormat numberFormat = getCurrencyNumberFormat(iwc);
            String resultString = numberFormat.format(value);
            return resultString;
        }
    }

    class WorkReportAccountTextConverter implements
            EntityToPresentationObjectConverter {
        public PresentationObject getHeaderPresentationObject(
                EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
            return browser.getDefaultConverter().getHeaderPresentationObject(
                    entityPath, browser, iwc);
        }

        public PresentationObject getPresentationObject(Object entity,
                EntityPath path, EntityBrowser browser, IWContext iwc) {
            String resultString;
            String name = path.getShortKey();
            Object value = ((EntityRepresentation) entity).getColumnValue(name);
            if (LEAGUE_NAME.equals(name)) {
                resultString = value.toString();
            } else {
                BigDecimal numberValue = (BigDecimal) value;
                NumberFormat numberFormat = getCurrencyNumberFormat(iwc);
                resultString = numberFormat.format(numberValue);
            }
            Text text = new Text(resultString);
            text.setBold();
            return text;
        }
    }

    class EmptyConverter implements EntityToPresentationObjectConverter {
        public PresentationObject getHeaderPresentationObject(
                EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
            return new Text(" ");
        }

        public PresentationObject getPresentationObject(Object entity,
                EntityPath path, EntityBrowser browser, IWContext iwc) {
            return new Text(" ");
        }
    }

}

