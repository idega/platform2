/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Statistic;
import is.idega.idegaweb.golf.entity.Stroke;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.io.IOException;
import java.math.BigDecimal;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;

/**
 * @author gimmi
 */
public class HandicapScore extends GolfWindow {

	public HandicapScore() {
		setWidth(650);
		setHeight(650);
		add(new Score());
	}

	public class Score extends GolfBlock {

		public void main(IWContext modinfo) throws Exception {
			IWResourceBundle iwrb = getResourceBundle();

			getParentPage().setTitle(iwrb.getLocalizedString("handicap.tournament_scorecards", "Tournament scorecards"));
			addHeading(iwrb.getLocalizedString("handicap.tournament_scorecards", "Tournament scorecards"));

			String tournament_id = modinfo.getParameter("tournament_id");
			String member_id = modinfo.getParameter("member_id");
			String tournament_group_id = modinfo.getParameter("tournament_group_id");

			Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
			int field_id = tournament.getFieldId();
			int numberOfRounds = tournament.getNumberOfRounds();

			Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard s, tournament_round tr where s.tournament_round_id = tr.tournament_round_id and member_id = " + member_id + " and tournament_id = " + tournament_id + " and scorecard_date is not null order by round_number");
			int rounds = scorecard.length + 4;

			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(field_id);

			TournamentGroup group_id = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_id));
			TeeColor tee_color = group_id.getTeeColor();
			int tee_color_id = tee_color.getID();
			String litur = "";
			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumn("field_id", String.valueOf(field_id), "tee_color_id", String.valueOf(tee_color_id));

			switch (tee_color_id) {
				case 1:
					litur = "FFFFFF";
					break;

				case 2:
					litur = "FFFF67";
					break;

				case 3:
					litur = "5757FF";
					break;

				case 4:
					litur = "FF5757";
					break;

				case 5:
					litur = "5757FF";
					break;

				case 6:
					litur = "FF5757";
					break;
			}

			Table myTable = new Table();
			myTable.setRows((numberOfRounds * 3) + 4);
			myTable.setColumns(20);
			
			myTable.setBorder(0);
			myTable.setAlignment("center");
			myTable.setCellpadding(getCellpadding());
			myTable.setCellspacing(0);
			myTable.mergeCells(1, 1, 20, 1);
			for (int z = 1; z <= myTable.getRows(); z++) {
				myTable.setRowAlignment(z, "center");
			}
			myTable.setRowAlignment(1, "left");
			myTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
			
			myTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
			myTable.setRowColor(1, "FFFFFF");
			myTable.setRowColor(2, getHeaderColor());
			myTable.setRowColor(3, litur);
			myTable.setWidth("600");


			Text hola = getSmallHeader(iwrb.getLocalizedString("handicap.hole", "Hole"));
			Text lengd = getSmallBoldText(iwrb.getLocalizedString("handicap.length", "Length"));
			Text par_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.par", "Par"));
			Text strokeText = getSmallBoldText(iwrb.getLocalizedString("handicap.strokes", "Strokes"));
			Text pointText = getSmallBoldText(iwrb.getLocalizedString("handicap.points", "Points"));
			Text differenceTexti = getSmallBoldText(iwrb.getLocalizedString("handicap.differnce", "Difference"));
			Text samtals = getSmallHeader(iwrb.getLocalizedString("handicap.total", "Total"));

			Table statTable = new Table(rounds - 2, 11);
			statTable.setWidth("50%");
			statTable.setBorder(0);
			statTable.setColor("FFFFFF");
			statTable.setCellpadding(getCellpadding());
			statTable.setCellspacing(0);
			statTable.setHorizontalZebraColored(getZebraColor2(), getZebraColor1());
			statTable.setRowColor(1, getHeaderColor());
			statTable.setAlignment("center");
			statTable.setRows(11);
			int statRow = 1;

			statTable.add(getSmallHeader(iwrb.getLocalizedString("handicap.total", "Total")), rounds - 2, statRow);

			for (int a = 1; a <= rounds - 4; a++) {
				statTable.add(getSmallHeader(String.valueOf(a)), a + 1, statRow++);
			}

			for (int a = 1; a <= statTable.getRows(); a++) {
				statTable.setRowAlignment(a, "center");
			}

			statTable.setColumnAlignment(1, "left");

