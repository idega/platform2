//idega 2000-2001 - Tryggvi Larusson - Grímur Jónsson - Þórhallur Helgason

/*

*Copyright 2000-2001 idega.is All Rights Reserved.

* $id$

*/



package is.idega.idegaweb.golf.business;



import com.idega.presentation.text.*;

import com.idega.presentation.ui.DropdownMenu;

import is.idega.idegaweb.golf.Handicap;

import com.idega.presentation.*;

import com.idega.presentation.ui.*;

import java.util.*;

import java.io.*;

import com.idega.jmodule.login.data.*;

import java.sql.*;

import java.io.*;

import com.idega.data.genericentity.*;

import com.idega.data.*;

import is.idega.idegaweb.golf.entity.*;

import com.idega.util.*;

import com.idega.idegaweb.IWBundle;

import com.idega.idegaweb.IWResourceBundle;

import com.idega.idegaweb.IWMainApplication;

import com.idega.jmodule.login.business.AccessControl;

import is.idega.idegaweb.golf.presentation.TournamentBox;

import com.idega.data.SimpleQuerier;



/**

*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>,<a href="mailto:gimmi@idega.is">Grímur Jónsson</a>,<a href="mailto:laddi@idega.is">Þórhallur Helgason</a>

*/



public class TournamentController  {



private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";



    public static String getBundleIdentifier(){

        return IW_BUNDLE_IDENTIFIER;

    }



    public static Tournament[] getNextTwoTournaments()throws Exception{

      Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();

      IWTimestamp stamp = IWTimestamp.RightNow();

      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time>'"+stamp.toSQLDateString()+"' order by start_time",2);

       return tourns;

    }



    public static Tournament[] getLastTwoTournaments()throws Exception{

      Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();

      IWTimestamp stamp = IWTimestamp.RightNow();

      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time<'"+stamp.toSQLDateString()+"' order by start_time desc",2);

      return tourns;

    }



    public static Tournament[] getNextTournaments(int number)throws Exception{

      Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();

      IWTimestamp stamp = IWTimestamp.RightNow();

      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time>'"+stamp.toSQLDateString()+"' order by start_time",number);

       return tourns;

    }



    public static Tournament[] getLastTournaments(int number)throws Exception{

      Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();

      IWTimestamp stamp = IWTimestamp.RightNow();

      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time<'"+stamp.toSQLDateString()+"' order by start_time desc",number);

      return tourns;

    }



    public static Tournament[] getTournaments(IWTimestamp stamp) throws Exception{

      IWTimestamp next = new IWTimestamp(stamp.RightNow());

          next.addDays(1);



      Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();

      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where start_time>= '"+stamp.toSQLDateString()+"' AND start_time<'"+next.toSQLDateString()+"' order by start_time");

       return tourns;

    }



    public static Tournament[] getTournamentToday() throws Exception{

        return TournamentController.getTournaments(IWTimestamp.RightNow());

    }



    public static int getTotalStrokes(Tournament tournament, TournamentRound round, is.idega.idegaweb.golf.entity.Member member) throws Exception{

      int totalStrokes = -1;



      try {

        StringBuffer sql = new StringBuffer();

          sql.append("select sum(stroke_count) from stroke st, scorecard s, tournament_round tr, tournament t");

          sql.append(" where st.scorecard_id = s.scorecard_id");

          sql.append(" and s.tournament_round_id = tr.tournament_round_id");

          sql.append(" and tr.tournament_id = t.tournament_id");

          sql.append(" and t.tournament_id = "+Integer.toString(tournament.getID()));

          sql.append(" and tr.round_number <= "+Integer.toString(round.getRoundNumber()));

          sql.append(" and s.member_id = "+Integer.toString(member.getID()));



        String[] overAllScore = com.idega.data.SimpleQuerier.executeStringQuery(sql.toString());

        if ( overAllScore != null ) {

          totalStrokes = Integer.parseInt(overAllScore[0]);

        }

      }

      catch (Exception e) {

        e.printStackTrace(System.err);

      }



      return totalStrokes;

    }



    public static Tournament[] getLastClosedTournaments(int number) throws Exception {

      Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();

      IWTimestamp stamp = IWTimestamp.RightNow();

      Tournament[] tourns = (Tournament[])tournament.findAll("select * from tournament where closed = 'Y' and closed_date<'"+stamp.toSQLString()+"' order by closed_date desc",number);

      return tourns;

    }





    public static boolean isTournamentRegistrable(Tournament tournament){

      boolean finished = tournament.isTournamentFinished();

      boolean ongoing = tournament.isTournamentOngoing();

      return (!(finished||ongoing));

    }



    public static void removeTournamentTableApplicationAttribute(IWContext iwc) {

      Enumeration enum = iwc.getServletContext().getAttributeNames();

      String myString = "";

      while (enum.hasMoreElements()) {

          myString = (String) enum.nextElement();

          if (myString.indexOf("tournament_table_union_id_") != -1) {

              iwc.removeApplicationAttribute(myString);

          }

          else if (myString.indexOf("tournament_dropdownmenu_ordered_by_union_clubadmin") != -1) {

              iwc.removeApplicationAttribute(myString);

          }

      }

      TournamentController.removeTournamentBoxApplication(iwc);





    }



    public static void removeTournamentBoxApplication(IWContext iwc) {

        iwc.removeApplicationAttribute("tournament_box");

    }



    public static TournamentBox getTournamentBox(IWContext iwc) {

      TournamentBox tBox = (TournamentBox) iwc.getApplicationAttribute("tournament_box");

      if (tBox == null) {

          System.err.println("TournamentBox IS NULL");

          tBox = new TournamentBox();

          iwc.setApplicationAttribute("tournament_box",tBox);

      }

      else {

          System.err.println("TournamentBox is NOT null");

      }

      return tBox;

    }





