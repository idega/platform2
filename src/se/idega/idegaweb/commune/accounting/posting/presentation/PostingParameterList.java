/*
 * $Id: PostingParameterList.java,v 1.2 2003/08/20 13:16:39 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;		
import com.idega.builder.data.IBPage;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;


/**
 * PostingList is an idegaWeb block that handles maintenance of some default data (PostingParameters) 
 * that is used in a "posting". The block shows/edits a list of Periode, Activity, Regulation specs, 
 * Company types and Commune belonging. 
 * This list shows only the most essential values of the PostingParameter.
 * 
 * Other submodules will use this data to search for a match on 
 * Periode, Activity, Regulation sec, Company type and Commune belonging.
 * When you have a hit you can retrive accounting data such as accounts, resources, activity codes 
 * etc. These values are always mirrored in "Own entries" and "Double entries". See Book-Keeping
 * terms.
 * 
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingString;
 * <p>
 * $Id: PostingParameterList.java,v 1.2 2003/08/20 13:16:39 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.2 $
 */
public class PostingParameterList extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_EDIT = 1;
	
	private final static String KEY_HEADER = "posting_def_list.header";
	private final static String KEY_PERIOD = "posting_def_list.periode";
	private final static String KEY_PERIOD_SEARCH = "posting_def_list.periode_search";
	private final static String KEY_FROM_DATE = "posting_parm_list.from_date";
	private final static String KEY_TO_DATE = "posting_parm_list.to_date";
	private final static String KEY_ACTIVITY = "posting_parm_list.activity";
	private final static String KEY_REG_SPEC = "posting_parm_list.reg_spec";
	private final static String KEY_COMPANY_TYPE = "posting_parm_list.company_type";
	private final static String KEY_COMMUNE_BELONGING = "posting_parm_list.commune_belonging";
	private final static String KEY_OWN_ENTRY = "posting_parm_list.own_entry";
	private final static String KEY_DOUBLE_ENTRY = "posting_parm_list.double_entry";
	private final static String KEY_SEARCH = "posting_parm_list.search";
	private final static String KEY_COPY = "posting_parm_list.copy";
	private final static String KEY_NEW = "posting_parm_list.new";
	private final static String KEY_REMOVE = "posting_parm_list.remove";
	private final static String KEY_CANCEL = "posting_parm_list.cancel";

	private final static String PARAM_SEARCH = "button_search";
	private final static String PARAM_COPY = "button_copy";
	private final static String PARAM_NEW = "button_new";
	private final static String PARAM_REMOVE = "button_remove";
	private final static String PARAM_CANCEL = "button_cancel";

	private final static String PARAM_FROM = "param_from";
	private final static String PARAM_TO = "param_to";

	private IBPage editPage;

	/**
	 * Handles the property setEditPage
	 */
	public void setEditPage(IBPage page) {
		editPage = page;
	}

	/**
	 * Handles the property setEditPage
	 */
	public IBPage getEditPage() {
		return editPage;
	}


	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT :
					viewDefaultForm(iwc);
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
		return ACTION_DEFAULT;
	}

	/*
	 * Adds the default form to the block.
	 */	
	 
	private void viewDefaultForm(IWContext iwc) {
		ApplicationForm app = new ApplicationForm();
		
		Table searchPanel = getSearchPanel(localize(KEY_PERIOD_SEARCH, "Period:"),
									getFromToDatePanel(PARAM_FROM, PARAM_TO),
									localize(KEY_CANCEL, "Sök"));
		
		ListTable postingTable = getPostingTable(iwc);
					
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton(PARAM_COPY, localize(KEY_COPY, "Kopiera"));
		buttonPanel.addButton(PARAM_NEW, localize(KEY_NEW, "Ny"), editPage);
		buttonPanel.addButton(PARAM_REMOVE, localize(KEY_REMOVE, "Ta bort"));
		buttonPanel.addButton(PARAM_CANCEL, localize(KEY_CANCEL, "Avbryt"));
		
		app.setTitle(localize(KEY_HEADER, "Konteringlista"));
		app.setSearchPanel(searchPanel);
		app.setMainPanel(postingTable);
		app.setButtonPanel(buttonPanel);
		add(app);		
	}
	
	/*
	 * Returns the PostingList
	 */
	private ListTable getPostingTable(IWContext iwc) {
		
		PostingBusiness pBiz;
		ListTable list = new ListTable(7);

		list.setHeader(localize(KEY_PERIOD, "Period"), 1);
		list.setHeader(localize(KEY_ACTIVITY, "Verksamhet"), 2);
		list.setHeader(localize(KEY_REG_SPEC, "Regelspec. typ"), 3);
		list.setHeader(localize(KEY_COMPANY_TYPE, "Bolagstyp"), 4);
		list.setHeader(localize(KEY_COMMUNE_BELONGING, "Kommuntillhörighet"), 5);
		list.setHeader(localize(KEY_OWN_ENTRY, "Egen kontering"), 6);
		list.setHeader(localize(KEY_DOUBLE_ENTRY, "Motkontering"), 7);
		
		
		try {
			pBiz = getPostingBusiness(iwc);
			pBiz.findAllPostingParameters();

			Collection items = pBiz.findAllPostingParameters();
			if(items != null) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					PostingParameters p = (PostingParameters) iter.next();
					p.getActivity().getActivityType();
					p.getPeriodeFrom();
					
					list.add(p.getPeriodeFrom() + "-" + p.getPeriodeTo());
					list.add(localize(p.getActivity().getTextKey(),""));
					list.add(localize(p.getRegSpecType().getTextKey(), ""));
					list.add(localize(p.getCompanyType().getTextKey(), ""));
					list.add(localize(p.getCommuneBelonging().getTextKey(), ""));
					list.add("");
					list.add("");
				}
			}			
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	
		
		return list;
	}
	
	private Table getSearchPanel(String what, Table fields, String searchButtonText) {
		GenericButton search = (GenericButton) getButton(new GenericButton(PARAM_SEARCH, searchButtonText));
		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setWidth(getWidth());
		table.add(getSmallText(what), 1, 1);
		table.add(fields, 2, 1);
		table.add(search, 3, 1);
		return table;
	}

	private Table getFromToDatePanel(String from, String to) {
		Table table = new Table();
		DateInput fromDate = new DateInput(from);
		DateInput toDate = new DateInput(to);
		table.add(fromDate, 1, 1);
		table.add(new String("-"), 2, 1);
		table.add(toDate, 3, 1);
		return table;
	}

	private PostingBusiness getPostingBusiness(IWContext iwc) throws RemoteException {
		return (PostingBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}
}
