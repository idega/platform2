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

import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplication;
import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplicationHome;
import is.idega.idegaweb.atvr.supplier.application.data.ProductCategory;
import is.idega.idegaweb.atvr.supplier.application.data.ProductCategoryHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class NewProductApplicationBusinessBean extends IBOServiceBean implements NewProductApplicationBusiness {
	public void insertApplication(NewProductApplication application) {
		application.setStatus("S");
		application.store();
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

	public void confirmApplications(String ids[]) {
		try {
			NewProductApplicationHome home = (NewProductApplicationHome) IDOLookup.getHome(NewProductApplication.class);
			for (int i = 0; i < ids.length; i++) {
				NewProductApplication app = home.findByPrimaryKey(new Integer(ids[i]));
				app.setStatus("C");
				app.store();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void markApplicationsAsSent(String ids[]) {
		try {
			NewProductApplicationHome home = (NewProductApplicationHome) IDOLookup.getHome(NewProductApplication.class);
			for (int i = 0; i < ids.length; i++) {
				NewProductApplication app = home.findByPrimaryKey(new Integer(ids[i]));
				app.setStatus("F");
				app.store();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void markApplicationsAsSent(Collection col) {
		try {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				NewProductApplication app = (NewProductApplication) it.next();
				app.setStatus("F");
				app.store();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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

	public Collection getAllUnconfirmedApplications() {
		try {
			return ((NewProductApplicationHome) IDOLookup.getHome(NewProductApplication.class)).findAllByStatus("S");
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}

		return null;
	}

	public Collection getAllConfirmedApplications() {
		try {
			return ((NewProductApplicationHome) IDOLookup.getHome(NewProductApplication.class)).findAllByStatus("C");
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}

		return null;
	}

	public Collection getAllSentToFileApplications() {
		try {
			return ((NewProductApplicationHome) IDOLookup.getHome(NewProductApplication.class)).findAllByStatus("F");
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}

		return null;
	}

	public boolean storeProductCategory(String category, String name, String parent) {
		try {
			ProductCategoryHome home = (ProductCategoryHome) IDOLookup.getHome(ProductCategory.class);
			ProductCategory cat = home.create();
			cat.setCategory(category);
			cat.setDescription(name);
			if (parent != null && !parent.equals("")) {
				Collection par = home.findAllByCategory(parent);
				if (par != null && !par.isEmpty()) {
					Iterator it = par.iterator();
					if (it.hasNext()) {
						ProductCategory catEntry = (ProductCategory)it.next();
						cat.setBelongsToCategory(((Integer)catEntry.getPrimaryKey()).intValue());
						
						cat.store();
						return true;
					}
				}
				
				return false;
			}
			
			cat.store();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}