/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.ifs.business;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
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
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.school.business.ProviderBusiness;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathException;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
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
	 * Returns all <code>JournalLog</code> data beans from the database for the given category.
	 * 
	 * @param category A school category primary key (String)
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
	 * Returns all <code>JournalLog</code> data beans from the database for the given category.
	 * 
	 * @param category A school category bean
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
	 * Returns the <code>IFSCheckHeader</code> data bean from the database for the given category.
	 * 
	 * @param category A school category primary key (String)
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
	 * Returns a collection of <code>IFSCheckRecord</code> data beans from the database for the given header.
	 * 
	 * @param headerId The primary key id for the header the records belong to.
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
	 * A method to create the files to be sent to the IFS system and updates the status of PaymentHeaders/InvoiceHeaders 
	 * from P to L.
	 * 
	 * @param schoolCategory The string primary key representing the school category we are creating files for.
	 * @param paymentDate The date the payment is supposed to be done on.
	 * @param periodText A text to be inserted on the customers bill/providers payment slip.
	 * 
	 * @author palli
	 */
	public void createFiles(String schoolCategory, String paymentDate, String periodText, User user) {
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

			//Create files in folder A. Must get folder info from ExportMappingBean!!!
			ExportDataMapping mapping = getExportBusiness().getExportDataMapping(schoolCategory);
			String folder = mapping.getFileCreationFolder();
			SchoolCategory childCare = getSchoolBusiness().getCategoryChildcare();
			SchoolCategory school = getSchoolBusiness().getCategoryElementarySchool();
			SchoolCategory highSchool = getSchoolBusiness().getCategoryHighSchool();

			GregorianCalendar cal = new GregorianCalendar();
			int year = cal.get(GregorianCalendar.YEAR);
			int month = cal.get(GregorianCalendar.MONTH) + 1;
			int day = cal.get(GregorianCalendar.DAY_OF_MONTH);
			int hour = cal.get(GregorianCalendar.HOUR_OF_DAY);
			int min = cal.get(GregorianCalendar.MINUTE);

			String sYear = Integer.toString(year);
			String sLongYear = sYear;
			if (sYear.length() == 4)
				sYear = sYear.substring(2);

			String sMonth = Integer.toString(month);
			if (sMonth.length() < 2)
				sMonth = "0" + sMonth;

			String sDay = Integer.toString(day);
			if (sDay.length() < 2)
				sDay = "0" + sDay;

			String sHour = Integer.toString(hour);
			if (sHour.length() < 2)
				sHour = "0" + sHour;

			String sMin = Integer.toString(min);
			if (sMin.length() < 2)
				sMin = "0" + sMin;

			if (schoolCategory.equals(childCare.getPrimaryKey())) {
				StringBuffer fileName1 = new StringBuffer(folder);
				fileName1.append("N24IFS_BOM_HVD_");
				StringBuffer fileName2 = new StringBuffer(folder);
				fileName2.append("N24IFS_BOM_LEV_");
				StringBuffer fileName3 = new StringBuffer(folder);
				fileName3.append("N24IFS_BOM_KND_");
				fileName1.append(sYear);
				fileName1.append(sMonth);
				fileName1.append(sDay);
				fileName1.append("_");
				fileName1.append(sHour);
				fileName1.append(sMin);
				fileName2.append(sYear);
				fileName2.append(sMonth);
				fileName2.append(sDay);
				fileName2.append("_");
				fileName2.append(sHour);
				fileName2.append(sMin);
				fileName3.append(sYear);
				fileName3.append(sMonth);
				fileName3.append(sDay);
				fileName3.append("_");
				fileName3.append(sHour);
				fileName3.append(sMin);

				createPaymentFiles(fileName1.toString(), fileName2.toString(), schoolCategory, sLongYear, sMonth, sDay);
				createInvoiceFiles(fileName3.toString(), schoolCategory);
			}
			else if (schoolCategory.equals(school.getPrimaryKey())) {
				StringBuffer fileName1 = new StringBuffer(folder);
				fileName1.append("N24IFS_GSK_HVD_");
				StringBuffer fileName2 = new StringBuffer(folder);
				fileName2.append("N24IFS_GSK_LEV_");
				fileName1.append(sYear);
				fileName1.append(sMonth);
				fileName1.append(sDay);
				fileName1.append("_");
				fileName1.append(sHour);
				fileName1.append(sMin);
				fileName2.append(sYear);
				fileName2.append(sMonth);
				fileName2.append(sDay);
				fileName2.append("_");
				fileName2.append(sHour);
				fileName2.append(sMin);

				createPaymentFiles(fileName1.toString(), fileName2.toString(), schoolCategory, sLongYear, sMonth, sDay);
			}
			else if (schoolCategory.equals(highSchool.getPrimaryKey())) {
				StringBuffer fileName1 = new StringBuffer(folder);
				fileName1.append("N24IFS_GYM_HVD_");
				StringBuffer fileName2 = new StringBuffer(folder);
				fileName2.append("N24IFS_GYM_LEV_");
				fileName1.append(sYear);
				fileName1.append(sMonth);
				fileName1.append(sDay);
				fileName1.append("_");
				fileName1.append(sHour);
				fileName1.append(sMin);
				fileName2.append(sYear);
				fileName2.append(sMonth);
				fileName2.append(sDay);
				fileName2.append("_");
				fileName2.append(sHour);
				fileName2.append(sMin);

				createPaymentFiles(fileName1.toString(), fileName2.toString(), schoolCategory, sLongYear, sMonth, sDay);
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

	private void createPaymentFiles(String fileName1, String fileName2, String schoolCategory, String year, String month, String day) throws FinderException, IOException, StudyPathException, RemoteException {
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
					bWriter.write(year);
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Konto"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Ansvar"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Resurs"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Verksamhet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Aktivitet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Projekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Objekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Motpart"));
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
					bWriter.write(year.substring(2) + month + " " + pRec.getPaymentText());
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
					bWriter.write(year + "-" + month + "-" + day);
					bWriter.write(";");
					//empty
					bWriter.write(";");
					//empty
					bWriter.write(";");
					bWriter.newLine();

					bWriter.write(";");
					bWriter.write(year);
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Konto"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Ansvar"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Resurs"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Verksamhet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Aktivitet"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Projekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Objekt"));
					bWriter.write(";");
					bWriter.write(pb.findFieldInStringByName(pRec.getDoublePosting(),"Motpart"));
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
					bWriter.write(year.substring(2) + month + " " + pRec.getPaymentText());
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
					bWriter.write(year + "-" + month + "-" + day);
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
				PaymentHeader head = (PaymentHeader)itor.next();
				head.setStatus('L');
				head.store();
			}
		}
		
		if (phOutsideCommune != null && !phOutsideCommune.isEmpty()) {
			FileWriter writer = new FileWriter(fileName2.toString());
			BufferedWriter bWriter = new BufferedWriter(writer);

			PostingBusiness pb = getPostingBusiness();
			ProviderBusiness provBiz = getProviderBusiness();

			Iterator phIt = phOutsideCommune.iterator();
			while (phIt.hasNext()) {
				PaymentHeader pHead = (PaymentHeader) phIt.next();
				Provider prov = provBiz.getProvider(pHead.getSchoolID());
				Collection rec = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeader(pHead);
				Iterator prIt = rec.iterator();
				Iterator sumIt = rec.iterator();
				
				float sum = 0;
				while (sumIt.hasNext()) {
					PaymentRecord r = (PaymentRecord)sumIt.next();
					sum += r.getTotalAmount();
				}

				String giro = prov.getAccountingProperties().getBankgiro();
				if (giro ==null)
					giro = prov.getAccountingProperties().getPostgiro();
				if (giro == null)
					giro = "";

				bWriter.write("H");
				bWriter.write(";");
				bWriter.write(giro);
				bWriter.write(";");
				bWriter.write(((Integer)pHead.getPrimaryKey()).toString());
				bWriter.write(";");
				bWriter.write("2004-01-15");
				bWriter.write(";");
				bWriter.write("SUPPEXT");
				bWriter.write(";");
				bWriter.write("2004-01-15");
				bWriter.write(";");
				bWriter.write("2004-01-15");
				bWriter.write(";");
				bWriter.write("2004-01-22");
				bWriter.write(";");
//				bWriter.write(IWTimestamp.getDaysBetween());
				bWriter.write("7");
				bWriter.write(";");
				bWriter.write("SEK");
				bWriter.write(";");
				//empty
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
				bWriter.write(((Integer)pHead.getPrimaryKey()).toString());
				bWriter.write(";");
				bWriter.write("1");
				bWriter.write(";");
				bWriter.write("L6");
				bWriter.write(";");
				bWriter.write(Float.toString(sum));
				bWriter.write(";");
				bWriter.write(Float.toString(sum));
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
//					IWTimestamp t = new IWTimestamp();
					
					if (pRec.getTotalAmount() != 0.0f) {
						bWriter.write("P");
						bWriter.write(";");
						bWriter.write(giro);
						bWriter.write(";");
						bWriter.write(((Integer)pHead.getPrimaryKey()).toString());						
						bWriter.write(";");
						bWriter.write("1");
						bWriter.write(";");
						bWriter.write(Integer.toString(row));
						row++;
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Konto"));						
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Ansvar"));
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Resurs"));
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Verksamhet"));
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Aktivitet"));
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Projekt"));
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Objekt"));
						bWriter.write(";");
						bWriter.write(pb.findFieldInStringByName(pRec.getOwnPosting(),"Motpart"));
						bWriter.write(";");
						//anlaggningsnummer
						bWriter.write(";");
						//internranta
						bWriter.write(";");
						//empty
						bWriter.write(";");
						bWriter.write(Float.toString(pRec.getTotalAmount()));
						bWriter.write(";");
						bWriter.write(Float.toString(pRec.getTotalAmount()));
						bWriter.write(";");
						//empty
						bWriter.write(";");
						//empty
						bWriter.write(";");
						bWriter.write(pRec.getPaymentText());
						bWriter.write(";");
						//empty
						bWriter.write(";");
						bWriter.write("-");						
						bWriter.newLine();
					}
				
					pRec.setStatus('L');
					pRec.store();
				}
				
				pHead.setStatus('L');
				pHead.store();	
			}
						
			bWriter.close();
		}
		
	}

	private void createInvoiceFiles(String fileName, String schoolCategory) {
		System.out.println("fileName = " + fileName);
		System.out.println("schoolCategory = " + schoolCategory);
	}

	/**
	 * A method to delete the files created for the IFS system. Also reverts the status of PaymentHeaders/InvoiceHeaders 
	 * to P.
	 * 
	 * @param schoolCategory The string primary key representing the school category we are deleting files for.
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

			//Delete files in folder A. Must get folder info from ExportMappingBean!!!
			//ExportDataMapping mapping = getExportBusiness().getExportDataMapping(schoolCategory);

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
	 * A method to send the files created to the IFS system. Also updates the status of PaymentHeaders/InvoiceHeaders 
	 * from L to H.
	 * 
	 * @param schoolCategory The string primary key representing the school category we are sending files for.
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

			// Palli, thu verdur bara ad drepa mig seinna fyrir ad hafa sett IWContext.getInstance() tharna
			// en klukkan er bara svo mange...
			// Eg geri rad fyrir ad herna fyrir nedan komi kodinn sem breytir status ur L i e-d annad.
			getCheckAmountBusiness().sendCheckAmountLists(IWContext.getInstance(), schoolCategory);
			
			//copy files from folder A to folder B. Must get folder info from ExportMappingBean!!!
			//ExportDataMapping mapping = getExportBusiness().getExportDataMapping(schoolCategory);

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