//idega 2000-2001 - Tryggvi Larusson - Grímur Jónsson
/*
*Copyright 2000-2001 idega.is All Rights Reserved.
* $id$
*/

package com.idega.projects.golf.business;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import java.util.*;
import java.io.*;
import com.idega.jmodule.login.data.*;
import java.sql.*;
import java.io.*;
import com.idega.data.genericentity.*;
import com.idega.data.*;
import com.idega.projects.golf.entity.*;
import com.idega.util.*;
import com.idega.jmodule.login.business.AccessControl;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>,<a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
*/

public class TournamentController{

    public static Tournament[] getNextTwoTournaments()throws Exception{
      Tournament tournament = new Tournament();
      idegaTimestamp stamp = idegaTimestamp.RightNow();
      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time>'"+stamp.toSQLDateString()+"' order by start_time",2);
       return tourns;
    }

    public static Tournament[] getLastTwoTournaments()throws Exception{
      Tournament tournament = new Tournament();
      idegaTimestamp stamp = idegaTimestamp.RightNow();
      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time<'"+stamp.toSQLDateString()+"' order by start_time desc",2);
      return tourns;
    }


    public static Tournament[] getNextTournaments(int number)throws Exception{
      Tournament tournament = new Tournament();
      idegaTimestamp stamp = idegaTimestamp.RightNow();
      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time>'"+stamp.toSQLDateString()+"' order by start_time",number);
       return tourns;
    }

    public static Tournament[] getLastTournaments(int number)throws Exception{
      Tournament tournament = new Tournament();
      idegaTimestamp stamp = idegaTimestamp.RightNow();
      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time<'"+stamp.toSQLDateString()+"' order by start_time desc",number);
      return tourns;
    }

    public static boolean isTournamentRegistrable(Tournament tournament){
      boolean finished = tournament.isTournamentFinished();
      boolean ongoing = tournament.isTournamentOngoing();
      return (!(finished||ongoing));
    }

    public static void removeTournamentTableApplicationAttribute(ModuleInfo modinfo) {
      Enumeration enum = modinfo.getServletContext().getAttributeNames();
      String myString = "";
      while (enum.hasMoreElements()) {
          myString = (String) enum.nextElement();
          if (myString.indexOf("tournament_table_union_id_") != -1) {
              modinfo.removeApplicationAttribute(myString);
          }
          else if (myString.indexOf("tournament_dropdownmenu_ordered_by_union_clubadmin") != -1) {
              modinfo.removeApplicationAttribute(myString);
          }
      }
    }


    public static DropdownMenu getDropdownOrderedByUnion(DropdownMenu menu,ModuleInfo modinfo)  {
        try {
        boolean clubAdmin = com.idega.jmodule.login.business.AccessControl.isClubAdmin(modinfo);
        com.idega.projects.golf.entity.Member member = (com.idega.projects.golf.entity.Member)com.idega.jmodule.login.business.AccessControl.getMember(modinfo);
        Union union = member.getMainUnion();
        int union_id = -1;
        if (union != null) {
            union_id = union.getID();
        }

        String menuName = "";
        if (menu != null) {
            menuName = menu.getName();
        }

        DropdownMenu applicationMenu = (DropdownMenu) modinfo.getApplicationAttribute("tournament_dropdownmenu_ordered_by_union_clubadmin_"+clubAdmin+"_union_id_"+union_id+"_menuName_"+menuName);
        if (applicationMenu == null) {
                if (clubAdmin) {
                    String union_abb = union.getAbbrevation();
                    //System.out.println("Gimmi að testa í TournamentController : "+union_abb + " " + union_id);
                    if (union_id != 1) {
                         Tournament[] tours = (Tournament[]) new Tournament().findAllByColumnOrdered("union_id",union_id+"","START_TIME");
                        for (int j = 0; j < tours.length; j++) {
                            menu.addMenuElement(tours[j].getID(),union_abb+"&nbsp;&nbsp;&nbsp;"+tours[j].getName());
                        }

                    }
                }
                else {  // normal admin
                    Union[] unions = (Union[]) new Union().findAllOrdered("ABBREVATION");
                    Tournament[] tours = null;
                      int unions_id = 1;
                      String union_abb= "";
                    for (int i = 0; i < unions.length; i++) {
                        unions_id = unions[i].getID();
                        union_abb = unions[i].getAbbrevation();
                        if (union_id != 1) {
                            tours = (Tournament[]) new Tournament().findAllByColumnOrdered("union_id",unions_id+"","START_TIME");
                            for (int j = 0; j < tours.length; j++) {
                                menu.addMenuElement(tours[j].getID(),union_abb+"&nbsp;&nbsp;&nbsp;"+tours[j].getName());
                            }

                        }
                    }
                }
                modinfo.setApplicationAttribute("tournament_dropdownmenu_ordered_by_union_clubadmin_"+clubAdmin+"_union_id_"+union_id+"_menuName_"+menuName,menu);
        }
        else {
            menu = applicationMenu;
        }
            }
            catch (Exception s) {
                s.printStackTrace(System.err);
            }

        return menu;
    }

