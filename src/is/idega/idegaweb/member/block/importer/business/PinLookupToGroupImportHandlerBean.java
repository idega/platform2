package is.idega.idegaweb.member.block.importer.business;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOSessionBean;
import com.idega.core.location.business.AddressBusiness;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.Status;
import com.idega.user.data.StatusHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.data.UserStatus;
import com.idega.user.data.UserStatusHome;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;
import com.idega.util.text.TextSoap;
/**
 * <p>Title: PinLookupToGroupImportHandlerBean</p>
 * <p>Description: A simple import file handler that reads file with personalIds and names and if the PIN exists in the database it adds that user to the root group</p>
 * <p>Idega Software Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */
public class PinLookupToGroupImportHandlerBean extends IBOSessionBean implements PinLookupToGroupImportHandler, ImportFileHandler {
	private static final int PIN_COLUMN = 0;
	private static final int NAME_COLUMN = 1;
	private static final int STATUS_COLUMN = 2;
	private List userProperties;
	private UserHome home;
	private UserStatusHome userStatusHome;
	private StatusHome statusHome;
	private AddressBusiness addressBiz;
	private UserBusiness userBiz;
	private Group rootGroup;
	private ImportFile file;
	private UserTransaction transaction;
	private ArrayList failedRecords;
	private Gender male;
	private Gender female;
	public PinLookupToGroupImportHandlerBean() {
	}
	public boolean handleRecords() throws RemoteException {
		transaction = this.getSessionContext().getUserTransaction();
		Timer clock = new Timer();
		clock.start();
		try {
			//initialize business beans and data homes
			userBiz = (UserBusiness) this.getServiceInstance(UserBusiness.class);
			statusHome = (StatusHome) this.getIDOHome(Status.class);
			userStatusHome = (UserStatusHome) this.getIDOHome(UserStatus.class);
			//addressBiz = (AddressBusiness) this.getServiceInstance(AddressBusiness.class);
			failedRecords = new ArrayList();
			//if the transaction failes all the users and their relations are removed
			transaction.begin();
			//iterate through the records and process them
			String item;
			int count = 0;
			while (!(item = (String) file.getNextRecord()).equals("")) {
				count++;
				if (!processRecord(item))
					failedRecords.add(item);
			}
			clock.stop();
			System.out.println("Time to handleRecords: " + clock.getTime() + " ms  OR " + ((int) (clock.getTime() / 1000)) + " s.");
			// System.gc();
			//success commit changes
			transaction.commit();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try {
				transaction.rollback();
			}
			catch (SystemException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	private boolean processRecord(String record) throws RemoteException {
	    record = TextSoap.findAndCut(record, "-");
		record = TextSoap.removeWhiteSpace(record);
		record = (record.length() == 9) ? "0" + record : record;
		
		userProperties = file.getValuesFromRecordString(record);
		User user = null;
		//variables
		String statusId = null;
		String name = null;
		String PIN = null;
		
		try{
			PIN = (String) userProperties.get(PIN_COLUMN);
		}
		catch (IndexOutOfBoundsException e4) {
			return false;
		}
		
		if (PIN == null){
			return false;
		}
		
//		try{
//			name = (String) userProperties.get(NAME_COLUMN);
//		}
//		catch (IndexOutOfBoundsException e4) {
//			e4.printStackTrace();
//		}
		
//		try {
		if(userProperties.size() > STATUS_COLUMN) {
			statusId = (String) userProperties.get(STATUS_COLUMN);
		}
//		catch (IndexOutOfBoundsException e4) {
//			e4.printStackTrace();
//		}

		try {
			user = userBiz.getUser(PIN);
		}
		catch (Exception e) {
			System.out.println(e.getMessage()+", failed personalID was = "+PIN);
		    //e.printStackTrace();
			return false;
		}
		
		
		if (!IWMemberConstants.GROUP_TYPE_CLUB.equals(rootGroup.getGroupType())) {
			rootGroup.addGroup(user);
		}
		else { //if a club then 
			String[] tempType = { IWMemberConstants.GROUP_TYPE_TEMPORARY };
			Collection groups = rootGroup.getChildGroups(tempType, true);
			if (groups != null && !groups.isEmpty()) {
				((Group) groups.iterator().next()).addGroup(user);
			}
		}
		
//		if (name != null){
//			user.setFullName(name);
//			user.store();
//		}
		
		if (statusId != null) {
			int statusNumber = Integer.parseInt(statusId);
			try {
				Status status = statusHome.findByPrimaryKey(new Integer(statusNumber));
				try {
					Collection statuses = userStatusHome.findAllByUserIDAndStatusID(((Integer) user.getPrimaryKey()), ((Integer) status.getPrimaryKey()));
					Iterator iter = statuses.iterator();
					while (iter.hasNext()) {
						UserStatus stat = (UserStatus) iter.next();
						if (stat.getDateTo() != null && stat.getDateTo().before(IWTimestamp.getTimestampRightNow())) {
							//no valid userstatus (by date)
							UserStatus userStatus;
							try {
								userStatus = userStatusHome.create();
								userStatus.setUser(user);
								userStatus.setStatus(status);
								userStatus.setDateFrom(IWTimestamp.getTimestampRightNow());
								userStatus.store();
							}
							catch (CreateException e3) {
								e3.printStackTrace();
								return false;
							}
						}
					}
				}
				catch (EJBException e2) {
					//no userstatus found add it
					UserStatus userStatus;
					try {
						userStatus = userStatusHome.create();
						userStatus.setUser(user);
						userStatus.setStatus(status);
						userStatus.setDateFrom(IWTimestamp.getTimestampRightNow());
						userStatus.store();
					}
					catch (CreateException e3) {
						e3.printStackTrace();
						return false;
					}
				}
			}
			catch (FinderException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		user = null;
		return true;
	}
	public void setImportFile(ImportFile file) {
		this.file = file;
	}
	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#setRootGroup(Group)
	 */
	public void setRootGroup(Group group) {
		rootGroup = group;
	}
	/**
	* @see com.idega.block.importer.business.ImportFileHandler#getFailedRecords()
	*/
	public List getFailedRecords() {
		return failedRecords;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		List list = new ArrayList(1);
		list.add(new PinLookupToGroupImportHandlerPlugin());
		return list;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group)
	 */
	public String canCreateSubGroup(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}