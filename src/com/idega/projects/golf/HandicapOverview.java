package com.idega.projects.golf;

/**
 * Title: HandicapOverview
 * Description: Displayes the handicap of a selected golfer, ordered by date
 * Copyright:    Copyright (c) 2001
 * Company: idega co.
 * @author  Laddi
 * @version 1.3
 */

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.*;
import com.idega.jmodule.news.data.*;
import com.idega.jmodule.news.presentation.*;
import com.idega.data.*;
import com.idega.projects.golf.service.*;
import com.idega.util.text.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.templates.*;

public class HandicapOverview extends JModuleObject {

private String member_id;
private boolean isAdmin = false;

private int year = 0;
private int month = 0;
private int day = 0;
private idegaCalendar dagatalid;
private String start_year;
private String start_month;
private String start_day;
private String end_year;
private String end_month;
private String end_day;
private Table myTable;
private Form myForm = new Form();

private String headerColor = "#336660";
private String headerTextColor = "#FFFFFF";

  public HandicapOverview() {
  }

  public HandicapOverview(String member_id) {
    this.member_id=member_id;
  }

  public HandicapOverview(int member_id) {
    this.member_id=String.valueOf(member_id);
  }

  public void main(ModuleInfo modinfo) throws Exception {

        this.isAdmin=isAdministrator(modinfo);

        if ( member_id == null ) {
          member_id = modinfo.getParameter("member_id");
          if ( member_id == null ) {
            member_id = (String) modinfo.getSession().getAttribute("member_id");
            if ( member_id == null ) {
                    Member memberinn = (Member) modinfo.getSession().getAttribute("member_login");
                            if ( memberinn != null ) {
                                    member_id = String.valueOf(memberinn.getID());
                                            if ( member_id == null ) {
                                                    member_id = "1";
                                            }
                            }
                            else {
                                    member_id = "1";
                            }
            }
          }
        }

	fillTable(modinfo);
        myForm.add(myTable);

	add(myForm);

  }

