package se.idega.idegaweb.commune.business;


public class CommuneReportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CommuneReportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CommuneReportBusiness.class;
 }


 public CommuneReportBusiness create() throws javax.ejb.CreateException{
  return (CommuneReportBusiness) super.createIBO();
 }



}