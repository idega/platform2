/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package se.idega.idegaweb.commune.accounting.export.ifs.business;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.accounting.export.business.ExportBusiness;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeaderHome;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckRecord;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckRecordHome;
import se.idega.idegaweb.commune.accounting.export.ifs.data.JournalLog;
import se.idega.idegaweb.commune.accounting.export.ifs.data.JournalLogHome;
import se.idega.idegaweb.commune.accounting.invoice.business.CheckAmountBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.school.business.ProviderBusiness;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathException;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class IFSBusinessBean extends IBOServiceBean implements IFSBusiness {
	/**
	 * Returns all <code>JournalLog</code> data beans from the database.
	 */
	public Collection getJournalLog() {
		Collection col = new Vector();
		try {
			col = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).findAll();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns all <code>JournalLog</code> data beans from the database for the
	 * given category.
	 * 
	 * @param category
	 *          A school category primary key (String)
	 * 
	 * @return A collection of JournalLog beans
	 */
	public Collection getJournalLogBySchoolCategory(String category) {
		Collection col = new Vector();
		try {
			col = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).findAllBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns all <code>JournalLog</code> data beans from the database for the
	 * given category.
	 * 
	 * @param category
	 *          A school category bean
	 * 
	 * @return A collection of JournalLog beans
	 */
	public Collection getJournalLogBySchoolCategory(SchoolCategory category) {
		Collection col = new Vector();
		try {
			col = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).findAllBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns the <code>IFSCheckHeader</code> data bean from the database for
	 * the given category.
	 * 
	 * @param category
	 *          A school category primary key (String)
	 * 
	 * @return A IFSCheckHeader bean
	 */
	public IFSCheckHeader getIFSCheckHeaderBySchoolCategory(String category) {
		IFSCheckHeader header = null;
		try {
			header = ((IFSCheckHeaderHome) IDOLookup.getHome(IFSCheckHeader.class)).findBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return header;
	}

	/**
	 * Returns the <code>IFSCheckHeader/code> data bean from the database for the given category.
	 * 
	 * @param category A school category bean
	 * 
	 * @return A IFSCheckHeader bean
	 */
	public IFSCheckHeader getIFSCheckHeaderBySchoolCategory(SchoolCategory category) {
		IFSCheckHeader header = null;
		try {
			header = ((IFSCheckHeaderHome) IDOLookup.getHome(IFSCheckHeader.class)).findBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return header;
	}

	/**
	 * Returns a collection of <code>IFSCheckRecord</code> data beans from the
	 * database for the given header.
	 * 
	 * @param headerId
	 *          The primary key id for the header the records belong to.
	 * 
	 * @return A Collection of IFSCheckRecord beans
	 */
	public Collection getIFSCheckRecordByHeaderId(int headerId) {
		Collection col = new Vector();
		try {
			col = ((IFSCheckRecordHome) IDOLookup.getHome(IFSCheckRecord.class)).findAllByHeaderId(headerId);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * A method to create the files to be sent to the IFS system and updates the
	 * status of PaymentHeaders/InvoiceHeaders from P to L.
	 * 
	 * @param schoolCategory
	 *          The string primary key representing the school category we are
	 *          creating files for.
	 * @param paymentDate
	 *          The date the payment is supposed to be done on.
	 * @param periodText
	 *          A text to be inserted on the customers bill/providers payment
	 *          slip.
	 * 
	 * @author palli
	 */
	public void createFiles(String schoolCategory, IWTimestamp paymentDate, String periodText, User user, Locale currentLocale) {
		UserTransaction trans = null;

		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();

			IWTimestamp now = IWTimestamp.RightNow();
			JournalLog log = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileCreated();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();

			IFSCheckHeader header = getIFSCheckHeaderBySchoolCategory(schoolCategory);
			if (header == null) {
				header = ((IFSCheckHeaderHome) IDOLookup.getHome(IFSCheckHeader.class)).create();
			}
			else {
				Collection col = this.getIFSCheckRecordByHeaderId(((Integer) header.getPrimaryKey()).intValue());
				if (col != null && !col.isEmpty()) {
					Iterator it = col.iterator();
					while (it.hasNext()) {
						IFSCheckRecord rec = (IFSCheckRecord) it.next();
						rec.remove();
					}
				}
			}
			header.setSchoolCategoryString(schoolCategory);
			header.setStatusFileCreated();
			header.setEventDate(now.getTimestamp());
			header.store();

			//Create files in folder A. Must get folder info from
			// ExportMappingBean!!!
			ExportDataMapping mapping = getExportBusiness().getExportDataMapping(schoolCategory);
			String folder = mapping.getFileCreationFolder();
			SchoolCategory childCare = getSchoolBusiness().getCategoryChildcare();
			SchoolCategory school = getSchoolBusiness().getCategoryElementarySchool();
			SchoolCategory highSchool = getSchoolBusiness().getCategoryHighSchool();

			if (schoolCategory.equals(childCare.getPrimaryKey())) {
				StringBuffer fileName1 = new StringBuffer(folder);
				fileName1.append("n24_ifs_hvd_bom_");
				StringBuffer fileName2 = new StringBuffer(folder);
				fileName2.append("n24_ifs_lev_bom_");
				StringBuffer fileName3 = new StringBuffer(folder);
				fileName3.append("n24_ifs_knd_bom_");
				fileName1.append(now.getDateString("yyMMdd_HHmm"));
				fileName2.append(now.getDateString("yyMMdd_HHmm"));
				fileName3.append(now.getDateString("yyMMdd_HHmm"));

				createPaymentFiles(fileName1.toString(), fileName2.toString(), schoolCategory, now, paymentDate, currentLocale);
				createInvoiceFiles(fileName3.toString(), schoolCategory, now, currentLocale, periodText);
			}
			else if (schoolCategory.equals(school.getPrimaryKey())) {
				StringBuffer fileName1 = new StringBuffer(folder);
				fileName1.append("n24_ifs_hvd_gsk_");
				StringBuffer fileName2 = new StringBuffer(folder);
				fileName2.append("n24_ifs_lev_gsk_");
				fileName1.append(now.getDateString("yyMMdd_HHmm"));
				fileName2.append(now.getDateString("yyMMdd_HHmm"));

				createPaymentFiles(fileName1.toString(), fileName2.toString(), schoolCategory, now, paymentDate, currentLocale);
			}
			else if (schoolCategory.equals(highSchool.getPrimaryKey())) {
				StringBuffer fileName1 = new StringBuffer(folder);
				fileName1.append("n24_ifs_hvd_gym_");
				StringBuffer fileName2 = new StringBuffer(folder);
				fileName2.append("n24_ifs_lev_gym_");
				fileName1.append(now.getDateString("yyMMdd_HHmm"));
				fileName2.append(now.getDateString("yyMMdd_HHmm"));

				createPaymentFiles(fileName1.toString(), fileName2.toString(), schoolCategory, now, paymentDate, currentLocale);
			}
			else {
				//What to do then?
			}

			trans.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			if (trans != null) {
				try {
					trans.rollback();
				}
				catch (SystemException se) {
					se.printStackTrace();
				}
			}
		}
	}

	private void createPaymentFiles(String fileName1, String fileName2, String schoolCategory, IWTimestamp executionDate, IWTimestamp paymentDate, Locale currentLocale)
		throws FinderException, IOException, StudyPathException, RemoteException {
		Collection phInCommune = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).findBySchoolCategoryStatusInCommuneWithCommunalManagement(schoolCategory, 'P');
		Collection phOutsideCommune = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(schoolCategory, 'P');

		if (phInCommune != null && !phInCommune.isEmpty()) {
			Collection rec = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeaders(phInCommune);
			Iterator it = rec.iterator();
			FileWriter writer = new FileWriter(fileName1.toString());
			BufferedWriter bWriter = new BufferedWriter(writer);

			PostingBusiness pb = getPostingBusiness();

			while (it.hasNext()) {
				PaymentRecord pRec = (PaymentRecord) it.next();
				if (pRec.getTotalAmount() != 0.0f) {
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyyy"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Konto"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Ansvar"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Resurs"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Verksamhet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Aktivitet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Projekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Objekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(), "Motpart"));
					bWriter.write(";");
					//anlaggningsnummer
					bWriter.write(";");
					//internranta
					bWriter.write(";");
					bWriter.write("SEK");
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(Integer.toString(Math.round(pRec.getTotalAmount())));
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(Integer.toString(Math.round(pRec.getTotalAmount())));
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyMM") + " " + pRec.getPaymentText());
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.newLine();

					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyyy"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Konto"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Ansvar"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Resurs"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Verksamhet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Aktivitet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Projekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Objekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(), "Motpart"));
					bWriter.write(";");
					//anlaggningsnummer
					bWriter.write(";");
					//internranta
					bWriter.write(";");
					bWriter.write("SEK");
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(Integer.toString(Math.round(-pRec.getTotalAmount())));
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(Integer.toString(Math.round(-pRec.getTotalAmount())));
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyMM") + " " + pRec.getPaymentText());
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.newLine();
				}

				pRec.setStatus('L');
				pRec.store();
			}

			bWriter.close();

			Iterator itor = phInCommune.iterator();
			while (itor.hasNext()) {
				PaymentHeader head = (PaymentHeader) itor.next();
				head.setStatus('L');
				head.store();
			}
		}

		if (phOutsideCommune != null && !phOutsideCommune.isEmpty()) {
			NumberFormat format = NumberFormat.getInstance(currentLocale);
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			format.setMinimumIntegerDigits(1);
			format.setGroupingUsed(false);
			//			format.
			FileWriter writer = new FileWriter(fileName2.toString());
			BufferedWriter bWriter = new BufferedWriter(writer);

			PostingBusiness pb = getPostingBusiness();
			ProviderBusiness provBiz = getProviderBusiness();

			Iterator phIt = phOutsideCommune.iterator();
			while (phIt.hasNext()) {
				PaymentHeader pHead = (PaymentHeader) phIt.next();
				Provider prov = provBiz.getProvider(pHead.getSchoolID());
				Collection rec = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeader(pHead);

				if (rec != null && !rec.isEmpty()) {
					Iterator prIt = rec.iterator();
					Iterator sumIt = rec.iterator();

					float sum = 0;
					while (sumIt.hasNext()) {
						PaymentRecord r = (PaymentRecord) sumIt.next();
						sum += r.getTotalAmount();
					}

					String giro = prov.getAccountingProperties().getBankgiro();
					if (giro == null)
						giro = prov.getAccountingProperties().getPostgiro();
					if (giro == null)
						giro = "";

					bWriter.write("H");
					bWriter.write(";");
					bWriter.write(giro);
					bWriter.write(";");
					bWriter.write(((Integer) pHead.getPrimaryKey()).toString());
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
					bWriter.write(";");
					bWriter.write("SUPPEXT");
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
					bWriter.write(";");
					bWriter.write(executionDate.getDateString("yyyy-MM-dd"));
					bWriter.write(";");
					bWriter.write(paymentDate.getDateString("yyyy-MM-dd"));
					bWriter.write(";");
					bWriter.write(Integer.toString(IWTimestamp.getDaysBetween(executionDate, paymentDate)));
					//				bWriter.write("7");
					bWriter.write(";");
					bWriter.write("SEK");
					bWriter.write(";");
					//empty
					bWriter.write("-");
					bWriter.write(";");
					bWriter.write("*");
					bWriter.write(";");
					for (int i = 13; i < 55; i++)
						bWriter.write("-;");
					bWriter.write("-");
					bWriter.newLine();

					bWriter.write("I");
					bWriter.write(";");
					bWriter.write(giro);
					bWriter.write(";");
					bWriter.write(((Integer) pHead.getPrimaryKey()).toString());
					bWriter.write(";");
					bWriter.write("1");
					bWriter.write(";");
					bWriter.write("L6");
					bWriter.write(";");
					bWriter.write(format.format(sum));
					bWriter.write(";");
					bWriter.write(format.format(sum));
					bWriter.write(";");
					bWriter.write("0,00");
					bWriter.write(";");
					bWriter.write("0,00");
					bWriter.write(";");
					for (int i = 10; i < 23; i++)
						bWriter.write("-;");
					bWriter.write("-");
					bWriter.newLine();

					int row = 1;
					while (prIt.hasNext()) {
						PaymentRecord pRec = (PaymentRecord) prIt.next();

						if (pRec.getTotalAmount() != 0.0f) {
							bWriter.write("P");
							bWriter.write(";");
							bWriter.write(giro);
							bWriter.write(";");
							bWriter.write(((Integer) pHead.getPrimaryKey()).toString());
							bWriter.write(";");
							bWriter.write("1");
							bWriter.write(";");
							bWriter.write(Integer.toString(row));
							row++;
							bWriter.write(";");
							String tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Konto");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Ansvar");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Resurs");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Verksamhet");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Aktivitet");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Projekt");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Objekt");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							tmp = pb.findFieldInStringByName(pRec.getOwnPosting(), "Motpart");
							if ("".equals(tmp))
								bWriter.write("-");
							else
								bWriter.write(tmp);
							bWriter.write(";");
							//anlaggningsnummer
							bWriter.write("-");
							bWriter.write(";");
							//internranta
							bWriter.write("-");
							bWriter.write(";");
							//empty
							bWriter.write("-");
							bWriter.write(";");
							bWriter.write(format.format(pRec.getTotalAmount()));
							bWriter.write(";");
							bWriter.write(format.format(pRec.getTotalAmount()));
							bWriter.write(";");
							//empty
							bWriter.write("-");
							bWriter.write(";");
							//empty
							bWriter.write("-");
							bWriter.write(";");
							bWriter.write(pRec.getPaymentText());
							bWriter.write(";");
							bWriter.write("-");
							bWriter.newLine();
						}

						pRec.setStatus('L');
						pRec.store();
					}
				}

				pHead.setStatus('L');
				pHead.store();
			}

			bWriter.close();
		}

	}

	private void createInvoiceFiles(String fileName, String schoolCategory, IWTimestamp executionDate, Locale currentLocale, String periodText)
		throws FinderException, IOException, StudyPathException, RemoteException {
		Collection iHeaders = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).findByStatusAndCategory("P", schoolCategory);

		StringBuffer empty = new StringBuffer("");
		for (int i = 0; i < 25; i++) {
			empty.append("          ");
		}

		if (iHeaders != null && !iHeaders.isEmpty()) {
			NumberFormat format = NumberFormat.getInstance(currentLocale);
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			format.setMinimumIntegerDigits(11);
			format.setMaximumIntegerDigits(11);
			format.setGroupingUsed(false);
			NumberFormat format2 = NumberFormat.getInstance(currentLocale);
			format2.setMaximumFractionDigits(0);
			format2.setMinimumFractionDigits(0);
			format2.setMinimumIntegerDigits(10);
			format2.setMaximumIntegerDigits(10);
			format2.setGroupingUsed(false);
			FileWriter writer = new FileWriter(fileName.toString());
			BufferedWriter bWriter = new BufferedWriter(writer);

			PostingBusiness pb = getPostingBusiness();

			//Posttyp
			bWriter.write("10");
			//Rutinkod + kundkod
			//			for (int i = 0; i < 8; i++)
			bWriter.write(empty.substring(0, 8));
			//Rutinkod for avsandande rutin
			bWriter.write("KU ");
			//Framstallandedatum
			bWriter.write(executionDate.getDateString("yyMMdd"));
			//Klockslag
			bWriter.write(executionDate.getDateString("hhmmss"));
			//Kommentar
			//			for (int i = 0; i < 225; i++)
			bWriter.write(empty.substring(0, 225));
			bWriter.newLine();

			int numberOfHeaders = 0;

			Iterator ihIt = iHeaders.iterator();
			while (ihIt.hasNext()) {
				InvoiceHeader iHead = (InvoiceHeader) ihIt.next();
				Collection rec = ((InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class)).findByInvoiceHeader(iHead);

				if (rec != null && !rec.isEmpty()) {
					Iterator irIt = rec.iterator();
					//Posttyp
					bWriter.write("60");
					//Filler etc
					//					for (int i = 0; i < 22; i++)
					bWriter.write(empty.substring(0, 22));
					//Perioden
					if (periodText.length() < 20) {
						StringBuffer p = new StringBuffer(periodText);
						while (p.length() < 20)
							p.append(' ');

						periodText = p.toString();
					}
					else if (periodText.length() > 20) {
						periodText = periodText.substring(0, 20);
					}
					bWriter.write(periodText);
					//Kundnrtyp
					bWriter.write('P');
					//Kundnr
					String pnr = iHead.getCustodian().getPersonalID();
					if (pnr.length() < 10) {
						StringBuffer p = new StringBuffer(pnr);
						while (p.length() < 10)
							p.insert(0, ' ');

						pnr = p.toString();
					}
					else if (pnr.length() > 10) {
						pnr = pnr.substring(2);
					}
					bWriter.write(pnr);
					//Kundnamn
					String name = iHead.getCustodian().getName();
					if (name.length() < 25) {
						StringBuffer p = new StringBuffer(name);
						while (p.length() < 25)
							p.append(' ');

						name = p.toString();
					}
					else if (name.length() > 25) {
						name = name.substring(0, 25);
					}
					bWriter.write(name);
					//Kundadress
					String address = getUserBusiness().getUsersMainAddress(iHead.getCustodian()).getName();
					if (address.length() < 27) {
						StringBuffer p = new StringBuffer(address);
						while (p.length() < 27)
							p.append(' ');

						address = p.toString();
					}
					else if (address.length() > 27) {
						address = address.substring(0, 27);
					}
					bWriter.write(address);
					//Kundpostnr
					String po = getUserBusiness().getUsersMainAddress(iHead.getCustodian()).getPostalCode().getPostalCode();
					if (po.length() < 5) {
						StringBuffer p = new StringBuffer(po);
						while (p.length() < 5)
							p.insert(0, ' ');

						po = p.toString();
					}
					else if (po.length() > 5) {
						po = po.substring(0, 5);
					}
					bWriter.write(po);
					//Kundort
					String poName = getUserBusiness().getUsersMainAddress(iHead.getCustodian()).getPostalCode().getName();
					if (poName.length() < 13) {
						StringBuffer p = new StringBuffer(poName);
						while (p.length() < 13)
							p.append(' ');

						poName = p.toString();
					}
					else if (poName.length() > 13) {
						poName = poName.substring(0, 13);
					}
					bWriter.write(poName);
					//C/O address
					Address ad = getUserBusiness().getUsersCoAddress(iHead.getCustodian());
					String co = "";
					if (ad != null)
						co = ad.getName();
					if (co.length() < 25) {
						StringBuffer p = new StringBuffer(co);
						while (p.length() < 25)
							p.append(' ');

						co = p.toString();
					}
					else if (co.length() > 25) {
						co = co.substring(0, 25);
					}
					bWriter.write(co);
					//Filler
					//					for (int i = 0; i < 8; i++)
					bWriter.write(empty.substring(0, 8));
					//Er referens
					StringBuffer bun = new StringBuffer("Kundvalgruppen Tel: 718 80 00");
					while (bun.length() < 40) {
						bun.append(' ');
					}
					bWriter.write(bun.toString());
					//Avsertyp
					bWriter.write("BARNOMSORG");
					//Filler
					//					for (int i = 0; i < 25; i++)
					bWriter.write(empty.substring(0, 25));
					//Verksamhetskod
					bWriter.write("BO");
					//Filler
					//					for (int i = 0; i < 15; i++)
					bWriter.write(empty.substring(0, 15));

					bWriter.newLine();

					while (irIt.hasNext()) {
						InvoiceRecord iRec = (InvoiceRecord) irIt.next();

						if (iRec.getAmount() != 0.0f) {
							//Posttype
							bWriter.write("62");
							//Filler
							//							for (int i = 0; i < 10; i++)
							bWriter.write(empty.substring(0, 10));
							//Tecken
							float am = iRec.getAmount();
							if (am < 0)
								bWriter.write('-');
							else
								bWriter.write(' ');
							//Belopp
							am = Math.abs(am);
							bWriter.write(format.format(am));
							//Antal, pris, moms, filler
							//							for (int i = 0; i < 29; i++)
							bWriter.write(empty.substring(0, 29));
							//Avser period f.o.m
							//							for (int i = 0; i < 8; i++)
							bWriter.write(empty.substring(0, 8));
							//Avser period t.o.m
							//							for (int i = 0; i < 8; i++)
							bWriter.write(empty.substring(0, 8));
							//Faktura text 1
							boolean insertLRow = false;
							String LText = null;
							String text = iRec.getInvoiceText();
							if (text.length() < 25) {
								StringBuffer t = new StringBuffer(text);
								while (t.length() < 25) {
									t.append(' ');
								}
								text = t.toString();
							}
							else if (text.length() > 25) {
								insertLRow = true;
								LText = text.substring(25);
								text = text.substring(0, 25);
							}

							bWriter.write(text);
							//Filler
							//							for (int i = 0; i < 11; i++)
							bWriter.write(empty.substring(0, 11));
							//Faktura text 2, 3 and 4
							//							for (int i = 0; i < 108; i++)
							bWriter.write(empty.substring(0, 108));
							//Kod
							bWriter.write('T');
							//Filler
							//							for (int i = 0; i < 33; i++)
							bWriter.write(empty.substring(0, 33));

							bWriter.newLine();

							if (insertLRow) {
								//Posttype
								bWriter.write("62");
								//Filler
								//								for (int i = 0; i < 10; i++)
								bWriter.write(empty.substring(0, 10));
								//Tecken
								am = iRec.getAmount();
								if (am < 0)
									bWriter.write('-');
								else
									bWriter.write(' ');
								//Belopp
								am = Math.abs(am);
								bWriter.write(format.format(am));
								//Antal, pris, moms, filler
								//								for (int i = 0; i < 29; i++)
								bWriter.write(empty.substring(0, 29));
								//Avser period f.o.m
								//								for (int i = 0; i < 8; i++)
								bWriter.write(empty.substring(0, 8));
								//Avser period t.o.m
								//								for (int i = 0; i < 8; i++)
								bWriter.write(empty.substring(0, 8));
								//Faktura text 1
								if (LText.length() < 36) {
									StringBuffer t = new StringBuffer(LText);
									while (t.length() < 36) {
										t.append(' ');
									}
									LText = t.toString();
								}
								else if (LText.length() > 36) {
									LText = LText.substring(0, 36);
								}

								bWriter.write(LText);
								//Faktura text 2, 3 and 4
								//								for (int i = 0; i < 108; i++)
								bWriter.write(empty.substring(0, 108));
								//Kod
								bWriter.write('L');
								//Filler
								//								for (int i = 0; i < 33; i++)
								bWriter.write(empty.substring(0, 33));

								bWriter.newLine();
							}

							bWriter.write("63");
							//Filler
							//							for (int i = 0; i < 6; i++)
							bWriter.write(empty.substring(0, 6));
							//Tecken
							am = iRec.getAmount();
							if (am < 0)
								bWriter.write('-');
							else
								bWriter.write(' ');
							//Belopp
							am = Math.abs(am);
							bWriter.write(format.format(am));
							//Kvantitet and Apris
							//							for (int i = 0; i < 27; i++)
							bWriter.write(empty.substring(0, 27));
							//Posting string business for check
							String posting = iRec.getOwnPosting();
							if (iRec.getRegSpecType().getLocalizationKey().equals(RegSpecConstant.CHECK)) {
								posting = iRec.getDoublePosting();
							}

							//Ansvar
							String tmp = pb.findFieldInStringByName(posting, "Ansvar");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Konto
							tmp = pb.findFieldInStringByName(posting, "Konto");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Resurs
							tmp = pb.findFieldInStringByName(posting, "Resurs");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Verksamhet
							tmp = pb.findFieldInStringByName(posting, "Verksamhet");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Aktivitet
							tmp = pb.findFieldInStringByName(posting, "Aktivitet");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Project
							tmp = pb.findFieldInStringByName(posting, "Projekt");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Object
							tmp = pb.findFieldInStringByName(posting, "Objekt");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Motpart
							tmp = pb.findFieldInStringByName(posting, "Motpart");
							if (tmp.length() < 10) {
								StringBuffer post = new StringBuffer(tmp);
								while (post.length() < 10)
									post.append(' ');
								tmp = post.toString();
							}
							else if (tmp.length() > 10) {
								tmp = tmp.substring(0, 10);
							}
							bWriter.write(tmp);
							//Anlaggnings nummber
							//							for (int i = 0; i < 10; i++)
							bWriter.write(empty.substring(0, 10));
							//Internranta
							//							for (int i = 0; i < 10; i++)
							bWriter.write(empty.substring(0, 10));
							//Filler
							//							for (int i = 0; i < 100; i++)
							bWriter.write(empty.substring(0, 100));

							bWriter.newLine();
						}
					}

					numberOfHeaders++;
				}

				iHead.setStatus('L');
				iHead.store();
			}

			//Posttyp
			bWriter.write("50");
			//Filler
			//			for (int i = 0; i < 94; i++)
			bWriter.write(empty.substring(0, 94));
			//Antal poster posttyp 60
			bWriter.write(format2.format(numberOfHeaders));
			//Filler, etc...
			//			for (int i = 0; i < 144; i++)
			bWriter.write(empty.substring(0, 144));
			bWriter.newLine();

			bWriter.close();
		}
	}

	/**
	 * A method to delete the files created for the IFS system. Also reverts the
	 * status of PaymentHeaders/InvoiceHeaders to P.
	 * 
	 * @param schoolCategory
	 *          The string primary key representing the school category we are
	 *          deleting files for.
	 * 
	 * @author palli
	 */
	public void deleteFiles(String schoolCategory, User user) {
		UserTransaction trans = null;
		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();

			IWTimestamp now = IWTimestamp.RightNow();
			JournalLog log = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileDeleted();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();

			IFSCheckHeader header = getIFSCheckHeaderBySchoolCategory(schoolCategory);
			if (header != null) {
				Collection col = this.getIFSCheckRecordByHeaderId(((Integer) header.getPrimaryKey()).intValue());
				if (col != null && !col.isEmpty()) {
					Iterator it = col.iterator();
					while (it.hasNext()) {
						IFSCheckRecord rec = (IFSCheckRecord) it.next();
						rec.remove();
					}
				}
			}

			Collection phInCommune = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).findBySchoolCategoryStatusInCommuneWithCommunalManagement(schoolCategory, 'L');
			Collection phOutsideCommune = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(schoolCategory, 'L');

			if (phInCommune != null && !phInCommune.isEmpty()) {
				Collection rec = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeaders(phInCommune);
				Iterator it = rec.iterator();

				while (it.hasNext()) {
					PaymentRecord pRec = (PaymentRecord) it.next();
					pRec.setStatus('P');
					pRec.store();
				}

				Iterator itor = phInCommune.iterator();
				while (itor.hasNext()) {
					PaymentHeader head = (PaymentHeader) itor.next();
					head.setStatus('P');
					head.store();
				}
			}

			if (phOutsideCommune != null && !phOutsideCommune.isEmpty()) {
				Collection rec = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeaders(phOutsideCommune);
				Iterator it = rec.iterator();

				while (it.hasNext()) {
					PaymentRecord pRec = (PaymentRecord) it.next();
					pRec.setStatus('P');
					pRec.store();
				}

				Iterator itor = phOutsideCommune.iterator();
				while (itor.hasNext()) {
					PaymentHeader head = (PaymentHeader) itor.next();
					head.setStatus('P');
					head.store();
				}
			}

			Collection iHeaders = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).findByStatusAndCategory("L", schoolCategory);
			Iterator itHead = iHeaders.iterator();
			while (itHead.hasNext()) {
				InvoiceHeader iHead = (InvoiceHeader) itHead.next();
				iHead.setStatus('P');
				iHead.store();
			}

			//Delete files in folder A. Must get folder info from
			// ExportMappingBean!!!
			//ExportDataMapping mapping =
			// getExportBusiness().getExportDataMapping(schoolCategory);

			trans.commit();
		}
		catch (Exception e) {
			if (trans != null) {
				try {
					trans.rollback();
				}
				catch (SystemException se) {
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * A method to send the files created to the IFS system. Also updates the
	 * status of PaymentHeaders/InvoiceHeaders from L to H.
	 * 
	 * @param schoolCategory
	 *          The string primary key representing the school category we are
	 *          sending files for.
	 * 
	 * @author palli
	 */
	public void sendFiles(String schoolCategory, User user) {
		UserTransaction trans = null;
		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();

			IWTimestamp now = IWTimestamp.RightNow();
			JournalLog log = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileSent();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();

			// Palli, thu verdur bara ad drepa mig seinna fyrir ad hafa sett
			// IWContext.getInstance() tharna
			// en klukkan er bara svo mange...
			// Eg geri rad fyrir ad herna fyrir nedan komi kodinn sem breytir status
			// ur L i e-d annad.
			getCheckAmountBusiness().sendCheckAmountLists(IWContext.getInstance(), schoolCategory);

			//copy files from folder A to folder B. Must get folder info from
			// ExportMappingBean!!!
			//ExportDataMapping mapping =
			// getExportBusiness().getExportDataMapping(schoolCategory);

			trans.commit();
		}
		catch (Exception e) {
			if (trans != null) {
				try {
					trans.rollback();
				}
				catch (SystemException se) {
					se.printStackTrace();
				}
			}
		}

	}

	public ExportBusiness getExportBusiness() {
		try {
			return (ExportBusiness) getServiceInstance(ExportBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) getServiceInstance(UserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public PostingBusiness getPostingBusiness() {
		try {
			return (PostingBusiness) getServiceInstance(PostingBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public ProviderBusiness getProviderBusiness() {
		try {
			return (ProviderBusiness) getServiceInstance(ProviderBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private CheckAmountBusiness getCheckAmountBusiness() {
		try {
			return (CheckAmountBusiness) getServiceInstance(CheckAmountBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}