package com.idega.block.finance.business;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryBMPBean;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.EntryGroup;
import com.idega.block.finance.data.EntryGroupHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.data.SimpleQuerier;
import com.idega.util.IWTimestamp;
/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class AssessmentBusinessBean extends IBOServiceBean  implements AssessmentBusiness  {
	public static final char cComplex = 'x';
	public static final char cAll = 'a';
	public static final char cBuilding = 'b';
	public static final char cFloor = 'f';
	public static final char cCategory = 'c';
	public static final char cType = 't';
	public static final char cApartment = 'p';
	
	public void groupEntriesWithSQL(IWTimestamp from, IWTimestamp to) throws Exception {
		javax.transaction.UserTransaction t = this.getSessionContext().getUserTransaction();
		//TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();
			///////////////////////////
			//AccountEntryHome ehome = (AccountEntryHome) IDOLookup.getHome(AccountEntry.class);
			
			EntryGroup EG = null;
			int gid = -1;
			try {
				EG = ((EntryGroupHome) IDOLookup.getHome(EntryGroup.class)).create();
				EG.setGroupDate(IWTimestamp.RightNow().getSQLDate());
				EG.store();
				gid = ((Integer)EG.getPrimaryKey()).intValue();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				EG = null;
			}
			if (EG != null) {
				String dateColummn = AccountEntryBMPBean.getPaymentDateColumnName();
				StringBuffer sql = new StringBuffer("update ");
				sql.append(AccountEntryBMPBean.getEntityTableName());
				sql.append(" set ");
				sql.append(AccountEntryBMPBean.getEntryGroupIdColumnName());
				sql.append(" = ");
				sql.append(gid);
				sql.append(" where ");
				sql.append(AccountEntryBMPBean.getEntryGroupIdColumnName());
				sql.append(" is null ");
				if (from != null) {
					sql.append(" and ").append(dateColummn).append(" >= '").append(from.getSQLDate());
					sql.append(" 00:00:00' ");
				}
				if (to != null) {
					sql.append(" and ").append(dateColummn).append(" <= ");
					sql.append('\'');
					sql.append(to.getSQLDate());
					sql.append(" 23:59:59'");
				}
				String where =
					" where "
						+ AccountEntryBMPBean.getEntryGroupIdColumnName()
						+ " = "
						+ gid;
				String sMinSql =
					"select min("
						+ AccountEntryBMPBean.getEntityTableName()+"_ID"
						+ ") from "
						+ AccountEntryBMPBean.getEntityTableName()
						+ where;
				String sMaxSql =
					"select max("
						+ AccountEntryBMPBean.getEntityTableName()+"_ID"
						+ ") from "
						+ com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName()
						+ where;
				/*
				        System.err.println(sql.toString());
				        System.err.println(sMinSql);
				        System.err.println(sMaxSql);
				*/
				SimpleQuerier.execute(sql.toString());
				String[] mi = SimpleQuerier.executeStringQuery(sMinSql);
				String[] ma = SimpleQuerier.executeStringQuery(sMaxSql);
				if (mi != null && mi.length > 0 && mi[0] != null) {
					EG.setEntryIdFrom(new Integer(mi[0]).intValue());
				}
				if (ma != null && ma.length > 0 && mi[0] != null) {
					EG.setEntryIdTo(new Integer(ma[0]).intValue());
				}
				EG.store();
			}
			t.commit();
			///////////////////////////
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
	public void groupEntries(IWTimestamp from, IWTimestamp to) throws Exception {
		Collection entries = ((AccountEntryHome)getIDOHome(AccountEntry.class)).findUnGrouped(from.getDate(),to.getDate());
		//List L = Finder.listOfFinanceEntriesWithoutGroup(from, to);
		if (entries != null) {
			int min = 0, max = 0;
			EntryGroup EG = null;
			try {
				EG =((EntryGroupHome) getIDOHome(EntryGroup.class)).create();
				EG.setGroupDate(IWTimestamp.RightNow().getSQLDate());
				EG.store();
				//System.err.println(" gid "+gid);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				try {
					EG.remove();
				}
				catch (Exception ex2) {
					ex2.printStackTrace();
					EG = null;
				}
			}
			if (EG != null) {
				javax.transaction.UserTransaction transaction = this.getSessionContext().getUserTransaction();
				//javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
				try {
					//t.begin();
					transaction.begin();
					////////////////////////
					Iterator It = entries.iterator();
					AccountEntry AE;
					int aeid = 0;
					AE = (AccountEntry) It.next();
					aeid = ((Integer)AE.getPrimaryKey()).intValue();
					min = aeid;
					max = aeid;
					AE.setEntryGroupId(((Integer)EG.getPrimaryKey()).intValue());
					AE.store();
					while (It.hasNext()) {
						AE = (AccountEntry) It.next();
						aeid =((Integer)AE.getPrimaryKey()).intValue();
						min = aeid < min ? aeid : min;
						max = aeid > min ? aeid : max;
						AE.setEntryGroupId(((Integer)EG.getPrimaryKey()).intValue());
						AE.store();
					}
					EG.setEntryIdFrom(min);
					EG.setEntryIdTo(max);
					EG.store();
					//////////////////////////////
					transaction.commit();
				}
				catch (Exception e) {
					try {
						transaction.rollback();
					}
					catch (javax.transaction.SystemException ex) {
						ex.printStackTrace();
					}
					try {
						EG.remove();
					}
					catch (Exception ex) {
					}
					e.printStackTrace();
				}
			} //if EG null
		}
	}
	public int getGroupEntryCount(EntryGroup entryGroup) {
		int count = 0;
		if (entryGroup != null) {
			try {
				count = ((AccountEntryHome) getIDOHome(AccountEntry.class)).countByGroup((Integer)entryGroup.getPrimaryKey());
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (EJBException e) {
				e.printStackTrace();
			} catch (IDOException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	public void assessTariffsToAccount(
		float price,
		String name,
		String info,
		Integer accountID,
		Integer accountKeyID,
		Date paydate,
		Integer tariffGroupID,
		Integer financeCategoryID,Integer externalID,
		boolean save,Integer assessmentRound) {
		javax.transaction.UserTransaction transaction = this.getSessionContext().getUserTransaction();
		try {
			transaction.begin();

			AssessmentRoundHome arh = (AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class);
			AssessmentRound AR = null;
			if(assessmentRound!=null && assessmentRound.intValue()>0){
				AR = arh.findByPrimaryKey(assessmentRound);
			}
			else{
				AR = arh.create();
				AR.setAsNew("account " + accountID);
				AR.setTariffGroupId(tariffGroupID.intValue());
				AR.setCategoryId(financeCategoryID.intValue());
				AR.store();
			}
			
			createAccountEntry(
				accountID,
				accountKeyID,
				new Integer(1),
				((Integer) AR.getPrimaryKey()),
				price,
				0,
				price,
				paydate,
				name,
				info,
				"C",externalID);
			if (save) {
				Tariff t = ((TariffHome) IDOLookup.getHome(Tariff.class)).create();
				t.setAccountKeyId(accountKeyID);
				t.setInfo(info);
				t.setName(name);
				t.setPrice(price);
				t.setTariffGroupId(tariffGroupID);
				t.store();
			}
			transaction.commit();
		}
		catch (Exception e) {
			try {
				transaction.rollback();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}

	}
	public void assessTariffsToAccount(
		Integer[] tariffIds,Double[] multiplyFactors,
		Integer accountID,
		Date paydate,
		int discount,
		Integer tariffGroupID,
		Integer financeCategoryID,
		Integer externalID,Integer assessmentRound)
		throws java.rmi.RemoteException {
		try {
			boolean useFactors = (multiplyFactors!=null && tariffIds.length==multiplyFactors.length);
			Vector tariffs = new Vector(tariffIds.length);
			Vector factors = useFactors?new Vector(multiplyFactors.length):null;
			Tariff tariff;
			TariffHome  home =  (TariffHome) IDOLookup.getHome(Tariff.class);
			for (int i = 0; i < tariffIds.length; i++) {
				tariff = home.findByPrimaryKey((tariffIds[i]));
				tariffs.add(tariff);
				if(useFactors)
					factors.add(multiplyFactors[i]);
			}
			
			//Collection tariffs = ((TariffHome) IDOLookup.getHome(Tariff.class)).findAllByPrimaryKeyArray(tariffIds);
			assessTariffsToAccount(tariffs,factors, accountID, paydate, discount, tariffGroupID, financeCategoryID,externalID,assessmentRound);
		}
		catch (javax.ejb.FinderException ex) {
			throw new java.rmi.RemoteException(ex.getMessage());
		}
	}
	public void assessTariffsToAccount(
		List tariffs,List multiplyFactors,
		Integer accountID,
		Date paydate,
		int discount,
		Integer tariffGroupID,
		Integer financeCategoryID,Integer externalID,Integer assessmentRound) {
		javax.transaction.UserTransaction transaction = this.getSessionContext().getUserTransaction();

		try {
			transaction.begin();
			AssessmentRound AR = null;
			if(assessmentRound!=null && assessmentRound.intValue()>0)
			   AR = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class)).create();
			else
			   AR = createAssessmentRound(accountID, tariffGroupID, financeCategoryID,(java.sql.Date)paydate);
			
			Integer roundID = ((Integer) AR.getPrimaryKey());
			createAccountEntries(tariffs,multiplyFactors,accountID, paydate, discount, roundID,externalID);
			transaction.commit();
		}
		catch (Exception e) {
			try {
				transaction.rollback();
				System.err.println("Assessment transaction rollback");
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}

	}
	private void createAccountEntries(List tariffs,List factors ,Integer accountID, Date paydate, int discount, Integer roundID,Integer externalID)
		throws RemoteException, CreateException {
		Tariff tariff;
		Double factor;
		for (int i = 0; i < tariffs.size(); i++) {
			tariff = (Tariff) tariffs.get(i);
			float price = tariff.getPrice();
			String info = tariff.getInfo();
			if(info == null)
				info = "";
			if(factors!=null){
				factor = (Double) factors.get(i);
				info += "[ "+factor+" x "+price+"] ";
				price*=factor.doubleValue();
			} 
			if (discount > 0 && discount < 100) {
				//System.out.print("price is "+price);
				double fact = ((100.0-discount) / 100.0) ;
				price = (float) (price*fact);
				//System.out.println(" whith discount  "+discount+"%  "+price);
				info += "(" + discount + " %)";
			}
			if(price>0)
				createAccountEntry(	accountID,new Integer(tariff.getAccountKeyId()),	new Integer(1),	roundID,price,0,	price,paydate,tariff.getName(),info,"C",externalID);
		}
	}
	private AssessmentRound createAssessmentRound(Integer accountID, Integer tariffGroupID, Integer financeCategory,java.sql.Date duedate)
		throws IDOLookupException, CreateException {
		AssessmentRoundHome arh = (AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class);
		AssessmentRound AR = arh.create();
		String name = "account " +accountID;
		AR.setTariffGroupId(tariffGroupID.intValue());
		AR.setCategoryId(financeCategory.intValue());
		AR.setAsNew(name);
		AR.setDueDate(duedate);
		AR.store();
		return AR;
	}
	public AccountEntry createAccountEntry(
		Integer accountID,
		Integer accountKeyID,
		Integer cashierID,
		Integer roundID,
		float netto,
		float VAT,
		float total,
		Date paydate,
		String Name,
		String Info,
		String status,
		Integer externalID)
		throws java.rmi.RemoteException, javax.ejb.CreateException {
		AccountEntry AE = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).create();
		AE.setAccountId(accountID.intValue());
		AE.setAccountKeyId(accountKeyID.intValue());
		AE.setCashierId(cashierID);
		AE.setRoundId(roundID);
		AE.setTotal(-total);
		AE.setVAT(-VAT);
		AE.setNetto(-netto);
		AE.setLastUpdated(new java.sql.Timestamp(new Date().getTime()));
		AE.setPaymentDate(new java.sql.Timestamp(paydate.getTime()));
		AE.setName(Name);
		AE.setInfo(Info);
		AE.setStatus(status);
		//System.out.println(	"AE(account: "	+ iAccountId+ " ,key: "+ iAccountKeyId+ " ,cashier: " +iCashierId+ ",round:"	+ iRoundId+ ")");
		AE.store();
		return AE;
	}
	public boolean rollBackAssessment(Integer assessmentRoundId) {
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName());
		sql.append(" where ").append(com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName());
		sql.append(" = ").append(assessmentRoundId);
		System.err.println(sql.toString());
		javax.transaction.UserTransaction t = this.getSessionContext().getUserTransaction();
		try {
			t.begin();
			AssessmentRound AR = ((AssessmentRoundHome) getIDOHome(AssessmentRound.class)).findByPrimaryKey(assessmentRoundId);
			com.idega.data.SimpleQuerier.execute(sql.toString());
			AR.remove();
			t.commit();
			return true;
		} // Try block
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.AssessmentBusiness#publishAssessment(java.lang.Integer)
	 */
	public void publishAssessment(Integer roundId) {
		try {
			AssessmentRound AR = ((AssessmentRoundHome) getIDOHome(AssessmentRound.class)).findByPrimaryKey(roundId);
			AR.setAsPublished(AR.getName());
			AR.store();
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

	}
}
