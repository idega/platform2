/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import java.math.BigDecimal;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.ScorecardHome;
import is.idega.idegaweb.golf.entity.Statistic;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

/**
 * @author laddi
 */
public class HandicapStatistics extends GolfWindow {
	
	public HandicapStatistics() {
		setWidth(600);
		setHeight(380);
		setTitle("Edit statistics");
		add(new Statistics());
	}

	public class Statistics extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();

		getParentPage().setTitle(iwrb.getLocalizedString("handicap.register_statistics", "Register statistics"));
		addHeading(iwrb.getLocalizedString("handicap.register_statistics", "Register statistics"));

		String mode = modinfo.getParameter("mode");
		if (mode == null) {
			mode = "";
		}

		String scorecard_id = modinfo.getParameter("scorecard_id");
		if (scorecard_id == null) {
			scorecard_id = "???";
		}

		Scorecard scorecard = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(Integer.parseInt(scorecard_id));
		Tee[] tee_id = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("SELECT * from tee where field_id='" + scorecard.getFieldID() + "' and tee_color_id='" + scorecard.getTeeColorID() + "' order by hole_number");
		Statistic[] stats = (Statistic[]) ((Statistic) IDOLookup.instanciateEntity(Statistic.class)).findAll("select statistic.* from statistic,tee where statistic.tee_id = tee.tee_id and scorecard_id = " + scorecard_id + " order by hole_number");

