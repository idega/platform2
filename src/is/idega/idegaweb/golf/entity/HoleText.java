package is.idega.idegaweb.golf.entity;

import is.idega.idegaweb.golf.block.text.data.TextModule;


public interface HoleText extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public int getFieldId();
 public int getHoleNumber();
 public int getTextID();
 public TextModule getOldText();
 public void setFieldId(int p0);
 public void setHoleNumber(int p0);
 public void setTextID(int p0);
}
