package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberBMPBean;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.sql.SQLException;
import java.util.List;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class RegistrationForMembers extends GolfBlock {



  CloseButton closeButton = new CloseButton(new Image("/pics/formtakks/loka.gif","Loka glugga"));

  public void main(IWContext modinfo) throws Exception {
  	IWResourceBundle iwrb = getResourceBundle();
      String tournament_id = modinfo.getParameter("tournament_id");

      if (tournament_id != null)  {
          setTournament(modinfo, ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id)));
      }

      Tournament tournament = getTournament(modinfo);
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);


      if (member != null) {
          if (tournament != null) {

              if (!TournamentController.isMemberRegisteredInTournament(tournament,member) ) {

                  String action = modinfo.getParameter("action");

                  if (action == null) {
                      add("Villa kom upp<br>");
                      add(new CloseButton());
                  }else if (action.equalsIgnoreCase("open")) {
                      register(modinfo, iwrb);
                  }else if (action.equals("directRegistrationMembersChosen")) {
                      finalizeDirectRegistration(modinfo,iwrb);
                  }
              }
              else {
                  add("<center>");
                  add(member.getName() + " er skrá›ur i móti›  \""+tournament.getName()+"\" ");
                  add("</center>");
              }
          }else {
              add("Ekkert mót vali›<br>");
              add(closeButton);
          }
      }
      else {
          add("<center><br>");
          add("ﬁú ver›ur a› ská ﬂig inn í kerfi› me› notendanafni og lykilor›i<br><br>");
          add(closeButton);
          add("</center>");
      }

  }


  public Tournament getTournament(IWContext modinfo) {
      return (Tournament) modinfo.getSessionAttribute("tournament_registrationForMembers");
  }

  public void setTournament(IWContext modinfo, Tournament tournament) {
      modinfo.setSessionAttribute("tournament_registrationForMembers",tournament);
  }

  public void register(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
      Tournament tournament = getTournament(modinfo);
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);

      if (getTournament(modinfo).isDirectRegistration()) {
          String subAction = modinfo.getParameter("sub_action");
          if (subAction == null) {
            try {
              getDirectRegistrationTable(modinfo,iwrb);
            }
            catch (Exception e) {
              e.printStackTrace(System.err);
            }
          }else if (subAction.equals("saveDirectRegistration")) {
              if (!TournamentController.isMemberRegisteredInTournament(tournament,member) ) {
                  saveDirectRegistration(modinfo,iwrb);
              }
          }
      }else {
          if (!TournamentController.isMemberRegisteredInTournament(tournament,member) ) {
              String subAction = modinfo.getParameter("subAction");
              if (subAction == null) {
                  notOnlineRegistration(modinfo);
              }else if (subAction.equals("yes")) {
                  String tournament_group_id = modinfo.getParameter("tournament_group");
                  if (tournament_group_id == null) {
                      getAvailableGroups(modinfo);
                  }else {
                      performRegistrationNotOnline(modinfo,tournament_group_id);
                  }

              }
          }
          else {
              add("<br><center>ﬁú er ﬂegar skrá›ur í móti›<br><br>");
              add(closeButton);
              add("</center>");
          }

      }
  }


  public void notOnlineRegistration(IWContext modinfo) {
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);

      Table table = new Table();
          table.setBorder(0);
          table.setAlignment("center");

      Form yesForm  = new Form();
          yesForm.maintainParameter("action");

          yesForm.add(new HiddenInput("subAction","yes"));
          SubmitButton yes = new SubmitButton(new Image("/pics/formtakks/ja.gif","J·"));
          yesForm.add(yes);

      Form noForm  = new Form();
          noForm.maintainParameter("action");
          noForm.add(new HiddenInput("subAction","no"));
          CloseButton no = new CloseButton(new Image("/pics/formtakks/nei.gif","Nei"));
          noForm.add(no);


      table.mergeCells(1,1,2,1);
      table.setAlignment(1,1,"center");
      table.setAlignment(1,3,"center");
      table.setAlignment(2,3,"center");
      table.add("Skrá \""+member.getName()+"\" í móti› \""+getTournament(modinfo).getName() +"\"?");
      table.add(yesForm,1,3);
      table.add(noForm,2,3);

      add("<br>");
      add(table);
  }

  public void getAvailableGroups(IWContext modinfo) throws SQLException{
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);

      Tournament tournament = getTournament(modinfo);

      TournamentGroup[] tGroups = tournament.getTournamentGroups();
      List groups = TournamentController.getTournamentGroups(member,tournament);

      if (tGroups.length != 0) {
          if (groups.size() != 0)  {
              Form form = new Form();
                  form.maintainParameter("action");
                  form.maintainParameter("subAction");
              Table table = new Table();
                  table.setBorder(0);
                  table.setAlignment("center");

              DropdownMenu groupsMenu = new DropdownMenu(groups);

              table.add("Veldu flokk til a› spila í");
              table.mergeCells(1,1,2,1);
              table.add(member.getName(),1,2);
              table.add(groupsMenu,2,2);

              SubmitButton afram = TournamentController.getAheadButton(modinfo,"","");
              table.setAlignment(2,3,"right");
              table.add(afram,2,3);


              add("<br>");
              form.add(table);
              add(form);


          }else {
              add("<br><center>");
              add("ﬁú hefur ekki réttindi til ﬂess a› skrá ﬂig í móti›.<br>");
              add("Haf›u samband vi› klúbbinn ef ﬂú telur ﬂetta ekki vera rétt.<br><br>");
              add(closeButton);
              add("</center>");
            }
      }
      else {
          incorrectSetup();
      }

  }


  public void performRegistrationNotOnline(IWContext modinfo,String tournament_group_id) throws SQLException{
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);
      Tournament tournament = getTournament(modinfo);

      TournamentController.registerMember(member,tournament,tournament_group_id);
      add("Skrá›ur í móti›<br>");
      add(new CloseButton(new Image("/pics/formtakks/loka.gif","Loka glugga")));

  }


  public void getDirectRegistrationTable(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
      Tournament tournament = getTournament(modinfo);
      String tournament_round_id = modinfo.getParameter("tournament_round");

      if (tournament_round_id == null) {
          TournamentRound[] tRounds = (TournamentRound[]) tournament.getTournamentRounds();
          if (tRounds.length > 0 ) {
              tournament_round_id = Integer.toString(tRounds[0].getID());
          }
      }

      if (tournament_round_id != null) {

          add("<br><center>");

          Table table = new Table();
            table.setBorder(0);
            table.setWidth("90%");
            table.mergeCells(1,1,2,1);
            table.setAlignment(1,1,"left");
            table.setAlignment(2,2,"left");
            table.setAlignment(2,3,"left");
            table.setAlignment(1,2,"right");
            table.setAlignment(1,3,"right");

            table.add("<b>Skráning í mót</b>");
            table.add("1",1,2);
            table.add("Slá inn kennitölu á lausan rástima.  Hægt er a› skrá fleiri en einn keppanda í senn",2,2);

            table.add("2",1,3);
            table.add("†ttu á \"VISTA\" takkann sem er sta›settur ne›st á sí›unni",2,3);

          PresentationObject form = TournamentController.getStartingtimeTable(tournament,tournament_round_id,false,true);

          add(table);
          add("<hr>");
          add("<br>");
          add(form);
          add("</center>");

      }
      else {
          incorrectSetup();
      }


  }


  public void incorrectSetup() {
      add("<br><center>");
      add("Móti› er ekki rétt sett upp.<br>");
      add("Haf›u samband vi› klúbbinn og láttu kippa ﬂessu í li›inn.<br><br>");
      add(closeButton);
      add("</center>");
  }


  public void saveDirectRegistration(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
    Tournament tournament = getTournament(modinfo);

    boolean noOne = true;
    try {

        String sNumberOfGroups = modinfo.getParameter("number_of_groups");
        int iNumberOfGroups = Integer.parseInt(sNumberOfGroups);

        String sTournamentRoundId = modinfo.getParameter("tournament_round");
        int iTournamentRoundId = Integer.parseInt(sTournamentRoundId);

        String[] numbers;
        Member member = null;
        Table content = new Table();
            content.setWidth("85%");
            content.setAlignment("center");

        //TextInput correction;
        Form form = new Form();
            form.maintainParameter("tournament_round");
            form.add(new HiddenInput("action","directRegistrationMembersChosen"));
        Table table = new Table();
            table.setBorder(0);
            int tableRow = 1;
            table.setWidth("100%");
            table.add("<u>Nafn</u>",1,tableRow);
            table.add("<u>Flokkur</u>",3,tableRow);
            table.add("<u>Forgjˆf</u>",5,tableRow);
            //table.add("<u>LeirÈtting</u>",7,tableRow);
            //form.add(table);

        Table other = new Table();
            other.setBorder(1);
            int otherRow = 1;
            other.setWidth("100%");
            other.add("<u>Fundust ekki / Rangt inn slegnar</u>");

        Table done = new Table();
            done.setBorder(0);
            int doneRow = 1;
            done.add("<u>ﬁegar skrá›ir í móti›</u>");
        Table rejects = new Table();
            rejects.setBorder(0);
            int rejectsRow = 1;
            rejects.add("<u>Hafa ekki réttindi</u>");


        int[] errors = new int[4];
        TournamentGroup[] allGroupsInTournament = tournament.getTournamentGroups();
        DropdownMenu allGroups = new DropdownMenu(allGroupsInTournament,"extra_player_groups");

        for (int i = 1; i <= iNumberOfGroups ; i++) {

            numbers = (String[]) modinfo.getParameterValues("social_security_number_for_group_"+i);
            if (numbers != null) {

                for (int j = 0; j < numbers.length; j++) {
                    if (!numbers[j].equals("")) {
                        member =  (Member) MemberBMPBean.getMember(numbers[j]);

                        if (member == null) {
                            ++otherRow;
                            other.add(numbers[j],1,otherRow);
                        }
                        else {
                            errors = TournamentController.isMemberAllowedToRegister(member,tournament);
                            if ( (errors[0] == 0) && (errors[1] == 0) && (errors[2] == 0) && (errors[3] == 0) ){
//                            if (canMemberRegister == 0) {
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
                                            }
                                            else {
                                                table.add(member.getHandicap()+"",5,tableRow);
                                            }
                                        }
                                        else {
                                            if (member.getHandicap() > tournament.getFemaleMaxHandicap() ) {
                                                table.add(tournament.getFemaleMaxHandicap()+" *("+member.getHandicap()+")",5,tableRow);
                                            }
                                            else {
                                                table.add(member.getHandicap()+"",5,tableRow);
                                            }
                                        }
                                        //correction = new TextInput("handicap_correction_"+member.getID());
                                        //    correction.setSize(3);
                                        //table.add(correction,7,tableRow);
                                    }
                                }
                                else {
                                        ++doneRow;
                                        done.add(member.getName(),1,doneRow);
                                }
                            }
                            else {
                                ++rejectsRow;
                                rejects.add(member.getName(),1,rejectsRow);
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
//                                                star = true;
                                            }
                                            else {
                                                table.add(TextSoap.singleDecimalFormat(member.getHandicap())+"",5,tableRow);
                                            }
                                        }
                                        else {
                                            if (member.getHandicap() > tournament.getFemaleMaxHandicap() ) {
                                                table.add(tournament.getFemaleMaxHandicap()+" *("+member.getHandicap()+")",5,tableRow);
 //                                               star = true;
                                            }
                                            else {
                                                table.add(member.getHandicap()+"",5,tableRow);
                                            }
                                        }
//                                        correction = new TextInput("handicap_correction_"+member.getID());
//                                            correction.setSize(3);
//                                       table.add(correction,7,tableRow);
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
                            }
                        }
                    }
                }
            }

        }

        if (tableRow > 1) {
                Table instructionTable = new Table();
                  instructionTable.setBorder(0);
                  instructionTable.setWidth("85%");
                  instructionTable.setAlignment("center");
                  instructionTable.mergeCells(1,1,3,1);
                  instructionTable.setAlignment(1,1,"left");
                  instructionTable.setAlignment(2,2,"left");
                  instructionTable.setAlignment(2,3,"left");
                  instructionTable.setAlignment(1,2,"right");
                  instructionTable.setAlignment(1,3,"right");

                  instructionTable.add("<b>Skráning í mót</b>");
                  instructionTable.add("3",1,2);
                  instructionTable.add("Ef ﬂú ert gjaldgeng/ur í fleiri en einn flokk, veldu ﬂá réttan flokk",3,2);

                  instructionTable.add("4",1,3);
                  instructionTable.add("†ttu á \"ÁFRAM\" takkann. ﬁar me› er skráningu loki›.",3,3);

                  instructionTable.add("5",1,4);
                  instructionTable.setVerticalAlignment(1,4,"top");
                  instructionTable.add("Ef kylfingur hefur hærri forgjöf en hámarksforgjöf móts ﬂá stendur forgjöf hans í sviga fyrir aftan leikforgjöf.",3,4);

                  instructionTable.add("6",1,5);
                  instructionTable.add("Far›u yfir skráningu í rástimatöflu",3,5);
              add("<br>");
              add(instructionTable);
                add("<hr>");
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

        ++tableRow;

        table.setAlignment(3,tableRow,"right");




//        add(TournamentController.getBackLink());

    }
    catch (Exception e) {
        e.printStackTrace(System.err);
    }
    try {
        if (noOne) {
            this.getDirectRegistrationTable(modinfo,iwrb);
        }
    }
    catch (Exception ex) {
        ex.printStackTrace(System.err);
    }
}

