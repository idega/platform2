/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.sql.SQLException;
import java.util.StringTokenizer;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public class HandicapFindMember extends GolfWindow {

	public HandicapFindMember() {
		setWidth(400);
		setHeight(300);
		setTitle("Find Member");
		add(new FindMember());
	}

	public class FindMember extends GolfBlock {

		public void main(IWContext modinfo) throws Exception {
			IWResourceBundle iwrb = getResourceBundle();

			getParentPage().setTitle(iwrb.getLocalizedString("handicap.member_selection", "Member selection"));
			addHeading(iwrb.getLocalizedString("handicap.member_selection", "Member selection"));

			String mode = modinfo.getParameter("mode");
			if (mode == null) {
				mode = "";
			}

			if (mode.equals("")) {

				Form myForm = new Form();
				myForm.add(new HiddenInput("mode", "leita"));

				Table myTable = new Table(1, 5);
				myTable.setCellpadding(3);
				myTable.setCellspacing(3);
				//myTable.setAlignment(1,2,"center");
				//myTable.setAlignment(1,4,"center");
				myTable.setAlignment(1, 5, "right");
				myTable.setAlignment("center");

				TextInput kennitala = (TextInput) getStyledInterface(new TextInput("ssn"));
				kennitala.setLength(10);
				kennitala.setMaxlength(10);

				TextInput nafn = (TextInput) getStyledInterface(new TextInput("name"));
				nafn.setLength(20);

				myTable.add(getHeader(iwrb.getLocalizedString("handicap.enter_social_security_number", "Enter social-security number") + ":"), 1, 1);
				myTable.add(kennitala, 1, 2);
				myTable.add(getHeader(iwrb.getLocalizedString("handicap.or_enter_name", "or enter name") + ":"), 1, 3);
				myTable.add(nafn, 1, 4);
				myTable.add(getButton(new SubmitButton(iwrb.getLocalizedString("handicap.search", "Search"))), 1, 5);

				myForm.add(myTable);

				add(new Text().getBreak());
				add(myForm);
			}

			else if (mode.equals("leita")) {

				String kennitala = modinfo.getParameter("ssn");
				String nafn = modinfo.getParameter("name");

				Member[] member = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAllByColumn("social_security_number", kennitala);
				if ((nafn != null && !nafn.equals("")) && (member == null || member.length == 0)) {
					member = findMembersByName(modinfo, nafn);
				}
				Member memberinn = (Member) modinfo.getSessionAttribute("member_login");

				Form myForm = new Form();
				myForm.add(new HiddenInput("mode", "submit"));

				Table myTable = new Table(1, 3);
				myTable.setCellpadding(3);
				myTable.setCellspacing(3);
				myTable.setAlignment(1, 2, "center");
				myTable.setAlignment(1, 3, "right");
				myTable.setAlignment("center");

				int memberLength = member.length;
				if (memberLength > 0) {
					myTable = new Table(3, member.length + 2);
					myTable.setCellpadding(1);
					myTable.setCellspacing(1);
					myTable.setAlignment("center");
					int row = 1;

					RadioButton button = null;

					myTable.add(getHeader(iwrb.getLocalizedString("handicap.use", "Use")), 1, row);
					myTable.add(getHeader(iwrb.getLocalizedString("handicap.name", "Name") + ":"), 2, row);
					myTable.add(getHeader(iwrb.getLocalizedString("handicap.handicap", "Handicap") + ":"), 3, row);
					for (int i = 0; i < memberLength; i++) {
						++row;
						button = this.getRadioButton("member_id", String.valueOf(member[i].getID()));
						myTable.add(button, 1, row);
						myTable.add(getText(member[i].getName()), 2, row);
						if ((int) member[i].getHandicap() == 100) {
							myTable.add(getText(iwrb.getLocalizedString("handicap.not_registered", "Not registered")), 3, row);
						}
						else {
							myTable.add(getText(TextSoap.singleDecimalFormat((double) member[i].getHandicap())), 3, row);
						}
					}
					if (memberLength == 1) {
						button.setSelected();
					}
					++row;
					myTable.setAlignment(1, row, "right");
					myTable.mergeCells(1, row, 3, row);
					myTable.add(getButton(new BackButton(iwrb.getLocalizedString("handicap.back", "Back"))), 1, row);
					myTable.add(getButton(new SubmitButton(iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 1, row);
				}

				else {
					myTable.add(getHeader(iwrb.getLocalizedString("handicap.no_member", "No member found in database")), 1, 2);
					myTable.add(getButton(new BackButton(iwrb.getLocalizedString("handicap.back", "Back"))), 1, 3);
				}

				myForm.add(myTable);

				add(new Text().getBreak());
				add(myForm);

			}

			else if (mode.equals("submit")) {

				String member_id = modinfo.getParameter("member_id");

				modinfo.setSessionAttribute("member_id", member_id);

				getParentPage().setParentToReload();
				getParentPage().close();
			}
		}

		public Member[] findMembersByName(IWContext modinfo, String name) throws SQLException {
			Member[] members = null;
			Member[] tempMembers = null;
			StringTokenizer nameParts;
			String fullName = "";
			String firstName = "";
			String middleName = "";
			String lastName = "";
			int manyNames = 0;
			int numberInserted = 0;

			String SQLString = null;

			try {
				manyNames = 0;
				fullName = "";
				firstName = "";
				middleName = "";
				lastName = "";

				fullName = name;
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
					case 0:
						break;
					case 1:
						SQLString = "Select distinct * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%'";
						break;
					case 2:
						SQLString = "Select distinct * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%' and (last_name like '%" + lastName + "%' OR middle_name like '%" + lastName + "%')";
						break;
					default:
						SQLString = "Select distinct * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%' and middle_name like '%" + middleName + "%' and last_name like '%" + lastName + "%'";
						break;
				}

			}
			catch (Exception e) {
				e.printStackTrace();
			}

			SQLString += " order by first_name, last_name, middle_name";
			members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(SQLString);

			return members;
		}
	}
}