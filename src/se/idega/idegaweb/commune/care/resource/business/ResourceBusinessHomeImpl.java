package se.idega.idegaweb.commune.care.resource.business;


public class ResourceBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ResourceBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ResourceBusiness.class;
 }


 public ResourceBusiness create() throws javax.ejb.CreateException{
  return (ResourceBusiness) super.createIBO();
 }



}