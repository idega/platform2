package is.idega.idegaweb.golf.entity;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

public class TournamentTournamentTourBMPBean extends GenericEntity implements TournamentTournamentTour{

	static final String COLUMN_TOURNAMENT_ID ="TOURNAMENT_ID";
	static final String COLUMN_TOURNAMENT_TOUR_ID = "TOURNAMENT_TOUR_ID";
	static final String COLUMN_TOTAL_SCORE = "TOTAL_SCORE";


	public String getEntityName() {
		return "TOURNAMENT_TOURNAMENT_TOUR";
	}

	public Object ejbFindByPrimaryKey(TournamentTournamentTourPK primaryKey) throws FinderException {
		return super.ejbFindByPrimaryKey(primaryKey);
	}

	public Object ejbFindByPrimaryKey(Object primaryKey) throws FinderException {
		return this.ejbFindByPrimaryKey((TournamentTournamentTourPK) primaryKey);
	}

	public Object ejbCreate(TournamentTournamentTourPK primaryKey) throws CreateException {
		setPrimaryKey(primaryKey);
		return super.ejbCreate();
	}

	public Class getPrimaryKeyClass() {
		return TournamentTournamentTourPK.class;
	}

	protected boolean doInsertInCreate() {
		return true;
	}

	public void initializeAttributes() {
		addManyToOneRelationship(COLUMN_TOURNAMENT_ID, Tournament.class);
		addManyToOneRelationship(COLUMN_TOURNAMENT_TOUR_ID, TournamentTour.class);
		addAttribute(COLUMN_TOTAL_SCORE, "total score", Integer.class);
		
		setAsPrimaryKey(COLUMN_TOURNAMENT_ID, true);
		setAsPrimaryKey(COLUMN_TOURNAMENT_TOUR_ID, true);
	}
	
	public void setTournamentID(Object tournamentID) {
		setColumn(COLUMN_TOURNAMENT_ID, tournamentID);
	}
	
	public int getTournamentID() {
		return getIntColumnValue(COLUMN_TOURNAMENT_ID);
	}

	public void setTournamentTourID(Object tournamentTourID) {
		setColumn(COLUMN_TOURNAMENT_TOUR_ID, tournamentTourID);
	}
	
	public int getTournamentTourID() {
		return getIntColumnValue(COLUMN_TOURNAMENT_TOUR_ID);
	}

	public void setTotalScore(int score) {
		setColumn(COLUMN_TOTAL_SCORE, score);
	}
	
	public int getTotalScore() {
		return getIntColumnValue(COLUMN_TOTAL_SCORE);
	}
}
