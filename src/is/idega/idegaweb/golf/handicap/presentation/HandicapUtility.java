/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.ScorecardHome;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class HandicapUtility extends GolfWindow {

	public static final String PARAMETER_METHOD = "hcp_util_method";

	public static final String PARAMETER_ACTION = "hcp_util_action";

	public static final String PARAMETER_SCORECARD_ID = "hcp_scorecard_id";

	public static final String PARAMETER_FIELD_ID = "hcp_field_id";

	public static final String PARAMETER_TEE_COLOR_ID = "hcp_tee_color_id";

	public static final String PARAMETER_MEMBER_ID = "hcp_member_id";

	public static final String PARAMETER_TOURNAMENT_ID = "hcp_tournament_id";

	private static final String PARAMETER_DAY = "hcp_day";

	private static final String PARAMETER_MONTH = "hcp_month";

	private static final String PARAMETER_YEAR = "hcp_year";

	private static final String PARAMETER_HANDICAP = "hcp_handicap";

	public static final int ACTION_CHANGE_TEES = 1;

	public static final int ACTION_DELETE_SCORECARD = 2;

	public static final int ACTION_RECALCULATE_HANDICAP = 3;

	public static final int ACTION_UPDATE_HANDICAP = 4;

	public static final int ACTION_FIND_FIELD = 6;

	public HandicapUtility() {
		setWidth(400);
		setHeight(260);
		setTitle("Handicap Utility");
		add(new Utility());
	}

	public class Utility extends GolfBlock {

		private int _method = -1;

		private int _action = -1;

		private IWResourceBundle _iwrb;

		private int _scorecardID = -1;

		private int _fieldID = -1;

		private int _teeColorID = -1;

		private int _memberID = -1;

		private int _tournamentID = -1;

		public void main(IWContext modinfo) throws Exception {
			_iwrb = getResourceBundle();

			switch (parseAction(modinfo)) {
				case ACTION_CHANGE_TEES:
					changeTees(modinfo);
					break;
				case ACTION_DELETE_SCORECARD:
					deleteScorecard(modinfo);
					break;
				case ACTION_RECALCULATE_HANDICAP:
					recalculate(modinfo);
					break;
				case ACTION_UPDATE_HANDICAP:
					updateHandicap(modinfo);
					break;
				case ACTION_FIND_FIELD:
					findField(modinfo);
					break;

				default:
					getForm(modinfo);
					break;
			}
		}

		private void getForm(IWContext modinfo) throws Exception {
			switch (_method) {
				case ACTION_CHANGE_TEES:
					getChangeTeesForm(modinfo);
					break;
				case ACTION_DELETE_SCORECARD:
					getDeleteScorecardForm(modinfo);
					break;
				case ACTION_RECALCULATE_HANDICAP:
					getRecalculateForm(modinfo);
					break;
				case ACTION_UPDATE_HANDICAP:
					getUpdateHandicapForm(modinfo);
					break;
				case ACTION_FIND_FIELD:
					getFindFieldForm(modinfo);
					break;

				default:
					getParentPage().close();
					break;
			}
		}

		private void getChangeTeesForm(IWContext modinfo) throws Exception {
			getParentPage().setTitle(_iwrb.getLocalizedString("handicap.change_tees", "Change tees"));
			addHeading(_iwrb.getLocalizedString("handicap.change_tees", "Change tees"));

			Scorecard scoreCard = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(_scorecardID);

			Table myTable = new Table(2, 3);
			myTable.mergeCells(1, 1, 2, 1);
			myTable.mergeCells(1, 2, 2, 2);
			myTable.setAlignment("center");
			myTable.setAlignment(1, 2, "center");

			Form myForm = new Form();
			myForm.add(new HiddenInput(PARAMETER_SCORECARD_ID, String.valueOf(_scorecardID)));
			myForm.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_CHANGE_TEES)));
			myForm.add(new HiddenInput(PARAMETER_FIELD_ID, String.valueOf(scoreCard.getFieldID())));

			TeeColor[] tee = (TeeColor[]) ((TeeColor) IDOLookup.instanciateEntity(TeeColor.class)).findAll("select distinct tc.* from tee_color tc, tee t, scorecard s, member m where tc.tee_color_id = t.tee_color_id and t.field_id = s.field_id and s.member_id = m.member_id and tc.gender = m.gender and s.scorecard_id = " + _scorecardID + " order by tc.tee_color_id");

			DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_TEE_COLOR_ID));
			for (int a = 0; a < tee.length; a++) {
				menu.addMenuElement(tee[a].getID(), tee[a].getName());
			}
			menu.setSelectedElement(scoreCard.getTeeColorID() + "");

			myTable.add(getHeader(_iwrb.getLocalizedString("handicap.choose_tees", "Choose tees") + ":"), 1, 1);
			myTable.add(menu, 1, 2);
			myTable.add(getButton(new SubmitButton(_iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 2, 3);
			myTable.add(getButton(new CloseButton(_iwrb.getLocalizedString("handicap.close", "Close"))), 1, 3);

			myForm.add(myTable);

			add(new Text().getBreak());
			add(myForm);
		}

		private void getDeleteScorecardForm(IWContext modinfo) throws Exception {
			getParentPage().setTitle(_iwrb.getLocalizedString("handicap.scorecard_delete", "Delete scorecard"));
			addHeading(_iwrb.getLocalizedString("handicap.scorecard_delete", "Delete scorecard"));

			Table myTable = new Table(2, 2);
			myTable.mergeCells(1, 1, 2, 1);
			myTable.setAlignment("center");

			Form myForm = new Form();
			myForm.add(new HiddenInput(PARAMETER_SCORECARD_ID, String.valueOf(_scorecardID)));
			myForm.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_DELETE_SCORECARD)));

			myTable.add(getHeader(_iwrb.getLocalizedString("handicap.scorecard_delete", "Delete scorecard") + "?"), 1, 1);
			myTable.add(getButton(new SubmitButton(_iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 2, 2);
			myTable.add(getButton(new CloseButton(_iwrb.getLocalizedString("handicap.close", "Close"))), 1, 2);

			myForm.add(myTable);
			Text breakText = new Text("<br>");

			add(breakText);
			add(myForm);
		}

		private void getRecalculateForm(IWContext modinfo) throws Exception {
			getParentPage().setTitle(_iwrb.getLocalizedString("handicap.recalculate_handicap", "Recalculate handicap"));
			addHeading(_iwrb.getLocalizedString("handicap.recalculate_handicap", "Recalculate handicap"));

			Form myForm = new Form();
			myForm.add(new HiddenInput(PARAMETER_MEMBER_ID, String.valueOf(_memberID)));
			myForm.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_RECALCULATE_HANDICAP)));

			Table myTable = new Table(2, 3);
			myTable.mergeCells(1, 1, 2, 1);
			myTable.mergeCells(1, 2, 2, 2);
			myTable.setAlignment("center");
			myTable.setAlignment(1, 2, "center");
			myTable.setAlignment(2, 3, "right");
			myTable.setCellpadding(4);

			IWCalendar dagatal = new IWCalendar();
			String month = String.valueOf(dagatal.getMonth());
			String year = String.valueOf(dagatal.getYear());
			String day = String.valueOf(dagatal.getDay());

			DropdownMenu select_month = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_MONTH));
			for (int m = 1; m <= 12; m++) {
				select_month.addMenuElement(String.valueOf(m), dagatal.getMonthName(m).toLowerCase());
			}
			select_month.setSelectedElement(month);

			DropdownMenu select_year = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_YEAR));
			for (int y = 2000; y <= dagatal.getYear(); y++) {
				select_year.addMenuElement(String.valueOf(y), String.valueOf(y));
			}
			select_year.setSelectedElement(year);

			DropdownMenu select_day = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_DAY));
			for (int d = 1; d <= 31; d++) {
				select_day.addMenuElement(String.valueOf(d), String.valueOf(d) + ".");
			}
			select_day.setSelectedElement(day);

			myTable.add(select_day, 1, 2);
			myTable.add(select_month, 1, 2);
			myTable.add(select_year, 1, 2);

			Text confirmText = getHeader(_iwrb.getLocalizedString("handicap.how_far_back", "How far back do you want to calculate"));

			myTable.add(confirmText, 1, 1);
			myTable.add(getButton(new SubmitButton(_iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 2, 3);
			myTable.add(getButton(new CloseButton(_iwrb.getLocalizedString("handicap.close", "Close"))), 1, 3);

			myForm.add(myTable);
			add("<br>");
			add(myForm);
		}

		private void getUpdateHandicapForm(IWContext modinfo) throws Exception {
			getParentPage().setTitle(_iwrb.getLocalizedString("handicap.update_handicap", "Update handicap"));
			addHeading(_iwrb.getLocalizedString("handicap.update_handicap", "Update handicap"));

			Table myTable = new Table(2, 3);
			myTable.mergeCells(1, 1, 2, 1);
			myTable.mergeCells(1, 2, 2, 2);
			myTable.setCellpadding(5);
			myTable.setAlignment("center");
			myTable.setAlignment(1, 2, "center");

			Form myForm = new Form();
			myForm.add(new HiddenInput(PARAMETER_MEMBER_ID, String.valueOf(_memberID)));
			myForm.add(new HiddenInput(PARAMETER_TOURNAMENT_ID, String.valueOf(_tournamentID)));
			myForm.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_UPDATE_HANDICAP)));

			myTable.add(getHeader(_iwrb.getLocalizedString("handicap.enter_handicap", "Enter handicap") + ":"), 1, 1);
			myTable.add(new TextInput(PARAMETER_HANDICAP), 1, 2);
			myTable.add(getButton(new SubmitButton(_iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 2, 3);
			myTable.add(getButton(new CloseButton(_iwrb.getLocalizedString("handicap.close", "Close"))), 1, 3);

			myForm.add(myTable);
			add(Text.getBreak());
			add(myForm);
		}

		public void getFindFieldForm(IWContext modinfo) throws Exception {
			getParentPage().setTitle(_iwrb.getLocalizedString("handicap.field_selection", "Field selection"));
			addHeading(_iwrb.getLocalizedString("handicap.field_selection", "Field selection"));

			Form myForm = new Form();
			myForm.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_FIND_FIELD)));

			Table myTable = new Table(1, 3);
			myTable.setCellpadding(3);
			myTable.setCellspacing(3);
			myTable.setAlignment(1, 3, "right");
			myTable.setAlignment("center");
			myTable.setVerticalAlignment("middle");

			Field[] field = (Field[]) ((Field) IDOLookup.instanciateEntity(Field.class)).findAllOrdered("name");

			DropdownMenu select_field = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_FIELD_ID));
			for (int a = 0; a < field.length; a++) {
				Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(field[a].getUnionID());
				select_field.addMenuElement(field[a].getID(), field[a].getName() + " - " + union.getAbbrevation());
			}

			myTable.add(getHeader(_iwrb.getLocalizedString("handicap.select_field", "Select field") + ":"), 1, 1);
			myTable.add(select_field, 1, 2);
			myTable.add(getButton(new SubmitButton(_iwrb.getLocalizedString("handicap.confirm", "Confirm"))), 1, 3);

			myForm.add(myTable);

			add(new Text().getBreak());
			add(myForm);
		}

		private void changeTees(IWContext modinfo) throws Exception {
			Scorecard scoreCard = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(_scorecardID);

			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct slope,course_rating from tee where field_id =  " + _fieldID + " and tee_color_id = " + _teeColorID);

			if (tee.length > 0) {
				TournamentRound round = scoreCard.getTournamentRound();
				Tournament tournament = round.getTournament();
				IWTimestamp stampur = new IWTimestamp(tournament.getStartTime());
				stampur.addDays(-2);

				Scorecard[] scorecards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard s, tournament_round tr where s.tournament_round_id = tr.tournament_round_id and tr.tournament_id = " + tournament.getID() + " and s.member_id = " + scoreCard.getMemberId());
				for (int a = 0; a < scorecards.length; a++) {
					scorecards[a].setSlope(tee[0].getSlope());
					scorecards[a].setCourseRating(tee[0].getCourseRating());
					scorecards[a].setTeeColorID(_teeColorID);
					scorecards[a].update();
				}

				UpdateHandicap.update(scoreCard.getMemberId(), stampur);
			}

			getParentPage().setParentToReload();
			getParentPage().close();
		}

		private void deleteScorecard(IWContext modinfo) throws Exception {
			Scorecard scorecard = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(_scorecardID);
			int memberID = scorecard.getMemberId();
			IWTimestamp stamp = new IWTimestamp(scorecard.getScorecardDate());
			scorecard.delete();
			UpdateHandicap.update(memberID, stamp);

			getParentPage().setParentToReload();
			getParentPage().close();
		}

		private void recalculate(IWContext modinfo) throws Exception {
			String year = modinfo.getParameter(PARAMETER_YEAR);
			String month = modinfo.getParameter(PARAMETER_MONTH);
			String day = modinfo.getParameter(PARAMETER_DAY);

			IWTimestamp stampur = new IWTimestamp(new IWTimestamp());
			if (day != null) stampur.setDay(Integer.parseInt(day));
			if (month != null) stampur.setMonth(Integer.parseInt(month) - 1);
			if (year != null) stampur.setYear(Integer.parseInt(year));

			UpdateHandicap.update(_memberID, stampur);

			getParentPage().setParentToReload();
			getParentPage().close();
		}

		private void updateHandicap(IWContext modinfo) throws Exception {
			float handicap = 100;
			String handicapString = modinfo.getParameter(PARAMETER_HANDICAP);

			if (handicapString != null && handicapString.length() > 0) {
				if (handicapString.indexOf(",") != -1) {
					handicapString = handicapString.replace(',', '.');
				}
				handicap = Float.parseFloat(handicapString);
			}

			Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(_tournamentID);
			MemberInfo memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(_memberID);
			IWTimestamp stampur2 = new IWTimestamp(tournament.getStartTime());
			IWTimestamp stampur = new IWTimestamp();
			stampur.setDay(stampur2.getDay());
			stampur.setMonth(stampur2.getMonth() - 1);
			stampur.setYear(stampur2.getYear());
			stampur.addDays(-1);

			Scorecard scoreCard = (Scorecard) IDOLookup.createLegacy(Scorecard.class);
			scoreCard.setMemberId(_memberID);
			scoreCard.setTournamentRoundId(1);
			scoreCard.setScorecardDate(stampur.getTimestamp());
			scoreCard.setTotalPoints(0);
			scoreCard.setHandicapBefore(memberInfo.getHandicap());
			scoreCard.setHandicapAfter(handicap);
			scoreCard.setSlope(0);
			scoreCard.setCourseRating(0);
			scoreCard.setTeeColorID(0);
			scoreCard.setFieldID(0);
			scoreCard.setHandicapCorrection(true);
			scoreCard.insert();

			UpdateHandicap.update(_memberID, stampur);

			getParentPage().setParentToReload();
			getParentPage().close();
		}

		private void findField(IWContext modinfo) {
			modinfo.setSessionAttribute("field_id", String.valueOf(_fieldID));

			getParentPage().setParentToReload();
			getParentPage().close();
		}

		private int parseAction(IWContext modinfo) {
			if (modinfo.isParameterSet(PARAMETER_METHOD)) {
				_method = Integer.parseInt(modinfo.getParameter(PARAMETER_METHOD));
			}
			if (modinfo.isParameterSet(PARAMETER_ACTION)) {
				_action = Integer.parseInt(modinfo.getParameter(PARAMETER_ACTION));
			}
			if (modinfo.isParameterSet(PARAMETER_SCORECARD_ID)) {
				_scorecardID = Integer.parseInt(modinfo.getParameter(PARAMETER_SCORECARD_ID));
			}
			if (modinfo.isParameterSet(PARAMETER_FIELD_ID)) {
				_fieldID = Integer.parseInt(modinfo.getParameter(PARAMETER_FIELD_ID));
			}
			if (modinfo.isParameterSet(PARAMETER_TEE_COLOR_ID)) {
				_teeColorID = Integer.parseInt(modinfo.getParameter(PARAMETER_TEE_COLOR_ID));
			}
			if (modinfo.isParameterSet(PARAMETER_MEMBER_ID)) {
				_memberID = Integer.parseInt(modinfo.getParameter(PARAMETER_MEMBER_ID));
			}
			if (modinfo.isParameterSet(PARAMETER_TOURNAMENT_ID)) {
				_tournamentID = Integer.parseInt(modinfo.getParameter(PARAMETER_TOURNAMENT_ID));
			}

			return _action;
		}
	}
}