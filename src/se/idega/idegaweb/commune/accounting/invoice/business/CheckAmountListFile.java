package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryOutputStream;
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
import java.awt.Color;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.posting.data.PostingFieldBMPBean;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

/**
 * Last modified: $Date: 2004/01/19 13:44:31 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class CheckAmountListFile {
	public static final String PREFIX = "cacc_ch_am_list_";

	private static final String AMOUNT_DEFAULT = "Belopp";
	private static final String AMOUNT_KEY = PREFIX + "amount";
	private static final String BANKGIRO_DEFAULT = "Bankgiro";
	private static final String BANKGIRO_KEY = PREFIX + "bankgiro";
	private static final String CHECK_AMOUNT_LIST_DEFAULT = "Checkbeloppslista";
	private static final String CHECK_AMOUNT_LIST_KEY = PREFIX + "check_amount_list";
	private static final String DOUBLE_POSTING_DEFAULT = "Motkontering";
	private static final String DOUBLE_POSTING_KEY = PREFIX + "double_posting";
	private static final String MAIN_ACTIVITY_DEFAULT = "Huvudverksamhet";
	private static final String MAIN_ACTIVITY_KEY = PREFIX + "main_activity";
	private static final String NOTE_DEFAULT = "Anmärkning";
	private static final String NOTE_KEY = PREFIX + "note";
	private static final String NUMBER_OF_DEFAULT = "Antal";
	private static final String NUMBER_OF_KEY = PREFIX + "number_of";
	private static final String OWN_POSTING_DEFAULT = "Egen kontering";
	private static final String OWN_POSTING_KEY = PREFIX + "own_posting";
	private static final String PERIOD_DEFAULT = "Period";
	private static final String PERIOD_KEY = PREFIX + "period";
	private static final String PIECE_AMOUNT_2_DEFAULT = "Á-pris";
	private static final String PIECE_AMOUNT_2_KEY = PREFIX + "piece_amount_2";
	private static final String PLACEMENT_DEFAULT = "Placering";
	private static final String PLACEMENT_KEY = PREFIX + "placement";
	private static final String POSTGIRO_DEFAULT = "Postgiro";
	private static final String POSTGIRO_KEY = PREFIX + "postgiro";
	private static final String PRINT_DATE_DEFAULT = "Utskriftsdatum";
	private static final String PRINT_DATE_KEY = PREFIX + "print_date";
	private static final String PROVIDER_DEFAULT = "Anordnare";
	private static final String PROVIDER_KEY = PREFIX + "provider";
	private static final String STATUS_DEFAULT = "Status";
	private static final String STATUS_KEY = PREFIX + "status";
	private static final String TOTAL_AMOUNT_INDIVIDUALS_DEFAULT = "Totalt antal individer";
	private static final String TOTAL_AMOUNT_INDIVIDUALS_KEY = PREFIX + "total_amount_individuals";
	private static final String TOTAL_AMOUNT_PLACEMENTS_DEFAULT = "Totalt antal placeringar";
	private static final String TOTAL_AMOUNT_PLACEMENTS_KEY = PREFIX + "total_amount_placements";
	private static final String TOTAL_AMOUNT_VAT_DEFAULT = "Totalbelopp, moms";
	private static final String TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT = "Totalbelopp, exklusive moms";
	private static final String TOTAL_AMOUNT_VAT_EXCLUDED_KEY = PREFIX + "total_amount_vat_excluded";
	private static final String TOTAL_AMOUNT_VAT_KEY = PREFIX + "total_amount_vat";

	private final static Font SANSSERIF_FONT
		= FontFactory.getFont (FontFactory.HELVETICA, 9);
	private static final NumberFormat integerFormatter
		= NumberFormat.getIntegerInstance (LocaleUtil.getSwedishLocale ());
	private static final SimpleDateFormat periodFormatter
		= new SimpleDateFormat ("yyMM");
	private static final SimpleDateFormat dateAndTimeFormatter
		= new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	private static final Color LIGHT_BLUE = new Color (0xf4f4f4);
	private final IWResourceBundle bundle;
	private final String schoolCategoryId;
	private final Integer providerId;
	private final Date startPeriod;
	private final Date endPeriod;
	private final int fileId;
	

	public CheckAmountListFile (final IWResourceBundle bundle, final String schoolCategoryId, final Integer providerId, final Date startPeriod, final Date endPeriod, final InvoiceBusiness invoiceBusiness, final PostingBusiness postingBusiness)
		throws RemoteException, FinderException {
		this.bundle = bundle;
		this.schoolCategoryId = schoolCategoryId;
		this.providerId = providerId;
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
		try {
			fileId = createCheckAmountListFile (invoiceBusiness, postingBusiness);
		} catch (DocumentException e) {
			e.printStackTrace ();
			throw new RemoteException (e.getMessage ());
		}
	}

	public int getFileId () {
		return fileId;
	}

	private void addPhrase (final PdfPTable table, final String string) {
		table.addCell (new Phrase (new Chunk (null != string ? string : "",
																					SANSSERIF_FONT)));
	}
	
	private static float mmToPoints (final float mm) {
		return mm*72/25.4f;
	}
	
	private int createCheckAmountListFile (final InvoiceBusiness invoiceBusiness,
																				 final PostingBusiness postingBusiness)
		throws DocumentException, RemoteException, FinderException {
		PaymentRecord [] records = new PaymentRecord [0];
		if (null != schoolCategoryId && null != providerId) {
			records = invoiceBusiness
					.getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
					(schoolCategoryId, providerId, new Date (startPeriod.getTime ()),
					 new Date (endPeriod.getTime ()));
		}
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
				(CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT);
		document.addTitle (title);
		document.addCreationDate ();
		document.open ();
		// add content to document
		final PdfPTable outerTable = new PdfPTable (1);
		outerTable.setWidthPercentage (100f);
		outerTable.getDefaultCell ().setBorder (0);
		addPhrase (outerTable, title + "\n\n");
		final PdfPTable headerTable = getRecordsHeaderPdfTable ();
		outerTable.addCell (headerTable);
		addPhrase (outerTable, "\n");
		final String [][] columnNames =
				{{ STATUS_KEY, STATUS_DEFAULT }, { PERIOD_KEY, PERIOD_DEFAULT },
				 { PLACEMENT_KEY, PLACEMENT_DEFAULT },
				 { NUMBER_OF_KEY, NUMBER_OF_DEFAULT },
				 { PIECE_AMOUNT_2_KEY, PIECE_AMOUNT_2_DEFAULT },
				 { AMOUNT_KEY, AMOUNT_DEFAULT },
				 { NOTE_KEY, NOTE_DEFAULT }};
		final PdfPTable table = new PdfPTable
				(new float [] { 1.0f, 1.0f, 5.0f, 1.0f, 1.2f, 1.4f, 3.0f });
		table.setWidthPercentage (100f);
		table.getDefaultCell ().setBackgroundColor (new Color (0xd0daea));
		for (int i = 0; i < columnNames.length; i++) {
			addPhrase (table, localize (columnNames [i][0],
																	columnNames [i][1]));
		}
		table.setHeaderRows (1);  // this is the end of the table header
		for (int i = 0; i < records.length; i++) {
			table.getDefaultCell ().setBackgroundColor (i % 2 == 0 ? Color.white
																									: LIGHT_BLUE);
			final PaymentRecord record = records [i];
			addRecordOnAPdfRow (table, record);
		}
		outerTable.addCell (table);
		addPhrase (outerTable, "\n");
		final PdfPTable summaryTable = getRecordsSummaryPdfTable(records);
		outerTable.addCell (summaryTable);
		addPhrase (outerTable, "\n");
		addPhrase (outerTable,
							 localize (OWN_POSTING_KEY, OWN_POSTING_DEFAULT) + ":");
		final PdfPTable ownPostingTable	= getPostingPdfTable (records, true,
																													postingBusiness);
		outerTable.addCell (ownPostingTable);
		addPhrase (outerTable, "");
		addPhrase (outerTable,
							 localize (DOUBLE_POSTING_KEY, DOUBLE_POSTING_DEFAULT) + ":");
		final PdfPTable doublePostingTable = getPostingPdfTable (records, false,
																														 postingBusiness);
		outerTable.addCell (doublePostingTable);
		document.add (outerTable);        

		// close and store document
		document.close ();
		final int docId = invoiceBusiness.generatePdf
				(localize (CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT),
				 buffer);
		return docId;
	}

	private PdfPTable getPostingPdfTable
		(final PaymentRecord [] records, final boolean isOwnPosting,
		 final PostingBusiness postingBusiness)
		throws RemoteException {
		final PostingField [] fields = getCurrentPostingFields (postingBusiness);
		final PdfPTable table = new PdfPTable (fields.length + 1);
		table.setWidthPercentage (100f);
		table.getDefaultCell ().setBackgroundColor (new Color (0xd0daea));
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_CENTER);
		for (int i = 0; i < fields.length; i++) {
			addPhrase (table, fields [i].getFieldTitle ());
		}
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (table, localize (AMOUNT_KEY, AMOUNT_DEFAULT));
		for (int i = 0; i < records.length; i++) {
			final PaymentRecord record = records [i];
			final String postingString = isOwnPosting ? record.getOwnPosting ()
					: record.getDoublePosting ();
			table.getDefaultCell ().setBackgroundColor (i % 2 == 0 ? Color.white
																									: LIGHT_BLUE);
			int offset = 0;
			for (int j = 0; j < fields.length; j++) {
				final PostingField field = fields [j];
				if (field.getJustification ()
						== PostingFieldBMPBean.JUSTIFY_RIGHT) {
					table.getDefaultCell ().setHorizontalAlignment
							(Element.ALIGN_RIGHT);
				} else {
					table.getDefaultCell ().setHorizontalAlignment
							(Element.ALIGN_LEFT);
				}
				final int endPosition = min (offset + field.getLen (),
																		 postingString.length ());
				addPhrase (table, postingString.substring
									 (offset, endPosition).trim ());
				offset = endPosition;
			}
			table.getDefaultCell ().setHorizontalAlignment
					(Element.ALIGN_RIGHT);
			addPhrase (table, getFormattedAmount (record.getTotalAmount ()));
		}
		return table;
	}
	
	private PdfPTable getRecordsHeaderPdfTable () {
		final PdfPTable headerTable = new PdfPTable (new float [] { 2.0f, 3.0f });
		headerTable.getDefaultCell ().setBorder (0);
		addPhrase (headerTable,
							 localize (MAIN_ACTIVITY_KEY, MAIN_ACTIVITY_DEFAULT) + ": ");
		addPhrase (headerTable, getSchoolCategoryName (schoolCategoryId));
		addPhrase (headerTable, localize (PERIOD_KEY, PERIOD_DEFAULT) + ": ");
		final String period	= (getFormattedPeriod (startPeriod)) + " - "
				+ (getFormattedPeriod (endPeriod));
		addPhrase (headerTable, period);
		addPhrase (headerTable, localize (PRINT_DATE_KEY, PRINT_DATE_DEFAULT)
							 + ": ");
		addPhrase (headerTable,
							 dateAndTimeFormatter.format (new java.util.Date ()));
		try {
			final SchoolHome schoolHome
					=	(SchoolHome) IDOLookup.getHome (School.class);
			final School school	= schoolHome.findByPrimaryKey (providerId);
			addPhrase (headerTable, localize (PROVIDER_KEY, PROVIDER_DEFAULT)
								 + ": ");
			addPhrase (headerTable, school.getName ());
			final Provider provider = new Provider (school);
			addPhrase (headerTable, localize (BANKGIRO_KEY, BANKGIRO_DEFAULT)
								 + ": ");
			final String bankgiro = provider.getBankgiro ();
			addPhrase (headerTable, bankgiro != null ? bankgiro : "");
			addPhrase (headerTable, localize (POSTGIRO_KEY, POSTGIRO_DEFAULT)
								 + ": ");
			final String postgiro = provider.getPostgiro ();
			addPhrase (headerTable, postgiro != null ? postgiro : "");
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		return headerTable;
	}
	
	private PdfPTable getRecordsSummaryPdfTable
		(PaymentRecord [] records) throws RemoteException,
																															 FinderException {
		final PdfPTable summaryTable
				= new PdfPTable (new float [] { 7.0f, 3.6f, 3.0f });
		summaryTable.getDefaultCell ().setBorder (0);
		final PaymentSummary summary = new PaymentSummary (records);
		addPhrase (summaryTable,
							 localize (TOTAL_AMOUNT_PLACEMENTS_KEY,
												 TOTAL_AMOUNT_PLACEMENTS_DEFAULT) + ": ");
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (summaryTable, integerFormatter.format (summary.getPlacementCount ()));
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		addPhrase (summaryTable, "");
		addPhrase (summaryTable,
							 localize (TOTAL_AMOUNT_INDIVIDUALS_KEY,
												 TOTAL_AMOUNT_INDIVIDUALS_DEFAULT) + ": ");
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (summaryTable, integerFormatter.format
							 (summary.getIndividualsCount ()));
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		addPhrase (summaryTable, "");
		addPhrase (summaryTable,
							 localize (TOTAL_AMOUNT_VAT_EXCLUDED_KEY,
												 TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT) + ": ");
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (summaryTable, getFormattedAmount
							 (summary.getTotalAmountVatExcluded ()));
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		addPhrase (summaryTable, "");
		addPhrase (summaryTable,
							 localize (TOTAL_AMOUNT_VAT_KEY, TOTAL_AMOUNT_VAT_DEFAULT)
							 + ": ");
		summaryTable.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
		addPhrase (summaryTable, getFormattedAmount (summary.getTotalAmountVat ()));
		addPhrase (summaryTable, "");
		return summaryTable;
	}
	
	private void addRecordOnAPdfRow (final PdfPTable table,
																	 final PaymentRecord record) {
		final Date period = record.getPeriod ();
		final String periodText = null != period
				? getFormattedPeriod (period) : "";
		addPhrase (table, record.getStatus () + "");
		addPhrase (table, periodText);
		addPhrase (table, record.getPaymentText ());
		table.getDefaultCell ().setHorizontalAlignment
				(Element.ALIGN_RIGHT);
		addPhrase (table, integerFormatter.format (record.getPlacements ()));
		addPhrase (table, getFormattedAmount (record.getPieceAmount ()));
		addPhrase (table, getFormattedAmount (record.getTotalAmount ()));
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		addPhrase (table, record.getNotes ());
	}

	private PostingField [] getCurrentPostingFields
		(final PostingBusiness postingBusiness) throws RemoteException {
		final Date now = new Date (System.currentTimeMillis ());
		final Collection fields = postingBusiness.getAllPostingFieldsByDate (now);
		final PostingField [] array = new PostingField [0];
		return fields != null ? (PostingField []) fields.toArray (array)
				: array;
	}
	
	private long roundAmount (final float f) {
		return se.idega.idegaweb.commune.accounting.business.AccountingUtil.roundAmount (f);
	}
	
	private int min (final int a, final int b) {
		return a < b ? a : b;
	}
	
	private String getFormattedAmount (final float f) {
		return f == -1.0f ? "0" : integerFormatter.format (roundAmount (f));
	}
	
	private String getFormattedPeriod (Date date) {
		return null != date ? periodFormatter.format (date) : "";
	}
	
	private String getSchoolCategoryName (final String schoolCategoryId) {
		try {
			final SchoolCategoryHome categoryHome
					=	(SchoolCategoryHome) IDOLookup.getHome (SchoolCategory.class);
			final SchoolCategory category
					= categoryHome.findByPrimaryKey (schoolCategoryId);
			return localize (category.getLocalizedKey (), category.getName ());
		} catch (Exception dummy) {
			return "";
		}
	}

	public String localize(String textKey, String defaultText) {
		return bundle.getLocalizedString(textKey, defaultText);
	}	
}
