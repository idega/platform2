package se.idega.idegaweb.commune.accounting.invoice.presentation;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;
import is.idega.idegaweb.member.presentation.UserSearcher;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.*;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.presentation.*;
import se.idega.idegaweb.commune.accounting.regulations.data.*;

/**
 * InvoiceCompilationEditor is an IdegaWeb block were the user can search, view
 * and edit invoice compilations.
 * <p>
 * <b>English - Swedish mini lexicon:</b>
 * <ul>
 * <li>Invoice compilation = Faktureringsunderlag
 * <li>Invoice Header = Fakturahuvud
 * <li>Invoice Record = Fakturrad
 * <li>Amount = Belopp exklusive moms
 * <li>Amount VAT = Momsbelopp i kronor
 * </ul>
 * <p>
 * Last modified: $Date: 2003/11/11 10:35:27 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.34 $
 * @see com.idega.presentation.IWContext
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.data
 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock
 */
public class InvoiceCompilationEditor extends AccountingBlock {
    private static final String PREFIX = "cacc_invcmp_";


    private static final String ADJUSTED_SIGNATURE_KEY = PREFIX + "adjusted_signature";
    private static final String AMOUNT_DEFAULT = "Belopp";
    private static final String AMOUNT_KEY = PREFIX + "amount";
    private static final String CHECK_END_PERIOD_KEY = PREFIX + "check_end_period";
    private static final String CHECK_PERIOD_DEFAULT = "Checkperiod";
    private static final String CHECK_PERIOD_KEY = PREFIX + "check_period";
    private static final String CHECK_START_PERIOD_KEY = PREFIX + "check_start_period";
    private static final String COULD_NOT_REMOVE_INVOICE_COMPILATION_OR_RECORDS_DEFAULT = "Det gick inte att at bort fakturaunderlaget eller någon av dess fakturarader";
    private static final String COULD_NOT_REMOVE_INVOICE_COMPILATION_OR_RECORDS_KEY = PREFIX + "could_not_remove_invoice_compilation_or_records";
    private static final String COULD_NOT_REMOVE_INVOICE_RECORD_DEFAULT = "Kunde inte ta bort fakturarad";
    private static final String COULD_NOT_REMOVE_INVOICE_RECORD_KEY = PREFIX + "";
    private static final String CREATE_INVOICE_COMPILATION_DEFAULT = "Skapa fakturaunderlag";
    private static final String CREATE_INVOICE_COMPILATION_KEY = PREFIX + "create_invoice_compilation";
    private static final String CREATE_INVOICE_RECORD_DEFAULT = "Skapa fakturarad";
    private static final String CREATE_INVOICE_RECORD_KEY = PREFIX + "create_invoice_record";
    private static final String CREATED_SIGNATURE_KEY = PREFIX + "created_signature";
    private static final String DATE_ADJUSTED_DEFAULT = "Justeringsdag";
    private static final String DATE_ADJUSTED_KEY = PREFIX + "date_adjusted";
    private static final String DATE_CREATED_DEFAULT = "Skapandedag";
    private static final String DATE_CREATED_KEY = PREFIX + "date_created";
    private static final String DELETE_ROW_DEFAULT = "Ta bort fakturaunderlag";
    private static final String DELETE_ROW_KEY = PREFIX + "delete_invoice_compilation";
    private static final String DOUBLE_POSTING_DEFAULT = "Motkontering";
    private static final String DOUBLE_POSTING_KEY = PREFIX + "double_posting";
    private static final String EDIT_ROW_DEFAULT = "Ändra rad";
    private static final String EDIT_ROW_KEY = PREFIX + "edit_row";
    private static final String END_PERIOD_DEFAULT = "T o m ";
    private static final String END_PERIOD_KEY = PREFIX + "end_period";
    private static final String FIRST_NAME_DEFAULT = "Förnamn";
    private static final String FIRST_NAME_KEY = PREFIX + "first_name";
    private static final String GO_BACK_DEFAULT = "Tillbaka";
    private static final String GO_BACK_KEY = PREFIX + "go_back";
    private static final String HEADER_KEY = PREFIX + "header";
    private static final String INVOICE_ADDRESS_DEFAULT = "Faktureringsadress";
    private static final String INVOICE_ADDRESS_KEY = PREFIX + "invoice_address";
    private static final String INVOICE_COMPILATION_AND_RECORDS_REMOVED_DEFAULT = "Fakturaunderlaget och dess fakturarader är nu borttagna";
    private static final String INVOICE_COMPILATION_AND_RECORDS_REMOVED_KEY = PREFIX + "invoice_compilation_and_records_removed";
    private static final String INVOICE_COMPILATION_CREATED_DEFAULT = "Fakturaunderlaget är nu skapat";
    private static final String INVOICE_COMPILATION_CREATED_KEY = PREFIX + "invoice_compilation_created";
    private static final String INVOICE_COMPILATION_DEFAULT = "Faktureringsunderlag";
    private static final String INVOICE_COMPILATION_KEY = PREFIX + "invoice_compilation";
    private static final String INVOICE_COMPILATION_LIST_DEFAULT = "Faktureringsunderlagslista";
    private static final String INVOICE_COMPILATION_LIST_KEY = PREFIX + "invoice_compilation_list";
    private static final String INVOICE_RECEIVER_DEFAULT = "Fakturamottagare";
    private static final String INVOICE_RECEIVER_KEY = PREFIX + "invoice_receiver";
    private static final String INVOICE_RECORD_CREATED_DEFAULT = "Fakturaraden är nu skapad";
    private static final String INVOICE_RECORD_CREATED_KEY = PREFIX + "invoice_record_created";
    private static final String INVOICE_RECORD_DEFAULT = "Fakturarad";
    private static final String INVOICE_RECORD_KEY = PREFIX + "invoice_record";
    private static final String INVOICE_RECORD_REMOVED_DEFAULT = "Fakturaraden är borttagen";
    private static final String INVOICE_RECORD_REMOVED_KEY = PREFIX + "";
    private static final String INVOICE_TEXT_DEFAULT  = "Fakturatext";
    private static final String INVOICE_TEXT_KEY = PREFIX + "invoice_text";
    private static final String JOURNAL_ENTRY_DATE_DEFAULT = "Bokföringsdag";
    private static final String JOURNAL_ENTRY_DATE_KEY = PREFIX + "journal_entry_date";
    private static final String LAST_NAME_DEFAULT = "Efternamn";
    private static final String LAST_NAME_KEY = PREFIX + "last_name";
    private static final String MAIN_ACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAIN_ACTIVITY_KEY = PREFIX + "main_activity";
    private static final String NAME_DEFAULT = "Namn";
    private static final String NAME_KEY = PREFIX + "name";
    private static final String NEW_DEFAULT = "Ny";
    private static final String NEW_KEY = PREFIX + "new";
    private static final String NOTE_DEFAULT = "Anmärkning";
    private static final String NOTE_KEY = PREFIX + "note";
    private static final String NUMBER_OF_DAYS_DEFAULT = "Antal dagar";
    private static final String NUMBER_OF_DAYS_KEY = PREFIX + "number_of_days";
    private static final String OWN_POSTING_DEFAULT = "Egen kontering";
    private static final String OWN_POSTING_KEY = PREFIX + "own_posting";
    private static final String PERIOD_DEFAULT = "Period";
    private static final String PERIOD_KEY = PREFIX + "period";
    private static final String PLACEMENT_END_PERIOD_KEY = PREFIX + "placement_end_period";
    private static final String PLACEMENT_PERIOD_DEFAULT = "Placeringsperiod";
    private static final String PLACEMENT_PERIOD_KEY = PREFIX + "placement_period";
    private static final String PLACEMENT_START_PERIOD_KEY = PREFIX + "placement_start_period";
    private static final String PLACEMENT_DEFAULT = "Placering";
    private static final String PLACEMENT_KEY = PREFIX + "placement";
    private static final String PROVIDER_DEFAULT = "Anordnare";
    private static final String PROVIDER_KEY = PREFIX + "provider";
    private static final String REGULATION_SPEC_TYPE_DEFAULT = "Regelspec.typ";
    private static final String REGULATION_SPEC_TYPE_KEY = PREFIX + "regulation_spec_type";
    private static final String REMARK_DEFAULT = "Anmärkning";
    private static final String REMARK_KEY = PREFIX + "remark";
    private static final String SEARCH_DEFAULT = "Sök";
    private static final String SEARCH_INVOICE_RECEIVER_DEFAULT = "Sök efter fakturamottagare";
    private static final String SEARCH_INVOICE_RECEIVER_KEY = PREFIX + "search_invoice_receiver";
    private static final String SEARCH_KEY = PREFIX + "search";
    private static final String SIGNATURE_DEFAULT = "Sigantur";
    private static final String SIGNATURE_KEY = PREFIX + "signature";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = PREFIX + "personal_id";
    private static final String START_PERIOD_DEFAULT = "Fr o m ";
    private static final String START_PERIOD_KEY = PREFIX + "start_period";
    private static final String STATUS_DEFAULT = "Status";
    private static final String STATUS_KEY = PREFIX + "status";
    private static final String TOO_MANY_RESULTS_DEFAULT = "För många sökträffar - försök att begränsa dina sökkriterier";
    private static final String TOO_MANY_RESULTS_KEY = PREFIX + "too_many_results";
    private static final String TOTAL_AMOUNT_DEFAULT = "Tot.belopp";
    private static final String TOTAL_AMOUNT_KEY = PREFIX + "total_amount";
    private static final String TOTAL_AMOUNT_VAT_DEFAULT = "Totalbelopp moms";
    private static final String TOTAL_AMOUNT_VAT_EXCLUSIVE_DEFAULT = "Totalbelopp, exklusive moms";
    private static final String TOTAL_AMOUNT_VAT_EXCLUSIVE_KEY = PREFIX + "total_amount_vat_exclusive";
    private static final String TOTAL_AMOUNT_VAT_KEY = PREFIX + "total_amount_vat";
    private static final String USERSEARCHER_ACTION_KEY = "mbe_act_search" + PREFIX;
    private static final String USERSEARCHER_FIRSTNAME_KEY = "usrch_search_fname" + PREFIX;
    private static final String USERSEARCHER_LASTNAME_KEY = "usrch_search_lname" + PREFIX;
    private static final String USERSEARCHER_PERSONALID_KEY = "usrch_search_pid" + PREFIX;
    private static final String VAT_AMOUNT_DEFAULT = "Momsbelopp";
    private static final String VAT_AMOUNT_KEY = PREFIX + "vat_amount";
    private static final String VAT_RULE_DEFAULT = "Momstyp";
    private static final String VAT_RULE_KEY = PREFIX + "vat_rule";
    private static final String RULE_TEXT_KEY = PREFIX + "rule_text";


