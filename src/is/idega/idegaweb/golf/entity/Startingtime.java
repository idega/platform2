package is.idega.idegaweb.golf.entity;


public interface Startingtime extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getCardName();
 public java.lang.String getCardNum();
 public java.lang.String getClubName();
 public int getFieldID();
 public int getGroupNum();
 public float getHandicap();
 public is.idega.idegaweb.golf.entity.Member getMember()throws java.sql.SQLException;
 public int getMemberID();
 public int getOwnerID();
 public java.lang.String getPlayerName();
 public java.sql.Date getStartingtimeDate();
 public int getTeeNumber();
 public void setCardName(java.lang.String p0);
 public void setCardNum(java.lang.String p0);
 public void setClubName(java.lang.String p0);
 public void setDefaultValues();
 public void setFieldID(java.lang.Integer p0);
 public void setFieldID(int p0);
 public void setGroupNum(java.lang.Integer p0);
 public void setGroupNum(int p0);
 public void setHandicap(java.lang.Float p0);
 public void setHandicap(float p0);
 public void setMemberID(int p0);
 public void setMemberID(java.lang.Integer p0);
 public void setOwnerID(int p0);
 public void setOwnerID(java.lang.Integer p0);
 public void setPlayerName(java.lang.String p0);
 public void setStartingtimeDate(java.sql.Date p0);
 public void setTeeNumber(int p0);
}
