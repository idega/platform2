/*
 * Created on 4.3.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 */
public abstract class TournamentBlock extends GolfBlock {

	private String SESSION_PARAMETER_TOURNAMENT_ID = "tournament_id";
	private TournamentAdministratorWindow dialog;

	protected String PARAMETER_CLASS_NAME = "clsName";


	public void setAdminView(String view) {
		Page p = getParentPage();
		if(p instanceof TournamentAdministratorWindow) {
			((TournamentAdministratorWindow)p).setTournamentAdminView(view);
		}
	}
	public int getTournamentID(IWContext modinfo) {
		String tId = (String) modinfo.getSessionAttribute(SESSION_PARAMETER_TOURNAMENT_ID);
		if (tId != null) {
			try {
				return Integer.parseInt(tId);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		else {
			tId = modinfo.getParameter(TournamentSelector.PARAMETER_TOURNAMENT);
			if (tId != null) {
				try {
					int tournamentID = Integer.parseInt(tId);
					if (tournamentID > 0) {
						setTournamentID(modinfo, String.valueOf(tournamentID));
					}
					return tournamentID;
				} catch (Exception e) {
					//Not a number...
				}
			}
		}
		return -1;
	}
	
	public void setTournamentID(IWContext modinfo, String tournamentID) {
		modinfo.setSessionAttribute(SESSION_PARAMETER_TOURNAMENT_ID, tournamentID);
	}

	protected abstract boolean tournamentMustBeSet();

	public void _main(IWContext modinfo) throws Exception {
		
//		System.out.println("------------------------------------------");
//		Enumeration enum = modinfo.getParameterNames();
//		while (enum.hasMoreElements()) {
//			String element = (String) enum.nextElement();
//			System.out.println(element+" : "+modinfo.getParameter(element));
//		}
//		System.out.println("-------------------END--------------------");		
		int tID = getTournamentID(modinfo);

		if (tID < 1 && tournamentMustBeSet()) {
			System.out.println("ClassCalling is " + getClassName());
			add(getMessageText(localize("tournament.loading_tournament_list...","Loading tournament list...")));
			add(Text.getBreak());
		  	add(Text.getBreak());
		  	add(modinfo.getIWMainApplication().getCoreBundle().getImage("busy.gif"));
			getParentPage().setToRedirect(modinfo.getIWMainApplication().getObjectInstanciatorURI(TournamentSelectorWindow.class) + "&" + PARAMETER_CLASS_NAME + "=" + getClassName()+"Window");
		} else {
			super._main(modinfo);
		}
	}
	/*
	 * public Tournament getTournament(ModuleInfo modinfo) throws SQLExceptionrn
	 * new Tournament(tID); } return null; }
	 */
}