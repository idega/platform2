/*
 * Created on 25.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;

import com.idega.block.school.business.SchoolAreaComparator;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareStatistics extends ChildCareBlock {

	protected static final String PARAMETER_ACTION = "cc_action";

	protected static final int ORDER_BY_AREA = 1;
	protected static final int ORDER_BY_PROVIDER = 2;
	
	private int _action = ORDER_BY_AREA;
	
	private boolean _useSorting = false;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		
		add(getNavigationTable());
		add(new Break());
		
		switch (_action) {
			case ORDER_BY_AREA :
				add(getAreaTable(iwc));
				break;
			case ORDER_BY_PROVIDER :
				add(getProviderTable(iwc));
				break;
		}
	}
	
	private Table getAreaTable(IWContext iwc) throws RemoteException {
		Table table = getTable(3);
		int row = 2;
		int column = 1;
		
		List areas = new Vector(getBusiness().getSchoolBusiness().findAllSchoolAreas());
		if (areas != null && !areas.isEmpty()) {
			if (_useSorting)
				Collections.sort(areas, new SchoolAreaComparator(iwc.getCurrentLocale()));
			
			Iterator iter = areas.iterator();
			while (iter.hasNext()) {
				column = 1;
				SchoolArea element = (SchoolArea) iter.next();
				int areaID = ((Integer)element.getPrimaryKey()).intValue();
				
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				table.add(getSmallText(element.getSchoolAreaName()), column++, row);
				table.add(getSmallText(String.valueOf(getBusiness().getQueueByArea(areaID))), column++, row);
				table.add(getSmallText(String.valueOf(getBusiness().getQueueTotalByArea(areaID))), column++, row++);
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	private Table getProviderTable(IWContext iwc) throws RemoteException {
		Table table = getTable(6);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 2;
		int column = 1;

		table.add(getLocalizedSmallHeader("child_care.prognosis_3m","Prognosis (3M)"), 4, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_12m","Prognosis (12M)"), 5, 1);
		table.add(getLocalizedSmallHeader("child_care.last_updated","Last updated"), 6, 1);

		List providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByType(getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare()));
		if (providers != null && !providers.isEmpty()) {
			School school;
			ChildCarePrognosis prognosis;
			int providerID = -1;
			
			if (_useSorting)
				Collections.sort(providers, new SchoolComparator(iwc.getCurrentLocale()));
			
			Iterator iter = providers.iterator();
			while (iter.hasNext()) {
				column = 1;
				school = (School) iter.next();
				providerID = ((Integer)school.getPrimaryKey()).intValue();
				prognosis = getBusiness().getPrognosis(providerID);
				
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				table.add(getSmallText(school.getSchoolName()), column++, row);
				table.add(getSmallText(String.valueOf(getBusiness().getQueueByProvider(providerID))), column++, row);
				table.add(getSmallText(String.valueOf(getBusiness().getQueueTotalByProvider(providerID))), column++, row);
				if (prognosis != null) {
					table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPrognosis())), column++, row);
					table.add(getSmallText(String.valueOf(prognosis.getOneYearPrognosis())), column++, row);
					table.add(getSmallText(new IWTimestamp(prognosis.getUpdatedDate()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row++);
				}
				else {
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row++);
				}
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	private Table getTable(int columns) {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(columns);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.order","Order"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row++);

		return table;
	}
	
	private Form getNavigationTable() {
		Form form = new Form();
		
		DropdownMenu menu = new DropdownMenu(PARAMETER_ACTION);
		menu.addMenuElement(ORDER_BY_AREA, localize("child_care.show_area_statistics","Show by area"));
		menu.addMenuElement(ORDER_BY_PROVIDER, localize("child_care.show_provider_statistics","Show by provider"));
		menu.setSelectedElement(_action);
		menu.setToSubmit();
		form.add(menu);
		
		return form;
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
	}
	
	/**
	 * @param useSorting
	 */
	public void setUseSorting(boolean useSorting) {
		_useSorting = useSorting;
	}

}