/*
 * StrokePrimaryKey.java, 8.10.2003 21:54:03 - laddi
 * 
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 * 
 */
package is.idega.idegaweb.golf.handicap.data;

import com.idega.data.PrimaryKey;

/**
 * @author laddi
 */
public class StrokesKey extends PrimaryKey {

	private String COLUMN_SCORECARD = StrokesBMPBean.COLUMN_SCORECARD_ID;
	private String COLUMN_HOLE = StrokesBMPBean.COLUMN_HOLE_ID;
	
	/**
	 * @param scorecardID
	 * @param holeID
	 */
	public StrokesKey(Object scorecardID, Object holeID) {
		this();
		setScorecard(scorecardID);
		setHole(holeID);
	}
	
	public StrokesKey() {
		super();
	}
	
	public void setScorecard(Object scorecardID) {
		setPrimaryKeyValue(COLUMN_SCORECARD, scorecardID);
	}

	public Object getScorecard() {
		return getPrimaryKeyValue(COLUMN_SCORECARD);
	}

	public void setHole(Object holeID) {
		setPrimaryKeyValue(COLUMN_HOLE, holeID);
	}

	public Object getHole() {
		return getPrimaryKeyValue(COLUMN_HOLE);
	}
}