package is.idega.idegaweb.golf.entity;


public interface Stroke extends com.idega.data.IDOLegacyEntity
{
	public static final String COLUMN_SCORECARD = "scorecard_id";
	public static final String COLUMN_POINT_COUNT = "point_count";
	public static final String COLUMN_TEE = "tee_id";
	public static final String COLUMN_STROKE_COUNT = "stroke_count";
	public static final String COLUMN_HOLE_PAR = "hole_par";
	public static final String COLUMN_HOLE_HANDICAP = "hole_handicap";
	public static final String TABLE_NAME = "stroke";
	
 public int getHoleHandicap();
 public int getHolePar();
 public int getPointCount();
 public int getScorecardID();
 public int getStrokeCount();
 public int getTeeID();
 public void setHoleHandicap(int p0);
 public void setHolePar(int p0);
 public void setPointCount(int p0);
 public void setScorecardID(int p0);
 public void setStrokeCount(int p0);
 public void setTeeID(int p0);
}
