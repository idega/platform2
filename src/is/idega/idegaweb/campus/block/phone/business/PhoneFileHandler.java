package is.idega.idegaweb.campus.block.phone.business;

import is.idega.idegaweb.campus.block.phone.data.PhoneFileInfo;
import is.idega.idegaweb.campus.data.AccountPhone;
import is.idega.idegaweb.campus.data.AccountPhoneHome;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.transaction.TransactionManager;

import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.AccountPhoneEntryHome;
import com.idega.data.IDOLookup;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.IWTimestamp;

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

						ape = ((AccountPhoneEntryHome) IDOLookup.getHome(AccountPhoneEntry.class)).create();
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

	public void process(File PhoneFile) throws java.rmi.RemoteException,FinderException {
		process3(PhoneFile);
	}

	public void process3(File PhoneFile) throws java.rmi.RemoteException,FinderException {
	//	Map M = PhoneFinder.mapOfAccountPhoneListsByPhoneNumber(null);
		Map M2 = PhoneFinder.mapOfAccountsWithPhoneNumber();
		//DateFormat  df = DateFormat.getDateInstance(DateFormat.SHORT,new Locale("is","IS"));
		// If we can assess something
		if (/*M != null &&*/ M2 != null) {
			try {
				long phonetime = -1;
				long from = -1;
				long to = -1;
				//long deliverTime = -1;
				//long returnTime = -1;

				FileReader fin = new FileReader(PhoneFile);
				LineNumberReader lin = new LineNumberReader(fin);

				javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
				try {
					t.begin();
					int entryFactor = -1;
					String line = null;
					StringTokenizer st;
					StringBuffer sbError = new StringBuffer(), sbNoAccount = new StringBuffer();
					
					String anumber, snumber, bnumber;
					int nightsec, daysec, sec;
					float price = 0, totPrice = 0;
					IWTimestamp stamp;
					int count = 10;
					int linecount = 1, noAccountCount = 0, errorCount = 0, numberCount = 0;
					//Vector vError = new Vector();
					//Vector vNoAccount = new Vector();
					Hashtable phoneNumbers = new Hashtable();
					AccountPhoneEntry ape;
					//Integer iAccountId;
					Account eAccount;
					AccountPhone ap;
					Collection accountList;
					AccountPhoneEntryHome apeHome = (AccountPhoneEntryHome) IDOLookup.getHome(AccountPhoneEntry.class);
					AccountPhoneHome accountPhoneHome = (AccountPhoneHome) IDOLookup.getHome(AccountPhone.class);
					boolean foundAccount = false;
//					int listsize;
					boolean cont = false;
					while ((line = lin.readLine()) != null) { 
						//&& count != 0){
						//System.err.println();
						foundAccount = false;
						cont = false;
						st = new StringTokenizer(line, ";");
						if (st.countTokens() == 8) {
							ape = apeHome.create();
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
								
								IWTimestamp phonedStamp = new IWTimestamp(ape.getPhonedStamp());
								phonedStamp.setHour(0);
								phonedStamp.setMinute(0);
								phonedStamp.setSecond(0);
								phonedStamp.setMilliSecond(0);
								
								accountList = accountPhoneHome.findByPhoneNumberAndPhonedDate(number, phonedStamp.getDate()); 
								//if (M.containsKey(number)) {
								if(!accountList.isEmpty()){
									//accountList = (List) M.get(number);
									if (accountList != null) {
									//	listsize = accountList.size();
										for (Iterator iter = accountList.iterator(); iter.hasNext(); ) {
											 ap = (AccountPhone) iter.next();
											from = ap.getValidFrom().getTime();
											IWTimestamp iwstamp = new IWTimestamp(ap.getValidTo().getTime());
											iwstamp.setHour(23);
											iwstamp.setMinute(59);
											iwstamp.setSecond(59);
											iwstamp.setMilliSecond(999);
											to = iwstamp.getTime().getTime();
//											deliverTime = ap.getDeliverTime()!=null?ap.getDeliverTime().getTime():from;
//											returnTime = ap.getReturnTime()!=null?ap.getReturnTime().getTime():to;
											
											if(from <= phonetime && phonetime <= to){
												ape.setAccountId(ap.getAccountId());
												ape.setStatus(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusRead);
												if (M2.containsKey(ap.getAccountId())) {
													eAccount = (Account) M2.get(ap.getAccountId());
													eAccount.addAmount(ape.getPrice());
													M2.put(ap.getAccountId(), eAccount);
												}
												ape.store();
												foundAccount=true;
												totPrice += ape.getPrice();
												break;
											}
											else{
												//System.err.println("NOS Contract "+ap.getAccountId().toString()+"\t del:"+df.format(new Date(deliverTime))+" ret:"+df.format(new Date(returnTime))+" phoned:"+new Timestamp(phonetime)+" number:"+number);
											}
										}
										if(!foundAccount){
											sbNoAccount.append(line);
											sbNoAccount.append("\n");
											noAccountCount++;
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
					pfi.store();

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
			TransactionManager t = IdegaTransactionManager.getInstance();

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
			try {
				t.begin();
				while ((line = lin.readLine()) != null) { //&& count != 0){
					cont = false;
					st = new StringTokenizer(line, ";");
					if (st.countTokens() == 8) {
						ape = ((AccountPhoneEntryHome) IDOLookup.getHome(AccountPhoneEntry.class)).create();
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
							ape.store();
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
				pfi.store();
	
		    t.commit();
	    } catch (Exception e) {
	    	e.printStackTrace(System.err);
	      
	      try {
	      	t.rollback();
	      } catch (Exception e1) {
	      	e1.printStackTrace(System.err);
	      }
	
	    }

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