/*
 * Created on 1.5.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * @author laddi
 */
public class ChildCareQueueStatistics extends ChildCareBlock {

	protected final int DBV_WITH_PLACE = 0;
	protected final int DBV_WITHOUT_PLACE = 1;
	protected final int FS_WITH_PLACE = 2;
	protected final int FS_WITHOUT_PLACE = 3;

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table table = new Table(3, 4);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 2;
		
		table.add(getLocalizedSmallHeader("child_care.total","Total"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.updated","Updated"), column++, row++);
		column = 1;
		
		table.add(getLocalizedSmallText("child_care.all_children_in_queue","All children in queue"), column++, row);
		table.add(getSmallText(String.valueOf(getBusiness().getOldQueueTotal(null, false))), column++, row);
		table.add(getSmallText(String.valueOf(getBusiness().getOldQueueTotal(null, true))), column++, row++);
		column = 1;
		
		String[] placedType = { String.valueOf(DBV_WITH_PLACE), String.valueOf(FS_WITH_PLACE) };
		table.add(getLocalizedSmallText("child_care.placed_children_in_queue","Children with placement"), column++, row);
		table.add(getSmallText(String.valueOf(getBusiness().getOldQueueTotal(placedType, false))), column++, row);
		table.add(getSmallText(String.valueOf(getBusiness().getOldQueueTotal(placedType, true))), column++, row++);
		column = 1;
		
		String[] nonPlacedType = { String.valueOf(DBV_WITHOUT_PLACE), String.valueOf(FS_WITHOUT_PLACE) };
		table.add(getLocalizedSmallText("child_care.non_placed_children_in_queue","Children without placement"), column++, row);
		table.add(getSmallText(String.valueOf(getBusiness().getOldQueueTotal(nonPlacedType, false))), column++, row);
		table.add(getSmallText(String.valueOf(getBusiness().getOldQueueTotal(nonPlacedType, true))), column++, row++);

		add(table);
	}
}