package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.entity.Account;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Payment;
import is.idega.idegaweb.golf.entity.PaymentHome;
import is.idega.idegaweb.golf.entity.PaymentRound;
import is.idega.idegaweb.golf.entity.PaymentRoundHome;
import is.idega.idegaweb.golf.entity.PaymentType;
import is.idega.idegaweb.golf.entity.PaymentTypeHome;
import is.idega.idegaweb.golf.entity.PriceCatalogue;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * @version 1.0
 */
public class PaymentViewer extends com.idega.presentation.PresentationObjectContainer {

	private String union_id, unionName, unionAbbrev, member_id;
	private int un_id, mem_id, cashier_id;
	private Union union;
	private String[][] Values;
	private Member thisMember, Cashier;
	private Member[] unionMembers;
	private PriceCatalogue[] Catalogs;
	private String MenuColor, ItemColor, HeaderColor, LightColor, DarkColor, OtherColor;
	private boolean isAdmin = false;
	private java.util.Locale currentLocale;
	private Link sidan, UpdateLink, PriceLink;
	private Window window;
	private Member[][] mbsArray;
	private Integer[][] totals;
	private int cellspacing = 1, cellpadding = 2;
	private Thread payThread = null;
	private String sTablewidth = "650";
	private int numOfCat, inputLines, saveCount, count, memberCount = 0;
	private String payment_action = "";
	private Payment[] memberPayments;
	private String strMessage = "";

	public PaymentViewer() {

		HeaderColor = "#336660";
		LightColor = "#CEDFD0";
		DarkColor = "#ADCAB1";
		OtherColor = "#6E9073";

		setMenuColor("#ADCAB1");//,"#CEDFD0"
		setItemColor("#CEDFD0");//"#D0F0D0"
		setInputLines(15);
		currentLocale = java.util.Locale.getDefault();
	}

	public void setMenuColor(String MenuColor) {
		this.MenuColor = MenuColor;
	}

	public void setItemColor(String ItemColor) {
		this.ItemColor = ItemColor;
	}

	public void setInputLines(int inputlines) {
		this.inputLines = inputlines;
	}

