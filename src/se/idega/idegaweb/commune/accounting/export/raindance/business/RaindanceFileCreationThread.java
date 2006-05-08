package se.idega.idegaweb.commune.accounting.export.raindance.business;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.export.business.MissingAddressException;
import se.idega.idegaweb.commune.accounting.export.business.MissingCustodianException;
import se.idega.idegaweb.commune.accounting.export.business.MissingPostalCodeException;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckHeader;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckHeaderHome;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckRecord;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckRecordHome;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLog;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class RaindanceFileCreationThread extends Thread {

	protected String schoolCategory = null;

	protected IWTimestamp paymentDate = null;

	protected String periodText = null;

	protected User user = null;

	protected Locale currentLocale = null;

	protected IWApplicationContext iwac = null;

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
	public RaindanceFileCreationThread(String schoolCategory,
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
		// excelFileUtil = new RaindanceCreateExcelFileUtil(iwac, paymentDate);
		IWTimestamp now = IWTimestamp.RightNow();
		RaindanceJournalLog log;
		try {
			log = ((RaindanceJournalLogHome) IDOLookup
					.getHome(RaindanceJournalLog.class)).create();
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

		RaindanceCheckHeader header = null;
		try {
			header = getRaindanceBusiness()
					.getRaindanceCheckHeaderBySchoolCategory(schoolCategory);
		} catch (Exception e) {
		}

		if (header == null) {
			try {
				header = ((RaindanceCheckHeaderHome) IDOLookup
						.getHome(RaindanceCheckHeader.class)).create();
			} catch (IDOLookupException e1) {
				e1.printStackTrace();
			} catch (CreateException e1) {
				e1.printStackTrace();
			}
		} else {
			Collection col = null;
			try {
				col = getRaindanceBusiness().getRaindanceCheckRecordByHeaderId(
						((Integer) header.getPrimaryKey()).intValue());
			} catch (RemoteException e5) {
				e5.printStackTrace();
			} catch (EJBException e5) {
				e5.printStackTrace();
			}
			if (col != null && !col.isEmpty()) {
				Iterator it = col.iterator();
				while (it.hasNext()) {
					RaindanceCheckRecord rec = (RaindanceCheckRecord) it.next();
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
			mapping = getRaindanceBusiness().getExportBusiness()
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
			childCare = getRaindanceBusiness().getSchoolBusiness()
					.getCategoryChildcare();
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}

		SchoolCategory school = null;
		try {
			school = getRaindanceBusiness().getSchoolBusiness()
					.getCategoryElementarySchool();
		} catch (RemoteException e3) {
			e3.printStackTrace();
		}

		StringBuffer fileName1 = null;
		StringBuffer fileName2 = null;
		StringBuffer fileName3 = null;
		if (childCare != null && (fileFolder != null || listFolder != null)
				&& school != null) {
			if (schoolCategory.equals(childCare.getPrimaryKey())) {
				if (fileFolder != null) {
					fileName1 = new StringBuffer(fileFolder);
					fileName1.append("BOIN");
					fileName2 = new StringBuffer(fileFolder);
					fileName2.append("BOEX");
					fileName3 = new StringBuffer(fileFolder);
					fileName3.append("DEB");
					fileName1.append(now.getDateString("yyMM"));
					fileName2.append(now.getDateString("yyMM"));
					fileName3.append(now.getDateString("yyMM"));
					fileName1.append(".txt");
					fileName2.append(".txt");
					fileName3.append(".txt");
				}

				try {
					createPaymentFiles(fileName1.toString(), fileName2
							.toString(), schoolCategory, now, paymentDate);
				} catch (IOException e5) {
					e5.printStackTrace();
				}

				try {
					createInvoiceFiles(fileName3.toString(), schoolCategory,
							currentLocale, header, mapping);
				} catch (IOException e6) {
					e6.printStackTrace();
				}
			}
		}
		now = IWTimestamp.RightNow();
		header.setEventEndTime(now.getTimestamp());
		header.store();
	}

	private void createPaymentFiles(String fileName1, String fileName2,
			String schoolCategory, IWTimestamp executionDate,
			IWTimestamp paymentDate)
			throws IOException {

		Collection headers = null;
		try {
			headers = ((PaymentHeaderHome) IDOLookup
					.getHome(PaymentHeader.class))
					.findBySchoolCategoryStatusInCommuneWithCommunalManagement(
							schoolCategory, 'P');
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		StringBuffer empty = new StringBuffer("");
		for (int i = 0; i < 60; i++) {
			empty.append("          ");
		}

		// Internal payment file
		if (headers != null && !headers.isEmpty()) {
			Iterator it = headers.iterator();
			OutputStreamWriter sWriter = new OutputStreamWriter(new FileOutputStream(fileName1), "ISO-8859-1");
			//FileWriter writer = new FileWriter(fileName1);
			BufferedWriter bWriter = new BufferedWriter(sWriter);

			Map ownPostingMap = new HashMap();
			Map doublePostingMap = new HashMap();

			while (it.hasNext()) {
				PaymentHeader header = (PaymentHeader) it.next();
				boolean includeHeader = false;

				if (header.getSchool() != null) {
					Provider provider = getRaindanceBusiness()
							.getProviderBusiness().getProvider(
									header.getSchoolID());
					if (provider.getAccountingProperties().getBankgiro() == null
							&& provider.getAccountingProperties().getPostgiro() == null) {
						includeHeader = true;
					}
				}

				if (includeHeader) {
					Collection records = null;
					try {
						records = ((PaymentRecordHome) IDOLookup
								.getHome(PaymentRecord.class))
								.findByPaymentHeader(header);
					} catch (IDOLookupException e) {
						e.printStackTrace();
					} catch (FinderException e) {
						e.printStackTrace();
					}

					if (records != null && !records.isEmpty()) {
						Iterator rIt = records.iterator();
						float sumForHeader = 0.0f;
						while (rIt.hasNext()) {
							PaymentRecord rec = (PaymentRecord) rIt.next();
							if (rec.getTotalAmount() != 0.0f) {
								sumForHeader += rec.getTotalAmount();
							}
							rec.setStatus('L');
							rec.store();
						}

						if (sumForHeader != 0.0f) {
							rIt = records.iterator();
							while (rIt.hasNext()) {
								PaymentRecord rec = (PaymentRecord) rIt.next();
								String ownPosting = rec.getOwnPosting();
								String doublePosting = rec.getDoublePosting();

								if (ownPosting != null) {
									if (ownPostingMap.containsKey(ownPosting)) {
										PaymentRecord r = (PaymentRecord) ownPostingMap
												.get(ownPosting);
										r.setTotalAmount(r.getTotalAmount()
												+ rec.getTotalAmount());
									} else {
										ownPostingMap.put(ownPosting, rec);
									}
								}

								if (doublePosting != null) {
									if (doublePostingMap
											.containsKey(doublePosting)) {
										PaymentRecord r = (PaymentRecord) doublePostingMap
												.get(doublePosting);
										r.setTotalAmount(r.getTotalAmount()
												+ rec.getTotalAmount());
									} else {
										doublePostingMap
												.put(doublePosting, rec);
									}
								}
							}
						}
					}
				}

				header.setStatus('L');
				header.store();
			}

			int numberOfLines = 0;
			Iterator keys = ownPostingMap.keySet().iterator();
			PostingBusiness pb = getRaindanceBusiness().getPostingBusiness();
			
			StringBuffer paymentText = new StringBuffer("UTBETALN BOCHECK ");
			paymentText.append(IWTimestamp.RightNow().getDateString("MMM yyyy"));

			NumberFormat format = NumberFormat.getInstance(currentLocale);
			format.setMaximumFractionDigits(0);
			format.setMinimumFractionDigits(0);
			format.setMaximumIntegerDigits(14);
			format.setMinimumIntegerDigits(14);
			format.setGroupingUsed(false);
			
			while (keys.hasNext()) {
				String ownPostingString = (String) keys.next();
				PaymentRecord r = (PaymentRecord) ownPostingMap
						.get(ownPostingString);

				bWriter.write("20");
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(ownPostingString,
						"Ansvar"), 3));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(ownPostingString,
						"Konto"), 7));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(ownPostingString,
					"Verksamhet"), 5));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(ownPostingString,
					"Motpart"), 3));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(ownPostingString,
					"Objekt"), 3));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(ownPostingString,
					"Projekt"), 6));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(ownPostingString,
					"Aktivitet"), 4));