	private void fillTable(ModuleInfo modinfo) throws IOException,SQLException {

		String[] dates = getDates(modinfo);

		Scorecard[] scoreCards = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id='"+member_id+"' and scorecard_date>='"+dates[0]+"' and scorecard_date<='"+(dates[1]+" 23:59:59.0")+"' and scorecard_date is not null order by scorecard_date");

		myTable = new Table();
			myTable.setWidth("100%");
			myTable.setBorder(0);
			myTable.setCellpadding(4);
			myTable.setCellspacing(0);
			myTable.setRowAlignment(1,"center");
			myTable.setRowVerticalAlignment(1,"bottom");

			Text dags = new Text("Dags");
				dags.setFontColor(headerTextColor);
				dags.setFontSize("1");
			Text vollur = new Text("Völlur");
				vollur.setFontColor(headerTextColor);
				vollur.setFontSize("1");
			Text mot = new Text("Mót");
				mot.setFontColor(headerTextColor);
				mot.setFontSize("1");
			Text teigar = new Text("Teigar");
				teigar.setFontColor(headerTextColor);
				teigar.setFontSize("1");
			Text vaegi = new Text("Slope/CR");
				vaegi.setFontColor(headerTextColor);
				vaegi.setFontSize("1");
			Text leikforgjof = new Text("Leik-<br>forgjöf");
				leikforgjof.setFontColor(headerTextColor);
				leikforgjof.setFontSize("1");
			Text punktar = new Text("Punktar");
				punktar.setFontColor(headerTextColor);
				punktar.setFontSize("1");
			Text grunnpunktar = new Text("Grunn-<br>punktar");
				grunnpunktar.setFontColor(headerTextColor);
				grunnpunktar.setFontSize("1");
			Text mismunur = new Text("Mis-<br>munur");
				mismunur.setFontColor(headerTextColor);
				mismunur.setFontSize("1");
			Text grunnforgjof = new Text("Grunn-<br>forgjöf");
				grunnforgjof.setFontColor(headerTextColor);
				grunnforgjof.setFontSize("1");
			Text ny_grunnforgjof = new Text("Ný&nbsp;grunn-<br>forgjöf");
				ny_grunnforgjof.setFontColor(headerTextColor);
				ny_grunnforgjof.setFontSize("1");
			Text skor = new Text("Skorkort");
				skor.setFontColor(headerTextColor);
				skor.setFontSize("1");

			myTable.add(dags,1,2);
			myTable.add(vollur,2,2);
			myTable.add(mot,3,2);
			myTable.add(teigar,4,2);
			myTable.add(vaegi,5,2);
			myTable.add(leikforgjof,6,2);
			myTable.add(punktar,7,2);
			myTable.add(mismunur,8,2);
			myTable.add(grunnforgjof,9,2);
			myTable.add(ny_grunnforgjof,10,2);
			myTable.add(skor,11,2);
			myTable.setRowAlignment(2,"center");
			myTable.setRowVerticalAlignment(2,"bottom");

		int grunn_punktar = 36;

		for ( int a = 0; a < scoreCards.length; a++ ) {

                    idegaTimestamp date = new idegaTimestamp(scoreCards[a].getScorecardDate());
                    Text date2 = new Text(date.getDate()+"/"+date.getMonth()+"/"+String.valueOf(date.getYear()).substring(2,4));
                            date2.setFontSize("1");

                    Window deleteWindow = new Window("Eyða skorkorti",400,220,"/handicap/handicap_delete.jsp?");

                    Link eyda = new Link(new Image("/pics/handicap/trash.gif","Eyða skorkorti",9,13),deleteWindow);
                            eyda.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

                    if ( scoreCards[a].getHandicapCorrection().equals("N") ) {
                        Field field = new Field(scoreCards[a].getFieldID());
			TeeColor teeColor = new TeeColor(scoreCards[a].getTeeColorID());
			TournamentRound tournamentRound = new TournamentRound();
			String tournament_name = "";
                        Tournament tournament = new Tournament();

			if ( scoreCards[a].getTournamentRoundId() != 1 && scoreCards[a].getTournamentRoundId() != -1 ) {
				tournamentRound = new TournamentRound(scoreCards[a].getTournamentRoundId());
				tournament = new Tournament(tournamentRound.getTournamentID());
				tournament_name = tournament.getName();
		 	}

			int teeColorID = scoreCards[a].getTeeColorID();
			String field_name = field.getName();
			String tee_name = teeColor.getName();

			double slope = (double) scoreCards[a].getSlope();
			double course_rating = (double) scoreCards[a].getCourseRating();
			String cr = scale_decimals(String.valueOf(course_rating),1);
			double field_par = (double) field.getFieldPar();
			double grunn = (double) scoreCards[a].getHandicapBefore();
			int heildarpunktar = scoreCards[a].getTotalPoints();
			float ny_grunn = scoreCards[a].getHandicapAfter();

			int leik_forgjof = 0;

			Handicap leik = new Handicap(grunn);
				leik_forgjof = leik.getLeikHandicap(slope, course_rating, field_par);

                        String grunn2 = scale_decimals(String.valueOf(grunn),1);
                        String ny_grunn3 = scale_decimals(String.valueOf(ny_grunn),1);

			Text field_name2 = new Text(field_name);
				field_name2.setFontSize("1");
                        Link fieldLink = new Link(field_name2,"/clubs/field.jsp");
                          fieldLink.addParameter("field_id",field.getID());
                          fieldLink.addParameter("union_id",field.getUnionID());
			Text tournament_id2 = new Text(tournament_name);
				if ( tournament_name == null ) {
					tournament_id2 = new Text("");
				}
				tournament_id2.setFontSize("1");
			Text slope2 = new Text(String.valueOf((int) Math.rint(slope))+"&nbsp;/&nbsp;"+cr);
				slope2.setFontSize("1");
			Text leik_forgjof2 = new Text(String.valueOf(leik_forgjof));
				leik_forgjof2.setFontSize("1");
			Text heildarpunktar2 = new Text(String.valueOf(heildarpunktar));
				heildarpunktar2.setFontSize("1");
			Text mispunktar = new Text(String.valueOf(heildarpunktar - grunn_punktar));
				mispunktar.setFontSize("1");
			Text grunn3 = new Text(grunn2);
				grunn3.setFontSize("1");
			Text ny_grunn2 = new Text(ny_grunn3);
				if ( scoreCards[a].getUpdateHandicap().equalsIgnoreCase("N") || grunn2.equalsIgnoreCase(ny_grunn3)) {
                                  ny_grunn2.setText("-");
				}
                                ny_grunn2.setFontSize("1");
			Text tee_text = new Text(tee_name);
				tee_text.setFontSize(1);
				tee_text.setFontColor(getTeeColor(teeColorID));

			myTable.setRowVerticalAlignment(a+3,"middle");
                        myTable.add(date2,1,a+3);
			myTable.add(fieldLink,2,a+3);
			if ( tournament_name != null ) {
				Link tournamentLink = new Link(tournament_id2,"/tournament/tournamentinfo.jsp");
                                  tournamentLink.addParameter("tournament_id",tournament.getID());
                                myTable.add(tournamentLink,3,a+3);
			}
                        else {
                                myTable.addText("",3,a+3);
                        }
			myTable.add(tee_text,4,a+3);
			myTable.add(slope2,5,a+3);
			myTable.add(leik_forgjof2,6,a+3);
			myTable.add(heildarpunktar2,7,a+3);
			myTable.add(mispunktar,8,a+3);

			myTable.add(grunn3,9,a+3);
			if ( Double.toString(grunn) != null ) {
			myTable.add(ny_grunn2,10,a+3);
			}

			myTable.setRowAlignment(a+3,"center");

                        GolfGroup golfGroup = new GolfGroup(member_id);
                        boolean canWrite = true;
                        if ( !isAdmin ) {
                          canWrite = golfGroup.getCanWrite();
                        }
                        if ( member_id.equalsIgnoreCase("1") ) {
                          canWrite = true;
                        }

			Window scorecardWindow = new Window("Skoða skorkort",650,475,"/handicap/handicap_skor.jsp?");
			Window updateWindow = new Window("Skoða skorkort",600,600,"/handicap/handicap.jsp?");

			Link tengill = new Link(new Image("/pics/handicap/eye.gif","Sjá skorkort",13,13),scorecardWindow);
				tengill.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

			Link update = new Link(new Image("/pics/handicap/pad.gif","Breyta skorkorti",11,13),updateWindow);
				update.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

			myTable.add(tengill,11,a+3);
			myTable.addText("&nbsp;",11,a+3);
			if ( canWrite && tournament_name.length() == 0 ) {
                          myTable.add(update,11,a+3);
			}
			if ( isAdmin || member_id.equalsIgnoreCase("1") ) {
				myTable.addText("&nbsp;",11,a+3);
				myTable.add(eyda,11,a+3);
			}

                    }

                    else {

                      myTable.mergeCells(2,a+3,8,a+3);
                      myTable.setRowAlignment(a+3,"center");

                      Text updateText = new Text("- Leiðrétting á forgjöf -");
                        updateText.setFontSize(1);
                      Text handicapBefore = new Text(scoreCards[a].getHandicapBefore()+"");
                        handicapBefore.setFontSize(1);
                      Text handicapAfter = new Text(scoreCards[a].getHandicapAfter()+"");
                        handicapAfter.setFontSize(1);

                      myTable.add(date2,1,a+3);
                      myTable.add(updateText,2,a+3);
                      myTable.add(handicapBefore,9,a+3);
                      myTable.add(handicapAfter,10,a+3);
                      myTable.addText("",11,a+3);
                      if ( isAdmin || member_id.equalsIgnoreCase("1") ) {
                              myTable.add(eyda,11,a+3);
                      }

                    }

		}

		myTable.mergeCells(1,1,11,1);
		myTable.setAlignment(1,1,"right");
		myTable.setRowColor(1,headerColor);
		myTable.setRowColor(2,headerColor);
                myTable.setHeight(1,"20");
                myTable.setHeight(2,"20");
                myTable.setHeight(myTable.getRows(),"100%");
		getForm();
	}

