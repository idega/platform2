package se.idega.idegaweb.commune.accounting.invoice.presentation;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.business.BillingThread;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRule;

/**
 * PaymentRecordMaintenance is an IdegaWeb block were the user can search, view
 * and edit payment records.
 * <p>
 * Last modified: $Date: 2003/11/26 12:40:27 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson</a>
 * @version $Revision: 1.22 $
 * @see com.idega.presentation.IWContext
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.data
 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock
 */
public class PaymentRecordMaintenance extends AccountingBlock {
    public static final String PREFIX = "cacc_payrec_";

    private static final String ADJUSTED_SIGNATURE_KEY = PREFIX + "adjusted_signature";
    private static final String ADJUSTMENT_DATE_DEFAULT = "Just.datum";
    private static final String ADJUSTMENT_DATE_KEY = PREFIX + "adjustment_date";
    private static final String ADJUSTMENT_SIGNATURE_DEFAULT = "Just.sign";
    private static final String ADJUSTMENT_SIGNATURE_KEY = PREFIX + "adjustment_signature";
    private static final String AMOUNT_DEFAULT = "Belopp";
    private static final String AMOUNT_KEY = PREFIX + "amount";
    private static final String CHECK_AMOUNT_DEFAULT = "Checkbelopp";
    private static final String CHECK_AMOUNT_KEY = PREFIX + "check_amount";
    private static final String CHECK_PERIOD_DEFAULT = "Checkperiod";
    private static final String CHECK_PERIOD_KEY = PREFIX + "check_period";
    private static final String CREATED_SIGNATURE_KEY = PREFIX + "created_signature";
    private static final String DATE_ADJUSTED_DEFAULT = "Justeringsdag";
    private static final String DATE_ADJUSTED_KEY = PREFIX + "date_adjusted";
    private static final String DATE_CREATED_DEFAULT = "Skapandedag";
    private static final String DATE_CREATED_KEY = PREFIX + "date_created";
    private static final String DAYS_DEFAULT = "Days";
    private static final String DAYS_KEY = PREFIX + "days";
    private static final String DELETE_ROW_DEFAULT = "Ta bort post";
    private static final String DELETE_ROW_KEY = PREFIX + "delete_invoice_compilation";
    private static final String DETAILED_PAYMENT_RECORDS_DEFAULT = "Detaljutbetalningsrader";
    private static final String DETAILED_PAYMENT_RECORDS_KEY = PREFIX + "detailed_payment_records";
    private static final String DOUBLE_POSTING_DEFAULT = "Motkontering";
    private static final String DOUBLE_POSTING_KEY = PREFIX + "double_posting";
    private static final String EDIT_PAYMENT_RECORD_DEFAULT = "Ändra utbetalningspost";
    private static final String EDIT_PAYMENT_RECORD_KEY = PREFIX + "edit_payment_record";
    private static final String END_PERIOD_KEY = PREFIX + "end_period";
    //private static final String HEADER_KEY = PREFIX + "end_period";
    private static final String GO_BACK_DEFAULT = "Tillbaka";
    private static final String GO_BACK_KEY = PREFIX + "go_back";
    private static final String HEADER_KEY = PREFIX + "end_period";
    private static final String MAIN_ACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAIN_ACTIVITY_KEY = PREFIX + "main_activity";
    private static final String MANAGEMENT_TYPE_DEFAULT = "Bolagsform";
    private static final String MANAGEMENT_TYPE_KEY = PREFIX + "management_type";
    private static final String NAME_DEFAULT = "Namn";
    private static final String NAME_KEY = PREFIX + "name";
    public static final String NOTE_DEFAULT = "Anmärkning";
    public static final String NOTE_KEY = PREFIX + "note";
    public static final String NO_OF_PLACEMENTS_DEFAULT = "Antal plac";
    public static final String NO_OF_PLACEMENTS_KEY = PREFIX + "no_of_placements";
    private static final String NO_PAYMENT_RECORDS_FOUND_DEFAULT = "Inga utbetalningsrader hittades";
    private static final String NO_PAYMENT_RECORDS_FOUND_KEY = PREFIX + "no_payment_records_found";
    private static final String NUMBER_OF_PLACEMENTS_DEFAULT = "Placeringar";
    private static final String NUMBER_OF_PLACEMENTS_KEY = PREFIX + "number_of_placements";
    private static final String OWN_POSTING_DEFAULT = "Egen kontering";
    private static final String OWN_POSTING_KEY = PREFIX + "own_posting";
    private static final String PAYMENT_HEADER_DEFAULT = "Utbetalning";
    private static final String PAYMENT_HEADER_KEY = PREFIX + "payment_header";
    private static final String PAYMENT_RECORD_DEFAULT = "Utbetalningspost";
    private static final String PAYMENT_RECORD_KEY = PREFIX + "payment_record";
    private static final String PAYMENT_RECORD_UPDATED_DEFAULT = "Utbetalninsraden är nu uppdaterad";
    private static final String PAYMENT_RECORD_UPDATED_KEY = PREFIX + "payment_record_updated";
    private static final String PAYMENT_TEXT_KEY = PREFIX + "payment_text";
    public static final String PERIOD_DEFAULT = "Period";
    public static final String PERIOD_KEY = PREFIX + "period";
    private static final String PIECE_AMOUNT_DEFAULT = "Styckepris";
    private static final String PIECE_AMOUNT_KEY = PREFIX + "piece_amount";
    public static final String PLACEMENT_DEFAULT = "Placering";
    public static final String PLACEMENT_KEY = PREFIX + "placement";
    private static final String PLACEMENT_PERIOD_DEFAULT = "Plac.period";
    private static final String PLACEMENT_PERIOD_KEY = PREFIX + "placement_period";
    private static final String PROVIDER_DEFAULT = "Anordnare";
    private static final String PROVIDER_KEY = PREFIX + "provider";
    private static final String REGULATION_SPEC_TYPE_DEFAULT = "Regelspec.typ";
    private static final String REGULATION_SPEC_TYPE_KEY = PREFIX + "regulation_spec_type";
    private static final String SAVE_EDITS_DEFAULT = "Spara ändringar";
    private static final String SAVE_EDITS_KEY = PREFIX + "save_edits";
    private static final String SCHOOL_CLASS_DEFAULT = "Grupp";
    private static final String SCHOOL_CLASS_KEY = PREFIX + "school_class";
    private static final String SCHOOL_TYPE_DEFAULT = "Verksamhet";
    private static final String SCHOOL_TYPE_KEY = PREFIX + "school_type";
    private static final String SCHOOL_YEAR_DEFAULT = "Skolår";
    private static final String SCHOOL_YEAR_KEY = PREFIX + "school_year";
    private static final String SEARCH_DEFAULT = "Sök";
    private static final String SEARCH_KEY = PREFIX + "search";
    private static final String SIGNATURE_DEFAULT = "Sigantur";
    private static final String SIGNATURE_KEY = PREFIX + "signature";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = PREFIX + "ssn";
    private static final String START_PERIOD_KEY = PREFIX + "start_period";
    private static final String STATUS_DEFAULT = "Status";
    private static final String STATUS_KEY = PREFIX + "status";
    public static final String TOTAL_AMOUNT_DEFAULT = "Totalbelopp";
    public static final String TOTAL_AMOUNT_INDIVIDUALS_DEFAULT = "Totalt antal individer";
    public static final String TOTAL_AMOUNT_INDIVIDUALS_KEY = PREFIX + "total_amount_individuals";
    public static final String TOTAL_AMOUNT_KEY = PREFIX + "total_amount";
    public static final String TOTAL_AMOUNT_PLACEMENTS_DEFAULT = "Totalt antal placeringar";
    public static final String TOTAL_AMOUNT_PLACEMENTS_KEY = PREFIX + "total_amount_placements";
    public static final String TOTAL_AMOUNT_VAT_DEFAULT = "Totalbelopp, moms";
    public static final String TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT = "Totalbelopp, exklusive moms";
    public static final String TOTAL_AMOUNT_VAT_EXCLUDED_KEY = PREFIX + "total_amount_vat_excluded";
    public static final String TOTAL_AMOUNT_VAT_KEY = PREFIX + "total_amount_vat";
    private static final String TRANSACTION_DATE_DEFAULT = "Bokföringsdag";
    private static final String TRANSACTION_DATE_KEY = PREFIX + "transaction_date";
    private static final String VAT_AMOUNT_DEFAULT = "Momsbelopp";
    private static final String VAT_AMOUNT_KEY = PREFIX + "vat_amount";
    private static final String VAT_RULE_DEFAULT = "Momstyp";
    private static final String VAT_RULE_KEY = PREFIX + "vat_rule";

