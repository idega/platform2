package com.idega.projects.golf;

import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.business.TournamentController;

public class UpdateHandicap {

    public static void update(int member_id) {

      try {

            Member member = new Member(member_id);
            MemberInfo memberInfo = new MemberInfo(member_id);
            TournamentRound round;
            TournamentRound[] rounds;

            double grunn = (double) memberInfo.getFirstHandicap();
            int tee_id = 0;

            Scorecard[] scorecard = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id = "+member_id+" order by scorecard_date");
            for (int m=0; m < scorecard.length; m++) {

                round = new TournamentRound(scorecard[m].getTournamentRoundId());
                rounds = (TournamentRound[]) (new TournamentRound()).findAllByColumn("tournament_id",round.getTournamentID());

                if ( scorecard[m].getHandicapCorrection().equalsIgnoreCase("N") ) {

                    float slope = (float) scorecard[m].getSlope();
                    float course_rating = scorecard[m].getCourseRating();
                    int teeColorID = scorecard[m].getTeeColorID();
                    int field_id = scorecard[m].getFieldID();
                    double tournamentHandicap = 0;
                    int tournamentRoundID = scorecard[m].getTournamentRoundId();

                    Field field = new Field(field_id);
                            float field_par = (float) field.getIntColumnValue("field_par");

                    Handicap leikForgjof = new Handicap(grunn);
                      if ( tournamentRoundID > 1 ) {
                        if ( round.getRoundNumber() == 1 ) {
                          tournamentHandicap = grunn;
                        }
                        float tournamentPlayHandicap = (float) leikForgjof.getLeikHandicap((double) slope,(double) course_rating,(double) field_par);
                        Tournament tournament = round.getTournament();
                        if ( member.getGender().equalsIgnoreCase("m") ) {
                          if ( tournamentPlayHandicap > tournament.getMaxHandicap() ) {
                            tournamentHandicap = leikForgjof.getHandicapForScorecard(tournament.getID(),teeColorID,tournament.getMaxHandicap());
                          }
                        }
                        else if ( member.getGender().equalsIgnoreCase("f") ) {
                          if ( tournamentPlayHandicap > tournament.getFemaleMaxHandicap() ) {
                            tournamentHandicap = leikForgjof.getHandicapForScorecard(tournament.getID(),teeColorID,tournament.getFemaleMaxHandicap());
                          }
                        }
                        leikForgjof = new Handicap((double) tournamentHandicap);

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

                    if ( stroke2.length == 9 ) {
                            heildarpunktar *= 2;
                    }

                    System.err.println(leik+": "+heildarpunktar);
                    scorecard[m].setTotalPoints(heildarpunktar);

                    if ( tournamentRoundID == 1 ) {
                      scorecard[m].setHandicapBefore((float) grunn);
                    }
                    else {
                      scorecard[m].setHandicapBefore((float) tournamentHandicap);
                    }

                    if ( scorecard[m].getUpdateHandicap().equalsIgnoreCase("Y") ) {
                      grunn = reiknaHandicap2((double)grunn,heildarpunktar);

                      if ( tournamentRoundID == 1 || round.getRoundNumber() == rounds.length ) {
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
                      scorecard[m].setHandicapBefore(memberInfo.getFirstHandicap());
                    }
                    grunn = (double) scorecard[m].getHandicapAfter();
                }

                  scorecard[m].update();

            }

            memberInfo.setHandicap((float) grunn);
            memberInfo.update();

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