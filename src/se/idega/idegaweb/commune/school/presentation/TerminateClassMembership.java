package se.idega.idegaweb.commune.school.presentation;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.*;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.*;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.*;
import com.idega.user.data.User;
import com.idega.user.presentation.UserSearcher;
import com.idega.util.IWTimestamp;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntryHome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMember;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

/**
 * TerminateClassMembership is an IdegaWeb block were the user can terminate a
 * membership in a school class. 
 * <p>
 * Last modified: $Date: 2004/11/18 12:59:50 $ by $Author: malin $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.33 $
 * @see com.idega.block.school.data.SchoolClassMember
 * @see se.idega.idegaweb.commune.school.businessSchoolCommuneBusiness
 * @see javax.ejb
 */
public class TerminateClassMembership extends SchoolCommuneBlock {
	private static final String PREFIX = "TermClassMemb_";
	
	private static final String ADDRESS_DEFAULT = "Adress";
	private static final String ADDRESS_KEY = PREFIX + "address";
	private static final String BACK_DEFAULT = "Tillbaka";
	private static final String BACK_KEY = PREFIX + "back";
	private static final String MEMBERSHIP_DEFAULT = "Placering";
	private static final String MEMBERSHIP_KEY
		= PREFIX + "membership";
	private static final String ISTERMINATED_DEFAULT = " har avslutats";
	private static final String ISTERMINATED_KEY = PREFIX + "isTerminated";
	private static final String NOTES_DEFAULT = "Kommentar";
	private static final String NOTES_KEY = PREFIX + "notes";
	private static final String NOTKNOWN_DEAFULT  = "Inte känd";
	private static final String NOTKNOWN_KEY  = PREFIX + "notKnown";
	private static final String NOUSERFOUND_KEY = PREFIX + "noUserFound";
	private static final String NOUSERFOUND_DEFAULT
		= "Inga träffar på valda sökvillkor";
	public static final String MEMBER_KEY = PREFIX + "member";
	private static final String MEMBERSHIPOF_DEFAULT = "Placeringen av ";
	private static final String MEMBERSHIPOF_KEY = PREFIX + "membershipOf";
	private static final String ONLY_UNENDED_PLACEMENTS_SEARCHABLE_KEY = PREFIX + "only_unended_placements_searchable";
	private static final String ONLY_MOVING_TO_OTHER_MUNICIP_KEY = PREFIX + "only_moving_to_other_municip";
	private static final String PROVIDERNOTFOUND_DEFAULT
		= "Anordnare ej funnen i databas";
	private static final String PROVIDERNOTFOUND_KEY
		= PREFIX + "providerNotFound";
	private static final String SCHOOLCLASS_DEFAULT = "klass";
	private static final String SCHOOLCLASS_KEY = PREFIX + "schoolClass";
	private static final String SCHOOLYEAR_DEFAULT = "skolår";
	private static final String SCHOOLYEAR_KEY = PREFIX + "schoolYear";
	private static final String STARTDATE_DEFAULT = "Startdatum";
	private static final String STARTDATE_KEY = PREFIX + "startDate";
	private static final String TERMINATE_MESSAGE_KEY = PREFIX + "terminate_message";
	private static final String TERMINATE_MESSAGE_DEFAULT = "Placeringen för {0} i {2}/{1} avslutades {3,date} av {5}.\n\n{4}.";
	private static final String TERMINATEMEMBERSHIP_DEFAULT
		= "Avsluta placering";
	private static final String TERMINATEMEMBERSHIP_KEY
		= PREFIX + "terminateMembership"; 
	private static final String TERMINATIONDATE_DEFAULT = "Avslutningsdatum";
	private static final String TERMINATIONDATE_KEY
		= PREFIX + "terminationDate";
	private static final String EMPTYTERMINATIONDATE_DEFAULT = "You have to set the termination date!";
	private static final String EMPTYTERMINATIONDATE_KEY
	= PREFIX + "emptyTerminationDate";
	private static final String EARLIESTTERMINATIONDATE_DEFAULT = "Earliest possible placement date is";
	private static final String EARLIESTTERMINATIONDATE_KEY
	= PREFIX + "earlyTerminationDate";
	private static final String EARLIESTTERMINATIONDATE2_DEFAULT = "Please contact the Kundvalsgruppen if you need to delete a placement.";
	private static final String EARLIESTTERMINATIONDATE2_KEY
	= PREFIX + "early2TerminationDate";
	private static final String TOOMANYSTUDENTSFOUND_DEFAULT
		= "För många träffar för att visas. Försök begränsa sökningen.";
	private static final String TOOMANYSTUDENTSFOUND_KEY
		= PREFIX + "tooManyStudentsFound";
	private static final String WRONGDATEFORMAT_DEFAULT
		= "Felaktigt datumformat";
	private static final String WRONGDATEFORMAT_KEY
		= PREFIX + "wrongDateFormat";
	private static final String YOUMUSTBELOGGEDON_DEFAULT
		= "Du måste vara inloggad för att använda denna funktion";
	private static final String YOUMUSTBELOGGEDON_KEY
		= PREFIX + "youMustBeLoggedOn";
	
