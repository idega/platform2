package is.idega.block.nationalregister.business;

import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.GenericImportFile;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;
import com.idega.util.datastructures.MultivaluedHashMap;

/**
 * @author palli
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NationalRegisterFileImportHandlerBean extends IBOServiceBean implements NationalRegisterFileImportHandler, ImportFileHandler {
	private ImportFile _file;
	//	private UserTransaction transaction;

	private ArrayList _failedRecords = new ArrayList();
	private ArrayList _value;
	private MultivaluedHashMap _familyRelations;
	private MultivaluedHashMap _spouseRelations;

	private static int COLUMN_SYMBOL = 0;
	private static int COLUMN_OLD_ID = 1;
	private static int COLUMN_SSN = 2;
	private static int COLUMN_FAMILY_ID = 3;
	private static int COLUMN_NAME = 4;
	private static int COLUMN_COMMUNE = 5;
	private static int COLUMN_STREET = 6;
	private static int COLUMN_BUILDING = 7;
	private static int COLUMN_FLOOR = 8;
	private static int COLUMN_SEX = 9;
	private static int COLUMN_MARITIAL_STATUS = 10;
	private static int COLUMN_EMPTY = 11;
	private static int COLUMN_NO_MAIL = 12;
	private static int COLUMN_NATIONALITY = 13;
	private static int COLUMN_PLACE_OF_BIRTH = 14;
	private static int COLUMN_SPOUSE_SSN = 15;
	private static int COLUMN_STATUS = 16;
	private static int COLUMN_PARISH = 17;
	private static int COLUMN_PO = 18;
	private static int COLUMN_ADDRESS = 19;

	
	
	public static void main(String[] args) throws RemoteException {
		NationalRegisterFileImportHandlerBean bean = new NationalRegisterFileImportHandlerBean();
		ImportFile file = new GenericImportFile(new File("/User/gimmi/Desktop/thjodskra.txt"));
		bean.setImportFile(file);
		boolean tmp = bean.handleRecords();
		System.out.println("Worked = "+tmp);
	}
	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#handleRecords()
	 */
	public boolean handleRecords() throws RemoteException {
//		UserTransaction transaction = getSessionContext().getUserTransaction();

		Timer clock = new Timer();
		clock.start();

		try {
			//if the transaction failes all the users and their relations are removed
//			transaction.begin();

			//iterate through the records and process them
			String item;
			_familyRelations = new MultivaluedHashMap();
			_spouseRelations = new MultivaluedHashMap();
			int count = 0;
			while (!(item = (String) _file.getNextRecord()).equals("")) {
				count++;

				if (!processRecord(item))
					_failedRecords.add(item);

				if ((count % 100) == 0) {
					System.out.println("NationalRegisterHandler processing RECORD [" + count + "] time: " + IWTimestamp.getTimestampRightNow().toString());
				}

				item = null;
			}
			
			clock.stop();
			System.out.println("Time to handleRecords: " + clock.getTime() + " ms  OR " + ((int) (clock.getTime() / 1000)) + " s");
			clock.start();
			handleFamilyRelation();

			printFailedRecords();

			clock.stop();
			System.out.println("Time to handleFamilyRelation: " + clock.getTime() + " ms  OR " + ((int) (clock.getTime() / 1000)) + " s");

//			transaction.commit();

			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();

//			try {
//				transaction.rollback();
//			}
//			catch (SystemException e) {
//				e.printStackTrace();
//			}

			return false;
		}

	}

	private boolean handleFamilyRelation() throws RemoteException {
		if (_familyRelations != null) {
			Set keys = _familyRelations.keySet();
			UserHome userHome = null; 
			NationalRegisterBusiness natReg = null;
			MemberFamilyLogic familyLogic = null;
			try {
				familyLogic = (MemberFamilyLogic) getServiceInstance(MemberFamilyLogic.class);
				natReg = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
				UserBusiness userBusiness = (UserBusiness) getServiceInstance(UserBusiness.class);
				userHome = userBusiness.getUserHome();
			} catch (IBOLookupException e) {
				e.printStackTrace();
			}
			
			if (keys != null && userHome != null && natReg != null) {
				Iterator keysIter = keys.iterator();
				Iterator ssnIter;
				String key;
				String ssn;
				int counter = 0;
				Collection ssnColl;
				Collection familyColl;
				while (keysIter.hasNext()) {
					++counter;
					key = (String) keysIter.next();
					familyColl = new Vector();
					ssnColl = _familyRelations.getCollection(key);
					if (ssnColl != null) {
						ssnIter = ssnColl.iterator();
						while (ssnIter.hasNext()) {
							ssn = (String) ssnIter.next();
							try {
								familyColl.add(userHome.findByPersonalID(ssn));
							} catch (FinderException e1) {
								e1.printStackTrace();
							}
						}
					}
					
					handleFamilyCollection(natReg, familyLogic, userHome, familyColl);
					if ((counter % 100) == 0) {
						System.out.println("NationalRegisterHandler processing family relations RECORD [" + counter + "] time: " + IWTimestamp.getTimestampRightNow().toString());
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean handleFamilyCollection(NationalRegisterBusiness natRegBus, MemberFamilyLogic familyLogic, UserHome uHome, Collection coll) throws RemoteException {
		if (coll != null) {
			NationalRegister natReg;
			Iterator iter = coll.iterator();
			Collection coll2 = new Vector(coll);
			Iterator iter2 = coll.iterator();
			Collection parents = new Vector();
			User user;
			User user2;
			Age age;
			int oldestAge = 0;
			String spouseSSN;
			User oldestPerson = null;
			while (iter.hasNext()) {
				try {
					user = (User) iter.next();
					if (user.getDateOfBirth() != null) {
						age = new Age(user.getDateOfBirth());
						if (age.getYears() > oldestAge) {
							oldestAge = age.getYears();
							oldestPerson = user;
						}
					}
					natReg = natRegBus.getEntryBySSN(user.getPersonalID());
					spouseSSN = natReg.getSpouseSSN();
					if (spouseSSN != null && !"".equals(spouseSSN)) {
						parents.add(user);
						parents.add(uHome.findByPersonalID(spouseSSN));
						oldestAge = 0;
						oldestPerson = null;
						break;
					}
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
			if (oldestPerson != null) {
				parents.add(oldestPerson);
			}
			
			iter = coll.iterator();
			while (iter.hasNext()) {
				user = (User) iter.next();
				//System.out.println("Got user : "+user.getName());
				coll2.remove(user);
				iter2 = coll2.iterator();
				while (iter2.hasNext()) {
					user2 = (User) iter2.next();
					//System.out.println("      checking user : "+user2.getName());
					try {
						if (parents.contains(user)) {
							if (parents.contains(user2)) {
								familyLogic.setAsSpouseFor(user, user2);
							} else {
								familyLogic.setAsParentFor(user, user2);
							}
						} else {
							if (parents.contains(user2)) {
								familyLogic.setAsChildFor(user, user2);
							} else {
								familyLogic.setAsSiblingFor(user, user2);
							}
						}
					} catch (CreateException e) {
						e.printStackTrace();
					}
				}
			}			
			
		}
		
		return false;
	}
	
	private boolean processRecord(String record) throws RemoteException {
		_value = _file.getValuesFromRecordString(record);

		boolean success = storeNationRegisterEntry();

		_value = null;

		return success;
	}

	public void printFailedRecords() {
		System.out.println("Import failed for these records, please fix and import again:");

		Iterator iter = _failedRecords.iterator();
		while (iter.hasNext()) {
			System.out.println((String) iter.next());
		}
	}

	protected boolean storeNationRegisterEntry() throws RemoteException {
		//variables
		String symbol = getProperty(COLUMN_SYMBOL);
		String oldId = getProperty(COLUMN_OLD_ID);
		String ssn = getProperty(COLUMN_SSN);
		String familyId = getProperty(COLUMN_FAMILY_ID);
		String name = getProperty(COLUMN_NAME);
		String commune = getProperty(COLUMN_COMMUNE);
		String street = getProperty(COLUMN_STREET);
		String building = getProperty(COLUMN_BUILDING);
		String floor = getProperty(COLUMN_FLOOR);
		String sex = getProperty(COLUMN_SEX);
		String maritialStatus = getProperty(COLUMN_MARITIAL_STATUS);
		String empty = getProperty(COLUMN_EMPTY);
		String prohibitMarking = getProperty(COLUMN_NO_MAIL);
		String nationality = getProperty(COLUMN_NATIONALITY);
		String placeOfBirth = getProperty(COLUMN_PLACE_OF_BIRTH);
		String spouseSSN = getProperty(COLUMN_SPOUSE_SSN);
		String fate = getProperty(COLUMN_STATUS);
		String parish = getProperty(COLUMN_PARISH);
		String po = getProperty(COLUMN_PO);
		String address = getProperty(COLUMN_ADDRESS);

//		System.out.println("ssn = " + ssn);

		if (ssn == null || ssn.equals(""))
			return false;

		//			//initialize business beans and data homes           
		NationalRegisterBusiness natReg = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
		
		boolean success = natReg.updateEntry(symbol,oldId,ssn,familyId,name,commune,street,building,
		                          floor,sex,maritialStatus,empty,prohibitMarking,
		                          nationality,placeOfBirth,spouseSSN,fate,parish,po,address);
		if (success) {
			_familyRelations.put(familyId, ssn);
		}
		
		return success;
	}

	private String getProperty(int columnIndex) {
		String value = null;

		if (_value != null) {

			try {
				value = (String) _value.get(columnIndex);
			}
			catch (RuntimeException e) {
				return null;
			}
			if (_file.getEmptyValueString().equals(value))
				return null;
			else
				return value;
		}
		else
			return null;
	}

	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#setImportFile(com.idega.block.importer.data.ImportFile)
	 */
	public void setImportFile(ImportFile file) throws RemoteException {
		_file = file;
	}

	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#setRootGroup(com.idega.user.data.Group)
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#getFailedRecords()
	 */
	public List getFailedRecords() throws RemoteException {
		return _failedRecords;
	}
}