package is.idega.idegaweb.member.isi.block.reports.business;


public class WorkReportStatsBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements WorkReportStatsBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return WorkReportStatsBusiness.class;
 }


 public WorkReportStatsBusiness create() throws javax.ejb.CreateException{
  return (WorkReportStatsBusiness) super.createIBO();
 }



}