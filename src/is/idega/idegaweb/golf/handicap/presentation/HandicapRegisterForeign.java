/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class HandicapRegisterForeign extends GolfWindow {

	public HandicapRegisterForeign() {
		setWidth(400);
		setHeight(310);
		setTitle("Foreign round");
		add(new RegisterForeign());
	}

	public class RegisterForeign extends GolfBlock {

		public void main(IWContext modinfo) throws Exception {
			try {
				IWResourceBundle iwrb = getResourceBundle();

				getParentPage().setTitle(iwrb.getLocalizedString("handicap.foreign_round", "Foreign round"));
				addHeading(iwrb.getLocalizedString("handicap.foreign_round", "Foreign round"));

				String mode = modinfo.getParameter("mode");
				if (mode == null) {
					mode = "";
				}

				String member_id = modinfo.getParameter("member_id");
				if (member_id == null) member_id = "3";

				if (mode.equals("")) {
					try {
						Table myTable = new Table();
						myTable.setCellpadding(5);
						myTable.setAlignment("center");

						Form myForm = new Form();
						myTable.add(new HiddenInput("member_id", member_id));
						myTable.add(new HiddenInput("mode", "save"));

						IWTimestamp stamp = new IWTimestamp();

						DateInput date = new DateInput("date");
						date.setYearRange(stamp.getYear(), 2000);
						date.setDate(stamp.getDate());

						myTable.add(getHeader(iwrb.getLocalizedString("handicap.foreign_field_name", "Field name") + ":"), 1, 1);
						myTable.add(getStyledInterface(new TextInput("name")), 2, 1);
						myTable.add(getHeader(iwrb.getLocalizedString("handicap.date", "Date") + ":"), 1, 2);
						myTable.add(date, 2, 2);
						myTable.add(getHeader(iwrb.getLocalizedString("handicap.total_points", "Total points") + ":"), 1, 3);
						myTable.add(new TextInput("total_points"), 2, 3);
						myTable.mergeCells(1, 4, 2, 4);
						myTable.add(getButton(new CloseButton(iwrb.getLocalizedString("handicap.back", "Back"))), 1, 4);
						myTable.add(Text.getNonBrakingSpace(), 1, 4);
						myTable.add(Text.getNonBrakingSpace(), 1, 4);
						myTable.add(getButton(new SubmitButton(iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 1, 4);

						myForm.add(myTable);
						add(Text.getBreak());
						add(myForm);
					}
					catch (Exception ex) {
						ex.printStackTrace(System.err);
					}
				}

				else if (mode.equalsIgnoreCase("save")) {
					String date = modinfo.getParameter("date");
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
						IWTimestamp stampur = new IWTimestamp(date);

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
						scoreCard.setHandicapCorrection(false);
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
}