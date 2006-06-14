package is.idega.idegaweb.golf.entity;


import com.idega.data.IDOEntity;

public interface TournamentTourScoreSystem extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourScoreSystemBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourScoreSystemBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourScoreSystemBMPBean#setDefaultPoints
	 */
	public void setDefaultPoints(int points);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourScoreSystemBMPBean#getDefaultPoints
	 */
	public int getDefaultPoints();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourScoreSystemBMPBean#setPointsDivision
	 */
	public void setPointsDivision(float[] pointsDivision);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourScoreSystemBMPBean#getPointsDivision
	 */
	public float[] getPointsDivision();
}