/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 */
public class TournamentAdministrator extends TournamentBlock {
	protected boolean tournamentMustBeSet() {
		return true;
	}

	public void main(IWContext modinfo) throws Exception {
	  String view = modinfo.getParameter("tournament_admin_view");
	  String URI = modinfo.getRequestURI();

		modinfo.removeSessionAttribute("tournament_id");

	  if (isAdmin() || isClubAdmin()) {
	    //add(getResourceBundle().getImage("tournament/adminlogo.gif"));
	  	add(getMessageText(localize("tournament.loading_tournament_list...","Loading tournament list...")));
		add(Text.getBreak());
	  	add(Text.getBreak());
	  	add(modinfo.getIWMainApplication().getCoreBundle().getImage("busy.gif"));
	  }
	  else {
	    add(getErrorText(localize("tournament.no_permission","No Permission")));
	    this.getParentPage().setToClose(5);
	  }

	}
}
