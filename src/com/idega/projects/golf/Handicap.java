package com.idega.projects.golf;

import java.text.DecimalFormat;

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

}