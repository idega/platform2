package is.idega.idegaweb.golf.handicap.data;


public interface Strokes extends com.idega.data.IDOEntity
{
 public boolean getGreenInRegulation();
 public boolean getHitFairway();
 public is.idega.idegaweb.golf.course.data.Hole getHole();
 public int getHoleID();
 public int getPoints();
 public int getPutts();
 public is.idega.idegaweb.golf.handicap.data.Scorecard getScorecard();
 public int getScorecardID();
 public int getStrokes();
 public void initializeAttributes();
 public void setGreenInRegulation(boolean p0);
 public void setHitFairway(boolean p0);
 public void setHole(is.idega.idegaweb.golf.course.data.Hole p0);
 public void setHoleID(int p0);
 public void setPoints(int p0);
 public void setPutts(int p0);
 public void setScorecard(is.idega.idegaweb.golf.handicap.data.Scorecard p0);
 public void setScorecardID(int p0);
 public void setStrokes(int p0);
}
