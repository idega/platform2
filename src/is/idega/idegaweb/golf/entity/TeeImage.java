package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface TeeImage extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public int getFieldId();
 public int getHoleNumber();
 public int getImageId();
 public void setFieldId(int p0);
 public void setHoleNumber(int p0);
 public void setImageId(int p0);
}
