/*
 * $Id: BankInfoBusinessBean.java,v 1.1 2005/02/22 10:57:08 birna Exp $
 * Created on Feb 18, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.business;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.finance.data.BankInfo;
import com.idega.block.finance.data.BankInfoHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2005/02/22 10:57:08 $ by $Author: birna $
 * 
 * @author <a href="mailto:birna@idega.com">birna</a>
 * @version $Revision: 1.1 $
 */
public class BankInfoBusinessBean extends IBOServiceBean implements BankInfoBusiness {
	
  public BankInfoHome getBankInfoHome()throws java.rmi.RemoteException{
    return (BankInfoHome) IDOLookup.getHome(BankInfo.class);
  }
  
  public boolean insertBankInfoContract(Group club, String div, String group, String branch, 
  		String accountId, String ssn, String name, String username, String password) {
 
    	try {
  			BankInfo bi = null;
//  			bi = getBankInfoHome().findByGroupId((new Integer(group)).intValue());
  			if(bi == null) {
  				bi = getBankInfoHome().create();
  			}
  			
  			bi.setClubId(((Integer) club.getPrimaryKey()).intValue());
  			if (div != null)	bi.setDivisionId(Integer.valueOf(div).intValue());
  			if(group != null) bi.setGroupId(Integer.valueOf(group).intValue());
  			bi.setClaimantsBankBranchNumber(branch);
  			bi.setAccountBook(66);
  			bi.setAccountId(accountId);
  			bi.setClaimantsSSN(ssn);
  			bi.setClaimantsName(name);
  			bi.setUsername(username);
  			bi.setPassword(password);
  			
  			bi.store();
  			
  			return true;
  		}
  		catch (NumberFormatException e) {
  			e.printStackTrace();
  		}
  		catch (RemoteException e) {
  			e.printStackTrace();
  		}
  		catch (EJBException e) {
  			e.printStackTrace();
  		}
//  		catch (FinderException e) {
//  			e.printStackTrace();
//  		}
  		catch (CreateException e) {
  			e.printStackTrace();
  		}

    	
   
    
  		return false;
  		
  	
  }
  
  public boolean deleteContract(String ids[]) {
    try {
	   	for (int i = 0; i < ids.length; i++) {
	        Integer id = new Integer(ids[i]);
	        BankInfo bi = getBankInfoHome()
	                .findByPrimaryKey(id);
	        bi.remove();
	    }
	
	    return true;
	    } catch (FinderException e) {
	        e.printStackTrace();
	    } catch (EJBException e) {
	        e.printStackTrace();
	    } catch (RemoteException e) {
	    		e.printStackTrace();
	    } catch (RemoveException e) {
	    		e.printStackTrace();
	    }
	
	    return false;
  }
  
  public Collection findAllContractsByClub(Group club) {
  		try {
			return getBankInfoHome().findAllByClub(club);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
  }
}
