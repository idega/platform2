package is.idega.idegaweb.member.data;


public interface UserTitles extends com.idega.data.IDOEntity
{
 public boolean getIsBoardTitle();
 public java.lang.String getLocalizedTitleKey();
 public void initializeAttributes();
 public void setIsBoardTitle(boolean p0);
 public void setLocalizedTitleKey(java.lang.String p0);
}
