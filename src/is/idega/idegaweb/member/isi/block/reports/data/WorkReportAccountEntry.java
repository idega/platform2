package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportAccountEntry extends com.idega.data.IDOEntity
{
 public double getAmount();
 public com.idega.user.data.Group getGroup();
 public int getGroupID();
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReport getWorkReport();
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKey getWorkReportAccountKey();
 public int getWorkReportAccountKeyID();
 public int getWorkReportID();
 public void initializeAttributes();
 public void setAmount(double p0);
 public void setGroup(com.idega.user.data.Group p0);
 public void setGroupID(int p0);
 public void setWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0);
 public void setWorkReportAccountKey(is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKey p0);
 public void setWorkReportAccountKeyID(int p0);
 public void setWorkReportID(int p0);
}
