package is.idega.idegaweb.golf.entity;

import is.idega.idegaweb.golf.block.image.data.ImageEntity;


public interface FieldImage extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public int getFieldId();
 public int getImageID();
 public void setFieldId(int p0);
 public void setImageID(int p0);
 public void setImageID(Integer p0);
 public ImageEntity getOldImage();
}
