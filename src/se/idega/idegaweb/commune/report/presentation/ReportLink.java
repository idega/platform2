package se.idega.idegaweb.commune.report.presentation;

import com.idega.block.category.data.ICCategory;
import com.idega.block.reports.business.*;
import com.idega.block.reports.data.*;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import java.text.DateFormat;
import java.util.*;
import javax.servlet.http.HttpSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.report.business.Fetcher;

/**
 * IdegaWeb presentation class for wizard input of a new Report Generator
 * <p>
 * Last modified: $Date: 2004/06/29 16:30:07 $ by $Author: thomas $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.13 $
 * @see com.idega.block.reports.data.Report
 */
public class ReportLink extends CommuneBlock {
	final private static String PREFIX = "report_";
	
	// Actions
	final private static String TRY_SQL_ACTION = PREFIX + "try_sql_action";
	final private static String SAVE_GENERATOR_ACTION = PREFIX
		+ "save_generator_action";
	final private static String EDIT_GENERATOR_ACTION = PREFIX
		+ "report_admin_action";
	
	// Localized key and default value pairs
	final private static String COLUMN_NAME_KEY = PREFIX + "column_name";
	final private static String CURRENT_REPORT_KEY = PREFIX + "current_report";
	final private static String NO_ROWS_FOUND_KEY = PREFIX + "no_rows_found";
	final private static String REPORT_NAME_DEFAULT = "Rapportens namn";
	final private static String REPORT_NAME_KEY = PREFIX + "report_name";
	final private static String SAMPLE_DEFAULT = "Exempeldata";
	final private static String SAMPLE_KEY = PREFIX + "sample";
	final private static String SAVE_GENERATOR_DEFAULT = "Spara rapportgenerator";
	final private static String SAVE_GENERATOR_KEY = PREFIX + "save_generator";
	final private static String SQL_ERROR_DEFAULT = "Ett fel inträffade med anledning av databasanropet";
	final private static String SQL_ERROR_KEY = PREFIX + "sql_error";
	final private static String SQL_QUERY_DEFAULT = "SQL-fråga";
	final private static String SQL_QUERY_KEY = PREFIX + "sql_query";
	final private static String STEP_1_TRY_SQL_DEFAULT = "1. Prova ut ett SQL-kommando till rapporten";
	final private static String STEP_1_TRY_SQL_KEY = PREFIX + "step_1_try_sql";
	final private static String STEP_2_NAME_REPORT_DEFAULT = "2. Namnge rapporten";
	final private static String STEP_2_NAME_REPORT_KEY = PREFIX + "step_2_name_report";
	final private static String TRY_SQL_DEFAULT = "Prova SQL";
	final private static String TRY_SQL_KEY = PREFIX + "try_sql";
	
	/**
	 * Dispatches according to action parameter. If none is set, then check if
	 * there is a defined report for this object instance and show at as a link
	 * or else display the report creation wizard.
	 */
	public void main (final IWContext context) {
		setResourceBundle (getResourceBundle (context));
		
		final Report report = findReport (context);
		if (report != null) {
			showReportLink (report);
		} else if (context.isParameterSet (SAVE_GENERATOR_ACTION)) {
			saveGenerator (context);
		} else if (context.isParameterSet (TRY_SQL_ACTION)) {
			trySql (context);
		} else if (context.isParameterSet (EDIT_GENERATOR_ACTION)) {
			editGenerator ();
		} else {
			showSqlForm (context);
		}
	}
	
	/**
	 * Edits an allready saved report generator
	 * @param reportInfo current report generator properties to edit
	 */
	private void editGenerator () {
		add ("administrateReport");
	}
	
	/**
	 * Displays a link to the report associated with this block. The report is
	 * dynammically generated as soon as the link is clicked.
	 * @param reportInfo current report generator properties
	 */
	private void showReportLink (final Report report) {
		final Window window = new Window ("Report", "/servlet/MediaServlet");
		window.setResizable (true);
		window.setMenubar (true);
		window.setHeight (400);
		window.setWidth (500);
		final Link link = new Link (report.getName ());
		link.setWindow (window);
		final String encryptedClassName	= IWMainApplication.getEncryptedClassName
				(se.idega.idegaweb.commune.report.presentation.ReportGenerator.class);
		link.addParameter ("wrcls", encryptedClassName);
		link.addParameter (ReportGenerator.REPORT_ID,
											 ((Integer)report.getPrimaryKey()).intValue());
		add (link);
	}
	