public void finalizeDirectRegistration(IWContext modinfo, IWResourceBundle iwrb) throws SQLException {
    String tournament_round = modinfo.getParameter("tournament_round");

    String[] member_ids = modinfo.getParameterValues("member_id");
    String[] tournament_groups = modinfo.getParameterValues("tournament_group");
    String[] starting_time = modinfo.getParameterValues("starting_time");
    String[] starting_tee = modinfo.getParameterValues("starting_tee");
    String sTournamentRoundId = modinfo.getParameter("tournament_round");

    Member member;
    TournamentGroup tGroup;
    Tournament tournament = getTournament(modinfo);

    javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
    if (member_ids != null) {
        for (int i = 0; i < member_ids.length; i++) {
            try {
                tm.begin();
                member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_ids[i]));

                tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_groups[i]));

                TournamentController.registerMember(member,tournament,tournament_groups[i]);
                if (starting_tee[i].equals("10")) {
                    TournamentController.setupStartingtime(modinfo, member,tournament,Integer.parseInt(sTournamentRoundId),Integer.parseInt(starting_time[i]),10);
                }else {
                    TournamentController.setupStartingtime(modinfo, member,tournament,Integer.parseInt(sTournamentRoundId),Integer.parseInt(starting_time[i]));
                }
                tm.commit();
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
                try {
                  tm.rollback();
                }catch (javax.transaction.SystemException se) {
                  se.printStackTrace(System.err);
                }
            }
        }
    }

    getDirectRegistrationTable(modinfo,iwrb);
}

}
