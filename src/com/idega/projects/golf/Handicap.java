package com.idega.projects.golf;

import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;
import com.idega.projects.golf.entity.*;
import java.util.Vector;

public class Handicap {

private double grunn;

	public Handicap(double grunn) {

		this.grunn = grunn;

	}

	public double getNewHandicap(double breyting) {

         if ( breyting > 0 ) {

          int change = (int) breyting;

          for ( int a = 1; a <= change; a++ ) {

            grunn -= getMultiplier();

          }

         }

         else {
          handicapIncrease(breyting);
         }

         return grunn;

	}

        public double getMultiplier() {

          double haekkun = 0.0;

          if (grunn <= 4.4) {
              haekkun = 0.1;
          }

          else if ((grunn > 4.4) && (grunn <= 11.4)) {
              haekkun = 0.2;
          }

          else if ((grunn > 11.4) && (grunn <= 18.4)) {
              haekkun = 0.3;
          }

          else if ((grunn > 18.4) && (grunn <= 26.4)) {
              haekkun = 0.4;
          }

          else if (grunn > 26.4) {
              haekkun = 0.5;
          }

          return haekkun;

	}

        private void handicapIncrease(double breyting) {

            if (grunn <= 4.4) {
                if ( breyting < -1.0 ) {
                        grunn += 0.1;
                }
            }

            else if ((grunn > 4.4) && (grunn <= 11.4)) {
                if ( breyting < -2.0 ) {
                        grunn += 0.1;
                }
            }

            else if ((grunn > 11.4) && (grunn <= 18.4)) {
                if ( breyting < -3.0 ) {
                        grunn += 0.1;
                }
            }

            else if ((grunn > 18.4) && (grunn <= 26.4)) {
                if ( breyting < -4.0 ) {
                        grunn += 0.1;
                }
            }

            else if (grunn > 26.4) {
                if ( breyting < -5.0 ) {
                        grunn += 0.2;
                }
            }

        }

	public int getLeikHandicap (double slope, double course_rating, double field_par) {

		double leikhandicap = grunn * (slope/113) + (course_rating-field_par);

		int leik = (int) Math.rint(leikhandicap);

		return leik;

	}

	public static float getHandicapForScorecard (int tournament_id, int tee_color_id, float max_handicap) throws IOException,SQLException {

            Tournament tournament = new Tournament(tournament_id);
            Field field = tournament.getField();

            float course_rating = 72;
            int slope = 113;
            int field_par = field.getFieldPar();

            Tee[] tee = (Tee[]) (new Tee()).findAllByColumn("field_id",field.getID()+"","tee_color_id",tee_color_id+"");
            if ( tee.length > 0 ) {
              course_rating = tee[0].getCourseRating();
              slope = tee[0].getSlope();
            }

            float handicap = (float) ( ( max_handicap - ( course_rating - field_par ) ) * 113 ) / slope;

            return handicap;

	}

  public static void calculatePoints(Scorecard sc, Vector strokes, int playHandicap) {
    int leik = playHandicap;
    int leikpunktar = leik + 36;
    int punktar = leikpunktar/18;
    int afgangur = leikpunktar%18;
    int punktar2 = punktar + 1;
    int punktar3 = 0;
    int heildarpunktar = 0;
    int hole_handicap = 0;
    int hole_par = 0;

    try {
      for (int c = 0 ; c < strokes.size(); c++ ) {
        Stroke stroke2 = (Stroke)strokes.elementAt(c);
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

      if ( strokes.size() == 9 ) {
        heildarpunktar += 18;
      }

      sc.setTotalPoints(heildarpunktar);
      sc.update();
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
    }
  }
}