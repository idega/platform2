/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.atvr.supplier.application.business;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;

import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplication;
import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplicationHome;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class NewProductApplicationBusinessBean extends IBOServiceBean implements NewProductApplicationBusiness {
	public void insertApplication(NewProductApplication application) {
		try {
			application.setStatus("S");
			
			application.store();
		}
		catch (RemoteException e) {
		}		
	}
	
	public NewProductApplication getNewApplication() {
		try {
			return ((NewProductApplicationHome) IDOLookup.getHome(NewProductApplication.class)).create();
		}
		catch (RemoteException e) {
		}
		catch (CreateException e) {
		}
	
		return null;
	}
	
	public Collection getAllApplications() {
		try {
			return ((NewProductApplicationHome) IDOLookup.getHome(NewProductApplication.class)).findAll();
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}
	
		return null;		
	}	
}