/*
 * Created on 9.6.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.golf.tournament.business;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.io.MediaWritable;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;


/**
 * @author aron
 *
 * PrintStickersWriter TODO Describe this type
 */
public class PrintStickersWriter extends ExcelWriter implements MediaWritable {
	
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.golf.tournament.business.ExcelWriter#writeToBuffer(com.idega.idegaweb.IWUserContext, com.idega.io.MemoryFileBuffer)
	 */
	public void writeFileContent(IWContext iwc, Writer out) {
	
		String tournamentRoundID = iwc.getParameter("tournament_round_id");
		Connection Conn = null;

		  try{
			Conn = com.idega.util.database.ConnectionBroker.getConnection();

			char[] c  = null;

			Statement stmt = Conn.createStatement();

			TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournamentRoundID));
			Tournament tournament = tournamentRound.getTournament();
			int interval = tournament.getInterval();

//			TODO: Gimmi : Why not use TournamentController.getTournamentRoundParticipants() here?

			String sql = "select m.member_id, m.social_security_number, m.first_name, m.middle_name,"+
			  " m.last_name, u.abbrevation,tm.tournament_id, tm.tournament_group_id, tg.name as tournament_group_name, tc.tee_color_name, s.scorecard_id,"+
			  " s.scorecard_date, tr.tournament_round_id, tr.round_number, count(str.stroke_count) as holes_played,"+
			  " cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0)) as round_handicap,"+
			  " sum(str.stroke_count) as strokes_without_handicap, sum(str.stroke_count) - cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ))as numeric (4,0)) as strokes_with_handicap,"+
			  " s.total_points, sum(str.hole_par) as total_par, sum(str.stroke_count) - sum(str.hole_par) as difference, start.grup_num, start.startingtime_id, tg.name as group_name"+
			  " from tournament_round tr, tee_color tc,"+
			  " member_info mi, member m, field f, union_ u, tournament_member tm, tournament t,tournament_group tg, startingtime start, tournament_ROUND_startingtime ts,"+
			  " scorecard s left join stroke str on str.scorecard_id = s.scorecard_id"+
			  " where s.tee_color_id = tc.tee_color_id and s.tournament_round_id = tr.tournament_round_id and tr.tournament_id = t.tournament_id"+
			  " and tm.tournament_group_id = tg.tournament_group_id and tm.union_id = u.union_id and t.tournament_id = tm.tournament_id and tm.member_id = mi.member_id"+
			  " and mi.member_id = s.member_id and s.member_id = m.member_id and s.field_id = f.field_id"+
			  " AND tR.tournament_ROUND_id = ts.tournament_ROUND_id AND ts.startingtime_id = start.startingtime_id"+
			  " AND start.field_id = t.field_id"+
			  //" AND start.startingtime_date >= cast (tr.round_date as datetime)"+
			  //" AND start.startingtime_date <= cast (tr.round_end_date as datetime)"+
			  " AND m.member_id = start.member_id and tm.tournament_group_id = tg.tournament_group_id"+
			  " and tr.tournament_round_id = "+tournamentRoundID+
			  " group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, tm.tournament_id, tm.tournament_group_id, tg.name,tc.tee_color_name,s.scorecard_id, s.scorecard_date, f.field_par,tr.tournament_round_id, tr.round_number, s.total_points, s.handicap_before, s.slope, s.course_rating, start.grup_num, start.startingtime_id, tg.name"+
			  " order by grup_num,start.startingtime_id";

			ResultSet RS  = stmt.executeQuery(sql);

			int count = 0;
			String tournName = tournament.getName();
			StringBuffer data = new StringBuffer();
			data.append(localize("tournament.tournament","Tournament")); data.append("\t");
			data.append(localize("tournament.name","Name")); data.append("\t");
			data.append(localize("tournament.ssn","Social security number")); data.append("\t");
			data.append(localize("tournament.club","Club")); data.append("\t");
			data.append(localize("tournament.group","Group")); data.append("\t");
			data.append(localize("tournament.tee","Tee")); data.append("\t");
			data.append(localize("tournament.gamehandicap","Gamehandicap"));data.append("\t");
			data.append(localize("tournament.teetime_with_date","Teetime with date")); data.append("\t");
			data.append(localize("tournament.teetime","Teetime")); data.append("\t");
			data.append("\n");
			out.write(data.toString().toCharArray());

			while(RS.next()){

			  IWTimestamp stampur2 = new IWTimestamp(tournamentRound.getRoundDate());

			  int addMinutes = (RS.getInt("grup_num")-1) * interval;
			  stampur2.addMinutes(addMinutes);

			  String minutes = stampur2.getMinute()+"";
			  if ( stampur2.getMinute() < 10 ) {
				minutes = "0"+minutes;
			  }

			  data = new StringBuffer();
			  String s = tournName; // tournName
			  data.append(s);       data.append("\t");
			  s = RS.getString("first_name"); // first_name
			  if(!RS.wasNull()){   data.append(s);       data.append(" ");     }
			  s = RS.getString("middle_name"); // middle_name
			  if(!RS.wasNull()){   data.append(s);       data.append(" ");     }
			  s = RS.getString("last_name"); // last_name
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  String ssc = RS.getString("social_security_number");
			  if ( ssc.length() == 10 ) {
				s = ssc.substring(0,6)+"-"+ssc.substring(6,ssc.length());
			  }
			  else {
				s =  ssc; // social_security_number
			  }
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  s = RS.getString("abbrevation"); // abbrevation
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  s = RS.getString(9); // tournament_group_name
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  s = RS.getString("tee_color_name"); // tee_color_name
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  s = RS.getString(16); // round_handicap
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  s = stampur2.getYear()+"-"+stampur2.getMonth()+"-"+stampur2.getDate()+" kl. "+stampur2.getHour()+"."+minutes; // startingtime
			  data.append(s);
			  data.append("\t");
			  s = stampur2.getHour()+":"+minutes; // startingtime
			  data.append(s);
			  data.append("\n");
			  c = data.toString().toCharArray();
			  out.write(c);
			  count++;

			}

			if(RS!=null)
			  RS.close();
			stmt.close();
			if(c!=null)
			  out.write(c);

			out.close();

		  }
		  catch(IOException io){
			io.printStackTrace();
		  }
		  catch(SQLException sql){
			sql.printStackTrace();
		  }
		  catch(FinderException e){
		  	e.printStackTrace();
		  }
		  finally {
			com.idega.util.database.ConnectionBroker.freeConnection(Conn);
		  }


	}

	


}
