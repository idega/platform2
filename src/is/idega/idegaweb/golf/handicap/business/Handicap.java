package is.idega.idegaweb.golf.handicap.business;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.FinderException;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.ScorecardHome;
import is.idega.idegaweb.golf.entity.Stroke;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;

import com.idega.data.IDOLookup;
import com.idega.util.text.TextSoap;

public class Handicap {

	private double grunn;
	
	public static Handicap getInstance() {
		return new Handicap(-1);
	}

	public Handicap(double grunn) {
		this.grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));
	}

	public Handicap(float grunn) {
		this.grunn = Double.parseDouble(TextSoap.singleDecimalFormat((double) grunn));
	}

	public double getNewHandicap(double breyting) {
		if (breyting > 0) {
			int change = (int) breyting;
			for (int a = 1; a <= change; a++) {
				grunn -= getMultiplier();
				grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));
			}
		}
		else {
			handicapIncrease(breyting);
		}

		return grunn;
	}

	public double getMultiplier() {

		double haekkun = 0.0;

		if (grunn < 4.5) {
			haekkun = 0.1;
		}
		else if ((grunn >= 4.5) && (grunn < 11.5)) {
			haekkun = 0.2;
		}
		else if ((grunn >= 11.5) && (grunn < 18.5)) {
			haekkun = 0.3;
		}
		else if ((grunn >= 18.5) && (grunn < 26.5)) {
			haekkun = 0.4;
		}
		else if (grunn >= 26.5) {
			haekkun = 0.5;
		}

		return haekkun;

	}

	private void handicapIncrease(double breyting) {
		if (grunn < 4.5) {
			if (breyting < -1.0) {
				grunn += 0.1;
			}
		}
		else if ((grunn >= 4.5) && (grunn < 11.5)) {
			if (breyting < -2.0) {
				grunn += 0.1;
			}
		}
		else if ((grunn >= 11.5) && (grunn < 18.5)) {
			if (breyting < -3.0) {
				grunn += 0.1;
			}
		}
		else if ((grunn >= 18.5) && (grunn < 26.5)) {
			if (breyting < -4.0) {
				grunn += 0.1;
			}
		}
		else if (grunn >= 26.5) {
			if (breyting < -5.0) {
				grunn += 0.2;
			}
		}
	}

	public int getLeikHandicap(int slope, float course_rating, int field_par) {
		int leik = getLeikHandicap((double) slope, (double) course_rating, (double) field_par);
		return leik;
	}

	public int getLeikHandicap(double slope, double course_rating, double field_par) {
		double leikhandicap = grunn * (slope / 113) + (course_rating - field_par);
		int leik = (int) Math.rint(leikhandicap);
		return leik;
	}

	public float getHandicapForScorecard(int tournament_id, int tee_color_id, float max_handicap) throws IOException, SQLException {
		try {
			Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(tournament_id);
			Field field = tournament.getField();

			float course_rating = 72;
			int slope = 113;
			int field_par = field.getFieldPar();

			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumn("field_id", field.getID() + "", "tee_color_id", tee_color_id + "");
			if (tee.length > 0) {
				course_rating = tee[0].getCourseRating();
				slope = tee[0].getSlope();
			}

			float handicap = (float) ((max_handicap - (course_rating - field_par)) * 113) / slope;

			return handicap;
		}
		catch (FinderException fe) {
			throw new SQLException(fe.getMessage());
		}
	}

	public int calculatePoints(Scorecard sc, Vector strokes, int playHandicap) {
		int leik = playHandicap;
		int leikpunktar = leik + 36;
		int punktar = leikpunktar / 18;
		int afgangur = leikpunktar % 18;
		int punktar2 = punktar + 1;
		int punktar3 = 0;
		int hole_handicap = 0;
		int hole_par = 0;
		int heildarpunktar = 0;

		try {
			for (int c = 0; c < strokes.size(); c++) {
				Stroke stroke2 = (Stroke) strokes.elementAt(c);
				if (stroke2 != null) {
					hole_handicap = (int) stroke2.getHoleHandicap();
					hole_par = stroke2.getHolePar();

					int strokes2 = stroke2.getStrokeCount();

					if (hole_handicap > afgangur) {
						punktar3 = hole_par + punktar - strokes2;
					}

					if (hole_handicap <= afgangur) {
						punktar3 = hole_par + punktar2 - strokes2;
					}

					if (punktar2 < 0) {
						punktar3 = 0;
					}

					if (punktar3 < 0) {
						punktar3 = 0;
					}

					if (strokes2 == 0) {
						punktar3 = 0;
					}

					heildarpunktar += punktar3;

					stroke2.setPointCount(punktar3);
					stroke2.update();
				}
			}

			if (strokes.size() == 9) {
				heildarpunktar += 18;
			}

			sc.setTotalPoints(heildarpunktar);
			sc.update();
		}
		catch (java.sql.SQLException e) {
			e.printStackTrace();
		}

		return heildarpunktar;
	}

	public int calculatePointsWithoutUpdate(Stroke[] strokes, int playHandicap) {
		int leik = playHandicap;
		int leikpunktar = leik + 36;
		int punktar = leikpunktar / 18;
		int afgangur = leikpunktar % 18;
		int punktar2 = punktar + 1;
		int punktar3 = 0;
		int heildarpunktar = 0;
		int hole_handicap = 0;
		int hole_par = 0;
		int numberOfStrokes = strokes.length;

		try {
			for (int c = 0; c < numberOfStrokes; c++) {
				hole_handicap = (int) strokes[c].getHoleHandicap();
				hole_par = strokes[c].getHolePar();

				int strokes2 = strokes[c].getStrokeCount();

				if (hole_handicap > afgangur) {
					punktar3 = hole_par + punktar - strokes2;
				}

				if (hole_handicap <= afgangur) {
					punktar3 = hole_par + punktar2 - strokes2;
				}

				if (punktar2 < 0) {
					punktar3 = 0;
				}

				if (punktar3 < 0) {
					punktar3 = 0;
				}

				if (strokes2 == 0) {
					punktar3 = 0;
				}

				heildarpunktar += punktar3;

			}

			if (strokes.length == 9) {
				heildarpunktar += 18;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return heildarpunktar;

	}

	public int getTotalPoints(int scorecard_id, float grunnHandicap) {
		int totalPoints = 0;
		try {
			Scorecard scorecard = ((ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class)).findByPrimaryKey(scorecard_id);
			Handicap handicap = new Handicap((double) grunnHandicap);
			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(scorecard.getFieldID());

			int leikHandicap = handicap.getLeikHandicap((double) scorecard.getSlope(), (double) scorecard.getCourseRating(), (double) field.getFieldPar());

			totalPoints = getTotalPoints(scorecard_id, leikHandicap);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return totalPoints;
	}

	public int getTotalPoints(int scorecard_id, int leikHandicap) {
		int totalPoints = 0;
		try {
			Stroke[] stroke = (Stroke[]) ((Stroke) IDOLookup.instanciateEntity(Stroke.class)).findAll("select s.* from stroke s,tee t where s.tee_id = t.tee_id and scorecard_id = " + scorecard_id + " order by hole_number");

			totalPoints = calculatePointsWithoutUpdate(stroke, leikHandicap);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return totalPoints;
	}
}