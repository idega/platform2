package is.idega.idegaweb.golf.entity;


public interface Statistic extends com.idega.data.IDOLegacyEntity
{
	public final static String COLUMN_SCORECARD_ID = "scorecard_id";
	public final static String COLUMN_TEE_ID = "tee_id";
	public final static String COLUMN_FAIRWAY = "fairway";
	public final static String COLUMN_GREENS = "greens";
	public final static String COLUMN_PUTTS = "putts";
	public final static String COLUMN_PUTTS_FLOAT ="putts_float";
	public final static String TABLE_NAME ="statistic";
	
 public int getFairway();
 public int getGreens();
 public int getPutts();
 public int getScorecardID();
 public int getTeeID();
 public void setFairway(int p0);
 public void setGreens(int p0);
 public void setPutts(int p0);
 public void setScorecardID(int p0);
 public void setTeeID(int p0);
}
