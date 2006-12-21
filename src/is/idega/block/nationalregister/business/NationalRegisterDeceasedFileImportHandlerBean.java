/*
 * $Id: NationalRegisterDeceasedFileImportHandlerBean.java,v 1.1.2.4 2006/12/21 13:26:11 idegaweb Exp $
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
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
import com.idega.util.Timer;


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

	private final static int BYTES_PER_RECORD_H1 = 95;

	private final static int BYTES_PER_RECORD_H2 = 107;

	private final static String AUTO_CREATE_NON_EXISTING_USERS_IN_DECEASED_IMPORT = "AUTO_CREATE_NON_EXISTING_USERS_IN_DECEASED_IMPORT";

	private static final String DATE_OF_DEATH_META_DATA_KEY = "date_of_death";

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
	
	private NumberFormat precentNF = NumberFormat.getPercentInstance();

	public final static String IW_BUNDLE_IDENTIFIER = "is.idega.block.nationalregister";

	public List getFailedRecords() throws RemoteException {
		return this.failedRecordList;
	}

	public FamilyLogic getMemberFamilyLogic() throws RemoteException {
		if (this.famLog == null) {
			this.famLog = (FamilyLogic) IBOLookup.getServiceInstance(
					getIWApplicationContext(), FamilyLogic.class);
		}
		return this.famLog;
	}

	public String getTimeString(long time) {
		long t = time;
		int milli = (int) (t % 1000);
		t = (t - milli) / 1000;
		int second = (int) (t % 60);
		t = (t - second) / 60;
		int minut = (int) (t) % 60;
		int hour = (int) ((t - minut) / 60);
		return this.twoDigits.format(hour) + ":" + this.twoDigits.format(minut) + ":"
				+ this.twoDigits.format(second) + "." + milli;
	}

	public boolean handleRecords() throws RemoteException {
		int count = 0;
		String item;
		Timer clock = new Timer();
		clock.start();
		try {
			this.natRegBiz = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
			this.deceasedBiz = (NationalRegisterDeceasedBusiness) getServiceInstance(NationalRegisterDeceasedBusiness.class);
			this.uBiz = (UserBusiness) getServiceInstance(UserBusiness.class);
			
			try {
				this.performer = IWContext.getInstance().getCurrentUser();
			} catch (NullPointerException n) {
				System.out.println("NationalRegisterDeceasedImporter iwcontext instance not found");
				this.performer = null;
			}

			if (this.performer == null) {
				com.idega.core.user.data.User admUser = this.getIWMainApplication().getAccessController().getAdministratorUser();
				this.performer = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(admUser.getPrimaryKey());
			}
			
			this.fate = ((NationalRegisterFateHome) IDOLookup.getHome(NationalRegisterFate.class))
			.findByFateCode(NationalRegisterConstants.FATE_DECEASED);
			
			if (this.deceasedAddressString == null) {
				try {
					IWBundle iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
					IWResourceBundle iwrb = iwb.getResourceBundle(LocaleUtil.getIcelandicLocale());
					this.deceasedAddressString = iwrb.getLocalizedString("national_register.deceased", "Deceased");
				}
				catch (Exception e) {
					this.deceasedAddressString = "";
					System.out.println("Unable to initialize deceasedAddressString");
				}
			}
			
			long totalBytes = this.file.getFile().length();
			String deceasedImportType = getDeceasedImportType();
			long totalRecords = 0;
			if (deceasedImportType.equalsIgnoreCase("H1")) {
				totalRecords = totalBytes / BYTES_PER_RECORD_H1;
			} else {
				totalRecords = totalBytes / BYTES_PER_RECORD_H2;
			}

			this.twoDigits.setMinimumIntegerDigits(2);

			long beginTime = System.currentTimeMillis();
			long lastTimeCheck = beginTime;
			long averageTimePerUser1000 = 0;
			long timeLeft1000 = 0;
			long estimatedTimeFinished100 = beginTime;

			IWTimestamp stamp;
			double progress = 0;
			int intervalBetweenOutput = 1000;

			System.out.println("NationalRegisterDeceasedImport processing RECORD [0] time: "
					+ IWTimestamp.getTimestampRightNow().toString());
			while (!(item = (String) this.file.getNextRecord()).equals("")) {
				count++;
				if (!processRecord(item)) {
					this.failedRecordList.add(item);
				}
				if ((count % intervalBetweenOutput) == 0) {
					averageTimePerUser1000 = (System.currentTimeMillis() - lastTimeCheck)
							/ intervalBetweenOutput;
					lastTimeCheck = System.currentTimeMillis();
					timeLeft1000 = averageTimePerUser1000
							* (totalRecords - count);
					estimatedTimeFinished100 = System.currentTimeMillis()
							+ timeLeft1000;

					progress = ((double) count) / ((double) totalRecords);

					System.out.print("NatRegDeceasedImport "
							+ IWTimestamp.getTimestampRightNow().toString()
							+ ", processing RECORD [" + count + " / "
							+ totalRecords + "]");

					stamp = new IWTimestamp(estimatedTimeFinished100);
					System.out.println(" | " + this.precentNF.format(progress)
							+ " done, guestimated time left of PHASE : "
							+ getTimeString(timeLeft1000) + "  finish at "
							+ stamp.getTime().toString());
				}
	
				item = null;
			}
			this.file.close();
			System.out.println("NationalRegisterDeceasedImport processed RECORD [" + count
					+ "] time: "
					+ IWTimestamp.getTimestampRightNow().toString());
			clock.stop();
			long msTime = clock.getTime();
			long secTime = msTime / 1000;

			System.out.println("Time to handleRecords: " + msTime + " ms  OR "
					+ secTime + " s, averaging " + (msTime / count)
					+ "ms per record");
			printFailedRecords();
	
			return true;
		
		} catch (Exception ex) {
			ex.printStackTrace();

			return false;
		}
	}

	private String getDeceasedImportType() throws FileNotFoundException, IOException {
		java.io.FileReader fRead = new java.io.FileReader(this.file.getFile());
		StringBuffer buff = new StringBuffer();
		for (int i=0;i<2;i++) {
		  int c = fRead.read();
		  buff.append((char)c);
		}
		fRead.close();
		return buff.toString();
	}

	public void printFailedRecords() {
		if (!this.failedRecordList.isEmpty()) {
			System.out.println("Import failed for these records, please fix and import again:");
		}
		Iterator iter = this.failedRecordList.iterator();
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

		if (this.valueList != null) {

			try {
				value = (String) this.valueList.get(columnIndex);
			} catch (RuntimeException e) {
				return null;
			}
			if (this.file.getEmptyValueString().equals(value)) {
				return null;
			}
			else {
				return value;
			}
		}
		else {
			return null;
		}
	}
	
	private boolean processRecord(String record) throws RemoteException,
	CreateException {
		this.valueList = this.file.getValuesFromRecordString(record);

		boolean success = storeDeceasedNationRegisterEntry();

		this.valueList = null;

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

		if (ssn == null || ssn.equals("")) {
			return false;
		}

		success = this.deceasedBiz.updateEntry(symbol, ssn, dateOfDeath, name,
				street, commune, gender, maritialStatus, spouseSSN);
	
		Gender userGender = null;
		try {
			userGender = getGender(gender);
		}
		catch (FinderException e) {
			System.out.println(e.getMessage());
		}
		IWBundle bundle = getIWMainApplication().getBundle(NationalRegisterDeceasedFileImportHandlerBean.IW_BUNDLE_IDENTIFIER);
		String autoCreateUsersNonExistingUsersString = bundle.getProperty(AUTO_CREATE_NON_EXISTING_USERS_IN_DECEASED_IMPORT, "false");
		User user = null;
		try {
			user = this.uBiz.getUser(ssn);
		} catch (FinderException e) {
			//User not found in the member system and ignored, if AUTO_CREATE_NON_EXISTING_USERS property is not set then 
		}
		if (user == null && autoCreateUsersNonExistingUsersString.equalsIgnoreCase("true")) {
			IWTimestamp dateOfBirth = getDateOfBirthFromSSN(ssn);
			this.uBiz.createUserByPersonalIDIfDoesNotExist(name,ssn,userGender,dateOfBirth);
		}
		if (user != null) {
			try {	
				NationalRegister natRegEntry = this.natRegBiz.getEntryBySSN(ssn);
				if (natRegEntry != null && this.fate != null) {
					natRegEntry.setFate(this.fate.getFateString());
					natRegEntry.store();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.uBiz.updateUsersMainAddressOrCreateIfDoesNotExist(user, this.deceasedAddressString, null, null, null, null, null, null);
			this.uBiz.updateUsersCoAddressOrCreateIfDoesNotExist(user, this.deceasedAddressString, null, null, null, null, null, null);
			
			user.setMetaData(DATE_OF_DEATH_META_DATA_KEY, dateOfDeath);
			user.store();

			FamilyLogic familyService = getMemberFamilyLogic();
			IWTimestamp dom = IWTimestamp.RightNow();
			familyService.registerAsDeceased(user, dom.getDate(), this.performer);
		}
		return success;
	}

	private IWTimestamp getDateOfBirthFromSSN(String ssn) {
		IWTimestamp dateOfBirth = null;
		try {
			String day = ssn.substring(0,2);
			String month = ssn.substring(2,4);
			String year = ssn.substring(4,6);
			
			int iDay = Integer.parseInt(day);
			int iMonth = Integer.parseInt(month);
			int iYear = Integer.parseInt(year);
			if (ssn.substring(9).equals("9")) {
				iYear += 1900;
			}
			else if (ssn.substring(9).equals("0")) {
				iYear += 2000;
			}
			else if (ssn.substring(9).equals("8")) {
				iYear += 1800;
			}
			dateOfBirth = new IWTimestamp();
			dateOfBirth.setHour(0);
			dateOfBirth.setMinute(0);
			dateOfBirth.setSecond(0);
			dateOfBirth.setMilliSecond(0);
			dateOfBirth.setDay(iDay);
			dateOfBirth.setMonth(iMonth);
			dateOfBirth.setYear(iYear);
		} catch (Exception e) {
			System.out.println("Error creating dateOfBirth timestamp from SSN = " + ssn + "Error was = " + e.getMessage());
		}
		return dateOfBirth;
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