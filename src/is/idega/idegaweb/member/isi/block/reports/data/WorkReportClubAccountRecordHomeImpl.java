package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportClubAccountRecordHomeImpl extends com.idega.data.IDOFactory implements WorkReportClubAccountRecordHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportClubAccountRecord.class;
 }


 public WorkReportClubAccountRecord create() throws javax.ejb.CreateException{
  return (WorkReportClubAccountRecord) super.createIDO();
 }


 public WorkReportClubAccountRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportClubAccountRecord) super.findByPrimaryKeyIDO(pk);
 }



}