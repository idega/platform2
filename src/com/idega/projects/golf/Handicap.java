package com.idega.projects.golf;

import java.text.DecimalFormat;

public class Handicap {

private double grunn;

	public Handicap(double grunn) {

		this.grunn = grunn;

	}

	public double getNewHandicap(double breyting) {

		double haekkun = 0.0;

		if (grunn <= 4.4) {

			if ( breyting > 0.0 ) {

				haekkun = breyting * 0.1;
				grunn -= haekkun;

			}

			else if ( breyting < -1.0 ) {

				grunn += 0.1;

			}

		}

		else if ((grunn > 4.4) && (grunn <= 11.4)) {

			if ( breyting > 0.0 ) {

				haekkun = breyting * 0.2;
				grunn -= haekkun;

			}

			else if ( breyting < -2.0 ) {

				grunn += 0.1;

			}

		}

		else if ((grunn > 11.4) && (grunn <= 18.4)) {

			if ( breyting > 0.0 ) {

				haekkun = breyting * 0.3;
				grunn -= haekkun;

			}

			else if ( breyting < -3.0 ) {

				grunn += 0.1;

			}

		}

		else if ((grunn > 18.4) && (grunn <= 26.4)) {

			if ( breyting > 0.0 ) {

				haekkun = breyting * 0.4;
				grunn -= haekkun;

			}

			else if ( breyting < -4.0 ) {

				grunn += 0.1;

			}

		}

		else if (grunn > 26.4) {

			if ( breyting > 0.0 ) {

				haekkun = breyting * 0.5;
				grunn -= haekkun;


			}

			else if ( breyting < -5.0 ) {

				grunn += 0.2;

			}

		}

		return grunn;

	}

	public int getLeikHandicap (double slope, double course_rating, double field_par) {

		double leikhandicap = grunn * (slope/113) + (course_rating-field_par);

		int leik = (int) Math.rint(leikhandicap);

		return leik;

	}

}