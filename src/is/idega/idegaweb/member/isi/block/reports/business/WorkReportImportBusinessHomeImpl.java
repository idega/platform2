package is.idega.idegaweb.member.isi.block.reports.business;


public class WorkReportImportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements WorkReportImportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return WorkReportImportBusiness.class;
 }


 public WorkReportImportBusiness create() throws javax.ejb.CreateException{
  return (WorkReportImportBusiness) super.createIBO();
 }



}