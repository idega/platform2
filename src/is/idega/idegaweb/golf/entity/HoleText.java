package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface HoleText extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public int getFieldId();
 public int getHoleNumber();
 public int getTextId();
 public void setFieldId(int p0);
 public void setHoleNumber(int p0);
 public void setTextId(int p0);
}
