/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome;
import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncomeHome;
import se.idega.idegaweb.commune.accounting.userinfo.data.InvoiceReceiver;
import se.idega.idegaweb.commune.accounting.userinfo.data.InvoiceReceiverHome;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOStoreException;
import com.idega.user.data.User;

/**
 * UserInfoServiceBean
 * @author aron 
 * @version 1.0
 */

public class UserInfoServiceBean extends IBOServiceBean implements UserInfoService {
	
	public BruttoIncome createBruttoIncome(Integer userID,Float income, Date validFrom,Integer creatorID) throws RemoteException{
		BruttoIncome bruttoIncome = null;
		try {
			bruttoIncome = getBruttoIncomeHome().create();
			bruttoIncome.setIncome(income);
			bruttoIncome.setValidFrom((java.sql.Date)validFrom);
			bruttoIncome.setUser(userID);
			bruttoIncome.setCreator(creatorID);
			bruttoIncome.setCreated(new Timestamp(System.currentTimeMillis()));
			bruttoIncome.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		return bruttoIncome;
	}
	
	public BruttoIncomeHome getBruttoIncomeHome() throws RemoteException{
		return (BruttoIncomeHome) getIDOHome(BruttoIncome.class);
	}

	/**
	 * Sets the specified user as invoice receiver.
	 * @param user the user to set as invoice receiver
	 * @author Anders Lindman
	 */
	public InvoiceReceiver createInvoiceReceiver(User user) {
		InvoiceReceiver ir = null;
		try {
			InvoiceReceiverHome home = getInvoiceReceiverHome();
			try {
				ir = home.findByPrimaryKey(user.getPrimaryKey());
			} catch (FinderException e) {
				ir = home.create();
				ir.setUser(user);
			}
			ir.setIsReceiver(true);
			ir.store();
		} catch (Exception e) {}
		return ir;
	}
	
	/**
	 * @author Anders Lindman 
	 */
	public InvoiceReceiverHome getInvoiceReceiverHome() throws RemoteException {
		return (InvoiceReceiverHome) getIDOHome(InvoiceReceiver.class);
	}
	
	/**
	 * Returns true if the user with the specified id is an invoice receiver.
	 * @author Anders Lindman
	 */
	public boolean isInvoiceReceiver(int userId) {
		boolean isReceiver = false;
		try {
			InvoiceReceiver ir = getInvoiceReceiverHome().findByUser(userId);
			isReceiver = ir.getIsReceiver();
		} catch (Exception e) {}
		return isReceiver;
	}
	
	/**
	 * Returns true if the specified user is an invoice receiver.
	 * @author Anders Lindman
	 */
	public boolean isInvoiceReceiver(User user) {
		int userId = -1;
		try {
			userId = ((Integer) user.getPrimaryKey()).intValue();
		} catch (Exception e) {}
		return isInvoiceReceiver(userId);
	}
}