	private static final String ACTION_TERMINATE_KEY
		= PREFIX + "terminateMembership";
	
	private static final SimpleDateFormat shortDateFormatter
		= new SimpleDateFormat ("yyyyMMdd");
	private static int MAX_FOUND_USER_COLS = 20;
	private static int MAX_FOUND_USER_ROWS = 1;
	private static int MAX_FOUND_USERS
		= MAX_FOUND_USER_COLS * MAX_FOUND_USER_ROWS;
	
	/**
	 * Main is the event handler of Terminate Class Membership Form.
	 *
	 * @param context session data like user info etc.
	 */
	public void init (final IWContext context) {
		
		try {
			if (!context.isLoggedOn ()) {
				displayRedText (YOUMUSTBELOGGEDON_KEY,
												YOUMUSTBELOGGEDON_DEFAULT);
			} else if (context.isParameterSet (ACTION_TERMINATE_KEY)) {
				add (createMainTable (getTerminateMembershipTable (context)));
			} else {
				add (createMainTable (getUserSearchFormTable (context)));
			}
		} catch (final Exception exception) {
			logWarning ("Exception caught in " + getClass ().getName ()
									+ " " + (new Date (System.currentTimeMillis())));
			logWarning ("Parameters:");
			final Enumeration enumer = context.getParameterNames ();
			while (enumer.hasMoreElements ()) {
				final String key = (String) enumer.nextElement ();
				logWarning ('\t' + key + "='"
										+ context.getParameter (key) + "'");
			}
			log (exception);
			add ("Det inträffade ett fel. Försök igen senare.");
		}
	}
	
	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
	 * @return Table to add to output
	 */
	private Table createMainTable (final PresentationObject content) {
		final Table mainTable = new Table();
		mainTable.setCellpadding (getCellpadding ());
		mainTable.setCellspacing (getCellspacing ());
		mainTable.setWidth (Table.HUNDRED_PERCENT);
		mainTable.setColumns (1);
		mainTable.setRowColor (1, getHeaderColor ());
		mainTable.setRowAlignment(1, Table.HORIZONTAL_ALIGN_CENTER) ;
		
		mainTable.add (getSmallHeader (localize (TERMINATEMEMBERSHIP_KEY,
																						 TERMINATEMEMBERSHIP_DEFAULT)),
									 1, 1);
		mainTable.add (content, 1, 2);
		return mainTable;
	}
	
