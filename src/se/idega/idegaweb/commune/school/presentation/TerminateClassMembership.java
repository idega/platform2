package se.idega.idegaweb.commune.school.presentation;

import com.idega.block.school.business.*;
import com.idega.block.school.data.*;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.data.User;
import is.idega.idegaweb.member.presentation.UserSearcher;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.FinderException;
import javax.servlet.http.HttpSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

/**
 * TerminateClassMembership is an IdegaWeb block were the user can terminate a
 * membership in a school class. 
 * <p>
 * Last modified: $Date: 2003/10/09 10:36:43 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.11 $
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
    private static final String CURRENTMEMBERSHIP_DEFAULT
        = "Nuvarande placering";
    private static final String CURRENTMEMBERSHIP_KEY
        = PREFIX + "currentMembership";
    private static final String ISTERMINATED_DEFAULT = " har avslutats";
    private static final String ISTERMINATED_KEY = PREFIX + "isTerminated";
    private static final String NOTES_DEFAULT = "Kommentar";
    private static final String NOTES_KEY = PREFIX + "notes";
    private static final String NOUSERFOUND_KEY = PREFIX + "noUserFound";
    private static final String NOUSERFOUND_DEFAULT
        = "Inga träffar på valda sökvillkor";
    public static final String MEMBER_KEY = PREFIX + "member";
    private static final String MEMBERSHIPOF_DEFAULT = "Placeringen av ";
    private static final String MEMBERSHIPOF_KEY = PREFIX + "membershipOf";
    private static final String PROVIDERNOTFOUND_DEFAULT
        = "Anordnare ej funnen i databas";
    private static final String PROVIDERNOTFOUND_KEY
        = PREFIX + "providerNotFound";
    private static final String SCHOOLCLASS_DEFAULT = "klass";
    private static final String SCHOOLCLASS_KEY = PREFIX + "schoolClass";
    private static final String SCHOOLYEAR_DEFAULT = "skolår";
    private static final String SCHOOLYEAR_KEY = PREFIX + "schoolYear";
    private static final String TERMINATEMEMBERSHIP_DEFAULT
        = "Avsluta placering";
    private static final String TERMINATEMEMBERSHIP_KEY
        = PREFIX + "terminateMembership";
    private static final String TERMINATIONDATE_DEFAULT = "Avslutningsdatum";
    private static final String TERMINATIONDATE_KEY
        = PREFIX + "terminationDate";
    private static final String TOOMANYSTUDENTSFOUND_DEFAULT
        = "För många träffar för att visas. Försök begränsa sökningen.";
    private static final String TOOMANYSTUDENTSFOUND_KEY
        = PREFIX + "tooManyStudentsFound";
    private static final String WRONGDATEFORMAT_DEFAULT
        = "Felaktigt datumformat";
    private static final String WRONGDATEFORMAT_KEY
        = PREFIX + "wrongDateFormat";
    private static final String ACTION_TERMINATE_KEY
        = PREFIX + "terminateMembership";
    
    private static final SimpleDateFormat shortDateFormatter
        = new SimpleDateFormat ("yyyyMMdd");
    private static int MAX_FOUND_USER_COLS = 20;
    private static int MAX_FOUND_USER_ROWS = 1;
    private static int MAX_FOUND_USERS
        = MAX_FOUND_USER_COLS * MAX_FOUND_USER_ROWS;

	/**
	 * Main is the event handler of InvoiceByCompensationForm.
	 *
	 * @param context session data like user info etc.
	 */
	public void init (final IWContext context) {

        try {
            if (context.isParameterSet (ACTION_TERMINATE_KEY)) {
                add (createMainTable (getTerminateMembershipTable (context)));
            } else {
                add (createMainTable (getUserSearchFormTable (context)));
            }
        } catch (final Exception exception) {
            System.err.println ("Exception caught in " + getClass ().getName ()
                                + " " + (new Date ()).toString ());
            System.err.println ("Parameters:");
            final Enumeration enum = context.getParameterNames ();
            while (enum.hasMoreElements ()) {
                final String key = (String) enum.nextElement ();
                System.err.println ('\t' + key + "='"
                                    + context.getParameter (key) + "'");
            }
            exception.printStackTrace ();
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
    private Table getTerminateMembershipTable (final IWContext context) {
        // find input values
        final HttpSession session = context.getSession ();
        final SchoolClassMember member = (SchoolClassMember)
                session.getAttribute (MEMBER_KEY);
        final Date date = getDateFromString (context.getParameter
                                             (TERMINATIONDATE_KEY));
        final Table table = new Table ();
        if (null == date) {
            final Text text = new Text (localize (WRONGDATEFORMAT_KEY,
                                                  WRONGDATEFORMAT_DEFAULT));
            text.setFontColor ("#ff0000");
            table.add (text, 1, 1);
        } else {
            // terminate membership
            member.setNotes (context.getParameter (NOTES_KEY));
            member.setRemovedDate (new  Timestamp (date.getTime ()));
            member.store ();
            
            // put confirmation output
            table.add (new Text (localize (MEMBERSHIPOF_KEY,
                                           MEMBERSHIPOF_DEFAULT)
                                 + member.getStudent ().getFirstName ()
                                 + " " + member.getStudent ().getLastName ()
                                 + localize (ISTERMINATED_KEY,
                                             ISTERMINATED_DEFAULT)), 1, 1);
        }
        table.setHeight (2, 12);
        table.add (getSmallLink (localize (BACK_KEY, BACK_DEFAULT)), 1, 3);
        return table;
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
        throws RemoteException {
        // 1. set up searcher
        final UserSearcher searcher = new UserSearcher ();
        searcher.setOwnFormContainer (false);
        searcher.setShowMiddleNameInSearch (false);
        searcher.setLastNameLength (14);
        searcher.setFirstNameLength (14);
        searcher.setPersonalIDLength (12);
        searcher.setMaxFoundUserCols (MAX_FOUND_USER_COLS); 
        searcher.setMaxFoundUserRows (MAX_FOUND_USER_ROWS);

        // 2. do search
        fillSearcherWithStudents (context, searcher);

        // 3. output result
        final Table table = new Table ();
        final User foundUser = searcher.getUser ();
        final Form searchForm = new Form();
        searchForm.setOnSubmit("return checkInfoForm()");
        searchForm.add (searcher);
        table.add (searchForm, 1, 1);
        if (null != foundUser) {
            // exactly one user found - display user info and termination form
            final Table terminateTable = new Table ();
            terminateTable.add (getStudentTable (context, foundUser), 1, 2);
            terminateTable.add (getSubmitButton
                                (ACTION_TERMINATE_KEY, TERMINATEMEMBERSHIP_KEY,
                                 TERMINATEMEMBERSHIP_DEFAULT), 1, 3);
            final Form terminateForm = new Form ();
            terminateForm.add (terminateTable);
            table.add (terminateForm, 1, 1);
        }
        return table;
    }

    /**
     * Retreives students that are currently members of a school class
     *
	 * @param context session data
	 * @param searcher to use for searching
     * @exception RemoteException if exception happens in lower layer
     */
    private void fillSearcherWithStudents (final IWContext context,
                                           final UserSearcher searcher)
        throws RemoteException {
        final Collection students = new ArrayList ();
        try {
            // 1. start with a raw search
            searcher.process (context);
            final Collection usersFound = searcher.getUsersFound ();
            if (null == usersFound) {
                throw new FinderException ();
            }

            // 2. filter out students that are placed and that the logged on
            // user is authorized to see
            final SchoolCommuneBusiness communeBusiness
                    = (SchoolCommuneBusiness) IBOLookup.getServiceInstance
                    (context, SchoolCommuneBusiness.class);
            final int schoolId = getSchoolID ();
            for (Iterator i = usersFound.iterator (); i.hasNext ();) {
                final User user = (User) i.next ();
                final SchoolClassMember student = schoolId >= 0
                        ? communeBusiness.getCurrentSchoolClassMembership
                        (user, schoolId)
                        : communeBusiness.getCurrentSchoolClassMembership
                        (user);
                if (null != student && null == student.getRemovedDate ()) {
                    if (MAX_FOUND_USERS <= students.size ()) {
                        // too many students found
                        displayRedText (TOOMANYSTUDENTSFOUND_KEY,
                                        TOOMANYSTUDENTSFOUND_DEFAULT);
                        throw new FinderException ();
                    }
                    students.add (user);
                }
            }
            if (!usersFound.isEmpty () && students.isEmpty ()) {
                displayRedText (NOUSERFOUND_KEY, NOUSERFOUND_DEFAULT);
            }
        } catch (FinderException e) {
            // no students found or too many students found
        }
        searcher.setUsersFound (students);
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
        (final IWContext context, final User user) throws RemoteException {

        // get business objects
        final SchoolCommuneBusiness communeBusiness = (SchoolCommuneBusiness)
                IBOLookup.getServiceInstance (context,
                                              SchoolCommuneBusiness.class);
        final SchoolClassMember student
                = communeBusiness.getCurrentSchoolClassMembership (user);

        // store student in session
        final HttpSession session = context.getSession ();
        session.setAttribute (MEMBER_KEY, student);

        // create interface objects
        final Text addressHeader = new Text (localize (ADDRESS_KEY,
                                                       ADDRESS_DEFAULT) + ':');
        addressHeader.setBold ();
        final Text address = new Text (getAddressStringFromUser (user));
        final Text memberHeader = new Text
                (localize (CURRENTMEMBERSHIP_KEY, CURRENTMEMBERSHIP_DEFAULT)
                 + ':');
        memberHeader.setBold ();
        final Text member = new Text
                (null != student
                 ? getMembershipString (student, communeBusiness)
                 : "ej placerad");
        final Text terminationDateHeader = new Text
                (localize (TERMINATIONDATE_KEY, TERMINATIONDATE_DEFAULT) + ':');
        terminationDateHeader.setBold ();
        final TextInput terminationDateInput
                = (TextInput) getStyledInterface (new TextInput
                                                  (TERMINATIONDATE_KEY));
        terminationDateInput.setLength (10);
		terminationDateInput.setContent (shortDateFormatter.format
                                         (new Date ()));
        final Text notesHeader = new Text (localize (NOTES_KEY, NOTES_DEFAULT)
                                           + ":");
        notesHeader.setBold ();
        final TextInput notesInput
                = (TextInput) getStyledInterface (new TextInput (NOTES_KEY));
        notesInput.setLength (40);

        // put interface objects in output table
        final Table table = new Table ();
        table.setColumns (2);
        int row = 1;
        table.add (addressHeader, 1, row);
        table.add (address, 2, row++);
        table.add (memberHeader, 1, row);
        table.add (member, 2, row++);
        table.add (terminationDateHeader, 1, row);
        table.add (terminationDateInput, 2, row++);
        table.add (notesHeader, 1, row);
        table.add (notesInput, 2, row++);

        return table;
    }

	/**
	 * Gets address information for this user
	 *
	 * @param user the person to get info about
     * @return displayable string with address info
	 */
    private static String getAddressStringFromUser (final User user) {
        String result = "not known";
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
            final Integer yearId = new Integer (schoolClass.getSchoolYearId ());
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
        return calendar.getTime ();
    }

    private SubmitButton getSubmitButton (final String action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (action, getLocalizedString
                                          (key, defaultName)));
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
}
