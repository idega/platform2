/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.clubs.presentation.MemberCorrectWindow;
import is.idega.idegaweb.golf.clubs.presentation.UnionCorrectWindow;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberBMPBean;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class TournamentRegistration extends TournamentBlock {


public void setTournament(IWContext modinfo, Tournament tournament) {
    modinfo.setSessionAttribute("golf_register_member_tournament",tournament);
}

public Tournament getTournament(IWContext modinfo) {
    Tournament tournament = (Tournament) modinfo.getSessionAttribute("golf_register_member_tournament");
    return tournament;
}

protected boolean tournamentMustBeSet() {
	return true;
}


public void main(IWContext modinfo)throws Exception{

	String tournament_id;
        String action = modinfo.getParameter("action");
        Table table = new Table(2,3);
        Member member = getMember();

        Tournament tournament = null;
        tournament_id= modinfo.getParameter("tournament");
        if (tournament_id == null) {
	        tournament_id = (String) modinfo.getSessionAttribute("golf_register_member_tournament_id");
	        if (tournament_id != null) {
	        	tournament = getTournament(modinfo);
	        	action = "selectmember";
	        }else {
	        	int tournamentID = getTournamentID(modinfo);
	        	if (tournamentID > 0) {
	        		tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(tournamentID);
	        		setTournament(modinfo, tournament);
	        	}
	        }
	    	} else {
	        tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
	        setTournament(modinfo,tournament);
        }

        if (action == null) {
            action = "selectmember";
        }


	if (tournament != null){
          if(action.equals("register")){
              /*
                if (member== null){
                  table.add("Þú verður að skrá þig inn og vera skráður félagi á golf.is");
                  add(table);
                }else{
                    table.add("Ertu viss um að þú viljir skrá þig mótið "+tournament.getName());
                    Form form = new Form();
                    table.add(form,1,2);
                    form.maintainParameter("tournament_id");
                    SubmitButton confirmButton  = new SubmitButton("Já","action","confirmregister");
                    form.add(confirmButton);
                    add(table);
                }
              */
            }else if (action.equals("selectmember") ) {
                selectMember(modinfo,getResourceBundle());
            }else if (action.equals("searchBySocialSecurityNumber")) {
                searchBySocialSecurityNumber(modinfo,getResourceBundle());
            }else if (action.equals("getSearchBySocialSecurityNumberResults")) {
                getSearchBySocialSecurityNumberResults(modinfo,getResourceBundle());
            }else if (action.equals("registermarkedmembers")) {
                checkMarkedMembers(modinfo,getResourceBundle());
            }else if (action.equals("confirmRegisterMarkedMembers")) {
                registerMarkedMembers(modinfo,getResourceBundle());
            }else if (action.equals("searchByName")) {
                searchByName(modinfo,getResourceBundle());
            }else if (action.equals("getSearchByNameResults")) {
                getSearchByNameResults(modinfo,getResourceBundle());
            }else if (action.equals("unionMemberList")) {
                getUnionMemberList(modinfo,getResourceBundle());
            }else if (action.equals("removeMemberFromTournament")) {
                String startingGroupNumber = modinfo.getParameter("startingGroupNumber");
                String member_id = modinfo.getParameter("member_id");
                TournamentController.removeMemberFromTournament(modinfo, tournament,((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id)));
                getDirectRegistrationTable(modinfo,false,getResourceBundle());
            }else if (action.equals("directRegistrationMembersChosen")) {
                finalizeDirectRegistration(modinfo,getResourceBundle());
            }else if (action.equals("tournamentMemberList")) {
                String sub_action = modinfo.getParameter("sub_action");
                if (sub_action != null) {
                	if (sub_action.equals("update")) {
                		TournamentController.setAllMemberToNotPaid(tournament);
						String[] paid = modinfo.getParameterValues("paid");
						Member mMember;
						if (paid != null) {
							for (int y = 0; y < paid.length; y++) {
								mMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(paid[y]));
								TournamentController.setHasMemberPaid(tournament, mMember, true);
							}
						}
						String[] rem = modinfo.getParameterValues("removeMember");
						if (rem != null) {
							for (int y = 0; y < rem.length; y++) {
								mMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(rem[y]));
		                        TournamentController.removeMemberFromTournament(modinfo, tournament,mMember);
							}
						}
                	}
                }
                tournamentMemberList(modinfo,getResourceBundle());
            }else {
                noAction(modinfo,getResourceBundle());
            }

	}else{
            noAction(modinfo,getResourceBundle());
        }

}

public void noAction(IWContext modinfo,IWResourceBundle iwrb) {
            Form form = new Form();
            Table table2 = new Table(1,3);
            table2.add(iwrb.getLocalizedString("tournament.choose_tournament","Choose a tournament") ,1,1);
            DropdownMenu menu = TournamentController.getDropdownOrderedByUnion(new DropdownMenu("tournament"),modinfo);
                menu.setMarkupAttribute("size","10");


            table2.add(menu,1,2);
            GenericButton aframButton = getButton(new SubmitButton(localize("tournament.continue","Continue"),"flipp","selectmember"));

            HiddenInput hidden = new HiddenInput("action","selectmember");
            //SubmitButton button = new SubmitButton("Velja","action","selectmember");
            table2.add(aframButton,1,3);
            table2.add(hidden);
            table2.setAlignment(1,3,"right");
            add(form);
            form.add(table2);
}

public void selectMember(IWContext modinfo,IWResourceBundle iwrb) throws SQLException{
    Tournament tournament = getTournament(modinfo);

    if(tournament.isDirectRegistration()){
        directRegistration(modinfo,iwrb);
    }else{
        add(tournament.getName() );
        this.searchByName(modinfo,iwrb);
        this.searchBySocialSecurityNumber(modinfo,iwrb);
        if (AccessControl.isClubAdmin(modinfo) ) {
            Link link = new Link(iwrb.getLocalizedString("tournament.list_of_club_members","List of club members"));
                link.addParameter("action","unionMemberList");
            add(link);
            add("<br>");
        }
        Link membersInTournament = new Link(iwrb.getLocalizedString("tournament.list_of_registered_members","Members registered in tournament"));
            membersInTournament.addParameter("action","tournamentMemberList");
        add(membersInTournament);
    }

}

public void directRegistration(IWContext modinfo,IWResourceBundle iwrb) throws SQLException {


    String subAction = modinfo.getParameter("sub_action");
    if (subAction != null) {
        if (subAction.equals("saveDirectRegistration")) {
            saveDirectRegistration(modinfo,iwrb);
        }
        else if (subAction.equals("removeMemberFromTournament")) {
            Tournament tournament = getTournament(modinfo);
            String startingGroupNumber = modinfo.getParameter("startingGroupNumber");
            String member_id = modinfo.getParameter("member_id");
            try {
            	TournamentController.removeMemberFromTournament(modinfo, tournament,((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id)));
            }
            catch (FinderException fe) {
            	throw new SQLException(fe.getMessage());
            }
            getDirectRegistrationTable(modinfo,false,iwrb);


        }
        else if (subAction.equals("")) {
            getDirectRegistrationTable(modinfo, false,iwrb);
        }
    }
    else {
        getDirectRegistrationTable(modinfo, false,iwrb);
    }
}


public void tournamentMemberList(IWContext modinfo, IWResourceBundle iwrb) throws SQLException{
    Tournament tournament = getTournament(modinfo);
    List members = getMembersInTournamentList(tournament);
    add(tournament.getName());

    String action_to_forward = "tournamentMemberList";

    Union union;
    int union_id = -1;
    String abbrevation = "-";

    if (members == null) {
        add("<br><br>"+iwrb.getLocalizedString("tournament.nobody_regstered","No one is registered in the tournament") );
    }
    else {
    	Form form = new Form();
	        form.addParameter("action",action_to_forward);
        Table table = new Table();
        form.add(table);
        java.text.DecimalFormat handicapFormat = new java.text.DecimalFormat("0.0");
        int numberOfMembers = members.size();
        Member member;
        Link link;
        CheckBox paid;
        CheckBox del;
        int row = 1;
        table.add(iwrb.getLocalizedString("tournament.kt","Ssn"), 1, row);
        table.add(iwrb.getLocalizedString("tournament.name","Name"), 3, row);
        table.add(iwrb.getLocalizedString("tournament.club","Union"), 5, row);
        table.add(iwrb.getLocalizedString("tournament.Hcp","Hcp"), 7, row);
        table.add(iwrb.getLocalizedString("tournament.Paid","Paid"), 9, row);
        table.add(iwrb.getLocalizedString("tournament.Del","Del"), 11, row);
        for (int i = 0; i < members.size(); i++) {
            ++row;
            member = (Member) members.get(i);
            table.add(member.getSocialSecurityNumber(),1,row);
            table.add(member.getName(),3,row);

            union_id = tournament.getTournamentMemberUnionId(member);

            if (union_id != -1) {
              try {
              	union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(union_id);
              }
              catch (FinderException fe) {
              	throw new SQLException(fe.getMessage());
              }
              abbrevation = union.getAbbrevation();
            }else {
              abbrevation = "-";
            }


            table.add(abbrevation,5,row);
            table.add(handicapFormat.format(member.getHandicap())+"",7,row);
            
            try {
	            paid = new CheckBox("paid", Integer.toString(member.getID()));
	            paid.setChecked(TournamentController.getHasMemberPaid(tournament, member));
	            table.add(paid, 9, row);
            }catch (Exception e) {
            	table.add("Villa", 9, row);
            }
            
            del = new CheckBox("removeMember", Integer.toString(member.getID()));
            table.add(del, 11, row);

			/*
            link = new Link(new Image("/pics/handicap/trash.gif","Skrá úr móti"));
                link.addParameter("action",action_to_forward);
                link.addParameter("sub_action","removeMember");
                link.addParameter("member_id",member.getID()+"");
            table.add(link,11,row);
            */
        }
        ++row;
        GenericButton save = getButton(new SubmitButton(localize("tournament.save","Save"), "sub_action","update"));
        table.mergeCells(1, row, 11, row);
        table.setAlignment(1, row, "right");
        table.add(save, 1, row);

        ++row;
        table.mergeCells(1,row,5,row);
        table.add(numberOfMembers+ " "+iwrb.getLocalizedString("tournament.members_registered","members_registered"),1,row);

        add(form);
    }

    add("<br><br>");
    Link back = getLocalizedLink("tournament.back","Back");
        back.addParameter("action","selectmember");
    add(back);

}

