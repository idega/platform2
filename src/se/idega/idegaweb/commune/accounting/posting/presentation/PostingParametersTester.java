/*
 * $Id: PostingParametersTester.java,v 1.6 2003/09/08 08:10:07 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.presentation;

import java.rmi.RemoteException;
import java.sql.Date;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.ui.Form;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;

/**
 * PostingParametersTest is an idegaWeb block that is used to test the Posting parameters retrieval 
 *  
 * <p>
 * $Id: PostingParametersTester.java,v 1.6 2003/09/08 08:10:07 laddi Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Version$
 */
public class PostingParametersTester extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_SEARCH = 1;
	private Form mainForm = null;

	private final static String PARAM_BUTTON_SEARCH = "button_search";

	private final static String PARAM_FIELD_ACTIVITY = "field_activity";
	private final static String PARAM_FIELD_REGSPEC = "field_regspec";
	private final static String PARAM_FIELD_COMPANY_TYPE = "field_company_type";
	private final static String PARAM_FIELD_COM_BELONGING = "field_com_belonging";
	private final static String PARAM_FIELD_DATE = "field_date";

	private String _errorMessage = "";

	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			prepareMainTable();
			switch (action) {
				case ACTION_DEFAULT :
					viewMainForm(iwc);
					break;
				case ACTION_SEARCH :
					_errorMessage = "";
					viewMainForm(iwc);
					viewSearchedResults(iwc);
					break;
			}
			add(mainForm);
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		if (iwc.isParameterSet(PARAM_BUTTON_SEARCH)) {
			action = ACTION_SEARCH;
		}
		return action;
	}

	private void viewSearchedResults(IWContext iwc) {
		Table table = new Table();
		int row = 1;

		PostingParameters pp = searchPostingParameter(iwc);

		if (pp == null) {
			_errorMessage = "Hittades ej";
		}
		if (_errorMessage.length() != 0) {
			table.add(getSmallErrorText(_errorMessage), 1, row);
			mainForm.add(table);
			return;
		}

		table.add(getSmallText(""), 1, row);

		table.add(getSmallText("Egen konteringssträng"), 1, row);
		table.add(getSmallText(pp.getPostingString()), 2, row++);

		table.add(getSmallText("Motkonteringsträng"), 1, row);
		table.add(getSmallText(pp.getDoublePostingString()), 2, row++);

		mainForm.add(table);
	}

	private void viewMainForm(IWContext iwc) {
		Table table = new Table();
		Date dd = new Date(System.currentTimeMillis());

		try {
			table.add(getLocalizedLabel("posting_test_date", "Datum"), 1, 1);
			table.add(getTextInput(PARAM_FIELD_DATE, formatDate(dd, 10)), 2, 1);

			table.add(getLocalizedLabel("posting_test_activity", "Verksamhet"), 1, 2);
			table.add(getTextInput(PARAM_FIELD_ACTIVITY, iwc.isParameterSet(PARAM_FIELD_ACTIVITY) ? iwc.getParameter(PARAM_FIELD_ACTIVITY) : ""), 2, 2);
			//			table.add(getSmallText("keys: " +rBiz.getActivityTypesAsString()), 3, 2);

			table.add(getLocalizedLabel("posting_test_regspec", "Regelspec. typ"), 1, 3);
			table.add(getTextInput(PARAM_FIELD_REGSPEC, iwc.isParameterSet(PARAM_FIELD_REGSPEC) ? iwc.getParameter(PARAM_FIELD_REGSPEC) : ""), 2, 3);
			//			table.add(getSmallText("keys: "+rBiz.getRegulationSpecTypesAsString()), 3, 3);

			table.add(getLocalizedLabel("posting_test_company_type", "Bolagstyp"), 1, 4);
			table.add(getTextInput(PARAM_FIELD_COMPANY_TYPE, iwc.isParameterSet(PARAM_FIELD_COMPANY_TYPE) ? iwc.getParameter(PARAM_FIELD_COMPANY_TYPE) : ""), 2, 4);
			//			table.add(getSmallText("keys: "+rBiz.getCompanyTypesAsString()), 3, 4);

			table.add(getLocalizedLabel("posting_test_com_bel_type", "Kommuntillhörighet"), 1, 5);
			table.add(getTextInput(PARAM_FIELD_COM_BELONGING, iwc.isParameterSet(PARAM_FIELD_COM_BELONGING) ? iwc.getParameter(PARAM_FIELD_COM_BELONGING) : ""), 2, 5);
			//			table.add(getSmallText("keys: "+rBiz.getCommuneBelongingsAsString()), 3, 5);

			table.add(getLocalizedButton(PARAM_BUTTON_SEARCH, "posting_test_search", "Sök"), 2, 6);
		}
		catch (Exception e) {
			_errorMessage = e.getMessage();
			return;
		}

		mainForm.add(table);
	}

	private PostingParameters searchPostingParameter(IWContext iwc) {
		PostingBusiness pBiz;
		PostingParameters pp = null;
		try {
			pBiz = getPostingBusiness(iwc);

			pp = pBiz.getPostingParameter(parseDate(iwc.getParameter(PARAM_FIELD_DATE)), Integer.parseInt(iwc.getParameter(PARAM_FIELD_ACTIVITY)), Integer.parseInt(iwc.getParameter(PARAM_FIELD_REGSPEC)), Integer.parseInt(iwc.getParameter(PARAM_FIELD_COMPANY_TYPE)), Integer.parseInt(iwc.getParameter(PARAM_FIELD_COM_BELONGING)));
		}
		catch (PostingParametersException e) {
			_errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}
		catch (RemoteException e) {
		}
		return pp;
	}

	private void prepareMainTable() {
		mainForm = new Form();
	}

	private PostingBusiness getPostingBusiness(IWContext iwc) throws RemoteException {
		return (PostingBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}
}
