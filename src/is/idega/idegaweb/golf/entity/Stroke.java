package is.idega.idegaweb.golf.entity;


public interface Stroke extends is.idega.idegaweb.golf.entity.GolfEntity
{
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