	/**
	 * Terminates membership in a school class for the user kept in session
	 * object MEMBER_KEY
	 *
	 * @param context session data
	 */
	private Table getTerminateMembershipTable (final IWContext context) 
		throws RemoteException, FinderException, javax.ejb.CreateException {
		// find input values
		final Integer memberId
				= new Integer (context.getParameter (ACTION_TERMINATE_KEY));
		final SchoolClassMember member
				= getSchoolClassMemberHome ().findByPrimaryKey (memberId);
		final Date date
				= getDateFromString (context.getParameter (TERMINATIONDATE_KEY));
		final User child = member.getStudent ();
		final String childName = child.getFirstName () + " "
				+ child.getLastName ();
		final User adminUser = context.getCurrentUser ();
		final String adminUserName = adminUser == null ? "Admin"
				: adminUser.getFirstName () + " " + adminUser.getLastName ();
		final SchoolClass schoolClass = member.getSchoolClass ();
		final School school = schoolClass.getSchool ();
		final Table table = new Table ();
		if (null == date) {
			final Text text = new Text (localize (WRONGDATEFORMAT_KEY,
																						WRONGDATEFORMAT_DEFAULT));
			text.setFontColor ("#ff0000");
			table.add (text, 1, 1);
		} else {

			// terminate membership
			final String notes = context.getParameter (NOTES_KEY);
			member.setNotes (notes);
			final Timestamp terminationDate = new  Timestamp (date.getTime ());
			member.setRemovedDate (terminationDate);
			member.store ();
			terminateResources (member, terminationDate);
			terminateRegularPayments (child, school, terminationDate);
			
			// put confirmation output
			final String subject = localize
					(MEMBERSHIPOF_KEY, MEMBERSHIPOF_DEFAULT) + childName
					+ localize (ISTERMINATED_KEY, ISTERMINATED_DEFAULT);
			table.add (new Text (subject), 1, 1);
			
			// send confirmation message
			final MessageBusiness messageBusiness = (MessageBusiness) IBOLookup
					.getServiceInstance (context, MessageBusiness.class);
			final CommuneUserBusiness communeUserBusiness
					= (CommuneUserBusiness) IBOLookup.getServiceInstance
					(context, CommuneUserBusiness.class);
			final User custodian
					= communeUserBusiness.getCustodianForChild (child);
			if (null != custodian) {
				final int custodianId
						= ((Integer) custodian.getPrimaryKey ()).intValue ();
				final Object [] arguments =
						{ childName, schoolClass.getName (), school.getName (),
							date, notes, adminUserName };
				final String messageBody = MessageFormat.format
						(localize (TERMINATE_MESSAGE_KEY,
											 TERMINATE_MESSAGE_DEFAULT), arguments);
				final Message message = messageBusiness.createUserMessage
						(custodianId, subject, messageBody);
				message.setSender (context.getCurrentUser ());
				message.store();
			}
			
		}
		table.setHeight (2, 12);
		table.add (getSmallLink (localize (BACK_KEY, BACK_DEFAULT)), 1, 3);
		return table;
	}
	
	private void terminateRegularPayments(final User child, final School school, final Timestamp terminationDate) throws IDOLookupException {
		try {
			final RegularPaymentEntryHome home =	(RegularPaymentEntryHome) IDOLookup.getHome (RegularPaymentEntry.class);
			final Date sqlDate = new Date (terminationDate.getTime ());
			final Collection regularPaymentEntries
					= home.findOngoingByUserAndProviderAndDate
					(child, school, sqlDate);
			for (Iterator i = regularPaymentEntries.iterator ();
					 i.hasNext ();) {
				final RegularPaymentEntry regularPaymentEntry
						= (RegularPaymentEntry) i.next ();
				regularPaymentEntry.setTo (sqlDate);
				regularPaymentEntry.store ();
			}
		} catch (FinderException e) {
			// no problem, no regular payments to remove
		}
	}
	
