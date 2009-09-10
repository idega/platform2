package com.idega.block.finance.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface AssessmentBusinessHome extends IBOHome {
	public AssessmentBusiness create() throws CreateException, RemoteException;
}