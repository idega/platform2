/*
 * Created on 19.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.business.GenderBusiness;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.Name;


/**
 * @author laddi
 */
public class MarathonGroupUsersImportBean extends IBOServiceBean implements MarathonGroupUsersImport {

	ImportFile file;
	UserBusiness userBusiness;
	GroupBusiness groupBusiness;
	Group group;
	
	public boolean handleRecords() throws RemoteException {
		int groupID = Integer.parseInt(getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getProperty(IWMarathonConstants.PROPERTY_STAFF_GROUP_ID, "-1"));
		
		userBusiness = getUserBusiness(getIWApplicationContext());
		groupBusiness = getGroupBusiness(getIWApplicationContext());
		try {
			group = groupBusiness.getGroupByGroupID(groupID);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
		Vector errors = new Vector();
		if (group != null) {
			if (file != null) {
				String line = (String) file.getNextRecord();
				int counter = 1;
				while (line != null && !"".equals(line)) {
					++counter;
					System.out.println("Counter = "+counter);
					if (!handleLine(line, groupID)) {
						errors.add(line);
					}
					line = (String) file.getNextRecord();
				}
				System.out.println(counter);
			}
		}
		else {
			errors.add("No group found with ID = " + groupID);
		}
		
		
		if (!errors.isEmpty()) {
			System.out.println("Errors in the following lines :");
			Iterator iter = errors.iterator();
			while (iter.hasNext()) {
				System.out.println((String) iter.next());
			}
		}
		return true;
	}
	
	private boolean handleLine(String line, int groupID) throws RemoteException {
		ArrayList values = file.getValuesFromRecordString(line);
		int size = values.size();
		boolean validLine = true;
		
		String personalID = (String) values.get(0);
		String name = (String) values.get(1);
		
		User user = null;
		try {
			user = userBusiness.getUser(personalID);
		}
		catch (FinderException fe) {
			System.out.println("User not found, creating...");
		}

		if (user == null) {
			Name fullName = new Name(name);
			try {
				user = userBusiness.createUser(fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName(), personalID);
			}
			catch (CreateException ce) {
				ce.printStackTrace();
				return false;
			}
		}
		
		groupBusiness.addUser(groupID, user);

		return true;
	}
	

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	public List getFailedRecords() throws RemoteException {
		return null;
	}
	
	protected UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected GroupBusiness getGroupBusiness(IWApplicationContext iwac) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}