	private void terminateResources
		(final SchoolClassMember placement, Timestamp terminationDate)
		throws IDOLookupException {
		try {
			final Collection resources = getResourceClassMemberHome ()
					.findAllByClassMemberId ((Integer) placement.getPrimaryKey ());
			for (Iterator i = resources.iterator (); i.hasNext ();) {
				final ResourceClassMember resource = (ResourceClassMember) i.next ();
				final Timestamp resourceEndDate = null == resource.getEndDate ()
						? null : new Timestamp (resource.getEndDate ().getTime ());
				if (null == resourceEndDate
						|| terminationDate.before (resourceEndDate)) {
					resource.setEndDate (new Date (terminationDate.getTime ()));
					resource.store ();
				}
			}
		} catch (FinderException e) {
			// no problem, no resources attached to this placement
		}
	}
	
	private static ResourceClassMemberHome getResourceClassMemberHome ()
		throws IDOLookupException {
		return (ResourceClassMemberHome)
				IDOLookup.getHome (ResourceClassMember.class);
	}
	
	/**
	 * Shows a form where the user can enter ssn, first name and/or last name
	 * and then after first search, click on one of possibly more than one name
	 * in the search result.
	 *
	 * @param context session data
	 * @exception RemoteException if exception happens in lower layer
	 */
	private Table getUserSearchFormTable (final IWContext context)
		throws RemoteException, FinderException {
		// set up searcher
		final UserSearcher searcher = createSearcher ();
		fillSearcherWithStudents (context, searcher);
		final User foundUser = searcher.getUser ();
		
		// output result
		final Table table = new Table ();
		final Form searchForm = new Form();
		searchForm.setOnSubmit("return checkInfoForm()");
		searchForm.add (searcher);
		table.add (searchForm, 1, 1);
		if (null == foundUser) {
			final String message1 = localize (ONLY_MOVING_TO_OTHER_MUNICIP_KEY,
																			ONLY_MOVING_TO_OTHER_MUNICIP_KEY);
			final String message2 = localize (ONLY_UNENDED_PLACEMENTS_SEARCHABLE_KEY,
																			 ONLY_UNENDED_PLACEMENTS_SEARCHABLE_KEY);
			
			
			table.add (getSmallText ("- " + message1), 1, 2);
			table.add (getSmallText ("- " + message2), 1, 3);
		} else {
			// exactly one user found - display user and termination form
			final Table terminateTable = new Table ();
			terminateTable.add (getStudentTable (context, foundUser), 1, 2);
			final Form terminateForm = new Form ();
			terminateForm.add (terminateTable);
			table.add (terminateForm, 1, 3);
		}
		return table;
	}
	
	/**
	 * Retreives students that are currently members of a school class
	 *
	 * @param context session data
	 * @param searcher to use for searching
	 * @return number of users found
	 * @exception RemoteException if exception happens in lower layer
	 */
	private void fillSearcherWithStudents (final IWContext context,
																				 final UserSearcher searcher)
		throws RemoteException {
		final Collection students = new HashSet ();
		try {
			// 1. start with a raw search
			searcher.process (context);
			Collection usersFound = searcher.getUsersFound ();
			if (null == usersFound) {
				final User singleUser = searcher.getUser ();
				if (null != singleUser) {
					usersFound = Collections.singleton (singleUser);
				} else {
					throw new FinderException ();
				}
			}
			
			// 2. filter out students that are placed and that the logged on
			// user is authorized to see
			final SchoolCommuneBusiness communeBusiness
					= (SchoolCommuneBusiness) IBOLookup.getServiceInstance
					(context, SchoolCommuneBusiness.class);
			final SchoolBusiness schoolBusiness
					= communeBusiness.getSchoolBusiness ();
			final SchoolClassMemberHome memberHome
					= schoolBusiness.getSchoolClassMemberHome ();
			final int schoolId = getSchoolID ();
			try {
				final Collection members = memberHome
						.findAllBySchoolAndUsersWithSchoolYearAndNotRemoved
						(schoolId, usersFound);
				for (Iterator i = members.iterator (); i.hasNext ();) {
					final SchoolClassMember student
							= (SchoolClassMember) i.next ();
					if (MAX_FOUND_USERS <= students.size ()) {
						// too many students found
						displayRedText (TOOMANYSTUDENTSFOUND_KEY,
														TOOMANYSTUDENTSFOUND_DEFAULT);
						throw new FinderException ();
					}
					students.add (student.getStudent ());
				}
			} catch (FinderException e) {
				// no problem, nu students found
			}
			if (!usersFound.isEmpty () && students.isEmpty ()) {
				displayRedText (NOUSERFOUND_KEY, NOUSERFOUND_DEFAULT);
			}
		} catch (FinderException e) {
			// no students found or too many students found
			// Collection 'students' will have the right members anyway
		}
		
		// 3. Set up search result for display
		if (students.isEmpty ()) {
			searcher.setUser (null);
			searcher.setUsersFound (null);
		} else if (1 == students.size ()) {
			searcher.setUser ((User) students.toArray () [0]);
			searcher.setUsersFound (null);
		} else {
			searcher.setUsersFound (students);
		}
	}
	
