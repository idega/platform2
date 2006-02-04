/*
 * Copyright (C) 2006 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.business;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
import se.idega.idegaweb.commune.accounting.export.business.MissingAddressException;
import se.idega.idegaweb.commune.accounting.export.business.MissingCustodianException;
import se.idega.idegaweb.commune.accounting.export.business.MissingPostalCodeException;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeader;
import se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderHome;
import se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecord;
import se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordHome;
import se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLog;
import se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class EconomaFileCreationThread extends Thread {

	protected String schoolCategory = null;

	protected IWTimestamp paymentDate = null;

	protected String periodText = null;

	protected User user = null;

	protected Locale currentLocale = null;

	protected IWApplicationContext iwac = null;
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.accounting";


	// private final static String IW_BUNDLE_IDENTIFIER =
	// "se.idega.idegaweb.commune.accounting";

	// private EconomaCreateExcelFileUtil excelFileUtil = null;

	/**
	 * Default constructor for the thread.
	 * 
	 * @param schoolCategory
	 * @param paymentDate
	 * @param periodText
	 * @param user
	 * @param currentLocale
	 * @param iwac
	 */
	public EconomaFileCreationThread(String schoolCategory,
			IWTimestamp paymentDate, String periodText, User user,
			Locale currentLocale, IWApplicationContext iwac) {
		this.schoolCategory = schoolCategory;
		this.paymentDate = paymentDate;
		this.periodText = periodText;
		this.user = user;
		this.currentLocale = currentLocale;
		this.iwac = iwac;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// excelFileUtil = new EconomaCreateExcelFileUtil(iwac, paymentDate);
		IWTimestamp now = IWTimestamp.RightNow();
		EconomaJournalLog log;
		try {
			log = ((EconomaJournalLogHome) IDOLookup
					.getHome(EconomaJournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileCreated();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}

		EconomaCheckHeader header = null;
		try {
			header = getEconomaBusiness()
					.getEconomaCheckHeaderBySchoolCategory(schoolCategory);
		} catch (RemoteException e) {
		}

		if (header == null) {
			try {
				header = ((EconomaCheckHeaderHome) IDOLookup
						.getHome(EconomaCheckHeader.class)).create();
			} catch (IDOLookupException e1) {
				e1.printStackTrace();
			} catch (CreateException e1) {
				e1.printStackTrace();
			}
		} else {
			Collection col = null;
			try {
				col = getEconomaBusiness().getEconomaCheckRecordByHeaderId(
						((Integer) header.getPrimaryKey()).intValue());
			} catch (RemoteException e5) {
				e5.printStackTrace();
			} catch (EJBException e5) {
				e5.printStackTrace();
			}
			if (col != null && !col.isEmpty()) {
				Iterator it = col.iterator();
				while (it.hasNext()) {
					EconomaCheckRecord rec = (EconomaCheckRecord) it.next();
					try {
						rec.remove();
					} catch (EJBException e1) {
						e1.printStackTrace();
					} catch (RemoveException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		header.setSchoolCategoryString(schoolCategory);
		header.setStatusFileCreated();
		header.setEventDate(now.getTimestamp());
		header.setEventStartTime(now.getTimestamp());
		header.setEventEndTime(null);
		header.store();
		// Get folder info from ExportMappingBean
		ExportDataMapping mapping = null;
		String fileFolder = null;
		String listFolder = null;
		try {
			mapping = getEconomaBusiness().getExportBusiness()
					.getExportDataMapping(schoolCategory);
			fileFolder = mapping.getFileCreationFolder();
			listFolder = mapping.getListCreationFolder();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}

		if (listFolder == null) {
			listFolder = fileFolder;
		}

		SchoolCategory childCare = null;
		try {
			childCare = getEconomaBusiness().getSchoolBusiness()
					.getCategoryChildcare();
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}

		SchoolCategory school = null;
		try {
			school = getEconomaBusiness().getSchoolBusiness()
					.getCategoryElementarySchool();
		} catch (RemoteException e3) {
			e3.printStackTrace();
		}

		/*
		 * SchoolCategory highSchool = null; try { highSchool =
		 * getEconomaBusiness().getSchoolBusiness().getCategoryHighSchool(); }
		 * catch (RemoteException e4) { e4.printStackTrace(); }
		 */

		StringBuffer fileName1 = null;
		StringBuffer fileName2 = null;
		StringBuffer fileName3 = null;
		/*
		 * StringBuffer fileName4 = null; StringBuffer fileName5 = null;
		 * StringBuffer fileName6 = null; StringBuffer fileName7 = null;
		 * StringBuffer fileName8 = null; StringBuffer fileName9 = null;
		 */
		if (childCare != null && (fileFolder != null || listFolder != null)
				&& school != null /* && highSchool != null */) {
			if (schoolCategory.equals(childCare.getPrimaryKey())) {
				if (fileFolder != null) {
					fileName1 = new StringBuffer(fileFolder);
					fileName1.append("AGUBEBO_");
					fileName2 = new StringBuffer(fileFolder);
					fileName2.append("AGUBOBO_");
					fileName3 = new StringBuffer(fileFolder);
					fileName3.append("AGUFABO_");
					fileName1.append(now.getDateString("yyMMdd"));
					fileName2.append(now.getDateString("yyMMdd"));
					fileName3.append(now.getDateString("yyMMdd"));
					fileName1.append(".txt");
					fileName2.append(".txt");
					fileName3.append(".txt");
				}
				/*
				 * if (listFolder != null) { fileName4 = new
				 * StringBuffer(listFolder);
				 * fileName4.append("n24_kontrollista_hvd_bom_"); fileName5 =
				 * new StringBuffer(listFolder);
				 * fileName5.append("n24_kontrollista_lev_bom_"); fileName6 =
				 * new StringBuffer(listFolder);
				 * fileName6.append("n24_attestlista_lev_bom_"); fileName7 = new
				 * StringBuffer(listFolder);
				 * fileName7.append("n24_attestlista_knd_bom_"); fileName8 = new
				 * StringBuffer(listFolder);
				 * fileName8.append("n24_avvikelselista_knd_bom_"); fileName9 =
				 * new StringBuffer(listFolder);
				 * fileName9.append("n24_kommun_bom_");
				 * fileName4.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName5.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName6.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName7.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName8.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName9.append(now.getDateString("yyMMdd_HHmm")); }
				 */
				/*
				 * try { createPaymentFiles(fileName1.toString(),
				 * fileName2.toString(), fileName4.toString(),
				 * fileName5.toString(), fileName6.toString(),
				 * fileName9.toString(), schoolCategory, now, paymentDate); }
				 * catch (IOException e5) { e5.printStackTrace(); }
				 */
				try {
					createInvoiceFiles(fileName3.toString(), schoolCategory,
							now, currentLocale, header);
				} catch (IOException e6) {
					e6.printStackTrace();
				}
			} else if (schoolCategory.equals(school.getPrimaryKey())) {
				if (fileFolder != null) {
					fileName1 = new StringBuffer(fileFolder);
					fileName1.append("AGUBEGSK_");
					fileName2 = new StringBuffer(fileFolder);
					fileName2.append("AGUBOGSK_");
					fileName1.append(now.getDateString("yyMMdd"));
					fileName2.append(now.getDateString("yyMMdd"));
					fileName1.append(".txt");
					fileName2.append(".txt");
				}
				/*
				 * if (listFolder != null) { fileName4 = new
				 * StringBuffer(listFolder);
				 * fileName4.append("n24_kontrollista_hvd_gsk_"); fileName5 =
				 * new StringBuffer(listFolder);
				 * fileName5.append("n24_kontrollista_lev_gsk_"); fileName6 =
				 * new StringBuffer(listFolder);
				 * fileName6.append("n24_attestlista_lev_gsk_"); fileName9 = new
				 * StringBuffer(listFolder);
				 * fileName9.append("n24_kommun_gsk_");
				 * fileName4.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName5.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName6.append(now.getDateString("yyMMdd_HHmm"));
				 * fileName9.append(now.getDateString("yyMMdd_HHmm")); }
				 */
				/*
				 * try { createPaymentFiles(fileName1.toString(),
				 * fileName2.toString(), fileName4.toString(),
				 * fileName5.toString(), fileName6.toString(),
				 * fileName9.toString(), schoolCategory, now, paymentDate); }
				 * catch (IOException e5) { e5.printStackTrace(); }
				 */
			}
			/*
			 * else if (schoolCategory.equals(highSchool.getPrimaryKey())) { if
			 * (fileFolder != null) { fileName1 = new StringBuffer(fileFolder);
			 * fileName1.append("n24_Economa_hvd_gym_"); fileName2 = new
			 * StringBuffer(fileFolder);
			 * fileName2.append("n24_Economa_lev_gym_");
			 * fileName1.append(now.getDateString("yyMMdd_HHmm"));
			 * fileName2.append(now.getDateString("yyMMdd_HHmm")); } if
			 * (listFolder != null) { fileName4 = new StringBuffer(listFolder);
			 * fileName4.append("n24_kontrollista_hvd_gym_"); fileName5 = new
			 * StringBuffer(listFolder);
			 * fileName5.append("n24_kontrollista_lev_gym_"); fileName6 = new
			 * StringBuffer(listFolder);
			 * fileName6.append("n24_attestlista_lev_gym_"); fileName9 = new
			 * StringBuffer(listFolder); fileName9.append("n24_kommun_gym_");
			 * fileName4.append(now.getDateString("yyMMdd_HHmm"));
			 * fileName5.append(now.getDateString("yyMMdd_HHmm"));
			 * fileName6.append(now.getDateString("yyMMdd_HHmm"));
			 * fileName9.append(now.getDateString("yyMMdd_HHmm")); } try {
			 * createPaymentFiles(fileName1.toString(), fileName2.toString(),
			 * fileName4.toString(), fileName5.toString(), fileName6.toString(),
			 * fileName9.toString(), schoolCategory, now, paymentDate); } catch
			 * (IOException e5) { e5.printStackTrace(); } }
			 */
		}
		now = IWTimestamp.RightNow();
		header.setEventEndTime(now.getTimestamp());
		header.store();
	}

	/*
	 * private void createPaymentFiles(String fileName1, String fileName2,
	 * String fileName3, String fileName4, String fileName5, String fileName6,
	 * String schoolCategory, IWTimestamp executionDate, IWTimestamp
	 * paymentDate) throws IOException { //String localizedSchoolCategoryName =
	 * iwac.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle( //
	 * currentLocale).getLocalizedString("school_category." + schoolCategory);
	 * Collection phInCommune = null; try { phInCommune = ((PaymentHeaderHome)
	 * IDOLookup.getHome(PaymentHeader.class)).findBySchoolCategoryStatusInCommuneWithCommunalManagement(
	 * schoolCategory, 'P'); } catch (IDOLookupException e) {
	 * e.printStackTrace(); } catch (FinderException e) { e.printStackTrace(); }
	 * Collection phOutsideCommune = null; try { phOutsideCommune =
	 * ((PaymentHeaderHome)
	 * IDOLookup.getHome(PaymentHeader.class)).findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(
	 * schoolCategory, 'P'); } catch (IDOLookupException e1) {
	 * e1.printStackTrace(); } catch (FinderException e1) {
	 * e1.printStackTrace(); } if (phInCommune != null &&
	 * !phInCommune.isEmpty()) { Collection rec = null; try { rec =
	 * ((PaymentRecordHome)
	 * IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeaders(phInCommune); }
	 * catch (IDOLookupException e2) { e2.printStackTrace(); } catch
	 * (FinderException e2) { e2.printStackTrace(); } // try { //
	 * excelFileUtil.createPaymentFilesExcel(rec, fileName3 + ".xls",
	 * "Checkutbetalning " + localizedSchoolCategoryName // + ", egna kommunala
	 * anordnare, " + executionDate.getDateString("yyyy-MM-dd"), //
	 * EconomaCreateExcelFileUtil.FILE_TYPE_DOUBLE_POSTING); //
	 * excelFileUtil.createPaymentFilesExcel(rec, fileName6 + ".xls",
	 * "Utbetalningsattestlista " // + localizedSchoolCategoryName + ", egna
	 * kommunala anordnare, " // + executionDate.getDateString("yyyy-MM-dd"),
	 * EconomaCreateExcelFileUtil.FILE_TYPE_KOMMUN); // } // catch (IOException
	 * e3) { // e3.printStackTrace(); // } // Collection phAll = new
	 * ArrayList(); // phAll.addAll(phOutsideCommune); //
	 * phAll.addAll(phInCommune); // try { //
	 * excelFileUtil.createPaymentSigningFilesExcel(phAll, fileName5 + ".xls",
	 * "Utbetalningsattestlista " // + localizedSchoolCategoryName + ", " +
	 * executionDate.getDateString("yyyy-MM-dd")); // } // catch (IOException
	 * e3) { // e3.printStackTrace(); // } // catch (FinderException e3) { //
	 * e3.printStackTrace(); // } Iterator it = rec.iterator(); FileWriter
	 * writer = null; try { writer = new FileWriter(fileName1); } catch
	 * (IOException e4) { e4.printStackTrace(); } BufferedWriter bWriter = new
	 * BufferedWriter(writer); PostingBusiness pb =
	 * getEconomaBusiness().getPostingBusiness(); while (it.hasNext()) {
	 * PaymentRecord pRec = (PaymentRecord) it.next(); if (pRec.getTotalAmount() !=
	 * 0.0f) { bWriter.write(";");
	 * bWriter.write(executionDate.getDateString("yyyy")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Konto"));
	 * bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),
	 * "Ansvar")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),
	 * "Resurs")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),
	 * "Verksamhet")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),
	 * "Aktivitet")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),
	 * "Projekt")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),
	 * "Objekt")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),
	 * "Motpart")); bWriter.write(";"); // anlaggningsnummer bWriter.write(";"); //
	 * internranta bWriter.write(";"); bWriter.write("SEK"); bWriter.write(";"); //
	 * empty bWriter.write(";"); // empty bWriter.write(";");
	 * bWriter.write(Integer.toString(Math.round(pRec.getTotalAmount())));
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";");
	 * bWriter.write(Integer.toString(Math.round(pRec.getTotalAmount())));
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); bWriter.write(executionDate.getDateString("yyMM") + " " +
	 * pRec.getPaymentText()); bWriter.write(";"); // empty bWriter.write(";"); //
	 * empty bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";");
	 * bWriter.write(paymentDate.getDateString("yyyy-MM-dd"));
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); bWriter.newLine(); bWriter.write(";");
	 * bWriter.write(executionDate.getDateString("yyyy")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Konto")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Ansvar")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Resurs")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Verksamhet")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Aktivitet")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Projekt")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Objekt")); bWriter.write(";");
	 * bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),
	 * "Motpart")); bWriter.write(";"); // anlaggningsnummer bWriter.write(";"); //
	 * internranta bWriter.write(";"); bWriter.write("SEK"); bWriter.write(";"); //
	 * empty bWriter.write(";"); // empty bWriter.write(";");
	 * bWriter.write(Integer.toString(Math.round(-pRec.getTotalAmount())));
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";");
	 * bWriter.write(Integer.toString(Math.round(-pRec.getTotalAmount())));
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); bWriter.write(executionDate.getDateString("yyMM") + " " +
	 * pRec.getPaymentText()); bWriter.write(";"); // empty bWriter.write(";"); //
	 * empty bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";");
	 * bWriter.write(paymentDate.getDateString("yyyy-MM-dd"));
	 * bWriter.write(";"); // empty bWriter.write(";"); // empty
	 * bWriter.write(";"); bWriter.newLine(); } pRec.setStatus('L');
	 * pRec.store(); } bWriter.close(); Iterator itor = phInCommune.iterator();
	 * while (itor.hasNext()) { PaymentHeader head = (PaymentHeader)
	 * itor.next(); head.setStatus('L'); head.store(); } } if (phOutsideCommune !=
	 * null && !phOutsideCommune.isEmpty()) { NumberFormat format =
	 * NumberFormat.getInstance(currentLocale);
	 * format.setMaximumFractionDigits(2); format.setMinimumFractionDigits(2);
	 * format.setMinimumIntegerDigits(1); format.setGroupingUsed(false); //
	 * format. // Collection recOutside = null; // try { // recOutside =
	 * ((PaymentRecordHome)
	 * IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeaders(phOutsideCommune); // } //
	 * catch (IDOLookupException e2) { // e2.printStackTrace(); // } // catch
	 * (FinderException e2) { // e2.printStackTrace(); // } // try { //
	 * excelFileUtil.createPaymentFilesExcel(recOutside, fileName4 + ".xls",
	 * "Checkutbetalning " // + localizedSchoolCategoryName + ", övriga
	 * anordnare, " // + executionDate.getDateString("yyyy-MM-dd"),
	 * EconomaCreateExcelFileUtil.FILE_TYPE_OWN_POSTING); // } // catch
	 * (IOException e3) { // e3.printStackTrace(); // } // catch (Exception e3) { //
	 * e3.printStackTrace(); // } FileWriter writer = new FileWriter(fileName2);
	 * BufferedWriter bWriter = new BufferedWriter(writer); PostingBusiness pb =
	 * getEconomaBusiness().getPostingBusiness(); ProviderBusiness provBiz =
	 * getEconomaBusiness().getProviderBusiness(); Iterator phIt =
	 * phOutsideCommune.iterator(); while (phIt.hasNext()) { PaymentHeader pHead =
	 * (PaymentHeader) phIt.next(); School school = pHead.getSchool(); Provider
	 * prov = null; try { prov = provBiz.getProvider(pHead.getSchoolID()); }
	 * catch (RemoteException e4) { e4.printStackTrace(); } Collection rec =
	 * null; try { rec = ((PaymentRecordHome)
	 * IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeader(pHead); }
	 * catch (IDOLookupException e3) { e3.printStackTrace(); } catch
	 * (FinderException e3) { e3.printStackTrace(); } if (rec != null &&
	 * !rec.isEmpty()) { Iterator prIt = rec.iterator(); Iterator sumIt =
	 * rec.iterator(); long sum = 0; while (sumIt.hasNext()) { PaymentRecord r =
	 * (PaymentRecord) sumIt.next(); sum +=
	 * AccountingUtil.roundAmount(r.getTotalAmount()); } String giro =
	 * prov.getAccountingProperties().getBankgiro(); if (giro == null) giro =
	 * prov.getAccountingProperties().getPostgiro(); if (giro == null) giro =
	 * ""; bWriter.write("H"); bWriter.write(";"); bWriter.write(giro);
	 * bWriter.write(";"); bWriter.write(((Integer)
	 * pHead.getPrimaryKey()).toString()); bWriter.write(";");
	 * bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
	 * bWriter.write(";"); bWriter.write("SUPPEXT"); bWriter.write(";");
	 * bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
	 * bWriter.write(";");
	 * bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
	 * bWriter.write(";");
	 * bWriter.write(paymentDate.getDateString("yyyy-MM-dd"));
	 * bWriter.write(";");
	 * bWriter.write(Integer.toString(AccountingUtil.getDayDiff(executionDate,
	 * paymentDate) + 1)); bWriter.write(";"); bWriter.write("SEK");
	 * bWriter.write(";"); // empty bWriter.write("-"); bWriter.write(";");
	 * bWriter.write("*"); bWriter.write(";"); for (int i = 13; i < 55; i++)
	 * bWriter.write("-;"); bWriter.write("-"); bWriter.newLine();
	 * bWriter.write("I"); bWriter.write(";"); bWriter.write(giro);
	 * bWriter.write(";"); bWriter.write(((Integer)
	 * pHead.getPrimaryKey()).toString()); bWriter.write(";");
	 * bWriter.write("1"); bWriter.write(";"); // VAT code, changed L6 to 11...
	 * bWriter.write("11"); bWriter.write(";");
	 * bWriter.write(format.format(sum)); bWriter.write(";");
	 * bWriter.write(format.format(sum)); bWriter.write(";");
	 * bWriter.write("0,00"); bWriter.write(";"); bWriter.write("0,00");
	 * bWriter.write(";"); for (int i = 10; i < 23; i++) bWriter.write("-;");
	 * bWriter.write("-"); bWriter.newLine(); int row = 1; while
	 * (prIt.hasNext()) { PaymentRecord pRec = (PaymentRecord) prIt.next(); if
	 * (pRec.getTotalAmount() != 0.0f) { bWriter.write("P"); bWriter.write(";");
	 * bWriter.write(giro); bWriter.write(";"); bWriter.write(((Integer)
	 * pHead.getPrimaryKey()).toString()); bWriter.write(";");
	 * bWriter.write("1"); bWriter.write(";");
	 * bWriter.write(Integer.toString(row)); row++; bWriter.write(";"); String
	 * tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Konto"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); tmp =
	 * pb.findFieldInStringByName(pRec.getOwnPosting(), "Ansvar"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); tmp =
	 * pb.findFieldInStringByName(pRec.getOwnPosting(), "Resurs"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); tmp =
	 * pb.findFieldInStringByName(pRec.getOwnPosting(), "Verksamhet"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); tmp =
	 * pb.findFieldInStringByName(pRec.getOwnPosting(), "Aktivitet"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); tmp =
	 * pb.findFieldInStringByName(pRec.getOwnPosting(), "Projekt"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); tmp =
	 * pb.findFieldInStringByName(pRec.getOwnPosting(), "Objekt"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); tmp =
	 * pb.findFieldInStringByName(pRec.getOwnPosting(), "Motpart"); if
	 * ("".equals(tmp)) bWriter.write("-"); else bWriter.write(tmp);
	 * bWriter.write(";"); // anlaggningsnummer bWriter.write("-");
	 * bWriter.write(";"); // internranta bWriter.write("-");
	 * bWriter.write(";"); // empty bWriter.write("-"); bWriter.write(";");
	 * bWriter.write(format.format(AccountingUtil.roundAmount(pRec.getTotalAmount())));
	 * bWriter.write(";");
	 * bWriter.write(format.format(AccountingUtil.roundAmount(pRec.getTotalAmount())));
	 * bWriter.write(";"); // empty bWriter.write("-"); bWriter.write(";"); //
	 * empty bWriter.write("-"); bWriter.write(";");
	 * bWriter.write(school.getName() + ", " + pRec.getPaymentText());
	 * bWriter.write(";"); bWriter.write("-"); bWriter.newLine(); }
	 * pRec.setStatus('L'); pRec.store(); } } pHead.setStatus('L');
	 * pHead.store(); } bWriter.close(); } }
	 */
	private void createInvoiceFiles(String fileName1, String schoolCategory,
			IWTimestamp executionDate, Locale currentLocale,
			EconomaCheckHeader checkHeader) throws IOException {
		Collection iHeaders = null;
		try {
			iHeaders = ((InvoiceHeaderHome) IDOLookup
					.getHome(InvoiceHeader.class)).findByStatusAndCategory("P",
					schoolCategory);
		} catch (IDOLookupException e2) {
			e2.printStackTrace();
		} catch (FinderException e2) {
			e2.printStackTrace();
		}

		StringBuffer empty = new StringBuffer("");
		for (int i = 0; i < 25; i++) {
			empty.append("          ");
		}

		if (iHeaders != null && !iHeaders.isEmpty()) {
			NumberFormat format = NumberFormat.getInstance(currentLocale);
			format.setMaximumFractionDigits(0);
			format.setMinimumFractionDigits(0);
			format.setMinimumIntegerDigits(13);
			format.setMaximumIntegerDigits(13);
			format.setGroupingUsed(false);

			NumberFormat format2 = NumberFormat.getInstance(currentLocale);
			format2.setMaximumFractionDigits(0);
			format2.setMinimumFractionDigits(0);
			format2.setMinimumIntegerDigits(5);
			format2.setMaximumIntegerDigits(5);
			format2.setGroupingUsed(false);

			NumberFormat format3 = NumberFormat.getInstance(currentLocale);
			format3.setMaximumFractionDigits(0);
			format3.setMinimumFractionDigits(0);
			format3.setMinimumIntegerDigits(3);
			format3.setMaximumIntegerDigits(3);
			format3.setGroupingUsed(false);

			NumberFormat format4 = NumberFormat.getInstance(currentLocale);
			format4.setMaximumFractionDigits(0);
			format4.setMinimumFractionDigits(0);
			format4.setMinimumIntegerDigits(15);
			format4.setMaximumIntegerDigits(15);
			format4.setGroupingUsed(false);

			FileWriter writer = new FileWriter(fileName1);
			BufferedWriter bWriter = new BufferedWriter(writer);
			// Posttyp
			bWriter.write("10");
			// Extftg
			bWriter.write("01602");
			// Rutinkod
			bWriter.write("SBO");
			// Ordernr
			bWriter.write("00000");
			// Vsamhkod
			bWriter.write(empty.substring(0, 2));
			// Radnr
			bWriter.write("000");
			// Radnrtext
			bWriter.write("000");
			// Framdat
			bWriter.write(executionDate.getDateString("yyyyMMdd"));
			// Framkloc
			bWriter.write(executionDate.getDateString("HHmmss"));
			// Faktdat
			bWriter.write(empty.substring(0, 8));
			// Forfdat
			bWriter.write(empty.substring(0, 8));
			// Bokfdat
			bWriter.write(empty.substring(0, 8));
			// Motrutin
			bWriter.write("EZF");
			// Filler
			bWriter.write(empty.substring(0, 228));
			bWriter.newLine();

			Iterator ihIt = iHeaders.iterator();
			int type30counter = 1;
			int type40counter = 1;
			int count30type = 0;
			int count50type = 0;
			long totalsum = 0;

			while (ihIt.hasNext()) {
				InvoiceHeader iHead = (InvoiceHeader) ihIt.next();

				Collection rec = null;
				try {
					rec = ((InvoiceRecordHome) IDOLookup
							.getHome(InvoiceRecord.class))
							.findByInvoiceHeader(iHead);
				} catch (IDOLookupException e3) {
					e3.printStackTrace();
				} catch (FinderException e3) {
					e3.printStackTrace();
				}

				boolean createInvoice = false;
				Iterator it2 = rec.iterator();
				while (it2.hasNext() && !createInvoice) {
					InvoiceRecord r2 = (InvoiceRecord) it2.next();
					Provider provider = getEconomaBusiness()
							.getProviderBusiness().getProvider(
									r2.getProviderId());
					if (provider.getAccountingProperties()
							.getCreateInvoiceRecord()) {
						createInvoice = true;
					}
				}

				try {
					User custodian = iHead.getCustodian();
					if (custodian == null) {
						throw new MissingCustodianException(
								"Economa_missing_custodian",
								"Missing custodian");
					}
					Address mainAddress = getEconomaBusiness()
							.getUserBusiness().getUsersMainAddress(
									iHead.getCustodian());
					if (mainAddress == null) {
						throw new MissingAddressException(
								"Economa_missing_address", "Missing address");
					}
					PostalCode poCode = mainAddress.getPostalCode();
					if (poCode == null) {
						throw new MissingPostalCodeException(
								"Economa_missing_postalcode",
								"Missing postalcode");
					}
					if (rec != null && !rec.isEmpty() && createInvoice) {

						// Posttyp
						bWriter.write("30");
						// Extftg
						bWriter.write("01602");
						// Rutinkod
						bWriter.write("SBO");
						// orderno
						String type30counterString = format2
								.format(type30counter);
						bWriter.write(type30counterString);
						// vsamhkod
						bWriter.write(empty.substring(0, 2));
						// radnr
						bWriter.write("000");
						// radnrtxt
						bWriter.write("000");
						// faktgrp
						bWriter.write("20");
						// faktlpnr
						bWriter.write(empty.substring(0, 10));
						// exknr
						bWriter.write(empty.substring(0, 16));
						// personnr
						String pnr = custodian.getPersonalID();
						if (pnr.length() < 12) {
							StringBuffer p = new StringBuffer(pnr);
							while (p.length() < 12)
								p.insert(0, ' ');
							pnr = p.toString();
						} else if (pnr.length() > 12) {
							pnr = pnr.substring(0, 12);
						}
						bWriter.write(pnr);
						// kundnr
						bWriter.write(empty.substring(0, 8));
						// namn
						String name = custodian.getLastName() + " "
								+ custodian.getFirstName();
						if (name.length() < 36) {
							StringBuffer p = new StringBuffer(name);
							while (p.length() < 36)
								p.append(' ');
							name = p.toString();
						} else if (name.length() > 36) {
							name = name.substring(0, 36);
						}
						bWriter.write(name);
						// adress
						String address = mainAddress.getStreetAddress();
						if (address.length() < 27) {
							StringBuffer p = new StringBuffer(address);
							while (p.length() < 27)
								p.append(' ');
							address = p.toString();
						} else if (address.length() > 27) {
							address = address.substring(0, 27);
						}
						bWriter.write(address);
						// postnr
						String po = poCode.getPostalCode();
						if (po.length() < 8) {
							StringBuffer p = new StringBuffer(po);
							while (p.length() < 8)
								p.append(' ');
							po = p.toString();
						} else if (po.length() > 8) {
							po = po.substring(0, 8);
						}
						bWriter.write(po);
						// ort
						String poName = poCode.getName();
						if (poName.length() < 18) {
							StringBuffer p = new StringBuffer(poName);
							while (p.length() < 18)
								p.append(' ');
							poName = p.toString();
						} else if (poName.length() > 18) {
							poName = poName.substring(0, 18);
						}
						bWriter.write(poName);
						// Landskod
						bWriter.write(empty.substring(0, 3));
						// conamn1
						bWriter.write(empty.substring(0, 36));
						// conamn2
						bWriter.write(empty.substring(0, 36));
						// utskrtyp
						bWriter.write("P ");
						// Filler
						bWriter.write(empty.substring(0, 55));
						type30counter++;
						count30type++;
						bWriter.newLine();

						// Posttype
						bWriter.write("40");
						// Extftg
						bWriter.write("01602");
						// Rutinkod
						bWriter.write("SBO");
						// orderno
						bWriter.write(type30counterString);
						// vsamhkod
						bWriter.write(empty.substring(0, 2));
						// radnr
						bWriter.write("000");
						// radnrtxt
						String type40counterString = format3
								.format(type40counter);
						bWriter.write(type40counterString);
						// texttyp
						bWriter.write("  O");
						// text
						if (periodText.length() < 60) {
							StringBuffer p = new StringBuffer(periodText);
							while (p.length() < 60)
								p.append(' ');

							periodText = p.toString();
						} else if (periodText.length() > 60) {
							periodText = periodText.substring(0, 60);
						}
						bWriter.write(periodText);
						// Filler
						bWriter.write(empty.substring(0, 206));
						type40counter++;
						bWriter.newLine();

						Iterator irIt = rec.iterator();

						int type50counter = 1;
						HashMap map = new HashMap();
						HashMap regular = new HashMap();
						while (irIt.hasNext()) {
							InvoiceRecord iRec = (InvoiceRecord) irIt.next();

							if (iRec.getAmount() != 0.0f) {
								if (iRec.getChildCareContract() != null) {
									if (map.containsKey(iRec
											.getChildCareContract()
											.getPrimaryKey())) {
										InvoiceRecord r = (InvoiceRecord) map
												.get(iRec
														.getChildCareContract()
														.getPrimaryKey());
										r.setAmount(r.getAmount()
												+ iRec.getAmount());
										map.put(iRec.getChildCareContract()
												.getPrimaryKey(), r);
									} else {
										map.put(iRec.getChildCareContract()
												.getPrimaryKey(), iRec);
									}
								} else {
									if (regular.containsKey(iRec.getSchoolClassMember().getPrimaryKey())) {
										ArrayList regList = (ArrayList) regular.get(iRec.getSchoolClassMember().getPrimaryKey());
										regList.add(iRec);
										regular.put(iRec.getSchoolClassMember().getPrimaryKey(), regList);
									} else {
										ArrayList regList = new ArrayList();
										regList.add(iRec);
										regular.put(iRec.getSchoolClassMember().getPrimaryKey(), regList);										
									}
								}
							}
						}

						Iterator recordIterator = map.keySet().iterator();
						while (recordIterator.hasNext()) {
							InvoiceRecord r = (InvoiceRecord) map
									.get(recordIterator.next());

							if (r.getAmount() != 0.0f) {
								// Posttype
								bWriter.write("50");
								// Extftg
								bWriter.write("01602");
								// Rutinkod
								bWriter.write("SBO");
								// orderno
								bWriter.write(type30counterString);
								// vsamhkod
								bWriter.write("01");
								// radnr
								String type50counterString = format3
										.format(type50counter);
								bWriter.write(type50counterString);
								// radnrtxt
								bWriter.write("000");
								// fomdat
								IWTimestamp t = new IWTimestamp(r
										.getPeriodStartCheck());
								bWriter.write(t.getDateString("yyyyMMdd"));
								// tomdat
								t = new IWTimestamp(r.getPeriodEndCheck());
								bWriter.write(t.getDateString("yyyyMMdd"));
								// artnr
								bWriter.write("INGEN-MOMS   ");
								// benamn
								String text = r.getInvoiceText();
								if (text == null) {
									text = "";
								} else {
									String start = text.substring(0, 6);
									if ("Check ".equals(start)) {
										String localized = iwac.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(currentLocale).getLocalizedString("Check", "Check");
										text = localized + text.substring(5);
									}
								}
								if (text.length() < 30) {
									StringBuffer tb = new StringBuffer(text);
									while (tb.length() < 30) {
										tb.append(' ');
									}
									text = tb.toString();
								} else if (text.length() > 30) {
									text = text.substring(0, 30);
								}
								bWriter.write(text);
								// avsrnamn
								bWriter.write(empty.substring(0, 30));
								// konto
								StringBuffer postingString = new StringBuffer(r
										.getOwnPosting());
								while (postingString.length() < 45) {
									postingString.append(' ');
								}
								bWriter.write(postingString.toString());
								// ktonamn
								bWriter.write(empty.substring(0, 30));
								// signata
								bWriter.write(empty.substring(0, 1));
								// antal
								bWriter.write("00000000000");
								// sort
								bWriter.write(empty.substring(0, 6));
								// avsrper
								bWriter.write(empty.substring(0, 13));
								// signapris
								bWriter.write(empty.substring(0, 1));
								// apris
								bWriter.write("0000000000000");
								// signrabs
								bWriter.write(empty.substring(0, 1));
								// rabsats
								bWriter.write("00000");
								// signbel
								long am = AccountingUtil.roundAmount(r
										.getAmount());
								boolean isNegative = false;
								if (am < 0) {
									isNegative = true;
									bWriter.write("-");
								} else {
									bWriter.write("+");
								}
								// Belopp
								am = Math.abs(am * 100);
								String amount = format.format(am);
								bWriter.write(amount);
								// signmsat
								bWriter.write(empty.substring(0, 1));
								// momssats
								bWriter.write("0000");
								// signmbel
								bWriter.write(empty.substring(0, 1));
								// momsbel
								bWriter.write("0000000000000");
								// omrade
								bWriter.write(empty.substring(0, 3));
								// kategori
								bWriter.write(empty.substring(0, 3));
								// Filler
								bWriter.write(empty.substring(0, 15));

								if (!isNegative)
									totalsum += am;
								else
									totalsum -= am;
								count50type++;
								type50counter++;
								bWriter.newLine();

								// posttyp
								bWriter.write("60");
								// extftg
								bWriter.write("01602");
								// rutinkod
								bWriter.write("SBO");
								// orderno
								bWriter.write(type30counterString);
								// vsamhkod
								bWriter.write("01");
								// radnr
								bWriter.write(type50counterString);
								// radnrtxt
								bWriter.write("001");
								// texttype
								bWriter.write("  U");
								// text
								text = r.getInvoiceText2();
								if (text == null)
									text = "";
								if (text.length() < 60) {
									StringBuffer tb = new StringBuffer(text);
									while (tb.length() < 60) {
										tb.append(' ');
									}
									text = tb.toString();
								} else if (text.length() > 60) {
									text = text.substring(0, 60);
								}
								bWriter.write(text);
								// filler
								bWriter.write(empty.substring(0, 206));
								bWriter.newLine();
								
								if (regular.containsKey(r.getSchoolClassMember().getPrimaryKey())) {
									ArrayList regList = (ArrayList) regular.get(r.getSchoolClassMember().getPrimaryKey());
									Iterator regit = regList.iterator();
									while (regit.hasNext()) {
										InvoiceRecord regRecord = (InvoiceRecord) regit.next();
										
									
										if (regRecord.getAmount() != 0.0f) {
											// Posttype
											bWriter.write("50");
											// Extftg
											bWriter.write("01602");
											// Rutinkod
											bWriter.write("SBO");
											// orderno
											bWriter.write(type30counterString);
											// vsamhkod
											bWriter.write("01");
											// radnr
											bWriter.write(type50counterString);
											// radnrtxt
											bWriter.write("000");
											// fomdat
											t = new IWTimestamp(regRecord
													.getPeriodStartCheck());
											bWriter.write(t.getDateString("yyyyMMdd"));
											// tomdat
											t = new IWTimestamp(regRecord.getPeriodEndCheck());
											bWriter.write(t.getDateString("yyyyMMdd"));
											// artnr
											bWriter.write("INGEN-MOMS   ");
											// benamn
											text = regRecord.getInvoiceText();
											if (text == null)
												text = "";
											if (text.length() < 30) {
												StringBuffer tb = new StringBuffer(text);
												while (tb.length() < 30) {
													tb.append(' ');
												}
												text = tb.toString();
											} else if (text.length() > 30) {
												text = text.substring(0, 30);
											}
											bWriter.write(text);
											// avsrnamn
											bWriter.write(empty.substring(0, 30));
											// konto
											postingString = new StringBuffer(regRecord
													.getOwnPosting());
											while (postingString.length() < 45) {
												postingString.append(' ');
											}
											bWriter.write(postingString.toString());
											// ktonamn
											bWriter.write(empty.substring(0, 30));
											// signata
											bWriter.write(empty.substring(0, 1));
											// antal
											bWriter.write("00000000000");
											// sort
											bWriter.write(empty.substring(0, 6));
											// avsrper
											bWriter.write(empty.substring(0, 13));
											// signapris
											bWriter.write(empty.substring(0, 1));
											// apris
											bWriter.write("0000000000000");
											// signrabs
											bWriter.write(empty.substring(0, 1));
											// rabsats
											bWriter.write("00000");
											// signbel
											am = AccountingUtil.roundAmount(regRecord
													.getAmount());
											isNegative = false;
											if (am < 0) {
												isNegative = true;
												bWriter.write("-");
											} else {
												bWriter.write("+");
											}
											// Belopp
											am = Math.abs(am * 100);
											amount = format.format(am);
											bWriter.write(amount);
											// signmsat
											bWriter.write(empty.substring(0, 1));
											// momssats
											bWriter.write("0000");
											// signmbel
											bWriter.write(empty.substring(0, 1));
											// momsbel
											bWriter.write("0000000000000");
											// omrade
											bWriter.write(empty.substring(0, 3));
											// kategori
											bWriter.write(empty.substring(0, 3));
											// Filler
											bWriter.write(empty.substring(0, 15));

											if (!isNegative)
												totalsum += am;
											else
												totalsum -= am;
											count50type++;
											type50counter++;
											bWriter.newLine();

											// posttyp
											bWriter.write("60");
											// extftg
											bWriter.write("01602");
											// rutinkod
											bWriter.write("SBO");
											// orderno
											bWriter.write(type30counterString);
											// vsamhkod
											bWriter.write("01");
											// radnr
											bWriter.write(type50counterString);
											// radnrtxt
											bWriter.write("001");
											// texttype
											bWriter.write("  U");
											// text
											text = regRecord.getInvoiceText2();
											if (text == null)
												text = "";
											if (text.length() < 60) {
												StringBuffer tb = new StringBuffer(text);
												while (tb.length() < 60) {
													tb.append(' ');
												}
												text = tb.toString();
											} else if (text.length() > 60) {
												text = text.substring(0, 60);
											}
											bWriter.write(text);
											// filler
											bWriter.write(empty.substring(0, 206));
											bWriter.newLine();
										}
										
									}
								}
							}
						}
					}
					iHead.setStatus('L');
					iHead.store();
				} catch (MissingCustodianException e) {
					EconomaCheckRecordHome home = getEconomaCheckRecordHome();
					if (home != null) {
						try {
							EconomaCheckRecord Economa_rec = home.create();
							Economa_rec.setHeader(checkHeader);
							Economa_rec.setError(e.getTextKey());
							Economa_rec.setErrorConcerns("Faktura "
									+ ((Integer) iHead.getPrimaryKey())
											.toString());
							Economa_rec.store();
						} catch (CreateException e1) {
							e1.printStackTrace();
						}
					}
				} catch (MissingAddressException e) {
					EconomaCheckRecordHome home = getEconomaCheckRecordHome();
					if (home != null) {
						try {
							EconomaCheckRecord Economa_rec = home.create();
							Economa_rec.setHeader(checkHeader);
							Economa_rec.setError(e.getTextKey());
							Economa_rec.setErrorConcerns("Fakturamottagare "
									+ iHead.getCustodian().getPersonalID());
							Economa_rec.store();
						} catch (CreateException e1) {
							e1.printStackTrace();
						}
					}
				} catch (MissingPostalCodeException e) {
					EconomaCheckRecordHome home = getEconomaCheckRecordHome();
					if (home != null) {
						try {
							EconomaCheckRecord Economa_rec = home.create();
							Economa_rec.setHeader(checkHeader);
							Economa_rec.setError(e.getTextKey());
							Economa_rec.setErrorConcerns("Fakturamottagare "
									+ iHead.getCustodian().getPersonalID());
							Economa_rec.store();
						} catch (CreateException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			// Posttyp
			bWriter.write("90");
			//
			bWriter.write("01602");
			// rutinkod
			bWriter.write("SBO");
			// ordernr
			bWriter.write("99999");
			// vsamhkod
			bWriter.write(empty.substring(0, 2));
			// radnr
			bWriter.write("000");
			// radnrtxt
			bWriter.write("000");
			// antframo
			bWriter.write(format2.format(count30type));
			// antframr
			bWriter.write(format2.format(count50type));
			// signbel
			if (totalsum < 0) {
				bWriter.write("-");
			} else {
				bWriter.write("+");
			}
			// Belopp
			totalsum = Math.abs(totalsum);
			String amount = format4.format(totalsum);
			bWriter.write(amount);
			// Filler
			bWriter.write(empty.substring(0, 243));
			bWriter.newLine();
			bWriter.close();
		}
	}

	private EconomaBusiness getEconomaBusiness() {
		try {
			return (EconomaBusiness) IBOLookup.getServiceInstance(iwac,
					EconomaBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private EconomaCheckRecordHome getEconomaCheckRecordHome() {
		try {
			return (EconomaCheckRecordHome) IDOLookup
					.getHome(EconomaCheckRecord.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		return null;
	}

}