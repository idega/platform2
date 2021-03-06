package is.idega.block.nationalregister.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.FamilyLogicBean;
import is.idega.block.family.data.FamilyMember;
import is.idega.block.family.data.FamilyMemberHome;
import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.block.nationalregister.data.NationalRegisterFate;
import is.idega.block.nationalregister.data.NationalRegisterFateHome;
import java.rmi.RemoteException;
import java.text.NumberFormat;
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
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.Timer;

/**
 * @author palli
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class NationalRegisterFileImportHandlerBean extends IBOServiceBean implements NationalRegisterFileImportHandler,
		ImportFileHandler {

	private ImportFile file;

	private ArrayList failedRecordList = new ArrayList();

	private ArrayList valueList;

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

	private final static String PROPERTY_NAME_GROUP_FIX = "NAT_REG_GROUP_ID_FIX";

	/*
	 * private final static String FATE_DECEASED = "L�ST"; private final static
	 * String FATE_CHANGE_PERSONAL_ID = "BRFD"; private final static String
	 * FATE_REMOVED = "BRFL"; //private final static String FATE_CHANGE_OLD_ID =
	 * "BRNN";
	 */
	private static String FATE_DECEASED = null;

	private static String FATE_CHANGE_PERSONAL_ID = null;

	private static String FATE_REMOVED = null;

	private boolean postalCodeFix = false;

	private boolean relationsOnly = false;

	private boolean citizenGroupFix = false;

	private User performer = null;

	private FamilyLogic famLog = null;

	private final static int BYTES_PER_RECORD = 301;

	private NumberFormat twoDigits = NumberFormat.getNumberInstance();

	private NumberFormat precentNF = NumberFormat.getPercentInstance();

	private HashMap postalToGroupMap = new HashMap();

	private NationalRegisterBusiness natBiz;

	private UserBusiness uBiz;

	private CommuneBusiness cBiz;

	private String deceasedAddressString = null;

	public final static String IW_BUNDLE_IDENTIFIER = "is.idega.block.nationalregister";

	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#handleRecords()
	 */
	public boolean handleRecords() throws RemoteException {
		// UserTransaction transaction =
		// getSessionContext().getUserTransaction();
		Timer clock = new Timer();
		clock.start();
		try {
			this.natBiz = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
			this.uBiz = (UserBusiness) getServiceInstance(UserBusiness.class);
			this.cBiz = (CommuneBusiness) getServiceInstance(CommuneBusiness.class);
			if (FATE_DECEASED == null || FATE_CHANGE_PERSONAL_ID == null || FATE_REMOVED == null) {
				NationalRegisterFate fate = ((NationalRegisterFateHome) IDOLookup.getHome(NationalRegisterFate.class)).findByFateCode(NationalRegisterConstants.FATE_DECEASED);
				if (fate == null || fate.getFateString() == null || "".equals(fate.getFateString())) {
					System.out.println("Missing DECEASED fate string in table reg_nat_is_fate");
					return false;
				}
				FATE_DECEASED = fate.getFateString();
				fate = ((NationalRegisterFateHome) IDOLookup.getHome(NationalRegisterFate.class)).findByFateCode(NationalRegisterConstants.FATE_CHANGE_PERSONAL_ID);
				if (fate == null || fate.getFateString() == null || "".equals(fate.getFateString())) {
					System.out.println("Missing CHANGE PERSONAL ID fate string in table reg_nat_is_fate");
					return false;
				}
				FATE_CHANGE_PERSONAL_ID = fate.getFateString();
				fate = ((NationalRegisterFateHome) IDOLookup.getHome(NationalRegisterFate.class)).findByFateCode(NationalRegisterConstants.FATE_REMOVED);
				if (fate == null || fate.getFateString() == null || "".equals(fate.getFateString())) {
					System.out.println("Missing REMOVED fate string in table reg_nat_is_fate");
					return false;
				}
				FATE_REMOVED = fate.getFateString();
			}
			try {
				this.performer = IWContext.getInstance().getCurrentUser();
			}
			catch (NullPointerException n) {
				System.out.println("NationalRegisterImporter iwcontext instance not found");
				this.performer = null;
			}
			if (this.performer == null) {
				com.idega.core.user.data.User admUser = this.getIWMainApplication().getAccessController().getAdministratorUser();
				this.performer = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(admUser.getPrimaryKey());
			}
			// iterate through the records and process them
			String item;
			IWBundle bundle = getIWMainApplication().getBundle(Importer.IW_BUNDLE_IDENTIFIER);
			String sRelationOnly = bundle.getProperty(PROPERTY_NAME_RELATION_ONLY);
			String sPostal = bundle.getProperty(PROPERTY_NAME_POSTAL_CODE_FIX);
			String sGroup = bundle.getProperty(PROPERTY_NAME_GROUP_FIX);
			this.affectedFamilies = new HashSet();
			this.postalCodeFix = (sPostal != null && sPostal.equalsIgnoreCase("yes"));
			this.relationsOnly = (sRelationOnly != null && sRelationOnly.equalsIgnoreCase("yes"));
			this.citizenGroupFix = (sGroup != null && sGroup.equalsIgnoreCase("yes"));
			int count = 0;
			if (this.postalCodeFix) {
				System.out.println("NationalRegisterHandler postalCodeFix variable set to TRUE");
			}
			if (this.relationsOnly) {
				System.out.println("NationalRegisterHandler relationsOnly variable set to TRUE");
			}
			if (this.citizenGroupFix) {
				System.out.println("NationalRegisterHandler citizenGroupFix variable set to TRUE");
			}
			long totalBytes = this.file.getFile().length();
			long totalRecords = totalBytes / BYTES_PER_RECORD;
			this.twoDigits.setMinimumIntegerDigits(2);
			long beginTime = System.currentTimeMillis();
			long lastTimeCheck = beginTime;
			long averageTimePerUser100 = 0;
			long timeLeft100 = 0;
			long estimatedTimeFinished100 = beginTime;
			IWTimestamp stamp;
			double progress = 0;
			int intervalBetweenOutput = 100;
			System.out.println("NatRegImport processing RECORD [0] time: "
					+ IWTimestamp.getTimestampRightNow().toString());
			while (!(item = (String) this.file.getNextRecord()).equals("")) {
				count++;
				if (!processRecord(item)) {
					this.failedRecordList.add(item);
				}
				if ((count % intervalBetweenOutput) == 0) {
					averageTimePerUser100 = (System.currentTimeMillis() - lastTimeCheck) / intervalBetweenOutput;
					lastTimeCheck = System.currentTimeMillis();
					timeLeft100 = averageTimePerUser100 * (totalRecords - count);
					estimatedTimeFinished100 = System.currentTimeMillis() + timeLeft100;
					progress = ((double) count) / ((double) totalRecords);
					System.out.print("NatRegImport " + IWTimestamp.getTimestampRightNow().toString()
							+ ", processing RECORD [" + count + " / " + totalRecords + "]");
					stamp = new IWTimestamp(estimatedTimeFinished100);
					System.out.println(" | " + this.precentNF.format(progress)
							+ " done, guestimated time left of PHASE 1 : " + getTimeString(timeLeft100)
							+ "  finish at " + stamp.getTime().toString());
				}
				item = null;
			}
			this.file.close();
			System.out.println("NatRegImport processed RECORD [" + count + "] time: "
					+ IWTimestamp.getTimestampRightNow().toString());
			clock.stop();
			long msTime = clock.getTime();
			long secTime = msTime / 1000;
			System.out.println("Time to handleRecords: " + msTime + " ms  OR " + secTime + " s, averaging "
					+ (msTime / count) + "ms per record");
			clock.start();
			handleFamilyRelation();
			clock.stop();
			msTime = clock.getTime();
			secTime = msTime / 1000;
			System.out.println("Time to handleFamilyRelation: " + clock.getTime() + " ms  OR "
					+ ((int) (clock.getTime() / 1000)) + " s, averaging " + (msTime / count) + "ms per record");
			printFailedRecords();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public String getTimeString(long time) {
		long t = time;
		int milli = (int) (t % 1000);
		t = (t - milli) / 1000;
		int second = (int) (t % 60);
		t = (t - second) / 60;
		int minut = (int) (t) % 60;
		int hour = (int) ((t - minut) / 60);
		return this.twoDigits.format(hour) + ":" + this.twoDigits.format(minut) + ":" + this.twoDigits.format(second)
				+ "." + milli;
	}

	/**
	 * After all lines in the import file has been imported, the relations are
	 * handled. When the records are processed, the relations are stored in the
	 * ArrayList _familyRelations
	 * 
	 * @return
	 * @throws RemoteException
	 */
	private boolean handleFamilyRelation() throws RemoteException {
		UserHome userHome = null;
		NationalRegisterBusiness natReg = null;
		try {
			natReg = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
			UserBusiness userBusiness = (UserBusiness) getServiceInstance(UserBusiness.class);
			userHome = userBusiness.getUserHome();
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		if (this.affectedFamilies != null && userHome != null && natReg != null) {
			long totalRecords = this.affectedFamilies.size();
			long beginTime = System.currentTimeMillis();
			// long lastTimeCheck = beginTime;
			long averageTimePerUser = 0;
			// long averageTimePerUser100 = 0;
			long timeLeft = 0;
			// long timeLeft100 = 0;
			long estimatedTimeFinished = beginTime;
			// long estimatedTimeFinished100 = beginTime;
			IWTimestamp stamp;
			double progress = 0;
			int intervalBetweenOutput = 100;
			Iterator keysIter = this.affectedFamilies.iterator();
			String key;
			int counter = 0;
			Collection familyColl;
			System.out.println("NatRegImport Total families to handle = " + totalRecords);
			System.out.println("NatRegImport processing family relations RECORD [0] time: "
					+ IWTimestamp.getTimestampRightNow().toString());
			// Loop through all households/families
			while (keysIter.hasNext()) {
				++counter;
				key = (String) keysIter.next();
				try {
					familyColl = getFamilyMemberHome().findAllByFamilyNR(key);
					handleFamilyCollection(natReg, userHome, familyColl);
				}
				catch (Exception e) {
					System.out.println("NatRegImport ERROR, familyRelation failed for family : " + key);
					e.printStackTrace();
				}
				if ((counter % intervalBetweenOutput) == 0) {
					/*
					 * averageTimePerUser100 = (System.currentTimeMillis() -
					 * lastTimeCheck) / intervalBetweenOutput; lastTimeCheck =
					 * System.currentTimeMillis(); timeLeft100 =
					 * averageTimePerUser100 * (totalRecords - counter);
					 * estimatedTimeFinished100 = System.currentTimeMillis() +
					 * timeLeft100;
					 */
					averageTimePerUser = (System.currentTimeMillis() - beginTime) / counter;
					progress = ((double) counter) / ((double) totalRecords);
					timeLeft = averageTimePerUser * (totalRecords - counter);
					estimatedTimeFinished = timeLeft + System.currentTimeMillis();
					System.out.print("NatRegImport " + IWTimestamp.getTimestampRightNow().toString()
							+ ", processing family RECORD [" + counter + " / " + totalRecords + "]");
					stamp = new IWTimestamp(estimatedTimeFinished);
					System.out.println(" | " + this.precentNF.format(progress)
							+ " done, guestimated time left of PHASE 2 : " + getTimeString(timeLeft) + "  finish at "
							+ stamp.getTime().toString());
					// stamp = new IWTimestamp(estimatedTimeFinished100);
					// System.out.println(" "+precentNF.format(progress)+" done,
					// guestimated time left of PHASE 2 :
					// "+getTimeString(timeLeft100)+" finish at
					// "+stamp.toString());
					// System.out.println("NationalRegisterHandler processing
					// family relations RECORD [" + counter + "] time: " +
					// IWTimestamp.getTimestampRightNow().toString());
				}
			}
			System.out.println("NatRegImport processed family relations RECORD [" + counter + "] time: "
					+ IWTimestamp.getTimestampRightNow().toString());
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
	private boolean handleFamilyCollection(NationalRegisterBusiness natRegBus, UserHome uHome, Collection coll)
			throws RemoteException, RemoveException {
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
			// This iteration sets the spouse, parent, custodian, child and
			// sibling relations.
			// The relations variables hold the relations that yet not have been
			// found in the import file
			// If there are any relations left in these variables after the new
			// relations have been set,
			// They have to be removed
			Relations oldRelations1 = new Relations(getMemberFamilyLogic());
			Relations newRelations1 = new Relations(getMemberFamilyLogic());
			Relations oldRelations2 = new Relations(getMemberFamilyLogic());
			Relations newRelations2 = new Relations(getMemberFamilyLogic());
			HashMap oldrelations = new HashMap();
			HashMap newrelations = new HashMap();
			// Loop through all family members to figure out what the relations
			// are
			while (iter.hasNext()) {
				member = (FamilyMember) iter.next();
				user = member.getUser();
				if (user == null) {
					System.out.println(" user == null : " + member.getPrimaryKey());
				}
				if (user.getDateOfBirth() != null) {
					age = new Age(user.getDateOfBirth());
					if (age.getYears() > oldestAge) {
						oldestAge = age.getYears();
						oldestPerson = user;
					}
				}
				// If person has a spouse, it is also registered as possible
				// parent
				natReg = natRegBus.getEntryBySSN(user.getPersonalID());
				spouseSSN = natReg.getSpouseSSN();
				if (spouseSSN != null && !"".equals(spouseSSN.trim())) {
					parents.add(user);
					try {
						User spouse = uHome.findByPersonalID(spouseSSN);
						newRelations1 = new Relations(getMemberFamilyLogic());
						newRelations1.setUser(user);
						newRelations1.setSpouse(spouse);
						oldRelations1 = new Relations(getMemberFamilyLogic());
						oldRelations1.setForUser(user);
						newrelations.put(user, newRelations1);
						oldrelations.put(user, oldRelations1);
						newRelations2 = new Relations(getMemberFamilyLogic());
						newRelations2.setUser(spouse);
						newRelations2.setSpouse(user);
						oldRelations2 = new Relations(getMemberFamilyLogic());
						oldRelations2.setForUser(spouse);
						newrelations.put(spouse, newRelations2);
						oldrelations.put(spouse, oldRelations2);
						parents.add(spouse);
						break;
					}
					catch (FinderException e) {
						// System.out.println("NationalRegisterHandler processed
						// family relations RECORD [" + counter + "] time: " +
						// IWTimestamp.getTimestampRightNow().toString());
						// e.printStackTrace();
					}
				}
			}
			if (parents.isEmpty() && oldestPerson != null) {
				parents.add(oldestPerson);
			}
			iter = coll.iterator();
			FamilyMember member2;
			while (iter.hasNext()) {
				member = (FamilyMember) iter.next();
				user = member.getUser();
				if (oldrelations.get(user) == null) {
					oldRelations1 = new Relations(getMemberFamilyLogic());
					oldRelations1.setForUser(user);
					oldrelations.put(user, oldRelations1);
				}
				if (newrelations.get(user) == null) {
					newRelations1 = new Relations(getMemberFamilyLogic());
					newRelations1.setUser(user);
					newrelations.put(user, newRelations1);
				}
				else {
					newRelations1 = ((Relations) newrelations.get(user));
				}
				coll2.remove(member);
				iter2 = coll2.iterator();
				while (iter2.hasNext()) {
					member2 = (FamilyMember) iter2.next();
					user2 = member2.getUser();
					if (oldrelations.get(user2) == null) {
						oldRelations2 = new Relations(getMemberFamilyLogic());
						oldRelations2.setForUser(user2);
						oldrelations.put(user2, oldRelations2);
					}
					if (newrelations.get(user2) == null) {
						newRelations2 = new Relations(getMemberFamilyLogic());
						newRelations2.setUser(user2);
						newrelations.put(user2, newRelations2);
					}
					else {
						newRelations2 = ((Relations) newrelations.get(user2));
					}
					if (parents.contains(user)) {
						if (parents.contains(user2)) {
							newRelations1.setSpouse(user2);
							newRelations2.setSpouse(user);
						}
						else {
							newRelations1.addChild(user2);
							newRelations1.addIsCustodianFor(user2);
							newRelations2.addParent(user);
							newRelations2.addHasCustodian(user);
						}
					}
					else {
						if (parents.contains(user2)) {
							newRelations1.addParent(user2);
							newRelations1.addHasCustodian(user2);
							newRelations2.addChild(user);
							newRelations2.addIsCustodianFor(user);
						}
						else {
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
				/*
				 * System.out.println("NEW"); newR.dumpInfo();
				 * System.out.println("OLD"); oldR.dumpInfo();
				 */
				try {
					Relations newToAdd = Relations.inANotB(newR, oldR, getMemberFamilyLogic());
					addNewRelations(memFamLog, tmpuser, newToAdd);
				}
				catch (CreateException e) {
					e.printStackTrace();
				}
				Relations oldToRemove = Relations.inANotB(oldR, newR, getMemberFamilyLogic());
				removeTerminatedRelations(memFamLog, tmpuser, oldToRemove);
			}
			return true;
		}
		return false;
	}

	/**
	 * Removes the old relations that previousley were set but now aren't in the
	 * import file and therefore should be removed
	 * 
	 * @param user
	 * @param rel
	 * @throws RemoveException
	 * @throws RemoteException
	 */
	private void removeTerminatedRelations(FamilyLogicBean memFamLog, User user, Relations rel) throws RemoteException,
			RemoveException {
		// NationalRegisterBusiness natRegBus;
		// remove spouse
		// System.out.println("REMOVING");
		// rel.dumpInfo();
		if (null != rel.getSpouse()) {
			memFamLog.removeAsSpouseFor(user, rel.getSpouse(), this.performer);
		}
		// Remove from collections
		Iterator iter = rel.getChildren().iterator();
		while (iter.hasNext()) {
			User child = (User) iter.next();
			memFamLog.removeAsChildFor(child, user, this.performer);
		}
		iter = rel.getIsCustodianFor().iterator();
		while (iter.hasNext()) {
			User child = (User) iter.next();
			memFamLog.removeAsCustodianFor(user, child, this.performer);
		}
		iter = rel.getHasCustodians().iterator();
		while (iter.hasNext()) {
			User custodian = (User) iter.next();
			memFamLog.removeAsCustodianFor(custodian, user, this.performer);
		}
		iter = rel.getParents().iterator();
		while (iter.hasNext()) {
			User parent = (User) iter.next();
			memFamLog.removeAsParentFor(parent, user, this.performer);
		}
		iter = rel.getSiblings().iterator();
		while (iter.hasNext()) {
			User sibling = (User) iter.next();
			memFamLog.removeAsSiblingFor(user, sibling, this.performer);
		}
	}

	private void addNewRelations(FamilyLogicBean memFamLog, User user, Relations rel) throws RemoveException,
			RemoteException, CreateException {
		// NationalRegisterBusiness natRegBus;
		// remove spouse
		// System.out.println("ADDING");
		// rel.dumpInfo();
		if (null != rel.getSpouse()) {
			memFamLog.setAsSpouseFor(user, rel.getSpouse());
		}
		// Remove from collections
		Iterator iter = rel.getChildren().iterator();
		while (iter.hasNext()) {
			User child = (User) iter.next();
			memFamLog.setAsChildFor(child, user);
		}
		iter = rel.getIsCustodianFor().iterator();
		while (iter.hasNext()) {
			User child = (User) iter.next();
			memFamLog.setAsCustodianFor(user, child);
		}
		iter = rel.getHasCustodians().iterator();
		while (iter.hasNext()) {
			User custodian = (User) iter.next();
			memFamLog.setAsCustodianFor(custodian, user);
		}
		iter = rel.getParents().iterator();
		while (iter.hasNext()) {
			User parent = (User) iter.next();
			memFamLog.setAsParentFor(parent, user);
		}
		iter = rel.getSiblings().iterator();
		while (iter.hasNext()) {
			User sibling = (User) iter.next();
			memFamLog.setAsSiblingFor(user, sibling);
		}
	}

	private boolean processRecord(String record) throws RemoteException, CreateException {
		this.valueList = this.file.getValuesFromRecordString(record);
		boolean success = storeNationRegisterEntry();
		this.valueList = null;
		return success;
	}

	public void printFailedRecords() {
		System.out.println("Import failed for these records, please fix and import again:");
		Iterator iter = this.failedRecordList.iterator();
		while (iter.hasNext()) {
			System.out.println((String) iter.next());
		}
	}

	protected boolean storeNationRegisterEntry() throws RemoteException, CreateException {
		// variables
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
		Group group;
		// System.out.println("ssn = " + ssn);
		boolean success = true;
		if (ssn == null || ssn.equals("")) {
			return false;
		}
		group = getGroupForPostalCode(po);
		if (!this.relationsOnly) {
			// initialize business beans and data homes
			success = this.natBiz.updateEntry(symbol, oldId, ssn, familyId, name, commune, street, building, floor,
					sex, maritialStatus, empty, prohibitMarking, nationality, placeOfBirth, spouseSSN, fate, parish,
					po, address, addressCode, dateOfModification, placementCode, dateOfCreation, lastDomesticAddress,
					agentSsn, sNew, addressName, dateOfDeletion, newSsnOrName, dateOfBirth, group);
			if (FATE_DECEASED.equalsIgnoreCase(fate)) {
				User user;
				try {
					user = this.uBiz.getUser(ssn);
				}
				catch (FinderException e) {
					e.printStackTrace();
					return false;
				}
				//user.setDeleted(true);
				//user.setDeletedBy(((Integer) performer.getPrimaryKey()).intValue());
				//user.setDeletedWhen(IWTimestamp.getTimestampRightNow());
				//user.store();
				if (this.deceasedAddressString == null) {
					try {
						IWBundle iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(
								IW_BUNDLE_IDENTIFIER);
						IWResourceBundle iwrb = iwb.getResourceBundle(LocaleUtil.getIcelandicLocale());
						this.deceasedAddressString = iwrb.getLocalizedString("national_register.deceased", "Deceased");
					}
					catch (Exception e) {
						this.deceasedAddressString = "";
						System.out.println("Unable to initialize deceasedAddressString");
					}
				}
				this.uBiz.updateUsersMainAddressOrCreateIfDoesNotExist(user, this.deceasedAddressString, null, null,
						null, null, null, null);
				this.uBiz.updateUsersCoAddressOrCreateIfDoesNotExist(user, this.deceasedAddressString, null, null,
						null, null, null, null);
				FamilyLogic familyService = getMemberFamilyLogic();
				IWTimestamp dom = new IWTimestamp();
				if (dateOfModification != null && !"".equals(dateOfModification.trim())) {
					try {
						dom = new IWTimestamp(dateOfModification);
					}
					catch (IllegalArgumentException e) {
						System.out.println("Could not parse the date '" + dateOfModification + "'");
						e.printStackTrace();
						dom = IWTimestamp.RightNow();
					}
				}
				else {
					dom = IWTimestamp.RightNow();
				}
				familyService.registerAsDeceased(user, dom.getDate(), this.performer);
			}
			if (FATE_CHANGE_PERSONAL_ID.equalsIgnoreCase(fate)) {
				try {
					User user = this.uBiz.getUser(ssn);
					if (user != null) {
						user.setPersonalID(newSsnOrName);
					}
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
				return true;
			}
			if (FATE_REMOVED.equalsIgnoreCase(fate)) {
				try {
					User user = this.uBiz.getUser(ssn);
					this.uBiz.deleteUser(user, this.performer);
				}
				catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
			/*
			 * if(FATE_CHANGE_OLD_ID.equalsIgnoreCase(fate)){
			 * natBiz.updateUserOldID(oldId,ssn); return true; }
			 */
			if (this.postalCodeFix) {
				try {
					// User user = uBiz.getUser(ssn);
					if (this.postalCodeFix) {
						this.natBiz.updateUserAddress(this.uBiz.getUser(ssn), this.uBiz, address, po, null, null, null);
					}
					return true;
				}
				catch (Exception e) {
					return false;
				}
			}
		}
		else { // Handling thing otherwise not handled in relationsship
			// only mode
			if (this.citizenGroupFix) {
				User user;
				try {
					user = this.uBiz.getUser(ssn);
					user.setPrimaryGroup(group);
					user.store();
				}
				catch (FinderException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		// Family is marked as affected, and needs to be updated
		this.affectedFamilies.add(familyId);
		try {
			// Users previous family is marked as affected, and needs to be
			// updated
			this.affectedFamilies.add(getFamilyMemberHome().findBySSN(ssn).getFamilyNr());
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			// FinderExxception is ignored, since not all users have a family
		}
		return success;
	}

	/**
	 * @param po
	 * @return
	 * @throws CreateException
	 * @throws RemoteException
	 */
	private Group getGroupForPostalCode(String po) throws RemoteException, CreateException {
		// First see if it already has been fetched and stored in map. If so
		// just return it
		Group group = (Group) this.postalToGroupMap.get(po);
		if (null != group) {
			return group;
		}
		// Group not found, so finding it
		PostalCode postalCode = this.natBiz.getPostalCode(po);
		if (postalCode != null) {
			Commune commune = this.cBiz.getCommuneByPostalCode(postalCode);
			if (commune != null && commune.getGroup() != null) {
				group = commune.getGroup();
				this.postalToGroupMap.put(po, group);
			}
		}
		else {
			try {
				group = this.cBiz.getOtherCommuneCreateIfNotExist().getGroup();
				if (null != group) {
					System.out.println("NationalRegisterImport : connecting po:'" + po + "' to group:'"
							+ group.getName() + "'");
				}
				this.postalToGroupMap.put(po, group);
			}
			catch (FinderException e) {
				System.out.println("NationalRegisterImport : 'Other' group not found, throwing RuntimeException. \n\nMake sure the PostalCodeBundleStarter is run at least once.");
				throw new RuntimeException(e);
			}
		}
		return group;
	}

	private String getProperty(int columnIndex) {
		String value = null;
		if (this.valueList != null) {
			try {
				value = (String) this.valueList.get(columnIndex);
			}
			catch (RuntimeException e) {
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

	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#setImportFile(com.idega.block.importer.data.ImportFile)
	 */
	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
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
		return this.failedRecordList;
	}

	public FamilyLogic getMemberFamilyLogic() throws RemoteException {
		if (this.famLog == null) {
			this.famLog = (FamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), FamilyLogic.class);
		}
		return this.famLog;
	}

	protected FamilyMemberHome getFamilyMemberHome() {
		try {
			return (FamilyMemberHome) this.getIDOHome(FamilyMember.class);
		}
		catch (RemoteException e) {
			throw new EJBException(e.getMessage());
		}
	}
	// TODO add fix for specific groupIDs for certain people
}