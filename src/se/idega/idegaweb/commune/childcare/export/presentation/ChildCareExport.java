/*
 * $Id: ChildCareExport.java,v 1.1 2005/01/12 13:11:54 anders Exp $
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
import com.idega.presentation.ui.SubmitButton;

/** 
 * This idegaWeb block exports child care placements to text files
 * for the IST Extens system.
 * <p>
 * Last modified: $Date: 2005/01/12 13:11:54 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ChildCareExport extends CommuneBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_EXPORT_PLACEMENTS = 1;
	private final static int ACTION_EXPORT_TAXEKATS = 2;
	
	private final static String PP = "cc_export_"; // Parameter prefix 

	private final static String PARAMETER_PLACEMENT_EXPORT = PP + "pe";
	private final static String PARAMETER_TAXEKAT_EXPORT = PP + "te";

	private final static String KP = "cc_export."; // Localization key prefix 
	
	private final static String KEY_EXPORT_PLACEMENTS = KP + "export_placements";
	private final static String KEY_EXPORT_TAXEKATS = KP + "export_taxekats";
	

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
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		SubmitButton elementarySchoolButton = new SubmitButton(PARAMETER_PLACEMENT_EXPORT, 
				localize(KEY_EXPORT_PLACEMENTS, "Export Placements"));
		SubmitButton highSchoolButton = new SubmitButton(PARAMETER_TAXEKAT_EXPORT, 
				localize(KEY_EXPORT_TAXEKATS, "Export Taxekats"));
		
		table.add(elementarySchoolButton, 1, 1);
		table.add(highSchoolButton, 2, 1);
		add(table);
		
		add(new Break());

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
				Link link = new Link(fileName);
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
		add(table);
	}
	
	/*
	 * Handles placement export.
	 */	
	private void handleExportPlacements(IWContext iwc) throws RemoteException {
		try {
			getChildCareExportBusiness(iwc).exportPlacementFile(iwc.getCurrentUserId());
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
			getChildCareExportBusiness(iwc).exportTaxekatFile(iwc.getCurrentUserId());
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
}
