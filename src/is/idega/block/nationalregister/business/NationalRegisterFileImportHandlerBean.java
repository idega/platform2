package is.idega.block.nationalregister.business;

import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.idegaweb.member.business.MemberFamilyLogic;

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
import com.idega.block.importer.data.ImportFile;
import com.idega.block.importer.presentation.Importer;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWBundle;
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

	public final static int COLUMN_SYMBOL = 0;
	public final static int COLUMN_OLD_ID = 1;
	public final static int COLUMN_SSN = 2;
	public final static int COLUMN_FAMILY_ID = 3;
	public final static int COLUMN_NAME = 4;
	public final static int COLUMN_COMMUNE = 5;
	public final static int COLUMN_STREET = 6;
	public final static int COLUMN_BUILDING = 7;
	public final static int COLUMN_FLOOR = 8;
	public final static int COLUMN_SEX = 9;
	public final static int COLUMN_MARITIAL_STATUS = 10;
	public final static int COLUMN_EMPTY = 11;
	public final static int COLUMN_NO_MAIL = 12;
	public final static int COLUMN_NATIONALITY = 13;
	public final static int COLUMN_PLACE_OF_BIRTH = 14;
	public final static int COLUMN_SPOUSE_SSN = 15;
	public final static int COLUMN_STATUS = 16;
	public final static int COLUMN_PARISH = 17;
	public final static int COLUMN_PO = 18;
	public final static int COLUMN_ADDRESS = 19;
	public final static int COLUMN_ADDRESS_CODE = 20;
	public final static int COLUMN_DATE_OF_MODIFICATION = 21;
	public final static int COLUMN_PLACEMENT_CODE = 22;
	public final static int COLUMN_DATE_OF_CREATION = 23;
	public final static int COLUMN_LAST_DOMESTIC_ADDRESS = 24;
	public final static int COLUMN_AGENT_SSN = 25;
	public final static int COLUMN_NEW = 26;
	public final static int COLUMN_ADDRESS_NAME = 27;
	public final static int COLUMN_DATE_OF_DELETION = 28;
	public final static int COLUMN_NEW_SSN_OR_NAME = 29;
	public final static int COLUMN_DATE_OF_BIRTH = 30;
	
	private final static String PROPERTY_NAME_CREATE_ONLY = "NAT_REG_CREATE_ONLY";
	private final static String PROPERTY_NAME_RELATION_ONLY = "NAT_REG_RELATION_ONLY";
	private final static String PROPERTY_NAME_POSTAL_CODE_FIX = "NAT_REG_POSTAL_CODE_FIX";
	private boolean createOnly = false;
	private boolean relationOnly = false;
	private boolean postalCodeFix = false;
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
			IWBundle bundle = getIWMainApplication().getBundle(Importer.IW_BUNDLE_IDENTIFIER);
			String sCreateOnly = bundle.getProperty(PROPERTY_NAME_CREATE_ONLY);
			String sRelationOnly = bundle.getProperty(PROPERTY_NAME_RELATION_ONLY);
			String sPostal = bundle.getProperty(PROPERTY_NAME_POSTAL_CODE_FIX);
			createOnly = (sCreateOnly != null && sCreateOnly.equalsIgnoreCase("yes"));
			relationOnly = (sRelationOnly != null && sRelationOnly.equalsIgnoreCase("yes"));
			postalCodeFix = (sPostal != null && sPostal.equalsIgnoreCase("yes"));
			int count = 0;
			if (createOnly) {
				System.out.println("NationalRegisterHandler create only variable set to TRUE");
			}
			if (relationOnly) {
				System.out.println("NationalRegisterHandler relation only variable set to TRUE");
			}
			if (postalCodeFix) {
				System.out.println("NationalRegisterHandler postalCodeFix variable set to TRUE");
			}
			
			System.out.println("NationalRegisterHandler processing RECORD [0] time: " + IWTimestamp.getTimestampRightNow().toString());
			while (!(item = (String) _file.getNextRecord()).equals("")) {
				count++;

				if (!processRecord(item))
					_failedRecords.add(item);

				if ((count % 100) == 0) {
					System.out.println("NationalRegisterHandler processing RECORD [" + count + "] time: " + IWTimestamp.getTimestampRightNow().toString());
				}

				item = null;
			}			
			System.out.println("NationalRegisterHandler processed RECORD [" + count + "] time: " + IWTimestamp.getTimestampRightNow().toString());
			clock.stop();
			long msTime = clock.getTime();
			long secTime = msTime / 1000;
			
			System.out.println("Time to handleRecords: " + msTime + " ms  OR " + secTime + " s, averaging "+(msTime / count)+"ms per record");
			clock.start();
			handleFamilyRelation();
			clock.stop();
			msTime = clock.getTime();
			secTime = msTime / 1000;
			System.out.println("Time to handleFamilyRelation: " + clock.getTime() + " ms  OR " + ((int) (clock.getTime() / 1000)) + " s, averaging "+(msTime / count)+"ms per record");

			printFailedRecords();


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
				System.out.println("NationalRegisterHandler Total families to handle = "+keys.size());
				System.out.println("NationalRegisterHandler processing family relations RECORD [0] time: " + IWTimestamp.getTimestampRightNow().toString());
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
					try {
						handleFamilyCollection(natReg, familyLogic, userHome, familyColl);
					} catch (Exception e) {
						System.out.println("NationalRegisterHandler ERROR, familyRelation failed for family : "+key);
					}
					if ((counter % 100) == 0) {
						System.out.println("NationalRegisterHandler processing family relations RECORD [" + counter + "] time: " + IWTimestamp.getTimestampRightNow().toString());
					}
				}
				System.out.println("NationalRegisterHandler processed family relations RECORD [" + counter + "] time: " + IWTimestamp.getTimestampRightNow().toString());
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
					try {
						parents.add(uHome.findByPersonalID(spouseSSN));
						break;
					} catch (FinderException e) {
						//System.out.println("NationalRegisterHandler processed family relations RECORD [" + counter + "] time: " + IWTimestamp.getTimestampRightNow().toString());
						//e.printStackTrace();
					}
				}
			}
			if (parents.isEmpty() && oldestPerson != null) {
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

		String addressCode = getProperty(COLUMN_ADDRESS_CODE);
		String dateOfModification = getProperty(COLUMN_DATE_OF_MODIFICATION);
		String placementCode = getProperty(COLUMN_PLACEMENT_CODE);
		String dateOfCreation = getProperty(COLUMN_DATE_OF_CREATION);
		String lastDomesticAddress = getProperty(COLUMN_LAST_DOMESTIC_ADDRESS);
		String agentSsn = getProperty(COLUMN_AGENT_SSN);
		String sNew = getProperty(COLUMN_NEW);
		String addressName = getProperty(COLUMN_ADDRESS_NAME);
		String dateOfDeletion = getProperty(COLUMN_DATE_OF_DELETION);
		String newSsnOrName = getProperty(COLUMN_NEW_SSN_OR_NAME);
		String dateOfBirth = getProperty(COLUMN_DATE_OF_BIRTH);
		
		//		System.out.println("ssn = " + ssn);

		if (ssn == null || ssn.equals(""))
			return false;

		if (relationOnly && familyId != null) {
			_familyRelations.put(familyId, ssn);
			return true;
		}
		
		//			//initialize business beans and data homes           
		NationalRegisterBusiness natReg = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);

		if (postalCodeFix) {
			UserBusiness uBiz = (UserBusiness) getServiceInstance(UserBusiness.class);
			try {
				natReg.updateUserAddress(uBiz.getUser(ssn), uBiz, address, po);
				return true;
			} catch (Exception e){
				return false;
			}
		}
		
		
		boolean success = false;
		if (createOnly) {
			NationalRegister reg = natReg.getEntryBySSN(ssn);
			if (reg == null) {
				success = natReg.updateEntry(symbol,oldId,ssn,familyId,name,commune,street,building,
            floor,sex,maritialStatus,empty,prohibitMarking,
            nationality,placeOfBirth,spouseSSN,fate,parish,po,address,
						addressCode, dateOfModification, placementCode, dateOfCreation, lastDomesticAddress,
						agentSsn, sNew, addressName, dateOfDeletion, newSsnOrName, dateOfBirth);
			} else {
				success = true;
			}
		} else {
			success = natReg.updateEntry(symbol,oldId,ssn,familyId,name,commune,street,building,
          floor,sex,maritialStatus,empty,prohibitMarking,
          nationality,placeOfBirth,spouseSSN,fate,parish,po,address,
					addressCode, dateOfModification, placementCode, dateOfCreation, lastDomesticAddress,
					agentSsn, sNew, addressName, dateOfDeletion, newSsnOrName, dateOfBirth);
		}
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