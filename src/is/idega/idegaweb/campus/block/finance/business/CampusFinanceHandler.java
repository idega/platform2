package is.idega.idegaweb.campus.block.finance.business;

import com.idega.block.building.business.BuildingCacher;

import com.idega.block.finance.business.AssessmentTariffPreview;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryBMPBean;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.SimpleQuerier;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.data.ContractAccountApartment;
import is.idega.idegaweb.campus.data.ContractAccountApartmentHome;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class CampusFinanceHandler implements FinanceHandler {
	int count = 0;
	NumberFormat nf = NumberFormat.getPercentInstance();
	
	public CampusFinanceHandler() {
	}

	public String getAccountType() {
		return com.idega.block.finance.data.AccountBMPBean.typeFinancial;
	}

	public boolean rollbackAssessment(int iAssessmentRoundId) {
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(AccountEntryBMPBean.getEntityTableName());
		sql.append(" where ").append(com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName());
		sql.append(" = ").append(iAssessmentRoundId);
		System.err.println(sql.toString());

		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

		try {
			t.begin();
			AssessmentRound AR = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class)).findByPrimaryKey(new Integer(iAssessmentRoundId));
			SimpleQuerier.execute(sql.toString());
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

	public boolean executeAssessment(int iCategoryId, int iTariffGroupId, String roundName, int iCashierId, int iAccountKeyId, IWTimestamp paydate, IWTimestamp start, IWTimestamp end) {
		System.out.println("Starting assessment for period "+start.toString()+"-"+end.toString());
		Collection tariffs = null;
		Collection listOfUsers  = null;
		String [] statuses = {ContractBMPBean.statusSigned,ContractBMPBean.statusEnded,ContractBMPBean.statusResigned,ContractBMPBean.statusTerminated};
		try {
			tariffs = ((TariffHome)IDOLookup.getHome(Tariff.class)).findByTariffGroup(new Integer (iTariffGroupId));
			listOfUsers = ((ContractAccountApartmentHome)IDOLookup.getHome(ContractAccountApartment.class)).findByTypeAndStatusAndOverlapPeriod(getAccountType(),statuses,start.getDate(),end.getDate());
		} catch (IDOLookupException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		//FinanceFinder.getInstance().listOfTariffs(iTariffGroupId);
		
		//List listOfUsers = CampusAccountFinder.listOfContractAccountApartment(getAccountType(), start, end);
		
		int iAccountCount = 0;
		if (tariffs != null) {
			if (listOfUsers != null) {
				NumberFormat nf = NumberFormat.getPercentInstance();
				int rlen = listOfUsers.size();
				Tariff eTariff;
				char cAttribute;
				ContractAccountApartment user;
				Vector vEntries = new Vector();
				int iAttributeId = -1;
				int iRoundId = -1;
				AssessmentRound AR = null;
				javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

				try {
					t.begin();
				//try {
					AR = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class)).create();
					AR.setAsNew(roundName);
					AR.setCategoryId(iCategoryId);
					//AR.setCategoryId(iCategoryId);
					AR.setTariffGroupId(iTariffGroupId);
					AR.setType(com.idega.block.finance.data.AccountBMPBean.typeFinancial);
					AR.insert();
					iRoundId = AR.getID();
					//iRoundId++; // is this quickfix of death
			/*	}
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
					javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

					try {
						t.begin();
						*/
						int totals = 0;
						int totalAmount = 0;
						double factor = 1;
						// All tenants accounts (Outer loop)
						for (Iterator iter = listOfUsers.iterator(); iter.hasNext();) {
							user = (ContractAccountApartment)  iter.next();
							factor = getFactor(user, start, end);
							///Account eAccount = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).findByPrimaryKeyLegacy(user.getAccountId());
							if (factor > 0) {
								totalAmount = 0;
								float Amount = 0;
								// For each tariff (Inner loop)
								for (Iterator iter2 = tariffs.iterator(); iter2.hasNext();) {
									eTariff = (Tariff) iter2.next();
									Amount = 0;
									String sAttribute = eTariff.getTariffAttribute();
									// If we have an tariff attribute
									if (sAttribute != null) {
										iAttributeId = -1;
										cAttribute = sAttribute.charAt(0);
										// If All
										if (cAttribute == BuildingCacher.CHARALL) {
											Amount = insertEntry(vEntries, eTariff, user.getAccountId(), iRoundId, paydate, iCashierId, factor);
										}
										// other than all
										else {
											// attribute check
											if (sAttribute.length() >= 3) {
												iAttributeId = Integer.parseInt(sAttribute.substring(2));
												switch (cAttribute) {
													case BuildingCacher.CHARTYPE : // Apartment type
														if (iAttributeId == user.getApartmentTypeId())
															Amount = insertEntry(vEntries, eTariff, user.getAccountId(), iRoundId, paydate, iCashierId, factor);
														break;
													case BuildingCacher.CHARCATEGORY : // Apartment category
														if (iAttributeId == user.getApartmentCategoryId())
															Amount = insertEntry(vEntries, eTariff, user.getAccountId(), iRoundId, paydate, iCashierId, factor);
														break;
													case BuildingCacher.CHARBUILDING : // Building
														if (iAttributeId == user.getBuildingId())
															Amount = insertEntry(vEntries, eTariff, user.getAccountId(), iRoundId, paydate, iCashierId, factor);
														break;
													case BuildingCacher.CHARFLOOR : // Floor
														if (iAttributeId == user.getFloorId())
															Amount = insertEntry(vEntries, eTariff, user.getAccountId(), iRoundId, paydate, iCashierId, factor);
														break;
													case BuildingCacher.CHARCOMPLEX : // Complex
														if (iAttributeId == user.getComplexId())
															Amount = insertEntry(vEntries, eTariff, user.getAccountId(), iRoundId, paydate, iCashierId, factor);
														break;
													case BuildingCacher.CHARAPARTMENT : // Apartment
														if (iAttributeId == user.getApartmentId())
															Amount = insertEntry(vEntries, eTariff, user.getAccountId(), iRoundId, paydate, iCashierId, factor);
														break;
												} // switch
											} // attribute check
										} // other than all
										if (sAttribute.length() >= 3) {
											iAttributeId = Integer.parseInt(sAttribute.substring(2));
										}
										totalAmount += Amount;

									}
								} // Inner loop block
							}
							totals += totalAmount * -1;

						} // Outer loop block

						AR.store();
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
				}
			//}
		}
		return false;
	}

	/**
	 *  Returns a multiplying factor to entry prices, for each contract
	 *  The factor makes use of the contract begin and end dates.
	 *  NOT if the contract has a delivertime, that is the begin date,
	 *  NOT if the contract has a returntime , that is the end date.
	 */
	public double getFactor(ContractAccountApartment con, IWTimestamp start, IWTimestamp end) {
		double ret = 0;
		start.setTime(0,0,0);
		end.setTime(23,59,59);
		long begin = start.getTimestamp().getTime();
		long endin = end.getTimestamp().getTime();
		long del = endin - begin;
		long valfr = con.getValidFrom().getTime();
		long valto = con.getValidTo().getTime();

		/*    if(con.getDeliverTime()!=null ){
		      valfr = con.getDeliverTime().getTime();
		    }
		    if(con.getReturnTime() !=null){
		      valto = con.getReturnTime().getTime();
		    }*/

		/*
		      System.err.print("Valfr: "+con.getValidFrom().toString());
		      System.err.print(" Valto: "+con.getValidTo().toString());
		      System.err.print(" start: "+start.toString());
		      System.err.print(" end: "+end.toString());
		*/

		// if contract begins and ends within period
		//if (begin <= valfr && valto <= endin) {
			//System.out.println("begins and ends  within period");
		//	begin = valfr;
		//	endin = valto;
		//}
		// if contract ends within period
		//else
		 if (begin <= valto && valto <= endin) {
			//System.out.println("ends within period");
			endin = valto;
		}
		// if contract begins within period
		//else 
		if (begin <= valfr && valfr <= endin) {
			//System.out.println("begins within period");
			begin = valfr;
		}
		// if contract begins and ends outside period
		//else if (valfr < begin && endin < valto) {
			//System.out.println("begins and ends  outside period");
			// donothing
		//}

		double diff = endin - begin;
		ret = (diff) / del;
		//System.out.println("factor for contract "+con.getContractId()+" aprt:"+con.getApartmentId() +" start:"+(new Date(begin)).toGMTString()+" end: "+(new Date(endin)).toGMTString()+" factor: "+ret);
		return ret;
	}

	public Collection listOfAssessmentTariffPreviews(int iTariffGroupId, IWTimestamp start, IWTimestamp end) throws java.rmi.RemoteException {
		Collection tariffs = null;
		try {
			tariffs = ((TariffHome)IDOLookup.getHome(Tariff.class)).findByTariffGroup(new Integer (iTariffGroupId));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		//FinanceFinder.getInstance().listOfTariffs(iTariffGroupId);
		//List listOfTariffs = new Vector(tariffs);
		//List listOfUsers = CampusAccountFinder.listOfRentingUserAccountsByType(getAccountType());
		Collection listOfUsers= null;;
		try {
			listOfUsers = ((ContractAccountApartmentHome)IDOLookup.getHome(ContractAccountApartment.class)).findAll();
		} catch (IDOLookupException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		if (tariffs != null && listOfUsers != null) {
			Hashtable H = new Hashtable(tariffs.size());
			int rlen = listOfUsers.size();
			Tariff eTariff;
			char cAttribute;
			ContractAccountApartment user;

			int iAttributeId = -1;
			String sAttribute;

			// All tenants accounts (Outer loop)
			for (Iterator iter1 = listOfUsers.iterator(); iter1.hasNext();) {
				user = (ContractAccountApartment) iter1.next();

				double factor = getFactor(user, end, start);
				// For each tariff (Inner loop)
				if (factor > 0) {
					for (Iterator iter = tariffs.iterator(); iter.hasNext();) {
						eTariff = (Tariff)  iter.next();
						sAttribute = eTariff.getTariffAttribute();
						// If we have an tariff attribute
						if (sAttribute != null) {
							iAttributeId = -1;
							cAttribute = sAttribute.charAt(0);
							//System.err.println("att "+String.valueOf(cAttribute));
							// If All
							if (cAttribute == BuildingCacher.CHARALL) {
								addAmount(H, eTariff, factor);
							}
							// other than all
							else {
								// attribute check
								if (sAttribute.length() >= 3) {
									iAttributeId = Integer.parseInt(sAttribute.substring(2));
									switch (cAttribute) {
										case BuildingCacher.CHARTYPE : // Apartment type
											if (iAttributeId == user.getApartmentTypeId())
												addAmount(H, eTariff, factor);
											break;
										case BuildingCacher.CHARCATEGORY : // Apartment category
											if (iAttributeId == user.getApartmentCategoryId())
												addAmount(H, eTariff, factor);
											break;
										case BuildingCacher.CHARBUILDING : // Building
											if (iAttributeId == user.getBuildingId())
												addAmount(H, eTariff, factor);
											break;
										case BuildingCacher.CHARFLOOR : // Floor
											if (iAttributeId == user.getFloorId())
												addAmount(H, eTariff, factor);
											break;
										case BuildingCacher.CHARCOMPLEX : // Complex
											if (iAttributeId == user.getComplexId())
												addAmount(H, eTariff, factor);
											break;
										case BuildingCacher.CHARAPARTMENT : // Apartment
											if (iAttributeId == user.getApartmentId())
												addAmount(H, eTariff, factor);
											break;
									} // switch
								} // attribute check
							} // other than all
							if (sAttribute.length() >= 3) {
								iAttributeId = Integer.parseInt(sAttribute.substring(2));
							}
						}
					} // Inner loop block
				} // factor check
			} // Outer loop block
			//System.err.println("count "+count);
			if (H != null) {
				return H.values();
			}
		} // listcheck
		else
			System.err.println("nothing to preview");
		return null;
	}

	private synchronized void addAmount(Map map, Tariff tariff, double factor) throws java.rmi.RemoteException {
		//System.err.println("map size "+map.size());
		Integer id = ((Integer) tariff.getPrimaryKey());
		AssessmentTariffPreview preview;
		if (map.containsKey(id)) {
			preview = (AssessmentTariffPreview) map.get(id);
		}
		else {
			preview = new AssessmentTariffPreview(tariff.getName());
		}
		preview.addAmount((float) (tariff.getPrice() * factor));
		map.put(id, preview);
		count++;
	}

	private float insertEntry(Vector V, Tariff T, int iAccountId, int iRoundId, IWTimestamp itPaydate, int iCashierId, double factor) throws CreateException, java.rmi.RemoteException {

		if (factor > 0) {
			AccountEntry AE = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).create();
			AE.setAccountId(iAccountId);
			AE.setAccountKeyId(T.getAccountKeyId());
			AE.setCashierId(iCashierId);
			AE.setLastUpdated(IWTimestamp.getTimestampRightNow());

			/** @todo  skeptical precision cut */

			AE.setTotal((int) (-T.getPrice() * factor));
			AE.setRoundId(iRoundId);
			AE.setName(T.getName());
			if (T.getInfo() != null)
				AE.setInfo(T.getInfo() + " " + nf.format(factor));
			else
				AE.setInfo(nf.format(factor));
			AE.setStatus(com.idega.block.finance.data.AccountEntryBMPBean.statusCreated);
			AE.setCashierId(1);
			AE.setPaymentDate(itPaydate.getTimestamp());
			AE.store();
			if (V != null)
				V.add(AE);

			return AE.getTotal();
		}
		return 0;

		/*
		System.err.println("totals before"+totals);
		totals = totals + AE.getPrice();
		System.err.println("price"+AE.getPrice());
		System.err.println("totals after"+totals);
		*/

	}

	public Map getAttributeMap() {
		Map map = BuildingCacher.mapOfLodgingsNames();
		map.put("a", "All");
		return map;
	}

	public List listOfAttributes() {
		List list = BuildingCacher.listOfMapEntries();
		list.add(0, "a");
		return list;
	}
}