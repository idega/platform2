package is.idega.idegaweb.golf.block.poll.data;


public interface PollOption extends com.idega.data.IDOLegacyEntity
{
 public int findNumberOfResponses()throws java.sql.SQLException;
 public java.lang.String getAnswer();
 public java.lang.String getName();
 public is.idega.idegaweb.golf.block.poll.data.Poll getPoll();
 public int getPollID();
 public void initializeAttributes();
 public void setAnswer(java.lang.String p0);
 public void setPoll(is.idega.idegaweb.golf.block.poll.data.Poll p0);
 public void setPollID(int p0);
}
