/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Window;
import com.idega.util.LocaleUtil;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public class MembersOverview extends GolfBlock {

	Connection Conn;
	Statement stmt;
	ResultSet RS;

	public void main(IWContext modinfo) throws Exception {
		try {
			Conn = getConnection();
			createTable(modinfo);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			if (stmt != null) {
				stmt.close();
			}
			if (RS != null) {
			  try {
			    RS.close();
			  }
			  catch (SQLException sql) {
			    log(sql);
			  }
			}
			if (Conn != null) {
				freeConnection(Conn);
			}
		}
	}

	public void getResultSetAlphabetOrderedMembersInUnion(String findLike, String unionId, String orderBy, String status) throws SQLException {
		findLike = findLike + "%";
		if (orderBy != null)
			orderBy = " order by " + orderBy;
		StringBuffer strSQL = new StringBuffer("select member.member_id,member.first_name,member.middle_name,member.last_name,");

		strSQL.append("member.email,member_info.handicap,member_info.handicap, address.street,union_member_info.member_status");
		strSQL.append(" from member,member_address,address,union_member_info,member_info where ");

		if (findLike.length() <= 2) {
			strSQL.append("member.first_name like '");
			strSQL.append(findLike);
			strSQL.append("' and ");
		}
		strSQL.append(" union_member_info.union_id='");
		strSQL.append(unionId);
		strSQL.append("' ");
		strSQL.append("and union_member_info.member_status='");
		strSQL.append(status);
		strSQL.append("' ");
		strSQL.append("and member_info.member_id = member.member_id ");
		strSQL.append("and member_address.member_id = member.member_id ");
		strSQL.append("and member_address.address_id = address.address_id ");
		strSQL.append("and member.member_id = union_member_info.member_id ");
		strSQL.append(orderBy);

		stmt = Conn.createStatement();
		RS = stmt.executeQuery(strSQL.toString());
	}

	public int getCount(String findLike, String unionId, String orderBy) throws SQLException {
		findLike = findLike + "%";
		if (orderBy != null)
			orderBy = " order by " + orderBy;
		StringBuffer strSQL = new StringBuffer("select count(member_id)");
		strSQL.append(" from member,member_address,address,union_member_info,member_info where ");
		if (findLike.length() <= 2) {
			strSQL.append("member.first_name like '");
			strSQL.append(findLike);
			strSQL.append("' and ");
		}
		strSQL.append(" union_member_info.union_id='");
		strSQL.append(unionId);
		strSQL.append("' ");
		strSQL.append("and union_member_info.member_status='A' ");
		strSQL.append("and member_info.member_id = member.member_id ");
		strSQL.append("and member_address.member_id = member.member_id ");
		strSQL.append("and member_address.address_id = address.address_id ");
		strSQL.append("and member.member_id = union_member_info.member_id ");
		strSQL.append(orderBy);

		Statement stmt2 = Conn.createStatement();
		ResultSet RS2 = stmt2.executeQuery(strSQL.toString());
		RS2.next();
		return RS2.getInt(1);
	}

	public void createTable(IWContext modinfo) throws Exception {
		boolean admin = isAdmin();
		Table table = null;
		Locale locale = modinfo.getCurrentLocale();
		String alphabet = null;
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			alphabet = "AÁBCDEÉFGHIÍJKLMNOÓPQRSTUÚVWXYÝZÞÆÖ";
		}
		else {
			alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}

		String toOrder = "A";
		String status = "A";
		String unionId = modinfo.getRequest().getParameter("union_id");
		if (unionId == null) {
			unionId = (String) modinfo.getSession().getAttribute("golf_union_id");
		}

		String[] colNames = new String[]{getResourceBundle().getLocalizedString("member.name", "Name"), getResourceBundle().getLocalizedString("member.address", "Address"), getResourceBundle().getLocalizedString("member.email", "Email"), getResourceBundle().getLocalizedString("member.handicap", "Handicap")};
		/*if (admin)
			colNames = new String[]{getResourceBundle().getLocalizedString("member.name", "Name"), getResourceBundle().getLocalizedString("member.address", "Address"), getResourceBundle().getLocalizedString("member.email", "Email"), getResourceBundle().getLocalizedString("member.handicap", "Handicap"), getResourceBundle().getLocalizedString("member.delete", "Delete"), getResourceBundle().getLocalizedString("member.more", "More")};
		else
			colNames = new String[]{getResourceBundle().getLocalizedString("member.name", "Name"), getResourceBundle().getLocalizedString("member.address", "Address"), getResourceBundle().getLocalizedString("member.email", "Email"), getResourceBundle().getLocalizedString("member.handicap", "Handicap")};
		*/
		
		Text text = null;
		Image greittImage = getBundle().getImage("shared/clubs/members/greitt.gif");
		Image infoImage = getBundle().getImage("shared/clubs/members/info.gif");
		Text noHandicap = new Text("-");
		noHandicap.setFontSize(2);

		int colNum = 4;
		if (admin)
			colNum = 6;

		Text headerText = new Text(getResourceBundle().getLocalizedString("member.members", "Members"), true, false, false);
		headerText.setFontSize(3);
		headerText.setFontColor("#FFFFFF");

		Form form = new Form();

		if (modinfo.isParameterSet("toOrder"))
			toOrder = modinfo.getParameter("toOrder");
		if (modinfo.isParameterSet("statusdrp"))
			status = modinfo.getParameter("statusdrp");
		else if (modinfo.isParameterSet("status"))
			status = modinfo.getParameter("status");

		getResultSetAlphabetOrderedMembersInUnion(toOrder, unionId, "first_name, middle_name, last_name", status);

		table = new Table();
		table.setWidth("100%");
		table.mergeCells(1, 1, colNum, 1);
		table.mergeCells(1, 2, colNum, 2);
		table.mergeCells(1, 3, colNum, 3);
		table.setAlignment(1, 1, "left");
		table.setAlignment(1, 3, "center");
		table.setAlignment(colNum, 3, "right");
		table.setColor(1, 1, "#FFFFFF");
		table.setColor(1, 2, "#336660");
		table.setRowColor(3, "#CEDFD0");

		table.add(headerText, 1, 2);

		table.add(new HiddenInput("toOrder", toOrder));
		table.add(new HiddenInput("status", status));
		table.add(new HiddenInput("union_id", unionId));
		
		Table alphabetTable = new Table(3, 1);
		alphabetTable.setWidth(Table.HUNDRED_PERCENT);
		alphabetTable.setWidth(2, 1, 20);
		alphabetTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_CENTER);
		alphabetTable.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(alphabetTable, 1, 3);

		for (int i = 0; i < alphabet.length(); i++) {
			Character charToString = new Character(alphabet.charAt(i));
			Link link = new Link(charToString.toString());
			link.addParameter("toOrder", charToString.toString());
			link.addParameter("status", status);
			link.addParameter("union_id", unionId);
			alphabetTable.add(link, 1, 1);
			alphabetTable.add("-", 1, 1);
		}
		Link link = new Link(getResourceBundle().getLocalizedString("member.all", "All"));
		link.addParameter("toOrder", "all");
		link.addParameter("union_id", unionId);
		link.addParameter("status", status);
		table.add(link, 1, 3);

		if (admin) {
			DropdownMenu drp = statusDropdown("statusdrp", status);
			drp.setToSubmit();
			alphabetTable.add(drp, 3, 1);
		}

		for (int i = 0; i < colNum; i++) {
			table.setColor(i + 1, 4, "#ADCAB1");
			if (i < colNames.length) {
				text = new Text(colNames[i]);
				text.setBold();
				table.add(text, i + 1, 4);
			}
		}

		String memberId = "";
		String email;
		String address;
		String handicap;
		String lastMember = "0";

		DecimalFormat handicapFormat = new DecimalFormat("0.0");
		DecimalFormatSymbols symbols = handicapFormat.getDecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		handicapFormat.setDecimalFormatSymbols(symbols);

		int i = 5;
		while (RS.next()) {
			lastMember = memberId;
			memberId = RS.getString("member_id");
			if (!lastMember.equalsIgnoreCase(memberId)) {
				email = RS.getString("email") != null ? RS.getString("email") : "-";
				address = RS.getString("street") != null ? RS.getString("street") : "-";
				handicap = RS.getString("handicap") != null ? RS.getString("handicap") : "100.0";

				if (handicap.equals("100.0"))
					handicap = "-";
				else
					handicap = TextSoap.singleDecimalFormat(handicap);

				if (address.length() > 15)
					address = address.substring(0, 15);
				if (email.length() > 15)
					email = email.substring(0, 15);

				StringBuffer nameBuffer = new StringBuffer();
				nameBuffer.append(RS.getString("first_name"));
				nameBuffer.append(" ");
				if (RS.getString("middle_name") != null)
					nameBuffer.append(RS.getString("middle_name"));
				nameBuffer.append(" ");
				if (RS.getString("last_name") != null)
					nameBuffer.append(RS.getString("last_name"));

				table.add(bodyText(nameBuffer.toString()), 1, i);
				table.add(bodyText(address), 2, i);
				table.add(bodyText(email), 3, i);
				table.add(bodyText(handicap), 4, i);

				if (admin) {
					table.setAlignment(5, i, "center");
					table.add(insertInfoImageLink(getBundle().getImage("shared/handicap/trash.gif"), "member_id", memberId, MemberDelete.class), 5, i);

					table.setAlignment(6, i, "center");
					table.add(insertInfoImageLink(infoImage, "member_id", memberId, MemberEditor.class), 6, i);
				}

				i++;
			}
		}

		if (admin) {
		  Link newMember = new Link(getResourceBundle().getImage("tabs/newmember1.gif"));
		  newMember.setWindowToOpen(MemberEditor.class);
			table.add(newMember, 1, 1);
			Window tarifWindow = new Window(getResourceBundle().getLocalizedString("member.payments", "Payments"), 600, 600, "/tarif/tarif.jsp");
			tarifWindow.setLocation(true);
			tarifWindow.setStatus(true);
			tarifWindow.setResizable(true);

			Link linkur = new Link(getResourceBundle().getImage("tabs/payments1.gif"), tarifWindow);

			Window listWindow = new Window(getResourceBundle().getLocalizedString("member.lists", "Lists"), 600, 600, "/reports/index.jsp");
			listWindow.setLocation(false);
			listWindow.setStatus(true);
			listWindow.setResizable(true);
			listWindow.setMenubar(true);

			Link linkur3 = new Link(getResourceBundle().getImage("tabs/lists1.gif"), listWindow);

			Window searchWindow = new Window("", 400, 280);
			searchWindow.add(new MemberSearcher());
			searchWindow.setResizable(true);
			Image selectMemberImage = getResourceBundle().getImage("tabs/search1.gif");
			Link selectMember = new Link(selectMemberImage, searchWindow);
			selectMember.clearParameters();

			table.add(linkur, 1, 1);
			table.add(linkur3, 1, 1);
			table.add(selectMember, 1, 1);
		}

		form.add(table);
		add(form);
	}

	public Link insertInfoImageLink(Image image, String URLParamName, String URLParamValue, Class windowClass) {
		image.setBorder(0);
		Link myLink = new Link(image);
		myLink.setWindowToOpen(windowClass);
		myLink.addParameter(URLParamName, URLParamValue);

		return myLink;
	}

	public Text bodyText(String s) {
		Text T = new Text();
		if (s != null) {
			T = new Text(s);
			//T.setFontColor("#000000");
			//T.setFontSize(1);
		}
		return T;
	}

	public DropdownMenu statusDropdown(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("A", getResourceBundle().getLocalizedString("member.active", "Virkur"));
		drp.addMenuElement("I", getResourceBundle().getLocalizedString("member.inactive", "Óvirkur"));
		drp.addMenuElement("W", getResourceBundle().getLocalizedString("member.waiting", "Í bið"));
		drp.addMenuElement("Q", getResourceBundle().getLocalizedString("member.retired", "Hættur"));
		drp.addMenuElement("D", getResourceBundle().getLocalizedString("member.deceased", "Látinn"));
		drp.setSelectedElement(selected);
		return drp;
	}

	public Text bodyText(int i) {
		return bodyText(String.valueOf(i));
	}
}