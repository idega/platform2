/*
 * Created on 15.4.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.business.AccessControl;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;


/**
 * Title: TournamentAdministratorWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TournamentAdministratorWindow extends GolfWindow {

	private String header;

	private String headerColor;
	private String mainColor;
	
	String selectedTabText = null;

	private String width = "100%";



	private String adminView;

	public static String ADMIN_VIEW_PARAMETER = "tournament_admin_view";

	public static String ADMIN_VIEW_SELECT_TOURNAMENT = "selTourn";
	public static String ADMIN_VIEW_CREATE_TOURNAMENT = "createTournament";
	public static String ADMIN_VIEW_MODIFY_TOURNAMENT = "modifyTournament";
	public static String ADMIN_VIEW_FINISH_TOURNAMENT = "finishTournament";
	public static String ADMIN_VIEW_REGISTER_SCORECARD = "tournamentScore";
	public static String ADMIN_VIEW_SETUP_STARTINGTIME = "setupStartingtime";
	public static String ADMIN_VIEW_REGISTER_MEMBER = "registerMembers";
	public static String ADMIN_VIEW_PRINTING = "outPrints";
	public static String ADMIN_VIEW_LISTS = "lists";

	
	/**
	 * 
	 */
	public TournamentAdministratorWindow() {
		this("TournamentAdmin",850,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public TournamentAdministratorWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(TournamentAdministrator.class);
		this.setResizable(true);
		this.setScrollbar(true);
		setContentAreaAlignment(Table.HORIZONTAL_ALIGN_CENTER);
	}
	
	public void setSelectedTabText(String text) {
		selectedTabText = text;
	}
	
	protected void doIn_main(IWContext iwc) throws Exception {

		if (AccessControl.isAdmin(iwc) || AccessControl.isClubAdmin(iwc)) {
			String view = iwc.getParameter(ADMIN_VIEW_PARAMETER);
			if (adminView != null) {
				view = adminView;
			}

			String URI = iwc.getRequestURI();
			
			if (view == null) {
				if(selectedTabText == null) {
					if (this instanceof TournamentListsWindow) {
						selectedTabText = localize("tournament.lists","Lists");
					} else if (this instanceof TournamentSelectorWindow) {
						selectedTabText = localize("tournament.select_tournament","Select Tournament");
					} else if (this instanceof CloseTournamentWindow) {
						selectedTabText = localize("tournament.close_tournament","Close Tournament");
					} else if (this instanceof TournamentCreatorWindow) {
						selectedTabText = localize("tournament.create_tournament","Create Tournament");
					} else if (this instanceof ScorecardSelectWindow) {
						selectedTabText = localize("tournament.scorecard","Scorecard");
					} else if (this instanceof TournamentStartingtimeSetupWindow) {
						selectedTabText = localize("tournament.setup_teetimes","Setup Teetimes");
					} else if (this instanceof TournamentRegistrationWindow) {
						selectedTabText = localize("tournament.register_member","Register Member");
					} else if (this instanceof TournamentCreatorWindow) {
						selectedTabText = localize("tournament.modify_tournament","Modify Tournament");
					} else if (this instanceof PrintingWindow) {
						selectedTabText = localize("tournament.printouts","Printouts");
					} else {
						view = ADMIN_VIEW_SELECT_TOURNAMENT;
						selectedTabText = localize("tournament.select_tournament","Select Tournament");
					}
				} else {
					view = ADMIN_VIEW_SELECT_TOURNAMENT;
				}
			} else if (view.equals(ADMIN_VIEW_LISTS)) {
				selectedTabText = localize("tournament.lists","Lists");
			} else if (view.equals(ADMIN_VIEW_SELECT_TOURNAMENT)) {
				selectedTabText = localize("tournament.select_tournament","Select Tournament");
			} else if (view.equals(ADMIN_VIEW_FINISH_TOURNAMENT)) {
				selectedTabText = localize("tournament.close_tournament","Close Tournament");
			} else if (view.equals(ADMIN_VIEW_CREATE_TOURNAMENT)) {
				selectedTabText = localize("tournament.create_tournament","Create Tournament");
			} else if (view.equals(ADMIN_VIEW_REGISTER_SCORECARD)) {
				selectedTabText = localize("tournament.scorecard","Scorecard");
			} else if (view.equals(ADMIN_VIEW_SETUP_STARTINGTIME)) {
				selectedTabText = localize("tournament.setup_teetimes","Setup Teetimes");
			} else if (view.equals(ADMIN_VIEW_REGISTER_MEMBER)) {
				selectedTabText = localize("tournament.register_member","Register Member");
			} else if (view.equals(ADMIN_VIEW_MODIFY_TOURNAMENT)) {
				selectedTabText = localize("tournament.modify_tournament","Modify Tournament");
			} else if (view.equals(ADMIN_VIEW_PRINTING)) {
				selectedTabText = localize("tournament.printouts","Printouts");
			}

		
			Link lSelectTournament = getTemplateHeaderLink(localize("tournament.select_tournament","Select Tournament"));//new Link(iSelectTournament, TournamentSelectorWindow.class);
			lSelectTournament.setClassToInstanciate(TournamentSelectorWindow.class);
			//			Link lSelectTournament = new
			// Link(iSelectTournament,"tournament_select.jsp");
			lSelectTournament.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_SELECT_TOURNAMENT);
	
			Link lPrintOuts = getTemplateHeaderLink(localize("tournament.printouts","Printouts"));//new Link(iPrintouts, PrintingWindow.class);
			lPrintOuts.setClassToInstanciate(PrintingWindow.class);
			//	    Link lPrintOuts = new Link(iPrintouts,"printing.jsp");
			lPrintOuts.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_PRINTING);
	
			Link lCreateTournament = getTemplateHeaderLink(localize("tournament.create_tournament","Create Tournament"));//new Link(iCreateTournament, TournamentCreatorWindow.class);
			lCreateTournament.setClassToInstanciate(TournamentCreatorWindow.class);
			//	    Link lCreateTournament = new
			// Link(iCreateTournament,"createtournament.jsp");
			lCreateTournament.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_CREATE_TOURNAMENT);
			lCreateTournament.addParameter("tournament_control_mode", "create");
	
			Link lFinish = getTemplateHeaderLink(localize("tournament.close_tournament","Close Tournament"));//new Link(iFinishTournament, CloseTournamentWindow.class);
			lFinish.setClassToInstanciate(CloseTournamentWindow.class);
			//	    Link lFinish = new
			// Link(iFinishTournament,"close_tournament.jsp");
			lFinish.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_FINISH_TOURNAMENT);
			
			Link lScore = getTemplateHeaderLink(localize("tournament.scorecard","Scorecard"));//new Link(iScorecard, ScorecardSelectWindow.class);
			lScore.setClassToInstanciate(ScorecardSelectWindow.class);
			//	    Link lScore = new Link(iScorecard,"scorecard_select.jsp");
			lScore.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_REGISTER_SCORECARD);
	
			//		    Link lSetupStartingtime = new
			// Link(iSetupStartingtime,"setupstartingtime.jsp");
			
			Link lSetupStartingtime = getTemplateHeaderLink(localize("tournament.setup_teetimes","Setup Teetimes"));//new Link(iSetupStartingtime, TournamentStartingtimeSetupWindow.class);
			lSetupStartingtime.setClassToInstanciate(TournamentStartingtimeSetupWindow.class);
			lSetupStartingtime.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_SETUP_STARTINGTIME);
			
			Link lRegisterMember = getTemplateHeaderLink(localize("tournament.register_member","Register Member"));//new Link(iRegisterMember, TournamentRegistrationWindow.class);
			lRegisterMember.setClassToInstanciate(TournamentRegistrationWindow.class);
			//	    Link lRegisterMember = new
			// Link(iRegisterMember,"registermember.jsp");
			lRegisterMember.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_REGISTER_MEMBER);
			
			Link lModifyTournament = getTemplateHeaderLink(localize("tournament.modify_tournament","Modify Tournament"));//new Link(iModifyTournament, TournamentUpdaterWindow.class);
			lModifyTournament.setClassToInstanciate(TournamentUpdaterWindow.class);
			//	    Link lModifyTournament = new
			// Link(iModifyTournament,"modifytournament.jsp");
			lModifyTournament.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_MODIFY_TOURNAMENT);
			lCreateTournament.addParameter("tournament_control_mode", "edit");
	
			Link lLists = getTemplateHeaderLink(localize("tournament.lists","Lists"));//new Link(iLists, TournamentListsWindow.class);
			lLists.setClassToInstanciate(TournamentListsWindow.class);
			//			Link lLists = new Link(iLists, "lists.jsp");
			lLists.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_LISTS);
	
			//                myTable.add(opinmot,2,1);
			
	
			this.emptyMenuArea();
			this.addMenuLink(lSelectTournament);
			this.addMenuLink(lLists);
			this.addMenuLink(lPrintOuts);
			this.addMenuLink(lFinish);
			this.addMenuLink(lScore);
			this.addMenuLink(lSetupStartingtime);
			this.addMenuLink(lRegisterMember);
			this.addMenuLink(lModifyTournament);
			this.addMenuLink(lCreateTournament);

		}
	}
	
	public void main(IWContext iwc) throws FinderException {
		/** Adding selected tournament */
		String tournament_id = (String) iwc.getSessionAttribute("tournament_id");
		String tournamentName;
		if (tournament_id != null) {
			int tourID = Integer.parseInt(tournament_id);
			if (tourID > 0) {
				Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(tourID);
				tournamentName = tournament.getName()+" : "+selectedTabText;
			} else {
				tournamentName = localize("tournament.no_tournament_selected", "No tournament selected")+" : "+selectedTabText;
			}
		} else {
			tournamentName = localize("tournament.no_tournament_selected", "No tournament selected")+" : "+selectedTabText;
		}
		addHeading(tournamentName);
	}


	public void setTournamentAdminView(String adminView) {
		this.adminView = adminView;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public void addMessage(String message) {
		add(getMessageText(message));
	}


	
}
