package is.idega.idegaweb.golf;



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



public class HandicapScore extends Block {



private String member_id;

private boolean isAdmin = false;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

protected IWResourceBundle iwrb;

protected IWBundle iwb;



private Table outerTable;

private Form myForm;



  public HandicapScore() {

  }



  public HandicapScore(String member_id) {

    this.member_id=member_id;

  }



  public HandicapScore(int member_id) {

    this.member_id=String.valueOf(member_id);

  }



  public void main(IWContext iwc) throws Exception {

    iwrb = getResourceBundle(iwc);

    iwb = getBundle(iwc);

    this.isAdmin=isAdministrator(iwc);



    if ( member_id == null ) {

      member_id = iwc.getRequest().getParameter("member_id");

    }

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



      Member member = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKeyLegacy(Integer.parseInt(member_id));



      if ( (int) member.getHandicap() == 100 ) {



        Table noTable = new Table();

          noTable.setAlignment("center");

          noTable.setCellpadding(12);

          noTable.setCellspacing(12);



        Text texti = new Text(iwrb.getLocalizedString("handicap.member_no_handicap","Member does not have a registered handicap."));

            texti.addBreak();

            texti.addBreak();

            texti.addToText(iwrb.getLocalizedString("handicap.handicap_help","Contact your club to get your handicap."));



        noTable.add(texti);

        add(noTable);

      }



      else {

        getForm();

        drawTable(iwc);



        myForm.add(outerTable);

        add(myForm);

      }

  }



