package se.idega.idegaweb.commune.childcare.business;


public class ChildCareReportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ChildCareReportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ChildCareReportBusiness.class;
 }


 public ChildCareReportBusiness create() throws javax.ejb.CreateException{
  return (ChildCareReportBusiness) super.createIBO();
 }



}