package se.idega.idegaweb.commune.accounting.export.business;


public class ExportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ExportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ExportBusiness.class;
 }


 public ExportBusiness create() throws javax.ejb.CreateException{
  return (ExportBusiness) super.createIBO();
 }



}