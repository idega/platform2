/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import com.idega.presentation.IWContext;
import is.idega.idegaweb.golf.templates.TournamentAdmin;

/**
 * @author gimmi
 */
public class TournamentAdministrator extends TournamentBlock {

	protected boolean tournamentMustBeSet() {
		return false;
	}

	public void main(IWContext modinfo) throws Exception {
	  String view = modinfo.getParameter("tournament_admin_view");
	  String URI = modinfo.getRequestURI();

		modinfo.removeSessionAttribute("tournament_id");

	  if (isAdmin() || isClubAdmin()) {
	    add(getResourceBundle().getImage("tournament/adminlogo.gif"));
	  }
	  else {
	    add("Engin réttindi");
	  }

	}
}
