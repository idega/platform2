package is.idega.idegaweb.golf.handicap.presentation;

/**
 * Title: HandicapOverview Description: Displayes the handicap of a selected
 * golfer, ordered by date Copyright: Copyright (c) 2001 Company: idega co.
 * 
 * @author Laddi
 * @version 1.3
 */

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeColorHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.service.GolfGroup;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;
import is.idega.idegaweb.golf.tournament.even.TournamentEventListener;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

public class HandicapOverview extends GolfBlock {

	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	private String iMemberID;
	private boolean isAdmin = false;

	//Bjarni added this boolean and color String 14.08.01
	private boolean isDefaultColors = true;
	private String teeTextColor;

	private int year = 0;
	private int month = 0;
	private int day = 0;
	private IWCalendar calendar;
	private IWTimestamp start;
	private IWTimestamp end;
	private Table table;
	private Form form;
	private boolean noIcons = false;

	private boolean setDifferentOverviewButton = false;
	private String getOverviewButtonParameterName, getOverviewButtonParameterValue;

	private boolean isTilPicture = false;
	private boolean isFraPicture = false;
	private String tilPictureUrlInBundle;
	private String fraPictureUrlInBundle;
	private String getOverviewButtonImageUrlInBundle;
	private String getOverViewParameterName;
	private String getOverViewParameterValue;

	private String headerColor = "#FFFFFF";

	private boolean isWindow = false;
	
	private ICPage iFieldPage;
	private ICPage iTournamentPage;

	public HandicapOverview() {
	}

	public HandicapOverview(String member_id) {
		this.iMemberID = member_id;
	}

	public HandicapOverview(int member_id) {
		this.iMemberID = String.valueOf(member_id);
	}

	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle();
		iwb = getBundle();

		this.isAdmin = isAdministrator(modinfo);

		if (iMemberID == null) {
			iMemberID = modinfo.getParameter("member_id");
			if (iMemberID == null) {
				iMemberID = (String) modinfo.getSession().getAttribute("member_id");
				if (iMemberID == null) {
					Member memberinn = (Member) modinfo.getSession().getAttribute("member_login");
					if (memberinn != null) {
						iMemberID = String.valueOf(memberinn.getID());
						if (iMemberID == null) {
							iMemberID = "1";
						}
					}
					else {
						iMemberID = "1";
					}
				}
			}
		}

		fillTable(modinfo);
		form.add(table);

