/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class HandicapUpdate extends GolfWindow {

	public HandicapUpdate() {
		setWidth(400);
		setHeight(320);
		setTitle("Handicap update");
		add(new Update());
	}

	public class Update extends GolfBlock {

		public void main(IWContext modinfo) throws Exception {
			IWResourceBundle iwrb = getResourceBundle();

			getParentPage().setTitle(iwrb.getLocalizedString("handicap.update_handicap", "Update handicap"));
			addHeading(iwrb.getLocalizedString("handicap.update_handicap", "Update handicap"));

			String mode = modinfo.getParameter("mode");
			if (mode == null) {
				mode = "";
			}

			String action = modinfo.getParameter("action");
			if (action == null) {
				action = "";
			}

			String action2 = modinfo.getParameter("action2");
			if (action2 == null) {
				action2 = "select_date";
			}

			float handicap = 100;
			String handicapString = modinfo.getParameter("handicap");

			if (handicapString != null && handicapString.length() > 0) {
				if (handicapString.indexOf(",") != -1) {
					handicapString = handicapString.replace(',', '.');
				}
				handicap = Float.parseFloat(handicapString);
			}
			String member_id = modinfo.getParameter("member_id");

			if (mode.equals("")) {

				Table myTable = new Table(2, 5);
				myTable.mergeCells(1, 1, 2, 1);
				myTable.mergeCells(1, 2, 2, 2);
				myTable.mergeCells(1, 3, 2, 3);
				myTable.mergeCells(1, 4, 2, 4);
				myTable.setCellpadding(5);
				myTable.setAlignment("center");
				myTable.setAlignment(1, 2, "center");

				Form myForm = new Form();
				myForm.add(new HiddenInput("member_id", member_id));
				myForm.add(new HiddenInput("mode", "submit"));

				RadioButton update = getRadioButton("action", "update");
				update.setSelected();
				RadioButton correction = getRadioButton("action", "correct");

				myTable.add(getHeader(iwrb.getLocalizedString("handicap.enter_handicap", "Enter handicap") + ":"), 1, 1);
				myTable.add(getStyledInterface(new TextInput("handicap")), 1, 2);
				myTable.add(update, 1, 3);
				myTable.add(getHeader(iwrb.getLocalizedString("handicap.change_first_handicap", "Change first handicap")), 1, 3);
				myTable.add(correction, 1, 4);
				myTable.add(getHeader(iwrb.getLocalizedString("handicap.correct_handicap", "Correct handicap")), 1, 4);
				myTable.add(getButton(new CloseButton(iwrb.getLocalizedString("handicap.back", "Back"))), 1, 5);
				myTable.add(getButton(new SubmitButton(iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 2, 5);

				myForm.add(myTable);
				add(Text.getBreak());
				add(myForm);
			}

			if (mode.equals("submit")) {

				MemberInfo[] memberInfo = (MemberInfo[]) ((MemberInfo) IDOLookup.instanciateEntity(MemberInfo.class)).findAllByColumn("member_id", member_id);

				if (action.equalsIgnoreCase("update")) {

					if (memberInfo.length > 0) {
						Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAllByColumn("member_id", member_id);

						if ((scorecard == null) || (scorecard.length == 0)) {
							memberInfo[0].setHandicap(handicap);
						}
						memberInfo[0].setFirstHandicap(handicap);
						memberInfo[0].update();
					}
					else {
						MemberInfo memberInfo2 = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(Integer.parseInt(member_id));
						memberInfo2.setFirstHandicap(handicap);
						memberInfo2.setHandicap(handicap);
						memberInfo2.insert();
					}

					UpdateHandicap.update(Integer.parseInt(member_id));
					getParentPage().setParentToReload();
					getParentPage().close();
				}

				else if (action.equalsIgnoreCase("correct")) {

					if (action2.equalsIgnoreCase("select_date")) {

						Table myTable = new Table();
						myTable.mergeCells(1, 1, 2, 1);
						myTable.mergeCells(1, 2, 2, 2);
						myTable.setCellpadding(5);
						myTable.setAlignment("center");
						myTable.setAlignment(1, 2, "center");
						myTable.setAlignment(2, 3, "right");

						Form myForm = new Form();
						myForm.add(new HiddenInput("member_id", member_id));
						myForm.add(new HiddenInput("mode", "submit"));
						myForm.add(new HiddenInput("action", "correct"));
						myForm.add(new HiddenInput("action2", "save"));
						myForm.add(new HiddenInput("handicap", handicapString));

						myTable.add(getHeader(iwrb.getLocalizedString("handicap.select_date_of_correction", "Select date of correction") + ":"), 1, 1);
						myTable.add(getButton(new CloseButton(iwrb.getLocalizedString("handicap.back", "Back"))), 1, 3);
						myTable.add(getButton(new SubmitButton(iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 2, 3);

						IWCalendar dagatal = new IWCalendar();
						String month = String.valueOf(dagatal.getMonth());
						String year = String.valueOf(dagatal.getYear());
						String day = String.valueOf(dagatal.getDay());

						DropdownMenu select_month = (DropdownMenu) getStyledInterface(new DropdownMenu("month"));
						for (int m = 1; m <= 12; m++) {
							select_month.addMenuElement(String.valueOf(m), dagatal.getMonthName(m).toLowerCase());
						}
						select_month.setSelectedElement(month);

						DropdownMenu select_year = (DropdownMenu) getStyledInterface(new DropdownMenu("year"));
						for (int y = 2000; y <= dagatal.getYear(); y++) {
							select_year.addMenuElement(String.valueOf(y), String.valueOf(y));
						}
						select_year.setSelectedElement(year);

						DropdownMenu select_day = (DropdownMenu) getStyledInterface(new DropdownMenu("day"));
						for (int d = 1; d <= 31; d++) {
							select_day.addMenuElement(String.valueOf(d), String.valueOf(d) + ".");
						}
						select_day.setSelectedElement(day);

						myTable.add(select_day, 1, 2);
						myTable.add(select_month, 1, 2);
						myTable.add(select_year, 1, 2);

						myForm.add(myTable);

						add(new Text().getBreak());
						add(myForm);

					}

					else if (action2.equalsIgnoreCase("save")) {

						String year = modinfo.getParameter("year");
						String month = modinfo.getParameter("month");
						String day = modinfo.getParameter("day");

						IWTimestamp stampur = new IWTimestamp();
						stampur.setDay(Integer.parseInt(day));
						stampur.setMonth(Integer.parseInt(month) - 1);
						stampur.setYear(Integer.parseInt(year));

						Scorecard scoreCard = (Scorecard) IDOLookup.createLegacy(Scorecard.class);

						scoreCard.setMemberId(Integer.parseInt(member_id));
						scoreCard.setTournamentRoundId(1);
						scoreCard.setScorecardDate(stampur.getTimestamp());
						scoreCard.setTotalPoints(0);
						scoreCard.setHandicapBefore(memberInfo[0].getHandicap());
						scoreCard.setHandicapAfter(handicap);
						scoreCard.setSlope(0);
						scoreCard.setCourseRating(0);
						scoreCard.setTeeColorID(0);
						scoreCard.setFieldID(0);
						scoreCard.setHandicapCorrection(true);

						scoreCard.insert();

						memberInfo[0].setHandicap(handicap);
						memberInfo[0].update();
						UpdateHandicap.update(Integer.parseInt(member_id), stampur);

						getParentPage().setParentToReload();
						getParentPage().close();

					}

				}

			}
		}
	}
}