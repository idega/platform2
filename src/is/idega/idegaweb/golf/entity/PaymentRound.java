package is.idega.idegaweb.golf.entity;


public interface PaymentRound extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getName();
 public java.sql.Timestamp getRoundDate();
 public int getTotals();
 public int getUnionId();
 public void setName(java.lang.String p0);
 public void setRoundDate(java.sql.Timestamp p0);
 public void setTotals(int p0);
 public void setTotals(java.lang.Integer p0);
 public void setUnionId(int p0);
 public void setUnionId(java.lang.Integer p0);
}