public void getDirectRegistrationTable(IWContext modinfo, boolean view,IWResourceBundle iwrb) throws SQLException {
    add("<center><big><b>");
    add(iwrb.getLocalizedString("tournament.tee_time_registration","Tee time registration"));
    add("</b></big></center>");

    Tournament tournament = getTournament(modinfo);
    TournamentRound[] tourRounds = tournament.getTournamentRounds();
    String tournament_round_id = modinfo.getParameter("tournament_round");

    if (tourRounds.length > 0) {
        String viewString = modinfo.getParameter("viewOnly");
        if (viewString != null) {
            if (viewString.equalsIgnoreCase("true")) {
                view = true;
            }
            else if (viewString.equalsIgnoreCase("false")) {
                view = false;
            }
        }

        Table table = new Table(2,1);
          table.setWidth("90%");
          table.setAlignment(1,1,"left");
          table.setAlignment(2,1,"right");
          table.setCellpadding(0);
          table.setCellspacing(0);

        if (AccessControl.isAdmin(modinfo) || AccessControl.isClubAdmin(modinfo) ) {
            GenericButton theChecker = getButton(new GenericButton(iwrb.getLocalizedString("tournament.verify_tournament","Verify tournament")));
            	theChecker.setWindowToOpen(TournamentCheckerWindow.class);
                theChecker.addParameter("tournament_id",tournament.getID());
                theChecker.addParameter("action","doCheck");
            table.add(theChecker,1,1);

        }

        GenericButton theSearch = getButton(new GenericButton(localize("tournament.search_for_member","Search for member")));
        		theSearch.setWindowToOpen(MemberSearchWindow.class);
            theSearch.addParameter("action","getSearch");
        table.add(theSearch,2,1);


        add(table);
        add(TournamentController.getStartingtimeTable(tournament,tournament_round_id, view));
        Link link = getLink(iwrb.getLocalizedString("tournament.print","print"));
        link.addParameter(TournamentStartingtimeWindow.PARAMETER_TOURNAMENT_ROUND_ID, tournament_round_id);
        link.addParameter(TournamentStartingtimeWindow.PARAMETER_TOURNAMENT_ID, tournament.getID());
        link.setWindowToOpen(TournamentStartingtimeWindow.class);
        add(link);

    }
    else {
        add(iwrb.getLocalizedString("tournament.no_tournament_rounds_in_tournament","No rounds have been specified for this tournament"));
        add("<br>");
    }

}



public void finalizeDirectRegistration(IWContext modinfo,IWResourceBundle iwrc) throws SQLException {
    if (isSaveEnabled(modinfo)) {

        Tournament tournament = getTournament(modinfo);

        String tournament_round = modinfo.getParameter("tournament_round");

        String[] member_ids = modinfo.getParameterValues("member_id");
        String[] tournament_groups = modinfo.getParameterValues("tournament_group");
        String[] starting_time = modinfo.getParameterValues("starting_time");
        String[] starting_tee = modinfo.getParameterValues("starting_tee");
        String sTournamentRoundId = modinfo.getParameter("tournament_round");
        String handicapCorrection;

        Member member;
        TournamentGroup tGroup;
        javax.transaction.TransactionManager tm;
        if (member_ids != null) {
            for (int i = 0; i < member_ids.length; i++) {
                tm = com.idega.transaction.IdegaTransactionManager.getInstance();
                try {
                    member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_ids[i]));
                    if (!TournamentController.isMemberRegisteredInTournament(tournament, member)) {
                        tm.begin();
                        handicapCorrection = modinfo.getParameter("handicap_correction_"+member.getID());
                        if (handicapCorrection != null) {
                            if (!handicapCorrection.equalsIgnoreCase("")) {
                                correctHandicap(modinfo,member,handicapCorrection);
                            }
                        }
                        tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_groups[i]));
                        TournamentController.registerMember(member,tournament,tournament_groups[i]);
                        if (starting_tee[i].equals("10")) {
                            TournamentController.setupStartingtime(modinfo, member,tournament,Integer.parseInt(sTournamentRoundId),Integer.parseInt(starting_time[i]),10);
                        }else {
                            TournamentController.setupStartingtime(modinfo, member,tournament,Integer.parseInt(sTournamentRoundId),Integer.parseInt(starting_time[i]));
                        }
                        tm.commit();
                    }
                    else {
                        //System.err.println("ÞEGAR SKRÁÐUR Í MÓTIÐ");
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace(System.err);
                    try {
                      tm.rollback();
                    }catch (javax.transaction.SystemException se) {se.printStackTrace(System.err);}
                }
            }
        }


        String[] extraPlayers = modinfo.getParameterValues("extra_player");
        if (extraPlayers != null) {
            String[] XPSocial = modinfo.getParameterValues("extra_player_social_security_number");
            String[] XPStartingtime = modinfo.getParameterValues("extra_player_starting_time");
            String[] XPName = modinfo.getParameterValues("extra_player_name");
            String[] XPGroups = modinfo.getParameterValues("extra_player_groups");
            String[] XPGender = modinfo.getParameterValues("extra_player_gender");
            String[] XPHandicap = modinfo.getParameterValues("extra_player_handicap");

            IWTimestamp dateStamp;
            int year;

            int XP = 0;

            for (int i = 0; i < extraPlayers.length; i++) {
                XP = Integer.parseInt(extraPlayers[i]);

                 dateStamp = new IWTimestamp();
                  year = Integer.parseInt(XPSocial[XP].substring(4,6));
                  if ( XPSocial[XP].substring(9,10).equals("9")) {
                    year = 1900 + year;
                  }else if ( XPSocial[XP].substring(9,10).equals("0")) {
                    year = 2000 + year;
                  }else if ( XPSocial[XP].substring(9,10).equals("8")) {
                    year = 1800 + year;
                  }else if ( XPSocial[XP].substring(9,10).equals("1")) {
                    year = 2100 + year;
                  }

                  dateStamp.setYear(year);
                  dateStamp.setMonth(Integer.parseInt(XPSocial[XP].substring(2,4)));
                  dateStamp.setDay(Integer.parseInt(XPSocial[XP].substring(0,2)));

                Member XPmember = (Member) IDOLookup.createLegacy(Member.class);
                    XPmember.setSocialSecurityNumber(XPSocial[XP]);
                    XPmember.setFirstName(XPName[XP]);
                    XPmember.setDateOfBirth(dateStamp.getSQLDate());
                    XPmember.setGender(XPGender[XP]);
                    XPmember.insert();

                MemberInfo memberInfo = (MemberInfo) IDOLookup.createLegacy(MemberInfo.class);
                    memberInfo.setMemberId(XPmember.getID());
                    memberInfo.setHandicap(Float.parseFloat(XPHandicap[XP]));
                    memberInfo.setFirstHandicap(Float.parseFloat(XPHandicap[XP]));
                    memberInfo.insert();

                UnionMemberInfo uMInfo = (UnionMemberInfo) IDOLookup.createLegacy(UnionMemberInfo.class);
                    uMInfo.setUnionID(1);
                    uMInfo.setMemberID(XPmember.getID() );
                    uMInfo.setMembershipType("main");
                    uMInfo.setMemberStatus("A");
                    uMInfo.insert();

                TournamentController.registerMember(XPmember,tournament,XPGroups[XP]);
                TournamentController.setupStartingtime(modinfo, XPmember, tournament, Integer.parseInt(tournament_round), Integer.parseInt(XPStartingtime[XP]) );
            }

        }




        disableSave(modinfo);
    }

    directRegistration(modinfo,iwrc);
}


