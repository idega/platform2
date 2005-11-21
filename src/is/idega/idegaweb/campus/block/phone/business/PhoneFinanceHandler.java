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

import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
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
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class PhoneFinanceHandler implements FinanceHandler {

	// public static float tax = 1.245f;
	public static float tax = 1.0f;
	public static float fastaGjald = -923.0f;
	public static int fastaGjaldsLykill = 4415;

	int count = 0;

	public PhoneFinanceHandler() {
	}

	public String getAccountType() {
		return com.idega.block.finance.data.AccountBMPBean.typePhone;
	}

	public boolean rollbackAssessment(IWApplicationContext iwac, Integer assessmentRoundId) {
		StringBuffer sql = new StringBuffer("update ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getEntityTableName());
		sql.append(" set ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameAccountEntryId()).append(
				" = null ");
		sql.append(" , ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getRoundIdColumnName()).append(
				" = null ");
		sql.append(" , ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameStatus()).append(
				" = '").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.statusRead).append("'");
		sql.append(" where ").append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getRoundIdColumnName()).append(
				" = ").append(assessmentRoundId);
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
			stmt.execute("delete from fin_phone_entry where total_price > 0 and fin_assessment_round_id = "
					+ assessmentRoundId);
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
				}
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (conn != null) {
				ConnectionBroker.freeConnection(conn);
			}
		}

		return false;
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

	public boolean executeAssessment(IWApplicationContext iwac, Integer categoryId, Integer tariffGroupId,
			String roundName, Integer cashierId, Integer accountKeyId, IWTimestamp paydate, IWTimestamp start,
			IWTimestamp end, Integer excessRoundID) {
		try {
			Collection listOfAccounts = ((ContractAccountsHome) IDOLookup.getHome(ContractAccounts.class)).findByPeriodOverLap(
					start.getDate(), end.getDate());
			if (listOfAccounts != null) {
				System.err.println("Accounts not null");
				Map M = getMapOfContractsAccountsByPhoneAccountId(listOfAccounts);
				System.err.println("Got map of contracts by phone");
				Map E = new HashMap(listOfAccounts.size());
				System.err.println("Created Hashmap of accounts");
				Collection entries = ((AccountPhoneEntryHome) IDOLookup.getHome(AccountPhoneEntry.class)).findUnbilledByAccountAndPeriod(
						null, start.getDate(), end.getDate());
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
						AR = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class)).create();
						AR.setAsNew(roundName);
						AR.setCategoryId(categoryId.intValue());
						AR.setTariffGroupId(tariffGroupId.intValue());
						AR.setType(getAccountType());
						AR.store();
						roundId = ((Integer) AR.getPrimaryKey());

						AccountKey AK = ((AccountKeyHome) IDOLookup.getHome(AccountKey.class)).findByPrimaryKey((accountKeyId));
						TariffKey TK = ((TariffKeyHome) IDOLookup.getHome(TariffKey.class)).findByPrimaryKey(new Integer(
								AK.getTariffKeyId()));
						
						AccountKey fastaAK = ((AccountKeyHome) IDOLookup.getHome(AccountKey.class)).findByPrimaryKey((new Integer(fastaGjaldsLykill)));
						TariffKey fastaTK = ((TariffKeyHome) IDOLookup.getHome(TariffKey.class)).findByPrimaryKey(new Integer(
								fastaAK.getTariffKeyId()));						
						
						Integer phAccId;
						Iterator iter = entries.iterator();
						AccountEntry AE;
						while (iter.hasNext()) {
							ape = (AccountPhoneEntry) iter.next();
							phAccId = new Integer(ape.getAccountId());
							if (M.containsKey(phAccId)) {
								accounts = (ContractAccounts) M.get(phAccId);
								if (E.containsKey(phAccId)) {
									AE = (AccountEntry) E.get(phAccId);
									AE.setNetto(AE.getNetto() + ape.getPrice());
								}
								else {
									Building building = ((BuildingHome) IDOLookup.getHome(Building.class)).findByPrimaryKey(new Integer(accounts.getBuildingId()));
									AE = insertEntry(new Integer(accounts.getFinanceAccountId()), roundId, paydate,
											ape.getPrice(), AK, TK, cashierId, building.getDivision(), null);
									E.put(phAccId, AE);
									//Fastagjald. 
									AccountEntry entry = insertEntry(new Integer(accounts.getFinanceAccountId()), roundId, paydate,
												fastaGjald, fastaAK, fastaTK, cashierId, building.getDivision(), "Fastagjald");
									entry.setTotal(entry.getNetto() * tax);
									entry.store();
								}
								ape.setAccountEntryId(((Integer) AE.getPrimaryKey()).intValue());
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
								phoneEntry.setAccountEntryId(((Integer) entry.getPrimaryKey()).intValue());
								phoneEntry.setStatus(AccountPhoneEntryBMPBean.statusBilled);
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
						e.printStackTrace();
					}

				}
				else
					System.err.println("Entries null ");

			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

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
		return sql.toString();
	}

	public Collection listOfAssessmentTariffPreviews(IWApplicationContext iwac, Integer tariffGroupId,
			IWTimestamp start, IWTimestamp end) {
		return null;
	}

	private static float insertEntry(Vector V, Tariff T, Integer accountId, Integer roundId, IWTimestamp itPaydate,
			Integer cashierId) throws Exception, java.rmi.RemoteException {
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
	}

	private static AccountEntry insertEntry(Integer accountId, Integer roundId, IWTimestamp itPaydate,
			float nettoamount, AccountKey key, TariffKey tkey, Integer cashierId, String division, String text) throws Exception {
		AccountEntry AE = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).create();
		AE.setAccountId(accountId);
		AE.setAccountKeyId(((Integer) key.getPrimaryKey()).intValue());
		AE.setCashierId(cashierId);
		AE.setLastUpdated(IWTimestamp.getTimestampRightNow());
		AE.setNetto(nettoamount);
		AE.setRoundId(roundId);
		if (text != null) {
			AE.setName(text);
		} else {
			AE.setName(tkey.getName());
		}
		AE.setInfo(tkey.getInfo());
		AE.setStatus(com.idega.block.finance.data.AccountEntryBMPBean.statusCreated);
		AE.setPaymentDate(itPaydate.getTimestamp());
		AE.setDivisionForAccounting(division);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.block.finance.business.FinanceHandler#getTariffsByUserAndGroup(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	public Collection getTariffsForAccountInGroup(Integer userID, Integer tariffGroupID) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.block.finance.business.FinanceHandler#publishAssessment(com.idega.idegaweb.IWApplicationContext,
	 *      java.lang.Integer)
	 */
	public void publishAssessment(IWApplicationContext iwc, Integer roundId) {
	}
}