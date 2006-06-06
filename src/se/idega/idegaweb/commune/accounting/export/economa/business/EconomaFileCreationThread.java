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
import java.util.StringTokenizer;

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
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
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
		} catch (Exception e) {
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

		StringBuffer fileName1 = null;
		StringBuffer fileName2 = null;
		StringBuffer fileName3 = null;
		if (childCare != null && (fileFolder != null || listFolder != null)
				&& school != null) {
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

				try {
					createPaymentFiles(fileName1.toString(), fileName2
							.toString(), schoolCategory, now, paymentDate,
							mapping);
				} catch (IOException e5) {
					e5.printStackTrace();
				}

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

				try {
					createPaymentFiles(fileName1.toString(), fileName2
							.toString(), schoolCategory, now, paymentDate,
							mapping);
				} catch (IOException e5) {
					e5.printStackTrace();
				}
			}
		}
		now = IWTimestamp.RightNow();
		header.setEventEndTime(now.getTimestamp());
		header.store();
	}

	private void createPaymentFiles(String fileName1, String fileName2,
			String schoolCategory, IWTimestamp executionDate,
			IWTimestamp paymentDate, ExportDataMapping mapping)
			throws IOException {

		Collection headers = null;
		try {
			headers = ((PaymentHeaderHome) IDOLookup
					.getHome(PaymentHeader.class))
					.findBySchoolCategoryAndStatus(schoolCategory, 'P');
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		StringBuffer empty = new StringBuffer("");
		for (int i = 0; i < 60; i++) {
			empty.append("          ");
		}

		NumberFormat format = NumberFormat.getInstance(currentLocale);
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);
		format.setMinimumIntegerDigits(12);
		format.setMaximumIntegerDigits(12);
		format.setGroupingUsed(false);
		NumberFormat format2 = NumberFormat.getInstance(currentLocale);
		format2.setMaximumFractionDigits(0);
		format2.setMinimumFractionDigits(0);
		format2.setMinimumIntegerDigits(9);
		format2.setMaximumIntegerDigits(9);
		format2.setGroupingUsed(false);
		NumberFormat format3 = NumberFormat.getInstance(currentLocale);
		format3.setMaximumFractionDigits(0);
		format3.setMinimumFractionDigits(0);
		format3.setMinimumIntegerDigits(7);
		format3.setMaximumIntegerDigits(7);
		format3.setGroupingUsed(false);
		NumberFormat format4 = NumberFormat.getInstance(currentLocale);
		format4.setMaximumFractionDigits(0);
		format4.setMinimumFractionDigits(0);
		format4.setMinimumIntegerDigits(16);
		format4.setMaximumIntegerDigits(16);
		format4.setGroupingUsed(false);
		// Payment file
		if (headers != null && !headers.isEmpty()) {
			Iterator it = headers.iterator();
			FileWriter writer = null;
			try {
				writer = new FileWriter(fileName1);
			} catch (IOException e4) {
				e4.printStackTrace();
			}
			BufferedWriter bWriter = new BufferedWriter(writer);

			bWriter.write("10");
			bWriter.write("460");
			bWriter.write("01602");
			bWriter.write(executionDate.getDateString("yyyyMMdd"));
			bWriter.write(executionDate.getDateString("hhmmss"));
			bWriter.write(empty.substring(0, 560));
			bWriter.newLine();

			int numberOfHeaders = 0;
			int numberOfRecords = 0;
			long totalAmount = 0;
			String vernr = mapping.getJournalNumber();
			if (vernr == null) {
				vernr = "1";
			}

			int vernrInt = Integer.valueOf(vernr).intValue();

			while (it.hasNext()) {
				PaymentHeader header = (PaymentHeader) it.next();
				boolean includeHeader = false;
				boolean isPostGiro = false;
				String giroString = "";
				String pGiroString = "";
				String bGiroString = "";
				String giroText = "";
				if (header.getSchool() != null) {
					Provider provider = getEconomaBusiness()
							.getProviderBusiness().getProvider(
									header.getSchoolID());
					if (provider.getAccountingProperties().getBankgiro() != null) {
						includeHeader = true;
						giroString = provider.getAccountingProperties()
								.getBankgiro();
						bGiroString = giroString;
					}

					if (provider.getAccountingProperties().getPostgiro() != null) {
						includeHeader = true;
						isPostGiro = true;
						giroString = provider.getAccountingProperties()
								.getPostgiro();
						pGiroString = giroString;
					}

					giroText = provider.getAccountingProperties().getGiroText();
				}

				if (includeHeader) {
					bWriter.write("90");
					bWriter.write("01602");
					bWriter.write("10");
					if (isPostGiro) {
						bWriter.write("P");
					} else {
						bWriter.write("B");
					}
					if (giroString.length() < 15) {
						StringBuffer p = new StringBuffer(giroString);
						while (p.length() < 15)
							p.append(' ');
						giroString = p.toString();
					} else if (giroString.length() > 15) {
						giroString = giroString.substring(0, 15);
					}
					bWriter.write(giroString);
					if (isPostGiro) {
						bWriter.write("P");
					} else {
						bWriter.write("B");
					}
					if (bGiroString.length() < 10) {
						StringBuffer p = new StringBuffer(bGiroString);
						while (p.length() < 10)
							p.append(' ');
						bGiroString = p.toString();
					} else if (bGiroString.length() > 10) {
						bGiroString = bGiroString.substring(0, 10);
					}
					bWriter.write(bGiroString);
					if (pGiroString.length() < 15) {
						StringBuffer p = new StringBuffer(pGiroString);
						while (p.length() < 15)
							p.append(' ');
						pGiroString = p.toString();
					} else if (pGiroString.length() > 15) {
						pGiroString = pGiroString.substring(0, 15);
					}
					bWriter.write(pGiroString);
					bWriter.write("30");
					String sName = header.getSchool().getName();
					if (sName.length() < 33) {
						StringBuffer p = new StringBuffer(sName);
						while (p.length() < 33)
							p.append(' ');
						sName = p.toString();
					} else if (sName.length() > 33) {
						sName = sName.substring(0, 33);
					}
					bWriter.write(sName);
					String sAddress = header.getSchool().getSchoolAddress();
					if (sAddress.length() < 27) {
						StringBuffer p = new StringBuffer(sAddress);
						while (p.length() < 27)
							p.append(' ');
						sAddress = p.toString();
					} else if (sAddress.length() > 27) {
						sAddress = sAddress.substring(0, 27);
					}
					bWriter.write(sAddress);
					String sZipCode = header.getSchool().getSchoolZipCode();
					
					sZipCode = sZipCode.replaceAll("\\s", "");
					if (sZipCode.length() < 10) {
						StringBuffer p = new StringBuffer(sZipCode);
						while (p.length() < 10)
							p.append(' ');
						sZipCode = p.toString();
					} else if (sZipCode.length() > 10) {
						sZipCode = sZipCode.substring(0, 10);
					}
					bWriter.write(sZipCode);
					String sZipArea = header.getSchool().getSchoolZipArea();
					if (sZipArea.length() < 20) {
						StringBuffer p = new StringBuffer(sZipArea);
						while (p.length() < 20)
							p.append(' ');
						sZipArea = p.toString();
					} else if (sZipCode.length() > 20) {
						sZipArea = sZipArea.substring(0, 20);
					}
					bWriter.write(sZipArea);
					bWriter.write(empty.substring(0, 365));
					bWriter.newLine();
					numberOfHeaders++;

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
						while (rIt.hasNext()) {
							PaymentRecord rec = (PaymentRecord) rIt.next();
							if (rec.getTotalAmount() != 0.0f) {
								bWriter.write("91");
								bWriter.write("01602");
								bWriter.write("12");
								if (isPostGiro) {
									bWriter.write("P");
								} else {
									bWriter.write("B");
								}
								bWriter.write(giroString);
								long am = AccountingUtil.roundAmount(rec
										.getTotalAmount());
								am *= 100;
								if (am > 0) {
									bWriter.write("FA");									
								} else {
									bWriter.write("KR");
								}
								am = Math.abs(am);
								String faknr = Integer.toString(vernrInt);
								if (faknr.length() < 27) {
									StringBuffer p = new StringBuffer(faknr);
									while (p.length() < 27)
										p.append(' ');
									faknr = p.toString();
								} else if (faknr.length() > 27) {
									faknr = faknr.substring(0, 27);
								}
								bWriter.write(faknr);
								totalAmount += am;
								bWriter.write(format.format(am));
								bWriter.write(empty.substring(0, 5));
								bWriter.write(paymentDate
										.getDateString("yyyyMMdd"));
								if (isPostGiro) {
									bWriter.write("P");
								} else {
									bWriter.write("B");
								}
								bWriter.write(giroString);
								bWriter.write(sName);
								bWriter.write(sAddress);
								sZipCode = header.getSchool()
										.getSchoolZipCode();
								sZipCode = sZipCode.replaceAll(" ", "");
								if (sZipCode.length() < 5) {
									StringBuffer p = new StringBuffer(sZipCode);
									while (p.length() < 5)
										p.append(' ');
									sZipCode = p.toString();
								} else if (sZipCode.length() > 5) {
									sZipCode = sZipCode.substring(0, 5);
								}
								bWriter.write(sZipCode);
								sZipArea = header.getSchool()
										.getSchoolZipArea();
								if (sZipArea.length() < 20) {
									StringBuffer p = new StringBuffer(sZipArea);
									while (p.length() < 20)
										p.append(' ');
									sZipArea = p.toString();
								} else if (sZipCode.length() > 20) {
									sZipArea = sZipArea.substring(0, 20);
								}
								bWriter.write(sZipArea);
								String vernrString = Integer.toString(vernrInt);
								if (vernrString.length() < 9) {
									StringBuffer p = new StringBuffer(vernrString);
									while (p.length() < 9)
										p.append(' ');
									vernrString = p.toString();
								} else if (vernrString.length() > 9) {
									vernrString = vernrString.substring(0, 9);
								}
								bWriter.write(vernrString);
								bWriter.write(paymentDate
										.getDateString("yyyyMMdd"));
								bWriter.write(empty.substring(0, 12));
								bWriter.write(empty.substring(0, 2));
//								if (isPostGiro) {
									bWriter.write(" ");
//								} else {
//									bWriter.write("B");
//								}
								String text = rec.getPaymentText();
								if (text.length() < 40) {
									StringBuffer p = new StringBuffer(text);
									while (p.length() < 40)
										p.append(' ');
									text = p.toString();
								} else if (text.length() > 40) {
									text = text.substring(0, 40);
								}
								bWriter.write(text);
								if (giroText == null) {
									bWriter.write(empty.substring(0, 40));
								} else {
									if (giroText.length() < 40) {
										StringBuffer p = new StringBuffer(
												giroText);
										while (p.length() < 40)
											p.append(' ');
										giroText = p.toString();
									} else if (giroText.length() > 40) {
										giroText = giroText.substring(0, 40);
									}
									bWriter.write(giroText);
								}
								bWriter.write(empty.substring(0, 40));
								bWriter.write(empty.substring(0, 40));
								bWriter.write(empty.substring(0, 40));
								bWriter.write(empty.substring(0, 40));
								bWriter.write(empty.substring(0, 40));
								bWriter.write(empty.substring(0, 40));
								bWriter.write(empty.substring(0, 4));
								bWriter.write(empty.substring(0, 33));
								bWriter.write("SEK");
								bWriter.write(empty.substring(0, 3));
								bWriter.write(empty.substring(0, 6));
								bWriter.write(empty.substring(0, 3));

								bWriter.newLine();
								numberOfRecords++;

								rec.setVernr(format2.format(vernrInt));
								vernrInt++;
							}

							rec.setStatus('L');
							rec.store();
						}
					}
				}

				header.setStatus('L');
				header.store();
			}
			mapping.setJournalNumber(Integer.toString(vernrInt));
			mapping.store();

			bWriter.write("50");
			bWriter.write(format3.format(numberOfHeaders));
			bWriter.write(format3.format(numberOfRecords));
			bWriter.write(format4.format(totalAmount));
			bWriter.write(empty.substring(0, 10));
			bWriter.newLine();

			bWriter.close();
		}

		NumberFormat format5 = NumberFormat.getInstance(currentLocale);
		format5.setMaximumFractionDigits(0);
		format5.setMinimumFractionDigits(0);
		format5.setMinimumIntegerDigits(10);
		format5.setMaximumIntegerDigits(10);
		format5.setGroupingUsed(false);
		NumberFormat format6 = NumberFormat.getInstance(currentLocale);
		format6.setMaximumFractionDigits(0);
		format6.setMinimumFractionDigits(0);
		format6.setMinimumIntegerDigits(160);
		format6.setMaximumIntegerDigits(160);
		format6.setGroupingUsed(false);
		NumberFormat format7 = NumberFormat.getInstance(currentLocale);
		format7.setMaximumFractionDigits(0);
		format7.setMinimumFractionDigits(0);
		format7.setMinimumIntegerDigits(15);
		format7.setMaximumIntegerDigits(15);
		format7.setGroupingUsed(false);
		NumberFormat format8 = NumberFormat.getInstance(currentLocale);
		format8.setMaximumFractionDigits(0);
		format8.setMinimumFractionDigits(0);
		format8.setMinimumIntegerDigits(14);
		format8.setMaximumIntegerDigits(14);
		format8.setGroupingUsed(false);

		// Bookkeeping file
		if (headers != null && !headers.isEmpty()) {
			Iterator it = headers.iterator();
			FileWriter writer = new FileWriter(fileName2);
			BufferedWriter bWriter = new BufferedWriter(writer);
			PostingBusiness pb = getEconomaBusiness().getPostingBusiness();

			bWriter.write("10");
			bWriter.write("430");
			bWriter.write("01602");
			bWriter.write("720");
			bWriter.write(executionDate.getDateString("yyMMdd"));
			bWriter.write(executionDate.getDateString("hhmmss"));
			bWriter.write(empty.substring(0, 3));
			bWriter.write(empty.substring(0, 222));
			bWriter.newLine();

			int numberOfRecords = 0;
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

				if (records != null && !records.isEmpty()) {
					Iterator rIt = records.iterator();
					while (rIt.hasNext()) {
						PaymentRecord rec = (PaymentRecord) rIt.next();
						if (rec.getTotalAmount() != 0.0f) {
							bWriter.write("20");
							bWriter.write("01602");
							bWriter.write(format5.format(0));
							bWriter.write(("180"));
							bWriter.write(("180"));
							if (rec.getVernr() != null) {
								bWriter.write(rec.getVernr());
							} else {
								String vernr = mapping.getJournalNumber();
								if (vernr == null) {
									vernr = "1";
								}

								int vernrInt = Integer.valueOf(vernr).intValue();								
								
								bWriter.write(format2.format(vernrInt));
								rec.setVernr(format2.format(vernrInt));
								vernrInt++;
								mapping.setJournalNumber(Integer.toString(vernrInt));
								mapping.store();
							}
							bWriter.write(paymentDate.getDateString("yy"));
							bWriter.write(paymentDate.getDateString("MM"));
							bWriter.write("00");
							bWriter.write(paymentDate.getDateString("dd"));
							String own1 = pb.findFieldInStringByName(rec
									.getOwnPosting(), "Ansvar");
							String own2 = pb.findFieldInStringByName(rec
									.getOwnPosting(), "Konto");
							String own3 = pb.findFieldInStringByName(rec
									.getOwnPosting(), "Verksamhet");
							if (own1.length() < 10) {
								StringBuffer p = new StringBuffer(own1);
								while (p.length() < 10)
									p.append(' ');
								own1 = p.toString();
							} else if (own1.length() > 10) {
								own1 = own1.substring(0, 10);
							}
							bWriter.write(own1);
							if (own2.length() < 10) {
								StringBuffer p = new StringBuffer(own2);
								while (p.length() < 10)
									p.append(' ');
								own2 = p.toString();
							} else if (own2.length() > 10) {
								own2 = own2.substring(0, 10);
							}
							bWriter.write(own2);
							if (own3.length() < 10) {
								StringBuffer p = new StringBuffer(own3);
								while (p.length() < 10)
									p.append(' ');
								own3 = p.toString();
							} else if (own3.length() > 10) {
								own3 = own3.substring(0, 10);
							}
							bWriter.write(own3);
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							if (rec.getTotalAmount() < 0.0f) {
								bWriter.write(format8.format(Math.abs(AccountingUtil
										.roundAmount(rec.getTotalAmount()) * 10)));
								bWriter.write("p");
							} else {
								bWriter.write(format7.format(AccountingUtil
									.roundAmount(rec.getTotalAmount()) * 100));
							}
							bWriter.write(format7.format(0));
							bWriter.write("00000000000");
							bWriter.write(empty.substring(0, 12));
							String text = rec.getPaymentText();
							if (text.length() < 36) {
								StringBuffer p = new StringBuffer(text);
								while (p.length() < 36)
									p.append(' ');
								text = p.toString();
							} else if (text.length() > 36) {
								text = text.substring(0, 36);
							}
							bWriter.write(text);
							bWriter.write(empty.substring(0, 18));
							bWriter.write(empty.substring(0, 3));
							bWriter.newLine();
							numberOfRecords++;

							bWriter.write("20");
							bWriter.write("01602");
							bWriter.write(format5.format(0));
							bWriter.write(("180"));
							bWriter.write(("180"));
							if (rec.getVernr() != null) {
								bWriter.write(rec.getVernr());
							} else {
								String vernr = mapping.getJournalNumber();
								if (vernr == null) {
									vernr = "1";
								}

								int vernrInt = Integer.valueOf(vernr).intValue();								
								
								bWriter.write(format2.format(vernrInt));
								rec.setVernr(format2.format(vernrInt));
								vernrInt++;
								mapping.setJournalNumber(Integer.toString(vernrInt));
								mapping.store();
							}
							bWriter.write(paymentDate.getDateString("yy"));
							bWriter.write(paymentDate.getDateString("MM"));
							bWriter.write("00");
							bWriter.write(paymentDate.getDateString("dd"));
							String doublePosting = rec.getDoublePosting();
							if (doublePosting == null
									|| "".equals(doublePosting.trim())) {
								doublePosting = mapping.getPayableAccount().trim();
								if (doublePosting == null) {
									doublePosting = "";
								}
							}
							String double1 = pb.findFieldInStringByName(
									doublePosting, "Ansvar");
							String double2 = pb.findFieldInStringByName(
									doublePosting, "Konto");
							String double3 = pb.findFieldInStringByName(
									doublePosting, "Verksamhet");
							if (double1.length() < 10) {
								StringBuffer p = new StringBuffer(double1);
								while (p.length() < 10)
									p.append(' ');
								double1 = p.toString();
							} else if (double1.length() > 10) {
								double1 = double1.substring(0, 10);
							}
							bWriter.write(double1);
							if (double2.length() < 10) {
								StringBuffer p = new StringBuffer(double2);
								while (p.length() < 10)
									p.append(' ');
								double2 = p.toString();
							} else if (double2.length() > 10) {
								double2 = double2.substring(0, 10);
							}
							bWriter.write(double2);
							if (double3.length() < 10) {
								StringBuffer p = new StringBuffer(double3);
								while (p.length() < 10)
									p.append(' ');
								double3 = p.toString();
							} else if (double3.length() > 10) {
								double3 = double3.substring(0, 10);
							}
							bWriter.write(double3);
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							bWriter.write(empty.substring(0, 10));
							if (rec.getTotalAmount() <= 0.0f) {
								bWriter.write(format7.format(Math.abs(AccountingUtil
										.roundAmount(rec.getTotalAmount()) * 100)));
							} else {
								bWriter.write(format8.format(AccountingUtil
									.roundAmount(rec.getTotalAmount()) * 10));
								bWriter.write("p");
							}
							bWriter.write(format7.format(0));
							bWriter.write("00000000000");
							bWriter.write(empty.substring(0, 12));
							bWriter.write(text);
							bWriter.write(empty.substring(0, 18));
							bWriter.write(empty.substring(0, 3));
							bWriter.newLine();
							numberOfRecords++;
						}

						rec.setStatus('L');
						rec.store();
					}
				}

				header.setStatus('L');
				header.store();
			}

			bWriter.write("50");
			bWriter.write(format5.format(numberOfRecords));
			bWriter.write(format6.format(0));
			bWriter.write(empty.substring(0, 78));
			bWriter.newLine();

			bWriter.close();
		}
	}

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
				float sum = 0.0f;
				while (it2.hasNext()) {
					InvoiceRecord r2 = (InvoiceRecord) it2.next();
					if (!createInvoice) {
					if (r2.getProvider() == null) {
						createInvoice = true;
					} else {
						Provider provider = getEconomaBusiness()
								.getProviderBusiness().getProvider(
										r2.getProviderId());
						if (provider.getAccountingProperties()
								.getCreateInvoiceRecord()) {
							createInvoice = true;
						}
					}
					}
					
					sum += r2.getAmount();
				}
				
				if (sum == 0.0f) {
					createInvoice = false;
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
									boolean addRecord = false;
									if (iRec.getProvider() == null) {
										addRecord = true;
									} else {
										Provider provider = getEconomaBusiness()
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
										String localized = iwac
												.getIWMainApplication()
												.getBundle(IW_BUNDLE_IDENTIFIER)
												.getResourceBundle(
														currentLocale)
												.getLocalizedString("Check",
														"Check");
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
								StringTokenizer token = new StringTokenizer(
										text, ",");
								if (token.hasMoreTokens()) {
									text = token.nextToken();
								}
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
											type50counterString = format3.format(type50counter);
											bWriter.write(type50counterString);
											// radnrtxt
											bWriter.write("000");
											// fomdat
											t = new IWTimestamp(regRecord
													.getPeriodStartCheck());
											bWriter.write(t
													.getDateString("yyyyMMdd"));
											// tomdat
											t = new IWTimestamp(regRecord
													.getPeriodEndCheck());
											bWriter.write(t
													.getDateString("yyyyMMdd"));
											// artnr
											bWriter.write("INGEN-MOMS   ");
											// benamn
											text = regRecord.getInvoiceText();
											if (text == null)
												text = "";
											if (text.length() < 30) {
												StringBuffer tb = new StringBuffer(
														text);
												while (tb.length() < 30) {
													tb.append(' ');
												}
												text = tb.toString();
											} else if (text.length() > 30) {
												text = text.substring(0, 30);
											}
											bWriter.write(text);
											// avsrnamn
											bWriter.write(empty
													.substring(0, 30));
											// konto
											postingString = new StringBuffer(
													regRecord.getOwnPosting());
											while (postingString.length() < 45) {
												postingString.append(' ');
											}
											bWriter.write(postingString
													.toString());
											// ktonamn
											bWriter.write(empty
													.substring(0, 30));
											// signata
											bWriter
													.write(empty
															.substring(0, 1));
											// antal
											bWriter.write("00000000000");
											// sort
											bWriter
													.write(empty
															.substring(0, 6));
											// avsrper
											bWriter.write(empty
													.substring(0, 13));
											// signapris
											bWriter
													.write(empty
															.substring(0, 1));
											// apris
											bWriter.write("0000000000000");
											// signrabs
											bWriter
													.write(empty
															.substring(0, 1));
											// rabsats
											bWriter.write("00000");
											// signbel
											am = AccountingUtil
													.roundAmount(regRecord
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
											bWriter
													.write(empty
															.substring(0, 1));
											// momssats
											bWriter.write("0000");
											// signmbel
											bWriter
													.write(empty
															.substring(0, 1));
											// momsbel
											bWriter.write("0000000000000");
											// omrade
											bWriter
													.write(empty
															.substring(0, 3));
											// kategori
											bWriter
													.write(empty
															.substring(0, 3));
											// Filler
											bWriter.write(empty
													.substring(0, 15));

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
												StringBuffer tb = new StringBuffer(
														text);
												while (tb.length() < 60) {
													tb.append(' ');
												}
												text = tb.toString();
											} else if (text.length() > 60) {
												text = text.substring(0, 60);
											}
											bWriter.write(text);
											// filler
											bWriter.write(empty.substring(0,
													206));
											bWriter.newLine();
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
									IWTimestamp t = new IWTimestamp(regRecord
											.getPeriodStartCheck());
									bWriter.write(t.getDateString("yyyyMMdd"));
									// tomdat
									t = new IWTimestamp(regRecord
											.getPeriodEndCheck());
									bWriter.write(t.getDateString("yyyyMMdd"));
									// artnr
									bWriter.write("INGEN-MOMS   ");
									// benamn
									String text = regRecord.getInvoiceText();
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
									StringBuffer postingString = new StringBuffer(
											regRecord.getOwnPosting());
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
									long am = AccountingUtil
											.roundAmount(regRecord.getAmount());
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