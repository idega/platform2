package se.idega.idegaweb.commune.school.presentation;

import se.idega.idegaweb.commune.accounting.school.presentation.ProviderEditor;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Window;

/**
 * @author borgman
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CentralPlacementFloatingWindow extends Window {

	// *** Localization keys ***
//	private static final String KP = "central_placement_floating_window.";
//	private static final String KEY_WINDOW_HEADING = KP + "Floating edit window from Central Placement Editor";
	Table mainTable = null;
	int mainTableRow = 1;
	
	public CentralPlacementFloatingWindow() {
		this.setWidth(800);
		this.setHeight(400);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
	}
	
	private Table getMainTable() {
		Table mainTable = new Table();
		mainTable.setBorder(0);
		mainTable.setCellpadding(2);
		mainTable.setCellspacing(0);
		int col = 1;
		mainTableRow = 1;
		
		//  *** WINDOW HEADING ***
		mainTable.add(
			//getLocalizedSmallHeader(KEY_WINDOW_HEADING, "Floating edit window from Central Placement Editor"),
		"Floating edit window from Central Placement Editor", 
																														col, mainTableRow);
		mainTable.setColor(col, mainTableRow, "#0000FF");
		mainTable.setAlignment(col, mainTableRow, Table.HORIZONTAL_ALIGN_CENTER);
		mainTable.setRowVerticalAlignment(mainTableRow, Table.VERTICAL_ALIGN_MIDDLE);
		mainTable.setRowHeight(mainTableRow++, "20");

		return mainTable;
	}
	
	private void setMainTableContent(PresentationObject obj) {
		int col = 1;
		mainTable.add(obj, col, mainTableRow++);
	}



	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		mainTable = getMainTable();
		setMainTableContent(new ProviderEditor());
		add(mainTable);
	}
	
}
