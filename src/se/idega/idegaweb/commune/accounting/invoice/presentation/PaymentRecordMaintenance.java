package se.idega.idegaweb.commune.accounting.invoice.presentation;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

/**
 * PaymentRecordMaintenance is an IdegaWeb block were the user can search, view
 * and edit payment records.
 * <p>
 * Last modified: $Date: 2003/11/12 15:56:32 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson</a>
 * @version $Revision: 1.9 $
 * @see com.idega.presentation.IWContext
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.data
 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock
 */
public class PaymentRecordMaintenance extends AccountingBlock {
    private static final String PREFIX = "cacc_payrec_";
        
    private static final String ADJUSTMENT_DATE_DEFAULT = "Just.datum";
    private static final String ADJUSTMENT_DATE_KEY = PREFIX + "adjustment_date";
    private static final String ADJUSTMENT_SIGNATURE_DEFAULT = "Just.sign";
    private static final String ADJUSTMENT_SIGNATURE_KEY = PREFIX + "adjustment_signature";
    private static final String CHECK_AMOUNT_DEFAULT = "Checkbelopp";
    private static final String CHECK_AMOUNT_KEY = PREFIX + "check_amount";
    private static final String CHECK_PERIOD_DEFAULT = "Checkperiod";
    private static final String CHECK_PERIOD_KEY = PREFIX + "check_period";
    private static final String DAYS_DEFAULT = "Days";
    private static final String DAYS_KEY = PREFIX + "days";
    private static final String DELETE_ROW_DEFAULT = "Ta bort post";
    private static final String DELETE_ROW_KEY = PREFIX + "delete_invoice_compilation";
    private static final String DETAILED_PAYMENT_RECORDS_DEFAULT = "Detaljutbetalningsrader";
    private static final String DETAILED_PAYMENT_RECORDS_KEY = PREFIX + "detailed_payment_records";
    private static final String EDIT_ROW_DEFAULT = "Ändra post";
    private static final String EDIT_ROW_KEY = PREFIX + "edit_row";
    private static final String END_PERIOD_KEY = PREFIX + "end_period";
    private static final String MAIN_ACTIVITY_DEFAULT = "Huvudverksamhet";
    private static final String MAIN_ACTIVITY_KEY = PREFIX + "main_activity";
    private static final String NAME_DEFAULT = "Namn";
    private static final String NAME_KEY = PREFIX + "name";
    private static final String NOTE_DEFAULT = "Anmärkning";
    private static final String NOTE_KEY = PREFIX + "note";
    private static final String NO_OF_PLACEMENTS_DEFAULT = "Antal plac";
    private static final String NO_OF_PLACEMENTS_KEY = PREFIX + "no_of_placements";
    private static final String PAYMENT_DEFAULT = "Utbetalning";
    private static final String PAYMENT_KEY = PREFIX + "payment";
    private static final String PAYMENT_RECORD_DEFAULT = "Utbetalningsrad";
    private static final String PAYMENT_RECORD_KEY = PREFIX + "payment_record";
    private static final String PERIOD_DEFAULT = "Period";
    private static final String PERIOD_KEY = PREFIX + "period";
    private static final String PLACEMENT_DEFAULT = "Placering";
    private static final String PLACEMENT_KEY = PREFIX + "placement";
    private static final String PLACEMENT_PERIOD_DEFAULT = "Plac.period";
    private static final String PLACEMENT_PERIOD_KEY = PREFIX + "placement_period";
    private static final String PROVIDER_DEFAULT = "Anordnare";
    private static final String PROVIDER_KEY = PREFIX + "provider";
    private static final String SEARCH_DEFAULT = "Sök";
    private static final String SEARCH_KEY = PREFIX + "search";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = PREFIX + "ssn";
    private static final String START_PERIOD_KEY = PREFIX + "start_period";
    private static final String STATUS_DEFAULT = "Status";
    private static final String STATUS_KEY = PREFIX + "status";
    private static final String TOTAL_AMOUNT_DEFAULT = "Totalbelopp";
    private static final String TOTAL_AMOUNT_KEY = PREFIX + "total_amount";


