package com.idega.projects.golf;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
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

public class HandicapInfo extends JModuleObject {

private String member_id;
private boolean isAdmin = false;

private Table myTable;

  public HandicapInfo() {
  }

  public HandicapInfo(String member_id) {
    this.member_id=member_id;
  }

  public HandicapInfo(int member_id) {
    this.member_id=String.valueOf(member_id);
  }

  public void main(ModuleInfo modinfo) throws Exception {

        this.isAdmin=isAdministrator(modinfo);

        if ( !isAdmin ) {
          modinfo.getSession().removeAttribute("member_id");
        }

        if ( member_id == null ) {
          member_id = modinfo.getRequest().getParameter("member_id");
        }
	if ( member_id == null ) {
          member_id = (String) modinfo.getSession().getAttribute("member_id");
        }
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

      drawTable();

      add(myTable);

  }

private void drawTable() throws IOException,SQLException {

        myTable = new Table(3,23);
                myTable.setBorder(0);
                myTable.setCellpadding(2);
                myTable.setCellspacing(0);
                //myTable.setWidth("100%");
                myTable.setAlignment("center");
                myTable.mergeCells(2,1,3,1);
                myTable.mergeCells(2,4,3,4);
                myTable.mergeCells(2,7,3,7);
                myTable.mergeCells(2,10,3,10);
                myTable.mergeCells(2,13,3,13);
                myTable.mergeCells(2,16,3,16);
                myTable.mergeCells(2,19,3,19);
                myTable.mergeCells(2,22,3,22);
                myTable.setWidth(2,2,"20");
                //myTable.setWidth(3,2,"100%");
                myTable.setHeight(3,"10");
                myTable.setHeight(6,"10");
                myTable.setHeight(9,"10");
                myTable.setHeight(12,"10");
                myTable.setHeight(15,"10");
                myTable.setHeight(18,"10");
                myTable.setHeight(21,"10");
                myTable.mergeCells(1,1,1,23);
                myTable.setAlignment(1,1,"center");
                myTable.setVerticalAlignment(1,1,"top");

        idegaTimestamp date = new idegaTimestamp();
        String dagur = date.getYear()+"-01-01";

        Member memberInfo = new Member(Integer.parseInt(member_id));
        MemberInfo memberInfo2 = new MemberInfo(Integer.parseInt(member_id));
        Union[] union = memberInfo.getUnions();
        int order = memberInfo2.getNumberOfRecords("handicap","<",""+memberInfo.getHandicap()) + 1;
        int clubOrder = 0;
        if ( union.length > 0 ) {
          clubOrder = memberInfo2.getNumberOfRecords("select count(*) from union_,union_member,member_info where union_.union_id='"+union[0].getID()+"' and union_.union_id=union_member.union_id and union_member.member_id=member_info.member_id and handicap<'"+memberInfo.getHandicap()+"'") + 1;
        }
        Scorecard[] scoreCards = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id='"+member_id+"' and handicap_correction='N' order by scorecard_date desc");
        Scorecard[] scoreCards2 = (Scorecard[]) (new Scorecard()).findAll("select * from scorecard where member_id='"+member_id+"' and scorecard_date>='"+dagur+"' and handicap_correction='N' order by total_points desc");

        Text member = new Text("Nafn kylfings:");
                member.setFontSize(1);
        Text handicap = new Text("Forgjöf:");
                handicap.setFontSize(1);
        Text cardTotal = new Text("Fjöldi spilaðra hringja á árinu:");
                cardTotal.setFontSize(1);
        Text scoreText = new Text("Síðasti spilaði hringur:");
                scoreText.setFontSize(1);
        Text points = new Text("Besti spilaði hringur á árinu:");
                points.setFontSize(1);
        Text averagepoints = new Text("Meðal punktafjöldi:");
                averagepoints.setFontSize(1);
        Text totalOrder = new Text("Forgjafarröð á landsvísu:");
                totalOrder.setFontSize(1);
        Text clubOrderText = new Text("Forgjafarröð innan klúbbs:");
                clubOrderText.setFontSize(1);

        Text memberText = new Text(memberInfo.getName());
                memberText.setFontSize(2);

        String handicapScaled = scale_decimals(String.valueOf(memberInfo2.getHandicap()),1);
        Text handicapText = new Text(handicapScaled);
            if ( (int) memberInfo2.getHandicap() == 100 ) {
              handicapText = new Text("Engin forgjöf");
            }
                handicapText.setFontSize(2);

        String cardText = String.valueOf(scoreCards.length);
        Text cardTotalText = new Text(cardText);
                if ( scoreCards2.length > 0 ) {
                        if ( cardText.substring(cardText.length()-1,cardText.length()).equals("1") ) { cardTotalText.addToText(" hringur"); }
                        else { cardTotalText.addToText(" hringir"); }
                }
                if ( scoreCards2.length < 1 ) { cardTotalText = new Text("Enginn hringur skráður..."); }
                cardTotalText.setFontSize(2);

        Text scoreCardsText = new Text("Enginn hringur skráður");
                if ( scoreCards.length > 0 ) {
                        idegaTimestamp scoreTime = new idegaTimestamp(scoreCards[0].getScorecardDate());
                        Field fieldId = new Field(scoreCards[0].getFieldID());

                        scoreCardsText = new Text(scoreTime.getISLDate()+"  -  "+fieldId.getName());
                }
                else {
                        scoreCardsText = new Text("Enginn hringur skráður...");
                }
                scoreCardsText.setFontSize(2);

        Text pointsText;
                if ( scoreCards2.length > 0 ) {
                        idegaTimestamp scoreTime = new idegaTimestamp(scoreCards2[0].getScorecardDate());
                        Field fieldId = new Field(scoreCards2[0].getFieldID());

                        pointsText = new Text(String.valueOf(scoreCards2[0].getTotalPoints())+" punktar  -  "+fieldId.getName()+", "+scoreTime.getISLDate());
                }
                else {
                        pointsText = new Text("Enginn hringur skráður...");
                }
                pointsText.setFontSize(2);

        Text averageText;
                if ( scoreCards.length > 0 ) {

                        float punktar = 0;

                        for ( int b = 0; b < scoreCards.length; b++ ) {

                          punktar += (float) scoreCards[b].getTotalPoints();

                        }

                        String averagePoints = scale_decimals(String.valueOf((punktar/(float) scoreCards.length)),2);

                        averageText = new Text(averagePoints+" punktar");
                }
                else {
                        averageText = new Text("Enginn hringur skráður...");
                }
                averageText.setFontSize(2);

        Text orderText = new Text(""+order);
            if ( (int) memberInfo2.getHandicap() == 100 ) {
             orderText = new Text("Engin forgjöf");
            }
            orderText.setFontSize(2);

        Text clubText = new Text(""+clubOrder);
            if ( (int) memberInfo2.getHandicap() == 100 ) {
             clubText = new Text("Engin forgjöf");
            }
            if ( clubOrder == 0 ) {
              clubText = new Text("Utan klúbba");
            }
            clubText.setFontSize(2);

        Window memberWindow = new Window("",400,220,"/handicap/select_member.jsp?");
        Image selectMemberImage = new Image("/pics/form_takkar/velja.gif","Velja kylfing");
          selectMemberImage.setAttribute("hspace","10");
        Link selectMember = new Link(selectMemberImage,memberWindow);
          selectMember.clearParameters();

        Window handicapWindow = new Window("",400,280,"/handicap/update_handicap.jsp?");
        Image updateHandicapImage = new Image("/pics/form_takkar/uppfaera.gif","Uppfæra forgjöf");
          updateHandicapImage.setAttribute("hspace","10");
        Link handicapUpdate = new Link(updateHandicapImage,handicapWindow);
          handicapUpdate.addParameter("member_id",member_id);

        myTable.add(member,2,1);
        myTable.add(memberText,3,2);
        if ( isAdmin ) {
          myTable.add(selectMember,3,2);
        }
        myTable.add(handicap,2,4);
        myTable.add(handicapText,3,5);
        if ( isAdmin ) {
          myTable.add(handicapUpdate,3,5);
        }
        myTable.add(cardTotal,2,7);
        myTable.add(cardTotalText,3,8);
        myTable.add(scoreText,2,10);
        myTable.add(scoreCardsText,3,11);
        myTable.add(points,2,13);
        myTable.add(pointsText,3,14);
        myTable.add(averagepoints,2,16);
        myTable.add(averageText,3,17);
        myTable.add(totalOrder,2,19);
        myTable.add(orderText,3,20);
        myTable.add(clubOrderText,2,22);
        myTable.add(clubText,3,23);
        myTable.addText("",2,3);
        myTable.addText("",3,3);
        myTable.addText("",2,6);
        myTable.addText("",3,6);
        myTable.addText("",2,9);
        myTable.addText("",3,9);
        myTable.addText("",2,12);
        myTable.addText("",3,12);
        myTable.addText("",2,15);
        myTable.addText("",3,15);
        myTable.addText("",2,18);
        myTable.addText("",3,18);
        myTable.addText("",2,21);
        myTable.addText("",3,21);

        Table imageTable = new Table();
                imageTable.setHeight("100%");
                imageTable.setAlignment(1,1,"center");
                imageTable.setBorder(0);


        Image memberImage = new Image(memberInfo.getImageId());
                if ( memberImage.getURL().equals("/servlet/imageModule?image_id=1") ) {
                        memberImage = new Image("/pics/member/x2.gif");
                }
              memberImage.setAttribute("hspace","10");

        imageTable.add(memberImage);

        myTable.add(imageTable,1,1);
}

private String scale_decimals(String nyForgjof,int scale) throws IOException {

        BigDecimal test2 = new BigDecimal(nyForgjof);

        String nyForgjof2 = test2.setScale(scale,5).toString();

        return nyForgjof2;

}

}