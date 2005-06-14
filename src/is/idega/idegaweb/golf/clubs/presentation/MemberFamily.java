/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Family;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public class MemberFamily extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		try {
			if (modinfo.getRequest().getParameter("rdbFamily") == null)
				showFamilyList(modinfo);
			if (modinfo.getRequest().getParameter("cmd") != null) {

				String familyId = modinfo.getRequest().getParameter("rdbFamily");

				if (modinfo.getRequest().getParameter("cmd").equals("submit")) {
					modinfo.getSession().setAttribute("family_id", familyId);
					getParentPage().close();
				}
			}
		}
		catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	public void showFamilyList(IWContext modinfo) throws Exception {
		Vector vError = new Vector();
		boolean isResult = true;
		int length = 0;
		Family[] famArr = new Family[0];
		Form form = new Form();
		String orderName = null;
		String unionId = null;
		Table table = null;
		Table searchTable = new Table(2, 2);
		RadioButton rbName = new RadioButton("rdbToFind", "nafn");
		rbName.keepStatusOnAction();
		RadioButton rbKt = new RadioButton("rdbToFind", "kennitala");
		rbKt.keepStatusOnAction();
		TextInput input = new TextInput("toFind");
		input.keepStatusOnAction();
		searchTable.add(input, 1, 2);
		searchTable.add(rbName, 1, 1);
		searchTable.add("Nafn", 1, 1);
		searchTable.add(rbKt, 1, 1);
		searchTable.add("Kennitala", 1, 1);
		searchTable.add(new SubmitButton("Leita"), 2, 2);
		// searchTable.setBorder(1);
		String alphabet = "AÁBCDEÉFGHIÍJKLMNOÓPQRSTUVXYÝZÞÆÖ";
		PrintWriter out = modinfo.getResponse().getWriter();

		if (modinfo.getSession().getAttribute("golf_union_id") == null) {
			vError.addElement("Félagsnúmer");
			//modinfo.getRequest().getSession().setAttribute("error", vError);
			//modinfo.getResponse().sendRedirect("membererror.jsp");
		}
		else
			unionId = (String) modinfo.getSession().getAttribute("golf_union_id");

		orderName = modinfo.getRequest().getParameter("letter");

		if (modinfo.getRequest().getParameter("rdbToFind") == null)
			rbName.setSelected();

		if (orderName != null)
			famArr = findAllFamilies(modinfo, unionId, orderName);
		else if (modinfo.getRequest().getParameter("toFind") != null) {
			if (modinfo.getRequest().getParameter("rdbToFind").equalsIgnoreCase("nafn"))
				famArr = this.findByMemberName(modinfo.getRequest().getParameter("toFind"), unionId);
			else
				famArr = this.findBySocialSecurityNumber(modinfo.getRequest().getParameter("toFind"), unionId);
		}

		//skipta array-inu upp i tvo jafna helminga

		length = famArr.length / 2;
		//ef útkoman úr deilingunni var oddatala, þá bæta 1 við
		if ((famArr.length % 2) > 0)
			length++;

		Text text = new Text("Fjölskyldur", true, true, false);
		text.setFontSize(3);
		text.setFontColor("red");
		table = new Table(4, length + 5);
		table.mergeCells(1, 1, 4, 1);
		table.add(searchTable, 1, 1);
		table.setHorizontalZebraColored("white", "#ADBDB3");
		table.mergeCells(1, 2, 4, 2);
		table.add(text, 1, 2);
		table.setAlignment("center");
		Table outerTable = new Table(1, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		//table.setBorder(1);
		//outerTable.setBorder(1);

		for (int i = 0; i < alphabet.length(); i++) {
			Character charToString = new Character(alphabet.charAt(i));
			outerTable.add(new Link(charToString.toString(), modinfo.getRequest().getRequestURI() + "?letter=" + charToString.toString()), 1, 1);
			if (i != alphabet.length() - 1)
				outerTable.add("-", 1, 1);
		}
		outerTable.add(table, 1, 1);
		outerTable.setAlignment("center");

		//fyrri helmingur arraysins fer í fyrstu tvo dálkana
		int i = 0;
		for (; i < length; i++) {
			table.add(new RadioButton("rdbFamily", String.valueOf(famArr[i].getID())), 1, i + 4);
			table.add(famArr[i].getName(), 2, i + 4);
		}

		//næsti helmingur arraysins fer í fyrstu tvo næstu dálkana
		int j = 0;
		for (; i < famArr.length; j++) {

			table.add(new RadioButton("rdbFamily", String.valueOf(famArr[i].getID())), 3, j + 4);
			table.add(famArr[i].getName(), 4, j + 4);
			i++;

		}
		table.setRowAlignment(j + 5, "right");
		if (famArr.length > 0)
			table.add(new SubmitButton("Velja"), 4, j + 5);
		form.add(outerTable);
		form.setAction(modinfo.getRequest().getRequestURI() + "?cmd=submit");
		add(form);

	}

	private Family[] findAllFamilies(IWContext modinfo, String unionId, String nameLike) {
		Family family = (Family) IDOLookup.instanciateEntity(Family.class);
		Family[] famArr = null;
		try {
			famArr = (Family[]) family.findAllByColumnOrdered("union_id", unionId, "name", nameLike + "%", "name");
		}
		catch (Exception e) {
			try {
				modinfo.getResponse().getWriter().print(e.getMessage());
			}
			catch (Exception err) {
				err.printStackTrace(System.err);
			}
		}
		return famArr;
	}

	private Family[] findBySocialSecurityNumber(String num) throws Exception {
		Member member = (Member) IDOLookup.instanciateEntity(Member.class);
		Member[] memberArr = null;
		Family[] familyArr = null;
		try {
			memberArr = (Member[]) member.findAllByColumnEquals("social_security_number", num);
			familyArr = getFamilies(memberArr);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return familyArr;
	}

	private Family[] findBySocialSecurityNumber(String num, String unionId) throws Exception {
		Member member = (Member) IDOLookup.instanciateEntity(Member.class);
		Member[] memberArr = null;
		Family[] familyArr = null;
		try {
			memberArr = (Member[]) member.findAll("select distinct * from member, family where member.family_id = family.family_id and family.union_id = '" + unionId + "' and member.social_security_number = '" + num + "'");
			familyArr = getFamilies(memberArr);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return familyArr;
	}

	private Family[] findByMemberName(String name, String unionId) throws Exception {
		Member member = (Member) IDOLookup.instanciateEntity(Member.class);
		Member[] memberArr = null;
		Family[] familyArr = null;
		String firstName = "";
		String middleName = "";
		String lastName = "";

		StringTokenizer token = new StringTokenizer(name);

		try {
			if (token.countTokens() > 2) {
				firstName = (String) token.nextElement();
				middleName = (String) token.nextElement();
				lastName = (String) token.nextElement();
				memberArr = (Member[]) member.findAll("select * from member, family where member.family_id = family.family_id and family.union_id = '" + unionId + "' and member.first_name like '" + firstName + "' and member.middle_name like '" + middleName + "' and member.last_name like '" + lastName + "'");
				familyArr = getFamilies(memberArr);
			}
			else if (token.countTokens() > 1) {
				firstName = (String) token.nextElement();
				lastName = (String) token.nextElement();
				Member[] tmpArr = (Member[]) member.findAll("select * from member, family where member.family_id = family.family_id and family.union_id = '" + unionId + "' and member.first_name like '" + firstName + "' and member.middle_name like '" + lastName + "%'");
				memberArr = (Member[]) member.findAll("select * from member, family where member.family_id = family.family_id and family.union_id = '" + unionId + "' and member.first_name like '" + firstName + "' and member.last_name like '" + lastName + "%'");
				memberArr = (Member[]) joinArrays(tmpArr, memberArr);
				familyArr = getFamilies(memberArr);
			}
			else if (token.countTokens() > 0) {
				firstName = (String) token.nextElement();
				memberArr = (Member[]) member.findAll("select * from member, family where member.family_id = family.family_id and family.union_id = '" + unionId + "' and member.first_name like '" + firstName + "%'");
				familyArr = getFamilies(memberArr);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return familyArr;
	}

	public Member[] joinArrays(Member[] p1, Member[] p2) throws Exception {
		int size = (p1.length + p2.length);
		Member[] returnEntity = new Member[size];
		int i = 0;
		try {
			for (; i < p1.length; i++) {
				returnEntity[i] = p1[i];
			}
			i++;
			for (int j = 0; j < p2.length; j++) {
				returnEntity[i] = p2[j];
				i++;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return returnEntity;
	}

	public Family[] getFamilies(Member[] memberArr) {
		if (memberArr == null)
			return new Family[0];

		Family[] familyArr = new Family[memberArr.length];
		for (int i = 0; i < memberArr.length; i++) {
			//familyArr[i] = memberArr[i].getFamily();
		}
		return familyArr;
	}
}