package se.idega.idegaweb.commune.accounting.invoice.business;

import is.idega.experimental.pdftest.PDFTest;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.FinderException;

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

import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
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

/**
 * Last modified: $Date: 2004/01/20 14:51:13 $ by $Author: staffan $
 *
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.6 $
 */
public class CheckAmountBusinessBean extends IBOServiceBean implements CheckAmountBusiness, InvoiceStrings {
	private final static Font SANSSERIF_FONT
		= FontFactory.getFont (FontFactory.HELVETICA, 9);
	private static final NumberFormat integerFormatter
		= NumberFormat.getIntegerInstance (LocaleUtil.getSwedishLocale ());
	private static final SimpleDateFormat periodFormatter
		= new SimpleDateFormat ("yyMM");
	private static final SimpleDateFormat dateAndTimeFormatter
		= new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	private static final Color LIGHT_BLUE = new Color (0xf4f4f4);

	/*
	public void main(IWContext iwc) {
		//IWBundle bundle = super.
		//iwrb = super.getResourceBundle(iwc);
		IWBundle accountingBundle = iwc.getApplication().getBundle(AccountingBlock.IW_ACCOUNTING_BUNDLE_IDENTIFER);
		IWResourceBundle iwrb = accountingBundle.getResourceBundle(iwc);
		String schoolCategory = null;
		try {
			schoolCategory = "ELEMENTARY_SCHOOL";
			sendCheckAmountLists(iwc, iwrb, schoolCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public Map sendCheckAmountLists(IWContext iwc, String schoolCategoryPK) {
		IWBundle accountingBundle = iwc.getApplication().getBundle(AccountingBlock.IW_ACCOUNTING_BUNDLE_IDENTIFER);
		IWResourceBundle iwrb = accountingBundle.getResourceBundle(iwc);
		return sendCheckAmountLists(iwc, iwrb, schoolCategoryPK);
	}
	
	public Map sendCheckAmountLists(IWContext iwc, IWResourceBundle iwrb, String schoolCategoryPK) {
		final Map result = new TreeMap ();
		final Map filesSentByEmail = new TreeMap ();
		final Map filesSentByPapermail = new TreeMap ();
		result.put (FILES_SENT_BY_EMAIL_KEY, filesSentByEmail);
		result.put (FILES_SENT_BY_PAPERMAIL_KEY, filesSentByPapermail);

		try {
			SchoolCategory schoolCategory = ((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(schoolCategoryPK);
			
			Collection schools = ((SchoolHome) IDOLookup.getHome(School.class)).findAllByCategory(schoolCategory);
			
			Font titleFont = new Font(Font.HELVETICA);
			titleFont.setSize(12);
			Font tagFont = new Font(Font.HELVETICA);
			tagFont.setSize(12);
			Font textFont = new Font(Font.HELVETICA);
			textFont.setSize(9);
			
			School school;
			if (schools != null && !schools.isEmpty()) {
				MessageBusiness mBusiness = (MessageBusiness) IBOLookup.getServiceInstance(iwc, MessageBusiness.class); 
				Iterator schoolIter = schools.iterator();
				while (schoolIter.hasNext()) {
					school = (School) schoolIter.next();

					File file = createExternalCheckAmountList (iwc, schoolCategory, school);
					//Link link = new Link(file.getName(),"file://"+file.getAbsolutePath());
					//add(link);

					SchoolUserBusiness sub = (SchoolUserBusiness) IBOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
					Collection headmasters = sub.getHeadmasters(school);
					Collection assheadmasters = sub.getAssistantHeadmasters(school);
					Collection users = new ArrayList();
					Collection emails = new ArrayList();
					users.addAll(headmasters);
					users.addAll(assheadmasters);
					User user;
					Email email;
					
					boolean emailFound = false;
					if (users.size() > 0) {
						for (Iterator i = users.iterator(); i.hasNext();) {
							user = (User) i.next();
							//System.out.println("Creating userMessage for "+user.getName());
							mBusiness.createUserMessage(null, user, iwc.getCurrentUser(), null, iwrb.getLocalizedString(CHECK_AMOUNT_LIST_SENT_KEY, CHECK_AMOUNT_LIST_SENT_DEFAULT), iwrb.getLocalizedString(CHECK_AMOUNT_LIST_SENT_TO_ALL_PARTIES_KEY, CHECK_AMOUNT_LIST_SENT_TO_ALL_PARTIES_DEFAULT), false);
							emails.addAll(user.getEmails());
							emailFound = !emails.isEmpty();
						}
					}
					
					if (!emailFound) {
						//add(" SnailMail");
						ICFileHome icFileHome = (ICFileHome) IDOLookup.getHome(ICFile.class); 
						ICFile icFile = icFileHome.create();
						//System.out.println(file.getAbsolutePath());
						icFile.setFileValue(new FileInputStream(file.getAbsoluteFile()));
						icFile.setMimeType("application/x-pdf");
						icFile.setName(file.getName());
						icFile.store();
						
						PrintedLetterMessageHome plmHome = (PrintedLetterMessageHome) IDOLookup.getHome(PrintedLetterMessage.class);
						PrintedLetterMessage plm = (PrintedLetterMessage) plmHome.create();
						plm.setSubject(iwrb.getLocalizedString(CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT));
						plm.setMessageData(icFile);
						plm.setOwner(iwc.getCurrentUser());
						
						mBusiness.flagMessageAsPrinted(iwc.getCurrentUser(), plm);
						plm.store();
						filesSentByPapermail.put (school.getName (),
																			icFile.getPrimaryKey ());
					} else {
						//add(" <b>Email</b> ");
						for (Iterator i = emails.iterator(); i.hasNext();) {
							email = (Email) i.next();
							if (mBusiness.getIfCanSendEmail()) {
								mBusiness.sendMessage(email.getEmailAddress(), iwrb.getLocalizedString( CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT)+" - "+school.getName(), "", file);
							}
							//mBusiness.sendMessage("gimmi@idega.is", "CheckbeloppsLista "+school.getName(), "Test Text", file);
							//SendMail.send( null, email.getEmailAddress(), null, null, "mail.idega.is", "CheckbeloppsLista "+school.getName(), "Test Text", file);
							//SendMail.send( null, "gimmi@idega.is", null, null, "mail.idega.is", "CheckbeloppsLista "+school.getName(), "Test Text", file);
							//add(email.getEmailAddress() +", ");
						}
						filesSentByEmail.put (school.getName (), new Integer (-1));					
					}
					file.delete();
					//add("<br>");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}

	private String getTitle (final Object schoolId) {
		return "CheckbeloppsLista_" + schoolId + "_"
				+ IWTimestamp.RightNow ().getDate ();
	}

	private File createExternalCheckAmountList (IWContext iwc, SchoolCategory schoolCategory, School school) throws Exception {
		FileOutputStream fileOut;
		File file = new File (getTitle (school.getPrimaryKey()) + ".pdf");
		fileOut = new FileOutputStream(file);
		
		Document outerDocument = PDFTest.getLetterDocumentTemplate();
		PdfWriter writer = PdfWriter.getInstance(outerDocument, fileOut);
		outerDocument.open();
		
		PaymentHeaderHome phHome = (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
		PaymentRecordHome prHome = (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
		Collection paymentHeaders = phHome.findBySchoolAndSchoolCategoryPKAndStatus(school.getPrimaryKey(), schoolCategory.getPrimaryKey(), "L");
		PaymentRecord [] records = new PaymentRecord [0];
		if (paymentHeaders != null && !paymentHeaders.isEmpty()) {
			final Collection recordCollection
					= prHome.findByPaymentHeaders(paymentHeaders);
			if (recordCollection != null && !recordCollection.isEmpty()) {
				records = (PaymentRecord []) recordCollection.toArray (records);
			}
		}
		DocumentBusiness docBus = (DocumentBusiness) IBOLookup.getServiceInstance(iwc, DocumentBusiness.class);
		outerDocument.newPage();
		String countryText = "";
		if (school.getCountry() != null) {
			countryText = school.getCountry().getName();
		}
		String sAddrString = school.getName()+"\n"+school.getSchoolAddress()+"\n"+school.getSchoolZipCode()+" "+school.getSchoolZipArea()+"\n"+countryText;
		docBus.createDefaultLetterHeader( outerDocument, sAddrString, writer);
		
		// add content to document
		final PdfPTable outerTable = new PdfPTable (1);
		outerTable.setWidthPercentage (100f);
		outerTable.getDefaultCell ().setBorder (0);
		//addPhrase (outerTable, getTitle (school.getPrimaryKey()) + "\n\n");
		//final PdfPTable headerTable	= getInternalHeaderTable (schoolCategory.getPrimaryKey (), school.getPrimaryKey (), startPeriod, endPeriod);
		//outerTable.addCell (headerTable);
		//addPhrase (outerTable, "\n");
		final PdfPTable recordListTable = getRecordListTable(records);
		outerTable.addCell (recordListTable);
		addPhrase (outerTable, "\n");
		final PdfPTable summaryTable = getSummaryTable(records);
		outerTable.addCell (summaryTable);
		outerDocument.add (outerTable);        
		/*
		float totalAmountSum = 0;
		int placements = 0;
		String placementText = "";
		String comment = "";
		int totalPlacements = 0;
		float totalVatSum = 0;

		Table table = new Table(5);
		table.setPadding(1);
		table.setSpacing(1);
		table.setWidths(new int[]{1,3,1,2,3});
		table.setWidth(100);
		//table.setBorderColor(Color.WHITE);
		table.addCell(new Cell(iwrb.getLocalizedString(PERIOD_KEY , PERIOD_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( PLACEMENT_KEY, PLACEMENT_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( NO_OF_PLACEMENTS_KEY, NO_OF_PLACEMENTS_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( TOTAL_AMOUNT_KEY, TOTAL_AMOUNT_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( NOTE_KEY, NOTE_DEFAULT)));
		
		
		paymentHeaders = phHome.findBySchoolAndSchoolCategoryPKAndStatus(school.getPrimaryKey(), schoolCategory.getPrimaryKey(), "L");
		if (paymentHeaders != null && !paymentHeaders.isEmpty()) {
			paymentRecords = prHome.findByPaymentHeaders(paymentHeaders);
			
			if (paymentRecords != null && !paymentRecords.isEmpty()) {
				paymentRecordIter = paymentRecords.iterator();
				while (paymentRecordIter.hasNext()) {
					paymentRecord = (PaymentRecord) paymentRecordIter.next();
					placementText = paymentRecord.getPaymentText();
					placements = paymentRecord.getPlacements();
					totalPlacements += placements;
					comment = paymentRecord.getNotes();
					totalAmountSum += paymentRecord.getTotalAmount();
					totalVatSum += paymentRecord.getTotalAmountVAT();
					//students.add(paymentRecord.get);
					
					Cell per = new Cell(getFormattedPeriod(paymentRecord.getPeriod()));
					table.addCell(per);
					Cell pla = new Cell("");
					if (placementText != null) {
						pla = new Cell(placementText);
					}
					table.addCell(pla);
					Cell noPl = new Cell(Integer.toString(placements));
					noPl.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(noPl);
					Cell tot = new Cell(Float.toString(paymentRecord.getTotalAmount()));
					tot.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(tot);
					Cell notes = new Cell("");
					if (comment != null) {
						notes = new Cell(comment);
					}
					table.addCell(notes);
				}
			}
		}
		Cell totPlacStr = new Cell(iwrb.getLocalizedString(TOTAL_AMOUNT_PLACEMENTS_KEY, TOTAL_AMOUNT_DEFAULT));
		totPlacStr.setColspan(2);
		totPlacStr.setBorderColor(Color.WHITE);
		table.addCell(totPlacStr);
		Cell totPlac = new Cell(Integer.toString(totalPlacements));
		totPlac.setColspan(3);
		totPlac.setBorderColor(Color.WHITE);
		table.addCell(totPlac);

		Cell totXMomStr = new Cell(iwrb.getLocalizedString(TOTAL_AMOUNT_VAT_EXCLUDED_KEY, TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT));
		totXMomStr.setColspan(2);
		totXMomStr.setBorderColor(Color.WHITE);
		table.addCell(totXMomStr);
		Cell totXMom = new Cell(Float.toString(totalAmountSum));
		totXMom.setColspan(3);
		totXMom.setBorderColor(Color.WHITE);
		table.addCell(totXMom);
		
		Cell totMomsStr = new Cell(iwrb.getLocalizedString(TOTAL_AMOUNT_VAT_KEY, TOTAL_AMOUNT_VAT_DEFAULT));
		totMomsStr.setColspan(2);
		totMomsStr.setBorderColor(Color.WHITE);
		table.addCell(totMomsStr);
		Cell totMoms = new Cell(Float.toString(totalVatSum));
		totMoms.setColspan(3);
		totMoms.setBorderColor(Color.WHITE);
		table.addCell(totMoms);
		outerDocument.add(table);
		*/
		
