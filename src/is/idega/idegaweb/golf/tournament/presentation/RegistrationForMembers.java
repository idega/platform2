package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberBMPBean;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.StartingtimeView;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Strong;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class RegistrationForMembers extends GolfBlock {

	public static final String PRM_TOURNAMENT_ID = "tournament_id";
	public static final String PRM_ACTION = "action";
	public static final String VAL_ACTION_OPEN = "open";

  CloseButton closeButton = new CloseButton();

  public void main(IWContext modinfo) throws Exception {
  	IWResourceBundle iwrb = getResourceBundle();
      String tournament_id = modinfo.getParameter(PRM_TOURNAMENT_ID);

      if (tournament_id != null)  {
          setTournament(modinfo, ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id)));
      }

      Tournament tournament = getTournament(modinfo);
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);


      if (member != null) {
          if (tournament != null) {

              if (!getTournamentBusiness(modinfo).isMemberRegisteredInTournament(tournament,member) ) {

                  String action = modinfo.getParameter(PRM_ACTION);

                  if (action == null) {
                  	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.endsWith(modinfo.getMarkupLanguage())){
                  		Paragraph p = new Paragraph();
                  		p.add(getLocalizedMessage("tournament.error_occurred","Error occurred"));
                  		add(p);
                  		
                  	}else{
                  	  add(getLocalizedMessage("tournament.error_occurred","Error occurred"));
                  	  add(Text.getBreak());
                       add(getButton(new CloseButton()));
                  	}
                  }else if (action.equalsIgnoreCase("open")) {
                      register(modinfo, iwrb);
                  }else if (action.equals("directRegistrationMembersChosen")) {
                      finalizeDirectRegistration(modinfo,iwrb);
                  }
              }
              else {
              	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.endsWith(modinfo.getMarkupLanguage())){
              		Paragraph p = new Paragraph();
              		p.add(getMessageText(member.getName() +" "+ localize("tournament.is_registered_in_the_tournament_named","is registered in the tournament named") +" \""+tournament.getName()+"\" "));
              		add(p);
              	}else{
                  add("<center>");
                  add(getMessageText(member.getName() +" "+ localize("tournament.is_registered_in_the_tournament_named","is registered in the tournament named") +" \""+tournament.getName()+"\" "));
                  add("</center>");
              	}
              }
          }else {
          	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.endsWith(modinfo.getMarkupLanguage())){
          		Paragraph p = new Paragraph();
          		p.add(getLocalizedMessage("tournament.no_tournament_selected","No tournament selected"));
	             add(p);
          		
          	}else{
              add(getLocalizedMessage("tournament.no_tournament_selected","No tournament selected"));
              add(Text.getBreak());
              add(closeButton);
              }
          }
      }
      else {
      	
      	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
      		Paragraph p = new Paragraph();
      		p.add(getLocalizedMessage("tournament.you_have_to_register_to_the_system_using_login_and_password","You have to register to the system using login and password"));
             add(p);
      		
      	}else{
          add("<center>");
          add(Text.getBreak());
          add(getLocalizedMessage("tournament.you_have_to_register_to_the_system_using_login_and_password","You have to register to the system using login and password"));
          add(Text.getBreak());
          add(Text.getBreak());
          add(closeButton);
          add("</center>");
      	}
      }

  }


  public Tournament getTournament(IWContext modinfo) {
      return (Tournament) modinfo.getSessionAttribute("tournament_registrationForMembers");
  }

  public void setTournament(IWContext modinfo, Tournament tournament) {
      modinfo.setSessionAttribute("tournament_registrationForMembers",tournament);
  }

  public void register(IWContext modinfo, IWResourceBundle iwrb) throws RemoteException, SQLException {
      Tournament tournament = getTournament(modinfo);
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);

      if (getTournament(modinfo).isDirectRegistration()) {
          String subAction = modinfo.getParameter("sub_action");
          if (subAction == null) {
            try {
            		if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
            			getDirectRegistrationTableWML(modinfo,iwrb);
              	} else {
              		getDirectRegistrationTable(modinfo,iwrb);
              	}
            }
            catch (Exception e) {
              e.printStackTrace(System.err);
            }
          }else if (subAction.equals("saveDirectRegistration")) {
              if (!getTournamentBusiness(modinfo).isMemberRegisteredInTournament(tournament,member) ) {
                  saveDirectRegistration(modinfo,iwrb);
              }
          }
      }else {
          if (!getTournamentBusiness(modinfo).isMemberRegisteredInTournament(tournament,member) ) {
              String subAction = modinfo.getParameter("subAction");
              if (subAction == null) {
              	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
              		notOnlineRegistrationWML(modinfo);
              	} else {
              		notOnlineRegistration(modinfo);
              	}
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
          	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
          		Paragraph p = new Paragraph();
          		p.add(getLocalizedMessage("tournament.you_are_already_registered_to_this_tournament","You are already registered to this tournament"));
	             add(p);
          		
          	}else{
	          	add(Text.getBreak());
	              add("<center>");
	              add(getLocalizedMessage("tournament.you_are_already_registered_to_this_tournament","You are already registered to this tournament"));
	              add(Text.getBreak());
	              add(Text.getBreak());
	              add(closeButton);
	              add("</center>");
          	}
          }

      }
  }


  public void notOnlineRegistration(IWContext modinfo) {
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);

      Table table = new Table();
          table.setBorder(0);
          table.setAlignment("center");

      Form yesForm  = new Form();
          yesForm.maintainParameter(PRM_ACTION);

          yesForm.add(new HiddenInput("subAction","yes"));
          SubmitButton yes = new SubmitButton(localize("tournament.yes","Yes"));
          yesForm.add(yes);

      Form noForm  = new Form();
          noForm.maintainParameter(PRM_ACTION);
          noForm.add(new HiddenInput("subAction","no"));
          CloseButton no = new CloseButton(localize("tournament.no","no"));
          noForm.add(no);


      table.mergeCells(1,1,2,1);
      table.setAlignment(1,1,"center");
      table.setAlignment(1,3,"center");
      table.setAlignment(2,3,"center");
      table.add(getText(localize("tournament.register","Register")+" \""+member.getName()+"\" "+localize("tournament.to_the_tournament_named","to the tournament named")+" \""+getTournament(modinfo).getName() +"\"?"));
      table.add(yesForm,1,3);
      table.add(noForm,2,3);

      add(Text.getBreak());
      add(table);
  }
  
  public void notOnlineRegistrationWML(IWContext modinfo) {
    Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);

    Table table = new Table();

    Paragraph p = new Paragraph();
    p.add(new Text(localize("tournament.register","Register")+" \""+member.getName()+"\" "+localize("tournament.to_the_tournament_named","to the tournament named")+" \""+getTournament(modinfo).getName() +"\"?"));
    add(p);
    
    
    Link yes = new Link(localize("tournament.yes","Yes"));
    yes.maintainParameter(PRM_ACTION,modinfo);
    yes.addParameter("subAction","yes");
    
    Link no = new Link(localize("tournament.cancel","Cancel"));
    no.maintainParameter(PRM_ACTION,modinfo);
    no.addParameter("subAction","no");

    Paragraph p2 = new Paragraph();
    Strong s = new Strong();
    s.add(yes);
    p2.add(s);
    p2.add(new Text(" | "));
    Strong s2 = new Strong();
    s2.add(no);
    p2.add(s2);

    add(p2);
}

  public void getAvailableGroups(IWContext modinfo) throws SQLException, RemoteException{
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);

      Tournament tournament = getTournament(modinfo);

      TournamentGroup[] tGroups = tournament.getTournamentGroups();
      List groups = getTournamentBusiness(modinfo).getTournamentGroups(member,tournament);

      if (tGroups.length != 0) {
          if (groups.size() != 0)  {
          	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
          		Form form = new Form();
	                form.maintainParameter("action");
	                form.maintainParameter("subAction");
	
	            DropdownMenu groupsMenu = new DropdownMenu(groups);
	
	            Label l = new Label(localize("tournament.choose_group_to_play_in","Choose group to play in"),groupsMenu);
	            form.add(l);
	            form.add(groupsMenu);
	
	            SubmitButton afram = getTournamentBusiness(modinfo).getAheadButton(modinfo,"","");
	            form.add(afram);
	
	            add(form);
          	} else {
          	    Form form = new Form();
	                form.maintainParameter("action");
	                form.maintainParameter("subAction");
	            Table table = new Table();
	                table.setBorder(0);
	                table.setAlignment("center");
	
	            DropdownMenu groupsMenu = new DropdownMenu(groups);
	
	            table.add(getLocalizedText("tournament.choose_group_to_play_in","Choose group to play in"));
	            table.mergeCells(1,1,2,1);
	            table.add(member.getName(),1,2);
	            table.add(groupsMenu,2,2);
	
	            SubmitButton afram = getTournamentBusiness(modinfo).getAheadButton(modinfo,"","");
	            table.setAlignment(2,3,"right");
	            table.add(afram,2,3);
	
	
	            add(Text.getBreak());
	            form.add(table);
	            add(form);

          	}
              

          }else {
          	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
          		Paragraph p = new Paragraph();
          		p.add(getLocalizedMessage("tournament.you_do_not_have_permission_to_register","You do not have permission to register"));
	             p.add(new Break());
	             p.add(getLocalizedMessage("tournament.contact_the_club","Contact the club"));
	             add(p);
          		
          	}else{
	          	 add(Text.getBreak());
	              add("<center>");
	              add(getLocalizedMessage("tournament.you_do_not_have_permission_to_register","You do not have permission to register"));
	              add(Text.getBreak());
	              add(getLocalizedMessage("tournament.contact_the_club","Contact the club"));
	              add(Text.getBreak());
	              add(Text.getBreak());
	              add(closeButton);
	              add("</center>");
          	}
            }
      }
      else {
          incorrectSetup(modinfo);
      }

  }


  public void performRegistrationNotOnline(IWContext modinfo,String tournament_group_id) throws RemoteException, SQLException{
      Member member = (is.idega.idegaweb.golf.entity.Member) AccessControl.getMember(modinfo);
      Tournament tournament = getTournament(modinfo);
      getTournamentBusiness(modinfo).registerMember(member,tournament,tournament_group_id);
      
      if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
  		Paragraph p = new Paragraph();
  		p.add(getLocalizedText("tounament.registered_to_the_tournament","Registered to the tournament"));
        add(p);
  	  } else {
        add(getLocalizedText("tounament.registered_to_the_tournament","Registered to the tournament"));
        add(Text.getBreak());
        add(getButton(new CloseButton()));
  	  }

  }


  public void getDirectRegistrationTable(IWContext modinfo, IWResourceBundle iwrb) throws RemoteException, SQLException {
      Tournament tournament = getTournament(modinfo);
      String tournament_round_id = modinfo.getParameter("tournament_round");

      if (tournament_round_id == null) {
          TournamentRound[] tRounds = (TournamentRound[]) tournament.getTournamentRounds();
          if (tRounds.length > 0 ) {
              tournament_round_id = Integer.toString(tRounds[0].getID());
          }
      }

      if (tournament_round_id != null) {

      	  add(Text.getBreak());
          add("<center>");

          Table table = new Table();
            table.setWidth("90%");

            table.setAlignment(2,1,"left");
            table.setAlignment(2,2,"left");
            table.setAlignment(1,1,"right");
            table.setAlignment(1,2,"right");

            addHeading(localize("tournament.tournament_registration","Tournament registration"));
            table.add("1",1,1);
            
            table.add(getLocalizedText("tournament.choose_teetime_and_enter_ssn_in_the_textbox.__it_is_posible_to_register_more_than_one_at_a_time","Choose teetime and enter social security number.  It is posible to register more than one at a time."),2,1);

            table.add("2",1,2);
            table.add(getText(localize("tournament.press_the","Press the")+" \""+localize("tournament.save","Save")+"\" "+localize("tournament.button_located_at_the_bottom_of_the_page","button located at the bottom of the page.")),2,2);

          TournamentStartingtimeList form = getTournamentBusiness(modinfo).getStartingtimeTable(tournament,tournament_round_id,false,true,false,true);
          form.setSubmitButtonParameter("action", "open");

          add(table);
          add("<hr>");
          add(Text.getBreak());
          add(form);
          add("</center>");

      }
      else {
          incorrectSetup(modinfo);
      }


  }
  
  public void getDirectRegistrationTableWML(IWContext modinfo, IWResourceBundle iwrb) throws RemoteException, SQLException {
    Tournament tournament = getTournament(modinfo);
    String tournament_round_id = modinfo.getParameter("tournament_round");

    if (tournament_round_id == null) {
        TournamentRound[] tRounds = (TournamentRound[]) tournament.getTournamentRounds();
        if (tRounds.length > 0 ) {
            tournament_round_id = Integer.toString(tRounds[0].getID());
        }
    }

    if (tournament_round_id != null) {

        TournamentStartingtimeList form = getTournamentBusiness(modinfo).getStartingtimeTable(tournament,tournament_round_id,false,true,false,true);
        form.setSubmitButtonParameter("action", "open");

        
        Form from = new Form();
        
        DropdownMenu teetimes = new DropdownMenu();
        
        Label l = new Label(localize("start.choose_teetime","Choose teetime"),teetimes);
        
        SubmitButton button = new SubmitButton(localize("tournament.register","Register"));
        
        from.add(l);
        form.add(teetimes);
        form.add(button);
        //add(form);
		
        Paragraph p = new Paragraph();
        p.add(new Text(localize("temp.not_ready","It is not possible to register to this type of tournament at the moment.")));
        add(p);
        
    }
    else {
        incorrectSetup(modinfo);
    }


}
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void getStartingtimeRegistrationForm(IWContext modinfo, Tournament tournament, String tournament_round_id) throws Exception {


		if (tournament != null ){
			Form form = new Form();
			
			
			Table topTable = new Table();
			Table table = new Table();
			Table borderTable = new Table();


			
			form.add(topTable);
			borderTable.add(table);
			form.add(borderTable);
			int row = 1;
			int numberOfMember = 0;


			TournamentRound[] tourRounds = tournament.getTournamentRounds();

			int tournamentRoundId = -1;
			if (tournament_round_id == null) {
				tournament_round_id = "-1";
				tournamentRoundId = tourRounds[0].getID();
			}
			else {
				tournamentRoundId = Integer.parseInt(tournament_round_id);
			}

			TournamentRound tournamentRound = null;
			try {
				tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournamentRoundId);
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}

			boolean display = false;
			if (tournamentRound.getVisibleStartingtimes()) {
				display = true;
			}
			int roundNumber = tournamentRound.getRoundNumber();

			IWTimestamp tourDay = null;

			DropdownMenu rounds = (DropdownMenu) getStyledInterface(new DropdownMenu("tournament_round"));

				tourDay = new IWTimestamp(tournamentRound.getRoundDate());
				rounds.addMenuElement(tournamentRound.getID(), getResourceBundle().getLocalizedString("tournament.round", "Round") + " " + tournamentRound.getRoundNumber() + " " + tourDay.getISLDate(".", true));

			Text timeText;
			Text dMemberSsn;
			Text dMemberName;
			Text dMemberHand;
			Text dMemberUnion;

			Text tim = new Text(getResourceBundle().getLocalizedString("tournament.time", "Time"));
			Text sc = new Text(getResourceBundle().getLocalizedString("tournament.social_security_number", "Social security number"));
			Text name = new Text(getResourceBundle().getLocalizedString("tournament.name", "Name"));
			Text club = new Text(getResourceBundle().getLocalizedString("tournament.club", "Club"));
			Text hc = new Text(getResourceBundle().getLocalizedString("tournament.handicap", "Handicap"));

			table.add(tim, 1, row);
			table.add(sc, 2, row);
			table.add(name, 3, row);
			table.add(club, 4, row);
			table.add(hc, 5, row);

	
			table.setRowStyleClass(row, getHeaderRowClass());

			java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
			java.text.DecimalFormat handicapFormat = new java.text.DecimalFormat("###.0");
			Field field = tournament.getField();
			List members;
			CheckBox delete;

			Image removeImage = getBundle().getImage("/shared/tournament/de.gif", getResourceBundle().getLocalizedString("tournament.remove_from_tournament", "Remove from tournament"));
			removeImage.setToolTip(getResourceBundle().getLocalizedString("tournament.remove_from_tournament", "Remove from tournament"));

			Text tTime = new Text("");


			Link remove;
			Text tooMany = getSmallErrorText(getResourceBundle().getLocalizedString("tournament.no_room", "No room"));

			Union union;
			int union_id;
			String abbrevation = "'";

			boolean displayTee = false;
			if (tournamentRound.getStartingtees() > 1) {
				displayTee = true;
			}

			int groupCounterNum = 0;

			for (int y = 1; y <= tournamentRound.getStartingtees(); y++) {
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
					Text startTee = new Text(getResourceBundle().getLocalizedString("tournament.starting_tee", "Starting tee") + " : " + tee_number);
					table.add(startTee, 1, row);
				}

				int startInGroup = 0;
				is.idega.idegaweb.golf.entity.Member tempMember;
				TextInput socialNumber;
				CheckBox paid;
				int zebraRow = 1;

				StartingtimeView[] sView;

				while (endHour.isLaterThan(startHour)) {
					++row;
					++groupCounter;
					++groupCounterNum;
					startInGroup = 0;

					timeText = (Text) tTime.clone();
					timeText.setText(Text.NON_BREAKING_SPACE + extraZero.format(startHour.getHour()) + ":" + extraZero.format(startHour.getMinute()) + Text.NON_BREAKING_SPACE);
					table.add(timeText, 1, row);

					sView = getTournamentBusiness(modinfo).getStartingtimeView(tournamentRound.getID(), "", "", "grup_num", groupCounter + "", tee_number, "");


					startInGroup = sView.length;

					String styleClass = null;
					for (int i = 0; i < sView.length; i++) {
						if (zebraRow % 2 != 0) {
							styleClass = getLightRowClass();
						}
						else {
							styleClass = getDarkRowClass();
						}
						zebraRow++;
						
						table.setHeight(row, 10);
						++numberOfMember;
						if (i != 0) table.add(tooMany, 1, row);

						if (display) {
							dMemberSsn = null;
							dMemberName = null;
							dMemberHand = null;
							dMemberUnion = null;
							if (sView[i].getMemberId() != 1) {
								dMemberSsn = new Text(sView[i].getSocialSecurityNumber());
								dMemberName = new Text(sView[i].getName());
								dMemberUnion = new Text(sView[i].getAbbrevation());
								dMemberHand = new Text(com.idega.util.text.TextSoap.singleDecimalFormat(sView[i].getHandicap()));
							}
							else {
								dMemberSsn = new Text("-");
								dMemberName = new Text(getResourceBundle().getLocalizedString("tournament.reserved", "Reserved"));
								dMemberUnion = new Text("-");
								dMemberHand = new Text("-");
							}

							table.add(dMemberSsn, 2, row);
							table.add(dMemberName, 3, row);
							table.add(dMemberUnion, 4, row);
							table.add(dMemberHand, 5, row);
						}
						else {
							table.mergeCells(2, row, 7, row);
							table.setStyleClass(2, row, styleClass);
						}
						row++;
					}

					for (int i = startInGroup; i < (numberInGroup); i++) {
						if (tee_number == 10) {
							socialNumber = (TextInput) getStyledInterface(new TextInput("social_security_number_for_group_" + groupCounter + "_"));
						}
						else {
							socialNumber = (TextInput) getStyledInterface(new TextInput("social_security_number_for_group_" + groupCounter));
						}
						socialNumber.setLength(15);
						socialNumber.setMaxlength(10);
						table.add(socialNumber, 2, row);

					}
					startHour.addMinutes(minutesBetween);
					--row;
				}
			}

			++row;

			Text many = getSmallHeader(getResourceBundle().getLocalizedString("tournament.number_of_participants", "Number of participants") + " : " + numberOfMember);
			table.add(many, 1, row);


			SubmitButton submitButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("tournament.save", "Save")));
