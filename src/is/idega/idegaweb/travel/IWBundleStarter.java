/*
 * $Id: IWBundleStarter.java,v 1.1 2005/05/10 21:34:41 gimmi Exp $
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
import com.idega.core.component.data.ICObject;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;


public class IWBundleStarter implements com.idega.idegaweb.IWBundleStartable {

	public static final String DATASOURCE = "datasource";

	public void start(IWBundle starterBundle) {
		System.out.print("Travel bundle starting ... ");
		checkDataSource(starterBundle);
		System.out.println(" .. done");
	}

	public void stop(IWBundle starterBundle) {
	}
	
	private void checkDataSource(IWBundle bundle) {
		// Switching the datasource
		String dataSource = bundle.getProperty(DATASOURCE);
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
							home.setDatasource(dataSource);
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