	/**
	 * Shows the sql form and also a column name form if an sql was just entered
	 */
	private void trySql (final IWContext context) {
		final String sql = context.getParameter (SQL_QUERY_KEY);
		showSqlForm (context);
		showReportNameForm (context);
		final HttpSession session = context.getSession ();
		session.setAttribute (SQL_QUERY_KEY, sql);
	}
	
	/**
	 * Shows a sql form where the user can try a query to be used in a
	 * report generator. The sql form is pre filled with the last entered query,
	 * if any,  during this session.
	 */
	private void showSqlForm (final IWContext context) {
		final Form form = new Form ();
		final Table table = new Table ();
		form.add (table);
		add (form);
		int row = 1;
		table.add (getHeader (localize (STEP_1_TRY_SQL_KEY,
																		STEP_1_TRY_SQL_DEFAULT)), 1, row++);
		final TextArea textArea = new TextArea (SQL_QUERY_KEY);
		if (context.isParameterSet (SQL_QUERY_KEY)) {
			textArea.setContent (context.getParameter (SQL_QUERY_KEY));
		}
		textArea.setColumns (60);
		textArea.setRows (10);
		final SubmitButton submit = (SubmitButton) getButton
				(new SubmitButton (TRY_SQL_ACTION, getLocalizedString
													 (TRY_SQL_KEY, TRY_SQL_DEFAULT)));
		final Text header = getSmallHeader (localize (SQL_QUERY_KEY,
																									SQL_QUERY_DEFAULT) + ":");
		table.add(header, 1, row++);
		table.add(textArea, 1, row++);
		table.add(submit, 1, row++);
	}
	
	/**
	 * Shows sample data that is fetched with last user query, a form where the
	 * user can name the report and submit button for storing the new report
	 * generator.
	 */
	private void showReportNameForm (final IWContext context) {
		if (context.isParameterSet (SQL_QUERY_KEY)) {
			final Form form = new Form ();
			final Table table = new Table ();
			form.add (table);
			add (form);
			int row = 1;
			try {
				final Table fetchTable = getFetchTable (context.getParameter
																								(SQL_QUERY_KEY));
				table.add (getHeader (localize (STEP_2_NAME_REPORT_KEY,
																				STEP_2_NAME_REPORT_DEFAULT)), 1, row++);
				table.add (getSmallHeader
									 (localize (REPORT_NAME_KEY, REPORT_NAME_DEFAULT) + ":"),
									 1, row++);
				table.add (getStyledInterface (new TextInput(REPORT_NAME_KEY)), 1,
									 row++);
				table.add (getSmallHeader
									 (localize (SQL_QUERY_KEY, SQL_QUERY_DEFAULT)
										+ ":"), 1, row++);
				table.add (getSmallText (context.getParameter (SQL_QUERY_KEY)), 1,
									 row++);
				table.add (getSmallHeader
									 (localize (SAMPLE_KEY, SAMPLE_DEFAULT) + ":"), 1, row++);
				table.add (fetchTable, 1, row++);
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.add (getSmallText
									 ('[' + DateFormat.getTimeInstance ().format
										(new Date ()) + ']'), 1, row++);
				final SubmitButton submit = (SubmitButton) getButton
						(new SubmitButton
						 (SAVE_GENERATOR_ACTION, getLocalizedString
							(SAVE_GENERATOR_KEY, SAVE_GENERATOR_DEFAULT)));
				table.add (submit, 1, row++);
			} catch (Fetcher.FetchException e) {
				table.add (getSmallHeader (localize (SQL_ERROR_KEY, SQL_ERROR_DEFAULT)
																	 + ":"), 1, row++);
				table.add (getSmallText(e.getMessage ()), 1, row++);
			}
		}
	}
	
