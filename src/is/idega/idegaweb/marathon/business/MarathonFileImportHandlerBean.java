package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Participant;
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
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.Name;


/**
 * @author gimmi
 */
public class MarathonFileImportHandlerBean extends IBOServiceBean  implements MarathonFileImportHandler{

	ImportFile file;
	Collection countries;
	RunBusiness business;
	GenderBusiness genderBusiness;
	Locale englishLocale;
	Locale icelandicLocale;
	Collection runs;
	
	public static void main(String[] args) throws RemoteException {
		/*File fFile = new File("/Users/gimmi/Desktop/Marathonhlauparar.csv");
		ImportFile ifile = new ColumnSeparatedImportFile(fFile);
		
		MarathonFileImportHandlerBean m = new MarathonFileImportHandlerBean();
		m.setImportFile(ifile);
		m.handleRecords();*/
	}
	
	public boolean handleRecords() throws RemoteException {
		business = getBusiness(getIWApplicationContext());
		genderBusiness = getGenderBusiness(getIWApplicationContext());
		countries = business.getCountries();
		runs = business.getRuns();
		englishLocale = LocaleUtil.getLocale(LocaleSwitcher.englishParameterString);
		icelandicLocale = LocaleUtil.getLocale(LocaleSwitcher.icelandicParameterString);
		
		Vector errors = new Vector();
		if (file != null) {
			String line = (String) file.getNextRecord();
			int counter = 1;
			while (line != null && !"".equals(line)) {
				++counter;
				System.out.println("Counter = "+counter);
				if (!handleLine(line)) {
					errors.add(line);
				}
				line = (String) file.getNextRecord();
			}
			System.out.println(counter);
			
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
	
	private boolean handleLine(String line) {
		ArrayList values = file.getValuesFromRecordString(line);
		int size = values.size();
		boolean validLine = true;
		
		String participantNumber = (String) values.get(0);
		String gender = (String) values.get(1);
		String dateOfBirth = (String) values.get(2);
		String name = (String) values.get(3);
		String personalID = (String) values.get(4);
		String nationality = (String) values.get(5);
		String chipTime = (String) values.get(6);
		String runTime = (String) values.get(7);
		String run = (String) values.get(8);
		String distance = (String) values.get(9);
		String year = (String) values.get(10);
		String group = (String) values.get(11);
		String chipNumber = (String) values.get(12);
		
		int number = -1;
		try {
			number = Integer.parseInt(participantNumber);
		} catch (NumberFormatException n) {
			System.out.println("not using line... starts with : " +participantNumber);
			validLine = false;
		}
		
		if (validLine) {
			String countryPK = null;
			
			Iterator iter = countries.iterator();
			Country country = null;
			boolean found = false;
			while (iter.hasNext() && !found) {
				country = (Country) iter.next();
				found = nationality.equals(country.getIsoAbbreviation());
				break;
			}
			
			IWTimestamp birth = new IWTimestamp(dateOfBirth);

			try {
				Group runGroup = null;
				Iterator iterator = runs.iterator();
				while (iterator.hasNext()) {
					Group element = (Group) iterator.next();
					if (element.getName().equals(run)) {
						runGroup = element;
						break;
					}
				}
				
				Group yearGroup = null;
				iterator = runGroup.getChildrenIterator();
				while (iterator.hasNext()) {
					Group element = (Group) iterator.next();
					if (element.getName().equals(year)) {
						yearGroup = element;
						break;
					}
				}
				
				Group distanceGroup = null;
				Collection distances = business.getDistancesMap(runGroup, year);
				iterator = distances.iterator();
				while (iterator.hasNext()) {
					Group element = (Group) iterator.next();
					if (element.getName().equals(distance)) {
						distanceGroup = element;
						break;
					}
				}
			
				if (validLine) {
					User user = null;
					try {
						if (personalID.length() > 0) {
							user = business.getUserBiz().getUser(personalID);
						}
						else {
							user = business.getUserBiz().getUserHome().findByDateOfBirthAndName(birth.getDate(), name);
						}
					}
					catch (FinderException fe) {
						System.out.println("User not found, creating...");;
					}
					if (user == null) {
						try {
							Gender theGender = genderBusiness.getGender(new Integer(gender));
							Name fullName = new Name(name);
							user = business.getUserBiz().createUser(fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName(), personalID, theGender, birth);
						}
						catch (FinderException fe) {
							System.err.println("Gender not found");
							return false;
						}
						catch (CreateException ce) {
							ce.printStackTrace();
							return false;
						}
					}
					
					Participant participant = null;
					try {
						participant = business.getParticipantByRunAndYear(user, runGroup, yearGroup);
					}
					catch (FinderException fe) {
						try {
							participant = business.importParticipant(user, runGroup, yearGroup, distanceGroup, country);
						}
						catch (CreateException ce) {
							ce.printStackTrace();
							return false;
						}
					}
					participant.setChipNumber(chipNumber);
					participant.setUserNationality(nationality);
					business.updateRunForParticipant(participant, number, runTime, chipTime);
					return true;
				}
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		return false;
	}
	

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	public List getFailedRecords() throws RemoteException {
		return null;
	}
	
	protected RunBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (RunBusiness) IBOLookup.getServiceInstance(iwac, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected GenderBusiness getGenderBusiness(IWApplicationContext iwac) {
		try {
			return (GenderBusiness) IBOLookup.getServiceInstance(iwac, GenderBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}