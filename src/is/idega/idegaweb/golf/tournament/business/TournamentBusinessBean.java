package is.idega.idegaweb.golf.tournament.business;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.DisplayScores;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Startingtime;
import is.idega.idegaweb.golf.entity.StartingtimeView;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentParticipants;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.entity.TournamentRoundParticipants;
import is.idega.idegaweb.golf.entity.TournamentTournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentType;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.tournament.presentation.TournamentBox;
import is.idega.idegaweb.golf.tournament.presentation.TournamentStartingtimeList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class TournamentBusinessBean extends IBOServiceBean implements TournamentBusiness {

	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";

	public TournamentBusinessBean() {
		super();
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public Tournament[] getNextTwoTournaments() throws Exception {
		Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
		IWTimestamp stamp = IWTimestamp.RightNow();
		Tournament[] tourns = (Tournament[]) tournament.findAll("select * from tournament where start_time>'" + stamp.toSQLDateString() + "' order by start_time", 2);
		return tourns;
	}

	public Tournament[] getLastTwoTournaments() throws Exception {
		Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
		IWTimestamp stamp = IWTimestamp.RightNow();
		Tournament[] tourns = (Tournament[]) tournament.findAll("select * from tournament where start_time<'" + stamp.toSQLDateString() + "' order by start_time desc", 2);
		return tourns;
	}

	public Tournament[] getNextTournaments(int number) throws Exception {
		Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
		IWTimestamp stamp = IWTimestamp.RightNow();
		Tournament[] tourns = (Tournament[]) tournament.findAll("select * from tournament where start_time>'" + stamp.toSQLDateString() + "' order by start_time", number);
		return tourns;
	}

	public Tournament[] getLastTournaments(int number) throws Exception {
		Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
		IWTimestamp stamp = IWTimestamp.RightNow();
		Tournament[] tourns = (Tournament[]) tournament.findAll("select * from tournament where start_time<'" + stamp.toSQLDateString() + "' order by start_time desc", number);
		return tourns;
	}

	public Tournament[] getTournaments(IWTimestamp stamp) throws Exception {
		IWTimestamp next = new IWTimestamp(IWTimestamp.RightNow());
		next.addDays(1);

		Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
		Tournament[] tourns = (Tournament[]) tournament.findAll("select * from tournament where start_time>= '" + stamp.toSQLDateString() + "' AND start_time<'" + next.toSQLDateString() + "' order by start_time");
		return tourns;
	}
	
	/**
	 * @param days How many days in search period.  If days<1 then it returns tournaments for the 
	 * 		  rest of the season or the next season if the month is 12
	 */
	public Tournament[] getTournamentsWithRegistration(IWTimestamp now, int days) throws Exception {
		Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
		IWTimestamp then = (IWTimestamp)now.clone();
		if(days<1){
			if(then.getMonth()>11){
				then.addYears(2);
			} else {
				then.addYears(1);
			}
			then.setMonth(1);
			then.setDay(1);
		} else {
			then.addDays(days+1);
		}
		then.setHour(0);
		then.setMinute(0);
		then.setSecond(0);
		String query = "select * from tournament where REGISTRATION_ONLINE like 'Y' and LAST_REGISTRATION_DATE <= '" + then.toSQLString() +
		"' AND LAST_REGISTRATION_DATE > '" + now.toSQLString() + "' and FIRST_REGISTRATION_DATE < '" + now.toSQLString() + "' order by start_time, name";
		//System.out.println(query);
		Tournament[] tourns = (Tournament[]) tournament.findAll(query);
		return tourns;
	}

	public Tournament[] getTournamentToday() throws Exception {
		return getTournaments(IWTimestamp.RightNow());
	}

	public int getTotalStrokes(Tournament tournament, TournamentRound round, is.idega.idegaweb.golf.entity.Member member) throws Exception {
		int totalStrokes = -1;

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select sum(stroke_count) from stroke st, scorecard s, tournament_round tr, tournament t");
			sql.append(" where st.scorecard_id = s.scorecard_id");
			sql.append(" and s.tournament_round_id = tr.tournament_round_id");
			sql.append(" and tr.tournament_id = t.tournament_id");
			sql.append(" and t.tournament_id = " + Integer.toString(tournament.getID()));
			sql.append(" and tr.round_number <= " + Integer.toString(round.getRoundNumber()));
			sql.append(" and s.member_id = " + Integer.toString(member.getID()));

			String[] overAllScore = com.idega.data.SimpleQuerier.executeStringQuery(sql.toString());
			if (overAllScore != null) {
				totalStrokes = Integer.parseInt(overAllScore[0]);
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return totalStrokes;
	}

	public Tournament[] getLastClosedTournaments(int number) throws Exception {
		Tournament tournament = (Tournament) IDOLookup.instanciateEntity(Tournament.class);
		IWTimestamp stamp = IWTimestamp.RightNow();
		Tournament[] tourns = (Tournament[]) tournament.findAll("select * from tournament where closed = 'Y' and closed_date<'" + stamp.toSQLString() + "' order by closed_date desc", number);
		return tourns;
	}

	public boolean isTournamentRegistrable(Tournament tournament) {
		boolean finished = tournament.isTournamentFinished();
		boolean ongoing = tournament.isTournamentOngoing();
		return (!(finished || ongoing));
	}

	public void removeTournamentTableApplicationAttribute(IWContext modinfo) {
		Enumeration enum = modinfo.getServletContext().getAttributeNames();
		String myString = "";
		List v = new Vector();
		while (enum.hasMoreElements()) {
			v.add(enum.nextElement());
		}
		Iterator iter = v.iterator();
		while (iter.hasNext()) {
			myString = (String) iter.next();
			if (myString.indexOf("tournament_table_union_id_") != -1) {
				modinfo.removeApplicationAttribute(myString);
			}
			else if (myString.indexOf("tournament_dropdownmenu_ordered_by_union_clubadmin") != -1) {
				modinfo.removeApplicationAttribute(myString);
			}
		}

		removeTournamentBoxApplication(modinfo);
	}

	public void removeTournamentBoxApplication(IWContext modinfo) {
		modinfo.removeApplicationAttribute("tournament_box");
	}

	public TournamentBox getTournamentBox(IWContext modinfo) {
		TournamentBox tBox = (TournamentBox) modinfo.getApplicationAttribute("tournament_box");
		if (tBox == null) {
			System.err.println("TournamentBox IS NULL");
			tBox = new TournamentBox();
			modinfo.setApplicationAttribute("tournament_box", tBox);
		}
		else {
			System.err.println("TournamentBox is NOT null");
		}
		return tBox;
	}

	public DropdownMenu getDropdownOrderedByUnion(DropdownMenu menu, IWContext modinfo) {
		return getDropdownOrderedByUnion(menu, modinfo, 2003);
	}

	public DropdownMenu getDropdownOrderedByUnion(DropdownMenu menu, IWContext modinfo, int year) {
		try {
			boolean clubAdmin = AccessControl.isClubAdmin(modinfo);
			Member member = (Member) AccessControl.getMember(modinfo);
			Union union = null;
			try {
				union = member.getMainUnion();
			}
			catch (Exception e) {
			}

			int union_id = -1;
			if (union != null) {
				union_id = union.getID();
			}

			String menuName = "";
			if (menu != null) {
				menuName = menu.getName();
			}

			DropdownMenu applicationMenu = (DropdownMenu) modinfo.getApplicationAttribute("tournament_dropdownmenu_ordered_by_union_clubadmin_" + clubAdmin + "_union_id_" + union_id + "_menuName_" + menuName + "_year_" + year);
			if (applicationMenu == null) {
				if (clubAdmin) {
					String union_abb = union.getAbbrevation();
					if (union_id != 1) {
						Tournament[] tours = {};
						if (year == -1) {
							tours = (Tournament[]) ((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAllByColumnOrdered("union_id", union_id + "", "START_TIME");
						}
						else {
							tours = (Tournament[]) ((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAll("select * from tournament where union_id = " + union_id + " and START_TIME < '" + year + "-12-31' and START_TIME > '" + year + "-01-01' order by START_TIME");
						}
						for (int j = 0; j < tours.length; j++) {
							menu.addMenuElement(tours[j].getID(), union_abb + "&nbsp;&nbsp;&nbsp;" + tours[j].getName());
						}

					}
				}
				else { // normal admin
					Union[] unions = (Union[]) ((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("ABBREVATION");
					Tournament[] tours = null;
					int unions_id = 1;
					String union_abb = "";
					for (int i = 0; i < unions.length; i++) {
						unions_id = unions[i].getID();
						union_abb = unions[i].getAbbrevation();
						if (year == -1) {
							tours = (Tournament[]) ((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAllByColumnOrdered("union_id", unions_id + "", "START_TIME");
						}
						else {
							tours = (Tournament[]) ((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAll("select * from tournament where union_id = " + unions_id + " and START_TIME < '" + year + "-12-31' and START_TIME > '" + year + "-01-01' order by START_TIME");
						}
						for (int j = 0; j < tours.length; j++) {
							menu.addMenuElement(tours[j].getID(), union_abb + "&nbsp;&nbsp;&nbsp;" + tours[j].getName());
						}
					}
				}
				modinfo.setApplicationAttribute("tournament_dropdownmenu_ordered_by_union_clubadmin_" + clubAdmin + "_union_id_" + union_id + "_menuName_" + menuName + "_year_" + year, menu);
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

	public List getMembersInTournamentGroup(Tournament tournament, TournamentGroup tourGroup) throws SQLException {
		return getMembersInTournamentGroup(tournament, tourGroup, getMembersInTournament(tournament));
	}

	public List getMembersInTournamentGroup(Tournament tournament, TournamentGroup tourGroup, Vector members_to_check) throws SQLException {
		is.idega.idegaweb.golf.entity.Member[] tempMembers = new is.idega.idegaweb.golf.entity.Member[members_to_check.size()];
		for (int i = 0; i < tempMembers.length; i++) {
			tempMembers[i] = (is.idega.idegaweb.golf.entity.Member) members_to_check.elementAt(i);

		}

		return getMembersInTournamentGroup(tournament, tourGroup, tempMembers);
	}

	public List getMembersInTournamentGroup(Tournament tournament, TournamentGroup tourGroup, is.idega.idegaweb.golf.entity.Member[] members_to_check) throws SQLException {
		is.idega.idegaweb.golf.entity.Member[] membersToCheck = members_to_check;
		List tournamentGroupMembers = new Vector();
		for (int i = 0; i < membersToCheck.length; i++) {
			try {
				tournamentGroupMembers = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), "SELECT member.* from member, tournament_member where member.member_id = tournament_member.member_id and tournament_member.tournament_id = " + tournament.getID() + " AND tournament_member.tournament_group_id = " + tourGroup.getID());
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

	public List getMembersInTournamentList(Tournament tournament) throws SQLException {
		List members = null;
		try {
			members = EntityFinder.findReverseRelated(tournament, (Member) IDOLookup.instanciateEntity(Member.class));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return members;
	}

	public is.idega.idegaweb.golf.entity.Member[] getMembersInTournament(Tournament tournament) throws SQLException {
		is.idega.idegaweb.golf.entity.Member[] members = null;
		is.idega.idegaweb.golf.entity.Member member = (Member) IDOLookup.instanciateEntity(Member.class);
		try {
			members = (is.idega.idegaweb.golf.entity.Member[]) tournament.findReverseRelated(member);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return members;
	}

	public List getTournamentGroups(is.idega.idegaweb.golf.entity.Member member, Tournament tournament) throws SQLException {
		TournamentGroup[] tGroup = tournament.getTournamentGroups();
		Vector returner = new Vector();
		for (int i = 0; i < tGroup.length; i++) {
			if (isMemberInTournamentGroup(member, tGroup[i])) {
				returner.addElement(tGroup[i]);
			}
			else {
			}
		}

		return returner;

	}

	public List getTournamentRoundMembersList(int tournament_round_id) throws SQLException {
		List members = null;
		try {
			members = EntityFinder.findAll((TournamentParticipants) IDOLookup.instanciateEntity(TournamentParticipants.class), "SELECT * from tournament_round_participants where tournament_round_id = " + tournament_round_id + " ORDER by grup_num");
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return members;

	}

	public is.idega.idegaweb.golf.entity.TournamentRoundParticipants[] getTournamentRoundMembers(int tournament_round_id) throws SQLException {
		is.idega.idegaweb.golf.entity.TournamentRoundParticipants[] members = (is.idega.idegaweb.golf.entity.TournamentRoundParticipants[]) ((TournamentRoundParticipants) IDOLookup.instanciateEntity(TournamentRoundParticipants.class)).findAll("SELECT * from tournament_round_participants where tournament_round_id = " + tournament_round_id + " ORDER by grup_num");
		return members;
	}

	public List getTournamentGroups(is.idega.idegaweb.golf.entity.Member member) throws SQLException {
		List groups = com.idega.data.EntityFinder.findAll((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class));
		Vector returner = new Vector();
		for (int i = 0; i < groups.size(); i++) {
			if (isMemberInTournamentGroup(member, (TournamentGroup) groups.get(i))) {
				returner.addElement(groups.get(i));
			}
			else {
				groups.remove(i);
			}
		}

		return returner;

	}

	public boolean isMemberInTournamentGroup(is.idega.idegaweb.golf.entity.Member member, TournamentGroup tourGroup) throws SQLException {
		boolean young = false;
		boolean old = false;
		boolean sex = false;
		boolean maxHand = false;
		boolean minHand = false;

		if ((tourGroup.getGender() == member.getGender().charAt(0)) || (tourGroup.getGender() == 'B')) sex = true;

		if (sex) {
			double memberHandicap = member.getHandicap();
			if (memberHandicap == 100) memberHandicap = 36;
			if (tourGroup.getMinHandicap() <= memberHandicap) minHand = true;
			if (minHand) {
				if (tourGroup.getMaxHandicap() >= memberHandicap) maxHand = true;
				if (maxHand) {
					int memberAge = member.getAge(); //getAge(member);
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
	 * Returns int error message. int[0]: Many members with the same social
	 * security number int[1]: UnionMemberInfo entry not correct. int[2]: Member
	 * does not fit critera for TournamentGroup. int[3]: Tournament not set up
	 * correctly, no TournamentGroups specified.
	 */
	public int[] isMemberAllowedToRegister(is.idega.idegaweb.golf.entity.Member member, Tournament tournament) throws SQLException {

		int[] errors = new int[4];

		try {
			String[] socials = SimpleQuerier.executeStringQuery("SELECT MEMBER_ID FROM MEMBER WHERE SOCIAL_SECURITY_NUMBER = '" + member.getSocialSecurityNumber() + "' ");
			if (socials.length > 1) {
				errors[0] = 1;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		try {
			String[] umi = SimpleQuerier.executeStringQuery("Select union_member_info_id from union_member_info where member_id = " + member.getID() + " and MEMBER_STATUS = 'A' and MEMBERSHIP_TYPE = 'main'");
			if (umi.length != 1) {
				errors[1] = 1;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		if (tournament.getIfGroupTournament()) {
			TournamentGroup[] groups = tournament.getTournamentGroups();
			errors[2] = 1;
			for (int i = 0; i < groups.length; i++) {
				if (isMemberInTournamentGroup(member, groups[i])) {
					errors[2] = 0;
					break;
				}
			}
		}
		else {
			errors[3] = 1;
		}

		return errors;
	}

	public boolean setupStartingtime(IWContext modinfo, is.idega.idegaweb.golf.entity.Member member, Tournament tournament, int tournament_round_id, int startingGroup) throws SQLException {
		return setupStartingtime(modinfo, member, tournament, tournament_round_id, startingGroup, 1);
	}

	public boolean setupStartingtime(IWContext modinfo, is.idega.idegaweb.golf.entity.Member member, Tournament tournament, int tournament_round_id, int startingGroup, int tee_number) throws SQLException {
		int howManyEachGroup = tournament.getNumberInGroup();
		TournamentRound tourRound = null;
		try {
			tourRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournament_round_id);
		}
		catch (FinderException fe) {
			throw new SQLException(fe.getMessage());
		}

		boolean returner = false;

		Startingtime startingtime = (Startingtime) IDOLookup.createLegacy(Startingtime.class);
		startingtime.setFieldID(tournament.getFieldId());
		startingtime.setMemberID(member.getID());
		startingtime.setStartingtimeDate(new IWTimestamp(tourRound.getRoundDate()).getSQLDate());
		startingtime.setPlayerName(member.getName());
		startingtime.setHandicap(member.getHandicap());
		try {
			startingtime.setClubName(member.getMainUnion().getAbbrevation());
		}
		catch (FinderException fe) {
			throw new SQLException(fe.getMessage());
		}
		startingtime.setCardName("");
		startingtime.setCardNum("");
		startingtime.setGroupNum(startingGroup);
		startingtime.setTeeNumber(tee_number);
		startingtime.insert();

		startingtime.addTo(tourRound);

		returner = true;
		invalidateStartingTimeCache(modinfo, tournament.getID(), String.valueOf(tournament_round_id));

		return returner;

	}

	public boolean blockStartingtime(IWContext modinfo, String name, int tournament_round_id, int startingGroup, int finishingGroup) throws SQLException {
		//System.out.println("Starting blocking");
		TournamentRound tourRound = null;
		try {
			tourRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournament_round_id);
		}
		catch (FinderException fe) {
			throw new SQLException(fe.getMessage());
		}
		Tournament tournament = tourRound.getTournament();
		int howManyEachGroup = tournament.getNumberInGroup();
		//System.out.println(" ... startingGroup ="+startingGroup+", finishingGroup
		// ="+finishingGroup+", howManyEachGroup = "+howManyEachGroup);

		for (int a = startingGroup; a <= finishingGroup; a++) {
			for (int b = 1; b <= howManyEachGroup; b++) {
				//System.out.println(" ... blocking groupNumber ="+a+",
				// currentTimeWithingGroup +"+b);
				Startingtime startingtime = (Startingtime) IDOLookup.createLegacy(Startingtime.class);
				startingtime.setFieldID(tournament.getFieldId());
				//System.out.println(" ... fieldId = "+tournament.getFieldId());
				startingtime.setStartingtimeDate(new IWTimestamp(tourRound.getRoundDate()).getSQLDate());
				//System.out.println(" ... date = "+(new
				// idegaTimestamp(tourRound.getRoundDate()).getSQLDate()).toString());
				startingtime.setPlayerName(name);
				//System.out.println(" ... name = "+name);
				startingtime.setHandicap(100);
				startingtime.setClubName("");
				startingtime.setGroupNum(a);
				startingtime.insert();
				startingtime.addTo(tourRound);
			}
		}
		invalidateStartingTimeCache(modinfo, tournament.getID(), String.valueOf(tournament_round_id));

		return true;
	}

	public boolean unblockStartingtime(IWContext modinfo, int tournament_round_id, int startingGroup, int finishingGroup) throws SQLException {
		TournamentRound tourRound = null;
		try {
			tourRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournament_round_id);
		}
		catch (FinderException fe) {
			throw new SQLException(fe.getMessage());
		}
		Tournament tournament = tourRound.getTournament();
		IWTimestamp stamp = new IWTimestamp(tourRound.getRoundDate());
		String SQLString = "SELECT * FROM TOURNAMENT_ROUND_STARTINGTIME, STARTINGTIME WHERE TOURNAMENT_ROUND_STARTINGTIME.TOURNAMENT_ROUND_ID = " + tournament_round_id + " AND STARTINGTIME.STARTINGTIME_ID = TOURNAMENT_ROUND_STARTINGTIME.STARTINGTIME_ID AND FIELD_ID =" + tournament.getFieldId() + " AND STARTINGTIME_DATE = '" + stamp.toSQLDateString() + "' and member_id = 1 and grup_num >= " + startingGroup + " and grup_num <=" + finishingGroup;
		//System.out.println("Tournament Controller: "+SQLString);
		List startingTimes = EntityFinder.findAll((Startingtime) IDOLookup.instanciateEntity(Startingtime.class), SQLString);

		Startingtime sTime;
		if (startingTimes != null) {
			try {
				for (int i = 0; i < startingTimes.size(); i++) {
					sTime = (Startingtime) startingTimes.get(i);
					sTime.removeFrom(tourRound);
					sTime.removeFrom((Tournament) IDOLookup.instanciateEntity(Tournament.class));
					sTime.delete();
				}
			}
			catch (Exception ex) {
				System.err.println("Tournament Controller : unBlockStartingTime  -  (VILLA)");
				ex.printStackTrace(System.err);
			}
		}
		invalidateStartingTimeCache(modinfo, tournament.getID(), String.valueOf(tournament_round_id));

		return true;
	}

	public boolean isMemberRegisteredInTournament(Tournament tournament, is.idega.idegaweb.golf.entity.Member member) throws SQLException {
		boolean returner = false;
		List listi = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), "Select member.* from member,tournament_member where member.member_id = tournament_member.member_id AND member.member_id = " + member.getID() + " AND tournament_member.tournament_id = " + tournament.getID() + "");
		if (listi != null) {
			returner = true;
		}

		return returner;

	}

	public boolean isMemberRegisteredInTournament(Tournament tournament, TournamentRound tourRound, int howManyEachGroup, is.idega.idegaweb.golf.entity.Member member) throws SQLException {
		boolean returner = false;
		com.idega.util.IWTimestamp startStamp = new com.idega.util.IWTimestamp(tourRound.getRoundDate());
		Startingtime[] startingtimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("SELECT startingtime.* FROM STARTINGTIME, tournament_round_STARTINGTIME WHERE tournament_round_startingtime.tournament_round_id = " + tourRound.getID() + " AND tournament_round_startingtime.startingtime_id = startingtime.startingtime_id AND STARTINGTIME_DATE = '" + startStamp.toSQLDateString() + "' AND field_id=" + tournament.getFieldId() + " AND member_id = " + member.getID());

		if (startingtimes.length > 0) {
			returner = true;
		}

		/*
		 * if (!returner) { returner =
		 * TournamentController.isMemberRegisteredInTournament(tournament,member); }
		 */

		return returner;
	}

	public List getMembersInStartingGroup(Tournament tournament, TournamentRound tournamentRound, int startingGroupNumber) {
		int fieldId = tournament.getFieldId();
		List members = new Vector();
		try {
			members = com.idega.data.EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), "SELECT member.* FROM member,STARTINGTIME, TOURNAMENT_ROUND_STARTINGTIME WHERE TOURNAMENT_ROUND_STARTINGTIME.tournament_round_id = " + tournamentRound.getID() + " AND TOURNAMENT_ROUND_STARTINGTIME.startingtime_id = startingtime.startingtime_id AND member.member_id = startingtime.member_id AND grup_num =" + startingGroupNumber + " AND field_id=" + tournament.getFieldId());
		}
		catch (SQLException sq) {
			sq.printStackTrace(System.err);
		}
		return members;

	}

	public BackButton getBackLink(IWContext modinfo, int backUpHowManyPages) {
		BackButton backLink = new BackButton();
		backLink.setHistoryMove(backUpHowManyPages);
		return backLink;
	}

	public BackButton getBackLink(IWContext modinfo) {
		return getBackLink(modinfo, 1);
	}

	public List getUnionTournamentGroups(IWContext modinfo) throws SQLException {
		try {
			Union union = ((is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo)).getMainUnion();
			return getUnionTournamentGroups(union);
		}
		catch (FinderException fe) {
			throw new SQLException(fe.getMessage());
		}
	}

	public List getUnionTournamentGroups(Union union) throws SQLException {
		List tGroup = null;
		if (union != null) {
			if (union.getID() == 3) {
				tGroup = EntityFinder.findAllOrdered((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class), "name");
			}
			else {
				tGroup = EntityFinder.findAll((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class), "SELECT * FROM TOURNAMENT_GROUP WHERE union_id =" + union.getID() + " OR union_id = 3 ORDER BY name");
			}
		}
		else {
			tGroup = EntityFinder.findAllOrdered((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class), "name");
		}

		return tGroup;
	}

	public SubmitButton getAheadButton(IWContext modinfo, String name, String value) {
		IWMainApplication iwma = modinfo.getIWMainApplication();
		IWBundle iwb = iwma.getBundle(getBundleIdentifier());
		IWResourceBundle iwrb = iwb.getResourceBundle(modinfo.getCurrentLocale());

		SubmitButton aheadButton = new SubmitButton(iwrb.getLocalizedString("trounament.continue","continue"), name, value);

		return aheadButton;
	}

	public TournamentStartingtimeList getStartingtimeTable(Tournament tournament, String tournament_round_id, boolean viewOnly, boolean forPrinting) throws SQLException {
		return getStartingtimeTable(tournament, tournament_round_id, viewOnly, false, true, forPrinting);
	}

	public TournamentStartingtimeList getStartingtimeTable(Tournament tournament, String tournament_round_id, boolean viewOnly) throws SQLException {
		return getStartingtimeTable(tournament, tournament_round_id, viewOnly, false, true);
	}

	public TournamentStartingtimeList getStartingtimeTable(Tournament tournament, String tournament_round_id, boolean viewOnly, boolean onlineRegistration, IWResourceBundle iwrb) throws SQLException {
		return getStartingtimeTable(tournament, tournament_round_id, viewOnly, onlineRegistration, true);
	}

	public TournamentStartingtimeList getStartingtimeTable(Tournament tournament, String tournament_round_id, boolean viewOnly, boolean onlineRegistration, boolean useBorder) throws SQLException {
		return getStartingtimeTable(tournament, tournament_round_id, viewOnly, onlineRegistration, useBorder, false);
	}

	public TournamentStartingtimeList getStartingtimeTable(Tournament tournament, String tournament_round_id, boolean viewOnly, boolean onlineRegistration, boolean useBorder, boolean forPrinting) throws SQLException {
		return new TournamentStartingtimeList(tournament, tournament_round_id, viewOnly, onlineRegistration, useBorder, forPrinting);
	}

	public boolean hasMemberStartingtime(Tournament tournament, TournamentRound tourRound, is.idega.idegaweb.golf.entity.Member member) {
		boolean returner = false;

		try {
			com.idega.util.IWTimestamp startStamp = new com.idega.util.IWTimestamp(tourRound.getRoundDate());
			Startingtime[] startingtimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("SELECT * FROM STARTINGTIME WHERE STARTINGTIME_DATE = '" + startStamp.toSQLDateString() + "' AND member_id =" + member.getID() + " AND field_id=" + tournament.getFieldId());

			if (startingtimes.length > 0) {
				returner = false;
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return returner;
	}

	public int getNextAvailableStartingGroup(Tournament tournament, TournamentRound tourRound, boolean useEmptyStartingGroup, int minimumGroupNumber) {
		int counter = -1;
		try {
			counter = minimumGroupNumber;
			boolean done = false;
			Startingtime[] startingtimes;
			com.idega.util.IWTimestamp startStamp = new com.idega.util.IWTimestamp(tourRound.getRoundDate());

			while (!done) {

				startingtimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("SELECT * FROM STARTINGTIME s, TOURNAMENT_ROUND_STARTINGTIME trs WHERE trs.startingtime_id = s.startingtime_id AND trs.tournament_round_id = " + tourRound.getID() + " AND s.field_id=" + tournament.getFieldId() + " AND s.grup_num=" + counter);

				if (useEmptyStartingGroup) {
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
	


	public int getNextAvailableStartingGroup(Tournament tournament, TournamentRound tourRound) {
		return getNextAvailableStartingGroup(tournament, tourRound, false);
	}

	public int getNextAvailableStartingGroup(Tournament tournament, TournamentRound tourRound, boolean useEmptyStartingGroup) {
		return getNextAvailableStartingGroup(tournament, tourRound, useEmptyStartingGroup, 1);
	}

	public List getStartingtimeOrder(Tournament tournament, TournamentRound tournamentRound) {
		List members = null;
		try {
			com.idega.util.IWTimestamp startStamp = new com.idega.util.IWTimestamp(tournamentRound.getRoundDate());
			com.idega.util.IWTimestamp endStamp = new com.idega.util.IWTimestamp(tournamentRound.getRoundEndDate());

			members = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class), "SELECT member.*,grup_num from startingtime, member, tournament_round_startingtime, tournament, tournament_member where tournament.tournament_id = " + tournament.getID() + " and tournament.tournament_id = tournament_member.tournament_id AND tournament_round_startingtime.tournament_round_id = " + tournamentRound.getID() + " AND tournament_round_startingtime.startingtime_id = startingtime.startingtime_id AND startingtime.field_id = " + tournament.getFieldId() + " AND STARTINGTIME.STARTINGTIME_DATE >= '" + startStamp.toSQLDateString() + "' AND STARTINGTIME.STARTINGTIME_DATE <= '" + endStamp.toSQLDateString() + "' AND member.member_id = startingtime.member_id and member.member_id = tournament_member.member_id ORDER by grup_num");
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return members;

	}

	public DropdownMenu getAvailableGrupNums(String dropdownName, Tournament tournament, TournamentRound tRound) throws SQLException {
		DropdownMenu menu = new DropdownMenu(dropdownName);

		int interval = tournament.getInterval();
		int grupNum = 0;
		IWTimestamp start = new IWTimestamp(tRound.getRoundDate());
		start.addMinutes(-interval);
		IWTimestamp end = new IWTimestamp(tRound.getRoundEndDate());
		java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
		menu.addMenuElement(0, "");

		boolean displayTee = false;
		if (tRound.getStartingtees() > 1) {
			displayTee = true;
		}

		while (end.isLaterThan(start)) {
			++grupNum;
			start.addMinutes(interval);
			if (displayTee) {
				menu.addMenuElement(grupNum, extraZero.format(start.getHour()) + ":" + extraZero.format(start.getMinute()) + "&nbsp;&nbsp; (1)");
				menu.addMenuElement(grupNum + "_", extraZero.format(start.getHour()) + ":" + extraZero.format(start.getMinute()) + "&nbsp;&nbsp;  (10)");
			}
			else {
				menu.addMenuElement(grupNum, extraZero.format(start.getHour()) + ":" + extraZero.format(start.getMinute()));
			}
		}

		return menu;
	}
	
	/**
	 * @return Returns boolean array where "boolean"[0] is the answer for teetime group number 1 and "boolean"[maximumGroupNumber-1] is the answer for the last teetime group
	 */
	public boolean[] getIfTeetimeGroupsAreFull(Tournament tournament, TournamentRound tourRound, int maximumGroupNumber, int teeNumber) {
		boolean[] groupIsFull = new boolean[maximumGroupNumber];
		try {
			boolean done = false;
			Startingtime[] startingtimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("SELECT * FROM STARTINGTIME s, TOURNAMENT_ROUND_STARTINGTIME trs WHERE trs.startingtime_id = s.startingtime_id AND trs.tournament_round_id = " + tourRound.getID() + " AND s.field_id=" + tournament.getFieldId()+"and s.tee_number="+teeNumber+" order by s.grup_num");
			com.idega.util.IWTimestamp startStamp = new com.idega.util.IWTimestamp(tourRound.getRoundDate());
			int maximumInGroup = tournament.getNumberInGroup();
			
			int currentGroup = 1; // first groupnumber
			int counter = 0;  //count of people
			boolean currentHasBeenAdded=false;
			if(startingtimes!=null && startingtimes.length>0){  // if any startingtimes reserved
				for (int i = 0; i < startingtimes.length; i++) {
					currentHasBeenAdded=false;
					Startingtime startingtime = startingtimes[i];
					int newGroup = startingtime.getGroupNum();
					if(newGroup==currentGroup){
						counter++;
					}else{
						if(counter>=maximumInGroup){ // if the group is not full
							groupIsFull[currentGroup]=true;
						}
						currentGroup=newGroup; //Set the new group as current group
						counter=1; // must be one registered to the new current group
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		return groupIsFull;
	}


	public int registerMember(is.idega.idegaweb.golf.entity.Member member, Tournament theTournament, String tournament_group_id) throws SQLException {
		int returner = 0;
		try {
			member.addTo(theTournament, "TOURNAMENT_GROUP_ID", tournament_group_id, "UNION_ID", "" + member.getMainUnionID(), "DISMISSAL_ID", "0");
			theTournament.setPosition(member, -1);
			createScorecardForMember(member, theTournament, tournament_group_id);
			returner = 0;
		}
		catch (SQLException s) {
			try {
				s.printStackTrace(System.err);
				Tournament[] tour = (Tournament[]) member.findRelated(theTournament);
				if (tour.length > 0) {
					//add("<br>Me�limur : \""+member.getName()+"\" er �egar skr��/ur �
					// m�ti�");
					returner = 1;
				}
			}
			catch (SQLException sq) {
				sq.printStackTrace(System.err);
				//add("<br>Me�limur : \""+member.getName()+"\" skr��ist ekki � m�ti�");
				returner = 2;
			}
		}

		return returner;
	}

	public void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament) throws SQLException {
		try {
			int tournament_group_id = tournament.getTournamentGroupId(member.getID());
			TournamentGroup tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(tournament_group_id);

			TournamentTournamentGroup[] tTGroup = (TournamentTournamentGroup[]) ((TournamentTournamentGroup) IDOLookup.instanciateEntity(TournamentTournamentGroup.class)).findAllByColumn("tournament_id", tournament.getID() + "", "tournament_group_id", tournament_group_id + "");

			if (tTGroup.length > 0) {
				createScorecardForMember(member, tournament, tTGroup[0]);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

	}

	public void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, String tournament_group_id) throws SQLException {
		TournamentGroup tGroup = null;
		try {
			((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_group_id));
		}
		catch (FinderException fe) {
			throw new SQLException(fe.getMessage());
		}

		TournamentTournamentGroup[] tTGroup = (TournamentTournamentGroup[]) ((TournamentTournamentGroup) IDOLookup.instanciateEntity(TournamentTournamentGroup.class)).findAllByColumn("tournament_id", tournament.getID() + "", "tournament_group_id", tournament_group_id);

		if (tTGroup.length > 0) {
			createScorecardForMember(member, tournament, tTGroup[0]);
		}
		else {
		}
	}

	public void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, TournamentTournamentGroup tTGroup, TournamentRound tRound) throws SQLException {
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
			Tee tee = ((TeeHome) IDOLookup.getHomeLegacy(Tee.class)).findByFieldAndTeeColorAndHoleNumber(field.getID(), tTGroup.getTeeColorId(), 1);
			if (tee != null) {
				CR = tee.getCourseRating();
				slope = tee.getSlope();
			}
		}
		catch (Exception e) {
		}

		handicap = member.getHandicap();
		if (handicap > 36) {
			try {
				MemberInfo memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(member.getID());
				memberInfo.setHandicap(36);
				memberInfo.update();
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}
		}
		maxHandicap = 36;

		if (member.getGender().equalsIgnoreCase("m")) {
			maxHandicap = tournament.getMaxHandicap();
		}
		else {
			maxHandicap = tournament.getFemaleMaxHandicap();
		}
		try {
			handicapper = new Handicap((double) handicap);
			playingHandicap = (float) handicapper.getLeikHandicap((double) slope, (double) CR, (double) tournament.getField().getFieldPar());

			if (playingHandicap > maxHandicap) {
				handicap = Handicap.getInstance().getHandicapForScorecard(tournament.getID(), tTGroup.getTeeColorId(), maxHandicap);
				playingHandicap = maxHandicap;
			}
		}
		catch (IOException io) {
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
					handicap = Handicap.getInstance().getHandicapForScorecard(tournament.getID(), tTGroup.getTeeColorId(), playingHandicap);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		scorecard = (Scorecard) IDOLookup.createLegacy(Scorecard.class);
		scorecard.setMemberId(member.getID());
		scorecard.setTournamentRoundId(tRound.getID());
		//scorecard.setScorecardDate(new
		// idegaTimestamp(tDays[i].getDate()).getTimestamp());
		scorecard.setTotalPoints(0);
		scorecard.setHandicapBefore(handicap);
		scorecard.setHandicapAfter(handicap);
		scorecard.setSlope(slope);
		scorecard.setCourseRating(CR);
		scorecard.setFieldID(field.getID());
		scorecard.setTeeColorID(tTGroup.getTeeColorId());
		scorecard.setHandicapCorrection(false);
		scorecard.setUpdateHandicap(false);
		scorecard.insert();

	}

	public void createScorecardForMember(is.idega.idegaweb.golf.entity.Member member, Tournament tournament, TournamentTournamentGroup tTGroup) throws SQLException {
		TournamentRound[] tRound = tournament.getTournamentRounds();
		int numberOfRounds = tRound.length;
		for (int i = 0; i < numberOfRounds; i++) {
			createScorecardForMember(member, tournament, tTGroup, tRound[i]);
		}
	}

	public boolean isOnlineRegistration(Tournament tournament) {
		return isOnlineRegistration(tournament, IWTimestamp.RightNow());
	}

	public DisplayScores[] getDisplayScores(String SQLConditions, String order) throws SQLException {

		DisplayScores[] tournParticipants = getDisplayScores(SQLConditions, order, "");
		return tournParticipants;

	}

	public DisplayScores[] getDisplayScores(String SQLConditions, String order, String having) throws SQLException {

		if (order.equalsIgnoreCase("tournament_handicap")) {
			order = "9";
		}
		else if (order.equalsIgnoreCase("holes_played")) {
			order = "10";
		}
		else if (order.equalsIgnoreCase("strokes_without_handicap")) {
			order = "11";
		}
		else if (order.equalsIgnoreCase("strokes_with_handicap")) {
			order = "12";
		}
		else if (order.equalsIgnoreCase("total_points")) {
			order = "13 desc";
		}
		else if (order.equalsIgnoreCase("difference")) {
			order = "14";
		}

		String queryString = "select m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, t.tournament_id, tm.tournament_group_id, sum(cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0))) / count(tr.tournament_round_id) as tournament_handicap, count(stroke_count) as holes_played, sum(stroke_count) as strokes_without_handicap, sum(stroke_count) - (sum(cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0))) / count(stroke_count) * count(distinct tr.tournament_round_id)) as strokes_with_handicap, sum(point_count) as total_points, sum(stroke_count) - sum(hole_par) as difference from scorecard s ,stroke str, tournament_round tr, tournament t, field f, member m, union_ u, tournament_member tm" + " where s.tournament_round_id = tr.tournament_round_id and str.scorecard_id = s.scorecard_id and tr.tournament_id = t.tournament_id and t.field_id = f.field_id and s.member_id = m.member_id and m.member_id = tm.member_id and t.tournament_id = tm.tournament_id and tm.union_id = u.union_id" + " and " + SQLConditions + " group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, t.tournament_id, tm.tournament_group_id, s.handicap_before, s.slope, s.course_rating, f.field_par " + having + " order by " + order;

		DisplayScores[] tournParticipants = (DisplayScores[]) ((DisplayScores) IDOLookup.instanciateEntity(DisplayScores.class)).findAll(queryString);

		return tournParticipants;

	}

	public TournamentParticipants[] getTournamentParticipants(String column_name, String column_value, String order) throws SQLException {

		if (order.equalsIgnoreCase("holes_played")) {
			order = "13";
		}
		else if (order.equalsIgnoreCase("round_handicap")) {
			order = "14";
		}
		else if (order.equalsIgnoreCase("strokes_without_handicap")) {
			order = "15";
		}
		else if (order.equalsIgnoreCase("strokes_with_handicap")) {
			order = "16";
		}
		else if (order.equalsIgnoreCase("total_par")) {
			order = "18";
		}
		else if (order.equalsIgnoreCase("difference")) {
			order = "19";
		}

		String queryString = "select m.member_id, m.social_security_number, m.first_name, m.middle_name," + " m.last_name, u.abbrevation,tm.tournament_id, tm.tournament_group_id,s.scorecard_id," + " s.scorecard_date, tr.tournament_round_id, tr.round_number, count(str.stroke_count) holes_played," + " cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0))round_handicap," + " sum(str.stroke_count) strokes_without_handicap, sum(str.stroke_count) - cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ))as numeric (4,0)) strokes_with_handicap," + " s.total_points, sum(str.hole_par) total_par, sum(str.stroke_count) - sum(str.hole_par) difference, tg.name as group_name, tm.paid" + " from tournament_round tr," + " member m, field f, union_ u, tournament_member tm, tournament t, tournament_group tg," + " scorecard s left join stroke str on str.scorecard_id = s.scorecard_id" + " where s.tournament_round_id = tr.tournament_round_id and tr.tournament_id = t.tournament_id" + " and tm.union_id = u.union_id and t.tournament_id = tm.tournament_id and tm.tournament_group_id = tg.tournament_group_id and tm.member_id = m.member_id" + " and s.member_id = m.member_id and t.field_id = f.field_id" + " and " + column_name + " = " + column_value + " group by m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, tm.tournament_id, tm.tournament_group_id,s.scorecard_id, s.scorecard_date, f.field_par,tr.tournament_round_id, tr.round_number, s.total_points, s.handicap_before, s.slope, s.course_rating, tg.name,tm.paid" + " order by " + order;

		//String queryString = "select * from tournament_participants where
		// "+column_name+"="+column_value+" order by "+order;
		TournamentParticipants[] tournParticipants = (TournamentParticipants[]) ((TournamentParticipants) IDOLookup.instanciateEntity(TournamentParticipants.class)).findAll(queryString);

		return tournParticipants;

	}

	public TournamentRoundParticipants[] getTournamentRoundParticipants(String column_name, String column_value, String order) throws SQLException {

		if (order.equalsIgnoreCase("holes_played")) {
			order = "13";
		}
		else if (order.equalsIgnoreCase("round_handicap")) {
			order = "14";
		}
		else if (order.equalsIgnoreCase("strokes_without_handicap")) {
			order = "15";
		}
		else if (order.equalsIgnoreCase("strokes_with_handicap")) {
			order = "16";
		}
		else if (order.equalsIgnoreCase("total_par")) {
			order = "18";
		}
		else if (order.equalsIgnoreCase("difference")) {
			order = "19";
		}

		String queryString = "SELECT m.member_id, m.social_security_number, m.first_name, m.middle_name," + " m.last_name, u.abbrevation,tm.tournament_id, tm.tournament_group_id,s.scorecard_id," + " s.scorecard_date, tr.tournament_round_id, tr.round_number, count(str.stroke_count) as holes_played," + " cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par )) as numeric (4,0)) as round_handicap," + " sum(str.stroke_count) as strokes_without_handicap, sum(str.stroke_count) - cast((( s.handicap_before * s.slope / 113 ) + ( s.course_rating - f.field_par ))as numeric (4,0)) as strokes_with_handicap," + " s.total_points, sum(str.hole_par) as total_par, sum(str.stroke_count) - sum(str.hole_par) as difference, start.grup_num, tg.name as group_name, tm.paid" + " FROM tournament_round tr,member m, field f, union_ u, tournament_member tm, tournament t," + " tournament_group tg, startingtime start, tournament_ROUND_startingtime ts, scorecard s" + " LEFT JOIN stroke str on str.scorecard_id = s.scorecard_id" + " WHERE s.tournament_round_id = tr.tournament_round_id" + " AND tr.tournament_id = t.tournament_id" + " AND tm.union_id = u.union_id" + " AND t.tournament_id = tm.tournament_id" + " AND tm.member_id = m.member_id" + " AND s.member_id = m.member_id" + " AND t.field_id = f.field_id" + " AND tR.tournament_ROUND_id = ts.tournament_ROUND_id" + " AND ts.startingtime_id = start.startingtime_id" + " AND start.field_id = t.field_id"
		//+ " AND start.startingtime_date >= cast (tr.round_date as datetime)"
				//+ " AND start.startingtime_date <= cast (tr.round_end_date as
				// datetime)"
				+ " AND m.member_id = start.member_id" + " AND tm.tournament_group_id = tg.tournament_group_id" + " AND " + column_name + " = " + column_value + " GROUP BY m.member_id, m.social_security_number, m.first_name, m.middle_name, m.last_name, u.abbrevation, tm.tournament_id, tm.tournament_group_id,s.scorecard_id, s.scorecard_date, f.field_par,tr.tournament_round_id, tr.round_number, s.total_points, s.handicap_before, s.slope, s.course_rating, start.grup_num, tg.name,tm.paid" + " ORDER BY " + order;

		//String queryString = "select * from tournament_round_participants where
		// "+column_name+"="+column_value+" order by "+order;
		TournamentRoundParticipants[] tournParticipants = (TournamentRoundParticipants[]) ((TournamentRoundParticipants) IDOLookup.instanciateEntity(TournamentRoundParticipants.class)).findAll(queryString);

		return tournParticipants;

	}

	public StartingtimeView[] getStartingtimeView(int tournament_round_id, String column_name, String column_value, String column_name_1, String column_value_1, String order) throws SQLException {
		return getStartingtimeView(tournament_round_id, column_name, column_value, column_name_1, column_value_1, 1, order);
	}

	public StartingtimeView[] getStartingtimeView(int tournament_round_id, String column_name, String column_value, String column_name_1, String column_value_1, int tee_number, String order) throws SQLException {
		return getStartingtimeView(tournament_round_id, column_name, column_value, column_name_1, column_value_1, tee_number, order, -1, -1);
	}

	public StartingtimeView[] getStartingtimeView(int tournament_round_id, String column_name, String column_value, String column_name_1, String column_value_1, int tee_number, String order, int first_group_number, int last_group_number) throws SQLException {

		is.idega.idegaweb.golf.entity.StartingtimeView[] startView = new is.idega.idegaweb.golf.entity.StartingtimeView[0];

//		String queryString = getStartingtimeViewSql(tournament_round_id, column_name, column_value, column_name_1, column_value_1, tee_number, order, first_group_number, last_group_number, false);
		String queryString = getStartingtimeViewSql(tournament_round_id, column_name, column_value, column_name_1, column_value_1, tee_number, order, first_group_number, last_group_number);

		//System.out.println("GettingStartingtimeView\n" + queryString);
		try {
			startView = (StartingtimeView[]) ((StartingtimeView) IDOLookup.instanciateEntity(StartingtimeView.class)).findAll(queryString);
			//System.out.println("GettingStartingtimeView\n"+queryString);
			if (startView == null || startView.length < 1) {
				queryString = getStartingtimeViewSql(tournament_round_id, column_name, column_value, column_name_1, column_value_1, tee_number, order, first_group_number, last_group_number); 
//				queryString = getStartingtimeViewSql(tournament_round_id, column_name, column_value, column_name_1, column_value_1, tee_number, order, first_group_number, last_group_number, true);
				//System.out.println("GettingStartingtimeView 2\n"+queryString);
				startView = (StartingtimeView[]) ((StartingtimeView) IDOLookup.instanciateEntity(StartingtimeView.class)).findAll(queryString);
			}
		}
		catch (SQLException sql) {
			System.err.println("queryString " + queryString);
			sql.printStackTrace(System.err);
		}

		return startView;

	}

	private String getStartingtimeViewSql(int tournament_round_id, String column_name, String column_value, String column_name_1, String column_value_1, int tee_number, String order, int first_group_number, int last_group_number) {
		boolean onlyReserved = true;
		String queryString =	"select distinct (st.startingtime_id), st.field_id, st.member_id, st.startingtime_date,st.grup_num, "
				+ "m.first_name,m.middle_name, m.last_name,m.social_security_number,mi.handicap ,u.abbrevation, u.union_id";
				if (!onlyReserved) {
					queryString = queryString + ", tm.paid";
				}
				queryString = queryString + " from startingtime st, member m, union_member_info umi, union_ u, member_info mi, tournament_round_startingtime trs, tournament_round tr ";
				if (!onlyReserved) {
					queryString = queryString + ", tournament_member tm";
				}
				queryString = queryString +	 " where "
				+ "trs.tournament_round_id = tr.tournament_round_id ";
				if (!onlyReserved) {
					queryString = queryString + "AND tr.tournament_id = tm.tournament_id ";
				}
				queryString = queryString + "AND trs.startingtime_id = st.startingtime_id AND trs.tournament_round_id = "
				+ tournament_round_id
				+ " AND "
				+ "m.member_id = st.member_id AND "
				+ "m.member_id = mi.member_id AND "
				+ "m.member_id = umi.member_id AND ";
				if (!onlyReserved) {
					queryString = queryString +  "(m.member_id = tm.member_id OR m.member_id = 1) AND ";
				} 
				queryString = queryString + "u.union_id = umi.union_id AND "
				+ "st.tee_number = "
				+ tee_number
				+ " AND "
				+ "umi.membership_type = 'main' AND "
				+ "umi.member_status = 'A' ";
		if (!column_name.equalsIgnoreCase(""))
		{
			queryString += " AND " + column_name + " = " + column_value;
		}
		if (!column_name_1.equalsIgnoreCase(""))
		{
			queryString += " AND " + column_name_1 + " = " + column_value_1;
		}
		if (first_group_number > 0)
		{
			queryString += "AND st.grup_num >= " + first_group_number;
		}
		if (last_group_number > 0)
		{
			queryString += "AND st.grup_num <= " + last_group_number;
		}
		if (!order.equalsIgnoreCase(""))
		{
			queryString += " order by " + order;
		}
		return queryString;
	}
/*	private String getStartingtimeViewSql(int tournament_round_id, String column_name, String column_value, String column_name_1, String column_value_1, int tee_number, String order, int first_group_number, int last_group_number, boolean onlyReserved) {
		String queryString = "select distinct (st.startingtime_id), st.field_id, st.member_id, st.startingtime_date,st.grup_num, " + "m.first_name,m.middle_name, m.last_name,m.social_security_number,mi.handicap ,u.abbrevation, u.union_id";
		if (!onlyReserved) {
			queryString = queryString + ", tm.paid";
		}
		queryString = queryString + " from startingtime st, member m, union_member_info umi, union_ u, member_info mi, tournament_round_startingtime trs, tournament_round tr , tournament_member tm where " + "trs.tournament_round_id = tr.tournament_round_id ";
		if (!onlyReserved) {
			queryString = queryString + "AND tr.tournament_id = tm.tournament_id ";
		}
		queryString = queryString + "AND trs.startingtime_id = st.startingtime_id AND trs.tournament_round_id = " + tournament_round_id + " AND " + "m.member_id = st.member_id AND " + "m.member_id = mi.member_id AND " + "m.member_id = umi.member_id AND ";
		if (!onlyReserved) {
			queryString = queryString + "(m.member_id = tm.member_id OR m.member_id = 1) AND ";
		}
		queryString = queryString + "u.union_id = umi.union_id AND " + "st.tee_number = " + tee_number + " AND " + "umi.membership_type = 'main' AND " + "umi.member_status = 'A' ";
		if (!column_name.equalsIgnoreCase("")) {
			queryString += " AND " + column_name + " = " + column_value;
		}
		if (!column_name_1.equalsIgnoreCase("")) {
			queryString += " AND " + column_name_1 + " = " + column_value_1;
		}
		if (first_group_number > 0) {
			queryString += "AND st.grup_num >= " + first_group_number;
		}
		if (last_group_number > 0) {
			queryString += "AND st.grup_num <= " + last_group_number;
		}
		if (!order.equalsIgnoreCase("")) {
			queryString += " order by " + order;
		}
		return queryString;
	}
*/
	public int getInt() {

		return 4;

	}

	public boolean isOnlineRegistration(Tournament tournament, IWTimestamp dateToCheck) {
		boolean returner = false;
		if (tournament.getIfRegistrationOnline()) {
			if (dateToCheck.isLaterThan(new IWTimestamp(tournament.getFirstRegistrationDate()))) {
				if (new IWTimestamp(tournament.getLastRegistrationDate()).isLaterThan(dateToCheck)) {
					returner = true;
				}
			}
		}

		return returner;
	}

	public void removeMemberFromTournament(IWContext modinfo, Tournament tournament, is.idega.idegaweb.golf.entity.Member member) {
		removeMemberFromTournament(modinfo, tournament, member, -10);
	}

	public void removeMemberFromTournament(IWContext modinfo, Tournament tournament, is.idega.idegaweb.golf.entity.Member member, int startingGroupNumber) {

		try {
			String member_id = "" + member.getID();

			IWTimestamp stamp;
			IWTimestamp endStamp;
			String SQLString;
			List startingTimes;
			Startingtime sTime;
			Scorecard[] scorecards;
			Scorecard scorecard;

			TournamentRound[] tRounds = tournament.getTournamentRounds();
			for (int j = 0; j < tRounds.length; j++) {
				stamp = new IWTimestamp(tRounds[j].getRoundDate());
				endStamp = new IWTimestamp(tRounds[j].getRoundEndDate());
				endStamp.addDays(1);
				invalidateStartingTimeCache(modinfo, tournament.getID(), String.valueOf(tRounds[j].getID()));

				if (startingGroupNumber != -10) {
					SQLString = "SELECT * FROM STARTINGTIME WHERE FIELD_ID = " + tournament.getFieldId() + " AND MEMBER_ID = " + member_id + " AND GRUP_NUM = " + startingGroupNumber + " AND STARTINGTIME_DATE >= '" + stamp.toSQLDateString() + "' AND STARTINGTIME_DATE < '" + endStamp.toSQLDateString() + "'";
				}
				else {
					SQLString = "SELECT * FROM TOURNAMENT_ROUND_STARTINGTIME, STARTINGTIME WHERE TOURNAMENT_ROUND_STARTINGTIME.TOURNAMENT_ROUND_ID = " + tRounds[j].getID() + " AND STARTINGTIME.STARTINGTIME_ID = TOURNAMENT_ROUND_STARTINGTIME.STARTINGTIME_ID AND FIELD_ID =" + tournament.getFieldId() + " AND MEMBER_ID = " + member_id;
				}
				startingTimes = EntityFinder.findAll((Startingtime) IDOLookup.instanciateEntity(Startingtime.class), SQLString);
				if (startingTimes != null) {
					try {
						for (int i = 0; i < startingTimes.size(); i++) {
							sTime = (Startingtime) startingTimes.get(i);
							sTime.removeFrom(tRounds[j]);
//							try {
//								sTime.removeFrom((Tournament) IDOLookup.instanciateEntity(Tournament.class));
//							} catch (Exception ignore) {}
							sTime.delete();
						}
					}
					catch (Exception ex) {
						System.err.println("Tournament Controller : removeMemberFromTournament, startingTime  -  (VILLA)");
						ex.printStackTrace(System.err);
					}
				}

				scorecards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAllByColumn("MEMBER_ID", member_id, "TOURNAMENT_ROUND_ID", tRounds[j].getID() + "");
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

	public boolean getHasMemberPaid(Tournament tournament, Member member) throws Exception {
		String[] repps = SimpleQuerier.executeStringQuery("select paid from tournament_member where member_id = " + member.getID() + " AND tournament_id = " + tournament.getID());
		if (repps != null && repps.length > 0 && repps[0] != null) { return "Y".equalsIgnoreCase(repps[0]); }
		return false;

	}
	
	public int getTournamentGroup(Member member, Tournament tournament) throws Exception {
		String[] repps = SimpleQuerier.executeStringQuery("select tournament_group_id from tournament_member where member_id = "+member.getID()+" AND tournament_id = "+tournament.getID());
		if (repps != null && repps.length > 0 && repps[0] != null) {
			return Integer.parseInt(repps[0]);
		}
		return -1;
	}

	public void setAllMemberToNotPaid(Tournament tournament) throws Exception {
		SimpleQuerier.execute("update tournament_member set paid = 'N' where tournament_id = " + tournament.getID());
	}

	public boolean setHasMemberPaid(Tournament tournament, Member member, boolean hasPaid) throws Exception {
		String sPaid = "Y";
		if (!hasPaid) {
			sPaid = "N";
		}
		return SimpleQuerier.execute("update tournament_member set paid = '" + sPaid + "' where member_id = " + member.getID() + " AND tournament_id = " + tournament.getID());
	}

	public void invalidateStartingTimeCache(IWContext modinfo, Tournament tournament) throws SQLException {
		TournamentRound[] tRounds = tournament.getTournamentRounds();
		for (int i = 0; i < tRounds.length; i++) {
			invalidateStartingTimeCache(modinfo, tournament.getID(), Integer.toString(tRounds[i].getID()));
		}
	}

	public void invalidateStartingTimeCache(IWContext modinfo, int tournamentID, String tournamentRoundID) {
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + false + "_" + false + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + true + "_" + false + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + true + "_" + true + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + true + "_" + false + "_" + true);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + true + "_" + true + "_" + true);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + false + "_" + true + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + false + "_" + true + "_" + true);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_" + tournamentRoundID + "_" + false + "_" + false + "_" + true);

		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + false + "_" + false + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + true + "_" + false + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + true + "_" + true + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + true + "_" + false + "_" + true);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + true + "_" + true + "_" + true);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + false + "_" + true + "_" + false);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + false + "_" + true + "_" + true);
		modinfo.removeApplicationAttribute("tournament_startingtime_" + tournamentID + "_null_" + false + "_" + false + "_" + true);
	}

}
