//idega 2000-2001 - Tryggvi Larusson
/*
*Copyright 2000-2001 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.business;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.*;
import java.util.*;
import java.io.*;
import com.idega.jmodule.login.data.*;
import java.sql.*;
import java.io.*;
import com.idega.data.genericentity.*;
import com.idega.projects.golf.entity.*;
import com.idega.util.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
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


    public static DropdownMenu getDropdownOrderedByUnion(DropdownMenu menu,ModuleInfo modinfo) {
        try {
            boolean clubAdmin = com.idega.jmodule.login.business.AccessControl.isClubAdmin(modinfo);
            com.idega.projects.golf.entity.Member member = (com.idega.projects.golf.entity.Member)com.idega.jmodule.login.business.AccessControl.getMember(modinfo);
            Union union = member.getMainUnion();
            int union_id = -1;
        if (union != null) {
            union_id = union.getID();
        }

        String menuName = menu.getName();
        String applicationName = "tournament_dropdownmenu_ordered_by_union_clubadmin_"+clubAdmin+"_union_id_"+union_id+"_menuname_"+menuName;
        //DropdownMenu applicationMenu = null;
        DropdownMenu applicationMenu = (DropdownMenu) modinfo.getApplicationAttribute(applicationName);
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
                modinfo.setApplicationAttribute(applicationName,menu);
        }
        else {
            menu = applicationMenu;
        }
            }
            catch (SQLException s) {}

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



}