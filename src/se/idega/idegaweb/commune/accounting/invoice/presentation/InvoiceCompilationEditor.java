package se.idega.idegaweb.commune.accounting.invoice.presentation;

import com.idega.presentation.*;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import is.idega.idegaweb.member.presentation.UserSearcher;
import java.rmi.RemoteException;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.presentation.*;

/**
 * InvoiceCompilationEditor is an IdegaWeb block were the user can search, view
 * and edit invoice compilations.
 * <p>
 * <b>English - Swedish mini lexicon:</b><br/>
 * Invoice compilation = Faktureringsunderlag<br/>
 * <p>
 * Last modified: $Date: 2003/10/27 16:15:33 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
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
			final int action = parseAction (context);
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
	
	private int parseAction (IWContext context) {
        return ACTION_SHOW_COMPILATION_LIST;
	}	

    /**
     * Shows one invoice compilation.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilation (final IWContext context) {
        throw new UnsupportedOperationException ("n.i.y.");
    }

    /**
     * Shows a list of invoice compilations and a search form.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilationList (final IWContext context)
        throws RemoteException {
        add (createMainTable (new Text ("content goes here...")));
        throw new UnsupportedOperationException ("n.i.y.");
    }

	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
     * @return Table to add to output
	 */
    private Table createMainTable (final PresentationObject content)
        throws RemoteException {
        final Table mainTable = new Table();
        mainTable.setCellpadding (getCellpadding ());
        mainTable.setCellspacing (getCellspacing ());
        mainTable.setWidth (Table.HUNDRED_PERCENT);
        mainTable.setColumns (1);
        mainTable.setRowColor (1, getHeaderColor ());
        mainTable.setRowAlignment(1, Table.HORIZONTAL_ALIGN_CENTER) ;
        
        mainTable.add (getSmallHeader
                       (localize (INVOICE_COMPILATION_KEY,
                                  INVOICE_COMPILATION_DEFAULT)),
                       1, 1);
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
