package is.idega.idegaweb.member.isi.block.reports.business;


public class WorkReportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements WorkReportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return WorkReportBusiness.class;
 }


 public WorkReportBusiness create() throws javax.ejb.CreateException{
  return (WorkReportBusiness) super.createIBO();
 }



}