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
 * Last modified: $Date: 2003/08/27 13:28:31 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 * @see com.idega.block.school.data.SchoolClassMember
 * @see se.idega.idegaweb.commune.school.businessSchoolCommuneBusiness
 * @see javax.ejb
 */
public class InvoiceByCompensationView extends CommuneBlock {


    private static final String INVOICEINTERVAL_DEFAULT = "Fakturaintervall";
    private static final String INVOICEINTERVAL_KEY
        = "CompByInv_invoiceInterval";
    private static final String LATESTINVOICEDATE_DEFAULT
        = "Senaste fakturadatum";
    private static final String LATESTINVOICEDATE_KEY
        = "CompByInv_latestInvoiceDate";
    private static final String NAME_DEFAULT = "Namn";
    private static final String NAME_KEY = "CompByInv_name";
    private static final String PROVIDER_DEFAULT = "Anordnare";
    private static final String PROVIDER_KEY = "CompByInv_provider";
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
            showInvoiceByCompensationList (context);
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
        final SchoolCommuneBusiness communeBusiness = (SchoolCommuneBusiness)
                IBOLookup.getServiceInstance (context,
                                              SchoolCommuneBusiness.class);
        final SchoolClassMember [] members
                = communeBusiness.getCurrentMembersWithInvoiceInterval ();
        final String [][] columnNames =
                {{ SSN_KEY, SSN_DEFAULT }, { NAME_KEY, NAME_DEFAULT },
                 { PROVIDER_KEY, PROVIDER_DEFAULT },
                 { INVOICEINTERVAL_KEY, INVOICEINTERVAL_DEFAULT },
                 { LATESTINVOICEDATE_KEY, LATESTINVOICEDATE_DEFAULT }};
        final Table table = new Table();
        table.setCellpadding(getCellpadding());
        table.setCellspacing(getCellspacing());
        table.setWidth(Table.HUNDRED_PERCENT);
        table.setColumns (columnNames.length);
        table.setRowColor(1, getHeaderColor());
        for (int i = 0; i < columnNames.length; i++) {
            table.add(getSmallHeader(localize (columnNames [i][0], columnNames
                                               [i][1])), i + 1, 1);
        }
        final SchoolBusiness schoolBusiness
                = communeBusiness.getSchoolBusiness ();
        final SchoolClassHome classHome = schoolBusiness.getSchoolClassHome ();
        final SchoolHome schoolHome = schoolBusiness.getSchoolHome ();

        for (int i = 0; i < members.length; i++) {
            int col = 1;
            int row = i + 1;
            table.setRowColor(row, (row % 2 == 0) ? getZebraColor1()
                              : getZebraColor2());
            final SchoolClassMember member = members [i];
            final User student = member.getStudent ();
            final String ssn = student.getPersonalID ();
            final Link ssnLink = getSmallLink (ssn);
            ssnLink.addParameter (SSN_KEY, ssn);
            ssnLink.addParameter (ACTION_KEY, ACTION_SHOWUSER_KEY);
            table.add (ssnLink, col++, row);
            table.add (new Text(student.getName ()), col++, row);
            final String schoolName
                    = getSchoolNameFromMember (member, classHome, schoolHome);
            table.add (new Text(schoolName), col++, row);
            table.add (new Text(member.getInvoiceInterval ()), col++, row);
            table.add (new Text(dateFormatter.format
                                (member.getLatestInvoiceDate ())), col++, row);
        }
        add (table);
    }

	/**
	 * Returns name of the school that the school class member is member of
	 *
	 * @param member The person that is member of a school class
	 * @param classHome home object of school class beans
	 * @param schoolHome home object of school beans
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