//			if (submitButtonParameter != null) {
//				submitButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("tournament.save", "Save"), submitButtonParameter[0], submitButtonParameter[1]));
//			}
			table.add(new HiddenInput("sub_action", "saveDirectRegistration"), 4, row);
			table.add(submitButton, 4, row);
			table.add(new HiddenInput("number_of_groups", "" + groupCounterNum), 4, row);


			add(form);
		} else {
			logError("Tournament not found in session, or in parameter");
			
		}
	}



  public void incorrectSetup(IWContext modinfo) {
  	if(modinfo.isClientHandheld() || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
  		Paragraph p = new Paragraph();
  		p.add(getLocalizedMessage("tournament.tournament_setup_is_not_right","Tournament setup is not right"));
  		p.add(Text.getBreak());
  		p.add(getLocalizedMessage("tournament.contact_the_club","Contact the club"));
        add(p);
  	} else {
  	  add(Text.getBreak());
  	  add("<center>");
      add(getLocalizedMessage("tournament.tournament_setup_is_not_right","Tournament setup is not right"));
      add(Text.getBreak());
      add(getLocalizedMessage("tournament.contact_the_club","Contact the club"));
      add(Text.getBreak());
      add(Text.getBreak());
      add(closeButton);
      add("</center>");
  	}
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
            table.setWidth(Table.HUNDRED_PERCENT);
            table.add(getLocalizedText("tournament.name","Name"),1,tableRow);
            table.add(getLocalizedText("tournament.group","Group"),3,tableRow);
            table.add(getLocalizedText("tournament.handicap","Handicap"),5,tableRow);

        Table other = new Table();
            other.setBorder(1);
            int otherRow = 1;
            other.setWidth(Table.HUNDRED_PERCENT);
            other.add(getLocalizedText("tournament.were_not_found","were not found"));

        Table done = new Table();
            done.setBorder(0);
            int doneRow = 1;
            done.add(getLocalizedText("tournament.are_already_registered","are already registered"));
        Table rejects = new Table();
            rejects.setBorder(0);
            int rejectsRow = 1;
            rejects.add(getLocalizedText("tournament.do_not_have_permission","do not have permission"));


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
                            errors = getTournamentBusiness(modinfo).isMemberAllowedToRegister(member,tournament);
                            if ( (errors[0] == 0) && (errors[1] == 0) && (errors[2] == 0) && (errors[3] == 0) ){
//                            if (canMemberRegister == 0) {
                                if (!getTournamentBusiness(modinfo).isMemberRegisteredInTournament(tournament, ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(iTournamentRoundId),tournament.getNumberInGroup(),member) ) {
                                    List tGroups = getTournamentBusiness(modinfo).getTournamentGroups(member,tournament);
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
                            errors = getTournamentBusiness(modinfo).isMemberAllowedToRegister(member,tournament);

                            if ( (errors[0] == 0) && (errors[1] == 0) && (errors[2] == 0) && (errors[3] == 0) ){
                                if (!getTournamentBusiness(modinfo).isMemberRegisteredInTournament(tournament, ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(iTournamentRoundId),tournament.getNumberInGroup(),member) ) {
                                    List tGroups = getTournamentBusiness(modinfo).getTournamentGroups(member,tournament);
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

                  instructionTable.add(getSmallHeader(localize("tournament.tournament_registration","Tournament registration")));
                  instructionTable.add("3",1,2);
                  instructionTable.add(getLocalizedText("tournaemnt.if_you_fit_in_more_than_one_group_then_choose_group","If you fit in more than one group, then choose group."),3,2);

                  instructionTable.add("4",1,3);
                  instructionTable.add(getText(localize("tournament.press_the","Press the")+" \""+localize("trounament.continue","continue")+"\" "+localize("tournament.button_and_the_registration_is_finished","button and the registration is finished.")),3,3);

                  instructionTable.add("5",1,4);
                  instructionTable.setVerticalAlignment(1,4,"top");
                  instructionTable.add(getText(localize("tournament.if_player_has_higher_handicap_than_the_max_handicap_for_the_tournament_then_his_handicap_is_visible_within_parenthesis_after_his_gamehandicap","if_player_has_higher_handicap_than_the_max_handicap_for_the_tournament_then_his_handicap_is_visible_within_parenthesis_after_his_gamehandicap")),3,4);

                  instructionTable.add("6",1,5);
                  instructionTable.add(getMessageText(localize("tournament.check_registration_in_teetime_table","Check registration in teetime table.")),3,5);
                  add(Text.getBreak());
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
            buttonTable.setWidth(Table.HUNDRED_PERCENT);
            buttonTable.add(getTournamentBusiness(modinfo).getAheadButton(modinfo,"",""));
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

public void finalizeDirectRegistration(IWContext modinfo, IWResourceBundle iwrb) throws RemoteException, SQLException {
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

                getTournamentBusiness(modinfo).registerMember(member,tournament,tournament_groups[i]);
                if (starting_tee[i].equals("10")) {
                	getTournamentBusiness(modinfo).setupStartingtime(modinfo, member,tournament,Integer.parseInt(sTournamentRoundId),Integer.parseInt(starting_time[i]),10);
                }else {
                	getTournamentBusiness(modinfo).setupStartingtime(modinfo, member,tournament,Integer.parseInt(sTournamentRoundId),Integer.parseInt(starting_time[i]));
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
