/*
 * Created on 14.7.2003 by  tryggvil in project golf.project
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
import com.idega.util.database.ConnectionBroker;



/**
 * PrintStickers Quick port of print_stickers.jsp
 * Copyright (C) idega software 2003
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class PrintStickers extends Block
{

	public void main(IWContext modinfo)throws Exception{

		Page jPage = getParentPage();
		  jPage.setTopMargin(0);
		  jPage.setLeftMargin(0);
		  jPage.setMarginHeight(0);
		  jPage.setMarginWidth(0);


			String tournamentRoundID = modinfo.getParameter("tournament_round_id");

			String fileSeperator = System.getProperty("file.separator");
			Connection Conn = null;


		  try{

			Conn = ConnectionBroker.getConnection();
			Statement stmt = Conn.createStatement();

			TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournamentRoundID));
			Tournament tournament = tournamentRound.getTournament();
			int interval = tournament.getInterval();
			
			//TODO: Gimmi : Why not use TournamentController.getTournamentRoundParticipants() here?

			String sql = "select m.member_id, m.social_security_number, m.first_name, m.middle_name,"+
			  " m.last_name, u.abbrevation,tm.tournament_id, tm.tournament_group_id,s.scorecard_id,"+
			  " s.scorecard_date, tr.tournament_round_id, tr.round_number, count(str.stroke_count) as holes_played,"+
			  " cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0)) as round_handicap,"+
			  " sum(str.stroke_count) as strokes_without_handicap, sum(str.stroke_count) - cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ))as numeric (4,0)) as strokes_with_handicap,"+
			  " s.total_points, sum(str.hole_par) as total_par, sum(str.stroke_count) - sum(str.hole_par) as difference, start.grup_num, tg.name as group_name"+
			  " from tournament_round tr,"+
			  " member_info mi, member m, field f, union_ u, tournament_member tm, tournament t,tournament_group tg, startingtime start, tournament_ROUND_startingtime ts,"+
			  " scorecard s left join stroke str on str.scorecard_id = s.scorecard_id"+
			  " where s.tournament_round_id = tr.tournament_round_id and tr.tournament_id = t.tournament_id"+
			  " and tm.union_id = u.union_id and t.tournament_id = tm.tournament_id and tm.member_id = mi.member_id"+
			  " and mi.member_id = s.member_id and s.member_id = m.member_id and s.field_id = f.field_id"+
			  " AND tR.tournament_ROUND_id = ts.tournament_ROUND_id AND ts.startingtime_id = start.startingtime_id"+
			  " AND start.field_id = t.field_id"+ 
			  //" AND start.startingtime_date >= cast (tr.round_date as datetime)"+
			  //" AND start.startingtime_date <= cast (tr.round_end_date as datetime)"+
			  " AND m.member_id = start.member_id and tm.tournament_group_id = tg.tournament_group_id"+
			  " and tr.tournament_round_id = "+tournamentRoundID+
			  " group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, tm.tournament_id, tm.tournament_group_id,s.scorecard_id, s.scorecard_date, f.field_par,tr.tournament_round_id, tr.round_number, s.total_points, s.handicap_before, s.slope, s.course_rating, start.grup_num, tg.name"+
			  " order by grup_num";

			ResultSet RS  = stmt.executeQuery(sql);

		Table myTable = new Table();
		  myTable.setBorder(0);
		  myTable.setCellpadding(3);
		  myTable.setCellspacing(0);
		  myTable.setWidth("100%");

		IWTimestamp stampur = new IWTimestamp(tournamentRound.getRoundDate());

		Text tournamentName = new Text(tournament.getName());
		  tournamentName.setFontSize(1);

		Text roundDate = new Text(stampur.toSQLDateString());
		  roundDate.setFontSize(1);

		   int b = 1;
		int a = 0;

			while(RS.next()){

		IWTimestamp stampur2 = new IWTimestamp(tournamentRound.getRoundDate());

		int addMinutes = (RS.getInt("grup_num")-1) * interval;
		stampur2.addMinutes(addMinutes);

		String minutes = stampur2.getMinute()+"";
		if ( stampur2.getMinute() < 10 ) {
			minutes = "0"+minutes;
		}

		String s;


		String memberName = "";
		s = RS.getString("first_name"); // first_name
		if(!RS.wasNull()){  memberName +=s;       memberName += " ";     }
		s = RS.getString("middle_name"); // middle_name
		if(!RS.wasNull()){   memberName +=s;       memberName += " ";     }
		s = RS.getString("last_name"); // last_name
		if(!RS.wasNull()){   memberName +=s; }


		if ( memberName == null ) { memberName = ""; }

		Text name = new Text(memberName);
			name.setFontSize(1);


		String ssc_s = RS.getString("social_security_number");
		String ssc_s2=null;
		if ( ssc_s.length() == 10 ) {
		ssc_s2 = ssc_s.substring(0,6)+"-"+ssc_s.substring(6,ssc_s.length());
		}
		else {
		ssc_s2 =  ssc_s; // social_security_number
		}
		if(ssc_s2==null){
		ssc_s2="";
		}
		Text ssc = new Text("kt. "+ssc_s2);
			ssc.setFontSize(1);



		String abbr=null;
			s = RS.getString("abbrevation"); // abbrevation
			if(!RS.wasNull()){    abbr=s; }
		else{
		 abbr=" ";
		}

		Text club = new Text(abbr);
			club.setFontSize(1);

		String handic=null;
			s = RS.getString(14); // round_handicap
			if(!RS.wasNull()){   handic=s; }
		else{
			handic = "";
		}


			Text handicap = new Text("Fgj. "+handic);
			  handicap.setFontSize(1);

			Text startingTime = new Text("Dags. "+stampur2.getISLDate("-",true)+" kl. "+stampur2.getHour()+"."+minutes);
			  startingTime.setFontSize(1);



			int newPage = b % 10;

			Table memberTable = new Table(2,5);
			  memberTable.mergeCells(1,1,2,1);
			  memberTable.mergeCells(1,2,2,2);
			  memberTable.mergeCells(1,3,2,3);
			  memberTable.mergeCells(1,5,2,5);
			  if ( newPage == 1 ) {
				myTable.setHeight(b,"106");
			  }
			  else if ( newPage == 0 ) {
				myTable.setHeight(b,"88");
				b++;
			  }
			  else {
				myTable.setHeight(b,"112");
			  }

			  memberTable.add(tournamentName,1,1);
			  memberTable.add(name,1,2);
			  memberTable.add(ssc,1,3);
			  memberTable.add(club,1,4);
			  memberTable.add(handicap,2,4);
			  memberTable.add(startingTime,1,5);

			int position = (a+1) % 4;

			if ( position == 1 ) {
			  myTable.add(memberTable,1,b);
			}
			if ( position == 2 ) {
			  myTable.add(memberTable,2,b);
			}
			if ( position == 3 ) {
			  myTable.add(memberTable,3,b);
			}
			if ( position == 0 ) {
			  myTable.add(memberTable,4,b);
			  b++;
			}
		a++;
		}

		if(RS!=null)
			  RS.close();
			stmt.close();


				myTable.setWidth(1,"23%");
			myTable.setColumnAlignment(1,"center");
			myTable.setColumnAlignment(2,"center");
			myTable.setColumnAlignment(3,"center");
			myTable.setColumnAlignment(4,"center");
			myTable.setWidth(2,"27%");
			myTable.setWidth(3,"27%");
			myTable.setWidth(4,"23%");

			add(myTable);

		  }
		  catch(SQLException sql){add("sql villa");sql.printStackTrace();}
		  finally{
			ConnectionBroker.freeConnection(Conn);
		  }
		}


}
