/*
 * Created on 14.7.2003 by  tryggvil in project golf.project
 */
package is.idega.idegaweb.golf.tournament.presentation;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.util.IWTimestamp;



/**
 * PrintUnfilledLeaderBoardExcel Quick port of print_excel.jsp
 * Copyright (C) idega software 2003
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class PrintUnfilledLeaderBoardExcel extends Block
{
	public void main(IWContext modinfo){

		  String tournamentRoundID = modinfo.getParameter("tournament_round_id");

		  IWTimestamp datenow = new IWTimestamp();
		  String fileSeperator = System.getProperty("file.separator");
		  String filepath = modinfo.getServletContext().getRealPath(fileSeperator+"files"+fileSeperator);
		  String fileName = "status.xls";

		  try{
			String file = filepath+fileName;
			FileWriter out = new FileWriter(file);

			char[] c  = null;

			Connection Conn = com.idega.util.database.ConnectionBroker.getConnection();
			Statement stmt = Conn.createStatement();
		
//			TODO: Gimmi : Why not use TournamentController.getTournamentRoundParticipants() here?

		String queryString = "select m.member_id, m.social_security_number, m.first_name, m.middle_name,"+
		" m.last_name, u.abbrevation,tm.tournament_id, tm.tournament_group_id,s.scorecard_id,"+
		" s.scorecard_date, tr.tournament_round_id, tr.round_number, count(str.stroke_count) as holes_played,"+
		" cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0)) as round_handicap,"+
		" sum(str.stroke_count) as strokes_without_handicap, sum(str.stroke_count) - cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ))as numeric (4,0)) as strokes_with_handicap,"+
		" s.total_points, sum(str.hole_par) as total_par, sum(str.stroke_count) - sum(str.hole_par) as difference, start.grup_num, tg.name as group_name"+
		" from tournament_round tr,"+
		" member m, field f, union_ u, tournament_member tm, tournament t,tournament_group tg, startingtime start, tournament_ROUND_startingtime ts,"+
		" scorecard s left join stroke str on str.scorecard_id = s.scorecard_id"+
		" where s.tournament_round_id = tr.tournament_round_id and tr.tournament_id = t.tournament_id"+
		" and tm.union_id = u.union_id and t.tournament_id = tm.tournament_id and tm.member_id = m.member_id"+
		" and s.member_id = m.member_id and t.field_id = f.field_id"+
		" AND tR.tournament_ROUND_id = ts.tournament_ROUND_id AND ts.startingtime_id = start.startingtime_id"+
		" AND start.field_id = t.field_id"+
		//" AND start.startingtime_date >= cast (tr.round_date as datetime)"+
		//" AND start.startingtime_date <= cast (tr.round_end_date as datetime)"+
		" AND m.member_id = start.member_id and tm.tournament_group_id = tg.tournament_group_id"+
		" and tr.tournament_round_id = "+tournamentRoundID+
		" group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, tm.tournament_id, tm.tournament_group_id,s.scorecard_id, s.scorecard_date, f.field_par,tr.tournament_round_id, tr.round_number, s.total_points, s.handicap_before, s.slope, s.course_rating, start.grup_num, tg.name"+
		" order by grup_num";

			ResultSet RS  = stmt.executeQuery(queryString);

			int count = 0;

			StringBuffer data = new StringBuffer();
			data.append("Nafn"); data.append("\t");
			data.append("Klúbbur"); data.append("\t");
			data.append("Forgjöf");data.append("\t");
			data.append("Fyrri 9.holur"); data.append("\t");
			data.append("Seinni 9.holur"); data.append("\t");
			data.append("Samtals 18.holur"); data.append("\t");
			data.append("\n");
			out.write(data.toString().toCharArray());

			while(RS.next()){
			  data = new StringBuffer();
			  String s = RS.getString("first_name"); // first_name
			  if(!RS.wasNull()){   data.append(s);       data.append(" ");     }
			  s = RS.getString("middle_name"); // middle_name
			  if(!RS.wasNull()){   data.append(s);       data.append(" ");     }
			  s = RS.getString("last_name"); // last_name
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  s = RS.getString("abbrevation"); // abbrevation
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  s = RS.getString("round_handicap"); // round_handicap
			  if(!RS.wasNull()){   data.append(s); }
			  data.append("\t");
			  data.append(" ");
			  data.append("\t");
			  data.append(" ");
			  data.append("\t");
			  data.append(" ");
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

			Page page = getParentPage();
			page.setToRedirect("/servlet/Excel?&dir="+file,1);

		  }
		  catch(IOException io){add("io villa");}
		  catch(SQLException sql){add("sql villa");sql.printStackTrace();}

		}
}