    private static final String ACTION_KEY = PREFIX + "action_key";
    private static final String LAST_ACTION_KEY = PREFIX + "last_action_key";
	private static final int ACTION_SHOW_PAYMENT = 0,
            ACTION_SHOW_RECORD_DETAILS = 1,
            ACTION_SHOW_EDIT_RECORD_FORM = 2,
            ACTION_SHOW_RECORD = 3,
            ACTION_SAVE_RECORD = 4;

    private static final SimpleDateFormat periodFormatter
        = new SimpleDateFormat ("yyMM");
    private static final SimpleDateFormat dateFormatter
        = new SimpleDateFormat ("yyyy-MM-dd");
    
	/**
	 * Init is the event handler of InvoiceCompilationEditor
	 *
	 * @param context session data like user info etc.
	 */
	public void init (final IWContext context) {
		try {
            int actionId = ACTION_SHOW_PAYMENT;

            try {
                actionId = Integer.parseInt (context.getParameter (ACTION_KEY));
            } catch (final Exception dummy) {
                try {
                    actionId = Integer.parseInt (context.getParameter
                                                 (LAST_ACTION_KEY));
                } catch (final Exception dummy2) {
                    // do nothing, actionId is default
                }
            }
            
			switch (actionId) {
                case ACTION_SHOW_RECORD_DETAILS:
                    showRecordDetails (context);
                    break;
                    
                case ACTION_SHOW_EDIT_RECORD_FORM:
                    showEditRecordForm (context);
                    break;
                    
                case ACTION_SHOW_RECORD:
                    showRecord (context);
                    break;
                    
                case ACTION_SAVE_RECORD:
                    saveRecord (context);
                    break;
                    
                default:
                    showPayment (context);
					break;					
			}
            
		} catch (Exception exception) {
            logUnexpectedException (context, exception);
		}
	}
    
    private void saveRecord (final IWContext context)
        throws RemoteException, FinderException {
        // get updated values
        final User currentUser = context.getCurrentUser ();
        final Integer amount = getIntegerParameter (context, AMOUNT_KEY);
        final Integer placementCount
                = getIntegerParameter (context, NUMBER_OF_PLACEMENTS_KEY);
        final Integer pieceAmount = getIntegerParameter (context,
                                                         PIECE_AMOUNT_KEY);
        final Integer vatAmount = getIntegerParameter (context, VAT_AMOUNT_KEY);

        final Date period = getPeriodParameter (context, PERIOD_KEY);
        final String doublePosting = getPostingString (context,
                                                       DOUBLE_POSTING_KEY);
        final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
        final String paymentText = context.getParameter (PAYMENT_TEXT_KEY);
        final String note = context.getParameter (NOTE_KEY);
        final String regulationSpecType
                = context.getParameter (REGULATION_SPEC_TYPE_KEY);
        final Integer vatRule = getIntegerParameter (context, VAT_RULE_KEY);
        final PaymentRecord record = getPaymentRecord (context);

        // set updated values
        if (null != amount) record.setTotalAmount (amount.floatValue ());
        if (null != placementCount) record.setPlacements
                                            (placementCount.intValue ());
        if (null != pieceAmount) record.setPieceAmount
                                         (pieceAmount.floatValue ());
        if (null != vatAmount) record.setTotalAmountVAT
                                       (vatAmount.floatValue ());
        record.setChangedBy (getSignature (currentUser));
        record.setDateChanged (new java.sql.Date (System.currentTimeMillis ()));
        if (null != period) record.setPeriod (period);
        if (null != doublePosting) record.setDoublePosting (doublePosting);
        if (null != ownPosting) record.setOwnPosting (ownPosting);
        if (null != paymentText) record.setPaymentText (paymentText);
        if (null != note) record.setNotes (note);
        record.setRuleSpecType (regulationSpecType);
        record.setVATType (vatRule.intValue ());

        // store updated record
        record.store ();

        //render
        final String [][] parameters =
                {{ACTION_KEY, ACTION_SHOW_PAYMENT + "" },
                 { PAYMENT_HEADER_KEY, record.getPaymentHeader () + ""}};
        final Table table = getConfirmTable
                (PAYMENT_RECORD_UPDATED_KEY,
                 PAYMENT_RECORD_UPDATED_DEFAULT, parameters);
        add (createMainTable (EDIT_PAYMENT_RECORD_KEY,
                              EDIT_PAYMENT_RECORD_DEFAULT, table));        
    }

