package is.idega.idegaweb.golf;

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
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.*;
import is.idega.idegaweb.golf.*;
import com.idega.jmodule.news.data.*;
import com.idega.jmodule.news.presentation.*;
import com.idega.data.*;
import is.idega.idegaweb.golf.service.*;
import com.idega.util.text.*;
import is.idega.idegaweb.golf.entity.*;
import is.idega.idegaweb.golf.templates.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

public class HandicapOverview extends Block {

private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
protected IWResourceBundle iwrb;
protected IWBundle iwb;

private String member_id;
private boolean isAdmin = false;

//Bjarni added this boolean and color String 14.08.01
private boolean isDefaultColors = true;
private String teeTextColor;

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

private boolean setDifferentOverviewButton = false;
private String getOverviewButtonParameterName, getOverviewButtonParameterValue;

private boolean isTilPicture = false;
private boolean isFraPicture = false;
private String tilPictureUrlInBundle;
private String fraPictureUrlInBundle;
private String getOverViewParameterName;
private String getOverViewParameterValue;

private String headerColor = "#336660";
private String headerTextColor = "#FFFFFF";
private String headerBackgroundColor;

private String viewScoreIceonUrlInBundle = "shared/eye.gif";
private String getOverviewButtonImageUrlInBundle = "getOverview.gif";
private Text headerText = new Text();  //Bjarni added 14.08.01
private Text tableText = new Text();
private Text tilFraText = new Text();
private Link textLink = new Link();

{  //text properties initialized.
  headerText.setFontSize(1);
  headerText.setFontColor(headerTextColor);

  tilFraText.setFontColor(headerTextColor);
  tilFraText.setBold();

  tableText.setFontSize(1);

  textLink.setFontSize(1);
}


  public HandicapOverview() {
  }

  public HandicapOverview(String member_id) {
    this.member_id=member_id;
  }

  public HandicapOverview(int member_id) {
    this.member_id=String.valueOf(member_id);
  }