  private void drawTable(IWContext iwc) throws IOException,SQLException {



      Member memberInfo = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKeyLegacy(Integer.parseInt(member_id));

      float forgjof = memberInfo.getHandicap();

      int union_id = memberInfo.getMainUnionID();

      String gender = memberInfo.getGender();



      String field_id = (String) iwc.getSession().getAttribute("field_id");

      if ( field_id == null ) {

        Field[] field = (Field[]) (((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).createLegacy()).findAllByColumn("union_id",String.valueOf(union_id));

          if ( union_id > 3 && field.length > 0 ) {

            field_id = String.valueOf(field[0].getID());

          }

          else field_id = "49";

      }



      HiddenInput fieldID = new HiddenInput("field_id",field_id);

      myForm.add(fieldID);



      Field fieldName = ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKeyLegacy(Integer.parseInt(field_id));



      idegaCalendar dagatal = new idegaCalendar();



      String month = String.valueOf(dagatal.getMonth());

      String year = String.valueOf(dagatal.getYear());

      String day = String.valueOf(dagatal.getDay());



      String tee_number = "1";

      if ( String.valueOf(memberInfo.getHandicap()) != null && memberInfo.getGender() != null ) {

        if ( forgjof >= 28.1 && gender.equals("M") ) { tee_number = "6"; }

        else if ( forgjof >= 10.5 && gender.equals("F") ) { tee_number = "4"; }

        else if ( forgjof <= 10.4 && gender.equals("F") ) { tee_number = "3"; }

        else if ( forgjof <= 28.0 && forgjof >= 4.5 && gender.equals("M") ) { tee_number = "2"; }

        else if ( forgjof <= 4.4 && gender.equals("M") ) { tee_number = "1"; }

      }



      DropdownMenu select_tee = new DropdownMenu("tee_number");



      DropdownMenu select_holes = new DropdownMenu("number_of_holes");

        select_holes.addMenuElement("18",iwrb.getLocalizedString("handicap.18_holes","18 holes"));

        select_holes.addMenuElement("1",iwrb.getLocalizedString("handicap.first_9_holes","First 9 holes"));

        select_holes.addMenuElement("10",iwrb.getLocalizedString("handicap.last_9_holes","Last 9 holes"));

        select_holes.keepStatusOnAction();



      DropdownMenu select_stats = new DropdownMenu("statistics");

        select_stats.addMenuElement("0",iwrb.getLocalizedString("handicap.no_statistics","No statistics"));

        select_stats.addMenuElement("1",iwrb.getLocalizedString("handicap.register_statistics","Register statistics"));

        select_stats.keepStatusOnAction();



      DropdownMenu select_month = new DropdownMenu("month");

        for ( int m = 1 ; m <= 12 ; m++ ) {

          select_month.addMenuElement(String.valueOf(m),dagatal.getNameOfMonth(m,iwc).toLowerCase());

        }

        select_month.setSelectedElement(month);

        select_month.keepStatusOnAction();



      DropdownMenu select_year = new DropdownMenu("year");

        for ( int y = 2001 ; y <= dagatal.getYear() ; y++ ) {

          select_year.addMenuElement(String.valueOf(y),String.valueOf(y));

        }



        select_year.setSelectedElement(year);

        select_year.keepStatusOnAction();



      DropdownMenu select_day = new DropdownMenu("day");

        for ( int d = 1 ; d <= 31 ; d++ ) {

          select_day.addMenuElement(String.valueOf(d),String.valueOf(d)+".");

        }



        select_day.setSelectedElement(day);

        select_day.keepStatusOnAction();



      Tee[] teeID = (Tee[]) (((is.idega.idegaweb.golf.entity.TeeHome)com.idega.data.IDOLookup.getHomeLegacy(Tee.class)).createLegacy()).findAll("select distinct tee_color_id from tee where field_id='"+field_id+"'");



      for ( int a = 0; a < teeID.length; a++ ) {

        int teeColorID = teeID[a].getIntColumnValue("tee_color_id");

        select_tee.addMenuElement(String.valueOf(teeColorID),(((is.idega.idegaweb.golf.entity.TeeColorHome)com.idega.data.IDOLookup.getHomeLegacy(TeeColor.class)).findByPrimaryKeyLegacy(teeColorID)).getStringColumnValue("tee_color_name"));

      }

        select_tee.keepStatusOnAction();

        select_tee.setSelectedElement(tee_number);



      Window memberWindow = new Window("",400,220,"/handicap/select_member.jsp");

      Image selectMemberImage = iwrb.getImage("buttons/search_for_member.gif","handicap.select","Select member");

        selectMemberImage.setAttribute("hspace","10");

      Link selectMember = new Link(selectMemberImage,memberWindow);

        selectMember.clearParameters();



      Window fieldWindow = new Window("",400,220,"/handicap/select_field.jsp");

      Image selectFieldImage =  iwrb.getImage("buttons/choose.gif","handicap.select_course","Select course");

        selectFieldImage.setAttribute("hspace","10");

      Link selectField = new Link(selectFieldImage,fieldWindow);

        selectField.clearParameters();



      SubmitButton writeScore = new SubmitButton(iwrb.getImage("buttons/register.gif"));



      Text member = new Text(iwrb.getLocalizedString("handicap.member","Member")+":" );

        member.setFontSize(1);

      Text memberText = new Text(memberInfo.getName());

        memberText.setFontSize(2);

      Text field = new Text(iwrb.getLocalizedString("handicap.course","Course")+":" );

        field.setFontSize(1);

      Text fieldText = new Text(fieldName.getName());

        fieldText.setFontSize(2);

      Text tees = new Text(iwrb.getLocalizedString("handicap.tees","Tees")+":" );

        tees.setFontSize(1);

      Text date = new Text(iwrb.getLocalizedString("handicap.day","Day")+":" );

        date.setFontSize(1);

      Text numberOfHoles = new Text(iwrb.getLocalizedString("handicap.number_of_holes","Number of holes")+":" );

        numberOfHoles.setFontSize(1);

      Text statistics = new Text(iwrb.getLocalizedString("handicap.statistics","Statistics")+":");

        statistics.setFontSize(1);



      outerTable = new Table(2,1);

        outerTable.setCellspacing(6);

        outerTable.setCellpadding(6);

        outerTable.setAlignment("center");

        outerTable.setAlignment(1,1,"center");



      Table myTable = new Table(2,7);

        myTable.setBorder(0);

        myTable.setCellpadding(8);

        myTable.setCellspacing(0);

        myTable.setAlignment("center");

        myTable.setColumnAlignment(1,"right");



      myTable.add(member,1,1);

      myTable.add(field,1,2);

      myTable.add(tees,1,3);

      myTable.add(date,1,4);

      myTable.add(numberOfHoles,1,5);

      myTable.add(statistics,1,6);



      myTable.add(memberText,2,1);

      if ( isAdmin ) {

        myTable.add(selectMember,2,1);

      }

      myTable.add(fieldText,2,2);

      myTable.add(selectField,2,2);

      myTable.add(select_day,2,4);

      myTable.add(select_month,2,4);

      myTable.add(select_year,2,4);

      myTable.add(select_holes,2,5);

      myTable.add(select_stats,2,6);



      myTable.mergeCells(1,7,2,7);

      myTable.setAlignment(1,7,"right");

      if ( teeID.length > 0 ) {

        myTable.add(select_tee,2,3);

        myTable.add(writeScore,1,7);

      }

      else {

        myTable.addText(iwrb.getLocalizedString("handicap.no_tees","No tees registered")+":",2,3);

      }



      Image swingImage = iwb.getImage("shared/swing.gif","",161,300);



      outerTable.add(myTable,1,1);

      outerTable.add(swingImage,2,1);



  }



  private void getForm() {

    Window skraWindow = new Window("",600,600,"/handicap/handicap.jsp");

    myForm = new Form(skraWindow);

      myForm.add(new HiddenInput("member_id",member_id));

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }

}
