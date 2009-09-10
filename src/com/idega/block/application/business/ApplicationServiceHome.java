package com.idega.block.application.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface ApplicationServiceHome extends IBOHome {
	public ApplicationService create() throws CreateException, RemoteException;
}