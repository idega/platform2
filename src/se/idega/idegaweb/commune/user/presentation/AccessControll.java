/*
 * Created on 8.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.user.presentation;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.TextInput;



/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AccessControll extends CommuneBlock {

	private final String PAR_FIRSTNAME = "PAR_FIRSTNAME";
	private final String PAR_LASTNAME = "PAR_LASTNAME";
	private final String PAR_EMAIL = "PAR_EMAIL";
	private final String PAR_PHONE = "PAR_PHONE";
	private final String PAR_USERNAME = "PAR_PHONE";	
	private final String PAR_PASSWORD = "PAR_PHONE";	
	private final String PAR_PASSWORDCONFIRM = "PAR_PHONE";	
	private final String PAR_RIGHTS = "PAR_RIGHTS";	
	
	private final String[] LOCAL_FIRSTNAME = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.FIRSTNAME", "Förnamn:"};
	private final String[] LOCAL_LASTNAME = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.LASTNAME", "Efternamn:"};
	private final String[] LOCAL_EMAIL = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.EMAIL", "Email:"};
	private final String[] LOCAL_PHONE = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.PHONE", "Telefon:"};
	private final String[] LOCAL_USERNAME = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.USERNAME", "Användarnamn:"};
	private final String[] LOCAL_PASSWORD = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.PASSWORD", "Lösen:"};
	private final String[] LOCAL_PASSWORDCONFIRM = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.PASSWORDCONFIRM", "Bekräfta Lösen:"};
	private final String[] LOCAL_RIGHTS_TITLE = new String[] {"se.idega.idegaweb.commune.user.presentation.AccessControll.RIGHTS_TITLE", "Tilldelas följande Rättigheter:"};
	
	private final int NUM_COLS = 2;
		
	public void main(IWContext iwc) throws Exception{
		
		Table table = new Table();
		table.add(new Text(localize(LOCAL_FIRSTNAME)), 1, 1);		
		table.add(new Text(localize(LOCAL_LASTNAME)), 2, 1);		
		table.add(new Text(localize(LOCAL_EMAIL)), 3, 1);		
		table.add(new Text(localize(LOCAL_PHONE)), 4, 1);		
		table.add(getStyledInterface(new TextInput(PAR_FIRSTNAME)), 1, 2);
		table.add(getStyledInterface(new TextInput(PAR_LASTNAME)), 2, 2);
		table.add(getStyledInterface(new TextInput(PAR_EMAIL)), 3, 2);
		table.add(getStyledInterface(new TextInput(PAR_PHONE)), 4, 2);
		
		Table rightsTable = new Table();
		
		rightsTable.setStyleAttribute("border: medium solid black");
		rightsTable.mergeCells(1, 1, NUM_COLS * 2, 1);
		rightsTable.add(new Text(localize(LOCAL_RIGHTS_TITLE)), 1, 1);
		
		//TODO: (roar) add rights checkboxes
		int numberOfRights = 13;

		int r = 1, row = 2, col = 1;
		while (r <= numberOfRights) {
			rightsTable.add(new CheckBox(PAR_RIGHTS + r), col++, row);
			rightsTable.add(new Text("Rights #" + r), col, row);			
			
			if (col == NUM_COLS * 2){
				col = 1;
				row++;
			} else {
				col++;	
			}
			
			r++;
		}
		rightsTable.setWidth("100%");
		rightsTable.setStyleAttribute("margin-top:10");
		rightsTable.setStyleAttribute("margin-bottom:10");
		table.mergeCells(1, 3, 4, 3);
		table.add(rightsTable, 1, 3);
		
		table.add(new Text(localize(LOCAL_USERNAME)), 1, 4);		
		table.add(new Text(localize(LOCAL_PASSWORD)), 2, 4);		
		table.add(new Text(localize(LOCAL_PASSWORDCONFIRM)), 3, 4);	
		table.add(getStyledInterface(new TextInput(PAR_USERNAME)), 1, 5);
		table.add(getStyledInterface(new TextInput(PAR_PASSWORD)), 2, 5);
		table.add(getStyledInterface(new TextInput(PAR_PASSWORDCONFIRM)), 3, 5);
		
		add(table);
	}	
}