		if (mode.equalsIgnoreCase("")) {

			String action = modinfo.getParameter("action");
			if (action == null) {
				action = "";
			}

			Tournament tournament = scorecard.getTournamentRound().getTournament();

			Text tournamentName = getHeader(tournament.getName() + " - " + scorecard.getTournamentRound().getRoundNumber() + ". hringur");

			Form myForm = new Form();

			Table contentTable = new Table();
			contentTable.setWidth("100%");
			contentTable.setCellpadding(3);
			contentTable.setCellspacing(3);

			contentTable.add(tournamentName, 1, 1);

			myForm.add(new HiddenInput("scorecard_id", scorecard_id));
			SubmitButton submit = new SubmitButton("mode", iwrb.getLocalizedString("handicap.calculate", "Calculate"));

			Text hola = getSmallHeader("&nbsp;" + iwrb.getLocalizedString("handicap.hole", "Hole"));
			Text ut = getSmallHeader(iwrb.getLocalizedString("handicap.out", "Out"));
			Text inn = getSmallHeader(iwrb.getLocalizedString("handicap.in", "In"));
			Text total = getSmallHeader(iwrb.getLocalizedString("handicap.total", "Total"));
			Text average = getSmallHeader(iwrb.getLocalizedString("handicap.average", "Average"));
			Text fairway_text = getSmallBoldText("&nbsp;" + iwrb.getLocalizedString("handicap.fairway", "Fairway"));
			Text greens_text = getSmallBoldText("&nbsp;" + iwrb.getLocalizedString("handicap.gir", "GIR"));
			Text putts_text = getSmallBoldText("&nbsp;" + iwrb.getLocalizedString("handicap.putts", "Putts"));

			Table statsTable = new Table();
			statsTable.setWidth("100%");
			statsTable.setBorder(0);
			statsTable.setColor("#FFFFFF");
			statsTable.setCellpadding(getCellpadding());
			statsTable.setCellspacing(0);

			contentTable.add(statsTable, 1, 2);

			statsTable.add(hola, 1, 1);
			statsTable.add(fairway_text, 1, 2);
			statsTable.add(greens_text, 1, 3);
			statsTable.add(putts_text, 1, 4);
			statsTable.add(total, 12, 5);
			statsTable.add(average, 13, 5);

			statsTable.add(hola, 1, 5);
			statsTable.add(fairway_text, 1, 6);
			statsTable.add(greens_text, 1, 7);
			statsTable.add(putts_text, 1, 8);
			statsTable.addText("", 12, 1);
			statsTable.addText("", 13, 1);
			statsTable.add(ut, 11, 1);
			statsTable.add(inn, 11, 5);

			for (int b = 1; b <= 18; b++) {

				int par_three = tee_id[b - 1].getPar();
				String tee_stats = String.valueOf(tee_id[b - 1].getID());

				Text hole_stats = getSmallHeader(String.valueOf(b));

				int fairway_score = 0;
				int greens_score = 0;
				int putt_score = 0;

				if (stats.length >= b) {
				}

				CheckBox fairway = getCheckBox("fairway_" + b, "1");
				fairway.keepStatusOnAction();

				if (fairway_score == 1) {
					fairway.setChecked(true);
				}

				CheckBox greens = getCheckBox("greens_" + b, "1");
				greens.keepStatusOnAction();

				if (greens_score == 1) {
					greens.setChecked(true);
				}

				TextInput putts = (TextInput) getStyledInterface(new TextInput("putts_" + b));
				putts.setLength(1);
				putts.setMaxlength(1);
				putts.keepStatusOnAction();
				if (putt_score > 0) {
					putts.setValue(putt_score + "");
				}

				if (b <= 9) {
					statsTable.add(hole_stats, b + 1, 1);
					if (par_three != 3) {
						statsTable.add(fairway, b + 1, 2);
					}
					statsTable.add(greens, b + 1, 3);
					statsTable.add(putts, b + 1, 4);
					statsTable.setColumnAlignment(b + 1, "center");
				}

				if (b > 9) {
					statsTable.add(hole_stats, (b - 8), 5);
					if (par_three != 3) {
						statsTable.add(fairway, (b - 8), 6);
					}
					statsTable.add(greens, (b - 8), 7);
					statsTable.add(putts, (b - 8), 8);
				}

			}

			statsTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
			statsTable.setRowColor(1, getHeaderColor());
			statsTable.setRowColor(5, getHeaderColor());

			if (action.equalsIgnoreCase(iwrb.getLocalizedString("handicap.calculate", "Calculate"))) { //Reikna
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

				for (int d = 1; d <= 18; d++) {

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
				utFairwayText.setFontSize(1);
				Text innFairwayText = getSmallText(inn_fairway + "/7");
				innFairwayText.setFontSize(1);
				Text utGreensText = getSmallText(ut_greens + "/9");
				utGreensText.setFontSize(1);
				Text innGreensText = getSmallText(inn_greens + "/9");
				innGreensText.setFontSize(1);
				Text utPuttsText = getSmallText("" + ut_putt);
				utPuttsText.setFontSize(1);
				Text innPuttsText = getSmallText("" + inn_putt);
				innPuttsText.setFontSize(1);

				statsTable.add(utFairwayText, 11, 2);
				statsTable.add(utGreensText, 11, 3);
				statsTable.add(utPuttsText, 11, 4);

				statsTable.add(innFairwayText, 11, 6);
				statsTable.add(innGreensText, 11, 7);
				statsTable.add(innPuttsText, 11, 8);

				String fairway_holes = String.valueOf((18 - parThrees));

				Text totalFairwayText = getSmallText(heildar_fairway + "/" + fairway_holes);
				totalFairwayText.setFontSize(1);
				Text totalGreensText = getSmallText(heildar_greens + "/" + 18);
				totalGreensText.setFontSize(1);
				Text totalPuttsText = getSmallText("" + heildar_putt);
				totalPuttsText.setFontSize(1);

				statsTable.add(totalFairwayText, 12, 6);
				statsTable.add(totalGreensText, 12, 7);
				statsTable.add(totalPuttsText, 12, 8);

				String medalfairway = String.valueOf((double) heildar_fairway / Integer.parseInt(fairway_holes) * 100);
				medalfairway = scaleDecimals(medalfairway, 2);

				String medalgreens = String.valueOf((double) heildar_greens / 18 * 100);
				medalgreens = scaleDecimals(medalgreens, 2);

				String medalputts = String.valueOf((double) heildar_putt / 18);
				medalputts = scaleDecimals(medalputts, 2);

				Text averageFairwayText = getSmallText(medalfairway + "%");
				averageFairwayText.setFontSize(1);
				Text averageGreensText = getSmallText(medalgreens + "%");
				averageGreensText.setFontSize(1);
				Text averagePuttsText = getSmallText("" + medalputts);
				averagePuttsText.setFontSize(1);

				statsTable.add(averageFairwayText, 13, 6);
				statsTable.add(averageGreensText, 13, 7);
				statsTable.add(averagePuttsText, 13, 8);

			}
			statsTable.setColumnAlignment(11, "center");
			statsTable.setColumnAlignment(12, "center");
			statsTable.setColumnAlignment(13, "center");

			contentTable.add(getButton(new SubmitButton("action", iwrb.getLocalizedString("handicap.calculate", "Calculate"))), 1, 3);
			contentTable.add(getButton(new SubmitButton("mode", iwrb.getLocalizedString("handicap.save", "Save"))), 1, 3);
			myForm.add(contentTable);

			add(myForm);

		}

		if (mode.equalsIgnoreCase(iwrb.getLocalizedString("handicap.save", "Save"))) {

			for (int hole_nr = 1; hole_nr <= 18; hole_nr++) {
				String abraut = modinfo.getParameter("fairway_" + String.valueOf(hole_nr));
				if (abraut == null) {
					abraut = "-1";
				}
				String aflot = modinfo.getParameter("greens_" + String.valueOf(hole_nr));
				if (aflot == null) {
					aflot = "-1";
				}
				String puttin = modinfo.getParameter("putts_" + String.valueOf(hole_nr));
				if (puttin == null || puttin.equals("")) {
					puttin = "-1";
				}

				int tee_nr = tee_id[hole_nr - 1].getID();

				Statistic[] statID = (Statistic[]) ((Statistic) IDOLookup.instanciateEntity(Statistic.class)).findAllByColumn("scorecard_id", scorecard_id, "tee_id", String.valueOf(tee_nr));

				if (statID.length > 0) {

					statID[statID.length - 1].setFairway(Integer.parseInt(abraut));
					statID[statID.length - 1].setGreens(Integer.parseInt(aflot));
					statID[statID.length - 1].setPutts(Integer.parseInt(puttin));

					statID[statID.length - 1].update();
				}

				else {

					Statistic statNew = (Statistic) IDOLookup.createLegacy(Statistic.class);
					statNew.setScorecardID(scorecard.getID());
					statNew.setTeeID(tee_nr);
					statNew.setFairway(Integer.parseInt(abraut));
					statNew.setGreens(Integer.parseInt(aflot));
					statNew.setPutts(Integer.parseInt(puttin));

					statNew.insert();

				}
			}
			getParentPage().close();
		}
	}

	public String scaleDecimals(String handicap, int scale) {
		BigDecimal decimal = new BigDecimal(handicap);
		return decimal.setScale(scale, 5).toString();
	}
	}
}