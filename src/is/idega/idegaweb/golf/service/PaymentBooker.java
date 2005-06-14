package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.entity.AccountYear;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Payment;
import is.idega.idegaweb.golf.entity.PaymentHome;
import is.idega.idegaweb.golf.entity.PaymentType;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.FileSaver;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FileInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * @version 1.0
 */
public class PaymentBooker extends com.idega.presentation.PresentationObjectContainer {

	private String sUnionID, sUnionName, sUnionAbbrev;
	private int iUnionID, iCashierID;
	private Union eUnion;
	private String[][] Values;
	private Member eMember, eCashier;
	private String sMenuColor, sItemColor, sHeaderColor, sLightColor, sDarkColor, sOtherColor, sWhiteColor;
	private boolean isAdmin = false;
	private java.util.Locale currentLocale;
	private int iCellspacing = 1, iCellpadding = 2;
	private String sTablewidth = "600";
	private String sAction = "", sMessage = "";
	private String sParameterPrefix = "payment_booker_";
	private final int iEUROID = 2, iVISAID = 3, iGIROID = 1;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.projects.golf.tariff";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	public PaymentBooker() {

		sHeaderColor = "#336660";
		sLightColor = "#CEDFD0";
		sDarkColor = "#ADCAB1";
		sOtherColor = "#6E9073 ";
		sWhiteColor = "#FFFFFF";

