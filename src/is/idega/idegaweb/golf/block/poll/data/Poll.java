package is.idega.idegaweb.golf.block.poll.data;


public interface Poll extends com.idega.data.IDOLegacyEntity
{
 public is.idega.idegaweb.golf.block.poll.data.PollOption[] findOptions()throws java.sql.SQLException;
 public java.sql.Timestamp getEndTime();
 public boolean getInUse();
 public java.lang.String getName();
 public java.lang.String getQuestion();
 public java.sql.Timestamp getStartTime();
 public void initializeAttributes();
 public void setEndTime(java.sql.Timestamp p0);
 public void setInUse(boolean p0);
 public void setQuestion(java.lang.String p0);
 public void setStartTime(java.sql.Timestamp p0);
}
