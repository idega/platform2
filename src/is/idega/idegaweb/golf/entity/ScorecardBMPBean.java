// idega 2000 - Ægir

package is.idega.idegaweb.golf.entity;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.SumColumn;
import com.idega.data.query.Table;

public class ScorecardBMPBean extends GenericEntity implements Scorecard {

	private final String YES = "Y";
	private final String NO = "N";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addManyToOneRelationship(COLUMN_MEMBER, Member.class);
		addManyToOneRelationship(COLUMN_TOURNAMENT_ROUND, TournamentRound.class);
		addManyToOneRelationship(COLUMN_FIELD, Field.class);
		addManyToOneRelationship(COLUMN_TEE_COLOR, TeeColor.class);
		
		addAttribute(COLUMN_SCORECARD_DATE, "Date", true, true, Timestamp.class);
		addAttribute(COLUMN_TOTAL_POINTS, "Total points", true, true, Integer.class);
		addAttribute(COLUMN_HANDICAP_BEFORE, "Handicap before", true, true, Float.class);
		addAttribute(COLUMN_HANDICAP_AFTER, "Handicap after", true, true, Float.class);
		addAttribute(COLUMN_SLOPE, "Slope", true, true, Integer.class);
		addAttribute(COLUMN_COURSE_RATING, "Course rating", true, true, Float.class);
		addAttribute(COLUMN_HANDICAP_CORRECTION, "Handicap correction", true, true, Boolean.class);
		addAttribute(COLUMN_UPDATE_HANDICAP, "Update handicap", true, true, Boolean.class);
		addAttribute(COLUMN_FOREIGN_ROUND, "Foreign round", true, true, Boolean.class);
		addAttribute(COLUMN_FOREIGN_COURSE_NAME, "Foreign course name", true, true, "java.lang.String");
		setMaxLength(COLUMN_FOREIGN_ROUND, 1);
		setMaxLength(COLUMN_FOREIGN_COURSE_NAME, 255);
		
