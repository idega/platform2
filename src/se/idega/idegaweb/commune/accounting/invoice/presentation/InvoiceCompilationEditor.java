package se.idega.idegaweb.commune.accounting.invoice.presentation;

import com.idega.business.IBOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.*;
import com.idega.user.data.User;
import is.idega.idegaweb.member.presentation.UserSearcher;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.presentation.*;

/**
 * InvoiceCompilationEditor is an IdegaWeb block were the user can search, view
 * and edit invoice compilations.
 * <p>
 * <b>English - Swedish mini lexicon:</b><br/>
 * Invoice compilation = Faktureringsunderlag<br/>
 * <p>
 * Last modified: $Date: 2003/10/29 09:23:12 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
 * @see com.idega.presentation.IWContext
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.data
 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock
 */
public class InvoiceCompilationEditor extends AccountingBlock {
    private static final String PREFIX = "cacc_invcmp_";
    
    private static final String INVOICE_COMPILATION_DEFAULT = "Faktureringsunderlag";
    private static final String INVOICE_COMPILATION_KEY = PREFIX + "invoice_compilation";
    private static final String INVOICE_COMPILATION_LIST_DEFAULT = "Faktureringsunderlagslista";
    private static final String INVOICE_COMPILATION_LIST_KEY = PREFIX + "invoice_compilation_list";
    private static final String MAIN_ACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAIN_ACTIVITY_KEY = PREFIX + "main_activity";
    private static final String SEARCH_DEFAULT = "Sök";
    private static final String SEARCH_KEY = PREFIX + "search";

    //private static final String USERSEARCHER_FIRSTNAME_KEY = "usrch_search_fname" + PREFIX;
    //private static final String USERSEARCHER_LASTNAME_KEY = "usrch_search_lname" + PREFIX;
    private static final String USERSEARCHER_ACTION_KEY = "mbe_act_search" + PREFIX;
    private static final String USERSEARCHER_PERSONALID_KEY = "usrch_search_pid" + PREFIX;

	private static final int ACTION_SHOW_COMPILATION = 0,
            ACTION_SHOW_COMPILATION_LIST = 1;

	/**
	 * Init is the event handler of InvoiceCompilationEditor
	 *
	 * @param context session data like user info etc.
	 */
	public void init (final IWContext context) {
        //		setResourceBundle (getResourceBundle(context));
        
		try {
			final int action = parseAction ();
			switch (action) {
				case ACTION_SHOW_COMPILATION:
					showCompilation (context);
					break;
                    
                default:
                    showCompilationList (context);
					break;					
			}
		} catch (Exception exception) {
            logUnexpectedException (context, exception);
		}
	}
	
	private int parseAction () {
        return ACTION_SHOW_COMPILATION_LIST;
	}	

    /**
     * Shows one invoice compilation.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilation (final IWContext context)
        throws RemoteException {
        add (createMainTable (INVOICE_COMPILATION_KEY,
                              INVOICE_COMPILATION_DEFAULT, new Table ()));
        throw new UnsupportedOperationException ("not implemnted yet ["
                                                 + context + ']');
    }

    /**
     * Shows a list of invoice compilations and a search form.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilationList (final IWContext context)
        throws RemoteException {
        final UserSearcher searcher = createSearcher ();
        try {
            searcher.process (context);
        } catch (final FinderException dummy) {
            // do nothing, it's ok that none was found
        }
        final boolean exactlyOneUserFound = null != searcher.getUser ();
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        final Table table = new Table ();
        form.add (table);
        form.add (new HiddenInput (USERSEARCHER_ACTION_KEY));
        int row = 1;
        table.add (new TextInput (USERSEARCHER_PERSONALID_KEY), 1, row++);
        table.add (getSubmitButton (ACTION_SHOW_COMPILATION_LIST + "",
                                    SEARCH_KEY, SEARCH_DEFAULT), 1, row++);
        table.add (searcher, 1, row++);
        if (exactlyOneUserFound) {
            // exactly one user found - display users invoice compilation list
            final User userFound = searcher.getUser ();
            final InvoiceBusiness business = (InvoiceBusiness)
                    IBOLookup.getServiceInstance (context,
                                                  InvoiceBusiness.class);
            final InvoiceHeader [] invoiceHeaders
                    = business.getInvoiceHeadersByCustodianOrChild (userFound);

            if (0 < invoiceHeaders.length) {
                table.add (new Text ("list goes here..."), 1, row++);
            } else {
                table.add (new Text ("no compilations found for this custodian"
                                     + " or child!"), 1, row++);
            }
        }
        final Table outerTable = new Table ();
        outerTable.add (form, 1, 1);
        add (createMainTable (INVOICE_COMPILATION_LIST_KEY,
                              INVOICE_COMPILATION_LIST_DEFAULT,  outerTable));
        throw new UnsupportedOperationException ("not implemented yet");
    }

	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
     * @return Table to add to output
	 */
    private Table createMainTable
                (final String headerKey, final String headerDefault,
                 final PresentationObject content) throws RemoteException {
        final Table mainTable = new Table();
        mainTable.setCellpadding (getCellpadding ());
        mainTable.setCellspacing (getCellspacing ());
        mainTable.setWidth (Table.HUNDRED_PERCENT);
        mainTable.setColumns (1);
        mainTable.setRowColor (1, getHeaderColor ());
        mainTable.setRowAlignment(1, Table.HORIZONTAL_ALIGN_CENTER) ;
        mainTable.add (getSmallHeader (localize (headerKey, headerDefault)), 1,
                       1);
        final Table innerTable = new Table ();
        innerTable.setColumns (2);
        innerTable.add (getSmallHeader (localize (MAIN_ACTIVITY_KEY,
                                                  MAIN_ACTIVITY_DEFAULT) + ":"),
                        1, 1);
        String operationalField = getSession ().getOperationalField();
        operationalField = operationalField == null ? "" : operationalField;
        innerTable.add (new OperationalFieldsMenu (), 2, 1);
        mainTable.add (innerTable, 1, 2);
        mainTable.add (content, 1, 3);
        return mainTable;
    }
    

    private SubmitButton getSubmitButton (final String action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (action, localize (key, defaultName)));
    }

    private UserSearcher createSearcher () {
        final UserSearcher searcher = new UserSearcher ();
        searcher.setOwnFormContainer (false);
        searcher.setShowFirstNameInSearch (false);
        searcher.setShowMiddleNameInSearch (false);
        searcher.setShowLastNameInSearch (false);
        searcher.setShowPersonalIDInSearch (false);
        searcher.setShowButtons (false);
        searcher.setUniqueIdentifier (PREFIX);
        searcher.setHeaderFontStyleName (getSmallHeaderFontStyle ());
        return searcher;
    }

    private void logUnexpectedException (final IWContext context,
                                         final Exception exception) {
        System.err.println ("Exception caught in " + getClass ().getName ()
                            + " " + (new java.util.Date ()));
        System.err.println ("Parameters:");
        final java.util.Enumeration enum = context.getParameterNames ();
        while (enum.hasMoreElements ()) {
            final String key = (String) enum.nextElement ();
            System.err.println ('\t' + key + "='"
                                + context.getParameter (key) + "'");
        }
        exception.printStackTrace ();
        add ("Det inträffade ett fel. Försök igen senare.");
    }
}
