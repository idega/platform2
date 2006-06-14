package is.idega.idegaweb.golf.entity;

import com.idega.data.PrimaryKey;

public class TournamentTournamentTourPK extends PrimaryKey {

	public TournamentTournamentTourPK(Object tournamentID, Object tournamentTourID) {
		super();
		setTournamentID(tournamentID);
		setTournamentTourID(tournamentTourID);
	}
	
	public TournamentTournamentTourPK() {
		super();
	}
	
	public void setTournamentID(Object tournamentID) {
		setPrimaryKeyValue(TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_ID, tournamentID);
	}

	public Object getTournamentID() {
		return getPrimaryKeyValue(TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_ID);
	}

	public void setTournamentTourID(Object tournamentTourID) {
		setPrimaryKeyValue(TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_TOUR_ID, tournamentTourID);
	}

	public Object getTournamentTourID() {
		return getPrimaryKeyValue(TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_TOUR_ID);
	}	
}
