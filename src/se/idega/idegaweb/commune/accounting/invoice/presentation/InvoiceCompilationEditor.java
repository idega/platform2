package se.idega.idegaweb.commune.accounting.invoice.presentation;

import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;
import is.idega.idegaweb.member.presentation.UserSearcher;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.*;
import se.idega.idegaweb.commune.accounting.presentation.*;

/**
 * InvoiceCompilationEditor is an IdegaWeb block were the user can search, view
 * and edit invoice compilations.
 * <p>
 * <b>English - Swedish mini lexicon:</b>
 * <ul>
 * <li>Invoice compilation = Faktureringsunderlag
 * <li>Invoice Header = Fakturahuvud
 * <li>Invoice Record = Fakturrad
 * <li>Amount = Belopp exklusive moms
 * <li>Amount VAT = Momsbelopp i kronor
 * </ul>
 * <p>
 * Last modified: $Date: 2003/11/04 09:15:17 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.14 $
 * @see com.idega.presentation.IWContext
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.data
 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock
 */
public class InvoiceCompilationEditor extends AccountingBlock {
    private static final String PREFIX = "cacc_invcmp_";

    private static final String ADJUSTMENT_DATE_DEFAULT = "Justeringsdag";
    private static final String ADJUSTMENT_DATE_KEY = PREFIX + "adjustment_date";
    private static final String AMOUNT_DEFAULT = "Belopp";
    private static final String AMOUNT_KEY = PREFIX + "amount";
    private static final String CREATION_DATE_DEFAULT = "Skapandedag";
    private static final String CREATION_DATE_KEY = PREFIX + "creation_date";
    private static final String DELETE_INVOICE_COMPILATION_DEFAULT = "Ta bort fakturaunderlag";
    private static final String DELETE_INVOICE_COMPILATION_KEY = PREFIX + "delete_invoice_compilation";
    private static final String DOUBLE_POSTING_DEFAULT = "Motkontering";
    private static final String DOUBLE_POSTING_KEY = PREFIX + "double_posting";
    private static final String EDIT_INVOICE_COMPILATION_DEFAULT = "Ändra fakturaunderlag";
    private static final String EDIT_INVOICE_COMPILATION_KEY = PREFIX + "edit_invoice_compilation";
    private static final String FIRST_NAME_DEFAULT = "Förnamn";
    private static final String FIRST_NAME_KEY = PREFIX + "first_name";
    private static final String FROM_PERIOD_KEY = PREFIX + "from_period";
    private static final String INVOICE_ADDRESS_DEFAULT = "Faktureringsadress";
    private static final String INVOICE_ADDRESS_KEY = PREFIX + "invoice_address";
    private static final String INVOICE_COMPILATION_DEFAULT = "Faktureringsunderlag";
    private static final String INVOICE_COMPILATION_KEY = PREFIX + "invoice_compilation";
    private static final String INVOICE_COMPILATION_LIST_DEFAULT = "Faktureringsunderlagslista";
    private static final String INVOICE_COMPILATION_LIST_KEY = PREFIX + "invoice_compilation_list";
    private static final String INVOICE_RECEIVER_DEFAULT = "Fakturamottagare";
    private static final String INVOICE_RECEIVER_KEY = PREFIX + "invoice_receiver";
    private static final String INVOICE_TEXT_DEFAULT  = "Fakturatext";
    private static final String INVOICE_TEXT_KEY = PREFIX + "invoice_text";
    private static final String JOURNAL_ENTRY_DATE_DEFAULT = "Bokföringsdag";
    private static final String JOURNAL_ENTRY_DATE_KEY = PREFIX + "journal_entry_date";
    private static final String LAST_NAME_DEFAULT = "Efternamn";
    private static final String LAST_NAME_KEY = PREFIX + "last_name";
    private static final String MAIN_ACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAIN_ACTIVITY_KEY = PREFIX + "main_activity";
    private static final String NEW_DEFAULT = "Ny";
    private static final String NEW_KEY = PREFIX + "new";
    private static final String NUMBER_OF_DAYS_DEFAULT = "Antal dagar";
    private static final String NUMBER_OF_DAYS_KEY = PREFIX + "number_of_days";
    private static final String OWN_POSTING_DEFAULT = "Egen kontering";
    private static final String OWN_POSTING_KEY = PREFIX + "own_posting";
    private static final String PERIOD_DEFAULT = "Period";
    private static final String PERIOD_KEY = PREFIX + "period";
    private static final String REMARK_DEFAULT = "Anmärkning";
    private static final String REMARK_KEY = PREFIX + "remark";
    //private static final String REMOVE_DEFAULT = "Ta bort";
    //private static final String REMOVE_KEY = PREFIX + "remove";
    private static final String SEARCH_DEFAULT = "Sök";
    private static final String SEARCH_KEY = PREFIX + "search";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = PREFIX + "personal_id";
    private static final String STATUS_DEFAULT = "Status";
    private static final String STATUS_KEY = PREFIX + "status";
    private static final String TOTAL_AMOUNT_DEFAULT = "Tot.belopp";
    private static final String TOTAL_AMOUNT_KEY = PREFIX + "total_amount";
    private static final String TOTAL_AMOUNT_VAT_DEFAULT = "Totalbelopp moms";
    private static final String TOTAL_AMOUNT_VAT_EXCLUSIVE_DEFAULT = "Totalbelopp, exklusive moms";
    private static final String TOTAL_AMOUNT_VAT_EXCLUSIVE_KEY = PREFIX + "total_amount_vat_exclusive";
    private static final String TOTAL_AMOUNT_VAT_KEY = PREFIX + "total_amount_vat";
    private static final String TO_PERIOD_KEY = PREFIX + "to_period";
    private static final String USERSEARCHER_ACTION_KEY = "mbe_act_search" + PREFIX;
    private static final String USERSEARCHER_FIRSTNAME_KEY = "usrch_search_fname" + PREFIX;
    private static final String USERSEARCHER_LASTNAME_KEY = "usrch_search_lname" + PREFIX;
    private static final String USERSEARCHER_PERSONALID_KEY = "usrch_search_pid" + PREFIX;
    
