package se.idega.idegaweb.commune.accounting.invoice.business;

import is.idega.experimental.pdftest.PDFTest;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.presentation.PaymentRecordMaintenance;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
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
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author gimmi
 */
public class CheckAmountBusinessBean extends IBOServiceBean {
	private static final SimpleDateFormat periodFormatter	= new SimpleDateFormat ("yyMM");
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
	public void sendCheckAmountLists(IWContext iwc, String schoolCategoryPK) {
		IWBundle accountingBundle = iwc.getApplication().getBundle(AccountingBlock.IW_ACCOUNTING_BUNDLE_IDENTIFER);
		IWResourceBundle iwrb = accountingBundle.getResourceBundle(iwc);
		sendCheckAmountLists(iwc, iwrb, schoolCategoryPK);
	}
	
	public void sendCheckAmountLists(IWContext iwc, IWResourceBundle iwrb, String schoolCategoryPK) {
		
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

					File file = createPDF(iwc, iwrb, schoolCategory, school);
					//Link link = new Link(file.getName(),"file://"+file.getAbsolutePath());
					//add(link);

					SchoolUserBusiness sub = (SchoolUserBusiness) IDOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
					Collection headmasters = sub.getHeadmasters(school);
					Collection assheadmasters = sub.getAssistantHeadmasters(school);
					Collection users = new Vector();
					Collection emails = new Vector();
					users.addAll(headmasters);
					users.addAll(assheadmasters);
					User user;
					Email email;
					
					boolean emailFound = false;
					if (users.size() > 0) {
						for (Iterator i = users.iterator(); i.hasNext();) {
							user = (User) i.next();
							//System.out.println("Creating userMessage for "+user.getName());
							mBusiness.createUserMessage(null, user, iwc.getCurrentUser(), null, iwrb.getLocalizedString( PaymentRecordMaintenance.PREFIX+"check_amount_list_sent", "Check amount list has been sent"), iwrb.getLocalizedString( PaymentRecordMaintenance.PREFIX+"check_amount_list_sent_to_all_parties", "Check amount list has been sent to all concerning parties"), false);
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
						plm.setSubject(iwrb.getLocalizedString( PaymentRecordMaintenance.PREFIX+"check_amount_list", "Check amount list"));
						plm.setMessageData(icFile);
						plm.setOwner(iwc.getCurrentUser());
						
						mBusiness.flagMessageAsPrinted(iwc.getCurrentUser(), plm);
						plm.store();
					} else {
						//add(" <b>Email</b> ");
						for (Iterator i = emails.iterator(); i.hasNext();) {
							email = (Email) i.next();
							if (mBusiness.getIfCanSendEmail()) {
								mBusiness.sendMessage(email.getEmailAddress(), iwrb.getLocalizedString( PaymentRecordMaintenance.PREFIX+"check_amount_list", "Check amount list")+" - "+school.getName(), "", file);
							}
							//mBusiness.sendMessage("gimmi@idega.is", "CheckbeloppsLista "+school.getName(), "Test Text", file);
							//SendMail.send( null, email.getEmailAddress(), null, null, "mail.idega.is", "CheckbeloppsLista "+school.getName(), "Test Text", file);
							//SendMail.send( null, "gimmi@idega.is", null, null, "mail.idega.is", "CheckbeloppsLista "+school.getName(), "Test Text", file);
							//add(email.getEmailAddress() +", ");
						}
					}
					file.delete();
					//add("<br>");
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private File createPDF(IWContext iwc, IWResourceBundle iwrb, SchoolCategory schoolCategory, School school) throws Exception {
		FileOutputStream fileOut;
		PaymentRecord paymentRecord;
		File file = new File("CheckbeloppsLista_"+school.getPrimaryKey()+"_"+IWTimestamp.RightNow().getDate().toString()+".pdf");
		fileOut = new FileOutputStream(file);
		
		Document outerDocument = PDFTest.getLetterDocumentTemplate();
		PdfWriter writer = PdfWriter.getInstance(outerDocument, fileOut);
		outerDocument.open();
		
		PaymentHeaderHome phHome = (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
		PaymentRecordHome prHome = (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
		Iterator paymentRecordIter;
		Collection paymentHeaders;
		Collection paymentRecords;
		
		DocumentBusiness docBus = (DocumentBusiness) IBOLookup.getServiceInstance(iwc, DocumentBusiness.class);
		
		outerDocument.newPage();
		String countryText = "";
		if (school.getCountry() != null) {
			countryText = school.getCountry().getName();
		}
		String sAddrString = school.getName()+"\n"+school.getSchoolAddress()+"\n"+school.getSchoolZipCode()+" "+school.getSchoolZipArea()+"\n"+countryText;
		docBus.createDefaultLetterHeader( outerDocument, sAddrString, writer);
		
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
		table.addCell(new Cell(iwrb.getLocalizedString( PaymentRecordMaintenance.PERIOD_KEY , PaymentRecordMaintenance.PERIOD_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( PaymentRecordMaintenance.PLACEMENT_KEY, PaymentRecordMaintenance.PLACEMENT_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( PaymentRecordMaintenance.NO_OF_PLACEMENTS_KEY, PaymentRecordMaintenance.NO_OF_PLACEMENTS_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( PaymentRecordMaintenance.TOTAL_AMOUNT_KEY, PaymentRecordMaintenance.TOTAL_AMOUNT_DEFAULT)));
		table.addCell(new Cell(iwrb.getLocalizedString( PaymentRecordMaintenance.NOTE_KEY, PaymentRecordMaintenance.NOTE_DEFAULT)));
		
		
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
					noPl.setHorizontalAlignment(Table.ALIGN_RIGHT);
					table.addCell(noPl);
					Cell tot = new Cell(Float.toString(paymentRecord.getTotalAmount()));
					tot.setHorizontalAlignment(Table.ALIGN_RIGHT);
					table.addCell(tot);
					Cell notes = new Cell("");
					if (comment != null) {
						notes = new Cell(comment);
					}
					table.addCell(notes);
				}
			}
		}
		Cell totPlacStr = new Cell(iwrb.getLocalizedString(PaymentRecordMaintenance.TOTAL_AMOUNT_PLACEMENTS_KEY, PaymentRecordMaintenance.TOTAL_AMOUNT_DEFAULT));
		totPlacStr.setColspan(2);
		totPlacStr.setBorderColor(Color.WHITE);
		table.addCell(totPlacStr);
		Cell totPlac = new Cell(Integer.toString(totalPlacements));
		totPlac.setColspan(3);
		totPlac.setBorderColor(Color.WHITE);
		table.addCell(totPlac);

		Cell totXMomStr = new Cell(iwrb.getLocalizedString(PaymentRecordMaintenance.TOTAL_AMOUNT_VAT_EXCLUDED_KEY, PaymentRecordMaintenance.TOTAL_AMOUNT_VAT_EXCLUDED_DEFAULT));
		totXMomStr.setColspan(2);
		totXMomStr.setBorderColor(Color.WHITE);
		table.addCell(totXMomStr);
		Cell totXMom = new Cell(Float.toString(totalAmountSum));
		totXMom.setColspan(3);
		totXMom.setBorderColor(Color.WHITE);
		table.addCell(totXMom);
		
		Cell totMomsStr = new Cell(iwrb.getLocalizedString(PaymentRecordMaintenance.TOTAL_AMOUNT_VAT_KEY, PaymentRecordMaintenance.TOTAL_AMOUNT_VAT_DEFAULT));
		totMomsStr.setColspan(2);
		totMomsStr.setBorderColor(Color.WHITE);
		table.addCell(totMomsStr);
		Cell totMoms = new Cell(Float.toString(totalVatSum));
		totMoms.setColspan(3);
		totMoms.setBorderColor(Color.WHITE);
		table.addCell(totMoms);
		outerDocument.add(table);
		
		
		outerDocument.close();
		writer.close();
		fileOut.close();
		return file;
	}

	private String getFormattedPeriod (Date date) {
		return null != date ? periodFormatter.format (date) : "";
	}
	
}
