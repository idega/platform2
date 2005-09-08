/*
 * $Id: IWBundleStarter.java,v 1.3 2005/09/08 22:28:50 gimmi Exp $
 * Created on 10.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel;

import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.core.component.data.ICObject;
import com.idega.core.data.ICApplicationBinding;
import com.idega.core.data.ICApplicationBindingHome;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;


public class IWBundleStarter implements com.idega.idegaweb.IWBundleStartable {

	public static final String DATASOURCE = "travel.datasource";

	public void start(IWBundle starterBundle) {
		System.out.print("Travel bundle starting ... ");
		checkDataSource(starterBundle);
		System.out.println(" .. done");
	}

	public void stop(IWBundle starterBundle) {
	}
	
	private void checkDataSource(IWBundle bundle) {
		// Switching the datasource
		String dataSource = null;
		try {
			ICApplicationBindingHome abHome = (ICApplicationBindingHome) IDOLookup.getHome(ICApplicationBinding.class);
			ICApplicationBinding ab = abHome.findByPrimaryKey(DATASOURCE);
			dataSource = ab.getValue();
		}
		catch (IDOLookupException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		if (dataSource == null) {
			dataSource = bundle.getProperty("datasource");
			try {
				ICApplicationBindingHome abHome = (ICApplicationBindingHome) IDOLookup.getHome(ICApplicationBinding.class);
				ICApplicationBinding ab = abHome.create();
				ab.setKey(DATASOURCE);
				ab.setValue(dataSource);
				ab.setBindingType("travel.binding");
				ab.store();
			}
			catch (IDOLookupException e1) {
				e1.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
		}

		System.out.print("datasource = "+dataSource);
		if (dataSource != null) {
			try {
				Collection entities = bundle.getDataObjects();
				if (entities != null){
					Iterator iter = entities.iterator();
					while (iter.hasNext())
					{
						ICObject ico = (ICObject) iter.next();
						try
						{
							Class c = ico.getObjectClass();
							IDOFactory home = (IDOFactory) IDOLookup.getHome(c);
							home.setDatasource(dataSource, false);
						}
						catch (ClassNotFoundException e)
						{
							System.out.println("Cant set the dataSource : Class " + e.getMessage() + " not found");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
