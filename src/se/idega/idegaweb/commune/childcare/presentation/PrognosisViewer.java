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
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class PrognosisViewer extends ChildCareBlock {

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	
		
	public void init(IWContext iwc) throws Exception {
		if (getSession().hasPrognosis()) {
			add(getPrognosisTable(iwc));
			add(new Break());
			add(getProviderCapacity());
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
	
	public Table getProviderCapacity() throws RemoteException {
		Table table = new Table();
		table.setWidth(400);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(1);
		table.setRows(2);
		
		int row = 1;
		ChildCarePrognosis prognosis = getBusiness().getPrognosis(getSession().getChildCareID());
		boolean showVacancies = getBusiness().getUseVacancies();
		if (prognosis != null) {
			int provierCapacity = prognosis.getProviderCapacity();
			table.add(getSmallText(localize("child_care.provider_capacity","Provider capacity")+": "),1,row);
			if (provierCapacity != -1) {
				table.add(getSmallHeader(String.valueOf(provierCapacity)),1,row++);	
			}
			else {
				table.add(getSmallHeader(localize("child_care.provider_capacity_not_set","Capacity not set")),1,row++);
			}
			if (showVacancies){
				int vacancies = prognosis.getVacancies();
				table.add(getSmallText(localize("child_care.vacancies","Number of vacancies")+": "),1,row);
				if (vacancies != -1) {
					table.add(getSmallHeader(String.valueOf(vacancies)),1,row++);
				}
				else {
					table.add(getSmallHeader(localize("child_care.vacancies_not_set","Vacancies not set")),1,row++);
				}
			}
			
			String comments = prognosis.getProviderComments();
			table.add(getSmallText(localize("child_care.provider_comments", "Comments")+": "),1,row);
			if (comments != null) {
				table.add(getSmallText(String.valueOf(comments)),1,row++);
			}
			else {
				table.add(getSmallHeader(localize("child_care.comments_not_set","Comments not set")),1,row++);
			}
		}
		return  table;
	}
	
	
	
	public Table getPrognosisTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(4);
		table.setRows(3);
		table.setRowColor(1, getHeaderColor());
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		int row = 1;
		int column = 1;
		
		ChildCarePrognosis prognosis = getBusiness().getPrognosis(getSession().getChildCareID());
		if (prognosis != null) {
			IWTimestamp stamp = new IWTimestamp(prognosis.getUpdatedDate());
				
			table.add(getLocalizedSmallHeader("child_care.prognosis","Prognosis"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.placings","Placings"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.priorities","Priorities"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.last_updated","Last updated"), column, row++);
			column = 1;
			
			table.setRowColor(row, getZebraColor1());
			table.add(getLocalizedSmallText("child_care.three_months_prognosis","Three months prognosis"), column++, row);
			table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPrognosis())), column++, row);
			if (prognosis.getThreeMonthsPriority() != -1)
				table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPriority())), column++, row);
			else
				table.add(getSmallText(""), column++, row);
			table.add(getSmallText(stamp.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row++);
			column = 1;
			
			table.setRowColor(row, getZebraColor2());
			table.add(getLocalizedSmallText("child_care.one_year_prognosis","Twelve months prognosis"), column++, row);
			table.add(getSmallText(String.valueOf(prognosis.getOneYearPrognosis())), column++, row);
			if (prognosis.getOneYearPriority() != -1)
				table.add(getSmallText(String.valueOf(prognosis.getOneYearPriority())), column++, row);
			else
				table.add(getSmallText(""), column++, row);
			table.add(getSmallText(stamp.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row++);
		}
		
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