    private void showEditRecordForm (final IWContext context)
        throws RemoteException, FinderException {
        final PaymentRecord record = getPaymentRecord (context);
        final java.util.Map map = new java.util.HashMap ();
        map.put (ADJUSTED_SIGNATURE_KEY,
                 getSmallSignature (record.getChangedBy ()));
        addSmallText (map, ADJUSTED_SIGNATURE_KEY, record.getChangedBy ());
        addStyledInput (map, AMOUNT_KEY, record.getTotalAmount ());
        map.put (CREATED_SIGNATURE_KEY,
                 getSmallSignature (record.getCreatedBy ()));
        addSmallText (map, DATE_ADJUSTED_KEY, record.getDateChanged ());
        addSmallText (map, DATE_CREATED_KEY, record.getDateCreated ());
        addStyledInput (map, NOTE_KEY, record.getNotes ());
        addStyledInput (map, PAYMENT_TEXT_KEY, record.getPaymentText ());
        map.put (PERIOD_KEY, getStyledInput (PERIOD_KEY, getFormattedPeriod
                                             (record.getPeriod ())));
        addStyledInput (map, PIECE_AMOUNT_KEY, record.getPieceAmount ());
        addStyledInput (map, NUMBER_OF_PLACEMENTS_KEY, record.getPlacements ());
        addSmallText (map, STATUS_KEY, record.getStatus () + "");
        addSmallText (map, TRANSACTION_DATE_KEY, record.getDateTransaction ());
        addStyledInput (map, VAT_AMOUNT_KEY, record.getTotalAmountVAT ());

        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final DropdownMenu regulationSpecTypeDropdown = getLocalizedDropdown
                (business.getAllRegulationSpecTypes ());
        map.put (REGULATION_SPEC_TYPE_KEY, regulationSpecTypeDropdown);
        final String regulationSpecType = record.getRuleSpecType ();
        if (null != regulationSpecType) {
            regulationSpecTypeDropdown.setSelectedElement (regulationSpecType);
        }
        final PresentationObject ownPostingForm = getPostingParameterForm
                (context, OWN_POSTING_KEY, record.getOwnPosting ());
        map.put (OWN_POSTING_KEY, ownPostingForm);
        final PresentationObject doublePostingForm = getPostingParameterForm
                (context, DOUBLE_POSTING_KEY, record.getDoublePosting ());
        map.put (DOUBLE_POSTING_KEY, doublePostingForm);
        final DropdownMenu vatRuleDropdown = getLocalizedDropdown
                (business.getAllVatRules ());
        map.put (VAT_RULE_KEY, vatRuleDropdown);
        final int vatRuleId = record.getVATType ();
        if (0 < vatRuleId) {
            vatRuleDropdown.setSelectedElement (vatRuleId + "");
        }
        try {
            final PaymentHeader header = getPaymentHeader (context);
            final School school = header.getSchool ();
            final SchoolManagementType managementType
                    = school.getManagementType ();
            //--verksamhet
            //--skolår/timmar
            //--grupp
            addSmallText (map, PROVIDER_KEY, school.getName ());
            addSmallText (map, MANAGEMENT_TYPE_KEY,
                          localize (managementType.getLocalizedKey (),
                                    managementType.getName ()));
        } catch (Exception e) {
            logWarning ("Missing school properties i payment record "
                        + record.getPrimaryKey ());
            log (e);
        }

        map.put (HEADER_KEY,
                 getSmallHeader (localize (EDIT_PAYMENT_RECORD_KEY,
                                           EDIT_PAYMENT_RECORD_DEFAULT)));
        map.put (ACTION_KEY, getSubmitButton (ACTION_SAVE_RECORD,
                                              SAVE_EDITS_KEY,
                                              SAVE_EDITS_DEFAULT));

        renderRecordDetailsOrForm (context, map);
    }
    
    private void showRecord (final IWContext context) 
        throws RemoteException, FinderException {
        final PaymentRecord record = getPaymentRecord (context);
        final java.util.Map map = new java.util.HashMap ();
        map.put (ADJUSTED_SIGNATURE_KEY,
                 getSmallSignature (record.getChangedBy ()));
        addSmallText (map, ADJUSTED_SIGNATURE_KEY, record.getChangedBy ());
        addSmallText (map, AMOUNT_KEY, (long) record.getTotalAmount ());
        map.put (CREATED_SIGNATURE_KEY,
                 getSmallSignature (record.getCreatedBy ()));
        addSmallText (map, DATE_ADJUSTED_KEY, record.getDateChanged ());
        addSmallText (map, DATE_CREATED_KEY, record.getDateCreated ());
        map.put (DOUBLE_POSTING_KEY,
                 getPostingListTable (context, record.getDoublePosting ()));
        addSmallText (map, NOTE_KEY, record.getNotes ());
        map.put (OWN_POSTING_KEY,
                 getPostingListTable (context, record.getOwnPosting ()));
        addSmallText (map, PAYMENT_TEXT_KEY, record.getPaymentText ());
        addSmallPeriodText (map, PERIOD_KEY, record.getPeriod ());
        addSmallText (map, PIECE_AMOUNT_KEY, (long) record.getPieceAmount ());
        addSmallText (map, NUMBER_OF_PLACEMENTS_KEY, record.getPlacements ());
        addSmallText (map, STATUS_KEY, record.getStatus () + "");
        addSmallText (map, TRANSACTION_DATE_KEY, record.getDateTransaction ());
        addSmallText (map, VAT_AMOUNT_KEY, (long) record.getTotalAmountVAT ());
        final String ruleSpecType = record.getRuleSpecType ();
        addSmallText (map, REGULATION_SPEC_TYPE_KEY,
                      localize (ruleSpecType, ruleSpecType));
        if (0 < record.getVATType ()) {
            final InvoiceBusiness business
                    = (InvoiceBusiness) IBOLookup.getServiceInstance
                    (context, InvoiceBusiness.class);
            final VATRule rule = business.getVatRule (record.getVATType ());
            final String ruleName = rule.getVATRule ();
            map.put (VAT_RULE_KEY, getSmallText (localize (ruleName,
                                                              ruleName)));
        }

        try {
            final PaymentHeader header = getPaymentHeader (context);
            final School school = header.getSchool ();
            final SchoolManagementType managementType
                    = school.getManagementType ();
            //--verksamhet
            //--skolår/timmar
            //--grupp
            addSmallText (map, PROVIDER_KEY, school.getName ());
            addSmallText (map, MANAGEMENT_TYPE_KEY,
                          localize (managementType.getLocalizedKey (),
                                    managementType.getName ()));
        } catch (Exception e) {
            logWarning ("Missing school properties i payment record "
                        + record.getPrimaryKey ());
            log (e);
        }

        map.put (HEADER_KEY, getSmallHeader
                 (localize (PAYMENT_RECORD_KEY, PAYMENT_RECORD_DEFAULT)));

        renderRecordDetailsOrForm (context, map);
    }
    
    private void showRecordDetails (final IWContext context)
        throws RemoteException, javax.ejb.FinderException {
        // get business objects
        final InvoiceBusiness business = (InvoiceBusiness) IBOLookup
                .getServiceInstance (context, InvoiceBusiness.class);
        final SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                .getServiceInstance (context, SchoolBusiness.class);

        // get home objects
        final PaymentHeaderHome headerHome = business.getPaymentHeaderHome ();
        final SchoolCategoryHome categoryHome
                = schoolBusiness.getSchoolCategoryHome ();
        final InvoiceRecordHome home = business.getInvoiceRecordHome ();
        
        // get data objects
        final PaymentRecord record = getPaymentRecord (context);
        final PaymentHeader header = headerHome.findByPrimaryKey
                (new Integer (record.getPaymentHeader ()));
        final SchoolCategory category
                = categoryHome.findByPrimaryKey (header.getSchoolCategoryID ());
        final School school = schoolBusiness.getSchool
                (new Integer (header.getSchoolID ()));
        final Collection invoiceRecords
                = home.findByPaymentRecord (record);
        
        // render
        final Table table = createTable (2);
        table.setColumnWidth (1, "25%");
        int row = 1; int col = 1;
        addSmallHeader (table, col++, row, MAIN_ACTIVITY_KEY,
                        MAIN_ACTIVITY_DEFAULT, ":");
        addSmallText (table, col++, row++, category.getLocalizedKey (),
                      category.getName ());
        col = 1;
        addSmallHeader (table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        addSmallText (table, col++, row++, school.getName ());
        col = 1;
        addSmallHeader (table, col++, row, PLACEMENT_KEY, PLACEMENT_DEFAULT,
                        ":");
        addSmallText (table, col++, row++, record.getPaymentText ());
        col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        addSmallText (table, col++, row++,
                      getFormattedPeriod (record.getPeriod ()));
        table.setHeight (row++, 6);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getDetailedPaymentRecordListTable
                   (context, invoiceRecords), 1, row++);
        table.setHeight (row++, 6);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getDetailedPaymentRecordSummaryTable
                   (context, invoiceRecords), 1, row++);