    public static DropdownMenu getDropdownOrderedByUnion(DropdownMenu menu,IWContext iwc)  {

        try {

            boolean clubAdmin = com.idega.jmodule.login.business.AccessControl.isClubAdmin(iwc);

            is.idega.idegaweb.golf.entity.Member member = (is.idega.idegaweb.golf.entity.Member)com.idega.jmodule.login.business.AccessControl.getMember(iwc);

            Union union = null;

            try {

              union = member.getMainUnion();

            }

            catch (Exception e) {}



            int union_id = -1;

            if (union != null) {

                union_id = union.getID();

            }



            String menuName = "";

            if (menu != null) {

                menuName = menu.getName();

            }



            DropdownMenu applicationMenu = (DropdownMenu) iwc.getApplicationAttribute("tournament_dropdownmenu_ordered_by_union_clubadmin_"+clubAdmin+"_union_id_"+union_id+"_menuName_"+menuName);

            if (applicationMenu == null) {

                    if (clubAdmin) {

                        String union_abb = union.getAbbrevation();

                        if (union_id != 1) {

//                             Tournament[] tours = (Tournament[]) ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy().findAllByColumnOrdered("union_id",union_id+"","START_TIME");

                             Tournament[] tours = (Tournament[]) ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy().findAll("select * from tournament where union_id = "+union_id+" and START_TIME > '2002-12-31' order by START_TIME");

                            for (int j = 0; j < tours.length; j++) {

                                menu.addMenuElement(tours[j].getID(),union_abb+"&nbsp;&nbsp;&nbsp;"+tours[j].getName());

                            }



                        }

                    }

                    else {  // normal admin

                        Union[] unions = (Union[]) ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).createLegacy().findAllOrdered("ABBREVATION");

                        Tournament[] tours = null;

                          int unions_id = 1;

                          String union_abb= "";

                        for (int i = 0; i < unions.length; i++) {

                            unions_id = unions[i].getID();

                            union_abb = unions[i].getAbbrevation();

                            //tours = (Tournament[]) ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy().findAllByColumnOrdered("union_id",unions_id+"","START_TIME");

                            tours = (Tournament[]) ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy().findAll("select * from tournament where union_id = "+unions_id+" and START_TIME > '2002-12-31' order by START_TIME");

                            for (int j = 0; j < tours.length; j++) {

                                menu.addMenuElement(tours[j].getID(),union_abb+"&nbsp;&nbsp;&nbsp;"+tours[j].getName());

                            }

                        }

                    }

                    iwc.setApplicationAttribute("tournament_dropdownmenu_ordered_by_union_clubadmin_"+clubAdmin+"_union_id_"+union_id+"_menuName_"+menuName,menu);

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

        is.idega.idegaweb.golf.entity.Member[] tempMembers = new is.idega.idegaweb.golf.entity.Member[members_to_check.size()];

        for (int i = 0; i < tempMembers.length; i++) {

            tempMembers[i] = (is.idega.idegaweb.golf.entity.Member) members_to_check.elementAt(i);



        }



        return getMembersInTournamentGroup(tournament, tourGroup, tempMembers);

    }





    public static List getMembersInTournamentGroup(Tournament tournament, TournamentGroup tourGroup, is.idega.idegaweb.golf.entity.Member[] members_to_check) throws SQLException{

        is.idega.idegaweb.golf.entity.Member[] membersToCheck = members_to_check;

        List tournamentGroupMembers = new Vector();

        for (int i = 0; i < membersToCheck.length; i++) {

            try {

                tournamentGroupMembers = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.entity.Member.class)).createLegacy(),"SELECT member.* from member, tournament_member where member.member_id = tournament_member.member_id and tournament_member.tournament_id = "+tournament.getID()+" AND tournament_member.tournament_group_id = "+tourGroup.getID());

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

            members = EntityFinder.findReverseRelated(tournament,((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.entity.Member.class)).createLegacy());

        }

        catch (Exception e) {

            e.printStackTrace(System.out);

        }

