package is.idega.idegaweb.golf.entity;


public interface PriceCatalogue extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getExtraInfo();
 public java.lang.String getName();
 public int getPrice();
 public int getUnionId();
 public boolean isInUse();
 public boolean isIndependent();
 public void setExtraInfo(java.lang.String p0);
 public void setInUse(boolean p0);
 public void setIndependent(boolean p0);
 public void setName(java.lang.String p0);
 public void setPrice(java.lang.Integer p0);
 public void setPrice(int p0);
 public void setUnion_id(java.lang.Integer p0);
 public void setUnion_id(int p0);
}
