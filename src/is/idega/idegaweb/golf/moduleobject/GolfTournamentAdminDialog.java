package is.idega.idegaweb.golf.moduleobject;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.tournament.presentation.CloseTournament;
import is.idega.idegaweb.golf.tournament.presentation.Printing;
import is.idega.idegaweb.golf.tournament.presentation.ScorecardSelect;
import is.idega.idegaweb.golf.tournament.presentation.TournamentCreator;
import is.idega.idegaweb.golf.tournament.presentation.TournamentLists;
import is.idega.idegaweb.golf.tournament.presentation.TournamentRegistration;
import is.idega.idegaweb.golf.tournament.presentation.TournamentSelector;
import is.idega.idegaweb.golf.tournament.presentation.TournamentStartingtimeSetup;
import is.idega.idegaweb.golf.tournament.presentation.TournamentUpdater;


public class GolfTournamentAdminDialog extends PresentationObjectContainer{

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

private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

  public GolfTournamentAdminDialog(){
		myTable = new Table(2,4);
		
		super.addBreak();
		super.add(myTable);
		myTable.setBorder(0);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setWidth("90%");
		myTable.setHeight("90%");
		myTable.setColor("#99CC99");
		myTable.setColor(1,1,"#FFFFFF");
		myTable.setColor(2,1,"#FFFFFF");
		myTable.setColor(1,2,"#CEDFD0");
		myTable.setColor(1,3,"#CEDFD0");
		myTable.setColor(1,4,"#CEDFD0");
		myTable.setAlignment("center");
		myTable.setAlignment(1,1,"center");
		myTable.setAlignment(2,1,"right");
		myTable.setAlignment(1,3,"center");
		myTable.setVerticalAlignment(2,1,"bottom");
		myTable.setVerticalAlignment(1,3,"top");
		myTable.mergeCells(1,2,2,2);
		myTable.mergeCells(1,3,2,3);
		myTable.mergeCells(1,4,2,4);
		myTable.setHeight(3,"100%");
		myTable.setHeight(2,"17");
	
	}
	
	public void setTournamentAdminView(String adminView) {
		this.adminView = adminView;
	}
			
	public void main(IWContext modinfo) throws Exception{
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    if (com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo) || com.idega.jmodule.login.business.AccessControl.isClubAdmin(modinfo)) {
	    String view = modinfo.getParameter(ADMIN_VIEW_PARAMETER);
	    if (adminView != null) {
	    	view = adminView;	
	    }
	
	    String URI = modinfo.getRequestURI();
	
	    Image mynd4 = iwrb.getImage("leftcorner.gif");
	    Image iSelectTournament = iwrb.getImage("tabs/selecttournament1.gif");
	    Image iCreateTournament = iwrb.getImage("tabs/newtournament1.gif");
	    Image iScorecard = iwrb.getImage("tabs/registerscorecard1.gif");
	    Image iFinishTournament = iwrb.getImage("tabs/handicapupdate1.gif");
	    Image iSetupStartingtime = iwrb.getImage("tabs/lineupteetimes1.gif");
	    Image iRegisterMember = iwrb.getImage("tabs/registergolfer1.gif");
	    Image iModifyTournament = iwrb.getImage("tabs/edittournament1.gif");
	    Image iPrintouts = iwrb.getImage("tabs/printouts1.gif");
	    Image iLists = iwrb.getImage("tabs/lists1.gif");
	
						//System.out.println("GolfTournamentAdminDialog : view = "+view+" ... adminView = "+adminView);
	
	    if (view == null) {
	//                    iCreateTournamente.setSrc("tabs/ollmot.gif");
	          view = "";
	    }
	    else if (view.equals(ADMIN_VIEW_LISTS)) {
	    	iLists = iwrb.getImage("tabs/lists.gif");	
	    }
	    else if ( view.equals(ADMIN_VIEW_SELECT_TOURNAMENT) ) {
	    	iSelectTournament = iwrb.getImage("tabs/selecttournament.gif");	
	    }
	    else if ( view.equals(ADMIN_VIEW_FINISH_TOURNAMENT) ) {
	        iFinishTournament = iwrb.getImage("tabs/handicapupdate.gif");
	    }
	    else if ( view.equals(ADMIN_VIEW_CREATE_TOURNAMENT) ) {
	        iCreateTournament = iwrb.getImage("tabs/newtournament.gif");
	    }
	    else if ( view.equals(ADMIN_VIEW_REGISTER_SCORECARD) ) {
	        iScorecard = iwrb.getImage("tabs/registerscorecard.gif");
	    }
	    else if ( view.equals(ADMIN_VIEW_SETUP_STARTINGTIME) ) {
	        iSetupStartingtime = iwrb.getImage("tabs/lineupteetimes.gif");
	    }
	    else if ( view.equals(ADMIN_VIEW_REGISTER_MEMBER) ) {
	        iRegisterMember = iwrb.getImage("tabs/registergolfer.gif");
	    }
	    else if ( view.equals(ADMIN_VIEW_MODIFY_TOURNAMENT) ) {
	        iModifyTournament = iwrb.getImage("tabs/edittournament.gif");
	    }
	    else if ( view.equals(ADMIN_VIEW_PRINTING) ) {
	        iPrintouts = iwrb.getImage("tabs/printouts.gif");
	    }
	
	
			Link lSelectTournament = new Link(iSelectTournament,TournamentSelector.class);
//			Link lSelectTournament = new Link(iSelectTournament,"tournament_select.jsp");
				lSelectTournament.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_SELECT_TOURNAMENT);
	
