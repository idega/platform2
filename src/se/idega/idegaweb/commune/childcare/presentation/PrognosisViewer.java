/*
 * Created on 25.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.GenericButton;

/**
 * @author laddi
 */
public class PrognosisViewer extends ChildCareBlock {

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().hasPrognosis()) {
			add(getPrognosisTable());
			add(new Break());
			add(getAdminButton());
		}
		else {
			if (getSession().hasOutdatedPrognosis())
				add(getSmallErrorText(localize("child_care.prognosis_outdated","Prognosis is outdated and needs to be set again!")));
			else
				add(getSmallErrorText(localize("child_care.prognosis_not_set","Prognosis have not been set!")));
			add(new Break());
			add(getAdminButton());
		}
	}
	
	public Table getPrognosisTable() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(2);
		table.setRows(3);
		table.setRowColor(1, getHeaderColor());
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		int row = 1;
		int column = 1;
		
		ChildCarePrognosis prognosis = getBusiness().getPrognosis(getSession().getChildCareID());
			
		table.add(getLocalizedSmallHeader("child_care.prognosis","Prognosis"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placings","Placings"), column, row++);
		column = 1;
		
		table.setRowColor(row, getZebraColor1());
		table.add(getLocalizedSmallText("child_care.three_months_prognosis","Three months prognosis"), column++, row);
		table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPrognosis())), column, row++);
		column = 1;
		
		table.setRowColor(row, getZebraColor2());
		table.add(getLocalizedSmallText("child_care.one_year_prognosis","Twelve months prognosis"), column++, row);
		table.add(getSmallText(String.valueOf(prognosis.getOneYearPrognosis())), column, row++);

		return table;
	}
	
	public GenericButton getAdminButton() {
		GenericButton updatePrognosis = (GenericButton) getStyledInterface(new GenericButton("update_prognosis", localize("child_care.set_prognosis","Set prognosis")));
		updatePrognosis.setWindowToOpen(ChildCareWindow.class);
		updatePrognosis.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_UPDATE_PROGNOSIS);
		updatePrognosis.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
		return updatePrognosis;
	}

}