    private static final String ACTION_KEY = PREFIX + "action_key";
	private static final int ACTION_SHOW_COMPILATION = 0,
            ACTION_SHOW_COMPILATION_LIST = 1,
            ACTION_NEW_RECORD = 2,
            ACTION_DELETE_COMPILATION = 3,
            ACTION_DELETE_RECORD = 4,
            ACTION_SHOW_NEW_COMPILATION_FORM = 5,
            ACTION_NEW_COMPILATION = 6,
            ACTION_SHOW_RECORD_DETAILS = 7,
            ACTION_SHOW_EDIT_RECORD_FORM = 8,
            ACTION_SAVE_RECORD = 9,
            ACTION_SHOW_NEW_RECORD_FORM = 10;

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
            int actionId = ACTION_SHOW_COMPILATION_LIST;
            try {
                actionId = Integer.parseInt (context.getParameter (ACTION_KEY));
            } catch (final Exception dummy) {
                // do nothing, actionId is default
            }

			switch (actionId) {
				case ACTION_SHOW_COMPILATION:
					showCompilation (context);
					break;

                case ACTION_DELETE_COMPILATION:
                    deleteCompilation (context);
                    break;

                case ACTION_DELETE_RECORD:
                    deleteRecord (context);
                    break;

                case ACTION_SHOW_NEW_COMPILATION_FORM:
                    showNewCompilationForm (context);
                    break;

                case ACTION_NEW_COMPILATION:
                    newCompilation (context);
                    break;

                case ACTION_SHOW_RECORD_DETAILS:
                case ACTION_SHOW_EDIT_RECORD_FORM:
                case ACTION_SHOW_NEW_RECORD_FORM:
                    showRecordDetails (context);
                    break;

                case ACTION_SAVE_RECORD:
                case ACTION_NEW_RECORD:
                    saveRecord (context);
                    break;

                default:
                    showCompilationList (context);
					break;					
			}
            /*
            displayRedText
                    ("<p>Denna funktion är inte färdig! Bl.a. så återstår:<ol>" 

                     + "<li>ändra i en manuell faktureringsrad"
                     + "<li>skapa rad: kopiera rule_text till invoice_text"
                     + "<li>skapa rad: hitta rule_text, order_id i regelverket"
                     + "<li>skapa rad: hitt payment_record_id med anordn.+verks"
                     + "<li>välj ett kontrakt från 'skapa fakturarrad'"
                     + "<li>se faktureringsunderlag i pdf"
                     + "<li>efter skapa ny faktura => ny record form"
                     + "<li>tillåt inte negativt taxbelopp mm"
                     + "<li>sök på huvudverksamhet - bara barnomsorg"
                     + "<li>felhantering för periodinmatning, t ex '1313'"
                     + "<li>confirm-page tillbaka till rätt sida"
                     + "<li>sortera efter order_id eller något annat"
                     + "<li>byt inte sida vid byte av huvudverksamhet"
                     + "<li>kontroll av felaktig eller utelämnad indata"
                     + "<li>skriv ut anordnare på 'skapa fakturarrad'"
                     + "<li>uppdatera totalb. och momsersättning vid justering"
                     + "<li>ta bort invoice record => ta bort payment-record?"
                     + "<li>funkar periodsökning på fakturaunderlag?"

                     + "</ol>\n\n(" + actionId + ')');
            */
		} catch (Exception exception) {
            logUnexpectedException (context, exception);
		}
	}

    private void saveRecord (final IWContext context)
        throws RemoteException, javax.ejb.CreateException {
        final User currentUser = context.getCurrentUser ();
        final Integer amount = getIntegerParameter (context, AMOUNT_KEY);
        final Date checkEndPeriod = getPeriodParameter (context,
                                                        CHECK_END_PERIOD_KEY);
        final Date checkStartPeriod
                = getPeriodParameter (context, CHECK_START_PERIOD_KEY);
        final String doublePosting = getPostingString (context,
                                                       DOUBLE_POSTING_KEY);
        final Integer invoiceCompilation
                = getIntegerParameter (context, INVOICE_COMPILATION_KEY);
        final String invoiceText = context.getParameter (INVOICE_TEXT_KEY);
        final String note = context.getParameter (NOTE_KEY);
        final Integer numberOfDays = getIntegerParameter (context,
                                                          NUMBER_OF_DAYS_KEY);
        final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
        final Date placementEndPeriod
                = getPeriodParameter (context, PLACEMENT_END_PERIOD_KEY);
        final Date placementStartPeriod
                = getPeriodParameter (context, PLACEMENT_START_PERIOD_KEY);
        final Integer providerId = getIntegerParameter (context, PROVIDER_KEY);
        final String regulationSpecType
                = context.getParameter (REGULATION_SPEC_TYPE_KEY);
        final Integer vatAmount = getIntegerParameter (context, VAT_AMOUNT_KEY);
        final Integer vatRule = getIntegerParameter (context, VAT_RULE_KEY);
        final InvoiceBusiness business = (InvoiceBusiness) IBOLookup
                .getServiceInstance (context, InvoiceBusiness.class);
        business.createInvoiceRecord
                (currentUser, amount,
                 new java.sql.Date (checkStartPeriod.getTime ()),
                 new java.sql.Date (checkEndPeriod.getTime ()), doublePosting,
                 invoiceCompilation, invoiceText, note, numberOfDays,
                 ownPosting,
                 new java.sql.Date (placementStartPeriod.getTime ()),
                 new java.sql.Date (placementEndPeriod.getTime ()),
                 providerId, regulationSpecType, vatAmount, vatRule);
        final Table table = getConfirmTable
                (INVOICE_RECORD_CREATED_KEY,
                 INVOICE_RECORD_CREATED_DEFAULT);
        add (createMainTable (CREATE_INVOICE_RECORD_KEY,
                              CREATE_INVOICE_RECORD_DEFAULT, table));
    }

    private InvoiceHeader getInvoiceHeader (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        int headerId;
        try {
            headerId = Integer.parseInt (context.getParameter
                                         (INVOICE_COMPILATION_KEY));
        } catch (NumberFormatException e) {
            final int recordId = Integer.parseInt (context.getParameter
                                                   (INVOICE_RECORD_KEY));
            final InvoiceRecordHome recordHome
                    = business.getInvoiceRecordHome ();
            final InvoiceRecord record = recordHome.findByPrimaryKey
                    (new Integer (recordId));
            headerId = record.getInvoiceHeader ();
        }
        final InvoiceHeaderHome headerHome = business.getInvoiceHeaderHome ();
        return headerHome.findByPrimaryKey (new Integer (headerId));
    }

    private void showRecordDetails (final IWContext context)
        throws RemoteException, FinderException {

        // get info from invoice header
        final InvoiceHeader header = getInvoiceHeader (context);
        final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (context, UserBusiness.class);
        final UserHome userHome = userBusiness.getUserHome ();
		final User custodian = userHome.findByPrimaryKey
		        (new Integer (header.getCustodianId ()));


        // get displayable objects for record data/inputs
        final int actionId
                = Integer.parseInt (context.getParameter (ACTION_KEY));
        Map presentationObjects;
        switch (actionId) {
            case ACTION_SHOW_NEW_RECORD_FORM:
                presentationObjects = getNewRecordInputsMap (context);
                break;

            default:
                presentationObjects = getRecordDetailsMap (context);
        }

        // render form/details
        final Table table = createTable (4);
        setColumnWidthsEqual (table);
        int row = 2;
        addOperationFieldDropdown (table, row++); 
        int col = 1;
        addSmallHeader (table, col++, row, SSN_KEY, SSN_DEFAULT, ":");
        addSmallText(table, formatSsn (custodian.getPersonalID ()), col++, row);
        addSmallHeader (table, col++, row, NAME_KEY, NAME_DEFAULT, ":");
        addSmallText(table, getUserName (custodian), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, INVOICE_TEXT_KEY,
                        INVOICE_TEXT_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get
                   (INVOICE_TEXT_KEY), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PLACEMENT_KEY, PLACEMENT_DEFAULT,
                        ":");
        table.mergeCells (col, row, table.getColumns (), row);
        table.add ((PresentationObject) presentationObjects.get (RULE_TEXT_KEY),
                   col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        table.add ((PresentationObject) presentationObjects.get (PROVIDER_KEY),
                   col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, NUMBER_OF_DAYS_KEY,
                        NUMBER_OF_DAYS_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get
                   (NUMBER_OF_DAYS_KEY), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, CHECK_PERIOD_KEY,
                        CHECK_PERIOD_DEFAULT, ":");
        addSmallHeader (table, col, row, START_PERIOD_KEY,
                        START_PERIOD_DEFAULT);
        table.add ((PresentationObject) presentationObjects.get
                   (CHECK_START_PERIOD_KEY), col++,
                   row);
        addSmallHeader (table, col, row, END_PERIOD_KEY, END_PERIOD_DEFAULT);
        table.add ((PresentationObject) presentationObjects.get
                   (CHECK_END_PERIOD_KEY), col++,
                   row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PLACEMENT_PERIOD_KEY,
                        PLACEMENT_PERIOD_DEFAULT, ":");
        addSmallHeader (table, col, row, START_PERIOD_KEY,
                        START_PERIOD_DEFAULT);
        table.add ((PresentationObject) presentationObjects.get
                   (PLACEMENT_START_PERIOD_KEY), col++, row);
        addSmallHeader (table, col, row, END_PERIOD_KEY, END_PERIOD_DEFAULT);
        table.add ((PresentationObject) presentationObjects.get
                   (PLACEMENT_END_PERIOD_KEY), col++,
                   row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DATE_CREATED_KEY,
                        DATE_CREATED_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get
                   (DATE_CREATED_KEY), col++, row);
        addSmallHeader (table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT,
                        ":");
        table.add ((PresentationObject) presentationObjects.get
                   (CREATED_SIGNATURE_KEY), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DATE_ADJUSTED_KEY,
                        DATE_ADJUSTED_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get
                   (DATE_ADJUSTED_KEY), col++, row);
        addSmallHeader (table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT,
                        ":");
        table.add ((PresentationObject) presentationObjects.get
                   (ADJUSTED_SIGNATURE_KEY), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, AMOUNT_KEY, AMOUNT_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get (AMOUNT_KEY),
                   col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, VAT_AMOUNT_KEY, VAT_AMOUNT_DEFAULT,
                        ":");
        table.add ((PresentationObject) presentationObjects.get
                   (VAT_AMOUNT_KEY), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, NOTE_KEY, NOTE_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get (NOTE_KEY),
                   col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, REGULATION_SPEC_TYPE_KEY,
                        REGULATION_SPEC_TYPE_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get
                   (REGULATION_SPEC_TYPE_KEY), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, OWN_POSTING_KEY, OWN_POSTING_DEFAULT,
                        ":");
        col = 1; row++;
        table.mergeCells (col, row, table.getColumns (), row);
        table.add ((PresentationObject) presentationObjects.get
                   (OWN_POSTING_KEY), col, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DOUBLE_POSTING_KEY,
                        DOUBLE_POSTING_DEFAULT, ":");
        col = 1; row++;
        table.mergeCells (col, row, table.getColumns (), row);
        table.add ((PresentationObject) presentationObjects.get
                   (DOUBLE_POSTING_KEY), col, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, VAT_RULE_KEY, VAT_RULE_DEFAULT, ":");
        table.add ((PresentationObject) presentationObjects.get (VAT_RULE_KEY),
                   col++, row);
        col = 1; row++;
        table.setHeight (row++, 12);
        table.mergeCells (col, row, table.getColumns (), row);
        table.add ((PresentationObject) presentationObjects.get (ACTION_KEY), 1,
                   row++);
        final Form form = new Form ();
        form.maintainParameter (INVOICE_COMPILATION_KEY);
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (presentationObjects.get (HEADER_KEY).toString (),
                              outerTable));
    }

    private Map getNewRecordInputsMap (final IWContext context)
        throws RemoteException {
        final Map result = new HashMap ();
        final String nowPeriod = periodFormatter.format (new Date ());
        result.put (INVOICE_TEXT_KEY, getStyledInput (INVOICE_TEXT_KEY));
        result.put (NUMBER_OF_DAYS_KEY, getStyledInput (NUMBER_OF_DAYS_KEY));
        result.put (CHECK_START_PERIOD_KEY, getStyledInput
                    (CHECK_START_PERIOD_KEY, nowPeriod));
        result.put (CHECK_END_PERIOD_KEY, getStyledInput
                    (CHECK_END_PERIOD_KEY, nowPeriod));
        result.put (PLACEMENT_START_PERIOD_KEY, getStyledInput
                    (PLACEMENT_START_PERIOD_KEY, nowPeriod));
        result.put (PLACEMENT_END_PERIOD_KEY, getStyledInput
                    (PLACEMENT_END_PERIOD_KEY, nowPeriod));
        result.put (DATE_CREATED_KEY, getSmallText (dateFormatter.format
                                                    (new Date ())));
        final User currentUser = context.getCurrentUser ();
        if (null != currentUser) {
            final String createdBySignature
                    = currentUser.getFirstName ().charAt (0)
                    + "" + currentUser.getLastName ().charAt (0);
            result.put (CREATED_SIGNATURE_KEY, createdBySignature);
        } else {
            result.put (CREATED_SIGNATURE_KEY, new Text (""));
        }
        result.put (DATE_ADJUSTED_KEY, new Text (""));
        result.put (ADJUSTED_SIGNATURE_KEY, new Text (""));

        result.put (AMOUNT_KEY, getStyledInput (AMOUNT_KEY));
        result.put (VAT_AMOUNT_KEY, getStyledInput (VAT_AMOUNT_KEY));
        result.put (NOTE_KEY, getStyledInput (NOTE_KEY));
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        result.put (REGULATION_SPEC_TYPE_KEY, getLocalizedDropdown
                    (business.getAllRegulationSpecTypes ()));
        result.put (OWN_POSTING_KEY, getPostingParameterForm (context,
                                                              OWN_POSTING_KEY));
        result.put (DOUBLE_POSTING_KEY, getPostingParameterForm
                    (context, DOUBLE_POSTING_KEY));
        result.put (VAT_RULE_KEY,  getLocalizedDropdown
                    (business.getAllVatRules ()));
        result.put (ACTION_KEY, getSubmitButton
                    (ACTION_NEW_RECORD + "", CREATE_INVOICE_RECORD_KEY,
                     CREATE_INVOICE_RECORD_DEFAULT));
        result.put (HEADER_KEY, localize (CREATE_INVOICE_RECORD_KEY,
                                          CREATE_INVOICE_RECORD_DEFAULT));
        result.put (RULE_TEXT_KEY, getSmallText (""));
        result.put (PROVIDER_KEY, getSmallText (""));
        return result;
    }

    private Map getRecordDetailsMap (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final int recordId = Integer.parseInt (context.getParameter
                                               (INVOICE_RECORD_KEY));
        final InvoiceRecordHome home = business.getInvoiceRecordHome ();
        final InvoiceRecord record
                = home.findByPrimaryKey (new Integer (recordId));

        final Map result = new HashMap ();
        if (0 < record.getProviderId ()) {
            final SchoolBusiness schoolBusiness
                    = (SchoolBusiness) IBOLookup.getServiceInstance
                    (context, SchoolBusiness.class);
            final School school = schoolBusiness.getSchool
                    (new Integer (record.getProviderId ()));
            result.put (PROVIDER_KEY, getSmallText (school.getName ()));
        } else {
            result.put (PROVIDER_KEY, getSmallText (""));
        }
        addSmallText (result, AMOUNT_KEY, (long) record.getAmount ());
        addSmallText (result, VAT_AMOUNT_KEY, (long) record.getAmountVAT ());
        addSmallText (result, ADJUSTED_SIGNATURE_KEY, record.getChangedBy ());
        addSmallText (result, CREATED_SIGNATURE_KEY, record.getCreatedBy ());
        addSmallDateText (result, DATE_ADJUSTED_KEY, record.getDateChanged ());
        addSmallDateText (result, DATE_CREATED_KEY, record.getDateCreated ());
        addSmallText (result, NUMBER_OF_DAYS_KEY, record.getDays ());
        result.put (DOUBLE_POSTING_KEY, getPostingListTable
                    (context, record.getDoublePosting ()));
        addSmallText (result, INVOICE_TEXT_KEY, record.getInvoiceText ());
        addSmallText (result, NOTE_KEY, record.getNotes ());
        result.put (OWN_POSTING_KEY,
                    getPostingListTable (context, record.getOwnPosting ()));
        addSmallPeriodText (result, CHECK_END_PERIOD_KEY,
                            record.getPeriodEndCheck ());
        addSmallPeriodText (result, PLACEMENT_START_PERIOD_KEY,
                            record.getPeriodEndPlacement ());
        addSmallPeriodText (result, CHECK_START_PERIOD_KEY,
                            record.getPeriodStartCheck ());
        addSmallPeriodText (result, PLACEMENT_END_PERIOD_KEY,
                            record.getPeriodStartPlacement ());
        final String ruleSpecType = record.getRuleSpecType ();
        addSmallText (result, REGULATION_SPEC_TYPE_KEY,
                      localize (ruleSpecType, ruleSpecType));
        if (0 < record.getVATType ()) {
            final VATRule rule = business.getVatRule (record.getVATType ());
            final String ruleName = rule.getVATRule ();
            result.put (VAT_RULE_KEY, getSmallText (localize (ruleName,
                                                              ruleName)));
        } else {
            result.put (VAT_RULE_KEY, getSmallText (""));
        }
        addSmallText (result, RULE_TEXT_KEY, record.getRuleText ());
        result.put (ACTION_KEY, getConfirmTable ("Tillbaka", "Tillbaka", new String [0][0]));
        result.put (HEADER_KEY, localize (INVOICE_RECORD_KEY,
                    INVOICE_RECORD_DEFAULT));
        return result;
    }

    private void addSmallText (final Map map, final String key,
                               final String value) {
        map.put (key, getSmallText (null != value ? value : ""));
    }

    private void addSmallText (final Map map, final String key,
                               final long value) {
        map.put (key, getSmallText (-1 != value ? value + "" : "0"));
    }

    private void addSmallDateText (final Map map, final String key,
                                     final Date date) {
        map.put (key, getSmallText (null != date ? dateFormatter.format (date)
                                    : ""));
    }

    private void addSmallPeriodText (final Map map, final String key,
                                     final Date date) {
        map.put (key, getSmallText (null != date ? periodFormatter.format (date)
                                    : ""));
    }

    /**
     * Shows a list of invoice compilations and a search form.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilationList (final IWContext context)
        throws RemoteException, FinderException {
        final UserSearcher searcher = createSearcher ();
        final Table table = createTable (6);
        setColumnWidthsEqual (table);
        int row = 2; // first row is reserved for setting column widths
        addOperationFieldDropdown (table, row++);
        addUserSearcherForm (table, row++, context, searcher);
        table.mergeCells (2, row, table.getColumns () - 1, row);
        addPeriodForm (table, row);
        table.add (getSubmitButton (ACTION_SHOW_COMPILATION_LIST + "",
                                    SEARCH_KEY, SEARCH_DEFAULT),
                   table.getColumns (), row++);

        if (null != searcher.getUser ()) {
            // exactly one user found - display users invoice compilation list
            final User userFound = searcher.getUser ();
            final Date fromPeriod = getPeriodParameter (context,
                                                        START_PERIOD_KEY);
            final Date toPeriod = getPeriodParameter (context, END_PERIOD_KEY);
            final InvoiceBusiness business = (InvoiceBusiness)
                    IBOLookup.getServiceInstance (context,
                                                  InvoiceBusiness.class);
            final InvoiceHeader [] headers = business
                    .getInvoiceHeadersByCustodianOrChild (userFound, fromPeriod,
                                                          toPeriod);
            table.mergeCells (1, row, table.getColumns (), row);            
            if (0 < headers.length) {
                table.add (getInvoiceCompilationListTable (context, headers), 1,
                           row++);
            } else {
                table.add (new Text ("no compilations found for this custodian"
                                     + " or child!"), 1, row++);
            }
        } else if (null != searcher.getUsersFound ()) {
            table.mergeCells (1, row, table.getColumns (), row);            
            table.add (getSearcherResultTable
                       (searcher.getUsersFound (),
                        ACTION_SHOW_COMPILATION_LIST), 1, row++);
        }
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getSubmitButton (ACTION_SHOW_NEW_COMPILATION_FORM + "",
                                    NEW_KEY, NEW_DEFAULT), 1, row);
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (INVOICE_COMPILATION_LIST_KEY,
                              INVOICE_COMPILATION_LIST_DEFAULT,  outerTable));
    }

    /**
     * Shows one invoice compilation.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilation (final IWContext context)
        throws RemoteException, FinderException {
        final int headerId = Integer.parseInt (context.getParameter
                                               (INVOICE_COMPILATION_KEY));
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final InvoiceHeaderHome home = business.getInvoiceHeaderHome ();
        final InvoiceHeader header
                = home.findByPrimaryKey (new Integer (headerId));
		final Date period = header.getPeriod ();
        final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (context, UserBusiness.class);
        final UserHome userHome = userBusiness.getUserHome ();
		final User custodian = userHome.findByPrimaryKey
		        (new Integer (header.getCustodianId ()));

        final Table table = createTable (4);
        setColumnWidthsEqual (table);
        int row = 2;
        addOperationFieldDropdown (table, row++); 
        int col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        addSmallText(table, (null != period ? periodFormatter.format (period)
                             : ""), col++, row);
        addSmallHeader (table, col++, row, STATUS_KEY, STATUS_DEFAULT, ":");
        addSmallText(table, header.getStatus () + "", col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, INVOICE_RECEIVER_KEY,
                        INVOICE_RECEIVER_DEFAULT, ":");
        addSmallText(table, getUserName (custodian), col++, row);
        addSmallHeader (table, col++, row, SSN_KEY, SSN_DEFAULT, ":");
        addSmallText(table, formatSsn (custodian.getPersonalID ()), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, INVOICE_ADDRESS_KEY,
                        INVOICE_ADDRESS_DEFAULT, ":");
        table.mergeCells (col, row, col + 2, row);
        addSmallText(table, getAddressString (custodian), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DATE_CREATED_KEY,
                        DATE_CREATED_DEFAULT, ":");
        addSmallText(table, getFormattedDate (header.getDateCreated ()), col++,
                     row);
        addSmallText(table, header.getCreatedBy (), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, DATE_ADJUSTED_KEY,
                        DATE_ADJUSTED_DEFAULT, ":");
        addSmallText(table, getFormattedDate (header.getDateAdjusted ()), col++,
                     row);
        addSmallText(table, header.getChangedBy (), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, JOURNAL_ENTRY_DATE_KEY,
                        JOURNAL_ENTRY_DATE_DEFAULT, ":");
        addSmallText(table, getFormattedDate (header.getDateJournalEntry ()),
                     col++, row++);
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);            
        table.add (getInvoiceRecordListTable (context, business, header), 1,
                   row++);
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getSubmitButton (ACTION_SHOW_NEW_RECORD_FORM + "", NEW_KEY,
                                    NEW_DEFAULT), 1, row);
        final Form form = new Form ();
        form.maintainParameter (INVOICE_COMPILATION_KEY);
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (INVOICE_COMPILATION_KEY,
                              INVOICE_COMPILATION_DEFAULT, outerTable));
    }

    private void deleteCompilation (final IWContext context)
        throws RemoteException {
        final int headerId = Integer.parseInt (context.getParameter
                                               (INVOICE_COMPILATION_KEY));
        try {
            final InvoiceBusiness business = (InvoiceBusiness) IBOLookup
                    .getServiceInstance (context, InvoiceBusiness.class);
            final InvoiceHeaderHome home = business.getInvoiceHeaderHome ();
            final InvoiceHeader header
                    = home.findByPrimaryKey (new Integer (headerId));
            business.removePreliminaryInvoice (header);
            final Table table = getConfirmTable
                    (INVOICE_COMPILATION_AND_RECORDS_REMOVED_KEY,
                     INVOICE_COMPILATION_AND_RECORDS_REMOVED_DEFAULT);
            add (createMainTable (INVOICE_COMPILATION_KEY,
                                  INVOICE_COMPILATION_DEFAULT, table));
        } catch (Exception e) {
            final Table table = getConfirmTable
                    (COULD_NOT_REMOVE_INVOICE_COMPILATION_OR_RECORDS_KEY,
                     COULD_NOT_REMOVE_INVOICE_COMPILATION_OR_RECORDS_DEFAULT);
            add (createMainTable (INVOICE_COMPILATION_KEY,
                                  INVOICE_COMPILATION_DEFAULT, table));
        }
    }
    
    private void deleteRecord (final IWContext context)
        throws RemoteException {
        final int recordId = Integer.parseInt (context.getParameter
                                               (INVOICE_RECORD_KEY));
        try {
            final InvoiceBusiness business = (InvoiceBusiness) IBOLookup
                    .getServiceInstance (context, InvoiceBusiness.class);
            final InvoiceRecordHome home = business.getInvoiceRecordHome ();
            final InvoiceRecord record
                    = home.findByPrimaryKey (new Integer (recordId));
            final String headerId = record.getInvoiceHeader () + "";
            business.removeInvoiceRecord (record);
            final String [][] parameters = getHeaderLinkParameters
                    (ACTION_SHOW_COMPILATION, headerId);
            final Table table = getConfirmTable
                    (INVOICE_RECORD_REMOVED_KEY,
                     INVOICE_RECORD_REMOVED_DEFAULT, parameters);
            add (createMainTable (INVOICE_RECORD_KEY,
                                  INVOICE_RECORD_DEFAULT, table));
        } catch (Exception e) {
            final Table table = getConfirmTable
                    (COULD_NOT_REMOVE_INVOICE_RECORD_KEY,
                     COULD_NOT_REMOVE_INVOICE_RECORD_DEFAULT);
            add (createMainTable (INVOICE_RECORD_KEY,
                                  INVOICE_RECORD_DEFAULT, table));
        }
    }
    
    private void showNewCompilationForm (final IWContext context)
        throws RemoteException {
        final UserSearcher searcher = createSearcher ();
        final Table table = createTable (6);
        setColumnWidthsEqual (table);
        int row = 2; // first row is reserved for setting column widths
        addOperationFieldDropdown (table, row++);
        addUserSearcherForm (table, row++, context, searcher);
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getSubmitButton (ACTION_SHOW_NEW_COMPILATION_FORM + "",
                                    SEARCH_INVOICE_RECEIVER_KEY,
                                    SEARCH_INVOICE_RECEIVER_DEFAULT), 1, row++);
        if (null != searcher.getUser ()) {
            // exactly one user found - display users invoice compilation list
            table.setHeight (row++, 12);
            final User user = searcher.getUser ();
            int col = 1;
            addSmallHeader (table, col++, row, INVOICE_RECEIVER_KEY,
                            INVOICE_RECEIVER_DEFAULT, ":");
            final String userInfo = getUserName (user) + " ("
                    + formatSsn (user.getPersonalID ()) + "), "
                    + getAddressString (user);
            table.mergeCells (2, row, table.getColumns (), row);
            table.add (new HiddenInput (INVOICE_RECEIVER_KEY,
                                        user.getPrimaryKey () + ""), col, row);
            addSmallText(table, userInfo, col++, row++);
            col = 1;
            addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
            final Date now = new Date ();
            table.add (getStyledInput (PERIOD_KEY, periodFormatter.format
                                       (now)), col++, row++);
            table.setHeight (row++, 12);
            addSmallHeader (table, col++, row, OWN_POSTING_KEY,
                            OWN_POSTING_DEFAULT, ":");
            table.mergeCells (1, row, table.getColumns (), row);
            table.add (getPostingParameterForm (context, OWN_POSTING_KEY), 1,
                       row++);
            addSmallHeader (table, col++, row, DOUBLE_POSTING_KEY,
                            DOUBLE_POSTING_DEFAULT, ":");
            table.mergeCells (1, row, table.getColumns (), row);
            table.add (getPostingParameterForm (context, DOUBLE_POSTING_KEY), 1,
                       row++);
            table.setHeight (row++, 12);
            table.mergeCells (1, row, table.getColumns (), row);
            table.add (getSubmitButton (ACTION_NEW_COMPILATION + "",
                                        CREATE_INVOICE_COMPILATION_KEY,
                                        CREATE_INVOICE_COMPILATION_DEFAULT), 1,
                       row++);
        } else if (null != searcher.getUsersFound ()) {
            table.mergeCells (1, row, table.getColumns (), row);
            table.add (getSearcherResultTable
                       (searcher.getUsersFound (),
                        ACTION_SHOW_NEW_COMPILATION_FORM), 1, row++);
        }
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (CREATE_INVOICE_COMPILATION_KEY,
                              CREATE_INVOICE_COMPILATION_DEFAULT,  outerTable));
    }

    private void newCompilation (final IWContext context)
        throws RemoteException, javax.ejb.CreateException {
        final String operationalField = getSession ().getOperationalField ();
        final Date period = getPeriodParameter (context, PERIOD_KEY);
        final int custodianId = Integer.parseInt (context.getParameter
                                                  (INVOICE_RECEIVER_KEY));
        final User currentUser = context.getCurrentUser ();
        final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
        final String doublePosting = getPostingString (context,
                                                       DOUBLE_POSTING_KEY);
        final InvoiceBusiness business = (InvoiceBusiness) IBOLookup
                .getServiceInstance (context, InvoiceBusiness.class);
        business.createInvoiceHeader
                (operationalField, currentUser, custodianId,  ownPosting,
                 doublePosting, new java.sql.Date (period.getTime ()));
        final Table table = getConfirmTable
                (INVOICE_COMPILATION_CREATED_KEY,
                 INVOICE_COMPILATION_CREATED_DEFAULT);
        add (createMainTable (CREATE_INVOICE_COMPILATION_KEY,
                              CREATE_INVOICE_COMPILATION_DEFAULT, table));
    }
	
    private Table getSearcherResultTable (final Collection users,
                                          int actionId) {
        final Table table = createTable (1);
        int row = 1;
        for (Iterator i = users.iterator (); row <= 10 && i.hasNext ();) {
            final User user = (User) i.next ();
            final StringBuffer name = new StringBuffer ();
            name.append (null != user.getFirstName ()
                         ? (user.getFirstName () + " ") : "");
            name.append (null != user.getMiddleName ()
                         ? (user.getMiddleName () + " ") : "");
            name.append (null != user.getLastName ()
                         ? (user.getLastName () + " ") : "");
            final String userText = formatSsn (user.getPersonalID ()) + " "
                    + name;
            String [][] parameters = {{ ACTION_KEY, actionId + "" },
                                      { USERSEARCHER_PERSONALID_KEY,
                                        user.getPersonalID () }};
            final Link link = createSmallLink (userText, parameters);
            link.addParameter (USERSEARCHER_ACTION_KEY, "unspecified");
            table.add (link, 1, row++);
        }
        if (10 < users.size ()) {
            table.setHeight (row++, 12);
            addSmallHeader (table, 1, row++, TOO_MANY_RESULTS_KEY,
                            TOO_MANY_RESULTS_DEFAULT);
        }

        return table;
    }

    private Table getConfirmTable (final String key,
                                   final String defaultString) {
        final String [][] noParameters = {};
        return getConfirmTable (key, defaultString, noParameters);
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
    
    private Table getInvoiceCompilationListTable
        (final IWContext context, final InvoiceHeader [] headers)
        throws RemoteException, FinderException {

        // set up header row
        final String [][] columnNames =
                {{ STATUS_KEY, STATUS_DEFAULT }, { PERIOD_KEY, PERIOD_DEFAULT },
                 { INVOICE_RECEIVER_KEY, INVOICE_RECEIVER_DEFAULT },
                 { TOTAL_AMOUNT_KEY, TOTAL_AMOUNT_DEFAULT }, {"", ""},
                 {"", ""}};
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

        // get some business objects
        final InvoiceBusiness invoiceBusiness = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (context, UserBusiness.class);
        final UserHome userHome = userBusiness.getUserHome ();

        // show each invoice header in a row
        for (int i = 0; i < headers.length; i++) {
			showInvoiceHeaderOnARow (table, row++, invoiceBusiness, userHome,
                                     headers [i]);
        }
        
       return table;
    }

	private void showInvoiceHeaderOnARow
        (final Table table, final int row, final InvoiceBusiness business,
         final UserHome userHome, final InvoiceHeader header)
        throws FinderException {
		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
		                   : getZebraColor2 ());
		final char status = header.getStatus ();
		final User custodian = userHome.findByPrimaryKey
		        (new Integer (header.getCustodianId ())) ;
		final Date period = header.getPeriod ();
        final String headerId = header.getPrimaryKey ().toString ();
        final String [][] editLinkParameters = getHeaderLinkParameters
                (ACTION_SHOW_COMPILATION, headerId);
        final String periodLinkText
                = null != period ? periodFormatter.format (period) : "?";
        final Link periodLink = createSmallLink (periodLinkText,
                                                 editLinkParameters);
		final long totalAmount = getTotalAmount (header, business);
		table.add (status + "", col++, row);
		table.add (periodLink, col++, row);
		table.add (getUserName (custodian), col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (totalAmount + "", col++, row);
        final Link editLink = createIconLink (getEditIcon (),
                                              editLinkParameters);
        table.add (editLink, col++, row);
        if (ConstantStatus.PRELIMINARY == status) {
            final String [][] deleteLinkParamaters = getHeaderLinkParameters
                    (ACTION_DELETE_COMPILATION,  headerId);
            final Link deleteLink = createIconLink (getDeleteIcon (),
                                                    deleteLinkParamaters);
            table.add (deleteLink, col++, row);
        }
	}

    private Table getInvoiceRecordListTable
        (final IWContext context, final InvoiceBusiness business,
         final InvoiceHeader header) throws RemoteException, FinderException {

 		final InvoiceRecord [] records
		        = business.getInvoiceRecordsByInvoiceHeader (header);

        // set up header row
        final String [][] columnNames =
                {{ SSN_KEY, SSN_DEFAULT },
                 { FIRST_NAME_KEY, FIRST_NAME_DEFAULT },
                 { INVOICE_TEXT_KEY, INVOICE_TEXT_DEFAULT },
                 { NUMBER_OF_DAYS_KEY, NUMBER_OF_DAYS_DEFAULT },
                 { AMOUNT_KEY, AMOUNT_DEFAULT },
                 { REMARK_KEY, REMARK_DEFAULT }, {"", ""}, {"", ""}};
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

        // get some business objects
        final InvoiceBusiness invoiceBusiness = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);

        // show each invoice header in a row
        for (int i = 0; i < records.length; i++) {
			showInvoiceRecordOnARow (table, row++, invoiceBusiness,
                                     records [i]);
        }
        table.setHeight(row++, 12);
        table.mergeCells (1, row, 4, row);
        addSmallHeader (table, 1, row, TOTAL_AMOUNT_VAT_EXCLUSIVE_KEY,
                        TOTAL_AMOUNT_VAT_EXCLUSIVE_DEFAULT, ":");
        table.setAlignment (5, row, Table.HORIZONTAL_ALIGN_RIGHT);
        table.add (getTotalAmount (records) + "", 5, row++);
        table.mergeCells (1, row, 4, row);
        addSmallHeader (table, 1, row, TOTAL_AMOUNT_VAT_KEY,
                        TOTAL_AMOUNT_VAT_DEFAULT, ":");
        table.setAlignment (5, row, Table.HORIZONTAL_ALIGN_RIGHT);
        table.add (getTotalAmountVat (records) + "", 5, row++);
        addSmallHeader (table, 1, row, OWN_POSTING_KEY, OWN_POSTING_DEFAULT,
                        ":");
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getPostingListTable (context, header.getOwnPosting ()), 1,
                   row++);
        addSmallHeader (table, 1, row, DOUBLE_POSTING_KEY,
                        DOUBLE_POSTING_DEFAULT, ":");
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getPostingListTable (context, header.getDoublePosting ()), 1,
                   row++);
        
        return table;
    }

	private void showInvoiceRecordOnARow
        (final Table table, final int row, final InvoiceBusiness business,
         final InvoiceRecord record) throws RemoteException {
		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
		                   : getZebraColor2 ());
        final User child = business.getChildByInvoiceRecord (record);
        if (null != child) {
            table.add (formatSsn (child.getPersonalID ()), col++, row);
            table.add (child.getFirstName (), col++, row);
        } else {
            col += 2;
        }
        final String recordId = record.getPrimaryKey ().toString ();
        final String [][] editLinkParameters = getRecordLinkParameters
                (isManualRecord (record) ? ACTION_SHOW_EDIT_RECORD_FORM
                 : ACTION_SHOW_RECORD_DETAILS, recordId);
        final Link textLink = createSmallLink (record.getInvoiceText (),
                                               editLinkParameters);
        table.add (textLink, col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (record.getDays () + "", col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (((long) record.getAmount ()) + "", col++, row);
		table.add (record.getNotes (), col++, row);
        final Link editLink = createIconLink (getEditIcon (),
                                              editLinkParameters);
        table.add (editLink, col++, row);
        if (isManualRecord (record)) {
            final String [][] deleteLinkParamaters = getRecordLinkParameters
                    (ACTION_DELETE_RECORD,  recordId);
            final Link deleteLink = createIconLink (getDeleteIcon (),
                                                    deleteLinkParamaters);
            table.add (deleteLink, col++, row);
        }
    }

	private ListTable getPostingParameterForm (final IWContext context,
                                               final String key)
        throws RemoteException {
        final PostingField [] fields = getCurrentPostingFields (context);
		final ListTable postingInputs = new ListTable (this, fields.length);
        for (int i = 0; i < fields.length; i++) {
            final PostingField field = fields [i];
            final int j = i + 1;
            postingInputs.setHeader (field.getFieldTitle(), j);
            postingInputs.add (getTextInput (key + j, "", 80, field.getLen()));
        }       
		return postingInputs;
	}

	private ListTable getPostingListTable (final IWContext context,
                                           final String postingString) 
        throws RemoteException {
        final PostingField [] fields = getCurrentPostingFields (context);
		final ListTable result = new ListTable (this, fields.length);
        int offset = 0;
        for (int i = 0; i < fields.length; i++) {
            final PostingField field = fields [i];
            result.setHeader (field.getFieldTitle(), i + 1);
            final int endPosition = min (offset + field.getLen (),
                                         postingString.length ());
            result.add (getSmallText (postingString.substring
                                      (offset, endPosition)));
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
        final java.sql.Date now = new java.sql.Date (new Date ().getTime ());
        final Collection fields = business.getAllPostingFieldsByDate (now);
        final PostingField [] array = new PostingField [0];
        return fields != null ? (PostingField []) fields.toArray (array)
                : array;
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

    static boolean isManualRecord (final InvoiceRecord record) {
        final String createdBy = record.getCreatedBy ();
        return null != createdBy && 2 == createdBy.length ();
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

    private void addUserSearcherForm
        (final Table table, final int row, final IWContext context,
         final UserSearcher searcher) throws RemoteException {
        int col = 1;
        table.add (new HiddenInput (USERSEARCHER_ACTION_KEY), col, row);
        try {
            searcher.process (context);
        } catch (final FinderException dummy) {
            // do nothing, it's ok that none was found
        }
        final User user = searcher.getUser ();
        final String ssn = null != user ? user.getPersonalID ()
                : context.isParameterSet (USERSEARCHER_PERSONALID_KEY)
                ? context.getParameter (USERSEARCHER_PERSONALID_KEY) : "";
        final String firstName = null != user ? user.getFirstName ()
                : context.isParameterSet (USERSEARCHER_FIRSTNAME_KEY)
                ? context.getParameter (USERSEARCHER_FIRSTNAME_KEY) : "";
        final String lastName = null != user ? user.getLastName ()
                : context.isParameterSet (USERSEARCHER_LASTNAME_KEY)
                ? context.getParameter (USERSEARCHER_LASTNAME_KEY) : "";


        addSmallHeader (table, col++, row, SSN_KEY, SSN_DEFAULT, ":");
        table.add (getStyledInput (USERSEARCHER_PERSONALID_KEY, ssn), col++,
                   row);
        addSmallHeader (table, col++, row, LAST_NAME_KEY, LAST_NAME_DEFAULT,
                        ":");
        table.add (getStyledInput (USERSEARCHER_LASTNAME_KEY, lastName), col++,
                   row);
        addSmallHeader (table, col++, row, FIRST_NAME_KEY,  FIRST_NAME_DEFAULT,
                        ":");
        table.add (getStyledInput (USERSEARCHER_FIRSTNAME_KEY, firstName),
                   col++, row);
    }

    private void addPeriodForm (final Table table, final int row) {
        int col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        final Date now = new Date ();
        table.add (getStyledInput (START_PERIOD_KEY, periodFormatter.format
                                   (now)), col, row);
        table.add (new Text (" - "), col, row);
        table.add (getStyledInput (END_PERIOD_KEY, periodFormatter.format
                                   (now)), col, row);
    }

    private TextInput getStyledInput (final String key) {
        return getStyledInput (key, null);
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

    private void addSmallText (final Table table, final String string,
                               final int col, final int row) {
        table.add (getSmallText (null != string ? string : ""), col, row);
    }

    private void addSmallHeader (final Table table, final int col,
                                 final int row, final String key,
                                 final String defaultString) {
        addSmallHeader (table, col, row, key, defaultString, "");
    }

    private void addSmallHeader
        (final Table table, final int col, final int row, final String key,
         final String defaultString, final String suffix) {
        final String localizedString = localize (key, defaultString) + suffix;
        table.add (getSmallHeader (localizedString), col, row);
    }

    private Table createTable (final int columnCount) {
        final Table table = new Table();
        table.setCellpadding (getCellpadding ());
        table.setCellspacing (getCellspacing ());
        table.setWidth (Table.HUNDRED_PERCENT);
        table.setColumns (columnCount);
        return table;
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

    private SubmitButton getSubmitButton (final String action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (localize (key, defaultName),
                                          ACTION_KEY, action));
    }

    private void addOperationFieldDropdown
        (final Table table, final int row) throws RemoteException {
        int col = 1;
        addSmallHeader (table, col++, row, MAIN_ACTIVITY_KEY,
                        MAIN_ACTIVITY_DEFAULT, ":");
        String operationalField = getSession ().getOperationalField();
        operationalField = operationalField == null ? "" : operationalField;
        table.mergeCells (col, row, table.getColumns (), row);
        table.add (new OperationalFieldsMenu (), col++, row);
    }

    private UserSearcher createSearcher () {
        final UserSearcher searcher = new UserSearcher ();
        searcher.setOwnFormContainer (false);
        searcher.setShowFirstNameInSearch (false);
        searcher.setShowMiddleNameInSearch (false);
        searcher.setShowLastNameInSearch (false);
        searcher.setShowPersonalIDInSearch (false);
        searcher.setShowButtons (false);
        searcher.setUniqueIdentifier (PREFIX);
        searcher.setHeaderFontStyleName (getSmallHeaderFontStyle ());
        return searcher;
    }

    private void logUnexpectedException (final IWContext context,
                                         final Exception exception) {
        System.err.println ("Exception caught in " + getClass ().getName ()
                            + " " + (new java.util.Date ()));
        System.err.println ("Parameters:");
        final java.util.Enumeration enum = context.getParameterNames ();
        while (enum.hasMoreElements ()) {
            final String key = (String) enum.nextElement ();
            System.err.println ('\t' + key + "='"
                                + context.getParameter (key) + "'");
        }
        exception.printStackTrace ();
        add ("Det inträffade ett fel. Försök igen senare.");
    }

    private static String getUserName (final User user) {
        return user.getLastName () + ", " + user.getFirstName ();
    }

    private static String getFormattedDate (final Date date) {
        return null == date ? "" : dateFormatter.format (date);
    }

    private static void setColumnWidthsEqual (final Table table) {
        final int columnCount = table.getColumns ();
        final int percentageInt = 100 / columnCount;
        final String percentageString = percentageInt + "%";
        for (int i = 1; i <= columnCount; i++) {
            table.setColumnWidth (i, percentageString);
        }
    }

    private static String getAddressString (final User user) {
        final StringBuffer result = new StringBuffer ();
        if (null != user) {
            final Collection addresses = user.getAddresses ();
            if (!addresses.isEmpty ()) {
                final Address address = (Address) addresses.toArray () [0];
                result.append (address.getStreetAddress());
                result.append (", ");
                result.append (address.getPostalAddress());
            }
        }
        return result.toString ();
    }

    private Image getEditIcon () {
        return getEditIcon (localize (EDIT_ROW_KEY,
                                      EDIT_ROW_DEFAULT));
    }

    private Image getDeleteIcon () {
        return getDeleteIcon (localize (DELETE_ROW_KEY,
                                        DELETE_ROW_DEFAULT));
    }

    private void setIconColumnWidth (final Table table) {
        final int columnCount = table.getColumns ();
        table.setColumnWidth (columnCount - 1, getEditIcon ().getWidth ());
        table.setColumnWidth (columnCount, getDeleteIcon ().getWidth ());
    }

    private static String [][] getRecordLinkParameters
        (final int actionId, final String recordId) {
        return new String [][] {{ ACTION_KEY, actionId + "" },
                                { INVOICE_RECORD_KEY, recordId }};
    }

    private static String [][] getHeaderLinkParameters
        (final int actionId, final String headerId) {
        return new String [][] {{ ACTION_KEY, actionId + "" },
                                { INVOICE_COMPILATION_KEY, headerId }};
    }

    private static Link createIconLink (final Image icon,
                                        final String [][] parameters) {
        final Link link = new Link (icon);
        addParametersToLink (link, parameters);
        return link;
    }        

    private Link createSmallLink (final String displayText,
                                  final String [][] parameters) {
        final Link link = getSmallLink (displayText);
        addParametersToLink (link, parameters);
        return link;
    }

    private static Link addParametersToLink (final Link link,
                                             final String [][] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            link.addParameter (parameters [i][0], parameters [i][1]);
        }
        return link;
    }

    private long getTotalAmount (final InvoiceHeader header,
                                   final InvoiceBusiness business) {
 		final InvoiceRecord[] records;
		try {
			records = business.getInvoiceRecordsByInvoiceHeader(header);
		} catch (RemoteException e) {
			return 0;
		}
        return getTotalAmount (records);
    }

    private long getTotalAmount (final InvoiceRecord [] records) {
		long totalAmount = 0;
		for (int i = 0; i < records.length; i++) {
		    totalAmount += records[i].getAmount ();
		}
        return totalAmount;
    }

    private long getTotalAmountVat (final InvoiceRecord [] records) {
		long totalAmountVat = 0;
		for (int i = 0; i < records.length; i++) {
		    totalAmountVat += records[i].getAmountVAT ();
		}
        return totalAmountVat;
    }

    private String formatSsn (final String ssn) {
        return null == ssn || 12 != ssn.length () ? ssn
                : ssn.substring (2, 8) + '-' + ssn.substring (8, 12);
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

    private void displayRedText (final String string) {
        final Text text = new Text ('\n' + string + '\n');
        text.setFontColor ("#ff0000");
        add (text);
    }

    /*private void displayRedText (final String key, final String defaultString) {
        final String localizedString = key != null
                ? localize (key, defaultString) : defaultString;
        displayRedText (localizedString);
    }*/

    /*private Table getButtonTable (final String [][] buttonInfo) {
        final Table table = new Table ();
        for (int i = 0; i < buttonInfo.length; i++) {
            table.add (getSubmitButton (buttonInfo [i][0], buttonInfo [i][1],
                                        buttonInfo [i][2]), i + 1, 1);
            table.add (Text.getNonBrakingSpace (), i + 1, 1);
        }
        return table;
    }*/
}
