package is.idega.idegaweb.golf.entity;


import com.idega.data.IDOEntity;

public interface TournamentTour extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourBMPBean#setUnionId
	 */
	public void setUnionId(int id);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourBMPBean#getUnion
	 */
	public Union getUnion();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourBMPBean#getUnionID
	 */
	public int getUnionID();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourBMPBean#setScoreSystem
	 */
	public void setScoreSystem(int tournamentTourScoreSystemID);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourBMPBean#getScoreSystem
	 */
	public TournamentTourScoreSystem getScoreSystem();
}