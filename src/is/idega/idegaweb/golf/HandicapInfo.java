package is.idega.idegaweb.golf;



/**

 * Title:

 * Description:

 * Copyright:    idega Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

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



public class HandicapInfo extends Block {



private String member_id;

private boolean isAdmin = false;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

protected IWResourceBundle iwrb;

protected IWBundle iwb;



private Table myTable;



  public HandicapInfo() {

  }



  public HandicapInfo(String member_id) {

    this.member_id=member_id;

  }



  public HandicapInfo(int member_id) {

    this.member_id=String.valueOf(member_id);

  }



  public void main(IWContext iwc) throws Exception {

    iwrb = getResourceBundle(iwc);

    iwb = getBundle(iwc);



        this.isAdmin=isAdministrator(iwc);



        if ( !isAdmin ) {

          iwc.getSession().removeAttribute("member_id");

        }



        if ( member_id == null ) {

          member_id = iwc.getRequest().getParameter("member_id");

        }

        if ( member_id == null ) {

          member_id = (String) iwc.getSession().getAttribute("member_id");

        }

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



      drawTable();



      add(myTable);



  }



  private void drawTable() throws IOException,SQLException {



        myTable = new Table(3,3);

                //myTable.setBorder(1);

                myTable.setCellpadding(6);

                myTable.setCellspacing(6);

                //myTable.setWidth("95%");

                myTable.setAlignment("center");

                myTable.setAlignment(1,1,"center");

                myTable.setVerticalAlignment(1,1,"top");

                myTable.setVerticalAlignment(1,2,"top");

                myTable.setVerticalAlignment(1,3,"top");

                myTable.setAlignment(3,1,"center");

                myTable.mergeCells(2,1,2,3);

                myTable.mergeCells(3,1,3,3);



        idegaTimestamp date = new idegaTimestamp();

        String dagur = date.getYear()+"-01-01";



        Member memberInfo = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKeyLegacy(Integer.parseInt(member_id));

        MemberInfo memberInfo2 = ((is.idega.idegaweb.golf.entity.MemberInfoHome)com.idega.data.IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKeyLegacy(Integer.parseInt(member_id));

        Union mainUnion = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(memberInfo.getMainUnionID());

        int order = memberInfo2.getNumberOfRecords("handicap","<",""+memberInfo.getHandicap()) + 1;

        int clubOrder = memberInfo2.getNumberOfRecords("select count(*) from union_,union_member_info,member_info where union_.union_id='"+mainUnion.getID()+"' and union_.union_id=union_member_info.union_id and union_member_info.member_id=member_info.member_id and handicap<'"+memberInfo.getHandicap()+"'") + 1;

        Scorecard[] scoreCards = (Scorecard[]) (((is.idega.idegaweb.golf.entity.ScorecardHome)com.idega.data.IDOLookup.getHomeLegacy(Scorecard.class)).createLegacy()).findAll("select * from scorecard where member_id='"+member_id+"' and handicap_correction='N' and scorecard_date is not null order by scorecard_date desc");

        Scorecard[] scoreCards2 = (Scorecard[]) (((is.idega.idegaweb.golf.entity.ScorecardHome)com.idega.data.IDOLookup.getHomeLegacy(Scorecard.class)).createLegacy()).findAll("select * from scorecard where member_id='"+member_id+"' and scorecard_date>='"+dagur+"' and scorecard_date is not null and handicap_correction='N' order by total_points desc");



        Text member = new Text(iwrb.getLocalizedString("handicap.member_name","Member name"));

                member.setFontSize(1);

        Text mainUnionText = new Text(iwrb.getLocalizedString("handicap.union_name","Club name"));

                mainUnionText.setFontSize(1);

        Text handicap = new Text(iwrb.getLocalizedString("handicap.handicap","Handicap"));

                handicap.setFontSize(1);

        Text cardTotal = new Text(iwrb.getLocalizedString("handicap.rounds_played","Number of rounds played this year"));

                cardTotal.setFontSize(1);

        Text scoreText = new Text(iwrb.getLocalizedString("handicap.last_round","Last round played"));

                scoreText.setFontSize(1);

        Text points = new Text(iwrb.getLocalizedString("handicap.best_round","Best round played this year"));

                points.setFontSize(1);

        Text averagepoints = new Text(iwrb.getLocalizedString("handicap.average","Average sum of points"));

                averagepoints.setFontSize(1);

        Text totalOrder = new Text(iwrb.getLocalizedString("handicap.national_ranking","National ranking"));

                totalOrder.setFontSize(1);

        Text clubOrderText = new Text(iwrb.getLocalizedString("handicap.club_ranking","Club ranking"));

                clubOrderText.setFontSize(1);

        Text memberText = new Text(memberInfo.getName());

                memberText.setFontSize(2);

                memberText.setBold();

        Text unionText = new Text(mainUnion.getAbbrevation()+" - "+mainUnion.getName());

                unionText.setFontSize(2);

                unionText.setBold();



        String handicapScaled = TextSoap.singleDecimalFormat(String.valueOf(memberInfo2.getHandicap()));

        Text handicapText = new Text(handicapScaled);

            if ( (int) memberInfo2.getHandicap() == 100 ) {

              handicapText = new Text(iwrb.getLocalizedString("handicap.no_handicap","No handicap"));

            }

            else {

              handicapText.setFontSize(6);

            }

            handicapText.setBold();



        String cardText = String.valueOf(scoreCards.length);

        String noRounds = iwrb.getLocalizedString("handicap.no_round","No rounds registered");

        Text cardTotalText = new Text(cardText);

                if ( scoreCards2.length > 0 ) {

                  if ( cardText.substring(cardText.length()-1,cardText.length()).equals("1") ) { cardTotalText.addToText(" "+iwrb.getLocalizedString("handicap.round","round")); }

                  else { cardTotalText.addToText(" "+iwrb.getLocalizedString("handicap.rounds","rounds")); }

                }

                if ( scoreCards2.length < 1 ) { cardTotalText = new Text(noRounds); }

                  cardTotalText.setFontSize(2);

                  cardTotalText.setBold();



        Text scoreCardsText = new Text(noRounds);

          if ( scoreCards.length > 0 ) {

            idegaTimestamp scoreTime = new idegaTimestamp(scoreCards[0].getScorecardDate());

            Field fieldId = ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKeyLegacy(scoreCards[0].getFieldID());

            scoreCardsText = new Text(scoreTime.getISLDate()+"  -  "+fieldId.getName());

          }

          else {

            scoreCardsText = new Text(noRounds);

          }

          scoreCardsText.setFontSize(2);

          scoreCardsText.setBold();



        Text pointsText;

          if ( scoreCards2.length > 0 ) {

            idegaTimestamp scoreTime = new idegaTimestamp(scoreCards2[0].getScorecardDate());

            Field fieldId = ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKeyLegacy(scoreCards2[0].getFieldID());

            pointsText = new Text(String.valueOf(scoreCards2[0].getTotalPoints())+" "+iwrb.getLocalizedString("handicap.points","points")+"  -  "+fieldId.getName()+", "+scoreTime.getISLDate());

          }

          else {

            pointsText = new Text(noRounds);

          }

          pointsText.setFontSize(2);

          pointsText.setBold();



        Text averageText;

          if ( scoreCards.length > 0 ) {

            float punktar = 0;

            for ( int b = 0; b < scoreCards.length; b++ ) {

              punktar += (float) scoreCards[b].getTotalPoints();

            }

            String averagePoints = TextSoap.decimalFormat(String.valueOf((punktar/(float) scoreCards.length)),2);

            averageText = new Text(averagePoints+" "+iwrb.getLocalizedString("handicap.points","points"));

          }

          else {

            averageText = new Text(noRounds);

          }

          averageText.setFontSize(2);

          averageText.setBold();



        Text orderText = new Text(Integer.toString(order));

            if ( (int) memberInfo2.getHandicap() == 100 ) {

              orderText = new Text(noRounds);

              orderText.setFontSize(2);

            }

            else {

              orderText.setFontSize(6);

            }

            orderText.setBold();



        Text clubText = new Text(""+clubOrder);

            if ( (int) memberInfo2.getHandicap() == 100 ) {

             clubText = new Text(iwrb.getLocalizedString("handicap.no_handicap","No handicap"));

            }

            if ( clubOrder == 0 ) {

              clubText = new Text(iwrb.getLocalizedString("handicap.no_club","Not registered"));

            }

            clubText.setFontSize(2);

            clubText.setBold();



        Window memberWindow = new Window("",400,220,"/handicap/select_member.jsp");

        Image selectMemberImage = iwrb.getImage("buttons/search_for_member.gif","handicap.select","Select member");

          selectMemberImage.setAttribute("hspace","10");

        Link selectMember = new Link(selectMemberImage,memberWindow);

          selectMember.clearParameters();



        Window handicapWindow = new Window("",400,280,"/handicap/update_handicap.jsp");

        Image updateHandicapImage = iwrb.getImage("buttons/update.gif","handicap.update_handicap","Update handicap");

          updateHandicapImage.setAttribute("hspace","10");

        Link handicapUpdate = new Link(updateHandicapImage,handicapWindow);

          handicapUpdate.addParameter("member_id",member_id);



        Table textTable = new Table();

          textTable.setCellpadding(4);

          textTable.setCellspacing(4);



        textTable.add(member,1,1);

        textTable.addBreak(1,1);

        textTable.add(memberText,1,1);

        if ( isAdmin ) {

          textTable.add(selectMember,1,1);

        }

        textTable.add(mainUnionText,1,2);

        textTable.addBreak(1,2);

        textTable.add(unionText,1,2);

        textTable.add(cardTotal,1,3);

        textTable.addBreak(1,3);

        textTable.add(cardTotalText,1,3);

        textTable.add(scoreText,1,4);

        textTable.addBreak(1,4);

        textTable.add(scoreCardsText,1,4);

        textTable.add(points,1,5);

        textTable.addBreak(1,5);

        textTable.add(pointsText,1,5);

        textTable.add(averagepoints,1,6);

        textTable.addBreak(1,6);

        textTable.add(averageText,1,6);

        textTable.add(clubOrderText,1,7);

        textTable.addBreak(1,7);

        textTable.add(clubText,1,7);



        Table imageTable = new Table();

          imageTable.setAlignment(1,1,"center");

          imageTable.setBorder(0);

          imageTable.setAlignment("center");

          imageTable.setCellpadding(0);

          imageTable.setCellspacing(1);

          imageTable.setColor("#000000");

          imageTable.setColor(1,1,"#CEDFD0");



        Image memberImage = null;

          if ( memberInfo.getImageId() == 1 ) {

            memberImage = iwrb.getImage("/member/x2.gif");

          }

          else {

            memberImage = new Image(memberInfo.getImageId());

          }



          memberImage.setMaxImageWidth(102);

          memberImage.setAttribute("alt",memberInfo.getName());

          memberImage.setAttribute("align","absmiddle");



        Image swingImage = iwb.getImage("shared/swing.gif","",161,300);



        imageTable.add(memberImage);



        Table outerHandicapTable = new Table(1,2);

          outerHandicapTable.setCellpadding(0);

          outerHandicapTable.setCellspacing(0);



        Table handicapTable = new Table(1,1);

          handicapTable.setWidth(120);

          handicapTable.setCellspacing(3);

          handicapTable.setCellpadding(6);

          handicapTable.setAlignment("center");

          handicapTable.setAlignment(1,1,"center");

          handicapTable.setColor("#336660");

          handicapTable.setColor(1,1,"#FFFFFF");

          handicapTable.add(handicapText,1,1);

          if ( isAdmin ) {

            handicapTable.add("<br>",1,1);

            handicapTable.add(handicapUpdate,1,1);

          }



          outerHandicapTable.add(handicap,1,1);

          outerHandicapTable.add(handicapTable,1,2);



        Table outerRankingTable = new Table(1,2);

          outerRankingTable.setCellpadding(0);

          outerRankingTable.setCellspacing(0);



        Table rankingTable = new Table(1,1);

          rankingTable.setWidth(120);

          rankingTable.setCellspacing(3);

          rankingTable.setCellpadding(6);

          rankingTable.setAlignment("center");

          rankingTable.setAlignment(1,1,"center");

          rankingTable.setColor("#336660");

          rankingTable.setColor(1,1,"#FFFFFF");

          rankingTable.add(orderText,1,1);



          outerRankingTable.add(totalOrder,1,1);

          outerRankingTable.add(rankingTable,1,2);



        myTable.add(imageTable,1,1);

        myTable.add(outerHandicapTable,1,2);

        myTable.add(outerRankingTable,1,3);

        myTable.add(textTable,2,1);

        myTable.add(swingImage,3,1);

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



}
