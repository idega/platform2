package is.idega.idegaweb.golf.block.login.data;


public interface LoginTable extends com.idega.data.IDOLegacyEntity
{
 public is.idega.idegaweb.golf.entity.Member getMember();
 public int getMemberId();
 public java.lang.String getUserLogin();
 public java.lang.String getUserPassword();
 public void setMemberId(java.lang.Integer p0);
 public void setMemberId(int p0);
 public void setUserLogin(java.lang.String p0);
 public void setUserPassword(java.lang.String p0);
}
