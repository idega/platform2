package se.idega.idegaweb.commune.school.presentation;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.*;
import com.idega.business.IBOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

/**
 * InvoiceByCompensationView is an IdegaWeb block were the user can view and
 * edit the factoring by compensation field of school members in the current
 * season.
 * <p>
 * Last modified: $Date: 2003/09/02 15:58:07 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
 * @see com.idega.block.school.data.SchoolClassMember
 * @see se.idega.idegaweb.commune.school.businessSchoolCommuneBusiness
 * @see javax.ejb
 */
public class InvoiceByCompensationView extends CommuneBlock {
    private static final String PREFIX = "CompByInv_";

    private static final String BACK_DEFAULT = "Tillbaka";
    private static final String BACK_KEY = PREFIX + "back";
    private static final String CANCEL_DEFAULT = "Avbryt";
    private static final String CANCEL_KEY = PREFIX + "cancel";
    private static final String COMPENSATIONBYINVOICE_DEFAULT
        = "Ersättning mot faktura";
    private static final String COMPENSATIONBYINVOICE_KEY
        = PREFIX + "compensationByInvoice";
    private static final String INVOICEINTERVAL_DEFAULT = "Fakturaintervall";
    private static final String INVOICEINTERVAL_KEY
        = PREFIX + "invoiceInterval";
    private static final String ISUPDATED_DEFAULT = " är nu ändrad till ";
    private static final String ISUPDATED_KEY = PREFIX + "isUpdated";
    private static final String LATESTINVOICEDATE_DEFAULT
        = "Senaste fakturadatum";
    private static final String LATESTINVOICEDATE_KEY
        = PREFIX + "latestInvoiceDate";
    private static final String MAINACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAINACTIVITY_KEY = PREFIX + "mainActivity";
    private static final String MEMBERID_KEY = PREFIX + "memberId";
    private static final String NAME_DEFAULT = "Namn";
    private static final String NAME_KEY = PREFIX + "name";
    private static final String PROVIDER_DEFAULT = "Anordnare";
    private static final String PROVIDER_KEY = PREFIX + "provider";
    private static final String SAVE_DEFAULT = "Spara";
    private static final String SAVE_KEY = PREFIX + "save";
    private static final String SCHOOL_DEFAULT = "Skola";
    private static final String SCHOOL_KEY = PREFIX + "school";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = PREFIX + "ssn";
    private static final String WRONGDATEFORMAT_DEFAULT
        = "Felaktigt datumformat";
    private static final String WRONGDATEFORMAT_KEY
        = PREFIX + "wrongDateFormat";

    private static final String ACTION_KEY = PREFIX + "action";
    private static final String ACTION_SAVE_KEY = PREFIX + "actionSave";
    private static final String ACTION_SHOWUSER_KEY = PREFIX + "actionShowUser";

    private static final SimpleDateFormat dateFormatter
        = new SimpleDateFormat ("yyyy-MM-dd");
    private static final SimpleDateFormat shortDateFormatter
        = new SimpleDateFormat ("yyyyMMdd");