/*				if (r.getTotalAmount() < 0.0f) {
					bWriter.write("-");
				} else {*/
					bWriter.write("+");					
				//}
				bWriter.write(format.format(Math.abs(r.getTotalAmount() * 100)));
				bWriter.write(paymentText.toString());
				bWriter.write(empty.substring(0, 5));
				bWriter.newLine();
				numberOfLines++;
			}

			keys = doublePostingMap.keySet().iterator();
			while (keys.hasNext()) {
				String doublePostingString = (String) keys.next();
				PaymentRecord r = (PaymentRecord) doublePostingMap
						.get(doublePostingString);

				bWriter.write("20");
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(doublePostingString,
						"Ansvar"), 3));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(doublePostingString,
						"Konto"), 7));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(doublePostingString,
					"Verksamhet"), 5));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(doublePostingString,
					"Motpart"), 3));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(doublePostingString,
					"Objekt"), 3));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(doublePostingString,
					"Projekt"), 6));
				bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(doublePostingString,
					"Aktivitet"), 4));
				//if (r.getTotalAmount() < 0.0f) {
					bWriter.write("-");
				/*} else {
					bWriter.write("+");					
				}*/
				bWriter.write(format.format(Math.abs(r.getTotalAmount() * 100)));
				bWriter.write(paymentText.toString());
				bWriter.write(empty.substring(0, 5));
				bWriter.newLine();
				numberOfLines++;
			}

			bWriter.write("900");
			bWriter.write(empty.substring(0, 17));			
			bWriter.write(getStringByLengthRightJustified(Integer.toString(numberOfLines), 5));
			bWriter.write(empty.substring(0, 2));			
			bWriter.newLine();

			bWriter.close();
		}

		try {
			headers = ((PaymentHeaderHome) IDOLookup
					.getHome(PaymentHeader.class))
					.findBySchoolCategoryStatusInCommuneWithoutCommunalManagement(
							schoolCategory, 'P');
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		// Bookkeeping file
		if (headers != null && !headers.isEmpty()) {
			Iterator it = headers.iterator();
			OutputStreamWriter sWriter = new OutputStreamWriter(new FileOutputStream(fileName2), "ISO-8859-1");
			//FileWriter writer = new FileWriter(fileName2);
			BufferedWriter bWriter = new BufferedWriter(sWriter);
			PostingBusiness pb = getRaindanceBusiness().getPostingBusiness();

			int numberOfHLines = 0;
			int numberOfRLines = 0;
			float totalInFile = 0.0f;
			StringBuffer paymentText = new StringBuffer("UTBETALN BOCHECK ");
			paymentText.append(IWTimestamp.RightNow().getDateString("MMM yyyy"));
			NumberFormat format = NumberFormat.getInstance(currentLocale);
			format.setMaximumFractionDigits(0);
			format.setMinimumFractionDigits(0);
			format.setGroupingUsed(false);

			NumberFormat format14 = NumberFormat.getInstance(currentLocale);
			format14.setMaximumFractionDigits(0);
			format14.setMinimumFractionDigits(0);
			format14.setMinimumIntegerDigits(14);
			format14.setMaximumIntegerDigits(14);
			format14.setGroupingUsed(false);

			while (it.hasNext()) {
				PaymentHeader header = (PaymentHeader) it.next();
				Collection records = null;
				try {
					records = ((PaymentRecordHome) IDOLookup
							.getHome(PaymentRecord.class))
							.findByPaymentHeader(header);
				} catch (IDOLookupException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}

				float totalAmountPrHeader = 0.0f; 
				if (records != null && !records.isEmpty()) {
					Iterator rIt = records.iterator();
					while (rIt.hasNext()) {
						PaymentRecord rec = (PaymentRecord) rIt.next();
						if (rec.getTotalAmount() != 0.0f) {
							totalAmountPrHeader += rec.getTotalAmount();
						}

						rec.setStatus('L');
						rec.store();
					}
				}
				
				if (totalAmountPrHeader != 0.0f) {
					totalInFile += totalAmountPrHeader;
					boolean isPostgiro = false;
					boolean isBankgiro = false;
					boolean isBKBankgiro = false;
					String giroString = null;
					String ownPostingString = null;
					
					if (header.getSchool() != null) {
						Provider provider = getRaindanceBusiness()
								.getProviderBusiness().getProvider(
										header.getSchoolID());
						if (provider.getAccountingProperties().getBankgiro() != null) {
							giroString = provider.getAccountingProperties().getBankgiro();
							if (giroString.startsWith("bk")) {
								isBKBankgiro = true;
								giroString = giroString.substring(2);
							} else {
								isBankgiro = true;
							}
						}

						if (provider.getAccountingProperties().getPostgiro() != null) {
							giroString = provider.getAccountingProperties().getPostgiro();
							isPostgiro = true;
						}
						
						ownPostingString = provider.getOwnPosting();
					}

					bWriter.write("H");
					if (isBKBankgiro) { 
						bWriter.write(empty.substring(0, 11));
						bWriter.write(empty.substring(0, 9));
					} else {
						giroString = getStringByLengthLeftJustified(giroString, 11);
						bWriter.write(giroString);
						bWriter.write(giroString.substring(0, 9));
					}
					
					bWriter.write(getStringByLengthLeftJustified(header.getSchool().getOrganizationNumber(), 10));
					bWriter.write(getStringByLengthLeftJustified(header.getSchool().getName(), 35));
					bWriter.write(getStringByLengthLeftJustified(header.getSchool().getSchoolAddress(), 35));

					StringBuffer zip = new StringBuffer(header.getSchool().getSchoolZipCode());
					zip.append(" ");
					zip.append(header.getSchool().getSchoolZipArea());
					bWriter.write(getStringByLengthLeftJustified(zip.toString(), 35));
										
					String postingString = pb.findFieldInStringByName(ownPostingString, "Motpart");
					bWriter.write(getStringByLengthLeftJustified(postingString, 3));
					bWriter.write(executionDate.getDateString("yyyyMM01"));
					bWriter.write(paymentDate.getDateString("yyyyMMdd"));
					bWriter.write(getStringByLengthLeftJustified(paymentText.toString(), 30));
					if (totalAmountPrHeader < 0.0f) {
						bWriter.write("-");
					} else {
						bWriter.write("+");						
					}
					bWriter.write(format14.format(Math.abs(totalAmountPrHeader * 100)));
					bWriter.write(empty.substring(0, 1));
					if (isPostgiro) {
						bWriter.write("PG1");
					} else if (isBankgiro) {
						bWriter.write("BG1");
					} else if (isBKBankgiro) {
						bWriter.write("BG2");
					}
					bWriter.write(empty.substring(0, 1));
					if (isBKBankgiro) {
						bWriter.write("J");
						bWriter.write(empty.substring(0, 1));
						bWriter.write(getStringByLengthLeftJustified(giroString, 16));
					} else {
						bWriter.write(empty.substring(0, 1));
						bWriter.write(empty.substring(0, 1));
						bWriter.write(empty.substring(0, 16));
					}

					bWriter.newLine();
					numberOfHLines++;
					
					Iterator rIt = records.iterator();
					while (rIt.hasNext()) {
						PaymentRecord rec = (PaymentRecord) rIt.next();
						bWriter.write("R");
						bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(rec.getOwnPosting(), "Ansvar"), 3));
						bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(rec.getOwnPosting(), "Konto"), 7));
						bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(rec.getOwnPosting(), "Verksamhet"), 5));
						bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(rec.getOwnPosting(), "Motpart"), 3));
						bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(rec.getOwnPosting(), "Objekt"), 3));
						bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(rec.getOwnPosting(), "Projekt"), 6));
						bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(rec.getOwnPosting(), "Aktivitet"), 4));
						if (rec.getTotalAmount() < 0.0f) {
							bWriter.write("-");
						} else {
							bWriter.write("+");							
						}
						bWriter.write(format14.format(Math.abs(rec.getTotalAmount()) * 100));
						bWriter.newLine();
						numberOfRLines++;
					}
				}

				header.setStatus('L');
				header.store();
			}

			bWriter.write("S");
			bWriter.write(getStringByLengthRightJustified(Integer.toString(numberOfHLines), 5));
			bWriter.write(getStringByLengthRightJustified(format.format(totalInFile * 100), 20));
			bWriter.write(getStringByLengthRightJustified(Integer.toString(numberOfRLines), 5));
			bWriter.newLine();

			bWriter.close();
		}
	}

	private String getStringByLengthLeftJustified(String original, int length) {
		if (original == null) {
			original = "";
		}
		
		if (original.length() < length) {
			StringBuffer p = new StringBuffer(original);
			while (p.length() < length) {
				p.append(' ');
			}
			 return p.toString();
		} else if (original.length() > length) {
			return original.substring(0, length);
		}
		
		return original;
	}

	private String getStringByLengthRightJustified(String original, int length) {
		if (original == null) {
			original = "";
		}

		if (original.length() < length) {
			StringBuffer p = new StringBuffer(original);
			while (p.length() < length) {
				p.insert(0, ' ');
			}
			 return p.toString();
		} else if (original.length() > length) {
			return original.substring(0, length);
		}
		
		return original;
	}

	private void createInvoiceFiles(String fileName1, String schoolCategory, Locale currentLocale,
			RaindanceCheckHeader checkHeader, ExportDataMapping mapping) throws IOException {
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
			NumberFormat format11 = NumberFormat.getInstance(currentLocale);
			format11.setMaximumFractionDigits(0);
			format11.setMinimumFractionDigits(0);
			format11.setMinimumIntegerDigits(11);
			format11.setMaximumIntegerDigits(11);
			format11.setGroupingUsed(false);

			NumberFormat format15 = NumberFormat.getInstance(currentLocale);
			format15.setMaximumFractionDigits(0);
			format15.setMinimumFractionDigits(0);
			format15.setMinimumIntegerDigits(15);
			format15.setMaximumIntegerDigits(15);
			format15.setGroupingUsed(false);

			NumberFormat format6 = NumberFormat.getInstance(currentLocale);
			format6.setMaximumFractionDigits(0);
			format6.setMinimumFractionDigits(0);
			format6.setMinimumIntegerDigits(6);
			format6.setMaximumIntegerDigits(6);
			format6.setGroupingUsed(false);

			NumberFormat format10 = NumberFormat.getInstance(currentLocale);
			format10.setMaximumFractionDigits(0);
			format10.setMinimumFractionDigits(0);
			format10.setMinimumIntegerDigits(10);
			format10.setMaximumIntegerDigits(10);
			format10.setGroupingUsed(false);

			NumberFormat format14 = NumberFormat.getInstance(currentLocale);
			format14.setMaximumFractionDigits(0);
			format14.setMinimumFractionDigits(0);
			format14.setMinimumIntegerDigits(14);
			format14.setMaximumIntegerDigits(14);
			format14.setGroupingUsed(false);

			OutputStreamWriter sWriter = new OutputStreamWriter(new FileOutputStream(fileName1), "ISO-8859-1");
			//FileWriter writer = new FileWriter(fileName1);
			BufferedWriter bWriter = new BufferedWriter(sWriter);
			PostingBusiness pb = getRaindanceBusiness().getPostingBusiness();
			int numberOfSLines = 0;
			int numberOfRLines = 0;
			float total = 0.0f;
			
			Iterator ihIt = iHeaders.iterator();

			while (ihIt.hasNext()) {
				InvoiceHeader iHead = (InvoiceHeader) ihIt.next();

				Collection records = null;
				try {
					records = ((InvoiceRecordHome) IDOLookup
							.getHome(InvoiceRecord.class))
							.findByInvoiceHeader(iHead);
				} catch (IDOLookupException e3) {
					e3.printStackTrace();
				} catch (FinderException e3) {
					e3.printStackTrace();
				}

				boolean createInvoice = false;
				Iterator itRec = records.iterator();
				float sum = 0.0f;
				while (itRec.hasNext()) {
					InvoiceRecord rec = (InvoiceRecord) itRec.next();

					sum += rec.getAmount();
				}

				if (sum >= mapping.getInvoiceLimit()) {
					createInvoice = true;
				}

				try {
					User custodian = iHead.getCustodian();
					if (custodian == null) {
						throw new MissingCustodianException(
								"Raindance_missing_custodian",
								"Missing custodian");
					}
					Address mainAddress = getRaindanceBusiness()
							.getUserBusiness().getUsersMainAddress(
									iHead.getCustodian());
					if (mainAddress == null) {
						throw new MissingAddressException(
								"Raindance_missing_address", "Missing address");
					}
					PostalCode poCode = mainAddress.getPostalCode();
					if (poCode == null) {
						throw new MissingPostalCodeException(
								"Raindance_missing_postalcode",
								"Missing postalcode");
					}
					if (records != null && !records.isEmpty() && createInvoice) {

						bWriter.write("IST");
						bWriter.write(empty.substring(0, 4));
						bWriter.write("S ");
						String pnr = custodian.getPersonalID();
						if (pnr.length() == 12) {
							pnr = pnr.substring(2);
						}
						bWriter.write(getStringByLengthLeftJustified(pnr, 10));
						bWriter.write(empty.substring(0, 1));

						StringBuffer name = new StringBuffer(custodian.getFirstName());
						name.append(" ");
						name.append(custodian.getLastName());
						bWriter.write(getStringByLengthLeftJustified(name.toString(), 72));
						bWriter.write(getStringByLengthLeftJustified(mainAddress.getStreetAddress(), 36));

						StringBuffer po = new StringBuffer(poCode.getPostalCode());
						po.append(" ");
						po.append(poCode.getName());
						bWriter.write(getStringByLengthLeftJustified(po.toString(), 35));
						bWriter.newLine();
						numberOfSLines++;

						bWriter.write("IST");
						bWriter.write(empty.substring(0, 4));
						bWriter.write("H ");
						bWriter.write(getStringByLengthLeftJustified(pnr, 10));
						bWriter.write(empty.substring(0, 70));
						bWriter.write("09");
						bWriter.write(empty.substring(0, 6));
						bWriter.newLine();
						
						Iterator irIt = records.iterator();

						HashMap map = new HashMap();
						HashMap regular = new HashMap();
						while (irIt.hasNext()) {
							InvoiceRecord iRec = (InvoiceRecord) irIt.next();

							if (iRec.getAmount() != 0.0f) {
								if (iRec.getChildCareContract() != null) {
									boolean addRecord = false;
									if (iRec.getProvider() == null) {
										addRecord = true;
									} else {
										Provider provider = getRaindanceBusiness()
												.getProviderBusiness()
												.getProvider(
														iRec.getProviderId());
										if (provider.getAccountingProperties()
												.getCreateInvoiceRecord()) {
											addRecord = true;
										}
									}

									if (addRecord) {
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
									}
								} else {
									if (regular.containsKey(iRec
											.getSchoolClassMember()
											.getPrimaryKey())) {
										ArrayList regList = (ArrayList) regular
												.get(iRec
														.getSchoolClassMember()
														.getPrimaryKey());
										regList.add(iRec);
										regular.put(iRec.getSchoolClassMember()
												.getPrimaryKey(), regList);
									} else {
										ArrayList regList = new ArrayList();
										regList.add(iRec);
										regular.put(iRec.getSchoolClassMember()
												.getPrimaryKey(), regList);
									}
								}
							}
						}

						Iterator recordIterator = map.keySet().iterator();
						while (recordIterator.hasNext()) {
							InvoiceRecord r = (InvoiceRecord) map
									.get(recordIterator.next());

							if (r.getAmount() != 0.0f) {
								bWriter.write("IST");
								bWriter.write(empty.substring(0, 4));
								bWriter.write("R ");
								bWriter.write(getStringByLengthLeftJustified(pnr, 10));
								bWriter.write(empty.substring(0, 1));
								
								StringBuffer responsibleString = new StringBuffer(); 
								User resp = r.getProvider().getResponsibleUser();
								if (resp != null) {
									responsibleString.append(resp.getFirstName());
									responsibleString.append(" ");
									responsibleString.append(resp.getLastName());
									Phone phone = getUserBusiness().getUserPhone(((Integer)resp.getPrimaryKey()).intValue(), 2);
									if (phone != null) {
										responsibleString.append(" ");
										responsibleString.append(phone.getNumber());
									}
								}
								bWriter.write(getStringByLengthLeftJustified(responsibleString.toString(), 36));
								bWriter.write(format11.format(0));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(format11.format(0));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(format15.format(0));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Ansvar"), 3));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Konto"), 7));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Verksamhet"), 5));
								bWriter.write(empty.substring(0, 26));								
								bWriter.newLine();
								numberOfRLines++;
								
								bWriter.write("IST");
								bWriter.write(empty.substring(0, 4));
								bWriter.write("R ");
								bWriter.write(getStringByLengthLeftJustified(pnr, 10));
								bWriter.write(empty.substring(0, 1));
								
								StringBuffer childString = new StringBuffer(); 
								User child = r.getSchoolClassMember().getStudent();
								if (child != null) {
									childString.append(child.getFirstName());
									if (this.periodText != null) {
										childString.append(", ");
										childString.append(this.periodText);
									}
								}
								bWriter.write(getStringByLengthLeftJustified(childString.toString(), 36));
								bWriter.write(format11.format(0));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(format11.format(0));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(format15.format(0));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Ansvar"), 3));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Konto"), 7));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Verksamhet"), 5));
								bWriter.write(empty.substring(0, 26));								
								bWriter.newLine();
								numberOfRLines++;
								
								bWriter.write("IST");
								bWriter.write(empty.substring(0, 4));
								bWriter.write("R ");
								bWriter.write(getStringByLengthLeftJustified(pnr, 10));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(getStringByLengthLeftJustified(r.getRuleText(), 36));
								bWriter.write(format11.format(0));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(format11.format(0));
								bWriter.write(empty.substring(0, 1));
								if (r.getAmount() < 0.0f) {
									bWriter.write("-");
								} else {
									bWriter.write("0");
								}
								bWriter.write(format14.format(Math.abs(r.getAmount() * 100)));
								bWriter.write(empty.substring(0, 1));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Ansvar"), 3));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Konto"), 7));
								bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(r.getOwnPosting(),
									"Verksamhet"), 5));
								bWriter.write(empty.substring(0, 26));								
								bWriter.newLine();
								numberOfRLines++;
								total += r.getAmount();
																
								if (regular
										.containsKey(r.getSchoolClassMember()
												.getPrimaryKey())) {
									ArrayList regList = (ArrayList) regular
											.get(r.getSchoolClassMember()
													.getPrimaryKey());
									Iterator regit = regList.iterator();
									while (regit.hasNext()) {
										InvoiceRecord regRecord = (InvoiceRecord) regit
												.next();

										if (regRecord.getAmount() != 0.0f) {
											bWriter.write("IST");
											bWriter.write(empty.substring(0, 4));
											bWriter.write("R ");
											bWriter.write(getStringByLengthLeftJustified(pnr, 10));
											bWriter.write(empty.substring(0, 1));
											bWriter.write(getStringByLengthLeftJustified(regRecord.getNotes(), 36));
											bWriter.write(format11.format(0));
											bWriter.write(empty.substring(0, 1));
											bWriter.write(format11.format(0));
											bWriter.write(empty.substring(0, 1));
											if (regRecord.getAmount() < 0.0f) {
												bWriter.write("-");
											} else {
												bWriter.write("0");
											}
											bWriter.write(format14.format(Math.abs(regRecord.getAmount() * 100)));
											bWriter.write(empty.substring(0, 1));
											bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(regRecord.getOwnPosting(),
												"Ansvar"), 3));
											bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(regRecord.getOwnPosting(),
												"Konto"), 7));
											bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(regRecord.getOwnPosting(),
												"Verksamhet"), 5));
											bWriter.write(empty.substring(0, 26));								
											bWriter.newLine();
											numberOfRLines++;
											total += regRecord.getAmount();
										}

									}

									regular.remove(r.getSchoolClassMember()
											.getPrimaryKey());
								}
							}
						}

						Iterator regularIt = regular.keySet().iterator();
						while (regularIt.hasNext()) {
							ArrayList regList = (ArrayList) regular
									.get(regularIt.next());
							Iterator regit = regList.iterator();
							while (regit.hasNext()) {
								InvoiceRecord regRecord = (InvoiceRecord) regit
										.next();

								if (regRecord.getAmount() != 0.0f) {
									bWriter.write("IST");
									bWriter.write(empty.substring(0, 4));
									bWriter.write("R ");
									bWriter.write(getStringByLengthLeftJustified(pnr, 10));
									bWriter.write(empty.substring(0, 1));
									bWriter.write(getStringByLengthLeftJustified(regRecord.getNotes(), 36));
									bWriter.write(format11.format(0));
									bWriter.write(empty.substring(0, 1));
									bWriter.write(format11.format(0));
									bWriter.write(empty.substring(0, 1));
									if (regRecord.getAmount() < 0.0f) {
										bWriter.write("-");
									} else {
										bWriter.write("0");
									}
									bWriter.write(format14.format(Math.abs(regRecord.getAmount() * 100)));
									bWriter.write(empty.substring(0, 1));
									bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(regRecord.getOwnPosting(),
										"Ansvar"), 3));
									bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(regRecord.getOwnPosting(),
										"Konto"), 7));
									bWriter.write(getStringByLengthLeftJustified(pb.findFieldInStringByName(regRecord.getOwnPosting(),
										"Verksamhet"), 5));
									bWriter.write(empty.substring(0, 26));								
									bWriter.newLine();
									numberOfRLines++;
									total += regRecord.getAmount();
								}
							}
						}
					}

					iHead.setStatus('L');
					iHead.store();
				} catch (MissingCustodianException e) {
					RaindanceCheckRecordHome home = getRaindanceCheckRecordHome();
					if (home != null) {
						try {
							RaindanceCheckRecord Raindance_rec = home.create();
							Raindance_rec.setHeader(checkHeader);
							if (e.getTextKey() != null && e.getTextKey().length() > 255) {
								Raindance_rec.setError(e.getTextKey().substring(0, 255));
								
							} else {
								Raindance_rec.setError(e.getTextKey());
							}
							Raindance_rec.setErrorConcerns("Faktura "
									+ ((Integer) iHead.getPrimaryKey())
											.toString());
							Raindance_rec.store();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} catch (MissingAddressException e) {
					RaindanceCheckRecordHome home = getRaindanceCheckRecordHome();
					if (home != null) {
						try {
							RaindanceCheckRecord Raindance_rec = home.create();
							Raindance_rec.setHeader(checkHeader);
							if (e.getTextKey() != null && e.getTextKey().length() > 255) {
								Raindance_rec.setError(e.getTextKey().substring(0, 255));
								
							} else {
								Raindance_rec.setError(e.getTextKey());
							}
							Raindance_rec.setErrorConcerns("Fakturamottagare "
									+ iHead.getCustodian().getPersonalID());
							Raindance_rec.store();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} catch (MissingPostalCodeException e) {
					RaindanceCheckRecordHome home = getRaindanceCheckRecordHome();
					if (home != null) {
						try {
							RaindanceCheckRecord Raindance_rec = home.create();
							Raindance_rec.setHeader(checkHeader);
							if (e.getTextKey() != null && e.getTextKey().length() > 255) {
								Raindance_rec.setError(e.getTextKey().substring(0, 255));
								
							} else {
								Raindance_rec.setError(e.getTextKey());
							}
							Raindance_rec.setErrorConcerns("Fakturamottagare "
									+ iHead.getCustodian().getPersonalID());
							Raindance_rec.store();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			// Posttyp
			bWriter.write("IST");
			bWriter.write(empty.substring(0, 4));
			bWriter.write("T ");
			bWriter.write(format6.format(numberOfSLines));
			bWriter.write(empty.substring(0, 1));
			bWriter.write(format6.format(numberOfRLines));
			bWriter.write(empty.substring(0, 1));
			bWriter.write(format10.format(total * 100));
			bWriter.newLine();
			bWriter.close();
		}
	}

	private RaindanceBusiness getRaindanceBusiness() {
		try {
			return (RaindanceBusiness) IBOLookup.getServiceInstance(iwac,
					RaindanceBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac,
					UserBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private RaindanceCheckRecordHome getRaindanceCheckRecordHome() {
		try {
			return (RaindanceCheckRecordHome) IDOLookup
					.getHome(RaindanceCheckRecord.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		return null;
	}
}