		outerDocument.close();
		writer.close();
		fileOut.close();
		return file;
	}

	/*
	public CheckAmountListFile (final IWResourceBundle bundle, , final InvoiceBusiness invoiceBusiness, final PostingBusiness postingBusiness)
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
	*/

	private void addPhrase (final PdfPTable table, final String string) {
		table.addCell (new Phrase (new Chunk (null != string ? string : "",
																					SANSSERIF_FONT)));
	}
	
	private static float mmToPoints (final float mm) {
		return mm*72/25.4f;
	}
	
	public int createInternalCheckAmountList
		(final String schoolCategoryId, final Integer providerId,
		 final Date startPeriod, final Date endPeriod) throws RemoteException,
																													FinderException {
		try {
			PaymentRecord [] records = new PaymentRecord [0];
			final InvoiceBusiness invoiceBusiness = getInvoiceBusiness ();
			if (null != schoolCategoryId && null != providerId) {
				records = invoiceBusiness
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
			addPhrase (outerTable, localize (CHECK_AMOUNT_LIST_KEY,
																			 CHECK_AMOUNT_LIST_DEFAULT)  + "\n\n");
			final PdfPTable headerTable	= getInternalHeaderTable
					(schoolCategoryId, providerId, startPeriod, endPeriod);
			outerTable.addCell (headerTable);
			addPhrase (outerTable, "\n");
			final PdfPTable recordListTable = getRecordListTable(records);
			outerTable.addCell (recordListTable);
			addPhrase (outerTable, "\n");
			final PdfPTable summaryTable = getSummaryTable(records);
			outerTable.addCell (summaryTable);
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
			document.add (outerTable);        
			
			// close and store document
			document.close ();
			final int docId = invoiceBusiness.generatePdf
					(localize (CHECK_AMOUNT_LIST_KEY, CHECK_AMOUNT_LIST_DEFAULT),
					 buffer);
			return docId;
		} catch (DocumentException e) {
			e.printStackTrace ();
			throw new RemoteException (e.getMessage ());
		}
	}

	private PdfPTable getRecordListTable(PaymentRecord[] records) {
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
			addRecordOnARow (table, record);
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

	private PdfPTable getInternalHeaderTable
		(final String schoolCategoryId, final Integer providerId,
		 final Date startPeriod, final Date endPeriod) {
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
	
	private PdfPTable getSummaryTable
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
	
	private void addRecordOnARow (final PdfPTable table,
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

	private InvoiceBusiness getInvoiceBusiness () {
		try {
			return (InvoiceBusiness) getServiceInstance (InvoiceBusiness.class);
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
 
	private String localize(String textKey, String defaultText) {
		return getResourceBundle ().getLocalizedString (textKey, defaultText);
	}	

	private IWResourceBundle getResourceBundle () {
		final IWMainApplication app = getIWApplicationContext ().getApplication ();
		final IWBundle bundle
				= app.getBundle (AccountingBlock.IW_ACCOUNTING_BUNDLE_IDENTIFER);
		return bundle.getResourceBundle (app.getSettings ().getDefaultLocale ());
	}
}
