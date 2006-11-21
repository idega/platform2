/*
 * $Id: NationalRegisterDeceasedFileImportHandlerBean.java,v 1.1.2.1 2006/11/21 23:35:25 idegaweb Exp $
 * Created on Nov 21, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.nationalregister.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.block.nationalregister.data.NationalRegisterFate;
import is.idega.block.nationalregister.data.NationalRegisterFateHome;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressType;
import com.idega.core.location.data.AddressTypeHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;


public class NationalRegisterDeceasedFileImportHandlerBean extends IBOServiceBean 
		implements NationalRegisterDeceasedFileImportHandler, ImportFileHandler {
	
	private ImportFile file;

	private ArrayList failedRecordList = new ArrayList();

	private ArrayList valueList;

	public final static int COLUMN_SYMBOL = 0;

	public final static int COLUMN_SSN = 1;
	
	public final static int COLUMN_DATE_OF_DEATH = 2;

	public final static int COLUMN_NAME = 3;

	public final static int COLUMN_STREET = 4;

	public final static int COLUMN_COMMUNE = 5;

	public final static int COLUMN_GENDER = 6;

	public final static int COLUMN_MARITIAL_STATUS = 7;

	public final static int COLUMN_SPOUSE_SSN = 8;

	private FamilyLogic famLog = null;

	private NationalRegisterBusiness natRegBiz;

	private NationalRegisterDeceasedBusiness deceasedBiz;

	private UserBusiness uBiz;
	
	private static Gender maleGender = null;
	
	private static Gender femaleGender = null;
	
	private String deceasedAddressString = null;
	
	private NationalRegisterFate fate = null;
	
	private User performer = null;

	private NumberFormat twoDigits = NumberFormat.getNumberInstance();
	
	public final static String IW_BUNDLE_IDENTIFIER = "is.idega.block.nationalregister";

	public List getFailedRecords() throws RemoteException {
		return failedRecordList;
	}

	public FamilyLogic getMemberFamilyLogic() throws RemoteException {
		if (famLog == null) {
			famLog = (FamilyLogic) IBOLookup.getServiceInstance(
					getIWApplicationContext(), FamilyLogic.class);
		}
		return famLog;
	}

	public String getTimeString(long time) {
		long t = time;
		int milli = (int) (t % 1000);
		t = (t - milli) / 1000;
		int second = (int) (t % 60);
		t = (t - second) / 60;
		int minut = (int) (t) % 60;
		int hour = (int) ((t - minut) / 60);
		return twoDigits.format(hour) + ":" + twoDigits.format(minut) + ":"
				+ twoDigits.format(second) + "." + milli;
	}

	public boolean handleRecords() throws RemoteException {
		int count = 0;
		String item;
		try {
			natRegBiz = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
			deceasedBiz = (NationalRegisterDeceasedBusiness) getServiceInstance(NationalRegisterDeceasedBusiness.class);
			uBiz = (UserBusiness) getServiceInstance(UserBusiness.class);
			
			try {
				performer = IWContext.getInstance().getCurrentUser();
			} catch (NullPointerException n) {
				System.out.println("NationalRegisterImporter iwcontext instance not found");
				performer = null;
			}

			if (performer == null) {
				com.idega.core.user.data.User admUser = this.getIWMainApplication().getAccessController().getAdministratorUser();
				performer = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(admUser.getPrimaryKey());
			}
			
			fate = ((NationalRegisterFateHome) IDOLookup.getHome(NationalRegisterFate.class))
			.findByFateCode(NationalRegisterConstants.FATE_DECEASED);
			
			if (deceasedAddressString == null) {
				try {
					IWBundle iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
					IWResourceBundle iwrb = iwb.getResourceBundle(LocaleUtil.getIcelandicLocale());
					deceasedAddressString = iwrb.getLocalizedString("national_register.deceased", "Deceased");
				}
				catch (Exception e) {
					deceasedAddressString = "";
					System.out.println("Unable to initialize deceasedAddressString");
				}
			}
			
			while (!(item = (String) file.getNextRecord()).equals("")) {
				count++;
				if (!processRecord(item)) {
					failedRecordList.add(item);
				}
	
				item = null;
			}
			file.close();
			printFailedRecords();
	
			return true;
		
		} catch (Exception ex) {
			ex.printStackTrace();

			return false;
		}
	}

	public void printFailedRecords() {
		if (!failedRecordList.isEmpty()) {
			System.out.println("Import failed for these records, please fix and import again:");
		}
		Iterator iter = failedRecordList.iterator();
		while (iter.hasNext()) {
			System.out.println((String) iter.next());
		}
	}

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}
	
	private String getProperty(int columnIndex) {
		String value = null;

		if (valueList != null) {

			try {
				value = (String) valueList.get(columnIndex);
			} catch (RuntimeException e) {
				return null;
			}
			if (file.getEmptyValueString().equals(value))
				return null;
			else
				return value;
		} else
			return null;
	}
	
	private boolean processRecord(String record) throws RemoteException,
	CreateException {
		valueList = file.getValuesFromRecordString(record);

		boolean success = storeDeceasedNationRegisterEntry();

		valueList = null;

		return success;
	}
	
	protected boolean storeDeceasedNationRegisterEntry() throws RemoteException,
	CreateException {
		// variables
		String symbol = getProperty(COLUMN_SYMBOL);
		String ssn = getProperty(COLUMN_SSN);
		String dateOfDeath = getProperty(COLUMN_DATE_OF_DEATH);
		String name = getProperty(COLUMN_NAME);
		String street = getProperty(COLUMN_STREET);
		String commune = getProperty(COLUMN_COMMUNE);
		String gender = getProperty(COLUMN_GENDER);
		String maritialStatus = getProperty(COLUMN_MARITIAL_STATUS);
		String spouseSSN = getProperty(COLUMN_SPOUSE_SSN);

		boolean success = true;

		if (ssn == null || ssn.equals(""))
			return false;

			success = deceasedBiz.updateEntry(symbol, ssn, dateOfDeath, name,
					street, commune, gender, maritialStatus, spouseSSN);
		
			Gender userGender = null;
			try {
				userGender = getGender(gender);
			}
			catch (FinderException e) {
				System.out.println(e.getMessage());
			}
			User user = uBiz.createUserByPersonalIDIfDoesNotExist(name,ssn,userGender,null);
			
			try {	
				NationalRegister natRegEntry = natRegBiz.getEntryBySSN(ssn);
				if (natRegEntry != null && fate != null) {
					natRegEntry.setFate(fate.getFateString());
					natRegEntry.store();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Collection addresses = user.getAddresses();
			if (!addresses.isEmpty()) {
				Iterator addrIt = addresses.iterator();
				Address addr = null;
				while (addrIt.hasNext()) {
					addr = (Address)addrIt.next();
					addr.setStreetName(deceasedAddressString);
					addr.setStreetNumber(null);
					addr.setPostalCode(null);
					addr.setCity(null);
					addr.setCommune(null);
					addr.setCountry(null);
					addr.store();
				}
			} else {
				AddressTypeHome addressHome = (AddressTypeHome) IDOLookup.getHome(AddressType.class);
				AddressType at1 = null;
				AddressType at2 = null;
				try {
				    at1 = addressHome.findAddressType1();
				    Address address1 = (Address) IDOLookup.instanciateEntity(Address.class);
					address1.setAddressType(at1);
					address1.setStreetName(deceasedAddressString);
					address1.store();
					
					at2 = addressHome.findAddressType2();
					Address address2 = (Address) IDOLookup.instanciateEntity(Address.class);
					address2.setAddressType(at2);
					address2.setStreetName(deceasedAddressString);	
					address2.store();
					
					user.addAddress(address1);
					user.addAddress(address2);
					
				} catch (FinderException e) {
				    e.printStackTrace();
				} catch (IDOAddRelationshipException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			FamilyLogic familyService = getMemberFamilyLogic();
			IWTimestamp dom = IWTimestamp.RightNow();
			familyService.registerAsDeceased(user, dom.getDate(), performer);
			
		return success;
	}
	
	private Gender getGender(String gender) throws RemoteException, FinderException {
		if (maleGender == null || femaleGender == null) {
			GenderHome home = (GenderHome) getIDOHome(Gender.class);
			maleGender = home.getMaleGender();
			femaleGender = home.getFemaleGender();
			System.out.println("NationalRegisterDeceasedBusinessBean : setting up gender");
		}
		if (gender == null || gender.equals("")) {
			return null;
		} else if (gender.equals("1") || gender.equals("3")) {
			return maleGender;
		} else  {
			return femaleGender;
		}
	}
}