        // add to form
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (localize (DETAILED_PAYMENT_RECORDS_KEY,
                                        DETAILED_PAYMENT_RECORDS_DEFAULT),
                              outerTable));
        
    }

    private void showPayment (final IWContext context)
        throws RemoteException, javax.ejb.FinderException {
        final Table table = createTable (3);
        setColumnWidthsEqual (table);
        int row = 2; // first row is reserved for setting column widths
        addOperationalFieldDropdown (context, table, row++);
        addProviderDropdown (context, table, row++);
        addPeriodForm (table, row);
        table.setAlignment (table.getColumns (), row,
                            Table.HORIZONTAL_ALIGN_RIGHT);
        table.add (getSubmitButton (ACTION_SHOW_PAYMENT + "",
                                    SEARCH_KEY, SEARCH_DEFAULT),
                   table.getColumns (), row++);
        final String schoolCategory = getSession().getOperationalField ();
        final String providerId = context.getParameter (PROVIDER_KEY);
        if (null != schoolCategory && null != providerId
            && 0 < providerId.length ()) {
            final Date startPeriod = getPeriodParameter (context,
                                                         START_PERIOD_KEY);
            final Date endPeriod = getPeriodParameter (context, END_PERIOD_KEY);
            final InvoiceBusiness business = (InvoiceBusiness) IBOLookup
                    .getServiceInstance(context, InvoiceBusiness.class);
            final PaymentRecord [] records = business
                    .getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
                    (schoolCategory, new Integer (providerId),
                     new java.sql.Date (startPeriod.getTime ()),
                     new java.sql.Date (endPeriod.getTime ()));
            table.mergeCells (1, row, table.getColumns (), row);
            if (0 < records.length) {
                table.add (getPaymentRecordListTable (records), 1, row++);
                table.mergeCells (1, row, table.getColumns (), row);
                table.add (getPaymentSummaryTable (context, records, business),
                           1, row++);
            } else {
                addSmallText (table, 1, row++, NO_PAYMENT_RECORDS_FOUND_KEY,
                              NO_PAYMENT_RECORDS_FOUND_DEFAULT);
            }
        }
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable
             (localize (PAYMENT_HEADER_KEY, PAYMENT_HEADER_DEFAULT),
              outerTable));
    }

    private Table getDetailedPaymentRecordListTable
        (final IWContext context, final Collection invoiceRecords)
        throws RemoteException, javax.ejb.FinderException {
        // set up header row
        final String [][] columnNames =
                {{ SSN_KEY, SSN_DEFAULT },
                 { NAME_KEY, NAME_DEFAULT },
                 { CHECK_PERIOD_KEY, CHECK_PERIOD_DEFAULT },
                 { DAYS_KEY, DAYS_DEFAULT },
                 { CHECK_AMOUNT_KEY, CHECK_AMOUNT_DEFAULT },
                 { PLACEMENT_PERIOD_KEY, PLACEMENT_PERIOD_DEFAULT },
                 { ADJUSTMENT_DATE_KEY, ADJUSTMENT_DATE_DEFAULT },
                 { ADJUSTMENT_SIGNATURE_KEY, ADJUSTMENT_SIGNATURE_DEFAULT }};
        final Table table = createTable (columnNames.length);
        table.setColumns (columnNames.length);
        setIconColumnWidth (table);
        int row = 1;
        table.setRowColor(row, getHeaderColor ());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader (table, i + 1, row, columnNames [i][0],
                            columnNames [i][1]);
        }
        row++;

        //render
        final SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                .getServiceInstance (context, SchoolBusiness.class);
        final SchoolClassMemberHome memberHome
                = schoolBusiness.getSchoolClassMemberHome ();
        for (Iterator i = invoiceRecords.iterator (); i.hasNext ();) {
            final InvoiceRecord invoiceRecord = (InvoiceRecord) i.next ();
			showDetailedPaymentRecordOnARow (table, row++, invoiceRecord,
                                             memberHome);
        }
        
        return table;
    }

    private Table getDetailedPaymentRecordSummaryTable
        (final IWContext context, final Collection invoiceRecords)
        throws RemoteException, javax.ejb.FinderException {
        final Set placements = new HashSet ();
        final Set individuals = new HashSet ();
        long totalAmountVatExcluded = 0;
        // get home object
        final SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                .getServiceInstance (context, SchoolBusiness.class);
        final SchoolClassMemberHome home
                = schoolBusiness.getSchoolClassMemberHome ();

        // count values for summary
        for (Iterator i = invoiceRecords.iterator (); i.hasNext ();) {
            final InvoiceRecord invoiceRecord = (InvoiceRecord) i.next ();
            final Integer placementId
                    = new Integer (invoiceRecord.getSchoolClassMemberId ());
            final SchoolClassMember placement
                    = home.findByPrimaryKey (placementId);
            final User user = placement.getStudent ();
            placements.add (placementId);
            individuals.add (user.getPrimaryKey ());
            totalAmountVatExcluded += (long) invoiceRecord.getAmount ();
        }

        // render
        final Table table = createTable (3);
        table.setColumnWidth (3, "50%");
        int row = 2; int col = 1;
        addSmallHeader (table, col++, row, TOTAL_AMOUNT_PLACEMENTS_KEY,
                        TOTAL_AMOUNT_PLACEMENTS_DEFAULT, ":");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText (table, col++, row++, placements.size () + "");
        col = 1;
        addSmallHeader (table, col++, row, TOTAL_AMOUNT_INDIVIDUALS_KEY,
                        TOTAL_AMOUNT_INDIVIDUALS_DEFAULT, ":");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText (table, col++, row++, individuals.size () + "");
        col = 1;
        addSmallHeader (table, col++, row, TOTAL_AMOUNT_VAT_EXCLUDED_KEY,
                        TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT, ":");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText (table, col++, row++, totalAmountVatExcluded + "");
        return table;
    }

	private void showDetailedPaymentRecordOnARow
        (final Table table, final int row, final InvoiceRecord record,
         final SchoolClassMemberHome home)
        throws RemoteException, javax.ejb.FinderException {
        final String checkPeriod = getFormattedPeriod
                (record.getPeriodStartCheck ()) + " - "
                + getFormattedPeriod (record.getPeriodEndCheck ());
        final String days = record.getDays () + "";
        final String amount = ((long) record.getAmount ()) + "";
        final String placementPeriod
                = getFormattedPeriod (record.getPeriodStartPlacement ()) + " - "
                + getFormattedPeriod (record.getPeriodEndPlacement ());
        final String dateChanged
                = getFormattedDate (record.getDateChanged ());
        final String changedBy = record.getChangedBy ();
        final Integer memberId = new Integer (record.getSchoolClassMemberId ());
        final SchoolClassMember member = home.findByPrimaryKey (memberId);
        final User user = member.getStudent ();
        final String ssn = formatSsn (user.getPersonalID ());
        final String userName = getUserName (user);

		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
                           : getZebraColor2 ());
		addSmallText (table, col++, row, ssn);
		addSmallText (table, col++, row, userName);
		addSmallText (table, col++, row, checkPeriod);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		addSmallText (table, col++, row, days);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		addSmallText (table, col++, row, amount);
		addSmallText (table, col++, row, placementPeriod);
		addSmallText (table, col++, row, dateChanged);
		addSmallText (table, col++, row, changedBy);
	}

    private Table getPaymentSummaryTable
        (final IWContext context, final PaymentRecord [] records,
         final InvoiceBusiness business)
        throws RemoteException, javax.ejb.FinderException {
        // get home objects
        final SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                .getServiceInstance (context, SchoolBusiness.class);
        final SchoolClassMemberHome placementHome
                = schoolBusiness.getSchoolClassMemberHome ();
        final InvoiceRecordHome home = business.getInvoiceRecordHome ();

        // initialize conters
        int placementCount = 0;
        final Set individuals = new HashSet ();
        long totalAmountVatExcluded = 0;
        long totalAmountVat = 0;

        // count values for summary
        for (int i = 0; i < records.length; i++) {
            placementCount += records [i].getPlacements ();
            totalAmountVatExcluded += records [i].getTotalAmount ();
            totalAmountVat += records [i].getTotalAmountVAT ();
            final Collection invoiceRecords
                    = home.findByPaymentRecord (records [i]);
            for (Iterator j = invoiceRecords.iterator (); j.hasNext ();) {
                final InvoiceRecord invoiceRecord = (InvoiceRecord) j.next ();
                final Integer placementId
                        = new Integer (invoiceRecord.getSchoolClassMemberId ());
                final SchoolClassMember placement
                        = placementHome.findByPrimaryKey (placementId);
                final User user = placement.getStudent ();
                individuals.add (user.getPrimaryKey ());
            }
        }

        // render
        final Table table = createTable (3);
        table.setColumnWidth (3, "50%");
        int row = 2; int col = 1;
        addSmallHeader (table, col++, row, TOTAL_AMOUNT_PLACEMENTS_KEY,
                        TOTAL_AMOUNT_PLACEMENTS_DEFAULT, ":");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText (table, col++, row++, placementCount + "");
        col = 1;
        addSmallHeader (table, col++, row, TOTAL_AMOUNT_INDIVIDUALS_KEY,
                        TOTAL_AMOUNT_INDIVIDUALS_DEFAULT, ":");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText (table, col++, row++, individuals.size () + "");
        col = 1;
        addSmallHeader (table, col++, row, TOTAL_AMOUNT_VAT_EXCLUDED_KEY,
                        TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT, ":");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText (table, col++, row++, totalAmountVatExcluded + "");
        col = 1;
        addSmallHeader (table, col++, row, TOTAL_AMOUNT_VAT_KEY,
                        TOTAL_AMOUNT_VAT_DEFAULT, ":");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addSmallText (table, col++, row++, totalAmountVat + "");
        return table;
    }

    private Table getPaymentRecordListTable (final PaymentRecord [] records)
        throws RemoteException {
        // set up header row
        final String [][] columnNames =
                {{ STATUS_KEY, STATUS_DEFAULT }, { PERIOD_KEY, PERIOD_DEFAULT },
                 { PLACEMENT_KEY, PLACEMENT_DEFAULT },
                 { NO_OF_PLACEMENTS_KEY, NO_OF_PLACEMENTS_DEFAULT },
                 { TOTAL_AMOUNT_KEY, TOTAL_AMOUNT_DEFAULT },
                 { NOTE_KEY, NOTE_DEFAULT }, {"no_text", ""}, {"no_text", ""}};
        final Table table = createTable (columnNames.length);
        table.setColumns (columnNames.length);
        setIconColumnWidth (table);
        int row = 1;
        table.setRowColor(row, getHeaderColor ());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader (table, i + 1, row, columnNames [i][0],
                            columnNames [i][1]);
        }
        row++;

        // show each payment record in a row
        for (int i = 0; i < records.length; i++) {
			showPaymentRecordOnARow (table, row++, records [i]);
        }
        
        return table;
    }

    private static boolean isManualRecord (final PaymentRecord record) {
        final String autoSignature = BillingThread.getBathRunSignatureKey ();
        final String createdBy = record.getCreatedBy ();
        return null == createdBy || !createdBy.equals (autoSignature);
    }

    private Text getSmallSignature (final String string) {
        final StringBuffer result = new StringBuffer ();
        final String autoSignature = BillingThread.getBathRunSignatureKey ();
        if (null != string) {
            if (string.equals (autoSignature)) {
                result.append (localize (string, string));
            } else {
                result.append (string);
            }
        }
        return getSmallText (result.toString ());
    }

	private void showPaymentRecordOnARow
        (final Table table, final int row, final PaymentRecord record) {
        final String recordId = record.getPrimaryKey () + "";
        final String [][] showDetailsLinkParameters
                = {{ ACTION_KEY, ACTION_SHOW_RECORD_DETAILS + "" },
                   { PAYMENT_RECORD_KEY, recordId }};
        final String [][] showRecordLinkParameters = isManualRecord (record)
                ? new String [][] {{ ACTION_KEY,
                                     ACTION_SHOW_EDIT_RECORD_FORM + "" },
                                   { PAYMENT_RECORD_KEY, recordId }}
                : new String [][] {{ ACTION_KEY,
                                     ACTION_SHOW_RECORD + "" },
                                   { PAYMENT_RECORD_KEY, recordId }};
		final char status = record.getStatus ();
        final Date period = record.getPeriod ();
        final String periodText = getFormattedPeriod (period);
        final Link paymentTextLink = createSmallLink (record.getPaymentText (),
                                                      showRecordLinkParameters);
        final Link placementLink = createSmallLink (record.getPlacements() + "",
                                                    showDetailsLinkParameters);
        final long  totalAmount = (long) record.getTotalAmount ();
        final String note = record.getNotes ();
        final Link editLink = createIconLink (getEditIcon (),
                                              showRecordLinkParameters);
		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
                           : getZebraColor2 ());
		addSmallText (table, col++, row, status + "");
		addSmallText (table, col++, row, periodText);
		table.add (paymentTextLink, col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (placementLink, col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		addSmallText (table, col++, row, totalAmount + "");
		addSmallText (table, col++, row, note);
        table.add (editLink, col++, row);
	}

    private PaymentRecord getPaymentRecord (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final Integer recordId = getIntegerParameter (context,
                                                      PAYMENT_RECORD_KEY);
        final PaymentRecordHome recordHome = business.getPaymentRecordHome ();
        return null != recordId ? recordHome.findByPrimaryKey (recordId) : null;
    }
    
    private PaymentHeader getPaymentHeader (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final Integer headerId = context.isParameterSet (PAYMENT_HEADER_KEY)
                ? getIntegerParameter (context, PAYMENT_HEADER_KEY)
                : new Integer (getPaymentRecord (context).getPaymentHeader ());
        final PaymentHeaderHome headerHome = business.getPaymentHeaderHome ();
        return null != headerId ? headerHome.findByPrimaryKey (headerId) : null;
    }
    
    private void renderRecordDetailsOrForm
        (final IWContext context, final java.util.Map presentationObjects)
        throws RemoteException, FinderException  {
        //final PaymentRecord record = getPaymentRecord (context);
        final PaymentHeader header = getPaymentHeader (context);

        // render form/details
        final Table table = createTable (4);
        setColumnWidthsEqual (table);
        int row = 2;
        int col = 1;
        addOperationalFieldRow (table, context, header, row++);
        col = 1;
        addSmallHeader (table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, PROVIDER_KEY, col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, MANAGEMENT_TYPE_KEY,
                        MANAGEMENT_TYPE_DEFAULT, ":");
        addPresentation (table, presentationObjects, MANAGEMENT_TYPE_KEY, col++,
                         row);
        col = 1; row++;
        table.setHeight (row++, 12);
        addSmallHeader (table, col++, row, PLACEMENT_KEY, PLACEMENT_DEFAULT,
                        ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, PAYMENT_TEXT_KEY, col++,
                         row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT,
                        ":");
        addPresentation (table, presentationObjects, PERIOD_KEY, col++,
                         row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DATE_CREATED_KEY,
                        DATE_CREATED_DEFAULT, ":");
        addPresentation (table, presentationObjects, DATE_CREATED_KEY, col++,
                         row);
        addSmallHeader (table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT,
                        ":");
        addPresentation (table, presentationObjects, CREATED_SIGNATURE_KEY,
                         col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DATE_ADJUSTED_KEY,
                        DATE_ADJUSTED_DEFAULT, ":");
        addPresentation (table, presentationObjects, DATE_ADJUSTED_KEY, col++,
                         row);
        addSmallHeader (table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT,
                        ":");
        addPresentation (table, presentationObjects, ADJUSTED_SIGNATURE_KEY,
                         col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, TRANSACTION_DATE_KEY,
                        TRANSACTION_DATE_DEFAULT, ":");
        addPresentation (table, presentationObjects, TRANSACTION_DATE_KEY,
                         col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, STATUS_KEY, STATUS_DEFAULT,
                        ":");
        addPresentation (table, presentationObjects, STATUS_KEY, col++,
                         row);
        col = 1; row++;
        table.setHeight (row++, 12);
        addSmallHeader (table, col++, row, NUMBER_OF_PLACEMENTS_KEY,
                        NUMBER_OF_PLACEMENTS_DEFAULT, ":");
        addPresentation (table, presentationObjects, NUMBER_OF_PLACEMENTS_KEY,
                         col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PIECE_AMOUNT_KEY,
                        PIECE_AMOUNT_DEFAULT, ":");
        addPresentation (table, presentationObjects, PIECE_AMOUNT_KEY, col++,
                         row);
        col = 1; row++;
        addSmallHeader (table, col++, row, AMOUNT_KEY, AMOUNT_DEFAULT, ":");
        addPresentation (table, presentationObjects, AMOUNT_KEY, col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, VAT_AMOUNT_KEY, VAT_AMOUNT_DEFAULT,
                        ":");
        addPresentation (table, presentationObjects, VAT_AMOUNT_KEY, col++,
                         row);
        col = 1; row++;
        table.setHeight (row++, 12);
        addSmallHeader (table, col++, row, NOTE_KEY, NOTE_DEFAULT, ":");
        addPresentation (table, presentationObjects, NOTE_KEY, col++, row);
        col = 1; row++;
        table.setHeight (row++, 12);
        addSmallHeader (table, col++, row, SCHOOL_TYPE_KEY, SCHOOL_TYPE_DEFAULT,
                        ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, SCHOOL_TYPE_KEY, col++,
                         row);
        col = 1; row++;
        addSmallHeader (table, col++, row, SCHOOL_YEAR_KEY, SCHOOL_YEAR_DEFAULT,
                        ":");
        addPresentation (table, presentationObjects, SCHOOL_YEAR_KEY, col++,
                         row);
        col = 1; row++;
        addSmallHeader (table, col++, row, SCHOOL_CLASS_KEY,
                        SCHOOL_CLASS_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, SCHOOL_CLASS_KEY, col++,
                         row);
        col = 1; row++;
        table.setHeight (row++, 12);
        addSmallHeader (table, col++, row, REGULATION_SPEC_TYPE_KEY,
                        REGULATION_SPEC_TYPE_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, REGULATION_SPEC_TYPE_KEY,
                         col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, VAT_RULE_KEY, VAT_RULE_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, VAT_RULE_KEY, col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, OWN_POSTING_KEY, OWN_POSTING_DEFAULT,
                        ":");
        col = 1; row++;
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, OWN_POSTING_KEY, col, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DOUBLE_POSTING_KEY,
                        DOUBLE_POSTING_DEFAULT, ":");
        col = 1; row++;
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, DOUBLE_POSTING_KEY, col,
                         row);
        col = 1; row++;
        table.setHeight (row++, 12);
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, ACTION_KEY, 1, row++);
        final Form form = new Form ();
        form.maintainParameter (PAYMENT_HEADER_KEY);
        form.maintainParameter (PAYMENT_RECORD_KEY);
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (presentationObjects.get (HEADER_KEY) + "",
                              outerTable));
    }

	private String getPostingString (final IWContext context,
                                     final String postingKey)
        throws RemoteException {
        final PostingBusiness business = (PostingBusiness)
                IBOLookup.getServiceInstance (context, PostingBusiness.class);
        final StringBuffer result = new StringBuffer ();
        final PostingField [] fields = getCurrentPostingFields (context);
        for (int i = 0; i < fields.length; i++) {
            final PostingField field = fields [i];
            final String key = postingKey + (i + 1);
            final String parameter = context.isParameterSet (key)
                    ? context.getParameter (key) : "";
            final String value =  parameter.length () > field.getLen ()
                    ? parameter.substring (0, field.getLen ())
                    : business.pad (parameter, field);
            result.append (value);
        }
        return result.toString ();
	}

	private ListTable getPostingParameterForm
        (final IWContext context, final String key, final String value)
        throws RemoteException {
        final String postingString = value != null && !value.equals (null + "")
                ? value : "";
        final PostingField [] fields = getCurrentPostingFields (context);
		final ListTable postingInputs = new ListTable (this, fields.length);
        int offset = 0;
        for (int i = 0; i < fields.length; i++) {
            final PostingField field = fields [i];
            final int j = i + 1;
            final int endPosition = min (offset + field.getLen (),
                                         postingString.length ());
            postingInputs.setHeader (field.getFieldTitle (), j);
            final String subString
                    = postingString.substring (offset, endPosition).trim ();
            final TextInput textInput
                    = getTextInput (key + j, subString, 80, field.getLen());
            postingInputs.add (getStyledInterface (textInput));
            offset = endPosition;
        }       
		return postingInputs;
	}

    private String getSignature (final User user) {
        if (null == user) return "";
        final String firstName = user.getFirstName ();
        final String lastName = user.getLastName ();
        return (firstName != null ? firstName + " " : "")
                + (lastName != null ? lastName : "");
    }

    private Table getConfirmTable (final String key, final String defaultString,
                                   final String [][] parameters) {
        final Table table = createTable (1);
        int row = 1;
        table.setHeight (row++, 24);
        table.add (new Text (localize (key, defaultString)), 1, row++);
        table.setHeight (row++, 12);
        final String goBackText = localize (GO_BACK_KEY, GO_BACK_DEFAULT);
        final Link link = createSmallLink (goBackText, parameters);
        table.add (link, 1, row++);
        return table;
    }
    
	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
     * @return Table to add to output
	 */
    private Table createMainTable
                (final String headerKey, final String headerDefault,
                 final PresentationObject content) throws RemoteException {
        return createMainTable (localize (headerKey, headerDefault), content);
    }

    private void addPresentation
        (final Table table, final java.util.Map map, final String key,
         final int col, final int row) {
        final PresentationObject object = (PresentationObject) map.get (key);
        if (null != object) {
            table.add (object, col, row);
        }
    }

    private String getFormattedPeriod (Date date) {
        return null != date ? periodFormatter.format (date) : "";
    }

    private String getFormattedDate (Date date) {
        return null != date ? dateFormatter.format (date) : "";
    }

    private Image getEditIcon () {
        return getEditIcon (localize (EDIT_PAYMENT_RECORD_KEY,
                                      EDIT_PAYMENT_RECORD_DEFAULT));
    }

    private Image getDeleteIcon () {
        return getDeleteIcon (localize (DELETE_ROW_KEY, DELETE_ROW_DEFAULT));
    }

    private static Link addParametersToLink (final Link link,
                                             final String [][] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            link.addParameter (parameters [i][0], parameters [i][1]);
        }
        return link;
    }

    private String formatSsn (final String ssn) {
        return null == ssn || 12 != ssn.length () ? ssn
                : ssn.substring (2, 8) + '-' + ssn.substring (8, 12);
    }

    private static String getUserName (final User user) {
        return user.getLastName () + ", " + user.getFirstName ();
    }

    private Link createSmallLink (final String displayText,
                                  final String [][] parameters) {
        final Link link = getSmallLink (displayText);
        addParametersToLink (link, parameters);
        return link;
    }

    private static Link createIconLink (final Image icon,
                                        final String [][] parameters) {
        final Link link = new Link (icon);
        addParametersToLink (link, parameters);
        return link;
    }        

    private void addSmallText (final Table table, final int col, final int row,
                               final String key, String value) {
        table.add (getSmallText (localize (key, value)), col, row);
    }

    private void addSmallText (final Table table, final int col, final int row,
                               final String string) {
        table.add (getSmallText (null == string || string.equals (null + "")
                                 ? "" : string), col, row);
    }

    private void addSmallHeader (final Table table, final int col,
                                 final int row, final String key,
                                 final String defaultString) {
        addSmallHeader (table, col, row, key, defaultString, "");
    }

    private SubmitButton getSubmitButton (final int action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (localize (key, defaultName),
                                          ACTION_KEY, action + ""));
    }

    private void setIconColumnWidth (final Table table) {
        final int columnCount = table.getColumns ();
        table.setColumnWidth (columnCount - 1, getEditIcon ().getWidth ());
        table.setColumnWidth (columnCount, getDeleteIcon ().getWidth ());
    }

    /**
     * Returns a date from a parameter string of type "YYMM". The date
     * represents the first day of that month. If the input string is unparsable
     * for this format then null is returned.
     *
     * @param context session data
     * @param key key to lookup in context to retreive the actual value
     * @return date from the first of this particular month or null on failure
     */
    private static Date getPeriodParameter (final IWContext context,
                                            final String key) {
        final String rawString = context.getParameter (key);
        if (null == rawString || rawString.length () != 4) return null;
        try {
            final int year = Integer.parseInt (rawString.substring (0, 2))
                    + 2000;
            final int month = Integer.parseInt (rawString.substring (2, 4))
                    + Calendar.JANUARY - 1;
            final Calendar calendar = Calendar.getInstance ();
            calendar.set (year, month, 1, 0, 0);
            return new Date (calendar.getTimeInMillis ());
        } catch (final NumberFormatException exception) {
            return null;
        }
    }

	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
     * @return Table to add to output
	 */
    private Table createMainTable
        (final String header, final PresentationObject content)
        throws RemoteException {
        final Table mainTable = createTable (1);
        mainTable.setCellpadding (getCellpadding ());
        mainTable.setCellspacing (getCellspacing ());
        mainTable.setWidth (Table.HUNDRED_PERCENT);
        int row = 1;
        mainTable.setRowColor (row, getHeaderColor ());
        mainTable.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER) ;
        mainTable.add (getSmallHeader (header), 1, row++);
        mainTable.add (content, 1, row++);
        return mainTable;
    }

    private DropdownMenu getLocalizedDropdown (final VATRule [] rules) {
        final DropdownMenu dropdown = (DropdownMenu)
                getStyledInterface (new DropdownMenu (VAT_RULE_KEY));
        for (int i = 0; i < rules.length; i++) {
            final VATRule rule = rules [i];
            final String ruleName = rule.getVATRule ();
            dropdown.addMenuElement (rule.getPrimaryKey () + "",
                                     localize (ruleName, ruleName));
        }
        return dropdown;
    }

    private DropdownMenu getLocalizedDropdown
        (final RegulationSpecType [] types) {
        final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (REGULATION_SPEC_TYPE_KEY));
        for (int i = 0; i < types.length; i++) {
            final RegulationSpecType type = types [i];
            final String regSpecType = type.getRegSpecType ();
            dropdown.addMenuElement (regSpecType, localize (regSpecType,
                                                            regSpecType));
        }
        return dropdown;
    }

    private void addPeriodForm (final Table table, final int row) {
        int col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        final Date now = new Date (System.currentTimeMillis ());
        table.add (getStyledInput (START_PERIOD_KEY, getFormattedPeriod
                                   (now)), col, row);
        table.add (new Text (" - "), col, row);
        table.add (getStyledInput (END_PERIOD_KEY, getFormattedPeriod
                                   (now)), col, row);
    }

    private String getSchoolCategoryName (final IWContext context,
                                          final PaymentHeader header) {
        try {
            final SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                    .getServiceInstance (context, SchoolBusiness.class);
            final SchoolCategoryHome categoryHome
                    = schoolBusiness.getSchoolCategoryHome ();
            final SchoolCategory category = categoryHome.findByPrimaryKey
                    (header.getSchoolCategoryID ());
            return localize (category.getLocalizedKey (), category.getName ());
        } catch (Exception dummy) {
            return "";
        }
    }

    private void addProviderDropdown
        (final IWContext context, final Table table, final int row)
        throws RemoteException {
        int col = 1;
        addSmallHeader (table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        final String schoolCategory = getSession().getOperationalField ();
        final DropdownMenu providerDropdown = (DropdownMenu)
                getStyledInterface (new DropdownMenu (PROVIDER_KEY));
        if (null != schoolCategory) {
            final SchoolBusiness business = (SchoolBusiness) IBOLookup
                    .getServiceInstance (context, SchoolBusiness.class);
            final Collection schools
                    = business.findAllSchoolsByCategory (schoolCategory);
            final String oldProviderId = context.getParameter (PROVIDER_KEY)
                    + "";
            for (Iterator i = schools.iterator (); i.hasNext ();) {
                final School school = (School) i.next ();
                final String primaryKey = school.getPrimaryKey ().toString ();
                final String name = school.getName ();
                providerDropdown.addMenuElement (primaryKey, name);
                if (primaryKey.equals (oldProviderId)) {
                    providerDropdown.setSelectedElement (primaryKey);
                }
            }
        } else {
            providerDropdown.addMenuElement ("", localize (PROVIDER_KEY,
                                                           PROVIDER_DEFAULT));
        }
        table.add (providerDropdown, col++, row);
    }

    private void addSmallText (final Table table, final String string,
                               final int col, final int row) {
        table.add (getSmallText (null != string && !string.equals (null + "")
                                 ? string : ""), col, row);
    }

    private void addOperationalFieldDropdown
        (final IWContext context, final Table table, final int row)
        throws RemoteException {
        int col = 1;
        addSmallHeader (table, col++, row, MAIN_ACTIVITY_KEY,
                        MAIN_ACTIVITY_DEFAULT, ":");
        String operationalField = getSession ().getOperationalField ();
        operationalField = operationalField == null ? "" : operationalField;
        table.mergeCells (col, row, table.getColumns (), row);
        final OperationalFieldsMenu dropdown = new OperationalFieldsMenu ();
        if (context.isParameterSet (ACTION_KEY)) {
            dropdown.setParameter (LAST_ACTION_KEY,
                                   context.getParameter (ACTION_KEY));
        } else if (context.isParameterSet (LAST_ACTION_KEY)) {
            dropdown.maintainParameter (LAST_ACTION_KEY);
        }
        table.add (dropdown, col++, row);
    }

    private void addOperationalFieldRow
        (final Table table, final IWContext context, final PaymentHeader header,
         final int row) throws RemoteException {
        int col = 1;
        addSmallHeader (table, col++, row, MAIN_ACTIVITY_KEY,
                        MAIN_ACTIVITY_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addSmallText (table, getSchoolCategoryName (context, header), col++,
                      row);
        final String schoolCategory = header.getSchoolCategoryID ();
        if (null != schoolCategory && 0 < schoolCategory.length ()) {
            getSession ().setOperationalField (schoolCategory);
        }
    }

    private Table createTable (final int columnCount) {
        final Table table = new Table();
        table.setCellpadding (getCellpadding ());
        table.setCellspacing (getCellspacing ());
        table.setWidth (Table.HUNDRED_PERCENT);
        table.setColumns (columnCount);
        return table;
    }

    private void logUnexpectedException (final IWContext context,
                                         final Exception exception) {
        final StringBuffer message = new StringBuffer ();
        message.append ("Exception caught in " + getClass ().getName ()
                        + " " + (new java.util.Date ()));
        message.append ("Parameters:");
        final java.util.Enumeration enum = context.getParameterNames ();
        while (enum.hasMoreElements ()) {
            final String key = (String) enum.nextElement ();
            message.append ('\t' + key + "='"
                            + context.getParameter (key) + "'");
        }
        logWarning (message.toString ());
        log (exception);
        add ("Det inträffade ett fel. Försök igen senare.");
    }

	private ListTable getPostingListTable (final IWContext context,
                                           final String postingString) 
        throws RemoteException {
        final PostingField [] fields = getCurrentPostingFields (context);
		final ListTable result = new ListTable (this, fields.length);
        int offset = 0;
        for (int i = 0; i < fields.length; i++) {
            final PostingField field = fields [i];
            final int endPosition = min (offset + field.getLen (),
                                         postingString.length ());
            result.setHeader (field.getFieldTitle (), i + 1);
            result.add (getSmallText (postingString.substring
                                      (offset, endPosition).trim ()));
            offset = endPosition;
        }       
		return result;
	}

    private int min (final int a, final int b) {
        return a < b ? a : b;
    }

    private PostingField [] getCurrentPostingFields (final IWContext context)
        throws RemoteException {
        final PostingBusiness business = (PostingBusiness)
                IBOLookup.getServiceInstance (context, PostingBusiness.class);
        final Date now = new Date (System.currentTimeMillis ());
        final Collection fields = business.getAllPostingFieldsByDate (now);
        final PostingField [] array = new PostingField [0];
        return fields != null ? (PostingField []) fields.toArray (array)
                : array;
    }

    private void addStyledInput (final java.util.Map map, final String key,
                                 final String value) {
        final TextInput input = getStyledInput
                (key, null != value && !value.equals (null + "") ? value : "");
        map.put (key, input);
    }

    private void addStyledInput (final java.util.Map map, final String key,
                                 final float number) {
        addStyledInput (map, key, ((long) number) + "");
    }

    private void addSmallText (final java.util.Map map, final String key,
                               final String value) {
        map.put (key, getSmallText (null != value && !value.equals (null + "")
                                    ? value : ""));
    }

    private void addSmallText (final java.util.Map map, final String key,
                               final long value) {
        map.put (key, getSmallText (-1 != value ? value + "" : "0"));
    }

    private void addSmallText (final java.util.Map map, final String key,
                               final Date date) {
        map.put (key, getSmallText (null != date ? dateFormatter.format (date)
                                    : ""));
    }

    private void addSmallPeriodText (final java.util.Map map, final String key,
                                     final Date date) {
        map.put (key, getSmallText (null != date ? periodFormatter.format (date)
                                    : ""));
    }

    private static Integer getIntegerParameter (final IWContext context,
                                                final String key) {
        final String rawString = context.getParameter (key);
        if (null == rawString) return null;
        try {
            final Integer result = new Integer (rawString);
            return result;
        } catch (final NumberFormatException exception) {
            return null;
        }
    }

    private static void setColumnWidthsEqual (final Table table) {
        final int columnCount = table.getColumns ();
        final int percentageInt = 100 / columnCount;
        final String percentageString = percentageInt + "%";
        for (int i = 1; i <= columnCount; i++) {
            table.setColumnWidth (i, percentageString);
        }
    }

    private TextInput getStyledInput (final String key, final String value) {
        final TextInput input = (TextInput) getStyledInterface
                (new TextInput (key));
        input.setLength (12);
        if (null != value) {
            input.setValue (value);
        }
        return input;
    }

    private void addSmallHeader
        (final Table table, final int col, final int row, final String key,
         final String defaultString, final String suffix) {
        final String localizedString = localize (key, defaultString) + suffix;
        table.add (getSmallHeader (localizedString), col, row);
    }

    private SubmitButton getSubmitButton (final String action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (localize (key, defaultName),
                                          ACTION_KEY, action));
    }
}
