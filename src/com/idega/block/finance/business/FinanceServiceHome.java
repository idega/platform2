package com.idega.block.finance.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface FinanceServiceHome extends IBOHome {
	public FinanceService create() throws CreateException, RemoteException;
}