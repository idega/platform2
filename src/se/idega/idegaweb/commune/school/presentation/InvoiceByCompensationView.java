package se.idega.idegaweb.commune.school.presentation;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.*;
import com.idega.business.IBOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
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
 * Last modified: $Date: 2003/08/28 12:59:32 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 * @see com.idega.block.school.data.SchoolClassMember
 * @see se.idega.idegaweb.commune.school.businessSchoolCommuneBusiness
 * @see javax.ejb
 */
public class InvoiceByCompensationView extends CommuneBlock {

    private static final String COMPENSATIONBYINVOICE_DEFAULT
        = "Ersättning mot faktura";
    private static final String COMPENSATIONBYINVOICE_KEY
        = "CompByInv_compensationByInvoice";
    private static final String INVOICEINTERVAL_DEFAULT
        = "Fakturaintervall";
    private static final String INVOICEINTERVAL_KEY
        = "CompByInv_invoiceInterval";
    private static final String LATESTINVOICEDATE_DEFAULT
        = "Senaste fakturadatum";
    private static final String LATESTINVOICEDATE_KEY
        = "CompByInv_latestInvoiceDate";
    private static final String MAINACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAINACTIVITY_KEY = "CompByInv_mainActivity";
    private static final String MEMBERID_KEY = "CompByInv_memberId";
    private static final String NAME_DEFAULT = "Namn";
    private static final String NAME_KEY = "CompByInv_name";
    private static final String PROVIDER_DEFAULT = "Anordnare";
    private static final String PROVIDER_KEY = "CompByInv_provider";
    private static final String SCHOOL_DEFAULT = "Skola";
    private static final String SCHOOL_KEY = "CompByInv_school";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = "CompByInv_ssn";

    private static final String ACTION_KEY = "CompByInv_action";
    private static final String ACTION_SHOWUSER_KEY
        = "CompByInv_actionShowUser";

    private static final SimpleDateFormat dateFormatter
        = new SimpleDateFormat ("yyyy-MM-dd");

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
            ssnLink.addParameter (MEMBERID_KEY, memberId.toString ());
            ssnLink.addParameter (ACTION_KEY, ACTION_SHOWUSER_KEY);
            memberTable.add (ssnLink, col++, row);
            final String studentName = student.getFirstName () + " "
                    + student.getLastName ();
            memberTable.add (new Text(studentName), col++, row);
            final String schoolName
                    = getSchoolNameFromMember (member, classHome, schoolHome);
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
        final Integer memberId
                = new Integer (context.getParameter (MEMBERID_KEY));

        // get some business objects
        final SchoolCommuneBusiness communeBusiness = (SchoolCommuneBusiness)
                IBOLookup.getServiceInstance (context,
                                              SchoolCommuneBusiness.class);
        final SchoolBusiness schoolBusiness
                = communeBusiness.getSchoolBusiness ();
        final SchoolClassMemberHome memberHome
                = schoolBusiness.getSchoolClassMemberHome ();

        final SchoolClassMember member = memberHome.findByPrimaryKey (memberId);

        add (createMainTable (new Text (member.getClassMemberId ()
                                        + "...more to come here")));
    }

	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
     * @return Table to add to output
	 */
    private Table createMainTable (final PresentationObject content) {
        final Table mainTable = new Table();
        mainTable.setCellpadding(getCellpadding());
        mainTable.setCellspacing(getCellspacing());
        mainTable.setWidth(Table.HUNDRED_PERCENT);
        mainTable.setColumns (1);
        mainTable.setRowColor(1, getHeaderColor());
        mainTable.setRowAlignment(1, Table.HORIZONTAL_ALIGN_CENTER) ;

        mainTable.add(getSmallHeader(localize (COMPENSATIONBYINVOICE_KEY,
                                               COMPENSATIONBYINVOICE_DEFAULT)),
                      1, 1);
        final Table innerTable = new Table ();
        innerTable.setColumns (2);
        innerTable.add (getSmallHeader(localize (MAINACTIVITY_KEY,
                                                 MAINACTIVITY_DEFAULT) + ":"),
                        1, 1);
        innerTable.add (new Text(localize (SCHOOL_KEY, SCHOOL_DEFAULT)), 2, 1);
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

	private String getLocalizedString (final String key, final String value) {
		return getResourceBundle ().getLocalizedString (key, value);
	}
}
