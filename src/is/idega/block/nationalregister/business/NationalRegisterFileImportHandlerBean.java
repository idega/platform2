package is.idega.block.nationalregister.business;

import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.MemberFamilyLogicBean;
import is.idega.idegaweb.member.business.NoChildrenFound;
import is.idega.idegaweb.member.business.NoCustodianFound;
import is.idega.idegaweb.member.business.NoParentFound;
import is.idega.idegaweb.member.business.NoSiblingFound;
import is.idega.idegaweb.member.business.NoSpouseFound;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.block.importer.presentation.Importer;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.util.Converter;
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
	
	private final static String PROPERTY_NAME_RELATION_ONLY = "NAT_REG_RELATION_ONLY";
	private final static String PROPERTY_NAME_POSTAL_CODE_FIX = "NAT_REG_POSTAL_CODE_FIX";
	private final static String PROPERTY_NAME_GROUP_ID_FIX = "NAT_REG_GROUP_ID_FIX";
	private final static String FATE_DECEASED = "LÉST";
	private final static String FATE_CHANGE_PERSONAL_ID = "BRFD";
	private final static String FATE_REMOVED = "BRFL";
	private final static String FATE_CHANGE_OLD_ID = "BRNN";
	private boolean relationOnly = false;
	private boolean postalCodeFix = false;
	private Group citizenGroup = null;
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
			String sRelationOnly = bundle.getProperty(PROPERTY_NAME_RELATION_ONLY);
			String sPostal = bundle.getProperty(PROPERTY_NAME_POSTAL_CODE_FIX);
			String sGroupID = bundle.getProperty(PROPERTY_NAME_GROUP_ID_FIX);
			
			relationOnly = (sRelationOnly != null && sRelationOnly.equalsIgnoreCase("yes"));
			postalCodeFix = (sPostal != null && sPostal.equalsIgnoreCase("yes"));
			if (sGroupID != null) {
				try {
					citizenGroup = ((GroupHome) IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(sGroupID));
				} catch (Exception e) {
					System.out.println("NationalRegisterHandler citizenGroup is NULL ("+e.getMessage()+")");
				}
			} else {
				System.out.println("NationalRegisterHandler citizenGroup is NULL");
			}
			
			int count = 0;
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
			_file.close();
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

	/**
	 * After all lines in the import file has been imported, the relations are handled. 
	 * When the records are processed, the relations are stored in the ArrayList _familyRelations
	 * 
	 * @return
	 * @throws RemoteException
	 */
	private boolean handleFamilyRelation() throws RemoteException {
		if (_familyRelations != null) {
			Set keys = _familyRelations.keySet();
			UserHome userHome = null; 
			NationalRegisterBusiness natReg = null;
			FamilyLogic familyLogic = null;
			try {
				familyLogic = (FamilyLogic) getServiceInstance(FamilyLogic.class);
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
				//Loop through all households/families
				while (keysIter.hasNext()) {
					++counter;
					key = (String) keysIter.next();
					familyColl = new Vector();
					ssnColl = _familyRelations.getCollection(key);
					if (ssnColl != null) {
						ssnIter = ssnColl.iterator();
						//Loop through all members of the familie
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
	
	/**
	 * 
	 * @param natRegBus
	 * @param familyLogic
	 * @param uHome
	 * @param coll
	 * @return
	 * @throws RemoveException
	 * @throws RemoveException
	 * @throws RemoteException
	 */
	private boolean handleFamilyCollection(NationalRegisterBusiness natRegBus, FamilyLogic familyLogic, UserHome uHome, Collection coll) throws RemoteException, RemoveException {
		if (coll != null) {
			MemberFamilyLogicBean memFamLog = (MemberFamilyLogicBean) getServiceInstance(MemberFamilyLogicBean.class);

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
			
			//Loop through all family members to figure out what the relations are
			while (iter.hasNext()) {
				user = (User) iter.next();
				if (user.getDateOfBirth() != null) {
					age = new Age(user.getDateOfBirth());
					if (age.getYears() > oldestAge) {
						oldestAge = age.getYears();
						oldestPerson = user;
					}
				}
				//If person has a spouse, it is also registered as possible parent
				natReg = natRegBus.getEntryBySSN(user.getPersonalID());
				spouseSSN = natReg.getSpouseSSN().trim();
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
			
			//This iteration sets the spouse, parent, custodian, child and sibling relations.
			//The relations variables hold the relations that yet not have been found in the import file
			//If there are any relations left in these variables after the new relations have been set, 
			//They have to be removed
			Relations rel1 = new Relations();
			Relations rel2 = new Relations();
			
			iter = coll.iterator();
			while (iter.hasNext()) {
				user = (User) iter.next();
				rel1.setForUser(user);
				
				//Debug info
				rel1.dumpInfo();
				coll2.remove(user);
				iter2 = coll2.iterator();
				while (iter2.hasNext()) {
					user2 = (User) iter2.next();
					rel2.setForUser(user2);
					try {
						if (parents.contains(user)) {
							if (parents.contains(user2)) {
								setAsSpouseFor(familyLogic, user, user2, rel1, rel2);	//Both are parents
							} else {
								setAsParentFor(familyLogic, user, user2, rel1, rel2);	//User is parent user2 is child
								setAsCustodianFor(familyLogic, user, user2, rel1, rel2);
							}
						} else {
							if (parents.contains(user2)) {
								setAsChildFor(familyLogic, user, user2, rel1, rel2);		//user is child and user2 is parent
								setAsCustodianFor(familyLogic, user2, user, rel1, rel2);
							} else {
								setAsSiblingFor(familyLogic, user, user2, rel1, rel2);	//Both are children
							}
						}
					} catch (CreateException e) {
						e.printStackTrace();
					}
					removeTerminatedRelations(memFamLog, user2, rel2);
				}
				removeTerminatedRelations(memFamLog, user, rel1);
				//Debug info
				rel1.dumpInfo();
			}
		}
		
		return false;
	}
	
	/**
	 * Removes the old relations that previousley were set but now aren't in the import file
	 * and therefore should be removed
	 * 
	 * @param user
	 * @param rel
	 * @throws RemoveException
	 * @throws RemoteException
	 */
	private void removeTerminatedRelations(MemberFamilyLogicBean memFamLog, User user, Relations rel) throws RemoteException, RemoveException {
		//remove spouse
		System.out.println("Relations to be removed:");
		rel.dumpInfo();
		if(null!=rel.spouse){
			memFamLog.removeAsSpouseFor(user, rel.spouse);
		}
		//Remove from collections
		Iterator iter = rel.child.iterator();
		while(iter.hasNext()){
			User child = (User)iter.next();
			memFamLog.removeAsChildFor(child, user);
		}
		
		iter = rel.isCustodianFor.iterator();
		while(iter.hasNext()){
			User child = (User)iter.next();
			memFamLog.removeAsCustodianFor(user, child);
		}
		
		iter = rel.hasCustodian.iterator();
		while(iter.hasNext()){
			User custodian = (User)iter.next();
			memFamLog.removeAsCustodianFor(custodian,user);
		}
		
		iter = rel.parent.iterator();
		while(iter.hasNext()){
			User parent = (User)iter.next();
			memFamLog.removeAsParentFor(parent,user);
		}
		
		iter = rel.sibling.iterator();
		while(iter.hasNext()){
			User sibling = (User)iter.next();
			memFamLog.removeAsSiblingFor(user , sibling);
		}
		
	}

	private void setAsSpouseFor(FamilyLogic familyLogic, User user1, User user2, Relations rel1, Relations rel2)
			throws RemoteException, CreateException {
		//See if this person already is married
		if (null != rel1.spouse) {
			//Check if is status quo
			if (rel1.spouse.equals(user2)) {
				rel1.spouse = null;
				rel2.spouse = null;
				return;
			}
		}
		System.out.println("new spouse relation: "+user1.getName()+" ; "+user2.getName());
		familyLogic.setAsSpouseFor(user1, user2); //Both are parents
		
	}
	
	private void setAsParentFor(FamilyLogic familyLogic, User user1, User user2, Relations rel1, Relations rel2)
			throws RemoteException, CreateException {
		if (rel1.child.contains(user2)) {
			rel1.child.remove(user2);
			rel2.parent.remove(user1);
			return;
		}
		System.out.println("new parent relation parent: "+user1.getName()+" ; Child :"+user2.getName());
		familyLogic.setAsParentFor(user1, user2); //User1 is parent; user2 is child
	}
	
	private void setAsCustodianFor(FamilyLogic familyLogic, User user1, User user2, Relations rel1, Relations rel2) throws RemoteException, CreateException{
		if(rel1.isCustodianFor.contains(user2)){
			rel1.isCustodianFor.remove(user2);
			rel2.hasCustodian.remove(user1);
			return;
		}
		System.out.println("new custodian relation custodian: "+user1.getName()+" ; Child :"+user2.getName());
		familyLogic.setAsCustodianFor(user1, user2);
	}
	
	private void setAsSiblingFor(FamilyLogic familyLogic, User user1, User user2, Relations rel1, Relations rel2) throws RemoteException, CreateException{
		if(rel1.sibling.contains(user2)){
			rel1.sibling.remove(user2);
			rel2.sibling.remove(user1);
			return;
		}
		System.out.println("new sibling relation: "+user1.getName()+" ; "+user2.getName());
		familyLogic.setAsSiblingFor(user1, user2);	//Both are children
	}
	
	private void setAsChildFor(FamilyLogic familyLogic, User user1, User user2, Relations rel1, Relations rel2) throws RemoteException, CreateException{
		if(rel1.parent.contains(user2)){
			rel1.parent.remove(user2);
			rel2.child.remove(user1);
			return;
		}
		System.out.println("new child relation child: "+user1.getName()+" ; Parent :"+user2.getName());
		familyLogic.setAsChildFor(user1, user2);
	}
	
	private class Relations{
		User user = null;
		User spouse = null;
		Collection child = new ArrayList();
		Collection parent = new ArrayList();
		Collection isCustodianFor = new ArrayList();
		Collection hasCustodian = new ArrayList();
		Collection sibling = new ArrayList();
		
		public void dumpInfo(){
			System.out.println("Relations to user "+user.getName());
			if(null!=spouse){
				System.out.println("spouse: "+spouse);
			}
			Iterator iter = child.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("child: "+user.getName());
			}
			iter = parent.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("parent: "+user.getName());
			}
			iter = isCustodianFor.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("is custodian for: "+user.getName());
			}
			iter = hasCustodian.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("has custodian: "+user.getName());
			}
			iter = sibling.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("sibling: "+user.getName());
			}
		}
		
		public void setForUser(User user) throws IBOLookupException, RemoteException{
			MemberFamilyLogicBean memFamLog = (MemberFamilyLogicBean) getServiceInstance(MemberFamilyLogicBean.class);
			this.user = user;
			try {
				spouse = memFamLog.getSpouseFor(user);
			}
			catch (NoSpouseFound e) {
				spouse = null;
			}

			try {
				child = memFamLog.getChildrenFor(user);
			}
			catch (NoChildrenFound e1) {
			}

			try {
				parent = memFamLog.getParentsFor(user);
			}
			catch (NoParentFound e) {
			}
			
			try {
				isCustodianFor = memFamLog.getChildrenInCustodyOf(user);
			}
			catch (NoChildrenFound e) {
			}
			
			try {
				hasCustodian = memFamLog.getCustodiansFor(user);
			}
			catch (NoCustodianFound e) {
			}

			try {
				sibling = memFamLog.getSiblingsFor(user);
			}
			catch (NoSiblingFound e) {
			}
		}
		
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


		//initialize business beans and data homes           
		NationalRegisterBusiness natReg = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
		UserBusiness uBiz = (UserBusiness) getServiceInstance(UserBusiness.class);

		if(FATE_DECEASED.equalsIgnoreCase(fate)){
			User user;
			try {
				user = uBiz.getUser(ssn);
			}
			catch (FinderException e) {
				e.printStackTrace();
				return false;
			}
			MemberFamilyLogic familyService = getMemberFamilyLogic();
			IWTimestamp dom = new IWTimestamp();
			if (dateOfModification != null) {
				dom = new IWTimestamp(dateOfModification);
			}
			else {
				dom = IWTimestamp.RightNow();
			}
			familyService.registerAsDeceased(user, dom.getDate());
		}
		
		if(FATE_CHANGE_PERSONAL_ID.equalsIgnoreCase(fate)){
			natReg.updateUserPersonalID(ssn,newSsnOrName);
			return true;
		}
		
		if (FATE_REMOVED.equalsIgnoreCase(fate)) {
			try {
				User user = uBiz.getUser(ssn);
				User performer = Converter.convertToNewUser(
						getIWApplicationContext().getIWMainApplication().getAccessController().getAdministratorUser());
				uBiz.deleteUser(user,performer);
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		if(FATE_CHANGE_OLD_ID.equalsIgnoreCase(fate)){
			natReg.updateUserOldID(oldId,ssn);
			return true;
		}
		
		if (postalCodeFix || relationOnly) {
			try {
//				User user = uBiz.getUser(ssn);
				if (postalCodeFix) {
					natReg.updateUserAddress(uBiz.getUser(ssn), uBiz, address, po);
				}
				//TODO (JJ) probably have to be able to null this one after all
				if (relationOnly && familyId != null && !"".equals(familyId.trim())) {
					_familyRelations.put(familyId, ssn);
				}
				return true;
			} catch (Exception e){
				return false;
			}
		}
		
		boolean success = natReg.updateEntry(symbol,oldId,ssn,familyId,name,commune,street,building,
		    floor,sex,maritialStatus,empty,prohibitMarking,
		    nationality,placeOfBirth,spouseSSN,fate,parish,po,address,
				addressCode, dateOfModification, placementCode, dateOfCreation, lastDomesticAddress,
				agentSsn, sNew, addressName, dateOfDeletion, newSsnOrName, dateOfBirth, citizenGroup);
		
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

	public MemberFamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (MemberFamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), MemberFamilyLogic.class);
	}

}