	private void control(IWContext modinfo) throws IOException {

		try {
			if (modinfo.getParameter("member_id") != null) {
				member_id = modinfo.getParameter("member_id");
				modinfo.getSession().setAttribute("payment_member_id", member_id);
			}

			union_id = (String) modinfo.getSession().getAttribute("golf_union_id");
			member_id = (String) modinfo.getSession().getAttribute("payment_member_id");

			if (modinfo.getSession().getAttribute("member_login") != null) {
				Cashier = (Member) modinfo.getSession().getAttribute("member_login");
				cashier_id = Cashier.getID();
			}

			un_id = Integer.parseInt(union_id);
			union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(un_id);
			unionName = union.getName();
			unionAbbrev = union.getAbbrevation();

			if (member_id != null) {
				mem_id = Integer.parseInt(member_id);
				thisMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(mem_id);
			}
			else
				mem_id = -1;

			boolean hasSomeValues = false;
			strMessage = "";

			if (modinfo.getRequest().getParameter("payment_action") == null) {
				doMain(modinfo);
			}
			if (modinfo.getRequest().getParameter("payment_action") != null) {
				payment_action = modinfo.getRequest().getParameter("payment_action");

				if (payment_action.equals("main")) {
					doMain(modinfo);
				}
				if (payment_action.equals("change")) {
					doChange(modinfo);
				}
				if (payment_action.equals("update")) {
					doUpdate(modinfo);
				}
				if (payment_action.equals("view")) {
					doView(modinfo);
				}
				if (payment_action.equals("save")) {
					doSave(modinfo);
				}
				if (payment_action.equals("list")) {
					doList(modinfo);
				}
				if (payment_action.equals("new")) {
					doNew(modinfo);
				}
				if (payment_action.equals("updatenew")) {
					doUpdateNew(modinfo);
				}

			}
		}
		catch (SQLException S) {
			S.printStackTrace();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

	private void doMain(IWContext modinfo) throws SQLException {
		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(2), 1, 1);
		MainTable.add(makeTopTable(), 1, 3);
		MainTable.add("<br><br><br>", 1, 4);
		MainTable.add(makeSubTable(), 1, 5);
		add(MainTable);
	}

	private void doChange(IWContext modinfo) throws SQLException, FinderException {
		String paym_id = modinfo.getRequest().getParameter("payment_id");
		PaymentType[] PT = (PaymentType[]) ((PaymentType) IDOLookup.instanciateEntity(PaymentType.class)).findAll();
		Form myForm = new Form();
		myForm.maintainAllParameters();
		if (paym_id != null) {
			int pm_id = Integer.parseInt(paym_id);
			Payment P = ((PaymentHome) IDOLookup.getHomeLegacy(Payment.class)).findByPrimaryKey(pm_id);
			String description = P.getExtraInfo();
			int price = P.getPrice();
			IWTimestamp Paydate = new IWTimestamp(P.getPaymentDate());
			IWTimestamp Update = new IWTimestamp(P.getLastUpdated());
			int pt_id = P.getPaymentTypeID();
			String part = P.getInstallmentNr() + "/" + P.getTotalInstallment();

			Table T = new Table(9, 2);

			T.setHorizontalZebraColored(DarkColor, LightColor);
			T.setRowColor(1, HeaderColor);
			String fontColor = "#FFFFFF";

			Text DESCR = new Text("LÝSING", true, false, false);
			DESCR.setFontColor(fontColor);
			Text PAYDATE = new Text("GJALDDAGI", true, false, false);
			PAYDATE.setFontColor(fontColor);
			Text PART = new Text("HLUTI", true, false, false);
			PART.setFontColor(fontColor);
			Text PRICE = new Text("UPPHÆÐ", true, false, false);
			PRICE.setFontColor(fontColor);
			Text PAYTYPE = new Text("GR.GERÐ", true, false, false);
			PAYTYPE.setFontColor(fontColor);
			Text UPDATED = new Text("UPPFÆRT", true, false, false);
			UPDATED.setFontColor(fontColor);
			Text PAID = new Text("GREITT", true, false, false);
			PAID.setFontColor(fontColor);
			Text UNPAID = new Text("GREITT", true, false, false);
			UNPAID.setFontColor(fontColor);
			Text DEL = new Text("EYÐA", true, false, false);
			DEL.setFontColor(fontColor);

			T.add(PAYDATE, 1, 1);
			T.add(DESCR, 2, 1);
			T.add(PRICE, 3, 1);
			T.add(PAYTYPE, 4, 1);
			T.add(PART, 5, 1);
			T.add(UPDATED, 6, 1);
			T.add(PAID, 7, 1);
			T.add(UNPAID, 8, 1);
			T.add(DEL, 9, 1);

			TextInput descInput = new TextInput("payment_idesc", description);
			descInput.setMaxlength(30);
			descInput.setSize(25);
			Text partText = new Text(part);
			IntegerInput priceInput = new IntegerInput("payment_iprice", price);
			priceInput.setSize(8);
			priceInput.setMaxlength(8);
			DropdownMenu drpPayType = new DropdownMenu(PT, "payment_ipaytype");
			drpPayType.setSelectedElement(String.valueOf(pt_id));
			Text payDateText = new Text(Paydate.toSQLDateString());
			Text lastUpdatedText = new Text(Update.toSQLDateString());
			CheckBox chkPaid = new CheckBox("payment_ichkpaid", "true");
			CheckBox chkUnPaid = new CheckBox("payment_ichkunpaid", "true");
			CheckBox chkDel = new CheckBox("payment_ichkdel", "true");

			T.add(payDateText, 1, 2);
			T.add(descInput, 2, 2);
			T.add(priceInput, 3, 2);
			T.add(drpPayType, 4, 2);
			T.add(partText, 5, 2);
			T.add(lastUpdatedText, 6, 2);
			T.add(chkPaid, 7, 2);
			T.add(chkUnPaid, 8, 2);
			T.add(chkDel, 9, 2);

			myForm.add(T);
			myForm.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")));
			myForm.add(new HiddenInput("payment_action", "update"));
			myForm.add(new HiddenInput("payment_id", paym_id));

			Table MainTable = makeMainTable();
			MainTable.add(makeLinkTable(2), 1, 1);
			MainTable.add(myForm, 1, 3);
			MainTable.add("<br><br><br>", 1, 4);
			add(MainTable);
		}
	}

	private void doUpdate(IWContext modinfo) throws SQLException, FinderException {
		String strPaymID, strPrice, strPaytype, strDescr, strChkPaid, strChkUnPaid, strChkDel;
		strPaymID = modinfo.getRequest().getParameter("payment_id");
		strPrice = modinfo.getRequest().getParameter("payment_iprice");
		strDescr = modinfo.getRequest().getParameter("payment_idesc");
		strPaytype = modinfo.getRequest().getParameter("payment_ipaytype");
		strChkPaid = modinfo.getRequest().getParameter("payment_ichkpaid");
		strChkUnPaid = modinfo.getRequest().getParameter("payment_ichkunpaid");
		strChkDel = modinfo.getRequest().getParameter("payment_ichkdel");

		int pm_id, price, pt_id;
		if (strChkDel != null && strChkDel.equalsIgnoreCase("true")) {
		}
		if (strPrice != null && strPaymID != null && strPaytype != null) {
			pm_id = Integer.parseInt(strPaymID);
			price = Integer.parseInt(strPrice);
			pt_id = Integer.parseInt(strPaytype);

			Payment P = ((PaymentHome) IDOLookup.getHomeLegacy(Payment.class)).findByPrimaryKey(pm_id);
			if (strChkDel != null && strChkDel.equalsIgnoreCase("true")) {
				try {
					P.delete();
				}
				catch (SQLException e) {
					strMessage = "Tókst ekki að eyða greiðslu";
				}
			}
			else {
				if (strChkPaid != null && strChkPaid.equalsIgnoreCase("true"))
					P.setStatus(true);
				if (strChkUnPaid != null && strChkUnPaid.equalsIgnoreCase("true"))
					P.setStatus(false);
				P.setPrice(price);
				P.setExtraInfo(strDescr);
				P.setPaymentTypeID(pt_id);
				try {
					P.update();
				}
				catch (SQLException e) {
					strMessage = "Tókst ekki að breyta greiðslu";
				}
			}
		}
		this.doMain(modinfo);

	}

	private void doView(IWContext modinfo) throws SQLException {

	}

	private void doSave(IWContext modinfo) throws SQLException {

	}

	private void doNew(IWContext modinfo) throws SQLException {
		PaymentType[] PT = (PaymentType[]) ((PaymentType) IDOLookup.instanciateEntity(PaymentType.class)).findAll();
		PaymentRound[] PR = (PaymentRound[]) ((PaymentRound) IDOLookup.instanciateEntity(PaymentRound.class)).findAllByColumnDescendingOrdered("union_id", union_id, "round_date");
		Form myForm = new Form();
		myForm.maintainAllParameters();

		Table T = new Table(6, 2);
		T.setWidth(sTablewidth);
		T.setCellspacing(1);
		T.setCellpadding(2);
		T.setHorizontalZebraColored(DarkColor, LightColor);
		T.setRowColor(1, HeaderColor);
		String fontColor = "#FFFFFF";

		Text DESCR = new Text("LÝSING", true, false, false);
		DESCR.setFontColor(fontColor);
		Text PAYDATE = new Text("GJALDDAGI", true, false, false);
		PAYDATE.setFontColor(fontColor);
		Text PART = new Text("HLUTI", true, false, false);
		PART.setFontColor(fontColor);
		Text PRICE = new Text("UPPHÆÐ", true, false, false);
		PRICE.setFontColor(fontColor);
		Text PAYTYPE = new Text("GR.GERÐ", true, false, false);
		PAYTYPE.setFontColor(fontColor);
		Text UPDATED = new Text("UPPFÆRT", true, false, false);
		UPDATED.setFontColor(fontColor);

		T.add(PAYDATE, 1, 1);
		T.add(DESCR, 2, 1);
		T.add(PRICE, 3, 1);
		T.add(PAYTYPE, 4, 1);
		T.add(PART, 5, 1);
		T.add(UPDATED, 6, 1);

		TextInput descInput = new TextInput("payment_idesc");
		Text partText = new Text("1/1");
		IntegerInput priceInput = new IntegerInput("payment_iprice");
		priceInput.setMaxlength(8);
		priceInput.setLength(8);
		DropdownMenu drpPayType = new DropdownMenu(PT, "payment_ipaytype");
		DropdownMenu drdInstallment = new DropdownMenu("payment_installments");
		for (int i = 1; i < 13; i++) {
			drdInstallment.addMenuElement(String.valueOf(i));
		}
		Text payDateText = new Text(new IWTimestamp().toSQLDateString());
		Text lastUpdatedText = new Text(new IWTimestamp().toSQLDateString());

		T.add(drpYear("payment_year"), 1, 2);
		T.add(drpMonth("payment_month"), 1, 2);
		T.add(drpDays("payment_day"), 1, 2);
		T.add(descInput, 2, 2);
		T.add(priceInput, 3, 2);
		T.add(drpPayType, 4, 2);
		T.add(drdInstallment, 5, 2);
		T.add(lastUpdatedText, 6, 2);

		CheckBox chkRoundRelation = new CheckBox("payment_roundrel", "true");
		DropdownMenu drpRounds = new DropdownMenu(PR, "payment_irounds");

		Table T2 = new Table(3, 1);
		T2.add("Tengja við álagningar hóp", 2, 1);
		T2.add(chkRoundRelation, 3, 1);
		T2.add(drpRounds, 3, 1);

		myForm.add(T);
		myForm.add(T2);
		myForm.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")));
		myForm.add(new HiddenInput("payment_action", "updatenew"));

		Table MainTable = makeMainTable();
		MainTable.add(makeLinkTable(3), 1, 1);
		MainTable.add(myForm, 1, 3);
		MainTable.add("<br><br><br>", 1, 4);
		add(MainTable);
	}

	private void doUpdateNew(IWContext modinfo) throws SQLException, FinderException {
		DecimalFormat Formatter = new DecimalFormat("00");

		String strPrice, strPaytype, strDescr, strIfRoundRel, strRoundId, strInst;
		strPrice = modinfo.getRequest().getParameter("payment_iprice");
		strDescr = modinfo.getRequest().getParameter("payment_idesc");
		strPaytype = modinfo.getRequest().getParameter("payment_ipaytype");
		strIfRoundRel = modinfo.getRequest().getParameter("payment_roundrel");
		strRoundId = modinfo.getRequest().getParameter("payment_irounds");
		strInst = modinfo.getRequest().getParameter("payment_installments");
		int iday = Integer.parseInt(modinfo.getRequest().getParameter("payment_day"));
		int imonth = Integer.parseInt(modinfo.getRequest().getParameter("payment_month"));
		int iyear = Integer.parseInt(modinfo.getRequest().getParameter("payment_year"));

		int inst = Integer.parseInt(strInst);
		int pm_id, price, pt_id;
		if (strPrice != null && strPaytype != null) {
			price = Integer.parseInt(strPrice);
			int payRoundId = -1;
			if (strIfRoundRel != null && strIfRoundRel.equalsIgnoreCase("true")) {
				if (strIfRoundRel != null && strIfRoundRel.equalsIgnoreCase("true")) {
					payRoundId = Integer.parseInt(strRoundId);
					PaymentRound pr = ((PaymentRoundHome) IDOLookup.getHomeLegacy(PaymentRound.class)).findByPrimaryKey(payRoundId);
					int prTotals = pr.getTotals();
					pr.setTotals(prTotals + price);
					pr.update();
				}
			}
			else {
				PaymentRound payround = (PaymentRound) IDOLookup.createLegacy(PaymentRound.class);
				payround.setName("Auka");
				payround.setRoundDate(IWTimestamp.getTimestampRightNow());
				payround.setTotals(price);
				payround.setUnionId(this.un_id);
				payround.insert();
				payRoundId = payround.getID();
			}
			if (payRoundId != -1) {
				pt_id = Integer.parseInt(strPaytype);
				for (int i = 0; i < inst; i++) {
					Payment P = (Payment) IDOLookup.createLegacy(Payment.class);
					P.setMemberId(this.mem_id);
					P.setPriceCatalogueId(0);
					P.setPaymentDate(new IWTimestamp(iday, imonth + i, iyear).getTimestamp());
					P.setLastUpdated(IWTimestamp.getTimestampRightNow());
					P.setCashierId(cashier_id);
					P.setStatus(false);
					P.setExtraInfo(strDescr);
					P.setPaymentTypeID(pt_id);
					if (i == 0)
						P.setPrice(price / inst + price % inst);
					else
						P.setPrice(price / inst);
					P.setInstallmentNr(i + 1);
					P.setTotalInstallment(inst);
					P.setRoundId(payRoundId);
					try {
						P.insert();
					}
					catch (SQLException e) {
						e.printStackTrace();
						strMessage = "Tókst ekki að breyta greiðslu";
					}
				}
			}
		}
		this.doMain(modinfo);
	}

	private void doList(IWContext modinfo) throws SQLException {

	}

	private Table makeMainTable() {
		Table MainTable = new Table(1, 6);
		MainTable.setWidth(sTablewidth);
		MainTable.setCellspacing(0);
		MainTable.setCellpadding(0);
		MainTable.add(this.makeHeaderTable(), 1, 2);
		MainTable.add(strMessage, 1, 6);
		return MainTable;
	}

	private Table makeHeaderTable() {
		Table HeaderTable = new Table(1, 1);
		HeaderTable.setColor(HeaderColor);
		HeaderTable.setWidth(sTablewidth);
		HeaderTable.setCellspacing(0);
		HeaderTable.setCellpadding(2);
		if (this.thisMember != null) {
			String sName = thisMember.getName();
			String sKt = "  Kt:";
			String kt = thisMember.getSocialSecurityNumber();
			if (kt != null)
				sKt += kt;
			Text sHeader = new Text(sName + "  " + sKt);
			sHeader.setFontColor("#FFFFFF");
			sHeader.setBold();
			HeaderTable.add(sHeader);

		}
		return HeaderTable;
	}

	private Table makeLinkTable(int menuNr) {
		Table LinkTable = new Table(1, 1);
		LinkTable.setBorder(0);
		LinkTable.setCellpadding(0);
		LinkTable.setCellspacing(0);

		LinkTable.setWidth(sTablewidth);

		Link MainLink = new Link(new Image(menuNr == 1 ? "/pics/tarif/gjaldskra.gif" : "/pics/tarif/gjaldskra1.gif"), "/tarif/tarif.jsp");
		MainLink.addParameter("catal_action", "view");
		MainLink.addParameter("union_id", union_id);

		Link UpdateLink = new Link(new Image(menuNr == 2 ? "/pics/tarif/greidslur.gif" : "/pics/tarif/greidslur1.gif"));
		UpdateLink.addParameter("payment_action", "main");

		Link ViewLink = new Link(new Image(menuNr == 3 ? "/pics/tarif/nytt.gif" : "/pics/tarif/nytt1.gif"));
		ViewLink.addParameter("payment_action", "new");

		LinkTable.add(sidan, 1, 1);
		if (isAdmin) {
			LinkTable.add(MainLink, 1, 1);
			LinkTable.add(UpdateLink, 1, 1);
			LinkTable.add(ViewLink, 1, 1);
		}
		return LinkTable;
	}

	private Table makeTopTable() {
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		memberPayments = this.getPayments(mem_id);
		Table T = new Table(1, 8);
		if (memberPayments != null) {
			int len = memberPayments.length;
			int d = len + 2;
			T = new Table(9, d);
			T.setWidth(sTablewidth);
			T.setCellspacing(1);
			T.setCellpadding(2);
			T.setHorizontalZebraColored(DarkColor, LightColor);
			T.setRowColor(1, HeaderColor);
			String fontColor = "#FFFFFF";
			Text NR = new Text("NR", true, false, false);
			NR.setFontColor(fontColor);
			Text DESCR = new Text("LÝSING", true, false, false);
			DESCR.setFontColor(fontColor);
			Text PAYDATE = new Text("GJALDDAGI", true, false, false);
			PAYDATE.setFontColor(fontColor);
			Text PART = new Text("HLUTI", true, false, false);
			PART.setFontColor(fontColor);
			Text DEBET = new Text("GREITT", true, false, false);
			DEBET.setFontColor(fontColor);
			Text KREDIT = new Text("ÓGREITT", true, false, false);
			KREDIT.setFontColor(fontColor);
			Text UPDATED = new Text("UPPFÆRT", true, false, false);
			UPDATED.setFontColor(fontColor);
			Text PAYTYPE = new Text("GR.GERÐ", true, false, false);
			PAYTYPE.setFontColor(fontColor);
			Text CHANGE = new Text("BREYTA", true, false, false);
			CHANGE.setFontColor(fontColor);

			T.add(NR, 1, 1);
			T.add(DESCR, 2, 1);
			T.add(PAYDATE, 3, 1);
			T.add(PART, 4, 1);
			T.add(DEBET, 5, 1);
			T.add(KREDIT, 6, 1);
			T.add(PAYTYPE, 7, 1);
			T.add(UPDATED, 8, 1);
			T.add(CHANGE, 9, 1);

			PaymentType pt;
			String pmtname = "";
			int price, debetprice = 0, kreditprice = 0;
			for (int i = 0; i < len; i++) {
				int r = i + 2;
				try {
					pt = ((PaymentTypeHome) IDOLookup.getHomeLegacy(PaymentType.class)).findByPrimaryKey(memberPayments[i].getPaymentTypeID());
					pmtname = pt.getName();
				}
				catch (FinderException e) {
				}
				T.add(String.valueOf(i + 1), 1, r);
				T.add(memberPayments[i].getExtraInfo(), 2, r);
				T.add(new IWTimestamp(memberPayments[i].getPaymentDate()).toSQLDateString(), 3, r);
				T.add(memberPayments[i].getInstallmentNr() + "/" + memberPayments[i].getTotalInstallment(), 4, r);
				price = memberPayments[i].getPrice();
				boolean b = memberPayments[i].getStatus();
				if (!b) {
					Text t = new Text(nf.format(price));
					T.add(t, 6, r);
					T.add(" Kr", 6, r);
					kreditprice += price;
				}
				else if (b) {
					Text t = new Text(nf.format(price));
					T.add(t, 5, r);
					T.add(" Kr", 5, r);
					debetprice += price;
				}
				T.add(pmtname, 7, r);
				T.add(new IWTimestamp(memberPayments[i].getLastUpdated()).toSQLDateString(), 8, r);
				T.add(makeChangeLink(memberPayments[i].getID()), 9, r);
			}
			String k = nf.format(kreditprice);
			Text krpriceText = new Text(k);
			krpriceText.setFontColor("#CC0000");
			krpriceText.setBold();
			krpriceText.setFontStyle("Regular");
			T.add(nf.format(debetprice) + " Kr", 5, d);
			T.add(krpriceText, 6, d);
			T.add(" Kr", 6, d);
		}
		return T;
	}

	private Table makeSubTable() {

		return new Table();
	}

	private Link makeChangeLink(int payment_id) {
		Link L = new Link("Breyta");
		L.addParameter("payment_action", "change");
		L.addParameter("payment_id", payment_id);
		L.addParameter("catal_member_is", member_id);
		return L;
	}

	private int findAccountID(int member_id, int union_id) {
		int id = -1;
		try {
			Account[] A = (Account[]) ((Account) IDOLookup.instanciateEntity(Account.class)).findAllByColumnEquals("member_id", String.valueOf(member_id), "union_id", String.valueOf(union_id));
			if (A.length > 0)
				id = A[0].getID();
		}
		catch (SQLException e) {
			strMessage = "Félagi á engan reikning";
		}
		return id;
	}

	private DropdownMenu drpDays(String name) {
		DropdownMenu drp = new DropdownMenu(name);
		for (int i = 1; i < 32; i++) {
			drp.addMenuElement(i, String.valueOf(i));
		}
		return drp;
	}

	private DropdownMenu drpMonth(String name) {
		IWTimestamp Today = new IWTimestamp();
		int iMonth = Today.getMonth();
		DropdownMenu drp = new DropdownMenu(name);
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
		for (int i = a - 10; i < a + 10; i++) {
			drp.addMenuElement(i, String.valueOf(i));
		}
		drp.setSelectedElement(String.valueOf(a));
		return drp;
	}

	private void createAccount(int member_id, int union_id, int cashier_id) throws SQLException {
		Account account = (Account) IDOLookup.createLegacy(Account.class);
		account.setMemberId(member_id);
		account.setUnionId(union_id);
		account.setCashierId(cashier_id);
		Payment[] P = this.getPayments(member_id);
		account.setBalance(this.calculateBalance(P));
		account.setLastUpdated(new IWTimestamp().getTimestampRightNow());
		account.setCreationDate(new IWTimestamp().getTimestampRightNow());
		account.setCashierId(cashier_id);
	}

	private void makePayment(int memberID, int PriceCatalogueId, int RoundId, int Price, boolean Status, String Info, int InstallmentNumber, int Totalinstallments, int PaymentTypeID, Timestamp PayDate, int cashier_is) throws SQLException {
		IWTimestamp today = new IWTimestamp();
		Payment P = (Payment) IDOLookup.createLegacy(Payment.class);
		P.setMemberId(memberID);
		P.setPriceCatalogueId(PriceCatalogueId);
		P.setRoundId(RoundId);
		P.setPrice(Price);
		P.setStatus(Status); // False meaning; has not been paid
		P.setExtraInfo(Info);
		P.setInstallmentNr(InstallmentNumber);
		P.setTotalInstallment(Totalinstallments);
		P.setPaymentTypeID(PaymentTypeID);
		P.setPaymentDate(PayDate);
		P.setLastUpdated(today.getTimestamp());
		P.setCashierId(cashier_id);
		P.insert();
	}

	private int calculateBalance(Payment[] payments) {
		int balance = 0;
		if (payments.length > 0) {
			for (int i = 0; i < payments.length; i++) {
				if (!payments[i].getStatus())
					balance += payments[i].getPrice();
				else
					balance -= payments[i].getPrice();
			}
		}
		return balance;
	}

	private Payment[] getPayments(int member_id) {
		IWTimestamp today = new IWTimestamp();
		Payment[] P;
		try {
			//P = (Payment[]) (new Payment()).findAll("select * from payment where
			// member_id = '"+member_id+"' and (payment_date <=
			// '"+today.getYear()+"-01-01' or status ='Y' ") ;
			P = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAll("select * from payment where member_id = '" + member_id + "' and round_id in ( select payment_round_id from payment_round where union_id like '" + union_id + "' ) ");
		}
		catch (SQLException e) {
			strMessage = "Engar greiðslur í gagnagrunni";
			P = new Payment[0];
		}
		return P;
	}

	public static Table getMemberPayments(String member_id, String union_id) throws SQLException {
		Payment[] payments = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAllByColumnOrdered("member_id", member_id, "payment_date");
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		Table T = new Table(6, payments.length + 2);
		T.setWidth("539");
		T.setCellspacing(0);
		T.setCellpadding(2);
		T.setHorizontalZebraColored("#ADCAB1", "#CEDFD0");
		T.setRowColor(1, "#336660");
		T.setColumnAlignment(3, "left");
		T.setColumnAlignment(4, "center");
		T.setColumnAlignment(5, "right");
		T.setColumnAlignment(6, "center");
		String fontColor = "#FFFFFF";
		Text NR = new Text("NR", true, false, false);
		NR.setFontColor(fontColor);
		Text DATE = new Text("DAGS", true, false, false);
		DATE.setFontColor(fontColor);
		Text DESCR = new Text("LÝSING", true, false, false);
		DESCR.setFontColor(fontColor);
		Text PART = new Text("HLUTI", true, false, false);
		PART.setFontColor(fontColor);
		Text PRICE = new Text("UPPHÆÐ", true, false, false);
		PRICE.setFontColor(fontColor);
		Text STATE = new Text("STAÐA", true, false, false);
		STATE.setFontColor(fontColor);

		T.add(NR, 1, 1);
		T.add(DATE, 2, 1);
		T.add(DESCR, 3, 1);
		T.add(PART, 4, 1);
		T.add(PRICE, 5, 1);
		T.add(STATE, 6, 1);

		T.add("ALLS : ", 4, payments.length + 2);
		int totalprice = 0;
		for (int i = 0; i < payments.length; i++) {
			T.add(" " + (i + 1), 1, i + 2);
			T.add(new IWTimestamp(payments[i].getPaymentDate()).getDateString("dd.mm.yyyy"), 2, i + 2);
			T.add(payments[i].getExtraInfo(), 3, i + 2);
			T.add(payments[i].getInstallmentNr() + "/" + payments[i].getTotalInstallment(), 4, i + 2);
			T.add("" + nf.format(payments[i].getPrice()) + " Kr", 5, i + 2);
			totalprice += payments[i].getPrice();
			if (!payments[i].getStatus()) {
				T.add(new Image("/pics/clubs/members/ogreitt.gif"), 6, i + 2);
			}
			else
				T.add(new Image("/pics/clubs/members/greitt.gif"), 6, i + 2);
		}
		T.add("" + nf.format(totalprice) + " Kr", 5, payments.length + 2);
		return T;
	}

	public void main(IWContext modinfo) throws IOException {
		//isAdmin =
		/** @todo: fixa Admin*/
		isAdmin = true;
		control(modinfo);
	}
}// class PaymentViewer

