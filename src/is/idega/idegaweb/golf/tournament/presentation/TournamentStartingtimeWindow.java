package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author gimmi
 */
public class TournamentStartingtimeWindow extends Window{

	public final static String PARAMETER_TOURNAMENT_ROUND_ID = "tsw_ptri";
	public final static String PARAMETER_TOURNAMENT_ID = "tsw_pti";

	public final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";	
	private IWResourceBundle iwrb;
	private Tournament tournament;
	private TournamentRound tournamentRound;

	public TournamentStartingtimeWindow() {
		this.setWidth(630);
		this.setScrollbar(true);
		this.setResizable(true);
		this.setToolbar(true);	
	}

	public void main(IWContext modinfo) {
		iwrb = getResourceBundle(modinfo);
		
		boolean valid = init(modinfo);
		
		if (valid) {
			try {
				add(TournamentController.getStartingtimeTable(tournament, Integer.toString(tournamentRound.getID()), true, true));
			} catch (SQLException e) {
				add("error");
				e.printStackTrace();
			}
		} else {
			add("Tournament not found");	
		}
		
	}
	
	private boolean init(IWContext modinfo) {
		String sti = modinfo.getParameter(PARAMETER_TOURNAMENT_ID);
		String stri = modinfo.getParameter(PARAMETER_TOURNAMENT_ROUND_ID);
		if (sti != null) {
			try {
				tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(sti));
				if (stri != null) {
					tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(stri));
				}else {
					TournamentRound[] rounds = tournament.getTournamentRounds();
					if (rounds.length > 0) {
						tournamentRound = rounds[0];
					}else {
						return false;	
					}	
				}
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				return false;
			}
		}
		return false;	
	}

	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
	}

}
