package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface Poll_option extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public void delete()throws java.sql.SQLException;
 public int findNumberOfResponses()throws java.sql.SQLException;
 public java.lang.String getAnswer();
 public java.lang.String getName();
 public is.idega.idegaweb.golf.entity.Poll getPoll();
 public int getPollID();
 public void setAnswer(java.lang.String p0);
 public void setPoll(is.idega.idegaweb.golf.entity.Poll p0);
 public void setPollID(int p0);
}
