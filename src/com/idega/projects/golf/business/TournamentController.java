//idega 2000-2001 - Tryggvi Larusson - Grímur Jónsson
/*
*Copyright 2000-2001 idega.is All Rights Reserved.
* $id$
*/

package com.idega.projects.golf.business;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.projects.golf.Handicap;
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
                        tours = (Tournament[]) new Tournament().findAllByColumnOrdered("union_id",unions_id+"","START_TIME");
                        for (int j = 0; j < tours.length; j++) {
                            menu.addMenuElement(tours[j].getID(),union_abb+"&nbsp;&nbsp;&nbsp;"+tours[j].getName());
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
        List tournamentGroupMembers = new Vector();
        for (int i = 0; i < membersToCheck.length; i++) {
            try {
                tournamentGroupMembers = EntityFinder.findAll(new com.idega.projects.golf.entity.Member(),"SELECT member.* from member, tournament_member where member.member_id = tournament_member.member_id and tournament_member.tournament_id = "+tournament.getID()+" AND tournament_member.tournament_group_id = "+tourGroup.getID());
//                if (isMemberInTournamentGroup(membersToCheck[i], tourGroup)) {
//                    tournamentGroupMembers.addElement(membersToCheck[i]);
//                }
            }
            catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        return tournamentGroupMembers;

    }

    public static List getMembersInTournamentList(Tournament tournament) throws SQLException {
        List members = null;
        try {
            members = EntityFinder.findReverseRelated(tournament,new com.idega.projects.golf.entity.Member());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return members;
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

    public static List getTournamentRoundMembersList(int tournament_round_id) throws SQLException {
        List members = null;
        try {
            members = EntityFinder.findAll(new com.idega.projects.golf.entity.TournamentParticipants(),"SELECT * from tournament_round_participants where tournament_round_id = "+tournament_round_id+" ORDER by grup_num");
        }
        catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return members;

    }

    public static com.idega.projects.golf.entity.TournamentRoundParticipants[] getTournamentRoundMembers(int tournament_round_id) throws SQLException {
        com.idega.projects.golf.entity.TournamentRoundParticipants[] members =  (com.idega.projects.golf.entity.TournamentRoundParticipants[]) (new com.idega.projects.golf.entity.TournamentRoundParticipants()).findAll("SELECT * from tournament_round_participants where tournament_round_id = "+tournament_round_id+" ORDER by grup_num");
        return members;
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
/*
            if (member.getMainUnionID() != 1) {
                TournamentGroup[] groups = tournament.getTournamentGroups();
//                if (tournament.getIfOpenTournament()){
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
*/

            if (tournament.getIfGroupTournament()){
                TournamentGroup[] groups = tournament.getTournamentGroups();
                for (int i = 0 ; i < groups.length; i++){
                    if (TournamentController.isMemberInTournamentGroup(member, groups[i])) {
                        theReturn = true;
                        break;
                    }
                }
            }
            else{
                theReturn=true;
            }



            return theReturn;
    }


    public static boolean setupStartingtime(com.idega.projects.golf.entity.Member member, Tournament tournament, int tournament_round_id, int startingGroup) throws SQLException{
        int howManyEachGroup = tournament.getNumberInGroup();
        TournamentRound tourRound = new TournamentRound(tournament_round_id);

        boolean returner = false;

        Startingtime startingtime = new Startingtime();
            startingtime.setFieldID(tournament.getFieldId());
            startingtime.setMemberID(member.getID());
            startingtime.setStartingtimeDate(new idegaTimestamp(tourRound.getRoundDate()).getSQLDate());
            startingtime.setPlayerName(member.getName());
            startingtime.setHandicap(member.getHandicap());
            startingtime.setClubName(member.getMainUnion().getAbbrevation());
            startingtime.setCardName("");
            startingtime.setCardNum("");
            startingtime.setGroupNum(startingGroup);
        startingtime.insert();
        startingtime.addTo(tournament);
        returner = true;

        return returner;

    }

    public static boolean isMemberRegisteredInTournament(Tournament tournament, com.idega.projects.golf.entity.Member member) throws SQLException {
        boolean returner = false;
        List listi = EntityFinder.findAll(new com.idega.projects.golf.entity.Member(),"Select member.* from member,tournament_member where member.member_id = tournament_member.member_id AND member.member_id = "+member.getID()+" AND tournament_member.tournament_id = "+tournament.getID()+"");
        if (listi != null) {
            returner = true;
        }

        return returner;

    }

    public static boolean isMemberRegisteredInTournament(Tournament tournament,TournamentRound tourRound,int howManyEachGroup, com.idega.projects.golf.entity.Member member) throws SQLException {
        boolean returner = false;
        com.idega.util.idegaTimestamp startStamp = new  com.idega.util.idegaTimestamp(tourRound.getRoundDate());
        Startingtime[] startingtimes = (Startingtime[]) (new Startingtime()).findAll("SELECT startingtime.* FROM STARTINGTIME, tournament_STARTINGTIME, tournament WHERE tournament.tournament_id = "+tournament.getID()+" AND tournament.tournament_id = tournament_startingtime.tournament_id AND tournament_startingtime.startingtime_id = startingtime.startingtime_id AND STARTINGTIME_DATE = '"+startStamp.toSQLDateString()+"' AND field_id="+tournament.getFieldId()+" AND member_id = "+member.getID());
        if (startingtimes.length > 0 ) {
            returner = true;
        }




        return returner;
    }

    public static List getMembersInStartingGroup(Tournament tournament, TournamentRound tournamentRound, int startingGroupNumber) {
        String SQLDate = new idegaTimestamp(tournamentRound.getRoundDate()).toSQLDateString();
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


    public static SubmitButton getAheadButton(String name, String value) {
        com.idega.jmodule.object.Image aheadImage = new com.idega.jmodule.object.Image("/pics/formtakks/afram.gif","");
        SubmitButton aheadButton = new SubmitButton(aheadImage,name,value);

        return aheadButton;
    }



    public static Form getStartingtimeTable(Tournament tournament,String tournament_round_id, boolean viewOnly) throws SQLException{
        return TournamentController.getStartingtimeTable(tournament,tournament_round_id,viewOnly,false,true);
    }

    public static Form getStartingtimeTable(Tournament tournament,String tournament_round_id, boolean viewOnly, boolean onlineRegistration) throws SQLException{
        return TournamentController.getStartingtimeTable(tournament,tournament_round_id,viewOnly,onlineRegistration,true);
    }

    public static Form getStartingtimeTable(Tournament tournament,String tournament_round_id, boolean viewOnly, boolean onlineRegistration, boolean useBorder) throws SQLException{
        Form form = new Form();
            form.maintainParameter("action");
            form.add(new HiddenInput("viewOnly",""+viewOnly));



        Table table = new Table();
            if (useBorder) {
                table.setBorder(5);
            }
            table.setWidth("90%");
            table.setCellpadding(1);
            table.setCellspacing(0);
        form.add(table);
        int row = 1;

        int numberOfMember = TournamentController.getMembersInTournament(tournament).length;

        TournamentRound[] tourRounds = tournament.getTournamentRounds();

        int tournamentRoundId= -1;
        if (tournament_round_id == null) {
            tournament_round_id = "-1";
            tournamentRoundId = tourRounds[0].getID();
        }
        else {
            tournamentRoundId = Integer.parseInt(tournament_round_id);
        }

        TournamentRound tournamentRound = new TournamentRound(tournamentRoundId);

        idegaTimestamp tourDay = null;

        DropdownMenu rounds = new DropdownMenu("tournament_round");

            if (tournamentRoundId != -1) {
              tourDay = new idegaTimestamp(tournamentRound.getRoundDate());
                rounds.addMenuElement(tournamentRound.getID() ,"Hringur "+tournamentRound.getRoundNumber()+ " "+tourDay.getISLDate(".",true) );
                table.add(rounds,1,row);
            }

        table.mergeCells(1,row,6,row);
        table.setAlignment(1,row,"right");
        ++row;
            table.add("&nbsp;Tími",1,row);
            table.add("Kennitala",2,row);
            table.add("Nafn",3,row);
            table.add("Klúbbur",4,row);
            table.add("Forgjöf",5,row);
            if (viewOnly || onlineRegistration){
                table.mergeCells(5,row,6,row);
            }
            else{
                table.add("Eyða",6,row);
            }

        java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
        java.text.DecimalFormat handicapFormat = new java.text.DecimalFormat("0.0");
        Field field = tournament.getField();
        List members;
        CheckBox delete;

        com.idega.jmodule.object.Image rusl = new com.idega.jmodule.object.Image("/pics/icons/trash.gif","Skrá úr móti");
        com.idega.jmodule.object.Image time;

        Link remove;
        Text tooMany = new Text("Ekki pláss");
            tooMany.setFontColor("RED");


        Union union;
        int union_id;
        String abbrevation = "'";


            idegaTimestamp startHour = new idegaTimestamp(tournamentRound.getRoundDate());
            idegaTimestamp endHour = new idegaTimestamp(tournamentRound.getRoundEndDate());
                endHour.addMinutes(1);
            int minutesBetween = tournament.getInterval();
            int numberInGroup = tournament.getNumberInGroup();


            int groupCounter = 0;
            int startInGroup = 0;
            com.idega.projects.golf.entity.Member tempMember;
            TextInput socialNumber;

            Connection conn = GenericEntity.getStaticInstance("com.idega.data.genericentity.Member").getConnection();

            while (endHour.isLaterThan(startHour) ) {
                ++row;
                ++groupCounter;
                startInGroup = 0;

                //time = new com.idega.jmodule.object.Image("http://clarke.idega.is/time.swt?type=gif&grc=true&time="+extraZero.format(startHour.getHour())+":"+extraZero.format(startHour.getMinute()) , extraZero.format(startHour.getHour())+":"+extraZero.format(startHour.getMinute()));
                //table.add(time,1,row );

                table.add("<b>&nbsp;"+extraZero.format(startHour.getHour())+":"+extraZero.format(startHour.getMinute())+"</b>",1,row);
                table.mergeCells(1,row,1,row + (numberInGroup -1));
                table.setVerticalAlignment(1,row,"top");
                members = TournamentController.getMembersInStartingGroup(tournament,tournamentRound,groupCounter);
                if (members != null) {
                    startInGroup =  members.size();
                    for (int i = 0; i < members.size() ; i++) {
                        tempMember = (com.idega.projects.golf.entity.Member) members.get(i);
                        if (i != 0) table.add(tooMany,1,row);
                        table.add(tempMember.getSocialSecurityNumber(),2,row);

                        if (conn != null) {
                            union_id = tournament.getTournamentMemberUnionId(conn, tempMember.getID());
                        }
                        else {
                            union_id = -1;
                        }

                        if (union_id != -1) {
                          union = new Union(union_id);
                          abbrevation = union.getAbbrevation();
                        }else {
                          abbrevation = "-";
                        }

                        table.add(abbrevation,4,row);

                        table.add(tempMember.getName() ,3,row);
                        table.add(handicapFormat.format(tempMember.getHandicap()),5,row);
                        if (!viewOnly) {
                            if (!onlineRegistration) {
                                delete = new CheckBox("deleteMember",Integer.toString(tempMember.getID()));
                                table.add(delete,6,row);
                            }
                        }
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
                startHour.addMinutes(minutesBetween);
                if (endHour.isLaterThan(startHour) ) {
                    table.mergeCells(1,row,6,row);
                }
            }

            if (conn != null) {
                GenericEntity.getStaticInstance("com.idega.data.genericentity.Member").freeConnection(conn);
            }




            table.mergeCells(1,row,3,row);
            table.add("Fjöldi þátttakenda : " +numberOfMember,1,row);

            table.mergeCells(4,row,6,row);
            if (!viewOnly) {
                SubmitButton submitButton = new SubmitButton(new com.idega.jmodule.object.Image("/pics/formtakks/vista.gif",""));
                table.add(new HiddenInput("sub_action","saveDirectRegistration"),4,row);
                table.add(submitButton,4,row);
                table.add(new HiddenInput("number_of_groups",""+groupCounter),4,row);
                table.setAlignment(4,row,"right");
            }

        return form;
    }

    public static int getNextAvailableStartingGroup(Tournament tournament, TournamentRound tourRound) {
        int counter = -1;
        try {
            counter = 1;
            boolean done = false;
            Startingtime[] startingtimes;
            com.idega.util.idegaTimestamp startStamp = new  com.idega.util.idegaTimestamp(tourRound.getRoundDate());
            com.idega.util.idegaTimestamp endStamp = new  com.idega.util.idegaTimestamp(tourRound.getRoundEndDate());

            while (!done) {
                startingtimes = (Startingtime[]) (new Startingtime()).findAll("SELECT * FROM STARTINGTIME WHERE STARTINGTIME_DATE >= '"+startStamp.toSQLString()+"' AND STARTINGTIME_DATE <= '"+endStamp.toSQLString()+"' AND field_id="+tournament.getFieldId()+" AND grup_num="+counter);

                if (startingtimes.length < tournament.getNumberInGroup()) {
                    done = true;
                }
                else {
                    ++counter;
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return counter;
    }


    public static List getStartingtimeOrder(Tournament tournament, TournamentRound tournamentRound) {
        List members = null;
        try {
            com.idega.util.idegaTimestamp startStamp = new  com.idega.util.idegaTimestamp(tournamentRound.getRoundDate());
            com.idega.util.idegaTimestamp endStamp = new  com.idega.util.idegaTimestamp(tournamentRound.getRoundEndDate());

            members = EntityFinder.findAll(new com.idega.projects.golf.entity.Member(),"SELECT member.*,grup_num from startingtime, member, tournament_startingtime, tournament, tournament_member where tournament.tournament_id = "+tournament.getID()+" and tournament.tournament_id = tournament_member.tournament_id AND tournament.tournament_id = tournament_startingtime.tournament_id AND tournament_startingtime.startingtime_id = startingtime.startingtime_id AND startingtime.field_id = "+tournament.getFieldId()+" AND STARTINGTIME.STARTINGTIME_DATE >= '"+startStamp.toSQLDateString()+"' AND STARTINGTIME.STARTINGTIME_DATE <= '"+endStamp.toSQLDateString()+"' AND member.member_id = startingtime.member_id and member.member_id = tournament_member.member_id ORDER by grup_num");
        }
        catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return members;

    }



public static int registerMember(com.idega.projects.golf.entity.Member member, Tournament theTournament, String tournament_group_id) throws SQLException {
    int returner = 0;
            try {
                member.addTo(theTournament,"TOURNAMENT_GROUP_ID",tournament_group_id,"UNION_ID",""+member.getMainUnionID() );
                TournamentController.createScorecardForMember(member,theTournament,tournament_group_id);
                returner = 0;
            }
            catch (SQLException s) {
                try {
                    s.printStackTrace(System.err);
                    Tournament[] tour = (Tournament[]) member.findRelated(theTournament);
                    if (tour.length > 0) {
                        //add("<br>Meðlimur : \""+member.getName()+"\" er þegar skráð/ur í mótið");
                        returner = 1;
                    }
                }
                catch (SQLException sq) {
                    sq.printStackTrace(System.err);
                    //add("<br>Meðlimur : \""+member.getName()+"\" skráðist ekki í mótið");
                    returner = 2;
                }
            }

      return returner;
}

public static void createScorecardForMember(com.idega.projects.golf.entity.Member member, Tournament tournament) throws SQLException {
    try {
        int tournament_group_id = tournament.getTournamentGroupId(member.getID());
        TournamentGroup tGroup = new TournamentGroup(tournament_group_id);

        TournamentTournamentGroup[] tTGroup = (TournamentTournamentGroup[]) (new TournamentTournamentGroup()).findAllByColumn("tournament_id", tournament.getID()+"","tournament_group_id",tournament_group_id+"");

        if (tTGroup.length > 0) {
            createScorecardForMember(member,tournament,tTGroup[0]);
        }
    }
    catch (Exception ex) {
        ex.printStackTrace(System.err);
    }

}

public static void createScorecardForMember(com.idega.projects.golf.entity.Member member, Tournament tournament, String tournament_group_id) throws SQLException {
        TournamentGroup tGroup = new TournamentGroup(Integer.parseInt(tournament_group_id));

        TournamentTournamentGroup[] tTGroup = (TournamentTournamentGroup[]) (new TournamentTournamentGroup()).findAllByColumn("tournament_id", tournament.getID()+"","tournament_group_id",tournament_group_id);

        if (tTGroup.length > 0) {
            createScorecardForMember(member,tournament,tTGroup[0]);
        }
        else {
        }
}

public static void createScorecardForMember(com.idega.projects.golf.entity.Member member, Tournament tournament, TournamentTournamentGroup tTGroup) throws SQLException {
    TournamentDay[] tDays = tournament.getTournamentDays();
    TournamentRound[] tRound = tournament.getTournamentRounds();
    TournamentType tType = tournament.getTournamentType();
    int numberOfRounds = tRound.length;
    int numberOfDays = tDays.length;
    Scorecard scorecard;
    Field field = tournament.getField();
    Handicap handicapper;
    float handicap;
    float playingHandicap;
    float CR = 0;
    int slope = 0;
    float maxHandicap = 36;
    float modifier = -1;

    try {
        Tee[] tee = (Tee[]) (new Tee()).findAllByColumn("field_id",field.getID()+"","tee_color_id",tTGroup.getTeeColorId()+"","hole_number","1");
        if (tee.length > 0) {
            CR = tee[0].getCourseRating();
            slope = tee[0].getSlope();
        }
    }
    catch (Exception e) {
    }

    for (int i = 0; i < numberOfRounds; i++) {
        handicap = member.getHandicap();
        if (handicap > 36 ) {
            MemberInfo memberInfo = new MemberInfo(member.getID());
                memberInfo.setHandicap(36);
                memberInfo.update();
        }
        maxHandicap = 36;


        if (member.getGender().equalsIgnoreCase("m") ) {
            maxHandicap = tournament.getMaxHandicap();
        }
        else {
            maxHandicap = tournament.getFemaleMaxHandicap();
        }
        try {
            handicapper = new Handicap((double)handicap);
            playingHandicap = (float) handicapper.getLeikHandicap((double)slope,(double) CR,(double) tournament.getField().getFieldPar());

            if (playingHandicap > maxHandicap) {
                handicap = Handicap.getHandicapForScorecard(tournament.getID(), tTGroup.getTeeColorId(),maxHandicap);
            }
        }catch (IOException io) {
            io.printStackTrace(System.err);
            if (handicap > maxHandicap) {
                handicap = maxHandicap;
            }
        }

        modifier = tType.getModifier();
        if (modifier != -1) {
          handicap = handicap * tType.getModifier();
        }

        scorecard = new Scorecard();
          scorecard.setMemberId(member.getID());
          scorecard.setTournamentRoundId(tRound[i].getID());
          //scorecard.setScorecardDate(new idegaTimestamp(tDays[i].getDate()).getTimestamp());
          scorecard.setTotalPoints(0);
          scorecard.setHandicapBefore(handicap);
          scorecard.setHandicapAfter(handicap);
          scorecard.setSlope(slope);
          scorecard.setCourseRating(CR);
          scorecard.setFieldID(field.getID());
          scorecard.setTeeColorID(tTGroup.getTeeColorId());
          scorecard.setHandicapCorrection("N");
          scorecard.setUpdateHandicap(false);
        scorecard.insert();
    }
}

  public static boolean isOnlineRegistration(Tournament tournament) {
    return isOnlineRegistration(tournament, idegaTimestamp.RightNow());
  }

  public static boolean isOnlineRegistration(Tournament tournament, idegaTimestamp dateToCheck) {
      boolean returner = false;
      if (tournament.getIfRegistrationOnline()) {
          if (dateToCheck.isLaterThan(new idegaTimestamp(tournament.getFirstRegistrationDate()))) {
              if (new idegaTimestamp(tournament.getLastRegistrationDate()).isLaterThan(dateToCheck) ) {
                  returner = true;
              }
          }
      }

      return returner;
  }

  public static void removeMemberFromTournament(Tournament tournament, com.idega.projects.golf.entity.Member member) {
      removeMemberFromTournament(tournament, member, -10);
  }

  public static void removeMemberFromTournament(Tournament tournament, com.idega.projects.golf.entity.Member member, int startingGroupNumber) {

      try {
          String member_id = ""+member.getID();

          idegaTimestamp stamp;
          String SQLString;
          List startingTimes;
          Startingtime sTime;
          Scorecard[] scorecards;
          Scorecard scorecard;

          TournamentRound[] tRounds = tournament.getTournamentRounds();
          for (int j = 0; j < tRounds.length; j++) {
              stamp = new idegaTimestamp(tRounds[j].getRoundDate());

              if ( startingGroupNumber != -10) {
                  SQLString = "SELECT * FROM STARTINGTIME WHERE FIELD_ID ="+tournament.getFieldId()+" AND MEMBER_ID = "+member_id+" AND GRUP_NUM = "+startingGroupNumber+" AND STARTINGTIME_DATE = '"+stamp.toSQLDateString()+"'";
              }
              else {
                  SQLString = "SELECT * FROM TOURNAMENT_STARTINGTIME, STARTINGTIME WHERE TOURNAMENT_STARTINGTIME.TOURNAMENT_ID = "+tournament.getID()+" AND STARTINGTIME.STARTINGTIME_ID = TOURNAMENT_STARTINGTIME.STARTINGTIME_ID AND FIELD_ID ="+tournament.getFieldId()+" AND MEMBER_ID = "+member_id+" AND STARTINGTIME_DATE = '"+stamp.toSQLDateString()+"'";
              }
              startingTimes = EntityFinder.findAll(new Startingtime(),SQLString);
              if (startingTimes != null) {
                  try {
                      for (int i = 0; i < startingTimes.size(); i++) {
                        sTime = (Startingtime) startingTimes.get(i);
                        sTime.removeFrom(tournament);
                        sTime.delete();
                      }
                  }
                  catch (Exception ex) {
                      System.err.println("Tournament Controller : removeMemberFromTournament, startingTime  -  (VILLA)");
                      ex.printStackTrace(System.err);
                  }
              }

              scorecards = (Scorecard[]) (new Scorecard()).findAllByColumn("MEMBER_ID",member_id,"TOURNAMENT_ROUND_ID",tRounds[j].getID()+"");
              if (scorecards != null) {
                  for (int i = 0; i < scorecards.length; i++) {
                      try {
                          scorecard = scorecards[i];
                          scorecard.delete();
                      }
                      catch (Exception ex) {
                          System.err.println("Tournament Controller : removeMemberFromTournament, scorecards  -  (VILLA)");
                          ex.printStackTrace(System.err);
                      }
                  }

              }

          }

          member.removeFrom(tournament);

      }
      catch (Exception e) {
          e.printStackTrace(System.err);
      }

  }




}