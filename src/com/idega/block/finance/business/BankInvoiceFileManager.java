/*
 * Created on Dec 6, 2004
 *
 */
package com.idega.block.finance.business;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.BankInfo;
import com.idega.block.finance.data.BankInfoHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;


/**
 * @author birna
 *
 */
public class BankInvoiceFileManager implements BankFileManager{
	
	
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getAccountBook(int)
	 */
	public int getAccountBook(int groupId) {
		BankInfo bi = getBankInfo(groupId);
		if(bi != null) {
			return bi.getAccountBook();
		}else {
			return 66;
		}
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getAmount(int)
	 */
	public String getAmount(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return String.valueOf(((int)ae.getTotal()*10)*10);
		}else {
			return "";
		}
		
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getBankId(int)
	 */
	public String getBankBranchNumber(int groupId) {
		BankInfo bi = getBankInfo(groupId);
		if(bi != null) {
			return bi.getClaimantsBankBranchNumber();
		}else {
			return "";
		}
	}
	public String getBookkeepingType(int invoiceNumber) {
		//TODO: K er stofnun krafa - ath med ad geyma thetta ser - eda eitthvad!!
		return "K";
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getClaimantsAccountId(int)
	 */
	public String getClaimantsAccountId(int groupId) {
		BankInfo bi = getBankInfo(groupId);
		if(bi != null) {
			return bi.getAccountId();
		}else {
			return "";
		}
	}
	public String getClaimantSSN(int groupId) {
		BankInfo bi = getBankInfo(groupId);
		if(bi != null) {
			return bi.getClaimantsSSN();
		}else {
			return "";
		}
	}
	public String getClaimantName(int groupId) {
		BankInfo bi = getBankInfo(groupId);
		if(bi != null) {
			return bi.getClaimantsName();
		}
		else {
			return "";
		}
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getClaimNumber(int)
	 */
	public String getClaimNumber(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	public Integer[] getInvoiceNumbers(int batchNumber) {
		Collection accountEntries = null; 
		AccountEntryHome aeh = null;
		try {
			aeh = (AccountEntryHome) IDOLookup.getHome(AccountEntry.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		if(aeh != null) {
			try {
				accountEntries = aeh.findByBatchNumber(batchNumber);
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
			Collection invoiceNumbers = null;
			if(accountEntries != null) {
				Iterator i = accountEntries.iterator();
				while(i.hasNext()) {
					AccountEntry accEntry = (AccountEntry) i.next();
					invoiceNumbers.add(accEntry.getInvoiceNumber());
				}
				return (Integer[])invoiceNumbers.toArray();
			}
		}
		return null;
	}
	public int[] getInvoiceNumbers(String claimantsSSN, Timestamp dueDate) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDepositRule(int)
	 */
	public String getDepositRule(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDisallowanceDate(int)
	 */
	public String getDisallowanceDate(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getDisallowanceDate().toString();
		}else {
			return "";
		}
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDiscount(int)
	 */
	public String getDiscount(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDiscount1(int)
	 */
	public String getDiscount1(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDiscount2(int)
	 */
	public String getDiscount2(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDisplay(int)
	 */
	public String getDisplay(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDisplayForm(int)
	 */
	public String getDisplayForm(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDisplayFormType(int)
	 */
	public String getDisplayFormType(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDisplayFormURL(int)
	 */
	public String getDisplayFormURL(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDueDate(int)
	 */
	public Calendar getDueDate(int invoiceNumber) {
		Calendar cal = Calendar.getInstance();
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			Date date = ae.getDueDate();
			if(date != null)	cal.setTime(date);
			return cal; 
		}else {
			return null;
		}
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDueDateFrom(int)
	 */
	public String getDueDateFrom(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getDueDateTo(int)
	 */
	public String getDueDateTo(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getExchangeBank(int)
	 */
	public String getExchangeBank(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getExchangeClaim(int)
	 */
	public String getExchangeClaim(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getExchangeCurrency(int)
	 */
	public String getExchangeCurrency(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getExchangeRule(int)
	 */
	public String getExchangeRule(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getExchangeType(int)
	 */
	public String getExchangeType(int claimNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getFinalDueDate(int)
	 */
	public Calendar getFinalDueDate(int invoiceNumber) {

		Calendar cal = Calendar.getInstance();
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			Date date = ae.getFinalDueDate();
			if(date != null)	cal.setTime(date);
			return cal; 
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getLoginName()
	 */
	public String getUsername(int groupId) {
		BankInfo bi = getBankInfo(groupId);
		if(bi != null) {
			return bi.getUsername();
		}
		return "";
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getLoginPsw()
	 */
	public String getPassword(int groupId) {
		BankInfo bi = getBankInfo(groupId);
		if(bi != null) {
			return bi.getPassword();
		}
		return "";
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getNoteNumber(int)
	 */
	public String getNoteNumber(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getNotificationAndPaymentFee1(int)
	 */
	public double getNotificationAndPaymentFee1(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getNotificationAndPaymentFee1();
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getNotificationAndPaymentFee2(int)
	 */
	public double getNotificationAndPaymentFee2(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getNotificationAndPaymentFee2();
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getOtherCost(int)
	 */
	public double getOtherCost(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getOtherCost();
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getOtherOverdueCost(int)
	 */
	public String getOtherOverdueCost(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getOverdueFee(int)
	 */
	public String getOverdueFee(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getOverdueFee1(int)
	 */
	public String getOverdueFee1(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getOverdueFee2(int)
	 */
	public String getOverdueFee2(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getPayersSSN(int)
	 */
	public String getPayersSSN(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			User user = getUserByUserId(ae.getUserId());
			return user.getPersonalID();
		}else {
			return "";
		}
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getPaymentRule(int)
	 */
	public String getPaymentCode(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getPaymentCode();
		}
		return "1";
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getPenalIntrest(int)
	 */
	public String getPenalIntrest(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getPenalIntrestBankRate(int)
	 */
	public String getPenalIntrestCode(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getPenalIntrestCode();
		}
		return "";
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getPenalIntrestProsent(int)
	 */
	public double getPenalIntrestProsent(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getPenalIntrestProsent();
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getPenalIntrestRule(int)
	 */
	public String getPenalIntrestRule(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getPenalIntrestRule();
		}
		return "1";
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getReferenceNumber(int)
	 */
	public int getBatchNumber(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getBatchNumber();
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getSessionId()
	 */
	public String getSessionId() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getStatus(int)
	 */
	public String getStatus(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getTradeNumber(int)
	 */
	public String getTradeNumber(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getTransactionDayFrom(int)
	 */
	public String getTransactionDayFrom(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.BankFileManager#getTransactionDayTo(int)
	 */
	public String getTransactionDayTo(int invoiceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * returns the status of the invoice. 
	 * The status can be:
	 * NOT PAYED - ÓGREIDD
	 * PAYED - GREIDD
	 * CREATED - STOFNUD
	 * SENT - SEND
	 * ERROR - VILLA
	 */
	public String getInvoiceStatus(int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			return ae.getInvoiceStatus();
		}
		return "STOFNUD";
		
	}
	public void setInvoiceStatus(String status, int invoiceNumber) {
		AccountEntry ae = getAccountEntry(invoiceNumber);
		if(ae != null) {
			ae.setInvoiceStatus(status);
		}
		
	}
	private AccountEntry getAccountEntry(int invoiceNumber) {
		AccountEntry ae = null;
		AccountEntryHome aeh = null;
		try {
			aeh = (AccountEntryHome) IDOLookup.getHome(AccountEntry.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		if(aeh != null) {
			try {
				ae = aeh.findByInvoiceNumber(invoiceNumber);
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		return ae;
	}
	
	private BankInfo getBankInfo(int groupId) {
		BankInfo bi = null;
		BankInfoHome bih = null;
		try {
			bih = (BankInfoHome) IDOLookup.getHome(BankInfo.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		if(bih != null) {
			try {
				bi = bih.findByGroupId(groupId);
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		return bi;
	}
	
	private User getUserByUserId(int userId) {
		User u = null;
		UserHome uh = null;
		try {
			uh = (UserHome) IDOLookup.getHome(User.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		if(uh != null) {
			try {
				u = uh.findByPrimaryKey(new Integer(userId));
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		return u;
	}
	private Group getGroupByGroupId(int groupId) {
		Group g = null;
		GroupHome gh = null;
		try {
			gh = (GroupHome) IDOLookup.getHome(Group.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		if(gh != null) {
			try {
				g = gh.findByPrimaryKey(new Integer(groupId));
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		return g;
	}
}
