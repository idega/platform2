/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.business.AccessControl;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.Editor;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class MemberCorrect extends Editor {

	Member eMember = null;
	String OkImageUrl = "/pics/formtakks/saekja.gif";
	String SaveImageUrl = "/pics/formtakks/vista.gif";
	String CloseImageUrl = "/pics/formtakks/loka.gif";
	List eMembers = null;
	List eUMIs = null;
	String sSocSecNum = null;

	protected PresentationObject makeLinkTable(int l) {
		return new Text("");
	}

	protected void control(IWContext modinfo) {
		String member_id = modinfo.getParameter("member_id");
		String SSN = modinfo.getParameter("ssn");
		String hSSN = modinfo.getParameter("hssn");
		this.makeView();
		if (isAdmin || AccessControl.isClubAdmin(modinfo)) {
			try {
				if (modinfo.getParameter("save") != null || modinfo.getParameter("save.x") != null) {
					if (modinfo.getParameter("rb") != null && hSSN != null) {
						/** @todo */
						int iMemberId = Integer.parseInt(modinfo.getParameter("rb"));
						eMembers = EntityFinder.findAllByColumn((Member) IDOLookup.instanciateEntity(Member.class), "social_security_number", hSSN);
						if (eMembers != null) {
							deleteMembers(eMembers, iMemberId);
							eMembers = EntityFinder.findAllByColumn((Member) IDOLookup.instanciateEntity(Member.class), "social_security_number", hSSN);
						}
					}
				}
				else if (modinfo.getParameter("ok") != null || modinfo.getParameter("ok.x") != null || modinfo.getParameter("ssn") != null) {
					String ssn = modinfo.getParameter("ssn").trim();
					if (ssn != null && ssn.length() > 5) {
						sSocSecNum = ssn;
						add(sSocSecNum);
						eMembers = EntityFinder.findAllByColumn((Member) IDOLookup.instanciateEntity(Member.class), "social_security_number", ssn);
					}
				}
				else if (member_id != null) {
					eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));
				}
				else if (SSN != null) {
					eMembers = EntityFinder.findAllByColumn((Member) IDOLookup.instanciateEntity(Member.class), "social_security_number", SSN);
				}
				else
					add("enginn valinn");

				this.addLinks(formatText("Félagi"));
				this.addMain(doView());
				this.setBorder(2);
			}
			catch (SQLException sql) {
				add("sql vandræði");
				sql.printStackTrace();
			}
			catch (FinderException sql) {
				add("sql vandræði");
				sql.printStackTrace();
			}
		}
		else
			this.addMain(formatText("Ekki Réttindi"));
	}

	private PresentationObject doView() {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setWidth("100%");

		TextInput SSN = new TextInput("ssn");
		SSN.setMaxlength(10);
		SSN.setLength(10);
		T.add(SSN, 1, 1);
		if (sSocSecNum != null)
			T.add(new HiddenInput("hssn", sSocSecNum));
		setTextFontColor("#FF0000");
		T.add(formatText(" Veljið réttan meðlim hinum verður eytt :"), 1, 3);
		setTextFontColor(DarkColor);
		T.add(getMemberTable(), 1, 5);
		SubmitButton ok = new SubmitButton(new Image(OkImageUrl), "ok");
		SubmitButton save = new SubmitButton(new Image(SaveImageUrl), "save");
		CloseButton close = new CloseButton(new Image(CloseImageUrl));
		T.add(ok, 1, 1);
		T.add(save, 1, 7);
		T.add(close, 1, 7);

		Form myForm = new Form();
		myForm.add(T);

		return myForm;
	}

	public Table getMemberTable() {
		Table bTable = new Table();
		bTable.setWidth("100%");

		if (eMembers != null) {
			int len = eMembers.size();
			Table T = new Table(5, len + 1);
			T.setCellpadding(0);
			T.setCellspacing(3);
			T.setColumnAlignment(1, "left");
			T.setColumnAlignment(2, "left");
			//T.setWidth("100%");
			T.add(formatText("Nafn"), 2, 1);
			T.add(formatText("Kennitala"), 3, 1);
			T.add(formatText("Klúbbar"), 4, 1);
			T.add(formatText("Forgjöf"), 5, 1);

			Member eMb;
			int iUnId;
			String color = DarkColor;
			List umis = null;
			for (int i = 0; i < len; i++) {
				eMb = (Member) eMembers.get(i);
				MemberInfo MI = eMb.getMemberInfo();
				try {
					eUMIs = EntityFinder.findAllByColumn((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class), "member_id", eMb.getID());
				}
				catch (SQLException sql) {
				}
				RadioButton RB = new RadioButton("rb", String.valueOf(eMb.getID()));
				T.add(RB, 1, i + 2);
				T.add(formatText(eMb.getName()), 2, i + 2);
				T.add(formatText(eMb.getSocialSecurityNumber()), 3, i + 2);
				if (eUMIs != null)
					T.add(getUMItable(eUMIs), 4, i + 2);
				if (MI != null)
					T.add(formatText(String.valueOf(MI.getHandicap())), 5, i + 2);
				eUMIs = null;
			}
			bTable.add(T);
		}
		return bTable;
	}

	private PresentationObject getUMItable(List eUmis) {
		int len = eUmis.size();
		Table T = new Table(len, 1);
		T.setBorder(1);
		T.setCellpadding(0);
		T.setCellspacing(0);
		UnionMemberInfo umi;
		Union U;
		for (int i = 0; i < len; i++) {
			umi = (UnionMemberInfo) eUmis.get(i);
			U = GolfCacher.getCachedUnion(umi.getUnionID());
			T.add(formatText(U.getAbbrevation() + "  "), i + 1, 1);
			T.add(formatText(mbsShipMap(umi.getMembershipType())), i + 1, 1);
			T.setWidth(i + 1, 1, "70");
		}
		return T;
	}

	private void deleteMembers(List eMembers, int notdelete) {
		for (int i = 0; i < eMembers.size(); i++) {
			Member m = (Member) eMembers.get(i);
			if (m.getID() != notdelete) {
				try {
					//m.debug = true;
					m.delete();
				}
				catch (SQLException sql) {
					System.err.println("MemberCorrect:");
					sql.printStackTrace();
				}
			}
		}

	}

	private String mbsShipMap(String type) {
		if ("main".equalsIgnoreCase(type))
			return "Aðalkl.";
		else if ("sub".equalsIgnoreCase(type))
			return "Aukakl.";
		else
			return "";
	}

	public Text headerText(String s) {
		Text T = new Text();
		if (s != null) {
			T = new Text(s);
			T.setFontColor(this.DarkColor);
			T.setFontSize(this.fontSize);
			T.setBold();
		}
		return T;
	}

	public Text headerText(int i) {
		return headerText(String.valueOf(i));
	}
}
