package se.idega.idegaweb.commune.school.presentation;

import com.idega.block.school.business.*;
import com.idega.block.school.data.*;
import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.data.User;
import is.idega.idegaweb.member.presentation.UserEditor;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.FinderException;
import javax.servlet.http.HttpSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

public class TerminateClassMembership extends UserEditor {
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
    private static final String MEMBER_KEY = PREFIX + "member";
    private static final String MEMBERSHIPOF_DEFAULT = "Placeringen av ";
    private static final String MEMBERSHIPOF_KEY = PREFIX + "membershipOf";
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

    private static final String ACTION_KEY = PREFIX + "action";
    private static final String ACTION_TERMINATE_KEY
        = PREFIX + "terminateMembership";

    private static final SimpleDateFormat shortDateFormatter
        = new SimpleDateFormat ("yyyyMMdd");

	public TerminateClassMembership () {
		super();
		setShowMiddleNameInSearch (false);
        setShowUserRelations (false);
		setLastNameLength (14);
		setFirstNameLength (14);
		setPersonalIDLength (12);
		setBundleIdentifer (CommuneBlock.IW_BUNDLE_IDENTIFIER);
	}

	/**
	 * Main is the event handler of InvoiceByCompensationForm.
	 *
	 * @param context session data like user info etc.
	 */
	public void main(final IWContext context) {

        try {
            if (context.isParameterSet (ACTION_KEY)
                && ACTION_TERMINATE_KEY.equals (context.getParameter
                                               (ACTION_KEY))) {
                terminateMembership (context);
            } else {
                super.main (context);
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
	 * Terminates membership in a school class for the user kept in session
     * object MEMBER_KEY
	 *
	 * @param context session data
	 */
    private void terminateMembership (final IWContext context) {
        // find input values
        final HttpSession session = context.getSession ();
        final SchoolClassMember member = (SchoolClassMember)
                session.getAttribute (MEMBER_KEY);
        final Date date = getDateFromString (context.getParameter
                                             (TERMINATIONDATE_KEY));
        final Timestamp timestamp = new  Timestamp (date.getTime ()); 

        // terminate membership
        member.setNotes (context.getParameter (NOTES_KEY));
        member.setRemovedDate (timestamp);
        member.store ();

        // put confirmation output
        final Table table = new Table ();
        table.add (new Text (localize (MEMBERSHIPOF_KEY, MEMBERSHIPOF_DEFAULT)
                             + member.getStudent ().getFirstName ()
                             + " " + member.getStudent ().getLastName ()
                             + localize (ISTERMINATED_KEY,
                                         ISTERMINATED_DEFAULT)), 1, 1);
		table.setHeight (2, 12);
        table.add (getSmallLink (localize (BACK_KEY, BACK_DEFAULT)), 1, 3);
        add (table);
    }

	/**
	 * Output info about current school class membership for this user
	 *
	 * @param context session data
     * @exception RemoteException if something fails in business layer
	 */
	protected void presentateUserInfo (final IWContext context)
        throws RemoteException {

        // create interface objects
        final Text addressHeader = new Text (localize (ADDRESS_KEY,
                                                       ADDRESS_DEFAULT) + ":");
        addressHeader.setBold ();
        final Text address = new Text (getAddressStringFromUser (user));
        final SchoolCommuneBusiness communeBusiness = (SchoolCommuneBusiness)
                IBOLookup.getServiceInstance (context,
                                              SchoolCommuneBusiness.class);
        final SchoolClassMember student
                = communeBusiness.getCurrentSchoolClassMembership (user);
        final Text memberHeader = new Text
                (localize (CURRENTMEMBERSHIP_KEY, CURRENTMEMBERSHIP_DEFAULT));
        memberHeader.setBold ();
        final Text member = new Text
                (null != student
                 ? getMembershipString (student, communeBusiness)
                 : "ej placerad");
        final Text terminationDateHeader = new Text
                (localize (TERMINATIONDATE_KEY, TERMINATIONDATE_DEFAULT) + ":");
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
		addToMainPart (table);

        // store student in session
        final HttpSession session = context.getSession ();
        session.setAttribute (MEMBER_KEY, student);
	}
	
	protected void presentateButtonRegister (IWContext dummy) {
        // nothing
    }

	/**
	 * Implementation of polymorf method for stating teh action in a button
	 *
	 * @param dummy not used
	 */
    protected void presentateButtonSave (IWContext dummy) {
        addButton (getButton (new SubmitButton
                              (localize (TERMINATEMEMBERSHIP_KEY,
                                         TERMINATEMEMBERSHIP_DEFAULT),
                               ACTION_KEY, ACTION_TERMINATE_KEY)));
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
            return "Skola ej funnen i databas";
        }
    }

    private static Date getDateFromString (final String rawInput) {
        final StringBuffer digitOnlyInput = new StringBuffer();
        for (int i = 0; i < rawInput.length(); i++) {
            if (Character.isDigit(rawInput.charAt(i))) {
                digitOnlyInput.append(rawInput.charAt(i));
            }
        }
        final Calendar rightNow = Calendar.getInstance();
        final int currentYear = rightNow.get(Calendar.YEAR);
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

	private String localize (final String textKey, final String defaultText) {
		return (iwrb == null) ? defaultText
                : iwrb.getLocalizedString (textKey, defaultText);
	}

	private GenericButton getButton (final GenericButton button) {
		button.setHeight ("20");
		return (GenericButton) setStyle
                (button, CommuneBlock.STYLENAME_INTERFACE_BUTTON);
	}
		
	public Link getSmallLink (final String link) {
		return getStyleLink (new Link (link),
                             CommuneBlock.STYLENAME_SMALL_LINK);
	}

    private InterfaceObject getStyledInterface (final InterfaceObject obj) {
		return (InterfaceObject) setStyle (obj,
                                           CommuneBlock.STYLENAME_INTERFACE);
	}
}
