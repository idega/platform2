package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportExportFile extends com.idega.data.IDOEntity
{
 public com.idega.core.file.data.ICFile getFile();
 public int getFileId();
 public com.idega.user.data.Group getGroup();
 public int getGroupId();
 public int getYear();
 public void initializeAttributes();
 public void setFile(com.idega.core.file.data.ICFile p0);
 public void setFileId(int p0);
 public void setGroup(com.idega.user.data.Group p0);
 public void setGroupId(int p0);
 public void setYear(int p0);
}