		setMenuColor("#ADCAB1");//,"#CEDFD0"
		setItemColor("#CEDFD0");//"#D0F0D0"
		currentLocale = java.util.Locale.getDefault();
	}

	public void setMenuColor(String MenuColor) {
		this.sMenuColor = MenuColor;
	}

	public void setItemColor(String ItemColor) {
		this.sItemColor = ItemColor;
	}

	private void control(IWContext modinfo) throws IOException {

		try {
			sUnionID = (String) modinfo.getSession().getAttribute("golf_union_id");

			if (modinfo.getSession().getAttribute("member_login") != null) {
				eCashier = (Member) modinfo.getSession().getAttribute("member_login");
				iCashierID = eCashier.getID();
			}

			iUnionID = Integer.parseInt(sUnionID);
			eUnion = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(iUnionID);
			sUnionName = eUnion.getName();
			sUnionAbbrev = eUnion.getAbbrevation();

			//add(""+Integer.parseInt("0000000000003665"));
			boolean hasSomeValues = false;

			if (modinfo.getParameter(sParameterPrefix + "action") != null) {
				sAction = modinfo.getParameter(sParameterPrefix + "action");
			}

			if (modinfo.getSession().getAttribute(sParameterPrefix + "action") != null)
				sAction = (String) modinfo.getSession().getAttribute(sParameterPrefix + "action");

			if (sAction.equals("main")) {
				doMain(modinfo);
			}
			else if (sAction.equals("list")) {
				doList(modinfo);
			}
			else if (sAction.equals("entries")) {
				doEntrySearch(modinfo);
			}
			else if (sAction.equals("update")) {
				doUpdate(modinfo);
			}
			else if (sAction.equals("view")) {
				doView(modinfo);
			}
			else if (sAction.equals("uploadfile")) {
				doUploadFile(modinfo);
			}
			else if (sAction.equals("file")) {
				doFile(modinfo);
			}
			else if (sAction.equals("write")) {
				doWriteFile(modinfo);
			}
			else if (sAction.equals("fetch")) {
				doFetchFile(modinfo);
			}
			else if (sAction.equals("pfile")) {
				doPaymentFile(modinfo);
			}
			else if (sAction.equals("pamwrite")) {
				writePaymentFile(modinfo);
			}
			else
				doMain(modinfo);

		}
		catch (SQLException S) {
			System.err.print(S.toString());
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

	private void doMain(IWContext modinfo) throws SQLException {
		Form myForm = new Form();
		myForm.maintainAllParameters();

		Table T = new Table(2, 4);
		T.setWidth(this.sTablewidth);
		T.setHorizontalZebraColored(sDarkColor, sLightColor);
		T.setRowColor(1, sHeaderColor);
		T.setCellpadding(2);
		T.setCellspacing(1);
		String sPayType = iwrb.getLocalizedString("paytype", "Paytype");
		String sDueDAte = iwrb.getLocalizedString("duedate", "Duedate");
		String sHow = iwrb.getLocalizedString("how", "How");
		String sUnpaid = iwrb.getLocalizedString("unpaid", "Unpaid");
		String sIspaid = iwrb.getLocalizedString("ispaid", "Paid");
		T.add(sPayType + " :", 1, 2);
		T.add(sDueDAte + " :", 1, 3);
		T.add(sHow + " :", 1, 4);
		T.add(this.drpPayType(sParameterPrefix + "drppaytype"), 2, 2);
		T.add(this.drpDays(sParameterPrefix + "drpday"), 2, 3);
		T.add(this.drpMonth(sParameterPrefix + "drpmonth"), 2, 3);
		T.add(this.drpYear(sParameterPrefix + "drpyear"), 2, 3);
		T.add(sUnpaid, 2, 4);
		RadioButton RB = new RadioButton(sParameterPrefix + "radio", "notpaid");
		RB.setSelected();
		T.add(RB, 2, 4);
		T.add(sIspaid, 2, 4);
		T.add(new RadioButton(sParameterPrefix + "radio", "paid"), 2, 4);

		myForm.add(T);
		myForm.add(new SubmitButton(iwrb.getImage("search.gif")));
		myForm.add(new HiddenInput(sParameterPrefix + "action", "list"));

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(2), 1, 1);
		MainTable.add(myForm, 1, 2);
		MainTable.add("<br><br><br>", 1, 3);
		add(MainTable);

	}

	private void doList(IWContext modinfo) throws SQLException {
		IWTimestamp toDay = new IWTimestamp();
		int iToDay = toDay.getDay();
		int iToMonth = toDay.getMonth();
		int iToYear = toDay.getYear();

		int iDay = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drpday"));
		int iMonth = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drpmonth"));
		int iYear = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drpyear"));
		int iPaytype = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drppaytype"));

		String radio = modinfo.getParameter(sParameterPrefix + "radio");
		boolean ifPaid = false;
		if (radio.equalsIgnoreCase("paid"))
			ifPaid = true;

		//String[][] sListEntrys =
		// this.getPaymentsMembers(this.iUnionID,iYear,iMonth,iDay,iPaytype,
		// ifPaid);
		List sListEntrys = this.getPaymentsMembersList(this.iUnionID, iYear, iMonth, iDay, iPaytype, ifPaid);

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(1), 1, 1);

		if (sListEntrys != null) {
			//int iSlen = sListEntrys.length;
			int iSlen = sListEntrys.size();
			Form myForm = new Form();
			myForm.maintainAllParameters();
			Table T = new Table(6, iSlen + 2);
			T.setHorizontalZebraColored(sDarkColor, sLightColor);
			T.setCellpadding(2);
			T.setCellspacing(1);
			T.setRowColor(1, sHeaderColor);
			T.setRowColor(iSlen + 2, sWhiteColor);
			T.setColumnAlignment(4, "center");
			T.setColumnAlignment(5, "right");
			T.setColumnAlignment(6, "center");

			String sfontColor = this.sWhiteColor;

			String sNumber = iwrb.getLocalizedString("nr", "Nr");
			String sName = iwrb.getLocalizedString("name", "Name");
			String sSsn = iwrb.getLocalizedString("ssn", "Socialnumber");
			String sAmount = iwrb.getLocalizedString("amount", "Amount");

			String sDueDAte = iwrb.getLocalizedString("duedate", "Duedate");
			String sPaid = iwrb.getLocalizedString("paid", "Paid");

			Text tNr = new Text(sNumber, true, false, false);
			tNr.setFontColor(sfontColor);
			Text tMember = new Text(sName, true, false, false);
			tMember.setFontColor(sfontColor);
			Text tKt = new Text(sSsn, true, false, false);
			tKt.setFontColor(sfontColor);
			Text tPrice = new Text(sAmount, true, false, false);
			tPrice.setFontColor(sfontColor);
			Text tPaydate = new Text(sDueDAte, true, false, false);
			tPaydate.setFontColor(sfontColor);
			Text tPaid = new Text(sPaid, true, false, false);
			tPaid.setFontColor(sfontColor);

			T.add(tNr, 1, 1);
			T.add(tMember, 2, 1);
			T.add(tKt, 3, 1);
			T.add(tPaydate, 4, 1);
			T.add(tPrice, 5, 1);
			T.add(tPaid, 6, 1);

			java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
			int totalprice = 0;
			Vector v = (Vector) sListEntrys;
			for (int i = 0; i < iSlen; i++) {
				pElement pe = (pElement) v.elementAt(i);
				T.add(String.valueOf(i + 1), 1, i + 2);
				//Link L = new Link(sListEntrys[i][1],"/tarif/accountview.jsp");
				//L.addParameter("member_id",sListEntrys[i][0]);
				Link L = new Link(pe.getName(), "/tarif/accountview.jsp");
				L.addParameter("member_id", pe.getMemberId());
				T.add(L, 2, i + 2);
				//T.add( sListEntrys[i][2],3,i+2);
				//T.add( sListEntrys[i][4],4,i+2);
				T.add(pe.getSocialSecurityNumberId(), 3, i + 2);
				if (pe.getPayDate() != null)
					T.add(pe.getPayDate().getISLDate(".", true), 4, i + 2);

				//int price = Integer.parseInt(sListEntrys[i][5]);
				int price = pe.getPrice();
				totalprice += price;
				T.add(nf.format(price), 5, i + 2);
				if (!ifPaid) {
					//T.add( new
					// CheckBox(this.sParameterPrefix+"pmnt_chk"+i,sListEntrys[i][3]),6,i+2);
					T.add(new CheckBox(this.sParameterPrefix + "pmnt_chk" + i, String.valueOf(pe.getPaymentId())), 6, i + 2);
				}
				else
					T.add(iwrb.getImage("greitt.gif"), 6, i + 2);

			}
			String sTotal = iwrb.getLocalizedString("total", "Total");
			T.add("Alls : ", 4, iSlen + 2);
			T.add(nf.format(totalprice), 5, iSlen + 2);
			if (!ifPaid)
				T.add(new SubmitButton(iwrb.getImage("update.gif")), 6, iSlen + 2);
			myForm.add(T);

			myForm.add(new HiddenInput(sParameterPrefix + "action", "update"));
			myForm.add(new HiddenInput(sParameterPrefix + "listcount", String.valueOf(iSlen)));
			MainTable.add(myForm, 1, 2);
		}
		else {
			String sMsg = iwrb.getLocalizedString("pb_msg1", "Found nothing");
			this.sMessage = sMsg;

		}

		MainTable.add("<br><br><br>", 1, 3);
		MainTable.add(sMessage, 1, 3);
		add(MainTable);

	}

	private void doUpdate(IWContext modinfo) throws SQLException {
		int iLen = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "listcount"));
		for (int i = 0; i < iLen; i++) {
			String sChkid = modinfo.getParameter(this.sParameterPrefix + "pmnt_chk" + i);
			if (sChkid != null) {
				int iPaymentId = Integer.parseInt(sChkid);
				try {
					Payment eP = ((PaymentHome) IDOLookup.getHomeLegacy(Payment.class)).findByPrimaryKey(iPaymentId);
					eP.setStatus(true);
					eP.setLastUpdated(IWTimestamp.getTimestampRightNow());
					eP.setCashierId(this.iCashierID);
					eP.update();

					int accountId = eP.getAccountId();
					TariffService.makeAccountEntry(accountId, eP.getPrice(), eP.getName(), "Greiðsla", "", "", "", this.iCashierID, IWTimestamp.getTimestampRightNow(), IWTimestamp.getTimestampRightNow());
					String sMsg = iwrb.getLocalizedString("pb_msg2", "Payments were booked");
					sMessage = sMsg;
				}
				catch (SQLException e) {
					String sMsg = iwrb.getLocalizedString("pb_msg3", "Failed to update payments");
					this.sMessage = sMsg;
					e.printStackTrace();
				}
				catch(FinderException fe) {
					fe.printStackTrace();
				}

			}

		}

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(1), 1, 1);
		add(MainTable);

	}

	private String[][] getPaymentsMembers(int iUnionID, int iYear, int iMonth, int iDay, int iPayType, boolean ifPaid) {
		String[][] sReturningMatrix = null;

		try {
			Payment[] ePayments = getPayments(iUnionID, iYear, iMonth, iDay, iPayType, ifPaid);
			if (ePayments != null) {
				int iLen = ePayments.length;
				if (iLen != 0) {
					sReturningMatrix = new String[iLen][6];
					Member eMember;
					Payment ePayment;
					for (int i = 0; i < iLen; i++) {
						ePayment = ePayments[i];
						eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(ePayment.getMemberId());
						sReturningMatrix[i][0] = String.valueOf(eMember.getID());
						sReturningMatrix[i][1] = eMember.getName();
						sReturningMatrix[i][2] = eMember.getSocialSecurityNumber();
						sReturningMatrix[i][3] = String.valueOf(ePayment.getID());
						sReturningMatrix[i][4] = new IWTimestamp(ePayment.getPaymentDate()).toSQLDateString();
						sReturningMatrix[i][5] = String.valueOf(ePayment.getPrice());
						pElement pe = new pElement(eMember.getID(), eMember.getName(), eMember.getSocialSecurityNumber(), ePayment.getID(), new IWTimestamp(ePayment.getPaymentDate()), ePayment.getPrice());
					}
				}
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}

		return sReturningMatrix;
	}

	private List getPaymentsMembersList(int iUnionID, int iYear, int iMonth, int iDay, int iPayType, boolean ifPaid) {
		Vector vector = null;

		try {
			Payment[] ePayments = getPayments(iUnionID, iYear, iMonth, iDay, iPayType, ifPaid);
			if (ePayments != null) {
				int iLen = ePayments.length;
				if (iLen != 0) {
					vector = new Vector(iLen);
					Member eMember;
					Payment ePayment;
					for (int i = 0; i < iLen; i++) {
						ePayment = ePayments[i];
						eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(ePayment.getMemberId());
						pElement pe = new pElement(eMember.getID(), eMember.getName(), eMember.getSocialSecurityNumber(), ePayment.getID(), new IWTimestamp(ePayment.getPaymentDate()), ePayment.getPrice());
						vector.addElement(pe);
					}
					Collections.sort(vector);
				}
			}
		}
		catch (FinderException sqle) {
			sqle.printStackTrace();
		}

		return vector;
	}

	private void doEntrySearch(IWContext modinfo) {
		Form myForm = new Form();
		myForm.maintainAllParameters();

		Table T = new Table(2, 4);
		T.setWidth(this.sTablewidth);
		T.setHorizontalZebraColored(sDarkColor, sLightColor);
		T.setRowColor(1, sHeaderColor);
		T.setCellpadding(2);
		T.setCellspacing(1);

		IWTimestamp today = IWTimestamp.RightNow();
		IWTimestamp from = new IWTimestamp(1, 1, today.getYear());
		IWTimestamp to = new IWTimestamp(31, 12, today.getYear());
		if (modinfo.isParameterSet("from_date"))
			from = parseStamp(modinfo.getParameter("from_date"));
		if (modinfo.isParameterSet("to_date"))
			to = parseStamp(modinfo.getParameter("to_date"));

		String sFromDate = getDateString(from);
		String sToDate = getDateString(to);

		TextInput tiFromDate = new TextInput("from_date", sFromDate);
		tiFromDate.setLength(10);
		TextInput tiToDate = new TextInput("to_date", sToDate);
		tiToDate.setLength(10);

		String sFrom = iwrb.getLocalizedString("from", "From");
		String sTo = iwrb.getLocalizedString("to", "To");
		T.add(sFrom + " :", 1, 2);
		T.add(sTo + " :", 1, 3);
		T.add(tiFromDate, 2, 2);
		T.add(tiToDate, 2, 3);
		T.add(new SubmitButton(iwrb.getImage("search.gif")), 2, 4);
		T.add(new HiddenInput("entry_search", "true"));

		myForm.add(T);
		myForm.add(new HiddenInput(sParameterPrefix + "action", "entries"));

		Table MainTable = makeMainTable();
		MainTable.setWidth("100%");
		MainTable.add(makeLinkTable(2), 1, 1);
		MainTable.add(myForm, 1, 2);
		MainTable.add("<br><br><br>", 1, 3);
		if (modinfo.isParameterSet("entry_search")) {
			MainTable.add(getEntryResultsTable(from, to), 1, 4);

		}
		add(MainTable);

	}

	public Table getEntryResultsTable(IWTimestamp from, IWTimestamp to) {

		Table table = new Table();
		table.setWidth(sTablewidth);
		table.setBorder(1);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.add(iwrb.getLocalizedString("name", "Name"), 1, 1);
		table.add(iwrb.getLocalizedString("info", "Info"), 2, 1);
		table.add(iwrb.getLocalizedString("count", "Count"), 3, 1);
		table.add(iwrb.getLocalizedString("debet", "Debet"), 4, 1);
		table.add(iwrb.getLocalizedString("kredit", "Kredit"), 5, 1);
		/*
		 * select e.name ,e.info ,count(e.account_id), sum(e.price) summa from
		 * account_entry e,account a where e.account_id = a.account_id and
		 * a.union_id = 93 and a.account_year_id = 2 group by e.name,e.info
		 */
		StringBuffer sql = new StringBuffer("select e.name ,e.info ,count(e.account_id),sum(e.price)");
		sql.append(" from account_entry e,account a ");
		sql.append(" where e.account_id = a.account_id ");
		sql.append(" and a.union_id = ");
		sql.append(this.iUnionID);
		if (from != null) {
			sql.append(" and e.last_updated >= '");
			sql.append(from.getSQLDate());
			sql.append("'");
		}
		if (to != null) {
			sql.append(" and e.last_updated <= '");
			sql.append(to.getSQLDate());
			sql.append(" 23:59:59'");
		}
		sql.append("group by e.name,e.info");
		//System.err.println(sql.toString());
		try {
			Connection Conn = com.idega.util.database.ConnectionBroker.getConnection();
			Statement stmt = Conn.createStatement();
			ResultSet RS = null;

			RS = stmt.executeQuery(sql.toString());
			String name, info;
			int count = 0, amount = 0, debet = 0, kredit = 0, total = 0, totcount = 0;
			int row = 2;
			while (RS.next()) {
				name = RS.getString(1); // name
				if (!RS.wasNull()) {
					table.add(name, 1, row);
				}
				info = RS.getString(2); // info
				if (!RS.wasNull()) {
					table.add(info, 2, row);
				}
				count = RS.getInt(3); // last_name
				if (!RS.wasNull()) {
					table.add(String.valueOf(count), 3, row);
				}
				amount = RS.getInt(4); // Socialnumber
				if (!RS.wasNull()) {
					if (amount >= 0) {
						table.add(String.valueOf(amount), 4, row);
						debet += amount;
					}
					else {
						table.add(String.valueOf(amount), 5, row);
						kredit += amount;
					}
					total += amount;

				}
				row++;
			}
			table.add(String.valueOf(debet), 4, row);
			table.add(String.valueOf(kredit), 5, row);
			row++;
			if (total >= 0)
				table.add(String.valueOf(total), 4, row);
			else
				table.add(String.valueOf(total), 5, row);

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return table;
	}

	private String getDateString(IWTimestamp stamp) {
		return stamp.getISLDate(".", true);
	}

	private IWTimestamp parseStamp(String sDate) {
		IWTimestamp it = new IWTimestamp();
		try {
			StringTokenizer st = new StringTokenizer(sDate, " .-/+");
			int day = 1, month = 1, year = 2001;
			if (st.hasMoreTokens()) {
				day = Integer.parseInt(st.nextToken());
				month = Integer.parseInt(st.nextToken());
				year = Integer.parseInt(st.nextToken());

			}
			it = new IWTimestamp(day, month, year);
		}
		catch (Exception pe) {
			it = new IWTimestamp();
		}
		return it;
	}

	public class pElement implements java.lang.Comparable {

		private int iMemberId;
		private String sName;
		private String sSocialSecurityNumber;
		private int iPaymentId;
		private IWTimestamp itPayDate;
		private int iPrice;

		public pElement(int iMemberId, String sName, String sSocialSecurityNumber, int iPaymentId, IWTimestamp itPayDate, int iPrice) {
			this.iMemberId = iMemberId;
			this.sName = sName;
			this.sSocialSecurityNumber = sSocialSecurityNumber;
			this.iPaymentId = iPaymentId;
			this.itPayDate = itPayDate;
			this.iPrice = iPrice;
		}

		public int compareTo(Object pE) {
			return sName.compareTo(((pElement) pE).getName());
		}

		public String toString() {
			return sName + sSocialSecurityNumber;
		}

		public void setMemberId(int i) {
			iMemberId = i;
		}

		public int getMemberId() {
			return iMemberId;
		}

		public void setName(String s) {
			sName = s;
		}

		public String getName() {
			return sName;
		}

		public void setSocialSecurityNumberId(String i) {
			sSocialSecurityNumber = i;
		}

		public String getSocialSecurityNumberId() {
			return sSocialSecurityNumber;
		}

		public void setPaymentId(int i) {
			iPaymentId = i;
		}

		public int getPaymentId() {
			return iPaymentId;
		}

		public void setPayDate(IWTimestamp i) {
			itPayDate = i;
		}

		public IWTimestamp getPayDate() {
			return itPayDate;
		}

		public void setPrice(int i) {
			iPrice = i;
		}

		public int getPrice() {
			return iPrice;
		}

	}

	private Payment[] getPayments(int iUnionID, int iYear, int iMonth, int iDay, int iPayType, boolean ifPaid) {
		java.text.DecimalFormat dateFormatter = new DecimalFormat("00");
		StringBuffer sbSQLpaydate = new StringBuffer("'");
		if (iYear == 0) {
			sbSQLpaydate.append("%");
		}
		else {
			sbSQLpaydate.append(iYear);
		}
		sbSQLpaydate.append("-");
		if (iMonth == 0) {
			sbSQLpaydate.append("%");
		}
		else {
			sbSQLpaydate.append(dateFormatter.format(iMonth));
		}
		sbSQLpaydate.append("-");
		if (iDay == 0) {
			sbSQLpaydate.append("%");
		}
		else
			sbSQLpaydate.append(dateFormatter.format(iDay));
		sbSQLpaydate.append("%'");

		String sPayTypeInSQL = " and payment_type_id like '" + iPayType + "'";
		if (iPayType == 0)
			sPayTypeInSQL = "";

		StringBuffer sbSQL = new StringBuffer("select * from payment where payment_date like ");
		sbSQL.append(sbSQLpaydate.toString());
		if (ifPaid)
			sbSQL.append(" and status like 'Y'");
		else
			sbSQL.append(" and status like 'N'");
		sbSQL.append(sPayTypeInSQL);
		sbSQL.append(" and round_id in (select payment_round_id from payment_round where union_id like '");
		sbSQL.append(this.sUnionID);
		sbSQL.append("')");

		// debug
		//add(sbSQL.toString());
		Payment[] ePayments = null;
		try {
			ePayments = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAll(sbSQL.toString());
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return ePayments;

	}

	private void doView(IWContext modinfo) throws SQLException {
		Form myForm = new Form();
		myForm.maintainAllParameters();

		Table T = new Table(2, 5);
		T.setWidth(this.sTablewidth);
		T.setHorizontalZebraColored(sDarkColor, sLightColor);
		T.setRowColor(1, sHeaderColor);
		T.setRowColor(5, "#FFFFFF");
		T.setCellpadding(2);
		T.setCellspacing(1);
		String sPaytype = iwrb.getLocalizedString("paytype", "Paytype");
		String sMonth = iwrb.getLocalizedString("montofduedate", "Due month");
		String sHow = iwrb.getLocalizedString("how", "How");
		T.add(sPaytype + " :", 1, 2);
		T.add(sMonth + " :", 1, 3);
		T.add(sHow + " :", 1, 4);
		T.add(this.drpPayCompany(sParameterPrefix + "drppaycompany"), 2, 2);
		T.add(this.drpMonth(sParameterPrefix + "drpmonth"), 2, 3);
		T.add(this.drpYear(sParameterPrefix + "drpyear"), 2, 3);
		T.add(new SubmitButton(iwrb.getImage("choose.gif")), 2, 5);

		myForm.add(T);
		myForm.add(new HiddenInput(sParameterPrefix + "action", "file"));

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(3), 1, 1);
		MainTable.add(myForm, 1, 2);
		MainTable.add("<br><br><br>", 1, 3);
		add(MainTable);

	}

	private void doFetchFile(IWContext modinfo) throws SQLException {
		String sLowerCaseUnionAbbreviation = this.sUnionAbbrev.toLowerCase();
		String fileSeperator = System.getProperty("file.separator");
		String filepath = modinfo.getServletContext().getRealPath(fileSeperator + sLowerCaseUnionAbbreviation + fileSeperator);
		String fileLink = (filepath + fileSeperator);

		Form myForm = new Form();
		myForm.maintainAllParameters();
		//myForm.setAction("/tarif/inputfile.jsp");
		myForm.setMultiPart();
		//com.idega.io.FileSaver.setUploadDir(modinfo,fileLink);
		modinfo.getSession().setAttribute(sParameterPrefix + "action", "uploadfile");
		myForm.add(new HiddenInput(FileSaver.getUploadDirParameterName(), fileLink));
		myForm.add(new FileInput());
		myForm.add(new SubmitButton());

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(5), 1, 1);
		MainTable.add("Choose file", 1, 2);
		MainTable.add(myForm, 1, 3);
		MainTable.add("<br><br><br>", 1, 4);
		add(MainTable);
	}

	private void doUploadFile(IWContext modinfo) {
		modinfo.getSession().removeAttribute(sParameterPrefix + "action");
		String fileSeperator = System.getProperty("file.separator");
		String sMessage = "";
		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(5), 1, 1);
		/*
		 * try{ File f = FileSaver.FileToDir(modinfo); FileReader reader = new
		 * FileReader(fileName); LineNumberReader lineReader = new
		 * LineNumberReader(reader); Payment P; String line; lineReader.mark(1);
		 * while (lineReader.read() != -1) { lineReader.reset(); line =
		 * lineReader.readLine(); if(line.startsWith("G2")){ int PID =
		 * line.substring(45,53); } lineReader.mark(1); } } catch
		 * (FileNotFoundException f) { sMessage ="File not
		 * found";e.printStackTrace(); } catch(IOException e){sMessage ="IO
		 * Error";e.printStackTrace();}
		 */
		try {
			MultipartParser mp = new MultipartParser(modinfo.getRequest(), 10 * 1024 * 1024); // 10MB
			Part part;
			File dir = null;
			String value = null;
			while ((part = mp.readNextPart()) != null) {
				String name = part.getName();
				if (part.isParam() && part.getName().equalsIgnoreCase(FileSaver.getUploadDirParameterName())) {

					ParamPart paramPart = (ParamPart) part;
					dir = new File(paramPart.getStringValue());
					value = paramPart.getStringValue();
					//add("<br>");
					//add("\nparam; name=" + name + ", value=" + value);
				}
				if (part.isFile() && dir != null) {
					// it's a file part
					FilePart filePart = (FilePart) part;
					String fileName = filePart.getFileName();
					if (fileName != null) {
						//dir = new File(value+fileSeperator+fileName);
						// the part actually contained a file
						long size = filePart.writeTo(dir);
						//add("<br>");
						//add("\nfile; name=" + name + "; filename=" + fileName +", content
						// type=" + filePart.getContentType() + " size=" + size);
						String l = fileSeperator + this.sUnionAbbrev.toLowerCase() + fileSeperator;
						if (filePart.getContentType().equalsIgnoreCase("image/pjpeg")) {
							MainTable.add(new Image(l + fileName), 1, 3);
						}
						else
							MainTable.add(new Link(l + fileName), 1, 3);
					}
				}
			}
		}
		catch (IOException lEx) {
			System.err.print("error reading or saving file");
		}

		MainTable.add(sMessage, 1, 2);
		MainTable.add("<br><br>", 1, 3);
		add(MainTable);

	}

	private void doFile(IWContext modinfo) throws SQLException {
		int iCompany = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drppaycompany"));
		int iMonth = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drpmonth"));
		int iYear = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drpyear"));

		Form myForm = new Form();
		myForm.maintainAllParameters();
		Table T = new Table();
		if (iCompany == this.iGIROID) {

			TextInput bankofficeInput = new TextInput("payment_bank_office");
			bankofficeInput.setAsIntegers();
			bankofficeInput.setLength(4);
			bankofficeInput.setSize(4);
			bankofficeInput.setMaxlength(4);
			String sNone = iwrb.getLocalizedString("none", "None");
			DropdownMenu drdFinalPayDay = new DropdownMenu("payment_finalpayday");
			drdFinalPayDay.addDisabledMenuElement("0", "sNone");
			for (int i = 1; i < 31; i++) {
				drdFinalPayDay.addMenuElement(String.valueOf(i));
			}

			TextInput B1input = new TextInput("payment_girotext1");
			B1input.setMaxlength(70);
			B1input.setSize(70);
			TextInput B2input = new TextInput("payment_girotext2");
			B2input.setMaxlength(70);
			B2input.setSize(70);
			TextInput B3input = new TextInput("payment_girotext3");
			B3input.setMaxlength(70);
			B3input.setSize(70);
			TextInput B4input = new TextInput("payment_girotext4");
			B4input.setMaxlength(70);
			B4input.setSize(70);

			T = new Table(2, 9);
			T.setHorizontalZebraColored(sDarkColor, sLightColor);
			T.setRowColor(1, sHeaderColor);
			T.setRowColor(9, "#FFFFFF");
			T.setCellpadding(iCellpadding);
			T.setCellspacing(iCellspacing);
			T.setWidth(sTablewidth);

			T.add("Útibú banka", 1, 2);
			T.add("Eindagi ", 1, 3);
			T.add("Texti Gíróseðla :", 1, 4);
			T.add(" 1.Textalína", 1, 5);
			T.add(" 2.Textalína", 1, 6);
			T.add(" 3.Textalína", 1, 7);
			T.add(" 4.Textalína", 1, 8);

			T.add(bankofficeInput, 2, 2);
			T.add(drdFinalPayDay, 2, 3);
			T.add(B1input, 2, 5);
			T.add(B2input, 2, 6);
			T.add(B3input, 2, 7);
			T.add(B4input, 2, 8);
			T.add(new SubmitButton(new Image("/pics/tarif/skrifa.gif")), 2, 9);
		}
		else if (iCompany == this.iEUROID || iCompany == this.iVISAID) {
			// KreditCard part
			DropdownMenu drpKredit = new DropdownMenu("payment_kreditcc");
			drpKredit.addMenuElement("euro", "Euro");
			drpKredit.addMenuElement("visa", "Visa");

			TextInput kreditPrInput = new TextInput("payment_kredit_pr");
			TextInput kreditKrInput = new TextInput("payment_kredit_kr");
			kreditPrInput.setAsIntegers();
			kreditKrInput.setAsIntegers();
			kreditPrInput.setLength(4);
			kreditPrInput.setSize(4);
			kreditPrInput.setMaxlength(4);
			kreditKrInput.setLength(4);
			kreditKrInput.setSize(4);
			kreditKrInput.setMaxlength(4);

			TextInput kreditContractNr = new TextInput("payment_kredit_contract");

			T = new Table(2, 6);
			T.setHorizontalZebraColored(sDarkColor, sLightColor);
			T.setRowColor(1, sHeaderColor);
			T.setRowColor(6, "#FFFFFF");
			T.setCellpadding(iCellpadding);
			T.setCellspacing(iCellspacing);
			T.setWidth(sTablewidth);
			T.add("Kortafyrirtæki", 1, 2);
			T.add("Færslugjald (%) ", 1, 3);
			T.add("Færslugjald (kr)", 1, 4);
			T.add("Samningsnúmer", 1, 5);

			T.add(iCompany == this.iEUROID ? "Euro" : "Visa", 2, 2);
			T.add(kreditPrInput, 2, 3);
			T.add(kreditKrInput, 2, 4);
			T.add(kreditContractNr, 2, 5);
			T.add(new SubmitButton(new Image("/pics/tarif/skrifa.gif")), 2, 6);
		}

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(3), 1, 1);
		MainTable.add(new HiddenInput(sParameterPrefix + "action", "write"));
		MainTable.add(T, 1, 2);

		myForm.add(MainTable);
		myForm.add(new HiddenInput(sParameterPrefix + "drppaycompany", String.valueOf(iCompany)));
		myForm.add(new HiddenInput(sParameterPrefix + "drpmonth", String.valueOf(iMonth)));
		myForm.add(new HiddenInput(sParameterPrefix + "drpyear", String.valueOf(iYear)));
		//add(new DateInput(this.sParameterPrefix));

		add(myForm);
		/*
		 * if(isAdmin()) { form.add(new Link(new
		 * Image("/pics/flipar_takkar/stofnafelaga1.gif"),memberWindow)); Window
		 * tarifWindow = new Window("Gjaldskrá",600,600,"/tarif/tarif.jsp");
		 * tarifWindow.setStatus(true) ; tarifWindow.setResizable(true); Link linkur =
		 * new Link(new Image("/pics/flipar_takkar/gjaldskra1.gif"),tarifWindow);
		 * form.add(linkur); } form.add(table); add(form);
		 */

	}

	private void doWriteFile(IWContext modinfo) throws SQLException, IOException {
		int iCompany = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drppaycompany"));
		int iMonth = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drpmonth"));
		int iYear = Integer.parseInt(modinfo.getParameter(sParameterPrefix + "drpyear"));
		String bank = modinfo.getParameter("payment_bank");
		String bnkofc = modinfo.getParameter("payment_bank_office");
		String kreditcc = modinfo.getParameter("payment_kreditcc");
		String Message = "";
		double dPercent = 0.0;
		int iAmount = 0;
		String sFileLink = "";
		try {

			Payment[] ePayments = this.getPayments(this.iUnionID, iYear, iMonth, 0, iCompany, false);
			if (iCompany == this.iGIROID) {
				if (bnkofc != null || !bnkofc.equals("")) {
					int bankOffice = Integer.parseInt(bnkofc);
					int finalpayday = Integer.parseInt(modinfo.getParameter("payment_finalpayday"));
					String B1input = modinfo.getParameter("payment_girotext1");
					String B2input = modinfo.getParameter("payment_girotext2");
					String B3input = modinfo.getParameter("payment_girotext3");
					String B4input = modinfo.getParameter("payment_girotext4");
					GiroFile GF = new GiroFile();
					GF.writeFile(modinfo, ePayments, bankOffice, finalpayday, B1input, B2input, B3input, B4input, this.iUnionID);
					sFileLink = GF.getFileLinkString();
					Message = ("<H3>File was saved</H3>");
				}
			}
			else if (iCompany == this.iEUROID || iCompany == this.iVISAID) {
				String sKreditPercent = modinfo.getParameter("payment_kredit_pr");
				String sKreditAmount = modinfo.getParameter("payment_kredit_kr");
				String sContractNumber = modinfo.getParameter("payment_kredit_contract");

				if (sKreditPercent.equalsIgnoreCase("")) {
					dPercent = 0.0;
					if (sKreditAmount != null)
						iAmount = Integer.parseInt(sKreditAmount);
				}
				else if (sKreditAmount.equalsIgnoreCase("")) {
					iAmount = 0;
					if (sKreditPercent != null) {
						dPercent = Double.parseDouble(sKreditPercent);
						dPercent = (dPercent / 100.0) + 1.0;
					}
				}
				if (iCompany == this.iEUROID) {
					EuroFile EF = new EuroFile();
					EF.writeFile(modinfo, ePayments, sContractNumber, dPercent, iAmount, this.iUnionID);
					sFileLink = EF.getFileLinkString();
					Message = ("<H3>File was saved</H3>");
				}
				if (iCompany == this.iVISAID) {
					VisaFile VF = new VisaFile();
					VF.writeFile(modinfo, ePayments, sContractNumber, dPercent, iAmount, this.iUnionID);
					sFileLink = VF.getFileLinkString();
					Message = ("<H3>File was saved</H3>");
				}
			}
			else
				Message = "<H3> No file was saved</H3>";
		}
		catch (NumberFormatException e) {
			Message = "<H3> No file was saved</H3>";
		}
		catch (FinderException fe) {
			Message = "<h3>No file was saved</h3>";
		}
		finally {
			Table MainTable = makeMainTable();
			MainTable.add(makeLinkTable(3), 1, 2);
			MainTable.add(Message, 1, 4);
			MainTable.add(new Link("right click and save", sFileLink), 1, 5);
			add(MainTable);
			//add(modinfo.getRequest().getRequestURI());
		}

	}

	private DropdownMenu drpDays(String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		for (int i = 1; i < 32; i++) {
			drp.addMenuElement(i, String.valueOf(i));
		}
		return drp;
	}

	private DropdownMenu drpMonth(String name) {
		IWTimestamp Today = new IWTimestamp();
		int iMonth = Today.getMonth();
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		for (int i = 1; i < 13; i++) {
			drp.addMenuElement(i, String.valueOf(i));
		}
		drp.setSelectedElement(String.valueOf(iMonth));
		return drp;
	}

	private DropdownMenu drpYear(String name) {
		IWTimestamp it = new IWTimestamp();
		int a = it.getYear();
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		for (int i = a - 10; i < a + 10; i++) {
			drp.addMenuElement(i, String.valueOf(i));
		}
		drp.setSelectedElement(String.valueOf(a));
		return drp;
	}

	private DropdownMenu drpPayCompany(String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		drp.addMenuElement(this.iGIROID, "Banki");
		drp.addMenuElement(this.iEUROID, "Euro");
		drp.addMenuElement(this.iVISAID, "Visa");
		return drp;
	}

	private DropdownMenu drpPayType(String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		try {
			PaymentType[] ePT = (PaymentType[]) ((PaymentType) IDOLookup.instanciateEntity(PaymentType.class)).findAll();
			int len = ePT.length;
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					drp.addMenuElement(ePT[i].getID(), ePT[i].getName());
				}
			}
		}
		catch (SQLException s) {
			s.printStackTrace();
		}
		return drp;
	}

	private Table makeMainTable() {
		Table MainTable = new Table(1, 5);
		MainTable.setWidth(1, "100");
		MainTable.setCellspacing(0);
		MainTable.setCellpadding(0);
		return MainTable;
	}

	private Table makeLinkTable(int menuNr) {
		Table LinkTable = new Table(1, 1);
		LinkTable.setBorder(0);
		LinkTable.setCellpadding(0);
		LinkTable.setCellspacing(0);

		LinkTable.setWidth(sTablewidth);

		Link MainLink = new Link(menuNr == 1 ? iwrb.getImage("ratelist.gif") : iwrb.getImage("ratelist1.gif"), "/tarif/tarif.jsp");
		MainLink.addParameter("catal_action", "view");
		MainLink.addParameter("union_id", this.sUnionID);

		Link UpdateLink = new Link(menuNr == 1 ? iwrb.getImage("find.gif") : iwrb.getImage("find1.gif"));
		UpdateLink.addParameter("payment_action", "main");

		Link OutLink = new Link(menuNr == 1 ? iwrb.getImage("look.gif") : iwrb.getImage("look1.gif"));
		OutLink.addParameter(sParameterPrefix + "action", "entries");

		Link pFileLink = new Link(menuNr == 1 ? iwrb.getImage("utskrift.gif") : iwrb.getImage("utskrift1.gif"));

		pFileLink.addParameter(sParameterPrefix + "action", "pfile");

		Link RollbackLink = new Link(menuNr == 1 ? iwrb.getImage("correction.gif") : iwrb.getImage("correction1.gif"), "/tarif/rollbackpaym.jsp");
		//Rollback.addParameter("catal_action","rollback");
		RollbackLink.addParameter("union_id", sUnionID);

		Link UploadLink = new Link(menuNr == 1 ? iwrb.getImage("skraning.gif") : iwrb.getImage("skraning1.gif"));
		UploadLink.addParameter(sParameterPrefix + "action", "fetch");

		LinkTable.add(MainLink, 1, 1);
		if (isAdmin) {
			LinkTable.add(UpdateLink, 1, 1);
			LinkTable.add(OutLink, 1, 1);
			LinkTable.add(pFileLink, 1, 1);
			LinkTable.add(RollbackLink, 1, 1);
			//LinkTable.add(UploadLink,1,1);
		}
		return LinkTable;
	}

	private Payment[] getUnionPayments(int month, int payment_type_id) {
		DecimalFormat monthFormatter = new DecimalFormat("00");
		IWTimestamp today = new IWTimestamp();
		String strMonth = monthFormatter.format(month);
		Payment[] P;
		try {
			P = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAll("select * from payment where payment_type_id = '" + payment_type_id + "' and payment_date like '" + today.getYear() + "-" + strMonth + "-%'  and  status ='N' ");
		}
		catch (SQLException e) {
			sMessage = "";
			P = null;
		}
		return P;
	}

	private void doPaymentFile(IWContext modinfo) throws SQLException {
		Form myForm = new Form();
		myForm.maintainAllParameters();
		Table T = new Table();

		AccountYear years[] = (AccountYear[]) ((AccountYear) IDOLookup.instanciateEntity(AccountYear.class)).findAllByColumnEquals("union_id", iUnionID);
		DropdownMenu drpYears = new DropdownMenu("account_year_id");
		for (int i = 0; i < years.length; i++) {
			drpYears.addMenuElement(years[i].getID(), years[i].getName());
			if (years[i].getActive())
				drpYears.setSelectedElement(String.valueOf(years[i].getID()));
		}

		PaymentType types[] = (PaymentType[]) ((PaymentType) IDOLookup.instanciateEntity(PaymentType.class)).findAll();
		DropdownMenu drpTypes = new DropdownMenu("payment_type");
		for (int i = 0; i < types.length; i++) {
			drpTypes.addMenuElement(types[i].getID(), types[i].getName());
		}

		T.mergeCells(1, 1, 2, 1);
		T.add(eUnion.getName() + " Greiðsluskrár");
		T.add("Reikningsár", 1, 2);
		T.add(drpYears, 2, 2);
		T.add("Greiðslugerð", 1, 3);
		T.add(drpTypes, 2, 3);
		T.add(new SubmitButton(new Image("/pics/tarif/skrifa.gif")), 2, 4);
		T.setWidth(this.sTablewidth);
		T.setHorizontalZebraColored(sDarkColor, sLightColor);
		T.setRowColor(4, "#FFFFFF");
		String fileSeperator = System.getProperty("file.separator");
		String filepath = modinfo.getServletContext().getRealPath(fileSeperator + eUnion.getAbbrevation().toLowerCase() + fileSeperator + "files" + fileSeperator);

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(3), 1, 1);
		MainTable.add(new HiddenInput(sParameterPrefix + "action", "pamwrite"));
		MainTable.add(T, 1, 2);
		MainTable.add(getFileTable(modinfo, filepath), 1, 3);
		// MainTable.setBorder(1);
		myForm.add(MainTable);
		add(myForm);
	}

	private Table getFileTable(IWContext modinfo, String filePath) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		//T.setRowColor(1,sHeaderColor);
		try {
			File F = new File(filePath);
			File[] Fs = F.listFiles();
			if (Fs.length > 0) {
				String name;
				java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
				int row = 1;
				Window w = new Window("files", "/servlet/excel");
				w.setResizable(true);
				w.setMenubar(true);
				T.add("Filename", 1, row);
				T.add("Size", 2, row);
				T.add("Modified", 3, row);
				row++;
				for (int i = 0; i < Fs.length; i++) {
					name = Fs[i].getName();
					Link L = new Link(new Text(name), w);
					L.addParameter("dir", filePath + "/" + name);
					L.setFontSize(2);
					T.add(L, 1, row);
					T.add(Long.toString(Fs[i].length() / 1000) + " KB", 2, row);
					T.add(new IWTimestamp(Fs[i].lastModified()).getLocaleDate(LocaleUtil.getIcelandicLocale()), 3, row);
					row++;
				}
			}
			else {
				T.add(iwrb.getLocalizedString("no_files", "No files"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//T.setWidth("100%");
		T.setCellpadding(2);
		T.setCellspacing(1);
		T.setWidth(this.sTablewidth);
		T.setHorizontalZebraColored(sDarkColor, sLightColor);
		return T;
	}

	private void writePaymentFile(IWContext modinfo) throws SQLException {
		int type_id = Integer.parseInt(modinfo.getParameter("payment_type"));
		int year_id = Integer.parseInt(modinfo.getParameter("account_year_id"));
		String fileSeperator = System.getProperty("file.separator");
		String filepath = modinfo.getServletContext().getRealPath(fileSeperator + eUnion.getAbbrevation().toLowerCase() + fileSeperator + "files" + fileSeperator);
		try {
			new PaymentFiler().makeFile(modinfo, filepath, type_id, year_id, iUnionID);
			doPaymentFile(modinfo);
		}
		catch (Exception ex) {
			add("villa");
			ex.printStackTrace();
		}
	}

	public void main(IWContext modinfo) throws IOException {
		//isAdmin =
		/** @todo: fixa Admin*/
		iwrb = getResourceBundle(modinfo);
		iwb = getBundle(modinfo);
		isAdmin = true;
		control(modinfo);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}// class PaymentBooker
