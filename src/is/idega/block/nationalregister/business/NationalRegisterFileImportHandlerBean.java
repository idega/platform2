package is.idega.block.nationalregister.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.FamilyLogicBean;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.business.NoParentFound;
import is.idega.block.family.business.NoSiblingFound;
import is.idega.block.family.business.NoSpouseFound;
import is.idega.block.family.data.FamilyMember;
import is.idega.block.family.data.FamilyMemberHome;
import is.idega.block.nationalregister.data.NationalRegister;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.block.importer.presentation.Importer;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
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

	private Collection affectedFamilies = new HashSet();
	
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
			IWBundle bundle = getIWMainApplication().getBundle(Importer.IW_BUNDLE_IDENTIFIER);
			String sRelationOnly = bundle.getProperty(PROPERTY_NAME_RELATION_ONLY);
			String sPostal = bundle.getProperty(PROPERTY_NAME_POSTAL_CODE_FIX);
			String sGroupID = bundle.getProperty(PROPERTY_NAME_GROUP_ID_FIX);
			
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
		
		if (affectedFamilies != null && userHome != null && natReg != null) {
			Iterator keysIter = affectedFamilies.iterator();
			Iterator ssnIter;
			String key;
			String ssn;
			int counter = 0;
			Collection ssnColl;
			Collection familyColl;
			System.out.println("NationalRegisterHandler Total families to handle = "+affectedFamilies.size());
			System.out.println("NationalRegisterHandler processing family relations RECORD [0] time: " + IWTimestamp.getTimestampRightNow().toString());
			//Loop through all households/families
			while (keysIter.hasNext()) {
				++counter;
				key = (String) keysIter.next();

				try {
					familyColl = getFamilyMemberHome().findAllByFamilyNR(key);
					handleFamilyCollection(natReg, familyLogic, userHome, familyColl);
				} catch (Exception e) {
					System.out.println("NationalRegisterHandler ERROR, familyRelation failed for family : "+key);
					e.printStackTrace();
				}
				if ((counter % 100) == 0) {
					System.out.println("NationalRegisterHandler processing family relations RECORD [" + counter + "] time: " + IWTimestamp.getTimestampRightNow().toString());
				}
			}
			System.out.println("NationalRegisterHandler processed family relations RECORD [" + counter + "] time: " + IWTimestamp.getTimestampRightNow().toString());
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
			FamilyLogicBean memFamLog = (FamilyLogicBean) getServiceInstance(FamilyLogicBean.class);
			
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
			FamilyMember member;
			
			//Loop through all family members to figure out what the relations are
			while (iter.hasNext()) {
				member = (FamilyMember) iter.next();
				user = member.getUser();
				if (user == null) {
					System.out.println(" user == null : "+member.getPrimaryKey());
				}
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
			Relations oldRelations1 = new Relations();
			Relations newRelations1 = new Relations();
			
			Relations oldRelations2 = new Relations();
			Relations newRelations2 = new Relations();
			
			HashMap oldrelations = new HashMap();
			HashMap newrelations = new HashMap();

			iter = coll.iterator();
			FamilyMember member2;
			while (iter.hasNext()) {
				member = (FamilyMember) iter.next();
				user = member.getUser();
				if (oldrelations.get(user) == null) {
					oldRelations1 = new Relations();
					oldRelations1.setForUser(user);
					oldrelations.put(user, oldRelations1);
				}
				if (newrelations.get(user) == null) {
					newRelations1 = new Relations();
					newRelations1.setUser(user);
					newrelations.put(user, newRelations1);
				} else {
					newRelations1 = ((Relations) newrelations.get(user));
				}
				
				coll2.remove(member);
				iter2 = coll2.iterator();
				while (iter2.hasNext()) {
					member2 = (FamilyMember) iter2.next();
					user2 = member2.getUser();
					if ( oldrelations.get(user2) == null) {
						oldRelations2 = new Relations();
						oldRelations2.setForUser(user2);
						oldrelations.put(user2, oldRelations2);
					} 
					if ( newrelations.get(user2) == null) {
						newRelations2 = new Relations();
						newRelations2.setUser(user2);
						newrelations.put(user2, newRelations2);
					} else {
						newRelations2 = ((Relations) newrelations.get(user2));
					}
					
					if (parents.contains(user)) {
						if (parents.contains(user2)) {
							newRelations1.setSpouse(user2);
							newRelations2.setSpouse(user);
						} else {
							newRelations1.addChild(user2);
							newRelations1.addIsCustodianFor(user2);
							newRelations2.addParent(user);
							newRelations2.addHasCustodian(user);
						}
					} else {
						if (parents.contains(user2)) {
							newRelations1.addParent(user2);
							newRelations1.addHasCustodian(user2);
							newRelations2.addChild(user);
							newRelations2.addIsCustodianFor(user);
						} else {
							newRelations1.addSibling(user2);
							newRelations2.addSibling(user);
						}
					}
				}
			}

			Set set = newrelations.keySet();
			Iterator newSetIt = set.iterator();
			while (newSetIt.hasNext()) {
				User tmpuser = (User) newSetIt.next();

				Relations newR = (Relations) newrelations.get(tmpuser);
				Relations oldR = (Relations) oldrelations.get(tmpuser);

				try {
					Relations newToAdd = newR.inANotB(newR, oldR);
					addNewRelations(memFamLog, tmpuser, newToAdd);
				}
				catch (CreateException e) {
					e.printStackTrace();
				}

				Relations oldToRemove = newR.inANotB(oldR, newR);
				removeTerminatedRelations(memFamLog, tmpuser, oldToRemove);

			}
			return true;
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
	private void removeTerminatedRelations(FamilyLogicBean memFamLog, User user, Relations rel) throws RemoteException, RemoveException {
//		NationalRegisterBusiness natRegBus;
		//remove spouse
		//rel.dumpInfo();
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

	private void addNewRelations(FamilyLogicBean memFamLog, User user, Relations rel) throws RemoveException, RemoteException, CreateException {
	//	NationalRegisterBusiness natRegBus;
		//remove spouse
		//rel.dumpInfo();
		if(null!=rel.spouse){
			memFamLog.setAsSpouseFor(user, rel.spouse);
		}
		//Remove from collections
		Iterator iter = rel.child.iterator();
		while(iter.hasNext()){
			User child = (User)iter.next();
			memFamLog.setAsChildFor(child, user);
		}
		
		iter = rel.isCustodianFor.iterator();
		while(iter.hasNext()){
			User child = (User)iter.next();
			memFamLog.setAsCustodianFor(user, child);
		}
		
		iter = rel.hasCustodian.iterator();
		while(iter.hasNext()){
			User custodian = (User)iter.next();
			memFamLog.setAsCustodianFor(custodian,user);
		}
		
		iter = rel.parent.iterator();
		while(iter.hasNext()){
			User parent = (User)iter.next();
			memFamLog.setAsParentFor(parent,user);
		}
		
		iter = rel.sibling.iterator();
		while(iter.hasNext()){
			User sibling = (User)iter.next();
			memFamLog.setAsSiblingFor(user , sibling);
		}
	}	
	
	private class Relations{
		private User user = null;
		private User spouse = null;
		private Collection child = new ArrayList();
		private Collection parent = new ArrayList();
		private Collection isCustodianFor = new ArrayList();
		private Collection hasCustodian = new ArrayList();
		private Collection sibling = new ArrayList();
		
		public void setUser(User user) {
			this.user = user;
		}
		
		public void setSpouse(User spouse) {
			this.spouse = spouse;
		}
		
		public void addChild(User child) {
			if (!this.child.contains(child)) {
				this.child.add(child);
			}
		}
		
		public void addParent(User parent) {
			if (!this.parent.contains(parent)) {
				this.parent.add(parent);
			}
		}
		
		public void addIsCustodianFor(User user) {
			if (!this.isCustodianFor.contains(user)) {
				this.isCustodianFor.add(user);
			}
		}
		
		public void addHasCustodian(User user) {
			if (!this.hasCustodian.contains(user)) {
				this.hasCustodian.add(user);
			}
		}	
		
		public void addSibling(User user) {
			if (!this.sibling.contains(user)) {
				this.sibling.add(user);
			}
		}		

		public void dumpInfo(){
			System.out.println("Relations for user "+user.getName()+" - "+user.getPersonalID());
			if(null!=spouse){
				System.out.println("spouse: "+spouse+" - "+spouse.getPersonalID());
			}
			Iterator iter = child.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("child: "+user.getName()+" - "+user.getPersonalID());
			}
			iter = parent.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("parent: "+user.getName()+" - "+user.getPersonalID());
			}
			iter = isCustodianFor.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("is custodian for: "+user.getName()+" - "+user.getPersonalID());
			}
			iter = hasCustodian.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("has custodian: "+user.getName()+" - "+user.getPersonalID());
			}
			iter = sibling.iterator();
			while(iter.hasNext()){
				User user = (User)iter.next();
				System.out.println("sibling: "+user.getName()+" - "+user.getPersonalID());
			}
		}
		
		public void setForUser(User user) throws IBOLookupException, RemoteException{
			FamilyLogicBean memFamLog = (FamilyLogicBean) getServiceInstance(FamilyLogicBean.class);
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
		
		public Relations inANotB(Relations a, Relations b) throws IllegalArgumentException {
			Relations c = new Relations();
			if (a.user != b.user) {
				throw new IllegalArgumentException("Relations must contain same users");
			} else {
				c.user = a.user;
			}
			if (a.spouse == b.spouse) {
				if (a.spouse == null) {
//					System.out.println("     - removing spouse for "+c.user.getName() +" - "+c.user.getPersonalID()+ "a="+a.spouse+", b="+b.spouse);
				}
				c.spouse = null;
			}
			c.child = new ArrayList(a.child);
			c.hasCustodian = new ArrayList(a.hasCustodian);
			c.isCustodianFor = new ArrayList(a.isCustodianFor);
			c.parent = new ArrayList(a.parent);
			c.sibling = new ArrayList(a.sibling);
			
			Iterator iter = b.child.iterator();
			while (iter.hasNext()) {
				c.child.remove((User) iter.next());
			}
			iter = b.hasCustodian.iterator();
			while (iter.hasNext()) {
				c.hasCustodian.remove((User) iter.next());
			}
			iter = b.isCustodianFor.iterator();
			while (iter.hasNext()) {
				c.isCustodianFor.remove((User) iter.next());
			}
			iter = b.parent.iterator();
			while (iter.hasNext()) {
				c.parent.remove((User) iter.next());
			}
			iter = b.sibling.iterator();
			while (iter.hasNext()) {
				c.sibling.remove((User) iter.next());
			}
			
			return c;
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
			FamilyLogic familyService = getMemberFamilyLogic();
			IWTimestamp dom = new IWTimestamp();
			if (dateOfModification != null && !"".equals(dateOfModification.trim())) {
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
		
		if (postalCodeFix) {
			try {
//				User user = uBiz.getUser(ssn);
				if (postalCodeFix) {
					natReg.updateUserAddress(uBiz.getUser(ssn), uBiz, address, po);
				}
				return true;
			} catch (Exception e){
				return false;
			}
		}

		// Family is marked as affected, and needs to be updated
		affectedFamilies.add(familyId);
		try {
			// Users previous family is marked as affected, and needs to be updated
			affectedFamilies.add(getFamilyMemberHome().findBySSN(ssn).getFamilyNr());
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			//FinderExxception is ignored, since not all users have a family
		}
		
		boolean success = natReg.updateEntry(symbol,oldId,ssn,familyId,name,commune,street,building,
		    floor,sex,maritialStatus,empty,prohibitMarking,
		    nationality,placeOfBirth,spouseSSN,fate,parish,po,address,
				addressCode, dateOfModification, placementCode, dateOfCreation, lastDomesticAddress,
				agentSsn, sNew, addressName, dateOfDeletion, newSsnOrName, dateOfBirth, citizenGroup);
	
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

	public FamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (FamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), FamilyLogic.class);
	}

  protected FamilyMemberHome getFamilyMemberHome(){
    try{
      return (FamilyMemberHome)this.getIDOHome(FamilyMember.class);
    }
    catch(RemoteException e){
      throw new EJBException(e.getMessage());
    }
  }	
  
  // TODO add fix for specific groupIDs for certain people
  
}