public void saveDirectRegistration(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
    Tournament tournament = getTournament(modinfo);

    enableSave(modinfo);

	try {
		String[] membersPaid = modinfo.getParameterValues("paid");
		Member member;
		TournamentController.setAllMemberToNotPaid(tournament);
		if (membersPaid != null) {
			for (int i = 0; i < membersPaid.length; i++) {
                member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(membersPaid[i]));
                TournamentController.setHasMemberPaid(tournament, member, true);
			}
			TournamentController.invalidateStartingTimeCache(modinfo, tournament);		
		}
	}catch (Exception e) {
        System.err.println("TournamentController : saveDirectRegistration : setting PAID");
        e.printStackTrace(System.err);
	}


    try {
        String[] memberIdsToDelete = modinfo.getParameterValues("deleteMember");
        Member member;
        if (memberIdsToDelete != null) {
            for (int i = 0; i < memberIdsToDelete.length; i++) {
                member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(memberIdsToDelete[i]));
                TournamentController.removeMemberFromTournament(modinfo, tournament,member);
            }
        }
    }
    catch (Exception exc) {
        System.err.println("TournamentController : saveDirectRegistration : deleting members");
        exc.printStackTrace(System.err);
    }


    boolean noOne = true;
    try {
        String sNumberOfGroups = modinfo.getParameter("number_of_groups");
        int iNumberOfGroups = Integer.parseInt(sNumberOfGroups);

        String sTournamentRoundId = modinfo.getParameter("tournament_round");
        int iTournamentRoundId = Integer.parseInt(sTournamentRoundId);

        String[] numbers;
        Member member = null;

        Link unionCorrect = null;

        Link memberCorrect = null;
        String[] socials = null;

        int[] errors = new int[4];


        Table content = new Table();
            content.setWidth("85%");

        TextInput correction;
        Form form = new Form();
            form.maintainParameter("tournament_round");
            form.add(new HiddenInput("action","directRegistrationMembersChosen"));

        Form removeForm = new Form();
            form.maintainParameter("tournament_round");
            form.add(new HiddenInput("action","removeMembersFromTournament"));

        Table table = new Table();
            table.setBorder(0);
            int tableRow = 1;
            table.setWidth("100%");
            table.add("<u>"+iwrb.getLocalizedString("tournament.name","Name")+"</u>",1,tableRow);
            table.add("<u>"+iwrb.getLocalizedString("tournament.club","Club")+"</u>",3,tableRow);
            table.add("<u>"+iwrb.getLocalizedString("tournament.handicap","Handicap")+"</u>",5,tableRow);
            table.add("<u>"+iwrb.getLocalizedString("tournament.correction","Correction")+"</u>",7,tableRow);

        Table other = new Table();
            other.setBorder(0);
            int otherRow = 1;
            other.setWidth("100%");
            other.add("<u>"+iwrb.getLocalizedString("tournament.social_security_number","Social security number")+"</u>",1,otherRow);
            other.add("<u>"+iwrb.getLocalizedString("tournament.name","Name")+"</u>",3,otherRow);
            other.add("<u>"+iwrb.getLocalizedString("tournament.group","Group")+"</u>",5,otherRow);
            other.add("<u>"+iwrb.getLocalizedString("tournament.handicap","Handicap")+"</u>",7,otherRow);
            other.add("<u>"+iwrb.getLocalizedString("tournament.sex","Sex")+"</u>",9,otherRow);
            other.add("<u>"+iwrb.getLocalizedString("tournament.register_sm","Register")+"</u>",11,otherRow);

        Table done = new Table();
            done.setBorder(0);
            int doneRow = 1;
            done.add("<u>"+iwrb.getLocalizedString("tournament.already_registered","Already registered")+"</u>");
            done.setWidth("100%");
            done.setAlignment(1,1,"left");

        Table rejects = new Table();
            rejects.setBorder(0);
            rejects.setWidth("100%");
            int rejectsRow = 1;
            rejects.setAlignment(1,1,"left");
            rejects.add("<u>"+iwrb.getLocalizedString("tournament.can_not_register_pl","Can not register")+"</u>");

        TournamentGroup[] allGroupsInTournament = tournament.getTournamentGroups();
        DropdownMenu allGroups = new DropdownMenu(allGroupsInTournament,"extra_player_groups");

        TextInput derName;
        boolean star = false;

        DropdownMenu memberGender = new DropdownMenu("extra_player_gender");
            memberGender.addMenuElement("M",iwrb.getLocalizedString("tournament.male","Male"));
            memberGender.addMenuElement("F",iwrb.getLocalizedString("tournament.female","Female"));

        for (int i = 1; i <= iNumberOfGroups ; i++) {
            numbers = (String[]) modinfo.getParameterValues("social_security_number_for_group_"+i);
            if (numbers != null) {
                for (int j = 0; j < numbers.length; j++) {
                    if (!numbers[j].equals("")) {
                        member =  (Member) MemberBMPBean.getMember(numbers[j]);

                        if (member == null) {
                            ++otherRow;
                            other.add(numbers[j],1,otherRow);
                            other.add(new HiddenInput("extra_player_social_security_number",numbers[j]),1,otherRow);
                            other.add(new HiddenInput("extra_player_starting_tee","10"),1,otherRow);
                            derName = new TextInput("extra_player_name");
                            other.add(derName,3,otherRow);
                            TextInput hand = new TextInput("extra_player_handicap");
                              hand.setSize(3);
                            try {
                                Integer.parseInt(numbers[j].substring(0,6));
                                Integer.parseInt(numbers[j].substring(9,10));
                            }
                            catch (NumberFormatException n) {
                                derName.setContent(iwrb.getLocalizedString("tournament.ssn_is_wrong","Social security number is incorrect"));
                            }
                            if (numbers[j].length() < 10) {
                                derName.setContent(iwrb.getLocalizedString("tournament.ssn_is_wrong","Social security number is incorrect"));
                            }

                            other.add(allGroups,5,otherRow);
                            other.add(hand,7,otherRow);
                            other.add(memberGender, 9,otherRow);
                            other.add(new HiddenInput("extra_player_starting_time",""+i),1,otherRow);
                            CheckBox box = new CheckBox("extra_player",""+ (otherRow-2));
                            other.add(box,11,otherRow);

                        }
                        else {
                            errors = TournamentController.isMemberAllowedToRegister(member,tournament);

                            if ( (errors[0] == 0) && (errors[1] == 0) && (errors[2] == 0) && (errors[3] == 0) ){
                                if (!TournamentController.isMemberRegisteredInTournament(tournament, ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(iTournamentRoundId),tournament.getNumberInGroup(),member) ) {
                                    List tGroups = TournamentController.getTournamentGroups(member,tournament);
                                    if (tGroups != null) {
                                        ++tableRow;
                                        table.add(member.getName(),1,tableRow);
                                        table.add(new HiddenInput("member_id",""+member.getID()),1,tableRow);
                                        table.add(new HiddenInput("starting_time",""+i),1,tableRow);
                                        table.add(new HiddenInput("starting_tee","1"),1,tableRow);
                                        table.add(new DropdownMenu(tGroups),3,tableRow);
                                        if (member.getGender().equalsIgnoreCase("M")) {
                                            if (member.getHandicap() > tournament.getMaxHandicap() ) {
                                                table.add(tournament.getMaxHandicap()+" *("+member.getHandicap()+")",5,tableRow);
                                                star = true;
                                            }
                                            else {
                                                table.add(TextSoap.singleDecimalFormat(member.getHandicap())+"",5,tableRow);
                                            }
                                        }
                                        else {
                                            if (member.getHandicap() > tournament.getFemaleMaxHandicap() ) {
                                                table.add(tournament.getFemaleMaxHandicap()+" *("+member.getHandicap()+")",5,tableRow);
                                                star = true;
                                            }
                                            else {
                                                table.add(member.getHandicap()+"",5,tableRow);
                                            }
                                        }
                                        correction = new TextInput("handicap_correction_"+member.getID());
                                            correction.setSize(3);
                                        table.add(correction,7,tableRow);
                                    }
                                }
                                else {
                                        ++doneRow;
                                        done.setAlignment(1,doneRow,"left");
                                        done.add(member.getName(),1,doneRow);
                                }
                            }
                            else {
                                ++rejectsRow;
                                rejects.setAlignment(1,rejectsRow,"left");
                                rejects.add(member.getName(),1,rejectsRow);

                                if ( errors[0] == 1) {
                                        memberCorrect = getLocalizedLink("tournament.check_member","Check Member");
                                        memberCorrect.setWindowToOpen(MemberCorrectWindow.class); 
                                        memberCorrect.addParameter("member_id",member.getID());
                                    rejects.add(memberCorrect,2,rejectsRow);
                                    rejects.add(" ",2,rejectsRow);
                                }

                                if ( errors[1] == 1) {
                                        unionCorrect = getLocalizedLink("trounament.check_club_membership","Check Club Membership");
                                        unionCorrect.setWindowToOpen(UnionCorrectWindow.class);
                                        unionCorrect.addParameter("member_id",member.getID());
                                    rejects.add(unionCorrect,2,rejectsRow);
                                }

                                if ( errors[2] == 1) {
                                    rejects.add(iwrb.getLocalizedString("tournament.groups","Groups"),2,otherRow);
                                }

                                if ( errors[3] == 1) {
                                        Link tournamentFix = getLocalizedLink("tournament.check_tournament","Check Tournament");
                                          tournamentFix.setWindowToOpen(TournamentCreatorWindow.class);
                                          tournamentFix.addParameter("tournament",tournament.getID());
                                          tournamentFix.addParameter("tournament_control_mode","edit");

                                    rejects.add(tournamentFix,2,rejectsRow);
                                }

                            }
                        }
                    }
                }
            }
            numbers = (String[]) modinfo.getParameterValues("social_security_number_for_group_"+i+"_");
            if (numbers != null) {
                for (int j = 0; j < numbers.length; j++) {
                    if (!numbers[j].equals("")) {
                        member =  (Member) MemberBMPBean.getMember(numbers[j]);

                        if (member == null) {
                            ++otherRow;
                            other.add(numbers[j],1,otherRow);
                            other.add(new HiddenInput("extra_player_social_security_number",numbers[j]),1,otherRow);
                            other.add(new HiddenInput("extra_player_starting_tee","10"),1,otherRow);
                            derName = new TextInput("extra_player_name");
                            other.add(derName,3,otherRow);
                            TextInput hand = new TextInput("extra_player_handicap");
                              hand.setSize(3);
                            try {
                                Integer.parseInt(numbers[j].substring(0,6));
                                Integer.parseInt(numbers[j].substring(9,10));
                            }
                            catch (NumberFormatException n) {
                                derName.setContent(iwrb.getLocalizedString("tournament.ssn_is_wrong","Social security number is incorrect"));
                            }
                            if (numbers[j].length() < 10) {
                                derName.setContent(iwrb.getLocalizedString("tournament.ssn_is_wrong","Social security number is incorrect"));
                            }

                            other.add(allGroups,5,otherRow);
                            other.add(hand,7,otherRow);
                            other.add(memberGender, 9,otherRow);
                            other.add(new HiddenInput("extra_player_starting_time",""+i),1,otherRow);
                            CheckBox box = new CheckBox("extra_player",""+ (otherRow-2));
                            other.add(box,11,otherRow);

                        }
                        else {
                            errors = TournamentController.isMemberAllowedToRegister(member,tournament);

                            if ( (errors[0] == 0) && (errors[1] == 0) && (errors[2] == 0) && (errors[3] == 0) ){
                                if (!TournamentController.isMemberRegisteredInTournament(tournament, ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(iTournamentRoundId),tournament.getNumberInGroup(),member) ) {
                                    List tGroups = TournamentController.getTournamentGroups(member,tournament);
                                    if (tGroups != null) {
                                        ++tableRow;
                                        table.add(member.getName(),1,tableRow);
                                        table.add(new HiddenInput("member_id",""+member.getID()),1,tableRow);
                                        table.add(new HiddenInput("starting_time",""+i),1,tableRow);
                                        table.add(new HiddenInput("starting_tee","10"),1,tableRow);
                                        table.add(new DropdownMenu(tGroups),3,tableRow);
                                        if (member.getGender().equalsIgnoreCase("M")) {
                                            if (member.getHandicap() > tournament.getMaxHandicap() ) {
                                                table.add(tournament.getMaxHandicap()+" *("+member.getHandicap()+")",5,tableRow);
                                                star = true;
                                            }
                                            else {
                                                table.add(TextSoap.singleDecimalFormat(member.getHandicap())+"",5,tableRow);
                                            }
                                        }
                                        else {
                                            if (member.getHandicap() > tournament.getFemaleMaxHandicap() ) {
                                                table.add(tournament.getFemaleMaxHandicap()+" *("+member.getHandicap()+")",5,tableRow);
                                                star = true;
                                            }
                                            else {
                                                table.add(member.getHandicap()+"",5,tableRow);
                                            }
                                        }
                                        correction = new TextInput("handicap_correction_"+member.getID());
                                            correction.setSize(3);
                                        table.add(correction,7,tableRow);
                                    }
                                }
                                else {
                                        ++doneRow;
                                        done.setAlignment(1,doneRow,"left");
                                        done.add(member.getName(),1,doneRow);
                                }
                            }
                            else {
                                ++rejectsRow;
                                rejects.setAlignment(1,rejectsRow,"left");
                                rejects.add(member.getName(),1,rejectsRow);

                                if ( errors[0] == 1) {
                                        memberCorrect = getLocalizedLink("tournament.check_member","Check Member");
                                        memberCorrect.setWindowToOpen(MemberCorrectWindow.class); 
                                        memberCorrect.addParameter("member_id",member.getID());
                                    rejects.add(memberCorrect,2,rejectsRow);
                                    rejects.add(" ",2,rejectsRow);
                                }

                                if ( errors[1] == 1) {
                                        unionCorrect = getLocalizedLink("tournament.check_club_membership","Check Club Membership");
                                        unionCorrect.setWindowToOpen(UnionCorrectWindow.class);
                                        unionCorrect.addParameter("member_id",member.getID());
                                    rejects.add(unionCorrect,2,rejectsRow);
                                }

                                if ( errors[2] == 1) {
                                    rejects.add(iwrb.getLocalizedString("tournament.groups","Groups"),2,otherRow);
                                }

                                if ( errors[3] == 1) {
                                        Link tournamentFix = getLocalizedLink("tournament.check_tournament","Check Tournament");
                                          tournamentFix.setWindowToOpen(TournamentCreatorWindow.class);
                                          tournamentFix.addParameter("tournament",tournament.getID());
                                          tournamentFix.addParameter("tournament_control_mode","edit");

                                    rejects.add(tournamentFix,2,rejectsRow);
                                }

                            }
                        }
                    }
                }
            }
        }

        if (tableRow > 1) {
            noOne = false;
            form.add(table);
            content.add(form);
            content.addBreak();
        }

        if (otherRow > 1) {
            if (noOne) {
                content.add(form);
            }
            noOne = false;
            form.add(other);
            content.addBreak();
        }

        Table buttonTable = new Table(1,1);
            buttonTable.setAlignment(1,1,"right");
            buttonTable.setWidth("100%");
            buttonTable.add(TournamentController.getAheadButton(modinfo,"",""));
        form.add(buttonTable);


        if (doneRow > 1) {
            noOne = false;
            content.add(done);
            content.addBreak();
        }
        if (rejectsRow > 1) {
            noOne = false;
            content.add(rejects);
        }
        if (!noOne) {
            add(content);
        }


        if (star) {
            //content.add("* Kylfingur spilar á hámarskforgjöf móts<br>");
        }

        if (otherRow > 1) {
            Table ruleTable = new Table();
              ruleTable.setAlignment("center");
              ruleTable.setWidth("100%");
              ruleTable.setAlignment(1,1,"left");
              ruleTable.setAlignment(1,2,"left");
              ruleTable.setAlignment(1,3,"left");
              ruleTable.setAlignment(1,4,"left");
              ruleTable.setAlignment(1,5,"left");

              ruleTable.add("<u>"+iwrb.getLocalizedString("tournament.ssn_rule_header","Rules concerning social security number of foreign visitors")+"</u>",1,1);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_1","Social security number must be 10 letters"),1,2);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_2","First 6 : DD/MM/YY"),1,3);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_3","Next 3 are whatever you like"),1,4);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_4","Last is the second number in the year ( 1998 => 9 )"),1,5);
            content.add("<br>");
            content.add(ruleTable);
        }

        ++tableRow;

        table.setAlignment(3,tableRow,"right");

        Table flippTable = new Table();
          flippTable.setWidth("100%");
          flippTable.setAlignment(1,1,"left");
          flippTable.add(TournamentController.getBackLink(modinfo),1,1);
      content.add("<br>");
      content.add(flippTable);

    }
    catch (Exception e) {
        e.printStackTrace(System.err);
    }
    try {
        if (noOne) {
            getDirectRegistrationTable(modinfo, false, iwrb);
        }
    }
    catch (Exception ex) {
        ex.printStackTrace(System.err);
    }
}


