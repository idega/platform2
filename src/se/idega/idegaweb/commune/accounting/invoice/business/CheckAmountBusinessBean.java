package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.user.data.User;
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
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.posting.data.PostingFieldBMPBean;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;

/**
 * Last modified: $Date: 2004/03/09 16:34:51 $ by $Author: staffan $
 *
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.29 $
 */
public class CheckAmountBusinessBean extends IBOServiceBean implements CheckAmountBusiness, InvoiceStrings {
	private final static Font SANSSERIF_FONT
		= FontFactory.getFont (FontFactory.HELVETICA, 9);
	private final static Font SANSSERIFBOLD_FONT
		= FontFactory.getFont (FontFactory.HELVETICA_BOLD, 11);
	private static final NumberFormat integerFormatter
		= NumberFormat.getIntegerInstance (LocaleUtil.getSwedishLocale ());
	private static final SimpleDateFormat periodFormatter
		= new SimpleDateFormat ("yyMM");
	private static final SimpleDateFormat yearAndMonthFormatter
		= new SimpleDateFormat ("yyyy-MM");
	private static final SimpleDateFormat dateAndTimeFormatter
		= new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	private static final Color LIGHT_BLUE = new Color (0xf4f4f4);

	public Map sendCheckAmountLists(User currentUser, String schoolCategoryPK) {
		final Map result = new TreeMap ();
		final Collection filesSentByEmail = new TreeSet ();
		final Collection filesSentByPapermail = new TreeSet ();
		final Collection noMailSentSinceNoPaymentRecords = new TreeSet ();
		result.put (FILES_SENT_BY_EMAIL_KEY, filesSentByEmail);
		result.put (FILES_SENT_BY_PAPERMAIL_KEY, filesSentByPapermail);
		result.put (NO_MAIL_SENT_SINCE_NO_PAYMENT_RECORDS_KEY,
								noMailSentSinceNoPaymentRecords);
		try {
			SchoolCategory schoolCategory
					= getSchoolCategoryHome ().findByPrimaryKey (schoolCategoryPK);
			Collection schools = getSchoolHome ().findAllByCategory (schoolCategory);
			if (schools != null && !schools.isEmpty()) {
				final MemoryFileBuffer checkAmountListsBuffer = new MemoryFileBuffer();
				final OutputStream outStream
						= new MemoryOutputStream (checkAmountListsBuffer);
				final Document document = createPdfDocument ();
				final PdfWriter writer = createPdfWriter(document, outStream);
				document.addCreationDate();
				document.open();
				for (Iterator i = schools.iterator (); i.hasNext();) {
					final School school = (School) i.next();
					final PaymentRecord[] records
							= getLockedPaymentRecords(schoolCategory, school);
					if (0 >= records.length) {
						noMailSentSinceNoPaymentRecords.add (school.getName ());
					} else {
						Collection emailReceivers = findEmailReceiversAndNotifyThem
								(currentUser, school);					
						if (emailReceivers.isEmpty ()) {
							addCheckAmountListToDocument (writer, document, schoolCategory,
																						school, records);
							filesSentByPapermail.add (school.getName ());
						} else {
							sendEmails(schoolCategory, school, records, emailReceivers);
							filesSentByEmail.add (school.getName ());
						}
						setStatusToHistory (records);
					}
				}
				document.close();
				writer.close ();
				outStream.close ();
				putCheckAmountListsInPrinterQueue(checkAmountListsBuffer, currentUser,
																					schoolCategory);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}

	private static void setStatusToHistory (final PaymentRecord[] records) {
		for (int i = 0; records.length > i; i++) {
			final PaymentRecord record = records [i];
			record.setStatus (ConstantStatus.HISTORY);
			record.store ();
			final PaymentHeader header = record.getPaymentHeader ();
			if (null != header) {
				header.setStatus (ConstantStatus.HISTORY);
				header.store ();
			}
		}
	}

	private Document createPdfDocument() {
		final DocumentBusiness documentBusiness = getDocumentBusiness ();
		final Document document = new Document
				(PageSize.A4,documentBusiness.getPointsFromMM(30),
				 documentBusiness.getPointsFromMM(30),
				 documentBusiness.getPointsFromMM(0),
				 documentBusiness.getPointsFromMM(0));
		return document;
	}

	private void putCheckAmountListsInPrinterQueue
		(final MemoryFileBuffer buffer, User currentUser,
		 SchoolCategory schoolCategory)
		throws RemoteException, CreateException, IDOLookupException {
		final ICFileHome icFileHome = (ICFileHome) getIDOHome(ICFile.class);
		final ICFile icFile = icFileHome.create();
		final InputStream inStream = new MemoryInputStream(buffer);
		icFile.setFileValue(inStream);
		icFile.setMimeType("application/x-pdf");
		icFile.setName("checkamountlist.pdf");
		icFile.setFileSize(buffer.length());
		icFile.store();
		//result.put (CHECK_AMOUNT_FILE_KEY, icFile.getPrimaryKey());
		PrintedLetterMessage queueItem
				= (PrintedLetterMessage) getPrintedLetterMessageHome ().create();
		final StringBuffer subject = new StringBuffer ();
		subject.append (localize (CHECK_AMOUNT_LIST_KEY,
															CHECK_AMOUNT_LIST_DEFAULT));
		subject.append (" ");
		subject.append (localize (schoolCategory.getLocalizedKey (),
															schoolCategory.getName ()));
		subject.append (" ");
		subject.append (yearAndMonthFormatter.format (new java.util.Date ()));
		queueItem.setSubject("" + subject);
		queueItem.setMessageData(icFile);
		queueItem.setOwner(currentUser);
		//mBusiness.flagMessageAsUnPrinted(currentUser, queueItem);
		getMessageBusiness ().flagMessageAsPrinted(currentUser, queueItem);
		queueItem.store();
	}

	private PdfWriter createPdfWriter
		(final Document document, final OutputStream outStream)
		throws DocumentException {
		final PdfWriter writer = PdfWriter.getInstance(document, outStream);
		writer.setViewerPreferences
				(PdfWriter.PageModeUseThumbs | PdfWriter.HideMenubar
				 | PdfWriter.PageLayoutOneColumn | PdfWriter.FitWindow
				 | PdfWriter.CenterWindow);
		return writer;
	}

	private void sendEmails
		(SchoolCategory schoolCategory, final School school,
		 final PaymentRecord[] records, Collection emailReceivers)
		throws Exception {
		// create pdf document
		File file = new File (getTitle (school.getPrimaryKey()) + ".pdf");
		OutputStream outStream = new FileOutputStream(file);
		final Document document = createPdfDocument ();
		final PdfWriter writer = createPdfWriter(document, outStream);
		document.addCreationDate();
		document.open();
		addCheckAmountListToDocument(writer, document, schoolCategory, school,
																 records);
		document.close();
		writer.close();
		outStream.close();
		
		// send mails with document attached
		final MessageBusiness messageBusiness = getMessageBusiness ();
		for (Iterator i = emailReceivers.iterator(); i.hasNext();) {
			Email email = (Email) i.next();
			if (messageBusiness.getIfCanSendEmail()) {
				messageBusiness.sendMessage
						(email.getEmailAddress(),
						 localize (CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT) + " - "
						 + school.getName() + " "
						 + yearAndMonthFormatter.format (new java.util.Date ()), "", file);
			}
		}
		file.delete();
	}

	private void addCheckAmountListToDocument
		(final PdfWriter writer, final Document document,
		 SchoolCategory schoolCategory, final School school,
		 final PaymentRecord[] records) throws Exception {
		final DocumentBusiness documentBusiness = getDocumentBusiness ();
		document.newPage();
		final String dateString = IWTimestamp.RightNow().getLocaleDate
				(getIWApplicationContext().getApplicationSettings().getDefaultLocale());
		documentBusiness.createHeaderDate(document, writer, dateString);
		documentBusiness.createLogoContent(document);
		documentBusiness.createAddressContent(getAddressString	(school),writer);
		documentBusiness.createNewlinesContent(document);
		document.add(getExternalContentTable(schoolCategory, records));
		documentBusiness.createCommuneFooter(writer);
	}

	private Collection findEmailReceiversAndNotifyThem
		(User currentUser, School school)
		throws IBOLookupException, RemoteException, FinderException {
		SchoolUserBusiness sub = getSchoolUserBusiness ();
		Collection users = new HashSet();
		users.addAll(sub.getHeadmasters(school));
		users.addAll(sub.getAssistantHeadmasters(school));
		users.addAll(sub.getEconomicalResponsibles(school));
		Collection emailReceivers = new HashSet();
		if (users.size() > 0) {
			for (Iterator i = users.iterator(); i.hasNext();) {
				final User user = (User) i.next();
				getMessageBusiness ().createUserMessage(null, user, currentUser, null, localize(CHECK_AMOUNT_LIST_SENT_KEY, CHECK_AMOUNT_LIST_SENT_DEFAULT), localize(CHECK_AMOUNT_LIST_SENT_TO_ALL_PARTIES_KEY, CHECK_AMOUNT_LIST_SENT_TO_ALL_PARTIES_DEFAULT), false);
				emailReceivers.addAll(user.getEmails());
			}
		}
		return emailReceivers;
	}

	private String getTitle (final Object schoolId) {
		return "CheckbeloppsLista_" + schoolId + "_"
				+ IWTimestamp.RightNow ().getDate ();
	}

	private PdfPTable getExternalContentTable(SchoolCategory schoolCategory, PaymentRecord[] records) throws RemoteException {
		// add content to document
		final PdfPTable outerTable = new PdfPTable (1);
		outerTable.setWidthPercentage (100f);
		outerTable.getDefaultCell ().setBorder (0);
		addBoldPhrase (outerTable, "\n\n" + localize
							 (CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT)  + "\n\n");
		final PdfPTable headerTable	= getExternalHeaderTable (schoolCategory);
		outerTable.addCell (headerTable);
		addPhrase (outerTable, "\n");
		final PdfPTable recordListTable = getRecordListTable(records, false);
		outerTable.addCell (recordListTable);
		addPhrase (outerTable, "\n");
		final PdfPTable summaryTable = getSummaryTable(records, false);
		outerTable.addCell (summaryTable);
		return outerTable;
	}

	private PaymentRecord[] getLockedPaymentRecords(SchoolCategory schoolCategory, School school) throws FinderException, IDOLookupException {
		Collection paymentHeaders
				= getPaymentHeaderHome ().findBySchoolAndSchoolCategoryPKAndStatus
				(school.getPrimaryKey(), schoolCategory.getPrimaryKey(),
				 ConstantStatus.LOCKED + "");
		PaymentRecord [] records = new PaymentRecord [0];
		if (paymentHeaders != null && !paymentHeaders.isEmpty()) {
			final Collection recordCollection
					= getPaymentRecordHome ().findByPaymentHeaders(paymentHeaders);
			if (recordCollection != null && !recordCollection.isEmpty()) {
				records = (PaymentRecord []) recordCollection.toArray (records);
			}
		}
		return records;
	}

	private String getAddressString (final School school) {
		final StringBuffer result = new StringBuffer ();
		final String street = school.getSchoolAddress ();
		final String zip = school.getSchoolZipCode ();
		final String city = school.getSchoolZipArea ();
		final String country = school.getCountry() != null
				? school.getCountry().getName() : null;
		result.append (school.getName() + '\n');
		result.append ((null != street ? street : "") + '\n');
		result.append (null != zip ? zip + " " : "");
		result.append (null != city ? city : "");
		result.append (null != country && !country.equals ("Sweden")
									 ? '\n' + country : "");
		return result.toString ();
	}

	private PdfPTable getExternalHeaderTable (final SchoolCategory category) {
		return getHeaderTable ("" + category.getPrimaryKey (), null, null,
													 null, false);
	}

	public MemoryFileBuffer getInternalCheckAmountListBuffer
		(final String schoolCategoryId, final Integer providerId,
		 final Date startPeriod, final Date endPeriod, final boolean isShowPosting)
		throws RemoteException, DocumentException {
		PaymentRecord [] records = new PaymentRecord [0];
		if (null != schoolCategoryId && null != providerId) {
			records = getInvoiceBusiness ()
					.getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
					(schoolCategoryId, providerId, startPeriod, endPeriod);
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
		document.addTitle (getTitle (providerId));
		document.addCreationDate ();
		document.open ();

		// add content to document
		final PdfPTable outerTable = new PdfPTable (1);
		outerTable.setWidthPercentage (100f);
		outerTable.getDefaultCell ().setBorder (0);
		addBoldPhrase (outerTable,
									 localize (CHECK_AMOUNT_LIST_KEY,
														 CHECK_AMOUNT_LIST_DEFAULT)  + "\n\n");
		final PdfPTable headerTable	= getHeaderTable
				(schoolCategoryId, providerId, startPeriod, endPeriod, true);
		outerTable.addCell (headerTable);
		addPhrase (outerTable, "\n");
		final PdfPTable recordListTable = getRecordListTable(records, true);
		outerTable.addCell (recordListTable);
		addPhrase (outerTable, "\n");
		final PdfPTable summaryTable = getSummaryTable(records, true);
		outerTable.addCell (summaryTable);
		if (isShowPosting) {
			addPhrase (outerTable, "\n");
			addPhrase (outerTable,
								 localize (OWN_POSTING_KEY, OWN_POSTING_DEFAULT) + ":");
			final PostingBusiness postingBusiness = getPostingBusiness ();
			final PdfPTable ownPostingTable
					= getPostingTable (records, true, postingBusiness);
			outerTable.addCell (ownPostingTable);
			addPhrase (outerTable, "");
			addPhrase (outerTable,
								 localize (DOUBLE_POSTING_KEY, DOUBLE_POSTING_DEFAULT) + ":");
			final PdfPTable doublePostingTable = getPostingTable (records, false,
																														postingBusiness);
			outerTable.addCell (doublePostingTable);
		}
		document.add (outerTable);        
			
		// close document
		document.close ();
		writer.setPdfVersion(PdfWriter.VERSION_1_2);
		writer.close ();
		buffer.setMimeType("application/pdf");

		return buffer;
	}

	private PdfPTable getRecordListTable (final PaymentRecord[] records,
																				final boolean isInternal) {
		final String [][] columnNames =
				{{ STATUS_KEY, STATUS_DEFAULT }, { PERIOD_KEY, PERIOD_DEFAULT },
				 { PLACEMENT_KEY, PLACEMENT_DEFAULT },
				 { NUMBER_OF_KEY, NUMBER_OF_DEFAULT },
				 { PRICE_PER_MONTH_KEY, PRICE_PER_MONTH_DEFAULT },
				 { PAYMENT_AMOUNT_KEY, PAYMENT_AMOUNT_DEFAULT },
				 { NOTE_KEY, NOTE_DEFAULT }};
		final float [] externalColWidth
				= new float [] {1.0f, 5.0f, 1.0f, 1.2f, 1.4f};
		final float [] internalColWidth
				= new float [] { 1.0f, 1.0f, 5.0f, 1.0f, 1.2f, 1.4f, 3.0f };
		final PdfPTable table
				= new PdfPTable	(isInternal ? internalColWidth : externalColWidth);
		table.setWidthPercentage (100f);
		table.getDefaultCell ().setBackgroundColor (new Color (0xd0daea));
		for (int i = 0; i < columnNames.length; i++) {
			if (isInternal || (0 != i && columnNames.length - 1 != i)) {
				addPhrase (table, localize (columnNames [i][0],	columnNames [i][1]));
			}
		}
		table.setHeaderRows (1);  // this is the end of the table header
		for (int i = 0; i < records.length; i++) {
			table.getDefaultCell ().setBackgroundColor (i % 2 == 0 ? Color.white
																									: LIGHT_BLUE);
			final PaymentRecord record = records [i];
			addRecordOnARow (table, record, isInternal);
		}
		return table;
	}

	private PdfPTable getPostingTable
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
				final StringBuffer value = new StringBuffer ();
				final PostingField field = fields [j];
				if (null != field) {
					if (field.getJustification ()
							== PostingFieldBMPBean.JUSTIFY_RIGHT) {
						table.getDefaultCell ().setHorizontalAlignment
								(Element.ALIGN_RIGHT);
					} else {
						table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
					}
					if (null != postingString) {
						final int endPosition = min (offset + field.getLen (),
																				 postingString.length ());
						value.append (postingString.substring
													(offset, endPosition).trim ());
						offset = endPosition;
					}
				}
				addPhrase (table, value.toString ());
			}
			table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_RIGHT);
			addPhrase (table, getFormattedAmount (record.getTotalAmount ()));
		}
		return table;
	}

	private PdfPTable getHeaderTable
		(final String schoolCategoryId, final Integer providerId,
		 final Date startPeriod, final Date endPeriod, final boolean isInternal) {
		final PdfPTable headerTable = new PdfPTable (new float [] { 2.0f, 3.0f });
		headerTable.getDefaultCell ().setBorder (0);
		addPhrase (headerTable,
							 localize (MAIN_ACTIVITY_KEY, MAIN_ACTIVITY_DEFAULT) + ": ");
		addPhrase (headerTable, getSchoolCategoryName (schoolCategoryId));
		if (isInternal) {
			addPhrase (headerTable, localize (PERIOD_KEY, PERIOD_DEFAULT) + ": ");
			final String period	= (getFormattedPeriod (startPeriod)) + " - "
					+ (getFormattedPeriod (endPeriod));
			addPhrase (headerTable, period);
		}
		addPhrase (headerTable, localize (PRINT_DATE_KEY, PRINT_DATE_DEFAULT)
							 + ": ");
		addPhrase (headerTable,
							 dateAndTimeFormatter.format (new java.util.Date ()));
		if (isInternal) {
			try {
				final School school	= getSchoolHome ().findByPrimaryKey (providerId);
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
		}
		
		return headerTable;
	}
	
	private PdfPTable getSummaryTable
		(final PaymentRecord [] records, final boolean isInternal)
		throws RemoteException {
		final float [] externalColWidth	= new float [] { 6.0f, 3.6f, 0.0f };
		final float [] internalColWidth	= new float [] { 7.0f, 3.6f, 3.0f };
		final PdfPTable summaryTable
				= new PdfPTable	(isInternal ? internalColWidth : externalColWidth);
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
	
	private void addRecordOnARow
		(final PdfPTable table, final PaymentRecord record,
		 final boolean isInternal) {
		if (isInternal) {
			addPhrase (table, record.getStatus () + "");
		}
		final Date period = record.getPeriod ();
		final String periodText = null != period
				? getFormattedPeriod (period) : "";
		addPhrase (table, periodText);
		addPhrase (table, record.getPaymentText ());
		table.getDefaultCell ().setHorizontalAlignment
				(Element.ALIGN_RIGHT);
		addPhrase (table, integerFormatter.format (record.getPlacements ()));
		addPhrase (table, getFormattedAmount (record.getPieceAmount ()));
		addPhrase (table, getFormattedAmount (record.getTotalAmount ()));
		table.getDefaultCell ().setHorizontalAlignment (Element.ALIGN_LEFT);
		if (isInternal) {
			addPhrase (table, record.getNotes ());
		}
	}

	private PostingField [] getCurrentPostingFields
		(final PostingBusiness postingBusiness) throws RemoteException {
		final Date now = new Date (System.currentTimeMillis ());
		final Collection fields = postingBusiness.getAllPostingFieldsByDate (now);
		final PostingField [] array = new PostingField [0];
		return fields != null ? (PostingField []) fields.toArray (array)
				: array;
	}
	
	private void addPhrase (final PdfPTable table, final String string) {
		table.addCell (new Phrase (new Chunk (null != string ? string : "",
																					SANSSERIF_FONT)));
	}
	
	private void addBoldPhrase (final PdfPTable table, final String string) {
		table.addCell (new Phrase (new Chunk (null != string ? string : "",
																					SANSSERIFBOLD_FONT)));
	}
	
	private static float mmToPoints (final float mm) {
		return mm*72/25.4f;
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
			final SchoolCategory category
					= getSchoolCategoryHome ().findByPrimaryKey (schoolCategoryId);
			return localize (category.getLocalizedKey (), category.getName ());
		} catch (Exception dummy) {
			return "";
		}
	}

	private InvoiceBusiness getInvoiceBusiness () {
		try {
			return (InvoiceBusiness) getServiceInstance (InvoiceBusiness.class);
		}	catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
 
	private DocumentBusiness getDocumentBusiness () {
		try {
			return (DocumentBusiness) getServiceInstance (DocumentBusiness.class);
		}	catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
 
	private PostingBusiness getPostingBusiness () {
		try {
			return (PostingBusiness) getServiceInstance (PostingBusiness.class);
		}	catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
 
	private SchoolUserBusiness getSchoolUserBusiness () {
		try {
			return (SchoolUserBusiness) getServiceInstance (SchoolUserBusiness.class);
		}	catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
 
	private MessageBusiness getMessageBusiness () {
		try {
			return (MessageBusiness) getServiceInstance (MessageBusiness.class);
		}	catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
 
	private SchoolCategoryHome getSchoolCategoryHome ()
		throws IDOLookupException {
		return (SchoolCategoryHome) IDOLookup.getHome (SchoolCategory.class);
	}
 
	private PaymentRecordHome getPaymentRecordHome () throws IDOLookupException {
		return (PaymentRecordHome) IDOLookup.getHome (PaymentRecord.class);
	}
 
	private PaymentHeaderHome getPaymentHeaderHome () throws IDOLookupException {
		return (PaymentHeaderHome) IDOLookup.getHome (PaymentHeader.class);
	}
 
	private PrintedLetterMessageHome getPrintedLetterMessageHome ()
		throws IDOLookupException {
		return (PrintedLetterMessageHome)
				IDOLookup.getHome (PrintedLetterMessage.class);
	}
 
	private SchoolHome getSchoolHome () throws IDOLookupException {
		return (SchoolHome) IDOLookup.getHome (School.class);
	}
 
	private String localize(String textKey, String defaultText) {
		return getResourceBundle ().getLocalizedString (textKey, defaultText);
	}	

	private IWResourceBundle getResourceBundle () {
		final IWMainApplication app = getIWApplicationContext ().getIWMainApplication ();
		final IWBundle bundle
				= app.getBundle (AccountingBlock.IW_ACCOUNTING_BUNDLE_IDENTIFER);
		return bundle.getResourceBundle (app.getSettings ().getDefaultLocale ());
	}
}
