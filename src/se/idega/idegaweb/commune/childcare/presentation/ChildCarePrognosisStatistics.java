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

import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.CloseButton;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCarePrognosisStatistics extends ChildCareBlock {

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		add(getProviderTable(iwc));
		add(new Break());
		
		CloseButton close = (CloseButton) getButton(new CloseButton(localize("close", "Close")));
		add(close);
	}

	private Table getProviderTable(IWContext iwc) throws RemoteException {
		Table table = getTable(7);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 2;
		int column = 1;

		table.add(getLocalizedSmallHeader("child_care.prognosis_3m","Prognosis (3M)"), 3, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_priority_3m","Priority (3M)"), 4, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_12m","Prognosis (12M)"), 5, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_priority_12m","Priority (12M)"), 6, 1);
		table.add(getLocalizedSmallHeader("child_care.last_updated","Last updated"), 7, 1);

		List providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByType(getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare()));
		if (providers != null && !providers.isEmpty()) {
			School school;
			ChildCarePrognosis prognosis;
			int providerID = -1;
			
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
				//table.add(getSmallText(String.valueOf(getBusiness().getQueueByProvider(providerID))), column++, row);
				table.add(getSmallText(String.valueOf(getBusiness().getQueueTotalByProvider(providerID))), column++, row);
				if (prognosis != null) {
					table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPrognosis())), column++, row);
					if (prognosis.getThreeMonthsPriority() != -1)
						table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPriority())), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					table.add(getSmallText(String.valueOf(prognosis.getOneYearPrognosis())), column++, row);
					if (prognosis.getOneYearPriority() != -1)
						table.add(getSmallText(String.valueOf(prognosis.getOneYearPriority())), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
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
		table.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_CENTER);
		
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
		table.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row++);

		return table;
	}
}