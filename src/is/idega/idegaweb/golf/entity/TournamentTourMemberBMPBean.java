package is.idega.idegaweb.golf.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.SimpleQuerier;

public class TournamentTourMemberBMPBean extends GenericEntity implements TournamentTourMember {

	static final String TOURNAMENT_TOUR_ID = "TOURNAMENT_TOUR_ID";
	static final String TOURNAMENT_ID = "TOURNAMENT_ID";
	static final String MEMBER_ID = "MEMBER_ID";
	static final String TOURNAMENT_GROUP_ID = "TOURNAMENT_GROUP_ID";
	private static final String SCORE ="SCORE";
	
	public String getEntityName() {
		return "TOURNAMENT_TOUR_MEMBER";
	}
	public Object ejbFindByPrimaryKey(TournamentTourMemberPK primaryKey) throws FinderException {
		return super.ejbFindByPrimaryKey(primaryKey);
	}

	public Object ejbFindByPrimaryKey(Object primaryKey) throws FinderException {
		return this.ejbFindByPrimaryKey((TournamentTourMemberPK) primaryKey);
	}

	public Object ejbCreate(TournamentTourMemberPK primaryKey) throws CreateException {
		setPrimaryKey(primaryKey);
		return super.ejbCreate();
	}

	public Class getPrimaryKeyClass() {
		return TournamentTourMemberPK.class;
	}

	protected boolean doInsertInCreate() {
		return true;
	}
	
	public void initializeAttributes() {
		addManyToOneRelationship(TOURNAMENT_TOUR_ID, TournamentTour.class);
		addManyToOneRelationship(TOURNAMENT_ID, Tournament.class);
		addManyToOneRelationship(TOURNAMENT_GROUP_ID, TournamentGroup.class);
		addManyToOneRelationship(MEMBER_ID, Member.class);
		
		setAsPrimaryKey(TOURNAMENT_TOUR_ID, true);
		setAsPrimaryKey(TOURNAMENT_ID, true);
		setAsPrimaryKey(MEMBER_ID, true);
		setAsPrimaryKey(TOURNAMENT_GROUP_ID, true);
		
		addAttribute(SCORE, "score", Float.class);
	}
	
	public void setTournamentTourID(Object id) {
		setColumn(TOURNAMENT_TOUR_ID, id);
	}
	
	public TournamentTour getTournamentTour() {
		return (TournamentTour) getColumnValue(TOURNAMENT_TOUR_ID);
	}

	public void setTournamentID(Object id) {
		setColumn(TOURNAMENT_ID, id);
	}
	
	public Tournament getTournament() {
		return (Tournament) getColumnValue(TOURNAMENT_ID);
	}

	public void setTournamentGroupID(Object id) {
		setColumn(TOURNAMENT_GROUP_ID, id);
	}
	
	public TournamentGroup getTournamentGroup() {
		return (TournamentGroup) getColumnValue(TOURNAMENT_GROUP_ID);
	}

	public void setMemberID(Object id) {
		setColumn(MEMBER_ID, id);
	}
	
	public Member getMember() {
		return (Member) getColumnValue(MEMBER_ID);
	}
	
	public void setScore(float score) {
		setColumn(SCORE, score);
	}
	
	public float getScore() {
		return getFloatColumnValue(SCORE);
	}
	
	public int[] ejbHomeGetTournamentGroupsInUse(TournamentTour tour) throws FinderException {
		StringBuffer sql = new StringBuffer("select distinct tournament_group_id from tournament_tour_member where tournament_tour_id =")
		.append(tour.getPrimaryKey().toString());
		try {
			String[] ids = SimpleQuerier.executeStringQuery(sql.toString());
			if (ids != null) {
				int[] iIds = new int[ids.length];
				for (int i = 0; i < ids.length; i++) {
					iIds[i] = Integer.parseInt(ids[i]);
				}
				return iIds;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinderException(e.getMessage());
		}
		return null;
	}

	/**
	 * @return A collection of TournamentTourResultMember
	 */
	public Collection ejbHomeGetScoresOrdered(TournamentTour tour, Collection tournamentPKs, Collection tournamentGroupPKs) throws FinderException {
		StringBuffer sql = new StringBuffer("select member_id, sum(score) as total_score from tournament_tour_member where tournament_tour_id = ")
		.append(tour.getPrimaryKey().toString());
		if (tournamentPKs != null && !tournamentPKs.isEmpty()) {
			sql.append(" AND tournament_id in (");
			Iterator iter = tournamentPKs.iterator();
			while (iter.hasNext()) {
				sql.append(iter.next().toString());
				if (iter.hasNext()) {
					sql.append(",");
				}
			}
			sql.append(")");
		}
		if (tournamentGroupPKs != null && !tournamentGroupPKs.isEmpty()) {
			sql.append(" AND tournament_group_id in (");
			Iterator iter = tournamentGroupPKs.iterator();
			while (iter.hasNext()) {
				sql.append(iter.next().toString());
				if (iter.hasNext()) {
					sql.append(",");
				}
			}
			sql.append(")");
		}

		sql.append(" group by member_id order by total_score desc");
		
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		Collection coll = new Vector();
		try {
			conn = super.getConnection();
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql.toString());
			
			while (rs.next()) {
				TournamentTourResultMember m = new TournamentTourResultMember();
				m.setMemberID(rs.getInt("member_id"));
				m.setScore(rs.getFloat("total_score"));
				coll.add(m);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FinderException(e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new FinderException("Cannot close ResultSet");
				}
			}
			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new FinderException("Cannot close Statement");
				}
			}
			
			if (conn != null) {
				super.freeConnection(conn);
			}
		}
		
		return coll;
	}

}
