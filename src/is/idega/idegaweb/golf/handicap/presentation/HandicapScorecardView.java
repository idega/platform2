/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import java.math.BigDecimal;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.ScorecardHome;
import is.idega.idegaweb.golf.entity.Statistic;
import is.idega.idegaweb.golf.entity.Stroke;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeHome;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class HandicapScorecardView extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();

		getParentPage().setTitle(iwrb.getLocalizedString("handicap.scorecard", "Scorecard"));

		String scorecard_id = modinfo.getParameter("scorecard_id");
		boolean x = true;

		Table outerTable = new Table();
		outerTable.setAlignment("center");
		outerTable.setAlignment(1, 3, "center");
		outerTable.setCellpadding(4);

		Table myTable = new Table();
		myTable.setBorder(0);
		myTable.setColor("FFFFFF");
		myTable.setAlignment("center");
		myTable.setCellpadding(3);
		myTable.setCellspacing(1);
		myTable.setColumnAlignment(1, "right");

		Text hola = new Text(iwrb.getLocalizedString("handicap.hole", "Hole"));
		hola.setBold();
		hola.setFontColor("FFFFFF");
		hola.setFontSize("1");
		Text lengd = new Text(iwrb.getLocalizedString("handicap.length", "Length"));
		lengd.setFontSize("1");
		Text par_texti = new Text(iwrb.getLocalizedString("handicap.par", "Par"));
		par_texti.setFontSize("1");
		Text forgjof = new Text(iwrb.getLocalizedString("handicap.handicap", "Handicap"));
		forgjof.setFontSize("1");
		Text hogg = new Text(iwrb.getLocalizedString("handicap.strokes", "Strokes"));
		hogg.setBold();
		hogg.setFontSize("1");
		Text punktar = new Text(iwrb.getLocalizedString("handicap.points", "Points"));
		punktar.setBold();
		punktar.setFontSize("1");
		Text mismunur = new Text(iwrb.getLocalizedString("handicap.difference", "Difference"));
		mismunur.setBold();
		mismunur.setFontSize("1");
		Text samtals = new Text(iwrb.getLocalizedString("handicap.total", "Total"));
		samtals.setBold();
		samtals.setFontColor("FFFFFF");
		samtals.setFontSize("1");

		myTable.add(hola, 1, 2);
		myTable.add(lengd, 1, 3);
		myTable.add(forgjof, 1, 4);
		myTable.add(par_texti, 1, 5);
		myTable.add(hogg, 1, 6);
		myTable.add(punktar, 1, 7);
		myTable.add(mismunur, 1, 8);

		Stroke[] stroke = (Stroke[]) ((Stroke) IDOLookup.instanciateEntity(Stroke.class)).findAll("select stroke.* from stroke,tee where tee.tee_id = stroke.tee_id and scorecard_id = " + scorecard_id + " order by hole_number");

		Scorecard scoreCard = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(Integer.parseInt(scorecard_id));
		IWTimestamp scoreTime = new IWTimestamp(scoreCard.getScorecardDate());

		Text scoreDate = new Text(" - " + scoreTime.getLocaleDate(modinfo.getCurrentLocale()));
		scoreDate.setBold();
		scoreDate.setFontSize(3);

		int total_length = 0;
		int total_par = 0;
		int total_strokes = 0;
		int total_points = 0;
		int difference = 0;
		String out_differ = "";
		int albatros_total = 0;
		int eagle_total = 0;
		int birdie_total = 0;
		int par_total = 0;
		int bogey_total = 0;
		int dbogey_total = 0;
		String litur = "";

		for (int a = 0; a < stroke.length; a++) {

			Tee tee = ((TeeHome) IDOLookup.getHomeLegacy(Tee.class)).findByPrimaryKey(stroke[a].getTeeID());

			int tee_litur = tee.getIntColumnValue("tee_color_id");
			switch (tee_litur) {
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

			myTable.setColumnAlignment(a + 2, "center");

			int hole_number = tee.getIntColumnValue("hole_number");

			int length = tee.getIntColumnValue("tee_length");
			total_length += length;

			int par = tee.getIntColumnValue("par");
			total_par += par;

			int handicap = (int) tee.getHandicap();

			int strokes = stroke[a].getStrokeCount();
			String strokes2 = String.valueOf(strokes);

			if (strokes == 0) {
				strokes2 = "X";
				x = false;
			}

			difference += strokes - par;
			String differ = "";

			if (difference > 0) {
				out_differ = "+" + String.valueOf(difference);
			}
			else if (difference == 0) {
				out_differ = "E";
			}
			else if (difference < 0) {
				out_differ = String.valueOf(difference);
			}

			Text prenta = new Text(out_differ);
			prenta.setFontSize("1");

			myTable.add(prenta, a + 2, 8);

			total_strokes += strokes;

			int birdie = strokes - par;

			if (birdie >= 2) {
				dbogey_total++;
			}
			else if (birdie == 1) {
				bogey_total++;
			}
			else if (birdie == 0) {
				par_total++;
			}
			else if (birdie == -1) {
				birdie_total++;
			}
			else if (birdie == -2) {
				eagle_total++;
			}

			int points = stroke[a].getIntColumnValue("point_count");
			total_points += points;

			Text hole_text = new Text(String.valueOf(hole_number));
			hole_text.setFontColor("FFFFFF");
			hole_text.setBold();
			hole_text.setFontSize("1");

			Text length_text = new Text(String.valueOf(length));
			length_text.setFontSize("1");
			Text par_text = new Text(String.valueOf(par));
			par_text.setFontSize("1");
			Text handicap_text = new Text(String.valueOf(handicap));
			handicap_text.setFontSize("1");

			Text strokes_text = new Text(strokes2);
			strokes_text.setFontSize(1);

			Text points_text = new Text(String.valueOf(points));
			points_text.setFontSize(1);

			myTable.add(hole_text, a + 2, 2);
			myTable.add(length_text, a + 2, 3);
			myTable.add(handicap_text, a + 2, 4);
			myTable.add(par_text, a + 2, 5);
			myTable.add(strokes_text, a + 2, 6);
			if (points != 0) {
				myTable.add(points_text, a + 2, 7);
			}
			else {
				myTable.addText("", a + 2, 7);
			}

			if ((a + 1) == stroke.length) {

				Tee teeID = ((TeeHome) IDOLookup.getHomeLegacy(Tee.class)).findByPrimaryKey(stroke[a].getTeeID());
				Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(teeID.getIntColumnValue("field_id"));

				Text fieldName = new Text(field.getName());
				fieldName.setBold();
				fieldName.setFontSize(3);

				myTable.add(fieldName, 1, 1);
				myTable.add(scoreDate, 1, 1);

				myTable.setColumnAlignment(a + 3, "center");

				Text total_length_text = new Text(String.valueOf(total_length));
				total_length_text.setFontSize(1);

				Text total_par_text = new Text(String.valueOf(total_par));
				total_par_text.setFontSize(1);

				Text total_strokes_text = new Text(String.valueOf(total_strokes));
				total_strokes_text.setFontSize(1);
				total_strokes_text.setBold();

				Text total_points_text = new Text(String.valueOf(total_points));
				total_points_text.setBold();
				total_points_text.setFontSize(1);

				myTable.add(total_length_text, a + 3, 3);
				myTable.addText("", a + 3, 4);
				myTable.add(total_par_text, a + 3, 5);
				if (x == true) {
					myTable.add(total_strokes_text, a + 3, 6);
				}
				if (total_points != 0) {
					myTable.add(total_points_text, a + 3, 7);
				}

				Text prenta2 = new Text(out_differ);
				prenta2.setFontSize("1");

				myTable.add(prenta2, a + 3, 8);

				myTable.add(samtals, a + 3, 2);

			}

		}

		myTable.setHorizontalZebraColored("99cc99", "8ab490");
		myTable.setRowColor(1, "FFFFFF");
		myTable.setRowColor(2, "336666");
		myTable.setRowColor(3, litur);
		myTable.setRowVerticalAlignment(1, "bottom");
		//myTable.setWidth("100%");
		myTable.mergeCells(1, 1, myTable.getColumns(), 1);
		myTable.setAlignment(1, 1, "left");

		outerTable.add(myTable, 1, 1);

		Table infoTable = new Table(3, 10);
		infoTable.setWidth("250");
		infoTable.setAlignment("center");
		infoTable.setColor("FFFFFF");
		infoTable.setCellpadding(3);
		infoTable.setCellspacing(1);
		infoTable.setHorizontalZebraColored("8ab490", "99cc99");
		infoTable.setRowColor(1, "336666");
		infoTable.setRowColor(7, "336666");

		Text info_head = new Text(iwrb.getLocalizedString("handicap.statistics", "Statistics"));
		info_head.setFontColor("FFFFFF");
		info_head.setFontSize(1);
		info_head.setBold();

		Text samtalsText = new Text(iwrb.getLocalizedString("handicap.total", "Total"));
		samtalsText.setFontSize(1);
		samtalsText.setFontColor("FFFFFF");

		Text fjoldiText = new Text(iwrb.getLocalizedString("handicap.count", "Count"));
		fjoldiText.setFontSize(1);
		fjoldiText.setFontColor("FFFFFF");

		infoTable.add(info_head, 1, 1);
		infoTable.add(samtalsText, 3, 1);
		infoTable.add(fjoldiText, 2, 1);

		Text ernir_text = new Text(" " + iwrb.getLocalizedString("handicap.eagles", "Eagles"));
		ernir_text.setFontSize("1");
		Text fuglar_text = new Text(" " + iwrb.getLocalizedString("handicap.birdies", "Birdies"));
		fuglar_text.setFontSize("1");
		Text por_text = new Text(" " + iwrb.getLocalizedString("handicap.pars", "Pars"));
		por_text.setFontSize("1");
		Text skollar_text = new Text(" " + iwrb.getLocalizedString("handicap.bogeys", "Bogeys"));
		skollar_text.setFontSize("1");
		Text skrambar_text = new Text(" " + iwrb.getLocalizedString("handicap.double_bogeys", "Double bogeys") + " +");
		skrambar_text.setFontSize("1");
		Text aBrautText = new Text(" " + iwrb.getLocalizedString("handicap.fairways", "Fairways"));
		aBrautText.setFontSize(1);
		Text aFlotText = new Text(" " + iwrb.getLocalizedString("handicap.greens_in_regulation", "Greens in regulation"));
		aFlotText.setFontSize(1);
		Text puttText = new Text(" " + iwrb.getLocalizedString("handicap.putts", "Putts"));
		puttText.setFontSize(1);

		infoTable.add(ernir_text, 1, 2);
		infoTable.add(fuglar_text, 1, 3);
		infoTable.add(por_text, 1, 4);
		infoTable.add(skollar_text, 1, 5);
		infoTable.add(skrambar_text, 1, 6);
		infoTable.add(aBrautText, 1, 8);
		infoTable.add(aFlotText, 1, 9);
		infoTable.add(puttText, 1, 10);

		Text eagle_total2 = new Text(String.valueOf(eagle_total));
		eagle_total2.setFontSize("1");
		Text birdie_total2 = new Text(String.valueOf(birdie_total));
		birdie_total2.setFontSize("1");
		Text par_total2 = new Text(String.valueOf(par_total));
		par_total2.setFontSize("1");
		Text bogey_total2 = new Text(String.valueOf(bogey_total));
		bogey_total2.setFontSize("1");
		Text dbogey_total2 = new Text(String.valueOf(dbogey_total));
		dbogey_total2.setFontSize("1");

		infoTable.add(eagle_total2, 2, 2);
		infoTable.add(birdie_total2, 2, 3);
		infoTable.add(par_total2, 2, 4);
		infoTable.add(bogey_total2, 2, 5);
		infoTable.add(dbogey_total2, 2, 6);

		String medaleagle = String.valueOf((double) eagle_total / (myTable.getColumns() - 2) * 100);
		medaleagle = scaleDecimals(medaleagle, 0);
		String medalbirdie = String.valueOf((double) birdie_total / (myTable.getColumns() - 2) * 100);
		medalbirdie = scaleDecimals(medalbirdie, 0);
		String medalpar = String.valueOf((double) par_total / (myTable.getColumns() - 2) * 100);
		medalpar = scaleDecimals(medalpar, 0);
		String medalbogey = String.valueOf((double) bogey_total / (myTable.getColumns() - 2) * 100);
		medalbogey = scaleDecimals(medalbogey, 0);
		String medaldbogey = String.valueOf((double) dbogey_total / (myTable.getColumns() - 2) * 100);
		medaldbogey = scaleDecimals(medaldbogey, 0);

		Text medaleagle2 = new Text(medaleagle + "%");
		medaleagle2.setFontSize("1");
		Text medalbirdie2 = new Text(medalbirdie + "%");
		medalbirdie2.setFontSize("1");
		Text medalpar2 = new Text(medalpar + "%");
		medalpar2.setFontSize("1");
		Text medalbogey2 = new Text(medalbogey + "%");
		medalbogey2.setFontSize("1");
		Text medaldbogey2 = new Text(medaldbogey + "%");
		medaldbogey2.setFontSize("1");

		infoTable.add(medaleagle2, 3, 2);
		infoTable.add(medalbirdie2, 3, 3);
		infoTable.add(medalpar2, 3, 4);
		infoTable.add(medalbogey2, 3, 5);
		infoTable.add(medaldbogey2, 3, 6);

		//infoTable.setColumnAlignment(1,"right");
		infoTable.setColumnAlignment(2, "center");
		infoTable.setColumnAlignment(3, "center");
		infoTable.setRowAlignment(1, "center");

		Statistic[] statistic = (Statistic[]) ((Statistic) IDOLookup.instanciateEntity(Statistic.class)).findAll("select statistic.* from statistic,tee where tee.tee_id = statistic.tee_id and scorecard_id = " + scorecard_id + " order by hole_number");

		int total_fairway = 0;
		int total_greens = 0;
		int total_putts = 0;

		for (int a = 0; a < statistic.length; a++) {

			int fairway_hit = statistic[a].getFairway();
			total_fairway += fairway_hit;

			int greens_hit = statistic[a].getGreens();
			total_greens += greens_hit;

			int nr_putts = statistic[a].getPutts();
			total_putts += nr_putts;

		}

		Text totalFairwayText = new Text(total_fairway + "/" + (statistic.length - 4));
		totalFairwayText.setFontSize(1);

		Text totalGreensText = new Text(total_greens + "/" + statistic.length);
		totalGreensText.setFontSize(1);

		Text totalPuttsText = new Text(String.valueOf(total_putts));
		totalPuttsText.setFontSize(1);

		if (total_fairway > 0) {
			infoTable.add(totalFairwayText, 2, 8);
		}
		if (total_greens > 0) {
			infoTable.add(totalGreensText, 2, 9);
		}
		if (total_putts > 0) {
			infoTable.add(totalPuttsText, 2, 10);
		}

		String medalfairway = "";
		String medalgreens = "";
		String medalputts = "";

		if (total_fairway != 0) {
			medalfairway = scaleDecimals(String.valueOf((double) total_fairway / (statistic.length - 4) * 100), 2);
		}

		if (total_greens != 0) {
			medalgreens = scaleDecimals(String.valueOf((double) total_greens / statistic.length * 100), 2);
		}

		if (total_putts != 0) {
			medalputts = scaleDecimals(String.valueOf((double) total_putts / statistic.length), 2);
		}

		Text medalFairwayText = new Text(medalfairway + "%");
		medalFairwayText.setFontSize(1);

		Text medalGreensText = new Text(medalgreens + "%");
		medalGreensText.setFontSize(1);

		Text medalPuttsText = new Text(medalputts);
		medalPuttsText.setFontSize(1);

		if (total_fairway > 0) {
			infoTable.add(medalFairwayText, 3, 8);
		}
		if (total_greens > 0) {
			infoTable.add(medalGreensText, 3, 9);
		}
		if (total_putts > 0) {
			infoTable.add(medalPuttsText, 3, 10);
		}

		infoTable.addText("", 1, 7);
		infoTable.addText("", 2, 7);
		infoTable.addText("", 3, 7);
		infoTable.addText("", 2, 8);
		infoTable.addText("", 2, 9);
		infoTable.addText("", 2, 10);
		infoTable.addText("", 3, 8);
		infoTable.addText("", 3, 9);
		infoTable.addText("", 3, 10);
		infoTable.setHeight(7, "10");

		outerTable.add(infoTable, 1, 2);
		outerTable.add(new CloseButton(iwrb.getImage("buttons/close.gif")), 1, 3);
		add(outerTable);
	}

	public String calculateHandicap(double baseHandicap, int totalPoints) {
		double change;

		if (totalPoints >= 0) {
			change = totalPoints - 36;
		}
		else {
			change = 0.0;
		}

		Handicap handicap = new Handicap(baseHandicap);
		double newHandicap = handicap.getNewHandicap(change);
		if (newHandicap > 36.0) {
			newHandicap = 36.0;
		}

		BigDecimal handicapDecimal = new BigDecimal(newHandicap);
		return handicapDecimal.setScale(1, 5).toString();
	}

	public String formatDate(String date) {
		IWCalendar dagatal = new IWCalendar();

		String returnString = date.substring(8, 10);
		returnString += "/" + date.substring(5, 7);
		returnString += "/" + date.substring(2, 4);

		return returnString;
	}

	public String scaleDecimals(String nyForgjof, int scale) {
		BigDecimal decimal = new BigDecimal(nyForgjof);
		return decimal.setScale(scale, 5).toString();
	}
}