    private static final String ACTION_KEY = PREFIX + "action_key";
	private static final int ACTION_SHOW_PAYMENT = 0,
            ACTION_SHOW_RECORD_DETAILS = 1;
    
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
            int actionId = ACTION_SHOW_PAYMENT;
            try {
                actionId = Integer.parseInt (context.getParameter (ACTION_KEY));
            } catch (final Exception dummy) {
                // do nothing, actionId is default
            }
            
			switch (actionId) {
                case ACTION_SHOW_RECORD_DETAILS:
                    showRecordDetails (context);
                    break;
                    
                default:
                    showPayment (context);
					break;					
			}
            
		} catch (Exception exception) {
            logUnexpectedException (context, exception);
		}
	}
    
    private void showRecordDetails (final IWContext context)
        throws RemoteException, javax.ejb.FinderException {
        // get business objects
        final InvoiceBusiness business = (InvoiceBusiness) IBOLookup
                .getServiceInstance (context, InvoiceBusiness.class);
        final SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                .getServiceInstance (context, SchoolBusiness.class);
        // get home objects
        final PaymentRecordHome recordHome = business.getPaymentRecordHome ();
        final PaymentHeaderHome headerHome = business.getPaymentHeaderHome ();
        final SchoolCategoryHome categoryHome
                = schoolBusiness.getSchoolCategoryHome ();
        
        // get data objects
        final Integer recordId
                = new Integer (context.getParameter (PAYMENT_RECORD_KEY));
        final PaymentRecord record = recordHome.findByPrimaryKey (recordId);
        final PaymentHeader header = headerHome.findByPrimaryKey
                (new Integer (record.getPaymentHeader ()));
        final SchoolCategory category
                = categoryHome.findByPrimaryKey (header.getSchoolCategoryID ());
        final School school = schoolBusiness.getSchool
                (new Integer (header.getSchoolID ()));
        
        // render
        final Table table = createTable (2);
        table.setColumnWidth (1, "25%");
        int row = 1; int col = 1;
        addSmallHeader (table, col++, row, MAIN_ACTIVITY_KEY,
                        MAIN_ACTIVITY_DEFAULT, ":");
        addSmallText (table, col++, row++, category.getLocalizedKey (),
                      category.getName ());
        col = 1;
        addSmallHeader (table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        addSmallText (table, col++, row++, school.getName ());
        col = 1;
        addSmallHeader (table, col++, row, PLACEMENT_KEY, PLACEMENT_DEFAULT,
                        ":");
        addSmallText (table, col++, row++, record.getPaymentText ());
        col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        addSmallText (table, col++, row++,
                      getFormattedPeriod (record.getPeriod ()));
        col = 1;
        table.mergeCells (1, row, table.getColumns (), row);
        table.add (getDetailedPaymentRecordListTable
                   (context, record, business), 1, row++);

        // add to form
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (localize (DETAILED_PAYMENT_RECORDS_KEY,
                                        DETAILED_PAYMENT_RECORDS_DEFAULT),
                              outerTable));
    }

    private Table getDetailedPaymentRecordListTable
        (final IWContext context, final PaymentRecord paymentRecord,
         final InvoiceBusiness business)
    throws RemoteException, javax.ejb.FinderException {
        // set up header row
        final String [][] columnNames =
                {{ STATUS_KEY, STATUS_DEFAULT },
                 { SSN_KEY, SSN_DEFAULT },
                 { NAME_KEY, NAME_DEFAULT },
                 { CHECK_PERIOD_KEY, CHECK_PERIOD_DEFAULT },
                 { DAYS_KEY, DAYS_DEFAULT },
                 { CHECK_AMOUNT_KEY, CHECK_AMOUNT_DEFAULT },
                 { PLACEMENT_PERIOD_KEY, PLACEMENT_PERIOD_DEFAULT },
                 { ADJUSTMENT_DATE_KEY, ADJUSTMENT_DATE_DEFAULT },
                 { ADJUSTMENT_SIGNATURE_KEY, ADJUSTMENT_SIGNATURE_DEFAULT }};
        final Table table = createTable (columnNames.length);
        table.setColumns (columnNames.length);
        setIconColumnWidth (table);
        int row = 1;
        table.setRowColor(row, getHeaderColor ());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader (table, i + 1, row, columnNames [i][0],
                            columnNames [i][1]);
        }
        row++;

        // get detailed records
        final InvoiceRecordHome home = business.getInvoiceRecordHome ();
        final Collection invoiceRecords
                = home.findByPaymentRecord (paymentRecord);

        //render
        final SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                .getServiceInstance (context, SchoolBusiness.class);
        final SchoolClassMemberHome memberHome
                = schoolBusiness.getSchoolClassMemberHome ();
        for (Iterator i = invoiceRecords.iterator (); i.hasNext ();) {
            final InvoiceRecord invoiceRecord = (InvoiceRecord) i.next ();
			showDetailedPaymentRecordOnARow (table, row++, invoiceRecord,
                                             memberHome);
        }
        
        return table;
    }

    private String getFormattedPeriod (Date date) {
        return null != date ? periodFormatter.format (date) : "";
    }

    private String getFormattedDate (Date date) {
        return null != date ? dateFormatter.format (date) : "";
    }

	private void showDetailedPaymentRecordOnARow
        (final Table table, final int row, final InvoiceRecord record,
         final SchoolClassMemberHome home)
    throws RemoteException, javax.ejb.FinderException {
        final String checkPeriod = getFormattedPeriod
                (record.getPeriodStartCheck ()) + " - "
                + getFormattedPeriod (record.getPeriodEndCheck ());
        final String days = record.getDays () + "";
        final String amount = ((long) record.getAmount ()) + "";
        final String placementPeriod = getFormattedPeriod
                (record.getPeriodStartPlacement ()) + " - "
                + getFormattedPeriod (record.getPeriodEndPlacement ());
        final String dateChanged
                = getFormattedDate (record.getDateChanged ());
        final String changedBy = record.getChangedBy ();
        final Integer memberId = new Integer (record.getSchoolClassMemberId ());
        final SchoolClassMember member = home.findByPrimaryKey (memberId);
        final User user = member.getStudent ();
        final String ssn = formatSsn (user.getPersonalID ());
        final String userName = getUserName (user);

		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
                           : getZebraColor2 ());
		addSmallText (table, col++, row, "");
		addSmallText (table, col++, row, ssn);
		addSmallText (table, col++, row, userName);
		addSmallText (table, col++, row, checkPeriod);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		addSmallText (table, col++, row, days);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		addSmallText (table, col++, row, amount);
		addSmallText (table, col++, row, checkPeriod);
		addSmallText (table, col++, row, dateChanged);
		addSmallText (table, col++, row, changedBy);
	}

    private String formatSsn (final String ssn) {
        return null == ssn || 12 != ssn.length () ? ssn
                : ssn.substring (2, 8) + '-' + ssn.substring (8, 12);
    }

    private static String getUserName (final User user) {
        return user.getLastName () + ", " + user.getFirstName ();
    }

    private void showPayment (final IWContext context) throws RemoteException {
        final Table table = createTable (3);
        setColumnWidthsEqual (table);
        int row = 2; // first row is reserved for setting column widths
        addOperationFieldDropdown (table, row++);
        addProviderDropdown (context, table, row++);
        addPeriodForm (table, row);
        table.setAlignment (table.getColumns (), row,
                            Table.HORIZONTAL_ALIGN_RIGHT);
        table.add (getSubmitButton (ACTION_SHOW_PAYMENT + "",
                                    SEARCH_KEY, SEARCH_DEFAULT),
                   table.getColumns (), row++);
        final String schoolCategory = getSession().getOperationalField ();
        final String providerId = context.getParameter (PROVIDER_KEY);
        if (null != schoolCategory && null != providerId
            && 0 < providerId.length ()) {
            table.mergeCells (1, row, table.getColumns (), row);
            table.add (getPaymentRecordListTable (context), 1, row++);
        }
        final Form form = new Form ();
        form.setOnSubmit("return checkInfoForm()");
        form.add (table);
        final Table outerTable = createTable (1);
        outerTable.add (form, 1, 1);
        add (createMainTable (localize (PAYMENT_KEY, PAYMENT_DEFAULT),
                              outerTable));
    }

    private Table getPaymentRecordListTable (final IWContext context)
        throws RemoteException {
        // set up header row
        final String [][] columnNames =
                {{ STATUS_KEY, STATUS_DEFAULT }, { PERIOD_KEY, PERIOD_DEFAULT },
                 { PLACEMENT_KEY, PLACEMENT_DEFAULT },
                 { NO_OF_PLACEMENTS_KEY, NO_OF_PLACEMENTS_DEFAULT },
                 { TOTAL_AMOUNT_KEY, TOTAL_AMOUNT_DEFAULT },
                 { NOTE_KEY, NOTE_DEFAULT }, {"", ""}, {"", ""}};
        final Table table = createTable (columnNames.length);
        table.setColumns (columnNames.length);
        setIconColumnWidth (table);
        int row = 1;
        table.setRowColor(row, getHeaderColor ());
        for (int i = 0; i < columnNames.length; i++) {
            addSmallHeader (table, i + 1, row, columnNames [i][0],
                            columnNames [i][1]);
        }
        row++;

        // get payment records
        final String schoolCategory = getSession().getOperationalField ();
        final Integer providerId
                = new Integer (context.getParameter (PROVIDER_KEY));
        final Date startPeriod = getPeriodParameter (context, START_PERIOD_KEY);
        final Date endPeriod = getPeriodParameter (context, END_PERIOD_KEY);
        final InvoiceBusiness business = (InvoiceBusiness)
                IBOLookup.getServiceInstance(context, InvoiceBusiness.class);
        final PaymentRecord [] records
                = business.getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
                (schoolCategory, providerId,
                 new java.sql.Date (startPeriod.getTime ()),
                 new java.sql.Date (endPeriod.getTime ()));

        // show each payment record in a row
        for (int i = 0; i < records.length; i++) {
			showPaymentRecordOnARow (table, row++, records [i]);
        }
        
        return table;
    }

	private void showPaymentRecordOnARow
        (final Table table, final int row, final PaymentRecord record) {
        final String [][] editLinkParameters
                = {{ ACTION_KEY, ACTION_SHOW_RECORD_DETAILS + "" },
                   { PAYMENT_RECORD_KEY, record.getPrimaryKey () + "" }};
		final char status = record.getStatus ();
        final Date period = record.getPeriod ();
        final String periodText = getFormattedPeriod (period);
        final Link paymentTextLink = createSmallLink (record.getPaymentText (),
                                                      editLinkParameters);
        final int placements = record.getPlacements();
        final long  totalAmount = (long) record.getTotalAmount ();
        final String note = record.getNotes ();
        final Link editLink = createIconLink (getEditIcon (),
                                              editLinkParameters);
		int col = 1;
		table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
                           : getZebraColor2 ());
		addSmallText (table, col++, row, status + "");
		addSmallText (table, col++, row, periodText);
		table.add (paymentTextLink, col++, row);
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		addSmallText (table, col++, row, placements + "");
        table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
		addSmallText (table, col++, row, totalAmount + "");
		addSmallText (table, col++, row, note);
        table.add (editLink, col++, row);
	}

    private Image getEditIcon () {
        return getEditIcon (localize (EDIT_ROW_KEY, EDIT_ROW_DEFAULT));
    }

    private Image getDeleteIcon () {
        return getDeleteIcon (localize (DELETE_ROW_KEY, DELETE_ROW_DEFAULT));
    }

    private static Link addParametersToLink (final Link link,
                                             final String [][] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            link.addParameter (parameters [i][0], parameters [i][1]);
        }
        return link;
    }

    private Link createSmallLink (final String displayText,
                                  final String [][] parameters) {
        final Link link = getSmallLink (displayText);
        addParametersToLink (link, parameters);
        return link;
    }

    private static Link createIconLink (final Image icon,
                                        final String [][] parameters) {
        final Link link = new Link (icon);
        addParametersToLink (link, parameters);
        return link;
    }        

    private void addSmallText (final Table table, final int col, final int row,
                               final String key, String value) {
        table.add (getSmallText (localize (key, value)), col, row);
    }

    private void addSmallText (final Table table, final int col, final int row,
                               final String string) {
        table.add (getSmallText (null == string || string.equals (null + "")
                                 ? "" : string), col, row);
    }

    private void addSmallHeader (final Table table, final int col,
                                 final int row, final String key,
                                 final String defaultString) {
        addSmallHeader (table, col, row, key, defaultString, "");
    }

    private void setIconColumnWidth (final Table table) {
        final int columnCount = table.getColumns ();
        table.setColumnWidth (columnCount - 1, getEditIcon ().getWidth ());
        table.setColumnWidth (columnCount, getDeleteIcon ().getWidth ());
    }

    /**
     * Returns a date from a parameter string of type "YYMM". The date
     * represents the first day of that month. If the input string is unparsable
     * for this format then null is returned.
     *
     * @param context session data
     * @param key key to lookup in context to retreive the actual value
     * @return date from the first of this particular month or null on failure
     */
    private static Date getPeriodParameter (final IWContext context,
                                            final String key) {
        final String rawString = context.getParameter (key);
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

	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
     * @return Table to add to output
	 */
    private Table createMainTable
        (final String header, final PresentationObject content)
        throws RemoteException {
        final Table mainTable = createTable (1);
        mainTable.setCellpadding (getCellpadding ());
        mainTable.setCellspacing (getCellspacing ());
        mainTable.setWidth (Table.HUNDRED_PERCENT);
        int row = 1;
        mainTable.setRowColor (row, getHeaderColor ());
        mainTable.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER) ;
        mainTable.add (getSmallHeader (header), 1, row++);
        mainTable.add (content, 1, row++);
        return mainTable;
    }

    private void addPeriodForm (final Table table, final int row) {
        int col = 1;
        addSmallHeader (table, col++, row, PERIOD_KEY, PERIOD_DEFAULT, ":");
        final Date now = new Date (System.currentTimeMillis ());
        table.add (getStyledInput (START_PERIOD_KEY, getFormattedPeriod
                                   (now)), col, row);
        table.add (new Text (" - "), col, row);
        table.add (getStyledInput (END_PERIOD_KEY, getFormattedPeriod
                                   (now)), col, row);
    }

    private void addProviderDropdown
        (final IWContext context, final Table table, final int row)
        throws RemoteException {
        int col = 1;
        addSmallHeader (table, col++, row, PROVIDER_KEY, PROVIDER_DEFAULT, ":");
        final String schoolCategory = getSession().getOperationalField ();
        final DropdownMenu providerDropdown = (DropdownMenu)
                getStyledInterface (new DropdownMenu (PROVIDER_KEY));
        if (null != schoolCategory) {
            final SchoolBusiness business = (SchoolBusiness) IBOLookup
                    .getServiceInstance (context, SchoolBusiness.class);
            final Collection schools
                    = business.findAllSchoolsByCategory (schoolCategory);
            final String oldProviderId = context.getParameter (PROVIDER_KEY)
                    + "";
            for (Iterator i = schools.iterator (); i.hasNext ();) {
                final School school = (School) i.next ();
                final String primaryKey = school.getPrimaryKey ().toString ();
                final String name = school.getName ();
                providerDropdown.addMenuElement (primaryKey, name);
                if (primaryKey.equals (oldProviderId)) {
                    providerDropdown.setSelectedElement (primaryKey);
                }
            }
        } else {
            providerDropdown.addMenuElement ("", localize (PROVIDER_KEY,
                                                           PROVIDER_DEFAULT));
        }
        table.add (providerDropdown, col++, row);
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

    private Table createTable (final int columnCount) {
        final Table table = new Table();
        table.setCellpadding (getCellpadding ());
        table.setCellspacing (getCellspacing ());
        table.setWidth (Table.HUNDRED_PERCENT);
        table.setColumns (columnCount);
        return table;
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

    private static void setColumnWidthsEqual (final Table table) {
        final int columnCount = table.getColumns ();
        final int percentageInt = 100 / columnCount;
        final String percentageString = percentageInt + "%";
        for (int i = 1; i <= columnCount; i++) {
            table.setColumnWidth (i, percentageString);
        }
    }

    private TextInput getStyledInput (final String key, final String value) {
        final TextInput input = (TextInput) getStyledInterface
                (new TextInput (key));
        input.setLength (12);
        if (null != value) {
            input.setValue (value);
        }
        return input;
    }

    private void addSmallHeader
        (final Table table, final int col, final int row, final String key,
         final String defaultString, final String suffix) {
        final String localizedString = localize (key, defaultString) + suffix;
        table.add (getSmallHeader (localizedString), col, row);
    }

    private SubmitButton getSubmitButton (final String action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (localize (key, defaultName),
                                          ACTION_KEY, action));
    }
}
