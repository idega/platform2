/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public class GroupSelector extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		if (modinfo.getRequest().getParameter("cmd") == null)
			showGroups(modinfo);
		else if (modinfo.getRequest().getParameter("cmd").equals("submit")) {
			String[] strGroupIDArr = modinfo.getRequest().getParameterValues("group_");
			modinfo.getResponse().getWriter().print("Subbari");
			if (strGroupIDArr != null) {
				Integer[] intArr = new Integer[strGroupIDArr.length];
				for (int i = 0; i < strGroupIDArr.length; i++) {
					intArr[i] = new Integer(strGroupIDArr[i]);
				}
				modinfo.getSession().setAttribute("group_id", intArr);
				getParentPage().close();
			}
		}
	}

	public void showGroups(IWContext modinfo) throws IOException, SQLException {
		Group group = (Group) IDOLookup.instanciateEntity(Group.class);
		Group[] groupArr = (Group[]) group.findAll();

		Group[] groupArrSelected = (Group[]) modinfo.getSessionAttribute("group_array");
		//modinfo.getSession().removeAttribute("group_array");
		Text headerText = new Text("Velja flokka");
		headerText.setBold();
		headerText.setFontColor("white");
		headerText.setFontFace("Arial");
		SubmitButton submit = new SubmitButton("     Vista     ");
		CloseButton close = new CloseButton("Hætta við");
		Form form = new Form();
		form.setAction(modinfo.getRequestURI() + "?cmd=submit");
		Table table = new Table(3, 5);
		table.mergeCells(1, 1, 3, 1);
		table.setColumnAlignment(2, "center");
		table.add(headerText, 1, 1);
		table.add("*Ath. að halda ctrl inni til að halda fyrri völdum", 2, 2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setRowColor(1, "#8ab490");

		for (int i = 2; i < 6; i++)
			table.setRowColor(i, "#ADBDB3");

		SelectionBox groupBox = new SelectionBox(groupArr);

		if (groupArrSelected != null) {
			//modinfo.getResponse().getWriter().print(groupArrSelected[0]);
			for (int i = 0; i < groupArrSelected.length; i++) {
				groupBox.setSelectedElement(String.valueOf(groupArrSelected[i].getID()));
			}
		}
		groupBox.setHeight(10);
		groupBox.keepStatusOnAction();
		table.add(close, 2, 5);
		table.add(submit, 2, 5);
		table.add(groupBox, 2, 3);
		form.add(table);
		add(form);
	}
}