package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportAccountKeyHome extends com.idega.data.IDOHome
{
 public WorkReportAccountKey create() throws javax.ejb.CreateException;
 public WorkReportAccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public WorkReportAccountKey findAccountKeyByName(java.lang.String p0)throws javax.ejb.FinderException;
 public WorkReportAccountKey findAccountKeyByNumber(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAssetAccountKeys()throws javax.ejb.FinderException;
 public java.util.Collection findDeptAccountKeys()throws javax.ejb.FinderException;
 public java.util.Collection findExpensesAccountKeys()throws javax.ejb.FinderException;
 public java.util.Collection findIncomeAccountKeys()throws javax.ejb.FinderException;
 public java.lang.String getCreditTypeString();
 public java.lang.String getDebetTypeString();

}