    private static final String ACTION_KEY = PREFIX + "action_key";
	private static final int ACTION_SHOW_COMPILATION = 0,
            ACTION_SHOW_COMPILATION_LIST = 1,
            ACTION_NEW_INVOICE_RECORD = 2,
            //ACTION_REMOVE_INVOICE_RECORD = 3,
            ACTION_DELETE_COMPILATION = 4;

    private static final SimpleDateFormat periodFormatter
        = new SimpleDateFormat ("yyMM");
    private static final SimpleDateFormat dateFormatter
        = new SimpleDateFormat ("yyyy-MM-dd");

	/**
	 * Init is the event handler of InvoiceCompilationEditor
	 *
	 * @param context session data like user info etc.
	 */
	public void init (final IWContext context) {
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

        displayRedText (null, "<p>Denna funktion är inte färdig. Bland annat så återstår:<ol><li>skapa manuell faktura ifrån 'visa underlagslista'<li>ta bort en faktura från listan - bara manuella<li>klicka på faktureringsrad och se detaljer<li>skapa justeringsrad till en faktura<li>se faktureringsunderlag i pdf<li>tillåt inte negativt taxbelopp mm<li>uppdatera totalbelopp och momsersättning vid justering</ol>\n\n");
	}
	
	private int parseAction (final IWContext context) {
        int actionId = ACTION_SHOW_COMPILATION_LIST;
        try {
            actionId = Integer.parseInt (context.getParameter (ACTION_KEY));
        } catch (final Exception dummy) {
            // do nothing, actionId is deafault
        }
        return actionId;
	}	

