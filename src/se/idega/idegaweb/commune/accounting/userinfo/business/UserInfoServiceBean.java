/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.CreateException;

import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome;
import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncomeHome;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOStoreException;

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

}
