/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.ScorecardHome;
import is.idega.idegaweb.golf.entity.Statistic;
import is.idega.idegaweb.golf.entity.Stroke;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.math.BigDecimal;
import java.util.Hashtable;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class HandicapRegister extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		this.empty();
		IWResourceBundle iwrb = getResourceBundle();

		getParentPage().setTitle(iwrb.getLocalizedString("handicap.register_scorecard", "Register scorecard"));

		String stat = modinfo.getParameter("statistics");
		if (stat == null) {
			stat = "0";
		}
		String mode = modinfo.getParameter("mode");
		if (mode == null) {
			mode = "";
		}
		String field_id = modinfo.getParameter("field_id");
		if (field_id == null) {
			field_id = "1";
		}
		String tee_number = modinfo.getParameter("tee_number");
		if (tee_number == null) {
			tee_number = "2";
		}

		String member_id = modinfo.getParameter("member_id");
		if (member_id == null) {
			Member memberinn = (Member) modinfo.getSessionAttribute("member_login");
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

		int numbers = 18;
		String start_hole = modinfo.getParameter("start_hole");
		if (start_hole == null) {
			start_hole = "1";
		}
		String number_of_holes = modinfo.getParameter("number_of_holes");
		if (number_of_holes == null) {
			number_of_holes = "18";
		}
		if (number_of_holes.equals("1") || (number_of_holes.equals("9") && start_hole.equals("1"))) {
			number_of_holes = "9";
			start_hole = "1";
			numbers = 9;
		}
		if (number_of_holes.equals("10")) {
			number_of_holes = "9";
			start_hole = "10";
		}

		String day = modinfo.getParameter("day");
		if (day == null) {
			day = "";
		}
		String month = modinfo.getParameter("month");
		if (month == null) {
			month = "";
		}
		String year = modinfo.getParameter("year");
		if (year == null) {
			year = "";
		}
		String tournament_round_id = modinfo.getParameter("tournament_round_id");
		if (tournament_round_id == null) {
			tournament_round_id = "1";
		}

		boolean update = false;
		boolean tournament = false;
		float handicapBefore = 36;
		float CourseRating = 70;
		int Slope = 113;
		IWTimestamp scoreDate = new IWTimestamp();
		boolean isDate = false;
		Hashtable str = null;

		String scorecard_id = modinfo.getParameter("scorecard_id");
		if (scorecard_id == null) {
			scorecard_id = "0";
		}
		if (Integer.parseInt(scorecard_id) > 0) {
			update = true;
		}

		if (update) {
			Scorecard scorecardID = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(Integer.parseInt(scorecard_id));
			Statistic[] statCrapper = (Statistic[]) ((Statistic) IDOLookup.instanciateEntity(Statistic.class)).findAll("select s.* from statistic s,tee t where scorecard_id = " + scorecard_id + " and s.tee_id = t.tee_id");
			//Statistic[] statCrapper = (Statistic[]) (new
			// Statistic()).findAllByColumn("scorecard_id",Integer.parseInt(scorecard_id));
			str = new Hashtable(18);
			Stroke[] strokesIDS = (Stroke[]) (com.idega.data.GenericEntity.getStaticInstance(is.idega.idegaweb.golf.entity.Stroke.class)).findAll("select s.* from stroke s,tee t where scorecard_id = " + scorecard_id + " and s.tee_id = t.tee_id order by s.tee_id");
			for (int i = 0; i < strokesIDS.length; i++)
				str.put(Integer.toString(strokesIDS[i].getTeeID()), strokesIDS[i]);

			field_id = String.valueOf(scorecardID.getFieldID());
			tee_number = String.valueOf(scorecardID.getTeeColorID());
			member_id = String.valueOf(scorecardID.getMemberId());

			if (scorecardID.getScorecardDate() != null) {
				scoreDate = new IWTimestamp(scorecardID.getScorecardDate());
				isDate = true;
			}
			year = String.valueOf(scoreDate.getYear());
			month = String.valueOf(scoreDate.getMonth());
			day = String.valueOf(scoreDate.getDay());
			tournament_round_id = String.valueOf(scorecardID.getTournamentRoundId());
			if (tournament_round_id.equalsIgnoreCase("1")) {
				tournament = false;
			}
			else {
				tournament = true;
				stat = "1";
			}

			if (statCrapper.length > 0) {
				stat = "1";
			}

			handicapBefore = scorecardID.getHandicapBefore();
			Slope = scorecardID.getSlope();
			CourseRating = scorecardID.getCourseRating();
		}

		IWCalendar dagatalid = new IWCalendar();
		if (Integer.parseInt(day) > dagatalid.getLengthOfMonth(Integer.parseInt(month), Integer.parseInt(year))) {
			day = String.valueOf(dagatalid.getLengthOfMonth(Integer.parseInt(month), Integer.parseInt(year)));
		}

		String dagur = year + "-" + month + "-" + day;

		boolean checker = true;
		boolean checker2 = true;
		double grunn = 36.0;

		Member memberID = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));
		grunn = (double) memberID.getHandicap();
		if (update) {
			grunn = (double) handicapBefore;
		}
		String grunn2 = scaleDecimals(String.valueOf(grunn), 1);

		Tee[] tee_id = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("SELECT * from tee where field_id='" + field_id + "' and tee_color_id='" + tee_number + "' and hole_number<='" + String.valueOf(numbers) + "' and hole_number>='" + start_hole + "' order by hole_number");
		Field fieldID = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));

		double course_rating = (double) tee_id[0].getCourseRating();
		double slope = (double) tee_id[0].getSlope();
		if (update) {
			course_rating = (double) CourseRating;
			slope = (double) Slope;
		}
		double field_par = (double) fieldID.getFieldPar();
		int tee_color_id = tee_id[0].getTeeColorID();
		String field_name = fieldID.getName();

		Handicap leikForgjof = new Handicap(grunn);

		int leik = leikForgjof.getLeikHandicap(slope, course_rating, field_par);

		modinfo.setDefaultFontSize("1");

		Form myForm = new Form();
		maintainParentReloadURL(modinfo,myForm);

		myForm.add(new HiddenInput("scorecard_id", scorecard_id));
		myForm.add(new HiddenInput("field_id", field_id));
		myForm.add(new HiddenInput("tee_number", tee_number));
		myForm.add(new HiddenInput("member_id", member_id));
		myForm.add(new HiddenInput("day", day));
		myForm.add(new HiddenInput("month", month));
		myForm.add(new HiddenInput("year", year));
		myForm.add(new HiddenInput("tournament_round_id", tournament_round_id));
		myForm.add(new HiddenInput("number_of_holes", number_of_holes));
		myForm.add(new HiddenInput("start_hole", start_hole));
		myForm.add(new HiddenInput("statistics", stat));
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton("mode", iwrb.getLocalizedString("handicap.calculate", "Calculate")));

		int a = 2;

		// Setur rétta stærð á töflu

		int haed = 12;
		int breidd = 12;

		if (number_of_holes.equals("9")) {
			haed = 6;
		}

		// Búinnnn!!!!

		Table contentTable = new Table();
		contentTable.setWidth("100%");
		contentTable.setCellpadding(3);
		contentTable.setCellspacing(3);

		Table myTable = new Table();
		myTable.setBorder(0);
		myTable.setWidth("100%");
		myTable.setColumnColor(12, "006600");
		myTable.setCellpadding(getCellpadding());
		myTable.setCellspacing(0);
		myTable.setColor("#FFFFFF");

		contentTable.add(myTable, 1, 2);
		contentTable.add(submit, 1, 5);

		Text grunn_forgjof = getHeader(iwrb.getLocalizedString("handicap.handicap", "Handicap") + ": " + grunn2);
		Text leik_forgjof = getHeader(iwrb.getLocalizedString("handicap.course_handicap", "Course") + " " + iwrb.getLocalizedString("handicap.handicap_lowercase", "handicap") + ": " + String.valueOf(leik));
		Text vallar_text = getHeader(field_name + "&nbsp;&nbsp;&nbsp;&nbsp;");
		Text uppl = getHeader(scaleDecimals(String.valueOf(slope), 0) + " / " + scaleDecimals(String.valueOf(course_rating), 1));

		Table handicapTable = new Table();
		handicapTable.setWidth("100%");
		handicapTable.setWidth(1, "33%");
		handicapTable.setWidth(2, "33%");
		handicapTable.setWidth(3, "33%");
		handicapTable.setRowAlignment(1, "center");

		handicapTable.add(grunn_forgjof, 1, 1);
		handicapTable.add(leik_forgjof, 2, 1);

		contentTable.add(vallar_text, 1, 1);
		contentTable.add(uppl, 1, 1);
		contentTable.add(handicapTable, 1, 4);

		myForm.add(contentTable);

		Text hola = getSmallHeader(iwrb.getLocalizedString("handicap.hole", "Hole"));
		Text lengd = getSmallBoldText(iwrb.getLocalizedString("handicap.length", "Length"));
		Text par = getSmallBoldText(iwrb.getLocalizedString("handicap.par", "Par"));
		Text forgjof = getSmallBoldText(iwrb.getLocalizedString("handicap.handicap", "Handicap"));
		Text hogg = getSmallBoldText(iwrb.getLocalizedString("handicap.strokes", "Strokes"));
		Text punktar_text = getSmallBoldText(iwrb.getLocalizedString("handicap.points", "Points"));
		Text ut = getSmallHeader(iwrb.getLocalizedString("handicap.out", "Out"));
		Text inn = getSmallHeader(iwrb.getLocalizedString("handicap.in", "In"));
		Text total = getSmallHeader(iwrb.getLocalizedString("handicap.total", "Total"));
		Text average = getSmallHeader(iwrb.getLocalizedString("handicap.average", "Average"));
		Text fairway_text = getSmallBoldText(iwrb.getLocalizedString("handicap.fairway", "Fairway"));
		Text greens_text = getSmallBoldText(iwrb.getLocalizedString("handicap.gir", "GIR"));
		Text putts_text = getSmallBoldText(iwrb.getLocalizedString("handicap.putts", "Putts"));

		myTable.add(hola, 1, 1);
		myTable.add(lengd, 1, 2);
		myTable.add(forgjof, 1, 3);
		myTable.add(par, 1, 4);
		myTable.add(hogg, 1, 5);
		myTable.add(punktar_text, 1, 6);

		if (haed == 12) {
			myTable.add(hola, 1, 7);
			myTable.add(lengd, 1, 8);
			myTable.add(forgjof, 1, 9);
			myTable.add(par, 1, 10);
			myTable.add(hogg, 1, 11);
			myTable.add(punktar_text, 1, 12);
		}

		int total_length = 0;
		int total_par = 0;
		int ut_length = 0;
		int ut_par = 0;
		int inn_length = 0;
		int inn_par = 0;

		int numer = 1;
		int lengdur = 2;
		int forgjofin = 3;
		int parid = 4;
		int skorid = 5;

		for (int b = 0; b < tee_id.length; b++) {
			myTable.setColumnAlignment(a, "center");

			int hole_number = tee_id[b].getHoleNumber();
			int hole_length = tee_id[b].getTeeLength();
			int hole_par = tee_id[b].getPar();
			int hole_handicap = (int) tee_id[b].getHandicap();

			Text hole_number_text = getSmallHeader(String.valueOf(hole_number));
			Text hole_length_text = getSmallText(String.valueOf(hole_length));
			Text hole_par_text = getSmallText(String.valueOf(hole_par));
			Text hole_handicap_text = getSmallText(String.valueOf(hole_handicap));

			total_length += hole_length;
			total_par += hole_par;

			if (hole_number <= 9) {
				ut_length += hole_length;
				ut_par += hole_par;
			}
			else if (hole_number > 9) {
				inn_length += hole_length;
				inn_par += hole_par;
			}

			myTable.add(hole_number_text, a, numer);
			myTable.add(hole_length_text, a, lengdur);
			myTable.add(hole_handicap_text, a, forgjofin);
			myTable.add(hole_par_text, a, parid);

			String hole_strokes = "";

			if (update) {
				Stroke stroke = (Stroke) str.get(Integer.toString(tee_id[b].getID()));

				if (stroke != null) {
					hole_strokes = String.valueOf(stroke.getStrokeCount());
				}
			}

			TextInput myScore = (TextInput) getStyledInterface(new TextInput("hole_" + String.valueOf(hole_number)));
			myScore.setLength(2);
			myScore.setMaxlength(2);
			myScore.keepStatusOnAction();
			if (hole_strokes != "") {
				if (Integer.parseInt(hole_strokes) > 0) {
					myScore.setValue(hole_strokes);
				}
				else {
					myScore.setValue("X");
				}
			}

			myTable.add(myScore, a, skorid);

			if (a == 10) {
				a = 1;
				numer = 7;
				lengdur = 8;
				forgjofin = 9;
				parid = 10;
				skorid = 11;
			}

			a++;
		}

		myTable.setColumnAlignment(11, "center");

		Text ut_length_text = getSmallBoldText(String.valueOf(ut_length));
		Text ut_par_text = getSmallBoldText(String.valueOf(ut_par));

		Text inn_length_text = getSmallBoldText(String.valueOf(inn_length));
		Text inn_par_text = getSmallBoldText(String.valueOf(inn_par));

		myTable.addText("", 12, 1);

		if (number_of_holes.equals("18") || (number_of_holes.equals("9") && start_hole.equals("1"))) {
			myTable.add(ut, 11, 1);
			myTable.add(ut_length_text, 11, 2);
			myTable.add(ut_par_text, 11, 3);
		}

		if (number_of_holes.equals("18")) {
			myTable.add(inn, 11, 7);
			myTable.add(inn_length_text, 11, 8);
			myTable.add(inn_par_text, 11, 9);
		}
		else if (number_of_holes.equals("9") && start_hole.equals("10")) {
			myTable.add(inn, 11, 1);
			myTable.add(inn_length_text, 11, 2);
			myTable.add(inn_par_text, 11, 3);
		}

		Text total_length_text = getSmallText(String.valueOf(total_length));
		Text total_par_text = getSmallText(String.valueOf(total_par));

		String litur = getTeeColor(Integer.parseInt(tee_number));

		myTable.setColumnAlignment(12, "center");
		myTable.setHorizontalZebraColored(getZebraColor2(), getZebraColor1());
		myTable.setRowColor(1, getHeaderColor());
		myTable.setRowColor(2, litur);
		myTable.addText("", 12, 1);
		myTable.addText("", 12, 2);
		myTable.addText("", 12, 3);
		myTable.addText("", 11, 4);
		myTable.addText("", 12, 4);

		if (haed == 12) {
			myTable.setRowColor(7, getHeaderColor());
			myTable.setRowColor(8, litur);
			myTable.addText("", 12, 8);
			myTable.addText("", 12, 9);
			myTable.addText("", 12, 10);
			myTable.addText("", 11, 10);
		}

		myTable.add(total, 12, haed - 5);
		myTable.add(total_length_text, 12, haed - 4);
		myTable.add(total_par_text, 12, haed - 3);
		myTable.addText("&nbsp;", 12, haed - 2);

		// Teikningu lokið (að mestu....)

		String[] difference = new String[18];
		int leikpunktar = leik + 36;
		double nyForgjof = 0.0;

		if (mode.equalsIgnoreCase(iwrb.getLocalizedString("handicap.calculate", "Calculate"))) { //Reikna
																																														 // forgjöf
			int punktar = leikpunktar / 18;
			int afgangur = leikpunktar % 18;
			int punktar2 = 0;
			int punktar3 = punktar + 1;
			int ut_skor = 0;
			int inn_skor = 0;
			int ut_punktar = 0;
			int inn_punktar = 0;
			int total_skor = 0;
			int heildarpunktar = 0;
			int heildarhogg = 0;
			int handicap2 = 0;
			int par2 = 0;
			int startHole = Integer.parseInt(start_hole);

			for (int c = startHole; c <= numbers; c++) {
				handicap2 = (int) tee_id[c - startHole].getHandicap();
				par2 = tee_id[c - startHole].getPar();

				String holanr = modinfo.getParameter("hole_" + c);

				if (holanr == null || holanr.equals("") || holanr.equals("0")) {
					holanr = "0";
					checker = false;
				}

				if (holanr.equals("x") || holanr.equals("X")) {
					holanr = "0";
					checker2 = false;
				}

				int strokes2 = Integer.parseInt(holanr);
				difference[c - 1] = String.valueOf(strokes2 - par2);

				if (strokes2 > 0) {
					if (handicap2 > afgangur) {
						punktar2 = par2 + punktar - strokes2;
					}

					if (handicap2 <= afgangur) {
						punktar2 = par2 + punktar3 - strokes2;
					}

					if (punktar2 < 0) {
						punktar2 = 0;
					}

					heildarpunktar += punktar2;

					myForm.add(new HiddenInput("point_hole_" + String.valueOf(c), String.valueOf(punktar2)));

					Text punktarText = getSmallText(punktar2 + "");

					if (c <= 9) {
						myTable.add(punktarText, c + 1, 6);
						ut_punktar += punktar2;
					}
					else if (c > 9 && number_of_holes.equals("9")) {
						myTable.add(punktarText, c - 8, 6);
						inn_punktar += punktar2;
					}
					else if (c > 9 && number_of_holes.equals("18")) {
						myTable.add(punktarText, c - 8, 12);
						inn_punktar += punktar2;
					}
				}

				if (c <= 9) {
					ut_skor += Integer.parseInt(holanr);
				}
				else if (c > 9) {
					inn_skor += Integer.parseInt(holanr);
				}
			}

			// Samtals skor út + inn
			total_skor = ut_skor + inn_skor;

			if (number_of_holes.equals("9")) {
				heildarpunktar += 18;
			}

			Text heildar_skor = getSmallBoldText(String.valueOf(total_skor));
			Text innSkor = getSmallText(inn_skor + "");
			Text innPunktar = getSmallText(inn_punktar + "");
			Text utSkor = getSmallText(ut_skor + "");
			Text utPunktar = getSmallText(ut_punktar + "");
			Text heildarPunktar = getSmallText(heildarpunktar + "");

			if ((start_hole.equals("1") && number_of_holes.equals("9")) || number_of_holes.equals("18")) {
				if (checker2 == true) {
					myTable.add(utSkor, 11, 5);
				}
				myTable.add(utPunktar, 11, 6);
			}

			if (start_hole.equals("1") && number_of_holes.equals("18")) {
				if (checker2 == true) {
					myTable.add(innSkor, 11, 11);
				}
				myTable.add(innPunktar, 11, 12);
			}
			else if (start_hole.equals("10")) {
				if (checker2 == true) {
					myTable.add(innSkor, 11, 5);
				}
				myTable.add(innPunktar, 11, 6);
			}

			if (checker2 == true) {
				myTable.add(heildar_skor, 12, haed - 1);
			}

			myTable.add(heildarPunktar, 12, haed);

			// Reikna nýja forgjöf
			String nyForgjof2 = UpdateHandicap.reiknaHandicap(memberID, grunn, heildarpunktar);
			nyForgjof2 = scaleDecimals(nyForgjof2, 1);

			Text ny_forgjof = getHeader(iwrb.getLocalizedString("handicap.new", "New") + " " + iwrb.getLocalizedString("handicap.handicap_lowercase", "handicap") + ": " + nyForgjof2);

			// Ný fjorgjöf sett í form til vistunar
			myForm.add(new HiddenInput("handicap", nyForgjof2));
			myForm.add(new HiddenInput("total_points", String.valueOf(heildarpunktar)));

			if (checker == true) {
				handicapTable.add(ny_forgjof, 3, 1);
			}
		} // Reikna

		if (tournament) {
			contentTable.setAlignment(1, 3, "right");

			Text timeText = getSmallText("Tími: ");

			TextInput hourInput = (TextInput) getStyledInterface(new TextInput("hour"));
			hourInput.setLength(2);
			hourInput.setMaxlength(2);
			hourInput.keepStatusOnAction();

			TextInput minuteInput = (TextInput) getStyledInterface(new TextInput("minute"));
			minuteInput.setLength(2);
			minuteInput.setMaxlength(2);
			minuteInput.keepStatusOnAction();

			if (isDate) {
				hourInput.setValue(scoreDate.getHour());
				minuteInput.setValue(scoreDate.getMinute());
			}

			contentTable.add(timeText, 1, 3);
			contentTable.add(hourInput, 1, 3);
			contentTable.add(getSmallText(": "), 1, 3);
			contentTable.add(minuteInput, 1, 3);
			contentTable.addBreak(1, 3);
		}

		if (stat.equals("1")) { // Tölfræði
			if (number_of_holes.equals("18")) {
				haed = 8;
			}
			else if (number_of_holes.equals("9")) {
				haed = 4;
			}

			Table statsTable = new Table();
			statsTable.setWidth("100%");
			statsTable.setBorder(0);
			statsTable.setColor("#FFFFFF");
			statsTable.setCellpadding(getCellpadding());
			statsTable.setCellspacing(0);
			statsTable.setColumnAlignment(11, "center");
			statsTable.setColumnAlignment(12, "center");
			statsTable.setColumnAlignment(13, "center");

			contentTable.add(statsTable, 1, 3);

			statsTable.add(hola, 1, 1);
			statsTable.add(fairway_text, 1, 2);
			statsTable.add(greens_text, 1, 3);
			statsTable.add(putts_text, 1, 4);
			statsTable.add(total, 12, haed - 3);
			statsTable.add(average, 13, haed - 3);

			if (number_of_holes.equals("18")) {
				statsTable.add(hola, 1, 5);
				statsTable.add(fairway_text, 1, 6);
				statsTable.add(greens_text, 1, 7);
				statsTable.add(putts_text, 1, 8);
				statsTable.addText("", 12, 1);
				statsTable.addText("", 13, 1);
			}

			if (number_of_holes.equals("18") || (number_of_holes.equals("9") && start_hole.equals("1"))) {
				statsTable.add(ut, 11, 1);
			}

			if (number_of_holes.equals("18")) {
				statsTable.add(inn, 11, 5);
			}
			else if (number_of_holes.equals("9") && start_hole.equals("10")) {
				statsTable.add(inn, 11, 1);
			}

			int startHole = Integer.parseInt(start_hole);
			for (int b = startHole; b <= numbers; b++) {
				int par_three = tee_id[b - startHole].getPar();
				String tee_stats = String.valueOf(tee_id[b - startHole].getID());

				Text hole_stats = getSmallHeader(String.valueOf(b));

				String fairway_score = "";
				String greens_score = "";
				String putt_score = "";

				if (putt_score == null) {
					putt_score = "";
				}
				CheckBox fairway = getCheckBox("fairway_" + b, "1");
				fairway.keepStatusOnAction();

				if (fairway_score.equals("1")) {
					fairway.setChecked(true);
				}

				CheckBox greens = getCheckBox("greens_" + b, "1");
				greens.keepStatusOnAction();

				if (greens_score.equals("1")) {
					greens.setChecked(true);
				}

				TextInput putts = (TextInput) getStyledInterface(new TextInput("putts_" + b, putt_score));
				putts.setLength(1);
				putts.setMaxlength(1);
				putts.keepStatusOnAction();

				if (b <= 9) {
					statsTable.add(hole_stats, b + 1, 1);
					if (par_three != 3) {
						statsTable.add(fairway, b + 1, 2);
					}
					statsTable.add(greens, b + 1, 3);
					statsTable.add(putts, b + 1, 4);
					statsTable.setColumnAlignment(b + 1, "center");
				}
				else if (b > 9 && haed >= 8) {
					statsTable.add(hole_stats, (b - 8), 5);
					if (par_three != 3) {
						statsTable.add(fairway, (b - 8), 6);
					}
					statsTable.add(greens, (b - 8), 7);
					statsTable.add(putts, (b - 8), 8);
				}
				else if (b > 9 && haed <= 4) {
					statsTable.add(hole_stats, (b - 8), 1);
					if (par_three != 3) {
						statsTable.add(fairway, (b - 8), 2);
					}
					statsTable.add(greens, (b - 8), 3);
					statsTable.add(putts, (b - 8), 4);
				}
			}

			statsTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
			statsTable.setRowColor(1, getHeaderColor());

			if (haed >= 8) {
				statsTable.setRowColor(5, getHeaderColor());
			}

			if (mode.equalsIgnoreCase(iwrb.getLocalizedString("handicap.calculate", "Calculate"))) { //Reikna
																																															 // tölfræði
				int ut_putt = 0;
				int inn_putt = 0;
				int inn_fairway = 0;
				int ut_fairway = 0;
				int inn_greens = 0;
				int ut_greens = 0;
				int heildar_putt = 0;
				int heildar_fairway = 0;
				int heildar_greens = 0;
				int parThrees = 0;

				for (int d = Integer.parseInt(start_hole); d <= numbers; d++) {
					// Púttin
					String putt_strokes = modinfo.getParameter("putts_" + d);
					int putts;

					if (tee_id[d - 1].getPar() == 3) {
						parThrees++;
					}

					if (putt_strokes == null || putt_strokes.equals("")) {
						putts = 0;
					}
					else {
						putts = Integer.parseInt(putt_strokes);

						heildar_putt += putts;

						if (d <= 9) {
							ut_putt += putts;
						}
						else {
							inn_putt += putts;
						}
					}
					// Púttin búin

					// Á braut
					String fairway_hit = modinfo.getParameter("fairway_" + d);

					if (fairway_hit == null) {
						fairway_hit = "";
					}
					else {
						heildar_fairway++;

						if (d <= 9) {
							ut_fairway++;
						}
						else {
							inn_fairway++;
						}
					}

					// Á braut búið

					// Á flöt
					String greens_hit = modinfo.getParameter("greens_" + d);

					if (greens_hit == null) {
						greens_hit = "";
					}
					else {
						heildar_greens++;

						if (d <= 9) {
							ut_greens++;
						}
						else {
							inn_greens++;
						}
					}
					// Á flöt búið
				}

				Text utFairwayText = getSmallText(ut_fairway + "/7");
				Text innFairwayText = getSmallText(inn_fairway + "/7");
				Text utGreensText = getSmallText(ut_greens + "/9");
				Text innGreensText = getSmallText(inn_greens + "/9");
				Text utPuttsText = getSmallText("" + ut_putt);
				Text innPuttsText = getSmallText("" + inn_putt);

				if (number_of_holes.equals("18") || (number_of_holes.equals("9") && start_hole.equals("1"))) {
					statsTable.add(utFairwayText, 11, 2);
					statsTable.add(utGreensText, 11, 3);
					statsTable.add(utPuttsText, 11, 4);
				}

				if (number_of_holes.equals("18")) {
					statsTable.add(innFairwayText, 11, 6);
					statsTable.add(innGreensText, 11, 7);
					statsTable.add(innPuttsText, 11, 8);
				}
				else if (number_of_holes.equals("9") && start_hole.equals("10")) {
					statsTable.add(innFairwayText, 11, 2);
					statsTable.add(innGreensText, 11, 3);
					statsTable.add(innPuttsText, 11, 4);
				}

				String fairway_holes = String.valueOf(Integer.parseInt(number_of_holes) - parThrees);

				Text totalFairwayText = getSmallText(heildar_fairway + "/" + fairway_holes);
				Text totalGreensText = getSmallText(heildar_greens + "/" + number_of_holes);
				Text totalPuttsText = getSmallText("" + heildar_putt);

				statsTable.add(totalFairwayText, 12, haed - 2);
				statsTable.add(totalGreensText, 12, haed - 1);
				statsTable.add(totalPuttsText, 12, haed);

				String medalfairway = String.valueOf((double) heildar_fairway / Integer.parseInt(fairway_holes) * 100);
				medalfairway = scaleDecimals(medalfairway, 2);

				String medalgreens = String.valueOf((double) heildar_greens / Integer.parseInt(number_of_holes) * 100);
				medalgreens = scaleDecimals(medalgreens, 2);

				String medalputts = String.valueOf((double) heildar_putt / Integer.parseInt(number_of_holes));
				medalputts = scaleDecimals(medalputts, 2);

				Text averageFairwayText = getSmallText(medalfairway + "%");
				Text averageGreensText = getSmallText(medalgreens + "%");
				Text averagePuttsText = getSmallText("" + medalputts);

				statsTable.add(averageFairwayText, 13, haed - 2);
				statsTable.add(averageGreensText, 13, haed - 1);
				statsTable.add(averagePuttsText, 13, haed);
			}
		} // Tölfræði búið
		else {
			myTable.setHeight(1, 3, "1");
			myTable.addText("", 1, 3);
		}

		if (tournament == false) {
			if (mode.equalsIgnoreCase(iwrb.getLocalizedString("handicap.calculate", "Calculate")) && checker == true) {
				SubmitButton saveSubmit = (SubmitButton) getButton(new SubmitButton("mode", iwrb.getLocalizedString("handicap.save", "Save")));
				saveSubmit.setOnSubmit("this.disabled=true;return true;");
				contentTable.add(saveSubmit, 1, 5);
			}
		}
		else {
			SubmitButton saveSubmit = (SubmitButton) getButton(new SubmitButton("mode", iwrb.getLocalizedString("handicap.save", "Save")));
			saveSubmit.setOnSubmit("this.disabled=true;return true;");
			contentTable.add(saveSubmit, 1, 5);
		}

		if (mode.equalsIgnoreCase(iwrb.getLocalizedString("handicap.save", "Save"))) {
			String new_handicap = modinfo.getParameter("handicap");
			if (new_handicap == null) {
				new_handicap = "0";
			}

			String total_points = modinfo.getParameter("total_points");
			if (total_points == null) {
				total_points = "0";
			}

			IWTimestamp stampur2 = new IWTimestamp();
			stampur2.getTimestampRightNow();
			IWTimestamp stampur = new IWTimestamp(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), stampur2.getHour(), stampur2.getMinute(), stampur2.getSecond());

			Scorecard scoreCard = (Scorecard) IDOLookup.createLegacy(Scorecard.class);
			int scorecardID = -666;

			if (update) {
				scoreCard = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(Integer.parseInt(scorecard_id));
				scoreCard.setTotalPoints(Integer.parseInt(total_points));
				if (tournament == true) {
					scoreCard.setUpdateHandicap(false);

					TournamentRound round = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
					stampur2 = new IWTimestamp(round.getRoundDate());
					if (modinfo.getParameter("hour") != null && modinfo.getParameter("minute") != null) {
						//stampur2.setHour(Integer.parseInt(request.getParameter("hour")));
						//stampur2.setMinute(Integer.parseInt(request.getParameter("minute")));
						stampur2.setHour(14);
						stampur2.setMinute(55);

					}
					scoreCard.setScorecardDate(stampur2.getTimestamp());
				}

				scoreCard.update();
				scorecardID = scoreCard.getID();
			}
			else {
				scoreCard.setMemberId(Integer.parseInt(member_id));
				scoreCard.setTournamentRoundId(Integer.parseInt(tournament_round_id));
				scoreCard.setScorecardDate(stampur.getTimestamp());
				scoreCard.setTotalPoints(Integer.parseInt(total_points));
				scoreCard.setHandicapBefore((float) grunn);
				scoreCard.setSlope((int) slope);
				scoreCard.setCourseRating((float) course_rating);
				scoreCard.setTeeColorID(tee_color_id);
				scoreCard.setFieldID(Integer.parseInt(field_id));
				scoreCard.setHandicapCorrection(false);
				scoreCard.setUpdateHandicap(true);

				scoreCard.insert();
				scorecardID = scoreCard.getID();
			}

			int teeNumber = 0;
			for (int hole_nr = Integer.parseInt(start_hole); hole_nr <= numbers; hole_nr++) {
				String hoggin = modinfo.getParameter("hole_" + String.valueOf(hole_nr));
				if (hoggin == null || hoggin.equals("") || hoggin.equalsIgnoreCase("x")) {
					hoggin = "0";
				}
				String punktar = modinfo.getParameter("point_hole_" + String.valueOf(hole_nr));
				if (punktar == null || punktar.equals("")) {
					punktar = "0";
				}
				String abraut = modinfo.getParameter("fairway_" + String.valueOf(hole_nr));
				if (abraut == null || abraut.equals("")) {
					abraut = "-1";
				}
				String aflot = modinfo.getParameter("greens_" + String.valueOf(hole_nr));
				if (aflot == null || aflot.equals("")) {
					aflot = "-1";
				}
				String puttin = modinfo.getParameter("putts_" + String.valueOf(hole_nr));
				if (puttin == null || puttin.equals("")) {
					puttin = "-1";
				}

				int tee_nr = tee_id[teeNumber].getID();
				int score_nr = 0;

				TransactionManager trans = IdegaTransactionManager.getInstance();
				try {
					trans.begin();
					if (update) {
						/*
						 * Stroke[] strokesID = (Stroke[]) (new
						 * Stroke()).findAllByColumn("scorecard_id",scorecard_id,"tee_id",String.valueOf(tee_nr));
						 */
						Stroke strokesID = (Stroke) str.get(Integer.toString(tee_nr));

						if (strokesID != null) {
							strokesID.setStrokeCount(Integer.parseInt(hoggin));
							strokesID.setPointCount(Integer.parseInt(punktar));

							strokesID.update();
						}
						else {
							Stroke strokeID = (Stroke) IDOLookup.createLegacy(Stroke.class);
							strokeID.setScorecardID(Integer.parseInt(scorecard_id));
							strokeID.setStrokeCount(Integer.parseInt(hoggin));
							strokeID.setPointCount(Integer.parseInt(punktar));
							strokeID.setTeeID(tee_nr);
							strokeID.setHolePar(tee_id[teeNumber].getPar());
							strokeID.setHoleHandicap((int) tee_id[teeNumber].getHandicap());

							if (tournament == false) {
								strokeID.insert();
							}
							else {
								if (Integer.parseInt(hoggin) > 0) {
									strokeID.insert();
								}
							}
						}

						Statistic[] statID = (Statistic[]) ((Statistic) IDOLookup.instanciateEntity(Statistic.class)).findAllByColumn("scorecard_id", scorecard_id, "tee_id", String.valueOf(tee_nr));

						if (statID.length > 0) {
							statID[statID.length - 1].setFairway(Integer.parseInt(abraut));
							statID[statID.length - 1].setGreens(Integer.parseInt(aflot));
							statID[statID.length - 1].setPutts(Integer.parseInt(puttin));
							statID[statID.length-1].update();
						}
						else {
							Statistic statNew = (Statistic) IDOLookup.createLegacy(Statistic.class);
							statNew.setScorecardID(Integer.parseInt(scorecard_id));
							statNew.setTeeID(tee_nr);
							statNew.setFairway(Integer.parseInt(abraut));
							statNew.setGreens(Integer.parseInt(aflot));
							statNew.setPutts(Integer.parseInt(puttin));
							statNew.insert();
						}
					}
					else {
						//Scorecard[] scoreCards = (Scorecard[]) (new
						// Scorecard()).findAllByColumnOrdered("member_id",member_id,"scorecard_id
						// desc");
						//score_nr = scoreCards[0].getID();

						Stroke strokeID = (Stroke) IDOLookup.createLegacy(Stroke.class);
						strokeID.setScorecardID(scorecardID);
						strokeID.setStrokeCount(Integer.parseInt(hoggin));
						strokeID.setPointCount(Integer.parseInt(punktar));
						strokeID.setTeeID(tee_nr);
						strokeID.setHolePar(tee_id[teeNumber].getPar());
						strokeID.setHoleHandicap((int) tee_id[teeNumber].getHandicap());
						strokeID.insert();

						if (stat.equals("1")) {
							Statistic statID = (Statistic) IDOLookup.createLegacy(Statistic.class);
							statID.setScorecardID(scorecardID);
							statID.setTeeID(tee_nr);
							statID.setFairway(Integer.parseInt(abraut));
							statID.setGreens(Integer.parseInt(aflot));
							statID.setPutts(Integer.parseInt(puttin));
							statID.insert();
						}
					}
					trans.commit();
				}
				catch (Exception e) {
					try {
						trans.rollback();
					}
					catch (SystemException se) {
						se.printStackTrace(System.err);
					}
					e.printStackTrace(System.err);
				}
				teeNumber++;
			}

			UpdateHandicap.update(Integer.parseInt(member_id), stampur);

			if(hasParentToReloadURL()) {
				getParentPage().setParentToReloadWithURL(getParentReloadURL(modinfo));
			} else {
				getParentPage().setParentToReload();
			}

			getParentPage().close();
		}

		add(myForm);

	}

	public String scaleDecimals(String nyForgjof, int scale) {
		BigDecimal decimal = new BigDecimal(nyForgjof);
		return decimal.setScale(scale, 5).toString();
	}

	public String getTeeColor(int teeColorID) {
		String litur = "";
		switch (teeColorID) {
			case 1 :
				litur = "FFFFFF";
				break;
			case 2 :
				litur = "FFFF67";
				break;
			case 3 :
				litur = "5757FF";
				break;
			case 4 :
				litur = "FF5757";
				break;
			case 5 :
				litur = "5757FF";
				break;
			case 6 :
				litur = "FF5757";
				break;
		}

		return litur;
	}
}