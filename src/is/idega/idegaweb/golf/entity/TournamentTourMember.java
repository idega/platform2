package is.idega.idegaweb.golf.entity;


import com.idega.data.IDOEntity;

public interface TournamentTourMember extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#setTournamentTourID
	 */
	public void setTournamentTourID(Object id);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#getTournamentTour
	 */
	public TournamentTour getTournamentTour();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#setTournamentID
	 */
	public void setTournamentID(Object id);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#getTournament
	 */
	public Tournament getTournament();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#setTournamentGroupID
	 */
	public void setTournamentGroupID(Object id);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#getTournamentGroup
	 */
	public TournamentGroup getTournamentGroup();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#setMemberID
	 */
	public void setMemberID(Object id);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#getMember
	 */
	public Member getMember();

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#setScore
	 */
	public void setScore(float score);

	/**
	 * @see is.idega.idegaweb.golf.entity.TournamentTourMemberBMPBean#getScore
	 */
	public float getScore();
}