		addIndex("IDX_SCORECARD_1", COLUMN_MEMBER);
		addIndex("IDX_SCORECARD_2", new String[] {COLUMN_MEMBER, COLUMN_SCORECARD_DATE});
		addIndex("IDX_SCORECARD_3", new String[] {COLUMN_MEMBER, COLUMN_TOURNAMENT_ROUND, COLUMN_SCORECARD_DATE});
		addIndex("IDX_SCORECARD_4", new String[] {COLUMN_TOURNAMENT_ROUND});
		addIndex("IDX_SCORECARD_5", new String[] {COLUMN_MEMBER, COLUMN_TOURNAMENT_ROUND});
	}

	public String getEntityName() {
		return TABLE_NAME;
	}

	public int getMemberId() {
		return getIntColumnValue(COLUMN_MEMBER);
	}

	public void setMemberId(int memberId) {
		setColumn(COLUMN_MEMBER, memberId);
	}

	public int getTournamentRoundId() {
		return getIntColumnValue(COLUMN_TOURNAMENT_ROUND);
	}

	public void setTournamentRoundId(int tournamentRoundId) {
		setColumn(COLUMN_TOURNAMENT_ROUND, tournamentRoundId);
	}

	public TournamentRound getTournamentRound() {
		return (TournamentRound) getColumnValue(COLUMN_TOURNAMENT_ROUND);
	}

	public void setTournamentRound(TournamentRound tournamentRound) {
		setColumn(COLUMN_TOURNAMENT_ROUND, tournamentRound);
	}

	public Timestamp getScorecardDate() {
		return (Timestamp) getColumnValue(COLUMN_SCORECARD_DATE);
	}

	public void setScorecardDate(Timestamp scorecardDate) {
		setColumn(COLUMN_SCORECARD_DATE, scorecardDate);
	}

	public int getTotalPoints() {
		return getIntColumnValue(COLUMN_TOTAL_POINTS);
	}

	public void setTotalPoints(int totalPoints) {
		setColumn(COLUMN_TOTAL_POINTS, totalPoints);
	}

	public float getHandicapBefore() {
		return getFloatColumnValue(COLUMN_HANDICAP_BEFORE);
	}

	public void setHandicapBefore(float handicapBefore) {
		setColumn(COLUMN_HANDICAP_BEFORE, handicapBefore);
	}

	public float getHandicapAfter() {
		return getFloatColumnValue(COLUMN_HANDICAP_AFTER);
	}

	public void setHandicapAfter(float handicapAfter) {
		setColumn(COLUMN_HANDICAP_AFTER, handicapAfter);
	}

	public void setMember(Member member) {
		setColumn(COLUMN_MEMBER, member);
	}

	public Member getMember() {
		return (Member) getColumnValue(COLUMN_MEMBER);
	}

	public int getSlope() {
		return getIntColumnValue(COLUMN_SLOPE);
	}

	public void setSlope(int slope) {
		setColumn(COLUMN_SLOPE, slope);
	}

	public float getCourseRating() {
		return getFloatColumnValue(COLUMN_COURSE_RATING);
	}

	public void setCourseRating(float courseRating) {
		setColumn(COLUMN_COURSE_RATING, courseRating);
	}

	public int getFieldID() {
		return getIntColumnValue(COLUMN_FIELD);
	}
	
	public Field getField() {
		return (Field) getColumnValue(COLUMN_FIELD);
	}

	public void setFieldID(int fieldID) {
		setColumn(COLUMN_FIELD, fieldID);
	}

	public int getTeeColorID() {
		return getIntColumnValue(COLUMN_TEE_COLOR);
	}

	public void setTeeColorID(int teeColorID) {
		setColumn(COLUMN_TEE_COLOR, teeColorID);
	}

	public boolean getHandicapCorrection() {
		return getBooleanColumnValue(COLUMN_HANDICAP_CORRECTION, false);
	}

	public void setHandicapCorrection(boolean correction) {
		setColumn(COLUMN_HANDICAP_CORRECTION, correction);
	}

	public boolean getUpdateHandicap() {
		return getBooleanColumnValue(COLUMN_UPDATE_HANDICAP, false);
	}

	public void setUpdateHandicap(boolean handicap) {
		setColumn(COLUMN_UPDATE_HANDICAP, handicap);
	}

	public void delete() throws SQLException {
		((Stroke) IDOLookup.instanciateEntity(Stroke.class)).deleteMultiple("scorecard_id", Integer.toString(this.getID()));
		((Statistic) IDOLookup.instanciateEntity(Statistic.class)).deleteMultiple("scorecard_id", Integer.toString(this.getID()));

		super.delete();
	}

	public void setForeignRound(boolean foreign) {
		setColumn(COLUMN_FOREIGN_ROUND, foreign);
	}

	public boolean getForeignRound() {
		return getBooleanColumnValue(COLUMN_FOREIGN_ROUND, false);
	}

	public void setForeignCourseName(String name) {
		setColumn(COLUMN_FOREIGN_COURSE_NAME, name);
	}

	public String getForeignCourseName() {
		return this.getStringColumnValue(COLUMN_FOREIGN_COURSE_NAME);
	}

	public int ejbHomeGetCountRoundsPlayedByMember(int member) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		Column column = new Column(table, COLUMN_SCORECARD_DATE);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, COLUMN_MEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, COLUMN_HANDICAP_CORRECTION, MatchCriteria.EQUALS, false));
		query.addCriteria(new MatchCriteria(column, true));
		return this.idoGetNumberOfRecords(query.toString());
	}

	public int ejbHomeGetSumPointsByMember(int member) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		Column column = new Column(table, COLUMN_SCORECARD_DATE);
		query.addColumn(new SumColumn(table, COLUMN_TOTAL_POINTS));
		query.addCriteria(new MatchCriteria(table, COLUMN_MEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(column, true));
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetNumberOfRoundsAfterDateByMember(int member, Date scorecardDate) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, COLUMN_MEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, COLUMN_SCORECARD_DATE, MatchCriteria.GREATEREQUAL, scorecardDate));
		query.addCriteria(new MatchCriteria(table, COLUMN_HANDICAP_CORRECTION, MatchCriteria.EQUALS, false));
		return this.idoGetNumberOfRecords(query.toString());
	}

	public Integer ejbFindBestRoundAfterDateByMember(int member, Date scorecardDate) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_MEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, COLUMN_SCORECARD_DATE, MatchCriteria.GREATEREQUAL, scorecardDate));
		query.addOrder(table, COLUMN_TOTAL_POINTS, false);
		return (Integer) this.idoFindOnePKBySQL(query.toString());
	}

	public Integer ejbFindLastPlayedRoundByMember(int member) throws FinderException {
		Table table = new Table(this);
		Column column = new Column(table, COLUMN_SCORECARD_DATE);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_MEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(column, true));
		query.addOrder(new Order(column, false));
		return (Integer) this.idoFindOnePKBySQL(query.toString());
	}
}