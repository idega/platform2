//idega 2000 - Tryggvi Larusson

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.SimpleQuerier;
import com.idega.data.query.AverageColumn;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
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
		setColumn(COLUMN_PUTTS_FLOAT, putts);
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
		query.addColumn(new WildCardColumn());
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		query.addCriteria(new MatchCriteria(colFairway, MatchCriteria.GREATER, 0));
		query.setAsCountQuery(true);
		
		return this.idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetCountByTeeId(Collection teeIDs) throws IDOException {
		if (teeIDs == null || teeIDs.isEmpty()) {
			return 0;
		}

		Table table = new Table(this);
		Column colTeeID = new Column(table, COLUMN_TEE_ID);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		query.setAsCountQuery(true);
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
		query.addColumn(new WildCardColumn());
		query.addCriteria(new InCriteria(colTeeID, teeIDs));
		query.addCriteria(new MatchCriteria(colGreen, MatchCriteria.GREATER, 0));
		query.setAsCountQuery(true);
		
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
		
		try {
			String[] returner = SimpleQuerier.executeStringQuery(query.toString());
			if (returner != null && returner.length > 0 && returner[0] != null) {
				return Double.parseDouble(returner[0]);
			}
			else {
				return 0;
			}
		} catch(Exception e) {
			throw new IDOException(e);
		}
		
	}

}
