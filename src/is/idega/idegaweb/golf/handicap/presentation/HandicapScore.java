package is.idega.idegaweb.golf.handicap.presentation;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author @version 1.0
 */

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeColorHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;

public class HandicapScore extends GolfBlock {

	private String member_id;
	private boolean isAdmin = false;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	private Table outerTable;
	private Form myForm;
	
	private Member iMember;

	public HandicapScore() {
	}

	public HandicapScore(String member_id) {
		this.member_id = member_id;
	}

	public HandicapScore(int member_id) {
		this.member_id = String.valueOf(member_id);
	}

	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle();
		iwb = getBundle();
		this.isAdmin = isAdministrator(modinfo);

		if (member_id == null) {
			member_id = modinfo.getRequest().getParameter("member_id");
		}
		if (member_id == null) {
			member_id = (String) modinfo.getSession().getAttribute("member_id");
			if (member_id == null) {
				Member memberinn = (Member) modinfo.getSession().getAttribute("member_login");
				if (memberinn != null) {
					member_id = String.valueOf(memberinn.getID());
					if (member_id == null) {
						member_id = "1";
					}
				}
				else {
					member_id = "1";
				}
			}
		}

		iMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));

		if ((int) iMember.getHandicap() == 100) {

			Table noTable = new Table();
			noTable.setAlignment("center");
			noTable.setCellpadding(12);
			noTable.setCellspacing(12);

			Text texti = getHeader(iwrb.getLocalizedString("handicap.member_no_handicap", "Member does not have a registered handicap."));
			texti.addBreak();
			texti.addBreak();
			texti.addToText(iwrb.getLocalizedString("handicap.handicap_help", "Contact your club to get your handicap."));

			noTable.add(texti);
			add(noTable);
		}

		else {
			getForm();
			drawTable(modinfo);

			myForm.add(outerTable);
			add(myForm);
		}
	}

	private void drawTable(IWContext modinfo) throws IOException, SQLException, FinderException {

		Member memberInfo = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));
		float forgjof = memberInfo.getHandicap();
		int union_id = memberInfo.getMainUnionID();
		String gender = memberInfo.getGender();

		String field_id = (String) modinfo.getSession().getAttribute("field_id");
		if (field_id == null) {
			Field[] field = (Field[]) ((Field) IDOLookup.instanciateEntity(Field.class)).findAllByColumn("union_id", String.valueOf(union_id));
			if (union_id > 3 && field.length > 0) {
				field_id = String.valueOf(field[0].getID());
			}
			else
				field_id = "49";
		}

		HiddenInput fieldID = new HiddenInput("field_id", field_id);
		myForm.add(fieldID);

		Field fieldName = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));

		IWCalendar dagatal = new IWCalendar();

		String month = String.valueOf(dagatal.getMonth());
		String year = String.valueOf(dagatal.getYear());
		String day = String.valueOf(dagatal.getDay());

		String tee_number = "1";
		if (String.valueOf(memberInfo.getHandicap()) != null && memberInfo.getGender() != null) {
			if (forgjof >= 26.5 && gender.equals("M")) {
				tee_number = "6";
			}
			else if (forgjof >= 10.5 && gender.equals("F")) {
				tee_number = "4";
			}
			else if (forgjof <= 10.4 && gender.equals("F")) {
				tee_number = "3";
			}
			else if (forgjof <= 28.0 && forgjof >= 4.5 && gender.equals("M")) {
				tee_number = "2";
			}
			else if (forgjof <= 4.4 && gender.equals("M")) {
				tee_number = "1";
			}
		}

		DropdownMenu select_tee = (DropdownMenu) getStyledInterface(new DropdownMenu("tee_number"));

		DropdownMenu select_holes = (DropdownMenu) getStyledInterface(new DropdownMenu("number_of_holes"));
		select_holes.addMenuElement("18", iwrb.getLocalizedString("handicap.18_holes", "18 holes"));
		if (forgjof > 26.4 || isAdmin) {
			select_holes.addMenuElement("1", iwrb.getLocalizedString("handicap.first_9_holes", "First 9 holes"));
			select_holes.addMenuElement("10", iwrb.getLocalizedString("handicap.last_9_holes", "Last 9 holes"));
		}
		select_holes.keepStatusOnAction();

		DropdownMenu select_stats = (DropdownMenu) getStyledInterface(new DropdownMenu("statistics"));
		select_stats.addMenuElement("0", iwrb.getLocalizedString("handicap.no_statistics", "No statistics"));
		select_stats.addMenuElement("1", iwrb.getLocalizedString("handicap.register_statistics", "Register statistics"));
		select_stats.keepStatusOnAction();

		DropdownMenu select_month = (DropdownMenu) getStyledInterface(new DropdownMenu("month"));
		for (int m = 1; m <= 12; m++) {
			select_month.addMenuElement(String.valueOf(m), dagatal.getMonthName(m).toLowerCase());
		}
		select_month.setSelectedElement(month);
		select_month.keepStatusOnAction();

		DropdownMenu select_year = (DropdownMenu) getStyledInterface(new DropdownMenu("year"));
		for (int y = 2001; y <= dagatal.getYear(); y++) {
			select_year.addMenuElement(String.valueOf(y), String.valueOf(y));
		}

		select_year.setSelectedElement(year);
		select_year.keepStatusOnAction();

		DropdownMenu select_day = (DropdownMenu) getStyledInterface(new DropdownMenu("day"));
		for (int d = 1; d <= 31; d++) {
			select_day.addMenuElement(String.valueOf(d), String.valueOf(d) + ".");
		}

		select_day.setSelectedElement(day);
		select_day.keepStatusOnAction();

		Tee[] teeID = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct tee_color_id from tee where field_id='" + field_id + "'");

		for (int a = 0; a < teeID.length; a++) {
			int teeColorID = teeID[a].getIntColumnValue("tee_color_id");
			select_tee.addMenuElement(String.valueOf(teeColorID), (((TeeColorHome) IDOLookup.getHomeLegacy(TeeColor.class)).findByPrimaryKey(teeColorID)).getStringColumnValue("tee_color_name"));
		}
		select_tee.keepStatusOnAction();
		select_tee.setSelectedElement(tee_number);

		GenericButton selectMember = getButton(new GenericButton("select_member", iwrb.getLocalizedString("handicap.select", "Select member")));
		selectMember.setWindowToOpen(HandicapFindMember.class);

		GenericButton selectField = getButton(new GenericButton("select_field", iwrb.getLocalizedString("handicap.select_course", "Select course")));
		selectField.setWindowToOpen(HandicapUtility.class);
		selectField.addParameterToWindow(HandicapUtility.PARAMETER_METHOD, HandicapUtility.ACTION_FIND_FIELD);

		SubmitButton writeScore = (SubmitButton) getButton(new SubmitButton(iwrb.getLocalizedString("handicap.register_score", "Register score")));

		Text member = getHeader(iwrb.getLocalizedString("handicap.member", "Member") + ":");
		Text memberText = getText(memberInfo.getName());
		Text field = getHeader(iwrb.getLocalizedString("handicap.course", "Course") + ":");
		Text fieldText = getText(fieldName.getName());
		Text tees = getHeader(iwrb.getLocalizedString("handicap.tees", "Tees") + ":");
		Text date = getHeader(iwrb.getLocalizedString("handicap.day", "Day") + ":");
		Text numberOfHoles = getHeader(iwrb.getLocalizedString("handicap.number_of_holes", "Number of holes") + ":");
		Text statistics = getHeader(iwrb.getLocalizedString("handicap.statistics", "Statistics") + ":");

		outerTable = new Table(2, 1);
		outerTable.setCellspacing(6);
		outerTable.setCellpadding(6);
		outerTable.setAlignment("center");
		outerTable.setAlignment(1, 1, "center");

		Table myTable = new Table(2, 7);
		myTable.setBorder(0);
		myTable.setCellpadding(8);
		myTable.setCellspacing(0);
		myTable.setAlignment("center");
		myTable.setColumnAlignment(1, "right");

		myTable.add(member, 1, 1);
		myTable.add(field, 1, 2);
		myTable.add(tees, 1, 3);
		myTable.add(date, 1, 4);
		myTable.add(numberOfHoles, 1, 5);
		myTable.add(statistics, 1, 6);

		myTable.add(memberText, 2, 1);
		if (isAdmin) {
			myTable.add(getSmallText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE), 2, 1);
			myTable.add(selectMember, 2, 1);
		}
		myTable.add(fieldText, 2, 2);
		myTable.add(getSmallText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE), 2, 2);
		myTable.add(selectField, 2, 2);
		myTable.add(select_day, 2, 4);
		myTable.add(select_month, 2, 4);
		myTable.add(select_year, 2, 4);
		myTable.add(select_holes, 2, 5);
		myTable.add(select_stats, 2, 6);

		myTable.mergeCells(1, 7, 2, 7);
		myTable.setAlignment(1, 7, "right");
		myTable.setVerticalAlignment(1, 7, "top");

		GenericButton foreignRound = getButton(new GenericButton("foreign_round", iwrb.getLocalizedString("handicap.foreign_round", "Foreign round")));
		foreignRound.setWindowToOpen(HandicapRegisterForeign.class);
		foreignRound.addParameterToWindow("member_id", member_id);
		myTable.add(foreignRound, 1, 7);

		if (teeID.length > 0) {
			myTable.add(select_tee, 2, 3);
			myTable.add(Text.getNonBrakingSpace(), 1, 7);
			myTable.add(Text.getNonBrakingSpace(), 1, 7);
			myTable.add(writeScore, 1, 7);
		}
		else {
			myTable.add(getErrorText(iwrb.getLocalizedString("handicap.no_tees", "No tees registered") + ":"), 2, 3);
		}

		//Image swingImage = iwb.getImage("shared/swing.gif", "", 161, 300);

		outerTable.add(myTable, 2, 1);
		outerTable.add(new HandicapMemberInfo(), 1, 1);

	}

	private void getForm() {
		//GolfWindow skraWindow = new GolfWindow("", 600, 600);
		//skraWindow.add(new HandicapRegister());
		//myForm = new Form(skraWindow);
		myForm = new Form();
		myForm.setWindowToOpen(HandicapRegisterWindow.class);
		myForm.add(new HiddenInput("member_id", member_id));
	}
}