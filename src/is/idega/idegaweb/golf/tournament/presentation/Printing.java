/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.tournament.business.PrintStickersWriter;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.block.reports.business.ReportWriter;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;

/**
 * @author gimmi
 */
public class Printing extends TournamentBlock {

	private IWResourceBundle iwrb;

	protected boolean tournamentMustBeSet() {
		return true;
	}

	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle();
	  getPrintScreen(modinfo);
	}

	  public void getTournaments(IWContext modinfo) {

	    DropdownMenu menu = null;
	    GenericButton submit = getButton(new SubmitButton(localize("tournament.continue","Continue")));

	    Form myForm = new Form();
	      myForm.setMethod("post");
	      myForm.add(new HiddenInput("mode","print"));

	    Table myTable = new Table(1,3);
	      myTable.setAlignment("center");
	      myTable.setAlignment(1,2,"center");
	      myTable.setAlignment(1,3,"right");
	      myTable.setCellpadding(4);

	    menu = TournamentController.getDropdownOrderedByUnion(new DropdownMenu("tournament"),modinfo);
	                menu.setMarkupAttribute("size","10");

	    Text selectText = new Text(iwrb.getLocalizedString("tournament.choose_tournament","Choose tournament")+":");
	      selectText.setBold();
	      selectText.setFontSize(3);

	    myTable.add(selectText,1,1);
	    myTable.add(menu,1,2);
	    myTable.add(submit,1,3);

	    myForm.add(myTable);
	    add("<br>");
	    add(myForm);

	  }

	  public void getPrintScreen(IWContext modinfo) throws IOException,SQLException,FinderException {

		String tournament_id = (String) modinfo.getSessionAttribute("tournament_id");
//	    String tournament_id = modinfo.getParameter("tournament");

	    Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
	    TournamentRound[] tournamentRounds = tournament.getTournamentRounds();

	    Table myTable = new Table();
	      myTable.setWidth(2,"25");
	      myTable.setWidth(4,"25");
	      myTable.setAlignment("center");
	      myTable.setCellpadding(6);
	      myTable.setVerticalAlignment(3,2,"top");
	      myTable.setVerticalAlignment(5,2,"top");

	    Text before = new Text(iwrb.getLocalizedString("tournament.before_tournament","Before tournament"));
	      before.setFontSize(4);
	      before.setBold();
	      before.setUnderline();

	    Text during = new Text(iwrb.getLocalizedString("tournament.during_tournament","During tournament"));
	      during.setFontSize(4);
	      during.setBold();
	      during.setUnderline();

	    Text after = new Text(iwrb.getLocalizedString("tournament.after_tournament","After tournament"));
	      after.setFontSize(4);
	      after.setBold();
	      after.setUnderline();

	    Link memberLink =getLocalizedLink("tournament.members_by_group","Members in tournament (by group)");
	    memberLink.setWindowToOpen(TournamentMembersWindow.class);  
	    memberLink.addParameter("tournament_id",tournament_id);

	      myTable.add(memberLink,1,2);

	    for ( int a = 0; a < tournamentRounds.length; a++ ) {

	      Table roundTable = new Table();

	      Text roundText = new Text(iwrb.getLocalizedString("tournament.round","Round")+" "+(a+1));
	        roundText.setBold();
	      

	      Link startingTime = getLink(iwrb.getLocalizedString("tournament.tee_times","Tee times"));
	        startingTime.setWindowToOpen(PrintStartingtimesWindow.class);
	        startingTime.addParameter(PrintStartingtimes.PARAMETER_TOURNAMENT_ROUND_ID,tournamentRounds[a].getID());
	      Link scorecards = getLink(iwrb.getLocalizedString("tournament.stickers_on_scorecards","Stickers on scorecards"));
	        scorecards.setWindowToOpen(PrintStickersWindow.class);
	        scorecards.addParameter("tournament_round_id",tournamentRounds[a].getID());
	      
	      //Link scorecardsExcel = getLink(iwrb.getLocalizedString("tournament.stickers_on_scorecards","Stickers on scorecards")+" (Excel)");
	        //scorecardsExcel.setWindowToOpen(PrintStickersExcelWindow.class);
	     
	        Link scorecardsExcel = getLink( iwrb.getLocalizedString("tournament.stickers_on_scorecards","Stickers on scorecards")+" (Excel)" );
	        scorecardsExcel.setURL(modinfo.getIWMainApplication().getMediaServletURI()+"stickers.xls");
	        scorecardsExcel.addParameter(ReportWriter.PRM_WRITABLE_CLASS,IWMainApplication.getEncryptedClassName(PrintStickersWriter.class));
	        scorecardsExcel.addParameter("tournament_round_id",tournamentRounds[a].getID());
	        scorecardsExcel.setTarget(Link.TARGET_BLANK_WINDOW);
	      
	        
	        Link excel = getLink(iwrb.getLocalizedString("tournament.unfilled_leaderboard","Unfilled leaderboard")+" (Excel)");
	        //excel.setWindowToOpen(PrintUnfilledLeaderBoardExcelWindow.class);
	      	excel.setURL(modinfo.getIWMainApplication().getMediaServletURI()+"status.xls");
	      	excel.addParameter(ReportWriter.PRM_WRITABLE_CLASS,IWMainApplication.getEncryptedClassName(PrintStickersWriter.class));
	        excel.addParameter("tournament_round_id",tournamentRounds[a].getID());
	        excel.setTarget(Link.TARGET_BLANK_WINDOW);

//			Link scorecardWindow = new Link(iwrb.getLocalizedString("tournament.scorecards","Scorecards"));
//			scorecardWindow.setWindowToOpen(PrintScorecardWindow.class);
//			scorecardWindow.addParameter(PrintScorecardWindow.PARAMETER_TOURNAMENT_ROUND_ID,tournamentRounds[a].getID());

	      roundTable.add(roundText,1,1);
	      roundTable.add(startingTime,1,2);
	      roundTable.add(scorecards,1,3);
	      roundTable.add(" 4x8 ("+iwrb.getLocalizedString("tournament.width_x_heigth","Width x Heigth")+")",1,3);
	      roundTable.add(scorecardsExcel,1,4);
	      roundTable.add(excel,1,5);
	      //roundTable.add(scorecardWindow, 1, 6);

	      myTable.add(roundTable,1,a+3);

	    }

	    Text status = new Text(iwrb.getLocalizedString("tournament.leaderboard","Leaderboard"));
	    Text results = new Text(iwrb.getLocalizedString("tournament.tournament_results","Results"));
	    Text statusExl = new Text(iwrb.getLocalizedString("tournament.leaderboard","Leaderboard")+" (Excel)");
	    Text resultsExl = new Text(iwrb.getLocalizedString("tournament.tournament_results","Results")+" (Excel)");

	    Link ranking = new Link(status);
	      ranking.setWindowToOpen(PrintStatusWindow.class);
	      ranking.addParameter("tournament_id",tournament.getID());
	    Link ranking2 = new Link(results);
	      ranking2.setWindowToOpen(PrintStatusWindow.class);
	      ranking2.addParameter("tournament_id",tournament.getID());

		Link excelRanking = new Link(statusExl);
		  excelRanking.setWindowToOpen(PrintStatusWindow.class);
	      excelRanking.addParameter("tournament_id",tournament.getID());
	      excelRanking.addParameter("xls", "true");
	    Link excelRanking2 = new Link(resultsExl);
	      excelRanking2.setWindowToOpen(PrintStatusWindow.class);
	      excelRanking2.addParameter("tournament_id",tournament.getID());
	      excelRanking2.addParameter("xls", "true");

	    myTable.add(ranking,3,2);
	    myTable.add(ranking2,5,2);
	    //myTable.addBreak(3, 2);
	    //myTable.addBreak(5, 2);
	    //myTable.add(excelRanking,3,2);
	    //myTable.add(excelRanking2,5,2);



	    myTable.add(before,1,1);
	    myTable.add(during,3,1);
	    myTable.add(after,5,1);

	    add(myTable);

	  }
	  
	  private Link getPrintScoreCardExcelLink(IWContext iwc,String label){
	     Link link = getLink( label );
	     link.setURL(iwc.getIWMainApplication().getMediaServletURI()+"stickers.xls");
	     link.addParameter(ReportWriter.PRM_WRITABLE_CLASS,IWMainApplication.getEncryptedClassName(PrintStickersWriter.class));
	     return link;
	  }

}
