package com.idega.block.finance.business;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;

import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.EntryGroup;
import com.idega.block.finance.data.EntryGroupHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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
			AccountEntryHome ehome = (AccountEntryHome) IDOLookup.getHome(AccountEntry.class);
			AccountEntry ae = ehome.create();
			EntryGroup EG = null;
			int gid = -1;
			try {
				EG = ((EntryGroupHome) IDOLookup.getHome(EntryGroup.class)).create();
				EG.setGroupDate(IWTimestamp.RightNow().getSQLDate());
				EG.store();
				gid = EG.getID();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				EG = null;
			}
			if (EG != null) {
				String dateColummn = com.idega.block.finance.data.AccountEntryBMPBean.getPaymentDateColumnName();
				StringBuffer sql = new StringBuffer("update ");
				sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName());
				sql.append(" set ");
				sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntryGroupIdColumnName());
				sql.append(" = ");
				sql.append(gid);
				sql.append(" where ");
				sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntryGroupIdColumnName());
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
						+ com.idega.block.finance.data.AccountEntryBMPBean.getEntryGroupIdColumnName()
						+ " = "
						+ gid;
				String sMinSql =
					"select min("
						+ ae.getIDColumnName()
						+ ") from "
						+ com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName()
						+ where;
				String sMaxSql =
					"select max("
						+ ae.getIDColumnName()
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
		List L = Finder.listOfFinanceEntriesWithoutGroup(from, to);
		if (L != null) {
			int min = 0, max = 0;
			EntryGroup EG = null;
			try {
				EG =
					((com.idega.block.finance.data.EntryGroupHome) com
						.idega
						.data
						.IDOLookup
						.getHomeLegacy(EntryGroup.class))
						.createLegacy();
				EG.setGroupDate(IWTimestamp.RightNow().getSQLDate());
				EG.insert();
				//System.err.println(" gid "+gid);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				try {
					EG.delete();
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
					Iterator It = L.iterator();
					AccountEntry AE;
					int aeid = 0;
					AE = (AccountEntry) It.next();
					aeid = AE.getID();
					min = aeid;
					max = aeid;
					AE.setEntryGroupId(EG.getID());
					AE.update();
					while (It.hasNext()) {
						AE = (AccountEntry) It.next();
						aeid = AE.getID();
						min = aeid < min ? aeid : min;
						max = aeid > min ? aeid : max;
						AE.setEntryGroupId(EG.getID());
						AE.update();
					}
					EG.setEntryIdFrom(min);
					EG.setEntryIdTo(max);
					EG.update();
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
						EG.delete();
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
			StringBuffer sql = new StringBuffer("select count(*) from ");
			sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName());
			sql.append(" where ");
			sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntryGroupIdColumnName());
			sql.append(" = ");
			sql.append(entryGroup.getID());
			//System.err.println(sql.toString());
			try {
				count = entryGroup.getNumberOfRecords(sql.toString());
			}
			catch (Exception ex) {
				ex.printStackTrace();
				count = 0;
			}
		}
		return count;
	}
	public void assessTariffsToAccount(
		float price,
		String name,
		String info,
		int iAccountId,
		int iAccountKeyId,
		Date paydate,
		int tariffGroupId,
		int financeCategory,
		boolean save) {
		javax.transaction.UserTransaction transaction = this.getSessionContext().getUserTransaction();
		try {
			transaction.begin();

			AssessmentRoundHome arh = (AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class);
			AssessmentRound AR = arh.create();
			AR.setAsNew("account " + iAccountId);
			AR.setTariffGroupId(tariffGroupId);
			AR.setCategoryId(financeCategory);
			AR.store();
			
			storeAccountEntry(
				iAccountId,
				iAccountKeyId,
				1,
				((Integer) AR.getPrimaryKey()).intValue(),
				price,
				0,
				price,
				paydate,
				name,
				info,
				"C");
			if (save) {
				Tariff t = ((TariffHome) IDOLookup.getHome(Tariff.class)).create();
				t.setAccountKeyId(iAccountKeyId);
				t.setInfo(info);
				t.setName(name);
				t.setPrice(price);
				t.setTariffGroupId(tariffGroupId);
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
		int iAccountId,
		Date paydate,
		int discount,
		int tariffGroupId,
		int financeCategory)
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
			assessTariffsToAccount(tariffs,factors, iAccountId, paydate, discount, tariffGroupId, financeCategory);
		}
		catch (javax.ejb.FinderException ex) {
			throw new java.rmi.RemoteException(ex.getMessage());
		}
	}
	public void assessTariffsToAccount(
		List tariffs,List multiplyFactors,
		int iAccountId,
		Date paydate,
		int discount,
		int tariffGroupId,
		int financeCategory) {
		javax.transaction.UserTransaction transaction = this.getSessionContext().getUserTransaction();

		try {
			transaction.begin();
			AssessmentRound AR = createAssessmentRound(iAccountId, tariffGroupId, financeCategory);
			int RoundID = ((Integer) AR.getPrimaryKey()).intValue();
			createAccountEntries(tariffs,multiplyFactors,iAccountId, paydate, discount, RoundID);
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
	private void createAccountEntries(List tariffs,List factors ,int iAccountId, Date paydate, int discount, int RoundID)
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
				storeAccountEntry(	iAccountId,tariff.getAccountKeyId(),	1,	RoundID,price,0,	price,paydate,tariff.getName(),info,"C");
		}
	}
	private AssessmentRound createAssessmentRound(int iAccountId, int tariffGroupId, int financeCategory)
		throws IDOLookupException, CreateException {
		AssessmentRoundHome arh = (AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class);
		AssessmentRound AR = arh.create();
		String name = "account " + iAccountId;
		AR.setTariffGroupId(tariffGroupId);
		AR.setCategoryId(financeCategory);
		AR.setAsNew(name);
		AR.store();
		return AR;
	}
	public AccountEntry storeAccountEntry(
		int iAccountId,
		int iAccountKeyId,
		int iCashierId,
		int iRoundId,
		float netto,
		float VAT,
		float total,
		Date paydate,
		String Name,
		String Info,
		String status)
		throws java.rmi.RemoteException, javax.ejb.CreateException {
		AccountEntry AE = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).create();
		AE.setAccountId(iAccountId);
		AE.setAccountKeyId(iAccountKeyId);
		AE.setCashierId(iCashierId);
		AE.setRoundId(iRoundId);
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
	public boolean rollBackAssessment(int iAssessmentRoundId) {
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName());
		sql.append(" where ").append(com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName());
		sql.append(" = ").append(iAssessmentRoundId);
		System.err.println(sql.toString());
		javax.transaction.UserTransaction t = this.getSessionContext().getUserTransaction();
		try {
			t.begin();
			AssessmentRound AR =
				(
					(com.idega.block.finance.data.AssessmentRoundHome) com.idega.data.IDOLookup.getHomeLegacy(
						AssessmentRound.class)).findByPrimaryKeyLegacy(
					iAssessmentRoundId);
			com.idega.data.SimpleQuerier.execute(sql.toString());
			AR.delete();
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
}
