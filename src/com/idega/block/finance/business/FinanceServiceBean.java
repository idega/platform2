/*
 * Created on Mar 9, 2004
 *
 */
package com.idega.block.finance.business;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountBMPBean;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AccountHome;
import com.idega.block.finance.data.AccountInfo;
import com.idega.block.finance.data.AccountInfoHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.AccountPhoneEntryHome;
import com.idega.block.finance.data.AccountType;
import com.idega.block.finance.data.AccountTypeHome;
import com.idega.block.finance.data.AccountUser;
import com.idega.block.finance.data.AccountUserHome;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.AssessmentStatus;
import com.idega.block.finance.data.EntryGroup;
import com.idega.block.finance.data.EntryGroupHome;
import com.idega.block.finance.data.FinanceHandlerInfo;
import com.idega.block.finance.data.FinanceHandlerInfoHome;
import com.idega.block.finance.data.PaymentType;
import com.idega.block.finance.data.PaymentTypeHome;
import com.idega.block.finance.data.RoundInfo;
import com.idega.block.finance.data.RoundInfoHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffGroup;
import com.idega.block.finance.data.TariffGroupHome;
import com.idega.block.finance.data.TariffHome;
import com.idega.block.finance.data.TariffIndex;
import com.idega.block.finance.data.TariffIndexHome;
import com.idega.block.finance.data.TariffKey;
import com.idega.block.finance.data.TariffKeyHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOException;
import com.idega.util.IWTimestamp;
/**
 * FinanceServiceBean
 * 
 * @author aron
 * @version 1.0
 */
