package se.idega.idegaweb.commune.childcare.export.business;


public class ChildCareExportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ChildCareExportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ChildCareExportBusiness.class;
 }


 public ChildCareExportBusiness create() throws javax.ejb.CreateException{
  return (ChildCareExportBusiness) super.createIBO();
 }



}