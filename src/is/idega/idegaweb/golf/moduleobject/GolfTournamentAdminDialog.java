package is.idega.idegaweb.golf.moduleobject;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.presentation.CloseTournamentWindow;
import is.idega.idegaweb.golf.tournament.presentation.PrintingWindow;
import is.idega.idegaweb.golf.tournament.presentation.ScorecardSelectWindow;
import is.idega.idegaweb.golf.tournament.presentation.TournamentCreatorWindow;
import is.idega.idegaweb.golf.tournament.presentation.TournamentListsWindow;
import is.idega.idegaweb.golf.tournament.presentation.TournamentRegistrationWindow;
import is.idega.idegaweb.golf.tournament.presentation.TournamentSelectorWindow;
import is.idega.idegaweb.golf.tournament.presentation.TournamentStartingtimeSetupWindow;
import is.idega.idegaweb.golf.tournament.presentation.TournamentUpdaterWindow;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

public class GolfTournamentAdminDialog extends GolfBlock {

	private String header;
	private Table myTable;

	private String headerColor;
	private String mainColor;

	private String width = "100%";

	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

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

	
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";

	public GolfTournamentAdminDialog() {
		myTable = new Table(1, 4);

		super.addBreak();
		super.add(myTable);
		//myTable.setBorder(1);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setWidth(Table.HUNDRED_PERCENT);
		myTable.setHeight("90%");
//		myTable.setColor("#99CC99");
//		myTable.setColor(1, 1, "#FFFFFF");
//		myTable.setColor(2, 1, "#FFFFFF");
//		myTable.setColor(1, 2, "#CEDFD0");
//		myTable.setColor(1, 3, "#CEDFD0");
//		myTable.setColor(1, 4, "#CEDFD0");
		myTable.setAlignment("center");
		myTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		myTable.setAlignment(1,2,Table.HORIZONTAL_ALIGN_CENTER);
		myTable.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_CENTER);
		myTable.setVerticalAlignment(1, 3, "top");
		myTable.setHeight(3, "100%");
		myTable.setHeight(2, "17");
		myTable.setHeight(1, "17");

	}

	public void setTournamentAdminView(String adminView) {
		this.adminView = adminView;
	}

	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle(modinfo);
		iwb = getBundle(modinfo);

		if (AccessControl.isAdmin(modinfo) || AccessControl.isClubAdmin(modinfo)) {
			String view = modinfo.getParameter(ADMIN_VIEW_PARAMETER);
			if (adminView != null) {
				view = adminView;
			}

			String URI = modinfo.getRequestURI();

//			Image iSelectTournament = iwrb.getImage("tabs/selecttournament1.gif");
//			Image iCreateTournament = iwrb.getImage("tabs/newtournament1.gif");
//			Image iScorecard = iwrb.getImage("tabs/registerscorecard1.gif");
//			Image iFinishTournament = iwrb.getImage("tabs/handicapupdate1.gif");
//			Image iSetupStartingtime = iwrb.getImage("tabs/lineupteetimes1.gif");
//			Image iRegisterMember = iwrb.getImage("tabs/registergolfer1.gif");
//			Image iModifyTournament = iwrb.getImage("tabs/edittournament1.gif");
//			Image iPrintouts = iwrb.getImage("tabs/printouts1.gif");
//			Image iLists = iwrb.getImage("tabs/lists1.gif");

			//System.out.println("GolfTournamentAdminDialog : view = "+view+"
			// ... adminView = "+adminView);
			String selectedTabText = localize("tournament.select_tournament","Select Tournament");
			if (view == null) {
				//                    iCreateTournamente.setSrc("tabs/ollmot.gif");
				view = ADMIN_VIEW_SELECT_TOURNAMENT;
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
			
			Table linkTable = new Table();
			linkTable.setCellpadding(0);
			linkTable.setCellspacing(0);
			linkTable.setNoWrap();
			linkTable.setStyleClass("link_tab");
			
			linkTable.setCellpaddingLeft(1,1,5);
			linkTable.setCellpaddingLeft(2,1,5);
			linkTable.setCellpaddingLeft(3,1,5);
			linkTable.setCellpaddingLeft(4,1,5);
			linkTable.setCellpaddingLeft(5,1,5);
			linkTable.setCellpaddingLeft(6,1,5);
			linkTable.setCellpaddingLeft(7,1,5);
			linkTable.setCellpaddingLeft(8,1,5);
			linkTable.setCellpaddingLeft(9,1,5);
			
			linkTable.setCellpaddingRight(1,1,5);
			linkTable.setCellpaddingRight(2,1,5);
			linkTable.setCellpaddingRight(3,1,5);
			linkTable.setCellpaddingRight(4,1,5);
			linkTable.setCellpaddingRight(5,1,5);
			linkTable.setCellpaddingRight(6,1,5);
			linkTable.setCellpaddingRight(7,1,5);
			linkTable.setCellpaddingRight(8,1,5);
			linkTable.setCellpaddingRight(9,1,5);
			
			linkTable.add(lSelectTournament, 1, 1);
			linkTable.add(lLists, 2, 1);
			linkTable.add(lPrintOuts, 3, 1);
			linkTable.add(lFinish, 4, 1);
			linkTable.add(lScore, 5, 1);
			linkTable.add(lSetupStartingtime, 6, 1);
			linkTable.add(lRegisterMember, 7, 1);
			linkTable.add(lModifyTournament, 8, 1);
			linkTable.add(lCreateTournament, 9, 1);
			
			myTable.add(linkTable,1,2);
			//myTable.add("Velkomin/n í mótastjórann",1,3);

			/** Adding selected tournament */
			String tournament_id = (String) modinfo.getSessionAttribute("tournament_id");
			Text tournamentName;
			if (tournament_id != null) {
				Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
				tournamentName = getBigHeader(tournament.getName()+" : "+selectedTabText);
			} else {
				tournamentName = getBigHeader(iwrb.getLocalizedString("tournament.no_tournament_selected", "No tournament selected")+" : "+selectedTabText);
			}
			myTable.add(tournamentName, 1, 1);
		}
	}

	private void setDefaultValues() {
		//mainColor="#99CC99";
		//headerColor="#336666";

		//mainTable.setColumnColor(1,mainColor);
		//mainTable.setRowColor(1,headerColor);
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public void add(PresentationObject objectToAdd) {
		myTable.add(objectToAdd, 1, 3);
	}

	public void addMessage(String message) {
		add(message);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}