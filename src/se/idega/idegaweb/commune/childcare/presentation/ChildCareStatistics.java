/*
 * Created on 25.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareStatistics extends ChildCareBlock {

	protected static final String PARAMETER_AREA = "cc_area";
	protected static final String PARAMETER_ACTION = "cc_action";

	protected static final int ORDER_BY_ALL_CHOICES = 1;
	protected static final int ORDER_BY_FIRST_HAND_CHOICES = 2;
	
	private int _action = ORDER_BY_ALL_CHOICES;
	private int _areaID = -1;
	
	private boolean _useSorting = false;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		
		add(getNavigationTable(iwc));
		add(new Break());
		
		switch (_action) {
			case ORDER_BY_ALL_CHOICES :
				add(getAllProviderTable(iwc));
				break;
			case ORDER_BY_FIRST_HAND_CHOICES :
				add(getFirstHandProviderTable(iwc));
				break;
		}
	}
	
	private Table getFirstHandProviderTable(IWContext iwc) throws RemoteException {
		Table table = getTable(2);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		int column = 1;

		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.number_of_applications","Applications"), column++, row++);

		List providers = null;
		if (_areaID == -1) 
			providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByType(getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare()));
		else
			providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByAreaAndTypes(_areaID, getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare()));

		if (providers != null && !providers.isEmpty()) {
			School school;
			int providerID = -1;
			
			if (_useSorting)
				Collections.sort(providers, new SchoolComparator(iwc.getCurrentLocale()));
			
			Iterator iter = providers.iterator();
			while (iter.hasNext()) {
				column = 1;
				school = (School) iter.next();
				providerID = ((Integer)school.getPrimaryKey()).intValue();
				
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				table.add(getSmallText(school.getSchoolName()), column++, row);
				table.add(getSmallText(String.valueOf(getBusiness().getNumberOfFirstHandChoicesByProvider(providerID))), column++, row++);
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	private Table getAllProviderTable(IWContext iwc) throws RemoteException {
		Table table = getTable(6);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		int column = 1;

		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.order","Order"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.prognosis_3m","Prognosis (3M)"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.prognosis_12m","Prognosis (12M)"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.last_updated","Last updated"), column++, row++);

		List providers = null;
		if (_areaID == -1) 
			providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByType(getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare()));
		else
			providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByAreaAndTypes(_areaID, getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare()));

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

		return table;
	}
	
	private Form getNavigationTable(IWContext iwc) {
		Form form = new Form();
		
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_ACTION));
		menu.addMenuElement(ORDER_BY_ALL_CHOICES, localize("child_care.show_provider_statistics","Show by area"));
		menu.addMenuElement(ORDER_BY_FIRST_HAND_CHOICES, localize("child_care.show_first_hand_statistics","Show by first hand choices"));
		menu.setSelectedElement(_action);
		form.add(menu);
		
		DropdownMenu areas = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_AREA));
		areas.addMenuElement(-1, localize("child_care.all_areas","All areas"));
		try {
			List schoolAreas = new ArrayList(getBusiness().getSchoolBusiness().findAllSchoolAreas());
			Collections.sort(schoolAreas, new SchoolAreaComparator(iwc.getCurrentLocale()));
			Iterator iter = schoolAreas.iterator();
			while (iter.hasNext()) {
				SchoolArea element = (SchoolArea) iter.next();
				areas.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolAreaName());
			}
		}
		catch (RemoteException re) {
			re.printStackTrace(System.err);
		}
		areas.setSelectedElement(_areaID);
		form.add(Text.getNonBrakingSpace());
		form.add(areas);
		
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("child_care.get", "Get")));
		form.add(Text.getNonBrakingSpace());
		form.add(submit);
		
		return form;
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		if (iwc.isParameterSet(PARAMETER_AREA))
			_areaID = Integer.parseInt(iwc.getParameter(PARAMETER_AREA));
	}
	
	/**
	 * @param useSorting
	 */
	public void setUseSorting(boolean useSorting) {
		_useSorting = useSorting;
	}
}