package com.idega.block.importer.business;


/**
 * Title:PostalCodeBundleStarter
 * Description: PostalCodeBundleStarter implements the IWBundleStartable interface. The start method of this
 * object is called during the Bundle loading when starting up a idegaWeb applications. It is used to register and update postalcodes from text files
 * within the bundle that it is registered in. Simply registed this starter class in your bundle and create a folder called 'postalcode' and put a text file(s) in
 * it with the postal codes. The text file must be a commaseparated file with the columns as such:<br>
 * code;areaname;countryname
 * Copyright: idega software 2002
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0 
 */

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import com.idega.block.importer.data.ColumnSeparatedImportFile;
import com.idega.business.IBOLookup;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.FileUtil;

public class PostalCodeBundleStarter implements IWBundleStartable{

private IWApplicationContext iwac;

  public PostalCodeBundleStarter() {
  }

  public void start(IWBundle bundle) {
		iwac = bundle.getApplication().getIWApplicationContext();
		File postalCodeFolder = new File(bundle.getResourcesRealPath() + FileUtil.getFileSeparator() + "postalcode");
		if (postalCodeFolder.isDirectory()) {
			File[] files = postalCodeFolder.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if(files[i].isFile()) {
						ColumnSeparatedImportFile postals = new ColumnSeparatedImportFile(files[i]);
						try {
							String record;
							while (!(record = (String) postals.getNextRecord()).equals("")) {
								ArrayList values = postals.getValuesFromRecordString(record);
								createPostalIfDoesNotExist((String) values.get(0), (String) values.get(1),
										(String) values.get(2));
							}
						}
						catch (Exception e) {
							System.err.println("PostalCodeBundleStarter : Cant use file = " + files[i].getName()
									+ " (error = " + e.getMessage() + ")");
						}
					}
				}
			}
		}
		
		//Add an "other" commmune
		
		try {
			AddressBusiness biz = getAddressBusiness();
			biz.getOtherCommuneCreateIfNotExist();
		} catch (Exception e) {
			System.out.println("Could not create the 'Other' commune");
			e.printStackTrace();
		}

	}


	private void createPostalIfDoesNotExist(String code, String area, String countryName){
		
		try {
			AddressBusiness biz = getAddressBusiness();
			Country country = ((CountryHome)IDOLookup.getHome(Country.class)).findByCountryName(countryName);
			
			// connect postal code to commune and create commune if needed
			PostalCode postalCode = biz.getPostalCodeAndCreateIfDoesNotExist(code,area,country);
			if(null == postalCode.getCommuneID() || postalCode.getCommuneID().length() == 0) {
				biz.connectPostalCodeToCommune(postalCode, area);
			}
			
		} catch (Exception e) {
			System.out.println("PostalCodeBundleStarter: import failed for : "+code+ ", "+area+", "+countryName );
			e.printStackTrace();
		}
	}

	private AddressBusiness getAddressBusiness() throws RemoteException{
		return (AddressBusiness) IBOLookup.getServiceInstance(iwac,AddressBusiness.class);
	}
	
	/**
	 * @see com.idega.idegaweb.IWBundleStartable#stop(IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		//does nothing...
	}
}