    /**
     * Shows one invoice compilation.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilation (final IWContext context)
        throws RemoteException, FinderException {
        final int headerId = Integer.parseInt (context.getParameter
                                               (INVOICE_COMPILATION_KEY));
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final InvoiceHeaderHome home = business.getInvoiceHeaderHome ();
        final InvoiceHeader header
                = home.findByPrimaryKey (new Integer (headerId));
		final Date period = header.getPeriod ();
        final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (context, UserBusiness.class);
        final UserHome userHome = userBusiness.getUserHome ();
		final User custodian = userHome.findByPrimaryKey
		        (new Integer (header.getCustodianId ()));

        final Table table = createTable (4);
        setColumnWidthsEqual (table);
        int row = 2;
        addOperationFieldDropdown (table, row++); 
        int col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        addSmallText(table, (null != period ? periodFormatter.format (period)
                             : ""), col++, row);
        addSmallHeader (table, col++, row, STATUS_KEY, STATUS_DEFAULT, ":");
        addSmallText(table, header.getStatus () + "", col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, INVOICE_RECEIVER_KEY,
                        INVOICE_RECEIVER_DEFAULT, ":");
        addSmallText(table, getUserName (custodian), col++, row);
        addSmallHeader (table, col++, row, SSN_KEY, SSN_DEFAULT, ":");
        addSmallText(table, custodian.getPersonalID (), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, INVOICE_ADDRESS_KEY,
                        INVOICE_ADDRESS_DEFAULT, ":");
        table.mergeCells (col, row, col + 2, row);
        addSmallText(table, getAddressString (custodian), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, CREATION_DATE_KEY,
                        CREATION_DATE_DEFAULT, ":");
        addSmallText(table, getFormattedDate (header.getDateCreated ()), col++,
                     row);
        addSmallText(table, header.getCreatedBy (), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, ADJUSTMENT_DATE_KEY,
                        ADJUSTMENT_DATE_DEFAULT, ":");
        addSmallText(table, getFormattedDate (header.getDateAdjusted ()), col++,
                     row);
        addSmallText(table, header.getChangedBy (), col++, row);
        col = 1; row++;
        addSmallHeader (table, col++, row, JOURNAL_ENTRY_DATE_KEY,
                        JOURNAL_ENTRY_DATE_DEFAULT, ":");
        addSmallText(table, getFormattedDate (header.getDateJournalEntry ()),
                     col++, row++);
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);            
        table.add (getInvoiceRecordListTable (context, business, header), 1,
                   row++);
        table.setHeight (row++, 12);
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getSubmitButton (ACTION_NEW_INVOICE_RECORD + "", NEW_KEY,
                                    NEW_DEFAULT), 1, row);
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (INVOICE_COMPILATION_KEY,
                              INVOICE_COMPILATION_DEFAULT, outerTable));
    }

    /**
     * Shows a list of invoice compilations and a search form.
	 *
	 * @param context session data like user info etc.
	 */
    private void showCompilationList (final IWContext context)
        throws RemoteException, FinderException {
        final UserSearcher searcher = createSearcher ();
        final Table table = createTable (6);
        setColumnWidthsEqual (table);
        int row = 2;
        addOperationFieldDropdown (table, row++);
        addUserSearcherForm (table, row++, context, searcher);
        table.mergeCells (2, row, table.getColumns () - 1, row);
        addPeriodForm (table, row, context);
        table.add (getSubmitButton (ACTION_SHOW_COMPILATION_LIST + "",
                                    SEARCH_KEY, SEARCH_DEFAULT),
                   table.getColumns (), row++);
        if (null != searcher.getUser ()) {
            // exactly one user found - display users invoice compilation list
            final User userFound = searcher.getUser ();
            final Date fromPeriod = getPeriodFromString
                    (context.getParameter (FROM_PERIOD_KEY));
            final Date toPeriod = getPeriodFromString
                    (context.getParameter (TO_PERIOD_KEY));
            final InvoiceBusiness business = (InvoiceBusiness)
                    IBOLookup.getServiceInstance (context,
                                                  InvoiceBusiness.class);
            final InvoiceHeader [] headers = business
                    .getInvoiceHeadersByCustodianOrChild (userFound, fromPeriod,
                                                          toPeriod);
            table.mergeCells (1, row, table.getColumns (), row);            
            if (0 < headers.length) {
                table.add (getInvoiceCompilationListTable (context, headers), 1,
                           row++);
            } else {
                table.add (new Text ("no compilations found for this custodian"
                                     + " or child!"), 1, row++);
            }
        }

        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (INVOICE_COMPILATION_LIST_KEY,
                              INVOICE_COMPILATION_LIST_DEFAULT,  outerTable));
    }

    private Table getInvoiceCompilationListTable
        (final IWContext context, final InvoiceHeader [] headers)
        throws RemoteException, FinderException {

        // set up header row
        final String [][] columnNames =
                {{ STATUS_KEY, STATUS_DEFAULT }, { PERIOD_KEY, PERIOD_DEFAULT },
                 { INVOICE_RECEIVER_KEY, INVOICE_RECEIVER_DEFAULT },
                 { TOTAL_AMOUNT_KEY, TOTAL_AMOUNT_DEFAULT }, {"", ""},
                 {"", ""}};
        final Table table = createTable (columnNames.length);
        table.setColumns (columnNames.length);
        int row = 1;
        table.setRowColor(row, getHeaderColor ());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader (table, i + 1, row, columnNames [i][0],
                            columnNames [i][1]);
        }
        row++;

        // get some business objects
        final InvoiceBusiness invoiceBusiness = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);
        final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (context, UserBusiness.class);
        final UserHome userHome = userBusiness.getUserHome ();

        // show each invoice header in a row
        for (int i = 0; i < headers.length; i++) {
			showInvoiceHeaderOnARow (table, row++, invoiceBusiness, userHome,
                                     headers [i]);
        }
        
       return table;
    }

	private void showInvoiceHeaderOnARow
        (final Table table, final int row, final InvoiceBusiness business,
         final UserHome userHome, final InvoiceHeader header)
        throws FinderException {
		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
		                   : getZebraColor2 ());
		final char status = header.getStatus ();
		final User custodian = userHome.findByPrimaryKey
		        (new Integer (header.getCustodianId ())) ;
		final Date period = header.getPeriod ();
		final Link periodLink = getSmallLink
                (null != period ? periodFormatter.format (period) : "?");
        periodLink.addParameter (ACTION_KEY, ACTION_SHOW_COMPILATION);
		periodLink.addParameter (INVOICE_COMPILATION_KEY,
                                 header.getPrimaryKey ().toString ());
		final long totalAmount = getTotalAmount (header, business);
		table.add (status + "", col++, row);
		table.add (periodLink, col++, row);
		table.add (getUserName (custodian), col++, row);
		table.add (totalAmount + "", col++, row);
        Link editLink = new Link
                (getEditIcon (localize (EDIT_INVOICE_COMPILATION_KEY,
                                        EDIT_INVOICE_COMPILATION_DEFAULT)));
        editLink.addParameter (ACTION_KEY, ACTION_SHOW_COMPILATION);
		editLink.addParameter (INVOICE_COMPILATION_KEY,
                               header.getPrimaryKey ().toString ());        
        table.add (editLink, col++, row);
        if ('P' == status) {
            Link deleteLink = new Link
                    (getDeleteIcon (localize
                                    (DELETE_INVOICE_COMPILATION_KEY,
                                     DELETE_INVOICE_COMPILATION_DEFAULT)));
            deleteLink.addParameter (ACTION_KEY, ACTION_DELETE_COMPILATION);
            deleteLink.addParameter (INVOICE_COMPILATION_KEY,
                                     header.getPrimaryKey ().toString ());
            table.add (deleteLink, col++, row);
        }
	}

    private Table getInvoiceRecordListTable
        (final IWContext context, final InvoiceBusiness business,
         final InvoiceHeader header) throws RemoteException, FinderException {

 		final InvoiceRecord [] records
		        = business.getInvoiceRecordsByInvoiceHeader (header);

        // set up header row
        final String [][] columnNames =
                {{ SSN_KEY, SSN_DEFAULT },
                 { FIRST_NAME_KEY, FIRST_NAME_DEFAULT },
                 { INVOICE_TEXT_KEY, INVOICE_TEXT_DEFAULT },
                 { NUMBER_OF_DAYS_KEY, NUMBER_OF_DAYS_DEFAULT },
                 { AMOUNT_KEY, AMOUNT_DEFAULT },
                 { REMARK_KEY, REMARK_DEFAULT }};
        final Table table = createTable (columnNames.length);
        table.setColumns (columnNames.length);
        int row = 1;
        table.setRowColor(row, getHeaderColor ());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader (table, i + 1, row, columnNames [i][0],
                            columnNames [i][1]);
        }
        row++;

        // get some business objects
        final InvoiceBusiness invoiceBusiness = (InvoiceBusiness)
                IBOLookup.getServiceInstance (context, InvoiceBusiness.class);

        // show each invoice header in a row
        for (int i = 0; i < records.length; i++) {
			showInvoiceRecordOnARow (table, row++, invoiceBusiness,
                                     records [i]);
        }
        table.setHeight(row++, 12);
        table.mergeCells (1, row, 4, row);
        addSmallHeader (table, 1, row, TOTAL_AMOUNT_VAT_EXCLUSIVE_KEY,
                        TOTAL_AMOUNT_VAT_EXCLUSIVE_DEFAULT, ":");
        table.add (getTotalAmount (records) + "", 5, row++);
        table.mergeCells (1, row, 4, row);
        addSmallHeader (table, 1, row, TOTAL_AMOUNT_VAT_KEY,
                        TOTAL_AMOUNT_VAT_DEFAULT, ":");
        table.add (getTotalAmountVat (records) + "", 5, row++);
        addSmallHeader (table, 1, row, OWN_POSTING_KEY, OWN_POSTING_DEFAULT,
                        ":");
        table.mergeCells (2, row, table.getColumns (), row);
        table.add (header.getOwnPosting (), 2, row++);
        addSmallHeader (table, 1, row, DOUBLE_POSTING_KEY,
                        DOUBLE_POSTING_DEFAULT, ":");
        table.mergeCells (2, row, table.getColumns (), row);
        table.add (header.getDoublePosting (), 2, row++);
        
       return table;
    }

	private void showInvoiceRecordOnARow
        (final Table table, final int row, final InvoiceBusiness business,
         final InvoiceRecord record) throws RemoteException {
		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
		                   : getZebraColor2 ());
        final User child = business.getChildByInvoiceRecord (record);
        if (null != child) {
            table.add (child.getPersonalID (), col++, row);
            table.add (child.getFirstName (), col++, row);
        } else {
            col += 2;
        }
		table.add (record.getInvoiceText (), col++, row);
		table.add (record.getDays () + "", col++, row);
		table.add (((long) record.getAmount ()) + "", col++, row);
		table.add (record.getNotes (), col++, row);
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
        final Table mainTable = createTable(1);
        mainTable.setCellpadding (getCellpadding ());
        mainTable.setCellspacing (getCellspacing ());
        mainTable.setWidth (Table.HUNDRED_PERCENT);
        int row = 1;
        mainTable.setRowColor (row, getHeaderColor ());
        mainTable.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER) ;
        addSmallHeader (mainTable, 1, row++,  headerKey, headerDefault);
        mainTable.add (content, 1, row++);
        return mainTable;
    }

    void addUserSearcherForm
        (final Table table, final int row, final IWContext context,
         final UserSearcher searcher) throws RemoteException {
        int col = 1;
        table.add (new HiddenInput (USERSEARCHER_ACTION_KEY), col, row);
        try {
            searcher.process (context);
        } catch (final FinderException dummy) {
            // do nothing, it's ok that none was found
        }
        addSmallHeader (table, col++, row, SSN_KEY, SSN_DEFAULT, ":");
        table.add (getUserSearcherInput
                   (context, USERSEARCHER_PERSONALID_KEY), col++, row);
        addSmallHeader (table, col++, row, FIRST_NAME_KEY,  FIRST_NAME_DEFAULT,
                        ":");
        table.add (getUserSearcherInput
                   (context, USERSEARCHER_FIRSTNAME_KEY), col++, row);
        addSmallHeader (table, col++, row, LAST_NAME_KEY, LAST_NAME_DEFAULT,
                        ":");
        table.add (getUserSearcherInput
                   (context, USERSEARCHER_LASTNAME_KEY), col++, row);
    }

    void addPeriodForm (final Table table, final int row,
                        final IWContext context) {
        int col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT);
        table.add (getUserSearcherInput (context, FROM_PERIOD_KEY), col, row);
        table.add (new Text (" - "), col, row);
        table.add (getUserSearcherInput (context, TO_PERIOD_KEY), col, row);
    }

    private TextInput getUserSearcherInput (final IWContext context,
                                            final String key) {
        final TextInput input = (TextInput) getStyledInterface
                (new TextInput (key));
        input.setLength (12);
        if (context.isParameterSet (key)) {
            input.setValue (context.getParameter (key));
        }
        return input;
    }

    private void addSmallText (final Table table, final String string,
                               final int col, final int row) {
        table.add (getSmallText (string), col, row);
    }

    private void addSmallHeader (final Table table, final int col,
                                 final int row, final String key,
                                 final String defaultString) {
        addSmallHeader (table, col, row, key, defaultString, "");
    }

    private void addSmallHeader
        (final Table table, final int col, final int row, final String key,
         final String defaultString, final String suffix) {
        final String localizedString = localize (key, defaultString) + suffix;
        table.add (getSmallHeader (localizedString), col, row);
    }

    private Table createTable (final int columnCount) {
        final Table table = new Table();
        table.setCellpadding (getCellpadding ());
        table.setCellspacing (getCellspacing ());
        table.setWidth (Table.HUNDRED_PERCENT);
        table.setColumns (columnCount);
        return table;
    }

    /**
     * Returns a date from a string of type "YYMM". The date represents the
     * first day of that month. If the input string is unparsable for this
     * format then null is returned.
     *
     * @param rawString input string of type "YYMM"
     * @return date from the first of this particular month or null on failure
     */
    private static Date getPeriodFromString (final String rawString) {
        if (null == rawString || rawString.length () != 4) return null;
        try {
            final int year = Integer.parseInt (rawString.substring (0, 2))
                    + 2000;
            final int month = Integer.parseInt (rawString.substring (2, 4))
                    + Calendar.JANUARY - 1;
            final Calendar calendar = Calendar.getInstance ();
            calendar.set (year, month, 1, 0, 0);
            return new Date (calendar.getTimeInMillis ());
        } catch (final NumberFormatException exception) {
            return null;
        }
    }

    /*private Table getButtonTable (final String [][] buttonInfo) {
        final Table table = new Table ();
        for (int i = 0; i < buttonInfo.length; i++) {
            table.add (getSubmitButton (buttonInfo [i][0], buttonInfo [i][1],
                                        buttonInfo [i][2]), i + 1, 1);
            table.add (Text.getNonBrakingSpace (), i + 1, 1);
        }
        return table;
    }*/

    private SubmitButton getSubmitButton (final String action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (action, localize (key, defaultName)));
    }

    private void addOperationFieldDropdown
        (final Table table, final int row) throws RemoteException {
        int col = 1;
        addSmallHeader (table, col++, row, MAIN_ACTIVITY_KEY,
                        MAIN_ACTIVITY_DEFAULT, ":");
        String operationalField = getSession ().getOperationalField();
        operationalField = operationalField == null ? "" : operationalField;
        table.mergeCells (col, row, table.getColumns (), row);
        table.add (new OperationalFieldsMenu (), col++, row);
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

    private static String getUserName (final User user) {
        return user.getLastName () + ", " + user.getFirstName ();
    }

    private static String getFormattedDate (final Date date) {
        return null == date ? "" : dateFormatter.format (date);
    }

    private static void setColumnWidthsEqual (final Table table) {
        final int columnCount = table.getColumns ();
        final int percentageInt = 100 / columnCount;
        final String percentageString = percentageInt + "%";
        for (int i = 1; i <= columnCount; i++) {
            table.setColumnWidth (i, percentageString);
        }
    }

    private static String getAddressString (final User user) {
        final StringBuffer result = new StringBuffer ();
        if (null != user) {
            final Collection addresses = user.getAddresses ();
            if (!addresses.isEmpty ()) {
                final Address address = (Address) addresses.toArray () [0];
                result.append (address.getStreetAddress());
                result.append (", ");
                result.append (address.getPostalAddress());
            }
        }
        return result.toString ();
    }

    private long getTotalAmount (final InvoiceHeader header,
                                   final InvoiceBusiness business) {
 		final InvoiceRecord [] records
		        = business.getInvoiceRecordsByInvoiceHeader (header);
        return getTotalAmount (records);
    }

    private long getTotalAmount (final InvoiceRecord [] records) {
		long totalAmount = 0;
		for (int i = 0; i < records.length; i++) {
		    totalAmount += records[i].getAmount ();
		}
        return totalAmount;
    }

    private long getTotalAmountVat (final InvoiceRecord [] records) {
		long totalAmountVat = 0;
		for (int i = 0; i < records.length; i++) {
		    totalAmountVat += records[i].getAmountVAT ();
		}
        return totalAmountVat;
    }

    private void displayRedText (final String key, final String defaultString) {
        final String localizedString = key != null
                ? localize (key, defaultString) : defaultString;
        final Text text
                = new Text ('\n' + localizedString + '\n');
        text.setFontColor ("#ff0000");
        add (text);
    }
}
