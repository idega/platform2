/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Window;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.presentation.GolfBlock;
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
		IWBundle iwb = getBundle(modinfo);
		IWResourceBundle iwrb = iwb.getResourceBundle(modinfo.getCurrentLocale());
		/*
		 * java.util.Enumeration en = modinfo.getParameterNames();
		 * while(en.hasMoreElements()){ String prm = (String) en.nextElement();
		 * System.err.println("prm "+prm+" value "+modinfo.getParameter(prm)); }
		 */

		try {
			Conn = getConnection();
			createTable(modinfo, iwb, iwrb);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			if (stmt != null)
				stmt.close();
			if (RS != null)
				RS.close();
			if (Conn != null)
				freeConnection(Conn);

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
		// System.out.println(strSQL.toString());
		RS = stmt.executeQuery(strSQL.toString());
	}

	public int getCount(String findLike, String unionId, String orderBy) throws SQLException {
		findLike = findLike + "%";
		if (orderBy != null)
			orderBy = " order by " + orderBy;
		StringBuffer strSQL = new StringBuffer("select count(member_id)");
		//strSQL.append(" from
		// member,member_address,address,union_member_info,member_info,union_member
		// where ");
		strSQL.append(" from member,member_address,address,union_member_info,member_info where ");
		if (findLike.length() <= 2) {
			strSQL.append("member.first_name like '");
			strSQL.append(findLike);
			strSQL.append("' and ");
		}
		//strSQL.append("union_member.union_id = '");
		//strSQL.append(unionId);
		//strSQL.append("' ");
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

	public void createTable(IWContext modinfo, IWBundle iwb, IWResourceBundle iwrb) throws Exception {
		boolean admin = isAdmin();
		Table table = null;
		Locale locale = modinfo.getCurrentLocale();
		String alphabet = null;
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			alphabet = "AÁBCDEÉFGHIÍJKLMNOÓPQRSTUÚVWXYÝZÞÆÖ";
		}
		/*
		 * else if(locale.equals(LocaleUtil.getSwedishLocale())){ alphabet =
		 * "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ"; }
		 */
		else {
			alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}

		String toOrder = "A";
		String status = "A";
		String unionId = modinfo.getRequest().getParameter("union_id");
		if (unionId == null) {
			unionId = (String) modinfo.getSession().getAttribute("golf_union_id");
		}

		String[] colNames = null;
		if (admin)
			colNames = new String[]{iwrb.getLocalizedString("member.name", "Name"), iwrb.getLocalizedString("member.address", "Address"), iwrb.getLocalizedString("member.email", "Email"), iwrb.getLocalizedString("member.handicap", "Handicap"), iwrb.getLocalizedString("member.delete", "Delete"), iwrb.getLocalizedString("member.more", "More")};
		else
			colNames = new String[]{iwrb.getLocalizedString("member.name", "Name"), iwrb.getLocalizedString("member.address", "Address"), iwrb.getLocalizedString("member.email", "Email"), iwrb.getLocalizedString("member.handicap", "Handicap")};

		Text text = null;
		Image greittImage = new Image("/pics/clubs/members/greitt.gif");
		Image infoImage = new Image("/pics/clubs/members/info.gif");
		Text noHandicap = new Text("-");
		noHandicap.setFontSize(2);

		int colNum = 4;
		//debug removed gjold
		if (admin)
			colNum = 6;

		Text headerText = new Text(iwrb.getLocalizedString("member.members", "Members"), true, false, false);
		headerText.setFontSize(3);
		headerText.setFontColor("#FFFFFF");

		Form form = new Form();

		if (modinfo.isParameterSet("toOrder"))
			toOrder = modinfo.getParameter("toOrder");
		if (modinfo.isParameterSet("statusdrp"))
			status = modinfo.getParameter("statusdrp");
		else if (modinfo.isParameterSet("status"))
			status = modinfo.getParameter("status");

		getResultSetAlphabetOrderedMembersInUnion(toOrder, unionId, "first_name", status);

		//int Length = getCount(toOrder, unionId, "first_name");

		table = new Table();
		table.setWidth("100%");
		table.mergeCells(1, 1, colNum, 1);
		table.mergeCells(1, 2, colNum, 2);
		table.mergeCells(1, 3, colNum - 1, 3);
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

		for (int i = 0; i < alphabet.length(); i++) {
			Character charToString = new Character(alphabet.charAt(i));
			Link link = new Link(charToString.toString());
			link.addParameter("toOrder", charToString.toString());
			link.addParameter("status", status);
			link.addParameter("union_id", unionId);
			table.add(link, 1, 3);
			//table.add(new
			// Link(charToString.toString(),"members.jsp?toOrder="+java.net.URLEncoder.encode(charToString.toString())+"&union_id="+unionId),
			// 1,2);
			// if( i != alphabet.length())
			table.add("-", 1, 3);
		}
		Link link = new Link(iwrb.getLocalizedString("member.all", "All"));
		link.addParameter("toOrder", "all");
		link.addParameter("union_id", unionId);
		link.addParameter("status", status);
		table.add(link, 1, 3);

		if (admin) {
			DropdownMenu drp = statusDropdown("statusdrp", status, iwrb);
			drp.setToSubmit();
			table.add(drp, colNum, 3);
		}

		//table.add(new
		// Link(iwrb.getLocalizedString("member.all","All"),"members.jsp?toOrder=all"+"&union_id="+unionId),
		// 1,2);

		for (int i = 0; i < colNum; i++) {
			table.setColor(i + 1, 4, "#ADCAB1");
			text = new Text(colNames[i], true, false, false);
			text.setFontColor("black");
			table.add(text, i + 1, 4);
		}

		String memberId = "";
		String email;
		String address;
		String handicap;
		String lastMember = "0";

		DecimalFormat handycapFormat = new DecimalFormat("0.0");
		DecimalFormatSymbols symbols = handycapFormat.getDecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		handycapFormat.setDecimalFormatSymbols(symbols);

		int i = 5;
		while (RS.next()) {
			//debug sía út tvöfalda membera gera þetta betur!
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
					table.add(insertInfoImageLink(new Image("/pics/handicap/trash.gif"), "member_id", memberId, "/clubs/delete.jsp"), 5, i);

					table.setAlignment(6, i, "center");
					table.add(insertInfoImageLink(infoImage, "member_id", memberId, "/clubs/member.jsp"), 6, i);
				}

				i++;
			}
		}
		//RS.next ends
		//debug dirty fix

		Window memberWindow = new Window(iwrb.getLocalizedString("member.new_member", "New member"), 700, 580, "/clubs/member.jsp");

		memberWindow.setMenubar(false);
		memberWindow.setResizable(true);
		memberWindow.setTitlebar(false);
		memberWindow.setScrollbar(true);

		if (admin) {
			table.add(new Link(iwrb.getImage("tabs/newmember1.gif"), memberWindow), 1, 1);
			Window tarifWindow = new Window(iwrb.getLocalizedString("member.payments", "Payments"), 600, 600, "/tarif/tarif.jsp");
			tarifWindow.setLocation(true);
			tarifWindow.setStatus(true);
			tarifWindow.setResizable(true);

			Link linkur = new Link(iwrb.getImage("tabs/payments1.gif"), tarifWindow);

			Window listWindow = new Window(iwrb.getLocalizedString("member.lists", "Lists"), 600, 600, "/reports/index.jsp");
			listWindow.setLocation(false);
			listWindow.setStatus(true);
			listWindow.setResizable(true);
			listWindow.setMenubar(true);

			Link linkur3 = new Link(iwrb.getImage("tabs/lists1.gif"), listWindow);

			Window searchWindow = new Window("", 400, 280, "/clubs/select_member.jsp");
			searchWindow.setResizable(true);
			Image selectMemberImage = iwrb.getImage("tabs/search1.gif");
			Link selectMember = new Link(selectMemberImage, searchWindow);
			selectMember.clearParameters();

			table.add(linkur, 1, 1);
			table.add(linkur3, 1, 1);
			table.add(selectMember, 1, 1);
		}

		form.add(table);
		add(form);
	}

	public Link insertInfoImageLink(Image image, String URLParamName, String URLParamValue, String action) {
		Window window = new Window("Memberwindow", 690, 610, action);
		window.setMenubar(false);
		window.setResizable(true);
		window.setScrollbar(true);
		window.setTitlebar(false);

		image.setBorder(0);
		Link myLink = new Link(image, window);
		//myLink.setURL(action);
		myLink.addParameter(URLParamName, URLParamValue);

		return myLink;
	}

	public Text bodyText(String s) {
		Text T = new Text();
		if (s != null) {
			T = new Text(s);
			T.setFontColor("#000000");
			T.setFontSize(1);
		}
		return T;
	}

	public DropdownMenu statusDropdown(String name, String selected, IWResourceBundle iwrb) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("A", iwrb.getLocalizedString("member.active", "Virkur"));
		drp.addMenuElement("I", iwrb.getLocalizedString("member.inactive", "Óvirkur"));
		drp.addMenuElement("W", iwrb.getLocalizedString("member.waiting", "Í bið"));
		drp.addMenuElement("Q", iwrb.getLocalizedString("member.retired", "Hættur"));
		drp.addMenuElement("D", iwrb.getLocalizedString("member.deceased", "Látinn"));
		drp.setSelectedElement(selected);
		return drp;
	}

	public Text bodyText(int i) {
		return bodyText(String.valueOf(i));
	}
}