public class FinanceServiceBean extends IBOServiceBean implements FinanceService {
	public AccountHome getAccountHome() throws RemoteException {
		return (AccountHome) getIDOHome(Account.class);
	}
	public AccountEntryHome getAccountEntryHome() throws RemoteException {
		return (AccountEntryHome) getIDOHome(AccountEntry.class);
	}
	public AccountKeyHome getAccountKeyHome() throws RemoteException {
		return (AccountKeyHome) getIDOHome(AccountKey.class);
	}
	public AccountInfoHome getAccountInfoHome() throws RemoteException {
		return (AccountInfoHome) getIDOHome(AccountInfo.class);
	}
	public AccountPhoneEntryHome getAccountPhoneEntryHome() throws RemoteException {
		return (AccountPhoneEntryHome) getIDOHome(AccountPhoneEntry.class);
	}
	public AccountTypeHome getAccountTypeHome() throws RemoteException {
		return (AccountTypeHome) getIDOHome(AccountType.class);
	}
	public AssessmentRoundHome getAssessmentRoundHome() throws RemoteException {
		return (AssessmentRoundHome) getIDOHome(AssessmentRound.class);
	}
	public RoundInfoHome getRoundInfoHome() throws RemoteException {
		return (RoundInfoHome) getIDOHome(RoundInfo.class);
	}
	public TariffHome getTariffHome() throws RemoteException {
		return (TariffHome) getIDOHome(Tariff.class);
	}
	public TariffKeyHome getTariffKeyHome() throws RemoteException {
		return (TariffKeyHome) getIDOHome(TariffKey.class);
	}
	public TariffGroupHome getTariffGroupHome() throws RemoteException {
		return (TariffGroupHome) getIDOHome(TariffGroup.class);
	}
	public EntryGroupHome getEntryGroupHome() throws RemoteException {
		return (EntryGroupHome) getIDOHome(EntryGroup.class);
	}
	public AccountUserHome getAccountUserHome() throws RemoteException {
		return (AccountUserHome) getIDOHome(AccountUser.class);
	}
	public TariffIndexHome getTariffIndexHome() throws RemoteException {
		return (TariffIndexHome) getIDOHome(TariffIndex.class);
	}
	public PaymentTypeHome getPaymentTypeHome() throws RemoteException {
		return (PaymentTypeHome) getIDOHome(PaymentType.class);
	}
	public FinanceHandlerInfoHome getFinanceHandlerInfoHome() throws RemoteException {
		return (FinanceHandlerInfoHome) getIDOHome(FinanceHandlerInfo.class);
	}
	public FinanceHandler getFinanceHandler(Integer handlerInfoID) {
		try {
			FinanceHandlerInfo handlerInfo = getFinanceHandlerInfoHome().findByPrimaryKey(handlerInfoID);
			if (handlerInfo.getClassName() != null)
				try {
					return (FinanceHandler) Class.forName(handlerInfo.getClassName()).newInstance();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public AssessmentBusiness getFinanceBusiness() throws RemoteException {
		return (AssessmentBusiness) getServiceInstance(AssessmentBusiness.class);
	}
	public AccountBusiness getAccountBusiness() throws RemoteException {
		return (AccountBusiness) getServiceInstance(AccountBusiness.class);
	}
	public void removeAccountKey(Integer keyID) throws FinderException, RemoteException, RemoveException {
		getAccountKeyHome().findByPrimaryKey(keyID).remove();
	}
	public AccountKey createOrUpdateAccountKey(Integer ID, String name, String info, Integer tariffKeyID,
			Integer categoryID) throws CreateException, RemoteException, FinderException {
		AccountKey key = getAccountKeyHome().create();
		if (ID != null && ID.intValue() > 0) {
			key = getAccountKeyHome().findByPrimaryKey(ID);
		}
		key.setName(name);
		key.setInfo(info);
		key.setTariffKeyId(tariffKeyID.intValue());
		key.setCategoryId(categoryID.intValue());
		key.store();
		return key;
	}
	public TariffKey createOrUpdateTariffKey(Integer ID, String name, String info, Integer categoryID)
			throws FinderException, RemoteException, CreateException {
		TariffKey key = getTariffKeyHome().create();
		if (ID != null && ID.intValue() > 0)
			key = getTariffKeyHome().findByPrimaryKey(ID);
		key.setName(name);
		key.setInfo(info);
		key.setCategoryId(categoryID.intValue());
		key.store();
		return key;
	}
	public Tariff createOrUpdateTariff(Integer ID, String name, String info, String attribute, String index,
			boolean useIndex, Timestamp indexStamp, float Price, Integer accountKeyID, Integer tariffGroupID)
			throws FinderException, RemoteException, CreateException {
		Tariff tariff = getTariffHome().create();
		if (ID != null && ID.intValue() > 0) {
			tariff = getTariffHome().findByPrimaryKey(ID);
		}
		tariff.setName(name);
		tariff.setInfo(info);
		tariff.setTariffAttribute(attribute);
		tariff.setAccountKeyId(accountKeyID);
		tariff.setTariffGroupId(tariffGroupID);
		tariff.setPrice(Price);
		tariff.setUseFromDate(IWTimestamp.getTimestampRightNow());
		tariff.setUseToDate(IWTimestamp.getTimestampRightNow());
		tariff.setIndexType(index);
		tariff.setUseIndex(useIndex);
		if (indexStamp != null) {
			tariff.setIndexUpdated(indexStamp);
		}
		tariff.store();
		return tariff;
	}
	public Tariff updateTariffPrice(Integer ID, Float Price, Timestamp indexStamp) throws RemoteException,
			FinderException {
		Tariff tariff = getTariffHome().findByPrimaryKey(ID);
		tariff.setPrice(Price.floatValue());
		if (indexStamp != null)
			tariff.setIndexUpdated(indexStamp);
		tariff.store();
		return tariff;
	}
	public void removeTariff(Integer ID) throws FinderException, RemoteException, RemoveException {
		getTariffHome().findByPrimaryKey(ID).remove();
	}
	public void removeTariffKey(Integer ID) throws FinderException, RemoteException, RemoveException {
		getTariffKeyHome().findByPrimaryKey(ID).remove();
	}
	public void removeTariffIndex(Integer ID) throws FinderException, RemoteException, RemoveException {
		getTariffIndexHome().findByPrimaryKey(ID).remove();
	}
	public Map mapOfTariffIndicesByTypes() throws RemoteException, FinderException {
		Collection coll = getTariffIndexHome().findLastTypeGrouped();
		if (coll != null) {
			Hashtable T = new Hashtable(coll.size());
			TariffIndex ti;
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				ti = (TariffIndex) iter.next();
				T.put(ti.getType(), ti);
			}
			return T;
		} else
			return null;
	}
	public TariffGroup createOrUpdateTariffGroup(Integer ID, String name, String info, Integer handlerId,
			boolean useIndex, Integer categoryId) throws CreateException, FinderException, RemoteException {
		TariffGroup tariff = getTariffGroupHome().create();
		if (ID != null && ID.intValue() > 0) {
			tariff = getTariffGroupHome().findByPrimaryKey(ID);
		}
		tariff.setName(name);
		tariff.setInfo(info);
		tariff.setCategoryId(categoryId.intValue());
		tariff.setUseIndex(useIndex);
		if (handlerId != null && handlerId.intValue() > 0)
			tariff.setHandlerId(handlerId.intValue());
		tariff.store();
		return tariff;
	}
	public TariffIndex createOrUpdateTariffIndex(Integer ID, String name, String info, String type, float newvalue,
			float oldvalue, Timestamp stamp, Integer categoryId) throws RemoteException, CreateException {
		TariffIndex ti = getTariffIndexHome().create();
		if (categoryId.intValue() > 0) {
			/*
			 * if(ID!=null && ID.intValue() > 0){ ti =
			 * getTariffIndexHome().findByPrimaryKey(ID); }
			 */
			ti.setName(name);
			ti.setInfo(info);
			ti.setOldValue(oldvalue);
			ti.setIndex(newvalue);
			ti.setDate(stamp);
			ti.setType(type);
			ti.setCategoryId(categoryId.intValue());
			ti.setNewValue(newvalue);
			ti.store();
			return ti;
		}
		throw new CreateException("Category missing");
	}
	
	public String getAccountTypeFinance(){
		return AccountBMPBean.typeFinancial;
	}
	public String getAccountTypePhone(){
		return AccountBMPBean.typePhone;
	}
	
	  public  Collection getKeySortedTariffsByAttribute(String attribute) throws FinderException,RemoteException{
	    Hashtable tar = null;
	    Map AccKeyMap = mapOfAccountKeys();
	    Map TarKeyMap = mapOfTariffKeys();
	    Collection tariffs = getTariffHome().findByAttribute(attribute);
	    if(tariffs != null ){
	      tar = new Hashtable();
	      java.util.Iterator iter = tariffs.iterator();
	      Tariff t;
	      Integer acckey;
	      Integer tarkey;
	      while(iter.hasNext()){
	        t = (Tariff) iter.next();
	        try{
	        acckey = new Integer(t.getAccountKeyId());
	        if(AccKeyMap.containsKey(acckey)){
	          AccountKey AK = (AccountKey) AccKeyMap.get(acckey);
	          tarkey = new Integer(AK.getTariffKeyId());
	          if(TarKeyMap.containsKey(tarkey)){
	            TariffKey TK = (TariffKey) TarKeyMap.get(tarkey);
	            if(tar.containsKey(tarkey)){
	              Tariff a = (Tariff)tar.get(tarkey);
	              a.setPrice(a.getPrice()+t.getPrice());
	            }
	            else{
	              t.setName(TK.getName());
	              t.setInfo(TK.getInfo());
	              tar.put(tarkey,t)  ;
	            }
	          }
	        }
	        }
	        catch(Exception ex)
	        {}
	      }
	      return tar.values();
	    }
	    return null;
	  }
	  
	  public Map mapOfAccountKeys() throws RemoteException, FinderException {
		Collection coll = getAccountKeyHome().findAll();
		if (coll != null) {
			Hashtable T = new Hashtable(coll.size());
			AccountKey ti;
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				ti = (AccountKey) iter.next();
				T.put((Integer)ti.getPrimaryKey(), ti);
			}
			return T;
		} else
			return null;
	}
	  
	  public Map mapOfTariffKeys() throws RemoteException, FinderException {
		Collection coll = getTariffKeyHome().findAll();
		if (coll != null) {
			Hashtable T = new Hashtable(coll.size());
			TariffKey ti;
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				ti = (TariffKey) iter.next();
				T.put((Integer) ti.getPrimaryKey(), ti);
			}
			return T;
		} else
			return null;
	}
	 
	/**
	 * Returns calculated account balance from account entries in published assessments
	 */
	public double getAccountBalancePublished(Integer accountID){
		return getAccountBalance(accountID,AssessmentStatus.PUBLISHED);
	}
	
	/**
	 * Returns calculated account balance from account entries
	 */
	public double getAccountBalance(Integer accountID){
		return getAccountBalance(accountID,null);
	}
	/**
	 * Returns calculated account balance from account entries with given assessment status flag
	 * See AssessmentStatus for available flags
	 */
	public double getAccountBalance(Integer accountID,String roundStatus){
		try {
			return getAccountEntryHome().getTotalSumByAccount(accountID,roundStatus);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.FinanceService#getAccountLastUpdate(java.lang.Integer)
	 */
	public Date getAccountLastUpdate(Integer accountID) {
		if(accountID!=null){
			try {
				return getAccountEntryHome().getMaxDateByAccount(accountID);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (IDOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}