		add(form);
	}

	private void fillTable(IWContext modinfo) throws IOException, SQLException, FinderException {
		Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(this.iMemberID));
		MemberInfo memberInfo = member.getMemberInfo();

		String[] dates = getDates(modinfo);

		Scorecard[] scoreCards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id='" + iMemberID + "' and scorecard_date>='" + dates[0] + "' and scorecard_date<='" + (dates[1] + " 23:59:59.0") + "' and scorecard_date is not null order by scorecard_date");
		Scorecard[] scoreCardsBefore = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id = " + iMemberID + " and scorecard_date < '" + dates[0] + "' order by scorecard_date desc");

		form = new Form();
		
		table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setBorder(0);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setRowAlignment(1, Table.HORIZONTAL_ALIGN_CENTER);
		table.setRowVerticalAlignment(1, Table.VERTICAL_ALIGN_BOTTOM);
		int row = 2;

		table.add(iwrb.getLocalizedString("handicap.date", "Date"), 1, row);
		table.add(iwrb.getLocalizedString("handicap.course", "Course"), 2, row);
		table.add(iwrb.getLocalizedString("handicap.tournament", "Tournament"), 3, row);
		table.add(iwrb.getLocalizedString("handicap.tees", "Tees"), 4, row);
		table.add(iwrb.getLocalizedString("handicap.slope", "Slope/CR"), 5, row);
		table.add(iwrb.getLocalizedString("handicap.course_handicap", "Course") + Text.BREAK + iwrb.getLocalizedString("handicap.handicap_lowercase", "handicap"), 6, row);
		table.add(iwrb.getLocalizedString("handicap.points", "Points"), 7, row);
		table.add(iwrb.getLocalizedString("handicap.difference", "Difference"), 8, row);
		table.add(iwrb.getLocalizedString("handicap.handicap", "Handicap"), 9, row);
		table.add(iwrb.getLocalizedString("handicap.new", "New") + Text.BREAK + iwrb.getLocalizedString("handicap.handicap_lowercase", "handicap"), 10, row);

		if (!isWindow) {
			table.add(iwrb.getLocalizedString("handicap.scorecard", "Scorecard"), 11, row);
			if (isAdmin) {
				table.setWidth(11, row, 72);
			}
			else {
				table.setWidth(11, row, 54);
			}
		}
		table.setRowStyleClass(row, getHeaderRowClass());
		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowVerticalAlignment(row++, Table.VERTICAL_ALIGN_BOTTOM);

		int basePoints = 36;
		int zebraRow = 1;
		boolean mergedCells = false;
		
		for (int a = 0; a < scoreCards.length; a++) {
			String date = new IWTimestamp(scoreCards[a].getScorecardDate()).getDateString("dd/MM/yy");
			
			Image deleteImage = iwb.getImage("shared/handicap/delete.gif", iwrb.getLocalizedString("handicap.scorecard_delete", "Delete scorecard"));
			deleteImage.setToolTip(iwrb.getLocalizedString("handicap.scorecard_delete", "Delete scorecard"));
			deleteImage.setHorizontalSpacing(1);
			Link deleteScorecard = new Link(deleteImage);
			deleteScorecard.setWindowToOpen(HandicapUtility.class);
			deleteScorecard.addParameter(HandicapUtility.PARAMETER_SCORECARD_ID, String.valueOf(scoreCards[a].getID()));
			deleteScorecard.addParameter(HandicapUtility.PARAMETER_METHOD, HandicapUtility.ACTION_DELETE_SCORECARD);

			if (scoreCards[a].getForeignRound()) {
				table.mergeCells(2, row, 6, row);
				mergedCells = true;

				table.add(date, 1, row);
				table.add("- " + scoreCards[a].getForeignCourseName() + " -", 2, row);
				table.add("" + scoreCards[a].getTotalPoints(), 7, a + 3);
				table.add(String.valueOf(scoreCards[a].getTotalPoints() - basePoints), 8, row);
				table.add(TextSoap.singleDecimalFormat((double) scoreCards[a].getHandicapBefore()), 9, row);
				table.add(TextSoap.singleDecimalFormat((double) scoreCards[a].getHandicapAfter()), 10, row);

				if (!isWindow) {
					if (isAdmin || iMemberID.equalsIgnoreCase("1")) {
						table.add(deleteScorecard, 11, row);
					}
				}
			}
			else {
				if (!scoreCards[a].getHandicapCorrection()) {
					Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(scoreCards[a].getFieldID());
					TeeColor teeColor = ((TeeColorHome) IDOLookup.getHomeLegacy(TeeColor.class)).findByPrimaryKey(scoreCards[a].getTeeColorID());
					TournamentRound tournamentRound = null;
					String tournamentName = "";
					Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
					mergedCells = false;

					if (scoreCards[a].getTournamentRoundId() != 1 && scoreCards[a].getTournamentRoundId() != -1) {
						tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(scoreCards[a].getTournamentRoundId());
						tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(tournamentRound.getTournamentID());
						tournamentName = tournament.getName();
					}

					int teeColorID = scoreCards[a].getTeeColorID();
					String fieldName = field.getName();
					String teeName = teeColor.getName();

					double slope = (double) scoreCards[a].getSlope();
					double courseRating = (double) scoreCards[a].getCourseRating();
					String cr = TextSoap.singleDecimalFormat(String.valueOf(courseRating));
					double fieldPar = (double) field.getFieldPar();
					double baseHandicap = (double) scoreCards[a].getHandicapBefore();
					int totalPoints = scoreCards[a].getTotalPoints();
					float newBaseHandicap = scoreCards[a].getHandicapAfter();

					int playHandicap = 0;

					Handicap leik = new Handicap(baseHandicap);
					playHandicap = leik.getLeikHandicap(slope, courseRating, fieldPar);

					float realHandicap = 0;

					if (a == 0) {
						if (scoreCardsBefore.length > 0) {
							realHandicap = scoreCardsBefore[0].getHandicapAfter();
						}
						else {
							realHandicap = memberInfo.getFirstHandicap();
						}
					}
					else {
						realHandicap = scoreCards[a - 1].getHandicapAfter();
					}

					Handicap courseHandicap = new Handicap((double) realHandicap);
					int realPlayHandicap = courseHandicap.getLeikHandicap(slope, courseRating, fieldPar);

					boolean showRealHandicap = false;
					if (scoreCards[a].getTournamentRoundId() > 1) {
						if (member.getGender().equalsIgnoreCase("m")) {
							if ((float) realPlayHandicap > tournament.getMaxHandicap()) {
								showRealHandicap = true;
							}
						}
						else if (member.getGender().equalsIgnoreCase("f")) {
							if ((float) realPlayHandicap > tournament.getFemaleMaxHandicap()) {
								showRealHandicap = true;
							}
						}
						if (tournament.getTournamentType().getModifier() != -1) {
							showRealHandicap = true;
						}
					}
					if (tournamentRound != null) {
						showRealHandicap = true;
					}

					int realPoints = 0;
					if (showRealHandicap) {
						realPoints = courseHandicap.getTotalPoints(scoreCards[a].getID(), realPlayHandicap);
					}

					table.add(date, 1, row);

					if (iFieldPage != null) {
						Link fieldLink = getSmallLink(fieldName);
						fieldLink.setPage(iFieldPage);
						fieldLink.addParameter("field_id", field.getID());
						fieldLink.addParameter("union_id", field.getUnionID());
						table.add(fieldLink, 2, row);
					}
					else {
						table.add(fieldName, 2, row);
					}

					if (tournamentName != null) {
						if (iTournamentPage != null) {
							Link tournamentLink = getSmallLink(tournamentName);
							tournamentLink.setPage(iTournamentPage);
							tournamentLink.setEventListener(TournamentEventListener.class);
							tournamentLink.addParameter(getTournamentSession(modinfo).getParameterNameTournamentID(), tournament.getID());
							table.add(tournamentLink, 3, row);
						}
						else {
							table.add(tournamentName, 3, row);
						}
					}
					else {
						table.addText("", 3, row);
					}

					table.add(teeName, 4, row);
					table.add(String.valueOf((int) Math.rint(slope)) + Text.NON_BREAKING_SPACE + "/" + Text.NON_BREAKING_SPACE + cr, 5, row);

					if (showRealHandicap) {
						table.add(String.valueOf(realPlayHandicap), 6, row);
						table.add(String.valueOf(realPoints), 7, row);
						table.add(String.valueOf(realPoints - basePoints), 8, row);
						table.add(TextSoap.singleDecimalFormat(String.valueOf(realHandicap)), 9, row);
					}
					else {
						table.add(String.valueOf(playHandicap), 6, row);
						table.add(String.valueOf(totalPoints), 7, row);
						table.add(String.valueOf(totalPoints - basePoints), 8, row);
						table.add(TextSoap.singleDecimalFormat(String.valueOf(baseHandicap)), 9, row);
					}

					if (Double.toString(baseHandicap) != null) {
						table.add(TextSoap.singleDecimalFormat(String.valueOf(newBaseHandicap)), 10, row);
					}

					table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);

					GolfGroup golfGroup = new GolfGroup(iMemberID);
					boolean canWrite = true;
					if (!isAdmin) {
						canWrite = golfGroup.getCanWrite();
					}
					if (iMemberID.equalsIgnoreCase("1")) {
						canWrite = true;
					}

					Image view = iwb.getImage("shared/handicap/eye.gif", iwrb.getLocalizedString("handicap.view_scorecard", "View scorecard"));
					view.setToolTip(iwrb.getLocalizedString("handicap.view_scorecard", "View scorecard"));
					view.setHorizontalSpacing(1);
					Link tengill = new Link(view);
					tengill.setWindowToOpen(HandicapScorecardView.class);
					tengill.addParameter("scorecard_id", String.valueOf(scoreCards[a].getID()));

					Image updateImage = iwb.getImage("shared/handicap/edit.gif", iwrb.getLocalizedString("handicap.update_scorecard", "Change scorecard"));
					updateImage.setToolTip(iwrb.getLocalizedString("handicap.update_scorecard", "Change scorecard"));
					updateImage.setHorizontalSpacing(1);
					Link update = new Link(updateImage);
					update.setWindowToOpen(HandicapRegisterWindow.class);
					update.addParameter("scorecard_id", String.valueOf(scoreCards[a].getID()));

					Image statisticsImage = iwb.getImage("shared/handicap/edit.gif", iwrb.getLocalizedString("handicap.register_statistics", "Register statistics"));
					statisticsImage.setToolTip(iwrb.getLocalizedString("handicap.register_statistics", "Register statistics"));
					statisticsImage.setHorizontalSpacing(1);
					Link update2 = new Link(statisticsImage);
					update2.setWindowToOpen(HandicapStatistics.class);
					update2.addParameter("scorecard_id", String.valueOf(scoreCards[a].getID()));

					if (!isWindow) {
						if (isAdmin || iMemberID.equalsIgnoreCase("1")) {
							table.add(deleteScorecard, 11, row);
						}

						if (isAdmin) {
							table.add(update, 11, row);
						}
						else {
							if (canWrite && tournamentName.length() == 0 && !noIcons) {
								table.add(update, 11, row);
							}
							if (tournamentName.length() > 0 && !noIcons) {
								table.add(update2, 11, row);
							}
						}

						table.add(tengill, 11, row);
					}

					if (tournamentRound != null) {
						boolean increase = tournamentRound.getIncreaseHandicap();
						boolean decrease = tournamentRound.getDecreaseHandicap();
						Image image = null;

						if (increase && !decrease) {
							image = iwb.getImage("shared/handicap/up.gif");
							image.setName(iwrb.getLocalizedString("handicap.increase", "Handicap increase"));
							image.setToolTip(iwrb.getLocalizedString("handicap.increase", "Handicap increase"));
						}
						else if (!increase && decrease) {
							image = iwb.getImage("shared/handicap/down.gif");
							image.setName(iwrb.getLocalizedString("handicap.decrease", "Handicap decrease"));
							image.setToolTip(iwrb.getLocalizedString("handicap.decrease", "Handicap decrease"));
						}
						else if (increase && decrease) {
							image = iwb.getImage("shared/handicap/updown.gif");
							image.setName(iwrb.getLocalizedString("handicap.increase_decrease", "Handicap increase/decrease"));
							image.setToolTip(iwrb.getLocalizedString("handicap.increase_decrease", "Handicap increase/decrease"));
						}
						else {
							image = iwb.getImage("shared/handicap/nochange.gif");
							image.setName(iwrb.getLocalizedString("handicap.no_change", "No change to handicap"));
							image.setToolTip(iwrb.getLocalizedString("handicap.no_change", "No change to handicap"));
						}
						image.setHorizontalSpacing(1);

						if (!isWindow) {
							table.add(image, 11, row);
						}
					}

				}

				else {
					mergedCells = true;
					table.mergeCells(2, row, 8, row);
					table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);

					Text updateText = new Text("- " + iwrb.getLocalizedString("handicap.handicap_correction", "Handicap correction") + " -");
					Text handicapBefore = new Text(TextSoap.singleDecimalFormat((double) scoreCards[a].getHandicapBefore()));
					Text handicapAfter = new Text(TextSoap.singleDecimalFormat((double) scoreCards[a].getHandicapAfter()));

					table.add(date, 1, row);
					table.add(updateText, 2, row);
					table.add(handicapBefore, 9, row);
					table.add(handicapAfter, 10, row);
					if (!isWindow) {
						table.addText("", 11, row);
						if (isAdmin || iMemberID.equalsIgnoreCase("1")) {
							table.add(deleteScorecard, 11, row);
						}
					}
				}
			}

			table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_MIDDLE);
			table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);
			table.setHeight(row, 20);
			if (!mergedCells) {
				table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_LEFT);
				table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_LEFT);
			}
			if (!isWindow) {
				table.setAlignment(11, row, Table.HORIZONTAL_ALIGN_LEFT);
				table.setNoWrap(11, row);
				if (isAdmin) {
					table.setWidth(11, row, 72);
				}
				else {
					table.setWidth(11, row, 54);
				}
			}
			if (zebraRow % 2 != 0) {
				table.setRowStyleClass(row++, getLightRowClass());
			}
			else {
				table.setRowStyleClass(row++, getDarkRowClass());
			}
			zebraRow++;
		}

		if (!isWindow) {
			table.mergeCells(1, 1, 11, 1);
			table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_MIDDLE);
			table.setHeight(1, 40);
			table.setHeight(2, 20);
			getForm();

			GenericButton print = getButton(new GenericButton("print", iwrb.getLocalizedString("handicap.print", "Print")));
			print.setWindowToOpen(HandicapOverviewWindow.class);
			print.addParameterToWindow(HandicapOverviewWindow.PARAMETER_MEMBER_ID, iMemberID);
			print.addParameterToWindow("start_date", modinfo.getParameter("start_date"));
			print.addParameterToWindow("end_date", modinfo.getParameter("end_date"));

			GenericButton recalculate = getButton(new GenericButton("recalculate", iwrb.getLocalizedString("handicap.update_handicap", "Update handicap")));
			recalculate.setWindowToOpen(HandicapUtility.class);
			recalculate.addParameterToWindow(HandicapUtility.PARAMETER_MEMBER_ID, iMemberID);
			recalculate.addParameterToWindow(HandicapUtility.PARAMETER_METHOD, HandicapUtility.ACTION_RECALCULATE_HANDICAP);

			table.mergeCells(1, row, 11, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setHeight(row, 40);
			if (Integer.parseInt(this.iMemberID) > 1 && !noIcons) {
				table.add(print, 1, row);
				table.add(Text.getNonBrakingSpace(), 1, row);
				table.add(recalculate, 1, row);
				table.setCellpaddingRight(1, row, 5);
			}
			if (noIcons) {
				table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
			}
		}

	}

	private void getForm() throws IOException {
		form.add(new HiddenInput("member_id", iMemberID));
		form.add(new HiddenInput("handicap_action", "overView"));
		
		DateInput startDate = (DateInput) getStyledInterface(new DateInput("start_date"));
		startDate.setYearRange(2000, year);
		startDate.setDate(start.getDate());

		DateInput endDate = (DateInput) getStyledInterface(new DateInput("end_date"));
		endDate.setYearRange(2000, year);
		endDate.setDate(end.getDate());

		SubmitButton getOverview = (SubmitButton) getButton(new SubmitButton(iwrb.getLocalizedString("handicap.get_overview", "Get overview")));
		if (setDifferentOverviewButton) {
			getOverview = new SubmitButton(iwrb.getImage(getOverviewButtonImageUrlInBundle), getOverviewButtonParameterName, getOverviewButtonParameterValue);
		}

		Table navigationTable = new Table();
		navigationTable.setCellpadding(0);
		navigationTable.setCellspacing(0);
		int row = 1;

		navigationTable.add(getHeader(iwrb.getLocalizedString("handicap.from", "From") + ": "), row++, 1);
		navigationTable.setWidth(row++, 5);
		navigationTable.add(startDate, row++, 1);
		navigationTable.setWidth(row++, 12);

		navigationTable.add(getHeader(iwrb.getLocalizedString("handicap.to", "To") + ": "), row++, 1);
		navigationTable.setWidth(row++, 5);
		navigationTable.add(endDate, row++, 1);
		navigationTable.setWidth(row++, 12);

		navigationTable.add(getOverview, row++, 1);
		navigationTable.setWidth(row++, 5);

		table.add(navigationTable, 1, 1);
	}

	private String[] getDates(IWContext modinfo) throws IOException {
		String[] dates = {"", ""};

		calendar = new IWCalendar();
		year = calendar.getYear();
		month = calendar.getMonth();
		day = calendar.getDay();

		if (modinfo.isParameterSet("start_date")) {
			start = new IWTimestamp(modinfo.getParameter("start_date"));
		}
		else {
			start = new IWTimestamp();
			start.addYears(-1);
		}

		if (modinfo.isParameterSet("end_date")) {
			end = new IWTimestamp(modinfo.getParameter("end_date"));
		}
		else {
			end = new IWTimestamp();
		}

		dates[0] = start.toSQLDateString();
		dates[1] = end.toSQLDateString();
		
		return dates;
	}

	public void noIcons() {
		this.noIcons = true;
	}

	public void setIsWindow(boolean isWindow) {
		this.isWindow = isWindow;
	}

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	/**
	 * @param fieldPage The fieldPage to set.
	 */
	public void setFieldPage(ICPage fieldPage) {
		this.iFieldPage = fieldPage;
	}
	
	/**
	 * @param tournamentPage The tournamentPage to set.
	 */
	public void setTournamentPage(ICPage tournamentPage) {
		this.iTournamentPage = tournamentPage;
	}
}