	private void getForm() throws IOException {

		myForm.add(new HiddenInput("member_id",member_id));

			DropdownMenu start_y = new DropdownMenu("start_year");
				for ( int y = 2000 ; y <= year ; y++ ) {

					start_y.addMenuElement(String.valueOf(y),String.valueOf(y));

				}

				start_y.setSelectedElement(start_year);
				start_y.setAttribute("style","font-size: 8pt");

			DropdownMenu start_m = new DropdownMenu("start_month");

				int mon = 12;

				for ( int m = 1 ; m <= mon ; m++ ) {

					start_m.addMenuElement(String.valueOf(m),dagatalid.getNameOfMonth(m).toLowerCase().substring(0,3)+".");

				}

				start_m.setSelectedElement(start_month);
				start_m.setAttribute("style","font-size: 8pt");

			DropdownMenu start_d = new DropdownMenu("start_day");

				for ( int d = 1 ; d <= 31 ; d++ ) {

					start_d.addMenuElement(String.valueOf(d),String.valueOf(d)+".");

				}

				start_d.setSelectedElement(start_day);
				start_d.setAttribute("style","font-size: 8pt");

			DropdownMenu end_y = new DropdownMenu("end_year");
				for ( int y = 2000 ; y <= year ; y++ ) {

					end_y.addMenuElement(String.valueOf(y),String.valueOf(y));

				}

				end_y.setSelectedElement(end_year);
				end_y.setAttribute("style","font-size: 8pt");

			DropdownMenu end_m = new DropdownMenu("end_month");

				mon = 12;

				for ( int m = 1 ; m <= mon ; m++ ) {

					end_m.addMenuElement(String.valueOf(m),dagatalid.getNameOfMonth(m).toLowerCase().substring(0,3)+".");

				}

				end_m.setSelectedElement(end_month);
				end_m.setAttribute("style","font-size: 8pt");

			DropdownMenu end_d = new DropdownMenu("end_day");

				for ( int d = 1 ; d <= 31 ; d++ ) {

					end_d.addMenuElement(String.valueOf(d),String.valueOf(d)+".");

				}

				end_d.setSelectedElement(end_day);
				end_d.setAttribute("style","font-size: 8pt");

			SubmitButton skoda = new SubmitButton("Sækja yfirlit");
				skoda.setAttribute("style","font-size: 8pt");

				Text fra = new Text("Frá: ");
					fra.setBold();
					fra.setFontColor(headerTextColor);
				Text til = new Text("Til: ");
					til.setBold();
					til.setFontColor(headerTextColor);
				Text strik = new Text("&nbsp;");
				Text bil = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

				myTable.add(fra,1,1);
				myTable.add(start_d,1,1);
				myTable.add(strik,1,1);
				myTable.add(start_m,1,1);
				myTable.add(strik,1,1);
				myTable.add(start_y,1,1);

				myTable.add(bil,1,1);

				myTable.add(til,1,1);
				myTable.add(end_d,1,1);
				myTable.add(strik,1,1);
				myTable.add(end_m,1,1);
				myTable.add(strik,1,1);
				myTable.add(end_y,1,1);

				myTable.add(strik,1,1);
				myTable.add(strik,1,1);

				myTable.add(skoda,1,1);

				myTable.add(strik,1,1);

	}