    public static List getMembersInTournamentGroup(Tournament tournament, TournamentGroup tourGroup) throws SQLException{
        return getMembersInTournamentGroup(tournament, tourGroup, getMembersInTournament(tournament));
    }

    public static List getMembersInTournamentGroup(Tournament tournament, TournamentGroup tourGroup, Vector members_to_check) throws SQLException{
        com.idega.projects.golf.entity.Member[] tempMembers = new com.idega.projects.golf.entity.Member[members_to_check.size()];
        for (int i = 0; i < tempMembers.length; i++) {
            tempMembers[i] = (com.idega.projects.golf.entity.Member) members_to_check.elementAt(i);

        }

        return getMembersInTournamentGroup(tournament, tourGroup, tempMembers);
    }


    public static List getMembersInTournamentGroup(Tournament tournament, TournamentGroup tourGroup, com.idega.projects.golf.entity.Member[] members_to_check) throws SQLException{
        com.idega.projects.golf.entity.Member[] membersToCheck = members_to_check;
        Vector tournamentGroupMembers = new Vector();
        for (int i = 0; i < membersToCheck.length; i++) {
            try {
                if (isMemberInTournamentGroup(membersToCheck[i], tourGroup)) {
                    tournamentGroupMembers.addElement(membersToCheck[i]);
                }
            }
            catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        return tournamentGroupMembers;

    }



    public static com.idega.projects.golf.entity.Member[] getMembersInTournament(Tournament tournament) throws SQLException {
        com.idega.projects.golf.entity.Member[] members = null;
        com.idega.projects.golf.entity.Member member = new com.idega.projects.golf.entity.Member();
        try {
            members = (com.idega.projects.golf.entity.Member[]) tournament.findReverseRelated(member);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return members;
    }

    public static List getTournamentGroups(com.idega.projects.golf.entity.Member member, Tournament tournament) throws SQLException {
        TournamentGroup[] tGroup = tournament.getTournamentGroups();
        Vector returner = new Vector();
        for (int i = 0; i < tGroup.length; i++) {
            if (isMemberInTournamentGroup(member,tGroup[i])) {
                returner.addElement(tGroup[i] );
            }
            else {
            }
        }

        return returner;

    }

    public static List getTournamentGroups(com.idega.projects.golf.entity.Member member) throws SQLException {
        List groups  = com.idega.data.EntityFinder.findAll(new TournamentGroup());
        Vector returner = new Vector();
        for (int i = 0; i < groups.size(); i++) {
            if (isMemberInTournamentGroup(member,(TournamentGroup)groups.get(i))) {
                returner.addElement(groups.get(i) );
            }
            else {
                groups.remove(i);
            }
        }

        return returner;

    }

    public static boolean isMemberInTournamentGroup(com.idega.projects.golf.entity.Member member, TournamentGroup tourGroup) throws SQLException {
        boolean young = false;
        boolean old = false;
        boolean sex = false;
        boolean maxHand = false;
        boolean minHand = false;

        if ((tourGroup.getGender() == member.getGender().charAt(0)) || (tourGroup.getGender() == 'B') ) sex = true;

        if (sex) {
            double memberHandicap = member.getHandicap();
            if (memberHandicap == 100) memberHandicap =36;
            if (tourGroup.getMinHandicap() <= memberHandicap ) minHand = true;
            if (minHand) {
                if (tourGroup.getMaxHandicap() >= memberHandicap ) maxHand = true;
                if (maxHand) {
                    int memberAge = member.getAge();//getAge(member);
                    if (tourGroup.getMinAge() <= memberAge) young = true;
                    if (young) {
                        if (tourGroup.getMaxAge() >= memberAge) old = true;
                    }
                }
            }
        }

        return (young && old && sex && maxHand && minHand);
    }

    public static boolean isMemberAllowedToRegister(com.idega.projects.golf.entity.Member member,Tournament tournament)throws SQLException{

            boolean theReturn = false;
            /**
             * TODO: Testa betur
             */

            if (member.getMainUnionID() != 1) {
                TournamentGroup[] groups = tournament.getTournamentGroups();
                if (tournament.getIfOpenTournament()){
                                if (tournament.getIfGroupTournament()){
                                        //Check if member is in the tournament group
                                        for (int i = 0 ; i < groups.length; i++){
                                                if (TournamentController.isMemberInTournamentGroup(member, groups[i])) {
                                                    theReturn = true;
                                                }
                                        }
                                }
                                else{
                                        theReturn=true;
                                }
                }
                else{
                        if (member.isMemberIn(tournament.getUnion())){
                                if (tournament.getIfGroupTournament()){
                                        //Check if member is in the tournament group
                                        for (int i = 0 ; i < groups.length; i++){
                                                if (TournamentController.isMemberInTournamentGroup(member, groups[i])) {
                                                    theReturn = true;
                                                }
                                        }
                                }
                                else{
                                        theReturn = true;
                                }
                        }
                }
            }
            else {
            }

            return theReturn;
    }


    public static boolean setupStartingtime(com.idega.projects.golf.entity.Member member, Tournament tournament, int tournament_day_id, int startingGroup) throws SQLException{
        int howManyEachGroup = tournament.getNumberInGroup();
        TournamentDay tourDay = new TournamentDay(tournament_day_id);

        boolean returner = false;

        if (!TournamentController.isMemberRegisteredInTournament(tournament,tourDay, howManyEachGroup, member)) {
            Startingtime startingtime = new Startingtime();
                startingtime.setFieldID(tournament.getFieldId());
                startingtime.setMemberID(member.getID());
                startingtime.setStartingtimeDate(tourDay.getDate());
                startingtime.setPlayerName(member.getName());
                startingtime.setHandicap(member.getHandicap());
                startingtime.setClubName(member.getMainUnion().getAbbrevation());
                startingtime.setCardName("");
                startingtime.setCardNum("");
                startingtime.setGroupNum(startingGroup);
            startingtime.insert();
            startingtime.addTo(tournament);
            returner = true;
            //add( member.getName() +" skráður í holl ");
            //add(startingGroup +"<br>");
        }

        return returner;

    }

    public static boolean isMemberRegisteredInTournament(Tournament tournament,TournamentDay tourDay,int howManyEachGroup, com.idega.projects.golf.entity.Member member) throws SQLException {
        boolean returner = false;
        com.idega.util.idegaTimestamp stamp = new  com.idega.util.idegaTimestamp(tourDay.getDate());
        Startingtime[] startingtimes = (Startingtime[]) (new Startingtime()).findAll("SELECT * FROM STARTINGTIME WHERE STARTINGTIME_DATE = '"+stamp.toSQLString()+"' AND field_id="+tournament.getFieldId()+" AND member_id = "+member.getID());
        if (startingtimes.length > 0 ) {
            returner = true;
        }

        return returner;
    }

    public static List getMembersInStartingGroup(Tournament tournament, TournamentDay tournamentDay, int startingGroupNumber) {
        String SQLDate = new idegaTimestamp(tournamentDay.getDate()).toSQLString();
        int fieldId = tournament.getFieldId();
        List members = new Vector();
        try {
            members = com.idega.data.EntityFinder.findAll(new com.idega.projects.golf.entity.Member(),"SELECT member.* FROM member,STARTINGTIME, TOURNAMENT_STARTINGTIME WHERE TOURNAMENT_STARTINGTIME.tournament_id = "+tournament.getID()+" AND TOURNAMENT_STARTINGTIME.startingtime_id = startingtime.startingtime_id AND member.member_id = startingtime.member_id AND STARTINGTIME_DATE = '"+SQLDate+"' AND grup_num ="+startingGroupNumber+" AND field_id="+tournament.getFieldId());
        }
         catch (SQLException sq) {
            sq.printStackTrace(System.err);
        }
        return members;

    }

    public static Link getBackLink(int backUpHowManyPages) {
        Link backLink = new Link(new com.idega.jmodule.object.Image("/pics/formtakks/tilbaka.gif","",76,19),"#");
            backLink.setAttribute("onClick","history.go(-"+backUpHowManyPages+")");

        return backLink;
    }

    public static Link getBackLink() {
        return getBackLink(1);
    }

    public static List getUnionTournamentGroups(ModuleInfo modinfo) throws SQLException {
        Union union = ((com.idega.projects.golf.entity.Member) com.idega.jmodule.login.business.AccessControl.getMember(modinfo)).getMainUnion();
        return getUnionTournamentGroups(union);
    }

    public static List getUnionTournamentGroups(Union union) throws SQLException {
        List tGroup = null;
            if (union != null) {
                if (union.getID() == 3) {
                    tGroup = EntityFinder.findAllOrdered(new TournamentGroup(),"name");
                }
                else {
                    tGroup = EntityFinder.findAll(new TournamentGroup(),"SELECT * FROM TOURNAMENT_GROUP WHERE union_id ="+union.getID()+" OR union_id = 3 ORDER BY name");
                }
            }
            else {
                tGroup = EntityFinder.findAllOrdered(new TournamentGroup(),"name");
            }

        return tGroup;
    }
/*
    public static List getUnions(ModuleInfo modinfo) throws SQLException {
            if (AccessControl.isClubAdmin(modinfo)) {

            }
            else if (AccessControl.isAdmin(modinfo)) {

            }

            return null;
    }

    public static List getFields(ModuleInfo modinfo, Union union) throws SQLException{
            if (AccessControl.isClubAdmin(modinfo)) {

            }
            else if (AccessControl.isAdmin(modinfo)) {

            }

            return null;
    }
*/
    public static SubmitButton getAheadButton(String name, String value) {
        com.idega.jmodule.object.Image aheadImage = new com.idega.jmodule.object.Image("/pics/formtakks/afram.gif","");
        SubmitButton aheadButton = new SubmitButton(aheadImage,name,value);

        return aheadButton;
    }



    public static Form getStartingtimeTable(Tournament tournament,String tournament_day_id, boolean viewOnly) throws SQLException{
        Form form = new Form();
            form.maintainParameter("action");
            form.add(new HiddenInput("viewOnly",""+viewOnly));


        Table table = new Table();
            table.setBorder(5);
            table.setWidth("90%");
            table.setCellpadding(1);
            table.setCellspacing(0);
        form.add(table);
        int row = 1;


        TournamentDay[] tourDays = tournament.getTournamentDays();

        int tournamentDayId= -1;
        if (tournament_day_id == null) {
            tournament_day_id = "-1";
            tournamentDayId = tourDays[0].getID();
        }
        else {
            tournamentDayId = Integer.parseInt(tournament_day_id);
        }

        TournamentDay tournamentDay = new TournamentDay(tournamentDayId);


        idegaTimestamp tourDay = null;
        if (tournament_day_id.equals("-1")) {
            tourDay = new idegaTimestamp(tournament.getStartTime());
        }
        else {
            tourDay = new idegaTimestamp(new TournamentDay(Integer.parseInt(tournament_day_id)).getDate());
        }



        DropdownMenu days = new DropdownMenu(tourDays);
            if (!tournament_day_id.equals("-1")) {
                days.setSelectedElement(tournament_day_id);
            }
            days.setToSubmit();
        table.add(days,1,row);
        table.mergeCells(1,row,6,row);
        table.setAlignment(1,row,"right");
        ++row;
            table.add("&nbsp;Tími",1,row);
            table.add("Kennitala",2,row);
            table.add("Nafn",3,row);
            table.add("Klúbbur",4,row);
            table.add("Forgjöf",5,row);

        java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
        Field field = tournament.getField();
        List members;
        com.idega.jmodule.object.Image rusl = new com.idega.jmodule.object.Image("/pics/icons/trash.gif","Skrá úr móti");
        Link remove;
        StartingtimeFieldConfig[] fieldConf = (StartingtimeFieldConfig[])(new StartingtimeFieldConfig()).findAllByColumn("tournament_id",""+tournament.getID() );
        Text tooMany = new Text("Ekki pláss");
            tooMany.setFontColor("RED");



        if (fieldConf.length > 0) {
            idegaTimestamp startHour = new idegaTimestamp(fieldConf[0].getOpenTime());
            idegaTimestamp endHour = new idegaTimestamp(fieldConf[0].getCloseTime());
                endHour.addMinutes(1);
            int minutesBetween = fieldConf[0].getMinutesBetweenStart();
            int numberInGroup = tournament.getNumberInGroup();


            int groupCounter = 0;
            int startInGroup = 0;
            com.idega.projects.golf.entity.Member tempMember;
            TextInput socialNumber;

            while (endHour.isLaterThan(startHour) ) {
                ++row;
                ++groupCounter;
                startInGroup = 0;

                table.add("<b>&nbsp;"+startHour.getHour() +":"+extraZero.format(startHour.getMinute())+"</b>",1,row);
                table.mergeCells(1,row,1,row + (numberInGroup -1));
                table.setVerticalAlignment(1,row,"top");
                members = TournamentController.getMembersInStartingGroup(tournament,tournamentDay,groupCounter);
                if (members != null) {
                    startInGroup =  members.size();
                    for (int i = 0; i < members.size() ; i++) {
                        tempMember = (com.idega.projects.golf.entity.Member) members.get(i);
                        if (i != 0) table.add(tooMany,1,row);
                        table.add(tempMember.getSocialSecurityNumber(),2,row);
                        table.add(tempMember.getName() ,3,row);
                        table.add(Float.toString(tempMember.getHandicap()),5,row);
                        remove = new Link(rusl);
                            remove.addParameter("sub_action","removeMemberFromTournament");
                            remove.addParameter("action","selectmember");
                            remove.addParameter("tournament_day",Integer.toString(tournamentDayId));
                            remove.addParameter("member_id",Integer.toString(tempMember.getID()));
                            remove.addParameter("startingGroupNumber", Integer.toString(groupCounter));
                        table.add(remove,6,row);
                        row++;
                    }

                }
                for (int i = startInGroup; i < (numberInGroup); i++) {
                    if (!viewOnly) {
                        socialNumber = new TextInput("social_security_number_for_group_"+groupCounter);
                        socialNumber.setLength(15);
                        socialNumber.setMaxlength(10);
                        table.add(socialNumber,2,row);
                    }
                    table.mergeCells(2,row,6,row);

                    ++row;
                }
                //table.setHeight(row,"20");
                table.mergeCells(1,row,6,row);
                startHour.addMinutes(minutesBetween);
            }


      //            SubmitButton submitButton = new SubmitButton("Vista","sub_action","saveDirectRegistration");
            if (!viewOnly) {
                SubmitButton submitButton = new SubmitButton(new com.idega.jmodule.object.Image("/pics/formtakks/vista.gif",""));
                table.add(new HiddenInput("sub_action","saveDirectRegistration"),1,row);
                table.add(submitButton,1,row);
                table.add(new HiddenInput("number_of_groups",""+groupCounter),1,row);
                table.setAlignment(1,row,"right");
            }

        }
        return form;
    }





}