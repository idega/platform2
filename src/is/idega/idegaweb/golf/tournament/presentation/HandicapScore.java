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
import com.idega.presentation.Image;
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

			Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard s, tournament_round tr where s.tournament_round_id = tr.tournament_round_id and member_id = " + member_id + " and tournament_id = " + tournament_id + " and scorecard_date is not null order by round_number");
			int rounds = scorecard.length + 4;

			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(field_id);

			TournamentGroup group_id = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_id));
			TeeColor tee_color = group_id.getTeeColor();
			int tee_color_id = tee_color.getID();
			String litur = "";

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

			Text hola = new Text(iwrb.getLocalizedString("handicap.hole", "Hole"));
			hola.setBold();
			hola.setFontColor("FFFFFF");
			hola.setFontSize("1");
			Text lengd = new Text(iwrb.getLocalizedString("handicap.length", "Length"));
			lengd.setFontSize("1");
			Text par_texti = new Text(iwrb.getLocalizedString("handicap.par", "Par"));
			par_texti.setFontSize("1");
			Text strokeText = new Text(iwrb.getLocalizedString("handicap.strokes", "Strokes"));
			strokeText.setFontSize("1");
			Text pointText = new Text(iwrb.getLocalizedString("handicap.points", "Points"));
			pointText.setFontSize("1");
			Text differenceTexti = new Text(iwrb.getLocalizedString("handicap.differnce", "Difference"));
			differenceTexti.setFontSize("1");
			Text samtals = new Text(iwrb.getLocalizedString("handicap.total", "Total"));
			samtals.setBold();
			samtals.setFontColor("FFFFFF");
			samtals.setFontSize("1");

			Table statTable = new Table(rounds - 2, 13);
			statTable.setWidth("50%");
			statTable.setBorder(0);
			statTable.setColor("FFFFFF");
			statTable.setCellpadding(3);
			statTable.setCellspacing(1);
			statTable.mergeCells(1, 4, rounds - 2, 4);
			statTable.mergeCells(1, 10, rounds - 2, 10);
			statTable.setHorizontalZebraColored("99cc99", "8ab490");
			statTable.setRowColor(1, "336666");
			statTable.setRowColor(4, "FFFFFF");
			statTable.setRowColor(10, "FFFFFF");
			statTable.setAlignment("center");

			statTable.add(new Image("/pics/tournament/samtals.gif"), rounds - 2, 1);

			for (int a = 1; a <= rounds - 4; a++) {
				statTable.add(new Image("/pics/tournament/no" + a + ".gif"), a + 1, 1);
			}

			for (int a = 1; a <= statTable.getRows(); a++) {
				statTable.setRowAlignment(a, "center");
			}

			for (int a = 2; a <= myTable.getRows(); a++) {
				myTable.setRowAlignment(a, "center");
			}

			statTable.setColumnAlignment(1, "left");

			Text hogg_texti = new Text(iwrb.getLocalizedString("handicap.strokes", "Strokes"));
			hogg_texti.setFontSize("1");
			Text ernir_texti = new Text(iwrb.getLocalizedString("handicap.eagles", "Eagles"));
			ernir_texti.setFontSize("1");
			Text fuglar_texti = new Text(iwrb.getLocalizedString("handicap.birdies", "Birdies"));
			fuglar_texti.setFontSize("1");
			Text por_texti = new Text(iwrb.getLocalizedString("handicap.pars", "Pars"));
			por_texti.setFontSize("1");
			Text skollar_texti = new Text(iwrb.getLocalizedString("handicap.bogeys", "Bogeys"));
			skollar_texti.setFontSize("1");
			Text skrambar_texti = new Text(iwrb.getLocalizedString("handicap.double_bogeys", "Double bogeys"));
			skrambar_texti.setFontSize("1");
			Text abraut_texti = new Text("% " + iwrb.getLocalizedString("handicap.fairways", "Fairways"));
			abraut_texti.setFontSize("1");
			Text aflot_texti = new Text("% " + iwrb.getLocalizedString("handicap.gir", "GIR"));
			aflot_texti.setFontSize("1");
			Text putt_texti = new Text(iwrb.getLocalizedString("handicap.putts", "putts"));
			putt_texti.setFontSize("1");

			statTable.add(hogg_texti, 1, 2);
			statTable.add(par_texti, 1, 3);
			statTable.add(new Image("/pics/tournament/spot.gif", "spot", 10, 2), 1, 4);
			statTable.setRowColor(4, "336666");
			statTable.add(ernir_texti, 1, 5);
			statTable.add(fuglar_texti, 1, 6);
			statTable.add(por_texti, 1, 7);
			statTable.add(skollar_texti, 1, 8);
			statTable.add(skrambar_texti, 1, 9);
			statTable.add(new Image("/pics/tournament/spot.gif", "spot", 10, 2), 1, 10);
			statTable.setRowColor(10, "336666");
			statTable.add(abraut_texti, 1, 11);
			statTable.add(aflot_texti, 1, 12);
			statTable.add(putt_texti, 1, 13);

			myTable.add(hola, 1, 2);
			myTable.add(lengd, 1, 3);
			myTable.add(par_texti, 1, 4);
			myTable.add(samtals, 20, 2);

			Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));

			Text score_head = new Text(member.getName());
			score_head.setFontColor("000000");
			score_head.setBold();
			score_head.setFontSize("3");

			myTable.add(score_head, 1, 1);

			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumn("field_id", String.valueOf(field_id), "tee_color_id", String.valueOf(tee_color_id));

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
				Text hole = new Text(String.valueOf(a + 1));
				hole.setFontSize(1);
				hole.setFontColor("FFFFFF");

				int holelength = tee[a].getIntColumnValue("tee_length");

				total_length += holelength;

				Text hole_length = new Text(String.valueOf(holelength));
				hole_length.setFontSize(1);

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

				String color = "";
				if (b % 2 != 0) {
					color = "99cc99";
				}
				else {
					color = "8ab490";
				}

				myTable.setRowColor(row, color);
				myTable.setRowColor(row + 1, color);
				myTable.setRowColor(row + 2, color);
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

					Text hole_par = new Text(String.valueOf(par3));
					hole_par.setFontSize(1);
					if (b == 0) {
						myTable.add(hole_par, c + 2, 4);
						total_par += par3;
					}

					Text strokes_text = new Text(String.valueOf(strokes));
					strokes_text.setFontSize(1);
					strokes_text.setBold();
					strokes_text.setFontFace("Verdana,Arial,sans serif");
					Text pointsText = new Text(String.valueOf(points));
					pointsText.setFontSize(1);

					Text differenceText = new Text();
					if (gameDifference > 0) {
						differenceText.setText("+" + gameDifference);
					}
					else {
						differenceText.setText(gameDifference + "");
					}
					differenceText.setFontSize(1);

					myTable.add(strokes_text, c + 2, row);
					myTable.add(pointsText, c + 2, row + 1);
					myTable.add(differenceText, c + 2, row + 2);

					if (birdie >= 2) {
						myTable.setColor(c + 2, row, "51787E");
						dbogey_total++;
					}
					else if (birdie == 1) {
						myTable.setColor(c + 2, row, "AACFCF");
						bogey_total++;
					}
					else if (birdie == 0) {
						strokes_text.setBold();
						par_total++;
					}
					else if (birdie == -1) {
						myTable.setColor(c + 2, row, "F5D6CD");
						strokes_text.setBold();
						birdie_total++;
					}
					else if (birdie == -2) {
						myTable.setColor(c + 2, row, "F6A690");
						strokes_text.setBold();
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

				Text total_strokes_text = new Text(String.valueOf(total_strokes));
				total_strokes_text.setFontSize(1);
				total_strokes_text.setBold();
				total_strokes_text.setFontFace("Verdana,Arial,sans serif");
				Text totalPointsText = new Text(scorecard[b].getTotalPoints() + "");
				totalPointsText.setFontSize(1);
				Text difference_text = new Text(String.valueOf(difference));
				difference_text.setFontSize(1);
				Text eagle_text = new Text(String.valueOf(eagle_total));
				eagle_text.setFontSize(1);
				Text birdie_text = new Text(String.valueOf(birdie_total));
				birdie_text.setFontSize(1);
				Text par_text = new Text(String.valueOf(par_total));
				par_text.setFontSize(1);
				Text bogey_text = new Text(String.valueOf(bogey_total));
				bogey_text.setFontSize(1);
				Text dbogey_text = new Text(String.valueOf(dbogey_total));
				dbogey_text.setFontSize(1);
				Text fairway_text = new Text(String.valueOf(fairway));
				fairway_text.setFontSize(1);
				Text greens_text = new Text(String.valueOf(greens));
				greens_text.setFontSize(1);
				Text putts_text = new Text(String.valueOf(putts));
				putts_text.setFontSize(1);

				if (total_strokes != 0) {
					myTable.add(total_strokes_text, 20, row);
					myTable.add(totalPointsText, 20, row + 1);
					myTable.addText("", 20, row + 2);
					myTable.addText("", 1, row + 1);
					myTable.addText("", 1, row + 2);
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

			Text all_strokes_text = new Text(String.valueOf(all_strokes));
			all_strokes_text.setFontSize(1);
			Text all_difference_text = new Text(String.valueOf(all_difference));
			all_difference_text.setFontSize(1);
			Text all_eagle_text = new Text(String.valueOf(all_eagle));
			all_eagle_text.setFontSize(1);
			Text all_birdie_text = new Text(String.valueOf(all_birdie));
			all_birdie_text.setFontSize(1);
			Text all_par_text = new Text(String.valueOf(all_par));
			all_par_text.setFontSize(1);
			Text all_bogey_text = new Text(String.valueOf(all_bogey));
			all_bogey_text.setFontSize(1);
			Text all_dbogey_text = new Text(String.valueOf(all_dbogey));
			all_dbogey_text.setFontSize(1);
			Text all_fairway_text = new Text(String.valueOf(all_fairway));
			all_fairway_text.setFontSize(1);
			Text all_greens_text = new Text(String.valueOf(all_greens));
			all_greens_text.setFontSize(1);
			Text all_putts_text = new Text(String.valueOf(all_putts));
			all_putts_text.setFontSize(1);

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

			Text total_length_text = new Text(String.valueOf(total_length));
			total_length_text.setFontSize(1);
			Text total_par_text = new Text(String.valueOf(total_par));
			total_par_text.setFontSize(1);

			myTable.add(total_length_text, 20, 3);
			myTable.add(total_par_text, 20, 4);

			Handicap handicap = new Handicap(handicapBefore);
			gameHandicap = handicap.getLeikHandicap(slope, CR, (double) field.getFieldPar());

			Text handicapText = new Text("&nbsp;&nbsp;-&nbsp;&nbsp;");
			handicapText.setBold();
			handicapText.addToText(iwrb.getLocalizedString("handicap.handicap_abbrevation", "Hcp") + ". ");
			handicapText.addToText(Integer.toString(gameHandicap));

			myTable.add(handicapText, 1, 1);
			myTable.setBorder(0);
			myTable.setColor("FFFFFF");
			myTable.setAlignment("center");
			myTable.setCellpadding(3);
			myTable.setCellspacing(1);
			myTable.setHeight(1, "1");
			myTable.mergeCells(1, 1, 20, 1);
			for (int z = 1; z <= myTable.getRows(); z++) {
				myTable.setRowAlignment(z, "center");
			}
			myTable.setRowAlignment(1, "left");
			//myTable.setHorizontalZebraColored("99cc99","8ab490");
			myTable.setRowColor(1, "FFFFFF");
			myTable.setRowColor(2, "336666");
			myTable.setRowColor(3, litur);
			myTable.setRowColor(4, "8ab490");
			myTable.setWidth("600");

			for (int x = 1; x <= statTable.getColumns(); x++) {
				for (int y = 1; y <= statTable.getRows(); y++) {
					statTable.addText("", x, y);
				}
			}

			add("<br>");
			add(myTable);
			add("<br>");

			add(statTable);
			add("<br>");

			add("<center>");
			add(new CloseButton(iwrb.getImage("buttons/close.gif")));
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