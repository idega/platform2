/*
 * $Id: ChildCareExport.java,v 1.3 2005/02/14 19:33:06 anders Exp $
 *
 * Copyright (C) 2005 Idega. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf & Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.export.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.export.business.ChildCareExportBusiness;
import se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.core.file.data.ICFile;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/** 
 * This idegaWeb block exports child care placements to text files
 * for the IST Extens system.
 * <p>
 * Last modified: $Date: 2005/02/14 19:33:06 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class ChildCareExport extends CommuneBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_EXPORT_PLACEMENTS = 1;
	private final static int ACTION_EXPORT_TAXEKATS = 2;
	
	private final static String PP = "cc_export_"; // Parameter prefix 

	private final static String PARAMETER_PLACEMENT_EXPORT = PP + "pe";
	private final static String PARAMETER_TAXEKAT_EXPORT = PP + "te";
	private final static String PARAMETER_FROM_DATE = PP + "fd";
	private final static String PARAMETER_TO_DATE = PP + "td";
	private final static String PARAMETER_TAXEKAT = PP + "tk";

	private final static String KP = "cc_export."; // Localization key prefix 
	
	private final static String KEY_EXPORT_PLACEMENTS = KP + "export_placements";
	private final static String KEY_EXPORT_TAXEKATS = KP + "export_taxekats";
	private final static String KEY_FROM_DATE_NOT_SET = KP + "from_date_not_set";
	private final static String KEY_TO_DATE_NOT_SET = KP + "to_date_not_set";
	private final static String KEY_FROM_DATE = KP + "from_date";
	private final static String KEY_TO_DATE = KP + "to_date";

	/**
	 * @see com.idega.presentation.Block#main()
	 */
	public void main(final IWContext iwc) {
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT:
					handleDefaultAction(iwc);
					break;
				case ACTION_EXPORT_PLACEMENTS:
					handleExportPlacements(iwc);
					break;
				case ACTION_EXPORT_TAXEKATS:
					handleExportTaxekats(iwc);
					break;
			}
		}
		catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		
		if (iwc.isParameterSet(PARAMETER_PLACEMENT_EXPORT)) {
			action = ACTION_EXPORT_PLACEMENTS;
		} else if (iwc.isParameterSet(PARAMETER_TAXEKAT_EXPORT)) {
			action = ACTION_EXPORT_TAXEKATS;
		}
		
		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) throws RemoteException {
		Form form = new Form();
		add(form);
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		DateInput fromInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_FROM_DATE));
		DateInput toInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_TO_DATE));
		table.add(getLocalizedHeader(KEY_FROM_DATE,"From"), 1, 1);
		table.add(fromInput, 2, 1);
		table.add(getLocalizedHeader(KEY_TO_DATE,"To"), 3, 1);
		table.add(toInput, 4, 1);
		form.add(table);

		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		SubmitButton placementExportButton = (SubmitButton) getStyledInterface(new SubmitButton(PARAMETER_PLACEMENT_EXPORT, 
				localize(KEY_EXPORT_PLACEMENTS, "Export Placements")));
		SubmitButton taxekatExportButton = (SubmitButton) getStyledInterface(new SubmitButton(PARAMETER_TAXEKAT_EXPORT, 
				localize(KEY_EXPORT_TAXEKATS, "Export Taxekats")));
		
		table.add(placementExportButton, 1, 1);
		table.add(taxekatExportButton, 2, 1);
		form.add(table);

		placementExportButton.setOnSubmitFunction("checkDate", getCheckDateScript());
		taxekatExportButton.setOnClick("javascript:findObj('" + PARAMETER_TAXEKAT + "').value = 'true';");
		form.add(new HiddenInput(PARAMETER_TAXEKAT, "false"));
		
		form.add(new Break());

		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 0;
		int col = 0;
		int p_row = 1;
		int t_row = 1;
		int p_col = 1;
		int t_col = 3;
		String pp = getChildCareExportBusiness(iwc).getPlacementExportFileNamePrefix();
		String tp = getChildCareExportBusiness(iwc).getTaxekatExportFileNamePrefix();
		Iterator iter = getChildCareExportBusiness(iwc).getAllExportFiles();
		if (iter != null) {
			while (iter.hasNext()) {
				ICFile file = (ICFile) iter.next();
				String fileName = file.getName();
				Link link = new Link(fileName + " (" + getChildCareExportBusiness(iwc).getFileDateInterval(fileName) + ")");
				link.setFile(file);
				if (fileName.substring(0, pp.length()).equals(pp)) {
					row = p_row;
					p_row++;
					col = p_col;					
				} else if (fileName.substring(0, tp.length()).equals(tp)) {
					row = t_row;
					t_row++;
					col = t_col;					
				} else {
					continue;
				}
				table.add(link, col, row);
			}
		}
		form.add(table);
	}
	
	/*
	 * Handles placement export.
	 */	
	private void handleExportPlacements(IWContext iwc) throws RemoteException {
		try {
			IWTimestamp to = new IWTimestamp(iwc.getParameter(PARAMETER_TO_DATE));
			getChildCareExportBusiness(iwc).exportPlacementFile(iwc.getCurrentUserId(), to.getDate());
		} catch (ChildCareExportException e) {
			log (e);
		}
		handleDefaultAction(iwc);
	}
	
	/*
	 * Handles taxekat export.
	 */	
	private void handleExportTaxekats(IWContext iwc) throws RemoteException {
		try {
			IWTimestamp from = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));
			IWTimestamp to = new IWTimestamp(iwc.getParameter(PARAMETER_TO_DATE));
			getChildCareExportBusiness(iwc).exportTaxekatFile(iwc.getCurrentUserId(), from.getDate(), to.getDate());
		} catch (ChildCareExportException e) {
			log (e);
		}
		handleDefaultAction(iwc);
	}

	/*
	 * Returns a child care export business instance.
	 */
	private ChildCareExportBusiness getChildCareExportBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareExportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareExportBusiness.class);
	}
	
	/*
	 * Returns script for checking date inputs for taxekat export.
	 */
	private String getCheckDateScript() {
		StringBuffer b = new StringBuffer();
		b.append("\n function checkDate() {");

		b.append("\n\t var message = '';");
		
		b.append("\n\t var dropToDay = ").append("findObj('").append(PARAMETER_TO_DATE + "_day").append("');");
		b.append("\n\t var dropToMonth = ").append("findObj('").append(PARAMETER_TO_DATE + "_month").append("');");
		b.append("\n\t var dropToYear = ").append("findObj('").append(PARAMETER_TO_DATE + "_year").append("');");
		b.append("\n\t var toDay = ").append("dropToDay.options[dropToDay.selectedIndex].value;");
		b.append("\n\t var toMonth = ").append("dropToMonth.options[dropToMonth.selectedIndex].value;");
		b.append("\n\t var toYear = ").append("dropToYear.options[dropToYear.selectedIndex].value;");
		
		b.append("\n\t if (toDay == '00' || toMonth == '00' || toYear == 'YY') {");
		b.append("\n\t\t message = '").append(localize(KEY_TO_DATE_NOT_SET, "To date must be selected.")).append("';");
		b.append("\n\t }");
		
		b.append("\n\t var dropFromDay = ").append("findObj('").append(PARAMETER_FROM_DATE + "_day").append("');");
		b.append("\n\t var dropFromMonth = ").append("findObj('").append(PARAMETER_FROM_DATE + "_month").append("');");
		b.append("\n\t var dropFromYear = ").append("findObj('").append(PARAMETER_FROM_DATE + "_year").append("');");
		b.append("\n\t var fromDay = ").append("dropFromDay.options[dropFromDay.selectedIndex].value;");
		b.append("\n\t var fromMonth = ").append("dropFromMonth.options[dropFromMonth.selectedIndex].value;");
		b.append("\n\t var fromYear = ").append("dropFromYear.options[dropFromYear.selectedIndex].value;");
		
		b.append("\n\t var taxekat = ").append("findObj('").append(PARAMETER_TAXEKAT).append("').value;");
		
		b.append("\n\t if (taxekat == 'true' && (fromDay == '00' || fromMonth == '00' || fromYear == 'YY')) {");
		b.append("\n\t\t message = '").append(localize(KEY_FROM_DATE_NOT_SET, "From date must be selected.")).append("';");
		b.append("\n\t }");
		
		b.append("\n\t if (message != '') {");
		b.append("\n\t\t alert(message);");
		b.append("\n\t\t return false;");
		b.append("\n\t }");
		
		b.append("\n\t return true;");

		b.append("\n }");
		
		return b.toString();
	}
}