public void getUnionMemberList(IWContext modinfo,IWResourceBundle iwrb) throws SQLException {
    try {
    	Member member =  ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(AccessControl.getMember(modinfo).getID());
    Union union = member.getMainUnion();

    List members = getMembersInUnion(union);

    this.drawTableWithMembers(modinfo,members,iwrb);
    }
    catch (FinderException fe) {
    	throw new SQLException(fe.getMessage());
    }
}

public List getMembersInUnion(Union union) throws SQLException{
    List members = union.getMembersInUnion();
    java.util.Collections.sort(members,new com.idega.util.GenericMemberComparator(com.idega.util.GenericMemberComparator.FIRSTLASTMIDDLE));
    return members;
}

public List getMembersInTournamentList(Tournament tournament) throws SQLException{
    List members = null;
    try {
        members = com.idega.data.EntityFinder.findReverseRelated(tournament,(Member) IDOLookup.instanciateEntity(Member.class));
        if (members != null) {
          java.util.Collections.sort(members,new com.idega.util.GenericMemberComparator(com.idega.util.GenericMemberComparator.FIRSTLASTMIDDLE));
        }
    }
    catch (Exception sql) {
          sql.printStackTrace(System.err);
    }
    return members;
}


public void searchByName(IWContext modinfo,IWResourceBundle iwrb) throws SQLException{
    Form form = new Form();
    Table table2 = new Table(2,3);
    table2.setWidth(200);
    table2.setAlignment("center");
    table2.setAlignment(2,3,"right");
    table2.mergeCells(1,1,2,1);
    table2.mergeCells(1,2,2,2);
    table2.add(iwrb.getLocalizedString("tournament.enter_names","Enter names")+":",1,1);


    TextArea numberInput = new TextArea("name");
        numberInput.setWidth(30);
        numberInput.setHeight(5);
    HiddenInput hidden = new HiddenInput("action","getSearchByNameResults");
    table2.add(numberInput,1,2);
    table2.add(hidden,1,2);
    if (AccessControl.isClubAdmin(modinfo)) {
        int union_id = ((Member )(AccessControl.getMember(modinfo))).getMainUnionID();
        CheckBox checkBox = new CheckBox("search_for_member_in_union_id",""+union_id);
        table2.add(iwrb.getLocalizedString("tournament.search_in_club","Search in club only")+" ",1,3);
        table2.add(checkBox,1,3);
    }
    GenericButton leitaButton = getButton(new SubmitButton(localize("tournament.search","Search")));
    table2.add(leitaButton,2,3);


    form.add(table2);
    add(form);

}

