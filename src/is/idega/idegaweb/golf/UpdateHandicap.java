package is.idega.idegaweb.golf;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Stroke;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.handicap.business.Handicap;

import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

public class UpdateHandicap {

    public static void update(int member_id) {
	try {
	    Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(member_id);
	    IWTimestamp stampur = new IWTimestamp(1,1,2000);
	    UpdateHandicap.update(member,stampur);
	}
	catch (FinderException sql) {
	    sql.printStackTrace(System.err);
	}
    }

    public static void update(int member_id,IWTimestamp stampur) {
	try {
	    Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(member_id);
	    stampur.addDays(-1);
	    UpdateHandicap.update(member,stampur);
	}
	catch (FinderException sql) {
	    sql.printStackTrace(System.err);
	}
    }

    public static void update(Member member,IWTimestamp stampur) {

      try {
	    int member_id = member.getID();
	    //System.out.println("MemberID: "+member_id);

	    MemberInfo memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByMember(member);
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
	    boolean updateHandicap = true;

	    Scorecard[] scorecardsBefore = (Scorecard[]) ((Scorecard)IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id = "+member_id+" and scorecard_date < '"+stampur.toSQLDateString()+"' order by scorecard_date desc");
	    if ( scorecardsBefore.length > 0 ) {
	      grunn = (double) scorecardsBefore[0].getHandicapAfter();
	    }
	    grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));

	    Scorecard[] scorecard = (Scorecard[]) ((Scorecard)IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id = "+member_id+" and ( scorecard_date > '"+stampur.toSQLDateString()+"' or scorecard_date is null ) order by scorecard_date");
	    if ( scorecard.length > 0 ) {

	      //System.out.println("Number of scorecards: "+scorecard.length);
	      for (int m=0; m < scorecard.length; m++) {

		  //System.out.println("Handicap: "+grunn);
		  int scorecardID = scorecard[m].getID();
		  round = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(scorecard[m].getTournamentRoundId());

		  if ( scorecard[m].getForeignRound() ) {
		    scorecard[m].setHandicapBefore((float) grunn);
		    grunn = reiknaHandicap2(member,(double)grunn,scorecard[m].getTotalPoints());
		    scorecard[m].setHandicapAfter((float) grunn);
		  }
		  else {
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
			updateHandicap = scorecard[m].getUpdateHandicapBoolean();


			stroke2 = (Stroke[]) ((Stroke) IDOLookup.instanciateEntity(Stroke.class)).findAll("select s.* from stroke s,tee t where s.tee_id = t.tee_id and scorecard_id = "+scorecardID+" order by hole_number");

			strokeVector = new Vector(stroke2.length);
			for (int i = 0; i < stroke2.length; i++)
			  strokeVector.add(stroke2[i]);

			field_par = getFieldPar(strokeVector);
			if ( field_par == 0 ) {
			  field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(field_id);
			  field_par = field.getFieldPar();
			}

			leikForgjof = new Handicap(grunn);
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
			      leikForgjof = new Handicap((double) tournamentHandicap);
			    }
			  }

			leik = leikForgjof.getLeikHandicap((double) slope,(double) course_rating,(double) field_par);
			realLeik = new Handicap(grunn).getLeikHandicap((double) slope,(double) course_rating,(double) field_par);
			//System.out.println("PlayHandicap: "+leik);
			//System.out.println("RealPlayHandicap: "+realLeik);

			heildarpunktar = 0;
			realTotalPoints = 0;

			heildarpunktar = Handicap.calculatePoints(scorecard[m],strokeVector,leik);

			if ( isTournament ) {
			  scorecard[m].setHandicapBefore((float) tournamentHandicap);
			  realTotalPoints = Handicap.calculatePointsWithoutUpdate(stroke2,realLeik);
			}
			else {
			  //grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));
			  scorecard[m].setHandicapBefore((float) grunn);
			}

			if ( !updateHandicap && realTotalPoints > 36 && round.getDecreaseHandicap() ) {
			  updateHandicap = true;
			}

			if ( updateHandicap ) {
			  if ( isTournament ) {
			    grunn = reiknaHandicap2(member,(double)grunn,realTotalPoints);
			  }
			  else {
			    grunn = reiknaHandicap2(member,(double)grunn,heildarpunktar);
			  }
			  //grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));
			  scorecard[m].setHandicapAfter((float) grunn);
			}
			else {
			  //grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));
			  scorecard[m].setHandicapAfter((float) grunn);
			}
		    }
		    else {
			//grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));
			scorecard[m].setHandicapBefore((float) grunn);
			grunn = (double) scorecard[m].getHandicapAfter();
			//grunn = Double.parseDouble(TextSoap.singleDecimalFormat(grunn));
		    }
		  }


		  scorecard[m].update();

		  //System.out.println("HandicapNew: "+grunn);
		  //System.out.println("----------------------------");
	      }
	    }

	    memberInfo.setHandicap((float) grunn);
	    memberInfo.update();
      }

      catch (Exception e) {
	  e.printStackTrace(System.err);
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

      nyForgjof = Double.parseDouble(TextSoap.singleDecimalFormat(nyForgjof));
      return nyForgjof;
    }

    public static String reiknaHandicap(Member member, double grunn, int heildarpunktar) {
      double nyForgjof = reiknaHandicap2(member,grunn,heildarpunktar);
      return Double.toString(nyForgjof);
    }

    private static int getFieldPar(Vector vector) {
      if ( vector != null ) {
	int par = 0;
	Iterator iter = vector.iterator();
	while (iter.hasNext()) {
	  Stroke item = (Stroke) iter.next();
	  par += item.getHolePar();
	}
	if ( vector.size() == 9 )
	  par = par * 2;
	else if ( vector.size() < 18 )
	  par = 0;
	return par;
      }
      return 0;
    }
}