			Text hogg_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.strokes", "Strokes"));
			Text ernir_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.eagles", "Eagles"));
			Text fuglar_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.birdies", "Birdies"));
			Text por_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.pars", "Pars"));
			Text skollar_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.bogeys", "Bogeys"));
			Text skrambar_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.double_bogeys", "Double bogeys"));
			Text abraut_texti = getSmallBoldText("% " + iwrb.getLocalizedString("handicap.fairways", "Fairways"));
			Text aflot_texti = getSmallBoldText("% " + iwrb.getLocalizedString("handicap.gir", "GIR"));
			Text putt_texti = getSmallBoldText(iwrb.getLocalizedString("handicap.putts", "putts"));

			statTable.add(hogg_texti, 1, statRow++);
			statTable.add(par_texti, 1, statRow);
			statTable.setRowStyle(statRow++, "border-bottom", "1px " + this.getLineSeperatorColor() + " solid;");
			statTable.add(ernir_texti, 1, statRow++);
			statTable.add(fuglar_texti, 1, statRow++);
			statTable.add(por_texti, 1, statRow++);
			statTable.add(skollar_texti, 1, statRow++);
			statTable.add(skrambar_texti, 1, statRow);
			statTable.setRowStyle(statRow++, "border-bottom", "1px " + this.getLineSeperatorColor() + " solid;");
			statTable.add(abraut_texti, 1, statRow++);
			statTable.add(aflot_texti, 1, statRow++);
			statTable.add(putt_texti, 1, statRow++);

			myTable.add(hola, 1, 2);
			myTable.add(lengd, 1, 3);
			myTable.add(par_texti, 1, 4);
			myTable.add(samtals, 20, 2);

			int total_length = 0;
			int total_par = 0;
			int all_strokes = 0;
			int all_difference = 0;
			int all_eagle = 0;
			int all_birdie = 0;
			int all_par = 0;
			int all_bogey = 0;
			int all_dbogey = 0;
			double all_fairway = 0;
			double all_greens = 0;
			double all_putts = 0;
			int rounds_played = 0;

			for (int a = 0; a < tee.length; a++) {
				Text hole = getSmallHeader(String.valueOf(a + 1));

				int holelength = tee[a].getIntColumnValue("tee_length");
				total_length += holelength;

				Text hole_length = getSmallText(String.valueOf(holelength));

				myTable.add(hole, a + 2, 2);
				myTable.add(hole_length, a + 2, 3);
			}

			int total_strokes = 0;
			int eagle_total = 0;
			int birdie_total = 0;
			int par_total = 0;
			int bogey_total = 0;
			int dbogey_total = 0;
			double fairway = 0;
			double greens = 0;
			double putts = 0;
			int hole = 0;
			int hole2 = 0;
			int gameHandicap = 0;
			int gameDifference = 0;
			int row = 2;
			double slope = 0;
			double CR = 0;
			double handicapBefore = 0;
			int roundPar = 0;