public void getSearchByNameResults(IWContext modinfo,IWResourceBundle iwrb) throws SQLException {
    String names = modinfo.getParameter("name");
    Member[] theMembers = this.findMembersByName(modinfo, names);
    drawTableWithMembers(modinfo, theMembers, iwrb);
}
public void getSearchBySocialSecurityNumberResults(IWContext modinfo,IWResourceBundle iwrb) throws SQLException {
    String socialSecurityNumbers = modinfo.getParameter("socialSecurityNumbers");
    Member[] theMembers = this.findMembersBySocialSecurityNumber(modinfo,socialSecurityNumbers);
    List guests = this.findGuestsBySocialSecurityNumber(modinfo,socialSecurityNumbers);

    drawTableWithMembers(modinfo, theMembers, guests, iwrb);
}


public void searchBySocialSecurityNumber(IWContext modinfo,IWResourceBundle iwrb) throws SQLException{
    Form form = new Form();
    Table table2 = new Table(2,3);
    table2.setWidth(200);
    table2.setAlignment("center");
    table2.setAlignment(2,3,"right");
    table2.add(iwrb.getLocalizedString("tournament.enter_social_security_number","Enter social security number")+":",1,1);
    table2.mergeCells(1,1,2,1);
    table2.mergeCells(1,2,2,2);

    TextArea numberInput = new TextArea("socialSecurityNumbers");
        numberInput.setWidth(30);
        numberInput.setHeight(5);
    HiddenInput hidden = new HiddenInput("action","getSearchBySocialSecurityNumberResults");

    table2.add(numberInput,1,2);
    table2.add(hidden,1,3);
    GenericButton leitaButton = getButton(new SubmitButton(localize("tournament.search","Search")));
    table2.add(leitaButton,2,3);

    if (AccessControl.isClubAdmin(modinfo)) {
        int union_id = ((Member )(AccessControl.getMember(modinfo))).getMainUnionID();
        CheckBox checkBox = new CheckBox("search_for_member_in_union_id",""+union_id);
        table2.add(iwrb.getLocalizedString("tournament.search_in_club","Search in club only")+" ",1,3);
        table2.add(checkBox,1,3);
    }

    form.add(table2);
    add(form);

}

public void drawTableWithMembers(IWContext modinfo, List theMembers, IWResourceBundle iwrb) {
    is.idega.idegaweb.golf.entity.Member[] theMembersArray = new is.idega.idegaweb.golf.entity.Member[theMembers.size()];

        for (int i = 0; i < theMembersArray.length; i++) {
            theMembersArray[i] = (is.idega.idegaweb.golf.entity.Member) theMembers.get(i);

        }


    drawTableWithMembers(modinfo,theMembersArray, iwrb);
}

public void drawTableWithMembers(IWContext modinfo, Member[] theMembers, IWResourceBundle iwrb) {
    drawTableWithMembers(modinfo, theMembers, null, iwrb);
}
public void drawTableWithMembers(IWContext modinfo, Member[] theMembers, List guests,IWResourceBundle iwrb) {
    int tableHeight = 5;
    int numberOfMember = 0;

    if (theMembers != null) {
      numberOfMember = theMembers.length;
      tableHeight += theMembers.length;
    }

    Form form = new Form();
    Table table = new Table();
        table.setBorder(0);
        table.setCellpadding(2);
        table.setCellspacing(0);
        table.setWidth("90%");
        table.setAlignment("center");
        table.setAlignment(1,tableHeight,"center");
        table.mergeCells(1,1,8,1);
        table.add(iwrb.getLocalizedString("tournament.search_results","Search results"),1,1);
    form.add(table);

    Table bottom = new Table(2,1);
        bottom.setWidth("90%");
        bottom.setAlignment(1,1,"left");
        bottom.setAlignment(2,1,"right");

    boolean ahead = false;
    boolean back = false;


    Member member = null;
    Union union = null;
    Member[] members = null;
    CheckBox checker = null;
    boolean error = false;
    boolean notFound = false;
    boolean doSearch = true;
    String memberSocialNumber;
    String memberName;
    Link link = null;
    int memberId;
    int row = 1;

    ++row;
    table.add("<u>"+iwrb.getLocalizedString("tournament.social_security_number","Social security number")+"</u>",1,row);
    table.add("<u>"+iwrb.getLocalizedString("tournament.name","Name")+"</u>",3,row);
    table.add("<u>"+iwrb.getLocalizedString("tournament.club","Club")+"</u>",5,row);

    if (theMembers != null) {
      if (theMembers.length > 0) {
        for (int i = 0; i < theMembers.length; i++) {
            ++row;
            memberSocialNumber = theMembers[i].getSocialSecurityNumber();
            memberName = theMembers[i].getName();
            memberId = theMembers[i].getID();

            checker = new CheckBox("checkedMemberId_"+memberId);

            link = new Link(iwrb.getLocalizedString("tournament.register","Register in tournament"),modinfo.getRequestURI());
              link.addParameter("action","registermarkedmembers");
              link.addParameter("checker","true");
              link.addParameter("member_id",memberId+"");
            table.add(memberSocialNumber,1,row);
            table.add(memberName,3,row);

            try {
                union = theMembers[i].getMainUnion();
                table.add(union.getAbbrevation(),5,row);
            }
            catch (Exception e) {
                e.printStackTrace(System.err);
                table.add("-",5,row);
            }

            table.add(link,7,row);
            table.add(checker,8,row);

            table.add(new HiddenInput("member_id",memberId+""),2,row);
            table.setAlignment(8,row,"right");
        }

        ++row;
        ++row;
        ahead = true;
        back = true;
      }
      else {
        ++row;
        back = true;
      }
    }
    else {
        ++row;
        back = true;
    }
//    table.resize(8,row);

    boolean rules = false;
    if (guests != null) {
        if (guests.size() > 0) {

        for (int i = 0; i < guests.size(); i++) {
            ahead = true;
            rules = true;

            ++row;
            memberSocialNumber = (String) guests.get(i);
            memberName = "gestur ?";
            memberId = -10;

            checker = new CheckBox("checkedMemberId_"+memberSocialNumber);

            link = new Link(iwrb.getLocalizedString("tournament.register","Register in tournament"),modinfo.getRequestURI());
              link.addParameter("action","registermarkedmembers");
              link.addParameter("checker","true");
              link.addParameter("member_id",memberId+"");
            table.add(memberSocialNumber,1,row);
            table.add(memberName,3,row);

            table.add("-",5,row);

            table.add(link,7,row);
            table.add(checker,8,row);

            table.add(new HiddenInput("member_id",memberId+""),2,row);
            table.add(new HiddenInput("ssn",memberSocialNumber),2,row);
            table.setAlignment(8,row,"right");
        }
        }
    }


    if (ahead) {
        GenericButton skraButton = getButton(new SubmitButton(localize("tournament.continue","Continue")));
        bottom.add(skraButton,2,1);
        bottom.add(new HiddenInput("action","registermarkedmembers"));
    }
    else {
        table.add(iwrb.getLocalizedString("tournament.no_one_was_found","No one was found"),1,row);
    }
    table.setColumnAlignment(1,"left");
    table.setColumnAlignment(3,"left");
    table.setColumnAlignment(5,"left");
    table.setColumnAlignment(7,"left");
     bottom.add(TournamentController.getBackLink(modinfo),1,1);


    form.addBreak();
    form.add(bottom);
    add(form);
}



