/* $Id: ControlList.java,v 1.13 2004/05/05 09:36:10 sigtryggur Exp $
*
* Copyright (C) 2003 Agura IT. All Rights Reserved.
*
* This software is the proprietary information of Agura IT AB.
* Use is subject to license terms.
*
*/
package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MediaWritable;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Window;
import com.idega.util.LocaleUtil;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.invoice.business.ControlListWriter;

import se.idega.idegaweb.commune.accounting.invoice.business.ControlListBusiness;
import se.idega.idegaweb.commune.accounting.invoice.business.ControlListException;

/**
 * ControlList
 * 
 * used to check the a batch run after it has completed
 * It presents:
 * Provider name
 * Number of childs last period
 * Number of childs this period
 * Amount paid last period
 * Amount paid this period
 * The list can also be presented as an Excel sheet
 * 
 * $Id: ControlList.java,v 1.13 2004/05/05 09:36:10 sigtryggur Exp $ 
 * <p>
 *
 * @author <a href="http://www.lindman.se">Kelly Lindman</a>
 */
public class ControlList extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_SEARCH = 1;
	
	private final static String KEY_PREFIX = "batch_reg_list."; 
		
	public final static String KEY_HEADER_OPERATION =  KEY_PREFIX + "header_operation";
	public final static String KEY_TITLE = KEY_PREFIX + "title";
	public final static String KEY_TALLY = KEY_PREFIX + "tally";
	public final static String KEY_COMPARE_PERIOD = KEY_PREFIX + "compare_period";
	public final static String KEY_WITH_PERIOD = KEY_PREFIX + "with_period";
	public final static String KEY_SEARCH = KEY_PREFIX + "search";
	public final static String KEY_PROVIDER = KEY_PREFIX + "provider";
	public final static String KEY_NUM_INDIVIDUALS_PREL = KEY_PREFIX + "num_individuals_prel";
	public final static String KEY_NUM_INDIVIDUALS_COMPARE_MONTH = KEY_PREFIX + "num_individuals_compare_month";
	public final static String KEY_TOTAL_AMOUNT_PREL = KEY_PREFIX + "total_amount_prel";
	public final static String KEY_TOTAL_AMOUNT_COMPARE_MONTH = KEY_PREFIX + "total_amount_compare_month";
	public final static String KEY_MEDIA_WINDOW_TITLE = KEY_PREFIX + "media_window_title";
	public final static String PARAMETER_SEARCH_PERIOD_COMPARE = "param_period_from";
	public final static String PARAMETER_SEARCH_PERIOD_CURRENT = "param_period_to";
	public final static String PARAMETER_SEARCH = "param_search";

	private String _errorMessage;
	private static final NumberFormat integerFormatter
		= NumberFormat.getIntegerInstance (LocaleUtil.getSwedishLocale ());
		
	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void init(final IWContext iwc) {
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT :
					handleDefaultAction(iwc);
					break;
				case ACTION_SEARCH :
					handleSearchAction(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		
		if (iwc.isParameterSet(PARAMETER_SEARCH)) {
			action = ACTION_SEARCH;
		}

		return action;
	}

	/*
	 * Handles default action with only search form 
	 */	
	private void handleDefaultAction(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Control list");
		app.setSearchPanel(getSearchPanel(iwc));
		add(app);
	}

	/*
	 * Handles search action with search form plus result and excel link 
	 */	
	private void handleSearchAction(IWContext iwc) {
		Date compareMonth = parseDate(iwc.getParameter(PARAMETER_SEARCH_PERIOD_COMPARE));
		Date withMonth = parseDate(iwc.getParameter(PARAMETER_SEARCH_PERIOD_CURRENT));
		_errorMessage = "";

		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Control list");
		app.setSearchPanel(getSearchPanel(iwc));
		app.setMainPanel(getControlList(iwc, compareMonth, withMonth));
		if(_errorMessage.length() == 0) {
			app.setButtonPanel(getButtonPanel(compareMonth, withMonth ));
		}
		add(app);
	}

	/*
	 * Returns the Control List
	 */
	private ListTable getControlList(IWContext iwc, Date compareMonth, Date withMonth) {

		ListTable list = new ListTable(this, 5);
		list.setLocalizedHeader(KEY_PROVIDER, "Provider", 1);
		list.setLocalizedHeader(KEY_NUM_INDIVIDUALS_PREL, "No of individuals Prel.", 2);
		list.setLocalizedHeader(KEY_NUM_INDIVIDUALS_COMPARE_MONTH, "Previous", 3);
		list.setLocalizedHeader(KEY_TOTAL_AMOUNT_PREL, "Total amount Prel.", 4);
		list.setLocalizedHeader(KEY_TOTAL_AMOUNT_COMPARE_MONTH, "Previous", 5);


		Collection collection = null;
		try {
			String operationalField = getSession().getOperationalField();
			collection = getControlListBusiness(iwc).getControlListValues(compareMonth, withMonth, operationalField);
		} catch (RemoteException e) {
		} catch (ControlListException e) {
			_errorMessage = localize(e.getTextKey(), e.getDefaultText());
			list.add(getErrorText(_errorMessage));
			return list;			
		}
				
		Iterator iter = collection.iterator();
		
		// render header
		int row = 1;
		Object[] header = (Object[]) iter.next();
		list.add(header[1] + "");
		list.add(header[2] + "");
		list.add(header[3] + "");
		list.add(header[4] + "");
		list.add(header[5] + "");
		row++;

		long sum1 = 0;
		long sum2 = 0;
		long sum3 = 0;
		long sum4 = 0;

		while (iter.hasNext()) {
			Object[] obj = (Object[]) iter.next();
			
			// get values
			final long currentMonthIndividualsCount = ((Long) obj[2]).longValue ();
			final long compareMonthIndividualsCount = ((Long) obj[3]).longValue ();
			final long currentMonthTotalAmount = ((Long) obj[4]).longValue ();
			final long compareMonthTotalAmount = ((Long) obj[5]).longValue ();

			// render row for one school
			list.add(getSmallText((String)(obj[1])));
			list.setColumnAlignment (2, Table.HORIZONTAL_ALIGN_RIGHT);
			list.add(integerFormatter.format (currentMonthIndividualsCount));
			list.setColumnAlignment (3, Table.HORIZONTAL_ALIGN_RIGHT);
			list.add(integerFormatter.format (compareMonthIndividualsCount));
			list.setColumnAlignment (4, Table.HORIZONTAL_ALIGN_RIGHT);
			list.add(integerFormatter.format (currentMonthTotalAmount));
			list.setColumnAlignment (5, Table.HORIZONTAL_ALIGN_RIGHT);
			list.add(integerFormatter.format (compareMonthTotalAmount));

			// acumulate to sum row
			sum1 += currentMonthIndividualsCount;
			sum2 += compareMonthIndividualsCount;
			sum3 += currentMonthTotalAmount;
			sum4 += compareMonthTotalAmount;
			row++;
		}

		// render empty row
		list.add(" ");
		list.add(" ");
		list.add(" ");
		list.add(" ");
		list.add(" ");

		// render sum row
		list.add(KEY_TALLY, "Total");
		list.setColumnAlignment (2, Table.HORIZONTAL_ALIGN_RIGHT);
		list.add (integerFormatter.format (sum1));
		list.setColumnAlignment (3, Table.HORIZONTAL_ALIGN_RIGHT);
		list.add (integerFormatter.format (sum2));
		list.setColumnAlignment (4, Table.HORIZONTAL_ALIGN_RIGHT);
		list.add (integerFormatter.format (sum3));
		list.setColumnAlignment (5, Table.HORIZONTAL_ALIGN_RIGHT);
		list.add (integerFormatter.format (sum4));
		list.skip ();

		return list;
	}


	/*
	 * Returns the search panel for this block.
	 */
	private Table getSearchPanel(IWContext iwc) {
		Table table = new Table();
		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "Huvudverksamhet"), 1, 1);
		table.add(new OperationalFieldsMenu(), 2, 1);
		table.add(getLocalizedLabel(KEY_COMPARE_PERIOD, "Compare month"), 1, 2);
		table.add(getTextInput(PARAMETER_SEARCH_PERIOD_COMPARE, getParameter(iwc, PARAMETER_SEARCH_PERIOD_COMPARE), 60), 2, 2);
		table.add(getLocalizedLabel(KEY_WITH_PERIOD, "with month"), 3, 2);
		table.add(getTextInput(PARAMETER_SEARCH_PERIOD_CURRENT,  getParameter(iwc, PARAMETER_SEARCH_PERIOD_CURRENT), 60), 4, 2);
		table.add(getLocalizedButton(PARAMETER_SEARCH, KEY_SEARCH, "Search"), 5, 2);
		return table;
	}	


	
	/*
	 * Returns the button panel for this block
	 */
	private ButtonPanel getButtonPanel(Date compareDate, Date withDate) {
		ButtonPanel bp = new ButtonPanel(this);
		Link excelLink = null;
		try {
			String operationalField = getSession().getOperationalField();
			excelLink = getXLSLink(ControlListWriter.class, 
					getBundle().getImage("shared/xls.gif"),
			 		compareDate,
			 		withDate, 
			 		operationalField
			);
		} catch (RemoteException e) {}
		bp.add(excelLink);
		
		return bp;
	}
	
	private Link getXLSLink(Class classToUse, Image image, Date compareDate, Date withDate, String opField) {
		Link link = new Link(image);
		link.setWindow(getFileWindow());
		link.addParameter(ControlListWriter.prmPrintType, ControlListWriter.XLS);
		link.addParameter(ControlListWriter.compareDate, compareDate.toString());
		link.addParameter(ControlListWriter.withDate, withDate.toString());
		link.addParameter(ControlListWriter.opField, opField);
		link.addParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(classToUse));
		return link;
	}
	public Window getFileWindow() {
		Window w = new Window(localize(KEY_MEDIA_WINDOW_TITLE, "Control list"), getIWApplicationContext().getIWMainApplication().getMediaServletURI());
		w.setResizable(true);
		w.setMenubar(true);
		w.setHeight(480);
		w.setWidth(640);
		return w;
	}

	private ControlListBusiness getControlListBusiness(IWContext iwc) throws RemoteException {
		return (ControlListBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ControlListBusiness.class);
	}	

}