	/**
	 * Creates a table with address, school name etc. for this student
	 *
	 * @param context session data
	 * @param user the student
	 * @return filled or empty collection of students - never 'null'
	 * @exception RemoteException if exception happens in lower layer
	 */
	private Table getStudentTable
		(final IWContext context, final User user) throws RemoteException,
																											FinderException {
		
		final SchoolCommuneBusiness communeBusiness
				= (SchoolCommuneBusiness) IBOLookup.getServiceInstance
				(context, SchoolCommuneBusiness.class);
		final SchoolBusiness schoolBusiness
				= communeBusiness.getSchoolBusiness ();
		final SchoolClassMemberHome memberHome
				= schoolBusiness.getSchoolClassMemberHome ();
		final int schoolId = getSchoolID ();
		final Collection members = memberHome
				.findAllBySchoolAndUsersWithSchoolYearAndNotRemoved
				(schoolId, java.util.Collections.singleton (user));
		final Table table = new Table ();
		table.setColumns (2);
		int row = 1;
		final Text addressHeader = new Text (localize (ADDRESS_KEY,
																									 ADDRESS_DEFAULT) + ':');
		addressHeader.setBold ();
		final Text address = new Text (getAddressStringFromUser (user));
		table.add (addressHeader, 1, row);
		table.add (address, 2, row++);
		table.setHeight (row++, 24);
			
		for (Iterator i = members.iterator (); i.hasNext ();) {
			final SchoolClassMember student = (SchoolClassMember) i.next ();
			
			final Text memberHeader = new Text
					(localize (MEMBERSHIP_KEY, MEMBERSHIP_DEFAULT) + ':');
			memberHeader.setBold ();
			final Text member = new Text (null != student ? getMembershipString
																		(student, communeBusiness) : "ej placerad");
			final Text startDateHeader = new Text
					(localize (STARTDATE_KEY, STARTDATE_DEFAULT) + ':');
			memberHeader.setBold ();
			startDateHeader.setBold ();
			final Text startDate = new Text (null != student ? shortDateFormatter.format(student.getRegisterDate()) : "ej placerad");
			final Text terminationDateHeader = new Text
					(localize (TERMINATIONDATE_KEY, TERMINATIONDATE_DEFAULT) + ':');
			terminationDateHeader.setBold ();
			/*final TextInput terminationDateInput = (TextInput) getStyledInterface
					(new TextInput (TERMINATIONDATE_KEY));
			terminationDateInput.setLength (10);
			*/
						
			final DateInput terminationDateInput = (DateInput) getStyledInterface
			(new DateInput (TERMINATIONDATE_KEY));
			terminationDateInput.setAsNotEmpty(localize(EMPTYTERMINATIONDATE_KEY, EMPTYTERMINATIONDATE_DEFAULT));
		
			IWTimestamp today = new IWTimestamp();
			IWTimestamp todayCompare = new IWTimestamp();
			IWTimestamp registerDate = new IWTimestamp(student.getRegisterDate());
			IWTimestamp earliestPossiblePlacementDate = null;
			boolean early2Contact = false;
			if (today.isEarlierThan(registerDate)){
				registerDate.addDays(1);
				earliestPossiblePlacementDate = new IWTimestamp(registerDate);
				early2Contact = true;
			}
			else if (today.isLaterThan(registerDate)){
				todayCompare.addWeeks(-2);
				if (todayCompare.isEarlierThan(registerDate)){
					registerDate.addDays(1);
					earliestPossiblePlacementDate= registerDate;
					early2Contact = true;
				}
				else {
					today.addWeeks(-2);
					earliestPossiblePlacementDate = today; 
				}
			}
			else { //same day
				today.addDays(1);
				earliestPossiblePlacementDate = today; 
				early2Contact = true;
			}
							
			if (early2Contact){
				terminationDateInput.setEarliestPossibleDate(earliestPossiblePlacementDate.getDate(), localize(EARLIESTTERMINATIONDATE_KEY, EARLIESTTERMINATIONDATE_DEFAULT) + ": " + new IWTimestamp(earliestPossiblePlacementDate).getLocaleDate(context.getCurrentLocale(), IWTimestamp.SHORT)+ " " + localize(EARLIESTTERMINATIONDATE2_KEY, EARLIESTTERMINATIONDATE2_DEFAULT));	
			}
			else {
				terminationDateInput.setEarliestPossibleDate(earliestPossiblePlacementDate.getDate(), localize(EARLIESTTERMINATIONDATE_KEY, EARLIESTTERMINATIONDATE_DEFAULT) + ": " + new IWTimestamp(earliestPossiblePlacementDate).getLocaleDate(context.getCurrentLocale(), IWTimestamp.SHORT));
			}
			
			
			//terminationDateInput.setContent
			//		(shortDateFormatter.format(new Date (System.currentTimeMillis ())));
			final Text notesHeader = new Text (localize (NOTES_KEY, NOTES_DEFAULT)
																				 + ":");
			notesHeader.setBold ();
			final TextInput notesInput
					= (TextInput) getStyledInterface (new TextInput (NOTES_KEY));
			notesInput.setLength (40);
		
			// put interface objects in output table
			table.add (memberHeader, 1, row);
			table.add (member, 2, row++);
			table.add (startDateHeader, 1, row);
			table.add (startDate, 2, row++);
			table.add (terminationDateHeader, 1, row);
			table.add (terminationDateInput, 2, row++);
			table.add (notesHeader, 1, row);
			table.add (notesInput, 2, row++);
			final String buttonText
					= getLocalizedString (TERMINATEMEMBERSHIP_KEY,
																TERMINATEMEMBERSHIP_DEFAULT);
			final SubmitButton submit = (SubmitButton) getButton
					(new SubmitButton	(buttonText, ACTION_TERMINATE_KEY,
														 student.getPrimaryKey () + ""));
			table.add (submit, 1, row++);
			table.setHeight (row++, 24);
		}

		return table;
	}
	
