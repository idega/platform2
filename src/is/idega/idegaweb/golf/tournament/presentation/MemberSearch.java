/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class MemberSearch extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();
		String action = modinfo.getParameter("action");
		if (action == null)
			action = "";

		if (action.equals("") || action.equals("getSearch")) {
			add("<br>");
			searchByName(modinfo, iwrb);
			searchBySocialSecurityNumber(modinfo, iwrb);
		}
		else if (action.equals("searchBySocialSecurityNumber")) {
			searchBySocialSecurityNumber(modinfo, iwrb);
		}
		else if (action.equals("getSearchBySocialSecurityNumberResults")) {
			getSearchBySocialSecurityNumberResults(modinfo, iwrb);
		}
		else if (action.equals("searchByName")) {
			searchByName(modinfo, iwrb);
		}
		else if (action.equals("getSearchByNameResults")) {
			getSearchByNameResults(modinfo, iwrb);
		}
		else {
		}

	}

	public void searchBySocialSecurityNumber(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		Form form = new Form();
		Table table2 = new Table(2, 3);
		table2.setWidth(200);
		table2.setAlignment("center");
		table2.setAlignment(2, 3, "right");
		table2.add(iwrb.getLocalizedString("tournament.enter_social_security_number", "Enter social security number"), 1, 1);
		table2.mergeCells(1, 1, 2, 1);
		table2.mergeCells(1, 2, 2, 2);

		TextArea numberInput = new TextArea("socialSecurityNumbers");
		numberInput.setWidth(30);
		numberInput.setHeight(5);
		//SubmitButton socialResult = new
		// SubmitButton("Leita","action","getSearchBySocialSecurityNumberResults");
		HiddenInput hidden = new HiddenInput("action", "getSearchBySocialSecurityNumberResults");

		table2.add(numberInput, 1, 2);
		table2.add(hidden, 1, 3);
		GenericButton leitaButton = getButton(new SubmitButton(localize("tournament.search","Search")));
		table2.add(leitaButton, 2, 3);

		if (AccessControl.isClubAdmin(modinfo)) {
			int union_id = ((Member) (AccessControl.getMember(modinfo))).getMainUnionID();
			CheckBox checkBox = new CheckBox("search_for_member_in_union_id", "" + union_id);
			table2.add(iwrb.getLocalizedString("tournament.search_in_club", "Search in club only") + " ", 1, 3);
			table2.add(checkBox, 1, 3);
		}

		form.add(table2);
		add(form);

	}

	public void searchByName(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
		Form form = new Form();
		Table table2 = new Table(2, 3);
		table2.setWidth(200);
		table2.setAlignment("center");
		table2.setAlignment(2, 3, "right");
		table2.mergeCells(1, 1, 2, 1);
		table2.mergeCells(1, 2, 2, 2);
		table2.add(iwrb.getLocalizedString("tournament.enter_name", "Enter name:"), 1, 1);

		TextArea numberInput = new TextArea("name");
		numberInput.setWidth(30);
		numberInput.setHeight(5);
		HiddenInput hidden = new HiddenInput("action", "getSearchByNameResults");
		table2.add(numberInput, 1, 2);
		table2.add(hidden, 1, 2);
		if (AccessControl.isClubAdmin(modinfo)) {
			int union_id = ((Member) (AccessControl.getMember(modinfo))).getMainUnionID();
			CheckBox checkBox = new CheckBox("search_for_member_in_union_id", "" + union_id);
			table2.add(iwrb.getLocalizedString("tournament.search_in_club", "Search in club only") + " ", 1, 3);
			table2.add(checkBox, 1, 3);
		}
		GenericButton leitaButton = getButton(new SubmitButton(localize("tournament.search","Search")));
		table2.add(leitaButton, 2, 3);

		form.add(table2);
		add(form);

	}

	public void getSearchByNameResults(IWContext modinfo, IWResourceBundle iwrb) throws SQLException, RemoteException {
		String names = modinfo.getParameter("name");
		Member[] theMembers = this.findMembersByName(modinfo, names);
		drawTableWithMembers(modinfo, theMembers, iwrb);
	}

	public void getSearchBySocialSecurityNumberResults(IWContext modinfo, IWResourceBundle iwrb) throws SQLException, RemoteException {
		String socialSecurityNumbers = modinfo.getParameter("socialSecurityNumbers");
		Member[] theMembers = this.findMembersBySocialSecurityNumber(modinfo, socialSecurityNumbers);

		drawTableWithMembers(modinfo, theMembers, iwrb);
	}

	public void drawTableWithMembers(IWContext modinfo, Member[] theMembers, IWResourceBundle iwrb) throws RemoteException {
		int tableHeight = 5;
		int numberOfMember = 0;
		if (theMembers != null) {
			numberOfMember = theMembers.length;
		}

		if (theMembers != null) {
			tableHeight += theMembers.length;
		}

		Form form = new Form();
		Table table = new Table();
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setWidth(2, "20");
		table.setWidth(4, "20");
		table.setWidth(6, "20");
		//    table.setWidth(200);
		table.setAlignment("center");
		table.setAlignment(1, tableHeight, "center");
		table.mergeCells(1, 1, 8, 1);
		table.add(iwrb.getLocalizedString("tournament.search_results", "Search results"), 1, 1);
		form.add(table);

		Member member = null;
		Union union = null;
		Member[] members = null;
		CheckBox checker = null;
		boolean error = false;
		boolean notFound = false;
		boolean doSearch = true;
		String memberSocialNumber;
		String memberName;
		Link link = null;
		int memberId;
		int row = 1;

		++row;
		table.add("<u>" + iwrb.getLocalizedString("tournament.social_security_number", "Social security number") + "</u>", 1, row);
		table.add("<u>" + iwrb.getLocalizedString("tournament.name", "Name") + "</u>", 3, row);
		table.add("<u>" + iwrb.getLocalizedString("tournament.club", "Club") + "</u>", 5, row);
		table.add("<u>" + iwrb.getLocalizedString("tournament.handicap", "Handicap") + "</u>", 7, row);

		if (theMembers != null) {
			if (theMembers.length > 0) {
				for (int i = 0; i < theMembers.length; i++) {
					++row;
					memberSocialNumber = theMembers[i].getSocialSecurityNumber();
					memberName = theMembers[i].getName();
					memberId = theMembers[i].getID();

					checker = new CheckBox("checkedMemberId_" + memberId);

					link = new Link("Skrá í mót", modinfo.getRequestURI());
					link.addParameter("action", "registermarkedmembers");
					link.addParameter("checker", "true");
					link.addParameter("member_id", memberId + "");
					table.add(memberSocialNumber, 1, row);
					table.add(memberName, 3, row);
					try {
						union = is.idega.idegaweb.golf.business.GolfCacher.getCachedUnion(theMembers[i].getMainUnionID());
						table.add(union.getAbbrevation(), 5, row);
					}
					catch (Exception e) {
						e.printStackTrace(System.err);
						table.add("-", 5, row);
					}

					try {
						table.add(Float.toString(theMembers[i].getHandicap()), 7, row);
					}
					catch (Exception ex) {
						ex.printStackTrace(System.err);
						table.add("-", 7, row);
					}

					//table.add(link,7,row);
					//table.add(checker,8,row);

					table.add(new HiddenInput("member_id", memberId + ""), 2, row);
					table.setAlignment(8, row, "right");
				}

				++row;
				++row;
				table.add(getTournamentBusiness(modinfo).getBackLink(modinfo), 1, row);
				//    table.add(this.skraButton,5,row);
				table.add(new HiddenInput("action", "registermarkedmembers"));
				table.setAlignment(5, row, "right");
				table.mergeCells(5, row, 8, row);
			}
			else {
				++row;
				table.add(iwrb.getLocalizedString("tournament.no_one_was_found", "No one was found"), 1, row);
				++row;
				++row;
				table.add(getTournamentBusiness(modinfo).getBackLink(modinfo), 1, row);
			}
		}
		else {
			++row;
			table.add(iwrb.getLocalizedString("tournament.no_one_was_found", "No one was found"), 1, row);
			++row;
			++row;
			table.add(getTournamentBusiness(modinfo).getBackLink(modinfo), 1, row);
		}

		add(form);
	}

	public Member[] findMembersBySocialSecurityNumber(IWContext modinfo, String socialSecurityNumbers) throws SQLException {
		StringTokenizer token = new StringTokenizer(socialSecurityNumbers, " \n\r\t\f,;:.+abcdefghijklmnopqrstuvwxyz");
		Vector vector = new Vector();
		while (token.hasMoreTokens()) {
			vector.addElement(token.nextToken());
		}
		return findMembersBySocialSecurityNumber(modinfo, vector);
	}

	public Member[] findMembersByName(IWContext modinfo, String socialSecurityNumbers) throws SQLException {
		StringTokenizer token = new StringTokenizer(socialSecurityNumbers, "\n\r\t\f,;:.+");
		Vector vector = new Vector();
		while (token.hasMoreTokens()) {
			vector.addElement(token.nextToken());
		}
		return findMembersByName(modinfo, vector);
	}

	public Member[] findMembersByName(IWContext modinfo, Vector name) throws SQLException {
		Member[] members = null;
		Member[] tempMembers = null;
		StringTokenizer nameParts;
		String fullName = "";
		String firstName = "";
		String middleName = "";
		String lastName = "";
		int manyNames = 0;
		int numberInserted = 0;

		String union_id = modinfo.getParameter("search_for_member_in_union_id");

		String SQLString = "Select * from member where ";
		String tempSQLString = "";

		for (int i = 0; i < name.size(); i++) {
			try {
				manyNames = 0;
				fullName = "";
				firstName = "";
				middleName = "";
				lastName = "";
				tempSQLString = "";

				fullName = (String) name.elementAt(i);
				nameParts = new StringTokenizer(fullName, " ");
				manyNames = nameParts.countTokens();
				for (int j = 0; j < manyNames; j++) {
					if (j == 0) {
						firstName = nameParts.nextToken();
					}
					else if (j == manyNames - 1) {
						lastName = nameParts.nextToken();
					}
					else {
						middleName += nameParts.nextToken();
						if (j != manyNames - 2) {
							middleName += " ";
						}
					}
				}

				switch (manyNames) {
					case 0 :
						break;
					case 1 :
						tempSQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%'";
						break;
					case 2 :
						tempSQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%' and (last_name like '%" + lastName + "%' OR middle_name like '%" + lastName + "%')";
						break;
					default :
						tempSQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%' and middle_name like '%" + middleName + "%' and last_name like '%" + lastName + "%'";
						break;
				}

				if (!tempSQLString.equalsIgnoreCase("")) {
					if (union_id != null) {
						tempSQLString += " AND union_member_info.union_id = " + union_id + " AND union_member_info.MEMBERSHIP_TYPE = 'main'";
					}
					tempMembers = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(tempSQLString);
					for (int g = 0; g < tempMembers.length; g++) {
						if (numberInserted != 0) {
							SQLString += " OR ";
						}
						++numberInserted;
						SQLString += " member_id = " + tempMembers[g].getID();
						//System.out.println("Meðlimur númer "+tempMembers[g].getID()+"
						// fundinn : "+idegaTimestamp.RightNow().toSQLTimeString());
					}
				}
			}
			catch (Exception e) {

			}
		}
		if (union_id != null) {

		}

		if (numberInserted != 0) {
			SQLString += " order by first_name, last_name, middle_name";
			members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(SQLString);
			System.out.println("Done printing array" + IWTimestamp.RightNow().toSQLTimeString());
		}

		return members;

	}

	public Member[] findMembersBySocialSecurityNumber(IWContext modinfo, Vector socialSecurityNumbers) throws SQLException {
		Member[] members = null;
		String securityNumber;
		String union_id = modinfo.getParameter("search_for_member_in_union_id");
		String SQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND union_member_info.MEMBERSHIP_TYPE = 'main' AND (";

		int numberInserted = 0;

		for (int i = 0; i < socialSecurityNumbers.size(); i++) {
			try {
				securityNumber = (String) socialSecurityNumbers.elementAt(i);
				if (securityNumber.equals("")) {
					securityNumber = "idega_engin_kennitala";
				}

				members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll("Select * from member where social_security_number like '%" + securityNumber + "%' ");
				if (members.length > 0) {
					for (int j = 0; j < members.length; j++) {
						if (numberInserted != 0) {
							SQLString += " OR ";
						}
						else {
							++numberInserted;
						}

						SQLString += "member_id = " + members[j].getID();
					}
				}
			}
			catch (SQLException s) {
				s.printStackTrace(System.err);
			}
		}
		SQLString += ")";
		if (union_id != null) {
			SQLString += " AND union_member_info.union_id = " + union_id + " AND union_member_info.MEMBER_STATUS = 'A'";
		}

		//SQLString += "order by first_name,last_name,middle_name";
		SQLString += "order by social_security_number";
		try {
			members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(SQLString);
		}
		catch (SQLException s) {
		}
		return members;

	}

}