public void registerMarkedMembers(IWContext modinfo,IWResourceBundle iwrb) throws SQLException{
    Tournament tournament = getTournament(modinfo);
    String[] extraPlayers = modinfo.getParameterValues("extra_player");
    String[] member_id = modinfo.getParameterValues("member_id");
    String[] tournament_group_id = modinfo.getParameterValues("tournament_group");
    String handicap_correction = "";

    int isError = 0;
    int row = 1;
    int rowOne = 2;
    int rowTwo = 2;
    int rowThree = 2;
    Member member = null;
    Table table = new Table();
        table.setBorder(1);
        table.setCellspacing(0);
        table.setCellpadding(2);
        table.setWidth("80%");
        table.setWidth(1,"33%");
        table.setWidth(2,"33%");
        table.setWidth(3,"33%");
        table.setAlignment(1,row,"center");
        table.setAlignment(2,row,"center");
        table.setAlignment(3,row,"center");
        table.add(iwrb.getLocalizedString("tournament.registered","Registered"),1,row);
        table.add(iwrb.getLocalizedString("tournament.did_not_register","Did not register"),2,row);
        table.add(iwrb.getLocalizedString("tournament.already_registered_si","Already registered"),3,row);

        add(table);


    if (member_id != null) {
        try {
          if (isSaveEnabled(modinfo)) {
            for (int i = 0; i < member_id.length; i++) {
                member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id[i]));


                    handicap_correction = modinfo.getParameter("corrected_handicap_"+member.getID());
                    if (!handicap_correction.equals("")) {
                        correctHandicap(modinfo,member , handicap_correction);
                    }
                    isError = TournamentController.registerMember(member,tournament,tournament_group_id[i]);

                    disableSave(modinfo);



                switch (isError) {
                  case 0:
                      rowOne++;
                      row = rowOne;
                    break;
                  case 1:
                      rowTwo++;
                      row = rowTwo;
                    break;
                  case 2:
                      rowThree++;
                      row = rowThree;
                    break;
                }
                table.add(member.getName(),(isError+1),row);


            }
          }
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }

        Form form = new Form();
          form.add(new HiddenInput("action","selectmember"));
          form.add(new SubmitButton(localize("tournament.back","Back")));
        add(form);


    }
    if (extraPlayers != null) {
            String[] XPSocial = modinfo.getParameterValues("extra_player_social_security_number");
            String[] XPStartingtime = modinfo.getParameterValues("extra_player_starting_time");
            String[] XPName = modinfo.getParameterValues("extra_player_name");
            String[] XPGroups = modinfo.getParameterValues("extra_player_groups");
            String[] XPGender = modinfo.getParameterValues("extra_player_gender");
            String[] XPHandicap = modinfo.getParameterValues("extra_player_handicap");

            IWTimestamp dateStamp;
            int year;

            int XP = 0;

            javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
            for (int i = 0; i < extraPlayers.length; i++) {
                try {
                    tm.begin();
                    XP = Integer.parseInt(extraPlayers[i]);

                     dateStamp = new IWTimestamp();
                      year = Integer.parseInt(XPSocial[XP].substring(4,6));
                      if ( XPSocial[XP].substring(9,10).equals("9")) {
                        year = 1900 + year;
                      }else if ( XPSocial[XP].substring(9,10).equals("0")) {
                        year = 2000 + year;
                      }else if ( XPSocial[XP].substring(9,10).equals("8")) {
                        year = 1800 + year;
                      }else if ( XPSocial[XP].substring(9,10).equals("1")) {
                        year = 2100 + year;
                      }

                      dateStamp.setYear(year);
                      dateStamp.setMonth(Integer.parseInt(XPSocial[XP].substring(2,4)));
                      dateStamp.setDay(Integer.parseInt(XPSocial[XP].substring(0,2)));

                    Member XPmember = (Member) IDOLookup.createLegacy(Member.class);
                        XPmember.setSocialSecurityNumber(XPSocial[XP]);
                        XPmember.setFirstName(XPName[XP]);
                        XPmember.setDateOfBirth(dateStamp.getSQLDate());
                        XPmember.setGender(XPGender[XP]);
                        XPmember.insert();

                    MemberInfo memberInfo = (MemberInfo) IDOLookup.createLegacy(MemberInfo.class);
                        memberInfo.setMemberId(XPmember.getID());
                        memberInfo.setHandicap(Float.parseFloat(XPHandicap[XP]));
                        memberInfo.setFirstHandicap(Float.parseFloat(XPHandicap[XP]));
                        memberInfo.insert();

                    UnionMemberInfo uMInfo = (UnionMemberInfo) IDOLookup.createLegacy(UnionMemberInfo.class);
                        uMInfo.setUnionID(1);
                        uMInfo.setMemberID(XPmember.getID() );
                        uMInfo.setMembershipType("main");
                        uMInfo.setMemberStatus("A");
                        uMInfo.insert();

                    isError = TournamentController.registerMember(XPmember,tournament,XPGroups[XP]);


                    switch (isError) {
                      case 0:
                          rowOne++;
                          row = rowOne;
                        break;
                      case 1:
                          rowTwo++;
                          row = rowTwo;
                        break;
                      case 2:
                          rowThree++;
                          row = rowThree;
                        break;
                    }
                    table.add(XPmember.getName(),(isError+1),row);
                    tm.commit();
                }
                catch (Exception e) {
                    try {
                        tm.rollback();
                    }
                    catch (javax.transaction.SystemException se) {
                        se.printStackTrace(System.err);
                    }
                    rowThree++;
                    row = rowThree;
                    isError = 2;
                    table.add(XPSocial[XP],(isError+1),row);
                    e.printStackTrace(System.err);
                }

            }
    }

    if ((member == null)&&(extraPlayers == null)){
        add(iwrb.getLocalizedString("tournament.no_one_was_chosen","No on was chosen")+"<br><br>");
        add(TournamentController.getBackLink(modinfo));

    }

}

public void correctHandicap(IWContext modinfo,Member member ,String handicapString) {

    try {
          float handicap = 100;

          if ( handicapString != null && handicapString.length() > 0 ) {
            if ( handicapString.indexOf(",") != -1 ) {
              handicapString = handicapString.replace(',','.');
            }
            handicap = Float.parseFloat(handicapString);
          }

          MemberInfo[] infos = (MemberInfo[]) ((MemberInfo) IDOLookup.instanciateEntity(MemberInfo.class)).findAllByColumn("member_id",member.getID()+"");
          MemberInfo memberInfo;
          if (infos.length > 0) {
              try {
              	memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(member.getID());
              }
              catch (FinderException fe) {
              	throw new SQLException(fe.getMessage());
              }
              memberInfo.setHandicap(handicap);
              memberInfo.update();
          }
          else {
              memberInfo = (MemberInfo) IDOLookup.createLegacy(MemberInfo.class);
              memberInfo.setMemberId(member.getID());
              memberInfo.setHandicap(handicap);
              memberInfo.setFirstHandicap(handicap);
              memberInfo.insert();
          }

          Tournament tournament = getTournament(modinfo);
          IWTimestamp stampur = new IWTimestamp(tournament.getStartTime());
            stampur.addDays(-1);


          Scorecard scoreCard = (Scorecard) IDOLookup.createLegacy(Scorecard.class);
            scoreCard.setMemberId(member.getID());
            scoreCard.setTournamentRoundId(1);
            scoreCard.setScorecardDate(stampur.getTimestamp());
            scoreCard.setTotalPoints(0);
            scoreCard.setHandicapBefore(memberInfo.getHandicap());
            scoreCard.setHandicapAfter(handicap);
            scoreCard.setSlope(0);
            scoreCard.setCourseRating(0);
            scoreCard.setTeeColorID(0);
            scoreCard.setFieldID(0);
            scoreCard.setHandicapCorrection("Y");
            scoreCard.insert();


    }
    catch (SQLException sq ) {
        sq.printStackTrace(System.err);
    }
}