	/**
	 * Gets address information for this user
	 *
	 * @param user the person to get info about
	 * @return displayable string with address info
	 */
	private String getAddressStringFromUser (final User user) {
		String result = localize (NOTKNOWN_KEY, NOTKNOWN_DEAFULT);
		final Collection addressCollection = user.getAddresses ();
		if (null != addressCollection && !addressCollection.isEmpty ()) {
			final Address address = (Address) addressCollection.toArray () [0];
			result = address.getStreetName () + address.getStreetNumber ()
					+ ", " + address.getPostalAddress ();
		}
		
		return result;
	}
	
	/**
	 * Gets string with info about current school class and school for this user
	 *
	 * @param student the person to get info about
	 * @param communeBusiness utility business object
	 * @return string with info about current school class membership
	 * @exception RemoteException if something fails in business layer
	 */
	private String getMembershipString
		(final SchoolClassMember student,
		 final SchoolCommuneBusiness communeBusiness) throws RemoteException {
		
		// get some business objects
		final SchoolBusiness schoolBusiness
				= communeBusiness.getSchoolBusiness ();
		final SchoolClassHome classHome = schoolBusiness.getSchoolClassHome ();
		final SchoolHome schoolHome = schoolBusiness.getSchoolHome ();
		final SchoolYearHome yearHome = schoolBusiness.getSchoolYearHome ();
		
		try {
			// find school class
			final Integer classId = new Integer (student.getSchoolClassId ());
			final SchoolClass schoolClass
					= classHome.findByPrimaryKey (classId);
			
			// find school
			final Integer schoolId = new Integer (schoolClass.getSchoolId ());
			final School school = schoolHome.findByPrimaryKey (schoolId);
			
			//find school year
			final Integer yearId = new Integer (student.getSchoolYearId ());
			final SchoolYear schoolYear = yearHome.findByPrimaryKey (yearId);
			
			return school.getName () + ", "
					+ localize (SCHOOLYEAR_KEY, SCHOOLYEAR_DEFAULT) + " "
					+ schoolYear.getName () + ", "
					+ localize (SCHOOLCLASS_KEY, SCHOOLCLASS_DEFAULT) + " "
					+ schoolClass.getName ();
		} catch (final FinderException e) {
			return localize (PROVIDERNOTFOUND_KEY, PROVIDERNOTFOUND_DEFAULT);
		}
	}
	
