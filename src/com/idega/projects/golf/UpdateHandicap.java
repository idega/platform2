package com.idega.projects.golf;

import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.*;
import java.util.Vector;
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
            //System.out.println("MemberID: "+member_id);

            MemberInfo memberInfo = new MemberInfo(member_id);
            TournamentRound round = null;
            Field field = null;
            Handicap leikForgjof = null;
            Vector strokeVector = null;
            Stroke[] stroke2 = null;

            int leik = 0;
            int realLeik = 0;
            int heildarpunktar = 0;
            int realTotalPoints = 0;

            int field_par = 0;
            int slope = 0;
            float course_rating = 0;
            int teeColorID = 0;
            int field_id = 0;
            int tournamentRoundID = 0;
            boolean isTournament = false;
            float modifier = -1;

            double grunn = (double) memberInfo.getFirstHandicap();
            int tee_id = 0;
            double tournamentHandicap = 0;

            Scorecard[] scorecard = (Scorecard[]) (Scorecard[]) Scorecard.getStaticInstance("com.idega.projects.golf.entity.Scorecard").findAll("select * from scorecard where member_id = "+member_id+" and ( scorecard_date > '"+stampur.toSQLDateString()+"' or scorecard_date is null ) order by scorecard_date");
            if ( scorecard.length > 0 ) {
              Scorecard[] scorecardsBefore = (Scorecard[]) Scorecard.getStaticInstance("com.idega.projects.golf.entity.Scorecard").findAll("select * from scorecard where member_id = "+member_id+" and scorecard_date < '"+stampur.toSQLDateString()+"' order by scorecard_date desc");
              if ( scorecardsBefore.length > 0 ) {
                grunn = (double) scorecardsBefore[0].getHandicapAfter();
              }
            }

            //System.out.println("Number of scorecards: "+scorecard.length);
            for (int m=0; m < scorecard.length; m++) {

                int scorecardID = scorecard[m].getID();
                round = new TournamentRound(scorecard[m].getTournamentRoundId());

                if ( scorecard[m].getHandicapCorrection().equalsIgnoreCase("N") ) {
                    slope = scorecard[m].getSlope();
                    course_rating = scorecard[m].getCourseRating();
                    teeColorID = scorecard[m].getTeeColorID();
                    field_id = scorecard[m].getFieldID();
                    tournamentRoundID = scorecard[m].getTournamentRoundId();
                    isTournament = false;
                    if ( tournamentRoundID > 1 ) {
                      isTournament = true;
                    }

                    field = new Field(field_id);
                      field_par = field.getFieldPar();

                    leikForgjof = new Handicap(grunn);
                    //System.out.println("Handicap: "+leikForgjof);
                      if ( isTournament ) {
                      //System.out.println("Is tournament");
                        if ( round.getRoundNumber() == 1 ) {
                          tournamentHandicap = grunn;
                        }
                        float tournamentPlayHandicap = (float) leikForgjof.getLeikHandicap(slope,course_rating,field_par);
                        //System.out.println("TournamentPlayHandicap: "+tournamentPlayHandicap);
                        Tournament tournament = round.getTournament();
                        modifier = tournament.getTournamentType().getModifier();
                        if ( member.getGender().equalsIgnoreCase("m") ) {
                          if ( tournamentPlayHandicap > tournament.getMaxHandicap() ) {
                            tournamentHandicap = leikForgjof.getHandicapForScorecard(tournament.getID(),teeColorID,tournament.getMaxHandicap());
                            //System.out.println("TournamentHandicap: "+tournamentHandicap);
                          }
                        }
                        else if ( member.getGender().equalsIgnoreCase("f") ) {
                          if ( tournamentPlayHandicap > tournament.getFemaleMaxHandicap() ) {
                            tournamentHandicap = leikForgjof.getHandicapForScorecard(tournament.getID(),teeColorID,tournament.getFemaleMaxHandicap());
                            //System.out.println("TournamentHandicap: "+tournamentHandicap);
                          }
                        }
                        leikForgjof = new Handicap((double) tournamentHandicap);
                        //System.out.println("TournamentHandicap: "+leikForgjof);
                        if ( modifier != -1 ) {
                          //System.out.println("Has modifier");
                          int modified = leikForgjof.getLeikHandicap((double) slope,(double) course_rating,(double) field_par);
                          int modifiedHandicap = Math.round((float) modified * modifier);
                          tournamentHandicap = leikForgjof.getHandicapForScorecard(tournament.getID(),teeColorID,modifiedHandicap);
                          //System.out.println("TournamentHandicap: "+tournamentHandicap);
                        }
                      }

                    leik = leikForgjof.getLeikHandicap((double) slope,(double) course_rating,(double) field_par);
                    realLeik = new Handicap(grunn).getLeikHandicap((double) slope,(double) course_rating,(double) field_par);
                    //System.out.println("PlayHandicap: "+leik);
                    //System.out.println("RealPlayHandicap: "+realLeik);

                    heildarpunktar = 0;
                    realTotalPoints = 0;

                    stroke2 = (Stroke[]) (new Stroke()).findAll("select stroke.* from stroke s,tee t where s.tee_id = t.tee_id and scorecard_id = "+scorecardID+" order by hole_number");

                    strokeVector = new Vector(stroke2.length);
                    for (int i = 0; i < stroke2.length; i++)
                      strokeVector.add(stroke2[i]);

                    heildarpunktar = Handicap.calculatePoints(scorecard[m],strokeVector,leik);

                    if ( isTournament ) {
                      scorecard[m].setHandicapBefore((float) tournamentHandicap);
                      realTotalPoints = Handicap.calculatePointsWithoutUpdate(stroke2,realLeik);
                    }
                    else {
                      scorecard[m].setHandicapBefore((float) grunn);
                    }

                    if ( scorecard[m].getUpdateHandicap().equalsIgnoreCase("Y") ) {
                      if ( isTournament ) {
                        grunn = reiknaHandicap2(member,(double)grunn,realTotalPoints);
                      }
                      else {
                        grunn = reiknaHandicap2(member,(double)grunn,heildarpunktar);
                      }

                      scorecard[m].setHandicapAfter((float) grunn);
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

                System.out.print(".");

            }

            memberInfo.setHandicap((float) grunn);
            memberInfo.update();
      }

      catch (Exception e) {
          e.printStackTrace(System.out);
      }

    }

    public static double reiknaHandicap2(Member member, double grunn, int heildarpunktar) {
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

        if ( member.getGender().equalsIgnoreCase("f") ) {
          if ( nyForgjof > 40.0 ) {
            nyForgjof = 40.0;
          }
        }
        else if ( member.getGender().equalsIgnoreCase("m") ) {
          if ( nyForgjof > 36.0 ) {
            nyForgjof = 36.0;
          }
        }
      }
      catch (Exception e) {
          e.printStackTrace(System.out);
      }

      return nyForgjof;
    }

    public static String reiknaHandicap(Member member, double grunn, int heildarpunktar) {
      double nyForgjof = reiknaHandicap2(member,grunn,heildarpunktar);
      return Double.toString(nyForgjof);
    }

}