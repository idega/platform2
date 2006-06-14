package is.idega.idegaweb.golf.entity;


import com.idega.data.IDOEntity;

public interface TournamentTournamentTour extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTournamentTourBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();
	public void setTournamentID(Object tournamentID);
	public int getTournamentID();
	public void setTournamentTourID(Object tournamentTourID) ;
	public int getTournamentTourID() ;
	public void setTotalScore(int score);
	public int getTotalScore() ;
}