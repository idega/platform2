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
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

public class HandicapOverview extends JModuleObject {

private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
protected IWResourceBundle iwrb;
protected IWBundle iwb;

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
private boolean noIcons = false;

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

        iwrb = getResourceBundle(modinfo);
        iwb = getBundle(modinfo);

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

		Member member = new Member(Integer.parseInt(this.member_id));
    MemberInfo memberInfo = member.getMemberInfo();

    String[] dates = getDates(modinfo);

    Scorecard[] scoreCards = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id='"+member_id+"' and scorecard_date>='"+dates[0]+"' and scorecard_date<='"+(dates[1]+" 23:59:59.0")+"' and scorecard_date is not null order by scorecard_date");
    Scorecard[] scoreCardsBefore = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id = "+member_id+" and scorecard_date < '"+dates[0]+"' order by scorecard_date desc");

    myTable = new Table();
			myTable.setWidth("100%");
			myTable.setBorder(0);
			myTable.setCellpadding(4);
			myTable.setCellspacing(0);
			myTable.setRowAlignment(1,"center");
			myTable.setRowVerticalAlignment(1,"bottom");

			Text dags = new Text(iwrb.getLocalizedString("handicap.date","Date"));
				dags.setFontColor(headerTextColor);
				dags.setFontSize("1");
        if ( noIcons ) {
          dags.setBold();
        }
			Text vollur = new Text(iwrb.getLocalizedString("handicap.course","Course"));
				vollur.setFontColor(headerTextColor);
				vollur.setFontSize("1");
        if ( noIcons ) {
          vollur.setBold();
        }
			Text mot = new Text(iwrb.getLocalizedString("handicap.tournament","Tournament"));
				mot.setFontColor(headerTextColor);
				mot.setFontSize("1");
        if ( noIcons ) {
          mot.setBold();
        }
			Text teigar = new Text(iwrb.getLocalizedString("handicap.tees","Tees"));
				teigar.setFontColor(headerTextColor);
				teigar.setFontSize("1");
        if ( noIcons ) {
          teigar.setBold();
        }
			Text vaegi = new Text(iwrb.getLocalizedString("handicap.slope","Slope/CR"));
				vaegi.setFontColor(headerTextColor);
				vaegi.setFontSize("1");
        if ( noIcons ) {
          vaegi.setBold();
        }
			Text leikforgjof = new Text(iwrb.getLocalizedString("handicap.course_handicap","Course"));
        leikforgjof.addBreak();
        leikforgjof.addToText(iwrb.getLocalizedString("handicap.handicap_lowercase","handicap"));
				leikforgjof.setFontColor(headerTextColor);
				leikforgjof.setFontSize("1");
        if ( noIcons ) {
          leikforgjof.setBold();
        }
			Text punktar = new Text(iwrb.getLocalizedString("handicap.points","Points"));
				punktar.setFontColor(headerTextColor);
				punktar.setFontSize("1");
        if ( noIcons ) {
          punktar.setBold();
        }
			Text mismunur = new Text(iwrb.getLocalizedString("handicap.difference","Difference"));
				mismunur.setFontColor(headerTextColor);
				mismunur.setFontSize("1");
        if ( noIcons ) {
          mismunur.setBold();
        }
			Text grunnforgjof = new Text(iwrb.getLocalizedString("handicap.handicap","Handicap"));
				grunnforgjof.setFontColor(headerTextColor);
				grunnforgjof.setFontSize("1");
        if ( noIcons ) {
          grunnforgjof.setBold();
        }
			Text ny_grunnforgjof = new Text(iwrb.getLocalizedString("handicap.new","New"));
        ny_grunnforgjof.addBreak();
        ny_grunnforgjof.addToText(iwrb.getLocalizedString("handicap.handicap_lowercase","handicap"));
				ny_grunnforgjof.setFontColor(headerTextColor);
				ny_grunnforgjof.setFontSize("1");
        if ( noIcons ) {
          ny_grunnforgjof.setBold();
        }
			Text skor = new Text(iwrb.getLocalizedString("handicap.scorecard","Scorecard"));
				skor.setFontColor(headerTextColor);
				skor.setFontSize("1");
        if ( noIcons ) {
          skor.setBold();
        }

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

      Window deleteWindow = new Window(iwrb.getLocalizedString("handicap.scorecard_delete","Delete scorecard"),400,220,"/handicap/handicap_delete.jsp?");

