package is.idega.idegaweb.golf.tournament.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.idega.util.IWTimestamp;

public class ResultDataHandler {

  private int tournamentId_ = -1;
  private int tournamentGroupId_ = -1;
  private int tournamentType_ = -1;
  private int[] tournamentRounds_ = null;
  private String gender_ = null;
  private int memberId_ = -1;

  public ResultDataHandler(int tournamentId, int tournamentType) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
  }

  public ResultDataHandler(int tournamentId, int tournamentType, int tournamentGroupId) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
  }
  public ResultDataHandler(int tournamentId, int tournamentType, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    gender_ = gender;
  }

  public ResultDataHandler(int tournamentId, int tournamentType, int[] tournamentRounds) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentRounds_ = tournamentRounds;
  }

  public ResultDataHandler(int tournamentId, int tournamentType, int tournamentGroupId, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
    gender_ = gender;
  }

  public ResultDataHandler(int tournamentId, int tournamentType, int tournamentGroupId, int[] tournamentRounds) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
    tournamentRounds_ = tournamentRounds;
  }

  public ResultDataHandler(int tournamentId, int tournamentType, int[] tournamentRounds, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentRounds_ = tournamentRounds;
    gender_ = gender;
  }

  public ResultDataHandler(int tournamentId, int tournamentType, int tournamentGroupId, int[] tournamentRounds, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
    tournamentRounds_ = tournamentRounds;
    gender_ = gender;
  }
  // added by gimmi
  public ResultDataHandler(int tournamentId, int tournamentType, int tournamentGroupId, int[] tournamentRounds, int member_id) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
    tournamentRounds_ = tournamentRounds;
    memberId_ = member_id;
  }

  public Vector getTournamentMembers() throws SQLException {
    Connection Conn = null;
    Vector result = null;

    try{
      Hashtable hash = new Hashtable();

      Conn = com.idega.util.database.ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      ResultSet RS  = stmt.executeQuery(getSQLString());
      int a = 0;

      while (RS.next()) {
	int member_id = RS.getInt("member_id");
	String first_name = RS.getString("first_name");
	String middle_name = RS.getString("middle_name");
	String last_name = RS.getString("last_name");
	int tournamentGroupId = RS.getInt("tournament_group_id");
	String abbrevation = RS.getString("abbrevation");
	int tournamentPosition = RS.getInt("tournament_position");
	int dismissal = RS.getInt("dismissal_id");

	ResultsCollector r = (ResultsCollector)hash.get(Integer.toString(member_id));
	if (r == null) {
	  r = new ResultsCollector(tournamentType_);
	  r.setMemberId(member_id);
	  r.setFirstName(first_name);
	  r.setMiddleName(middle_name);
	  r.setLastName(last_name);
	  r.setTournamentGroupId(tournamentGroupId);
	  r.setAbbrevation(abbrevation);
	  r.setTournamentPosition(tournamentPosition);
	  r.setDismissal(dismissal);
	  hash.put(Integer.toString(member_id),r);
	}

	Statement stmt2 = Conn.createStatement();
	ResultSet RS2  = stmt2.executeQuery(getMemberSQLString(member_id));

	a = 0;
	int roundNumber = 0;

	while (RS2.next() ) {
	  double stroke = RS2.getDouble("stroke_count");
	  int holeNumber = RS2.getInt("hole_number");
	  double point = RS2.getDouble("point_count");
	  int par = RS2.getInt("hole_par");
	  //double handicap = RS2.getDouble("tournament_handicap");
	  double handicap = RS2.getDouble("tournament_handicap");
	  int holes = RS2.getInt("holes");
	  int numberOfRounds = RS2.getInt("rounds");

	  if ( a == 0 ) {
	    r.setHandicap(handicap);
	    r.setHoles(holes);
	    r.setNumberOfRounds(numberOfRounds);
	  }
	  if ( roundNumber != RS2.getInt("round_number") ) {
	    roundNumber = RS2.getInt("round_number");
	    r.addRoundNumber(roundNumber);
	  }

	  if ( RS2.getTimestamp("scorecard_date") != null ) {
	    //System.out.println("RESULT_DATA_HANDLER : scorecard_date = "+RS2.getTimestamp("scorecard_date"));
	    r.setDate(new IWTimestamp(RS2.getTimestamp("scorecard_date")));
	  }
	  r.addStroke(holeNumber,stroke);
	  r.addPoints(point);
	  r.addPar(par);
	  a++;
	}

	RS2.close();
	stmt2.close();
      }

      RS.close();
      stmt.close();
      result = new Vector(hash.values());
      hash.clear();

    }
    finally{
	com.idega.util.database.ConnectionBroker.freeConnection(Conn);
    }

    return result;
  }

  public String getSQLString() {
    StringBuffer sql = new StringBuffer();
      sql.append("select m.member_id,m.first_name,m.middle_name,m.last_name,u.abbrevation,tm.tournament_group_id,tm.tournament_position, tm.dismissal_id");
      sql.append(" from tournament t, member m, tournament_member tm, union_ u");
      sql.append(" where m.member_id = tm.member_id");
      sql.append(" and tm.tournament_id = t.tournament_id");
      sql.append(" and tm.union_id = u.union_id");
      sql.append(" and t.tournament_id = "+Integer.toString(tournamentId_));

      //added by gimmi 07.08.2001
      if ( memberId_ != -1) {
	sql.append(" and m.member_id = '"+memberId_+"'");
      }
      // gimmi done

      if ( gender_ != null )
	sql.append(" and gender = '"+gender_+"'");
      if ( tournamentGroupId_ != -1 )
	sql.append(" and tournament_group_id = "+Integer.toString(tournamentGroupId_));

      sql.append(" order by first_name,middle_name,last_name");

    return sql.toString();
  }

  private String getMemberSQLString(int memberId) {
    StringBuffer sql = new StringBuffer();
      sql.append("select round_number,tournament_group_id,holes,rounds,stroke_count, hole_number, point_count,hole_par,scorecard_date,");
      sql.append(" cast(( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ) as numeric(3,0)) as tournament_handicap");
      sql.append(" from scorecard s, stroke st, tee t, tournament_round tr, tournament tou, field f, tournament_member tm");
      sql.append(" where t.tee_id = st.tee_id");
      sql.append(" and st.scorecard_id = s.scorecard_id");
      sql.append(" and s.member_id = tm.member_id");
      sql.append(" and tm.tournament_id = tr.tournament_id");
      sql.append(" and tr.tournament_id = tou.tournament_id");
      sql.append(" and s.tournament_round_id = tr.tournament_round_id");
      sql.append(" and s.field_id = f.field_id");
      sql.append(" and tou.tournament_id = "+Integer.toString(tournamentId_));
      sql.append(" and s.member_id = "+Integer.toString(memberId));
      if ( tournamentRounds_ != null ) {
	for ( int a = 0; a < tournamentRounds_.length; a++ ) {
	  if ( a == 0 )
	    sql.append(" and ( tr.tournament_round_id = "+Integer.toString(tournamentRounds_[a]));
	  if ( a > 0 && a < tournamentRounds_.length -1 )
	    sql.append(" or tr.tournament_round_id = "+Integer.toString(tournamentRounds_[a]));
	  if ( a == tournamentRounds_.length -1 )
	    sql.append(" or tr.tournament_round_id = "+Integer.toString(tournamentRounds_[a])+" ) ");
	}
      }
      sql.append(" order by s.member_id,tr.round_number,t.hole_number");
    //System.out.println("ResultDataHandler: "+sql.toString());

    return sql.toString();
  }
}
