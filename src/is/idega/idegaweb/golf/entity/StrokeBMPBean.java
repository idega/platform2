//idega 2000 - Tryggvi Larusson

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.SumColumn;
import com.idega.data.query.Table;

public class StrokeBMPBean extends GenericEntity implements Stroke{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_SCORECARD, "Skorkort", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Scorecard");
		addAttribute(COLUMN_POINT_COUNT, "Punktafjöldi", true, true, "java.lang.Integer");
		addAttribute(COLUMN_TEE, "Teigur", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Tee");
		addAttribute(COLUMN_STROKE_COUNT, "Höggafjöldi", true, true, "java.lang.Integer");
		addAttribute(COLUMN_HOLE_PAR, "Par holu", true, true, "java.lang.Integer");
		addAttribute(COLUMN_HOLE_HANDICAP, "Forgjöf holu", true, true, "java.lang.Integer");

	}

	public String getEntityName(){
		return TABLE_NAME;
	}

	public int getScorecardID(){
		return getIntColumnValue(COLUMN_SCORECARD);
	}

	public void setScorecardID(int scorecard_id) {
		setColumn(COLUMN_SCORECARD,scorecard_id);
	}

	public int getPointCount(){
		return getIntColumnValue(COLUMN_POINT_COUNT);
	}

	public void setPointCount(int point_count) {
		setColumn(COLUMN_POINT_COUNT,point_count);
	}

	public int getTeeID() {
		return getIntColumnValue(COLUMN_TEE);
	}

	public void setTeeID(int tee_id) {
		setColumn(COLUMN_TEE,tee_id);
	}

	public int getStrokeCount(){
		return getIntColumnValue(COLUMN_STROKE_COUNT);
	}

	public void setStrokeCount(int stroke_count) {
		setColumn(COLUMN_STROKE_COUNT,stroke_count);
	}

	public int getHolePar(){
		return getIntColumnValue(COLUMN_HOLE_PAR);
	}

	public void setHolePar(int hole_par) {
		setColumn(COLUMN_HOLE_PAR,hole_par);
	}

	public int getHoleHandicap(){
		return getIntColumnValue(COLUMN_HOLE_HANDICAP);
	}

	public void setHoleHandicap(int hole_handicap) {
		setColumn(COLUMN_HOLE_HANDICAP,hole_handicap);
	}
	
	public int ejbHomeGetCountDifferenceByMember(int member, int difference, String criteria) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);

		Column strokes = new Column(table, COLUMN_STROKE_COUNT);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(strokes, criteria, COLUMN_HOLE_PAR + " + (" + difference + ")", false));
		
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetCountOfHolesPlayedByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);

		Column strokes = new Column(table, COLUMN_STROKE_COUNT);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetSumOfStrokesByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);

		Column strokes = new Column(table, COLUMN_STROKE_COUNT);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new SumColumn(table, COLUMN_STROKE_COUNT));
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		
		return this.idoGetNumberOfRecords(query.toString());
	}
}