      Link eyda = new Link(iwb.getImage("shared/trash.gif",iwrb.getLocalizedString("handicap.scorecard_delete","Delete scorecard"),9,13),deleteWindow);
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
			String cr = TextSoap.singleDecimalFormat(String.valueOf(course_rating));
			double field_par = (double) field.getFieldPar();
			double grunn = (double) scoreCards[a].getHandicapBefore();
			int heildarpunktar = scoreCards[a].getTotalPoints();
			float ny_grunn = scoreCards[a].getHandicapAfter();

			int leik_forgjof = 0;

			Handicap leik = new Handicap(grunn);
				leik_forgjof = leik.getLeikHandicap(slope, course_rating, field_par);

      float realHandicap = 0;

     if ( a == 0 ) {
        if ( scoreCardsBefore.length > 0 ) {
          realHandicap = scoreCardsBefore[0].getHandicapAfter();
        }
        else {
          realHandicap = memberInfo.getFirstHandicap();
        }
      }
      else {
        realHandicap = scoreCards[a-1].getHandicapAfter();
      }

      Handicap realLeik = new Handicap((double)realHandicap);
      int realPlayHandicap = realLeik.getLeikHandicap(slope, course_rating, field_par);

      boolean showRealHandicap = false;
      if ( scoreCards[a].getTournamentRoundId() > 1 ) {
        if ( member.getGender().equalsIgnoreCase("m") ) {
          if ( (float) realPlayHandicap > tournament.getMaxHandicap() ) {
            showRealHandicap = true;
          }
        }
        else if ( member.getGender().equalsIgnoreCase("f") ) {
          if ( (float) realPlayHandicap > tournament.getFemaleMaxHandicap() ) {
            showRealHandicap = true;
          }
        }
        if ( tournament.getTournamentType().getModifier() != -1 ) {
          showRealHandicap = true;
        }
      }
      if ( tournamentRound.getRoundNumber() > 1 ) {
        showRealHandicap = true;
      }

      int realPoints = 0;
      if ( showRealHandicap ) {
        realPoints = Handicap.getTotalPoints(scoreCards[a].getID(),realPlayHandicap);
      }

      String grunn2 = TextSoap.singleDecimalFormat(String.valueOf(grunn));
      String ny_grunn3 = TextSoap.singleDecimalFormat(String.valueOf(ny_grunn));

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
      Text realLeikForgjof = new Text();
        if ( showRealHandicap ) {
          realLeikForgjof.setText(""+realPlayHandicap);
        }
        realLeikForgjof.setFontSize(1);
			Text heildarpunktar2 = new Text(String.valueOf(heildarpunktar));
				heildarpunktar2.setFontSize("1");
      Text realHeildarPunktar = new Text();
        if ( showRealHandicap ) {
          realHeildarPunktar.setText(""+realPoints);
        }
        realHeildarPunktar.setFontSize(1);
			Text mispunktar = new Text(String.valueOf(heildarpunktar - grunn_punktar));
				mispunktar.setFontSize("1");
      Text realMisPunktar = new Text();
        if ( showRealHandicap ) {
          realMisPunktar.setText(""+String.valueOf(realPoints - grunn_punktar));
        }
        realMisPunktar.setFontSize(1);
			Text grunn3 = new Text(grunn2);
				grunn3.setFontSize("1");
      Text realGrunn = new Text();
        if ( showRealHandicap ) {
          realGrunn.setText(TextSoap.singleDecimalFormat(String.valueOf(realHandicap)));
        }
        realGrunn.setFontSize(1);
			Text ny_grunn2 = new Text(ny_grunn3);
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
      if ( showRealHandicap ) {
        myTable.add(realLeikForgjof,6,a+3);
      }
      else {
  			  myTable.add(leik_forgjof2,6,a+3);
      }

      if ( showRealHandicap ) {
        myTable.add(realHeildarPunktar,7,a+3);
      }
      else {
        myTable.add(heildarpunktar2,7,a+3);
      }

      if ( showRealHandicap ) {
        myTable.add(realMisPunktar,8,a+3);
      }
      else {
			  myTable.add(mispunktar,8,a+3);
      }

      if ( showRealHandicap ) {
        myTable.add(realGrunn,9,a+3);
      }
      else {
			  myTable.add(grunn3,9,a+3);
      }

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

			Window scorecardWindow = new Window(iwrb.getLocalizedString("handicap.view_scorecard","View scorecard"),650,475,"/handicap/handicap_skor.jsp?");
			Window updateWindow = new Window(iwrb.getLocalizedString("handicap.view_scorecard","View scorecard"),600,600,"/handicap/handicap.jsp?");
			Window updateWindow2 = new Window(iwrb.getLocalizedString("handicap.register_statistics","Register statistics"),600,350,"/handicap/handicap_statistics.jsp?");

