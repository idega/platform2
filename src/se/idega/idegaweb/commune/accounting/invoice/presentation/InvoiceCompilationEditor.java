package se.idega.idegaweb.commune.accounting.invoice.presentation;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.presentation.UserSearcher;
import java.awt.Color;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.business.BillingThread;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRule;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

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
 * Last modified: $Date: 2003/12/04 20:07:36 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.81 $
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
    private static final String CANCEL_DEFAULT = "Avbryt";
    private static final String CANCEL_KEY = PREFIX + "cancel";
    private static final String CHECK_END_PERIOD_KEY = PREFIX + "check_end_period";
    private static final String CHECK_PERIOD_DEFAULT = "Checkperiod";
    private static final String CHECK_PERIOD_KEY = PREFIX + "check_period";
    private static final String CHECK_START_PERIOD_KEY = PREFIX + "check_start_period";
    private static final String COULD_NOT_REMOVE_INVOICE_COMPILATION_OR_RECORDS_DEFAULT = "Det gick inte att at bort fakturaunderlaget eller någon av dess fakturarader";
    private static final String COULD_NOT_REMOVE_INVOICE_COMPILATION_OR_RECORDS_KEY = PREFIX + "could_not_remove_invoice_compilation_or_records";
    private static final String COULD_NOT_REMOVE_INVOICE_RECORD_DEFAULT = "Kunde inte ta bort fakturarad";
    private static final String COULD_NOT_REMOVE_INVOICE_RECORD_KEY = PREFIX + "could_not_remove_invoice_record";
    private static final String CREATED_SIGNATURE_KEY = PREFIX + "created_signature";
    private static final String CREATE_INVOICE_COMPILATION_DEFAULT = "Skapa fakturaunderlag";
    private static final String CREATE_INVOICE_COMPILATION_KEY = PREFIX + "create_invoice_compilation";
    private static final String CREATE_INVOICE_RECORD_DEFAULT = "Skapa fakturarad";
    private static final String CREATE_INVOICE_RECORD_KEY = PREFIX + "create_invoice_record";
    private static final String CUSTODIAN_DEFAULT = "Vårdnadshavare";
    private static final String CUSTODIAN_KEY = PREFIX + "custodian";
    private static final String DATE_ADJUSTED_DEFAULT = "Justeringsdag";
    private static final String DATE_ADJUSTED_KEY = PREFIX + "date_adjusted";
    private static final String DATE_CREATED_DEFAULT = "Skapandedag";
    private static final String DATE_CREATED_KEY = PREFIX + "date_created";
    private static final String DELETE_ROW_DEFAULT = "Ta bort fakturaunderlag";
    private static final String DELETE_ROW_KEY = PREFIX + "delete_invoice_compilation";
    private static final String DOUBLE_POSTING_DEFAULT = "Motkontering";
    private static final String DOUBLE_POSTING_KEY = PREFIX + "double_posting";
    private static final String EDIT_INVOICE_RECORD_DEFAULT = "Ändra fakturarad";
    private static final String EDIT_INVOICE_RECORD_KEY = PREFIX + "edit_invoice_record";
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
    private static final String INVOICE_RECORD_REMOVED_KEY = PREFIX + "invoice_record_removed";
    private static final String INVOICE_RECORD_UPDATED_DEFAULT = "Fakturaraden är nu uppdaterad";
    private static final String INVOICE_RECORD_UPDATED_KEY = PREFIX + "invoice_record_updated";
    private static final String INVOICE_TEXT_DEFAULT  = "Fakturatext";
    private static final String INVOICE_TEXT_KEY = PREFIX + "invoice_text";
    private static final String JOURNAL_ENTRY_DATE_DEFAULT = "Bokföringsdag";
    private static final String JOURNAL_ENTRY_DATE_KEY = PREFIX + "journal_entry_date";
    private static final String LAST_NAME_DEFAULT = "Efternamn";
    private static final String LAST_NAME_KEY = PREFIX + "last_name";
    private static final String MAIN_ACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAIN_ACTIVITY_KEY = PREFIX + "main_activity";
    private static final String NEW_DEFAULT = "Ny";
    private static final String NEW_KEY = PREFIX + "new";
    private static final String NOTE_DEFAULT = "Anmärkning";
    private static final String NOTE_KEY = PREFIX + "note";
    private static final String NO_COMPILATION_FOUND_DEFAULT = "Ingen faktura hittades för denna vårdnadshavare eller barn";
    private static final String NO_COMPILATION_FOUND_KEY = PREFIX + "no_compilation_found";
    private static final String NUMBER_OF_DAYS_DEFAULT = "Antal dagar";
    private static final String NUMBER_OF_DAYS_KEY = PREFIX + "number_of_days";
    private static final String OWN_POSTING_DEFAULT = "Egen kontering";
    private static final String OWN_POSTING_KEY = PREFIX + "own_posting";
    private static final String PDF_DEFAULT = "PDF";
    private static final String PDF_KEY = PREFIX + "pdf";
    private static final String PERIOD_DEFAULT = "Period";
    private static final String PERIOD_KEY = PREFIX + "period";
    private static final String PLACEMENT_DEFAULT = "Placering";
    private static final String PLACEMENT_END_PERIOD_KEY = PREFIX + "placement_end_period";
    private static final String PLACEMENT_KEY = PREFIX + "placement";
    private static final String PLACEMENT_PERIOD_DEFAULT = "Placeringsperiod";
    private static final String PLACEMENT_PERIOD_KEY = PREFIX + "placement_period";
    private static final String PLACEMENT_START_PERIOD_KEY = PREFIX + "placement_start_period";
    private static final String PROVIDER_DEFAULT = "Anordnare";
    private static final String PROVIDER_KEY = PREFIX + "provider";
    private static final String REGULATION_SPEC_TYPE_DEFAULT = "Regelspec.typ";
    private static final String REGULATION_SPEC_TYPE_KEY = PREFIX + "regulation_spec_type";
    private static final String REMARK_DEFAULT = "Anmärkning";
    private static final String REMARK_KEY = PREFIX + "remark";
    private static final String SEARCH_RULE_TEXT_KEY = PREFIX + "search_rule_text";
    private static final String SEARCH_RULE_TEXT_DEFAULT = "Sök regeltext";
    private static final String RULE_TEXT_KEY = PREFIX + "rule_text";
    private static final String RULE_TEXT_LINK_LIST_KEY = PREFIX + "rule_text_link_list";
    private static final String SEARCH_DEFAULT = "Sök";
    private static final String SEARCH_INVOICE_RECEIVER_DEFAULT = "Sök efter fakturamottagare";
    private static final String SEARCH_INVOICE_RECEIVER_KEY = PREFIX + "search_invoice_receiver";
    private static final String SEARCH_KEY = PREFIX + "search";
    private static final String SIGNATURE_DEFAULT = "Signatur";
    private static final String SIGNATURE_KEY = PREFIX + "signature";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = PREFIX + "personal_id";
    private static final String START_PERIOD_DEFAULT = "Fr o m ";
    private static final String START_PERIOD_KEY = PREFIX + "start_period";
    private static final String STATUS_DEFAULT = "Status";
    private static final String STATUS_KEY = PREFIX + "status";
    //private static final String IN_CUSTODY_OF_KEY = PREFIX + "in_custody_of";
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
    private static final String NO_RELATED_PLACEMENT_FOUND_FOR_DEFAULT = "Ingen relaterad placering hittades för";
    private static final String NO_RELATED_PLACEMENT_FOUND_FOR_KEY = PREFIX + "no_related_placement_found_for";

    private static final String ACTION_KEY = PREFIX + "action_key";
    private static final String LAST_ACTION_KEY = PREFIX + "last_action_key";
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
            ACTION_SHOW_NEW_RECORD_FORM = 10,
            ACTION_SHOW_NEW_RECORD_FORM_AND_SEARCH_RULE_TEXT = 11,
            ACTION_GENERATE_COMPILATION_PDF = 12;

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
            final int actionId = getActionId (context);

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
                    showRecordDetails (context);
                    break;

                case ACTION_SHOW_EDIT_RECORD_FORM:
                    showEditRecordForm (context);
                    break;

                case ACTION_SHOW_NEW_RECORD_FORM:
                case ACTION_SHOW_NEW_RECORD_FORM_AND_SEARCH_RULE_TEXT:
                    showNewRecordForm (context);
                    break;

                case ACTION_SAVE_RECORD:
                    saveRecord (context);
                    break;

                case ACTION_NEW_RECORD:
                    newRecord (context);
                    break;

                case ACTION_GENERATE_COMPILATION_PDF:
                    generateCompilationPdf (context);
                    break;

                default:
                    showCompilationList (context);
					break;					
			}
		} catch (Exception exception) {
            logUnexpectedException (context, exception);
		}
	}

    private int getActionId (final IWContext context) {
        int actionId = ACTION_SHOW_COMPILATION_LIST;
        
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
        return actionId;
    }

	private void generateCompilationPdf (final IWContext context)
        throws RemoteException, FinderException, DocumentException {

        final String [] columnNames =
                {localize (SSN_KEY, SSN_DEFAULT),
                 localize (FIRST_NAME_KEY, FIRST_NAME_DEFAULT),
                 localize (INVOICE_TEXT_KEY, INVOICE_TEXT_DEFAULT),
                 localize (NUMBER_OF_DAYS_KEY, NUMBER_OF_DAYS_DEFAULT),
                 localize (AMOUNT_KEY, AMOUNT_DEFAULT),
                 localize (REMARK_KEY, REMARK_DEFAULT)};

        final InvoiceBusiness business = getInvoiceBusiness (context);
        final InvoiceHeader header = getInvoiceHeader (context);        
        final String headerId = header.getPrimaryKey ().toString ();
        final InvoiceRecord [] records
                = business.getInvoiceRecordsByInvoiceHeader (header);
        final Document document = new Document
                (PageSize.A4, mmToPoints (20), mmToPoints (20),
                 mmToPoints (20), mmToPoints (20));
        final MemoryFileBuffer buffer = new MemoryFileBuffer ();
        final OutputStream outStream = new MemoryOutputStream (buffer);
        final PdfWriter writer = PdfWriter.getInstance (document, outStream);
        writer.setViewerPreferences
                (PdfWriter.HideMenubar | PdfWriter.PageLayoutOneColumn |
                 PdfWriter.PageModeUseNone | PdfWriter.FitWindow
                 | PdfWriter.CenterWindow);
        final String title = localize
                (INVOICE_COMPILATION_KEY,
                 INVOICE_COMPILATION_DEFAULT) + " " + headerId;
        document.addTitle (title);
        document.addCreationDate ();
        document.open ();
        
        // add content to document
        final PdfPTable outerTable = new PdfPTable (1);
        outerTable.setWidthPercentage (100f);
        outerTable.getDefaultCell ().setBorder (0);
        addPhrase (outerTable, title);
        addPhrase (outerTable, "\n");
        final User custodian = header.getCustodian ();
        addPhrase (outerTable, localize (CUSTODIAN_KEY, CUSTODIAN_DEFAULT)
                   + ": " + getUserInfo (custodian) + "\n");
        addPhrase (outerTable, localize (PERIOD_KEY, PERIOD_DEFAULT) + ": "
                   + header.getPeriod ());
        addPhrase (outerTable, "\n");
        final Color lightBlue = new Color (0xf4f4f4);
        final PdfPTable recordTable = getInvoiceRecordPdfTable
                (columnNames, business, records, lightBlue);
        outerTable.addCell (recordTable);
        addPhrase (outerTable, "\n");
        addPhrase (outerTable,
                   localize (OWN_POSTING_KEY, OWN_POSTING_DEFAULT) + ":");
        final PdfPTable postingTable
                = getOwnPostingPdfTable (context, records, lightBlue);
        outerTable.addCell (postingTable);
        document.add (outerTable);
        
        // close and store document
        document.close ();

        final int docId = business.generatePdf
                (localize (INVOICE_COMPILATION_KEY,
                           INVOICE_COMPILATION_DEFAULT), buffer);
        final Link viewLink
                = new Link("Öppna fakturaunderlaget i Acrobat Reader");
        viewLink.setFile (docId);
        viewLink.setTarget ("letter_window_" + docId);
        add (createMainTable (INVOICE_COMPILATION_KEY,
                              INVOICE_COMPILATION_DEFAULT, viewLink));
    }

    private void newRecord (final IWContext context)
        throws RemoteException, CreateException, FinderException {
        final User currentUser = context.getCurrentUser ();
        final Integer amount = getIntegerParameter (context, AMOUNT_KEY);
        final Date checkEndPeriod = getPeriodParameter (context,
                                                        CHECK_END_PERIOD_KEY);
        final Date checkStartPeriod
                = getPeriodParameter (context, CHECK_START_PERIOD_KEY);
        final Integer numberOfDays
                = new Integer (dayDiff (checkStartPeriod, checkEndPeriod));
        final String doublePosting = getPostingString (context,
                                                       DOUBLE_POSTING_KEY);
        final Integer invoiceCompilation
                = getIntegerParameter (context, INVOICE_COMPILATION_KEY);
        final String invoiceText = context.getParameter (INVOICE_TEXT_KEY);
        final String note = context.getParameter (NOTE_KEY);
        final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
        final Date placementEndPeriod
                = getPeriodParameter (context, PLACEMENT_END_PERIOD_KEY);
        final Date placementStartPeriod
                = getPeriodParameter (context, PLACEMENT_START_PERIOD_KEY);
        Integer providerId = getIntegerParameter (context, PROVIDER_KEY);
        final Integer placementId = getIntegerParameter (context,
                                                         PLACEMENT_KEY);
        if (null == providerId && null != placementId) {
            final SchoolBusiness schoolBusiness = getSchoolBusiness (context);
            final SchoolClassMemberHome placementHome
                    = schoolBusiness.getSchoolClassMemberHome ();
            final SchoolClassMember placement
                    = placementHome.findByPrimaryKey (placementId);
            providerId = (Integer)
                    placement.getSchoolClass ().getSchool ().getPrimaryKey ();
        }
        final Integer regulationSpecTypeId
                = getIntegerParameter (context, REGULATION_SPEC_TYPE_KEY);
        final Integer vatAmount = getIntegerParameter (context, VAT_AMOUNT_KEY);
        final Integer vatRule = getIntegerParameter (context, VAT_RULE_KEY);
        final String ruleText = context.getParameter (RULE_TEXT_KEY);
        final InvoiceBusiness business = getInvoiceBusiness (context);
        business.createInvoiceRecord
                (currentUser, amount,
                 new java.sql.Date (checkStartPeriod.getTime ()),
                 new java.sql.Date (checkEndPeriod.getTime ()), doublePosting,
                 invoiceCompilation, invoiceText, note, numberOfDays,
                 ownPosting,
                 new java.sql.Date (placementStartPeriod.getTime ()),
                 new java.sql.Date (placementEndPeriod.getTime ()),
                 providerId, regulationSpecTypeId, vatAmount, vatRule, ruleText,
                 placementId);
        final String [][] parameters =
                {{ACTION_KEY, ACTION_SHOW_COMPILATION + "" },
                { INVOICE_COMPILATION_KEY, invoiceCompilation + "" }};
        final Table table = getConfirmTable
                (INVOICE_RECORD_CREATED_KEY,
                 INVOICE_RECORD_CREATED_DEFAULT, parameters);
        add (createMainTable (CREATE_INVOICE_RECORD_KEY,
                              CREATE_INVOICE_RECORD_DEFAULT, table));
    }

    private void saveRecord (final IWContext context)
        throws RemoteException, FinderException {
        // get updated values
        final User currentUser = context.getCurrentUser ();
        final Integer amount = getIntegerParameter (context, AMOUNT_KEY);
        final Date checkEndPeriod = getPeriodParameter (context,
                                                        CHECK_END_PERIOD_KEY);
        final Date checkStartPeriod
                = getPeriodParameter (context, CHECK_START_PERIOD_KEY);
        final Integer numberOfDays
                = new Integer (dayDiff (checkStartPeriod, checkEndPeriod));
        final String doublePosting = getPostingString (context,
                                                       DOUBLE_POSTING_KEY);
        final String invoiceText = context.getParameter (INVOICE_TEXT_KEY);
        final String note = context.getParameter (NOTE_KEY);
        final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
        final Date placementEndPeriod
                = getPeriodParameter (context, PLACEMENT_END_PERIOD_KEY);
        final Date placementStartPeriod
                = getPeriodParameter (context, PLACEMENT_START_PERIOD_KEY);
        Integer providerId = getIntegerParameter (context, PROVIDER_KEY);
        final Integer placementId = getIntegerParameter (context,
                                                         PLACEMENT_KEY);
        if (null == providerId && null != placementId) {
            final SchoolBusiness schoolBusiness = getSchoolBusiness (context);
            final SchoolClassMemberHome placementHome
                    = schoolBusiness.getSchoolClassMemberHome ();
            final SchoolClassMember placement
                    = placementHome.findByPrimaryKey (placementId);
            providerId = (Integer)
                    placement.getSchoolClass ().getSchool ().getPrimaryKey ();
        }
        final Integer regSpecTypeId
                = getIntegerParameter (context, REGULATION_SPEC_TYPE_KEY);
        final Integer vatAmount = getIntegerParameter (context, VAT_AMOUNT_KEY);
        final Integer vatRule = getIntegerParameter (context, VAT_RULE_KEY);
        final String ruleText = context.getParameter (RULE_TEXT_KEY);
        final InvoiceRecord record = getInvoiceRecord (context);

        // set updated values
        record.setAmount (amount.floatValue ());
        record.setAmountVAT (vatAmount.floatValue ());
        record.setChangedBy (getSignature (currentUser));
        record.setDateChanged (new java.sql.Date (System.currentTimeMillis ()));
        if (null != numberOfDays) record.setDays (numberOfDays.intValue ());
        record.setDoublePosting (doublePosting);
        record.setInvoiceText (null != invoiceText && 0 < invoiceText.length ()
                               ? invoiceText : ruleText);
        record.setNotes (note);
        record.setOwnPosting (ownPosting);
        record.setPeriodStartCheck (new java.sql.Date
                                    (checkStartPeriod.getTime ()));
        record.setPeriodEndCheck (new java.sql.Date
                                  (checkEndPeriod.getTime ()));
        record.setPeriodStartPlacement (new java.sql.Date
                                        (placementStartPeriod.getTime ()));
        record.setPeriodEndPlacement (new java.sql.Date
                                      (placementEndPeriod.getTime ()));
        if (null != providerId) record.setProviderId (providerId.intValue ());
        if (null != placementId) record.setSchoolClassMemberId
                                         (placementId.intValue ());
        if (null != regSpecTypeId) record.setRegSpecTypeId
                                           (regSpecTypeId.intValue ());
        if (null != vatRule) record.setVATType (vatRule.intValue ());
        record.setRuleText (ruleText);

        // store updated record
        record.store ();

        //render
        final String [][] parameters =
                {{ACTION_KEY, ACTION_SHOW_COMPILATION + "" },
                { INVOICE_COMPILATION_KEY, record.getInvoiceHeader () + ""}};
        final Table table = getConfirmTable
                (INVOICE_RECORD_UPDATED_KEY,
                 INVOICE_RECORD_UPDATED_DEFAULT, parameters);
        add (createMainTable (EDIT_INVOICE_RECORD_KEY,
                              EDIT_INVOICE_RECORD_DEFAULT, table));
    }

    private void showNewRecordForm (final IWContext context)
        throws RemoteException, FinderException, IDOLookupException {
        final java.util.Map inputs = new java.util.HashMap ();
        final String nowPeriod = periodFormatter.format (new Date ());
        final InvoiceHeader header = getInvoiceHeader (context);
        final User custodian = header.getCustodian ();
        final InvoiceBusiness business = getInvoiceBusiness (context);
        final String searchString = context.getParameter (RULE_TEXT_KEY);
        final java.sql.Date period = header.getPeriod ();
        final String categoryId =  header.getSchoolCategoryID ();
        final Collection regulations = new ArrayList ();
        inputs.put (PLACEMENT_KEY, getPlacementsDropdown (context, header));
        final SchoolClassMember placement = (SchoolClassMember)
                context.getSessionAttribute (PLACEMENT_KEY);
        final Provider provider
                = (null != placement ? new Provider
                   (placement.getSchoolClass ().getSchool ()) : null); 
        if (null != searchString && null != period && null != categoryId
            && null != provider && getActionId (context)
            == ACTION_SHOW_NEW_RECORD_FORM_AND_SEARCH_RULE_TEXT) {
            final RegulationHome home = getRegulationHome ();
            try {
                regulations.addAll
                        (home.findRegulationsByNameNoCaseDateAndCategory
                         (searchString + '%', period, categoryId));
            } catch (FinderException e) {
                // no problem, no regulation found
            }
        } 
        if (1 == regulations.size ()) {
            final Regulation regulation
                    = (Regulation) regulations.iterator ().next ();
            addPresentationObjectsForNewRecordForm
                    (context, inputs, header, business, period, provider,
                     regulation);
        } else {
            if (!regulations.isEmpty ()) {
                // regulations.size > 1
                addRegulationLinkListForNewRecordForm (context, inputs,
                                                       regulations);
            }
            inputs.put (RULE_TEXT_KEY, getStyledWideInput (RULE_TEXT_KEY,
                                                           searchString));
            inputs.put (INVOICE_TEXT_KEY, getStyledWideInput
                        (INVOICE_TEXT_KEY));
            inputs.put (AMOUNT_KEY, getStyledInput (AMOUNT_KEY));
            inputs.put (VAT_AMOUNT_KEY, getStyledInput (VAT_AMOUNT_KEY));
            inputs.put (REGULATION_SPEC_TYPE_KEY, getLocalizedDropdown
                        (business.getAllRegulationSpecTypes ()));
            inputs.put (VAT_RULE_KEY,  getLocalizedDropdown
                        (business.getAllVatRules ()));
            inputs.put (OWN_POSTING_KEY, getPostingParameterForm
                        (context, OWN_POSTING_KEY));
            inputs.put (DOUBLE_POSTING_KEY, getPostingParameterForm
                    (context, DOUBLE_POSTING_KEY));
        }
        inputs.put (INVOICE_RECEIVER_KEY, getSmallText (getUserInfo
                                                        (custodian)));
        inputs.put (CHECK_START_PERIOD_KEY, getStyledInput
                    (CHECK_START_PERIOD_KEY, nowPeriod));
        inputs.put (CHECK_END_PERIOD_KEY, getStyledInput
                    (CHECK_END_PERIOD_KEY, nowPeriod));
        inputs.put (PLACEMENT_START_PERIOD_KEY, getStyledInput
                    (PLACEMENT_START_PERIOD_KEY, nowPeriod));
        inputs.put (PLACEMENT_END_PERIOD_KEY, getStyledInput
                    (PLACEMENT_END_PERIOD_KEY, nowPeriod));
        inputs.put (NOTE_KEY, getStyledInput (NOTE_KEY));
        inputs.put (ACTION_KEY, getSubmitButton
                    (ACTION_NEW_RECORD, CREATE_INVOICE_RECORD_KEY,
                     CREATE_INVOICE_RECORD_DEFAULT));
        inputs.put (HEADER_KEY, localize (CREATE_INVOICE_RECORD_KEY,
                                          CREATE_INVOICE_RECORD_DEFAULT));
        if (null != period && null != categoryId && null != provider) {
            inputs.put (SEARCH_RULE_TEXT_KEY, getSubmitButton
                        (ACTION_SHOW_NEW_RECORD_FORM_AND_SEARCH_RULE_TEXT,
                         SEARCH_RULE_TEXT_KEY, SEARCH_RULE_TEXT_DEFAULT));
        }
        renderRecordDetailsOrForm (context, inputs);
    }

	private void showEditRecordForm (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = getInvoiceBusiness (context);
        final InvoiceRecord record = getInvoiceRecord (context);
        final java.util.Map inputs = new java.util.HashMap ();
        final InvoiceHeader header = getInvoiceHeader (context);
        final User custodian = header.getCustodian ();
        inputs.put (INVOICE_RECEIVER_KEY, getSmallText (getUserInfo
                                                        (custodian)));
        inputs.put (INVOICE_TEXT_KEY, getStyledWideInput
                    (INVOICE_TEXT_KEY, record.getInvoiceText ()));
        inputs.put (CHECK_START_PERIOD_KEY, getStyledInput
                    (CHECK_START_PERIOD_KEY, getFormattedPeriod
                     (record.getPeriodStartCheck ())));
        inputs.put (CHECK_END_PERIOD_KEY, getStyledInput
                    (CHECK_END_PERIOD_KEY, getFormattedPeriod
                     (record.getPeriodEndCheck ())));
        inputs.put (PLACEMENT_START_PERIOD_KEY, getStyledInput
                    (PLACEMENT_START_PERIOD_KEY, getFormattedPeriod
                     (record.getPeriodStartPlacement ())));
        inputs.put (PLACEMENT_END_PERIOD_KEY, getStyledInput
                    (PLACEMENT_END_PERIOD_KEY, getFormattedPeriod
                     (record.getPeriodEndPlacement ())));
        inputs.put (DATE_CREATED_KEY, getSmallText
                    (dateFormatter.format (record.getDateCreated ())));
        inputs.put (CREATED_SIGNATURE_KEY, getSmallSignature
                    (record.getCreatedBy ()));
        addSmallDateText (inputs, DATE_ADJUSTED_KEY, record.getDateChanged ());
        inputs.put (ADJUSTED_SIGNATURE_KEY,
                    getSmallSignature (record.getChangedBy ()));
        inputs.put (AMOUNT_KEY, getStyledInput
                    (AMOUNT_KEY, ((long) record.getAmount ()) +""));
        inputs.put (VAT_AMOUNT_KEY, getStyledInput (VAT_AMOUNT_KEY,
                    ((long) record.getAmountVAT ()) + ""));
        inputs.put (NOTE_KEY, getStyledInput (NOTE_KEY, record.getNotes ()));
        final DropdownMenu regulationSpecTypeDropdown = getLocalizedDropdown
                (business.getAllRegulationSpecTypes ());
        inputs.put (REGULATION_SPEC_TYPE_KEY, regulationSpecTypeDropdown);
        final int regulationSpecTypeId = record.getRegSpecTypeId ();
        if (0 < regulationSpecTypeId) {
            regulationSpecTypeDropdown.setSelectedElement
                    (regulationSpecTypeId + "");
        }
        final PresentationObject ownPostingForm = getPostingParameterForm
                (context, OWN_POSTING_KEY, record.getOwnPosting ());
        inputs.put (OWN_POSTING_KEY, ownPostingForm);
        final PresentationObject doublePostingForm = getPostingParameterForm
                (context, DOUBLE_POSTING_KEY, record.getDoublePosting ());
        inputs.put (DOUBLE_POSTING_KEY, doublePostingForm);
        final DropdownMenu vatRuleDropdown = getLocalizedDropdown
                (business.getAllVatRules ());
        inputs.put (VAT_RULE_KEY, vatRuleDropdown);
        final int vatRuleId = record.getVATType ();
        if (0 < vatRuleId) {
            vatRuleDropdown.setSelectedElement (vatRuleId + "");
        }
        inputs.put (ACTION_KEY, getSubmitButton
                    (ACTION_SAVE_RECORD, EDIT_INVOICE_RECORD_KEY,
                     EDIT_INVOICE_RECORD_DEFAULT));
        inputs.put (HEADER_KEY, localize (EDIT_INVOICE_RECORD_KEY,
                                          EDIT_INVOICE_RECORD_DEFAULT));
        inputs.put (RULE_TEXT_KEY, getSmallText (record.getRuleText ()));
        final String providerName = getProviderName
                (record.getSchoolClassMember ());
        inputs.put (PLACEMENT_KEY, getSmallText (providerName));
        renderRecordDetailsOrForm (context, inputs);
    }

    private void showRecordDetails (final IWContext context)
        throws RemoteException, FinderException {

        final InvoiceBusiness business = getInvoiceBusiness (context);
        final InvoiceRecord record = getInvoiceRecord (context);
        final java.util.Map details = new java.util.HashMap ();
        final InvoiceHeader header = getInvoiceHeader (context);
        details.put (INVOICE_RECEIVER_KEY,
                     getSmallText (getUserInfo (header.getCustodian ())));
        final SchoolClassMember placement = record.getSchoolClassMember ();
        if (null != placement) {
            addSmallText (details, PLACEMENT_KEY, getProviderName (placement));
        }
        addSmallText (details, AMOUNT_KEY, (long) record.getAmount ());
        addSmallText (details, VAT_AMOUNT_KEY, (long) record.getAmountVAT ());
        details.put (ADJUSTED_SIGNATURE_KEY,
                     getSmallSignature (record.getChangedBy ()));
        details.put (CREATED_SIGNATURE_KEY,
                     getSmallSignature (record.getCreatedBy ()));
        addSmallDateText (details, DATE_ADJUSTED_KEY, record.getDateChanged ());
        addSmallDateText (details, DATE_CREATED_KEY, record.getDateCreated ());
        addSmallText (details, NUMBER_OF_DAYS_KEY, record.getDays ());
        details.put (DOUBLE_POSTING_KEY, getPostingListTable
                    (context, record.getDoublePosting ()));
        addSmallText (details, INVOICE_TEXT_KEY, record.getInvoiceText ());
        addSmallText (details, NOTE_KEY, record.getNotes ());
        details.put (OWN_POSTING_KEY,
                    getPostingListTable (context, record.getOwnPosting ()));
        addSmallPeriodText (details, CHECK_START_PERIOD_KEY,
                            record.getPeriodStartCheck ());
        addSmallPeriodText (details, CHECK_END_PERIOD_KEY,
                            record.getPeriodEndCheck ());
        addSmallPeriodText (details, PLACEMENT_START_PERIOD_KEY,
                            record.getPeriodStartPlacement ());
        addSmallPeriodText (details, PLACEMENT_END_PERIOD_KEY,
                            record.getPeriodEndPlacement ());
        final RegulationSpecType regSpecType = record.getRegSpecType ();
        if (null != regSpecType) {
            final String typeName = regSpecType.getRegSpecType ();
            addSmallText (details, REGULATION_SPEC_TYPE_KEY,
                          localize (typeName, typeName));
        }
        if (0 < record.getVATType ()) {
            final VATRule rule = business.getVatRule (record.getVATType ());
            final String ruleName = rule.getVATRule ();
            details.put (VAT_RULE_KEY, getSmallText (localize (ruleName,
                                                              ruleName)));
        }
        addSmallText (details, RULE_TEXT_KEY, record.getRuleText ());
        details.put (HEADER_KEY, localize (INVOICE_RECORD_KEY,
                    INVOICE_RECORD_DEFAULT));
        renderRecordDetailsOrForm (context, details);
    }

    /**
     * Shows a list of invoice compilations and a search form.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilationList (final IWContext context) throws RemoteException {
        final UserSearcher searcher = createSearcher ();
        final Table table = createTable (6);
        setColumnWidthsEqual (table);
        int row = 2; // first row is reserved for setting column widths
        addOperationalFieldDropdown (context, table, row++);
        addUserSearcherForm (table, row++, context, searcher);
        table.mergeCells (2, row, table.getColumns () - 1, row);
        addPeriodForm (table, row, context);
        table.setAlignment (table.getColumns (), row,
                            Table.HORIZONTAL_ALIGN_RIGHT);
        table.add (getSubmitButton (ACTION_SHOW_COMPILATION_LIST,
                                    SEARCH_KEY, SEARCH_DEFAULT),
                   table.getColumns (), row++);

        if (null != searcher.getUser ()) {
            // exactly one user found - display users invoice compilation list
            final String operationalField
                    = getSession ().getOperationalField ();
            final User userFound = searcher.getUser ();
            final Date fromPeriod = getPeriodParameter (context,
                                                        START_PERIOD_KEY);
            final Date toPeriod = getPeriodParameter (context, END_PERIOD_KEY);
            final InvoiceBusiness business = getInvoiceBusiness (context);
            final InvoiceHeader [] headers
                    = business.getInvoiceHeadersByCustodianOrChild
                    (operationalField, userFound, fromPeriod, toPeriod);
            table.mergeCells (1, row, table.getColumns (), row);            
            if (0 < headers.length) {
                table.add (getInvoiceCompilationListTable (context, headers), 1,
                           row++);
            } else {
                table.add (new Text (localize (NO_COMPILATION_FOUND_KEY,
                                               NO_COMPILATION_FOUND_DEFAULT)),
                           1, row++);
            }
        } else if (null != searcher.getUsersFound ()) {
            table.mergeCells (1, row, table.getColumns (), row);            
            table.add (getSearcherResultTable
                       (searcher.getUsersFound (),
                        ACTION_SHOW_COMPILATION_LIST), 1, row++);
        }
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getSubmitButton (ACTION_SHOW_NEW_COMPILATION_FORM,
                                    NEW_KEY, NEW_DEFAULT), 1, row);
        createForm (context, table, INVOICE_COMPILATION_LIST_KEY,
                    INVOICE_COMPILATION_LIST_DEFAULT);
    }

	/**
     * Shows one invoice compilation.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilation (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = getInvoiceBusiness (context);
        final InvoiceHeader header = getInvoiceHeader (context);
		final Date period = header.getPeriod ();
		final User custodian = header.getCustodian ();

        final Table table = createTable (4);
        setColumnWidthsEqual (table);
        int row = 2;
        addOperationalFieldRow (table, header, row);
        final SubmitButton button
                = getSubmitButton (ACTION_GENERATE_COMPILATION_PDF, PDF_KEY,
                                   PDF_DEFAULT);
        int col = table.getColumns ();
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        table.add (button, col, row);
        col = 1; row++;
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
        table.add (getSubmitButton (ACTION_SHOW_NEW_RECORD_FORM, NEW_KEY,
                                    NEW_DEFAULT), 1, row);
        addCancelButton (table, 1, row);
        createForm (context, table, INVOICE_COMPILATION_KEY,
                    INVOICE_COMPILATION_DEFAULT);
    }

    private void deleteCompilation (final IWContext context) {
        try {
            final InvoiceBusiness business = getInvoiceBusiness (context);
            final InvoiceHeader header = getInvoiceHeader (context);
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
    
    private void deleteRecord (final IWContext context) {
        try {
            final InvoiceBusiness business = getInvoiceBusiness (context);
            final InvoiceRecord record = getInvoiceRecord (context);
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

    private void addRegulationLinkListForNewRecordForm
        (final IWContext context, final java.util.Map inputs,
         final Collection regulations) {
		final Table table = createTable (1);
		int row = 1;
		for (Iterator i = regulations.iterator (); i.hasNext ();) {
		    final Regulation regulation = (Regulation) i.next ();
		    final Link link = getSmallLink (regulation.getName ());
		    link.addParameter (ACTION_KEY, context.getParameter (ACTION_KEY));
		    link.addParameter (RULE_TEXT_KEY, regulation.getName ());
		    link.addParameter (INVOICE_COMPILATION_KEY, context.getParameter
		             (INVOICE_COMPILATION_KEY));
            link.addParameter (PLACEMENT_KEY, context.getSessionAttribute
                               (PLACEMENT_KEY) + "");
		    table.add (link, 1, row++);
		}
		inputs.put (RULE_TEXT_LINK_LIST_KEY, table);
	}

	private void addPresentationObjectsForNewRecordForm
        (final IWContext context, final java.util.Map inputs,
         final InvoiceHeader header, final InvoiceBusiness business,
         final java.sql.Date period, final Provider provider,
         final Regulation regulation) throws EJBException, RemoteException {
		final String regulationName = regulation.getName ();
		final RegulationSpecType regSpecType = regulation.getRegSpecType ();
		final Integer regSpecTypeId
		        = (Integer) regSpecType.getPrimaryKey ();
		final VATRule vatRule = regulation.getVATRegulation ();
		final SchoolCategory category = header.getSchoolCategory ();
		final RegulationsBusiness regulationsBusiness
		        = getRegulationsBusiness (context);
		final SchoolType schoolType
		        = regulationsBusiness.getSchoolType (regulation);
		final PostingBusiness postingBusiness = getPostingBusiness (context);
		inputs.put (RULE_TEXT_KEY, getStyledWideInput (RULE_TEXT_KEY,
                                                       regulationName));
		inputs.put (INVOICE_TEXT_KEY, getStyledWideInput (INVOICE_TEXT_KEY,
                                                          regulationName));
		inputs.put (AMOUNT_KEY, getStyledInput
		            (AMOUNT_KEY, regulation.getAmount () + ""));
		inputs.put (VAT_AMOUNT_KEY, getStyledInput (VAT_AMOUNT_KEY));
		inputs.put (REGULATION_SPEC_TYPE_KEY, getLocalizedDropdown
		            (business.getAllRegulationSpecTypes (), regSpecType));
		inputs.put (VAT_RULE_KEY,  getLocalizedDropdown
		            (business.getAllVatRules (), vatRule));
		try {
		    final String [] postings = postingBusiness.getPostingStrings
		            (category, schoolType, regSpecTypeId.intValue (),
		             provider, period);	
		    final PresentationObject ownPostingForm = getPostingParameterForm
                    (context, OWN_POSTING_KEY, postings [0]);
		    inputs.put (OWN_POSTING_KEY, ownPostingForm);
		    final PresentationObject doublePostingForm = getPostingParameterForm
                    (context, DOUBLE_POSTING_KEY, postings [1]);
		    inputs.put (DOUBLE_POSTING_KEY, doublePostingForm);
		} catch (PostingException e) {
		    e.printStackTrace ();
		    inputs.put (OWN_POSTING_KEY, getPostingParameterForm
		                (context, OWN_POSTING_KEY));
		    inputs.put (DOUBLE_POSTING_KEY, getPostingParameterForm
		                (context, DOUBLE_POSTING_KEY));
		}
	}

    private String getUserInfo (final User user) {
        return user == null ? "" : getUserName (user) + " (" + formatSsn
                (user.getPersonalID ()) + "), " + getAddressString (user);
    }
    
    private boolean isCustodian (final IWContext context, final User user)
        throws RemoteException {
        final MemberFamilyLogic familyBusiness = getMemberFamilyLogic (context);
        try {
            final Collection children
                    = familyBusiness.getChildrenInCustodyOf (user);
            return null != children && !children.isEmpty ();
        } catch (Exception e) {
            return false;
        }
    }

    private void addInvoiceReceiver (final IWContext context, final Table table,
                                     final int col, final int row,
                                     final User user) {
        table.mergeCells (col, row, table.getColumns (), row);
        User receiver = null;
        try {
            if (isOver18 (user.getPersonalID ()) || isCustodian (context,
                                                                 user)) {
                receiver = user;
            } else {
                final MemberFamilyLogic familyBusiness
                        = getMemberFamilyLogic (context);
                final Collection custodians
                        = familyBusiness.getCustodiansFor (user);
                if (null == custodians || custodians.isEmpty ()) {
                    receiver = user;
                } else if (1 == custodians.size ()) {
                    receiver = (User) custodians.iterator ().next ();
                } else {
                    final Table radioTable = createTable (2);
                    int radioRow = 1;
                    for (Iterator i = custodians.iterator (); i.hasNext ();
                         radioRow++) {
                        final User custodian = (User) i.next ();
                        final RadioButton button = getRadioButton
                                (INVOICE_RECEIVER_KEY,
                                 custodian.getPrimaryKey () + "");
                        if (1 == radioRow) button.setSelected ();
                        radioTable.add (button, 1, radioRow);
                        radioTable.add (getUserInfo (custodian), 2, radioRow);
                    }
                    table.add (radioTable, col, row);
                }
            }
        } catch (Exception e) {
            receiver = user;
        }

        if (null != receiver) {
            table.add (new HiddenInput
                       (INVOICE_RECEIVER_KEY, receiver.getPrimaryKey () + ""),
                       col, row);
            addSmallText (table, getUserInfo (receiver), col, row);
        }
    }

    private static boolean isOver18 (final String ssn) {
        final int year = new Integer (ssn.substring (0, 4)).intValue ();
        final int month = new Integer (ssn.substring (4, 6)).intValue ();
        final int day = new Integer (ssn.substring (6, 8)).intValue ();
        final Date rightNow = Calendar.getInstance ().getTime();
        final Calendar birthday18 = Calendar.getInstance ();
        birthday18.set (year + 18, month - 1, day);
        return rightNow.after (birthday18.getTime());
    }

    private void showNewCompilationForm (final IWContext context)
        throws RemoteException {
        final UserSearcher searcher = createSearcher ();
        final Table table = createTable (6);
        setColumnWidthsEqual (table);
        int row = 2; // first row is reserved for setting column widths
        addOperationalFieldDropdown (context, table, row++);
        addUserSearcherForm (table, row++, context, searcher);
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getSubmitButton (ACTION_SHOW_NEW_COMPILATION_FORM,
                                    SEARCH_INVOICE_RECEIVER_KEY,
                                    SEARCH_INVOICE_RECEIVER_DEFAULT), 1, row++);
        if (null != searcher.getUser ()) {
            // exactly one user found - display users invoice compilation list
            table.setHeight (row++, 12);
            int col = 1;
            addSmallHeader (table, col++, row, INVOICE_RECEIVER_KEY,
                            INVOICE_RECEIVER_DEFAULT, ":");
            addInvoiceReceiver (context, table, col, row++,
                                searcher.getUser ());
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
            table.add (getSubmitButton (ACTION_NEW_COMPILATION,
                                        CREATE_INVOICE_COMPILATION_KEY,
                                        CREATE_INVOICE_COMPILATION_DEFAULT), 1,
                       row);
        } else if (null != searcher.getUsersFound ()) {
            // many users found
            table.mergeCells (1, row, table.getColumns (), row);
            table.add (getSearcherResultTable
                       (searcher.getUsersFound (),
                        ACTION_SHOW_NEW_COMPILATION_FORM), 1, row);
        }
        addCancelButton (table, 1, row);
        createForm (context, table, CREATE_INVOICE_COMPILATION_KEY,
                    CREATE_INVOICE_COMPILATION_DEFAULT);
    }

    private void newCompilation (final IWContext context)
        throws RemoteException, CreateException {
        final String operationalField = getSession ().getOperationalField ();
        final Date period = getPeriodParameter (context, PERIOD_KEY);
        final int custodianId = Integer.parseInt (context.getParameter
                                                  (INVOICE_RECEIVER_KEY));
        final User currentUser = context.getCurrentUser ();
        final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
        final String doublePosting = getPostingString (context,
                                                       DOUBLE_POSTING_KEY);
        final InvoiceBusiness business = getInvoiceBusiness (context);
        final InvoiceHeader header = business.createInvoiceHeader
                (operationalField, currentUser, custodianId,  ownPosting,
                 doublePosting, new java.sql.Date (period.getTime ()));
        final String [][] parameters =
                {{ACTION_KEY, ACTION_SHOW_COMPILATION + "" },
                { INVOICE_COMPILATION_KEY,
                  header.getPrimaryKey ().toString () }};
        final Table table = getConfirmTable
                (INVOICE_COMPILATION_CREATED_KEY,
                 INVOICE_COMPILATION_CREATED_DEFAULT, parameters);
        add (createMainTable (CREATE_INVOICE_COMPILATION_KEY,
                              CREATE_INVOICE_COMPILATION_DEFAULT, table));
    }
	
    private void renderRecordDetailsOrForm
        (final IWContext context, final java.util.Map presentationObjects)
        throws RemoteException, FinderException {
        final InvoiceHeader header = getInvoiceHeader (context);

        // render form/details
        final Table table = createTable (4);
        setColumnWidthsEqual (table);
        int row = 2;
        int col = 1;
        addOperationalFieldRow (table, header, row++);
        col = 1;
        addSmallHeader (table, col++, row, INVOICE_RECEIVER_KEY,
                        INVOICE_RECEIVER_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, INVOICE_RECEIVER_KEY,
                         col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, PLACEMENT_KEY, col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PLACEMENT_KEY, PLACEMENT_DEFAULT,
                        ":");
        table.mergeCells (col, row, table.getColumns () - 1, row);
        addPresentation (table, presentationObjects, RULE_TEXT_KEY, col++, row);
        col = table.getColumns ();
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
        addPresentation (table, presentationObjects, SEARCH_RULE_TEXT_KEY,
                         col, row);
        col = 1; row++;
        if (presentationObjects.containsKey (RULE_TEXT_LINK_LIST_KEY)) {
            table.mergeCells (1, row, table.getColumns (), row);
            addPresentation (table, presentationObjects,
                             RULE_TEXT_LINK_LIST_KEY, 1, row);
            row++;
        }
        addSmallHeader (table, col++, row, INVOICE_TEXT_KEY,
                        INVOICE_TEXT_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns (), row);
        addPresentation (table, presentationObjects, INVOICE_TEXT_KEY, col++,
                         row);
        col = 1; row++;
        if (presentationObjects.containsKey (NUMBER_OF_DAYS_KEY)) {
            addSmallHeader (table, col++, row, NUMBER_OF_DAYS_KEY,
                            NUMBER_OF_DAYS_DEFAULT, ":");
            addPresentation (table, presentationObjects, NUMBER_OF_DAYS_KEY,
                             col++, row);
            col = 1; row++;
        }
        addSmallHeader (table, col++, row, CHECK_PERIOD_KEY,
                        CHECK_PERIOD_DEFAULT, ":");
        addSmallHeader (table, col, row, START_PERIOD_KEY,
                        START_PERIOD_DEFAULT);
        addPresentation (table, presentationObjects, CHECK_START_PERIOD_KEY,
                         col++,
                   row);
        addSmallHeader (table, col, row, END_PERIOD_KEY, END_PERIOD_DEFAULT);
        addPresentation (table, presentationObjects, CHECK_END_PERIOD_KEY,
                         col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, PLACEMENT_PERIOD_KEY,
                        PLACEMENT_PERIOD_DEFAULT, ":");
        addSmallHeader (table, col, row, START_PERIOD_KEY,
                        START_PERIOD_DEFAULT);
        addPresentation (table, presentationObjects, PLACEMENT_START_PERIOD_KEY,
                         col++, row);
        addSmallHeader (table, col, row, END_PERIOD_KEY, END_PERIOD_DEFAULT);
        addPresentation (table, presentationObjects, PLACEMENT_END_PERIOD_KEY,
                         col++, row);
        col = 1; row++;
        if (presentationObjects.containsKey (DATE_CREATED_KEY)) {
            addSmallHeader (table, col++, row, DATE_CREATED_KEY,
                            DATE_CREATED_DEFAULT, ":");
            addPresentation (table, presentationObjects, DATE_CREATED_KEY,
                             col++,  row);
            addSmallHeader (table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT,
                            ":");
            addPresentation (table, presentationObjects, CREATED_SIGNATURE_KEY,
                             col++, row);
            col = 1; row++;
            addSmallHeader (table, col++, row, DATE_ADJUSTED_KEY,
                            DATE_ADJUSTED_DEFAULT, ":");
            addPresentation (table, presentationObjects, DATE_ADJUSTED_KEY,
                             col++, row);
            addSmallHeader (table, col++, row, SIGNATURE_KEY, SIGNATURE_DEFAULT,
                            ":");
            addPresentation (table, presentationObjects, ADJUSTED_SIGNATURE_KEY,
                             col++, row);
            col = 1; row++;
        }
        addSmallHeader (table, col++, row, AMOUNT_KEY, AMOUNT_DEFAULT, ":");
        addPresentation (table, presentationObjects, AMOUNT_KEY, col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, VAT_AMOUNT_KEY, VAT_AMOUNT_DEFAULT,
                        ":");
        addPresentation (table, presentationObjects, VAT_AMOUNT_KEY, col++,
                         row);
        col = 1; row++;
        addSmallHeader (table, col++, row, NOTE_KEY, NOTE_DEFAULT, ":");
        addPresentation (table, presentationObjects, NOTE_KEY, col++, row);
        col = 1; row++;
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
        addPresentation (table, presentationObjects, ACTION_KEY, 1, row);
        addCancelButton (table, 1, row);
        createForm (context, table,
                    presentationObjects.get (HEADER_KEY).toString ());
    }

    private void addCancelButton (final Table table, final int col,
                                  final int row) {
        table.add (Text.getNonBrakingSpace(), col, row);
        table.add (getSubmitButton (ACTION_SHOW_COMPILATION_LIST,
                                    CANCEL_KEY, CANCEL_DEFAULT), col, row);
    }

	private PdfPTable getOwnPostingPdfTable
        (final IWContext context, final InvoiceRecord [] records,
         final Color lightBlue) throws RemoteException {
		final PostingField [] fields = getCurrentPostingFields (context);
        final PdfPTable table = new PdfPTable (fields.length + 1);
        table.setWidthPercentage (100f);
        table.getDefaultCell ().setBackgroundColor (new Color (0xd0daea));
        table.getDefaultCell ().setBorder (0);
        for (int i = 0; i < fields.length; i++) {
            addPhrase (table, fields [i].getFieldTitle ());
        }
        addPhrase (table, localize (AMOUNT_KEY, AMOUNT_DEFAULT));
        for (int i = 0; i < records.length; i++) {
            final InvoiceRecord record = records [i];
            final String postingString = record.getOwnPosting ();
            table.getDefaultCell ().setBackgroundColor (i % 2 == 0 ? Color.white
                                                        : lightBlue);
            int offset = 0;
            for (int j = 0; j < fields.length; j++) {
                final PostingField field = fields [j];
                final int endPosition = min (offset + field.getLen (),
                                             postingString.length ());
                addPhrase (table, postingString.substring
                           (offset, endPosition).trim ());
                offset = endPosition;
            }
            addPhrase (table, ((long)record.getAmount ()) + "");
        }
		return table;
	}

	private PdfPTable getInvoiceRecordPdfTable
        (final String [] columnNames, final InvoiceBusiness business,
         final InvoiceRecord[] records, final Color lightBlue)
        throws RemoteException {
        final PdfPTable table = new PdfPTable
                (new float [] { 1.2f, 1.2f, 2.0f, 1.0f, 1.0f, 2.0f });
		table.setWidthPercentage (100f);
        table.getDefaultCell ().setBackgroundColor (new Color (0xd0daea));
        table.getDefaultCell ().setBorder (0);
        for (int i = 0; i < columnNames.length; i++) {
            addPhrase (table, columnNames [i]);
        }
        table.setHeaderRows (1);  // this is the end of the table header
        for (int i = 0; i < records.length; i++) {
            final InvoiceRecord record = records [i];
            table.getDefaultCell ().setBackgroundColor (i % 2 == 0 ? Color.white
                                                        : lightBlue);
            addInvoiceRecordOnAPdfRow (business, table, record);
        }
		return table;
	}

	private void addInvoiceRecordOnAPdfRow
        (final InvoiceBusiness business, final PdfPTable table,
         final InvoiceRecord record) throws RemoteException {
		final User child = business.getChildByInvoiceRecord (record);
		final String ssn = null != child ? formatSsn (child.getPersonalID ())
                : "";
		final String firstName = null != child ? child.getFirstName () : "";
		addPhrase (table, ssn);
		addPhrase (table, firstName);
		addPhrase (table, record.getInvoiceText ());
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (table, record.getDays () + "");
		addPhrase (table, ((long) record.getAmount ()) + "");
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		addPhrase (table, record.getNotes ());
	}

    private int dayDiff (final Date date1, final Date date2) {
        long millis1 = date1.getTime ();
        long millis2 = date2.getTime ();
        long millisDiff = millis2 - millis1;
        return 0 <= millisDiff
                ? 1 + (int) (millisDiff / (1000 * 60 * 60 * 24)) : 0;
    }

	private static float mmToPoints (final float mm) {
		return mm*72/25.4f;
	}

	private final static Font SANSSERIF_FONT
		= FontFactory.getFont (FontFactory.HELVETICA, 9);
    
    private void addPhrase (final PdfPTable table, final String string) {
        table.addCell (new Phrase (new Chunk (null != string ? string : "",
                                              SANSSERIF_FONT)));
    }

    private void addPresentation
        (final Table table, final java.util.Map map, final String key,
         final int col, final int row) {
        final PresentationObject object = (PresentationObject) map.get (key);
        if (null != object) {
            table.add (object, col, row);
        }
    }

    private InvoiceHeader getInvoiceHeader (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = getInvoiceBusiness (context);
        Integer headerId = getIntegerParameter (context,
                                                INVOICE_COMPILATION_KEY);
        if (null == headerId) {
            final InvoiceRecord record = getInvoiceRecord (context);
            headerId = new Integer (record.getInvoiceHeader ());
        }
        final InvoiceHeaderHome headerHome = business.getInvoiceHeaderHome ();
        return headerHome.findByPrimaryKey (headerId);
    }

    private InvoiceRecord getInvoiceRecord (final IWContext context)
        throws RemoteException, FinderException {
        final InvoiceBusiness business = getInvoiceBusiness (context);
        final Integer recordId = getIntegerParameter (context,
                                                      INVOICE_RECORD_KEY);
        final InvoiceRecordHome recordHome = business.getInvoiceRecordHome ();
        return null != recordId ? recordHome.findByPrimaryKey (recordId) : null;
    }
    
    private String getSignature (final User user) {
        if (null == user) return "";
        final String firstName = user.getFirstName ();
        final String lastName = user.getLastName ();
        return (firstName != null ? firstName + " " : "")
                + (lastName != null ? lastName : "");
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
        throws RemoteException {

        // set up header row
        final String [][] columnNames =
                {{ STATUS_KEY, STATUS_DEFAULT }, { PERIOD_KEY, PERIOD_DEFAULT },
                 { INVOICE_RECEIVER_KEY, INVOICE_RECEIVER_DEFAULT },
                 { TOTAL_AMOUNT_KEY, TOTAL_AMOUNT_DEFAULT }, {"no_text", ""},
                 {"no_text", ""}};
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
        final InvoiceBusiness invoiceBusiness = getInvoiceBusiness (context);

        // show each invoice header in a row
        for (int i = 0; i < headers.length; i++) {
			showInvoiceHeaderOnARow (table, row++, invoiceBusiness,
                                     headers [i]);
        }
        
       return table;
    }

	private void showInvoiceHeaderOnARow
        (final Table table, final int row,
         final InvoiceBusiness business, final InvoiceHeader header) {
		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
		                   : getZebraColor2 ());
		final char status = header.getStatus ();
		final User custodian = header.getCustodian ();
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
         final InvoiceHeader header) throws RemoteException {

 		final InvoiceRecord [] records
		        = business.getInvoiceRecordsByInvoiceHeader (header);

        // set up header row
        final String [][] columnNames =
                {{ SSN_KEY, SSN_DEFAULT },
                 { FIRST_NAME_KEY, FIRST_NAME_DEFAULT },
                 { INVOICE_TEXT_KEY, INVOICE_TEXT_DEFAULT },
                 { NUMBER_OF_DAYS_KEY, NUMBER_OF_DAYS_DEFAULT },
                 { AMOUNT_KEY, AMOUNT_DEFAULT },
                 { REMARK_KEY, REMARK_DEFAULT }, {"no_text", ""},
                 {"no_text", ""}};
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
        final InvoiceBusiness invoiceBusiness = getInvoiceBusiness (context);

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
            addSmallText (table, formatSsn (child.getPersonalID ()), col++,
                          row);
            addSmallText (table, child.getFirstName (), col++, row);
        } else {
            col += 2;
        }
        final String recordId = record.getPrimaryKey ().toString ();
        final boolean isManualRecord = isManualRecord (record);
        final String [][] editLinkParameters = getRecordLinkParameters
                (isManualRecord ? ACTION_SHOW_EDIT_RECORD_FORM
                 : ACTION_SHOW_RECORD_DETAILS, recordId);
        final Link textLink = createSmallLink (record.getInvoiceText (),
                                               editLinkParameters);
        table.add (textLink, col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (getSmallText (record.getDays () + ""), col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (getSmallText (((long) record.getAmount ()) + ""), col++,
                   row);
        addSmallText (table, record.getNotes (), col++, row);
        final Link editLink = createIconLink (getEditIcon (),
                                              editLinkParameters);
        table.add (editLink, col++, row);
        if (isManualRecord) {
            final String [][] deleteLinkParamaters = getRecordLinkParameters
                    (ACTION_DELETE_RECORD,  recordId);
            final Link deleteLink = createIconLink (getDeleteIcon (),
                                                    deleteLinkParamaters);
            table.add (deleteLink, col++, row);
        }
    }

    private String getProviderName (final SchoolClassMember placement) {
        if (null == placement) return "";
        final String providerName
                = placement.getSchoolClass ().getSchool ().getName ();
        final String groupName = placement.getSchoolClass ().getName ();
        final User child = placement.getStudent ();
        return providerName + ", " + groupName + " - " + getUserName (child)
                + " (" + formatSsn (child.getPersonalID ()) + ")";
    }

    private String getSchoolCategoryName (final InvoiceHeader header) {
        try {
            final SchoolCategory category = header.getSchoolCategory ();
            return localize (category.getLocalizedKey (), category.getName ());
        } catch (Exception dummy) {
            return "";
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
            postingInputs.add (getStyledInterface
                               (getTextInput (key + j, "", 80,
                                              field.getLen())));
        }       
		return postingInputs;
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
        final PostingBusiness business = getPostingBusiness (context);
        final java.sql.Date now = new java.sql.Date (new Date ().getTime ());
        final Collection fields = business.getAllPostingFieldsByDate (now);
        final PostingField [] array = new PostingField [0];
        return fields != null ? (PostingField []) fields.toArray (array)
                : array;
    }

	private String getPostingString (final IWContext context,
                                     final String postingKey)
        throws RemoteException {
        final PostingBusiness business = getPostingBusiness (context);
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

    private void createForm
        (final IWContext context, final Table table, final String key,
         final String defaultHeader) {
        createForm (context, table, localize (key, defaultHeader));
    }

    private void createForm
        (final IWContext context, final Table table, final String header) {
		final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        form.maintainParameter (INVOICE_COMPILATION_KEY);
        form.maintainParameter (INVOICE_RECORD_KEY);
        if (context.isParameterSet (ACTION_KEY)) {
            table.add (new HiddenInput
                       (LAST_ACTION_KEY, context.getParameter (ACTION_KEY)),
                       1, 1);
        } else if (context.isParameterSet (LAST_ACTION_KEY)) {
            form.maintainParameter (LAST_ACTION_KEY);
        }
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (header, outerTable));
	}

	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
     * @return Table to add to output
	 */
    private Table createMainTable
        (final String header, final PresentationObject content) {
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
                 final PresentationObject content) {
        return createMainTable (localize (headerKey, headerDefault), content);
    }

    private static boolean isManualRecord (final InvoiceRecord record) {
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

    private void addPeriodForm (final Table table, final int row,
                                final IWContext context) {
        int col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        final Date now = new Date ();
        final Date fromDate = getPeriodParameter (context, START_PERIOD_KEY);
        final Date endDate = getPeriodParameter (context, END_PERIOD_KEY);
        table.add (getStyledInput
                   (START_PERIOD_KEY, periodFormatter.format
                    (null != fromDate ? fromDate : now)), col, row);
        table.add (new Text (" - "), col, row);
        table.add (getStyledInput
                   (END_PERIOD_KEY, periodFormatter.format
                    (null != endDate ? endDate : now)), col, row);
    }

    private String getFormattedPeriod (final Date date) {
        return date == null ? "" : periodFormatter.format (date);
    }

    private TextInput getStyledInput (final String key) {
        return getStyledInput (key, null);
    }

    private TextInput getStyledInput (final String key, final String value) {
        final TextInput input = (TextInput) getStyledInterface
                (new TextInput (key));
        input.setLength (12);
        if (null != value && !value.equals (null + "")) {
            input.setValue (value);
        }
        return input;
    }

    private TextInput getStyledWideInput (final String key) {
        return getStyledWideInput (key, null);
    }

    private TextInput getStyledWideInput (final String key,
                                          final String value) {
        final TextInput input = getStyledInput (key, value);
        input.setLength (36);
        return input;
    }

    private void addSmallText (final Table table, final String string,
                               final int col, final int row) {
        table.add (getSmallText (null != string && !string.equals (null + "")
                                 ? string : ""), col, row);
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

    private SubmitButton getSubmitButton (final int action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (localize (key, defaultName),
                                          ACTION_KEY, action + ""));
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
        (final Table table, final InvoiceHeader header,
         final int row) throws RemoteException {
        int col = 1;
        addSmallHeader (table, col++, row, MAIN_ACTIVITY_KEY,
                        MAIN_ACTIVITY_DEFAULT, ":");
        table.mergeCells (col, row, table.getColumns () - 1, row);
        addSmallText (table, getSchoolCategoryName (header), col++,
                      row);
        final String schoolCategory = header.getSchoolCategoryID ();
        if (null != schoolCategory && 0 < schoolCategory.length ()) {
            getSession ().setOperationalField (schoolCategory);
        }
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
        final StringBuffer message = new StringBuffer ();
        message.append ("Exception caught in " + getClass ().getName ()
                        + " " + (new java.util.Date ()) + '\n');
        message.append ("Parameters:\n");
        final java.util.Enumeration enum = context.getParameterNames ();
        while (enum.hasMoreElements ()) {
            final String key = (String) enum.nextElement ();
            message.append ('\t' + key + "='"
                            + context.getParameter (key) + "'\n");
        }
        logWarning (message.toString ());
        final java.io.StringWriter sw = new java.io.StringWriter ();
        exception.printStackTrace (new java.io.PrintWriter (sw, true));
        logWarning (sw.toString ());
        add ("Det inträffade ett fel. Försök igen senare.");
    }

    private static String getUserName (final User user) {
        return getUserName (user, true);
    }
    
    private static String getUserName (final User user,
                                       final boolean backwards) {
        final StringBuffer result = new StringBuffer ();
        if (null != user) {
            final String firstName = user.getFirstName ();
            final String lastName = user.getLastName ();
            if (backwards) {
                result.append (null != lastName ? lastName : "");
                result.append (null != firstName && null != lastName ? ", "
                               : "");
                result.append (null != firstName ? firstName : "");
            } else {
                result.append (null != firstName ? firstName + " ": "");
                result.append (null != lastName ? lastName : "");
            }
        }
        return result.toString ();
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

    private void addSmallText (final java.util.Map map, final String key,
                               final String value) {
        map.put (key, getSmallText (null != value && !value.equals (null + "")
                                    ? value : ""));
    }

    private void addSmallText (final java.util.Map map, final String key,
                               final long value) {
        map.put (key, getSmallText (-1 != value ? value + "" : "0"));
    }

    private void addSmallDateText (final java.util.Map map, final String key,
                                     final Date date) {
        map.put (key, getSmallText (null != date ? dateFormatter.format (date)
                                    : ""));
    }

    private void addSmallPeriodText (final java.util.Map map, final String key,
                                     final Date date) {
        map.put (key, getSmallText (null != date ? periodFormatter.format (date)
                                    : ""));
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
        return getLocalizedDropdown (rules, null);
    }

    private DropdownMenu getLocalizedDropdown
        (final VATRule [] rules, final VATRule defaultRule) {
        final DropdownMenu dropdown = (DropdownMenu)
                getStyledInterface (new DropdownMenu (VAT_RULE_KEY));
        final Object defaultRuleId = null != defaultRule
                ? defaultRule.getPrimaryKey () : null;
        for (int i = 0; i < rules.length; i++) {
            final VATRule rule = rules [i];
            final String ruleName = rule.getVATRule ();
            final Object ruleId = rule.getPrimaryKey ();
            dropdown.addMenuElement (ruleId + "",
                                     localize (ruleName, ruleName));
            if (null != defaultRuleId && defaultRuleId.equals (ruleId)) {
                dropdown.setSelectedElement (ruleId + "");
            }
        }
        return dropdown;
    }

    private PresentationObject getPlacementsDropdown
        (final IWContext context, final InvoiceHeader header)
        throws RemoteException {
        PresentationObject result = getSmallText
                (localize (NO_RELATED_PLACEMENT_FOUND_FOR_KEY,
                           NO_RELATED_PLACEMENT_FOUND_FOR_DEFAULT) + " "
                 + getUserName (header.getCustodian (), false));
        final InvoiceBusiness business = getInvoiceBusiness (context);
        final SchoolClassMember [] placements
                = business.getSchoolClassMembers (header);
        context.setSessionAttribute (PLACEMENT_KEY, null);
        if (1 == placements.length) {
            final Table table = createTable (1);
            final SchoolClassMember placement = placements [0];
            table.add (new HiddenInput (PLACEMENT_KEY,
                                        placement.getPrimaryKey () + ""), 1, 1);
            addSmallText (table, getProviderName (placement), 1, 1);
            result = table;
            context.setSessionAttribute (PLACEMENT_KEY, placement); 
        } else if (1 < placements.length) {
            final Integer oldPlacementId = getIntegerParameter (context,
                                                                PLACEMENT_KEY);
            final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                    (new DropdownMenu (PLACEMENT_KEY));
            for (int i = 0; i < placements.length; i++) {
                final SchoolClassMember placement = placements [i];
                final Integer placementId
                        = (Integer) placement.getPrimaryKey ();
                dropdown.addMenuElement (placementId + "",
                                         getProviderName (placement));
                if (null == context.getSessionAttribute (PLACEMENT_KEY) ||
                    (null != oldPlacementId
                     && placementId.equals (oldPlacementId))) {
                    dropdown.setSelectedElement (placementId + "") ;
                    context.setSessionAttribute (PLACEMENT_KEY, placement); 
                }
            }
            dropdown.setOnChange ("this.form.submit()");
            result = dropdown;
        }

        return result;
    }

    private DropdownMenu getLocalizedDropdown
        (final RegulationSpecType [] types) {
        final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (REGULATION_SPEC_TYPE_KEY));
        for (int i = 0; i < types.length; i++) {
            final RegulationSpecType type = types [i];
            final String name = type.getRegSpecType ();
            if (null != name && !name.endsWith (".blank")) {
                dropdown.addMenuElement (type.getPrimaryKey () + "",
                                         localize (name, name));
            }
        }
        return dropdown;
    }
 
    private DropdownMenu getLocalizedDropdown
        (final RegulationSpecType [] types,
         final RegulationSpecType defaultType) {
        final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (REGULATION_SPEC_TYPE_KEY));
        final Object defaultTypeId = null != defaultType
                ? defaultType.getPrimaryKey () : null;
        for (int i = 0; i < types.length; i++) {
            final RegulationSpecType type = types [i];
            final Object typeId = type.getPrimaryKey ();
            final String name = type.getRegSpecType ();
            if (null != name && !name.endsWith (".blank")) {
                dropdown.addMenuElement (typeId + "", localize (name, name));
            }
            if (null != defaultTypeId && defaultTypeId.equals (typeId)) {
                dropdown.setSelectedElement (typeId + "");
            }
        }
        return dropdown;
    }

   private static RegulationHome getRegulationHome ()
        throws IDOLookupException {
        return (RegulationHome) IDOLookup.getHome (Regulation.class);
    }

	private static SchoolBusiness getSchoolBusiness
        (final IWContext context) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance
                (context, SchoolBusiness.class);	
	}

	private static MemberFamilyLogic getMemberFamilyLogic
        (final IWContext context) throws RemoteException {
		return (MemberFamilyLogic) IBOLookup.getServiceInstance
                (context, MemberFamilyLogic.class);	
	}

	private static InvoiceBusiness getInvoiceBusiness
        (final IWContext context) throws RemoteException {
		return (InvoiceBusiness) IBOLookup.getServiceInstance
                (context, InvoiceBusiness.class);	
	}

	private static PostingBusiness getPostingBusiness
        (final IWContext context) throws RemoteException {
		return (PostingBusiness) IBOLookup.getServiceInstance
                (context, PostingBusiness.class);	
	}

	private static RegulationsBusiness getRegulationsBusiness
        (final IWContext context) throws RemoteException {
		return (RegulationsBusiness) IBOLookup.getServiceInstance
                (context, RegulationsBusiness.class);	
	}
}