	private String[] getDates(ModuleInfo modinfo) throws IOException {

		String[] dates = {"",""};

		dagatalid = new idegaCalendar();
			year = dagatalid.getYear();
			month = dagatalid.getMonth();
			day = dagatalid.getDay();

		this.start_year = modinfo.getParameter("start_year");
			if ( start_year == null ) {
				start_year = String.valueOf(year - 1);
			}

		this.start_month = modinfo.getParameter("start_month");
			if ( start_month == null ) {
				start_month = String.valueOf(month);
			}
		this.start_day = modinfo.getParameter("start_day");
			if ( start_day == null ) {
				start_day = String.valueOf(day);
			}

			if ( Integer.parseInt(start_day) > dagatalid.getLengthOfMonth(start_month,start_year) ) {
				start_day = String.valueOf(dagatalid.getLengthOfMonth(start_month,start_year));
			}

		this.end_year = modinfo.getParameter("end_year");
			if ( end_year == null ) {
				end_year = String.valueOf(year);
			}
		this.end_month = modinfo.getParameter("end_month");
			if ( end_month == null ) {
				end_month = String.valueOf(month);
			}
		this.end_day = modinfo.getParameter("end_day");
			if ( end_day == null ) {
				end_day = String.valueOf(day);
			}

			if ( Integer.parseInt(end_day) > dagatalid.getLengthOfMonth(end_month,end_year) ) {
				end_day = String.valueOf(dagatalid.getLengthOfMonth(end_month,end_year));
			}

		String start_time = start_year+"-"+start_month+"-"+start_day;
		String end_time = end_year+"-"+end_month+"-"+end_day;

		dates[0] = start_time;
		dates[1] = end_time;

		return dates;
	}

