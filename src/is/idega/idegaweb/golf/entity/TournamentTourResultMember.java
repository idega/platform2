package is.idega.idegaweb.golf.entity;

public class TournamentTourResultMember {

	private float score = 0;
	private int memberID = -1;
	
	public void setScore(float score) {
		this.score = score;
	}
	
	public float getScore() {
		return score;
	}
	
	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}
	
	public int getMemberID() {
		return memberID;
	}
}
