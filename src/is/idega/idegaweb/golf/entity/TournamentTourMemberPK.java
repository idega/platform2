package is.idega.idegaweb.golf.entity;

import com.idega.data.PrimaryKey;

public class TournamentTourMemberPK extends PrimaryKey {

	public TournamentTourMemberPK(Object tournamentID, Object tournamentTourID, Object tournamentGroupID, Object memberID) {
		super();
		setTournamentID(tournamentID);
		setTournamentTourID(tournamentTourID);
		setTournamentGroupID(tournamentGroupID);
		setMemberID(memberID);
	}
	
	public TournamentTourMemberPK() {
		super();
	}
	
	public void setTournamentID(Object tournamentID) {
		setPrimaryKeyValue(TournamentTourMemberBMPBean.TOURNAMENT_ID, tournamentID);
	}

	public Object getTournamentID() {
		return getPrimaryKeyValue(TournamentTourMemberBMPBean.TOURNAMENT_ID);
	}

	public void setTournamentTourID(Object tournamentTourID) {
		setPrimaryKeyValue(TournamentTourMemberBMPBean.TOURNAMENT_TOUR_ID, tournamentTourID);
	}

	public Object getTournamentTourID() {
		return getPrimaryKeyValue(TournamentTourMemberBMPBean.TOURNAMENT_TOUR_ID);
	}

	public void setTournamentGroupID(Object tournamentGroupID) {
		setPrimaryKeyValue(TournamentTourMemberBMPBean.TOURNAMENT_GROUP_ID, tournamentGroupID);
	}

	public Object getTournamentGroupID() {
		return getPrimaryKeyValue(TournamentTourMemberBMPBean.TOURNAMENT_GROUP_ID);
	}
	
	public void setMemberID(Object memberID) {
		setPrimaryKeyValue(TournamentTourMemberBMPBean.MEMBER_ID, memberID);
	}
	
	public Object getMemberID() {
		return getPrimaryKeyValue(TournamentTourMemberBMPBean.MEMBER_ID);
	}
	
}