	private String reiknaHandicap(double grunn, int heildarpunktar) throws IOException{

		double breyting;

		if ( heildarpunktar >= 0 ) {

			breyting = heildarpunktar - 36;

		}

		else {

			breyting = 0.0;

		}

		Handicap forgjof = new Handicap(grunn);

		double nyForgjof = forgjof.getNewHandicap(breyting);

		if ( nyForgjof > 36.0 ) {

			nyForgjof = 36.0;

		}

		BigDecimal test2 = new BigDecimal(nyForgjof);

		String nyForgjof2 = test2.setScale(1,5).toString();

		return nyForgjof2;
	}

	private double reiknaHandicap2(double grunn, int heildarpunktar) throws IOException{

		double breyting;

		if ( heildarpunktar >= 0 ) {

			breyting = heildarpunktar - 36;

		}

		else {

			breyting = 0.0;

		}

		Handicap forgjof = new Handicap(grunn);

		double nyForgjof = forgjof.getNewHandicap(breyting);

		if ( nyForgjof > 36.0 ) {

			nyForgjof = 36.0;

		}

		return nyForgjof;
	}

	private String formatDate(String date) {

		 idegaCalendar dagatal = new idegaCalendar();

		 String ReturnString = date.substring(8, 10);

		 ReturnString += "/"+date.substring(5,7);
		 ReturnString += "/"+date.substring(2, 4);

 	return ReturnString;

	}

	private String scale_decimals(String nyForgjof,int scale) throws IOException {

		BigDecimal test2 = new BigDecimal(nyForgjof);

		String nyForgjof2 = test2.setScale(scale,5).toString();

		return nyForgjof2;

	}

	private void update_handicap(String member_id) throws SQLException,IOException {

		MemberInfo member = new MemberInfo(Integer.parseInt(member_id));

		double grunn = (double) member.getFloatColumnValue("handicap_first");
		int tee_id = 0;

		Scorecard[] scorecard = (Scorecard[]) (new Scorecard()).findAllByColumnOrdered("member_id",member_id,"scorecard_date");
		for (int m=0; m < scorecard.length; m++) {
			Stroke[] stroke = (Stroke[]) (new Stroke()).findAllByColumn("scorecard_id",scorecard[m].toString());

				float slope = (float) scorecard[m].getSlope();
				float course_rating = scorecard[m].getCourseRating();
				int field_id = scorecard[m].getFieldID();

			Field field = new Field(field_id);
				float field_par = (float) field.getIntColumnValue("field_par");

			Handicap leikForgjof = new Handicap(grunn);
				int leik = leikForgjof.getLeikHandicap((double) slope,(double) course_rating,(double) field_par);

			int leikpunktar = leik + 36;
			int punktar = leikpunktar/18;
			int afgangur = leikpunktar%18;
			int punktar2 = punktar + 1;
			int punktar3 = 0;
			int heildarpunktar = 0;
			int hole_handicap = 0;
			int hole_par = 0;

			Stroke[] stroke2 = (Stroke[]) (new Stroke()).findAllByColumn("scorecard_id",scorecard[m].toString());

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

				heildarpunktar += punktar3;

				stroke2[c].setPointCount(punktar3);
				stroke2[c].update();

			}

			if ( stroke2.length <= 9 ) {
				heildarpunktar *= 2;
			}

			scorecard[m].setHandicapBefore((float) grunn);
			scorecard[m].setTotalPoints(heildarpunktar);
			scorecard[m].update();

			grunn = reiknaHandicap2((double)grunn,heildarpunktar);

		}

		member.setHandicap((float) grunn);
		member.update();

	}

	private String getTeeColor(int teeColorID) throws IOException {

		String litur = "";

		switch (teeColorID)  {
			case 1 : litur ="FFFFFF";
			break;

			case 2 : litur="FFFF00";
			break;

			case 3 : litur="5757FF";
			break;

			case 4 : litur="FF5757";
			break;

			case 5 : litur="5757FF";
			break;

			case 6 : litur="FF5757";
			break;
		}

		return litur;

	}

    public void setHeaderColor(String headerColor) {
      this.headerColor=headerColor;
    }

    public void setHeaderTextColor(String headerTextColor) {
      this.headerTextColor=headerTextColor;
    }

}