	/**
	 * Main is the event handler of InvoiceByCompensationForm.
	 *
	 * @param context session data like user info etc.
	 */
	public void main(final IWContext context) {
		setResourceBundle (getResourceBundle(context));

		try {
            if (context.isParameterSet (ACTION_KEY)
                && ACTION_SHOWUSER_KEY.equals (context.getParameter
                                               (ACTION_KEY))) {
                showUser (context);
            } else if (context.isParameterSet (ACTION_KEY)
                && ACTION_SAVE_KEY.equals (context.getParameter (ACTION_KEY))) {
                updateUser (context);
            } else {
                showInvoiceByCompensationList (context);
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
	 * Displays table with all school members that have invoice interval is set
	 *
	 * @param context session data like user info etc.
     * @exception FinderException if the school class wasn't found
     * @exception RemoteException if something fails in business layer
	 */
    private void showInvoiceByCompensationList (final IWContext context)
        throws RemoteException, FinderException {

        // set up header row
        final String [][] columnNames =
                {{ SSN_KEY, SSN_DEFAULT }, { NAME_KEY, NAME_DEFAULT },
                 { PROVIDER_KEY, PROVIDER_DEFAULT },
                 { INVOICEINTERVAL_KEY, INVOICEINTERVAL_DEFAULT },
                 { LATESTINVOICEDATE_KEY, LATESTINVOICEDATE_DEFAULT }};
        final Table memberTable = new Table();
        memberTable.setCellpadding(getCellpadding());
        memberTable.setCellspacing(getCellspacing());
        memberTable.setWidth(Table.HUNDRED_PERCENT);
        memberTable.setColumns (columnNames.length);
        memberTable.setRowColor(1, getHeaderColor());
        for (int i = 0; i < columnNames.length; i++) {
            memberTable.add(getSmallHeader(localize (columnNames [i][0],
                                                     columnNames [i][1])),
                            i + 1, 1);
        }

        // get some business objects
        final SchoolCommuneBusiness communeBusiness = (SchoolCommuneBusiness)
                IBOLookup.getServiceInstance (context,
                                              SchoolCommuneBusiness.class);
        final SchoolBusiness schoolBusiness
                = communeBusiness.getSchoolBusiness ();
        final SchoolClassHome classHome = schoolBusiness.getSchoolClassHome ();
        final SchoolHome schoolHome = schoolBusiness.getSchoolHome ();

        // search the database for members to display
        final SchoolClassMember [] members
                = communeBusiness.getCurrentMembersWithInvoiceInterval ();

        // display each member
        for (int i = 0; i < members.length; i++) {
            int col = 1;
            int row = i + 2;
            memberTable.setRowColor(row, (row % 2 == 0) ? getZebraColor1()
                              : getZebraColor2());
            final SchoolClassMember member = members [i];
            final User student = member.getStudent ();
            final String ssn = student.getPersonalID ();
            final Link ssnLink = getSmallLink (ssn);
            final Object memberId = member.getPrimaryKey ();
            final String studentName = student.getFirstName () + " "
                    + student.getLastName ();
            final String schoolName
                    = getSchoolNameFromMember (member, classHome, schoolHome);
            ssnLink.addParameter (ACTION_KEY, ACTION_SHOWUSER_KEY);
            ssnLink.addParameter (MEMBERID_KEY, memberId.toString ());
            ssnLink.addParameter (SSN_KEY, ssn);
            ssnLink.addParameter (NAME_KEY, studentName);
            ssnLink.addParameter (SCHOOL_KEY, schoolName);
            memberTable.add (ssnLink, col++, row);
            memberTable.add (new Text(studentName), col++, row);
            memberTable.add (new Text(schoolName), col++, row);
            memberTable.add (new Text(member.getInvoiceInterval ()), col++,
                             row);
            final Date latestInvoiceDate = member.getLatestInvoiceDate ();
            if (null != latestInvoiceDate) {
                memberTable.add (new Text(dateFormatter.format
                                          (latestInvoiceDate)), col++, row);
            }
        }

        // add to output
        add (createMainTable (memberTable));
    }

	/**
	 * Displays user info form, where latest invoice date can be modified
	 *
	 * @param context session data like user info etc.
     * @exception FinderException if the school class member wasn't found
     * @exception RemoteException if something fails in business layer
	 */
    private void showUser (final IWContext context) throws RemoteException,
                                                           FinderException {
        // get some business objects
        final SchoolCommuneBusiness communeBusiness = (SchoolCommuneBusiness)
                IBOLookup.getServiceInstance (context,
                                              SchoolCommuneBusiness.class);
        final SchoolBusiness schoolBusiness
                = communeBusiness.getSchoolBusiness ();
        final SchoolClassMemberHome memberHome
                = schoolBusiness.getSchoolClassMemberHome ();

        // get student info
        final Integer memberId
                = new Integer (context.getParameter (MEMBERID_KEY));
        final SchoolClassMember member = memberHome.findByPrimaryKey (memberId);
        final String ssn = context.getParameter (SSN_KEY);
        final String studentName = context.getParameter (NAME_KEY);
        final String schoolName = context.getParameter (SCHOOL_KEY);
        final Date latestInvoiceDate = member.getLatestInvoiceDate ();

        // display student info
        final Table studentTable = new Table ();
        studentTable.setCellpadding (getCellpadding ());
        studentTable.setCellspacing (getCellspacing ());
        studentTable.setColumns (2);
        final String [][] cells =
                {{ SSN_KEY, SSN_DEFAULT,  ssn },
                 { NAME_KEY, NAME_DEFAULT, studentName },
                 { PROVIDER_KEY, PROVIDER_DEFAULT, schoolName },
                 { INVOICEINTERVAL_KEY, INVOICEINTERVAL_DEFAULT,
                   member.getInvoiceInterval () }};
        final TextInput textInput = (TextInput) getStyledInterface
                (new TextInput (LATESTINVOICEDATE_KEY));
        textInput.setLength (10);
		textInput.setContent (null == latestInvoiceDate
                              ? shortDateFormatter.format (new Date ())
                              : shortDateFormatter.format (latestInvoiceDate));
        int row = 1;
        for (int i = 0; i < cells.length; i++) {
            studentTable.add (getSmallHeader (localize (cells [i][0],
                                                        cells [i][1]) + ":"), 1,
                              row);
            studentTable.add(new Text (cells [i][2]), 2, row++);
        }
        studentTable.add (getSmallHeader (localize (LATESTINVOICEDATE_KEY,
                                                    LATESTINVOICEDATE_DEFAULT)
                                          + ":"), 1, row);
        studentTable.add (textInput, 2, row++);
        
        // display buttons
        final Table  buttonTable = new Table ();
        buttonTable.setCellpadding (getCellpadding ());
        buttonTable.setCellspacing (getCellspacing ());
        buttonTable.setColumns (2);
        buttonTable.add (getButton (new SubmitButton
                                    (localize (SAVE_KEY, SAVE_DEFAULT),
                                     ACTION_KEY, ACTION_SAVE_KEY)), 1, 1);
		buttonTable.add (getButton (new SubmitButton (CANCEL_KEY, localize
                                                      (CANCEL_KEY,
                                                       CANCEL_DEFAULT))), 2, 1);

        // put output together and publish
        final Table mainTable = new Table ();
        mainTable.setCellpadding (getCellpadding ());
        mainTable.setCellspacing (getCellspacing ());
        mainTable.add (studentTable, 1, 1);
        mainTable.add (buttonTable, 1, 2);
        final Form form = new Form ();
        form.add (mainTable);
        form.maintainParameter (MEMBERID_KEY);
        final Table formTable = new Table ();
        formTable.add (form);
        add (createMainTable (formTable));
    }


	/**
	 * Updates latest invoice date
	 *
	 * @param context session data like user info etc.
     * @exception FinderException if the school class member wasn't found
     * @exception RemoteException if something fails in business layer
	 */
    private void updateUser (final IWContext context) throws RemoteException,
                                                             FinderException   {
        // get some business objects
        final SchoolCommuneBusiness communeBusiness = (SchoolCommuneBusiness)
                IBOLookup.getServiceInstance (context,
                                              SchoolCommuneBusiness.class);
        final SchoolBusiness schoolBusiness
                = communeBusiness.getSchoolBusiness ();
        final SchoolClassMemberHome memberHome
                = schoolBusiness.getSchoolClassMemberHome ();

        // get student info
        final Integer memberId
                = new Integer (context.getParameter (MEMBERID_KEY));
        final SchoolClassMember member = memberHome.findByPrimaryKey (memberId);
        final String latestInvoiceDate
                = context.getParameter (LATESTINVOICEDATE_KEY);
        final Date date = getDateFromString (latestInvoiceDate);

        if (null != date) {
            member.setLatestInvoiceDate (new java.sql.Timestamp
                                         (date.getTime ()));
            member.store ();
        }
        // display output
        final Table table = new Table ();
        table.setCellpadding (getCellpadding ());
        table.setCellspacing (getCellspacing ());

        if (null != date) {
            table.add (new Text (localize (LATESTINVOICEDATE_KEY,
                                           LATESTINVOICEDATE_DEFAULT) + 
                                 localize (ISUPDATED_KEY, ISUPDATED_DEFAULT)
                                 +  dateFormatter.format (date)), 1, 1);
        } else {
            final Text text = new Text (localize (WRONGDATEFORMAT_KEY,
                                                  WRONGDATEFORMAT_DEFAULT));
            text.setFontColor ("#ff0000");
            table.add (text, 1, 1);
        }
		table.setHeight (2, 12);
        table.add (getSmallLink (localize (BACK_KEY, BACK_DEFAULT)), 1, 3);
        add (createMainTable (table));
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

        mainTable.add (getSmallHeader (localize (COMPENSATIONBYINVOICE_KEY,
                                                COMPENSATIONBYINVOICE_DEFAULT)),
                      1, 1);
        final Table innerTable = new Table ();
        innerTable.setColumns (2);
        innerTable.add (getSmallHeader (localize (MAINACTIVITY_KEY,
                                                  MAINACTIVITY_DEFAULT) + ":"),
                        1, 1);
        innerTable.add (new Text (localize (SCHOOL_KEY, SCHOOL_DEFAULT)), 2, 1);
        mainTable.add (innerTable, 1, 2);
        mainTable.add (content, 1, 3);
        return mainTable;
    }

	/**
	 * Returns name of the school that the school class member is member of
	 *
	 * @param member The person that is member of a school class
	 * @param classHome home object of school class beans
	 * @param schoolHome home object of school beans
     * @return String with school name
     * @exception FinderException if the school class wasn't found
	 */
    static private String getSchoolNameFromMember
        (final SchoolClassMember member, final SchoolClassHome classHome,
         final SchoolHome schoolHome) throws FinderException {
        final Integer classId = new Integer (member.getSchoolClassId ());
        final SchoolClass schoolClass = classHome.findByPrimaryKey(classId);
        final Integer schoolId = new Integer (schoolClass.getSchoolId ());
        final School school = schoolHome.findByPrimaryKey (schoolId);
        return school.getName ();
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

	private String getLocalizedString (final String key, final String value) {
		return getResourceBundle ().getLocalizedString (key, value);
	}
}
