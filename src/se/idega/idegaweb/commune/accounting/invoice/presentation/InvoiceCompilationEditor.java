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
import com.idega.user.presentation.UserSearcher;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
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
import is.idega.block.family.business.FamilyLogic;
import java.awt.Color;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
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
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRule;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

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
 * Last modified: $Date: 2004/10/14 10:22:53 $ by $Author: thomas $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.134 $
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
	private static final String CLEAR_DEFAULT = "Rensa";
	private static final String CLEAR_KEY = PREFIX + "clear";
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
	private static final String DOUBLE_PAYMENT_POSTING_KEY = PREFIX + "double_payment_posting";
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
	private static final String INVOICE_RECORD_DEFAULT = "Fakturarad";
	private static final String INVOICE_RECORD_KEY = PREFIX + "invoice_record";
	private static final String INVOICE_RECORD_REMOVED_DEFAULT = "Fakturaraden är borttagen";
	private static final String INVOICE_RECORD_REMOVED_KEY = PREFIX + "invoice_record_removed";
	private static final String INVOICE_TEXT2_KEY = PREFIX + "invoice_text2";
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
	private static final String NO_PAYMENT_POSTINGS_FOUND_DEFAULT = "Ingen kontering för utbetalningen hittades";
	private static final String NO_PAYMENT_POSTINGS_FOUND_KEY = PREFIX + "no_payment_postings_found";
	private static final String NO_RELATED_PLACEMENT_FOUND_FOR_DEFAULT = "Ingen relaterad placering hittades för";
	private static final String NO_RELATED_PLACEMENT_FOUND_FOR_KEY = PREFIX + "no_related_placement_found_for";
	private static final String NO_RULE_FOUND_STARTING_WITH_DEFAULT = "Ingen regel börjar med";
	private static final String NO_RULE_FOUND_STARTING_WITH_KEY = PREFIX + "no_rule_found_starting_with";
	private static final String NUMBER_OF_DAYS_DEFAULT = "Antal dagar";
	private static final String NUMBER_OF_DAYS_KEY = PREFIX + "number_of_days";
	private static final String NUMBER_OF_DEFAULT = "Antal";
	private static final String NUMBER_OF_KEY = PREFIX + "number_of";
	private static final String ORDER_ID_KEY = PREFIX + "order_id";
	private static final String OWN_PAYMENT_POSTING_KEY = PREFIX + "own_payment_posting";
	private static final String OWN_POSTING_DEFAULT = "Egen kontering";
	private static final String OWN_POSTING_KEY = PREFIX + "own_posting";
	private static final String PDF_DEFAULT = "PDF";
	private static final String PDF_KEY = PREFIX + "pdf";
	private static final String PERIOD_DEFAULT = "Period";
	private static final String PERIOD_KEY = PREFIX + "period";
	private static final String PIECE_AMOUNT_KEY = PREFIX + "piece_amount";
	private static final String PLACEMENT_DEFAULT = "Placering";
	private static final String PLACEMENT_END_PERIOD_KEY = PREFIX + "placement_end_period";
	private static final String PLACEMENT_KEY = PREFIX + "placement";
	private static final String PLACEMENT_PERIOD_DEFAULT = "Placeringsperiod";
	private static final String PLACEMENT_PERIOD_KEY = PREFIX + "placement_period";
	private static final String PLACEMENT_START_PERIOD_KEY = PREFIX + "placement_start_period";
	private static final String PRINT_DATE_DEFAULT = "Utskriftsdatum";
	private static final String PRINT_DATE_KEY = PREFIX + "print_date";
	private static final String PROVIDER_DEFAULT = "Anordnare";
	private static final String PROVIDER_KEY = PREFIX + "provider";
	private static final String REGULATION_SPEC_TYPE_DEFAULT = "Regelspec.typ";
	private static final String REGULATION_SPEC_TYPE_KEY = PREFIX + "regulation_spec_type";
	private static final String REMARK_DEFAULT = "Anmärkning";
	private static final String REMARK_KEY = PREFIX + "remark";
	private static final String RULE_TEXT_KEY = PREFIX + "rule_text";
	private static final String RULE_TEXT_LINK_LIST_KEY = PREFIX + "rule_text_link_list";
	private static final String SEARCH_DEFAULT = "Sök";
	private static final String SEARCH_INVOICE_RECEIVER_DEFAULT = "Sök efter fakturamottagare";
	private static final String SEARCH_INVOICE_RECEIVER_KEY = PREFIX + "search_invoice_receiver";
	private static final String SEARCH_KEY = PREFIX + "search";
	private static final String SEARCH_RULE_TEXT_DEFAULT = "Sök regeltext";
	private static final String SEARCH_RULE_TEXT_KEY = PREFIX + "search_rule_text";
	private static final String SIGNATURE_DEFAULT = "Signatur";
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
			ACTION_GENERATE_COMPILATION_PDF = 12,
			ACTION_SHOW_COMPILATION_LIST_AND_CLEAR_FORM = 13;
	
	private static final SimpleDateFormat periodFormatter
		= new SimpleDateFormat ("yyMM");
	private static final SimpleDateFormat dateFormatter
		= new SimpleDateFormat ("yyyy-MM-dd");
	private static final SimpleDateFormat dateAndTimeFormatter
		= new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	private static final NumberFormat integerFormatter
		= NumberFormat.getIntegerInstance (LocaleUtil.getSwedishLocale ());

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
				 localize (NUMBER_OF_KEY, NUMBER_OF_DEFAULT),
				 localize (AMOUNT_KEY, AMOUNT_DEFAULT),
				 localize (REMARK_KEY, REMARK_DEFAULT)};
		
		final InvoiceBusiness business = getInvoiceBusiness (context);
		final InvoiceHeader header = getInvoiceHeader (context);        
		final String headerId = header.getPrimaryKey ().toString ();
		final InvoiceRecord [] records
				= business.getInvoiceRecordsByInvoiceHeader (header);
		final MemoryFileBuffer buffer = new MemoryFileBuffer ();
		final String title = localize
				(INVOICE_COMPILATION_KEY,
				 INVOICE_COMPILATION_DEFAULT) + " " + headerId;
		final Document document = createPdfDocument (buffer, title);
		
		// create document table
		final PdfPTable outerTable = new PdfPTable (1);
		outerTable.setWidthPercentage (100f);
		outerTable.getDefaultCell ().setBorder (0);
		addPhrase (outerTable, title);

		// add document header
		addPhrase (outerTable, "\n");
		final User custodian = header.getCustodian ();
		addPhrase (outerTable, localize (CUSTODIAN_KEY, CUSTODIAN_DEFAULT)
							 + ": " + getUserInfo (custodian) + "\n");
		addPhrase (outerTable, localize (PERIOD_KEY, PERIOD_DEFAULT) + ": "
							 + getFormattedPeriod (header.getPeriod ()));
		addPhrase (outerTable, localize (PRINT_DATE_KEY, PRINT_DATE_DEFAULT)
							 + ": " + dateAndTimeFormatter.format (now ()));
		addPhrase (outerTable, "\n");

		// add invoices info to document
		final Color lightBlue = new Color (0xf4f4f4);
		final PdfPTable recordTable = getInvoiceRecordPdfTable
				(columnNames, business, records, lightBlue);
		outerTable.addCell (recordTable);

		// add summary
		addPhrase (outerTable, "\n");
		final PdfPTable summaryTable = getSummaryPdfTable (records);
		outerTable.addCell (summaryTable);

		// add posting info to document
		addPhrase (outerTable, "\n");
		addPhrase (outerTable,
							 localize (OWN_POSTING_KEY, OWN_POSTING_DEFAULT) + ":");
		final PdfPTable postingTable
				= getOwnPostingPdfTable (context, records, lightBlue);
		outerTable.addCell (postingTable);

		// close and store document
		document.add (outerTable);
		document.close ();

		// create link		
		final int docId = business.generatePdf
				(localize (INVOICE_COMPILATION_KEY,
									 INVOICE_COMPILATION_DEFAULT), buffer);
		final Link viewLink
				= new Link("Öppna fakturaunderlaget i Acrobat Reader");
		viewLink.setFile (docId);
		viewLink.setTarget ("letter_window_" + docId);
		final Table table = createTable (1);
		table.add (viewLink, 1, 1);
		table.setHeight (2, 12);
		addCancelButton (table, 1, 3, ACTION_SHOW_COMPILATION);
		table.add (new HiddenInput (INVOICE_COMPILATION_KEY, headerId), 1, 3);
		createForm (context, table, INVOICE_COMPILATION_KEY,
								INVOICE_COMPILATION_DEFAULT);
	}
	
	private Document createPdfDocument
		(final MemoryFileBuffer buffer, final String title)
		throws DocumentException {
		final Document document = new Document
				(PageSize.A4, mmToPoints (20), mmToPoints (20),
				 mmToPoints (20), mmToPoints (20));
		final OutputStream outStream = new MemoryOutputStream (buffer);
		final PdfWriter writer = PdfWriter.getInstance (document, outStream);
		writer.setViewerPreferences
				(PdfWriter.HideMenubar | PdfWriter.PageLayoutOneColumn
				 | PdfWriter.PageModeUseNone | PdfWriter.FitWindow
				 | PdfWriter.CenterWindow);
		document.addTitle (title);
		document.addCreationDate ();
		document.open ();
		return document;
	}

	private PdfPTable getSummaryPdfTable (final InvoiceRecord[] records) {
		final PdfPTable summaryTable = new PdfPTable (3);
		summaryTable.setWidthPercentage (100f);
		summaryTable.getDefaultCell ().setBorder (0);
		addPhrase (summaryTable,
							 localize (TOTAL_AMOUNT_VAT_EXCLUSIVE_KEY,
												 TOTAL_AMOUNT_VAT_EXCLUSIVE_DEFAULT) + ':');
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (summaryTable, getTotalAmount (records));
		addPhrase (summaryTable, ""); // add empty cell
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		addPhrase (summaryTable, localize (TOTAL_AMOUNT_VAT_KEY,
																			 TOTAL_AMOUNT_VAT_DEFAULT) + ':');
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (summaryTable, getTotalAmountVat (records));
		addPhrase (summaryTable, ""); // add empty cell
		return summaryTable;
	}

	private void newRecord (final IWContext context)
		throws RemoteException, CreateException, FinderException {
		final User currentUser = context.getCurrentUser ();
		final Integer amount = getIntegerParameter (context, AMOUNT_KEY);
		final Date checkEndPeriod = getDateParameter (context,
																									CHECK_END_PERIOD_KEY);
		final Date checkStartPeriod
				= getDateParameter (context, CHECK_START_PERIOD_KEY);
		final int dayDiff = null != checkStartPeriod && null != checkEndPeriod
				? 1 + AccountingUtil.getDayDiff (checkStartPeriod, checkEndPeriod) : 0;
		final Integer numberOfDays = new Integer (0 > dayDiff ? 0 : dayDiff);
		final String doublePosting = getPostingString (context,
																									 DOUBLE_POSTING_KEY);
		final InvoiceHeader header = getInvoiceHeader (context);
		final String invoiceText = context.getParameter (INVOICE_TEXT_KEY);
		final String invoiceText2 = context.getParameter (INVOICE_TEXT2_KEY);
		final String note = context.getParameter (NOTE_KEY);
		final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
		final Date placementEndPeriod
				= getDateParameter (context, PLACEMENT_END_PERIOD_KEY);
		final Date placementStartPeriod
				= getDateParameter (context, PLACEMENT_START_PERIOD_KEY);
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
		final Integer pieceAmount
				= getIntegerParameter (context, PIECE_AMOUNT_KEY);
		final Integer orderId
				= getIntegerParameter (context, ORDER_ID_KEY);
		final String ownPaymentPosting
				= context.getParameter (OWN_PAYMENT_POSTING_KEY);
		final String doublePaymentPosting
				= context.getParameter (DOUBLE_PAYMENT_POSTING_KEY);
		final InvoiceBusiness business = getInvoiceBusiness (context);
		business.createInvoiceRecord
				(currentUser,
				 header,
				 placementId,
				 providerId,
				 ruleText,
				 invoiceText,
				 invoiceText2,
				 note,
				 placementStartPeriod,
				 placementEndPeriod,
				 checkStartPeriod,
				 checkEndPeriod,
				 amount,
				 vatAmount,
				 numberOfDays,
				 regulationSpecTypeId,
				 vatRule,
				 ownPosting,
				 doublePosting,
				 pieceAmount,
				 ownPaymentPosting,
				 doublePaymentPosting,
				 orderId);

		if (null == ownPaymentPosting) {
			final Table table = createTable (1);
			table.add (getRedText (localize (NO_PAYMENT_POSTINGS_FOUND_KEY,
																			 NO_PAYMENT_POSTINGS_FOUND_DEFAULT)),
								 1, 1);
			add (table);
		}
		showCompilation (context);
	}
	
	private void saveRecord (final IWContext context)
		throws RemoteException, FinderException {
		// get updated values
		final Integer amount = getIntegerParameter (context, AMOUNT_KEY);
		final Date checkEndPeriod = getDateParameter (context,
																									CHECK_END_PERIOD_KEY);
		final Date checkStartPeriod
				= getDateParameter (context, CHECK_START_PERIOD_KEY);
		final String doublePosting = getPostingString (context,
																									 DOUBLE_POSTING_KEY);
		final String invoiceText = context.getParameter (INVOICE_TEXT_KEY);
		final String invoiceText2 = context.getParameter (INVOICE_TEXT2_KEY);
		final String note = context.getParameter (NOTE_KEY);
		final String ownPosting = getPostingString (context, OWN_POSTING_KEY);
		final Date placementEndPeriod
				= getDateParameter (context, PLACEMENT_END_PERIOD_KEY);
		final Date placementStartPeriod
				= getDateParameter (context, PLACEMENT_START_PERIOD_KEY);
		Integer providerId = getIntegerParameter (context, PROVIDER_KEY);
		final Integer placementId = getIntegerParameter (context,
																										 PLACEMENT_KEY);
		final Integer regSpecTypeId
				= getIntegerParameter (context, REGULATION_SPEC_TYPE_KEY);
		final Integer vatAmount = getIntegerParameter (context, VAT_AMOUNT_KEY);
		final Integer vatRule = getIntegerParameter (context, VAT_RULE_KEY);
		final String ruleText = context.getParameter (RULE_TEXT_KEY);
		final Integer recordId = getIntegerParameter (context, INVOICE_RECORD_KEY);
		final User currentUser = context.getCurrentUser ();		

		getInvoiceBusiness (context).saveInvoiceRecord
				(recordId, currentUser, placementId, providerId, invoiceText,
				 invoiceText2, ruleText, note, checkEndPeriod, checkStartPeriod,
				 placementStartPeriod, placementEndPeriod, ownPosting, doublePosting,
				 amount, vatAmount, vatRule, regSpecTypeId);
		
		//render
		showCompilation (context);
	}
	
	private void showNewRecordForm (final IWContext context)
		throws RemoteException, FinderException, IDOLookupException {
		final java.util.Map inputs = new java.util.HashMap ();
		final CalendarMonth calendarMonth = new CalendarMonth (now ());
		final String lastDateOfMonth = getFormattedDate
				(calendarMonth.getLastDateOfMonth ());
		final String firstDateOfMonth = getFormattedDate
				(calendarMonth.getFirstDateOfMonth ());
		final InvoiceHeader header = getInvoiceHeader (context);
		final User custodian = header.getCustodian ();
		final InvoiceBusiness business = getInvoiceBusiness (context);
		final String searchString = context.getParameter (RULE_TEXT_KEY);
		final Date period = header.getPeriod ();
		final String categoryId =  header.getSchoolCategoryID ();
		inputs.put (PLACEMENT_KEY, getPlacementsDropdown (context, header));
		final SchoolClassMember placement = (SchoolClassMember)
				context.getSessionAttribute (PLACEMENT_KEY);
		final Provider provider
				= (null != placement ? new Provider
					 (placement.getSchoolClass ().getSchool ()) : null); 
		Regulation matchedRegulation  = null;
		if (null != searchString && null != period && null != categoryId
				&& null != provider && getActionId (context)
				== ACTION_SHOW_NEW_RECORD_FORM_AND_SEARCH_RULE_TEXT) {
			// the form is correctly filled - do search for regulation
			matchedRegulation = findRegulation
					(context, inputs, header, business, searchString, period, categoryId,
					 placement, provider);
		} 
		if (null == matchedRegulation) {
			addSearchRegulationForm(context, inputs, business, searchString);
		}
		inputs.put (INVOICE_RECEIVER_KEY, getSmallText (getUserInfo
																										(custodian)));
		inputs.put (CHECK_START_PERIOD_KEY, getStyledInput
								(CHECK_START_PERIOD_KEY, firstDateOfMonth));
		inputs.put (CHECK_END_PERIOD_KEY, getStyledInput
								(CHECK_END_PERIOD_KEY, lastDateOfMonth));
		inputs.put (PLACEMENT_START_PERIOD_KEY, getStyledInput
								(PLACEMENT_START_PERIOD_KEY));
		inputs.put (PLACEMENT_END_PERIOD_KEY, getStyledInput
								(PLACEMENT_END_PERIOD_KEY));
		inputs.put (NOTE_KEY, getStyledWideInput (NOTE_KEY));
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

	private void addSearchRegulationForm(final IWContext context, final java.util.Map inputs, final InvoiceBusiness business, final String searchString) throws RemoteException {
		// found 0 or more than 1 regulation, present search form
		inputs.put (RULE_TEXT_KEY, getStyledWideInput (RULE_TEXT_KEY,
																									 searchString));
		inputs.put (INVOICE_TEXT_KEY, getStyledWideInput
								(INVOICE_TEXT_KEY));
		inputs.put (INVOICE_TEXT2_KEY, getStyledWideInput
								(INVOICE_TEXT2_KEY));
		inputs.put (AMOUNT_KEY, getStyledInput (AMOUNT_KEY));
		inputs.put (VAT_AMOUNT_KEY, getStyledInput (VAT_AMOUNT_KEY));
		inputs.put (REGULATION_SPEC_TYPE_KEY, getLocalizedDropdown
								(business.getAllRegulationSpecTypes ()));
		inputs.put (VAT_RULE_KEY,  getLocalizedDropdown
								(business.getAllVATRuleRegulations()));
		inputs.put (OWN_POSTING_KEY, getPostingParameterForm
								(context, OWN_POSTING_KEY));
		inputs.put (DOUBLE_POSTING_KEY, getPostingParameterForm
								(context, DOUBLE_POSTING_KEY));
	}

	private Regulation findRegulation
		(final IWContext context, final java.util.Map inputs,
		 final InvoiceHeader header, final InvoiceBusiness business,
		 final String searchString, final Date period, final String categoryId,
		 final SchoolClassMember placement, final Provider provider)
		throws IDOLookupException, RemoteException {
		final Collection regulations = new ArrayList ();
		Regulation matchedRegulation  = null;
		final RegulationHome home = getRegulationHome ();
		try {
			regulations.addAll
					(home.findRegulationsByNameNoCaseDateAndCategory
					 (searchString + '%', period, categoryId));
			if (1 == regulations.size ()) {
				matchedRegulation = (Regulation) regulations.iterator ().next ();
			} else {
				for (Iterator i = regulations.iterator ();
						 i.hasNext () && null == matchedRegulation;) {
					final Regulation regulation = (Regulation) i.next ();
					if (regulation.getName ().trim ().equalsIgnoreCase
							(searchString.trim ())) {
						matchedRegulation = regulation;
					}
				}
			}
			if (null != matchedRegulation) {
				// found exactly one regulation, display it
				addPresentationObjectsForNewRecordForm
						(context, inputs, header, business, provider, matchedRegulation,
						 placement);
			} else {
				// regulations.size > 1
				addRegulationLinkListForNewRecordForm (context, inputs,
																							 regulations);
			}
		} catch (FinderException e) {
			// take care of 'regulations.isEmpty' below since FinderException isn't
			// allways thrown
		}
		if (regulations.isEmpty ()) {
			// no regulations found from search
			final String message = localize
					(NO_RULE_FOUND_STARTING_WITH_KEY,
					 NO_RULE_FOUND_STARTING_WITH_DEFAULT) + " '" + searchString + "'.";
			inputs.put (RULE_TEXT_LINK_LIST_KEY, getRedText (message));
		}
		return matchedRegulation;
	}

	private Text getRedText (final String string) {
		final Text text = new Text (string);
		text.setFontColor ("#ff0000");
		return text;
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
		inputs.put (INVOICE_TEXT2_KEY, getStyledWideInput
								(INVOICE_TEXT2_KEY, record.getInvoiceText2 ()));
		inputs.put (CHECK_START_PERIOD_KEY, getStyledInput
								(CHECK_START_PERIOD_KEY, getFormattedDate
								 (record.getPeriodStartCheck ())));
		inputs.put (CHECK_END_PERIOD_KEY, getStyledInput
								(CHECK_END_PERIOD_KEY, getFormattedDate
								 (record.getPeriodEndCheck ())));
		inputs.put (PLACEMENT_START_PERIOD_KEY, getStyledInput
								(PLACEMENT_START_PERIOD_KEY, getFormattedDate
								 (record.getPeriodStartPlacement ())));
		inputs.put (PLACEMENT_END_PERIOD_KEY, getStyledInput
								(PLACEMENT_END_PERIOD_KEY, getFormattedDate
								 (record.getPeriodEndPlacement ())));
		inputs.put (DATE_CREATED_KEY, getSmallText
								(dateFormatter.format (record.getDateCreated ())));
		inputs.put (CREATED_SIGNATURE_KEY, getSmallSignature
								(record.getCreatedBy ()));
		addSmallDateText (inputs, DATE_ADJUSTED_KEY, record.getDateChanged ());
		inputs.put (ADJUSTED_SIGNATURE_KEY,
								getSmallSignature (record.getChangedBy ()));
		inputs.put (AMOUNT_KEY, getStyledInput
								(AMOUNT_KEY, roundAmount (record.getAmount ()) + ""));
		inputs.put (VAT_AMOUNT_KEY, getStyledInput
								(VAT_AMOUNT_KEY, roundAmount (record.getAmountVAT ()) + ""));
		inputs.put (NOTE_KEY, getStyledWideInput (NOTE_KEY,
																							record.getNotes ()));
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
				(business.getAllVATRuleRegulations());
		inputs.put (VAT_RULE_KEY, vatRuleDropdown);
		final int vatRuleId = record.getVATRuleRegulationId();
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
		addSmallText (details, AMOUNT_KEY, getFormattedAmount (record.getAmount ()));
		addSmallText (details, VAT_AMOUNT_KEY,
									getFormattedAmount (record.getAmountVAT ()));
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
		addSmallText (details, INVOICE_TEXT2_KEY, record.getInvoiceText2 ());
		addSmallText (details, NOTE_KEY, record.getNotes ());
		details.put (OWN_POSTING_KEY,
								 getPostingListTable (context, record.getOwnPosting ()));
		addSmallDateText (details, CHECK_START_PERIOD_KEY,
											record.getPeriodStartCheck ());
		addSmallDateText (details, CHECK_END_PERIOD_KEY,
											record.getPeriodEndCheck ());
		addSmallDateText (details, PLACEMENT_START_PERIOD_KEY,
											record.getPeriodStartPlacement ());
		addSmallDateText (details, PLACEMENT_END_PERIOD_KEY,
											record.getPeriodEndPlacement ());
		final RegulationSpecType regSpecType = record.getRegSpecType ();
		if (null != regSpecType) {
			final String typeName = regSpecType.getRegSpecType ();
			addSmallText (details, REGULATION_SPEC_TYPE_KEY,
										localize (typeName, typeName));
		}
		if (0 < record.getVATRuleRegulationId()) {
			final Regulation vatRuleRegulation = business.getVATRuleRegulation(record.getVATRuleRegulationId());
			final String ruleName = vatRuleRegulation.getName ();
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
	private void showCompilationList (final IWContext context)
		throws RemoteException {
		final UserSearcher searcher = createSearcher ();
		final Table table = createTable (6);
		setColumnWidthsEqual (table);
		int row = 2; // first row is reserved for setting column widths
		addOperationalFieldDropdown (context, table, row++);
		addUserSearcherForm (table, row++, context, searcher);
		table.mergeCells (2, row, table.getColumns () - 1, row);
		addPeriodForm (table, row, context);
		final int colCount = table.getColumns ();
		table.setAlignment (colCount, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (getSubmitButton (ACTION_SHOW_COMPILATION_LIST,
																SEARCH_KEY, SEARCH_DEFAULT), colCount, row);
		table.add (Text.getNonBrakingSpace (), colCount, row);
		table.add (getSubmitButton (ACTION_SHOW_COMPILATION_LIST_AND_CLEAR_FORM,
																CLEAR_KEY, CLEAR_DEFAULT), colCount, row++);
		
		if (null != searcher.getUser ()) {
			// exactly one user found - display users invoice compilation list
			final String operationalField
					= getSession ().getOperationalField ();
			final User userFound = searcher.getUser ();
			final CalendarMonth fromPeriod = getCalendarMonthParameter
					(context, START_PERIOD_KEY);
			final CalendarMonth toPeriod = getCalendarMonthParameter
					(context, END_PERIOD_KEY);
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
			// there are mote than one user found - display name list
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
		table.add (getSmallSignature (header.getCreatedBy ()), col++, row);
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
		table.add (new HiddenInput (INVOICE_COMPILATION_KEY,
																"" + header.getPrimaryKey ()), 1, row);
		table.setHeight (row++, 12);
		table.mergeCells (1, row, table.getColumns (), row);
		table.add (getSubmitButton (ACTION_SHOW_NEW_RECORD_FORM, NEW_KEY,
																NEW_DEFAULT), 1, row);
		addCancelButton (table, 1, row, ACTION_SHOW_COMPILATION_LIST);
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
			final String headerId = record.getInvoiceHeaderId () + "";
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
		 final Provider provider, final Regulation regulation,
		 final SchoolClassMember placement)
		throws EJBException, RemoteException {
		final String regulationName = regulation.getName ();
		final RegulationSpecType regSpecType = regulation.getRegSpecType ();
		final Integer regSpecTypeId
				= (Integer) regSpecType.getPrimaryKey ();
		final Regulation vatRuleRegulation = regulation.getVATRuleRegulation();
		final SchoolCategory category = header.getSchoolCategory ();
		final RegulationsBusiness regulationsBusiness
				= getRegulationsBusiness (context);
		final SchoolType schoolType
				= regulationsBusiness.getSchoolType (regulation);
		final PostingBusiness postingBusiness = getPostingBusiness (context);
		inputs.put (RULE_TEXT_KEY, getStyledWideInput (RULE_TEXT_KEY,regulationName));
		final StringBuffer invoiceText1 = new StringBuffer ();
		final StringBuffer invoiceText2 = new StringBuffer ();
		fillInvoiceTextBuffers(invoiceText1, invoiceText2, header, provider,
													 placement, regulationName, regSpecType);
		inputs.put (INVOICE_TEXT_KEY, getStyledWideInput
								(INVOICE_TEXT_KEY,invoiceText1 + ""));
		inputs.put (INVOICE_TEXT2_KEY, getStyledWideInput
								(INVOICE_TEXT2_KEY,invoiceText2 + ""));
		inputs.put (PIECE_AMOUNT_KEY, new HiddenInput
								(PIECE_AMOUNT_KEY, regulation.getAmount () + ""));
		inputs.put (ORDER_ID_KEY, new HiddenInput
								(ORDER_ID_KEY, regulation.getConditionOrder () + ""));
		inputs.put (AMOUNT_KEY, getStyledInput
		            (AMOUNT_KEY, regulation.getAmount () + ""));
		inputs.put (VAT_AMOUNT_KEY, getStyledInput (VAT_AMOUNT_KEY));
		inputs.put (REGULATION_SPEC_TYPE_KEY, getLocalizedDropdown
		            (business.getAllRegulationSpecTypes (), regSpecType));
		inputs.put (VAT_RULE_KEY,  getLocalizedDropdown
		            (business.getAllVATRuleRegulations(), vatRuleRegulation));
		String [] paymentPostings = null;
		try {
			paymentPostings = postingBusiness.getPostingStrings
					(category, schoolType, regSpecTypeId.intValue (), provider,
					 header.getPeriod ());
			inputs.put (OWN_PAYMENT_POSTING_KEY, new HiddenInput
									(OWN_PAYMENT_POSTING_KEY, paymentPostings [0]));
			inputs.put (DOUBLE_PAYMENT_POSTING_KEY, new HiddenInput
									(DOUBLE_PAYMENT_POSTING_KEY, paymentPostings [1]));
		} catch (PostingException e) {
			inputs.put (NO_PAYMENT_POSTINGS_FOUND_KEY, getRedText
									(localize (NO_PAYMENT_POSTINGS_FOUND_KEY,
														 NO_PAYMENT_POSTINGS_FOUND_DEFAULT)));
			e.printStackTrace ();
		}
		final String regSpecTypeName = regSpecType.getRegSpecType ();
		addPostingsToNewRecordForm
				(context, inputs, header, provider, category, regulationsBusiness,
				 schoolType, postingBusiness, paymentPostings, regSpecTypeName);
	}
	
	private void addPostingsToNewRecordForm
		(final IWContext context, final java.util.Map inputs,
		 final InvoiceHeader header, final Provider provider,
		 final SchoolCategory category,
		 final RegulationsBusiness regulationsBusiness,
		 final SchoolType schoolType, final PostingBusiness postingBusiness,
		 String[] paymentPostings, final String regSpecTypeName)
		throws RemoteException {
		String [] invoicePostings = paymentPostings;
		try {
			if (isCheck (regSpecTypeName)) {
				final RegulationSpecType invoiceRegSpecType
						= regulationsBusiness.findRegulationSpecType
						(RegSpecConstant.CHECKTAXA);
				final int invoiceRegSpecTypeId
						= ((Integer) invoiceRegSpecType.getPrimaryKey ()).intValue ();
				invoicePostings = postingBusiness.getPostingStrings
						(category, schoolType, invoiceRegSpecTypeId, provider,
						 header.getPeriod ());
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		if (null != invoicePostings) {
			final PresentationObject ownPostingForm = getPostingParameterForm
					(context, OWN_POSTING_KEY, invoicePostings [0]);
			inputs.put (OWN_POSTING_KEY, ownPostingForm);
			final PresentationObject doublePostingForm = getPostingParameterForm
					(context, DOUBLE_POSTING_KEY, invoicePostings [1]);
			inputs.put (DOUBLE_POSTING_KEY, doublePostingForm);
		}	else {
			inputs.put (OWN_POSTING_KEY, getPostingParameterForm
									(context, OWN_POSTING_KEY));
			inputs.put (DOUBLE_POSTING_KEY, getPostingParameterForm
									(context, DOUBLE_POSTING_KEY));
		}
	}
	
	private boolean isCheck (final RegulationSpecType regSpecType) {
		try {
				final MainRule mainRule = regSpecType.getMainRule ();
				final String mainRuleName = mainRule.getMainRule ();
				return mainRuleName.equals (RegSpecConstant.MAIN_RULE_CHECK);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isCheck (final String regSpecTypeName) {
		try {
			final RegulationSpecTypeHome regSpecTypeHome =
					(RegulationSpecTypeHome) IDOLookup.getHome (RegulationSpecType.class);
			final	RegulationSpecType regSpecType
					= regSpecTypeHome.findByRegulationSpecType (regSpecTypeName);
			return isCheck (regSpecType);
		} catch (Exception e) {
			return false;
		}
	}

	private void fillInvoiceTextBuffers
		(final StringBuffer invoiceText1, final StringBuffer invoiceText2,
		 final InvoiceHeader header, final Provider provider,
		 final SchoolClassMember placement, final String regulationName,
		 final RegulationSpecType regSpecType) {
		if (isCheck (regSpecType)) {
			try {
				final User student = null != placement ? placement.getStudent () : null;
				final String studentName = null != student ? student.getFirstName ()
						: "?";
				final ChildCareContract contract
						= getChildCareContractHome ().findBySchoolClassMember (placement);
				final CalendarMonth period = new CalendarMonth (header.getPeriod ());
				final IWTimestamp firstPlacementDate
						= new IWTimestamp (placement.getRegisterDate ());
				final IWTimestamp firstCheckDate
						= period.beginsBefore (firstPlacementDate)
						? firstPlacementDate : period.getFirstTimestamp();
				final IWTimestamp lastPlacementDate
						= null == placement.getRemovedDate ()
						? null : new IWTimestamp (placement.getRemovedDate ());
				final IWTimestamp lastCheckDate = null != lastPlacementDate
						&& !period.endsBefore (lastPlacementDate)
						? lastPlacementDate : period.getLastTimestamp ();
				firstCheckDate.setAsDate ();
				lastCheckDate.setAsDate ();
				final int days = lastCheckDate.isEarlierThan (firstCheckDate) ? 0
						: 1 + AccountingUtil.getDayDiff (firstCheckDate, lastCheckDate);
				invoiceText1.append ("Check ");
				invoiceText1.append (provider.getSchool ().getName ());
				invoiceText2.append (studentName);
				invoiceText2.append (", " + contract.getCareTime() + "t/v, ");
				invoiceText2.append (days + " dagar");
			} catch (Exception e) {
				e.printStackTrace ();
				// no problem, use method below ro fill invoice text
			}
		}
		if (0 == invoiceText2.length ()) {
			invoiceText1.append (regulationName);
		}
	}
	
	private String getUserInfo (final User user) {
		return user == null ? "" : getUserName (user) + " (" + formatSsn
				(user.getPersonalID ()) + "), " + getAddressString (user);
	}
	
	private boolean isCustodian (final IWContext context, final User user)
		throws RemoteException {
		final FamilyLogic familyBusiness = getMemberFamilyLogic (context);
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
				final FamilyLogic familyBusiness
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
		final Calendar birthday18 = Calendar.getInstance ();
		birthday18.set (year + 18, month - 1, day);
		return now ().after (birthday18.getTime());
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
																SEARCH_INVOICE_RECEIVER_DEFAULT), 1, row);
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
			table.add (getStyledInput (PERIOD_KEY, periodFormatter.format
																 (now ())), col++, row++);
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
		addCancelButton (table, 1, row, ACTION_SHOW_COMPILATION_LIST);
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
		final InvoiceBusiness business = getInvoiceBusiness (context);
		final InvoiceHeader header = business.createInvoiceHeader
				(operationalField, currentUser, custodianId, period);
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
		col = 2; row++;
		table.mergeCells (col, row, table.getColumns (), row);
		addPresentation (table, presentationObjects, INVOICE_TEXT2_KEY, col++,
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
		addPresentation (table, presentationObjects, PIECE_AMOUNT_KEY, col,
										 row);
		addPresentation (table, presentationObjects, ORDER_ID_KEY, col,
										 row);
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
		if (presentationObjects.containsKey (NO_PAYMENT_POSTINGS_FOUND_KEY)) {
			table.mergeCells (col, row, table.getColumns (), row);
			addPresentation (table, presentationObjects,
											 NO_PAYMENT_POSTINGS_FOUND_KEY, 1, row);
			col = 1; row++;
		}
		addSmallHeader (table, col++, row, OWN_POSTING_KEY, OWN_POSTING_DEFAULT,
										":");
		col = 1; row++;
		table.mergeCells (col, row, table.getColumns (), row);
		addPresentation (table, presentationObjects, OWN_POSTING_KEY, col, row);
		addPresentation (table, presentationObjects, OWN_PAYMENT_POSTING_KEY,
										 col, row);
		col = 1; row++;
		addSmallHeader (table, col++, row, DOUBLE_POSTING_KEY,
										DOUBLE_POSTING_DEFAULT, ":");
		col = 1; row++;
		table.mergeCells (col, row, table.getColumns (), row);
		addPresentation (table, presentationObjects, DOUBLE_PAYMENT_POSTING_KEY,
										 col, row);
		addPresentation (table, presentationObjects, DOUBLE_POSTING_KEY, col,
										 row);
		col = 1; row++;
		table.setHeight (row++, 12);
		table.mergeCells (col, row, table.getColumns (), row);
		addPresentation (table, presentationObjects, ACTION_KEY, 1, row);
		addCancelButton (table, 1, row, ACTION_SHOW_COMPILATION);
		createForm (context, table,
								presentationObjects.get (HEADER_KEY).toString ());
	}
	
	private void addCancelButton (final Table table, final int col,
																final int row, final int actionId) {
		table.add (Text.getNonBrakingSpace(), col, row);
		table.add (getSubmitButton (actionId,	CANCEL_KEY, CANCEL_DEFAULT), col,
							 row);
	}
	
	private PdfPTable getOwnPostingPdfTable
		(final IWContext context, final InvoiceRecord [] records,
		 final Color lightBlue) throws RemoteException {
		final PostingField [] fields = getCurrentPostingFields (context);
		final PdfPTable table = new PdfPTable (fields.length + 1);
		table.setWidthPercentage (100f);
		table.getDefaultCell ().setBackgroundColor (new Color (0xd0daea));
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_CENTER);
		for (int i = 0; i < fields.length; i++) {
			addPhrase (table, fields [i].getFieldTitle ());
		}
		addPhrase (table, localize (AMOUNT_KEY, AMOUNT_DEFAULT));
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		for (int i = 0; i < records.length; i++) {
			final InvoiceRecord record = records [i];
			final String postingString = record.getOwnPosting ();
			table.getDefaultCell ().setBackgroundColor (i % 2 == 0 ? Color.white
																									: lightBlue);
			int offset = 0;
			for (int j = 0; j < fields.length; j++) {
				final PostingField field = fields [j];
				final StringBuffer value = new StringBuffer ();
				if (null != field && null != postingString) {
					final int endPosition = min (offset + field.getLen (),
																			 postingString.length ());
					value.append (postingString.substring
												(offset, endPosition).trim ());
					offset = endPosition;
				}
				addPhrase (table, value.toString ());
			}
			addPhrase (table, getFormattedAmount (record.getAmount ()));
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
		final String invoiceText1 = record.getInvoiceText ();
		final String invoiceText2 = record.getInvoiceText2 ();
		final StringBuffer invoiceText = new StringBuffer ();
		if (null != invoiceText1 && 0 < invoiceText1.trim ().length ()) {
			invoiceText.append (invoiceText1.trim ());
		}
		if (null != invoiceText2 && 0 < invoiceText2.trim ().length ()) {
			invoiceText.append ('\n' + invoiceText2.trim ());
		}
		addPhrase (table, invoiceText.toString ());
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (table, record.getDays () + "");
		addPhrase (table, getFormattedAmount (record.getAmount ()));
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		addPhrase (table, record.getNotes ());
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
			headerId = new Integer (record.getInvoiceHeaderId ());
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
	
	private long roundAmount (final float f) {
		return AccountingUtil.roundAmount (f);
	}
	
	private String getFormattedAmount (final float f) {
		return f == -1.0f ? "0" : integerFormatter.format (roundAmount (f));
	}
	
	private Table getSearcherResultTable (final Collection users, int actionId) {
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
		final String totalAmount = getTotalAmount (header, business);
		table.add (status + "", col++, row);
		table.add (periodLink, col++, row);
		table.add (getUserName (custodian), col++, row);
		table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (totalAmount, col++, row);
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
		table.add (getTotalAmount (records), 5, row++);
		table.mergeCells (1, row, 4, row);
		addSmallHeader (table, 1, row, TOTAL_AMOUNT_VAT_KEY,
										TOTAL_AMOUNT_VAT_DEFAULT, ":");
		table.setAlignment (5, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (getTotalAmountVat (records), 5, row++);
		
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
		final boolean isPreliminaryRecord = isPreliminaryRecord (record);
		final String [][] editLinkParameters = getRecordLinkParameters
				(isPreliminaryRecord ? ACTION_SHOW_EDIT_RECORD_FORM
				 : ACTION_SHOW_RECORD_DETAILS, recordId);
		final Link textLink = createSmallLink (record.getInvoiceText (),
																					 editLinkParameters);
		table.add (textLink, col++, row);
		table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (getSmallText (record.getDays () + ""), col++, row);
		table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add (getSmallText (getFormattedAmount (record.getAmount ())), col++,
							 row);
		addSmallText (table, record.getNotes (), col++, row);
		final Link editLink = createIconLink (getEditIcon (),
																					editLinkParameters);
		table.add (editLink, col++, row);
		if (isPreliminaryRecord) {
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
			final StringBuffer title = new StringBuffer ();
			final StringBuffer value = new StringBuffer ();
			final PostingField field = fields [i];
			if (null != field) {
				title.append (field.getFieldTitle ());
				if (null != postingString) {
					final int endPosition = min (offset + field.getLen (),
																			 postingString.length ());
					value.append (postingString.substring
												(offset, endPosition).trim ());
					offset = endPosition;
				}
			}
			result.setHeader (title.toString (), i + 1);
			result.add (getSmallText (value.toString ()));
		}       
		return result;
	}
	
	private int min (final int a, final int b) {
		return a < b ? a : b;
	}
	
	private PostingField [] getCurrentPostingFields (final IWContext context)
		throws RemoteException {
		final PostingBusiness business = getPostingBusiness (context);
		final Collection fields = business.getAllPostingFieldsByDate (now ());
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
	
	private static boolean isPreliminaryRecord (final InvoiceRecord record) {
		try {
			final InvoiceHeader header = record.getInvoiceHeader ();
			return header.getStatus () == ConstantStatus.PRELIMINARY;
		} catch (Exception e) {
			e.printStackTrace ();
			return false;
		}
		/*
		final String autoSignature = BillingThread.getBatchRunSignatureKey ();
		final String createdBy = record.getCreatedBy ();
		return null == createdBy || !createdBy.equals (autoSignature);
		*/
	}
	
	private Text getSmallSignature (final String string) {
		final StringBuffer result = new StringBuffer ();
		final String autoSignature = BillingThread.getBatchRunSignatureKey ();
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
		String ssn = "";
		String firstName = "";
		String lastName = "";
		if (ACTION_SHOW_COMPILATION_LIST_AND_CLEAR_FORM
				!= getActionId (context)) {
			try {
				searcher.process (context);
			} catch (final FinderException dummy) {
				// do nothing, it's ok that none was found
			}
			final User user = searcher.getUser ();
			if (null != user) {
				ssn = user.getPersonalID ();
				firstName = user.getFirstName ();
				lastName = user.getLastName ();
			} else {
				ssn = getParameterFromFormOrSession (context,
																						 USERSEARCHER_PERSONALID_KEY);
				firstName = getParameterFromFormOrSession (context,
																									 USERSEARCHER_FIRSTNAME_KEY);
				lastName = getParameterFromFormOrSession (context,
																									USERSEARCHER_LASTNAME_KEY);
			}
		}
		context.setSessionAttribute (USERSEARCHER_PERSONALID_KEY, ssn);
		context.setSessionAttribute (USERSEARCHER_FIRSTNAME_KEY, firstName);
		context.setSessionAttribute (USERSEARCHER_LASTNAME_KEY, lastName);
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
	
	private String getParameterFromFormOrSession (final IWContext context,
																								final String key) {
		String result = "";
		if (null != key) {
			final String sessionAttribute
					= (String) context.getSessionAttribute (key);
			if (context.isParameterSet (key)) result = context.getParameter (key);
			else if (null != sessionAttribute) result = sessionAttribute;
		}
		return result;
	}

	private void addPeriodForm (final Table table, final int row,
															final IWContext context) {
		int col = 1;
		addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
		final Date now = now ();
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
	
	private String getFormattedDate (final Date date) {
		return date == null ? "" : dateFormatter.format (date);
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
	
	private static CalendarMonth getCalendarMonthParameter
		(final IWContext context,	final String key) {
		final Date date = getPeriodParameter (context, key);
		final long dateAsLong = date != null ? date.getTime ()
				: System.currentTimeMillis ();
		return new CalendarMonth (new Date (dateAsLong));
	}
	
	private static Date getDateParameter (final IWContext iwc, final String key) {
		String rawInput = iwc.getParameter(key);
		if (null == rawInput) return null;
		final StringBuffer digitOnlyInput = new StringBuffer();
		for (int i = 0; i < rawInput.length(); i++) {
			char number = rawInput.charAt(i);
			if (Character.isDigit(number)) {
				digitOnlyInput.append(number);
			}
		}
		rawInput = digitOnlyInput.toString();
		final Calendar rightNow = Calendar.getInstance();
		final int currentYear = rightNow.get(Calendar.YEAR);
		if (rawInput.length() == 10) {
			final int inputYear = new Integer(rawInput.substring(0, 2)).intValue();
			final int century = inputYear + 2000 > currentYear ? 19 : 20;
			rawInput = century + rawInput;
		}
		if (rawInput.length() != 8)	return null;
		final int year = new Integer(rawInput.substring(0, 4)).intValue();
		final int month = new Integer(rawInput.substring(4, 6)).intValue();
		final int day = new Integer(rawInput.substring(6, 8)).intValue();
		if (month < 1 || month > 12 || day < 1 || day > 31) return null;
		final Calendar calendar = Calendar.getInstance ();
		calendar.set (year, month - 1, day);
		return new Date (calendar.getTimeInMillis ());
	}
	
	private static Date now () {
		return new Date (System.currentTimeMillis ());
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
		map.put (key, getSmallText (-1 != value ? integerFormatter.format (value)
																: "0"));
	}
	
	private void addSmallDateText (final java.util.Map map, final String key,
																 final Date date) {
		map.put (key, getSmallText (null != date ? dateFormatter.format (date)
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
	
	private String getTotalAmount (final InvoiceHeader header,
																 final InvoiceBusiness business) {
 		final InvoiceRecord[] records;
		try {
			records = business.getInvoiceRecordsByInvoiceHeader(header);
		} catch (RemoteException e) {
			return "0";
		}
		return getTotalAmount (records);
	}
	
	private String getTotalAmount (final InvoiceRecord [] records) {
		long totalAmount = 0;
		for (int i = 0; i < records.length; i++) {
			totalAmount += roundAmount (records [i].getAmount ());
		}
		return getFormattedAmount (totalAmount);
	}
	
	private String getTotalAmountVat (final InvoiceRecord [] records) {
		long totalAmountVat = 0;
		for (int i = 0; i < records.length; i++) {
			totalAmountVat += roundAmount (records [i].getAmountVAT ());
		}
		return getFormattedAmount (totalAmountVat);
	}
	
	private String formatSsn (final String ssn) {
		return null == ssn || 12 != ssn.length () ? ssn
				: ssn.substring (2, 8) + '-' + ssn.substring (8, 12);
	}
	
	private DropdownMenu getLocalizedDropdown (final Collection vatRuleRegulations) {
		return getLocalizedDropdown (vatRuleRegulations, null);
	}
	
	private DropdownMenu getLocalizedDropdown(final Collection vatRuleRegulations, final Regulation defaultRule) {
		final DropdownMenu dropdown = (DropdownMenu)
				getStyledInterface (new DropdownMenu (VAT_RULE_KEY));
		final Object defaultRuleId = null != defaultRule
				? defaultRule.getPrimaryKey () : null;
		for (Iterator iter = vatRuleRegulations.iterator(); iter.hasNext();) {
			final Regulation vatRuleRegulation = (Regulation) iter.next();
			final String ruleName = vatRuleRegulation.getName();
			final Object ruleId = vatRuleRegulation.getPrimaryKey();
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
	
	private static ChildCareContractHome getChildCareContractHome ()
		throws IDOLookupException {
		return (ChildCareContractHome)
				IDOLookup.getHome (ChildCareContract.class);
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
	
	private static FamilyLogic getMemberFamilyLogic
		(final IWContext context) throws RemoteException {
		return (FamilyLogic) IBOLookup.getServiceInstance
				(context, FamilyLogic.class);	
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
