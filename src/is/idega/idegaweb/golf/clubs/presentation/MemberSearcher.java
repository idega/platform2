/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public class MemberSearcher extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();

		getParentPage().setTitle(iwrb.getLocalizedString("handicap.member_selection", "Member selection"));

		String mode = modinfo.getParameter("mode");
		if (mode == null) {
			mode = "";
		}

		if (mode.equals("") || ("".equals(modinfo.getParameter("ssn")) && "".equals(modinfo.getParameter("name")))) {

			Form myForm = new Form("select_member.jsp", "get");
			myForm.add(new HiddenInput("mode", "leita"));

			Table myTable = new Table(1, 5);
			myTable.setCellpadding(3);
			myTable.setCellspacing(3);
			//myTable.setAlignment(1,2,"center");
			//myTable.setAlignment(1,4,"center");
			myTable.setAlignment(1, 5, "right");
			myTable.setAlignment("center");

			TextInput kennitala = new TextInput("ssn");
			kennitala.setLength(10);
			kennitala.setMaxlength(10);

			TextInput nafn = new TextInput("name");
			nafn.setLength(20);

			myTable.addText(iwrb.getLocalizedString("handicap.enter_social_security_number", "Enter social-security number") + ":", 1, 1);
			myTable.add(kennitala, 1, 2);
			myTable.addText(iwrb.getLocalizedString("handicap.or_enter_name", "or enter name") + ":", 1, 3);
			myTable.add(nafn, 1, 4);
			myTable.add(new SubmitButton(iwrb.getImage("buttons/search.gif", "", 76, 19)), 1, 5);

			myForm.add(myTable);

			add(new Text().getBreak());
			add(myForm);
		}

		else if (mode.equals("leita")) {

			String kennitala = modinfo.getParameter("ssn");
			String nafn = modinfo.getParameter("name");
			String unionId = (String) modinfo.getSession().getAttribute("golf_union_id");

			Member[] member = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAllByColumnEquals("social_security_number", kennitala);
			if (nafn != null && (member == null || member.length == 0)) {
				//try {
				member = findMembersByName(modinfo, nafn, unionId);
				//}catch (SQLException e) {
				//	add(e.getMessage());
				//}
				//member = (Member[]) (new Member()).findAll("select * from member
				// where full_name like '%"+nafn+"%'");
			}
			Member memberinn = (Member) modinfo.getSessionAttribute("member_login");

			Form myForm = new Form("select_member.jsp");
			myForm.add(new HiddenInput("mode", "submit"));

			Table myTable = new Table(1, 3);
			myTable.setCellpadding(3);
			myTable.setCellspacing(3);
			myTable.setAlignment(1, 2, "center");
			myTable.setAlignment(1, 3, "right");
			myTable.setAlignment("center");

			int memberLength = member.length;
			if (memberLength > 0) {

				//if ( member[0].getMainUnionID() == memberinn.getMainUnionID() ||
				// memberinn.getMainUnionID() == 3 ) {
				myTable = new Table(3, member.length + 2);
				myTable.setCellpadding(1);
				myTable.setCellspacing(1);
				myTable.setAlignment("center");
				int row = 1;

				myTable.addText(iwrb.getLocalizedString("handicap.name", "Name") + ":", 2, row);
				myTable.addText(iwrb.getLocalizedString("handicap.handicap", "Handicap") + ":", 3, row);
				//myTable.setAlignment(1,2,"left");
				for (int i = 0; i < memberLength; i++) {
					++row;
					//button = new
					// RadioButton("member_id",String.valueOf(member[i].getID()) );
					//myTable.add(button, 1, row);
					myTable.add(insertInfoImageLink(String.valueOf(member[i].getID())), 1, row);
					myTable.addText(member[i].getName(), 2, row);
					if ((int) member[i].getHandicap() == 100) {
						myTable.addText(iwrb.getLocalizedString("handicap.not_registered", "Not registered"), 3, row);
					}
					else {
						myTable.addText(TextSoap.singleDecimalFormat((double) member[i].getHandicap()), 3, row);
					}
				}
				//	                    if (memberLength == 1) {
				//	                    	button.setSelected();
				//	                    }
				++row;
				myTable.setAlignment(1, row, "center");
				myTable.mergeCells(1, row, 3, row);
				myTable.add(new BackButton(iwrb.getImage("buttons/back.gif", "", 76, 19)), 1, row);
				//myTable.add(new
				// SubmitButton(iwrb.getImage("buttons/confirm.gif","",76,19)),1,row);

				//myForm.add(new
				// HiddenInput("member_id",String.valueOf(member[0].getID())));
				//}

				//else {
				//myTable.addText("Kylfingur ekki í "+new
				// Union(memberinn.getMainUnionID()).getAbbrevation(),1,2);
				//myTable.add(new BackButton("Til baka"),1,3);
				//}
			}

			else {
				myTable.addText(iwrb.getLocalizedString("handicap.no_member", "No member found in database"), 1, 2);
				myTable.add(new BackButton("Til baka"), 1, 3);
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

	public Member[] findMembersByName(IWContext modinfo, String name, String union_id) throws SQLException {
		Member[] members = null;
		Member[] tempMembers = null;
		StringTokenizer nameParts;
		String fullName = "";
		String firstName = "";
		String middleName = "";
		String lastName = "";
		int manyNames = 0;
		int numberInserted = 0;

		String SQLString = "Select * from member where ";
		String tempSQLString = "";

		try {
			manyNames = 0;
			fullName = "";
			firstName = "";
			middleName = "";
			lastName = "";
			tempSQLString = "";

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
				case 0 :
					break;
				case 1 :
					SQLString = "Select distinct * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%'";
					break;
				case 2 :
					SQLString = "Select distinct * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%' and (last_name like '%" + lastName + "%' OR middle_name like '%" + lastName + "%')";
					break;
				default :
					SQLString = "Select distinct * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%" + firstName + "%' and middle_name like '%" + middleName + "%' and last_name like '%" + lastName + "%'";
					break;
			}

			if (union_id != null && !"3".equals(union_id)) {
				SQLString += " AND union_member_info.union_id = " + union_id + " AND union_member_info.MEMBERSHIP_TYPE = 'main'";
			}

		}
		catch (Exception e) {
			//	add(e.getMessage());
		}

		//    if (numberInserted != 0) {
		SQLString += " order by first_name, last_name, middle_name";
		members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(SQLString);
		//add("SQL = "+SQLString);
		//System.out.println("Done printing
		// array"+idegaTimestamp.RightNow().toSQLTimeString() );
		//    }

		return members;

	}

	private String scale_decimals(String nyForgjof, int scale) throws IOException {

		BigDecimal test2 = new BigDecimal(nyForgjof);

		String nyForgjof2 = test2.setScale(scale, 5).toString();

		return nyForgjof2;

	}

	public Link insertInfoImageLink(String member_id) {
		Image image = new Image("/pics/clubs/members/info.gif");
		Window window = new Window("Memberwindow", 690, 610, "/clubs/member.jsp");
		window.setMenubar(false);
		window.setResizable(true);
		window.setScrollbar(true);
		window.setTitlebar(false);

		image.setBorder(0);
		Link myLink = new Link(image, window);
		//myLink.setURL(action);
		myLink.addParameter("member_id", member_id);

		return myLink;
	}

}