public void checkMarkedMembers(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
    enableSave(modinfo);

    Tournament tournament = getTournament(modinfo);

    String[] member_id = (String[]) modinfo.getParameterValues("member_id");
    String checker;
    Member member;

    int numberOK = 0;
    int otherGOK = 0;

    TournamentGroup[] groups = tournament.getTournamentGroups();
    if (groups.length > 0) {

        int row = 1;
        Form form = new Form();
        Table table = new Table();
            table.setBorder(0);
            table.setWidth("90%");
            table.add("<u>"+iwrb.getLocalizedString("tournament.social_security_number","Social security number")+"</u>",1,row);
            table.add("<u>"+iwrb.getLocalizedString("tournament.name","Name")+"</u>",3,row);
            table.add("<u>"+iwrb.getLocalizedString("tournament.group","Group")+"</u>",5,row);
            table.add("<u>"+iwrb.getLocalizedString("tournament.handicap","Handicap")+"</u>",7,row);
            table.add("<u>"+iwrb.getLocalizedString("tournament.correction","Correction")+"</u>",9,row);
        add(form);

        TextInput correction;
        int[] errors = new int[4];

        Link unionCorrect = null;
        Link memberCorrect = null;

        Table otherG = new Table();

        Table bottom = new Table(2,1);
            bottom.setWidth("90%");
            bottom.setAlignment(1,1,"left");
            bottom.setAlignment(2,1,"right");

        Table already = new Table();
          already.setWidth("90%");
          already.add("<u>"+iwrb.getLocalizedString("tournament.already_registered","Already registered")+":</u>",1,1);
          int alreadyRow = 1;

        Table other = new Table();
          other.setWidth("90%");
          other.add("<u>"+iwrb.getLocalizedString("tournament.can_not_register_pl","Can not register")+":</u>",1,1);
          int otherRow = 1;

        Text tooMany = new Text("*");
          tooMany.setFontColor("red");
        DropdownMenu tGroups;
        List tGroupsList;

        for (int i = 0; i < member_id.length; i++) {
            checker = modinfo.getParameter("checkedMemberId_"+member_id[i]);
            if (checker == null) {
                if (modinfo.getParameter("checker") != null) {
                    checker = "";
                }
            }
            if ((checker != null)&& (!member_id[i].equals("-10"))){
              try {
              	member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id[i]));
              }
              catch (FinderException fe) {
              	throw new SQLException(fe.getMessage());
              }
              if (!TournamentController.isMemberRegisteredInTournament(tournament, member) ) {
                  errors = TournamentController.isMemberAllowedToRegister(member,tournament);

                  if ( (errors[0] == 0) && (errors[1] == 0) && (errors[2] == 0) && (errors[3] == 0) ){
//                  if (TournamentController.isMemberAllowedToRegister(member,tournament) == 0) {
                        ++row;
                        table.add(member.getSocialSecurityNumber(),1,row);
                        table.add(member.getName(),3,row);
                        table.add(new HiddenInput("member_id",member.getID()+""));
                        tGroupsList = TournamentController.getTournamentGroups(member,tournament);
                        tGroups = new DropdownMenu(tGroupsList);
                        table.add(tGroups,5,row);
                        if (tGroupsList.size() > 1) {
                          table.add(tooMany,5,row);
                        }
                        table.add(member.getHandicap()+"",7,row);
                        correction = new TextInput("corrected_handicap_"+member.getID());
                          correction.setSize(3);
                        table.add(correction,9,row);

                        table.setVerticalAlignment(1,row,"top");
                        table.setVerticalAlignment(3,row,"top");
                        table.setVerticalAlignment(5,row,"top");
                        table.setVerticalAlignment(7,row,"top");
                        table.setVerticalAlignment(9,row,"top");

                        ++numberOK;
                  }
                  else {
                        ++otherRow;
                        other.setAlignment(1,otherRow,"left");
                        other.add(member.getName(),1,otherRow);

                        if ( errors[0] == 1) {
                                memberCorrect = getLocalizedLink("tournament.check_member","Check Member");
                                memberCorrect.setWindowToOpen(MemberCorrectWindow.class);
                                memberCorrect.addParameter("member_id",member.getID());
                            other.add(memberCorrect,2,otherRow);
                            other.add(" ",2,otherRow);
                        }

                        if ( errors[1] == 1) {
                                unionCorrect = getLocalizedLink("tournament.check_club_membership","Check Club Membership");
                                	unionCorrect.setWindowToOpen(UnionCorrectWindow.class);
                                unionCorrect.addParameter("member_id",member.getID());
                            other.add(unionCorrect,2,otherRow);
                        }

                        if ( errors[2] == 1) {
                            other.add(iwrb.getLocalizedString("tournament.groups","Groups"),2,otherRow);
                        }

                        if ( errors[3] == 1) {
                                Link tournamentFix = getLocalizedLink("tournament.check_tournament","Check Tournament");
                                  tournamentFix.setWindowToOpen(TournamentCreatorWindow.class);
                                  tournamentFix.addParameter("tournament",tournament.getID());
                                  tournamentFix.addParameter("tournament_control_mode","edit");

                            other.add(tournamentFix,2,otherRow);
                        }

                      //++otherRow;
                      //other.add(member.getName(),1,otherRow);
                  }
              }
              else {
                  ++alreadyRow;
                  already.add(member.getName(),1,alreadyRow);
              }
            }
        }

        boolean rules = false;

        String[] ssns = modinfo.getParameterValues("ssn");
        if (ssns != null) {
            if (ssns.length > 0) {
                otherG.setBorder(0);
                int otherGRow = 1;
                otherG.setWidth("90%");
                otherG.add("<u>"+iwrb.getLocalizedString("tournament.social_security_number","Social security number")+"</u>",1,otherGRow);
                otherG.add("<u>"+iwrb.getLocalizedString("tournament.name","Name")+"</u>",3,otherGRow);
                otherG.add("<u>"+iwrb.getLocalizedString("tournament.group","Group")+"</u>",5,otherGRow);
                otherG.add("<u>"+iwrb.getLocalizedString("tournament.handicap","Handicap")+"</u>",7,otherGRow);
                otherG.add("<u>"+iwrb.getLocalizedString("tournament.sex","Sex")+"</u>",9,otherGRow);
                otherG.add("<u>"+iwrb.getLocalizedString("tournament.register_sm","Register")+"</u>",11,otherGRow);

            String ssn;
            TextInput derName;
            TournamentGroup[] allGroupsInTournament = tournament.getTournamentGroups();
            DropdownMenu memberGender = new DropdownMenu("extra_player_gender");
                memberGender.addMenuElement("M",iwrb.getLocalizedString("tournament.male","Male"));
                memberGender.addMenuElement("F",iwrb.getLocalizedString("tournament.female","Female"));
            DropdownMenu allGroups = new DropdownMenu(allGroupsInTournament,"extra_player_groups");

            for (int i = 0; i < ssns.length; i++) {
                if ( modinfo.getParameter("checkedMemberId_"+ssns[i]) != null ) {

                        ++otherGOK;
                        ++otherGRow;
                        ssn = ssns[i];
                        otherG.add(ssn,1,otherGRow);
                        otherG.add(new HiddenInput("extra_player_social_security_number",ssn),1,otherGRow);
                        derName = new TextInput("extra_player_name");
                        otherG.add(derName,3,otherGRow);
                        TextInput hand = new TextInput("extra_player_handicap");
                          hand.setSize(3);
                        if (ssn.length() == 10) {
                            try {
                                Integer.parseInt(ssn.substring(0,6));
                                Integer.parseInt(ssn.substring(9,10));
                            }
                            catch (NumberFormatException n) {
                                derName.setContent(iwrb.getLocalizedString("tournament.ssn_is_wrong","Social security number is incorrect"));
                                rules = true;
                            }
                        }
                        if (ssn.length() != 10) {
                            derName.setContent(iwrb.getLocalizedString("tournament.ssn_is_wrong","Social security number is incorrect"));
                            rules = true;
                        }

                        otherG.add(allGroups,5,otherGRow);
                        otherG.add(hand,7,otherGRow);
                        otherG.add(memberGender, 9,otherGRow);
                        otherG.add(new HiddenInput("extra_player_starting_time",""+i),1,otherGRow);
                        CheckBox box = new CheckBox("extra_player",""+ (otherGRow-2));
                        otherG.add(box,11,otherGRow);
                    }
                }

                otherG.setColumnAlignment(1,"left");
                otherG.setColumnAlignment(3,"left");
                otherG.setColumnAlignment(5,"left");
                otherG.setColumnAlignment(7,"left");
                otherG.setColumnAlignment(9,"left");
                otherG.setColumnAlignment(11,"left");
            }
        }

        table.setColumnAlignment(1,"left");
        table.setColumnAlignment(3,"left");
        table.setColumnAlignment(5,"left");
        table.setColumnAlignment(7,"left");
        table.setColumnAlignment(9,"left");


        ++row;
        table.setAlignment(5,row,"right");


        if (row > 2) {
            form.add(table);
        }

        if (otherGOK > 0) {
            form.addBreak();
            form.add(otherG);
        }

        if (alreadyRow > 1) {
            form.addBreak();
            form.add(already);
            already.setColumnAlignment(1,"left");
        }
        if (otherRow > 1) {
            form.addBreak();
            form.add(other);
            other.setColumnAlignment(1,"left");
        }

        if (rules) {
            Table ruleTable = new Table();
              ruleTable.setAlignment("center");
              ruleTable.setWidth("90%");
              ruleTable.setAlignment(1,1,"left");
              ruleTable.setAlignment(1,2,"left");
              ruleTable.setAlignment(1,3,"left");
              ruleTable.setAlignment(1,4,"left");
              ruleTable.setAlignment(1,5,"left");

              ruleTable.add("<u>"+iwrb.getLocalizedString("tournament.ssn_rule_header","Rules concerning social security number of foreign visitors")+"</u>",1,1);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_1","Social security number must be 10 letters"),1,2);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_2","First 6 : DD/MM/YY"),1,3);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_3","Next 3 are whatever you like"),1,4);
              ruleTable.add(iwrb.getLocalizedString("tournament.ssn_rule_4","Last is the second number in the year ( 1998 => 9 )"),1,5);
            form.addBreak();
            form.add(ruleTable);
        }

        if ((numberOK > 0) || (otherGOK>0)) {
            bottom.add(getButton(new SubmitButton(localize("tournament.continue","Continue"))),2,1);
            bottom.add(new HiddenInput("action","confirmRegisterMarkedMembers"),2,1);
        }
        bottom.add(TournamentController.getBackLink(modinfo),1,1);
        form.addBreak();
        form.add(bottom);

    }
    else {
        add(iwrb.getLocalizedString("tournament.no_tournament_groups_in_tournament","There are no tournament groups set up in the tournament"));
        //add("<br>Hægt að bæta þeim við í \"Breyta móti\".<br>");
        add(TournamentController.getBackLink(modinfo));
    }

}


