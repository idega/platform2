package com.idega.block.finance.business;

import java.util.Collection;
import com.idega.block.finance.data.BankInfoHome;
import com.idega.user.data.Group;


public interface BankInfoBusiness extends com.idega.business.IBOService {
	
	public BankInfoHome getBankInfoHome()throws java.rmi.RemoteException;
	
	public boolean insertBankInfoContract(Group club, String div, String group, String branch, 
  		String accountId, String ssn, String name, String username, String password);
	
  public boolean deleteContract(String ids[]);
  
  public Collection findAllContractsByClub(Group club);
}
