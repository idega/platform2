package com.idega.projects.golf;

import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.*;

public class UpdateHandicap {

    public static void update(int member_id) {

      try {

            MemberInfo member = new MemberInfo(member_id);

            double grunn = (double) member.getFloatColumnValue("handicap_first");
            int tee_id = 0;

            Scorecard[] scorecard = (Scorecard[]) (new Scorecard()).findAllByColumnOrdered("member_id",member_id+"","scorecard_date");
            for (int m=0; m < scorecard.length; m++) {

                TournamentRound round = scorecard[m].getTournamentRound();
                TournamentRound[] rounds = (TournamentRound[]) (new TournamentRound()).findAllByColumn("tournament_id",round.getTournamentID());

                if ( scorecard[m].getHandicapCorrection().equalsIgnoreCase("N") ) {

                            float slope = (float) scorecard[m].getSlope();
                            float course_rating = scorecard[m].getCourseRating();
                            int field_id = scorecard[m].getFieldID();

                    Field field = new Field(field_id);
                            float field_par = (float) field.getIntColumnValue("field_par");

                    Handicap leikForgjof = new Handicap(grunn);
                      if ( scorecard[m].getTournamentRoundId() > 1 ) {
                        leikForgjof = new Handicap((double) scorecard[m].getHandicapBefore());
                      }
                            int leik = leikForgjof.getLeikHandicap((double) slope,(double) course_rating,(double) field_par);

                    int leikpunktar = leik + 36;
                    int punktar = leikpunktar/18;
                    int afgangur = leikpunktar%18;
                    int punktar2 = punktar + 1;
                    int punktar3 = 0;
                    int heildarpunktar = 0;
                    int hole_handicap = 0;
                    int hole_par = 0;

                    Stroke[] stroke2 = (Stroke[]) (new Stroke()).findAllByColumn("scorecard_id",scorecard[m].getID());

                    for (int c = 0 ; c < stroke2.length; c++ ) {

                            hole_handicap = (int) stroke2[c].getHoleHandicap();
                            hole_par = stroke2[c].getHolePar();

                            int strokes2 = stroke2[c].getStrokeCount();

                            if ( hole_handicap > afgangur ) {
                                    punktar3 = hole_par + punktar - strokes2;
                            }

                            if ( hole_handicap <= afgangur ) {
                                    punktar3 = hole_par + punktar2 - strokes2;
                            }

                            if ( punktar2 < 0 ) {
                                    punktar3 = 0;
                            }

                            if ( punktar3 < 0 ) {
                                    punktar3 = 0;
                            }

                            if ( strokes2 == 0 ) {
                              punktar3 = 0;
                            }

                            heildarpunktar += punktar3;

                            stroke2[c].setPointCount(punktar3);
                            stroke2[c].update();

                    }

                    if ( stroke2.length <= 9 ) {
                            heildarpunktar *= 2;
                    }

                    scorecard[m].setTotalPoints(heildarpunktar);

                    if ( scorecard[m].getTournamentRoundId() == 1 ) {
                      scorecard[m].setHandicapBefore((float) grunn);
                    }

                    if ( scorecard[m].getUpdateHandicap().equalsIgnoreCase("Y") ) {
                      grunn = reiknaHandicap2((double)grunn,heildarpunktar);

                      if ( scorecard[m].getTournamentRoundId() == 1 || round.getRoundNumber() == rounds.length ) {
                        scorecard[m].setHandicapAfter((float) grunn);
                      }
                    }
                    else {
                      scorecard[m].setHandicapAfter((float) grunn);
                    }
                }
                else {
                    scorecard[m].setHandicapBefore((float) grunn);
                    if ( m == 0 ) {
                      scorecard[m].setHandicapBefore(member.getFirstHandicap());
                    }
                    grunn = (double) scorecard[m].getHandicapAfter();
                }

                  scorecard[m].update();

            }

            member.setHandicap((float) grunn);
            member.update();

      }

      catch (Exception e) {
          e.printStackTrace(System.out);
      }

    }

    public static double reiknaHandicap2(double grunn, int heildarpunktar) {

            double nyForgjof = 0;

            try {

              double breyting;

              if ( heildarpunktar >= 0 ) {

                      breyting = heildarpunktar - 36;

              }

              else {

                      breyting = 0.0;

              }

              Handicap forgjof = new Handicap(grunn);

              nyForgjof = forgjof.getNewHandicap(breyting);

              if ( nyForgjof > 36.0 ) {

                      nyForgjof = 36.0;

              }
            }

            catch (Exception e) {
                e.printStackTrace(System.out);
            }

            return nyForgjof;
    }

    public static String reiknaHandicap(double grunn, int heildarpunktar) {

        double nyForgjof = 0;

        try {

            double breyting;

            if ( heildarpunktar >= 0 ) {

                    breyting = heildarpunktar - 36;

            }

            else {

                    breyting = 0.0;

            }

            Handicap forgjof = new Handicap(grunn);

            nyForgjof = forgjof.getNewHandicap(breyting);

            if ( nyForgjof > 36.0 ) {

                    nyForgjof = 36.0;

            }

        }

        catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return Double.toString(nyForgjof);
    }

}