			Link tengill = new Link(iwb.getImage("shared/eye.gif",iwrb.getLocalizedString("handicap.view_scorecard","View scorecard"),13,13),scorecardWindow);
				tengill.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

			Link update = new Link(iwb.getImage("shared/pad.gif",iwrb.getLocalizedString("handicap.update_scorecard","Change scorecard"),11,13),updateWindow);
				update.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

			Link update2 = new Link(iwb.getImage("shared/pad.gif",iwrb.getLocalizedString("handicap.register_statistics","Register statistics"),11,13),updateWindow2);
				update2.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

			myTable.add(tengill,11,a+3);
			myTable.addText("&nbsp;",11,a+3);

      if ( isAdmin ) {
        myTable.add(update,11,a+3);
      }
      else {
        if ( canWrite && tournament_name.length() == 0 && !noIcons ) {
          myTable.add(update,11,a+3);
        }
        if ( tournament_name.length() > 0 && !noIcons ) {
          myTable.add(update2,11,a+3);
        }
      }

      if ( isAdmin || member_id.equalsIgnoreCase("1") ) {
				myTable.addText("&nbsp;",11,a+3);
				myTable.add(eyda,11,a+3);
			}

      }

      else {

        myTable.mergeCells(2,a+3,8,a+3);
        myTable.setRowAlignment(a+3,"center");

        Text updateText = new Text("- "+iwrb.getLocalizedString("handicap.handicap_correction","Handicap correction")+" -");
          updateText.setFontSize(1);
        Text handicapBefore = new Text(TextSoap.singleDecimalFormat((double)scoreCards[a].getHandicapBefore()));;
          handicapBefore.setFontSize(1);
        Text handicapAfter = new Text(TextSoap.singleDecimalFormat((double)scoreCards[a].getHandicapAfter()));
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

		int rows = myTable.getRows()+1;
                myTable.mergeCells(1,1,11,1);
		myTable.setAlignment(1,1,"right");
		myTable.setRowColor(1,headerColor);
		myTable.setRowColor(2,headerColor);
                myTable.setHeight(1,"20");
                myTable.setHeight(2,"20");
                myTable.setHeight(rows+1,"100%");
		getForm();

    Window recalculate = new Window(iwrb.getLocalizedString("handicap.recalculate","Recalculate"),350,200,"/handicap/recalculate.jsp?");

    Link recalculateLink = new Link(iwrb.getImage("buttons/update.gif",iwrb.getLocalizedString("handicap.update_handicap","Update handicap"),76,19),recalculate);
            recalculateLink.addParameter("member_id",member_id);

    myTable.mergeCells(1,rows,11,rows);
    myTable.setAlignment(1,rows,"right");
    if ( Integer.parseInt(this.member_id) > 1 && !noIcons ) {
      myTable.add(recalculateLink,1,rows);
    }
    if ( noIcons ) {
      myTable.setAlignment(1,1,"left");
    }

	}

	private void getForm() throws IOException {

		myForm.add(new HiddenInput("member_id",member_id));
                myForm.add(new HiddenInput("handicap_action","overView"));

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

			SubmitButton skoda = new SubmitButton(iwrb.getLocalizedString("handicap.get_overview","Get overview"));
				skoda.setAttribute("style","font-size: 8pt");

				Text fra = new Text(iwrb.getLocalizedString("handicap.from","From")+": ");
					fra.setBold();
					fra.setFontColor(headerTextColor);
				Text til = new Text(iwrb.getLocalizedString("handicap.to","To")+": ");
					til.setBold();
					til.setFontColor(headerTextColor);
				Text strik = new Text("&nbsp;");
				Text bil = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

				myTable.add(fra,1,1);
				myTable.add(start_d,1,1);
				myTable.add(start_m,1,1);
				myTable.add(start_y,1,1);

				myTable.add(bil,1,1);

				myTable.add(til,1,1);
				myTable.add(end_d,1,1);
				myTable.add(end_m,1,1);
				myTable.add(end_y,1,1);

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

	private String formatDate(String date) {

		 idegaCalendar dagatal = new idegaCalendar();

		 String ReturnString = date.substring(8, 10);

		 ReturnString += "/"+date.substring(5,7);
		 ReturnString += "/"+date.substring(2, 4);

 	return ReturnString;

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

    public void noIcons() {
      this.noIcons = true;
    }

    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }
}