/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import java.io.IOException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Family;
import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public class MemberFamilyCreator extends GolfBlock {


	public void main(IWContext modinfo) throws Exception {
		if (modinfo.getRequest().getParameter("cmd") == null)
			newFamily(modinfo);
		else if (modinfo.getRequest().getParameter("cmd").equals("submit")) {
			String familyName = modinfo.getRequest().getParameter("family_name");
			if (familyName != null && !familyName.equals("")) {
				String familyId = insertFamily(modinfo);
				System.out.print(familyId);
				modinfo.getSession().setAttribute("family_id", familyId);
				getParentPage().close();
			}
		}
	}

	public void newFamily(IWContext modinfo) throws IOException {
		Table table = new Table(3, 5);
		table.setAlignment("center");
		Text headerText = new Text("Nýskra fjölskyldu");
		Text nameText = new Text("Nafn");
		headerText.setBold();
		headerText.setFontColor("white");
		headerText.setFontFace("Arial");
		nameText.setFontFace("Arial");

		for (int i = 2; i < 6; i++) {
			table.setRowColor(i, "#ADBDB3");
		}

		table.setRowColor(1, "#8ab490");
		//table.setBorder(1);

		table.setCellpadding(0);
		table.setCellspacing(0);
		//table.setWidth(300);
		table.setColumnAlignment(2, "right");
		table.mergeCells(1, 1, 2, 1);
		//table.mergeCells(2, 3, 3, 3);

		TextInput input = new TextInput("family_name");
		input.setSize(40);
		Form form = new Form();
		form.setAction(modinfo.getRequest().getRequestURI() + "?cmd=submit");
		SubmitButton submit = new SubmitButton("     Vista     ");
		CloseButton close = new CloseButton("Hætta við");

		table.add(headerText, 1, 1);
		table.setRowAlignment(2, "left");
		table.add(nameText, 2, 2);

		table.add(input, 2, 3);
		table.add(close, 2, 5);
		table.add(submit, 2, 5);
		form.add(table);
		add(form);
	}

	//returns the family_id, whitch is being created
	public String insertFamily(IWContext modinfo) throws IOException {
		Family family = (Family) IDOLookup.createLegacy(Family.class);
		String familyName = modinfo.getRequest().getParameter("family_name");

		if (familyName != null) {
			try {
				family.setName(familyName);
				family.insert();
				return String.valueOf(family.getID());
			}
			catch (Exception e) {
				return e.getMessage();
			}
		}
		return "-1";
	}

}