	/**
	 * Stores a new report generator in the databse. Also creates a report
	 * category if it didn't exist.
	 */
	private void saveGenerator (final IWContext context) {
		// 1. get web form posted parameters
		final String sql = getSqlParameter (context.getSession ());
		final String reportName = context.getParameter (REPORT_NAME_KEY);
		final List columnHeaderList = new ArrayList ();
		for (int col = 0; context.isParameterSet (COLUMN_NAME_KEY + col); col++) {
			columnHeaderList.add (context.getParameter (COLUMN_NAME_KEY + col));
		}
		final String [] columnHeaders = (String []) columnHeaderList.toArray
				(new String [columnHeaderList.size ()]);
		
		// 2. get report category id - or create if it didn't exist
		final int objectInstanceId = getICObjectInstanceID ();
		ICCategory category = findCategory (objectInstanceId);
		if (category == null) {
			category = ReportBusiness.createReportCategory (objectInstanceId);
		}
		final int categoryId = ((Integer)category.getPrimaryKey()).intValue();
		
		// 3. save report
		//final ReportEntityHandler business = new ReportEntityHandler ();
		final Report report = ReportEntityHandler.saveReport
				(reportName, null, columnHeaders, sql, categoryId);
		
		// 4. show feedback to user
		final Table table = new Table ();
		int row = 1;
		table.add (getHeader ("Ny rapportgenerator"), 1, row++);
		table.add (getSmallHeader ("Id:"), 1, row++);
		table.add (getSmallText (report.getPrimaryKey () + ""), 1, row++);
		table.add (getSmallHeader ("Namn:"), 1, row++);
		table.add (getSmallText (report.getName ()), 1, row++);
		table.add (getSmallHeader ("SQL:"), 1, row++);
		table.add (getSmallText (report.getSQL ()), 1, row++);
		table.add (getSmallHeader ("Kolumnrubriker:"), 1, row++);
		table.add (getSmallText (report.getHeader ()), 1, row++);
		add (table);
	}
	
	
	/**
	 * @return report if available as either a session attribute or else in the
	 * database stored on this objectInstanceId
	 */
	private Report findReport (final IWContext context) {
		final HttpSession session = context.getSession ();
		final int objectInstanceId = getICObjectInstanceID ();
		Report result = (Report) session.getAttribute (CURRENT_REPORT_KEY
																									 + objectInstanceId);
		if (result == null) {
			// there was no report in the session object
			final ICCategory category = findCategory (getICObjectInstanceID ());
			if (category != null) {
				// a category were found, let's look for reports in it
				final int categoryId = ((Integer)category.getPrimaryKey()).intValue();
				final Report [] reports	= ReportEntityHandler.findReports (categoryId);
				if (reports.length > 0) {
					// at least one report was found in this category
					result = reports [0];
					session.setAttribute (CURRENT_REPORT_KEY + objectInstanceId, result);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * @return ICCategory if available for this objectInstanceId or else null
	 */
	private ICCategory findCategory (final int objectInstanceId) {
		ICCategory result = null;
		List categoryList = null;
		try {
			categoryList = ReportFinder.listOfEntityForObjectInstanceId
					(objectInstanceId);
		} catch (final Exception e) {
			logWarning
					("ReportFinder.listOfEntityForObjectInstanceId (" + objectInstanceId
					 + ") -> " + e.getMessage ());
		}
		if (categoryList != null && categoryList.size () > 0) {
			result = (ICCategory) categoryList.iterator ().next ();
		}
		return result;
	}
	
	/**
	 * @return fetches data from db according to this sql statement
	 */
	private Table getFetchTable (final String sql)
		throws Fetcher.FetchException {
		final Fetcher.FetchResult result = Fetcher.fetchFromDatabase (sql, 5);
		final String [][] data = result.getData ();
		final Table table = new Table ();
		final int rowCount = data.length;
		if (1 > rowCount) {
			table.add (getSmallText (localize (NO_ROWS_FOUND_KEY, NO_ROWS_FOUND_KEY)),
								 1, 1);
			return table;
		}
		final int colCount = data [0].length;
		table.setColumns (colCount);
		table.setCellpadding (getCellpadding ());
		table.setCellspacing (getCellspacing ());
		table.setWidth(getWidth());
		
		// show headers and column name inputs
		table.setRowColor (1, getHeaderColor());
		final String [] columnLabels = result.getColumnLabels ();
		for (int col = 0; col < colCount; col++) {
			final HiddenInput hidden = new HiddenInput (COLUMN_NAME_KEY + col,
																									columnLabels [col]);
			table.add (hidden, col + 1, 1);
			table.add (getSmallHeader (columnLabels [col]), col + 1, 1);
		}
		
		// show fetched content
		for (int row = 0; row < rowCount; row++) {
			table.setRowColor(row + 2, row % 2 == 0	? getZebraColor1()
												: getZebraColor2());
			for (int col = 0; col < colCount; col++) {
				table.add (getSmallText(data [row][col]), col + 1, row + 2);
			}
		}
		return table;
	}
	
	private static String getSqlParameter (final HttpSession session) {
		final String original = (String) session.getAttribute (SQL_QUERY_KEY);
		if (original == null) return "";
		final StringBuffer result = new StringBuffer (original);
		for (int i = 0; i < result.length (); i++) {
			if (Character.isWhitespace (result.charAt(i))) {
				result.setCharAt (i, ' ');
			}
		}
		
		return result.toString ();
	}
	
	/**
	 * Convinience method for accessing the localized version of a string
	 */
	private String getLocalizedString(final String key, final String value) {
		return getResourceBundle().getLocalizedString(key, value);
	}
}