	private static Date getDateFromString (final String rawInput) {
		final StringBuffer digitOnlyInput = new StringBuffer();
		for (int i = 0; i < rawInput.length(); i++) {
			if (Character.isDigit(rawInput.charAt(i))) {
				digitOnlyInput.append(rawInput.charAt(i));
			}
		}
		if (digitOnlyInput.length() == 6) {
			digitOnlyInput.insert(0, 20);
		}
		
		if (digitOnlyInput.length() != 8) {
			return null;
		}
		final int year = new Integer(digitOnlyInput.substring(0, 4)).intValue();
		final int month
				= new Integer(digitOnlyInput.substring(4, 6)).intValue();
		final int day = new Integer(digitOnlyInput.substring(6, 8)).intValue();
		if (year < 2003 || month < 1 || month > 12 || day < 1 || day > 31) {
			return null;
		}
		final Calendar calendar = Calendar.getInstance ();
		calendar.set (year, month - 1, day);
		return new Date (calendar.getTimeInMillis ());
	}
		
	private String getLocalizedString(final String key, final String value) {
		return getResourceBundle().getLocalizedString(key, value);
	}
	
	private void displayRedText (final String key, final String defaultString) {
		final Text text
				= new Text ('\n' + localize (key, defaultString) + '\n');
		text.setFontColor ("#ff0000");
		add (text);
	}
	
	private UserSearcher createSearcher () {
		final UserSearcher searcher = new UserSearcher ();
		searcher.setOwnFormContainer (false);
		searcher.setShowMiddleNameInSearch (false);
		searcher.setLastNameLength (14);
		searcher.setFirstNameLength (14);
		searcher.setPersonalIDLength (12);
		searcher.setMaxFoundUserCols (MAX_FOUND_USER_COLS); 
		searcher.setMaxFoundUserRows (MAX_FOUND_USER_ROWS);
		return searcher;
	}

	private static SchoolClassMemberHome getSchoolClassMemberHome () {
		try {
			return (SchoolClassMemberHome) IDOLookup.getHome (SchoolClassMember.class);
		} catch (Exception e) {
			e.printStackTrace ();
			return null;
		}
	}	
}
