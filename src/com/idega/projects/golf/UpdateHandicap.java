package com.idega.projects.golf;

import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.*;
import java.sql.SQLException;
import com.idega.util.idegaTimestamp;
import com.idega.projects.golf.business.TournamentController;

public class UpdateHandicap {

    public static void update(int member_id) {
        try {
            Member member = new Member(member_id);
            idegaTimestamp stampur = new idegaTimestamp(1,1,2000);
            UpdateHandicap.update(member,stampur);
        }
        catch (SQLException sql) {
            sql.printStackTrace(System.err);
        }
    }

    public static void update(int member_id,idegaTimestamp stampur) {
        try {
            Member member = new Member(member_id);
            stampur.addDays(-1);
            UpdateHandicap.update(member,stampur);
        }
        catch (SQLException sql) {
            sql.printStackTrace(System.err);
        }
    }

    public static void update(Member member,idegaTimestamp stampur) {

      try {
            int member_id = member.getID();

            MemberInfo memberInfo = new MemberInfo(member_id);
            TournamentRound round;
            TournamentRound[] rounds;

            double grunn = (double) memberInfo.getFirstHandicap();
            int tee_id = 0;
            double tournamentHandicap = 0;

            Scorecard[] scorecard = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id = "+member_id+" and ( scorecard_date > '"+stampur.toSQLDateString()+"' or scorecard_date is null ) order by scorecard_date");

            System.out.println("Total scorecards: "+scorecard.length);
            if ( scorecard.length > 0 ) {
              Scorecard[] scorecardsBefore = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id = "+member_id+" and scorecard_date < '"+stampur.toSQLDateString()+"' order by scorecard_date desc");
              if ( scorecardsBefore.length > 0 ) {
                grunn = (double) scorecardsBefore[0].getHandicapAfter();
                System.out.println("HandicapBefore: "+grunn);
              }
            }

            for (int m=0; m < scorecard.length; m++) {

                if ( scorecard[m].getScorecardDate() != null ) {
                  System.out.println("Date: "+new idegaTimestamp(scorecard[m].getScorecardDate()).toSQLDateString());
                }
                round = new TournamentRound(scorecard[m].getTournamentRoundId());
                rounds = (TournamentRound[]) (new TournamentRound()).findAllByColumn("tournament_id",round.getTournamentID());

                if ( scorecard[m].getHandicapCorrection().equalsIgnoreCase("N") ) {

                    float slope = (float) scorecard[m].getSlope();
                    float course_rating = scorecard[m].getCourseRating();
                    int teeColorID = scorecard[m].getTeeColorID();
                    int field_id = scorecard[m].getFieldID();
                    int tournamentRoundID = scorecard[m].getTournamentRoundId();
                    System.out.println("TournamentRoundID: "+tournamentRoundID);

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
                    int realLeik = new Handicap(grunn).getLeikHandicap((double) slope,(double) course_rating,(double) field_par);

                    System.out.println("GameHandicap: "+leik);
                    System.out.println("RealHandicap: "+realLeik);

                    int leikpunktar = leik + 36;
                    int punktar = leikpunktar/18;
                    int afgangur = leikpunktar%18;
                    int punktar2 = punktar + 1;
                    int punktar3 = 0;
                    int heildarpunktar = 0;
                    int hole_handicap = 0;
                    int hole_par = 0;
                    int old_point = 0;
                    int old_total_points = scorecard[m].getTotalPoints();

                    Stroke[] stroke2 = (Stroke[]) (new Stroke()).findAllByColumn("scorecard_id",scorecard[m].getID());

                    for (int c = 0 ; c < stroke2.length; c++ ) {

                            hole_handicap = (int) stroke2[c].getHoleHandicap();
                            hole_par = stroke2[c].getHolePar();
                            old_point = stroke2[c].getPointCount();

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

                            if ( old_point != punktar3 ) {
                              stroke2[c].setPointCount(punktar3);
                              stroke2[c].update();
                            }

                    }

                    if ( stroke2.length == 9 ) {
                            heildarpunktar += 18;
                    }

                    int realTotalPoints = 0;

                    System.out.println("TotalPoints: "+heildarpunktar);

                    if( old_total_points != heildarpunktar ) {
                      scorecard[m].setTotalPoints(heildarpunktar);
                    }

                    if ( tournamentRoundID == 1 ) {
                      scorecard[m].setHandicapBefore((float) grunn);
                    }
                    else {
                      scorecard[m].setHandicapBefore((float) tournamentHandicap);
                      realTotalPoints = Handicap.calculatePointsWithoutUpdate(stroke2,realLeik);
                      System.out.println("RealTotalPoints: "+realTotalPoints);
                    }

                    if ( scorecard[m].getUpdateHandicap().equalsIgnoreCase("Y") ) {
                      if ( tournamentRoundID > 1 ) {
                        grunn = reiknaHandicap2((double)grunn,realTotalPoints);
                      }
                      else {
                        grunn = reiknaHandicap2((double)grunn,heildarpunktar);
                      }

                      System.out.println("NewHandicap: "+grunn);
                      scorecard[m].setHandicapAfter((float) grunn);
                    }
                    else {
                      scorecard[m].setHandicapAfter((float) grunn);
                    }
                }
                else {
                    System.out.println("Setting handicapBefore");
                    scorecard[m].setHandicapBefore((float) grunn);
                    if ( m == 0 ) {
                      scorecard[m].setHandicapBefore(memberInfo.getFirstHandicap());
                    }
                    System.out.println("Setting handicapAfter = Correction");
                    grunn = (double) scorecard[m].getHandicapAfter();
                    System.out.println(grunn+" - "+scorecard[m].getHandicapAfter());
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