package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportImportClubAccountRecord extends com.idega.data.IDOEntity
{
 public int getAccountKeyId();
 public float getAmount();
 public int getReportId();
 public int getWorkReportGroupId();
 public void initializeAttributes();
 public void setAccountKey(is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKey p0);
 public void setAccountKeyId(int p0);
 public void setAmount(float p0);
 public void setReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0);
 public void setReportId(int p0);
 public void setWorkReportGroup(is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p0);
 public void setWorkReportGroupId(int p0);
}
