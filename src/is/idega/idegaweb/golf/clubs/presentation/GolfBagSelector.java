/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.Window;

/**
 * @author laddi
 */
public class GolfBagSelector extends GolfBlock {

	SubmitButton leitaButton = new SubmitButton(localize("search","Search"));
	SubmitButton skraButton = new SubmitButton(localize("register","Register"));

	public void main(IWContext modinfo) throws Exception {
		getParentPage().setTitle("Pokamerki");

		String action = modinfo.getParameter("action");

		if (action == null) {
			action = "begin";
		}

		if (action.equalsIgnoreCase("begin")) {
			searchBySocialSecurityNumber(modinfo);
		}
		if (action.equalsIgnoreCase("getSearchBySocialSecurityNumberResults")) {
			getSearchBySocialSecurityNumberResults(modinfo);
		}
	}

	public void getSearchBySocialSecurityNumberResults(IWContext modinfo) throws SQLException {
		String socialSecurityNumbers = modinfo.getParameter("socialSecurityNumbers");
		Member[] theMembers = this.findMembersBySocialSecurityNumber(socialSecurityNumbers);
		drawTableWithMembers(modinfo, theMembers);
	}

	public void searchBySocialSecurityNumber(IWContext modinfo) {

		Form form = new Form();
		Table table2 = new Table(1, 3);
		table2.setWidth(200);
		table2.setAlignment("center");
		table2.setAlignment(1, 3, "right");
		table2.add("Sláðu inn kennitölur:", 1, 1);

		TextArea numberInput = new TextArea("socialSecurityNumbers");
		numberInput.setWidth(30);
		numberInput.setHeight(5);
		HiddenInput hidden = new HiddenInput("action", "getSearchBySocialSecurityNumberResults");

		table2.add(numberInput, 1, 2);
		table2.add(hidden, 1, 3);
		table2.add(leitaButton, 1, 3);
		form.add(table2);

		add("<br>");
		add(form);

	}

	public void drawTableWithMembers(IWContext modinfo, Member[] theMembers) {
		int tableHeight = 4;

		if (theMembers != null) {
			tableHeight += theMembers.length;
		}

		Window window = new Window("Pokamerki", 800, 800, "/clubs/bags.jsp");
		window.setMenubar(true);
		Form form = new Form(window);
		Table table = new Table(8, tableHeight);
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setWidth(2, "20");
		table.setWidth(4, "20");
		table.setWidth(6, "20");
		table.setAlignment("center");
		table.setAlignment(1, tableHeight, "center");
		table.mergeCells(1, 1, 8, 1);
		table.add("<u>Niðurstaða leitar:</u>", 1, 1);
		form.add(table);

		Member member = null;
		Union union = null;
		Member[] members = null;
		CheckBox checker = null;
		boolean error = false;
		boolean notFound = false;
		boolean doSearch = true;
		Text memberSocialNumber = null;
		Text memberName = null;
		Text link = null;
		int memberId;
		int row = 1;

		++row;
		table.add("Kennitala", 1, row);
		table.add("Nafn", 3, row);

		if (theMembers != null) {
			for (int i = 0; i < theMembers.length; i++) {
				++row;
				memberSocialNumber = new Text(theMembers[i].getSocialSecurityNumber());
				memberName = new Text(theMembers[i].getName());
				memberId = theMembers[i].getID();

				Window memberWindow = new Window("Meðlimur", 930, 630, "/clubs/member.jsp");
				memberWindow.setScrollbar(false);
				Link memberLink = new Link(memberName, memberWindow);
				memberLink.addParameter("member_id", theMembers[i].getID());

				checker = new CheckBox("member_id", memberId + "");
				checker.setChecked(true);

				link = new Text("Prenta pokamerki");
				table.add(memberSocialNumber, 1, row);
				table.add(memberLink, 3, row);

				table.add(link, 7, row);
				table.add(checker, 8, row);

				table.setAlignment(8, row, "right");
			}

			++row;
			++row;
			table.add(this.skraButton, 5, row);
			table.add(new HiddenInput("action", "registermarkedmembers"));
			table.setAlignment(5, row, "right");
			table.mergeCells(5, row, 8, row);
		}
		else {
			++row;
			table.add("Enginn fannst", 1, row);
		}
		table.resize(8, row);

		add("<br>");
		add(form);
	}

	public Member[] findMembersBySocialSecurityNumber(String socialSecurityNumbers) throws SQLException {
		StringTokenizer token = new StringTokenizer(socialSecurityNumbers, " \n\r\t\f,;:.+abcdefghijklmnopqrstuvwxyz");
		Vector vector = new Vector();
		while (token.hasMoreTokens()) {
			vector.addElement(token.nextToken());
		}
		return findMembersBySocialSecurityNumber(vector);
	}

	public Member[] findMembersBySocialSecurityNumber(Vector socialSecurityNumbers) throws SQLException {
		Member[] members = null;
		String securityNumber;
		String SQLString = "Select * from member where ";

		int numberInserted = 0;

		for (int i = 0; i < socialSecurityNumbers.size(); i++) {
			try {
				securityNumber = (String) socialSecurityNumbers.elementAt(i);
				if (securityNumber.equals("")) {
					securityNumber = "idega_engin_kennitala";
				}

				members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll("Select * from member where social_security_number = '" + securityNumber + "' ");
				if (members.length > 0) {
					if (numberInserted != 0) {
						SQLString += " OR ";
					}
					else {
						++numberInserted;
					}

					SQLString += "member_id = " + members[0].getID();
				}
			}
			catch (SQLException s) {
				s.printStackTrace(System.err);
			}
		}

		SQLString += "order by social_security_number";

		try {
			members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(SQLString);
		}
		catch (SQLException s) {
		}
		return members;

	}
}