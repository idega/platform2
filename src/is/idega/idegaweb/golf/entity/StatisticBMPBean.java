//idega 2000 - Tryggvi Larusson

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.query.AverageColumn;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.SumColumn;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

public class StatisticBMPBean extends GenericEntity implements Statistic{
	
	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_SCORECARD_ID, "Skorkort", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Scorecard");
		addAttribute(COLUMN_TEE_ID, "Teigur", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Tee");
		addAttribute(COLUMN_FAIRWAY, "Á braut", true, true, "java.lang.Integer");
		addAttribute(COLUMN_GREENS, "Á flöt", true, true, "java.lang.Integer");
		addAttribute(COLUMN_PUTTS, "Pútt", true, true, "java.lang.Integer");
		addAttribute(COLUMN_PUTTS_FLOAT, "puttfloat", true, true, Float.class);
	}
	

	public String getEntityName(){
		return TABLE_NAME;
	}

	public int getScorecardID() {
		return getIntColumnValue(COLUMN_SCORECARD_ID);
	}

	public int getTeeID() {
		return getIntColumnValue(COLUMN_TEE_ID);
	}

	public int getFairway(){
		return getIntColumnValue(COLUMN_FAIRWAY);
	}

	public int getGreens(){
		return getIntColumnValue(COLUMN_GREENS);
	}

	public int getPutts(){
		return getIntColumnValue(COLUMN_PUTTS);
	}

	public void setScorecardID(int scorecard_id) {
		setColumn(COLUMN_SCORECARD_ID,scorecard_id);
	}

	public void setTeeID(int tee_id) {
		setColumn(COLUMN_TEE_ID,tee_id);
	}

	public void setFairway(int fairway) {
		setColumn(COLUMN_FAIRWAY,fairway);
	}

	public void setGreens(int greens) {
		setColumn(COLUMN_GREENS,greens);
	}

	public void setPutts(int putts) {
		setColumn(COLUMN_PUTTS,putts);
		setColumn(COLUMN_PUTTS_FLOAT, (float)putts);
	}

	
	public Collection ejbFindByTeeID(Collection teeIDs) throws FinderException{
		Table table = new Table(this);
		Column colTeeID = new Column(table, COLUMN_TEE_ID);
		SelectQuery query = new SelectQuery(table);	
		query.addColumn(new WildCardColumn());
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		System.out.println(query);
		return this.idoFindPKsBySQL(query.toString());
		
	}
	
	
	public int ejbHomeGetNumberOnFairwayByTeeID(Collection teeIDs) throws IDOException {
		if (teeIDs == null || teeIDs.isEmpty()) {
			return 0;
		}

		Table table = new Table(this);
		Column colTeeID = new Column(table, COLUMN_TEE_ID);
		Column colFairway = new Column(table, COLUMN_FAIRWAY);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		query.addCriteria(new MatchCriteria(colFairway, MatchCriteria.GREATER, 0));
		
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetCountByTeeId(Collection teeIDs) throws IDOException {
		if (teeIDs == null || teeIDs.isEmpty()) {
			return 0;
		}

		Table table = new Table(this);
		Column colTeeID = new Column(table, COLUMN_TEE_ID);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetNumberOnGreenByTeeID(Collection teeIDs) throws IDOException {
		if (teeIDs == null || teeIDs.isEmpty()) {
			return 0;
		}

		Table table = new Table(this);
		Column colTeeID = new Column(table, COLUMN_TEE_ID);
		Column colGreen = new Column(table, COLUMN_GREENS);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		query.addCriteria(new MatchCriteria(colGreen, MatchCriteria.GREATER, 0));
		
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public double ejbHomeGetPuttAverageByTeeID(Collection teeIDs) throws IDOException {
		if (teeIDs == null || teeIDs.isEmpty()) {
			return 0;
		}

		Table table = new Table(this);
		Column colTeeID = new Column(table, COLUMN_TEE_ID);
		AverageColumn colPutts = new AverageColumn(table, COLUMN_PUTTS_FLOAT);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(colPutts);
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		
		return this.idoGetAverage(query.toString());
	}
	
	public double ejbHomeGetPuttAverageByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);
		
		AverageColumn colPutts = new AverageColumn(table, COLUMN_PUTTS_FLOAT);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(colPutts);
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		
		return this.idoGetAverage(query.toString());
	}

	public int ejbHomeGetPuttSumByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);
		
		Column colPutts = new SumColumn(table, COLUMN_PUTTS_FLOAT);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(colPutts);
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		
		return this.idoGetNumberOfRecords(query.toString());
	}

	public int ejbHomeGetCountFairwaysByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, COLUMN_FAIRWAY, MatchCriteria.GREATEREQUAL, 0));
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetCountOnGreenByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, COLUMN_GREENS, MatchCriteria.GREATEREQUAL, 0));
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetNumberOnGreenByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);

		Column colGreen = new Column(table, COLUMN_GREENS);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(colGreen, MatchCriteria.GREATER, 0));
		
		return this.idoGetNumberOfRecords(query.toString());
	}

	public int ejbHomeGetNumberOnFairwayByMember(int member) throws IDOException {
		Table table = new Table(this);
		Table scorecard = new Table(Scorecard.class);

		Column colFairway = new Column(table, COLUMN_FAIRWAY);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, this.getIDColumnName()));
		query.addJoin(table, scorecard);
		query.addCriteria(new MatchCriteria(scorecard, "member_id", MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(colFairway, MatchCriteria.GREATER, 0));
		
		return this.idoGetNumberOfRecords(query.toString());
	}
}