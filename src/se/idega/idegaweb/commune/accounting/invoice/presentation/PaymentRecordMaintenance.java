package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import se.idega.idegaweb.commune.accounting.export.business.ExportBusiness;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.business.BillingThread;
import se.idega.idegaweb.commune.accounting.invoice.business.CheckAmountBusiness;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceStrings;
import se.idega.idegaweb.commune.accounting.invoice.business.PaymentSummary;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRule;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolUserBMPBean;
import com.idega.block.school.data.SchoolUserHome;
import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.io.MemoryFileBuffer;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.LocaleUtil;
import com.lowagie.text.DocumentException;

/**
 * PaymentRecordMaintenance is an IdegaWeb block were the user can search, view
 * and edit payment records.
 * <p>
 * Last modified: $Date: 2004/05/12 16:00:58 $ by $Author: roar $
 * 
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg </a>
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson </a>
 * @version $Revision: 1.110 $
 * @see com.idega.presentation.IWContext
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.data
 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock
 */
public class PaymentRecordMaintenance extends AccountingBlock implements
        InvoiceStrings {

    private static final String ACTION_KEY = PREFIX + "action_key";

    private static final String LAST_ACTION_KEY = PREFIX + "last_action_key";

    private static final int ACTION_SHOW_PAYMENT = 0,
            ACTION_SHOW_RECORD_DETAILS = 1, ACTION_SHOW_EDIT_RECORD_FORM = 2,
            ACTION_SHOW_RECORD = 3, ACTION_SAVE_RECORD = 4,
            ACTION_REMOVE_RECORD = 5,
            ACTION_GENERATE_CHECK_AMOUNT_LIST_PDF = 6;

    private static final NumberFormat integerFormatter = NumberFormat
            .getIntegerInstance(LocaleUtil.getSwedishLocale());

    private static final SimpleDateFormat periodFormatter = new SimpleDateFormat(
            "yyMM");

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "yyyy-MM-dd");

    private ICPage providerAuthorizationPage = null;

    private ICPage createPaymentPage = null;

    /**
     * Init is the event handler of InvoiceCompilationEditor
     * 
     * @param context
     *            session data like user info etc.
     */
    public void init(final IWContext context) {
        try {
            if (null != getSchoolByLoggedInUser(context)
                    && !isSchoolUserAllowed(context)) {
                add(getSmallText(localize(
                        YOU_DONT_HAVE_ACCESS_TO_THIS_FUNCTION_KEY,
                        YOU_DONT_HAVE_ACCESS_TO_THIS_FUNCTION_DEFAULT)));
                return;
            }

            int actionId = ACTION_SHOW_PAYMENT;

            try {
                if (!hasChangedProvider(context)) {
                    actionId = Integer.parseInt(context
                            .getParameter(ACTION_KEY));
                }
            } catch (final Exception dummy) {
                try {
                    actionId = Integer.parseInt(context
                            .getParameter(LAST_ACTION_KEY));
                } catch (final Exception dummy2) {
                    // do nothing, actionId is default
                }
            }

            switch (actionId) {
            case ACTION_SHOW_RECORD_DETAILS:
                showRecordDetails(context);
                break;

            case ACTION_SHOW_EDIT_RECORD_FORM:
                showEditRecordForm(context);
                break;

            case ACTION_SHOW_RECORD:
                showRecord(context);
                break;

            case ACTION_SAVE_RECORD:
                saveRecord(context);
                break;

            case ACTION_REMOVE_RECORD:
                removeRecord(context);
                break;

            case ACTION_GENERATE_CHECK_AMOUNT_LIST_PDF:
                generateCheckAmountListPdf(context);
                break;

            default:
                showPayment(context);
                break;
            }

        } catch (Exception exception) {
            logUnexpectedException(context, exception);
        }
    }

    private void generateCheckAmountListPdf(final IWContext context)
            throws RemoteException, DocumentException, FinderException {
        Link link1 = getCheckAmountListLink(context, localize(
                CHECK_AMOUNT_LIST_WITHOUT_POSTING_KEY,
                CHECK_AMOUNT_LIST_WITHOUT_POSTING_DEFAULT), false);
        Link link2 = getCheckAmountListLink(context, localize(
                CHECK_AMOUNT_LIST_WITH_POSTING_KEY,
                CHECK_AMOUNT_LIST_WITH_POSTING_DEFAULT), true);
        link2.addParameter(POSTING_KEY, true + "");
        int row = 1;
        final Table htmlTable = createTable(1);
        htmlTable.add("<li>", 1, row);
        htmlTable.add(link1, 1, row++);
        htmlTable.add("<li>", 1, row);
        htmlTable.add(link2, 1, row++);

        htmlTable.setHeight(row++, 12);
        addCancelButton(htmlTable, 1, row++, ACTION_SHOW_PAYMENT);
        final Form form = new Form();
        form.maintainParameter(PROVIDER_KEY);
        form.setOnSubmit("return checkInfoForm()");
        form.add(htmlTable);
        final Table formTable = createTable(1);
        formTable.add(form, 1, 1);
        add(createMainTable(CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT,
                formTable));
    }

    private Link getCheckAmountListLink(final IWContext context,
            final String linkText, final boolean isShowPosting)
            throws RemoteException, DocumentException, FinderException {
        final Integer providerId = getProviderIdParameter(context);
        final Date startPeriod = getPeriodParameter(context, START_PERIOD_KEY);
        final Date endPeriod = getPeriodParameter(context, END_PERIOD_KEY);
        final String schoolCategoryId = getSession().getOperationalField();
        final MemoryFileBuffer buffer = getCheckAmountBusiness()
                .getInternalCheckAmountListBuffer(schoolCategoryId, providerId,
                        startPeriod, endPeriod, isShowPosting);
        final int fileId = getInvoiceBusiness().generatePdf(linkText, buffer);
        final Link link = new Link(linkText);
        link.setFile(fileId);
        link.setTarget("check_amount_list_window_" + fileId);
        return link;
    }

    private void removeRecord(final IWContext context) throws RemoteException,
            FinderException, RemoveException {
        // find payment record
        final PaymentRecord record = getPaymentRecord(context);

        // remove record
        getInvoiceBusiness().removePaymentRecord(record);

        // re-render
        showPayment(context);
    }

    private void saveRecord(final IWContext context) throws RemoteException,
            FinderException {
        // get updated values
        final User currentUser = context.getCurrentUser();
        final Integer amount = getIntegerParameter(context, AMOUNT_KEY);
        final Integer placementCount = getIntegerParameter(context,
                NUMBER_OF_PLACEMENTS_KEY);
        final Integer pieceAmount = getIntegerParameter(context,
                PIECE_AMOUNT_KEY);
        final Integer vatAmount = getIntegerParameter(context, VAT_AMOUNT_KEY);

        final Date period = getPeriodParameter(context, PERIOD_KEY);
        final String doublePosting = getPostingString(context,
                DOUBLE_POSTING_KEY);
        final String ownPosting = getPostingString(context, OWN_POSTING_KEY);
        final String paymentText = context.getParameter(PAYMENT_TEXT_KEY);
        final String note = context.getParameter(NOTE_KEY);
        final String regulationSpecType = context
                .getParameter(REGULATION_SPEC_TYPE_KEY);
        final Integer vatRule = getIntegerParameter(context, VAT_RULE_KEY);
        final PaymentRecord record = getPaymentRecord(context);

        // set updated values
        if (null != amount) record.setTotalAmount(amount.floatValue());
        if (null != placementCount)
                record.setPlacements(placementCount.intValue());
        if (null != pieceAmount)
                record.setPieceAmount(pieceAmount.floatValue());
        if (null != vatAmount)
                record.setTotalAmountVAT(vatAmount.floatValue());
        record.setChangedBy(getSignature(currentUser));
        record.setDateChanged(new Date(System.currentTimeMillis()));
        if (null != period) record.setPeriod(period);
        if (null != doublePosting) record.setDoublePosting(doublePosting);
        if (null != ownPosting) record.setOwnPosting(ownPosting);
        if (null != paymentText) record.setPaymentText(paymentText);
        if (null != note) record.setNotes(note);
        record.setRuleSpecType(regulationSpecType);
        if (null != vatRule && vatRule.intValue() > 0) {
            record.setVATRuleRegulationId(vatRule.intValue());
        }
        // store updated record
        record.store();

        // re-render
        showPayment(context);
    }

    private void showEditRecordForm(final IWContext context)
            throws RemoteException, FinderException {
        final PaymentRecord record = getPaymentRecord(context);
        final java.util.Map map = new java.util.HashMap();
        map.put(ADJUSTED_SIGNATURE_KEY,
                getSmallSignature(record.getChangedBy()));
        addSmallText(map, ADJUSTED_SIGNATURE_KEY, record.getChangedBy());
        addStyledInput(map, AMOUNT_KEY, record.getTotalAmount());
        map
                .put(CREATED_SIGNATURE_KEY, getSmallSignature(record
                        .getCreatedBy()));
        addSmallText(map, DATE_ADJUSTED_KEY, record.getDateChanged());
        addSmallText(map, DATE_CREATED_KEY, record.getDateCreated());
        addStyledWideInput(map, NOTE_KEY, record.getNotes());
        addStyledWideInput(map, PAYMENT_TEXT_KEY, record.getPaymentText());
        map.put(PERIOD_KEY, getStyledInput(PERIOD_KEY,
                getFormattedPeriod(record.getPeriod())));
        addStyledInput(map, PIECE_AMOUNT_KEY, record.getPieceAmount());
        addStyledInput(map, NUMBER_OF_PLACEMENTS_KEY, record.getPlacements());
        addSmallText(map, STATUS_KEY, record.getStatus() + "");
        addSmallText(map, TRANSACTION_DATE_KEY, record.getDateTransaction());
        addStyledInput(map, VAT_AMOUNT_KEY, record.getTotalAmountVAT());

        final InvoiceBusiness business = getInvoiceBusiness();
        final DropdownMenu regulationSpecTypeDropdown = getLocalizedDropdown(business
                .getAllRegulationSpecTypes());
        map.put(REGULATION_SPEC_TYPE_KEY, regulationSpecTypeDropdown);
        final String regulationSpecType = record.getRuleSpecType();
        if (null != regulationSpecType) {
            regulationSpecTypeDropdown.setSelectedElement(regulationSpecType);
        }
        final PresentationObject ownPostingForm = getPostingParameterForm(
                OWN_POSTING_KEY, record.getOwnPosting());
        map.put(OWN_POSTING_KEY, ownPostingForm);
        final PresentationObject doublePostingForm = getPostingParameterForm(
                DOUBLE_POSTING_KEY, record.getDoublePosting());
        map.put(DOUBLE_POSTING_KEY, doublePostingForm);
        final DropdownMenu vatRuleDropdown = getLocalizedDropdownForVAT(business
                .getAllVATRuleRegulations());
        map.put(VAT_RULE_KEY, vatRuleDropdown);
        final int vatRuleRegulationId = record.getVATRuleRegulationId();
        if (0 < vatRuleRegulationId) {
            vatRuleDropdown.setSelectedElement(vatRuleRegulationId + "");
        } else {
            vatRuleDropdown.addMenuElement("-1", "");
            vatRuleDropdown.setSelectedElement("-1");
        }
        try {
            final PaymentHeader header = getPaymentHeader(context);
            final School school = header.getSchool();
            map.put(LAST_PROVIDER_KEY, school.getPrimaryKey());
            final SchoolManagementType managementType = school
                    .getManagementType();
            //--verksamhet
            //--skolår/timmar
            //--grupp
            addSmallText(map, PROVIDER_KEY, school.getName());
            addSmallText(map, MANAGEMENT_TYPE_KEY, managementType
                    .getLocalizedKey(), managementType.getName());
        } catch (Exception e) {
            logWarning("Missing school properties in payment record "
                    + record.getPrimaryKey());
            log(e);
        }

        map.put(HEADER_KEY, getSmallHeader(localize(EDIT_PAYMENT_RECORD_KEY,
                EDIT_PAYMENT_RECORD_DEFAULT)));
        map.put(ACTION_KEY, getSubmitButton(ACTION_SAVE_RECORD, SAVE_EDITS_KEY,
                SAVE_EDITS_DEFAULT));
        renderRecordDetailsOrForm(context, map);
    }

    private void showRecord(final IWContext context) throws RemoteException,
            FinderException {
        final PaymentRecord record = getPaymentRecord(context);
        final java.util.Map map = new java.util.HashMap();
        map.put(ADJUSTED_SIGNATURE_KEY,
                getSmallSignature(record.getChangedBy()));
        addSmallText(map, ADJUSTED_SIGNATURE_KEY, record.getChangedBy());
        addSmallText(map, AMOUNT_KEY, getFormattedAmount(record
                .getTotalAmount()));
        map
                .put(CREATED_SIGNATURE_KEY, getSmallSignature(record
                        .getCreatedBy()));
        addSmallText(map, DATE_ADJUSTED_KEY, record.getDateChanged());
        addSmallText(map, DATE_CREATED_KEY, record.getDateCreated());
        map.put(DOUBLE_POSTING_KEY, getPostingListTable(record
                .getDoublePosting()));
        addSmallText(map, NOTE_KEY, record.getNotes());
        map.put(OWN_POSTING_KEY, getPostingListTable(record.getOwnPosting()));
        addSmallText(map, PAYMENT_TEXT_KEY, record.getPaymentText());
        addSmallPeriodText(map, PERIOD_KEY, record.getPeriod());
        addSmallText(map, PIECE_AMOUNT_KEY, getFormattedAmount(record
                .getPieceAmount()));
        addSmallText(map, NUMBER_OF_PLACEMENTS_KEY, record.getPlacements());
        addSmallText(map, STATUS_KEY, record.getStatus() + "");
        addSmallText(map, TRANSACTION_DATE_KEY, record.getDateTransaction());
        addSmallText(map, VAT_AMOUNT_KEY, getFormattedAmount(record
                .getTotalAmountVAT()));
        final String ruleSpecType = record.getRuleSpecType();
        addSmallText(map, REGULATION_SPEC_TYPE_KEY, ruleSpecType, ruleSpecType);
        if (0 < record.getVATRuleRegulationId()) {
            final InvoiceBusiness business = getInvoiceBusiness();
            final Regulation vatRule = business.getVATRuleRegulation(record
                    .getVATRuleRegulationId());
            final String ruleName = vatRule.getLocalizationKey();
            map.put(VAT_RULE_KEY, getSmallText(localize(ruleName, ruleName)));
        }

        try {
            final PaymentHeader header = getPaymentHeader(context);
            final School school = header.getSchool();
            map.put(LAST_PROVIDER_KEY, school.getPrimaryKey());
            final SchoolManagementType managementType = school
                    .getManagementType();
            //--verksamhet
            //--skolår/timmar
            //--grupp
            addSmallText(map, PROVIDER_KEY, school.getName());
            addSmallText(map, MANAGEMENT_TYPE_KEY, managementType
                    .getLocalizedKey(), managementType.getName());
        } catch (Exception e) {
            logWarning("Missing school properties i payment record "
                    + record.getPrimaryKey());
            log(e);
        }

        map.put(HEADER_KEY, getSmallHeader(localize(PAYMENT_RECORD_KEY,
                PAYMENT_RECORD_DEFAULT)));

        renderRecordDetailsOrForm(context, map);
    }

    private SchoolCategory getSchoolCategory(final String schoolCategoryId)
            throws RemoteException, FinderException {
        final SchoolBusiness schoolBusiness = getSchoolBusiness();
        final SchoolCategoryHome categoryHome = schoolBusiness
                .getSchoolCategoryHome();
        return categoryHome.findByPrimaryKey(schoolCategoryId);
    }

    private void showRecordDetails(final IWContext context)
            throws RemoteException, FinderException {
        // get business objects
        final InvoiceBusiness business = getInvoiceBusiness();
        final SchoolBusiness schoolBusiness = getSchoolBusiness();

        // get home objects
        final InvoiceRecordHome home = business.getInvoiceRecordHome();

        // get data objects
        final PaymentRecord record = getPaymentRecord(context);
        final PaymentHeader header = record.getPaymentHeader();
        final SchoolCategory category = getSchoolCategory(header
                .getSchoolCategoryID());
        final School school = schoolBusiness.getSchool(new Integer(header
                .getSchoolID()));
        final Collection invoiceRecords = home
                .findByPaymentRecordOrderedByStudentName(record);

        // render
        final Table table = createTable(2);
        table.setColumnWidth(1, "25%");
        int row = 1;
        int col = 1;
        addSmallHeader(table, col++, row, MAIN_ACTIVITY_KEY,
                MAIN_ACTIVITY_DEFAULT, ":");
        addSmallText(table, col++, row++, category.getLocalizedKey(), category
                .getName());
        col = 1;
        addSmallHeader(table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        addSmallText(table, col++, row++, school.getName());
        col = 1;
        addSmallHeader(table, col++, row, PLACEMENT_KEY, PLACEMENT_DEFAULT, ":");
        addSmallText(table, col++, row++, record.getPaymentText());
        col = 1;
        addSmallHeader(table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        addSmallText(table, col++, row++,
                getFormattedPeriod(record.getPeriod()));
        table.setHeight(row++, 6);
        table.mergeCells(1, row, table.getColumns(), row);
        table.add(getDetailedPaymentRecordListTable(invoiceRecords), 1, row++);
        table.setHeight(row++, 6);
        table.mergeCells(1, row, table.getColumns(), row);
        table.add(getDetailedPaymentRecordSummaryTable(invoiceRecords), 1,
                row++);
        table.setHeight(row++, 6);
        table.mergeCells(1, row, table.getColumns(), row);
        table.add(new HiddenInput(PROVIDER_KEY, school.getPrimaryKey() + ""),
                1, row);
        addCancelButton(table, 1, row++, ACTION_SHOW_PAYMENT);

        // add to form
        final Form form = new Form();
        form.setOnSubmit("return checkInfoForm()");
        form.add(table);
        final Table outerTable = createTable(1);
        outerTable.add(form, 1, 1);
        add(createMainTable(localize(DETAILED_PAYMENT_RECORDS_KEY,
                DETAILED_PAYMENT_RECORDS_DEFAULT), outerTable));
    }

    private void showPayment(final IWContext context) throws RemoteException,
            FinderException {
        final Table table = createTable(3);
        setColumnWidthsEqual(table);
        int row = 2; // first row is reserved for setting column widths
        addOperationalFieldDropdown(context, table, row++);
        addProviderDropdown(context, table, row++);
        addPeriodForm(context, table, row);
        final int columnCount = table.getColumns();
        table.setAlignment(columnCount, row, Table.HORIZONTAL_ALIGN_RIGHT);
        table.add(getSubmitButton(ACTION_SHOW_PAYMENT + "", SEARCH_KEY,
                SEARCH_DEFAULT), columnCount, row++);
        final String schoolCategory = getSession().getOperationalField();
        final Integer providerId = getProviderIdParameter(context);
        if (null != schoolCategory && null != providerId) {
            final InvoiceBusiness business = getInvoiceBusiness();
            final Date startPeriod = getPeriodParameter(context,
                    START_PERIOD_KEY);
            final Date endPeriod = getPeriodParameter(context, END_PERIOD_KEY);
            final PaymentRecord[] records = business
                    .getPaymentRecordsBySchoolCategoryAndProviderAndPeriod(
                            schoolCategory, providerId, new Date(startPeriod
                                    .getTime()), new Date(endPeriod.getTime()));
            table.setHeight(row++, 12);
            table.mergeCells(1, row, columnCount, row);
            final ButtonPanel buttonPanel = new ButtonPanel(this);
            if (0 < records.length) {
                table
                        .setAlignment(columnCount, 2,
                                Table.HORIZONTAL_ALIGN_RIGHT);
                table.add(getSubmitButton(ACTION_GENERATE_CHECK_AMOUNT_LIST_PDF
                        + "", PRINT_KEY, PRINT_DEFAULT), columnCount, 2);
                table
                        .add(getPaymentRecordListTable(context, records), 1,
                                row++);
                table.mergeCells(1, row, columnCount, row);
                table.add(getPaymentSummaryTable(records), 1, row++);
                if (null != providerAuthorizationPage) {
//                    buttonPanel.addLocalizedButton("no_param",
//                            PROVIDER_CONFIRM_KEY, PROVIDER_CONFIRM_DEFAULT,
//                            providerAuthorizationPage);
					GenericButton button = new GenericButton("no_param",
							localize(PROVIDER_CONFIRM_KEY, PROVIDER_CONFIRM_DEFAULT));
					button.setPageToOpen(providerAuthorizationPage);
					button.addParameterToPage(
							ManuallyPaymentEntriesList.PAR_SELECTED_PROVIDER,
							providerId + "");                            
					buttonPanel.addButton(button);                         
                }
            } else {
                addSmallText(table, 1, row++, NO_PAYMENT_RECORDS_FOUND_KEY,
                        NO_PAYMENT_RECORDS_FOUND_DEFAULT);
            }
            table.setHeight(row++, 12);
            table.mergeCells(1, row, columnCount, row);
            table.add(buttonPanel, 1, row);
            if (null != createPaymentPage) {
                final GenericButton button = new GenericButton("no_param",
                        localize(NEW_KEY, NEW_DEFAULT));
                button.setPageToOpen(createPaymentPage);
                button.addParameterToPage(
                        ManuallyPaymentEntriesList.PAR_SELECTED_PROVIDER,
                        providerId + "");
                buttonPanel.addButton(button);
            }
        }
        final Form form = new Form();
        form.setOnSubmit("return checkInfoForm()");
        form.add(table);
        final Table outerTable = createTable(1);
        outerTable.add(form, 1, 1);
        add(createMainTable(
                localize(PAYMENT_HEADER_KEY, PAYMENT_HEADER_DEFAULT),
                outerTable));
    }

    private boolean hasCurrentSchoolCategoryFlowInAndFlowOut()
            throws RemoteException, FinderException {
        final String schoolCategory = getSession().getOperationalField();
        if (null == schoolCategory) return false;
        final ExportDataMapping mapping = getExportBusiness()
                .getExportDataMapping(schoolCategory);
        if (null == mapping) return false;
        return mapping.getCashFlowIn() && mapping.getCashFlowOut();
    }

    private Table getDetailedPaymentRecordListTable(
            final Collection invoiceRecords) throws RemoteException {
        // set up header row
        final String[][] columnNames = { { SSN_KEY, SSN_DEFAULT},
                { NAME_KEY, NAME_DEFAULT},
                { CHECK_PERIOD_KEY, CHECK_PERIOD_DEFAULT},
                { DAYS_KEY, DAYS_DEFAULT},
                { CHECK_AMOUNT_KEY, CHECK_AMOUNT_DEFAULT},
                { PLACEMENT_PERIOD_KEY, PLACEMENT_PERIOD_DEFAULT},
                { ADJUSTMENT_DATE_KEY, ADJUSTMENT_DATE_DEFAULT},
                { ADJUSTMENT_SIGNATURE_KEY, ADJUSTMENT_SIGNATURE_DEFAULT}};
        final Table table = createTable(columnNames.length);
        table.setColumns(columnNames.length);
        setIconColumnWidth(table);
        int row = 1;
        table.setRowColor(row, getHeaderColor());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader(table, i + 1, row, columnNames[i][0],
                    columnNames[i][1]);
        }
        row++;

        //render
        final SchoolBusiness schoolBusiness = getSchoolBusiness();
        final SchoolClassMemberHome memberHome = schoolBusiness
                .getSchoolClassMemberHome();
        for (Iterator i = invoiceRecords.iterator(); i.hasNext();) {
            final InvoiceRecord invoiceRecord = (InvoiceRecord) i.next();
            showDetailedPaymentRecordOnARow(table, row++, invoiceRecord,
                    memberHome);
        }

        return table;
    }

    private Table getDetailedPaymentRecordSummaryTable(
            final Collection invoiceRecords) throws RemoteException {
        final Set placements = new HashSet();
        final Set individuals = new HashSet();
        long totalAmountVatExcluded = 0;
        // get home object
        final SchoolBusiness schoolBusiness = getSchoolBusiness();
        final SchoolClassMemberHome home = schoolBusiness
                .getSchoolClassMemberHome();

        // count values for summary
        for (Iterator i = invoiceRecords.iterator(); i.hasNext();) {
            final InvoiceRecord invoiceRecord = (InvoiceRecord) i.next();
            try {
                final Integer placementId = new Integer(invoiceRecord
                        .getSchoolClassMemberId());
                placements.add(placementId);
                final SchoolClassMember placement = home
                        .findByPrimaryKey(placementId);
                final User user = placement.getStudent();
                individuals.add(user.getPrimaryKey());
            } catch (Exception e) {
                logError("InvoiceRecord " + invoiceRecord.getPrimaryKey()
                        + " has an unknown School Class Member Id");
                e.printStackTrace();
            }
            totalAmountVatExcluded += roundAmount(invoiceRecord.getAmount());
        }

        // render
        final Table table = createTable(3);
        table.setColumnWidth(3, "50%");
        int row = 2;
        int col = 1;
        addSmallHeader(table, col++, row, TOTAL_AMOUNT_PLACEMENTS_KEY,
                TOTAL_AMOUNT_PLACEMENTS_DEFAULT, ":");
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row++, integerFormatter.format(placements
                .size()));
        col = 1;
        addSmallHeader(table, col++, row, TOTAL_AMOUNT_INDIVIDUALS_KEY,
                TOTAL_AMOUNT_INDIVIDUALS_DEFAULT, ":");
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row++, integerFormatter.format(individuals
                .size()));
        col = 1;
        addSmallHeader(table, col++, row, TOTAL_AMOUNT_VAT_EXCLUDED_KEY,
                TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT, ":");
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row++,
                getFormattedAmount(totalAmountVatExcluded));
        return table;
    }

    private void showDetailedPaymentRecordOnARow(final Table table,
            final int row, final InvoiceRecord record,
            final SchoolClassMemberHome home) {
        final String checkPeriod = getFormattedDate(record
                .getPeriodStartCheck())
                + " - " + getFormattedDate(record.getPeriodEndCheck());
        final String days = integerFormatter.format(record.getDays());
        final String amount = getFormattedAmount(record.getAmount());
        final String placementPeriod = getFormattedDate(record
                .getPeriodStartPlacement())
                + " - " + getFormattedDate(record.getPeriodEndPlacement());
        final String dateChanged = getFormattedDate(record.getDateChanged());
        final String changedBy = record.getChangedBy();
        final Integer memberId = new Integer(record.getSchoolClassMemberId());
        String ssn = "";
        String userName = localize(PLACEMENT_REMOVED_KEY,
                PLACEMENT_REMOVED_DEFAULT);
        try {
            final SchoolClassMember member = home.findByPrimaryKey(memberId);
            final User user = member.getStudent();
            ssn = formatSsn(user.getPersonalID());
            userName = getUserName(user);
        } catch (Exception e) {
            logError("InvoiceRecord " + record.getPrimaryKey()
                    + " has an unknown School Class Member Id " + memberId);
            e.printStackTrace();
        }
        int col = 1;
        table.setRowColor(row, (row % 2 == 0) ? getZebraColor1()
                : getZebraColor2());
        addSmallText(table, col++, row, ssn);
        addSmallText(table, col++, row, userName);
        addSmallText(table, col++, row, checkPeriod);
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row, days);
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row, amount);
        addSmallText(table, col++, row, placementPeriod);
        addSmallText(table, col++, row, dateChanged);
        addSmallText(table, col++, row, changedBy);
    }

    private Table getPaymentSummaryTable(final PaymentRecord[] records)
            throws RemoteException {
        final PaymentSummary summary = new PaymentSummary(records);

        // render
        final Table table = createTable(3);
        table.setColumnWidth(3, "50%");
        int row = 2;
        int col = 1;
        addSmallHeader(table, col++, row, TOTAL_AMOUNT_PLACEMENTS_KEY,
                TOTAL_AMOUNT_PLACEMENTS_DEFAULT, ":");
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row++, integerFormatter.format(summary
                .getPlacementCount()));
        col = 1;
        addSmallHeader(table, col++, row, TOTAL_AMOUNT_INDIVIDUALS_KEY,
                TOTAL_AMOUNT_INDIVIDUALS_DEFAULT, ":");
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row++, integerFormatter.format(summary
                .getIndividualsCount()));
        col = 1;
        addSmallHeader(table, col++, row, TOTAL_AMOUNT_VAT_EXCLUDED_KEY,
                TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT, ":");
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row++, getFormattedAmount(summary
                .getTotalAmountVatExcluded()));
        col = 1;
        addSmallHeader(table, col++, row, TOTAL_AMOUNT_VAT_KEY,
                TOTAL_AMOUNT_VAT_DEFAULT, ":");
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row++, getFormattedAmount(summary
                .getTotalAmountVat()));
        return table;
    }

    private Table getPaymentRecordListTable(final IWContext context,
            final PaymentRecord[] records) throws FinderException,
            RemoteException {
        // set up header row
        final String[][] columnNames = { { STATUS_KEY, STATUS_DEFAULT},
                { PERIOD_KEY, PERIOD_DEFAULT},
                { PLACEMENT_KEY, PLACEMENT_DEFAULT},
                { NO_OF_PLACEMENTS_KEY, NO_OF_PLACEMENTS_DEFAULT},
                { TOTAL_AMOUNT_KEY, TOTAL_AMOUNT_DEFAULT},
                { NOTE_KEY, NOTE_DEFAULT}, { "no_text", ""}, { "no_text", ""}};
        final Table table = createTable(columnNames.length);
        table.setColumns(columnNames.length);
        setIconColumnWidth(table);
        int row = 1;
        table.setRowColor(row, getHeaderColor());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader(table, i + 1, row, columnNames[i][0],
                    columnNames[i][1]);
        }
        row++;

        // show each payment record in a row
        for (int i = 0; i < records.length; i++) {
            showPaymentRecordOnARow(context, table, row++, records[i]);
        }

        return table;
    }

    private static boolean isPreliminaryRecord(final PaymentRecord record) {
        return record.getStatus() == ConstantStatus.PRELIMINARY;
        /*
         * final String autoSignature = BillingThread.getBatchRunSignatureKey
         * (); final String createdBy = record.getCreatedBy (); return null ==
         * createdBy || !createdBy.equals (autoSignature);
         */
    }

    private Text getSmallSignature(final String string) {
        final StringBuffer result = new StringBuffer();
        final String autoSignature = BillingThread.getBatchRunSignatureKey();
        if (null != string) {
            if (string.equals(autoSignature)) {
                result.append(localize(string, string));
            } else {
                result.append(string);
            }
        }
        return getSmallText(result.toString());
    }

    private boolean isCheck(final RegulationSpecType regSpecType) {
        try {
            final MainRule mainRule = regSpecType.getMainRule();
            final String mainRuleName = mainRule.getMainRule();
            return mainRuleName.equals(RegSpecConstant.MAIN_RULE_CHECK);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCheck(final String regSpecTypeName) {
        try {
            final RegulationSpecTypeHome regSpecTypeHome = (RegulationSpecTypeHome) IDOLookup
                    .getHome(RegulationSpecType.class);
            final RegulationSpecType regSpecType = regSpecTypeHome
                    .findByRegulationSpecType(regSpecTypeName);
            return isCheck(regSpecType);
        } catch (Exception e) {
            return false;
        }
    }

    private void showPaymentRecordOnARow(final IWContext context,
            final Table table, final int row, final PaymentRecord record)
            throws RemoteException, FinderException {
        final String recordId = record.getPrimaryKey() + "";
        final String[][] showDetailsLinkParameters = {
                { ACTION_KEY, ACTION_SHOW_RECORD_DETAILS + ""},
                { PAYMENT_RECORD_KEY, recordId}};
        final String regSpecType = record.getRuleSpecType();
        final boolean userIsSchoolManager = null != getSchoolByLoggedInUser(context);
        final boolean isFlowInAndOut = hasCurrentSchoolCategoryFlowInAndFlowOut();
        final boolean isRecordEditAllowed = isPreliminaryRecord(record)
                && !(isFlowInAndOut && isCheck(regSpecType))
                && !userIsSchoolManager;
        final String[][] showRecordLinkParameters = isRecordEditAllowed ? new String[][] {
                { ACTION_KEY, ACTION_SHOW_EDIT_RECORD_FORM + ""},
                { PAYMENT_RECORD_KEY, recordId}}
                : new String[][] { { ACTION_KEY, ACTION_SHOW_RECORD + ""},
                        { PAYMENT_RECORD_KEY, recordId}};
        final char status = record.getStatus();
        final Date period = record.getPeriod();
        final String periodText = getFormattedPeriod(period);
        final String paymentText = null == record.getPaymentText() ? "?"
                : record.getPaymentText();
        final Link paymentTextLink = createSmallLink(paymentText,
                showRecordLinkParameters);
        final Link placementLink = createSmallLink(integerFormatter
                .format(record.getPlacements()), showDetailsLinkParameters);
        final String note = record.getNotes();
        final Link editLink = createIconLink(getEditIcon(),
                showRecordLinkParameters);
        int col = 1;
        table.setRowColor(row, (row % 2 == 0) ? getZebraColor1()
                : getZebraColor2());
        addSmallText(table, col++, row, status + "");
        addSmallText(table, col++, row, periodText);
        table.add(paymentTextLink, col++, row);
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        table.add(placementLink, col++, row);
        table.setAlignment(col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText(table, col++, row, getFormattedAmount(record
                .getTotalAmount()));
        addSmallText(table, col++, row, note);
        table.add(editLink, col++, row);
        if (isRecordEditAllowed) {
            final String[][] removeRecordLinkParameters = new String[][] {
                    { ACTION_KEY, ACTION_REMOVE_RECORD + ""},
                    { PAYMENT_RECORD_KEY, recordId}};
            final Link removeLink = createIconLink(getRemoveIcon(),
                    removeRecordLinkParameters);
            table.add(removeLink, col++, row);
        }
    }

    private PaymentRecord getPaymentRecord(final IWContext context)
            throws RemoteException, FinderException {
        final InvoiceBusiness business = getInvoiceBusiness();
        final Integer recordId = getIntegerParameter(context,
                PAYMENT_RECORD_KEY);
        final PaymentRecordHome recordHome = business.getPaymentRecordHome();
        return null != recordId ? recordHome.findByPrimaryKey(recordId) : null;
    }

    private PaymentHeader getPaymentHeader(final IWContext context)
            throws RemoteException, FinderException {
        final InvoiceBusiness business = getInvoiceBusiness();
        if (!context.isParameterSet(PAYMENT_HEADER_KEY)) { return getPaymentRecord(
                context).getPaymentHeader(); }
        final Integer headerId = getIntegerParameter(context,
                PAYMENT_HEADER_KEY);
        final PaymentHeaderHome headerHome = business.getPaymentHeaderHome();
        return null != headerId ? headerHome.findByPrimaryKey(headerId) : null;
    }

    private void renderRecordDetailsOrForm(final IWContext context,
            final java.util.Map presentationObjects) throws RemoteException,
            FinderException {
        final PaymentHeader header = getPaymentHeader(context);

        // render form/details
        final Table table = createTable(4);
        setColumnWidthsEqual(table);
        int row = 2;
        int col = 1;
        addOperationalFieldRow(table, header, row++);
        col = 1;
        addSmallHeader(table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, PROVIDER_KEY, col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, MANAGEMENT_TYPE_KEY,
                MANAGEMENT_TYPE_DEFAULT, ":");
        addPresentation(table, presentationObjects, MANAGEMENT_TYPE_KEY, col++,
                row);
        col = 1;
        row++;
        table.setHeight(row++, 12);
        addSmallHeader(table, col++, row, PLACEMENT_KEY, PLACEMENT_DEFAULT, ":");
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, PAYMENT_TEXT_KEY, col++,
                row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        addPresentation(table, presentationObjects, PERIOD_KEY, col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, DATE_CREATED_KEY,
                DATE_CREATED_DEFAULT, ":");
        addPresentation(table, presentationObjects, DATE_CREATED_KEY, col++,
                row);
        addSmallHeader(table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT, ":");
        addPresentation(table, presentationObjects, CREATED_SIGNATURE_KEY,
                col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, DATE_ADJUSTED_KEY,
                DATE_ADJUSTED_DEFAULT, ":");
        addPresentation(table, presentationObjects, DATE_ADJUSTED_KEY, col++,
                row);
        addSmallHeader(table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT, ":");
        addPresentation(table, presentationObjects, ADJUSTED_SIGNATURE_KEY,
                col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, TRANSACTION_DATE_KEY,
                TRANSACTION_DATE_DEFAULT, ":");
        addPresentation(table, presentationObjects, TRANSACTION_DATE_KEY,
                col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, STATUS_KEY, STATUS_DEFAULT, ":");
        addPresentation(table, presentationObjects, STATUS_KEY, col++, row);
        col = 1;
        row++;
        table.setHeight(row++, 12);
        addSmallHeader(table, col++, row, NUMBER_OF_PLACEMENTS_KEY,
                NUMBER_OF_PLACEMENTS_DEFAULT, ":");
        addPresentation(table, presentationObjects, NUMBER_OF_PLACEMENTS_KEY,
                col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, PIECE_AMOUNT_KEY,
                PIECE_AMOUNT_DEFAULT, ":");
        addPresentation(table, presentationObjects, PIECE_AMOUNT_KEY, col++,
                row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, AMOUNT_KEY, AMOUNT_DEFAULT, ":");
        addPresentation(table, presentationObjects, AMOUNT_KEY, col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, VAT_AMOUNT_KEY, VAT_AMOUNT_DEFAULT,
                ":");
        addPresentation(table, presentationObjects, VAT_AMOUNT_KEY, col++, row);
        col = 1;
        row++;
        table.setHeight(row++, 12);
        addSmallHeader(table, col++, row, NOTE_KEY, NOTE_DEFAULT, ":");
        addPresentation(table, presentationObjects, NOTE_KEY, col++, row);
        col = 1;
        row++;
        table.setHeight(row++, 12);
        addSmallHeader(table, col++, row, SCHOOL_TYPE_KEY, SCHOOL_TYPE_DEFAULT,
                ":");
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, SCHOOL_TYPE_KEY, col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, SCHOOL_YEAR_KEY, SCHOOL_YEAR_DEFAULT,
                ":");
        addPresentation(table, presentationObjects, SCHOOL_YEAR_KEY, col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, SCHOOL_CLASS_KEY,
                SCHOOL_CLASS_DEFAULT, ":");
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, SCHOOL_CLASS_KEY, col++,
                row);
        col = 1;
        row++;
        table.setHeight(row++, 12);
        addSmallHeader(table, col++, row, REGULATION_SPEC_TYPE_KEY,
                REGULATION_SPEC_TYPE_DEFAULT, ":");
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, REGULATION_SPEC_TYPE_KEY,
                col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, VAT_RULE_KEY, VAT_RULE_DEFAULT, ":");
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, VAT_RULE_KEY, col++, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, OWN_POSTING_KEY, OWN_POSTING_DEFAULT,
                ":");
        col = 1;
        row++;
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, OWN_POSTING_KEY, col, row);
        col = 1;
        row++;
        addSmallHeader(table, col++, row, DOUBLE_POSTING_KEY,
                DOUBLE_POSTING_DEFAULT, ":");
        col = 1;
        row++;
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, DOUBLE_POSTING_KEY, col,
                row);
        col = 1;
        row++;
        table.setHeight(row++, 12);
        table.mergeCells(col, row, table.getColumns(), row);
        addPresentation(table, presentationObjects, ACTION_KEY, 1, row);
        table.add(new HiddenInput(PROVIDER_KEY, ""
                + presentationObjects.get(LAST_PROVIDER_KEY)), 1, row);
        addCancelButton(table, 1, row++, ACTION_SHOW_PAYMENT);
        final Form form = new Form();
        form.maintainParameter(PAYMENT_HEADER_KEY);
        form.maintainParameter(PAYMENT_RECORD_KEY);
        form.setOnSubmit("return checkInfoForm()");
        form.add(table);
        final Table outerTable = createTable(1);
        outerTable.add(form, 1, 1);
        add(createMainTable(presentationObjects.get(HEADER_KEY) + "",
                outerTable));
    }

    private String getPostingString(final IWContext context,
            final String postingKey) throws RemoteException {
        final PostingBusiness business = getPostingBusiness();
        final StringBuffer result = new StringBuffer();
        final PostingField[] fields = getCurrentPostingFields();
        for (int i = 0; i < fields.length; i++) {
            final PostingField field = fields[i];
            final String key = postingKey + (i + 1);
            final String parameter = context.isParameterSet(key) ? context
                    .getParameter(key) : "";
            final String value = parameter.length() > field.getLen() ? parameter
                    .substring(0, field.getLen())
                    : business.pad(parameter, field);
            result.append(value);
        }
        return result.toString();
    }

    private ListTable getPostingParameterForm(final String key,
            final String value) throws RemoteException {
        final String postingString = value != null && !value.equals(null + "") ? value
                : "";
        final PostingField[] fields = getCurrentPostingFields();
        final ListTable postingInputs = new ListTable(this, fields.length);
        int offset = 0;
        for (int i = 0; i < fields.length; i++) {
            final PostingField field = fields[i];
            final int j = i + 1;
            final int endPosition = min(offset + field.getLen(), postingString
                    .length());
            postingInputs.setHeader(field.getFieldTitle(), j);
            final String subString = postingString.substring(offset,
                    endPosition).trim();
            final TextInput textInput = getTextInput(key + j, subString, 80,
                    field.getLen());
            postingInputs.add(getStyledInterface(textInput));
            offset = endPosition;
        }
        return postingInputs;
    }

    private String getSignature(final User user) {
        if (null == user) return "";
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();
        return (firstName != null ? firstName + " " : "")
                + (lastName != null ? lastName : "");
    }

    /**
     * Returns a styled table with content placed properly
     * 
     * @param content
     *            the page unique content
     * @return Table to add to output
     */
    private Table createMainTable(final String headerKey,
            final String headerDefault, final PresentationObject content) {
        return createMainTable(localize(headerKey, headerDefault), content);
    }

    private void addPresentation(final Table table, final java.util.Map map,
            final String key, final int col, final int row) {
        final PresentationObject object = (PresentationObject) map.get(key);
        if (null != object) {
            table.add(object, col, row);
        }
    }

    private String getFormattedPeriod(Date date) {
        return null != date ? periodFormatter.format(date) : "";
    }

    private String getFormattedDate(Date date) {
        return null != date ? dateFormatter.format(date) : "";
    }

    private Image getEditIcon() {
        return getEditIcon(localize(EDIT_PAYMENT_RECORD_KEY,
                EDIT_PAYMENT_RECORD_DEFAULT));
    }

    private Image getRemoveIcon() {
        return getDeleteIcon(localize(DELETE_ROW_KEY, DELETE_ROW_DEFAULT));
    }

    private static Link addParametersToLink(final Link link,
            final String[][] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            link.addParameter(parameters[i][0], parameters[i][1]);
        }
        return link;
    }

    private String formatSsn(final String ssn) {
        return null == ssn || 12 != ssn.length() ? ssn : ssn.substring(2, 8)
                + '-' + ssn.substring(8, 12);
    }

    private static String getUserName(final User user) {
        return user.getLastName() + ", " + user.getFirstName();
    }

    private Link createSmallLink(final String displayText,
            final String[][] parameters) {
        final Link link = getSmallLink(displayText);
        addParametersToLink(link, parameters);
        return link;
    }

    private static Link createIconLink(final Image icon,
            final String[][] parameters) {
        final Link link = new Link(icon);
        addParametersToLink(link, parameters);
        return link;
    }

    private void addSmallText(final Table table, final int col, final int row,
            final String key, String value) {
        table.add(getSmallText(localize(key, value)), col, row);
    }

    private void addSmallText(final Table table, final int col, final int row,
            final String string) {
        table.add(getSmallText(null == string || string.equals(null + "") ? ""
                : string), col, row);
    }

    private void addSmallHeader(final Table table, final int col,
            final int row, final String key, final String defaultString) {
        addSmallHeader(table, col, row, key, defaultString, "");
    }

    private SubmitButton getSubmitButton(final int action, final String key,
            final String defaultName) {
        return (SubmitButton) getButton(new SubmitButton(localize(key,
                defaultName), ACTION_KEY, action + ""));
    }

    private void setIconColumnWidth(final Table table) {
        final int columnCount = table.getColumns();
        table.setColumnWidth(columnCount - 1, getEditIcon().getWidth());
        table.setColumnWidth(columnCount, getRemoveIcon().getWidth());
    }

    /**
     * Returns a date from a parameter string of type "YYMM". The date
     * represents the first day of that month. If the input string is unparsable
     * for this format then null is returned.
     * 
     * @param context
     *            session data
     * @param key
     *            key to lookup in context to retreive the actual value
     * @return date from the first of this particular month or null on failure
     */
    private static Date getPeriodParameter(final IWContext context,
            final String key) {
        return getPeriodParameter(context.getRequest(), key);
    }

    static Date getPeriodParameter(final HttpServletRequest request,
            final String key) {
        final String rawString = request.getParameter(key);
        final HttpSession session = request.getSession();
        final Date sessionPeriod = (Date) session.getAttribute(key);
        Date result = null != sessionPeriod ? sessionPeriod : new Date(System
                .currentTimeMillis());
        if (null != rawString && 4 == rawString.length()) {
            try {
                final int year = Integer.parseInt(rawString.substring(0, 2)) + 2000;
                final int month = Integer.parseInt(rawString.substring(2, 4))
                        + Calendar.JANUARY - 1;
                final Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, 1, 0, 0);
                result = new Date(calendar.getTimeInMillis());
            } catch (final NumberFormatException exception) {
                // no problem, stick with current time
            }
        }
        session.setAttribute(key, result);
        return result;
    }

    private static boolean hasChangedProvider(final IWContext context) {
        final HttpSession session = context.getRequest().getSession();
        final Integer lastProviderId = (Integer) session
                .getAttribute(PROVIDER_KEY + "old");
        final Integer currentProviderId = getProviderIdParameter(context);
        session.setAttribute(PROVIDER_KEY + "old", currentProviderId);
        return null != lastProviderId && null != currentProviderId
                && !lastProviderId.equals(currentProviderId);
    }

    private static Integer getProviderIdParameter(final IWContext context) {
        return getProviderIdParameter(context.getRequest());
    }

    static Integer getProviderIdParameter(final HttpServletRequest request) {
        Integer result = null;
        final String postedString = request.getParameter(PROVIDER_KEY);
        final HttpSession session = request.getSession();
        try {
            result = new Integer(postedString);
        } catch (final Exception exception) {
            result = (Integer) session.getAttribute(PROVIDER_KEY);
        }
        session.setAttribute(PROVIDER_KEY, result);
        return result;
    }

    /**
     * Returns a styled table with content placed properly
     * 
     * @param content
     *            the page unique content
     * @return Table to add to output
     */
    private Table createMainTable(final String header,
            final PresentationObject content) {
        final Table mainTable = createTable(1);
        mainTable.setCellpadding(getCellpadding());
        mainTable.setCellspacing(getCellspacing());
        mainTable.setWidth(Table.HUNDRED_PERCENT);
        int row = 1;
        mainTable.setRowColor(row, getHeaderColor());
        mainTable.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);
        mainTable.add(getSmallHeader(header), 1, row++);
        mainTable.add(content, 1, row++);
        return mainTable;
    }

    private DropdownMenu getLocalizedDropdownForVAT(final Collection rules) {
        final DropdownMenu dropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(
                VAT_RULE_KEY));
        for (Iterator iter = rules.iterator(); iter.hasNext();) {
            Regulation rule = (Regulation) iter.next();
            final String ruleName = rule.getName();
            final Object ruleId = rule.getPrimaryKey();
            dropdown.addMenuElement(ruleId + "", localize(ruleName, ruleName));
        }
        return dropdown;
    }

    private DropdownMenu getLocalizedDropdown(final RegulationSpecType[] types) {
        final DropdownMenu dropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(
                REGULATION_SPEC_TYPE_KEY));
        for (int i = 0; i < types.length; i++) {
            final RegulationSpecType type = types[i];
            final String regSpecType = type.getRegSpecType();
            dropdown.addMenuElement(regSpecType, localize(regSpecType,
                    regSpecType));
        }
        return dropdown;
    }

    private void addPeriodForm(final IWContext context, final Table table,
            final int row) {
        int col = 1;
        addSmallHeader(table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        final Date now = new Date(System.currentTimeMillis());
        final Date startDate = getPeriodParameter(context, START_PERIOD_KEY);
        final Date endDate = getPeriodParameter(context, END_PERIOD_KEY);
        table.add(getStyledInput(START_PERIOD_KEY,
                getFormattedPeriod(startDate == null ? now : startDate)), col,
                row);
        table.add(new Text(" - "), col, row);
        table.add(getStyledInput(END_PERIOD_KEY,
                getFormattedPeriod(endDate == null ? now : endDate)), col, row);
    }

    private String getSchoolCategoryName(final String schoolCategoryId) {
        try {
            final SchoolCategory category = getSchoolCategory(schoolCategoryId);
            return localize(category.getLocalizedKey(), category.getName());
        } catch (Exception dummy) {
            return "";
        }
    }

    private void addProviderDropdown(final IWContext context,
            final Table table, final int row) throws RemoteException {
        final SchoolBusiness business = getSchoolBusiness();
        int col = 1;
        addSmallHeader(table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        final String schoolCategory = getSession().getOperationalField();
        final School loggedInUsersProvider = getSchoolByLoggedInUser(context);
        if (null != loggedInUsersProvider) {
            addSmallText(table, loggedInUsersProvider.getName(), col, row);
            table.add(new HiddenInput(PROVIDER_KEY, ""
                    + loggedInUsersProvider.getPrimaryKey()), col, row);
        } else if (isCentralAdministrator(context) && null != schoolCategory) {
            final DropdownMenu providerDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(
                    PROVIDER_KEY));
            final Collection schools = business
                    .findAllSchoolsByCategory(schoolCategory);
            final Integer oldProviderId = getProviderIdParameter(context);
            for (Iterator i = schools.iterator(); i.hasNext();) {
                final School provider = (School) i.next();
                final Integer providerId = (Integer) provider.getPrimaryKey();
                final String providerName = provider.getName();
                if (null != providerName && null != providerId) {
                    providerDropdown.addMenuElement(providerId + "",
                            providerName);
                    if (null != oldProviderId
                            && oldProviderId.equals(providerId)) {
                        providerDropdown.setSelectedElement(oldProviderId + "");
                    }
                }
            }
            providerDropdown.setOnChange("this.form.submit()");
            table.add(providerDropdown, col++, row);
        }
    }

    private boolean isCentralAdministrator(final IWContext context) {
        try {
            // first see if we have cached certificate
            final String sessionKey = getClass() + ".isCentralAdministrator";
            final User verifiedCentralAdmin = (User) context
                    .getSessionAttribute(sessionKey);
            final User user = context.getCurrentUser();

            if (null != verifiedCentralAdmin
                    && user.equals(verifiedCentralAdmin)) {
            // certificate were cached
            return true; }

            // since no cert were cached, check current users group instaed
            final int groupId = getCommuneUserBusiness()
                    .getRootAdministratorGroupID();
            final GroupHome home = (GroupHome) IDOLookup.getHome(Group.class);
            final Group communeGroup = home.findByPrimaryKey(new Integer(
                    groupId));
            final Collection usersGroups = getUserBusiness().getUserGroups(
                    ((Integer) user.getPrimaryKey()).intValue());
            if (usersGroups != null
                    && communeGroup != null
                    && (usersGroups.contains(communeGroup) || user
                            .getPrimaryKey().equals(new Integer(1)))) {
                // user is allaowed, cache certificate and return true
                context.setSessionAttribute(sessionKey, user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addSmallText(final Table table, final String string,
            final int col, final int row) {
        table
                .add(
                        getSmallText(null != string
                                && !string.equals(null + "") ? string : ""),
                        col, row);
    }

    private boolean isSchoolUserAllowed(final IWContext context) {
        try {
            final School provider = getSchoolByLoggedInUser(context);
            final User user = context.getCurrentUser();
            final SchoolUserHome home = (SchoolUserHome) IDOLookup
                    .getHome(SchoolUser.class);
            final Collection schoolUsers = home.findBySchoolAndUser(provider,
                    user);
            for (Iterator i = schoolUsers.iterator(); i.hasNext();) {
                final SchoolUser schoolUser = (SchoolUser) i.next();
                if (SchoolUserBMPBean.USER_TYPE_HEADMASTER == schoolUser
                        .getUserType()
                        || schoolUser.isEconomicalResponsible()) { return true; }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private Collection getSchoolCategoryIdsManagedByLoggedOnUser(
            final IWContext context) {
        final Collection categoryIds = new HashSet();
        try {
            final School provider = getSchoolByLoggedInUser(context);
            if (null != provider) {
                final Collection schoolTypes = provider.getSchoolTypes();
                for (Iterator i = schoolTypes.iterator(); i.hasNext();) {
                    final SchoolType schoolType = (SchoolType) i.next();
                    categoryIds.add(schoolType.getSchoolCategory());
                }
            }
        } catch (Exception e) {
            // no problem, ignore this
        }
        return categoryIds;
    }

    private void addOperationalFieldDropdown(final IWContext context,
            final Table table, final int row) throws RemoteException {
        int col = 1;
        addSmallHeader(table, col++, row, MAIN_ACTIVITY_KEY,
                MAIN_ACTIVITY_DEFAULT, ":");
        table.mergeCells(col, row, table.getColumns() - 1, row);
        final Collection schoolCategoryIds = getSchoolCategoryIdsManagedByLoggedOnUser(context);
        if (1 == schoolCategoryIds.size()) {
            final String schoolCategoryId = ""
                    + schoolCategoryIds.iterator().next();
            if (null != schoolCategoryId && 0 < schoolCategoryId.length()) {
                addSmallText(table, getSchoolCategoryName(schoolCategoryId),
                        col++, row);
                getSession().setOperationalField(schoolCategoryId);
            }
        } else {
            String operationalField = getSession().getOperationalField();
            operationalField = operationalField == null ? "" : operationalField;
            final OperationalFieldsMenu dropdown = new OperationalFieldsMenu();
            if (context.isParameterSet(ACTION_KEY)) {
                dropdown.setParameter(LAST_ACTION_KEY, context
                        .getParameter(ACTION_KEY));
            } else if (context.isParameterSet(LAST_ACTION_KEY)) {
                dropdown.maintainParameter(LAST_ACTION_KEY);
            }
            table.add(dropdown, col++, row);
        }
    }

    private void addOperationalFieldRow(final Table table,
            final PaymentHeader header, final int row) throws RemoteException {
        int col = 1;
        addSmallHeader(table, col++, row, MAIN_ACTIVITY_KEY,
                MAIN_ACTIVITY_DEFAULT, ":");
        table.mergeCells(col, row, table.getColumns() - 1, row);
        addSmallText(table,
                getSchoolCategoryName(header.getSchoolCategoryID()), col++, row);
        final String schoolCategory = header.getSchoolCategoryID();
        if (null != schoolCategory && 0 < schoolCategory.length()) {
            getSession().setOperationalField(schoolCategory);
        }
    }

    private Table createTable(final int columnCount) {
        final Table table = new Table();
        table.setCellpadding(getCellpadding());
        table.setCellspacing(getCellspacing());
        table.setWidth(Table.HUNDRED_PERCENT);
        table.setColumns(columnCount);
        return table;
    }

    private void logUnexpectedException(final IWContext context,
            final Exception exception) {
        final StringBuffer message = new StringBuffer();
        message.append("Exception caught in " + getClass().getName() + " "
                + (new java.util.Date()) + '\n');
        message.append("Parameters:\n");
        final java.util.Enumeration enum = context.getParameterNames();
        while (enum.hasMoreElements()) {
            final String key = (String) enum.nextElement();
            message.append('\t' + key + "='" + context.getParameter(key)
                    + "'\n");
        }
        logWarning(message.toString());
        final java.io.StringWriter sw = new java.io.StringWriter();
        exception.printStackTrace(new java.io.PrintWriter(sw, true));
        logWarning(sw.toString());
        add("Det inträffade ett fel. Försök igen senare.");
    }

    private ListTable getPostingListTable(final String postingString)
            throws RemoteException {
        final PostingField[] fields = getCurrentPostingFields();
        final ListTable result = new ListTable(this, fields.length);
        int offset = 0;
        for (int i = 0; i < fields.length; i++) {
            final StringBuffer title = new StringBuffer();
            final StringBuffer value = new StringBuffer();
            final PostingField field = fields[i];
            if (null != field) {
                title.append(field.getFieldTitle());
                if (null != postingString) {
                    final int endPosition = min(offset + field.getLen(),
                            postingString.length());
                    value.append(postingString.substring(offset, endPosition)
                            .trim());
                    offset = endPosition;
                }
            }
            result.setHeader(title.toString(), i + 1);
            result.add(getSmallText(value.toString()));
        }
        return result;
    }

    private int min(final int a, final int b) {
        return a < b ? a : b;
    }

    private PostingField[] getCurrentPostingFields() throws RemoteException {
        final PostingBusiness business = getPostingBusiness();
        final Date now = new Date(System.currentTimeMillis());
        final Collection fields = business.getAllPostingFieldsByDate(now);
        final PostingField[] array = new PostingField[0];
        return fields != null ? (PostingField[]) fields.toArray(array) : array;
    }

    private TextInput getStyledWideInput(final String key, final String value) {
        final TextInput input = getStyledInput(key, value);
        input.setLength(48);
        return input;
    }

    private void addStyledWideInput(final java.util.Map map, final String key,
            final String value) {
        final TextInput input = getStyledWideInput(key, null != value
                && !value.equals(null + "") ? value : "");
        map.put(key, input);
    }

    private void addStyledInput(final java.util.Map map, final String key,
            final float number) {
        final TextInput input = getStyledInput(key, roundAmount(number) + "");
        map.put(key, input);
    }

    private void addSmallText(final java.util.Map map, final String mapKey,
            final String localKey, final String localDefault) {
        final String value = null != localKey && null != localDefault ? localize(
                localKey, localDefault)
                : "";
        addSmallText(map, mapKey, value);
    }

    private void addSmallText(final java.util.Map map, final String key,
            final String value) {
        map.put(key,
                getSmallText(null != value && !value.equals(null + "") ? value
                        : ""));
    }

    private void addSmallText(final java.util.Map map, final String key,
            final long value) {
        map.put(key, getSmallText(-1 != value ? integerFormatter.format(value)
                : "0"));
    }

    private void addSmallText(final java.util.Map map, final String key,
            final Date date) {
        map.put(key, getSmallText(null != date ? dateFormatter.format(date)
                : ""));
    }

    private void addSmallPeriodText(final java.util.Map map, final String key,
            final Date date) {
        map.put(key, getSmallText(null != date ? periodFormatter.format(date)
                : ""));
    }

    private static Integer getIntegerParameter(final IWContext context,
            final String key) {
        final String rawString = context.getParameter(key);
        if (null == rawString) return null;
        try {
            final Integer result = new Integer(rawString);
            return result;
        } catch (final NumberFormatException exception) {
            return null;
        }
    }

    private static void setColumnWidthsEqual(final Table table) {
        final int columnCount = table.getColumns();
        final int percentageInt = 100 / columnCount;
        final String percentageString = percentageInt + "%";
        for (int i = 1; i <= columnCount; i++) {
            table.setColumnWidth(i, percentageString);
        }
    }

    private TextInput getStyledInput(final String key, final String value) {
        final TextInput input = (TextInput) getStyledInterface(new TextInput(
                key));
        input.setLength(12);
        if (null != value) {
            input.setValue(value);
        }
        return input;
    }

    private void addCancelButton(final Table table, final int col,
            final int row, final int actionId) {
        table.add(Text.getNonBrakingSpace(), col, row);
        table.add(getSubmitButton(actionId, CANCEL_KEY, CANCEL_DEFAULT), col,
                row);
    }

    private void addSmallHeader(final Table table, final int col,
            final int row, final String key, final String defaultString,
            final String suffix) {
        if (null != key) {
            final String localizedString = localize(key, defaultString)
                    + suffix;
            table.add(getSmallHeader(localizedString), col, row);
        }
    }

    private SubmitButton getSubmitButton(final String action, final String key,
            final String defaultName) {
        return (SubmitButton) getButton(new SubmitButton(localize(key,
                defaultName), ACTION_KEY, action));
    }

    private School getSchoolByLoggedInUser(final IWContext context)
            throws RemoteException {
        final User user = context.getCurrentUser();
        School school = null;
        if (null != user) {
            final SchoolUserBusiness business = getSchoolUserBusiness();
            try {
                final Collection schoolIds = business.getSchools(user);
                if (!schoolIds.isEmpty()) {
                    final Object schoolId = schoolIds.iterator().next();
                    school = getSchoolBusiness().getSchool(schoolId);
                }
            } catch (FinderException e) {
                // no problem, no school found
            }
        }
        return school;
    }

    long roundAmount(final float f) {
        return se.idega.idegaweb.commune.accounting.business.AccountingUtil
                .roundAmount(f);
    }

    private String getFormattedAmount(final float f) {
        return f == -1.0f ? "0" : integerFormatter.format(roundAmount(f));
    }

    public ICPage getProviderAuthorizationPage() {
        return providerAuthorizationPage;
    }

    public void setProviderAuthorizationPage(final ICPage page) {
        providerAuthorizationPage = page;
    }

    public ICPage getCreatePaymentPage() {
        return createPaymentPage;
    }

    public void setCreatePaymentPage(final ICPage page) {
        createPaymentPage = page;
    }

    private UserBusiness getUserBusiness() throws RemoteException {
        return (UserBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), UserBusiness.class);
    }

    private SchoolUserBusiness getSchoolUserBusiness() throws RemoteException {
        return (SchoolUserBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), SchoolUserBusiness.class);
    }

    private SchoolBusiness getSchoolBusiness() throws RemoteException {
        return (SchoolBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), SchoolBusiness.class);
    }

    private ExportBusiness getExportBusiness() throws RemoteException {
        return (ExportBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), ExportBusiness.class);
    }

    private PostingBusiness getPostingBusiness() throws RemoteException {
        return (PostingBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), PostingBusiness.class);
    }

    private InvoiceBusiness getInvoiceBusiness() throws RemoteException {
        return (InvoiceBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), InvoiceBusiness.class);
    }

    CheckAmountBusiness getCheckAmountBusiness() throws RemoteException {
        return (CheckAmountBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), CheckAmountBusiness.class);
    }

    CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
        return (CommuneUserBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), CommuneUserBusiness.class);
    }
}