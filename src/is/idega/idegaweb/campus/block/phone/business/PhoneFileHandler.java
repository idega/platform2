package is.idega.idegaweb.campus.block.phone.business;

import java.io.*;
import com.idega.block.finance.data.*;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import com.idega.data.EntityBulkUpdater;
import com.idega.util.IWTimestamp;
import is.idega.idegaweb.campus.data.AccountPhone;
import is.idega.idegaweb.campus.block.phone.data.PhoneFileInfo;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public class PhoneFileHandler {

	public PhoneFileHandler() {

	}

	public List parseFile(File PhoneFile) {

		List L = null;
		try {
			FileReader fin = new FileReader(PhoneFile);
			LineNumberReader lin = new LineNumberReader(fin);
			String line = null;
			StringTokenizer st;
			StringBuffer sb;
			String anumber, snumber, bnumber;
			int nightsec, daysec, sec;
			float price;
			IWTimestamp stamp;
			int count = 10;
			int linecount = 1;
			Vector V = new Vector();
			AccountPhoneEntry ape;
			while ((line = lin.readLine()) != null) { //&& count != 0){
				st = new StringTokenizer(line, ";");
				sb = new StringBuffer();
				if (st.countTokens() == 8) {
					try {
						anumber = st.nextToken().trim();
						snumber = st.nextToken().trim();
						bnumber = st.nextToken().trim();
						String s = st.nextToken().trim();
						stamp = parseStamp(s);
						nightsec = Integer.parseInt(st.nextToken());
						daysec = Integer.parseInt(st.nextToken());
						sec = Integer.parseInt(st.nextToken());
						price = Float.parseFloat(st.nextToken());

						ape = ((com.idega.block.finance.data.AccountPhoneEntryHome) com.idega.data.IDOLookup.getHomeLegacy(AccountPhoneEntry.class)).createLegacy();
						ape.setLastUpdated(IWTimestamp.getTimestampRightNow());
						ape.setDayDuration(daysec);
						ape.setNightDuration(nightsec);
						ape.setDuration(sec);
						ape.setMainNumber(anumber);
						ape.setPhonedStamp(stamp.getTimestamp());
						ape.setPhoneNumber(bnumber);
						ape.setPrice(-price);
						ape.setSubNumber(snumber);
						ape.setStatus(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusUnread);
						V.add(ape);
					}
					catch (Exception ex) {
						System.err.println("error in line " + linecount);
					}
					L = V;

				}
				else {
					System.err.println("error in line " + linecount + " : too few columns");
				}

				//System.err.println(sb.toString());
				count--;
				linecount++;
			}
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException fnfe) {
			fnfe.printStackTrace();
		}
		return L;
	}

	public IWTimestamp parseStamp(String stamp) {
		StringBuffer st = new StringBuffer();
		if (stamp != null && stamp.length() >= 19) {
			st.append(stamp.substring(0, 10));
			st.append(" ");
			st.append(stamp.substring(11, 13));
			st.append(":");
			st.append(stamp.substring(14, 16));
			st.append(":");
			st.append(stamp.substring(17, 19));
		}
		return new IWTimestamp(st.toString());
	}

	public void process(File PhoneFile) throws java.rmi.RemoteException {
		process3(PhoneFile);
	}

	public void process3(File PhoneFile) throws java.rmi.RemoteException {
		Map M = PhoneFinder.mapOfAccountPhoneListsByPhoneNumber(null);
		Map M2 = PhoneFinder.mapOfAccountsWithPhoneNumber();

		// If we can assess something
		if (M != null && M2 != null) {
			try {
				long phonetime = -1;
				long from = -1;
				long to = -1;

				FileReader fin = new FileReader(PhoneFile);
				LineNumberReader lin = new LineNumberReader(fin);

				javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
				try {
					t.begin();
					int entryFactor = -1;
					String line = null;
					StringTokenizer st;
					StringBuffer sbError = new StringBuffer(), sbNoAccount = new StringBuffer();
					;
					String anumber, snumber, bnumber;
					int nightsec, daysec, sec;
					float price = 0, totPrice = 0;
					IWTimestamp stamp;
					int count = 10;
					int linecount = 1, noAccountCount = 0, errorCount = 0, numberCount = 0;
					Vector vError = new Vector();
					Vector vNoAccount = new Vector();
					Hashtable phoneNumbers = new Hashtable();
					AccountPhoneEntry ape;
					Integer iAccountId;
					Account eAccount;
					AccountPhone ap;
					List accountList;
					int listsize;
					boolean cont = false;
					while ((line = lin.readLine()) != null) { //&& count != 0){
						cont = false;
						st = new StringTokenizer(line, ";");
						if (st.countTokens() == 8) {
							ape = ((com.idega.block.finance.data.AccountPhoneEntryHome) com.idega.data.IDOLookup.getHomeLegacy(AccountPhoneEntry.class)).createLegacy();
							try {
								anumber = st.nextToken().trim();
								snumber = st.nextToken().trim();
								bnumber = st.nextToken().trim();
								String s = st.nextToken().trim();
								stamp = parseStamp(s);
								nightsec = Integer.parseInt(st.nextToken());
								daysec = Integer.parseInt(st.nextToken());
								sec = Integer.parseInt(st.nextToken());
								price = Float.parseFloat(st.nextToken());
								ape.setLastUpdated(IWTimestamp.getTimestampRightNow());
								ape.setDayDuration(daysec);
								ape.setNightDuration(nightsec);
								ape.setDuration(sec);
								ape.setMainNumber(anumber);
								ape.setPhonedStamp(stamp.getTimestamp());
								ape.setPhoneNumber(bnumber);
								ape.setPrice(entryFactor * price);
								ape.setSubNumber(snumber);
								ape.setStatus(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusUnread);
								phonetime = ape.getPhonedStamp().getTime();
								cont = true;
							}
							catch (Exception ex) {
								System.err.println("error in line " + linecount);
								cont = false;
							}
							// valid line in file
							if (cont) {
								String number = ape.getSubNumber();
								// account for phonenumber exist
								if (phoneNumbers.containsKey(number)) {
									Integer ncount = (Integer) phoneNumbers.get(number);
									phoneNumbers.put(number, new Integer(ncount.intValue() + 1));
								}
								else {
									phoneNumbers.put(number, new Integer(1));
									numberCount++;
								}

								if (M.containsKey(number)) {
									accountList = (List) M.get(number);
									if (accountList != null) {
										listsize = accountList.size();
										for (int i = 0; i < listsize; i++) {
											ap = (AccountPhone) accountList.get(i);
											from = ap.getValidFrom().getTime();
											to = ap.getValidTo().getTime();
											if( phonetime >= from && phonetime <= to){
//											if (phonetime <= to) {
												//System.err.println("ape "+ape.getSubNumber()+" account "+ap.getAccountId().intValue());
												ape.setAccountId(ap.getAccountId());
												ape.setStatus(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusRead);
												if (M2.containsKey(ap.getAccountId())) {
													eAccount = (Account) M2.get(ap.getAccountId());
													eAccount.addAmount(ape.getPrice());
													M2.put(ap.getAccountId(), eAccount);
												}
												ape.insert();
												totPrice += ape.getPrice();
												break;
											}
										}
									}
								}
								// account for phonenumber doesn´t exist
								else {
									sbNoAccount.append(line);
									sbNoAccount.append("\n");
									noAccountCount++;
								}
							}
							// invalid line in file
							else {
								System.err.println("error in line " + linecount + " : parsing error");
								sbError.append(line);
								sbError.append("\n");
								errorCount++;
							}
						}
						else {
							System.err.println("error in line " + linecount + " : too few columns");
							sbError.append(line);
							sbError.append("\n");
							errorCount++;
						}
						count--;
						linecount++;
					} // while

					Iterator It = M2.values().iterator();
					while (It.hasNext()) {
						eAccount = (Account) It.next();
						eAccount.store();
					}

					PhoneFileInfo pfi = ((is.idega.idegaweb.campus.block.phone.data.PhoneFileInfoHome) com.idega.data.IDOLookup.getHomeLegacy(PhoneFileInfo.class)).createLegacy();
					pfi.setDateRead(IWTimestamp.getTimestampRightNow());
					pfi.setLineCount(linecount - 1);
					pfi.setErrorCount(errorCount);
					pfi.setNoAccountCount(noAccountCount);
					pfi.setFileName(PhoneFile.getName());
					pfi.setNumberCount(numberCount);
					pfi.setTotalAmount(totPrice);
					pfi.insert();

					if (errorCount > 0) {
						FileWriter out = new FileWriter(new File(PhoneFile.getParentFile(), "e_" + PhoneFile.getName()));
						BufferedWriter bout = new BufferedWriter(out);
						bout.write(sbError.toString());
						bout.close();
						out.close();
					}
					if (noAccountCount > 0) {
						FileWriter out = new FileWriter(new File(PhoneFile.getParentFile(), "n_" + PhoneFile.getName()));
						BufferedWriter bout = new BufferedWriter(out);
						bout.write(sbNoAccount.toString());
						bout.close();
						out.close();
					}

					t.commit();
				}
				catch (Exception e) {
					try {
						t.rollback();
					}
					catch (javax.transaction.SystemException ex) {
						ex.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			}
			/*catch(IOException fnfe){
			  fnfe.printStackTrace();
			}*/
		}
		else {
			System.err.println("no accounts behind phonenumbers");
		}
	}

	public void processFile(String fileName) {
		//System.err.println(fileName);
		if (fileName != null) {
			try {
				File phoneFile = new File(fileName);
				process3(phoneFile);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			System.err.println("no filename in Campus phonefilehandler");
		}
	}

	public void process2(File PhoneFile) {

		//Map M = PhoneFinder.mapOfAccountsListsByPhoneNumber();
		// If we can assess something
		//if( M != null ){
		try {

			FileReader fin = new FileReader(PhoneFile);
			LineNumberReader lin = new LineNumberReader(fin);
			EntityBulkUpdater bulk = new EntityBulkUpdater();

			String line = null;
			StringTokenizer st;
			StringBuffer sbError = new StringBuffer(), sbNoAccount = new StringBuffer();
			String anumber, snumber, bnumber;
			int nightsec, daysec, sec;
			float price, totPrice = 0;
			IWTimestamp stamp;
			int count = 10;
			int linecount = 1, noAccountCount = 0, errorCount = 0, numberCount = 0;
			Vector vError = new Vector();
			Vector vNoAccount = new Vector();
			Vector vEntries = new Vector();
			Hashtable phoneNumbers = new Hashtable();
			Hashtable phoneEntries = new Hashtable();
			IWTimestamp from = IWTimestamp.RightNow();
			IWTimestamp to = IWTimestamp.RightNow();
			AccountPhoneEntry ape;
			Integer iAccountId;
			Account eAccount;
			boolean cont = false;
			List entryList;
			while ((line = lin.readLine()) != null) { //&& count != 0){
				cont = false;
				st = new StringTokenizer(line, ";");
				if (st.countTokens() == 8) {
					ape = ((com.idega.block.finance.data.AccountPhoneEntryHome) com.idega.data.IDOLookup.getHomeLegacy(AccountPhoneEntry.class)).createLegacy();
					try {
						anumber = st.nextToken().trim();
						snumber = st.nextToken().trim();
						bnumber = st.nextToken().trim();
						String s = st.nextToken().trim();
						stamp = parseStamp(s);
						nightsec = Integer.parseInt(st.nextToken());
						daysec = Integer.parseInt(st.nextToken());
						sec = Integer.parseInt(st.nextToken());
						price = Float.parseFloat(st.nextToken());
						ape.setLastUpdated(IWTimestamp.getTimestampRightNow());
						ape.setDayDuration(daysec);
						ape.setNightDuration(nightsec);
						ape.setDuration(sec);
						ape.setMainNumber(anumber);
						ape.setPhonedStamp(stamp.getTimestamp());
						ape.setPhoneNumber(bnumber);
						ape.setPrice(price);
						ape.setSubNumber(snumber);
						ape.setStatus(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusUnread);
						vEntries.add(ape);
						cont = true;
					}
					catch (Exception ex) {
						System.err.println("error in line " + linecount);
						cont = false;
					}
					// valid line in file
					if (cont) {
						String number = ape.getSubNumber();
						// account for phonenumber exist
						if (phoneNumbers.containsKey(number)) {
							Integer ncount = (Integer) phoneNumbers.get(number);
							phoneNumbers.put(number, new Integer(ncount.intValue() + 1));
						}
						else {
							phoneNumbers.put(number, new Integer(1));
							numberCount++;
						}
					}
					// invalid line in file
					else {
						System.err.println("error in line " + linecount + " : parsing error");
						sbError.append(line);
						sbError.append("\n");
						errorCount++;
					}
				}
				else {
					System.err.println("error in line " + linecount + " : too few columns");
					sbError.append(line);
					sbError.append("\n");
					errorCount++;
				}
				count--;
				linecount++;
			} // while

			Map M = PhoneFinder.mapOfAccountPhoneListsByPhoneNumber(from);
			if (M != null && vEntries.size() > 0) {
				String number;
				AccountPhone ap;
				List accountList;

				int listsize;
				Iterator it = vEntries.iterator();
				while (it.hasNext()) {
					ape = (AccountPhoneEntry) it.next();
					number = ape.getSubNumber();
					if (M != null && M.containsKey(number)) {
						accountList = (List) M.get(number);
						if (accountList != null) {
							listsize = accountList.size();
							for (int i = 0; i < listsize; i++) {
								ap = (AccountPhone) accountList.get(i);
								if (ape.getPhonedStamp().getTime() <= ap.getValidTo().getTime()) {
									System.err.println("ape " + ape.getSubNumber() + " account" + ap.getAccountId().intValue());
									ape.setAccountId(ap.getAccountId());
								}
							}

						}
						bulk.add(ape, bulk.insert);
					}
					// account for phonenumber doesn´t exist
					else {
						sbNoAccount.append(line);
						sbNoAccount.append("\n");
						noAccountCount++;
					}
				}
			}
			else
				System.err.println(" no accounts ");

			PhoneFileInfo pfi = ((is.idega.idegaweb.campus.block.phone.data.PhoneFileInfoHome) com.idega.data.IDOLookup.getHomeLegacy(PhoneFileInfo.class)).createLegacy();
			pfi.setDateRead(IWTimestamp.getTimestampRightNow());
			pfi.setLineCount(linecount - 1);
			pfi.setErrorCount(errorCount);
			pfi.setNoAccountCount(noAccountCount);
			pfi.setFileName(PhoneFile.getName());
			pfi.setNumberCount(numberCount);
			pfi.setTotalAmount(totPrice);
			bulk.add(pfi, bulk.insert);

			bulk.execute();

			if (errorCount > 0) {
				FileWriter out = new FileWriter(new File(PhoneFile.getParentFile(), "e_" + PhoneFile.getName()));
				BufferedWriter bout = new BufferedWriter(out);
				bout.write(sbError.toString());
				bout.close();
				out.close();
			}
			if (noAccountCount > 0) {
				FileWriter out = new FileWriter(new File(PhoneFile.getParentFile(), "n_" + PhoneFile.getName()));
				BufferedWriter bout = new BufferedWriter(out);
				bout.write(sbNoAccount.toString());
				bout.close();
				out.close();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}