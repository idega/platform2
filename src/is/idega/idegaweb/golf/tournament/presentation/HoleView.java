package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.sql.SQLException;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class HoleView extends GolfWindow {

	public HoleView() {
		setWidth(796);
		setHeight(600);
		setResizable(true);
		setScrollbar(true);
	}
	
	public void main(IWContext modinfo) throws Exception {
	  int refresh = 180;
	  getParentPage().setToRedirect("",refresh);

	  String tournamentID = modinfo.getParameter("tournamentID");
	    if ( tournamentID == null ) getParentPage().close();
	  String tournamentGroupID = null;
//	= request.getParameter("tournamentGroupID");
//	    if ( tournamentGroupID == null ) getPage().close();
	  String tournamentRoundID = modinfo.getParameter("tournamentRoundID");
	    if ( tournamentRoundID == null ) getParentPage().close();
	  String[] tournament_group_ids = modinfo.getParameterValues("tournamentGroupID");
	    if ( tournament_group_ids == null ) getParentPage().close();

	  IWBundle bundle = getBundle(modinfo);
	  IWResourceBundle iwrb = bundle.getResourceBundle(modinfo.getCurrentLocale());



	  if (tournament_group_ids.length == 1) {
	      tournamentGroupID = tournament_group_ids[0];
	  }else if (tournament_group_ids.length > 1) {
	      String currentID = (String)modinfo.getSessionAttribute("viewing_tournament_group_id");
	      if (currentID == null) {
	          tournamentGroupID = tournament_group_ids[0];
	      } else {
	          boolean done = false;
	          for (int i = 0; i < tournament_group_ids.length; i++) {
	              if (done) {break;}
	              if (currentID.equalsIgnoreCase(tournament_group_ids[i])) {
	                  if (i < tournament_group_ids.length -1) {
	                      tournamentGroupID = tournament_group_ids[i+1];
	                      done = true;
	                  }else if (i < tournament_group_ids.length) {
	                      tournamentGroupID = tournament_group_ids[0];
	                      done = true;
	                  }
	              }
	          }
	      }
	  }
	  modinfo.setSessionAttribute("viewing_tournament_group_id",tournamentGroupID);

	  HoleByHole hbyh = new HoleByHole(tournamentID, tournamentRoundID,tournamentGroupID);
	   hbyh.setCacheable("1_hole_by_hole_"+tournamentID+"_"+tournamentRoundID+"_"+tournamentGroupID,180000);

	  Table legendTable = new Table(2,4);
	    legendTable.setBorder(0);
	    legendTable.setCellspacing(3);

	    legendTable.setColor(1,1,"#2050A8");
	    legendTable.setColor(1,2,"#BB2322");
	    legendTable.setColor(1,3,"#04463C");
	    legendTable.setColor(1,4,"#777D1A");

	    legendTable.add(iwrb.getLocalizedString("tournament.eagle","Eagle"),2,1);
	    legendTable.add(iwrb.getLocalizedString("tournament.birdie","Birdie"),2,2);
	    legendTable.add(iwrb.getLocalizedString("tournament.bogey","Bogey"),2,3);
	    legendTable.add(iwrb.getLocalizedString("tournament.dbl_bogey","Dbl bogey"),2,4);

	    legendTable.setColumnAlignment(2,"left");
	    legendTable.setWidth(1,"18");


	  Text exp = new Text(iwrb.getLocalizedString("tournament.page_will_refresh","This page is updated every 60 seconds.  The page can also be updated by clicking"));
	  Text exp3 = new Text(iwrb.getLocalizedString("tournament.page_will_refresh3","here"));

	  Link link = new Link(exp3,modinfo.getRequestURI()+"?"+modinfo.getQueryString());


	  Table tal = new Table();
	    tal.setWidth("200");
	    tal.setCellspacing(5);
	    tal.setAlignment(1,1,"left");
	    tal.add(exp+" ",1,1);
	    tal.add(link,1,1);



	  Table ta = new Table();
	    ta.setAlignment("center");
	    ta.setWidth("780");
	    ta.setBorder(0);

	    ta.add(legendTable,1,1);
	    ta.add(bundle.getImage("shared/gsi_logo.jpg"),2,1);
//	    ta.add(bundle.getImage("shared/canon_logo.jpg"),3,1);
	    ta.add(bundle.getImage("shared/idegaweb_logo.jpg"),3,1);
	    ta.add(tal,4,1);

	    ta.setAlignment(1,1,"center");
	    ta.setAlignment(2,1,"center");
	    ta.setAlignment(3,1,"center");
	    ta.setAlignment(4,1,"center");
	 //   ta.setAlignment(5,1,"center");

	    ta.setVerticalAlignment(1,1,"middle");
	    ta.setVerticalAlignment(2,1,"middle");
	    ta.setVerticalAlignment(3,1,"middle");
	    ta.setVerticalAlignment(4,1,"middle");
	  //  ta.setVerticalAlignment(5,1,"middle");


	  add(hbyh);
	  add("<br>");
	  add(ta);
	}

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


  public class HoleByHole extends Block {
      String tournamentID;
      String tournamentRoundID;
      String tournamentGroupID;

      public HoleByHole(String tour_ID, String tRound_ID, String tGroup_ID) {
          tournamentID = tour_ID;
          tournamentRoundID = tRound_ID;
          tournamentGroupID = tGroup_ID;
      }

      public void main(IWContext modinfo) throws SQLException {
          TournamentResultsDetailed t = new TournamentResultsDetailed(Integer.parseInt(tournamentID),Integer.parseInt(tournamentGroupID),Integer.parseInt(tournamentRoundID));
          add(t);
      }
  }
}
