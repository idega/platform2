/*
 * Created on 3.5.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;


/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentAdminLink extends GolfBlock {

	private String _styleName = "";
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		Link link = new Link(getResourceBundle().getLocalizedString("tournament.tournament_admin", "Tournament admin"));
		link.setStyleClass(_styleName);
		link.setWindowToOpen(TournamentAdministratorWindow.class);
		link.addParameter(TournamentSelector.PARAMETER_TOURNAMENT, getTournamentSession(iwc).getTournamentID());
		add(link);
	}
	
	public void setStyleName(String styleName) {
		_styleName = styleName;
	}

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}