	    Link lPrintOuts = new Link(iPrintouts,Printing.class);
//	    Link lPrintOuts = new Link(iPrintouts,"printing.jsp");
	      lPrintOuts.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_PRINTING);
	
	    Link lCreateTournament = new Link(iCreateTournament,TournamentCreator.class);
//	    Link lCreateTournament = new Link(iCreateTournament,"createtournament.jsp");
	      lCreateTournament.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_CREATE_TOURNAMENT);
	      lCreateTournament.addParameter("tournament_control_mode","create");
	
	    Link lFinish = new Link(iFinishTournament, CloseTournament.class);
//	    Link lFinish = new Link(iFinishTournament,"close_tournament.jsp");
	      lFinish.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_FINISH_TOURNAMENT);
	    Link lScore = new Link(iScorecard, ScorecardSelect.class);
//	    Link lScore = new Link(iScorecard,"scorecard_select.jsp");
	      lScore.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_REGISTER_SCORECARD);
	
//		    Link lSetupStartingtime = new Link(iSetupStartingtime,"setupstartingtime.jsp");
	    Link lSetupStartingtime = new Link(iSetupStartingtime,TournamentStartingtimeSetup.class);
	      lSetupStartingtime.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_SETUP_STARTINGTIME);
	    Link lRegisterMember = new Link(iRegisterMember,TournamentRegistration.class);
//	    Link lRegisterMember = new Link(iRegisterMember,"registermember.jsp");
	      lRegisterMember.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_REGISTER_MEMBER);
	    Link lModifyTournament = new Link(iModifyTournament,TournamentUpdater.class);
//	    Link lModifyTournament = new Link(iModifyTournament,"modifytournament.jsp");
	      lModifyTournament.addParameter(ADMIN_VIEW_PARAMETER,ADMIN_VIEW_MODIFY_TOURNAMENT);
	      lCreateTournament.addParameter("tournament_control_mode","edit");
	
			Link lLists = new Link(iLists, TournamentLists.class);
//			Link lLists = new Link(iLists, "lists.jsp");
				lLists.addParameter(ADMIN_VIEW_PARAMETER, ADMIN_VIEW_LISTS);
	
	//                myTable.add(opinmot,2,1);
	
						myTable.add(lSelectTournament, 1, 1);
	
			myTable.add(lLists,2,1);
	    myTable.add(lPrintOuts,2,1);
	    myTable.add(lFinish,2,1);
	    myTable.add(lScore,2,1);
	    myTable.add(lSetupStartingtime,2,1);
	    myTable.add(lRegisterMember,2,1);
	    myTable.add(lModifyTournament,2,1);
	    myTable.add(lCreateTournament,2,1);
	    //myTable.add("Velkomin/n í mótastjórann",1,3);
        
	    /** Adding selected tournament */
	    String tournament_id = (String) modinfo.getSessionAttribute("tournament_id");
						Text tournamentName;
	    if (tournament_id != null) {
	    	Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
	    	tournamentName = new Text(tournament.getName());
	    } else {
	    	tournamentName = new Text(iwrb.getLocalizedString("tournament.no_tournament_selected","No tournament selected"));	
	    }
	    tournamentName.setFontFace(Text.FONT_FACE_VERDANA);
	    tournamentName.setFontSize(Text.FONT_SIZE_12_HTML_3);
	    myTable.setAlignment(1, 2, "center");
	    myTable.add(tournamentName, 1, 2);
    }
	}

	private void setDefaultValues(){
		//mainColor="#99CC99";
		//headerColor="#336666";

                //mainTable.setColumnColor(1,mainColor);
		//mainTable.setRowColor(1,headerColor);
	}

	public void setHeader(String header){
		this.header=header;
	}

	public String getHeader(){
		return header;
	}

	public void add(PresentationObject objectToAdd){
		myTable.add(objectToAdd,1,3);
	}

	public void addMessage(String message){
	  add(message);
	}

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