			for (int b = 0; b < scorecard.length; b++) {
				row += 3;

				if ((b + 1) < scorecard.length) {
					myTable.setRowStyle(row + 2, "border-bottom", "1px " + this.getLineSeperatorColor() + " solid;");
				}
				myTable.add(strokeText, 1, row);
				myTable.add(pointText, 1, row + 1);
				myTable.add(differenceTexti, 1, row + 2);

				if (b == 0) {
					handicapBefore = (double) scorecard[b].getHandicapBefore();
				}
				if (b == 0) {
					slope = (double) scorecard[b].getSlope();
					CR = (double) scorecard[b].getCourseRating();
				}

				int scorecard_id = scorecard[b].getID();
				total_strokes = 0;
				total_par = 0;
				eagle_total = 0;
				birdie_total = 0;
				par_total = 0;
				bogey_total = 0;
				dbogey_total = 0;
				fairway = 0;
				greens = 0;
				putts = 0;
				hole = 0;
				hole2 = 0;
				roundPar = 0;

				Statistic[] stats = (Statistic[]) ((Statistic) IDOLookup.instanciateEntity(Statistic.class)).findAll("select statistic.* from statistic,tee where tee.tee_id = statistic.tee_id and scorecard_id = " + scorecard_id + " order by hole_number");

				Stroke[] stroke = (Stroke[]) ((Stroke) IDOLookup.instanciateEntity(Stroke.class)).findAll("select stroke.* from stroke,tee where tee.tee_id = stroke.tee_id and scorecard_id = " + scorecard_id + " order by hole_number");
				for (int c = 0; c < stroke.length; c++) {
					int tee_id2 = stroke[c].getTeeID();
					int strokes = stroke[c].getStrokeCount();
					int par3 = stroke[c].getHolePar();
					int points = stroke[c].getPointCount();
					roundPar += par3;

					if (par3 != 3) {
						hole++;
					}
					hole2++;

					int fair = 0;
					int green = 0;
					int putt = 0;

					if (stats != null && stats.length != 0) {
						try {
							fair = stats[c].getFairway();
							green = stats[c].getGreens();
							putt = stats[c].getPutts();
						}
						catch (Exception ex) {
							fair = 0;
							green = 0;
							putt = 0;
						}
					}

					if (fair != -1) {
						fairway += (double) fair;
					}
					if (green != -1) {
						greens += (double) green;
					}
					if (putt != -1) {
						putts += (double) putt;
					}

					int birdie = strokes - par3;

					total_strokes += strokes;
					gameDifference += (strokes - par3);

					Text hole_par = getSmallText(String.valueOf(par3));
					if (b == 0) {
						myTable.add(hole_par, c + 2, 4);
						total_par += par3;
					}

					Text strokes_text = getSmallText(String.valueOf(strokes));
					Text pointsText = getSmallText(String.valueOf(points));

					Text differenceText = getSmallText("");
					if (gameDifference > 0) {
						differenceText.setText("+" + gameDifference);
					}
					else {
						differenceText.setText(gameDifference + "");
					}

					myTable.add(strokes_text, c + 2, row);
					myTable.add(pointsText, c + 2, row + 1);
					myTable.add(differenceText, c + 2, row + 2);

					if (birdie >= 2) {
						myTable.setColor(c + 2, row, "#51787E");
						dbogey_total++;
					}
					else if (birdie == 1) {
						myTable.setColor(c + 2, row, "#AACFCF");
						bogey_total++;
					}
					else if (birdie == 0) {
						par_total++;
					}
					else if (birdie == -1) {
						myTable.setColor(c + 2, row, "#F5D6CD");
						birdie_total++;
					}
					else if (birdie == -2) {
						myTable.setColor(c + 2, row, "#F6A690");
						eagle_total++;
					}
				}

				int difference = 0;
				if (fairway != 0) {
					fairway = scale_decimals((fairway / hole * 100), 2);
					all_fairway += fairway;
				}

				if (greens != 0) {
					greens = scale_decimals((greens / hole2 * 100), 2);
					all_greens += greens;
				}

				if (putts != 0) {
					putts = scale_decimals((putts / hole2), 2);
					all_putts += putts;
				}

				if (total_strokes != 0) {
					difference = total_strokes - roundPar;
					all_strokes += total_strokes;
					all_difference += difference;
					all_eagle += eagle_total;
					all_birdie += birdie_total;
					all_par += par_total;
					all_bogey += bogey_total;
					all_dbogey += dbogey_total;
					rounds_played++;
				}

				Text total_strokes_text = getSmallBoldText(String.valueOf(total_strokes));
				Text totalPointsText = getSmallText(scorecard[b].getTotalPoints() + "");
				Text difference_text = getSmallText(String.valueOf(difference));
				Text eagle_text = getSmallText(String.valueOf(eagle_total));
				Text birdie_text = getSmallText(String.valueOf(birdie_total));
				Text par_text = getSmallText(String.valueOf(par_total));
				Text bogey_text = getSmallText(String.valueOf(bogey_total));
				Text dbogey_text = getSmallText(String.valueOf(dbogey_total));
				Text fairway_text = getSmallText(String.valueOf(fairway));
				Text greens_text = getSmallText(String.valueOf(greens));
				Text putts_text = getSmallText(String.valueOf(putts));

				if (total_strokes != 0) {
					myTable.add(total_strokes_text, 20, row);
					myTable.add(totalPointsText, 20, row + 1);
					statTable.add(total_strokes_text, b + 2, 2);
					statTable.add(difference_text, b + 2, 3);

					if (eagle_total != 0) {
						statTable.add(eagle_text, b + 2, 5);
					}
					if (birdie_total != 0) {
						statTable.add(birdie_text, b + 2, 6);
					}
					if (par_total != 0) {
						statTable.add(par_text, b + 2, 7);
					}
					if (bogey_total != 0) {
						statTable.add(bogey_text, b + 2, 8);
					}
					if (dbogey_total != 0) {
						statTable.add(dbogey_text, b + 2, 9);
					}
					if (fairway != 0) {
						statTable.add(fairway_text, b + 2, 11);
					}
					if (greens != 0) {
						statTable.add(greens_text, b + 2, 12);
					}
					if (putts != 0) {
						statTable.add(putts_text, b + 2, 13);
					}
				}

			}