        return members;

    }







    public static is.idega.idegaweb.golf.entity.Member[] getMembersInTournament(Tournament tournament) throws SQLException {

        is.idega.idegaweb.golf.entity.Member[] members = null;

        is.idega.idegaweb.golf.entity.Member member = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.entity.Member.class)).createLegacy();

        try {

            members = (is.idega.idegaweb.golf.entity.Member[]) tournament.findReverseRelated(member);

        }

        catch (Exception e) {

            e.printStackTrace(System.out);

        }

        return members;

    }



    public static List getTournamentGroups(is.idega.idegaweb.golf.entity.Member member, Tournament tournament) throws SQLException {

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

            members = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.TournamentParticipantsHome)com.idega.data.IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.entity.TournamentParticipants.class)).createLegacy(),"SELECT * from tournament_round_participants where tournament_round_id = "+tournament_round_id+" ORDER by grup_num");

        }

        catch (Exception ex) {

            ex.printStackTrace(System.err);

        }

        return members;



    }



    public static is.idega.idegaweb.golf.entity.TournamentRoundParticipants[] getTournamentRoundMembers(int tournament_round_id) throws SQLException {

        is.idega.idegaweb.golf.entity.TournamentRoundParticipants[] members =  (is.idega.idegaweb.golf.entity.TournamentRoundParticipants[]) (((is.idega.idegaweb.golf.entity.TournamentRoundParticipantsHome)com.idega.data.IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.entity.TournamentRoundParticipants.class)).createLegacy()).findAll("SELECT * from tournament_round_participants where tournament_round_id = "+tournament_round_id+" ORDER by grup_num");

        return members;

    }



    public static List getTournamentGroups(is.idega.idegaweb.golf.entity.Member member) throws SQLException {

        List groups  = com.idega.data.EntityFinder.findAll(((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy());

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



    public static boolean isMemberInTournamentGroup(is.idega.idegaweb.golf.entity.Member member, TournamentGroup tourGroup) throws SQLException {

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





    /**

     * Returns int error message.

     *    int[0]: Many members with the same social security number

     *    int[1]: UnionMemberInfo entry not correct.

     *    int[2]: Member does not fit critera for TournamentGroup.

     *    int[3]: Tournament not set up correctly, no TournamentGroups specified.

     */

    public static int[] isMemberAllowedToRegister(is.idega.idegaweb.golf.entity.Member member,Tournament tournament)throws SQLException{



        int[] errors = new int[4];



        try {

            String[] socials = SimpleQuerier.executeStringQuery("SELECT MEMBER_ID FROM MEMBER WHERE SOCIAL_SECURITY_NUMBER = '"+member.getSocialSecurityNumber()+"' ");

            if (socials.length > 1) {

                errors[0] = 1;

            }

        }catch (Exception ex) {

            ex.printStackTrace(System.err);

        }



        try {

            String[] umi = SimpleQuerier.executeStringQuery("Select union_member_info_id from union_member_info where member_id = "+member.getID()+" and MEMBER_STATUS = 'A' and MEMBERSHIP_TYPE = 'main'" );

            if (umi.length != 1 ) {

                errors[1] = 1;

            }

        }catch (Exception ex) {

            ex.printStackTrace(System.err);

        }



        if (tournament.getIfGroupTournament()){

            TournamentGroup[] groups = tournament.getTournamentGroups();

            errors[2] = 1;

            for (int i = 0 ; i < groups.length; i++){

                if (TournamentController.isMemberInTournamentGroup(member, groups[i])) {

                    errors[2] = 0;

                    break;

                }

            }

        }

        else{

            errors[3] = 1;

        }



        return errors;

    }



    public static boolean setupStartingtime(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, int tournament_round_id, int startingGroup) throws SQLException{

		return setupStartingtime(member,tournament, tournament_round_id,startingGroup, 1);

	}



    public static boolean setupStartingtime(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, int tournament_round_id, int startingGroup, int tee_number) throws SQLException{

        int howManyEachGroup = tournament.getNumberInGroup();

        TournamentRound tourRound = ((is.idega.idegaweb.golf.entity.TournamentRoundHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKeyLegacy(tournament_round_id);



        boolean returner = false;



        Startingtime startingtime = ((is.idega.idegaweb.golf.entity.StartingtimeHome)com.idega.data.IDOLookup.getHomeLegacy(Startingtime.class)).createLegacy();

            startingtime.setFieldID(tournament.getFieldId());

            startingtime.setMemberID(member.getID());

            startingtime.setStartingtimeDate(new IWTimestamp(tourRound.getRoundDate()).getSQLDate());

            startingtime.setPlayerName(member.getName());

            startingtime.setHandicap(member.getHandicap());

            startingtime.setClubName(member.getMainUnion().getAbbrevation());

            startingtime.setCardName("");

            startingtime.setCardNum("");

            startingtime.setGroupNum(startingGroup);

            startingtime.setTeeNumber(tee_number);

        startingtime.insert();



        startingtime.addTo(tourRound);



        returner = true;



        return returner;



    }



    public static boolean isMemberRegisteredInTournament(Tournament tournament, is.idega.idegaweb.golf.entity.Member member) throws SQLException {

        boolean returner = false;

        List listi = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.entity.Member.class)).createLegacy(),"Select member.* from member,tournament_member where member.member_id = tournament_member.member_id AND member.member_id = "+member.getID()+" AND tournament_member.tournament_id = "+tournament.getID()+"");

        if (listi != null) {

            returner = true;

        }



        return returner;



    }



    public static boolean isMemberRegisteredInTournament(Tournament tournament,TournamentRound tourRound,int howManyEachGroup, is.idega.idegaweb.golf.entity.Member member) throws SQLException {

        boolean returner = false;

        com.idega.util.IWTimestamp startStamp = new  com.idega.util.IWTimestamp(tourRound.getRoundDate());

        Startingtime[] startingtimes = (Startingtime[]) (((is.idega.idegaweb.golf.entity.StartingtimeHome)com.idega.data.IDOLookup.getHomeLegacy(Startingtime.class)).createLegacy()).findAll("SELECT startingtime.* FROM STARTINGTIME, tournament_round_STARTINGTIME WHERE tournament_round_startingtime.tournament_round_id = "+tourRound.getID()+" AND tournament_round_startingtime.startingtime_id = startingtime.startingtime_id AND STARTINGTIME_DATE = '"+startStamp.toSQLDateString()+"' AND field_id="+tournament.getFieldId()+" AND member_id = "+member.getID());



        if (startingtimes.length > 0 ) {

            returner = true;

        }



        /*

        if (!returner) {

            returner = TournamentController.isMemberRegisteredInTournament(tournament,member);

        }

        */



        return returner;

    }



    public static List getMembersInStartingGroup(Tournament tournament, TournamentRound tournamentRound, int startingGroupNumber) {

        String SQLDate = new IWTimestamp(tournamentRound.getRoundDate()).toSQLDateString();

        int fieldId = tournament.getFieldId();

        List members = new Vector();

        try {

            members = com.idega.data.EntityFinder.findAll(com.idega.data.GenericEntity.getStaticInstance("is.idega.idegaweb.golf.entity.Member"),"SELECT member.* FROM member,STARTINGTIME, TOURNAMENT_ROUND_STARTINGTIME WHERE TOURNAMENT_ROUND_STARTINGTIME.tournament_round_id = "+tournamentRound.getID()+" AND TOURNAMENT_ROUND_STARTINGTIME.startingtime_id = startingtime.startingtime_id AND member.member_id = startingtime.member_id AND STARTINGTIME_DATE = '"+SQLDate+"' AND grup_num ="+startingGroupNumber+" AND field_id="+tournament.getFieldId());

        }

         catch (SQLException sq) {

            sq.printStackTrace(System.err);

        }

        return members;



    }



    public static Link getBackLink(IWContext iwc,int backUpHowManyPages) {

        IWMainApplication iwma = iwc.getApplication();

        IWBundle iwb = iwma.getBundle(TournamentController.getBundleIdentifier());

        IWResourceBundle iwrb = iwb.getResourceBundle(iwc.getCurrentLocale());



        Link backLink = new Link(iwrb.getImage("buttons/back.gif"),"#");

            backLink.setAttribute("onClick","history.go(-"+backUpHowManyPages+")");



        return backLink;

    }



    public static Link getBackLink(IWContext iwc) {

        return getBackLink(iwc,1);

    }



    public static List getUnionTournamentGroups(IWContext iwc) throws SQLException {

        Union union = ((is.idega.idegaweb.golf.entity.Member) com.idega.jmodule.login.business.AccessControl.getMember(iwc)).getMainUnion();

        return getUnionTournamentGroups(union);

    }



    public static List getUnionTournamentGroups(Union union) throws SQLException {

        List tGroup = null;

            if (union != null) {

                if (union.getID() == 3) {

                    tGroup = EntityFinder.findAllOrdered(((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy(),"name");

                }

                else {

                    tGroup = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy(),"SELECT * FROM TOURNAMENT_GROUP WHERE union_id ="+union.getID()+" OR union_id = 3 ORDER BY name");

                }

            }

            else {

                tGroup = EntityFinder.findAllOrdered(((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy(),"name");

            }



        return tGroup;

    }





    public static SubmitButton getAheadButton(IWContext iwc, String name, String value) {

        IWMainApplication iwma = iwc.getApplication();

        IWBundle iwb = iwma.getBundle(TournamentController.getBundleIdentifier());

        IWResourceBundle iwrb = iwb.getResourceBundle(iwc.getCurrentLocale());



        com.idega.presentation.Image aheadImage = iwrb.getImage("buttons/continue.gif");

        SubmitButton aheadButton = new SubmitButton(aheadImage,name,value);



        return aheadButton;

    }







    public static Form getStartingtimeTable(Tournament tournament,String tournament_round_id, boolean viewOnly, IWResourceBundle iwrb) throws SQLException{

        return TournamentController.getStartingtimeTable(tournament,tournament_round_id,viewOnly,false,true, iwrb);

    }



    public static Form getStartingtimeTable(Tournament tournament,String tournament_round_id, boolean viewOnly, boolean onlineRegistration, IWResourceBundle iwrb) throws SQLException{

        return TournamentController.getStartingtimeTable(tournament,tournament_round_id,viewOnly,onlineRegistration,true,iwrb);

    }



    public static Form getStartingtimeTable(Tournament tournament,String tournament_round_id, boolean viewOnly, boolean onlineRegistration, boolean useBorder, IWResourceBundle iwrb) throws SQLException{

        Form form = new Form();

            form.maintainParameter("action");

            form.add(new HiddenInput("viewOnly",""+viewOnly));



        Table table = new Table();

            if (useBorder) {

                table.setBorder(0);

            }

            table.setWidth("100%");

            table.setCellpadding(1);

            table.setCellspacing(1);

        form.add(table);

        int row = 1;

        int numberOfMember = 0;



        String headerColor = "#2C4E3B";

        String darkColor = "#DCEFDE";

        String lightColor = "#EAFAEC";

        String activeColor = darkColor;



        TournamentRound[] tourRounds = tournament.getTournamentRounds();



        int tournamentRoundId= -1;

        if (tournament_round_id == null) {

            tournament_round_id = "-1";

            tournamentRoundId = tourRounds[0].getID();

        }

        else {

            tournamentRoundId = Integer.parseInt(tournament_round_id);

        }



        TournamentRound tournamentRound = ((is.idega.idegaweb.golf.entity.TournamentRoundHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKeyLegacy(tournamentRoundId);



        boolean display = false;

        if (tournamentRound.getVisibleStartingtimes()) {

            display = true;

        }

            int roundNumber = tournamentRound.getRoundNumber();



            IWTimestamp tourDay = null;



            DropdownMenu rounds = new DropdownMenu("tournament_round");

              rounds.setAttribute("style","font-size: 8pt");

              if (!onlineRegistration) {

                for (int i = 0; i < tourRounds.length; i++) {

                  tourDay = new IWTimestamp(tourRounds[i].getRoundDate());

                    rounds.addMenuElement(tourRounds[i].getID() ,iwrb.getLocalizedString("tournament.round","Round")+" "+tourRounds[i].getRoundNumber()+ " "+tourDay.getISLDate(".",true) );

                }



                if (tournamentRoundId != -1) {

                    rounds.setSelectedElement(""+tournamentRound.getID());

                }

                rounds.setToSubmit();

              }

              else {

                  tourDay = new IWTimestamp(tournamentRound.getRoundDate());

                    rounds.addMenuElement(tournamentRound.getID() ,iwrb.getLocalizedString("tournament.round","Round")+ " "+tournamentRound.getRoundNumber()+ " "+tourDay.getISLDate(".",true) );

              }

            table.add(rounds,1,row);



            table.mergeCells(1,row,6,row);

            table.setAlignment(1,row,"right");

            ++row;



            Text textLook = new Text("");

              textLook.setFontSize(Text.FONT_SIZE_7_HTML_1);

            Text headerLook = new Text("");

              headerLook.setFontColor("#FFFFFF");

              headerLook.setFontFace(Text.FONT_FACE_VERDANA);

              headerLook.setFontSize(Text.FONT_SIZE_7_HTML_1);

              headerLook.setBold();



            Text space = new Text(" ");

              space.setFontSize(1);



            Text dMemberSsn;

            Text dMemberName;

            Text dMemberHand;

            Text dMemberUnion;





                Text tim = (Text) headerLook.clone();

                  tim.setText(iwrb.getLocalizedString("tournament.time","Time"));

                Text sc = (Text) headerLook.clone();

                  sc.setText(iwrb.getLocalizedString("tournament.social_security_number","Social security number") );

                Text name = (Text) headerLook.clone();

                  name.setText(iwrb.getLocalizedString("tournament.name","Name"));

                Text club = (Text) headerLook.clone();

                  club.setText(iwrb.getLocalizedString("tournament.club","Club"));

                Text hc = (Text) headerLook.clone();

                  hc.setText(iwrb.getLocalizedString("tournament.handicap","Handicap"));



                table.add(tim,1,row);

                table.add(sc,2,row);

                table.add(name,3,row);

                table.add(club,4,row);

                table.add(hc,5,row);



                if (viewOnly || onlineRegistration){

                    table.mergeCells(5,row,6,row);

                }

                else{

                    Text del = (Text) headerLook.clone();

                      del.setText(iwrb.getLocalizedString("tournament.remove","Remove"));

                    table.add(del,6,row);

                }

                table.setRowColor(row,headerColor);





            java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");

            java.text.DecimalFormat handicapFormat = new java.text.DecimalFormat("0.0");

            Field field = tournament.getField();

            List members;

            CheckBox delete;



            com.idega.presentation.Image rusl = new com.idega.presentation.Image("/pics/icons/trash.gif","Skrá úr móti");

//            com.idega.presentation.Image time;

            Flash time;



            Link remove;

            Text tooMany = new Text(iwrb.getLocalizedString("tournament.no_room","No room"));

                tooMany.setFontColor("RED");





            Union union;

            int union_id;

            String abbrevation = "'";



                    boolean displayTee = false;

                    if (tournamentRound.getStartingtees() > 1) {

                            displayTee = true;

                    }



                    int groupCounterNum = 0;





                    for (int y = 1 ; y <= tournamentRound.getStartingtees() ; y++) {

                            // HARÐKÓÐUN DAUÐANS

                            int tee_number = 1;

                            if (y == 2) tee_number = 10;





                            IWTimestamp startHour = new IWTimestamp(tournamentRound.getRoundDate());

                            IWTimestamp endHour = new IWTimestamp(tournamentRound.getRoundEndDate());

                                    endHour.addMinutes(1);



                            int minutesBetween = tournament.getInterval();

                            int numberInGroup = tournament.getNumberInGroup();

                            int groupCounter = 0;



                            if (displayTee) {

                                    ++row;

                                    Text startTee = (Text) headerLook.clone();

                                      startTee.setText(iwrb.getLocalizedString("tournament.starting_tee","Starting tee") +" : "+tee_number);

                                    table.add(startTee,1,row);

                                table.setRowColor(row,headerColor);

                                table.mergeCells(1,row,6,row);

                                table.setAlignment(1,row,"center");

                            }



                            int startInGroup = 0;

                            is.idega.idegaweb.golf.entity.Member tempMember;

                            TextInput socialNumber;





                            StartingtimeView[] sView;



                            while (endHour.isLaterThan(startHour) ) {

                                    ++row;

                                    ++groupCounter;

                                    ++groupCounterNum;

                                    startInGroup = 0;



                                    if (activeColor.equals(darkColor)) {

                                        activeColor = lightColor;

                                    }

                                    else {

                                        activeColor = darkColor;

                                    }



                                    time = new Flash("/time.swt?type=gif&grc=true&time="+extraZero.format(startHour.getHour())+":"+extraZero.format(startHour.getMinute()),60,38);

                                    table.add(time,1,row);

                                    table.setAlignment(1,row,"center");

                                    table.setVerticalAlignment(1,row,"middle");

    //				table.add("<b>&nbsp;"+extraZero.format(startHour.getHour())+":"+extraZero.format(startHour.getMinute())+"</b>",1,row);

                                    table.mergeCells(1,row,1,row + (numberInGroup -1));

    //				table.setVerticalAlignment(1,row,"top");



                                    sView = TournamentController.getStartingtimeView(tournamentRound.getID(),"","","grup_num",groupCounter+"",tee_number,"");

                                    // old sView = TournamentController.getStartingtimeView(tournamentRound.getID(),"startingtime_date","'"+startHour.toSQLDateString()+"'","grup_num",groupCounter+"",tee_number,"");

                                    //sView = (StartingtimeView[]) (((is.idega.idegaweb.golf.entity.StartingtimeViewHome)com.idega.data.IDOLookup.getHomeLegacy(StartingtimeView.class)).createLegacy()).findAll("Select sv.* from startingtime_view sv, tournament_round_startingtime trs where sv.startingtime_id = trs.startingtime_id AND trs.tournament_id = "+tournamentRound.getID()+" AND sv.startingtime_date = '" +startHour.toSQLDateString()+"' AND sv.grup_num ="+groupCounter );

                                    startInGroup = sView.length;



                                    for (int i = 0; i < sView.length; i++) {

                                            table.setHeight(row,"20");

                                            table.setColor(1,row,activeColor);

                                            table.setColor(2,row,activeColor);

                                            table.setColor(3,row,activeColor);

                                            table.setColor(4,row,activeColor);

                                            table.setColor(5,row,activeColor);

                                            table.setColor(6,row,activeColor);

                                            ++numberOfMember;

                                            if (i != 0) table.add(tooMany,1,row);



                                            if (display) {

                                                dMemberSsn = (Text) textLook.clone();

                                                dMemberName = (Text) textLook.clone();

                                                dMemberHand = (Text) textLook.clone();

                                                dMemberUnion = (Text) textLook.clone();

                                                  dMemberSsn.setText(sView[i].getSocialSecurityNumber());

                                                  dMemberName.setText(sView[i].getName());

                                                  dMemberUnion.setText(sView[i].getAbbrevation());

                                                  dMemberHand.setText(com.idega.util.text.TextSoap.singleDecimalFormat(sView[i].getHandicap()));



                                                table.add(dMemberSsn,2,row);

                                                table.add(dMemberName,3,row);

                                                table.add(dMemberUnion,4,row);

                                                table.add(dMemberHand,5,row);

                                                //table.add(sView[i].getSocialSecurityNumber(),2,row);

                                                //abbrevation = sView[i].getAbbrevation();

                                                //table.add(sView[i].getName() ,3,row);

                                                //table.add(abbrevation,4,row);

                                                //table.add(com.idega.util.text.TextSoap.singleDecimalFormat(sView[i].getHandicap()),5,row);

                                                if (!viewOnly) {

                                                        if (!onlineRegistration) {

                                                                delete = new CheckBox("deleteMember",Integer.toString(sView[i].getMemberId()));

                                                                table.add(delete,6,row);

                                                        }

                                                        else {

                                                            table.mergeCells(5,row,6,row);

                                                        }

                                                }

                                                else {

                                                    table.mergeCells(5,row,6,row);

                                                }

                                            }else {

                                                table.mergeCells(2,row,6,row);

                                            }

                                            row++;

                                    }





                                    for (int i = startInGroup; i < (numberInGroup); i++) {

                                            table.setHeight(row,"20");

                                            table.add(space,2,row);

                                            table.setColor(1,row,activeColor);

                                            table.setColor(2,row,activeColor);

                                            table.setColor(3,row,activeColor);

                                            table.setColor(4,row,activeColor);

                                            table.setColor(5,row,activeColor);

                                            table.setColor(6,row,activeColor);

                                            if ((!viewOnly) && (roundNumber == 1)) {

                                                if (tee_number == 10) {

                                                    socialNumber = new TextInput("social_security_number_for_group_"+groupCounter+"_");

                                                }else {

                                                    socialNumber = new TextInput("social_security_number_for_group_"+groupCounter);

                                                }

                                                socialNumber.setAttribute("style","font-size: 8pt");

                                                socialNumber.setLength(15);

                                                socialNumber.setMaxlength(10);

                                                table.add(socialNumber,2,row);

                                            }

                                            table.mergeCells(2,row,6,row);



                                            ++row;

                                    }

                                    startHour.addMinutes(minutesBetween);

                                    /*

                                    if (endHour.isLaterThan(startHour) ) {

                                            table.mergeCells(1,row,6,row);

                                    }

                                    */

                                    --row;

                            }

                    }



                ++row;

                table.setHeight(row,"20");

                table.setRowColor(row,headerColor);

                table.mergeCells(1,row,3,row);

                Text many = (Text) headerLook.clone();

                    many.setText(iwrb.getLocalizedString("tournament.number_of_participants","Number of participants") +" : " +numberOfMember);

                table.add(many,1,row);



                table.mergeCells(4,row,6,row);

                if (!viewOnly) {

                    SubmitButton submitButton = new SubmitButton(iwrb.getImage("buttons/save.gif"));

                    table.add(new HiddenInput("sub_action","saveDirectRegistration"),4,row);

                    table.add(submitButton,4,row);

                    table.add(new HiddenInput("number_of_groups",""+groupCounterNum),4,row);

                    table.setAlignment(4,row,"right");

                }





        return form;

    }



    public static boolean hasMemberStartingtime(Tournament tournament, TournamentRound tourRound, is.idega.idegaweb.golf.entity.Member member) {

        boolean returner = false;



        try {

            com.idega.util.IWTimestamp startStamp = new  com.idega.util.IWTimestamp(tourRound.getRoundDate());

            Startingtime[] startingtimes = (Startingtime[]) (((is.idega.idegaweb.golf.entity.StartingtimeHome)com.idega.data.IDOLookup.getHomeLegacy(Startingtime.class)).createLegacy()).findAll("SELECT * FROM STARTINGTIME WHERE STARTINGTIME_DATE = '"+startStamp.toSQLDateString()+"' AND member_id ="+member.getID()+" AND field_id="+tournament.getFieldId());



            if (startingtimes.length > 0) {

                returner = false;

            }

        }

        catch (Exception e) {

            e.printStackTrace(System.err);

        }





        return returner;

    }



    public static int getNextAvailableStartingGroup(Tournament tournament, TournamentRound tourRound, boolean useEmptyStartingGroup, int minimumGroupNumber) {

        int counter = -1;

        try {

            counter = minimumGroupNumber;

            boolean done = false;

            Startingtime[] startingtimes;

            com.idega.util.IWTimestamp startStamp = new  com.idega.util.IWTimestamp(tourRound.getRoundDate());



            while (!done) {



                startingtimes = (Startingtime[]) (((is.idega.idegaweb.golf.entity.StartingtimeHome)com.idega.data.IDOLookup.getHomeLegacy(Startingtime.class)).createLegacy()).findAll("SELECT * FROM STARTINGTIME s, TOURNAMENT_ROUND_STARTINGTIME trs WHERE trs.startingtime_id = s.startingtime_id AND trs.tournament_round_id = "+tourRound.getID()+" AND s.STARTINGTIME_DATE = '"+startStamp.toSQLDateString()+"'  AND s.field_id="+tournament.getFieldId()+" AND s.grup_num="+counter);



                if (useEmptyStartingGroup)  {

                    if (startingtimes.length == 0) {

                        done = true;

                    }

                }

                else {

                    if (startingtimes.length < tournament.getNumberInGroup()) {

                        done = true;

                    }

                }



                if (!done) {

                    ++counter;

                }

            }

        }

        catch (Exception e) {

            e.printStackTrace(System.err);

        }



        return counter;

    }



    public static int getNextAvailableStartingGroup(Tournament tournament, TournamentRound tourRound) {

        return getNextAvailableStartingGroup(tournament,tourRound, false);

    }



    public static int getNextAvailableStartingGroup(Tournament tournament, TournamentRound tourRound, boolean useEmptyStartingGroup) {

        return getNextAvailableStartingGroup(tournament,tourRound, useEmptyStartingGroup, 1);

    }





    public static List getStartingtimeOrder(Tournament tournament, TournamentRound tournamentRound) {

        List members = null;

        try {

            com.idega.util.IWTimestamp startStamp = new  com.idega.util.IWTimestamp(tournamentRound.getRoundDate());

            com.idega.util.IWTimestamp endStamp = new  com.idega.util.IWTimestamp(tournamentRound.getRoundEndDate());



            members = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.entity.Member.class)).createLegacy(),"SELECT member.*,grup_num from startingtime, member, tournament_round_startingtime, tournament, tournament_member where tournament.tournament_id = "+tournament.getID()+" and tournament.tournament_id = tournament_member.tournament_id AND tournament_round_startingtime.tournament_round_id = "+tournamentRound.getID()+" AND tournament_round_startingtime.startingtime_id = startingtime.startingtime_id AND startingtime.field_id = "+tournament.getFieldId()+" AND STARTINGTIME.STARTINGTIME_DATE >= '"+startStamp.toSQLDateString()+"' AND STARTINGTIME.STARTINGTIME_DATE <= '"+endStamp.toSQLDateString()+"' AND member.member_id = startingtime.member_id and member.member_id = tournament_member.member_id ORDER by grup_num");

        }

        catch (Exception ex) {

            ex.printStackTrace(System.err);

        }

        return members;



    }







public static int registerMember(is.idega.idegaweb.golf.entity.Member member, Tournament theTournament, String tournament_group_id) throws SQLException {

    int returner = 0;

            try {

                member.addTo(theTournament,"TOURNAMENT_GROUP_ID",tournament_group_id,"UNION_ID",""+member.getMainUnionID(),"DISMISSAL_ID","0" );

                theTournament.setPosition(member,-1);

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



public static void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament) throws SQLException {

    try {

        int tournament_group_id = tournament.getTournamentGroupId(member.getID());

        TournamentGroup tGroup = ((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKeyLegacy(tournament_group_id);



        TournamentTournamentGroup[] tTGroup = (TournamentTournamentGroup[]) (((is.idega.idegaweb.golf.entity.TournamentTournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentTournamentGroup.class)).createLegacy()).findAllByColumn("tournament_id", tournament.getID()+"","tournament_group_id",tournament_group_id+"");



        if (tTGroup.length > 0) {

            createScorecardForMember(member,tournament,tTGroup[0]);

        }

    }

    catch (Exception ex) {

        ex.printStackTrace(System.err);

    }



}



public static void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, String tournament_group_id) throws SQLException {

        TournamentGroup tGroup = ((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKeyLegacy(Integer.parseInt(tournament_group_id));



        TournamentTournamentGroup[] tTGroup = (TournamentTournamentGroup[]) (((is.idega.idegaweb.golf.entity.TournamentTournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentTournamentGroup.class)).createLegacy()).findAllByColumn("tournament_id", tournament.getID()+"","tournament_group_id",tournament_group_id);



        if (tTGroup.length > 0) {

            createScorecardForMember(member,tournament,tTGroup[0]);

        }

        else {

        }

}





public static void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, TournamentTournamentGroup tTGroup, TournamentRound tRound) throws SQLException {

    TournamentType tType = tournament.getTournamentType();

    Scorecard scorecard;

    Field field = tournament.getField();

    Handicap handicapper;

    float handicap;

    float playingHandicap = -100;

    float CR = 0;

    int slope = 0;

    float maxHandicap = 36;

    float modifier = 1;



    try {

        Tee[] tee = (Tee[]) (((is.idega.idegaweb.golf.entity.TeeHome)com.idega.data.IDOLookup.getHomeLegacy(Tee.class)).createLegacy()).findAllByColumn("field_id",field.getID()+"","tee_color_id",tTGroup.getTeeColorId()+"","hole_number","1");

        if (tee.length > 0) {

            CR = tee[0].getCourseRating();

            slope = tee[0].getSlope();

        }

    }

    catch (Exception e) {

    }



        handicap = member.getHandicap();

        if (handicap > 36 ) {

            MemberInfo memberInfo = ((is.idega.idegaweb.golf.entity.MemberInfoHome)com.idega.data.IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKeyLegacy(member.getID());

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

                playingHandicap = maxHandicap;

            }

        }catch (IOException io) {

            io.printStackTrace(System.err);

            if (handicap > maxHandicap) {

                handicap = maxHandicap;

            }

        }



		try {

			modifier = tType.getModifier();

			if (modifier != -1) {

				if (playingHandicap != -100) {

				  float modified = (float) playingHandicap * tType.getModifier();

				  playingHandicap = Math.round(modified);

				  handicap = Handicap.getHandicapForScorecard(tournament.getID(), tTGroup.getTeeColorId(),playingHandicap);

				}

			}

		}

		catch (Exception e) {

			e.printStackTrace(System.err);

		}





        scorecard = ((is.idega.idegaweb.golf.entity.ScorecardHome)com.idega.data.IDOLookup.getHomeLegacy(Scorecard.class)).createLegacy();

          scorecard.setMemberId(member.getID());

          scorecard.setTournamentRoundId(tRound.getID());

          //scorecard.setScorecardDate(new IWTimestamp(tDays[i].getDate()).getTimestamp());

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



public static void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, TournamentTournamentGroup tTGroup) throws SQLException {

    TournamentRound[] tRound = tournament.getTournamentRounds();

    int numberOfRounds = tRound.length;

    for (int i = 0; i < numberOfRounds; i++) {

        TournamentController.createScorecardForMember(member,tournament,tTGroup,tRound[i]);

    }

}



  public static boolean isOnlineRegistration(Tournament tournament) {

    return isOnlineRegistration(tournament, IWTimestamp.RightNow());

  }



  public static DisplayScores[] getDisplayScores(String SQLConditions,String order) throws SQLException {



    DisplayScores[] tournParticipants = getDisplayScores(SQLConditions,order,"");

    return tournParticipants;



  }



  public static DisplayScores[] getDisplayScores(String SQLConditions,String order,String having) throws SQLException {



    if ( order.equalsIgnoreCase("tournament_handicap") ) {

      order = "9";

    }

    else if ( order.equalsIgnoreCase("holes_played") ) {

      order = "10";

    }

    else if ( order.equalsIgnoreCase("strokes_without_handicap") ) {

      order = "11";

    }

    else if ( order.equalsIgnoreCase("strokes_with_handicap") ) {

      order = "12";

    }

    else if ( order.equalsIgnoreCase("total_points") ) {

      order = "13 desc";

    }

    else if ( order.equalsIgnoreCase("difference") ) {

      order = "14";

    }



    String queryString = "select m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, t.tournament_id, tm.tournament_group_id, sum(cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0))) / count(tournament_round_id) as tournament_handicap, count(stroke_count) as holes_played, sum(stroke_count) as strokes_without_handicap, sum(stroke_count) - (sum(cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0))) / count(stroke_count) * count(distinct tournament_round_id)) as strokes_with_handicap, sum(point_count) as total_points, sum(stroke_count) - sum(hole_par) as difference from scorecard s ,stroke str, tournament_round tr, tournament t, field f, member m, union_ u, tournament_member tm"+

    " where s.tournament_round_id = tr.tournament_round_id and str.scorecard_id = s.scorecard_id and tr.tournament_id = t.tournament_id and t.field_id = f.field_id and s.member_id = m.member_id and m.member_id = tm.member_id and t.tournament_id = tm.tournament_id and tm.union_id = u.union_id"+

    " and "+SQLConditions+

    " group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, t.tournament_id, tm.tournament_group_id, s.handicap_before, s.slope, s.course_rating, f.field_par "+

    having+" order by "+order;



    DisplayScores[] tournParticipants = (DisplayScores[]) (((is.idega.idegaweb.golf.entity.DisplayScoresHome)com.idega.data.IDOLookup.getHomeLegacy(DisplayScores.class)).createLegacy()).findAll(queryString);



   return tournParticipants;



  }



  public static TournamentParticipants[] getTournamentParticipants(String column_name,String column_value,String order) throws SQLException {



    if ( order.equalsIgnoreCase("holes_played") ) {

      order = "13";

    }

    else if ( order.equalsIgnoreCase("round_handicap") ) {

      order = "14";

    }

    else if ( order.equalsIgnoreCase("strokes_without_handicap") ) {

      order = "15";

    }

    else if ( order.equalsIgnoreCase("strokes_with_handicap") ) {

      order = "16";

    }

    else if ( order.equalsIgnoreCase("total_par") ) {

      order = "18";

    }

    else if ( order.equalsIgnoreCase("difference") ) {

      order = "19";

    }



    String queryString = "select m.member_id, m.social_security_number, m.first_name, m.middle_name,"+

    " m.last_name, u.abbrevation,tm.tournament_id, tm.tournament_group_id,s.scorecard_id,"+

    " s.scorecard_date, tr.tournament_round_id, tr.round_number, count(str.stroke_count) holes_played,"+

    " cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0))round_handicap,"+

    " sum(str.stroke_count) strokes_without_handicap, sum(str.stroke_count) - cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ))as numeric (4,0)) strokes_with_handicap,"+

    " s.total_points, sum(str.hole_par) total_par, sum(str.stroke_count) - sum(str.hole_par) difference, tg.name as group_name"+

    " from tournament_round tr,"+

    " member m, field f, union_ u, tournament_member tm, tournament t, tournament_group tg,"+

    " scorecard s left join stroke str on str.scorecard_id = s.scorecard_id"+

    " where s.tournament_round_id = tr.tournament_round_id and tr.tournament_id = t.tournament_id"+

    " and tm.union_id = u.union_id and t.tournament_id = tm.tournament_id and tm.tournament_group_id = tg.tournament_group_id and tm.member_id = m.member_id"+

    " and s.member_id = m.member_id and t.field_id = f.field_id"+

    " and "+column_name+" = "+column_value+

    " group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, tm.tournament_id, tm.tournament_group_id,s.scorecard_id, s.scorecard_date, f.field_par,tr.tournament_round_id, tr.round_number, s.total_points, s.handicap_before, s.slope, s.course_rating, tg.name"+

    " order by "+order;



    TournamentParticipants[] tournParticipants = (TournamentParticipants[]) (((is.idega.idegaweb.golf.entity.TournamentParticipantsHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentParticipants.class)).createLegacy()).findAll(queryString);



   return tournParticipants;



  }



  public static TournamentRoundParticipants[] getTournamentRoundParticipants(String column_name,String column_value,String order) throws SQLException {



    if ( order.equalsIgnoreCase("holes_played") ) {

      order = "13";

    }

    else if ( order.equalsIgnoreCase("round_handicap") ) {

      order = "14";

    }

    else if ( order.equalsIgnoreCase("strokes_without_handicap") ) {

      order = "15";

    }

    else if ( order.equalsIgnoreCase("strokes_with_handicap") ) {

      order = "16";

    }

    else if ( order.equalsIgnoreCase("total_par") ) {

      order = "18";

    }

    else if ( order.equalsIgnoreCase("difference") ) {

      order = "19";

    }



    String queryString = "select m.member_id, m.social_security_number, m.first_name, m.middle_name,"+

    " m.last_name, u.abbrevation,tm.tournament_id, tm.tournament_group_id,s.scorecard_id,"+

    " s.scorecard_date, tr.tournament_round_id, tr.round_number, count(str.stroke_count) as holes_played,"+

    " cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0)) as round_handicap,"+

    " sum(str.stroke_count) as strokes_without_handicap, sum(str.stroke_count) - cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ))as numeric (4,0)) as strokes_with_handicap,"+

    " s.total_points, sum(str.hole_par) as total_par, sum(str.stroke_count) - sum(str.hole_par) as difference, start.grup_num, tg.name as group_name"+

    " from tournament_round tr,"+

    " member m, field f, union_ u, tournament_member tm, tournament t,tournament_group tg, startingtime start, tournament_ROUND_startingtime ts,"+

    " scorecard s left join stroke str on str.scorecard_id = s.scorecard_id"+

    " where s.tournament_round_id = tr.tournament_round_id and tr.tournament_id = t.tournament_id"+

    " and tm.union_id = u.union_id and t.tournament_id = tm.tournament_id and tm.member_id = m.member_id"+

    " and s.member_id = m.member_id and t.field_id = f.field_id"+

    " AND tR.tournament_ROUND_id = ts.tournament_ROUND_id AND ts.startingtime_id = start.startingtime_id"+

    " AND start.field_id = t.field_id AND start.startingtime_date >= cast (tr.round_date as date)"+

    " AND start.startingtime_date <= cast (tr.round_end_date as date) AND m.member_id = start.member_id and tm.tournament_group_id = tg.tournament_group_id"+

    " and "+column_name+" = "+column_value+

    " group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, tm.tournament_id, tm.tournament_group_id,s.scorecard_id, s.scorecard_date, f.field_par,tr.tournament_round_id, tr.round_number, s.total_points, s.handicap_before, s.slope, s.course_rating, start.grup_num, tg.name"+

    " order by "+order;



    TournamentRoundParticipants[] tournParticipants = (TournamentRoundParticipants[]) (((is.idega.idegaweb.golf.entity.TournamentRoundParticipantsHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentRoundParticipants.class)).createLegacy()).findAll(queryString);



   return tournParticipants;



  }



  public static StartingtimeView[] getStartingtimeView(int tournament_round_id, String column_name,String column_value,String column_name_1,String column_value_1,String order) throws SQLException {

                  return TournamentController.getStartingtimeView(tournament_round_id,column_name,column_value, column_name_1, column_value_1, 1,order);

  }



  public static StartingtimeView[] getStartingtimeView(int tournament_round_id, String column_name,String column_value,String column_name_1,String column_value_1,int tee_number,String order) throws SQLException {



    is.idega.idegaweb.golf.entity.StartingtimeView[] startView = new is.idega.idegaweb.golf.entity.StartingtimeView[0];



    String queryString = "select st.startingtime_id, st.field_id, st.member_id, st.startingtime_date,st.grup_num, " +

    "m.first_name,m.middle_name, m.last_name,m.social_security_number,mi.handicap ,u.abbrevation, u.union_id " +

    "from startingtime st, member m, union_member_info umi, union_ u, member_info mi, tournament_round_startingtime trs where " +

    "trs.startingtime_id = st.startingtime_id AND " +

    "trs.tournament_round_id = "+tournament_round_id+" AND " +

    "m.member_id = st.member_id AND " +

    "m.member_id = mi.member_id AND " +

    "m.member_id = umi.member_id AND " +

    "u.union_id = umi.union_id AND " +

    "st.tee_number = "+tee_number+" AND " +

    "umi.membership_type = 'main' AND " +

    "umi.member_status = 'A' ";

    if (!column_name.equalsIgnoreCase("")) {

      queryString += " AND "+column_name+" = "+column_value;

    }

    if (!column_name_1.equalsIgnoreCase("")) {

      queryString += " AND "+column_name_1+" = "+column_value_1;

    }

    if (!order.equalsIgnoreCase("")) {

      queryString += " order by "+order;

    }



    try {

        startView = (StartingtimeView[]) (((is.idega.idegaweb.golf.entity.StartingtimeViewHome)com.idega.data.IDOLookup.getHomeLegacy(StartingtimeView.class)).createLegacy()).findAll(queryString);

    }

    catch (SQLException sql) {

        System.err.println("queryString " + queryString);

        sql.printStackTrace(System.err);

    }



    return startView;



  }



  public static int getInt() {



   return 4;



  }



  public static boolean isOnlineRegistration(Tournament tournament, IWTimestamp dateToCheck) {

      boolean returner = false;

      if (tournament.getIfRegistrationOnline()) {

          if (dateToCheck.isLaterThan(new IWTimestamp(tournament.getFirstRegistrationDate()))) {

              if (new IWTimestamp(tournament.getLastRegistrationDate()).isLaterThan(dateToCheck) ) {

                  returner = true;

              }

          }

      }



      return returner;

  }



  public static void removeMemberFromTournament(Tournament tournament, is.idega.idegaweb.golf.entity.Member member) {

      removeMemberFromTournament(tournament, member, -10);

  }



  public static void removeMemberFromTournament(Tournament tournament, is.idega.idegaweb.golf.entity.Member member, int startingGroupNumber) {



      try {

          String member_id = ""+member.getID();



          IWTimestamp stamp;

          String SQLString;

          List startingTimes;

          Startingtime sTime;

          Scorecard[] scorecards;

          Scorecard scorecard;



          TournamentRound[] tRounds = tournament.getTournamentRounds();

          for (int j = 0; j < tRounds.length; j++) {

              stamp = new IWTimestamp(tRounds[j].getRoundDate());



              if ( startingGroupNumber != -10) {

                  SQLString = "SELECT * FROM STARTINGTIME WHERE FIELD_ID ="+tournament.getFieldId()+" AND MEMBER_ID = "+member_id+" AND GRUP_NUM = "+startingGroupNumber+" AND STARTINGTIME_DATE = '"+stamp.toSQLDateString()+"'";

              }

              else {

                  SQLString = "SELECT * FROM TOURNAMENT_ROUND_STARTINGTIME, STARTINGTIME WHERE TOURNAMENT_ROUND_STARTINGTIME.TOURNAMENT_ROUND_ID = "+tRounds[j].getID()+" AND STARTINGTIME.STARTINGTIME_ID = TOURNAMENT_ROUND_STARTINGTIME.STARTINGTIME_ID AND FIELD_ID ="+tournament.getFieldId()+" AND MEMBER_ID = "+member_id+" AND STARTINGTIME_DATE = '"+stamp.toSQLDateString()+"'";

              }

              startingTimes = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.StartingtimeHome)com.idega.data.IDOLookup.getHomeLegacy(Startingtime.class)).createLegacy(),SQLString);

              if (startingTimes != null) {

                  try {

                      for (int i = 0; i < startingTimes.size(); i++) {

                        sTime = (Startingtime) startingTimes.get(i);

                        sTime.removeFrom(tRounds[j]);

                        sTime.removeFrom(is.idega.idegaweb.golf.entity.TournamentBMPBean.getStaticInstance("is.idega.idegaweb.golf.entity.Tournament"));

                        sTime.delete();

                      }

                  }

                  catch (Exception ex) {

                      System.err.println("Tournament Controller : removeMemberFromTournament, startingTime  -  (VILLA)");

                      ex.printStackTrace(System.err);

                  }

              }



              scorecards = (Scorecard[]) (((is.idega.idegaweb.golf.entity.ScorecardHome)com.idega.data.IDOLookup.getHomeLegacy(Scorecard.class)).createLegacy()).findAllByColumn("MEMBER_ID",member_id,"TOURNAMENT_ROUND_ID",tRounds[j].getID()+"");

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