  public void main(IWContext iwc) throws Exception {

        iwrb = getResourceBundle(iwc);
        iwb = getBundle(iwc);

        this.isAdmin=isAdministrator(iwc);

        if ( member_id == null ) {
          member_id = iwc.getParameter("member_id");
          if ( member_id == null ) {
            member_id = (String) iwc.getSession().getAttribute("member_id");
            if ( member_id == null ) {
                    Member memberinn = (Member) iwc.getSession().getAttribute("member_login");
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

	fillTable(iwc);
        myForm.add(myTable);

	add(myForm);

  }

	private void fillTable(IWContext iwc) throws IOException,SQLException {

		Member member = new Member(Integer.parseInt(this.member_id));
    MemberInfo memberInfo = member.getMemberInfo();

    String[] dates = getDates(iwc);

    Scorecard[] scoreCards = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id='"+member_id+"' and scorecard_date>='"+dates[0]+"' and scorecard_date<='"+(dates[1]+" 23:59:59.0")+"' and scorecard_date is not null order by scorecard_date");
    Scorecard[] scoreCardsBefore = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id = "+member_id+" and scorecard_date < '"+dates[0]+"' order by scorecard_date desc");

    myTable = new Table();
			myTable.setWidth("100%");
			myTable.setBorder(0);
			myTable.setCellpadding(4);
			myTable.setCellspacing(0);
			myTable.setRowAlignment(1,"center");
			myTable.setRowVerticalAlignment(1,"bottom");


                        Text dags;
                        dags = ((Text) this.headerText.clone());
                        dags.setText(iwrb.getLocalizedString("handicap.date","Date"));
        if ( noIcons ) {
          dags.setBold();
        }
			Text vollur;
                        vollur = ( (Text) this.headerText.clone());
                        vollur.setText(iwrb.getLocalizedString("handicap.course","Course"));
        if ( noIcons ) {
          vollur.setBold();
        }
			Text mot;
                        mot = ( (Text) this.headerText.clone());
                        mot.setText(iwrb.getLocalizedString("handicap.tournament","Tournament"));
        if ( noIcons ) {
          mot.setBold();
        }
			Text teigar;
                        teigar = ( (Text) this.headerText.clone());
                        teigar.setText(iwrb.getLocalizedString("handicap.tees","Tees"));
        if ( noIcons ) {
          teigar.setBold();
        }
			Text vaegi = ( (Text) this.headerText.clone());
                        vaegi.setText(iwrb.getLocalizedString("handicap.slope","Slope/CR"));
        if ( noIcons ) {
          vaegi.setBold();
        }
			Text leikforgjof = ( (Text) this.headerText.clone());
                        leikforgjof.setText(iwrb.getLocalizedString("handicap.course_handicap","Course"));
        leikforgjof.addBreak();
        leikforgjof.addToText(iwrb.getLocalizedString("handicap.handicap_lowercase","handicap"));
        if ( noIcons ) {
          leikforgjof.setBold();
        }
			Text punktar = ( (Text) headerText.clone());
                        punktar.setText(iwrb.getLocalizedString("handicap.points","Points"));
        if ( noIcons ) {
          punktar.setBold();
        }
			Text mismunur = ( (Text) headerText.clone());
                        mismunur.setText(iwrb.getLocalizedString("handicap.difference","Difference"));
        if ( noIcons ) {
          mismunur.setBold();
        }
			Text grunnforgjof = ( (Text) headerText.clone());
                        grunnforgjof.setText(iwrb.getLocalizedString("handicap.handicap","Handicap"));
        if ( noIcons ) {
          grunnforgjof.setBold();
        }
			Text ny_grunnforgjof = ( (Text) headerText.clone());
                        ny_grunnforgjof.setText(iwrb.getLocalizedString("handicap.new","New"));
        ny_grunnforgjof.addBreak();
        ny_grunnforgjof.addToText(iwrb.getLocalizedString("handicap.handicap_lowercase","handicap"));
        if ( noIcons ) {
          ny_grunnforgjof.setBold();
        }
			Text skor = ( (Text) headerText.clone());
                        skor.setText(iwrb.getLocalizedString("handicap.scorecard","Scorecard"));
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
      Text date2 = ((Text) tableText.clone());
      date2.setText(date.getDate()+"/"+date.getMonth()+"/"+String.valueOf(date.getYear()).substring(2,4));

      Window deleteWindow = new Window(iwrb.getLocalizedString("handicap.scorecard_delete","Delete scorecard"),400,220,"/handicap/handicap_delete.jsp");

/*Mynd-Link*/      Link eyda = new Link(iwb.getImage("shared/trash.gif",iwrb.getLocalizedString("handicap.scorecard_delete","Delete scorecard"),9,13),deleteWindow);
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

			/*Text field_name2 = new Text(field_name);
				field_name2.setFontSize("1");*/
      Link fieldLink = ((Link) textLink.clone());
      fieldLink.setText(field_name);
      fieldLink.setURL("/clubs/field.jsp");
        fieldLink.setFontSize("1");
        fieldLink.addParameter("field_id",field.getID());
        fieldLink.addParameter("union_id",field.getUnionID());
		/*Text tournament_id2 = new Text(tournament_name);
				if ( tournament_name == null ) {
					tournament_id2 = new Text("");
				}
				tournament_id2.setFontSize("1");*/
			Text slope2 = ((Text) tableText.clone());
                        slope2.setText(String.valueOf((int) Math.rint(slope))+"&nbsp;/&nbsp;"+cr);
			Text leik_forgjof2 = ((Text) tableText.clone());
                        leik_forgjof2.setText(String.valueOf(leik_forgjof));
      Text realLeikForgjof = (Text) tableText.clone();
        if ( showRealHandicap ) {
          realLeikForgjof.setText(""+realPlayHandicap);
        }
			Text heildarpunktar2 = ((Text) tableText.clone());
                        heildarpunktar2.setText(String.valueOf(heildarpunktar));
      Text realHeildarPunktar = (Text) tableText.clone();
        if ( showRealHandicap ) {
          realHeildarPunktar.setText(""+realPoints);
        }
			Text mispunktar = ((Text) tableText.clone());
                        mispunktar.setText(String.valueOf(heildarpunktar - grunn_punktar));
      Text realMisPunktar = (Text) tableText.clone();
        if ( showRealHandicap ) {
          realMisPunktar.setText(""+String.valueOf(realPoints - grunn_punktar));
        }
			Text grunn3 = ((Text) tableText.clone());
                        grunn3.setText(grunn2);
      Text realGrunn = (Text) tableText.clone();
        if ( showRealHandicap ) {
          realGrunn.setText(TextSoap.singleDecimalFormat(String.valueOf(realHandicap)));
        }
        realGrunn.setFontSize(1);
			Text ny_grunn2 = ((Text) tableText.clone());
                        ny_grunn2.setText(ny_grunn3);
        ny_grunn2.setFontSize("1");
			Text tee_text = ((Text) tableText.clone());
                        tee_text.setText(tee_name);
				tee_text.setFontSize(1);

                                if (isDefaultColors) tee_text.setFontColor(getTeeColor(teeColorID));
                                else tee_text.setFontColor(teeTextColor);

			myTable.setRowVerticalAlignment(a+3,"middle");
                        myTable.add(date2,1,a+3);
			myTable.add(fieldLink,2,a+3);
			if ( tournament_name != null ) {
        				//Link tournamentLink = new Link(tournament_name, "/tournament/tournamentinfo.jsp");
                                        Link tournamentLink = ((Link) textLink.clone());
                                          tournamentLink.setText(tournament_name);
                                          tournamentLink.setURL("/tournament/tournamentinfo.jsp");
                                          tournamentLink.setFontSize(1);
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

			Window scorecardWindow = new Window(iwrb.getLocalizedString("handicap.view_scorecard","View scorecard"),650,475,"/handicap/handicap_skor.jsp");
			Window updateWindow = new Window(iwrb.getLocalizedString("handicap.view_scorecard","View scorecard"),600,600,"/handicap/handicap.jsp");
			Window updateWindow2 = new Window(iwrb.getLocalizedString("handicap.register_statistics","Register statistics"),600,350,"/handicap/handicap_statistics.jsp");

/*MYNDA LINKUR*/		Link tengill = new Link(iwb.getImage(viewScoreIceonUrlInBundle, iwrb.getLocalizedString("handicap.view_scorecard","View scorecard"),13,13),scorecardWindow);  //
        /*if ( noIcons ) {
          tengill = new Link(iwb.getImage("shared/eye2.gif",iwrb.getLocalizedString("handicap.view_scorecard","View scorecard"),13,13),scorecardWindow);  //
        }*/
				tengill.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

			Link update = new Link(iwb.getImage("shared/pad.gif",iwrb.getLocalizedString("handicap.update_scorecard","Change scorecard"),11,13),updateWindow);  //
				update.addParameter("scorecard_id",String.valueOf(scoreCards[a].getID()));

			Link update2 = new Link(iwb.getImage("shared/pad.gif",iwrb.getLocalizedString("handicap.register_statistics","Register statistics"),11,13),updateWindow2);  //
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

        Text updateText = ((Text) tableText.clone());
        updateText.setText("- "+iwrb.getLocalizedString("handicap.handicap_correction","Handicap correction")+" -");
        Text handicapBefore = ((Text) tableText.clone());
        handicapBefore.setText(TextSoap.singleDecimalFormat((double)scoreCards[a].getHandicapBefore()));;
        Text handicapAfter = ((Text) tableText.clone());
        handicapAfter.setText(TextSoap.singleDecimalFormat((double)scoreCards[a].getHandicapAfter()));

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
                if (this.headerBackgroundColor != null) {
                  myTable.setRowColor(2, this.headerBackgroundColor);
                }else {
  		  myTable.setRowColor(2,headerColor);
                }
                myTable.setHeight(1,"20");
                myTable.setHeight(2,"20");
                myTable.setHeight(rows+1,"100%");
		getForm();

    Window recalculate = new Window(iwrb.getLocalizedString("handicap.recalculate","Recalculate"),350,200,"/handicap/recalculate.jsp");

/*MYNDA LINKUR*/  Link recalculateLink = new Link(iwrb.getImage("buttons/update.gif",iwrb.getLocalizedString("handicap.update_handicap","Update handicap"),76,19),recalculate);  //
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
        if ( setDifferentOverviewButton ) {
          skoda = new SubmitButton(iwrb.getImage(getOverviewButtonImageUrlInBundle), getOverviewButtonParameterName, getOverviewButtonParameterValue);
        }
                                Table dummyTable = new Table(12,1);
                                dummyTable.setCellpadding(0);
                                dummyTable.setCellspacing(0);

                                Image iFra = new Image();
                                Image iTil = new Image();

                                Text fra = new Text();
                                Text til = new Text();

                                if  (!isFraPicture){
                                  fra = ( (Text) tilFraText.clone());
                                  fra.setText(iwrb.getLocalizedString("handicap.from","From")+": ");
                                }
                                else{
                                  iFra = iwrb.getImage(fraPictureUrlInBundle, iwrb.getLocalizedString("handicap.from","From")+": ");
                                }
                                if (!isTilPicture){
                                  til = ( (Text) tilFraText.clone());
                                  til.setText(iwrb.getLocalizedString("handicap.to","To")+": ");
                                }
                                else{
                                  iTil = iwrb.getImage(tilPictureUrlInBundle, iwrb.getLocalizedString("handicap.to","To")+": ");
                                }
				Text strik = new Text("&nbsp;");
				Text bil = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

				if (!isFraPicture) {

                                }
                                else {
                                }
				dummyTable.add(start_d,2,1);
				dummyTable.add(start_m,3,1);
				dummyTable.add(start_y,4,1);

				dummyTable.add(bil,5,1);

				if (!isFraPicture) {
                                  dummyTable.add(fra,1,1);
                                  dummyTable.add(til,6,1);
                                  dummyTable.add(skoda,11,1);
                                }
                                else {
                                  Table dummyTable2 = new Table(2,1);
                                  dummyTable2.add(iFra,1,1);
                                  dummyTable.add(dummyTable2,1,1);

                                  Table dummyTable3 = new Table(2,1);
                                  dummyTable3.add(iTil,1,1);
                                  dummyTable.add(dummyTable3,7,1);

                                  Table dummyTable4 = new Table(2,1);
//                                  dummyTable4.add(" ",1,1);
                                  dummyTable4.add(skoda,2,1);
                                  dummyTable.add(dummyTable4,11,1);
                                }
				dummyTable.add(end_d,8,1);
				dummyTable.add(end_m,9,1);
				dummyTable.add(end_y,10,1);



				dummyTable.add(strik,12,1);

                                myTable.add(dummyTable,1,1);

	}

	private String[] getDates(IWContext iwc) throws IOException {

		String[] dates = {"",""};

		dagatalid = new idegaCalendar();
			year = dagatalid.getYear();
			month = dagatalid.getMonth();
			day = dagatalid.getDay();

		this.start_year = iwc.getParameter("start_year");
			if ( start_year == null ) {
				start_year = String.valueOf(year - 1);
			}

		this.start_month = iwc.getParameter("start_month");
			if ( start_month == null ) {
				start_month = String.valueOf(month);
			}
		this.start_day = iwc.getParameter("start_day");
			if ( start_day == null ) {
				start_day = String.valueOf(day);
			}

			if ( Integer.parseInt(start_day) > dagatalid.getLengthOfMonth(start_month,start_year) ) {
				start_day = String.valueOf(dagatalid.getLengthOfMonth(start_month,start_year));
			}

		this.end_year = iwc.getParameter("end_year");
			if ( end_year == null ) {
				end_year = String.valueOf(year);
			}
		this.end_month = iwc.getParameter("end_month");
			if ( end_month == null ) {
				end_month = String.valueOf(month);
			}
		this.end_day = iwc.getParameter("end_day");
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

    public void setHeaderBackgroundColor(String color) {
      this.headerBackgroundColor = color;
    }

    public void setHeaderTextColor(String headerTextColor) {
      this.headerTextColor=headerTextColor;
      headerText.setFontColor(headerTextColor);
    }

    //Bjarni added these methood 14.08.01
    public void setTeeTextColor(String teeTextColor){
      isDefaultColors = false;
      this.teeTextColor = teeTextColor;
    }

    /*public setDefaultTeeTextColor(){
      isDefaultColors = true;
    }*/

    public void setHeaderTextProperties(Text textToClonePropertiesFrom){
      this.headerText = textToClonePropertiesFrom;
    }

    public void setTableTextProperties(Text textToClonePropertiesFrom){
      this.tableText = textToClonePropertiesFrom;
    }

    public void setTilFraTextProperties(Text textToClonePropertiesFrom){
      this.tilFraText = textToClonePropertiesFrom;
    }

    public void setTextLinkProperties(Link linkToClonePropertiesFrom){
      this.textLink = linkToClonePropertiesFrom;
    }

    public void setViewScoreIconUrlInBundle(String viewScoreIconUrlInBundle){
      this.viewScoreIceonUrlInBundle = viewScoreIconUrlInBundle;
    }

    public void setGetOverviewButton(String getOverviewButtonImageUrlInBundle,
      String getOverviewButtonParameterName, String getOverviewButtonParameterValue){
      setDifferentOverviewButton = true;
      this.getOverviewButtonImageUrlInBundle = getOverviewButtonImageUrlInBundle;
      this.getOverviewButtonParameterName = getOverviewButtonParameterName;
      this.getOverviewButtonParameterValue = getOverviewButtonParameterValue;
    }


    public void setFraPicture(String fraPictureUrlInBundle){
      isFraPicture = true;
      this.fraPictureUrlInBundle = fraPictureUrlInBundle;
    }

    public void setTilPicture(String tilPictureUrlInBundle){
      isTilPicture = true;
      this.tilPictureUrlInBundle = tilPictureUrlInBundle;
    }

    public void noIcons() {
      this.noIcons = true;
    }

    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }
}