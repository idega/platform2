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

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.boxoffice.data.BoxLink;
import com.idega.block.boxoffice.data.BoxLinkHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
import com.idega.core.user.data.User;
import com.idega.core.user.data.UserHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.util.IWTimestamp;

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
						ProductCategory catEntry = (ProductCategory) it.next();
						cat.setBelongsToCategory(((Integer) catEntry.getPrimaryKey()).intValue());

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

	public boolean checkForNewReports(String path, IWBundle bundle) {
		try {
			File F = new File(path);
			ReportsFilenameFilter filter = new ReportsFilenameFilter();
			File[] Fs = F.listFiles(filter);
			if (Fs.length > 0) {
				List allFiles = java.util.Arrays.asList(Fs);
				Iterator it = allFiles.iterator();

				String sale_month = bundle.getProperty("npaa_sale_month_type", "manadarsala");
				String sale_year = bundle.getProperty("npaa_sale_year_type", "arssala");
				String sale_last_year = bundle.getProperty("npaa_sale_last_year_type", "sidarssala");
				String waitinglist = bundle.getProperty("npaa_waitinglist_type", "bidlisti");
				String pending = bundle.getProperty("npaa_pending_type", "vaentanlegt");
				String current = bundle.getProperty("npaa_current_type", "isolu");

				String sale_month_bx = bundle.getProperty("npaa_sale_month_bx");
				String sale_year_bx = bundle.getProperty("npaa_sale_year_bx");
				String sale_last_year_bx = bundle.getProperty("npaa_sale_last_year_bx");
				String waitinglist_bx = bundle.getProperty("npaa_waitinglist_bx");
				String pending_bx = bundle.getProperty("npaa_pending_bx");
				String current_bx = bundle.getProperty("npaa_current_bx");

				String sale_month_cat = bundle.getProperty("npaa_sale_month_cat");
				String sale_year_cat = bundle.getProperty("npaa_sale_year_cat");
				String sale_last_year_cat = bundle.getProperty("npaa_sale_last_year_cat");
				String waitinglist_cat = bundle.getProperty("npaa_waitinglist_cat");
				String pending_cat = bundle.getProperty("npaa_pending_cat");
				String current_cat = bundle.getProperty("npaa_current_cat");

				while (it.hasNext()) {
					File file = (File) it.next();
					String name = file.getName();
					StringTokenizer tok = new StringTokenizer(name, "_.");
					int num = tok.countTokens() - 1;
					String ssn = null;
					String type = null;
					String month = null;
					String year = null;
					switch (num) {
						case 2 :
							ssn = tok.nextToken();
							type = tok.nextToken();
							break;
						case 3 :
							ssn = tok.nextToken();
							type = tok.nextToken();
							year = tok.nextToken();
							break;
						case 4 :
							ssn = tok.nextToken();
							type = tok.nextToken();
							month = tok.nextToken();
							year = tok.nextToken();
					}

					int pos = ssn.indexOf("-");
					if (pos > -1)
						ssn = ssn.substring(0,pos) + ssn.substring(pos+1);

					System.out.println("ssn = " + ssn);

					BoxLink bx = ((BoxLinkHome) IDOLookup.getHome(BoxLink.class)).create();

					User user = null;
					try {
						user = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(ssn);
					}
					catch (FinderException ex) {
					}

					ICFile icfile = ((ICFileHome) IDOLookup.getHome(ICFile.class)).create();
					icfile.setFileValue(new FileInputStream(file));
					icfile.store();

					if (user != null) {
						int boxid = -1;
						int catid = -1;
						
						if (type.equals(sale_month)) {
							boxid = Integer.parseInt(sale_month_bx);
							catid = Integer.parseInt(sale_month_cat);
						}
						else if (type.equals(sale_year)) {
							boxid = Integer.parseInt(sale_year_bx);
							catid = Integer.parseInt(sale_year_cat);
						}
						else if (type.equals(sale_last_year)) {
							boxid = Integer.parseInt(sale_last_year_bx);
							catid = Integer.parseInt(sale_last_year_cat);
						}
						else if (type.equals(waitinglist)) {
							boxid = Integer.parseInt(waitinglist_bx);
							catid = Integer.parseInt(waitinglist_cat);
						}
						else if (type.equals(pending)) {
							boxid = Integer.parseInt(pending_bx);
							catid = Integer.parseInt(pending_cat);
						}
						else if (type.equals(current)) {
							boxid = Integer.parseInt(current_bx);
							catid = Integer.parseInt(current_cat);
						}

						if (boxid > -1 && catid > -1) {
							bx.setBoxCategoryID(boxid);
							bx.setBoxID(catid);
							bx.setCreationDate(IWTimestamp.getTimestampRightNow());
							bx.setName(name);
							bx.setFileID(((Integer)icfile.getPrimaryKey()).intValue());
							bx.setTarget("_blank");
							bx.setUserID(((Integer)user.getPrimaryKey()).intValue());
							
							bx.store();
						}
					}
					
					file.delete();
				}
			}

			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}