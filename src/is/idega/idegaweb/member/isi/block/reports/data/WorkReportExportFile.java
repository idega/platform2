package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportExportFile extends com.idega.data.IDOEntity
{
 public com.idega.user.data.Group getClub();
 public int getClubId();
 public com.idega.core.file.data.ICFile getFile();
 public int getFileId();
 public com.idega.user.data.Group getUnion();
 public int getUnionId();
 public int getYear();
 public void initializeAttributes();
 public void setClub(com.idega.user.data.Group p0);
 public void setClubId(int p0);
 public void setFile(com.idega.core.file.data.ICFile p0);
 public void setFileId(int p0);
 public void setUnion(com.idega.user.data.Group p0);
 public void setUnionId(int p0);
 public void setYear(int p0);
}
