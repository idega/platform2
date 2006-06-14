package is.idega.idegaweb.golf.entity;

import java.util.Collection;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.SimpleQuerier;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class TournamentTourBMPBean extends GenericEntity implements TournamentTour{

	private static final String ENTITY_NAME = "TOURNAMENT_TOUR";
	private static final String COLUMN_NAME = "NAME";
	private static final String COLUMN_UNION_ID = "UNION_ID";
	private static final String COLUMN_SCORE_SYSTEM = "TOURNAMENT_TOUR_SCORESYSTEM_ID";
	
	public String getEntityName() {
		return TournamentTourBMPBean.ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", String.class);
		addManyToOneRelationship(COLUMN_UNION_ID, Union.class);
		addManyToOneRelationship(COLUMN_SCORE_SYSTEM, TournamentTourScoreSystem.class);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public void setUnionId(int id) {
		setColumn(COLUMN_UNION_ID, id);
	}
	
	public Union getUnion() {
		return (Union) getColumnValue(COLUMN_UNION_ID);
	}
	
	public int getUnionID() {
		return getIntColumnValue(COLUMN_UNION_ID);
	}
	
	public void setScoreSystem(int tournamentTourScoreSystemID) {
		setColumn(COLUMN_SCORE_SYSTEM, tournamentTourScoreSystemID);
	}
	
	public TournamentTourScoreSystem getScoreSystem() {
		return (TournamentTourScoreSystem) getColumnValue(COLUMN_SCORE_SYSTEM);
	}
	
	public Collection ejbFindAllByUnionID(int unionID) throws FinderException {
		Table table = new Table(this);
		Column col = new Column(table, getIDColumnName());
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(col);
		if (unionID != 1 && unionID != 3) {
			// HACK OF DEATH
			query.addCriteria(new MatchCriteria(new Column(table, COLUMN_UNION_ID), MatchCriteria.EQUALS, unionID));
		}
		return this.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByTournamentID(Object tournamentID) throws FinderException, IDORelationshipException {
		Table table = new Table(this);
		Table mTable = new Table(TournamentTournamentTour.class);
		Column cTournamentID = new Column(mTable, TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_ID);
		Column cTourID = new Column(mTable, TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_TOUR_ID);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addJoin(mTable, table);
		query.addCriteria(new MatchCriteria(cTournamentID, MatchCriteria.EQUALS, tournamentID));
		return this.idoFindPKsByQuery(query);
	}
	
	public Collection ejbHomeGetTournamentIDs(TournamentTour tour) throws FinderException {
//		Table table = new Table(TournamentTournamentTour.class);
//		Column tournamentID = new Column(table, TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_ID);
//		Column tourID = new Column(table, TournamentTournamentTourBMPBean.COLUMN_TOURNAMENT_TOUR_ID);
//		
//		SelectQuery query = new SelectQuery(table);
//		query.addColumn(tournamentID);
//		query.addCriteria(new MatchCriteria(tourID, MatchCriteria.EQUALS, tour));
		// TMP 'fix'
		try {
			String sql = "select tt.tournament_id from tournament_tournament_tour tt, tournament t where t.tournament_id = tt.tournament_id and tt.tournament_tour_id = "+tour.getPrimaryKey().toString()+" order by t.start_time";
			String[] ids = SimpleQuerier.executeStringQuery(sql);
			Collection coll = new Vector();
			for (int i = 0; i < ids.length; i++) {
				coll.add(new Integer(ids[i]));
			}
			return coll;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinderException(e.getMessage());
		}
//		return idoFindPKsByQuery(query);
	}
}