public void unregisterMember(Member member, Tournament theTournament) throws SQLException{
        // temp lausn....
            try {
                member.removeFrom(theTournament);
                add("<br>Meðlimur : \""+member.getName()+"\" hefur verið skráð/ur úr mótinu \"");
                add(theTournament.getName()+"\"");
            }
            catch (SQLException s) {
                try {
                    Tournament[] tour = (Tournament[]) member.findRelated(theTournament);
                    if (tour.length == 0) {
                        add("<br>Meðlimur : \""+member.getName()+"\" er ekki skráð/ur í mótið");
                    }
                }
                catch (SQLException sq) {
                    sq.printStackTrace(System.err);
                    add("<br>Meðlimur : \""+member.getName()+"\" skráðist ekki úr mótinu");
                }
            }
        //...temp lausn endar hér
}


public int getAge(Member member) {
    int currentYear = IWTimestamp.RightNow().getYear();
    int memberYear = 0;

    java.sql.Date date = member.getDateOfBirth();
    if (date != null) {
        IWTimestamp stamp = new IWTimestamp(date);
        memberYear = stamp.getYear();
    }
    else {
        String socialSecurityNumber = member.getSocialSecurityNumber();
        if ( socialSecurityNumber != null) {
          if (socialSecurityNumber.length() >= 6) {
              try {
                  memberYear = Integer.parseInt(socialSecurityNumber.substring(4,6));
              }
              catch (NumberFormatException n) {
              }
          }
        }
    }

    return currentYear - memberYear;
}




public Member[] findMembersBySocialSecurityNumber(IWContext modinfo, String socialSecurityNumbers) throws SQLException{
    StringTokenizer token = new StringTokenizer(socialSecurityNumbers," \n\r\t\f,;:.+");
    Vector vector = new Vector();
    while (token.hasMoreTokens()) {
        vector.addElement(token.nextToken());
    }
    return findMembersBySocialSecurityNumber(modinfo,vector);
}

public List findGuestsBySocialSecurityNumber(IWContext modinfo, String socialSecurityNumbers) throws SQLException{
    StringTokenizer token = new StringTokenizer(socialSecurityNumbers," \n\r\t\f,;:.+");
    Vector vector = new Vector();
    while (token.hasMoreTokens()) {
        vector.addElement(token.nextToken());
    }


    return findGuestsBySocialSecurityNumber(modinfo,vector);
}


public List findGuestsBySocialSecurityNumber(IWContext modinfo, Vector ssn) {
    Vector returner = new Vector();
    if (ssn != null) {
        String[] flipp = null;
        for (int i = 0; i < ssn.size(); i++) {
            try {
                flipp = SimpleQuerier.executeStringQuery("Select member_id from member where social_security_number = '"+ssn.get(i)+"'");
                if (flipp.length == 0 ) {
                    returner.add(ssn.get(i));
                }
            }catch (Exception e){
            }
        }
    }
    return returner;
}

public Member[] findMembersByName(IWContext modinfo, String socialSecurityNumbers) throws SQLException{
    StringTokenizer token = new StringTokenizer(socialSecurityNumbers,"\n\r\t\f,;:.+");
    Vector vector = new Vector();
    while (token.hasMoreTokens()) {
        vector.addElement(token.nextToken());
    }
    return findMembersByName(modinfo, vector);
}



public Member[] findMembersByName(IWContext modinfo, Vector name) throws SQLException {
    Member[] members = null;
    Member[] tempMembers = null;
    StringTokenizer  nameParts;
    String fullName = "";
    String firstName = "";
    String middleName = "";
    String lastName = "";
    int manyNames = 0;
    int numberInserted = 0;

    String union_id = modinfo.getParameter("search_for_member_in_union_id");

    String SQLString = "Select * from member where ";
    String tempSQLString = "";


    for (int i = 0; i < name.size(); i++) {
        try {
            manyNames = 0;
            fullName = "";
            firstName = "";
            middleName = "";
            lastName = "";
            tempSQLString = "";


            fullName = (String) name.elementAt(i);
            nameParts = new StringTokenizer(fullName," ");
            manyNames = nameParts.countTokens();
            for (int j = 0; j < manyNames; j++) {
                if (j == 0) {
                    firstName = nameParts.nextToken();
                }
                else if (j == manyNames -1) {
                    lastName = nameParts.nextToken();
                }
                else {
                    middleName += nameParts.nextToken();
                    if (j != manyNames -2) {
                        middleName += " ";
                    }
                }
            }

            switch (manyNames) {
                case 0: break;
                case 1:
                        tempSQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%"+firstName+"%'";
                        break;
                case 2:
                        tempSQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%"+firstName+"%' and (last_name like '%"+lastName+"%' OR middle_name like '%"+lastName+"%')";
                        break;
                default:
                        tempSQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND first_name like '%"+firstName+"%' and middle_name like '%"+middleName+"%' and last_name like '%"+lastName+"%'";
                        break;
            }

            if (!tempSQLString.equalsIgnoreCase("")) {
                if (union_id != null) {
                    tempSQLString += " AND union_member_info.union_id = "+union_id+" AND union_member_info.MEMBERSHIP_TYPE = 'A'";
                }
                tempMembers = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(tempSQLString);
                for (int g = 0; g < tempMembers.length; g++) {
                    if (numberInserted != 0) {
                        SQLString += " OR ";
                    }
                    ++ numberInserted;
                    SQLString += " member_id = "+tempMembers[g].getID();
                    //System.out.println("Meðlimur númer "+tempMembers[g].getID()+" fundinn : "+idegaTimestamp.RightNow().toSQLTimeString());
                }
            }
        }
        catch (Exception e) {

        }
    }
    if (union_id != null) {


    }

    if (numberInserted != 0) {
        SQLString += " order by first_name, last_name, middle_name";
        members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(SQLString);
        System.out.println("Buinn ad smida array : "+IWTimestamp.RightNow().toSQLTimeString() );
    }

    return members;

}

public Member[] findMembersBySocialSecurityNumber(IWContext modinfo,Vector socialSecurityNumbers) throws SQLException {

    Member[] members = null;
    String securityNumber;


/*
    String union_id = modinfo.getParameter("search_for_member_in_union_id");
    String SQLString = "Select * from member, union_member_info where member.member_id = union_member_info.member_id AND union_member_info.MEMBERSHIP_TYPE = 'main' AND (";

    int numberInserted = 0;

    for (int i = 0; i < socialSecurityNumbers.size() ; i++) {
        try {
            securityNumber = (String) socialSecurityNumbers.elementAt(i);
            if (securityNumber.equals("")) {
                securityNumber = "idega_engin_kennitala";
            }

            members = (Member[]) (new Member()).findAll("Select * from member where social_security_number like '%"+securityNumber+"%' ");
            if (members.length > 0) {
              for (int j = 0; j < members.length; j++) {
                  if (numberInserted != 0) {
                      SQLString += " OR ";
                  }
                  else {++numberInserted;}

                  SQLString += "member_id = "+members[j].getID();
              }
            }
        }
        catch (SQLException s){
            s.printStackTrace(System.err);
        }
    }
    SQLString += ")";
    if (union_id != null) {
        SQLString += " AND union_member_info.union_id = "+union_id+" AND union_member_info.MEMBER_STATUS = 'A'";
    }

//    SQLString += "order by first_name,last_name,middle_name";
    SQLString += "order by social_security_number";
*/

    String SQLString = "SELEct * from member where ";
    int numberInserted = 0;
    for (int i = 0; i < socialSecurityNumbers.size() ; i++) {
            securityNumber = (String) socialSecurityNumbers.elementAt(i);
            if (securityNumber.equals("")) {
                securityNumber = "idega_engin_kennitala";
            }
            members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll("Select * from member where social_security_number like '%"+securityNumber+"%' ");
            if (members.length > 0) {
              for (int j = 0; j < members.length; j++) {
                  if (numberInserted != 0) {
                      SQLString += " OR ";
                  }
                  else {++numberInserted;}

                  SQLString += "member_id = "+members[j].getID();
              }
            }
    }
    SQLString += " order by social_security_number";

    try {
      members = (Member[]) ((Member) IDOLookup.instanciateEntity(Member.class)).findAll(SQLString);
    }
    catch (SQLException s){
    }
    return members;

}

    public void enableSave(IWContext modinfo) {
        modinfo.setSessionAttribute("isSaveEnabled",new Boolean(true));
    }
    public void disableSave(IWContext modinfo) {
        modinfo.setSessionAttribute("isSaveEnabled",new Boolean(false));
    }

    public boolean isSaveEnabled(IWContext modinfo) {
        Boolean isSaveEnabled = (Boolean) modinfo.getSessionAttribute("isSaveEnabled");
        boolean returner = false;
        try {
            if (isSaveEnabled != null) {
                returner = isSaveEnabled.booleanValue();
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return returner;
    }

}
