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
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class HandicapRegisterForeign extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		try {
			IWResourceBundle iwrb = getResourceBundle();

			getParentPage().setTitle(iwrb.getLocalizedString("handicap.foreign_round", "Foreign round"));

			String mode = modinfo.getParameter("mode");
			if (mode == null) {
				mode = "";
			}

			String member_id = modinfo.getParameter("member_id");
			if (member_id == null)
				member_id = "3";

			if (mode.equals("")) {
				try {
					Table myTable = new Table();
					myTable.setCellpadding(5);
					myTable.setAlignment("center");

					Form myForm = new Form();
					myTable.add(new HiddenInput("member_id", member_id));
					myTable.add(new HiddenInput("mode", "save"));

					IWCalendar dagatal = new IWCalendar();
					String month = String.valueOf(dagatal.getMonth());
					String year = String.valueOf(dagatal.getYear());
					String day = String.valueOf(dagatal.getDay());

					DropdownMenu select_month = new DropdownMenu("month");
					for (int m = 1; m <= 12; m++) {
						select_month.addMenuElement(String.valueOf(m), dagatal.getMonthName(m).toLowerCase());
					}
					select_month.setSelectedElement(month);

					DropdownMenu select_year = new DropdownMenu("year");
					for (int y = 2000; y <= dagatal.getYear(); y++) {
						select_year.addMenuElement(String.valueOf(y), String.valueOf(y));
					}
					select_year.setSelectedElement(year);

					DropdownMenu select_day = new DropdownMenu("day");
					for (int d = 1; d <= 31; d++) {
						select_day.addMenuElement(String.valueOf(d), String.valueOf(d) + ".");
					}
					select_day.setSelectedElement(day);

					myTable.addText(iwrb.getLocalizedString("handicap.foreign_field_name", "Field name") + ":", 1, 1);
					myTable.add(new TextInput("name"), 2, 1);
					myTable.addText(iwrb.getLocalizedString("handicap.date", "Date") + ":", 1, 2);
					myTable.add(select_day, 2, 2);
					myTable.add(select_month, 2, 2);
					myTable.add(select_year, 2, 2);
					myTable.addText(iwrb.getLocalizedString("handicap.total_points", "Total points") + ":", 1, 3);
					myTable.add(new TextInput("total_points"), 2, 3);
					myTable.add(new CloseButton(iwrb.getImage("buttons/back.gif", "", 76, 19)), 1, 4);
					myTable.add(new SubmitButton(iwrb.getImage("buttons/confirm.gif", "", 76, 19)), 2, 4);

					myForm.add(myTable);
					add(Text.getBreak());
					add(myForm);
				}
				catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
			}

			else if (mode.equalsIgnoreCase("save")) {
				String year = modinfo.getParameter("year");
				String month = modinfo.getParameter("month");
				String day = modinfo.getParameter("day");
				int totalPoints = 0;

				String totalPointsString = modinfo.getParameter("total_points");
				String nameString = modinfo.getParameter("name");

				if (totalPointsString != null && totalPointsString.length() > 0) {
					try {
						totalPoints = Integer.parseInt(totalPointsString);
					}
					catch (Exception ex) {
						totalPoints = 0;
					}
				}

				if (totalPoints > 0 && nameString != null) {
					IWTimestamp stampur = new IWTimestamp();
					stampur.setDay(Integer.parseInt(day));
					stampur.setMonth(Integer.parseInt(month) - 1);
					stampur.setYear(Integer.parseInt(year));

					MemberInfo memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(Integer.parseInt(member_id));
					Scorecard scoreCard = (Scorecard) IDOLookup.createLegacy(Scorecard.class);
					scoreCard.setMemberId(Integer.parseInt(member_id));
					scoreCard.setTournamentRoundId(1);
					scoreCard.setScorecardDate(stampur.getTimestamp());
					scoreCard.setTotalPoints(totalPoints);
					scoreCard.setHandicapBefore(memberInfo.getHandicap());
					scoreCard.setHandicapAfter(memberInfo.getHandicap());
					scoreCard.setSlope(0);
					scoreCard.setCourseRating(0);
					scoreCard.setTeeColorID(0);
					scoreCard.setFieldID(0);
					scoreCard.setHandicapCorrection("N");
					scoreCard.setForeignRound(true);
					scoreCard.setForeignCourseName(nameString);
					scoreCard.insert();

					UpdateHandicap.update(Integer.parseInt(member_id), stampur);
				}
				getParentPage().close();
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}