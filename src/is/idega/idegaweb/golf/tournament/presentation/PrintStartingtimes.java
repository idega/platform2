package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;

/**
 * @author gimmi
 */
public class PrintStartingtimes extends GolfBlock {

	public static String PARAMETER_TOURNAMENT_ROUND_ID = "tournament_round_id";
	
	public void main(IWContext modinfo) throws Exception {

    String tournament_round_id = modinfo.getParameter(PARAMETER_TOURNAMENT_ROUND_ID);

    TournamentRound tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
    Tournament tournament = tournamentRound.getTournament();

    Form myForm = TournamentController.getStartingtimeTable(modinfo, tournament,tournament_round_id,true,true,getResourceBundle());
    add(myForm);

  }}