			if (all_fairway != 0) {
				all_fairway = scale_decimals((all_fairway / rounds_played), 2);
			}

			if (all_greens != 0) {
				all_greens = scale_decimals((all_greens / rounds_played), 2);
			}

			if (all_putts != 0) {
				all_putts = scale_decimals((all_putts / rounds_played), 2);
			}

			Text all_strokes_text = getSmallText(String.valueOf(all_strokes));
			Text all_difference_text = getSmallText(String.valueOf(all_difference));
			Text all_eagle_text = getSmallText(String.valueOf(all_eagle));
			Text all_birdie_text = getSmallText(String.valueOf(all_birdie));
			Text all_par_text = getSmallText(String.valueOf(all_par));
			Text all_bogey_text = getSmallText(String.valueOf(all_bogey));
			Text all_dbogey_text = getSmallText(String.valueOf(all_dbogey));
			Text all_fairway_text = getSmallText(String.valueOf(all_fairway));
			Text all_greens_text = getSmallText(String.valueOf(all_greens));
			Text all_putts_text = getSmallText(String.valueOf(all_putts));

			if (all_strokes != 0) {
				statTable.add(all_strokes_text, rounds - 2, 2);
			}

			statTable.add(all_difference_text, rounds - 2, 3);

			if (all_eagle != 0) {
				statTable.add(all_eagle_text, rounds - 2, 5);
			}
			if (all_birdie != 0) {
				statTable.add(all_birdie_text, rounds - 2, 6);
			}
			if (all_par != 0) {
				statTable.add(all_par_text, rounds - 2, 7);
			}
			if (all_bogey != 0) {
				statTable.add(all_bogey_text, rounds - 2, 8);
			}
			if (all_dbogey != 0) {
				statTable.add(all_dbogey_text, rounds - 2, 9);
			}
			if (all_fairway != 0) {
				statTable.add(all_fairway_text, rounds - 2, 11);
			}
			if (all_greens != 0) {
				statTable.add(all_greens_text, rounds - 2, 12);
			}
			if (all_putts != 0) {
				statTable.add(all_putts_text, rounds - 2, 13);
			}

			String out_differ = "";

			if (all_difference == 0) {
				out_differ = "E";
			}
			else if (all_difference > 0) {
				out_differ = "+" + String.valueOf(all_difference);
			}
			else if (all_difference < 0) {
				out_differ = String.valueOf(all_difference);
			}

			Text total_length_text = getSmallText(String.valueOf(total_length));
			Text total_par_text = getSmallText(String.valueOf(total_par));

			myTable.add(total_length_text, 20, 3);
			myTable.add(total_par_text, 20, 4);

			Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));
			Text score_head = getHeader(member.getName());
			myTable.add(score_head, 1, 1);

			Handicap handicap = new Handicap(handicapBefore);
			gameHandicap = handicap.getLeikHandicap(slope, CR, (double) field.getFieldPar());

			Text handicapText = getHeader("&nbsp;&nbsp;-&nbsp;&nbsp;");
			handicapText.addToText(iwrb.getLocalizedString("handicap.handicap_abbrevation", "Hcp"));
			handicapText.addToText(Integer.toString(gameHandicap));
			myTable.add(handicapText, 1, 1);

			add("<br>");
			add(myTable);
			add("<br>");

			add(statTable);
			add("<br>");

			add("<center>");
			add(new CloseButton(iwrb.getLocalizedString("close", "Close")));
			add("</center>");

		}

		public double scale_decimals(double tala, int scale) throws IOException {

			String nyForgjof = String.valueOf(tala);

			BigDecimal test2 = new BigDecimal(nyForgjof);

			double nyForgjof2 = test2.setScale(scale, 5).doubleValue();

			return nyForgjof2;

		}
	}
}