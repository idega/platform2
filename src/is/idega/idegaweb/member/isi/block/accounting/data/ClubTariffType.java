package is.idega.idegaweb.member.isi.block.accounting.data;


public interface ClubTariffType extends com.idega.data.IDOEntity
{
 public com.idega.user.data.Group getClub();
 public int getClubId();
 public boolean getDeleted();
 public java.lang.String getLocalizedKey();
 public java.lang.String getName();
 public java.lang.String getTariffType();
 public void setClub(com.idega.user.data.Group p0);
 public void setClubId(int p0);
 public void setDeleted(boolean p0);
 public void setLocalizedKey(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setTariffType(java.lang.String p0);
}
