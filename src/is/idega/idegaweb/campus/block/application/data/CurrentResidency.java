package is.idega.idegaweb.campus.block.application.data;


public interface CurrentResidency extends com.idega.data.IDOEntity
{
 public java.lang.String getDescription();
 public java.lang.String getDescriptionColumnName();
 public java.lang.String getName();
 public boolean getRequiresExtraInfo();
 public java.lang.String getRequiresExtranInfoColumnName();
 public void initializeAttributes();
 public void setDescription(java.lang.String p0);
 public void setRequiresExtranInfo(boolean p0);
}
