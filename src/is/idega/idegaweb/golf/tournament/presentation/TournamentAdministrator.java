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
		return false;
	}

	public void main(IWContext modinfo) throws Exception {
		String view = modinfo.getParameter("tournament_admin_view");
		String URI = modinfo.getRequestURI();

		modinfo.removeSessionAttribute("tournament_id");

		if (isAdmin() || isClubAdmin()) {
			//add(getResourceBundle().getImage("tournament/adminlogo.gif"));
			add(getMessageText(localize("tournament.loading_tournament_list...", "Loading tournament list...")));
			add(Text.getBreak());
			add(Text.getBreak());
			add(modinfo.getIWMainApplication().getCoreBundle().getImage("busy.gif"));
			String URL = modinfo.getIWMainApplication().getObjectInstanciatorURI(TournamentSelectorWindow.class) + "&" + PARAMETER_CLASS_NAME + "=" + TournamentSelectorWindow.class.getName();
			if (modinfo.isParameterSet(TournamentSelector.PARAMETER_TOURNAMENT)) {
				URL += "&" + TournamentSelector.PARAMETER_TOURNAMENT + "=" + modinfo.getParameter(TournamentSelector.PARAMETER_TOURNAMENT);
			}
			getParentPage().setToRedirect(URL);
		}
		else {
			add(getErrorText(localize("tournament.no_permission", "No Permission")));
			this.getParentPage().setToClose(5);
		}

	}
}