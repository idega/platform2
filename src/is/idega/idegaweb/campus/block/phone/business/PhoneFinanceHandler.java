package is.idega.idegaweb.campus.block.phone.business;

import is.idega.idegaweb.campus.data.ContractAccounts;
import is.idega.idegaweb.campus.data.ContractAccountsHome;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.transaction.TransactionManager;

import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.AccountPhoneEntryBMPBean;
import com.idega.block.finance.data.AccountPhoneEntryHome;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffKey;
import com.idega.block.finance.data.TariffKeyHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.IWTimestamp;
import com.idega.util.database.ConnectionBroker;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class PhoneFinanceHandler implements FinanceHandler {

//	public static float tax = 1.245f;
	public static float tax = 1.0f;
	
	int count = 0;
	public PhoneFinanceHandler() {
	}

	public String getAccountType() {
		return com.idega.block.finance.data.AccountBMPBean.typePhone;
	}

	public boolean rollbackAssessment(IWApplicationContext iwac,Integer assessmentRoundId) {
		StringBuffer sql = new StringBuffer("update ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getEntityTableName());
		sql.append(" set ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameAccountEntryId()).append(" = null ");
		sql.append(" , ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getRoundIdColumnName()).append(" = null ");
		sql.append(" , ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameStatus()).append(" = '").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusRead).append("'");
		sql.append(" where ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getRoundIdColumnName()).append(" = ").append(assessmentRoundId);
		System.err.println(sql.toString());
		TransactionManager t = IdegaTransactionManager.getInstance();
		Connection conn = null;
		Statement stmt = null;
		try {
			t.begin();
			conn = ConnectionBroker.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());
			stmt.executeUpdate(getSQLString(assessmentRoundId));
			stmt.execute("delete from fin_phone_entry where total_price > 0 and fin_assessment_round_id = " + assessmentRoundId);
			stmt.execute("delete from fin_acc_entry where fin_assessment_round_id = " + assessmentRoundId);
			stmt.execute("delete from fin_assessment_round where fin_assessment_round_id = " + assessmentRoundId);

			t.commit();
			return true;
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
		finally {
            if (stmt != null) {
                try {
					stmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
            if (conn != null) {
                ConnectionBroker.freeConnection(conn);
            }
        }
		return false;

		/*
		EntityBulkUpdater bulk = new EntityBulkUpdater();
		Hashtable H = new Hashtable();
		Vector V = new Vector();
		if(iAssessmentRoundId > 0){
		  AssessmentRound AR = ((com.idega.block.finance.data.AssessmentRoundHome)com.idega.data.IDOLookup.getHomeLegacy(AssessmentRound.class)).createLegacy();
		  try{
		    AR = ((com.idega.block.finance.data.AssessmentRoundHome)com.idega.data.IDOLookup.getHomeLegacy(AssessmentRound.class)).findByPrimaryKeyLegacy(iAssessmentRoundId);
		
		  List L = AccountManager.listOfAccountEntries(AR.getID());
		
		  if(L!=null){
		    java.util.Iterator I = L.iterator();
		    AccountEntry ae;
		    Account a;
		    Integer Aid;
		    float Amount;
		    while(I.hasNext()){
		      ae = (AccountEntry) I.next();
		      if(ae.getStatus().equals(ae.statusCreated)){
		        Amount = ae.getTotal();
		        Aid = new Integer(ae.getAccountId());
		        if( H.containsKey( Aid ) ){
		          a = (Account) H.get(Aid);
		        }
		        else{
		          a = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).findByPrimaryKeyLegacy(ae.getAccountId());
		          H.put(new Integer(a.getID()),a);
		        }
		        bulk.add(ae,bulk.delete);
		        // lowering the account
		        a.addKredit( Amount);
		      }
		    }
		  }
		  }
		  catch(Exception ex){ ex.printStackTrace();}
		  bulk.addAll(H.values(),bulk.update);
		  bulk.add(AR,bulk.delete);
		  try{
		  SimpleQuerier.execute(getSQLString(iAssessmentRoundId));
		  bulk.execute();
		   return true;
		  }
		  catch(Exception ex){
		   ex.printStackTrace();
		  }
		
		}
		return false;
		*/
	}

	public static List listOfContractAccounts() {
		try {
			return com.idega.data.EntityFinder.getInstance().findAll(ContractAccounts.class);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Map getMapOfContractsAccountsByPhoneAccountId(Collection listOfContractAccounts) {
		if (listOfContractAccounts != null) {
			Iterator iter = listOfContractAccounts.iterator();
			ContractAccounts conAcc;
			Hashtable H = new Hashtable(listOfContractAccounts.size());
			while (iter.hasNext()) {
				conAcc = (ContractAccounts) iter.next();
				H.put(new Integer(conAcc.getPhoneAccountId()), conAcc);
			}
			return H;
		}
		return null;
	}

	public boolean executeAssessment(IWApplicationContext iwac,Integer categoryId, Integer tariffGroupId, String roundName, Integer cashierId,Integer accountKeyId, IWTimestamp paydate, IWTimestamp start, IWTimestamp end,Integer excessRoundID) {
		try {
			Collection listOfAccounts = ((ContractAccountsHome)IDOLookup.getHome(ContractAccounts.class)).findByPeriodOverLap(start.getDate(),end.getDate());
			//List listOfAccounts = CampusAccountFinder.listOfContractAccounts(start, end);

			//List listOfAccounts = listOfContractAccounts();
			if (listOfAccounts != null) {
				System.err.println("Accounts not null");
				Map M = getMapOfContractsAccountsByPhoneAccountId(listOfAccounts);
				System.err.println("Got map of contracts by phone");
				Map E = new HashMap(listOfAccounts.size());
				System.err.println("Created Hashmap of accounts");
				Collection entries = ((AccountPhoneEntryHome)IDOLookup.getHome(AccountPhoneEntry.class)).findUnbilledByAccountAndPeriod(null,start.getDate(),end.getDate());
				//List entries = FinanceFinder.getInstance().listOfUnBilledPhoneEntries(-1, start, end);
				System.err.println("Got entries");

				AccountPhoneEntry ape;
				ContractAccounts accounts;
				System.out.println("entries = " + entries);
				if (entries != null) {
					System.err.println("Entries not null size = " + entries.size());
					AssessmentRound AR = null;

					Integer roundId = new Integer(-1);
					Integer accountCount = new Integer(0);
					TransactionManager t = IdegaTransactionManager.getInstance();
					try {
						t.begin();
					//try {
						AR = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class)).create();
						AR.setAsNew(roundName);
						AR.setCategoryId(categoryId.intValue());
						AR.setTariffGroupId(tariffGroupId.intValue());
						AR.setType(getAccountType());
						AR.store();
						//try {
							roundId = ((Integer) AR.getPrimaryKey()) ; // + 1; 
						//}
						//catch (RemoteException e) {
						//}
						/*
					}
					catch (SQLException ex) {
						ex.printStackTrace();
						try {
							AR.delete();
						}
						catch (SQLException ex2) {
							ex2.printStackTrace();
							AR = null;
						}
					}

					if (AR != null) {
						TransactionManager t = IdegaTransactionManager.getInstance();

						try {
							t.begin();
							*/
							AccountKey AK = ((AccountKeyHome) IDOLookup.getHome(AccountKey.class)).findByPrimaryKey((accountKeyId));
							TariffKey TK = ((TariffKeyHome) IDOLookup.getHome(TariffKey.class)).findByPrimaryKey(new Integer(AK.getTariffKeyId()));
							Integer phAccId;
							Iterator iter = entries.iterator();
							AccountEntry AE;
							// long maxstamp = 0;
							while (iter.hasNext()) {
								ape = (AccountPhoneEntry) iter.next();
								//if(ape.getPhonedStamp().getTime() > maxstamp)
								//  maxstamp = ape.getPhonedStamp().getTime();
								phAccId = new Integer(ape.getAccountId());
								if (M.containsKey(phAccId)) {
									accounts = (ContractAccounts) M.get(phAccId);
									if (E.containsKey(phAccId)) {
										AE = (AccountEntry) E.get(phAccId);
										AE.setNetto(AE.getNetto() + ape.getPrice());
									}
									else {
										AE = insertEntry(new Integer(accounts.getFinanceAccountId()), roundId, paydate, ape.getPrice(), AK, TK, cashierId);
										E.put(phAccId, AE);
									}
									ape.setAccountEntryId(((Integer)AE.getPrimaryKey()).intValue());
									ape.setLastUpdated(IWTimestamp.getTimestampRightNow());
									ape.setStatus(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusBilled);
									ape.setRoundId(roundId);
									ape.store();
								}
								else {
									System.err.println("Map doesn't contain this accountid " + phAccId);
								}
							}

							if (E.size() > 0) {

								Iterator ents = E.entrySet().iterator();
								AccountEntry entry;
								AccountPhoneEntry phoneEntry;
								Map.Entry me;
								Integer AccountId;
								while (ents.hasNext()) {
									me = (Map.Entry) ents.next();
									entry = (AccountEntry) me.getValue();
									AccountId = (Integer) me.getKey();
									entry.setTotal(entry.getNetto() * tax);
									phoneEntry = ((AccountPhoneEntryHome) IDOLookup.getHome(AccountPhoneEntry.class)).create();
									phoneEntry.setAccountId(AccountId.intValue());
									phoneEntry.setPrice(-1 * entry.getNetto());
									phoneEntry.setRoundId(roundId);
									phoneEntry.setAccountEntryId(((Integer)entry.getPrimaryKey()).intValue());
									phoneEntry.setStatus(AccountPhoneEntryBMPBean.statusBilled);
									//phoneEntry.setPhonedStamp(new IWTimestamp(maxstamp).getTimestamp());
									phoneEntry.store();
									entry.store();
								}
							}
							t.commit();
							return true;
						}
						catch (Exception e) {
							try {
								t.rollback();
							}
							catch (javax.transaction.SystemException ex) {
								ex.printStackTrace();
							}
							/*try {
								AR.delete();
							}
							catch (Exception ex2) {
								ex2.printStackTrace();

							}*/
							e.printStackTrace();
						}
				//	}

				}
				else
					System.err.println("Entries null ");

			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	Iterator I = listOfAccounts.iterator();
	ContractAccounts accounts;
	AssessmentRound AR = null;
	
	int iRoundId = -1;
	int iAccountCount = 0;
	try {
	    AR = ((com.idega.block.finance.data.AssessmentRoundHome)com.idega.data.IDOLookup.getHomeLegacy(AssessmentRound.class)).createLegacy();
	    AR.setAsNew(roundName);
	    AR.setCategoryId(iCategoryId);
	    AR.setTariffGroupId(iTariffGroupId);
	    AR.setType(getAccountType());
	    AR.insert();
	    iRoundId = AR.getID();
	  }
	  catch (SQLException ex) {
	    ex.printStackTrace();
	    try {
	      AR.delete();
	    }
	    catch (SQLException ex2) {
	      ex2.printStackTrace();
	      AR = null;
	    }
	  }
	
	  if(AR != null){
	    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
	
	    try{
	      t.begin();
	      int totals = 0;
	      int totalAmount = 0;
	      // All tenants accounts (Outer loop)
	      AccountPhoneEntry ape;
	      AccountKey AK = ((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).findByPrimaryKeyLegacy(iAccountKeyId);
	      TariffKey TK = ((com.idega.block.finance.data.TariffKeyHome)com.idega.data.IDOLookup.getHomeLegacy(TariffKey.class)).findByPrimaryKeyLegacy(AK.getTariffKeyId());
	      while (I.hasNext()) {
	        accounts = (ContractAccounts)I.next();
	        totalAmount = 0;
	        float Amount = 0;
	        List PhoneEntries = FinanceFinder.getInstance().listOfUnBilledPhoneEntries(accounts.getPhoneAccountId(),null,IWTimestamp.RightNow());
	        if(PhoneEntries != null){
	          Iterator it = PhoneEntries.iterator();
	          AccountEntry AE = insertKreditEntry(accounts.getFinanceAccountId(),iRoundId,paydate,0,AK,TK,iCashierId);
	          while(it.hasNext()){
	            ape = (AccountPhoneEntry) it.next();
	            Amount = ape.getPrice();
	            totalAmount += Amount;
	            ape.setAccountEntryId(AE.getID());
	            ape.setLastUpdated(IWTimestamp.getTimestampRightNow());
	            ape.setStatus(ape.statusBilled);
	            ape.setRoundId(iRoundId);
	            ape.update();
	          }
	          //System.err.println("totalAmount : "+totalAmount);
	          AE.setTotal(totalAmount*tax);
	          AE.setNetto(totalAmount);
	          AE.update();
	          totals += totalAmount;
	
	          iAccountCount++;
	        }
	        else{
	         // System.err.println("no phone entries for account "+accounts.getPhoneAccountId());
	        }
	      }
	      AR.update();
	      t.commit();
	      return true;
	    }
	    catch(Exception e) {
	      try {
	        t.rollback();
	      }
	      catch(javax.transaction.SystemException ex) {
	        ex.printStackTrace();
	      }
	      try {
	        AR.delete();
	      }
	      catch (Exception ex2) {
	        ex2.printStackTrace();
	
	      }
	      e.printStackTrace();
	    }
	  }
	}
	return false;
	*/

	public String getSQLString(Integer assessmentRound) {
		StringBuffer sql = new StringBuffer("update fin_phone_entry p ");
		sql.append(" set p.fin_acc_entry_id = null ");
		sql.append(" where p.fin_phone_entry_id in ( ");
		sql.append(" select pe.fin_phone_entry_id ");
		sql.append(" from fin_phone_entry pe, fin_acc_entry ae, fin_assessment_round ar ");
		sql.append(" where pe.fin_acc_entry_id = ae.fin_acc_entry_id ");
		sql.append(" and ae.fin_assessment_round_id = ");
		sql.append(assessmentRound);
		sql.append(" )");
		//System.err.println(sql.toString());
		return sql.toString();
	}

	public Collection listOfAssessmentTariffPreviews(IWApplicationContext iwac,Integer tariffGroupId, IWTimestamp start, IWTimestamp end) {
		return null;
	}

	private static float insertEntry(Vector V, Tariff T, Integer accountId, Integer roundId, IWTimestamp itPaydate, Integer cashierId) throws Exception, java.rmi.RemoteException {
		AccountEntry AE = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).create();
		AE.setAccountId(accountId);
		AE.setAccountKeyId(T.getAccountKeyId());
		AE.setCashierId(cashierId);
		AE.setLastUpdated(IWTimestamp.getTimestampRightNow());
		AE.setPrice(-T.getPrice());
		AE.setRoundId(roundId);
		AE.setName(T.getName());
		AE.setInfo(T.getInfo());
		AE.setStatus(com.idega.block.finance.data.AccountEntryBMPBean.statusCreated);
		AE.setCashierId(1);
		AE.setPaymentDate(itPaydate.getTimestamp());
		AE.store();
		if (V != null)
			V.add(AE);
		return AE.getTotal();
		/*
		System.err.println("totals before"+totals);
		totals = totals + AE.getPrice();
		System.err.println("price"+AE.getPrice());
		System.err.println("totals after"+totals);
		*/
	}

	private static AccountEntry insertEntry(Integer accountId, Integer roundId, IWTimestamp itPaydate, float nettoamount, AccountKey key, TariffKey tkey, Integer cashierId) throws Exception {
		//System.out.println("iRoundId = " + iRoundId);
		AccountEntry AE = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).create();
		AE.setAccountId(accountId);
		AE.setAccountKeyId(((Integer)key.getPrimaryKey()).intValue());
		AE.setCashierId(cashierId);
		AE.setLastUpdated(IWTimestamp.getTimestampRightNow());
		AE.setNetto(nettoamount);
		AE.setRoundId(roundId);
		AE.setName(tkey.getName());
		AE.setInfo(tkey.getInfo());
		AE.setStatus(com.idega.block.finance.data.AccountEntryBMPBean.statusCreated);
		AE.setPaymentDate(itPaydate.getTimestamp());
		AE.store();
		return AE;
	}

	public Map getAttributeMap() {
		Hashtable H = new Hashtable(2);
		H.put("a", "M?na?argjald");
		H.put("b", "Stofngjald");
		H.put("t", "Skattur (%)");

		return H;
	}

	public List listOfAttributes() {
		Vector v = new Vector();
		v.add("a");
		v.add("b");
		v.add("t");
		return v;

	}

	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.FinanceHandler#getTariffsByUserAndGroup(java.lang.Integer, java.lang.Integer)
	 */
	public Collection getTariffsForAccountInGroup(Integer userID, Integer tariffGroupID) {
		// TODO Auto-generated method stub
		return null;
	}
}
